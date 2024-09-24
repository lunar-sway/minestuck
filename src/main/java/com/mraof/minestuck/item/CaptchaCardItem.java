package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
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
		if(AlchemyHelper.hasDecodedItem(stack))
			return 16;
		else return 64;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		
		ItemStack stack = playerIn.getItemInHand(handIn);
		
		if(playerIn.isShiftKeyDown() && AlchemyHelper.isGhostCard(stack))
		{
			AlchemyHelper.removeItemFromCard(stack);
			return InteractionResultHolder.success(new ItemStack(playerIn.getItemInHand(handIn).getItem(), playerIn.getItemInHand(handIn).getCount()));
		} else return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
	}
	
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack content = AlchemyHelper.getDecodedItem(stack);
			if(!content.isEmpty())
			{
				Component contentName = content.getHoverName();
				tooltipComponents.add(makeTooltipInfo((AlchemyHelper.isPunchedCard(stack) || AlchemyHelper.isGhostCard(stack))
						? contentName : Component.literal(content.getCount() + "x").append(contentName)));
				
				if(AlchemyHelper.isPunchedCard(stack))
					tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".punched")));
				else if(AlchemyHelper.isGhostCard(stack))
				{
					Component captcha = getCaptcha(stack);
					if(captcha != null)
						tooltipComponents.add(captcha);
					
					tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".ghost")));
				} else
				{
					Component captcha = getCaptcha(stack);
					if(captcha != null)
						tooltipComponents.add(captcha);
				}
			} else tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".invalid")));
		} else
		{
			tooltipComponents.add(Component.literal(CardCaptchas.EMPTY_CARD_CAPTCHA));
			tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".empty")));
		}
	}
	
	//TODO consider obfuscated characters for unreadable unpunched card
	@Nullable
	private Component getCaptcha(ItemStack stack)
	{
		EncodedItemComponent dataComponent = stack.getOrDefault(MSItemComponents.ENCODED_ITEM, EncodedItemComponent.EMPTY);
		return dataComponent.code().isEmpty() ? null : Component.literal(dataComponent.code()).withStyle(style -> style.withObfuscated(!dataComponent.canReadCode()));
	}
	
	private Component makeTooltipInfo(Component info)
	{
		return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
	}
}