package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandlers;
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
			return fuel.getUraniumStored();
		}
		
		@Override
		public void set(int value)
		{
			fuel.setUraniumStored(value);
		}
	};
	
	private final SimpleUraniumHandlers fuel = new SimpleUraniumHandlers.Insert(MAX_FUEL);
	public static final short MAX_FUEL = 128;
	
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
		compound.put("fuel", fuel.save());
	}
	
	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider pRegistries)
	{
		super.loadAdditional(nbt, pRegistries);
		
		if(nbt.contains("fuel", 99))
		{
			// Kept for backwards compatibility
			fuel.setUraniumStored(nbt.getShort("fuel"));
		} else
		{
			fuel.load(nbt.getCompound("fuel"));
		}
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
			itemHandler.extractItem(1, 1, false);
		}
		if(canIrradiate())
		{
			ItemStack output = irradiate();
			if(itemHandler.getStackInSlot(2).isEmpty() && this.fuel.getUraniumStored() > 0)
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
			this.fuel.extractUranium(1, false);
		}
	}
	
	private boolean canIrradiate()
	{
		ItemStack output = irradiate();
		if(fuel.getUraniumStored() > 0 && !itemHandler.getStackInSlot(0).isEmpty() && !output.isEmpty())
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
		return fuel;
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
		return fuel.receiveUranium(amount, true) == amount;
	}
	
	public void addFuel(ItemStack fuelStack)
	{
		int amount = UraniumPower.getUraniumPower(fuelStack);
		fuel.receiveUranium(amount, false);
	}
}
