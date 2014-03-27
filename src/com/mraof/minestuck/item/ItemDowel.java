package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class ItemDowel extends Item {
	
	public IIcon uncarved;
	public IIcon carved;
	
	public ItemDowel() {
		this.maxStackSize = 16;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("dowelCruxite");
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
	   carved = par1IconRegister.registerIcon("minestuck:CruxiteCarved");
	   uncarved = par1IconRegister.registerIcon("minestuck:CruxiteDowel");
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("contentID"))
			return this.carved;
		else return this.uncarved;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		   if (par1ItemStack.hasTagCompound()) {
				NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
				NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
				NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
				
				if (contentID != null && contentMeta != null && Item.itemRegistry.containsKey(contentID.func_150285_a_())) {
					
					par3List.add("(" + (AlchemyRecipeHandler.getDecodedItem(par1ItemStack)).getDisplayName() + ")");
					
					return;
				}
				else {
					par3List.add("("+StatCollector.translateToLocal("item.captchaCard.invalid")+")");
				}
			}
     
	   	}
}
