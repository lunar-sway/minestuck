package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.inventory.ContainerMiniCruxtruder;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;

public class TileEntityMiniCruxtruder extends TileEntityMachineProcess implements IInteractionObject
{
	public int color = -1;
	
	public TileEntityMiniCruxtruder()
	{
		super(MinestuckTiles.MINI_CRUXTRUDER);
	}
	
	@Override
	public RunType getRunType()
	{
		return RunType.AUTOMATIC;
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
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		this.color = compound.getInt("color");
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		compound.putInt("color", color);
		return super.write(compound);
	}
	
	@Override
	public ITextComponent getName()
	{
		return new TextComponentTranslation("container.mini_cruxtruder");
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if(side == EnumFacing.DOWN)
			return new int[] {1};
		else return new int[] {0};
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerMiniCruxtruder(playerInventory, this);
	}
	
	@Override
	public String getGuiID()
	{
		return GuiHandler.MINI_CRUXTRUDER_ID.toString();
	}
}
