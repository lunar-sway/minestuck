package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.ConsortMerchantMenu;
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

/**
 * Used to track mixed client sided events.
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEventHandler
{
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public static void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(event.getEntity() != null && event.getEntity().containerMenu instanceof ConsortMerchantMenu
					&& event.getEntity().containerMenu.getItems().contains(stack))
			{
				String unlocalized = stack.getDescriptionId();
				
				EnumConsort type = ((ConsortMerchantMenu)event.getEntity().containerMenu).getConsortType();
				String arg1 = I18n.get(type.getConsortType().getDescriptionId());
				
				String name = "store."+unlocalized;
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.exists(name))
					event.getToolTip().add(Component.translatable(name, arg1));
				else event.getToolTip().add(stack.getHoverName());
				if(I18n.exists(tooltip))
					event.getToolTip().add(Component.translatable(tooltip, arg1).withStyle(ChatFormatting.GRAY));
			} else {
				final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
				if(itemId != null && itemId.getNamespace().equals(Minestuck.MOD_ID))
				{
					String name = stack.getDescriptionId() + ".tooltip";
					if(I18n.exists(name))
						event.getToolTip().add(1, Component.translatable(name).withStyle(ChatFormatting.GRAY));
				}
			}
		}
	}
}
