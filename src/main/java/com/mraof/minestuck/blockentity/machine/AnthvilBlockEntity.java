package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.*;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.AnthvilMenu;
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
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Comparator;

/**
 * Mends an item at the cost of uranium power and grist. The grist used is the one with the highest value impact (weighted against build grist)
 */
public class AnthvilBlockEntity extends MachineProcessBlockEntity implements MenuProvider, UraniumPowered
{
	public static final String TITLE = "container.minestuck.anthvil";
	public static final short MAX_FUEL = 128;
	public static final short MEND_FUEL_COST = 5;
	
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
		
		fuel = compound.getShort("fuel");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
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
	}
	
	public static void attemptMendAndRefuel(AnthvilBlockEntity anthvil, ServerPlayer player)
	{
		Level level = anthvil.level;
		ItemStackHandler itemHandler = anthvil.itemHandler;
		ItemStack slotStack = itemHandler.getStackInSlot(0);
		GristCache playerCache = GristCache.get(player);
		
		if(level == null || !isMendableItem(slotStack))
			return;
		
		if(anthvil.canBeRefueled() && itemHandler.getStackInSlot(1).is(ExtraForgeTags.Items.URANIUM_CHUNKS))
		{
			anthvil.addFuel((short) FUEL_INCREASE);
			itemHandler.extractItem(1, 1, false);
		}
		
		GristSet pickedGrist = mendingGrist(level, slotStack);
		
		if(pickedGrist.isEmpty() || !hasEnoughFuel(anthvil))
			return;
		
		if(player.isCreative() || playerCache.tryTake(pickedGrist, GristHelper.EnumSource.CLIENT))
		{
			//amount of repair changes with the value of the grist, 10 at base
			int repairAmount = (int) (10 * pickedGrist.asAmounts().get(0).getValue());
			slotStack.setDamageValue(slotStack.getDamageValue() - repairAmount);
			
			if(!player.isCreative())
				anthvil.fuel -= MEND_FUEL_COST;
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
	 * Checks a mend-able item's grist type and returns the grist if its valid
	 */
	private static ImmutableGristSet mendingGrist(Level level, ItemStack slotStack)
	{
		GristSet fullSet = GristCostRecipe.findCostForItem(slotStack, null, false, level);
		
		if(fullSet != null && !fullSet.isEmpty())
			return getUsedGrist(fullSet);
		
		return GristSet.EMPTY;
	}
	
	/**
	 * Takes the GristSet of the item stored in the mending slot of the anthvil, finds the most used grist type, then returns a GristSet composed of one value of said grist type.
	 */
	public static GristAmount getUsedGrist(GristSet fullSet)
	{
		GristType pickedGrist = fullSet.asAmounts().stream().max(Comparator.comparingDouble(AnthvilBlockEntity::getModifiedGristValue)).map(GristAmount::type).orElse(GristTypes.BUILD.get());
		return pickedGrist.amount(1);
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
	
	@Nullable
	public IItemHandler getItemHandler(@Nullable Direction side)
	{
		if(side == null)
			return this.itemHandler;
		
		if(side == Direction.UP)
			return new RangedWrapper(itemHandler, 0, 1);
		if(side == Direction.DOWN)
			return null;
		
		return new RangedWrapper(itemHandler, 1, 2);
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new AnthvilMenu(windowId, playerInventory, itemHandler, fuelHolder, ContainerLevelAccess.create(level, worldPosition));
	}
}