package com.mraof.minestuck.event;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.client.gui.GuiColorSelector;
import com.mraof.minestuck.client.gui.playerStats.GuiDataChecker;
import com.mraof.minestuck.client.gui.playerStats.GuiEcheladder;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
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
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Used to track mixed client sided events.
 */
@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	
	@SubscribeEvent
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
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START)
		{
			if(ColorCollector.displaySelectionGui && Minecraft.getMinecraft().currentScreen == null)
			{
				ColorCollector.displaySelectionGui = false;
				if(MinestuckConfig.loginColorSelector)
					Minecraft.getMinecraft().displayGuiScreen(new GuiColorSelector(true));
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
				String unlocalized = stack.getUnlocalizedName();
				if(stack.getItem() instanceof ItemPotion)
					unlocalized = PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.");
				
				String name = "store."+unlocalized+".name";
				String tooltip = "store."+unlocalized+".tooltip";
				event.getToolTip().clear();
				if(I18n.hasKey(name))
					event.getToolTip().add(I18n.format(name));
				else event.getToolTip().add(stack.getDisplayName());
				if(I18n.hasKey(tooltip))
					event.getToolTip().add(I18n.format(tooltip));
			} else if(stack.getItem().getRegistryName().getResourceDomain().equals(Minestuck.class.getAnnotation(Mod.class).modid()))
			{
				String name = stack.getUnlocalizedName() + ".tooltip";
				if(I18n.hasKey(name))
					event.getToolTip().add(1, I18n.format(name));
			}
		}
	}
	
}
