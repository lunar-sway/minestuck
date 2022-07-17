package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class FloorCogFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_1 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_1");
	private static final ResourceLocation STRUCTURE_LARGE_FLOOR_COG_2 = new ResourceLocation(Minestuck.MOD_ID, "large_floor_cog_2");
	
	public FloorCogFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		ChunkGenerator generator = context.chunkGenerator();
		Random rand = context.random();
		StructureManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(rand.nextBoolean() ? STRUCTURE_LARGE_FLOOR_COG_1 : STRUCTURE_LARGE_FLOOR_COG_2);
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(generator)));
		
		Vec3i size = template.getSize();
		int xOffset = rand.nextInt(16 - size.getX()), zOffset = rand.nextInt(16 - size.getZ());
		
		int yMin = Integer.MAX_VALUE, yMax = 0;
		for(BlockPos floorPos : BlockPos.betweenClosed(0, 0, 0, size.getX(), 0, size.getZ()))
		{
			int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + floorPos.getX() + xOffset, pos.getZ() + floorPos.getZ() + zOffset);
			yMax = Math.max(yMax, y);
			yMin = Math.min(yMin, y);
		}
		
		int y;
		if(yMin == yMax)
			y = yMin - size.getY() + 1;
		else y = yMin - size.getY() + 2;
		
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, Rotation.NONE);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		return true;
	}
}