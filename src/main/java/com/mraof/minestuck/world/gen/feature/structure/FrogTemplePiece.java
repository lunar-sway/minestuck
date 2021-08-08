package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.DecorBlock;
import com.mraof.minestuck.block.LotusTimeCapsuleBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
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
		super(MSStructurePieces.FROG_TEMPLE, random, x - 21, 64, z - 35, 42, 100, 70);
		
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
			buildMainPlatform(wallBlock, worldIn, boundingBoxIn, a);
		}
		
		buildStairsAndUnderneath(wallBlock, worldIn, boundingBoxIn);
		buildWallsAndFloors(floorBlock, worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		buildIndoorBlocks(columnBlock, worldIn, boundingBoxIn);
		generateLoot(worldIn, boundingBoxIn, randomIn, chunkPosIn);
		buildFrog(stoneBlock, worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, ChunkPos chunkPos)
	{
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.EAST), 21, 49, 20 + 14, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.NORTH), 21, 49, 21 + 14, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.WEST), 20, 49, 21 + 14, boundingBox);
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.SOUTH), 20, 49, 20 + 14, boundingBox);
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getCoordBaseMode() == Direction.SOUTH || this.getCoordBaseMode() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestFarPos = new BlockPos(this.getXWithOffset(21, 12 + 14), this.getYWithOffset(21), this.getZWithOffset(21, 12 + 14));
		fillWithBlocks(worldIn, boundingBoxIn, 20, 20, 12 + 14, 21, 20, 12 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.SOUTH), MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState(), false);
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getCoordBaseMode(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestFarPos = new BlockPos(this.getXWithOffset(20, 12 + 14), this.getYWithOffset(21), this.getZWithOffset(20, 12 + 14));
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getCoordBaseMode(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		BlockPos chestNearDoorPos = new BlockPos(this.getXWithOffset(11, 29 + 14), this.getYWithOffset(21), this.getZWithOffset(11, 29 + 14));
		fillWithBlocks(worldIn, boundingBoxIn, 10, 20, 29 + 14, 11, 20, 29 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState().with(StairsBlock.HALF, Half.TOP), MSBlocks.GREEN_STONE_BRICK_STAIRS.getDefaultState(), false);
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getCoordBaseMode().getOpposite(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestNearDoorPos = new BlockPos(this.getXWithOffset(10, 29 + 14), this.getYWithOffset(21), this.getZWithOffset(10, 29 + 14));
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getCoordBaseMode().getOpposite(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		//TODO Find out if there is a better fix than the createRan boolean to prevent the lotus entity from generating several times
		if(!createRan)
		{
			LotusFlowerEntity lotusFlowerEntity = MSEntityTypes.LOTUS_FLOWER.create(worldIn.getWorld());
			if(lotusFlowerEntity == null)
				throw new IllegalStateException("Unable to create a new lotus flower. Entity factory returned null!");
			lotusFlowerEntity.setLocationAndAngles(getEntityXWithOffset(21, 21 + 14), this.getYWithOffset(50), getEntityZWithOffset(21, 21 + 14), 0F, 0);
			worldIn.addEntity(lotusFlowerEntity);
			createRan = true;
		}
	}
	
	private void buildMainPlatform(BlockState block, IWorld world, MutableBoundingBox boundingBox, int a)
	{
		fillWithBlocks(world, boundingBox, a * 2, 8 * a, a * 2 + 14, (int) (40 * (1F - a / 20F)) + 1, (8 * a) + 8, (int) (40 * (1F - a / 20F)) + 1 + 14, block, block, false);
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		int pushUp = 0;
		for(int i = 0; i < 24; i++)
		{
			StructureBlockUtil.fillWithBlocksCheckWater(world, boundingBox,
					getXWithOffset(17, i), getYWithOffset(pushUp), getZWithOffset(17, i),
					getXWithOffset(24, i), getYWithOffset(pushUp), getZWithOffset(24, i),
					MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs base
			StructureBlockUtil.fillWithBlocksCheckWater(world, boundingBox,
					getXWithOffset(17, i), getYWithOffset(pushUp + 1), getZWithOffset(17, i),
					getXWithOffset(24, i), getYWithOffset(pushUp + 1), getZWithOffset(24, i),
					MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs top
			fillWithBlocks(world, boundingBox, 17, pushUp, i + 1, 24, pushUp, 26, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //stairs base fill in
			fillWithBlocks(world, boundingBox, 17, pushUp + 1, i + 1, 24, pushUp + 1, 26, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //stairs top fill in
			pushUp = pushUp + 2; //allows the stairs height to increment twice as fast as sideways placement
		}
		
		StructureBlockUtil.fillWithBlocksCheckWater(world, boundingBox, 17, 48, 24, 24, 48, 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite())); //stairs base at top
		fillWithBlocks(world, boundingBox, 17, -10, 20 + 24, 24, -1, 24, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), false); //underneath stairs
		
		fillWithBlocks(world, boundingBox, 20, -10, 14, 41, 0, 55, block, block, false); //underneath main platform
		for(int i = 0; i < 28; i++)
		{
			fillWithBlocks(world, boundingBox, i, -i - 10, i + 14, 41 - i, -i - 10, 55 - i, block, block, false); //underneath main platform
		}
	}
	
	private void buildWallsAndFloors(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		fillWithBlocks(world, boundingBox, 14, 48, 27, 27, 48, 41, block, block, false); //lotus room floor
		fillWithRandomizedBlocks(world, boundingBox, 13, 50, 27, 28, 54, 42, true, rand, HIEROGLYPHS); //lotus room hieroglyphs
		fillWithBlocks(world, boundingBox, 13, 49, 27, 28, 49, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 13, 55, 27, 28, 55, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
		
		fillWithRandomizedBlocks(world, boundingBox, 6, 6, 20, 35, 15, 49, true, rand, HIEROGLYPHS); //lower room hieroglyphs
		fillWithBlocks(world, boundingBox, 6, 5, 20, 35, 5, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 6, 16, 20, 35, 16, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
	}
	
	private void buildIndoorBlocks(BlockState columnBlock, IWorld world, MutableBoundingBox boundingBox)
	{
		//lower room columns
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				fillWithBlocks(world, boundingBox, 10 + i * 10, 5, 24 + j * 10, 11 + i * 10, 23, 25 + j * 10, columnBlock.with(MSDirectionalBlock.FACING, Direction.UP), columnBlock.with(MSDirectionalBlock.FACING, Direction.UP), false);
				fillWithBlocks(world, boundingBox, 8 + i * 10, 16, 22 + j * 10, 13 + i * 10, 16, 27 + j * 10, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //platform around columns
			}
		}
		
		//lower room stepping stones
		fillWithBlocks(world, boundingBox, 24, 16, 44, 24, 16, 45, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //first step
		fillWithBlocks(world, boundingBox, 30, 16, 41, 31, 16, 41, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //second step
		fillWithBlocks(world, boundingBox, 27, 16, 34, 27, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //third step
		fillWithBlocks(world, boundingBox, 17, 16, 34, 17, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //fourth step
		fillWithBlocks(world, boundingBox, 10, 16, 31, 11, 16, 31, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //fifth step
		fillWithBlocks(world, boundingBox, 14, 16, 24, 14, 16, 25, MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.getDefaultState(), false); //sixth step
		
		//lower room ladder
		fillWithBlocks(world, boundingBox, 20, 5, 48, 21, 16, 48, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.SOUTH), Blocks.LADDER.getDefaultState(), false);
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithAir(world, boundingBox, 14, 49, 28, 27, 55, 41); //lotus room
		fillWithAirCheckWater(world, boundingBox, 19, 49, 25, 22, 52, 27); //lotus room entry
		fillWithAir(world, boundingBox, 7, 5, 21, 34, 23, 48); //lower room
		fillWithAirCheckWater(world, boundingBox, 19, 17, 47, 22, 20, 55); //lower room entry //TODO this entrance will occasionally generate below water and cause this to generate awkwardly
	}
	
	private void buildFrog(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithBlocks(world, boundingBox, 23, 57, 14 + 14, 27, 58, 31, block, block, false); //right front leg
		fillWithBlocks(world, boundingBox, 14, 57, 14 + 14, 18, 58, 31, block, block, false); //left front leg
		fillWithBlocks(world, boundingBox, 23, 57, 23 + 14, 27, 61, 40, block, block, false); //right back leg
		fillWithBlocks(world, boundingBox, 14, 57, 23 + 14, 18, 61, 40, block, block, false); //left back leg
		fillWithBlocks(world, boundingBox, 16, 57, 16 + 14, 25, 65, 39, block, block, false); //body
		fillWithBlocks(world, boundingBox, 15, 62, 14 + 14, 26, 67, 35, block, block, false); //head
		fillWithBlocks(world, boundingBox, 23, 65, 15 + 14, 27, 68, 33, block, block, false); //right eye
		fillWithBlocks(world, boundingBox, 14, 65, 15 + 14, 18, 68, 33, block, block, false); //left eye
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