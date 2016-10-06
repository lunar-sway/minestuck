package com.mraof.minestuck.world.lands.structure.blocks;

import java.util.Random;

import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class StructureBlockUtil
{
	
	public static boolean placeSpawner(BlockPos pos, World world, StructureBoundingBox bb, String entityName)
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
	
	public static boolean placeLootChest(BlockPos pos, World world, StructureBoundingBox bb, EnumFacing facing, ResourceLocation lootTable, Random rand)
	{
		if(bb.isVecInside(pos))
		{
			world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing), 2);
			
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityChest)
			{
				TileEntityChest chest = (TileEntityChest) te;
				chest.setLootTable(lootTable, rand.nextLong());
			}
		}
		return false;
	}
}