package com.mraof.minestuck.world.lands;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LandTypePair
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
	
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putString("terrain_aspect", terrain.getRegistryName().toString());
		nbt.putString("title_aspect", title.getRegistryName().toString());
		return nbt;
	}
	
	public static LandTypePair read(CompoundNBT nbt)
	{
		String terrainName = nbt.getString("terrain_aspect");
		String titleName = nbt.getString("title_aspect");
		TerrainLandType terrain = LandTypes.TERRAIN_REGISTRY.getValue(new ResourceLocation(terrainName));
		TitleLandType title = LandTypes.TITLE_REGISTRY.getValue(new ResourceLocation(titleName));
		Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
		Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
		
		return new LandTypePair(terrain, title);
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
		
		public CompoundNBT write(CompoundNBT nbt)
		{
			nbt.putString("terrain_aspect", terrainName.toString());
			nbt.putString("title_aspect", titleName.toString());
			return nbt;
		}
		
		public static LazyInstance read(CompoundNBT nbt)
		{
			String terrainName = nbt.getString("terrain_aspect");
			String titleName = nbt.getString("title_aspect");
			
			return new LazyInstance(new ResourceLocation(terrainName), new ResourceLocation(titleName));
		}
		
		public void write(PacketBuffer buffer)
		{
			buffer.writeResourceLocation(terrainName);
			buffer.writeResourceLocation(titleName);
		}
		
		public static LazyInstance read(PacketBuffer buffer)
		{
			ResourceLocation terrain = buffer.readResourceLocation();
			ResourceLocation title = buffer.readResourceLocation();
			return new LazyInstance(terrain, title);
		}
	}
}