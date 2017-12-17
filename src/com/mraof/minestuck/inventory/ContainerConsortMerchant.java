package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerConsortMerchant extends Container
{
	private final InventoryConsortMerchant inventory;
	
	private EntityPlayer player;
	private EntityConsort consort;
	public EnumConsort consortType;
	public EnumConsort.MerchantType merchantType;
	
	public ContainerConsortMerchant(EntityPlayer player)
	{
		inventory = new InventoryConsortMerchant();
		this.player = player;
		
		this.addSlotToContainer(new SlotConsortMerchant(inventory, 0, 17, 35));
	}
	
	public void setConsort(EntityConsort consort)
	{
		this.consort = consort;
		consortType = consort.getConsortType();
		merchantType = consort.merchantType;
		
		for(IContainerListener listener : listeners)
		{
			listener.sendWindowProperty(this, 0, consortType.ordinal());
			listener.sendWindowProperty(this, 1, merchantType.ordinal());
		}
		
		inventory.setInventorySlotContents(0, new ItemStack(MinestuckItems.grasshopper));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.player == playerIn;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		if(id == 0)
			consortType = EnumConsort.values()[data % EnumConsort.values().length];
		else if(id == 1)
			merchantType = EnumConsort.MerchantType.values()[data % EnumConsort.MerchantType.values().length];
	}
}
