package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.MiniCruxtruderContainer;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MiniCruxtruderTileEntity extends MachineProcessTileEntity implements INamedContainerProvider
{
	public static final RunType TYPE = RunType.AUTOMATIC;
	public int color = -1;
	
	public MiniCruxtruderTileEntity()
	{
		super(ModTileEntityTypes.MINI_CRUXTRUDER);
	}
	
	@Override
	public RunType getRunType()
	{
		return TYPE;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 2;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 && stack.getItem() == MinestuckItems.RAW_CRUXITE;
	}
	
	@Override
	public boolean contentsValid()
	{
		ItemStack stack1 = this.inv.get(1);
		return (!world.isBlockPowered(this.getPos()) && !this.inv.get(0).isEmpty() && (stack1.isEmpty() || stack1.getCount() < stack1.getMaxStackSize() && ColorCollector.getColorFromStack(stack1, -1) == this.color + 1));
	}
	
	@Override
	public void processContents()
	{
		// Process the Raw Cruxite
		
		if (this.inv.get(1).isEmpty())
			setInventorySlotContents(1, ColorCollector.setColor(new ItemStack(MinestuckBlocks.CRUXITE_DOWEL), color + 1));	//TODO Sort out color storage
		else this.inv.get(1).grow(1);
		decrStackSize(0, 1);
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		this.color = compound.getInt("color");
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
		return new TranslationTextComponent("container.mini_cruxtruder");
	}
	
	@Override
	public int[] getSlotsForFace(Direction side)
	{
		if(side == Direction.DOWN)
			return new int[] {1};
		else return new int[] {0};
	}
	
	@Nullable
	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player)
	{
		return new MiniCruxtruderContainer(windowId, playerInventory, this, parameters);
	}
}