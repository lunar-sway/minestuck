package com.mraof.minestuck.client.settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Needed for getting key-input inside containers.
import org.lwjgl.input.Keyboard;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;

public class MinestuckKeyHandler
{
	
	KeyBinding statKey;
	KeyBinding editKey;
	KeyBinding captchaKey;
	boolean statKeyPressed = false;
	boolean editKeyPressed = false;
	boolean captchaKeyPressed = false;
	
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
			if(Minecraft.getMinecraft().currentScreen == null && Minecraft.getMinecraft().player.getHeldItemMainhand() != null)
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE));
		}
		else if(Keyboard.isKeyDown(captchaKey.getKeyCode()) && !captchaKeyPressed) {
			if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer && ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse().getHasStack())
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE_INV));
		}
		
		
		statKeyPressed = statKey.isKeyDown();
		editKeyPressed = editKey.isKeyDown();
		//Raw keyboard input is gotten because the Minecraft key-input handler doesn't work inside containers.
		captchaKeyPressed = Keyboard.isKeyDown(captchaKey.getKeyCode());
		
	}
	
}
