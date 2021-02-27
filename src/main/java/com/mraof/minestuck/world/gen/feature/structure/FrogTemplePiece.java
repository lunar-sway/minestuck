package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePiece extends ScatteredStructurePiece
{
	private final boolean[] torches = new boolean[4];
	private boolean placedChest;
	
	public FrogTemplePiece(Random random, int minX, int minZ, float skyLight)
	{
		super(MSStructurePieces.FROG_TEMPLE, random, minX, 64, minZ, 120, 120, 120);
	}
	
	public FrogTemplePiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.FROG_TEMPLE, nbt);
		
		placedChest = nbt.getBoolean("placed_chest");
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt)    //Note: incorrectly mapped. Should be writeAdditional
	{
		super.readAdditional(nbt);
		nbt.putBoolean("placed_chest", placedChest);
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		if(!isInsideBounds(worldIn, boundingBoxIn, 0))
			return false;
		
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
		BlockState wallBlock = blocks.getBlockState("structure_primary");
		BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
		BlockState floorBlock = blocks.getBlockState("structure_secondary");
		BlockState wallTorch = blocks.getBlockState("wall_torch");
		
		
		for(int a = 0; a < 7; a++)
		{
			buildMainPlatform(wallBlock, worldIn, randomIn, boundingBoxIn, a);
		}
		
		buildStairsAndUnderneath(wallBlock, worldIn, boundingBoxIn);
		carveRooms(worldIn, boundingBoxIn);
		generateLoot(worldIn, boundingBoxIn, randomIn, this.getCoordBaseMode().getOpposite(), MSLootTables.BASIC_MEDIUM_CHEST);
		
		return true;
	}
	
	private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, Direction direction, ResourceLocation lootTable)
	{
		setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction), 18, 17+30, 9+38, boundingBox);
		/*
		BlockPos blockPos = new BlockPos(18,17,9);
		TileEntity te = worldIn.getTileEntity(blockPos);
		if(te instanceof ChestTileEntity)
		{
			ChestTileEntity chest = (ChestTileEntity) te;
			chest.setLootTable(lootTable, randomIn.nextLong());
		}*/
	}
	
	private void buildMainPlatform(BlockState block, IWorld world, Random rand, MutableBoundingBox boundingBox, int a)
	{
		fillWithBlocks(world, boundingBox, a*2, 0+30, a*2+38, (int)(40 * (1-(float)a/20)), 8+(8*a)+30, (int)(40 * (1-(float)a/20))+38, block, block, false);
		for(int i = 0; i < 8; i++)
		{
			//fillWithBlocks(world, boundingBox, 15, i+(8*a), -60+i, 25, i+(8*a), 60-i, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false);
			/*
			for(int j = 0; j < (40 * (1-(float)a/10)); j++)
			{
				for(int k = 0; k < (40 * (1-(float)a/10)); k++)
				{
					//setBlockState(world, block, j+(a*2), i+(8*a), k+(a*2), boundingBox);
				}
			}*/
		}
		if(a == 0)
		{
			//setBlockState(world, Blocks.DIAMOND_BLOCK.getDefaultState(), 0, 0, 0, boundingBox);
			fillWithBlocks(world, boundingBox, 0, 0+30, 0, 0, 16+30, 0, Blocks.DIAMOND_BLOCK.getDefaultState(), Blocks.DIAMOND_BLOCK.getDefaultState(), false);
			/*for(int i = 0; i < 40; i++)
			{
				for(int j = 0; j < 40; j++)
				{
					for(int k = 0; k < 40; k++)
					{
						//setBlockState(world, Blocks.PUMPKIN.getDefaultState(), j, i, k, boundingBox);
					}
				}
			}*/
		}
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		for(int i = 0; i < 49; i++)
		{
			fillWithBlocks(world, boundingBox, 16, i+30, i, 24, i+30, 50, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //stairs
		}
		fillWithBlocks(world, boundingBox, 16, 0, 0, 24, -30+30, 12+38, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //underneath stairs
		fillWithBlocks(world, boundingBox, 0, 0, 0+38, 40, -30+30, 40+38, block, block, false); //underneath main platform
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		//fillWithBlocks(world, boundingBox, 15, 48, 15, 30, 55, 32, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //underneath stairs
		fillWithAir(world, boundingBox, 13, 49+30, 13+38, 27, 55+30, 27+38); //lotus room
		fillWithAir(world, boundingBox, 19, 49+30, 12+38, 21, 52+30, 12+38); //lotus room entry
		fillWithAir(world, boundingBox, 8, 16+30, 8+38, 32, 24+30, 32+38); //lower room
	}
}