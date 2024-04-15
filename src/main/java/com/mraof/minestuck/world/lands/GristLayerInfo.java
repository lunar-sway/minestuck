package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypeSpawnCategory;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Container for grist layers.
 */
@Mod.EventBusSubscriber
public class GristLayerInfo
{
	public static final String INFO = "grist_layer.info";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<ResourceKey<Level>, GristLayerInfo> infoByWorldMap = new HashMap<>();
	
	@SubscribeEvent
	public static void serverStopped(ServerStoppedEvent event)
	{
		infoByWorldMap.clear();
	}
	
	private static Optional<GristLayerInfo> initAndGetGristLayer(ServerLevel level)
	{
		ChunkGenerator generator = level.getChunkSource().getGenerator();
		if (generator instanceof LandChunkGenerator) {
			
			long seed = level.getSeed();
			
			GristType baseType;
			Optional<SburbPlayerData> landData = SburbPlayerData.getForLand(level);
			if (landData.isPresent())
				baseType = landData.get().getBaseGrist();
			else
			{
				LOGGER.error("Unable to find sburb connection for land dimension \"{}\" when creating grist layers. Defaulting to amber base type.", level.dimension().location());
				baseType = GristTypes.AMBER.get();
			}
			
			GristLayerInfo info = new GristLayerInfo(seed, baseType);
			infoByWorldMap.put(level.dimension(), info);
			return Optional.of(info);
		} else
			return Optional.empty();
	}
	
	public static Optional<GristLayerInfo> get(ServerLevel world)
	{
		if (infoByWorldMap.containsKey(world.dimension()))
			return Optional.ofNullable(infoByWorldMap.get(world.dimension()));
		else
			return initAndGetGristLayer(world);
	}
	
	private final GristTypeLayer anyGristLayer, commonGristLayer, uncommonGristLayer;
	
	private GristLayerInfo(long seed, GristType baseType)
	{
		commonGristLayer = GristTypeLayer.createLayer(GristTypeSpawnCategory.COMMON, 0, seed, 10, null);
		anyGristLayer = GristTypeLayer.createLayer(GristTypeSpawnCategory.ANY, 1, seed, 8, baseType);
		uncommonGristLayer = GristTypeLayer.createLayer(GristTypeSpawnCategory.UNCOMMON, 2, seed, 7, baseType);
	}
	
	public GristType randomTypeFor(LivingEntity entity)
	{
		// In hardmode, any underling anywhere can get the artifact grist type at a 50% chance.
		if(MinestuckConfig.SERVER.hardMode.get() && entity.getRandom().nextBoolean()) {
			return GristTypes.ARTIFACT.get();
		} else {
			// Otherwise, the grist type is picked from a random grist layer.
			BlockPos pos = entity.blockPosition();
			return randomLayer(entity.getRandom()).getTypeAt(pos.getX(), pos.getZ());
		}
	}
	
	private GristTypeLayer randomLayer(RandomSource rand)
	{
		switch(rand.nextInt(3))
		{
			case 0:
				return commonGristLayer;
			case 1:
				return uncommonGristLayer;
			default:
				return anyGristLayer;
		}
	}
	
	public Component getGristLayerInfo(int x, int z)
	{
		GristType commonType = commonGristLayer.getTypeAt(x, z);
		GristType uncommonType = uncommonGristLayer.getTypeAt(x, z);
		GristType anyType = anyGristLayer.getTypeAt(x, z);
		
		return Component.translatable(INFO, commonType.getDisplayName(), uncommonType.getDisplayName(), anyType.getDisplayName());
	}
	
	public GristTypeLayer getCommonGristLayer()
	{
		return commonGristLayer;
	}
	
	public GristTypeLayer getUncommonGristLayer()
	{
		return uncommonGristLayer;
	}
	
	public GristTypeLayer getAnyGristLayer()
	{
		return anyGristLayer;
	}
}