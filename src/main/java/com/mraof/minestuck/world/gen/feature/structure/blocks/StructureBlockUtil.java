package com.mraof.minestuck.world.gen.feature.structure.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;
import java.util.Random;

public class StructureBlockUtil
{
	
	public static boolean placeSpawner(BlockPos pos, IWorld world, MutableBoundingBox bb, EntityType<?> entityType)
	{
		WeightedSpawnerEntity entity = new WeightedSpawnerEntity();
		entity.getTag().putString("id", Objects.requireNonNull(entityType.getRegistryName()).toString());
		return placeSpawner(pos, world, bb, entity);
	}
	
	public static boolean placeSpawner(BlockPos pos, IWorld world, MutableBoundingBox bb, WeightedSpawnerEntity entity)
	{
		if(bb.isInside(pos))
		{
			world.setBlock(pos, Blocks.SPAWNER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof MobSpawnerTileEntity)
			{
				MobSpawnerTileEntity spawner = (MobSpawnerTileEntity) te;
				spawner.getSpawner().setNextSpawnData(entity);
				
				return true;
			}
		}
		return false;
	}
	
	public static void placeLootChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ResourceLocation lootTable, Random rand)
	{
		placeLootChest(pos, world, bb, direction, ChestType.SINGLE, lootTable, rand);
	}
	
	public static void placeLootChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ChestType type, ResourceLocation lootTable, Random rand)
	{
		if(bb == null || bb.isInside(pos))
		{
			world.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, direction).setValue(ChestBlock.TYPE, type), Constants.BlockFlags.BLOCK_UPDATE);
			
			TileEntity te = world.getBlockEntity(pos);
			if(te instanceof ChestTileEntity)
			{
				ChestTileEntity chest = (ChestTileEntity) te;
				chest.setLootTable(lootTable, rand.nextLong());
			}
			
			if(bb != null)
				world.getChunk(pos).markPosForPostprocessing(pos);
		}
	}
}