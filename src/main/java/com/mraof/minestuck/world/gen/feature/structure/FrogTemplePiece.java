package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.LotusTimeCapsuleBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
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
	private int randomDepth;
	
	public FrogTemplePiece(Random random, int minX, int minZ, float skyLight)
	{
		super(MSStructurePieces.FROG_TEMPLE, random, minX, 64, minZ, 140, 100, 140);
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
		BlockState wallBlock = MSBlocks.BROWN_STONE_BRICKS.getDefaultState();
		BlockState columnBlock = MSBlocks.BROWN_STONE_COLUMN.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_BROWN_STONE.getDefaultState();
		BlockState wallTorch = blocks.getBlockState("wall_torch");
		
		randomDepth = randomIn.nextInt(5);
		
		
		for(int a = 0; a < 7; a++)
		{
			buildMainPlatform(wallBlock, worldIn, randomIn, boundingBoxIn, a);
		}
		
		buildStairsAndUnderneath(wallBlock, worldIn, boundingBoxIn);
		buildFloors(floorBlock, worldIn, boundingBoxIn);
		carveRooms(worldIn, boundingBoxIn);
		generateLoot(worldIn, boundingBoxIn, randomIn, this.getCoordBaseMode().getOpposite(), MSLootTables.BASIC_MEDIUM_CHEST, chunkPosIn);
		buildPillars(columnBlock, worldIn, boundingBoxIn, randomIn);
		
		return true;
	}
	
	private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, Direction direction, ResourceLocation lootTable, ChunkPos chunkPos)
	{
		//TODO There is only a percent chance of the rotateYCCW applying correctly
		Direction modifiedDirection = direction.rotateYCCW();
		setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction.getOpposite()), 18 + 20, 17, 9 + 38 + 20, boundingBox);
		
		MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.placeWithRotation(worldIn, chunkPos.getBlock(20 + 20, 51, 22 + 38 + 20), getRotation());
		
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 20 + 20, 49, 22 + 38 + 20, boundingBox);
		modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 20 + 20, 49, 23 + 38 + 20, boundingBox);
		modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 19 + 20, 49, 23 + 38 + 20, boundingBox);
		modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 19 + 20, 49, 22 + 38 + 20, boundingBox);
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
		//fillWithBlocks(world, boundingBox, a*2, 0+30, a*2+38, (int)(40 * (1-(float)a/20)), 8+(8*a)+30, (int)(40 * (1-(float)a/20))+38, block, block, false);
		fillWithBlocks(world, boundingBox, a * 2 + 20, 0, a * 2 + 37 + 20, (int) (45 * (1 - (float) a / 15)) + 20, (8 * a) + 8, (int) (45 * (1 - (float) a / 15)) + 36 + 20, block, block, false);
		
		if(a == 0)
		{
			fillWithBlocks(world, boundingBox, 0, 0, 0, 0, 16, 0, Blocks.DIAMOND_BLOCK.getDefaultState(), Blocks.DIAMOND_BLOCK.getDefaultState(), false);
		}
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		for(int i = 0; i < 49; i++)
		{
			fillWithBlocks(world, boundingBox, 16 + 20, i, i + 20, 23 + 20, i, 50 + 20, MSBlocks.POLISHED_BROWN_STONE.getDefaultState(), MSBlocks.POLISHED_BROWN_STONE.getDefaultState(), false); //stairs
		}
		fillWithBlocks(world, boundingBox, 16 + 20, 0, 0 + 20, 24 + 20, -30, 50 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.BROWN_STONE.getDefaultState(), false); //underneath stairs
		//fillWithBlocks(world, boundingBox, 0, 0, 0+38, 40, 30, 40+38, block, block, false); //underneath main platform
		for(int i = 0; i < 28; i++)
		{
			fillWithBlocks(world, boundingBox, i + 20, i, -i + 20, 45 - +20, -i, 80 - i + 20, block, block, false); //underneath main platform
		}
	}
	
	private void buildFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, 13 + 20, 48, 51 + 20, 26 + 20, 48, 63 + 20, block, block, false); //lotus room floor
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		//fillWithBlocks(world, boundingBox, 15, 48, 15, 30, 55, 32, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //underneath stairs
		/*fillWithAir(world, boundingBox, 13, 79, 51, 27, 85, 65); //lotus room
		fillWithAir(world, boundingBox, 19, 79, 50, 21, 82, 50); //lotus room entry
		fillWithAir(world, boundingBox, 8+20, 46, 46+20, 32+20, 54, 70+20); //lower room
		fillWithAir(world, boundingBox, 20+20, 47, 70+20, 22+20, 50, 75+20); //lower room entry*/
		
		fillWithBlocks(world, boundingBox, 14 + 20, 49, 51 + 20, 26 + 20, 55, 64 + 20, Blocks.GREEN_STAINED_GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lotus room
		fillWithBlocks(world, boundingBox, 18 + 20, 49, 49 + 20, 21 + 20, 52, 50 + 20, Blocks.RED_STAINED_GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lotus room entry
		fillWithBlocks(world, boundingBox, 5 + 20, 17, 46 + 20, 34 + 20, 23, 70 + 20, Blocks.BLUE_STAINED_GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lower room
		fillWithBlocks(world, boundingBox, 20 + 20, 17, 71 + 20, 23 + 20, 20, 79 + 20, Blocks.YELLOW_STAINED_GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lower room entry
	}
	
	private void buildPillars(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random random)
	{
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 1, 21, 40, 4, block, block, false); //front/south pillar
				fillWithBlocks(world, boundingBox, 17 + 20, 41, 0, 22 + 20, 46, 5, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 1, 21, 40 - randReduction, 4, block, block, false); //front/south pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 40, 60 + 20, block, block, false); //west pillar
				fillWithBlocks(world, boundingBox, 0, 41, 56 + 20, 5, 46, 61 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 40 - randReduction, 60 + 20, block, block, false); //west pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 40, 108 + 20, block, block, false); //north pillar
				fillWithBlocks(world, boundingBox, 17 + 20, 41, 102 + 20, 22 + 20, 46, 109 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 40 - randReduction, 108 + 20, block, block, false); //north pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 40, 60 + 20, block, block, false); //east pillar
				fillWithBlocks(world, boundingBox, 66 + 20, 41, 56 + 20, 71 + 20, 46, 61 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 40 - randReduction, 60 + 20, block, block, false); //east pillar
			}
		}
		/*if(random.nextBoolean())
		{
			int randReduction = random.nextInt(5);
			fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 6, 40 - randReduction, 60 + 20, block, block, false); //west pillar
			fillWithBlocks(world, boundingBox, 0, 41 - randReduction, 56 + 20, 5, 46 - randReduction, 61 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
		}
		if(random.nextBoolean())
		{
			int randReduction = random.nextInt(5);
			fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 40 - randReduction, 108 + 20, block, block, false); //north pillar
			fillWithBlocks(world, boundingBox, 17 + 20, 41 - randReduction, 102 + 20, 22 + 20, 46 - randReduction, 109 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
		}
		if(random.nextBoolean())
		{
			int randReduction = random.nextInt(5);
			fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 72 + 20, 40 - randReduction, 60 + 20, block, block, false); //east pillar
			fillWithBlocks(world, boundingBox, 66 + 20, 41 - randReduction, 56 + 20, 73 + 20, 46 - randReduction, 61 + 20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
		}*/
	}
	
	/*private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, Direction direction, ResourceLocation lootTable)
	{
		Direction modifiedDirection = direction;
		setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction.rotateY()), 18+20, 17+30-, 9+38+20, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, direction), 20+20, 49+30, 19+38+20, boundingBox);
		modifiedDirection = modifiedDirection.rotateY();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 21+20, 49+30, 19+38+20, boundingBox);
		modifiedDirection = modifiedDirection.rotateY();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 21+20, 49+30, 18+38+20, boundingBox);
		modifiedDirection = modifiedDirection.rotateY();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, modifiedDirection), 20+20, 49+30, 18+38+20, boundingBox);
		
		
	 */
		/*
		BlockPos blockPos = new BlockPos(18,17,9);
		TileEntity te = worldIn.getTileEntity(blockPos);
		if(te instanceof ChestTileEntity)
		{
			ChestTileEntity chest = (ChestTileEntity) te;
			chest.setLootTable(lootTable, randomIn.nextLong());
		}
		*/
	/*
}
	
	private void buildMainPlatform(BlockState block, IWorld world, Random rand, MutableBoundingBox boundingBox, int a)
	{
		//fillWithBlocks(world, boundingBox, a*2, 0+30, a*2+38, (int)(40 * (1-(float)a/20)), 8+(8*a)+30, (int)(40 * (1-(float)a/20))+38, block, block, false);
		fillWithBlocks(world, boundingBox, a*2+20, 30, a*2+38+20, (int)(45 * (1-(float)a/15))+20, (8*a)+38, (int)(45 * (1-(float)a/15))+38+20, block, block, false);
		
		if(a == 0)
		{
			fillWithBlocks(world, boundingBox, 0, 30, 0, 0, 46, 0, Blocks.DIAMOND_BLOCK.getDefaultState(), Blocks.DIAMOND_BLOCK.getDefaultState(), false);
		}
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		for(int i = 0; i < 49; i++)
		{
			fillWithBlocks(world, boundingBox, 16+20, i+30, i+20, 23+20, i+30, 50+20, MSBlocks.POLISHED_BROWN_STONE.getDefaultState(), MSBlocks.POLISHED_BROWN_STONE.getDefaultState(), false); //stairs
		}
		fillWithBlocks(world, boundingBox, 16+20, 0, 0+20, 24+20, 30, 50+20, MSBlocks.BROWN_STONE.getDefaultState(), MSBlocks.BROWN_STONE.getDefaultState(), false); //underneath stairs
		//fillWithBlocks(world, boundingBox, 0, 0, 0+38, 40, 30, 40+38, block, block, false); //underneath main platform
		fillWithBlocks(world, boundingBox, 0+20, 0, 38+20, 45+20, 30, 80+20, block, block, false); //underneath main platform
	}
	
	private void buildFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, 13+20, 78, 51+20, 27+20, 78, 68+20, block, block, false); //lotus room floor
	}
	
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{*/
	//fillWithBlocks(world, boundingBox, 15, 48, 15, 30, 55, 32, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //underneath stairs
		/*
		fillWithAir(world, boundingBox, 13+20, 79, 51+20, 27+20, 85, 65+20); //lotus room
		fillWithAir(world, boundingBox, 19+20, 79, 50+20, 21+20, 82, 50+20); //lotus room entry
		fillWithAir(world, boundingBox, 8+20, 46, 46+20, 32+20, 54, 70+20); //lower room
		fillWithAir(world, boundingBox, 20+20, 47, 70+20, 22+20, 50, 75+20); //lower room entry*/
		
		/*
		fillWithBlocks(world, boundingBox, 13+20, 79, 57+20, 27+20, 85, 65+20, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lotus room
		fillWithBlocks(world, boundingBox, 18+20, 79, 50+20, 21+20, 82, 50+20, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lotus room entry
		fillWithBlocks(world, boundingBox, 8+20, 46, 46+20, 32+20, 54, 70+20, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lower room
		fillWithBlocks(world, boundingBox, 20+20, 47, 70+20, 22+20, 50, 77+20, Blocks.GLASS.getDefaultState(), Blocks.GLASS.getDefaultState(), false); //lower room entry
	}
	
	private void buildPillars(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, 32, 0, 0, 34, 50, 1, block, block, false); //front/south pillar
		fillWithBlocks(world, boundingBox, 0, 0, 62, 1, 50, 64, block, block, false); //west pillar
		fillWithBlocks(world, boundingBox, 32, 0, 110, 34, 50, 112, block, block, false); //north pillar
		fillWithBlocks(world, boundingBox, 79, 0, 62, 80, 50, 64, block, block, false); //east pillar
	}*/
}