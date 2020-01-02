package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class BoondollarsItem extends Item	//TODO Add custom crafting recipe that merges boondollar stacks
{
	public BoondollarsItem(Properties properties)
	{
		super(properties);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "count"), (stack, world, holder) -> getCount(stack));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		if(!worldIn.isRemote)
		{
			PlayerSavedData.getData((ServerPlayerEntity) playerIn).addBoondollars(getCount(playerIn.getHeldItem(handIn)));
		}
		return new ActionResult<>(ActionResultType.SUCCESS, ItemStack.EMPTY);
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
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		long amount = getCount(stack);
		tooltip.add(new TranslationTextComponent("item.minestuck.boondollars.amount", amount));
	}
	
	public static long getCount(ItemStack stack)
	{
		if(!stack.hasTag() || !stack.getTag().contains("value", Constants.NBT.TAG_ANY_NUMERIC))
			return 1;
		else return stack.getTag().getInt("value");
	}
	
	public static ItemStack setCount(ItemStack stack, int value)
	{
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundNBT();
			stack.setTag(nbt);
		}
		nbt.putInt("value", value);
		return stack;
	}
}