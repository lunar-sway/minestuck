package com.mraof.minestuck.world;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.skaianet.SkaianetHandler;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.registries.ClearableRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.FORGE)
public class MSDimensions
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation SKAIA_ID = new ResourceLocation(Minestuck.MOD_ID, "skaia");
	
	public static DimensionType skaiaDimension;
	
	/**
	 * On server init, this function is called to register dimensions.
	 * The dimensions registered will then be sent and registered by forge client-side.
	 */
	@SubscribeEvent
	public static void registerDimensionTypes(final RegisterDimensionsEvent event)
	{
		//register dimensions
		skaiaDimension = DimensionManager.registerOrGetDimension(SKAIA_ID, MSDimensionTypes.SKAIA, null, true);
	}
	
	@SubscribeEvent
	public static void serverStopped(final FMLServerStoppedEvent event)
	{
		if(!event.getServer().isDedicatedServer())
		{
			LOGGER.warn("Unregistering land dimensions unsafely. If the dimension type registry is messed up after this, blame minestuck.");
			ClearableRegistry<DimensionType> registry = (ClearableRegistry<DimensionType>) DimensionManager.getRegistry();
			Map<ResourceLocation, DimensionType> dimensionsToKeep = registry.stream().filter(dimensionType -> !isLandDimension(dimensionType)).collect(Collectors.toMap(registry::getKey, v -> v));
			registry.clear();
			for(Map.Entry<ResourceLocation, DimensionType> entry : dimensionsToKeep.entrySet())
			{
				LOGGER.debug("Re-Registering non-land dimension ID: {} Name: {} Value: {}", entry.getValue().getId() + 1, entry.getKey().toString(), entry.getValue().toString());
				registry.register(entry.getValue().getId() + 1, entry.getKey(), entry.getValue());
			}
		}
	}
	
	public static LandTypePair getAspects(MinecraftServer server, DimensionType dimension)
	{
		LandInfo info = getLandInfo(server, dimension);
		if(info != null)
			return info.getLandAspects();
		else if(isLandDimension(dimension))
		{
			LOGGER.warn("Tried to get land aspects for {}, but did not find a container reference! Using defaults instead.", dimension.getRegistryName());
			return new LandTypePair(LandTypes.TERRAIN_NULL, LandTypes.TITLE_NULL);
		} else return null;
	}
	
	public static LandInfo getLandInfo(World world)
	{
		return getLandInfo(world.getServer(), world.getDimension().getType());
	}
	
	public static LandInfo getLandInfo(MinecraftServer server, DimensionType dimension)
	{
		return SkaianetHandler.get(server).landInfoForDimension(dimension);
	}
	
	public static boolean isLandDimension(DimensionType dimension)
	{
		return dimension != null && dimension.getModType() == MSDimensionTypes.LANDS;
	}
	
	public static boolean isSkaia(DimensionType dimension)
	{
		return dimension != null && dimension.getModType() == MSDimensionTypes.SKAIA;
	}
}