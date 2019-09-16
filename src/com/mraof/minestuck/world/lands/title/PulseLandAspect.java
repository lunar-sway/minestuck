package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.gen.LandGenSettings;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

public class PulseLandAspect extends TitleLandAspect
{
	public PulseLandAspect()
	{
		super(EnumAspect.BLOOD);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	public void registerBlocks(StructureBlockRegistry registry)
	{
		registry.setBlockState("structure_wool_2", Blocks.RED_WOOL.getDefaultState());
		registry.setBlockState("carpet", Blocks.BROWN_CARPET.getDefaultState());
		
		registry.setBlockState("ocean", MSBlocks.BLOOD.getDefaultState());
		registry.setBlockState("river", MSBlocks.BLOOD.getDefaultState());
		registry.setBlockState("slime", MSBlocks.COAGULATED_BLOOD.getDefaultState());
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public void setGenSettings(LandGenSettings settings)
	{
		settings.oceanChance = Math.max(settings.oceanChance, 0.2F);
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		
		chunkProvider.decorators.add(new SurfaceDecoratorVein(MSBlocks.COAGULATED_BLOOD.getDefaultState(), 25, 30, MSBiomes.mediumRough));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}