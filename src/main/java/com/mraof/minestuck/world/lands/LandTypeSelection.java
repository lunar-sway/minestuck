package com.mraof.minestuck.world.lands;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.tags.TagKey;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public final class LandTypeSelection
{
	private static List<LandsSupplier<TerrainLandType>> terrainList;
	
	@SubscribeEvent
	public static void onServerStart(ServerStartingEvent event)
	{
		ImmutableList.Builder<LandsSupplier<TerrainLandType>> builder = ImmutableList.builder();
		builder.add(of(MSTags.TerrainLandTypes.FOREST));
		builder.add(of(LandTypes.FROST));
		builder.add(of(LandTypes.FUNGI));
		builder.add(of(LandTypes.HEAT));
		builder.add(of(MSTags.TerrainLandTypes.ROCK));
		builder.add(of(MSTags.TerrainLandTypes.SAND));
		builder.add(of(MSTags.TerrainLandTypes.SANDSTONE));
		builder.add(of(LandTypes.SHADE));
		builder.add(of(LandTypes.WOOD));
		builder.add(of(LandTypes.RAINBOW));
		builder.add(of(LandTypes.FLORA));
		builder.add(of(LandTypes.END));
		terrainList = builder.build();
	}
	
	public static Collection<List<TerrainLandType>> terrainAlternatives()
	{
		return terrainList.stream().map(LandsSupplier::get).toList();
	}
	
	private static <A> LandsSupplier<A> of(Supplier<A> landType)
	{
		return new LandList<>(Collections.singletonList(landType.get()));
	}
	
	private static <A> LandsSupplier<A> of(TagKey<A> tag)
	{
		return new LandTag<>(tag);
	}
	
	private interface LandsSupplier<A>
	{
		List<A> get();
	}
	
	private record LandList<A>(List<A> landTypes) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			return this.landTypes;
		}
	}
	
	private record LandTag<A>(TagKey<A> tag) implements LandsSupplier<A>
	{
		@Override
		public List<A> get()
		{
			ForgeRegistry<A> registry;
			if(tag.registry().equals(LandTypes.TERRAIN_KEY))
				registry = (ForgeRegistry<A>) LandTypes.TERRAIN_REGISTRY.get();
			else
				throw new RuntimeException("Has tag for unhandled registry: " + this.tag.registry());
			
			return Objects.requireNonNull(registry.tags()).getTag(this.tag).stream().toList();
		}
	}
}
