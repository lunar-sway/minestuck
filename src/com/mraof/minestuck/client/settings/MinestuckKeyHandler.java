package com.mraof.minestuck.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import com.mraof.minestuck.client.gui.playerStats.GuiGristCache;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class MinestuckKeyHandler
{
	
	KeyBinding statKey;
	KeyBinding editKey;
	KeyBinding captchaKey;
	boolean statKeyPressed = false;
	boolean editKeyPressed = false;
	boolean captchaPressed = false;
	
	public MinestuckKeyHandler()
	{
		statKey = new KeyBinding("key.statsGui", 34, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding("key.exitEdit", 45, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding("key.captchalouge", 46, "key.categories.minestuck");
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(statKey.getIsKeyPressed() && !statKeyPressed)
		{
			
			GuiPlayerStats.openGui(false);
			
		}
		else if(editKey.getIsKeyPressed() && !editKeyPressed)
		{
			
			if(Minecraft.getMinecraft().currentScreen == null)
				ClientEditHandler.onKeyPressed();
			
		}
		else if(captchaKey.getIsKeyPressed() && !captchaPressed)
		{
			if(Minecraft.getMinecraft().currentScreen == null)
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE));
		}
		
		statKeyPressed = statKey.getIsKeyPressed();
		editKeyPressed = editKey.getIsKeyPressed();
		captchaPressed = captchaKey.getIsKeyPressed();
		
	}
	
}
