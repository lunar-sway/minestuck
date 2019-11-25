package com.mraof.minestuck.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.ColorSelectorScreen;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.inventory.ConsortMerchantContainer;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Used to track mixed client sided events.
 */
public class ClientEventHandler
{
	
	/*@SubscribeEvent TODO Find new event or similar here
	public void onConnectedToServer(ClientConnectedToServerEvent event)	//Reset all static client-side data here
	{
		GuiPlayerStats.normalTab = GuiPlayerStats.NormalGuiType.CAPTCHA_DECK;
		GuiPlayerStats.editmodeTab = GuiPlayerStats.EditmodeGuiType.DEPLOY_LIST;
		ContainerEditmode.clientScroll = 0;
		CaptchaDeckHandler.clientSideModus = null;
		MinestuckPlayerData.title = null;
		MinestuckPlayerData.rung = -1;
		ColorCollector.playerColor = -1;
		ColorCollector.displaySelectionGui = false;
		GuiDataChecker.activeComponent = null;
		GuiEcheladder.lastRung = -1;
		GuiEcheladder.animatedRung = 0;
		SkaiaClient.clear();
	}*/
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			if(ColorCollector.displaySelectionGui && Minecraft.getInstance().currentScreen == null)
			{
				ColorCollector.displaySelectionGui = false;
				if(MinestuckConfig.loginColorSelector.get())
					Minecraft.getInstance().displayGuiScreen(new ColorSelectorScreen(true));
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(event.getPlayer() != null && event.getPlayer().openContainer instanceof ConsortMerchantContainer
					&& event.getPlayer().openContainer.getInventory().contains(stack))
			{
				String unlocalized = stack.getTranslationKey();
				
				EnumConsort type = ((ConsortMerchantContainer)event.getPlayer().openContainer).inventory.getConsortType();
				String arg1 = I18n.format(type.getConsortType().getTranslationKey());
				
				String name = "store."+unlocalized;
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.hasKey(name))
					event.getToolTip().add(new TranslationTextComponent(name, arg1));
				else event.getToolTip().add(stack.getDisplayName());
				if(I18n.hasKey(tooltip))
					event.getToolTip().add(new TranslationTextComponent(tooltip, arg1).setStyle(new Style().setColor(TextFormatting.GRAY)));
			} else if(stack.getItem().getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
			{
				String name = stack.getTranslationKey() + ".tooltip";
				if(I18n.hasKey(name))
					event.getToolTip().add(1, new TranslationTextComponent(name).setStyle(new Style().setColor(TextFormatting.GRAY)));
			}
		}
	}
	
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		if (event.getInfo().getFluidState().getFluid() == MSFluids.ENDER.get())
		{
			event.setCanceled(true);
			event.setDensity(Float.MAX_VALUE);
			GlStateManager.fogMode(GlStateManager.FogMode.EXP);
		}
	}
}
