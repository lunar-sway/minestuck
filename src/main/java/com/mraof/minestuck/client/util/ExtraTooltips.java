package com.mraof.minestuck.client.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
import com.mraof.minestuck.item.components.CassettePlayable;
import com.mraof.minestuck.item.components.MSItemComponents;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

/**
 * Handles the logic of custom tooltips.
 * If the tooltip shows inside of a consort menu, it'll replace the tooltip,
 * optionally overriding the item name with a custom name and optionally adding a store tooltip.
 * Otherwise, it will optionally add an extra tooltip for minestuck items specifically.
 * In all these cases, "optionally" means "if the to-be-used translation key exists".
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ExtraTooltips
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	private static void addCustomTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		
		// Handle cassettes
		if(stack.has(MSItemComponents.CASSETTE_SONG))
		{
			CassettePlayable cassette = stack.get(MSItemComponents.CASSETTE_SONG);
			cassette.addToTooltip(event.getContext(), component -> event.getToolTip().add(1, component), event.getFlags());
		}

		if(event.getEntity() != null && event.getEntity().containerMenu instanceof ConsortMerchantMenu menu
				&& event.getEntity().containerMenu.getItems().contains(stack))
			handleStoreTooltips(stack, event.getToolTip(), menu);
		else
			handleCustomTooltips(stack, event.getToolTip());
	}
	
	private static void handleCustomTooltips(ItemStack stack, List<Component> tooltips)
	{
		final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
		if(itemId.getNamespace().equals(Minestuck.MOD_ID))
		{
			String name = stack.getDescriptionId() + ".tooltip";
			if(I18n.exists(name))
				tooltips.add(1, Component.translatable(name).withStyle(ChatFormatting.GRAY));
		}
	}
	
	private static void handleStoreTooltips(ItemStack stack, List<Component> tooltips, ConsortMerchantMenu menu)
	{
		String unlocalized = stack.getDescriptionId();
		
		EnumConsort type = menu.getConsortType();
		Component consortDescription = Component.translatable(type.getConsortType().getDescriptionId());
		
		tooltips.clear();
		String name = "store." + unlocalized;
		if(I18n.exists(name))
			tooltips.add(Component.translatable(name, consortDescription));
		else tooltips.add(stack.getHoverName());
		
		String tooltip = "store." + unlocalized + ".tooltip";
		if(I18n.exists(tooltip))
			tooltips.add(Component.translatable(tooltip, consortDescription).withStyle(ChatFormatting.GRAY));
	}
}
