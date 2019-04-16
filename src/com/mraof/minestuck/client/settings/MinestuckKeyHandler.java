package com.mraof.minestuck.client.settings;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//Needed for getting key-input inside containers.
import org.lwjgl.input.Keyboard;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;

public class MinestuckKeyHandler
{
	public static final MinestuckKeyHandler instance = new MinestuckKeyHandler();
	public KeyBinding statKey;
	public KeyBinding editKey;
	public KeyBinding captchaKey;
	public KeyBinding effectToggleKey;
	public KeyBinding sylladexKey;
	boolean captchaKeyPressed = false;
	
	public void registerKeys()
	{
		if(statKey != null)
			throw new IllegalStateException("Minestucck keys have already been registered!");
		
		statKey = new KeyBinding("key.statsGui", Keyboard.KEY_G, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding("key.exitEdit", Keyboard.KEY_X, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding("key.captchalouge", Keyboard.KEY_C, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(captchaKey);
		effectToggleKey = new KeyBinding("key.aspectEffectToggle", Keyboard.KEY_BACKSLASH, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(effectToggleKey);
		sylladexKey = new KeyBinding("key.sylladex", Keyboard.KEY_NONE, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(sylladexKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)	//This is only called during the game, when no gui is active
	{
		while(statKey.isPressed())
		{
			GuiPlayerStats.openGui(false);
		}
		
		while(editKey.isPressed())
		{
			ClientEditHandler.onKeyPressed();
		}
		
		while(captchaKey.isPressed())
		{
			if(!Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty())
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE));
		}
		
		while(effectToggleKey.isPressed())
		{
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.EFFECT_TOGGLE));
		}
		
		while(sylladexKey.isPressed())
		{
			if(CaptchaDeckHandler.clientSideModus != null)
				Minecraft.getMinecraft().displayGuiScreen(CaptchaDeckHandler.clientSideModus.getGuiHandler());
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(Keyboard.isKeyDown(captchaKey.getKeyCode()) && !captchaKeyPressed) {

				//This statement is here because for some reason 'slotNumber' always returns as 0 if it is referenced inside the creative inventory.
			if (Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative && Minecraft.getMinecraft().player.openContainer instanceof GuiContainerCreative.ContainerCreative && ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse() != null && ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE_INV, ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse().getSlotIndex()));
			else if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer && ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse() != null && ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(MinestuckPacket.Type.CAPTCHA, CaptchaDeckPacket.CAPTCHALOUGE_INV, ((GuiContainer)Minecraft.getMinecraft().currentScreen).getSlotUnderMouse().slotNumber));
		}
		
		captchaKeyPressed = Keyboard.isKeyDown(captchaKey.getKeyCode());
	}
	
}
