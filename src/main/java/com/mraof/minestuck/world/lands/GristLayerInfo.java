package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Containers for grist layers.
 * Grist layers need the base grist type, which is kept in the {@link com.mraof.minestuck.skaianet.SburbConnection},
 * and the world seed, which is kept in {@link com.mraof.minestuck.world.gen.LandChunkGenerator}.
 * The sburb connection is created before the chunk generator, meaning that the layers can't be created with the connection as the seed wouldn't be available.
 * When the chunk generator is created, it lacks the information needed to find the related sburb connection, meaning that the layers can't be created in the chunk generator.
 * As such, this approach is needed until we get a more sane data structure.
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
			
			long seed = ((LandChunkGenerator) generator).getSeed();
			
			GristType baseType;
			SburbConnection connection = SburbHandler.getConnectionForDimension(level.getServer(), level.dimension());
			if (connection != null)
				baseType = connection.getBaseGrist();
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
		commonGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.COMMON, 0, seed, 10, null);
		anyGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.ANY, 1, seed, 8, baseType);
		uncommonGristLayer = GristTypeLayer.createLayer(GristType.SpawnCategory.UNCOMMON, 2, seed, 7, baseType);
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
	
	private GristTypeLayer randomLayer(Random rand)
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
		
		return new TranslatableComponent(INFO, commonType.getDisplayName(), uncommonType.getDisplayName(), anyType.getDisplayName());
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