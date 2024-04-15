package com.mraof.minestuck.blockentity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public abstract class MachineProcessBlockEntity extends BlockEntity
{
	protected final ItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> itemHandler);
	
	public static final int FUEL_INCREASE = 32; //how many units of fuel a chunk of uranium adds to a machine powered by it, used by Sendificator and UraniumCooker
	
	protected MachineProcessBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
	{
		super(blockEntityType, pos, state);
	}
	
	protected abstract ItemStackHandler createItemHandler();
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		compound.put("inventory", itemHandler.serializeNBT());
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == Capabilities.ITEM_HANDLER)
			return itemOptional.cast();
		return super.getCapability(cap, side);
	}
	
	public static void serverTick(Level ignoredLevel, BlockPos ignoredPos, BlockState ignoredState, MachineProcessBlockEntity blockEntity)
	{
		blockEntity.tick();
	}
	
	protected abstract void tick();
	
	protected class CustomHandler extends ItemStackHandler
	{
		private final BiPredicate<Integer, ItemStack> isValidPredicate;
		
		protected CustomHandler(int size)
		{
			this(size, (slot, stack) -> true);
		}
		
		protected CustomHandler(int size, BiPredicate<Integer, ItemStack> isValidPredicate)
		{
			super(size);
			this.isValidPredicate = isValidPredicate;
		}
		
		@Override
		public boolean isItemValid(int slot, @Nonnull ItemStack stack)
		{
			return isValidPredicate.test(slot, stack);
		}
		
		@Override
		protected void onContentsChanged(int slot)
		{
			MachineProcessBlockEntity.this.setChanged();
		}
	}
}