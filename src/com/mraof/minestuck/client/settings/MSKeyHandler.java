package com.mraof.minestuck.client.settings;

import com.mraof.minestuck.client.gui.MSScreenFactories;
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
import com.mraof.minestuck.network.MSPacketHandler;
import org.lwjgl.glfw.GLFW;

public class MSKeyHandler
{
	public static final String CATEGORY = "key.categories.minestuck";
	public static final String STATS_GUI = "key.minestuck.stats_gui";
	public static final String EXIT_EDIT_MODE = "key.minestuck.exit_edit_mode";
	public static final String CAPTCHALOGUE = "key.minestuck.captchalogue";
	public static final String ASPECT_EFFECT_TOGGLE = "key.minestuck.aspext_effect_toggle";
	public static final String SYLLADEX = "key.minestuck.sylladex";
	
	public static final MSKeyHandler instance = new MSKeyHandler();
	public KeyBinding statKey;
	public KeyBinding editKey;
	public KeyBinding captchaKey;
	public KeyBinding effectToggleKey;
	public KeyBinding sylladexKey;
	boolean captchaKeyPressed = false;
	
	public void registerKeys()
	{
		if(statKey != null)
			throw new IllegalStateException("Minestuck keys have already been registered!");
		
		statKey = new KeyBinding(STATS_GUI, GLFW.GLFW_KEY_G, CATEGORY);
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding(EXIT_EDIT_MODE, GLFW.GLFW_KEY_K, CATEGORY);
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding(CAPTCHALOGUE, GLFW.GLFW_KEY_V, CATEGORY);
		ClientRegistry.registerKeyBinding(captchaKey);
		effectToggleKey = new KeyBinding(ASPECT_EFFECT_TOGGLE, GLFW.GLFW_KEY_BACKSLASH, CATEGORY);
		ClientRegistry.registerKeyBinding(effectToggleKey);
		sylladexKey = new KeyBinding(SYLLADEX, -1, CATEGORY);
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
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
		}
		
		while(effectToggleKey.isPressed())
		{
			MSPacketHandler.sendToServer(new EffectTogglePacket());
		}
		
		while(sylladexKey.isPressed())
		{
			if(CaptchaDeckHandler.clientSideModus != null)
				MSScreenFactories.displaySylladexScreen(CaptchaDeckHandler.clientSideModus);
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), captchaKey.getKey().getKeyCode()) && !captchaKeyPressed) {

				//This statement is here because for some reason 'slotNumber' always returns as 0 if it is referenced inside the creative inventory.
			if (Minecraft.getInstance().currentScreen instanceof CreativeScreen && Minecraft.getInstance().player.openContainer instanceof CreativeScreen.CreativeContainer && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getSlotIndex()));
			else if(Minecraft.getInstance().currentScreen instanceof ContainerScreen && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((ContainerScreen)Minecraft.getInstance().currentScreen).getSlotUnderMouse().slotNumber));
		}
		
		captchaKeyPressed = InputMappings.isKeyDown(Minecraft.getInstance().mainWindow.getHandle(), captchaKey.getKey().getKeyCode());
	}
	
}
