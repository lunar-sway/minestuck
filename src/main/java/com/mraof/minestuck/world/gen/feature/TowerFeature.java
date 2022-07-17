package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class TowerFeature extends Feature<NoneFeatureConfiguration>
{
	private static final ResourceLocation STRUCTURE_TOWER = new ResourceLocation(Minestuck.MOD_ID, "tower");
	private static final ResourceLocation STRUCTURE_TOWER_WITH_CHEST = new ResourceLocation(Minestuck.MOD_ID, "tower_with_chest");
	private static final ResourceLocation STRUCTURE_TOWER_WALL = new ResourceLocation(Minestuck.MOD_ID, "tower_wall");
	private static final ResourceLocation STRUCTURE_ELEVATED_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "elevated_tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_DOOR = new ResourceLocation(Minestuck.MOD_ID, "tower_door");
	private static final ResourceLocation STRUCTURE_TOWER_BALCONY = new ResourceLocation(Minestuck.MOD_ID, "tower_balcony");
	
	public TowerFeature(Codec<NoneFeatureConfiguration> codec)
	{
		super(codec);
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context)
	{
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		Random rand = context.random();
		Rotation rotation = Rotation.getRandom(rand);
		ResourceLocation tower = rand.nextInt(50) == 0 ? STRUCTURE_TOWER_WITH_CHEST : STRUCTURE_TOWER;
		StructureManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(tower);
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator())));
		
		Vec3i size = template.getSize(rotation);
		int xOffset = rand.nextInt(16 - size.getX() - 2) + 1, zOffset = rand.nextInt(16 - size.getZ() - 2) + 1;
		
		int y = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX() + xOffset + size.getX()/2, pos.getZ() + zOffset + size.getZ()/2) - 2;
		
		BlockPos center = pos.offset(xOffset + size.getX()/2, y - pos.getY(), zOffset + size.getZ()/2);
		final int doorSide = 1, doorFront = 4;
		for(Direction direction : Direction.Plane.HORIZONTAL)
		{
			BlockPos doorPos = center.relative(direction, doorFront);
			
			ResourceLocation doorType;
			if(level.getBlockState(doorPos.above(2)).canOcclude())
			{
				if(level.getBlockState(doorPos.above(3)).canOcclude())
				{
					doorType = STRUCTURE_TOWER_WALL;
				} else
				{
					doorType = STRUCTURE_ELEVATED_TOWER_DOOR;
				}
			} else
			{
				if(level.getBlockState(doorPos.above()).canOcclude())
				{
					doorType = STRUCTURE_TOWER_DOOR;
				} else
				{
					doorType = STRUCTURE_TOWER_BALCONY;
				}
			}
			
			StructureTemplate door = templates.getOrCreate(doorType);
			
			Rotation doorRotation;
			switch(direction)
			{
				case EAST:
					doorRotation = Rotation.CLOCKWISE_90;
					break;
				case SOUTH:
					doorRotation = Rotation.CLOCKWISE_180;
					break;
				case WEST:
					doorRotation = Rotation.COUNTERCLOCKWISE_90;
					break;
				case NORTH: default:
					doorRotation = Rotation.NONE;
					break;
			}
			
			settings.setRotation(doorRotation);
			BlockPos structurePos = doorPos.relative(direction.getCounterClockWise(), doorSide).relative(direction, door.getSize(doorRotation).getZ() - 2);
			door.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		}
		
		settings.setRotation(rotation);
		BlockPos structurePos = template.getZeroPositionWithTransform(new BlockPos(pos.getX() + xOffset, y, pos.getZ() + zOffset), Mirror.NONE, rotation);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		for(StructureTemplate.StructureBlockInfo blockInfo : template.filterBlocks(structurePos, settings, Blocks.STRUCTURE_BLOCK))
		{
			if(blockInfo.nbt != null)
			{
				StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
				if (structuremode == StructureMode.DATA)
				{
					String data = blockInfo.nbt.getString("metadata");
					if(data.equals("basic_chest"))
					{
						level.setBlock(blockInfo.pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
						if (level.getBlockEntity(blockInfo.pos.below()) instanceof ChestBlockEntity chest)
						{
							chest.setLootTable(MSLootTables.BASIC_MEDIUM_CHEST, rand.nextLong());
						}
					}
				}
			}
		}
		
		return true;
	}
}