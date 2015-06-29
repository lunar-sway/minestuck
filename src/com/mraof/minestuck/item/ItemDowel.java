package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.ColorCollector;

public class ItemDowel extends Item
{
	
	public ItemDowel()
	{
		this.maxStackSize = 16;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setUnlocalizedName("dowelCruxite");
		this.setHasSubtypes(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if (par1ItemStack.hasTagCompound())
		{
			NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			
			if (contentID != null && contentMeta != null && Item.itemRegistry.containsKey(new ResourceLocation(contentID.getString())))
			{
				par3List.add("(" + (AlchemyRecipeHandler.getDecodedItem(par1ItemStack)).getDisplayName() + ")");
				return;
			}
			else
			{
				par3List.add("("+StatCollector.translateToLocal("item.captchaCard.invalid")+")");
			}
		}
	}
	
	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		if(stack.getMetadata() == 0)
			return -1;
		else return ColorCollector.getColor(stack.getMetadata() - 1);
	}
}
