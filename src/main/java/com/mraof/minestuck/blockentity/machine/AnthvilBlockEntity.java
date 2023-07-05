package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.*;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.util.ExtraForgeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * Mends an item at the cost of uranium power and grist. The grist used is the one with the highest value impact (weighted against build grist)
 */
public class AnthvilBlockEntity extends MachineProcessBlockEntity implements MenuProvider, UraniumPowered
{
	public static final String TITLE = "container.minestuck.anthvil";
	public static final short MAX_FUEL = 128;
	public static final short MEND_FUEL_COST = 5;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.ONCE, 0, this::setChanged, this::contentsValid);
	private short fuel = 0;
	
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
			fuel = (short) value;
		}
	};
	
	
	public AnthvilBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.ANTHVIL.get(), pos, state);
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		this.progressTracker.load(compound);
		
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		this.progressTracker.save(compound);
		
		compound.putShort("fuel", fuel);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new MachineProcessBlockEntity.CustomHandler(2, (index, stack) -> index == 0 || stack.is(ExtraForgeTags.Items.URANIUM_CHUNKS));
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
		
		boolean goodOnFuel = canBeRefueled() && fuel.is(ExtraForgeTags.Items.URANIUM_CHUNKS);
		return goodOnFuel || !input.isEmpty();
	}
	
	/**
	 * Checks for a uranium itemstack in the right item slot, increases the fuel value if some is found and then removes one count from the fuel stack.
	 * Only handles fuel refilling
	 */
	private void processContents()
	{
		if(canBeRefueled() && itemHandler.getStackInSlot(1).is(ExtraForgeTags.Items.URANIUM_CHUNKS))
		{
			addFuel((short) FUEL_INCREASE);
			itemHandler.extractItem(1, 1, false);
		}
	}
	
	public static void attemptMend(AnthvilBlockEntity anthvil, ServerPlayer player)
	{
		Level level = anthvil.level;
		ItemStack slotStack = anthvil.itemHandler.getStackInSlot(0);
		GristCache playerCache = GristCache.get(player);
		
		if(level == null || !isMendableItem(slotStack) || !hasEnoughFuel(anthvil))
			return;
		
		GristSet pickedGrist = mendingGrist(playerCache, level, slotStack);
		
		if(!pickedGrist.isEmpty())
		{
			if(slotStack.hasCraftingRemainingItem())
			{
				anthvil.itemHandler.setStackInSlot(0, slotStack.getCraftingRemainingItem());
			} else
			{
				//amount of repair changes with the value of the grist, 10 at base
				int repairAmount = (int) (10 * pickedGrist.asAmounts().get(0).getValue());
				slotStack.setDamageValue(slotStack.getDamageValue() - repairAmount);
				
				if(!player.isCreative() && playerCache.tryTake(pickedGrist, GristHelper.EnumSource.CLIENT))
				{
					anthvil.fuel -= MEND_FUEL_COST;
				}
			}
		}
	}
	
	private static boolean isMendableItem(ItemStack slotStack)
	{
		return !slotStack.isEmpty() && slotStack.isRepairable() && slotStack.isDamageableItem() && slotStack.isDamaged();
	}
	
	private static boolean hasEnoughFuel(AnthvilBlockEntity anthvil)
	{
		return anthvil.fuel >= MEND_FUEL_COST;
	}
	
	/**
	 * Checks a mend-able item's grist type and returns the grist if its valid and the player can afford to spend the amount
	 */
	private static ImmutableGristSet mendingGrist(GristCache playerCache, Level level, ItemStack slotStack)
	{
		GristSet fullSet = GristCostRecipe.findCostForItem(slotStack, null, false, level);
		
		if(fullSet != null && !fullSet.isEmpty())
		{
			GristAmount pickedGrist = getUsedGrist(fullSet);
			
			if(playerCache.canAfford(pickedGrist))
				return pickedGrist;
		}
		
		return GristSet.EMPTY;
	}
	
	/**
	 * Takes the GristSet of the item stored in the mending slot of the anthvil, finds the most used grist type, then returns a GristSet composed of one value of said grist type.
	 */
	public static GristAmount getUsedGrist(GristSet fullSet)
	{
		List<GristAmount> gristAmounts = fullSet.asAmounts();
		GristAmount defaultGrist = new GristAmount(GristTypes.BUILD.get(), 1);
		
		if(gristAmounts.size() > 1) //if theres more than one, establishes which GristAmount has the highest impact and uses that
		{
			GristAmount pickedGrist = fullSet.asAmounts().stream().max(Comparator.comparingDouble(AnthvilBlockEntity::getModifiedGristValue)).orElse(defaultGrist);
			return new GristAmount(pickedGrist.type(), 1);
		} else if(gristAmounts.get(0).type() != GristTypes.BUILD.get())
			return new GristAmount(gristAmounts.get(0).type(), 1);
		else
			return defaultGrist;
	}
	
	private static double getModifiedGristValue(GristAmount grist)
	{
		double gristValue = Math.abs(grist.getValue()); //negative grist values still count
		if(grist.type() == GristTypes.BUILD.get())
			gristValue /= 10; //weighted against build grist to promote higher type diversity
		
		return gristValue;
	}
	
	/**
	 * Checks that fuel can be added without any excess/wasted points being attributed
	 */
	public boolean canBeRefueled()
	{
		return fuel <= MAX_FUEL - FUEL_INCREASE;
	}
	
	@Override
	public void addFuel(short fuelAmount)
	{
		fuel += fuelAmount;
	}
	
	@Override
	public boolean atMaxFuel()
	{
		return fuel >= MAX_FUEL;
	}
	
	private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1)); //regenerating item slot
	private final LazyOptional<IItemHandler> fuelHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2)); //uranium fuel slot
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ITEM_HANDLER && side != null)
		{
			if(side == Direction.UP)
				return inputHandler.cast();
			else if(side == Direction.DOWN)
				return LazyOptional.empty();
			else
				return fuelHandler.cast(); //will fill the anthvil with fuel if fed from the sides
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new AnthvilMenu(windowId, playerInventory, itemHandler,
				this.progressTracker, fuelHolder, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}