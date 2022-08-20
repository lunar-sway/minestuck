package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public abstract class AbstractTemplateFeature<T extends FeatureConfiguration> extends Feature<T>
{
	public AbstractTemplateFeature(Codec<T> pCodec)
	{
		super(pCodec);
	}
	
	protected abstract ResourceLocation pickTemplate(Random random);
	
	protected abstract int pickY(WorldGenLevel level, BlockPos pos, Vec3i templateSize, Random random);
	
	protected static int minWorldHeightInSize(WorldGenLevel level, BlockPos pos, Vec3i templateSize)
	{
		int minY = Integer.MAX_VALUE;
		for(BlockPos floorPos : BlockPos.betweenClosed(0, 0, 0, templateSize.getX(), 0, templateSize.getZ()))
		{
			int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + floorPos.getX(), pos.getZ() + floorPos.getZ());
			minY = Math.min(minY, y);
		}
		return minY;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<T> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		ChunkGenerator generator = context.chunkGenerator();
		Random rand = context.random();
		Rotation rotation = Rotation.getRandom(rand);
		StructureManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(this.pickTemplate(rand));
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation).setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(generator)));
		
		Vec3i size = template.getSize(rotation);
		pos.offset(-size.getX()/2, 0, -size.getZ()/2);
		
		int y = this.pickY(level, pos, size, rand);
		
		BlockPos structurePos = template.getZeroPositionWithTransform(pos.atY(y), Mirror.NONE, rotation);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		return true;
	}
}
