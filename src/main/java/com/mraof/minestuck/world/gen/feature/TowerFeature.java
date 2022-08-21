package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;
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
		BlockPos pos = context.origin().below(2);
		Random rand = context.random();
		Rotation rotation = Rotation.getRandom(rand);
		ResourceLocation tower = rand.nextInt(50) == 0 ? STRUCTURE_TOWER_WITH_CHEST : STRUCTURE_TOWER;
		StructureManager templates = level.getLevel().getStructureManager();
		StructureTemplate template = templates.getOrCreate(tower);
		
		ChunkPos chunkPos = new ChunkPos(pos);
		BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, level.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
		StructurePlaceSettings settings = new StructurePlaceSettings().setBoundingBox(boundingBox).setRandom(rand)
				.addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(context.chunkGenerator())));
		
		for(Rotation doorRotation : Rotation.values())
		{
			placeDoor(level, rand, templates, settings, pos, doorRotation);
		}
		
		Vec3i size = template.getSize(rotation);
		settings.setRotation(rotation);
		BlockPos structurePos = template.getZeroPositionWithTransform(pos.offset(-size.getX()/2, 0, -size.getZ()/2), Mirror.NONE, rotation);
		template.placeInWorld(level, structurePos, structurePos, settings, rand, Block.UPDATE_INVISIBLE);
		
		handleDataMarkers(level, rand, template, settings, structurePos);
		
		return true;
	}
	
	private static final BlockPos DOOR_CHECK_OFFSET = new BlockPos(0, 0, 4), DOOR_PLACE_OFFSET = new BlockPos(-1, 0, 3);
	
	private void placeDoor(WorldGenLevel level, Random rand, StructureManager templates, StructurePlaceSettings settings, BlockPos towerCenter, Rotation rotation)
	{
		StructureTemplate door = templates.getOrCreate(getDoorTemplate(level, towerCenter, rotation));
		
		BlockPos structurePos = towerCenter.offset(DOOR_PLACE_OFFSET.rotate(rotation));
		door.placeInWorld(level, structurePos, structurePos, settings.setRotation(rotation), rand, Block.UPDATE_INVISIBLE);
	}
	
	@Nonnull
	private ResourceLocation getDoorTemplate(WorldGenLevel level, BlockPos towerCenter, Rotation rotation)
	{
		BlockPos doorCheckPos = towerCenter.offset(DOOR_CHECK_OFFSET.rotate(rotation));
		
		if(level.getBlockState(doorCheckPos.above(2)).canOcclude())
		{
			if(level.getBlockState(doorCheckPos.above(3)).canOcclude())
			{
				return STRUCTURE_TOWER_WALL;
			} else
			{
				return STRUCTURE_ELEVATED_TOWER_DOOR;
			}
		} else
		{
			if(level.getBlockState(doorCheckPos.above()).canOcclude())
			{
				return STRUCTURE_TOWER_DOOR;
			} else
			{
				return STRUCTURE_TOWER_BALCONY;
			}
		}
	}
	
	private void handleDataMarkers(WorldGenLevel level, Random rand, StructureTemplate template, StructurePlaceSettings settings, BlockPos structurePos)
	{
		for(StructureTemplate.StructureBlockInfo blockInfo : template.filterBlocks(structurePos, settings, Blocks.STRUCTURE_BLOCK))
		{
			//noinspection ConstantConditions
			if(blockInfo.nbt != null)
			{
				StructureMode structuremode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
				if (structuremode == StructureMode.DATA)
				{
					handleDataMarker(level, rand, blockInfo.pos, blockInfo.nbt.getString("metadata"));
				}
			}
		}
	}
	
	private void handleDataMarker(WorldGenLevel level, Random rand, BlockPos pos, String data)
	{
		if(data.equals("basic_chest"))
		{
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			if (level.getBlockEntity(pos.below()) instanceof ChestBlockEntity chest)
			{
				chest.setLootTable(MSLootTables.BASIC_MEDIUM_CHEST, rand.nextLong());
			}
		}
	}
}