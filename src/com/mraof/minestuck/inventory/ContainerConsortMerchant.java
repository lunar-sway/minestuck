package com.mraof.minestuck.inventory;

import com.mraof.minestuck.entity.consort.EntityConsort;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.Pair;
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
	public int[] prices = new int[9];
	
	public ContainerConsortMerchant(EntityPlayer player)
	{
		inventory = new InventoryConsortMerchant();
		this.player = player;
		
		for(int i = 0; i < 9; i++)
			this.addSlotToContainer(new SlotConsortMerchant(inventory, i, 17 + 35*(i%3), 35 + 33*(i/3)));
	}
	
	public void setConsort(EntityConsort consort)
	{
		this.consort = consort;
		consortType = consort.getConsortType();
		merchantType = consort.merchantType;
		
		for(int i = 0; i < consort.stock.size(); i++)
		{
			Pair<ItemStack, Integer> item = consort.stock.get(i);
			inventory.setInventorySlotContents(i, item.object1);
			prices[i] = item.object2;
			for(IContainerListener listener : listeners)
			{
				listener.sendWindowProperty(this, i, item.object2);	//Kinda inefficient when it comes to packages
			}
		}
		
		for(IContainerListener listener : listeners)
		{
			listener.sendWindowProperty(this, -1, consortType.ordinal());
			listener.sendWindowProperty(this, -2, merchantType.ordinal());
		}
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
		if(id == -1)
			consortType = EnumConsort.values()[data % EnumConsort.values().length];
		else if(id == -2)
			merchantType = EnumConsort.MerchantType.values()[data % EnumConsort.MerchantType.values().length];
		else if(id >= 0 && id < 9)
			prices[id] = data;
	}
}
