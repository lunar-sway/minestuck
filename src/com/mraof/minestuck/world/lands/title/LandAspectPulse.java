package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.util.EnumAspect;
import com.mraof.minestuck.world.lands.LandDimension;
import com.mraof.minestuck.world.biome.ModBiomes;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Vec3d;

public class LandAspectPulse extends TitleLandAspect
{
	public LandAspectPulse()
	{
		super(null, EnumAspect.BLOOD);
	}
	
	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	public void prepareWorldProvider(LandDimension worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	//@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.RED_WOOL.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.BROWN_CARPET.getDefaultState());
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		/*chunkProvider.blockRegistry.setBlockState("ocean", MinestuckBlocks.blockBlood.getDefaultState());TODO
		chunkProvider.blockRegistry.setBlockState("river", MinestuckBlocks.blockBlood.getDefaultState());*/
		chunkProvider.blockRegistry.setBlockState("slime", MinestuckBlocks.COAGULATED_BLOOD.getDefaultState());
		
		chunkProvider.decorators.add(new SurfaceDecoratorVein(MinestuckBlocks.COAGULATED_BLOOD.getDefaultState(), 25, 30, ModBiomes.mediumRough));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}