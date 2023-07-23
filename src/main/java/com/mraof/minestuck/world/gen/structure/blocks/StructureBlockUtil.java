package com.mraof.minestuck.world.gen.structure.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructureBlockUtil
{
	
	public static boolean placeSpawner(BlockPos pos, LevelAccessor level, BoundingBox bb, EntityType<?> entityType, RandomSource random)
	{
		if(bb.isInside(pos))
		{
			level.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), Block.UPDATE_CLIENTS);
			
			BlockEntity te = level.getBlockEntity(pos);
			if(te instanceof SpawnerBlockEntity spawner)
			{
				spawner.setEntityId(entityType, random);
				
				return true;
			}
		}
		return false;
	}
	
	public static void placeLootChest(BlockPos pos, LevelAccessor level, BoundingBox bb, Direction direction, ResourceLocation lootTable, RandomSource rand)
	{
		placeLootChest(pos, level, bb, direction, ChestType.SINGLE, lootTable, rand);
	}
	
	public static void placeLootChest(BlockPos pos, LevelAccessor level, BoundingBox bb, Direction direction, ChestType type, ResourceLocation lootTable, RandomSource rand)
	{
		if(bb == null || bb.isInside(pos))
		{
			level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction).setValue(ChestBlock.TYPE, type), Block.UPDATE_CLIENTS);
			
			if(level.getBlockEntity(pos) instanceof ChestBlockEntity chest)
			{
				chest.setLootTable(lootTable, rand.nextLong());
			}
			
			if(bb != null)
				level.getChunk(pos).markPosForPostprocessing(pos);
		}
	}
}