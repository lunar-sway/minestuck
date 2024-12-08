package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.CardCaptchas;
import com.mraof.minestuck.item.components.CaptchaCodeComponent;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CaptchaCardItem extends Item
{
	public CaptchaCardItem(Properties properties)
	{
		super(properties.component(MSItemComponents.CAPTCHA_CODE, CaptchaCodeComponent.ZERO));
	}
	
	@Override
	public int getMaxStackSize(ItemStack stack)
	{
		if(stack.has(MSItemComponents.CARD_STORED_ITEM) || stack.has(MSItemComponents.ENCODED_ITEM))
			return 16;
		else return 64;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		
		ItemStack stack = playerIn.getItemInHand(handIn);
		
		if(playerIn.isShiftKeyDown() && stack.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).isGhostItem())
		{
			stack.remove(MSItemComponents.CARD_STORED_ITEM);
			return InteractionResultHolder.success(new ItemStack(stack.getItem(), stack.getCount()));
		} else return InteractionResultHolder.pass(stack);
	}
	
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		EncodedItemComponent encodedItemComponent = stack.get(MSItemComponents.ENCODED_ITEM);
		CardStoredItemComponent cardStoredItemComponent = stack.get(MSItemComponents.CARD_STORED_ITEM);
		if(encodedItemComponent != null)
		{
			ItemStack content = encodedItemComponent.asItemStack();
			if(!content.isEmpty())
			{
				Component contentName = content.getHoverName();
				tooltipComponents.add(makeTooltipInfo(contentName));
				
				tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".punched")));
			} else tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".invalid")));
		} else if(cardStoredItemComponent != null)
		{
			ItemStack content = cardStoredItemComponent.storedStack();
			if(!content.isEmpty())
			{
				Component contentName = content.getHoverName();
				tooltipComponents.add(makeTooltipInfo(cardStoredItemComponent.isGhostItem()
						? contentName : Component.literal(content.getCount() + "x").append(contentName)));
				
				//TODO consider obfuscated characters for unreadable captcha
				CaptchaCodeComponent captchaCode = stack.get(MSItemComponents.CAPTCHA_CODE);
				if(captchaCode != null)
					tooltipComponents.add(Component.literal(captchaCode.code()));
				
				if(cardStoredItemComponent.isGhostItem())
					tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".ghost")));
				
			} else
				tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".invalid")));
		} else
		{
			tooltipComponents.add(Component.literal(CardCaptchas.EMPTY_CARD_CAPTCHA));
			tooltipComponents.add(makeTooltipInfo(Component.translatable(getDescriptionId() + ".empty")));
		}
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected)
	{
		if(!(level instanceof ServerLevel serverLevel))
			return;
		CaptchaCodeComponent existingCode = stack.get(MSItemComponents.CAPTCHA_CODE);
		if(existingCode == null || existingCode.hasRefreshed())
			return;
		
		CardStoredItemComponent storedItem = stack.get(MSItemComponents.CARD_STORED_ITEM);
		if(storedItem != null)
		{
			stack.set(MSItemComponents.CAPTCHA_CODE, CaptchaCodeComponent.createFor(storedItem.storedStack(), serverLevel.getServer()));
		}
	}
	
	private static Component makeTooltipInfo(Component info)
	{
		return Component.literal("(").append(info).append(")").withStyle(ChatFormatting.GRAY);
	}
	
	public static ItemStack createCardWithItem(ItemStack storedStack, MinecraftServer mcServer)
	{
		return createCardWithStorage(new CardStoredItemComponent(storedStack, false), mcServer);
	}
	
	public static ItemStack createGhostCard(ItemStack ghostStack, MinecraftServer mcServer)
	{
		return createCardWithStorage(new CardStoredItemComponent(ghostStack, true), mcServer);
	}
	
	private static ItemStack createCardWithStorage(CardStoredItemComponent component, MinecraftServer mcServer)
	{
		ItemStack cardStack = new ItemStack(MSItems.CAPTCHA_CARD.get());
		cardStack.set(MSItemComponents.CARD_STORED_ITEM, component);
		if(component.storedStack().is(MSTags.Items.UNREADABLE))
			cardStack.remove(MSItemComponents.CAPTCHA_CODE);
		else
			cardStack.set(MSItemComponents.CAPTCHA_CODE, CaptchaCodeComponent.createFor(component.storedStack(), mcServer));
		return cardStack;
	}
	
	public static ItemStack createPunchedCard(Item encodedItem)
	{
		return EncodedItemComponent.createEncoded(MSItems.CAPTCHA_CARD, encodedItem);
	}
	
	public static boolean isUnpunchedCard(ItemStack stack)
	{
		return stack.is(MSItems.CAPTCHA_CARD) && !stack.has(MSItemComponents.ENCODED_ITEM);
	}
}
