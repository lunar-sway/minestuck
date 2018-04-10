package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBoondollars extends Item
{
	public ItemBoondollars()	//TODO Add custom crafting recipe that merges boondollar stacks
	{
		setUnlocalizedName("boondollars");
		setCreativeTab(TabMinestuck.instance);
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(!worldIn.isRemote)
		{
			MinestuckPlayerData.addBoondollars(playerIn, getCount(playerIn.getHeldItem(handIn)));
		}
		return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if(isInCreativeTab(tab))
		{
			items.add(new ItemStack(this));
			items.add(setCount(new ItemStack(this), 10));
			items.add(setCount(new ItemStack(this), 100));
			items.add(setCount(new ItemStack(this), 1000));
			items.add(setCount(new ItemStack(this), 10000));
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		int amount = getCount(stack);
		tooltip.add(I18n.translateToLocalFormatted("item.boondollars.amount", amount));
	}
	
	public static int getCount(ItemStack stack)
	{
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("value", 99))
			return 1;
		else return stack.getTagCompound().getInteger("value");
	}
	
	public static ItemStack setCount(ItemStack stack, int value)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setInteger("value", value);
		return stack;
	}
}
