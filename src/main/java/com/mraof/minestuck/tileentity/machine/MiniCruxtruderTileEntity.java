package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.inventory.MiniCruxtruderContainer;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiniCruxtruderTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final String TITLE = "container.minestuck.mini_cruxtruder";
	public static final RunType TYPE = RunType.AUTOMATIC;
	public int color = ColorHandler.DEFAULT_COLOR;
	
	public MiniCruxtruderTileEntity()
	{
		super(MSTileEntityTypes.MINI_CRUXTRUDER.get());
	}
	
	@Override
	protected ItemStackHandler createItemHandler()
	{
		return new CustomHandler(2, (index, stack) -> index == 0 && stack.getItem() == MSItems.RAW_CRUXITE);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public boolean contentsValid()
	{
		ItemStack stack1 = itemHandler.getStackInSlot(1);
		return (!world.isBlockPowered(this.getPos()) && !itemHandler.getStackInSlot(0).isEmpty() && (stack1.isEmpty() || stack1.getCount() < stack1.getMaxStackSize() && ColorHandler.getColorFromStack(stack1) == this.color));
	}
	
	@Override
	public void processContents()
	{
		// Process the Raw Cruxite
		
		if (itemHandler.getStackInSlot(1).isEmpty())
			itemHandler.setStackInSlot(1, ColorHandler.setColor(new ItemStack(MSBlocks.CRUXITE_DOWEL), color));
		else itemHandler.extractItem(1, -1, false);
		itemHandler.extractItem(0, 1, false);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		this.color = nbt.getInt("color");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.putInt("color", color);
		return super.write(compound);
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(TITLE);
	}
	
	private final LazyOptional<IItemHandler> sideHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 0, 1));
	private final LazyOptional<IItemHandler> downHandler = LazyOptional.of(() -> new RangedWrapper(itemHandler, 1, 2));
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null)
		{
			return side == Direction.DOWN ? downHandler.cast() : sideHandler.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new MiniCruxtruderContainer(windowId, playerInventory, itemHandler, parameters, IWorldPosCallable.of(world, pos), pos);
	}
}