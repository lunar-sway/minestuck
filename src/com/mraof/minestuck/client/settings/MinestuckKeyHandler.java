package com.mraof.minestuck.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.mraof.minestuck.client.gui.playerStats.GuiGristCache;
import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.Debug;

public class MinestuckKeyHandler
{
	
	KeyBinding statKey;
	KeyBinding editKey;
	KeyBinding captchaKey;
	boolean statKeyPressed = false;
	boolean editKeyPressed = false;
	
	public MinestuckKeyHandler()
	{
		statKey = new KeyBinding("key.statsGui", 34, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding("key.exitEdit", 45, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding("key.captchalouge", 46, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(captchaKey);
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(statKey.isKeyDown() && !statKeyPressed)
		{
			
			GuiPlayerStats.openGui(false);
			
		}
		else if(editKey.isKeyDown() && !editKeyPressed)
		{
			
			if(Minecraft.getMinecraft().currentScreen == null)
				ClientEditHandler.onKeyPressed();
			
		}
		else if(captchaKey.isPressed())
		{
			if(Minecraft.getMinecraft().currentScreen == null && Minecraft.getMinecraft().thePlayer.getHeldItem() != null)
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE));
		}
		
		statKeyPressed = statKey.isKeyDown();
		editKeyPressed = editKey.isKeyDown();
		
	}
	
}
