package com.mraof.minestuck.client.settings;

import com.mraof.minestuck.client.gui.ModScreenFactories;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.network.EffectTogglePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import org.lwjgl.glfw.GLFW;

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
		
		statKey = new KeyBinding("key.statsGui", GLFW.GLFW_KEY_G, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding("key.exitEdit", GLFW.GLFW_KEY_K, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding("key.captchalogue", GLFW.GLFW_KEY_V, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(captchaKey);
		effectToggleKey = new KeyBinding("key.aspectEffectToggle", GLFW.GLFW_KEY_BACKSLASH, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(effectToggleKey);
		sylladexKey = new KeyBinding("key.sylladex", -1, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(sylladexKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)	//This is only called during the game, when no gui is active
	{
		while(statKey.isPressed())
		{
			PlayerStatsScreen.openGui(false);
		}
		
		while(editKey.isPressed())
		{
			ClientEditHandler.onKeyPressed();
		}
		
		while(captchaKey.isPressed())
		{
			if(!Minecraft.getInstance().player.getHeldItemMainhand().isEmpty())
				MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
		}
		
		while(effectToggleKey.isPressed())
		{
			MinestuckPacketHandler.sendToServer(new EffectTogglePacket());
		}
		
		while(sylladexKey.isPressed())
		{
			if(CaptchaDeckHandler.clientSideModus != null)
				ModScreenFactories.displaySylladexScreen(CaptchaDeckHandler.clientSideModus);
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), captchaKey.getKey().getKeyCode()) && !captchaKeyPressed) {

				//This statement is here because for some reason 'slotNumber' always returns as 0 if it is referenced inside the creative inventory.
			if (Minecraft.getInstance().currentScreen instanceof CreativeScreen && Minecraft.getInstance().player.openContainer instanceof CreativeScreen.CreativeContainer && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getSlotIndex()));
			else if(Minecraft.getInstance().currentScreen instanceof ContainerScreen && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().slotNumber));
		}
		
		captchaKeyPressed = InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), captchaKey.getKey().getKeyCode());
	}
	
}
