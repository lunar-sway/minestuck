package com.mraof.minestuck.client.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.computer.editmode.ClientEditToolDrag;
import com.mraof.minestuck.computer.editmode.ClientEditmodeData;
import com.mraof.minestuck.computer.editmode.EditTools;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.network.ToggleAspectEffectsPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class MSKeyHandler
{
	public static final String CATEGORY = "key.categories.minestuck";
	public static final String STATS_GUI = "key.minestuck.stats_gui";
	public static final String CAPTCHALOGUE = "key.minestuck.captchalogue";
	public static final String ASPECT_EFFECT_TOGGLE = "key.minestuck.aspext_effect_toggle";
	public static final String SYLLADEX = "key.minestuck.sylladex";
	
	public static final String EXIT_EDIT_MODE = "key.minestuck.exit_edit_mode";
	public static final String SELECT_EDIT_MODE = "key.minestuck.select_edit_mode";
	public static final String ROTATE_SELECTION = "key.minestuck.rotate_selection";
	public static final String MOVE_SELECTION = "key.minestuck.move_selection";
	public static final String COPY_SELECTION = "key.minestuck.copy_selection";
	
	public static KeyMapping selectKey;
	public static KeyMapping rotateKey;
	public static KeyMapping moveKey;
	public static KeyMapping copyKey;
	
	public static KeyMapping statKey;
	public static KeyMapping editKey;
	public static KeyMapping captchaKey;
	public static KeyMapping effectToggleKey;
	public static KeyMapping sylladexKey;
	
	public static void registerKeys(RegisterKeyMappingsEvent event)
	{
		if(statKey != null)
			throw new IllegalStateException("Minestuck keys have already been registered!");
		
		statKey = new KeyMapping(STATS_GUI, GLFW.GLFW_KEY_G, CATEGORY);
		event.register(statKey);
		
		selectKey = new KeyMapping(SELECT_EDIT_MODE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, CATEGORY);
		event.register(selectKey);
		rotateKey = new KeyMapping(ROTATE_SELECTION, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, CATEGORY);
		event.register(rotateKey);
		moveKey = new KeyMapping(MOVE_SELECTION, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_T, CATEGORY);
		event.register(moveKey);
		copyKey = new KeyMapping(COPY_SELECTION, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Y, CATEGORY);
		event.register(copyKey);
		
		editKey = new KeyMapping(EXIT_EDIT_MODE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);
		event.register(editKey);
		captchaKey = new KeyMapping(CAPTCHALOGUE, GLFW.GLFW_KEY_V, CATEGORY);
		event.register(captchaKey);
		effectToggleKey = new KeyMapping(ASPECT_EFFECT_TOGGLE, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, CATEGORY);
		event.register(effectToggleKey);
		sylladexKey = new KeyMapping(SYLLADEX, GLFW.GLFW_KEY_UNKNOWN, CATEGORY);
		event.register(sylladexKey);
	}
	
	@SubscribeEvent
	public static void guiKeyInput(ScreenEvent.KeyPressed.Post event)
	{
		InputConstants.Key input = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
		
		if(captchaKey.isActiveAndMatches(input) && Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>)
		{
			captchalogueInGui((AbstractContainerScreen<?>) Minecraft.getInstance().screen);
			event.setCanceled(true);
		}
	}
	
	private static boolean isNotRelease(InputEvent.Key event)
	{
		return event.getAction() != 0;
	}
	
	@SubscribeEvent
	public static void onKeyInput(InputEvent.Key event)    //This is only called during the game, when no gui is active
	{
		if(isNotRelease(event) && Minecraft.getInstance().screen == null)
		{
			InputConstants.Key input = InputConstants.getKey(event.getKey(), event.getScanCode());
			
			if(statKey.isActiveAndMatches(input))
				PlayerStatsScreen.openGui(false);
			
			if(rotateKey.isActiveAndMatches(input) && ClientEditmodeData.isInEditmode())
				ClientEditToolDrag.cycleRotation();
			
			if(editKey.isActiveAndMatches(input))
				ClientEditHandler.onKeyPressed();
			
			if(captchaKey.isActiveAndMatches(input))
				captchalogueInGame();
			
			if(effectToggleKey.isActiveAndMatches(input))
				PacketDistributor.sendToServer(new ToggleAspectEffectsPacket());
			
			if(sylladexKey.isActiveAndMatches(input) && ClientPlayerData.getModus() != null)
				PlayerStatsScreen.openGui(false);
		}
		
	}
	
	private static void captchalogueInGame()
	{
		if(!Minecraft.getInstance().player.getMainHandItem().isEmpty())
			PacketDistributor.sendToServer(new CaptchaDeckPackets.CaptchalogueHeldItem());
	}
	
	private static void captchalogueInGui(AbstractContainerScreen<?> screen)
	{
		if(!(screen instanceof CreativeModeInventoryScreen))
		{
			Slot slot = screen.getSlotUnderMouse();
			if(slot != null && slot.hasItem())
				PacketDistributor.sendToServer(new CaptchaDeckPackets.CaptchalogueInventorySlot(slot.index, screen.getMenu().containerId));
		}
	}
	
	@SubscribeEvent
	public static void onScroll(net.neoforged.neoforge.client.event.InputEvent.MouseScrollingEvent event)
	{
		if(!(moveKey.isDown() || copyKey.isDown()) || !ClientEditmodeData.isInEditmode())
			return;
		
		Player player = Minecraft.getInstance().player;
		if(player == null)
			return;
		
		EditTools cap = player.getData(MSAttachments.EDIT_TOOLS);
		double newDistance = net.minecraft.util.Mth.clamp(cap.getPreviewDistance() + event.getScrollDeltaY() * 1.5, 1.0, 64.0);
		cap.setPreviewDistance(newDistance);
		
		event.setCanceled(true); //dont also scroll the hotbar while adjusting placement distance
	}
}
