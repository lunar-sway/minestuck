package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class LandAspects
{
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
}