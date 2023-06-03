package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CardCaptchas;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items)
	{
		if(this.allowedIn(tab))
		{
			items.add(new ItemStack(this));
			items.add(AlchemyHelper.createCard(new ItemStack(MSItems.CRUXITE_APPLE.get()), true));
		}
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
					String captcha = getCaptcha(stack, content);
					if(captcha != null)
						tooltip.add(Component.literal(captcha));
					
					tooltip.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".ghost")));
				} else
				{
					String captcha = getCaptcha(stack, content);
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
	private String getCaptcha(ItemStack decodedStack, ItemStack content)
	{
		if(AlchemyHelper.isReadableCard(decodedStack))
			return CardCaptchas.getCaptchaFromItem(content.getItem());
		
		return null;
	}
	
	private Component makeTooltipInfo(Component info)
	{
		return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
	}
}