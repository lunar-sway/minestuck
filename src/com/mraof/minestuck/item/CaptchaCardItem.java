package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.AlchemyRecipes;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CaptchaCardItem extends Item
{
	public static final IItemPropertyGetter CONTENT = (stack, world, holder) -> AlchemyRecipes.hasDecodedItem(stack) ? 1 : 0;
	public static final ResourceLocation CONTENT_NAME = new ResourceLocation(Minestuck.MOD_ID, "content");
	
	public CaptchaCardItem(Properties properties)
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
			items.add(AlchemyRecipes.createCard(new ItemStack(MSItems.CRUXITE_APPLE), true));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		
		CompoundNBT nbt = playerIn.getHeldItem(handIn).getTag();
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(playerIn.isSneaking() && stack.hasTag() && ((AlchemyRecipes.isGhostCard(stack) && !AlchemyRecipes.isPunchedCard(stack)) || !AlchemyRecipes.hasDecodedItem(stack)))
		{	//TODO should only remove content tags
			return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(playerIn.getHeldItem(handIn).getItem(), playerIn.getHeldItem(handIn).getCount()));
		}
		else
			return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyRecipes.hasDecodedItem(stack))
		{
			CompoundNBT nbt = stack.getTag();
			ItemStack content = AlchemyRecipes.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				String stackSize = (nbt.getBoolean("punched") || nbt.getInt("contentSize") <= 0) ? "" : nbt.getInt("contentSize") + "x";
				tooltip.add(new StringTextComponent("(").appendText(stackSize).appendSibling(content.getDisplayName()).appendText(")"));
				if(nbt.getBoolean("punched"))
					tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent("item.minestuck.captcha_card.punched")).appendText(")"));
				else if(nbt.getInt("contentSize") <= 0)
					tooltip.add(new StringTextComponent("(").appendSibling(new StringTextComponent("item.minestuck.captcha_card.ghost")).appendText(")"));
			} else
			{
				tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent("item.minestuck.captcha_card.invalid")).appendText(")"));
			}
		} else
			tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent("item.minestuck.captcha_card.empty")).appendText(")"));
	}
	
}
