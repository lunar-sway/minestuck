package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.inventory.MiniCruxtruderMenu;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiniCruxtruderBlockEntity extends MachineProcessBlockEntity implements MenuProvider
{
	public static final String TITLE = "container.minestuck.mini_cruxtruder";
	public static final int MAX_PROGRESS = 100;
	
	private final ProgressTracker progressTracker = new ProgressTracker(ProgressTracker.RunType.AUTOMATIC, MAX_PROGRESS, this::setChanged, this::contentsValid);
	public int color = ColorHandler.DEFAULT_COLOR;
	
	public MiniCruxtruderBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.MINI_CRUXTRUDER.get(), pos, state);
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(2, (index, stack) -> index == 0 && stack.getItem() == MSItems.RAW_CRUXITE.get());
	}
	
	@Override
	protected void tick()
	{
		this.progressTracker.tick(this::processContents);
	}
	
	private boolean contentsValid()
	{
		ItemStack stack1 = itemHandler.getStackInSlot(1);
		return (!level.hasNeighborSignal(this.getBlockPos()) && !itemHandler.getStackInSlot(0).isEmpty() && (stack1.isEmpty() || stack1.getCount() < stack1.getMaxStackSize() && ColorHandler.getColorFromStack(stack1) == this.color));
	}
	
	private void processContents()
	{
		// Process the Raw Cruxite
		
		if (itemHandler.getStackInSlot(1).isEmpty())
			itemHandler.setStackInSlot(1, ColorHandler.setColor(new ItemStack(MSBlocks.CRUXITE_DOWEL.get()), color));
		else itemHandler.extractItem(1, -1, false);
		itemHandler.extractItem(0, 1, false);
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		this.progressTracker.load(nbt);
		this.color = nbt.getInt("color");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		this.progressTracker.save(compound);
		compound.putInt("color", color);
		super.saveAdditional(compound);
	}
	
	@Override
	public Component getDisplayName()
	{
		return Component.translatable(TITLE);
	}
	
	private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2));
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == ForgeCapabilities.ITEM_HANDLER && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() : sideHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player)
	{
		return new MiniCruxtruderMenu(windowId, playerInventory, itemHandler, this.progressTracker, ContainerLevelAccess.create(level, worldPosition), worldPosition);
	}
}