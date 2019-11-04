package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class LandAspects
{
	public static final String FORMAT = "land.format";
	
	public LandAspects(TerrainLandAspect terrainAspect, TitleLandAspect titleAspect)
	{
		terrain = Objects.requireNonNull(terrainAspect);
		title = Objects.requireNonNull(titleAspect);
	}
	
	@Nonnull
	public final TerrainLandAspect terrain;
	@Nonnull
	public final TitleLandAspect title;
	
	public CompoundNBT write(CompoundNBT nbt)
	{
		nbt.putString("terrain_aspect", terrain.getRegistryName().toString());
		nbt.putString("title_aspect", title.getRegistryName().toString());
		return nbt;
	}
	
	public static LandAspects read(CompoundNBT nbt)
	{
		String terrainName = nbt.getString("terrain_aspect");
		String titleName = nbt.getString("title_aspect");
		TerrainLandAspect terrain = LandAspectRegistry.TERRAIN_REGISTRY.getValue(new ResourceLocation(terrainName));
		TitleLandAspect title = LandAspectRegistry.TITLE_REGISTRY.getValue(new ResourceLocation(titleName));
		Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
		Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
		
		return new LandAspects(terrain, title);
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
		
		public LandAspects create()
		{
			TerrainLandAspect terrain = LandAspectRegistry.TERRAIN_REGISTRY.getValue(terrainName);
			TitleLandAspect title = LandAspectRegistry.TITLE_REGISTRY.getValue(titleName);
			Objects.requireNonNull(terrain, "Could not find terrain land aspect by name " + terrainName);
			Objects.requireNonNull(title, "Could not find title land aspect by name " + titleName);
			
			return new LandAspects(terrain, title);
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