package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

public class CakePedestalFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_CAKE_PEDESTAL = new ResourceLocation(Minestuck.MOD_ID, "cake_pedestal");
	
	public CakePedestalFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		TemplateManager templates = ((ServerWorld) worldIn.getWorld()).getSaveHandler().getStructureTemplateManager();
		Template template = templates.getTemplateDefaulted(STRUCTURE_CAKE_PEDESTAL);
		
		PlacementSettings settings = new PlacementSettings().setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.getSize();
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getX());
		
		int yMin = Integer.MAX_VALUE;
		for(BlockPos floorPos : BlockPos.getAllInBoxMutable(pos, pos.add(xOffset, 0, zOffset)))
			yMin = Math.min(yMin, worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, floorPos).getY());
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, yMin, pos.getZ() + zOffset), Mirror.NONE, Rotation.NONE);
		template.addBlocksToWorld(worldIn, structurePos, settings);
		
		return true;
	}
}
