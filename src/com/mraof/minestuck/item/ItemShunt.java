package com.mraof.minestuck.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemShunt extends Item {
	public ItemShunt()
	{
		this.setCreativeTab(TabMinestuck.instance);
		//this.setHasSubtypes(true);
		this.setUnlocalizedName("shunt");
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(this.isInCreativeTab(tab))
		{
			items.add(new ItemStack(this));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(stack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta;
			try
			{
				contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			}
			catch(Throwable e)
			{
				contentMeta = null;
			}
			
			if (contentID != null && contentMeta != null && Item.REGISTRY.containsKey(new ResourceLocation(contentID.getString())))
			{
				tooltip.add("(" + (AlchemyRecipes.getDecodedItem(stack)).getDisplayName() + ")");
				return;
			}
			else {
				tooltip.add("("+I18n.translateToLocal("item.shunt.invalid")+")");
			}
		} else {
			tooltip.add("("+I18n.translateToLocal("item.shunt.empty")+")");
		}
		
	}
}
