package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.UraniumCookerMenu;
import com.mraof.minestuck.item.crafting.IrradiatingRecipe;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;
import com.mraof.minestuck.util.ExtraModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class UraniumCookerBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.uranium_cooker";
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE_OR_LOOPING, 0, this::setChanged, this::contentsValid);
	
	private final DataSlot fuelHolder = new DataSlot()
	{
		@Override
		public int get()
		{
			return fuel;
		}
		
		@Override
		public void set(int value)
		{
			fuel = value;
		}
	};
	
	private int fuel;
	private final IUraniumHandler uraniumHandler = new SimpleUraniumHandler(() -> MAX_FUEL, () -> this.fuel, fuel -> this.fuel = fuel)
	{
		public boolean canExtractUranium()
		{
			return false;
		}
	};
	
	public static final int MAX_FUEL = 128;
	
	public UraniumCookerBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.URANIUM_COOKER.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(3, (index, stack) -> index == 1 ? stack.is(ExtraModTags.Items.URANIUM_CHUNKS) : index != 2);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		
		compound.putInt("fuel", fuel);
	}
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		
		if (nbt.contains("fuel", 2))
			fuel = nbt.getShort("fuel");
		else
			fuel = nbt.getInt("fuel");
	}
	
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
	}
	
	private boolean contentsValid()
	{
		if(level.hasNeighborSignal(this.getBlockPos()))
		{
			return false;
		}
		
		ItemStack fuel = itemHandler.getStackInSlot(1);
		ItemStack input = itemHandler.getStackInSlot(0);
		ItemStack output = irradiate();
		return canBeRefueled(fuel) || !input.isEmpty() && !output.isEmpty();
	}
	
	private ItemStack irradiate()    //TODO Handle the recipe and make sure to use its exp/cooking time
	{
		if(level == null)
			return ItemStack.EMPTY;
		
		SingleRecipeInput recipeInventory = new SingleRecipeInput(itemHandler.getStackInSlot(0));
		
		//List of all recipes that match to the current input
		Stream<IrradiatingRecipe> stream = level.getRecipeManager().getRecipesFor(MSRecipeTypes.IRRADIATING_TYPE.get(), recipeInventory, level).stream().map(RecipeHolder::value);
		//Sort the stream to get non-fallback recipes first, and fallback recipes second
		stream = stream.sorted(Comparator.comparingInt(o -> (o.isFallback() ? 1 : 0)));
		//Let the recipe return the recipe actually used (for fallbacks), to clear out all that are not present, and then get the first
		Optional<? extends AbstractCookingRecipe> cookingRecipe = stream.flatMap(recipe -> recipe.getCookingRecipe(recipeInventory, level).stream()).findFirst();
		
		return cookingRecipe.map(abstractCookingRecipe -> abstractCookingRecipe.assemble(recipeInventory, level.registryAccess())).orElse(ItemStack.EMPTY);
	}
	
	private void processContents()
	{
		ItemStack fuel = itemHandler.getStackInSlot(1);
		if(canBeRefueled(fuel))
		{
			//Refill fuel
			addFuel(fuel);
			ItemStack taken = itemHandler.extractItem(1, 1, false);
			ItemStack remainder = taken.getCraftingRemainingItem();
			if(!remainder.isEmpty() && level != null)
			{
				ItemEntity remainderEntity = new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), remainder);
				level.addFreshEntity(remainderEntity);
			}
		}
		if(canIrradiate())
		{
			ItemStack output = irradiate();
			if(itemHandler.getStackInSlot(2).isEmpty() && this.fuel > 0)
			{
				itemHandler.setStackInSlot(2, output);
			} else
			{
				itemHandler.extractItem(2, -output.getCount(), false);
			}
			if(itemHandler.getStackInSlot(0).hasCraftingRemainingItem())
			{
				itemHandler.setStackInSlot(0, itemHandler.getStackInSlot(0).getCraftingRemainingItem());
			} else
			{
				itemHandler.extractItem(0, 1, false);
			}
			this.fuel--;
		}
	}
	
	private boolean canIrradiate()
	{
		ItemStack output = irradiate();
		if(fuel > 0 && !itemHandler.getStackInSlot(0).isEmpty() && !output.isEmpty())
		{
			ItemStack out = itemHandler.getStackInSlot(2);
			
			return out.isEmpty() || out.getMaxStackSize() >= output.getCount() + out.getCount() && ItemStack.isSameItem(out, output);
		}
		return false;
	}
	
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.DOWN)
			return new RangedWrapper(itemHandler, 2, 3);
		if(side == Direction.UP)
			return new RangedWrapper(itemHandler, 0, 1);
		return new RangedWrapper(itemHandler, 1, 2);
	}
	
	@Nullable
	public IUraniumHandler getUraniumHandler(@Nullable Direction side)
	{
		return uraniumHandler;
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new UraniumCookerMenu(windowId, playerInventory, itemHandler, this.progressTracker, fuelHolder, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	public boolean canBeRefueled(ItemStack fuelStack)
	{
		int amount = UraniumPower.getUraniumPower(fuelStack);
		return fuel + amount <= MAX_FUEL;
	}
	
	public void addFuel(ItemStack fuelStack)
	{
		int amount = UraniumPower.getUraniumPower(fuelStack);
		fuel += amount;
	}
}
