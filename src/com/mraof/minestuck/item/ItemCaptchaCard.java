package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class ItemCaptchaCard extends Item
{
	
	public ItemCaptchaCard()
	{
		this.maxStackSize = 16;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("captchaCard");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		itemList.add(new ItemStack(this));
		itemList.add(AlchemyRecipeHandler.createCard(new ItemStack(MinestuckItems.cruxiteApple), true));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			@SuppressWarnings("rawtypes") List par3List, boolean par4) {
		if (par1ItemStack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			
			if (contentID != null && contentMeta != null && Item.itemRegistry.containsKey(new ResourceLocation(contentID.getString())))
			{
				String stackSize = nbttagcompound.getBoolean("punched") ? "" : nbttagcompound.getInteger("contentSize") + "x";
				par3List.add("(" + stackSize + (AlchemyRecipeHandler.getDecodedItem(par1ItemStack)).getDisplayName() + ")");
				if(nbttagcompound.getBoolean("punched"))
					par3List.add("("+I18n.translateToLocal("item.captchaCard.punched")+")");
				return;
			}
			else {
				par3List.add("("+I18n.translateToLocal("item.captchaCard.invalid")+")");
			}
		} else {
			par3List.add("("+I18n.translateToLocal("item.captchaCard.empty")+")");
		}
		
	}
	
}
