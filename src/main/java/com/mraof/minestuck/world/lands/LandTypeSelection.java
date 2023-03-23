package com.mraof.minestuck.world.lands;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber
public final class LandTypeSelection
{
	private static List<List<TerrainLandType>> terrainList;
	
	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event)
	{
		ImmutableList.Builder<List<TerrainLandType>> builder = ImmutableList.builder();
		builder.add(List.of(LandTypes.FOREST.get(), LandTypes.TAIGA.get()));
		builder.add(List.of(LandTypes.FROST.get()));
		builder.add(List.of(LandTypes.FUNGI.get()));
		builder.add(List.of(LandTypes.HEAT.get()));
		builder.add(List.of(LandTypes.ROCK.get(), LandTypes.PETRIFICATION.get()));
		builder.add(List.of(LandTypes.SAND.get(), LandTypes.RED_SAND.get(), LandTypes.LUSH_DESERTS.get()));
		builder.add(List.of(LandTypes.SANDSTONE.get(), LandTypes.RED_SANDSTONE.get()));
		builder.add(List.of(LandTypes.SHADE.get()));
		builder.add(List.of(LandTypes.WOOD.get()));
		builder.add(List.of(LandTypes.RAINBOW.get()));
		builder.add(List.of(LandTypes.FLORA.get()));
		builder.add(List.of(LandTypes.END.get()));
		terrainList = builder.build();
	}
	
	public static Collection<List<TerrainLandType>> terrainAlternatives()
	{
		return terrainList;
	}
}
