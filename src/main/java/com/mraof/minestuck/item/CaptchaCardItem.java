package com.mraof.minestuck.item;

import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class CaptchaCardItem extends Item
{
	public CaptchaCardItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items)
	{
		if(this.allowdedIn(group))
		{
			items.add(new ItemStack(this));
			items.add(AlchemyHelper.createCard(new ItemStack(MSItems.CRUXITE_APPLE), true));
		}
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		
		ItemStack stack = playerIn.getItemInHand(handIn);
		
		if(playerIn.isShiftKeyDown() && stack.hasTag() && ((AlchemyHelper.isGhostCard(stack) && !AlchemyHelper.isPunchedCard(stack)) || !AlchemyHelper.hasDecodedItem(stack)))
		{
			AlchemyHelper.removeItemFromCard(stack);
			return ActionResult.success(new ItemStack(playerIn.getItemInHand(handIn).getItem(), playerIn.getItemInHand(handIn).getCount()));
		}
		else return ActionResult.pass(playerIn.getItemInHand(handIn));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				ITextComponent contentName = content.getHoverName();
				tooltip.add(makeTooltipInfo((AlchemyHelper.isPunchedCard(stack) || AlchemyHelper.isGhostCard(stack))
						? contentName : new StringTextComponent(content.getCount() + "x").append(contentName)));
				
				if(AlchemyHelper.isPunchedCard(stack))
					tooltip.add(makeTooltipInfo(new TranslationTextComponent(getDescriptionId() + ".punched")));
				else if(AlchemyHelper.isGhostCard(stack))
					tooltip.add(makeTooltipInfo(new TranslationTextComponent(getDescriptionId() + ".ghost")));
			} else tooltip.add(makeTooltipInfo(new TranslationTextComponent(getDescriptionId() + ".invalid")));
		} else
			tooltip.add(makeTooltipInfo(new TranslationTextComponent(getDescriptionId() + ".empty")));
	}
	
	private ITextComponent makeTooltipInfo(ITextComponent info)
	{
		return new StringTextComponent("(").append(info).append(")").withStyle(TextFormatting.GRAY);
	}
}