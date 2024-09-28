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
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MSDimensions
{
	
	public static ResourceKey<Level> SKAIA = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(Minestuck.MOD_ID, "skaia"));
	public static final ResourceLocation LAND_EFFECTS = new ResourceLocation(Minestuck.MOD_ID, "land");
	
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
		PacketDistributor.ALL.noArg().send(createLandTypesPacket(server));
	}
	
	@SubscribeEvent
	private static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
	{
		ServerPlayer player = (ServerPlayer) event.getEntity();
		PacketDistributor.PLAYER.with(player).send(createLandTypesPacket(player.server));
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
