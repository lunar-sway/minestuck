package com.mraof.minestuck.world.lands;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public final class LandTypePair
{
	public static final Codec<LandTypePair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TerrainLandType.CODEC.fieldOf("terrain").forGetter(LandTypePair::getTerrain),
			TitleLandType.CODEC.fieldOf("title").forGetter(LandTypePair::getTitle)).apply(instance, LandTypePair::new));
	
	/**
	 * Custom font made by Riotmode. Similar to Carima used for "Land of _ and _" in comic. Font size of 4 is smaller than default of 11
	 */
	public static final Style LAND_OF_COPYLEFT_AND_FREEDOM_FONT_STYLE = Style.EMPTY.withFont(new ResourceLocation("minestuck", "land_of_copyleft_and_freedom"));
	public static final String FORMAT = "land.format";
	
	public LandTypePair(TerrainLandType terrainType, TitleLandType titleType)
	{
		terrain = Objects.requireNonNull(terrainType);
		title = Objects.requireNonNull(titleType);
	}
	
	@Nonnull
	private final TerrainLandType terrain;
	@Nonnull
	private final TitleLandType title;
	
	@Nonnull
	public TerrainLandType getTerrain()
	{
		return terrain;
	}
	
	@Nonnull
	public TitleLandType getTitle()
	{
		return title;
	}
	
	public CompoundTag write(CompoundTag nbt)
	{
		nbt.putString("terrain_aspect", LandTypes.TERRAIN_REGISTRY.getKey(terrain).toString());
		nbt.putString("title_aspect", LandTypes.TITLE_REGISTRY.getKey(title).toString());
		return nbt;
	}
	
	public static LandTypePair read(CompoundTag nbt)
	{
		String terrainName = nbt.getString("terrain_aspect");
		String titleName = nbt.getString("title_aspect");
		TerrainLandType terrain = LandTypes.TERRAIN_REGISTRY.get(new ResourceLocation(terrainName));
		TitleLandType title = LandTypes.TITLE_REGISTRY.get(new ResourceLocation(titleName));
		Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
		Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
		
		return new LandTypePair(terrain, title);
	}
	
	public static Optional<LandTypePair> getTypes(MinecraftServer server, ResourceKey<Level> levelKey)
	{
		return getNamed(server, levelKey).map(Named::landTypes);
	}
	
	public static Optional<LandTypePair> getTypes(ServerLevel level)
	{
		return getNamed(level).map(Named::landTypes);
	}
	
	public static LandTypePair getTypesOrDefaulted(ChunkGenerator generator)
	{
		return getNamed(generator).map(Named::landTypes)
				.orElseGet(() -> new LandTypePair(LandTypes.TERRAIN_NULL.get(), LandTypes.TITLE_NULL.get()));
	}
	
	public static Optional<Named> getNamed(MinecraftServer server, ResourceKey<Level> levelKey)
	{
		return Optional.ofNullable(server.getLevel(levelKey)).flatMap(LandTypePair::getNamed);
	}
	
	public static Optional<Named> getNamed(ServerLevel level)
	{
		return getNamed(level.getChunkSource().getGenerator());
	}
	
	public static Optional<Named> getNamed(ChunkGenerator generator)
	{
		if(generator instanceof LandChunkGenerator chunkGenerator)
			return Optional.of(chunkGenerator.namedTypes);
		else
			return Optional.empty();
	}
	
	public record Named(LandTypePair landTypes, boolean useReverseOrder, int terrainNameIndex, int titleNameIndex)
	{
		public static final Codec<Named> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LandTypePair.CODEC.fieldOf("types").forGetter(Named::landTypes),
				Codec.BOOL.fieldOf("reverse").forGetter(Named::useReverseOrder),
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("terrain_name").forGetter(Named::terrainNameIndex),
				Codec.intRange(0, Integer.MAX_VALUE).fieldOf("title_name").forGetter(Named::titleNameIndex)
		).apply(instance, Named::new));
		
		private Component landName(boolean first)
		{
			if(first != useReverseOrder)
				return Component.translatable("land." + loopingGet(landTypes.getTerrain().getNames(), terrainNameIndex));
			else return Component.translatable("land." +  loopingGet(landTypes.getTitle().getNames(), titleNameIndex));
		}
		
		public MutableComponent asComponent()
		{
			return Component.translatable(LandTypePair.FORMAT, landName(true), landName(false));
		}
		
		public Component asComponentWithLandFont()
		{
			return asComponent().withStyle(LAND_OF_COPYLEFT_AND_FREEDOM_FONT_STYLE);
		}
		
		private static String loopingGet(String[] names, int index)
		{
			return names[Math.floorMod(index, names.length)];
		}
	}
	
	public Named createNamedRandomly(RandomSource random)
	{
		return new Named(this, random.nextBoolean(),
				random.nextInt(this.terrain.getNames().length), random.nextInt(this.title.getNames().length));
	}
}