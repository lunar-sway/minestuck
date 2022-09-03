package com.mraof.minestuck.world.lands;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.gen.LandChunkGenerator;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public final class LandTypePair
{
	public static final Codec<LandTypePair> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TerrainLandType.CODEC.fieldOf("terrain").forGetter(LandTypePair::getTerrain),
			TitleLandType.CODEC.fieldOf("title").forGetter(LandTypePair::getTitle)).apply(instance, LandTypePair::new));
	
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
		nbt.putString("terrain_aspect", terrain.getRegistryName().toString());
		nbt.putString("title_aspect", title.getRegistryName().toString());
		return nbt;
	}
	
	public static LandTypePair read(CompoundTag nbt)
	{
		String terrainName = nbt.getString("terrain_aspect");
		String titleName = nbt.getString("title_aspect");
		TerrainLandType terrain = LandTypes.TERRAIN_REGISTRY.getValue(new ResourceLocation(terrainName));
		TitleLandType title = LandTypes.TITLE_REGISTRY.getValue(new ResourceLocation(titleName));
		Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
		Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
		
		return new LandTypePair(terrain, title);
	}
	
	public static LandTypePair getTypes(ChunkGenerator generator)
	{
		if (generator instanceof LandChunkGenerator)
			return ((LandChunkGenerator) generator).namedTypes.landTypes();
		else return new LandTypePair(LandTypes.TERRAIN_NULL, LandTypes.TITLE_NULL);
	}
	
	public LazyInstance createLazy()
	{
		return new LazyInstance(this.terrain.getRegistryName(), this.title.getRegistryName());
	}
	
	public static class LazyInstance
	{
		private LazyInstance(ResourceLocation terrainAspect, ResourceLocation titleAspect)
		{
			terrainName = Objects.requireNonNull(terrainAspect);
			titleName = Objects.requireNonNull(titleAspect);
		}
		
		@Nonnull
		public final ResourceLocation terrainName;
		@Nonnull
		public final ResourceLocation titleName;
		
		public LandTypePair create()
		{
			TerrainLandType terrain = LandTypes.TERRAIN_REGISTRY.getValue(terrainName);
			TitleLandType title = LandTypes.TITLE_REGISTRY.getValue(titleName);
			Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
			Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
			
			return new LandTypePair(terrain, title);
		}
		
		public CompoundTag write(CompoundTag nbt)
		{
			nbt.putString("terrain_aspect", terrainName.toString());
			nbt.putString("title_aspect", titleName.toString());
			return nbt;
		}
		
		public static LazyInstance read(CompoundTag nbt)
		{
			String terrainName = nbt.getString("terrain_aspect");
			String titleName = nbt.getString("title_aspect");
			
			return new LazyInstance(new ResourceLocation(terrainName), new ResourceLocation(titleName));
		}
		
		public void write(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(terrainName);
			buffer.writeResourceLocation(titleName);
		}
		
		public static LazyInstance read(FriendlyByteBuf buffer)
		{
			ResourceLocation terrain = buffer.readResourceLocation();
			ResourceLocation title = buffer.readResourceLocation();
			return new LazyInstance(terrain, title);
		}
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
				return new TranslatableComponent("land." + loopingGet(landTypes.getTerrain().getNames(), terrainNameIndex));
			else return new TranslatableComponent("land." +  loopingGet(landTypes.getTitle().getNames(), titleNameIndex));
		}
		
		public Component asComponent()
		{
			return new TranslatableComponent(LandTypePair.FORMAT, landName(true), landName(false));
		}
		
		private static String loopingGet(String[] names, int index)
		{
			return names[Math.floorMod(index, names.length)];
		}
	}
	
	public Named createNamedRandomly(Random random)
	{
		return new Named(this, random.nextBoolean(),
				random.nextInt(this.terrain.getNames().length), random.nextInt(this.title.getNames().length));
	}
}