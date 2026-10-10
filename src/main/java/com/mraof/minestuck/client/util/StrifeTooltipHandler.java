package com.mraof.minestuck.client.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Marks weapons that were drawn from a strife deck, and (with advanced tooltips) lists the kinds that an item belongs to.
 * Ported from Minestuck Universe (fogre 1.12.2)
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class StrifeTooltipHandler
{
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();
		if(stack.isEmpty()) return;
		
		List<Component> tooltip = event.getToolTip();
		
		if(stack.has(MSItemComponents.STRIFE_ASSIGNED.get()))
			tooltip.add(Math.min(1, tooltip.size()), Component.translatable("strife.item.allocated").withStyle(ChatFormatting.DARK_GREEN));
		
		if(event.getFlags().isAdvanced())
		{
			List<String> kinds = new ArrayList<>();
			for(KindAbstratusType type : KindAbstratusList.getTypeList())
				if(type.partOf(stack))
					kinds.add(type.getDisplayName().getString().toLowerCase());
			
			if(!kinds.isEmpty())
				tooltip.add(Component.translatable("strife.item.abstrataList", String.join(", ", kinds)).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
		}
	}
}
