package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.CardCaptchas;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.*;

public class RightClickMessageItem extends Item
{
	private final Type type;
	final CardCaptchas captchas = new CardCaptchas();
	
	public enum Type
	{
		EIGHTBALL,
		DICE,
		DEFAULT
	}
	
	public RightClickMessageItem(Properties properties, Type type)
	{
		super(properties);
		this.type = type;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		if(!level.isClientSide)
		{
			if(playerIn.isShiftKeyDown())
			{
				Item offhandItem = playerIn.getItemInHand(InteractionHand.OFF_HAND).getItem();
				Optional<ResourceKey<Item>> resourceKey = offhandItem.builtInRegistryHolder().unwrapKey();
				resourceKey.ifPresent(key -> captchas.captchaFromItem(key.location().toString(), level));
				
			} else
			{
				for(ItemStack iteratedItem : playerIn.inventoryMenu.getItems())
				{
					Optional<ResourceKey<Item>> resourceKey = iteratedItem.getItem().builtInRegistryHolder().unwrapKey();
					resourceKey.ifPresent(key -> captchas.captchaFromItem(key.location().toString(), level));
				}
			}
		}
		
		if(level.isClientSide)
		{
			if(type == Type.EIGHTBALL)
			{
				int key = playerIn.getRandom().nextInt(20);
				MutableComponent message = Component.translatable("message.eightball." + key);
				playerIn.sendSystemMessage(message.withStyle(ChatFormatting.BLUE));
			} else if(type == Type.DICE)
			{
				int key = playerIn.getRandom().nextInt(6);
				MutableComponent message = Component.translatable("message.dice." + key);
				playerIn.sendSystemMessage(message.withStyle(ChatFormatting.WHITE));
			} else
			{
				playerIn.sendSystemMessage(Component.translatable(getDescriptionId() + ".message")); //default, creates message for that item under that item's "addExtra" in MSEnUsLang provider
			}
		}
		
		return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
	}
}
