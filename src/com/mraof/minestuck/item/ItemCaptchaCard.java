package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class ItemCaptchaCard extends Item
{
	
	public ItemCaptchaCard()
	{
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("captchaCard");
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTagCompound())
			return 16;
		else return 64;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this));
		subItems.add(AlchemyRecipeHandler.createCard(new ItemStack(MinestuckItems.cruxiteApple), true));
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			
			if (contentID != null && contentMeta != null && Item.REGISTRY.containsKey(new ResourceLocation(contentID.getString())))
			{
				String stackSize = nbttagcompound.getBoolean("punched") ? "" : nbttagcompound.getInteger("contentSize") + "x";
				tooltip.add("(" + stackSize + (AlchemyRecipeHandler.getDecodedItem(stack)).getDisplayName() + ")");
				if(nbttagcompound.getBoolean("punched"))
					tooltip.add("("+I18n.translateToLocal("item.captchaCard.punched")+")");
				return;
			}
			else {
				tooltip.add("("+I18n.translateToLocal("item.captchaCard.invalid")+")");
			}
		} else {
			tooltip.add("("+I18n.translateToLocal("item.captchaCard.empty")+")");
		}
		
	}
	
}
