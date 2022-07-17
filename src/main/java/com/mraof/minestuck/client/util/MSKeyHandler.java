package com.mraof.minestuck.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.EffectTogglePacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MSKeyHandler
{
	public static final String CATEGORY = "key.categories.minestuck";
	public static final String STATS_GUI = "key.minestuck.stats_gui";
	public static final String EXIT_EDIT_MODE = "key.minestuck.exit_edit_mode";
	public static final String CAPTCHALOGUE = "key.minestuck.captchalogue";
	public static final String ASPECT_EFFECT_TOGGLE = "key.minestuck.aspext_effect_toggle";
	public static final String SYLLADEX = "key.minestuck.sylladex";
	
	public static KeyMapping statKey;
	public static KeyMapping editKey;
	public static KeyMapping captchaKey;
	public static KeyMapping effectToggleKey;
	public static KeyMapping sylladexKey;
	
	public static void registerKeys()
	{
		if(statKey != null)
			throw new IllegalStateException("Minestuck keys have already been registered!");
		
		statKey = new KeyMapping(STATS_GUI, GLFW.GLFW_KEY_G, CATEGORY);
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyMapping(EXIT_EDIT_MODE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyMapping(CAPTCHALOGUE, GLFW.GLFW_KEY_V, CATEGORY);
		ClientRegistry.registerKeyBinding(captchaKey);
		effectToggleKey = new KeyMapping(ASPECT_EFFECT_TOGGLE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, CATEGORY);
		ClientRegistry.registerKeyBinding(effectToggleKey);
		sylladexKey = new KeyMapping(SYLLADEX, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
		ClientRegistry.registerKeyBinding(sylladexKey);
	}
	
	@SubscribeEvent
	public static void guiKeyInput(ScreenEvent.KeyboardKeyPressedEvent.Post event)
	{
		InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
		
		if(captchaKey.isActiveAndMatches(input) && Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>)
		{
			captchalogueInGui((AbstractContainerScreen<?>) Minecraft.getInstance().screen);
			event.setCanceled(true);
		}
	}
	
	private static boolean isNotRelease(InputEvent.KeyInputEvent event)
	{
		return event.getAction() != 0;
	}
	
	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event)	//This is only called during the game, when no gui is active
	{
		if(isNotRelease(event) && Minecraft.getInstance().screen == null)
		{
			InputConstants.Key input = InputConstants.getKey(event.getKey(), event.getScanCode());
			
			if(statKey.isActiveAndMatches(input))
				PlayerStatsScreen.openGui(false);
			
			if(editKey.isActiveAndMatches(input))
				ClientEditHandler.onKeyPressed();
			
			if(captchaKey.isActiveAndMatches(input))
				captchalogueInGame();
			
			if(effectToggleKey.isActiveAndMatches(input))
				MSPacketHandler.sendToServer(new EffectTogglePacket());
			
			if(sylladexKey.isActiveAndMatches(input) && ClientPlayerData.getModus() != null)
				MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus());
		}
		
	}
	
	private static void captchalogueInGame()
	{
		if(!Minecraft.getInstance().player.getMainHandItem().isEmpty())
			MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
	}
	
	private static void captchalogueInGui(AbstractContainerScreen<?> screen)
	{
		if(!(screen instanceof CreativeModeInventoryScreen))
		{
			Slot slot = screen.getSlotUnderMouse();
			if(slot != null && slot.hasItem())
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.index, screen.getMenu().containerId));
		}
	}
}
