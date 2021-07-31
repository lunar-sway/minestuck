package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class KeyItem extends Item
{
	public EnumKeyType keyID;
	//public Dimension dimension = null;
	
	public KeyItem(Properties builder/*, EnumKeyType keyName*/)
	{
		super(builder);
		//this.keyID = keyName;
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "key"), (stack, world, holder) -> getKeyType(stack));
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(isInGroup(group))
		{
			items.add(new ItemStack(this));
			items.add(setKeyType(new ItemStack(this), 1));
			items.add(setKeyType(new ItemStack(this), 2));
		}
	}
	
	public static int getKeyType(ItemStack stack) //converted to int for use in constructor
	{
		if(!stack.hasTag() || !stack.getTag().contains("key", Constants.NBT.TAG_ANY_NUMERIC))
		{
			return EnumKeyType.none.ordinal();
		} else
		{
			return stack.getTag().getInt("key");
		}
	}
	
	public static ItemStack setKeyType(ItemStack stack, int keyOrdinal) //takes in EnumKeyType ordinal
	{
		CompoundNBT nbt = stack.getTag();
		if(nbt == null)
		{
			nbt = new CompoundNBT();
			stack.setTag(nbt);
		}
		//this.keyID = EnumKeyType.fromInt(keyOrdinal);
		nbt.putInt("key", keyOrdinal);
		return stack;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		EnumKeyType keyTypeFromInt = EnumKeyType.fromInt(getKeyType(stack));
		tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".key", keyTypeFromInt));
	}
}
