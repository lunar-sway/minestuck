package com.mraof.minestuck.world.gen.feature;

import com.mojang.datafixers.Dynamic;
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
import net.minecraft.world.gen.feature.template.*;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;
import java.util.function.Function;

/**
 * A version of the {@link net.minecraft.world.gen.feature.FossilsFeature}, but it can be spawned on the terrain surface reliably.
 */
public class SurfaceFossilsFeature extends Feature<NoFeatureConfig>
{
	private static final ResourceLocation STRUCTURE_SPINE_01 = new ResourceLocation("fossil/spine_1");
	private static final ResourceLocation STRUCTURE_SPINE_02 = new ResourceLocation("fossil/spine_2");
	private static final ResourceLocation STRUCTURE_SPINE_03 = new ResourceLocation("fossil/spine_3");
	private static final ResourceLocation STRUCTURE_SPINE_04 = new ResourceLocation("fossil/spine_4");
	private static final ResourceLocation STRUCTURE_SKULL_01 = new ResourceLocation("fossil/skull_1");
	private static final ResourceLocation STRUCTURE_SKULL_02 = new ResourceLocation("fossil/skull_2");
	private static final ResourceLocation STRUCTURE_SKULL_03 = new ResourceLocation("fossil/skull_3");
	private static final ResourceLocation STRUCTURE_SKULL_04 = new ResourceLocation("fossil/skull_4");
	private static final ResourceLocation[] FOSSILS = new ResourceLocation[]{STRUCTURE_SPINE_01, STRUCTURE_SPINE_02, STRUCTURE_SPINE_03, STRUCTURE_SPINE_04, STRUCTURE_SKULL_01, STRUCTURE_SKULL_02, STRUCTURE_SKULL_03, STRUCTURE_SKULL_04};
	
	public SurfaceFossilsFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
	{
		Random random = worldIn.getRandom();
		Rotation[] arotation = Rotation.values();
		Rotation rotation = arotation[random.nextInt(arotation.length)];
		int i = random.nextInt(FOSSILS.length);
		TemplateManager templatemanager = ((ServerWorld) worldIn.getWorld()).getSaveHandler().getStructureTemplateManager();
		Template template = templatemanager.getTemplateDefaulted(FOSSILS[i]);
		
		PlacementSettings settings = new PlacementSettings().setRotation(rotation).setChunk(new ChunkPos(pos)).setRandom(rand).addProcessor(StructureBlockRegistryProcessor.INSTANCE);
		
		BlockPos size = template.getSize();
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		
		int yMin = Integer.MAX_VALUE, yMax = 0;
		for(BlockPos floorPos : BlockPos.getAllInBoxMutable(0, 0, 0, size.getX(), 0, size.getZ()))
		{
			int y = worldIn.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos.getX() + floorPos.getX() + xOffset, pos.getZ() + floorPos.getZ() + zOffset);
			yMax = Math.max(yMax, y);
			yMin = Math.min(yMin, y);
		}
		
		int y;
		if(yMin == yMax)
			y = yMin - size.getY() + 1;
		else y = yMin - size.getY() + 2;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, Rotation.NONE);
		template.addBlocksToWorld(worldIn, structurePos, settings);
		
		return true;
	}
}
