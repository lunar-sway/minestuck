package com.mraof.minestuck.world.gen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * A helper class for placing a structure template from a {@link Feature}.
 * Some functions take positions in "unrotated coordinate space",
 * which is the coordinate space that the template itself is in before it is rotated and placed into the world,
 * which covers (0, 0, 0) to (size.x - 1, size.y - 1, size.z - 1).
 * "level space" instead refers to the actual, in-world position of a block.
 */
@SuppressWarnings("unused")
public record TemplatePlacement(StructureTemplate template, BlockPos zeroPos, Rotation rotation)
{
	public static TemplatePlacement centeredWithRandomRotation(StructureTemplate template, BlockPos centerPos, RandomSource randomSource)
	{
		Rotation rotation = Rotation.getRandom(randomSource);
		Vec3i size = template.getSize(rotation);
		BlockPos cornerPos = centerPos.offset(-size.getX() / 2, 0, -size.getZ() / 2);
		BlockPos zeroPos = template.getZeroPositionWithTransform(cornerPos, Mirror.NONE, rotation);
		return new TemplatePlacement(template, zeroPos, rotation);
	}
	
	public Vec3i size()
	{
		return this.template.getSize(this.rotation);
	}
	
	public BlockPos actualFromRelative(int x, int y, int z)
	{
		return this.actualFromRelative(new BlockPos(x, y, z));
	}
	
	/**
	 * Takes a position in unrotated template space and translates it into level space.
	 */
	public BlockPos actualFromRelative(BlockPos templatePos)
	{
		return this.zeroPos.offset(StructureTemplate.transform(templatePos, Mirror.NONE, this.rotation, BlockPos.ZERO));
	}
	
	public Iterable<BlockPos> xzPlacedRange()
	{
		Vec3i unrotatedSize = this.template.getSize();
		return xzRange(0, 0, unrotatedSize.getX() - 1, unrotatedSize.getZ() - 1);
	}
	
	/**
	 * Takes coordinates that forms a rectangle in unrotated template space,
	 * and returns all positions in this rectangle in level space.
	 */
	public Iterable<BlockPos> xzRange(int x1, int z1, int x2, int z2)
	{
		return BlockPos.betweenClosed(actualFromRelative(x1, 0, z1), actualFromRelative(x2, 0, z2));
	}
	
	public int minHeight(Heightmap.Types type, LevelReader level)
	{
		return minHeight(type, level, this.xzPlacedRange());
	}
	public int minHeight(Heightmap.Types type, LevelReader level, Iterable<BlockPos> positions)
	{
		int minY = Integer.MAX_VALUE;
		for(BlockPos floorPos : positions)
			minY = Math.min(minY, level.getHeight(type, floorPos.getX(), floorPos.getZ()));
		
		return minY;
	}
	
	public int maxHeight(Heightmap.Types type, LevelReader level)
	{
		return maxHeight(type, level, this.xzPlacedRange());
	}
	
	public static int maxHeight(Heightmap.Types type, LevelReader level, Iterable<BlockPos> positions)
	{
		int maxY = Integer.MIN_VALUE;
		for(BlockPos floorPos : positions)
			maxY = Math.max(maxY, level.getHeight(type, floorPos.getX(), floorPos.getZ()));
		
		return maxY;
	}
	
	public Range heightRange(Heightmap.Types type, LevelReader level)
	{
		return heightRange(type, level, this.xzPlacedRange());
	}
	
	public static Range heightRange(Heightmap.Types type, LevelReader level, Iterable<BlockPos> positions)
	{
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for(BlockPos floorPos : positions)
		{
			int height = level.getHeight(type, floorPos.getX(), floorPos.getZ());
			minY = Math.min(minY, height);
			maxY = Math.max(maxY, height);
		}
		
		return new Range(minY, maxY);
	}
	
	public record Range(int min, int max)
	{
		public int difference()
		{
			return max - min;
		}
	}
	
	public void placeWithStructureBlockRegistry(int y, FeaturePlaceContext<?> context)
	{
		this.placeAt(y, context, new StructurePlaceSettings().addProcessor(StructureBlockRegistryProcessor.from(context)));
	}
	
	@SuppressWarnings("unused")
	public void placeAt(int y, FeaturePlaceContext<?> context)
	{
		this.placeAt(y, context, new StructurePlaceSettings());
	}
	
	public void placeAt(int y, FeaturePlaceContext<?> context, StructurePlaceSettings settings)
	{
		ChunkPos chunkPos = new ChunkPos(context.origin());
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, context.level().getMinBuildHeight(), chunkPos.getMinBlockZ() - 16,
				chunkPos.getMaxBlockX() + 16, context.level().getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		
		BlockPos structurePos = this.zeroPos.atY(y);
		this.template.placeInWorld(context.level(), structurePos, structurePos,
				settings.setMirror(Mirror.NONE).setRotation(this.rotation).setRandom(context.random()).setBoundingBox(boundingBox),
				context.random(), Block.UPDATE_INVISIBLE);
	}
}
