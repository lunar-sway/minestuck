package com.mraof.minestuck.world.gen.structure.blocks;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;

public class StructureBlockUtil
{
	
	/*public static boolean placeSpawner(BlockPos pos, World world, StructureBoundingBox bb, String entityName)
	{
		WeightedSpawnerEntity entity = new WeightedSpawnerEntity();
		entity.getNbt().setString("id", entityName);
		entity.getNbt().setBoolean("spawned", true);
		return placeSpawner(pos, world, bb, entity);
	}
	
	public static boolean placeSpawner(BlockPos pos, World world, StructureBoundingBox bb, WeightedSpawnerEntity entity)
	{
		if(bb.isVecInside(pos))
		{
			world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityMobSpawner)
			{
				TileEntityMobSpawner spawner = (TileEntityMobSpawner) te;
				spawner.getSpawnerBaseLogic().setNextSpawnData(entity);
				
				return true;
			}
		}
		return false;
	}
	*/
	public static void placeLootChest(BlockPos pos, IWorld world, MutableBoundingBox bb, Direction direction, ResourceLocation lootTable, Random rand)
	{
		if(bb == null || bb.isVecInside(pos))
		{
			world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction), 2);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof ChestTileEntity)
			{
				ChestTileEntity chest = (ChestTileEntity) te;
				chest.setLootTable(lootTable, rand.nextLong());
			}
		}
	}
}