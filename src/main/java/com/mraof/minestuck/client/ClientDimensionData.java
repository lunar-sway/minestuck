package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.LandTypesDataPacket;
import com.mraof.minestuck.world.lands.LandProperties;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientDimensionData
{
	private static final Map<ResourceKey<Level>, LandTypePair> landTypes = new HashMap<>();
	
	private static LandProperties properties;
	private static ResourceKey<Level> currentWorld;
	
	public static LandTypePair getLandTypes(ResourceKey<Level> level)
	{
		return landTypes.get(level);
	}
	
	public static boolean isLand(ResourceKey<Level> level)
	{
		return landTypes.containsKey(level);
	}
	
	public static LandProperties getProperties(ClientLevel level)
	{
		if (level == null)
			return null;
		
		ResourceKey<Level> key = level.dimension();
		if (currentWorld != key)
		{
			currentWorld = key;
			LandTypePair pair = getLandTypes(key);
			if (pair != null)
				properties = LandProperties.create(pair);
			else properties = null;
		}
		return properties;
	}
	
	@SubscribeEvent
	public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event)
	{
		landTypes.clear();
		currentWorld = null;
		properties = null;
	}
	
	public static void receivePacket(LandTypesDataPacket packet)
	{
		landTypes.clear();
		landTypes.putAll(packet.types());
	}
}
