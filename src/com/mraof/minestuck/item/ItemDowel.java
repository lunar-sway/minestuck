package com.mraof.minestuck.item;

import java.util.List;

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

public class ItemDowel extends Item
{
	
	public ItemDowel()
	{
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("dowelCruxite");
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTagCompound())
			return 16;
		else return 64;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		if (stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			
			if (contentID != null && contentMeta != null && Item.REGISTRY.containsKey(new ResourceLocation(contentID.getString())))
			{
				tooltip.add("(" + (AlchemyRecipeHandler.getDecodedItem(stack)).getDisplayName() + ")");
				return;
			}
			else
			{
				tooltip.add("("+I18n.translateToLocal("item.captchaCard.invalid")+")");
			}
		}
	}
	
}