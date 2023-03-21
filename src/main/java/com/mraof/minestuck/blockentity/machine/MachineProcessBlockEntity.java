package com.mraof.minestuck.blockentity.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;

public abstract class MachineProcessBlockEntity extends BlockEntity
{
	protected final ItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> itemOptional = LazyOptional.of(() -> itemHandler);
	public static final int DEFAULT_MAX_PROGRESS = 100;
	protected final ProgressTracker progressTracker = new ProgressTracker(this.getRunType(), this.getMaxProgress());
	
	public static final int FUEL_INCREASE = 32; //how many units of fuel a chunk of uranium adds to a machine powered by it, used by Sendificator and UraniumCooker
	
	protected MachineProcessBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
	{
		super(blockEntityType, pos, state);
	}
	
	protected abstract ItemStackHandler createItemHandler();
	
	public abstract ProgressTracker.RunType getRunType();
	
	protected int getMaxProgress()
	{
		return DEFAULT_MAX_PROGRESS;
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		this.progressTracker.load(nbt);
		itemHandler.deserializeNBT(nbt.getCompound("inventory"));
	}
	
	@Override
	protected void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
		this.progressTracker.save(compound);
		compound.put("inventory", itemHandler.serializeNBT());
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ITEM_HANDLER)
			return itemOptional.cast();
		return super.getCapability(cap, side);
	}
	
	public static void serverTick(Level ignoredLevel, BlockPos ignoredPos, BlockState ignoredState, MachineProcessBlockEntity blockEntity)
	{
		blockEntity.tick();
	}
	
	protected void tick()
	{
		this.progressTracker.tick(this::contentsValid, this::processContents);
	}
	
	public abstract boolean contentsValid();

	public abstract void processContents();
	
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