package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.EditmodeSettingsScreen;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.network.editmode.EditmodeLocationsPacket;
import com.mraof.minestuck.network.editmode.ServerEditPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientEditmodeData
{
	public static final String ENTERED = "minestuck.editmode.entered";
	
	private static boolean activated;
	@Nullable
	private static EditmodeLocations locations;
	@Nullable
	private static ResourceKey<Level> clientLand;
	
	public static boolean isInEditmode()
	{
		return activated;
	}
	
	@Nullable
	public static EditmodeLocations getLocations()
	{
		return locations;
	}
	
	@Nullable
	public static ResourceKey<Level> getClientLand()
	{
		return clientLand;
	}
	
	private static void disable()
	{
		activated = false;
		locations = null;
		clientLand = null;
	}
	
	public static void onActivatePacket()
	{
		Player player = Minecraft.getInstance().player;
		if(player != null)
			player.sendSystemMessage(Component.translatable(ENTERED, MSKeyHandler.editKey.getTranslatedKeyMessage()));
		activated = true;
	}
	
	public static void onExitPacket(ServerEditPackets.Exit ignored)
	{
		Player player = Minecraft.getInstance().player;
		if(player != null)
			player.fallDistance = 0;
		disable();
	}
	
	public static void onLocationsPacket(EditmodeLocationsPacket packet)
	{
		locations = packet.locations();
		clientLand = packet.land().orElse(null);
		if(Minecraft.getInstance().screen instanceof EditmodeSettingsScreen screen)
			screen.recreateTeleportButtons();
	}
	
	@SubscribeEvent
	public static void onWorldUnload(LevelEvent.Unload event)
	{
		if(event.getLevel().isClientSide())
			disable();
	}
}
