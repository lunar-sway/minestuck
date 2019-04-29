package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MinestuckPlayerData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBoondollars extends Item	//TODO Add custom crafting recipe that merges boondollar stacks
{
	public ItemBoondollars(Properties properties)
	{
		super(properties);
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
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(isInGroup(group))
		{
			items.add(new ItemStack(this));
			items.add(setCount(new ItemStack(this), 10));
			items.add(setCount(new ItemStack(this), 100));
			items.add(setCount(new ItemStack(this), 1000));
			items.add(setCount(new ItemStack(this), 10000));
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		int amount = getCount(stack);
		tooltip.add(new TextComponentTranslation("item.boondollars.amount", amount));
	}
	
	public static int getCount(ItemStack stack)
	{
		if(!stack.hasTag() || !stack.getTag().contains("value", 99))
			return 1;
		else return stack.getTag().getInt("value");
	}
	
	public static ItemStack setCount(ItemStack stack, int value)
	{
		NBTTagCompound nbt = stack.getTag();
		if(nbt == null)
		{
			nbt = new NBTTagCompound();
			stack.setTag(nbt);
		}
		nbt.setInt("value", value);
		return stack;
	}
}
