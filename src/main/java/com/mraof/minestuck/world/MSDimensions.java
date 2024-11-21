package com.mraof.minestuck.world;

import com.google.common.collect.ImmutableMap;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.network.LandTypesDataPacket;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class MSDimensions
{
	
	public static ResourceKey<Level> SKAIA = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "skaia"));
	public static final ResourceLocation LAND_EFFECTS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "land");
	
	public static boolean isLandDimension(MinecraftServer server, ResourceKey<Level> levelKey)
	{
		if(server != null)
			return LandTypePair.getNamed(server, levelKey).isPresent();
		else
			return false;
	}
	
	public static boolean isSkaia(ResourceKey<Level> dimension)
	{
		return dimension == SKAIA;
	}
	
	public static boolean isInMedium(MinecraftServer server, ResourceKey<Level> dimension)
	{
		return isLandDimension(server, dimension) || isSkaia(dimension);
	}
	
	public static void sendLandTypesToAll(MinecraftServer server)
	{
		PacketDistributor.sendToAllPlayers(createLandTypesPacket(server));
	}
	
	@SubscribeEvent
	private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PacketDistributor.sendToPlayer(player, createLandTypesPacket(player.server));
	}
	
	private static LandTypesDataPacket createLandTypesPacket(MinecraftServer server)
	{
		ImmutableMap.Builder<ResourceKey<Level>, LandTypePair> builder = new ImmutableMap.Builder<>();
		
		for(ResourceKey<Level> levelKey : server.levelKeys())
		{
			LandTypePair.getTypes(server, levelKey)
					.ifPresent(landTypes -> builder.put(levelKey, landTypes));
		}
		
		return new LandTypesDataPacket(builder.build());
	}
}
