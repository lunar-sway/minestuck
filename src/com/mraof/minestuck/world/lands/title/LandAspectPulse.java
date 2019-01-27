package com.mraof.minestuck.world.lands.title;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.lands.decorator.PillarDecorator;
import com.mraof.minestuck.world.lands.decorator.SurfaceDecoratorVein;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import com.mraof.minestuck.world.lands.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.Vec3d;

public class LandAspectPulse extends TitleLandAspect
{

	@Override
	public String getPrimaryName()
	{
		return "pulse";
	}

	@Override
	public String[] getNames()
	{
		return new String[]{"pulse", "blood"};
	}
	
	@Override
	public void prepareWorldProvider(WorldProviderLands worldProvider)
	{
		worldProvider.mergeFogColor(new Vec3d(0.8, 0, 0), 0.8F);
	}
	
	@Override
	public void prepareChunkProvider(ChunkProviderLands chunkProvider)
	{
	
	}
	
	@Override
	public void prepareChunkProviderServer(ChunkProviderLands chunkProvider)
	{
		chunkProvider.blockRegistry.setBlockState("structure_wool_2", Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED));
		chunkProvider.blockRegistry.setBlockState("carpet", Blocks.CARPET.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN));
		chunkProvider.oceanChance = Math.max(chunkProvider.oceanChance, 0.2F);
		
		chunkProvider.blockRegistry.setBlockState("ocean", MinestuckBlocks.blockBlood.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("river", MinestuckBlocks.blockBlood.getDefaultState());
		chunkProvider.blockRegistry.setBlockState("slime", MinestuckBlocks.coagulatedBlood.getDefaultState());
		
		chunkProvider.decorators.add(new SurfaceDecoratorVein(MinestuckBlocks.coagulatedBlood.getDefaultState(), 25, 30, BiomeMinestuck.mediumRough));
	}
	
	@Override
	public boolean isAspectCompatible(TerrainLandAspect aspect)
	{
		StructureBlockRegistry registry = new StructureBlockRegistry();
		aspect.registerBlocks(registry);
		return registry.getBlockState("ocean").getMaterial() != Material.LAVA;	//Lava is likely a too important part of the terrain aspect to be replaced
	}
	
}