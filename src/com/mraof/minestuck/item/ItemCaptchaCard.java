package com.mraof.minestuck.item;

import com.mraof.minestuck.util.AlchemyRecipeHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCaptchaCard extends Item
{
	
	public ItemCaptchaCard()
	{
		this.setCreativeTab(TabMinestuck.instance);
		//this.setHasSubtypes(true);
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			items.add(new ItemStack(this));
			items.add(AlchemyRecipeHandler.createCard(new ItemStack(MinestuckItems.cruxiteApple), true));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
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
