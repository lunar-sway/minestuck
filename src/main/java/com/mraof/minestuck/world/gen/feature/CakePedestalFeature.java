package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class CakePedestalFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_CAKE_PEDESTAL = new ResourceLocation(Minestuck.MOD_ID, "cake_pedestal");
	
	public CakePedestalFeature(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		TemplateManager templates = world.getLevel().getStructureManager();
		Template template = templates.getOrCreate(STRUCTURE_CAKE_PEDESTAL);
		
		PlacementSettings settings = new PlacementSettings().setChunkPos(new ChunkPos(pos)).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(generator)));
		
		BlockPos size = template.getSize();
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getX());
		
		int yMin = Integer.MAX_VALUE;
		for(BlockPos floorPos : BlockPos.betweenClosed(pos, pos.offset(xOffset, 0, zOffset)))
			yMin = Math.min(yMin, world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, floorPos).getY());
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, yMin, pos.getZ() + zOffset), Mirror.NONE, Rotation.NONE);
		template.placeInWorld(world, structurePos, structurePos, settings, rand, Constants.BlockFlags.NO_RERENDER);
		
		return true;
	}
}
