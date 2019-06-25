package com.mraof.minestuck.client.settings;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.network.EffectTogglePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.mraof.minestuck.client.gui.playerStats.GuiPlayerStats;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;

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
		
		statKey = new KeyBinding("key.statsGui", 71, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(statKey);
		editKey = new KeyBinding("key.exitEdit", 75, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(editKey);
		captchaKey = new KeyBinding("key.captchalogue", 86, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(captchaKey);
		effectToggleKey = new KeyBinding("key.aspectEffectToggle", 43, "key.categories.minestuck");
		ClientRegistry.registerKeyBinding(effectToggleKey);
		sylladexKey = new KeyBinding("key.sylladex", -1, "key.categories.minestuck");
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
				Minecraft.getInstance().displayGuiScreen(CaptchaDeckHandler.clientSideModus.getGuiHandler());
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(InputMappings.isKeyDown(captchaKey.getKey().getKeyCode()) && !captchaKeyPressed) {

				//This statement is here because for some reason 'slotNumber' always returns as 0 if it is referenced inside the creative inventory.
			if (Minecraft.getInstance().currentScreen instanceof GuiContainerCreative && Minecraft.getInstance().player.openContainer instanceof GuiContainerCreative.ContainerCreative && ((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getSlotIndex()));
			else if(Minecraft.getInstance().currentScreen instanceof GuiContainer && ((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse() != null && ((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse().getHasStack())
				MinestuckPacketHandler.sendToServer(CaptchaDeckPacket.captchalogueInv(((GuiContainer)Minecraft.getInstance().currentScreen).getSlotUnderMouse().slotNumber));
		}
		
		captchaKeyPressed = InputMappings.isKeyDown(captchaKey.getKey().getKeyCode());
	}
	
}
