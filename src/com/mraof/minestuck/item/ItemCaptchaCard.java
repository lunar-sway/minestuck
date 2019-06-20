package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCaptchaCard extends Item
{
	public static final IItemPropertyGetter CONTENT = (stack, world, holder) -> AlchemyRecipes.hasDecodedItem(stack) ? 1 : 0;
	public static final ResourceLocation CONTENT_NAME = new ResourceLocation(Minestuck.MOD_ID, "content");
	
	public ItemCaptchaCard(Properties properties)
	{
		super(properties);
		this.addPropertyOverride(CONTENT_NAME, CONTENT);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "punched"), (stack, world, holder) -> AlchemyRecipes.isPunchedCard(stack) ? 1 : 0);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "ghost"), (stack, world, holder) -> AlchemyRecipes.isGhostCard(stack) ? 1 : 0);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.isInGroup(group))
		{
			items.add(new ItemStack(this));
			items.add(AlchemyRecipes.createCard(new ItemStack(MinestuckItems.CRUXITE_APPLE), true));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		
		NBTTagCompound nbt = playerIn.getHeldItem(handIn).getTag();
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(playerIn.isSneaking() && stack.hasTag() && ((nbt.getInt("contentSize") <= 0 && !nbt.getBoolean("punched") && AlchemyRecipes.getDecodedItem(stack) != ItemStack.EMPTY) || !nbt.hasKey("contentID")))
		{
			return new ActionResult<>(EnumActionResult.SUCCESS, new ItemStack(playerIn.getHeldItem(handIn).getItem(), playerIn.getHeldItem(handIn).getCount()));
		}
		else
			return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyRecipes.hasDecodedItem(stack))
		{
			NBTTagCompound nbt = stack.getTag();
			ItemStack content = AlchemyRecipes.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				String stackSize = (nbt.getBoolean("punched") || nbt.getInt("contentSize") <= 0) ? "" : nbt.getInt("contentSize") + "x";
				tooltip.add(new TextComponentString("(").appendText(stackSize).appendSibling(content.getDisplayName()).appendText(")"));
				if(nbt.getBoolean("punched"))
					tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.captchaCard.punched")).appendText(")"));
				else if(nbt.getInt("contentSize") <= 0)
					tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.captchaCard.ghost")).appendText(")"));
			} else
			{
				tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.captchaCard.invalid")).appendText(")"));
			}
		} else
			tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.captchaCard.empty")).appendText(")"));
	}
	
}
