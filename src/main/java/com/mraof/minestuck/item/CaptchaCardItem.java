package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CardCaptchas;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class CaptchaCardItem extends Item
{
	public CaptchaCardItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		
		ItemStack stack = playerIn.getItemInHand(handIn);
		
		if(playerIn.isShiftKeyDown() && stack.hasTag() && ((AlchemyHelper.isGhostCard(stack) && !AlchemyHelper.isPunchedCard(stack)) || !AlchemyHelper.hasDecodedItem(stack)))
		{
			AlchemyHelper.removeItemFromCard(stack);
			return InteractionResultHolder.success(new ItemStack(playerIn.getItemInHand(handIn).getItem(), playerIn.getItemInHand(handIn).getCount()));
		} else return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				Component contentName = content.getHoverName();
				tooltip.add(makeTooltipInfo((AlchemyHelper.isPunchedCard(stack) || AlchemyHelper.isGhostCard(stack))
						? contentName : Component.literal(content.getCount() + "x").append(contentName)));
				
				if(AlchemyHelper.isPunchedCard(stack))
					tooltip.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".punched")));
				else if(AlchemyHelper.isGhostCard(stack))
				{
					String captcha = getCaptcha(stack);
					if(captcha != null)
						tooltip.add(Component.literal(captcha));
					
					tooltip.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".ghost")));
				} else
				{
					String captcha = getCaptcha(stack);
					if(captcha != null)
						tooltip.add(Component.literal(captcha));
				}
			} else tooltip.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".invalid")));
		} else
		{
			tooltip.add(Component.literal(CardCaptchas.EMPTY_CARD_CAPTCHA));
			tooltip.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".empty")));
		}
	}
	
	//TODO consider obfuscated characters for unreadable unpunched card
	private String getCaptcha(ItemStack stack)
	{
		CompoundTag tag = stack.getTag();
		if(tag != null && tag.contains("captcha_code", Tag.TAG_STRING))
			return tag.getString("captcha_code");
		
		return null;
	}
	
	private Component makeTooltipInfo(Component info)
	{
		return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
	}
}