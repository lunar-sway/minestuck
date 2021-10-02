package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.data.LandTypesDataPacket;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientDimensionData
{
	private static final Map<RegistryKey<World>, LandTypePair> landTypes = new HashMap<>();
	
	public static LandTypePair getLandTypes(RegistryKey<World> world)
	{
		return landTypes.get(world);
	}
	
	public static boolean isLand(RegistryKey<World> world)
	{
		return landTypes.containsKey(world);
	}
	
	@SubscribeEvent
	public static void onLogout(ClientPlayerNetworkEvent.LoggedOutEvent event)
	{
		landTypes.clear();
	}
	
	public static void receivePacket(LandTypesDataPacket packet)
	{
		landTypes.clear();
		landTypes.putAll(packet.getTypes());
	}
}
