package com.mraof.minestuck.client.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.network.*;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class MSKeyHandler
{
	public static final String CATEGORY = "key.categories.minestuck";
	public static final String STATS_GUI = "key.minestuck.stats_gui";
	public static final String EXIT_EDIT_MODE = "key.minestuck.exit_edit_mode";
	public static final String CAPTCHALOGUE = "key.minestuck.captchalogue";
	public static final String USER_ASPECT_EFFECT = "key.minestuck.user_aspect_power";
	public static final String POSITIVE_TARGET_ASPECT_EFFECT = "key.minestuck.positive_target_aspect_power";
	public static final String NEGATIVE_TARGET_ASPECT_EFFECT = "key.minestuck.negative_target_aspect_power";
	public static final String PASSIVE_ASPECT_EFFECT_TOGGLE = "key.minestuck.passive_aspect_power_toggle";
	public static final String SYLLADEX = "key.minestuck.sylladex";
	
	public static KeyBinding statKey;
	public static KeyBinding editKey;
	public static KeyBinding captchaKey;
	public static KeyBinding userPowerKey;
	public static KeyBinding positiveTargetPowerKey;
	public static KeyBinding negativeTargetPowerKey;
	public static KeyBinding passivePowerToggleKey;
	public static KeyBinding sylladexKey;
	
	public static void registerKeys()
	{
		if(statKey != null)
			throw new IllegalStateException("Minestuck keys have already been registered!");
		
		statKey = new KeyBinding(STATS_GUI, GLFW.GLFW_KEY_G, CATEGORY);
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding(EXIT_EDIT_MODE, KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding(CAPTCHALOGUE, GLFW.GLFW_KEY_V, CATEGORY);
		ClientRegistry.registerKeyBinding(captchaKey);
		userPowerKey = new KeyBinding(USER_ASPECT_EFFECT, GLFW.GLFW_KEY_U, CATEGORY);
		ClientRegistry.registerKeyBinding(userPowerKey);
		positiveTargetPowerKey = new KeyBinding(POSITIVE_TARGET_ASPECT_EFFECT, -1, CATEGORY);
		ClientRegistry.registerKeyBinding(positiveTargetPowerKey);
		negativeTargetPowerKey = new KeyBinding(NEGATIVE_TARGET_ASPECT_EFFECT, -1, CATEGORY);
		ClientRegistry.registerKeyBinding(negativeTargetPowerKey);
		passivePowerToggleKey = new KeyBinding(PASSIVE_ASPECT_EFFECT_TOGGLE, -1, CATEGORY);
		ClientRegistry.registerKeyBinding(passivePowerToggleKey);
		
		sylladexKey = new KeyBinding(SYLLADEX, -1, CATEGORY);
		ClientRegistry.registerKeyBinding(sylladexKey);
	}
	
	@SubscribeEvent
	public static void guiKeyInput(GuiScreenEvent.KeyboardKeyPressedEvent.Post event)
	{
		InputMappings.Input input = InputMappings.getInputByCode(event.getKeyCode(), event.getScanCode());
		
		if(captchaKey.isActiveAndMatches(input) && Minecraft.getInstance().currentScreen instanceof ContainerScreen<?>)
		{
			captchalogueInGui((ContainerScreen<?>) Minecraft.getInstance().currentScreen);
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
		if(isNotRelease(event) && Minecraft.getInstance().currentScreen == null)
		{
			InputMappings.Input input = InputMappings.getInputByCode(event.getKey(), event.getScanCode());
			
			if(statKey.isActiveAndMatches(input))
				PlayerStatsScreen.openGui(false);
			
			if(editKey.isActiveAndMatches(input))
				ClientEditHandler.onKeyPressed();
			
			if(captchaKey.isActiveAndMatches(input))
				captchalogueInGame();
			
			if(userPowerKey.isActiveAndMatches(input))
				MSPacketHandler.sendToServer(new UserEffectPacket());
			
			if(positiveTargetPowerKey.isActiveAndMatches(input))
				MSPacketHandler.sendToServer(new PositiveOtherEffectPacket());
			
			if(negativeTargetPowerKey.isActiveAndMatches(input))
				MSPacketHandler.sendToServer(new NegativeOtherEffectPacket());
			
			if(passivePowerToggleKey.isActiveAndMatches(input))
				MSPacketHandler.sendToServer(new PassiveEffectTogglePacket());
			
			if(sylladexKey.isActiveAndMatches(input) && ClientPlayerData.getModus() != null)
				MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus());
		}
		
	}
	
	private static void captchalogueInGame()
	{
		if(!Minecraft.getInstance().player.getHeldItemMainhand().isEmpty())
			MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
	}
	
	private static void captchalogueInGui(ContainerScreen<?> screen)
	{
		if(!(screen instanceof CreativeScreen))
		{
			Slot slot = screen.getSlotUnderMouse();
			if(slot != null && slot.getHasStack())
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.slotNumber, screen.getContainer().windowId));
		}
	}
}
