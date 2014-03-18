package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.Debug;

public class ItemCardPunched extends Item {

	public ItemCardPunched() {
		this.maxStackSize = 1;
//		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("cardPunched");
	}

	public void registerIcons(IIconRegister par1IconRegister) {
	   itemIcon = par1IconRegister.registerIcon("minestuck:CardPunched");
	}
    
	   @Override
	    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
	        if (par1ItemStack.hasTagCompound())
	        {
		        NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
		        NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
		        NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
		
		        if (contentID != null && contentMeta != null && Item.itemRegistry.containsKey(contentID.func_150285_a_())) {
		        	//par3List.add("(" + contentID.data + ":" + contentMeta.data + ")");
		        	par3List.add("(" + (new ItemStack((Item)Item.itemRegistry.getObject(contentID.func_150285_a_()), 1, contentMeta.func_150287_d())).getDisplayName() + ")");
		        	return;
		        }
		        else {
		        	 par3List.add("("+StatCollector.translateToLocal("item.cardPunched.invalid")+")");
		        }
	        } else {
	        	par3List.add("("+StatCollector.translateToLocal("item.cardPunched.empty")+")");
	        }
      
	   	}
       
    }
