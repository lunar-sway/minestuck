package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiColorSelector;
import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.client.gui.playerStats.GuiEcheladder;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.ContainerConsortMerchant;
import com.mraof.minestuck.inventory.ContainerEditmode;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.skaianet.SkaiaClient;
import com.mraof.minestuck.util.ColorCollector;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Used to track mixed client sided events.
 */
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler
{
	
	/*@SubscribeEvent TODO Find new event hor similar here
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
				if(MinestuckConfig.loginColorSelector)
					Minecraft.getInstance().displayGuiScreen(new GuiColorSelector(true));
			}
			
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void addCustomTooltip(ItemTooltipEvent event)
	{
		//Add config check
		{
			ItemStack stack = event.getItemStack();
			if(event.getEntityPlayer() != null && event.getEntityPlayer().openContainer instanceof ContainerConsortMerchant
					&& event.getEntityPlayer().openContainer.getInventory().contains(stack))
			{
				String unlocalized = stack.getTranslationKey();
				if(stack.getItem() instanceof ItemPotion)
					unlocalized = PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.");
				
				EnumConsort type = ((ContainerConsortMerchant)event.getEntityPlayer().openContainer).inventory.getConsortType();
				String arg1 = I18n.format("entity.minestuck." + type.getName() + ".name");
				
				String name = "store."+unlocalized+".name";
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.hasKey(name))
					event.getToolTip().add(new TextComponentTranslation(name, arg1));
				else event.getToolTip().add(stack.getDisplayName());
				if(I18n.hasKey(tooltip))
					event.getToolTip().add(new TextComponentTranslation(tooltip, arg1));
			} else if(stack.getItem().getRegistryName().getNamespace().equals(Minestuck.MOD_ID))
			{
				String name = stack.getTranslationKey() + ".tooltip";
				if(I18n.hasKey(name))
					event.getToolTip().add(1, new TextComponentTranslation(name));
			}
		}
	}
	/*
	@SubscribeEvent
	public void onFogRender(EntityViewRenderEvent.FogDensity event)
	{
		if (event.getState().getBlock() == MinestuckBlocks.blockEnder)
		{
			event.setCanceled(true);
			event.setDensity(Float.MAX_VALUE);
			GlStateManager.setFog(GlStateManager.FogMode.EXP);
		}
	}*/
}
