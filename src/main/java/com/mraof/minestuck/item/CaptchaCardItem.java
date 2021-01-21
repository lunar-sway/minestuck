package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CaptchaCardItem extends Item
{
	public static final IItemPropertyGetter CONTENT = (stack, world, holder) -> AlchemyHelper.hasDecodedItem(stack) ? 1 : 0;
	public static final ResourceLocation CONTENT_NAME = new ResourceLocation(Minestuck.MOD_ID, "content");
	
	public CaptchaCardItem(Properties properties)
	{
		super(properties);
		this.addPropertyOverride(CONTENT_NAME, CONTENT);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "punched"), (stack, world, holder) -> AlchemyHelper.isPunchedCard(stack) ? 1 : 0);
		this.addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "ghost"), (stack, world, holder) -> AlchemyHelper.isGhostCard(stack) ? 1 : 0);
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
			items.add(AlchemyHelper.createCard(new ItemStack(MSItems.CRUXITE_APPLE), true));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(playerIn.isSneaking() && stack.hasTag() && ((AlchemyHelper.isGhostCard(stack) && !AlchemyHelper.isPunchedCard(stack)) || !AlchemyHelper.hasDecodedItem(stack)))
		{
			AlchemyHelper.removeItemFromCard(stack);
			return new ActionResult<>(ActionResultType.SUCCESS, stack);
		}
		else return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				ITextComponent contentName = content.getDisplayName();
				tooltip.add(makeTooltipInfo((AlchemyHelper.isPunchedCard(stack) || AlchemyHelper.isGhostCard(stack))
						? contentName : new StringTextComponent(content.getCount() + "x").appendSibling(contentName)));
				
				if(AlchemyHelper.isPunchedCard(stack))
					tooltip.add(makeTooltipInfo(new TranslationTextComponent(getTranslationKey() + ".punched")));
				else if(AlchemyHelper.isGhostCard(stack))
					tooltip.add(makeTooltipInfo(new TranslationTextComponent(getTranslationKey() + ".ghost")));
			} else tooltip.add(makeTooltipInfo(new TranslationTextComponent(getTranslationKey() + ".invalid")));
		} else
			tooltip.add(makeTooltipInfo(new TranslationTextComponent(getTranslationKey() + ".empty")));
	}
	
	private ITextComponent makeTooltipInfo(ITextComponent info)
	{
		return new StringTextComponent("(").appendSibling(info).appendText(")").applyTextStyle(TextFormatting.GRAY);
	}
}