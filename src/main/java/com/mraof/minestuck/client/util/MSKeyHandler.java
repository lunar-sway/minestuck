package com.mraof.minestuck.client.util;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.computer.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.EffectTogglePacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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
	public static final String ASPECT_EFFECT_TOGGLE = "key.minestuck.aspext_effect_toggle";
	public static final String SYLLADEX = "key.minestuck.sylladex";
	
	public static KeyBinding statKey;
	public static KeyBinding editKey;
	public static KeyBinding captchaKey;
	public static KeyBinding effectToggleKey;
	public static KeyBinding sylladexKey;
	static boolean captchaKeyPressed = false;
	
	public static void registerKeys()
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
	public static void onKeyInput(InputEvent event)	//This is only called during the game, when no gui is active
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
			if(!Minecraft.getInstance().player.getHeldItemMainhand().isEmpty() && !ModList.get().isLoaded("cosmeticarmorreworked"))
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
			//Apparently when CosmeticArmorReloaded is active, the game no longer refuses to call onKeyInput when a gui is open, and so we need to make sure that a gui is not open.
			if(!Minecraft.getInstance().player.getHeldItemMainhand().isEmpty() && ModList.get().isLoaded("cosmeticarmorreworked") && !(Minecraft.getInstance().currentScreen instanceof ContainerScreen<?>))
				MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogue());
		}
		
		while(effectToggleKey.isPressed())
		{
			MSPacketHandler.sendToServer(new EffectTogglePacket());
		}
		
		while(sylladexKey.isPressed())
		{
			if(ClientPlayerData.getModus() != null)
				MSScreenFactories.displaySylladexScreen(ClientPlayerData.getModus());
		}
	}
	
	@SubscribeEvent
	public static void onTick(TickEvent.ClientTickEvent event)
	{

		if(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), captchaKey.getKey().getKeyCode()) && !captchaKeyPressed) {

			Screen screen = Minecraft.getInstance().currentScreen;

			//screen.getFocused() is not used when CAR is loaded because CosmeticArmorReworked sets it to a value when in the default normal inventory, thus not allowing anything to be captchalogued.
			//The extra getSlotUnderMouse() check is done to make sure that the value of getSlotUnderMouse() is not referenced while the mouse isn't hovering over a ContainerScreen. For some reason, if neither this check nor the getFocused() check are here, the game will crash when pressing C while the mouse is not hovering over the inventory gui.
			if(ModList.get().isLoaded("cosmeticarmorreworked")) {
				if (screen instanceof ContainerScreen<?> && ((ContainerScreen<?>) screen).getSlotUnderMouse() != null && !(screen instanceof CreativeScreen)) {
					Slot slot = ((ContainerScreen<?>) screen).getSlotUnderMouse();

					if (slot != null)
						MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.slotNumber, ((ContainerScreen<?>) screen).getContainer().windowId));
				}
				//For some reason, creative mode container treats slot numbers differently than every other inventory container, so this is done to ensure that the player can still captchalogue items from their hotbar even when in creative mode.
				//The creative mode container does some wonky reordering of the slotNumbers, so getSlotUnderMouse() doesn't work without these weird workarounds using getSlotIndex().
				else if (screen instanceof ContainerScreen<?> && ((ContainerScreen<?>) screen).getSlotUnderMouse() != null && (screen instanceof CreativeScreen)) {
					Slot slot = ((ContainerScreen<?>) screen).getSlotUnderMouse();

					if (slot != null)
						MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.getSlotIndex(), ((ContainerScreen<?>) screen).getContainer().windowId));
				}
			}
			else {
				if (screen instanceof ContainerScreen<?> && screen.getFocused() == null && !(screen instanceof CreativeScreen)) {
					Slot slot = ((ContainerScreen<?>) screen).getSlotUnderMouse();

					if (slot != null)
						MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.slotNumber, ((ContainerScreen<?>) screen).getContainer().windowId));
				}
				//For some reason, creative mode container treats slot numbers differently than every other inventory container, so this is done to ensure that the player can still captchalogue items from their hotbar even when in creative mode.
				//The creative mode container does some wonky reordering of the slotNumbers, so getSlotUnderMouse() doesn't work without these weird workarounds using getSlotIndex().
				else if (screen instanceof ContainerScreen<?> && screen.getFocused() == null && (screen instanceof CreativeScreen)) {
					Slot slot = ((ContainerScreen<?>) screen).getSlotUnderMouse();

					if (slot != null)
						MSPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(slot.getSlotIndex(), ((ContainerScreen<?>) screen).getContainer().windowId));
				}
			}
		}
		
		captchaKeyPressed = InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), captchaKey.getKey().getKeyCode());
	}
	
}
