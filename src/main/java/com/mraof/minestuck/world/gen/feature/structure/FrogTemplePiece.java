package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.DecorBlock;
import com.mraof.minestuck.block.LotusTimeCapsuleBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePiece extends ScatteredStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain objects(the lotus flower entity) from spawning several times over
	private static final FrogTemplePiece.Selector HIEROGLYPHS = new FrogTemplePiece.Selector();
	
	public FrogTemplePiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.FROG_TEMPLE, random, x - 70, 64, z - 70, 140, 100, 140);
		
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		
		boundingBox.offset(0, posHeightPicked - boundingBox.minY, 0); //takes the lowest Ocean Floor gen viable height
	}
	
	public FrogTemplePiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.FROG_TEMPLE, nbt);
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		BlockState wallBlock = MSBlocks.GREEN_STONE_BRICKS.getDefaultState();
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_GREEN_STONE.getDefaultState();
		BlockState stoneBlock = MSBlocks.GREEN_STONE.getDefaultState();
		
		for(int a = 0; a < 7; a++)
		{
			buildMainPlatform(wallBlock, worldIn, randomIn, boundingBoxIn, a);
		}
		
		buildStairsAndUnderneath(wallBlock, worldIn, boundingBoxIn);
		buildWallsAndFloors(floorBlock, worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		buildIndoorBlocks(columnBlock, worldIn, boundingBoxIn);
		generateLoot(worldIn, boundingBoxIn, randomIn, chunkPosIn);
		buildFrog(stoneBlock, worldIn, boundingBoxIn);
		//buildPillars(columnBlock, worldIn, boundingBoxIn, randomIn);
		
		return true;
	}
	
	private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, ChunkPos chunkPos)
	{
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.EAST), 21 + 20, 49, 20 + 38 + 20, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.NORTH), 21 + 20, 49, 21 + 38 + 20, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.WEST), 20 + 20, 49, 21 + 38 + 20, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.SOUTH), 20 + 20, 49, 20 + 38 + 20, boundingBox);
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getCoordBaseMode() == Direction.SOUTH || this.getCoordBaseMode() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestFarPos = new BlockPos(this.getXWithOffset(21 + 20, 12 + 38 + 20), this.getYWithOffset(21), this.getZWithOffset(21 + 20, 12 + 38 + 20));
		fillWithBlocks(worldIn, boundingBoxIn, 20 + 20, 20, 12 + 38 + 20, 21 + 20, 20, 12 + 38 + 20, MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.SOUTH), MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState(), false);
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getCoordBaseMode(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestFarPos = new BlockPos(this.getXWithOffset(20 + 20, 12 + 38 + 20), this.getYWithOffset(21), this.getZWithOffset(20 + 20, 12 + 38 + 20));
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getCoordBaseMode(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		BlockPos chestNearDoorPos = new BlockPos(this.getXWithOffset(11 + 20, 29 + 38 + 20), this.getYWithOffset(21), this.getZWithOffset(11 + 20, 29 + 38 + 20));
		fillWithBlocks(worldIn, boundingBoxIn, 10 + 20, 20, 29 + 38 + 20, 11 + 20, 20, 29 + 38 + 20, MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.HALF, Half.TOP), MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState(), false);
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getCoordBaseMode().getOpposite(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestNearDoorPos = new BlockPos(this.getXWithOffset(10 + 20, 29 + 38 + 20), this.getYWithOffset(21), this.getZWithOffset(10 + 20, 29 + 38 + 20));
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getCoordBaseMode().getOpposite(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		//TODO Everything seems to generate several times over, first noticed with lotus flower entity, find better fix than below
		if(!createRan)
		{
			LotusFlowerEntity lotusFlowerEntity = MSEntityTypes.LOTUS_FLOWER.create(worldIn.getWorld());
			if(lotusFlowerEntity == null)
				throw new IllegalStateException("Unable to create a new lotus flower. Entity factory returned null!");
			lotusFlowerEntity.setLocationAndAngles(getEntityXWithOffset(21 + 20, 21 + 38 + 20), this.getYWithOffset(50), getEntityZWithOffset(21 + 20, 21 + 38 + 20), 0F, 0);
			worldIn.addEntity(lotusFlowerEntity);
			createRan = true;
		}
	}
	
	private void buildMainPlatform(BlockState block, IWorld world, Random rand, MutableBoundingBox boundingBox, int a)
	{
		fillWithBlocks(world, boundingBox, a * 2 + 20, 8 * a, a * 2 + 38 + 20, (int) (40 * (1F - a / 20F)) + 1 + 20, (8 * a) + 8, (int) (40 * (1F - a / 20F)) + 1 + 38 + 20, block, block, false);
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		int pushUp = 0;
		for(int i = 0; i < 24; i++)
		{
			fillWithBlocksCheckWater(world, boundingBox, 17 + 20, pushUp, i + 20 + 24, 24 + 20, pushUp, i + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs base
			fillWithBlocksCheckWater(world, boundingBox, 17 + 20, pushUp + 1, i + 20 + 24, 24 + 20, pushUp + 1, i + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs top
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp, i + 20 + 1 + 24, 24 + 20, pushUp, 50 + 20, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //stairs base fill in
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp + 1, i + 20 + 1 + 24, 24 + 20, pushUp + 1, 50 + 20, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //stairs top fill in
			pushUp = pushUp + 2; //allows the stairs height to increment twice as fast as sideways placement
		}
		
		fillWithBlocksCheckWater(world, boundingBox, 17 + 20, 48, 24 + 20 + 24, 24 + 20, 48, 24 + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs base at top
		fillWithBlocks(world, boundingBox, 17 + 20, -10, 20 + 24, 24 + 20, -1, 24 + 20 + 24, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //underneath stairs
		
		fillWithBlocks(world, boundingBox, 20, -10, 38 + 20, 41 + 20, 0, 79 + 20, block, block, false); //underneath main platform
		for(int i = 0; i < 28; i++)
		{
			fillWithBlocks(world, boundingBox, i + 20, -i - 10, i + 38 + 20, 41 - i + 20, -i - 10, 79 - i + 20, block, block, false); //underneath main platform
		}
	}
	
	private void buildWallsAndFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		fillWithBlocks(world, boundingBox, 14 + 20, 48, 51 + 20, 27 + 20, 48, 65 + 20, block, block, false); //lotus room floor
		fillWithRandomizedBlocks(world, boundingBox, 13 + 20, 50, 51 + 20, 28 + 20, 54, 66 + 20, true, rand, HIEROGLYPHS); //lotus room hieroglyphs
		fillWithBlocks(world, boundingBox, 13 + 20, 49, 51 + 20, 28 + 20, 49, 66 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 13 + 20, 55, 51 + 20, 28 + 20, 55, 66 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
		
		fillWithRandomizedBlocks(world, boundingBox, 6 + 20, 6, 44 + 20, 35 + 20, 15, 73 + 20, true, rand, HIEROGLYPHS); //lower room hieroglyphs
		fillWithBlocks(world, boundingBox, 6 + 20, 5, 44 + 20, 35 + 20, 5, 73 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 6 + 20, 16, 44 + 20, 35 + 20, 16, 73 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
	}
	
	private void buildIndoorBlocks(BlockState columnBlock, IWorld world, MutableBoundingBox boundingBox)
	{
		//lower room columns
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				fillWithBlocks(world, boundingBox, 10 + 20 + i * 10, 5, 48 + 20 + j * 10, 11 + 20 + i * 10, 23, 49 + 20 + j * 10, columnBlock.with(MSDirectionalBlock.FACING, Direction.UP), columnBlock.with(MSDirectionalBlock.FACING, Direction.UP), false);
				fillWithBlocks(world, boundingBox, 8 + 20 + i * 10, 16, 46 + 20 + j * 10, 13 + 20 + i * 10, 16, 51 + 20 + j * 10, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //platform around columns
			}
		}
		
		//lower room stepping stones
		fillWithBlocks(world, boundingBox, 24 + 20, 16, 68 + 20, 24 + 20, 16, 69 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //first step
		fillWithBlocks(world, boundingBox, 30 + 20, 16, 65 + 20, 31 + 20, 16, 65 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //second step
		fillWithBlocks(world, boundingBox, 27 + 20, 16, 58 + 20, 27 + 20, 16, 59 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //third step
		fillWithBlocks(world, boundingBox, 17 + 20, 16, 58 + 20, 17 + 20, 16, 59 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //fourth step
		fillWithBlocks(world, boundingBox, 10 + 20, 16, 55 + 20, 11 + 20, 16, 55 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //fifth step
		fillWithBlocks(world, boundingBox, 14 + 20, 16, 48 + 20, 14 + 20, 16, 49 + 20, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //sixth step
		
		//lower room ladder
		fillWithBlocks(world, boundingBox, 20 + 20, 5, 72 + 20, 21 + 20, 16, 72 + 20, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.SOUTH), Blocks.LADDER.getDefaultState(), false);
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithAir(world, boundingBox, 14 + 20, 49, 52 + 20, 27 + 20, 55, 65 + 20); //lotus room
		fillWithAirCheckWater(world, boundingBox, 19 + 20, 49, 49 + 20, 22 + 20, 52, 51 + 20); //lotus room entry
		fillWithAir(world, boundingBox, 7 + 20, 5, 45 + 20, 34 + 20, 23, 72 + 20); //lower room
		fillWithAirCheckWater(world, boundingBox, 19 + 20, 17, 71 + 20, 22 + 20, 20, 79 + 20); //lower room entry //TODO this entrance will occasionally generate below water and cause this to generate awkwardly
	}
	
	/*
	private void buildPillars(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random random) //TODO pieces of pillars often fail to generate, which is why it is not in use currently
	{
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 19 + 20, -20, 1, 22, 30, 4, block, block, false); //front/south pillar
				fillWithBlocks(world, boundingBox, 18 + 20, 31, 0, 23 + 20, 36, 5, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 1, 23, 30 - randReduction, 4, block, block, false); //front/south pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 30, 60 + 20, block, block, false); //west pillar
				fillWithBlocks(world, boundingBox, 0, 31, 56 + 20, 5, 36, 61 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 1, -20, 57 + 20, 4, 30 - randReduction, 60 + 20, block, block, false); //west pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 30, 108 + 20, block, block, false); //north pillar
				fillWithBlocks(world, boundingBox, 17 + 20, 31, 102 + 20, 22 + 20, 36, 109 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.CRUXITE_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 18 + 20, -20, 103 + 20, 21 + 20, 30 - randReduction, 108 + 20, block, block, false); //north pillar
			}
		}
		if(random.nextBoolean())
		{
			if(random.nextBoolean())
			{
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 30, 60 + 20, block, block, false); //east pillar
				fillWithBlocks(world, boundingBox, 66 + 20, 31, 56 + 20, 71 + 20, 36, 61 + 20, MSBlocks.GREEN_STONE.getDefaultState(), MSBlocks.URANIUM_BLOCK.getDefaultState(), false);
			} else
			{
				int randReduction = random.nextInt(5);
				fillWithBlocks(world, boundingBox, 67 + 20, -20, 57 + 20, 70 + 20, 30 - randReduction, 60 + 20, block, block, false); //east pillar
			}
		}
	}
	 */
	
	private void buildFrog(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, 23 + 20, 57, 14 + 38 + 20, 27 + 20, 58, 55 + 20, block, block, false); //right front leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 14 + 38 + 20, 18 + 20, 58, 55 + 20, block, block, false); //left front leg
		fillWithBlocks(world, boundingBox, 23 + 20, 57, 23 + 38 + 20, 27 + 20, 61, 64 + 20, block, block, false); //right back leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 23 + 38 + 20, 18 + 20, 61, 64 + 20, block, block, false); //left back leg
		fillWithBlocks(world, boundingBox, 16 + 20, 57, 16 + 38 + 20, 25 + 20, 65, 63 + 20, block, block, false); //body
		fillWithBlocks(world, boundingBox, 15 + 20, 62, 14 + 38 + 20, 26 + 20, 67, 59 + 20, block, block, false); //head
		fillWithBlocks(world, boundingBox, 23 + 20, 65, 15 + 38 + 20, 27 + 20, 68, 57 + 20, block, block, false); //right eye
		fillWithBlocks(world, boundingBox, 14 + 20, 65, 15 + 38 + 20, 18 + 20, 68, 57 + 20, block, block, false); //left eye
	}
	
	protected int getEntityXWithOffset(int x, int z)
	{
		int posX = getXWithOffset(x, z);
		if(getCoordBaseMode() == Direction.WEST) //fixes direction specific shifting which had been moving the lotus flower one block farther towards or away from the entrance
			posX++;
		return posX;
	}
	
	protected int getEntityZWithOffset(int x, int z)
	{
		int posZ = getZWithOffset(x, z);
		if(getCoordBaseMode() == Direction.NORTH)
			posZ++;
		return posZ;
	}
	
	protected void fillWithBlocksCheckWater(IWorld worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					if(this.getBlockStateFromPos(worldIn, x, y, z, boundingboxIn).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //normal fill command, except that it waterlogs blocks if it is replacing water and has inside vs outside blockstates removed + existingOnly
						blockState = blockState.with(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
					this.setBlockState(worldIn, blockState, x, y, z, boundingboxIn);
				}
			}
		}
	}
	
	protected void fillWithAirCheckWater(IWorld worldIn, MutableBoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		for(int y = minY; y <= maxY; ++y)
		{
			for(int x = minX; x <= maxX; ++x)
			{
				for(int z = minZ; z <= maxZ; ++z)
				{
					BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
					if(structurebb.isVecInside(pos) && !this.getBlockStateFromPos(worldIn, x, y, z, structurebb).getFluidState().getFluid().isEquivalentTo(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
						worldIn.removeBlock(pos, false);
				}
			}
		}
	}
	
	static class Selector extends StructurePiece.BlockSelector
	{
		private Selector()
		{
		}
		
		public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
		{
			int randomBlock = rand.nextInt(14);
			if(randomBlock == 12 || randomBlock == 13)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_FROG.getDefaultState();
			} else if(randomBlock == 10 || randomBlock == 11)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_TURTLE.getDefaultState();
			} else if(randomBlock == 8 || randomBlock == 9)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SKAIA.getDefaultState();
			} else if(randomBlock == 6 || randomBlock == 7)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_LOTUS.getDefaultState();
			} else if(randomBlock == 5)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT.getDefaultState();
			} else if(randomBlock == 4)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT.getDefaultState();
			} else if(randomBlock == 3)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_NAK_LEFT.getDefaultState();
			} else if(randomBlock == 2)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT.getDefaultState();
			} else if(randomBlock == 1)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT.getDefaultState();
			} else
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT.getDefaultState();
			}
		}
	}
}