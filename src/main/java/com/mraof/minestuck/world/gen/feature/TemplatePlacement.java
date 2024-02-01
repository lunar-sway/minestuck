package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * A helper class for placing a structure template from a {@link net.minecraft.world.level.levelgen.feature.Feature}.
 */
public record TemplatePlacement(StructureTemplate template, BlockPos cornerPos, Rotation rotation)
{
	public static TemplatePlacement centeredWithRandomRotation(StructureTemplate template, BlockPos centerPos, RandomSource randomSource)
	{
		Rotation rotation = Rotation.getRandom(randomSource);
		Vec3i size = template.getSize(rotation);
		return new TemplatePlacement(template, centerPos.offset(-size.getX() / 2, 0, -size.getZ() / 2), rotation);
	}
	
	public Vec3i size()
	{
		return this.template.getSize(this.rotation);
	}
	
	public Iterable<BlockPos> xzPlacedRange()
	{
		Vec3i size = this.size();
		return BlockPos.betweenClosed(this.cornerPos, this.cornerPos.offset(size.getX(), 0, size.getZ()));
	}
	
	public void placeWithStructureBlockRegistry(int y, FeaturePlaceContext<?> context)
	{
		StructureBlockRegistry structureBlockRegistry = StructureBlockRegistry.getOrDefault(context.chunkGenerator());
		this.placeAt(y, context, new StructurePlaceSettings().addProcessor(new StructureBlockRegistryProcessor(structureBlockRegistry)));
	}
	
	public void placeAt(int y, FeaturePlaceContext<?> context, StructurePlaceSettings settings)
	{
		ChunkPos chunkPos = new ChunkPos(context.origin());
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, context.level().getMinBuildHeight(), chunkPos.getMinBlockZ() - 16,
				chunkPos.getMaxBlockX() + 16, context.level().getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		
		BlockPos structurePos = this.template.getZeroPositionWithTransform(this.cornerPos.atY(y), Mirror.NONE, this.rotation);
		this.template.placeInWorld(context.level(), structurePos, structurePos,
				settings.setRotation(this.rotation).setRandom(context.random()).setBoundingBox(boundingBox),
				context.random(), Block.UPDATE_INVISIBLE);
	}
}
