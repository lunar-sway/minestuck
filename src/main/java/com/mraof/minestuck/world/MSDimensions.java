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
	
	public static ResourceKey<Level> SKAIA = ResourceKey.create(Registries.DIMENSION, Minestuck.id("skaia"));
	public static ResourceKey<Level> PROSPIT = ResourceKey.create(Registries.DIMENSION,  Minestuck.id("prospit"));
	public static ResourceKey<Level> DERSE = ResourceKey.create(Registries.DIMENSION,  Minestuck.id("derse"));
	public static ResourceKey<Level> VEIL = ResourceKey.create(Registries.DIMENSION, Minestuck.id("veil"));
	
	public static final ResourceLocation LAND_EFFECTS =  Minestuck.id("land");
	public static final ResourceLocation PROSPIT_EFFECTS =  Minestuck.id("prospit_effects");
	public static final ResourceLocation DERSE_EFFECTS =  Minestuck.id("derse_effects");
	public static final ResourceLocation VEIL_EFFECTS =  Minestuck.id("veil_effects");
	
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
	
	public static boolean isProspit(ResourceKey<Level> dimension)
	{
		return dimension == PROSPIT;
	}
	
	public static boolean isDerse(ResourceKey<Level> dimension)
	{
		return dimension == DERSE;
	}
	
	public static boolean isVeil(ResourceKey<Level> dimension)
	{
		return dimension == VEIL;
	}
	
	public static boolean isInMedium(MinecraftServer server, ResourceKey<Level> dimension)
	{
		return isLandDimension(server, dimension) || isSkaia(dimension) || isVeil(dimension);
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
