package com.mraof.minestuck.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nonnull;

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
		BlockPos towerPos = context.origin().below(2);
		RandomSource rand = context.random();
		
		ResourceLocation templateId = rand.nextInt(50) == 0 ? STRUCTURE_TOWER_WITH_CHEST : STRUCTURE_TOWER;
		StructureTemplate template = level.getLevel().getStructureManager().getOrCreate(templateId);
		TemplatePlacement placement = TemplatePlacement.centeredWithRandomRotation(template, towerPos, rand);
		placement.placeWithStructureBlockRegistry(context);
		
		placement.handleDataMarkers(context, (pos, data) -> handleDataMarker(level, rand, pos, data));
		
		for(Rotation doorRotation : Rotation.values())
			placeDoor(context, towerPos, doorRotation);
		
		return true;
	}
	
	private static final BlockPos DOOR_CHECK_OFFSET = new BlockPos(0, 0, 4), DOOR_PLACE_OFFSET = new BlockPos(0, 0, 3);
	
	private void placeDoor(FeaturePlaceContext<?> context, BlockPos towerCenter, Rotation rotation)
	{
		BlockPos doorPos = towerCenter.offset(DOOR_PLACE_OFFSET.rotate(rotation));
		StructureTemplate template = context.level().getLevel().getStructureManager().getOrCreate(getDoorTemplate(context.level(), towerCenter, rotation));
		
		TemplatePlacement.edgeCentered(template, doorPos, rotation).placeWithStructureBlockRegistry(context);
	}
	
	@Nonnull
	private ResourceLocation getDoorTemplate(WorldGenLevel level, BlockPos towerCenter, Rotation rotation)
	{
		BlockPos doorCheckPos = towerCenter.offset(DOOR_CHECK_OFFSET.rotate(rotation));
		if(level.getBlockState(doorCheckPos.above(3)).canOcclude())
			return STRUCTURE_TOWER_WALL;
		
		if(level.getBlockState(doorCheckPos.above(2)).canOcclude())
			return STRUCTURE_ELEVATED_TOWER_DOOR;
		
		if(level.getBlockState(doorCheckPos.above()).canOcclude())
			return STRUCTURE_TOWER_DOOR;
		else
			return STRUCTURE_TOWER_BALCONY;
	}
	
	private void handleDataMarker(WorldGenLevel level, RandomSource rand, BlockPos pos, String data)
	{
		if(data.equals("basic_chest"))
		{
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			if (level.getBlockEntity(pos.below()) instanceof ChestBlockEntity chest)
				chest.setLootTable(MSLootTables.BASIC_MEDIUM_CHEST, rand.nextLong());
		}
	}
}
