package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.CustomShapeBlock;
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
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePiece extends ScatteredStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain objects(the lotus flower entity) from spawning several times over
	private static final FrogTemplePiece.Selector HIEROGLYPHS = new FrogTemplePiece.Selector();
	
	public FrogTemplePiece(ChunkGenerator generator, Random random, int x, int z)
	{
		super(MSStructurePieces.FROG_TEMPLE, random, x - 21, 64, z - 35, 42, 100, 70);
		
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.x0; xPos <= boundingBox.x1; xPos++)
			for(int zPos = boundingBox.z0; zPos <= boundingBox.z1; zPos++)
			{
				int posHeight = generator.getBaseHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		
		boundingBox.move(0, posHeightPicked - boundingBox.y0, 0); //takes the lowest Ocean Floor gen viable height
	}
	
	public FrogTemplePiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.FROG_TEMPLE, nbt);
		createRan = nbt.getBoolean("createRan");
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("createRan", createRan);
		super.addAdditionalSaveData(tagCompound);
	}
	
	@Override
	public boolean postProcess(ISeedReader worldIn, StructureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState wallBlock = MSBlocks.GREEN_STONE_BRICKS.defaultBlockState();
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_GREEN_STONE.defaultBlockState();
		BlockState stoneBlock = MSBlocks.GREEN_STONE.defaultBlockState();
		
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
	
	private void generateLoot(ISeedReader worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, ChunkPos chunkPos)
	{
		placeBlock(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.EAST), 21, 49, 20 + 14, boundingBox);
		placeBlock(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.NORTH), 21, 49, 21 + 14, boundingBox);
		placeBlock(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.WEST), 20, 49, 21 + 14, boundingBox);
		placeBlock(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.SOUTH), 20, 49, 20 + 14, boundingBox);
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getOrientation() == Direction.SOUTH || this.getOrientation() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestFarPos = new BlockPos(this.getWorldX(21, 12 + 14), this.getWorldY(21), this.getWorldZ(21, 12 + 14));
		generateBox(worldIn, boundingBoxIn, 20, 20, 12 + 14, 21, 20, 12 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.defaultBlockState().setValue(StairsBlock.HALF, Half.TOP).setValue(StairsBlock.FACING, Direction.SOUTH), MSBlocks.GREEN_STONE_BRICK_STAIRS.defaultBlockState(), false);
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getOrientation(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestFarPos = new BlockPos(this.getWorldX(20, 12 + 14), this.getWorldY(21), this.getWorldZ(20, 12 + 14));
		StructureBlockUtil.placeLootChest(chestFarPos, worldIn, boundingBoxIn, getOrientation(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		BlockPos chestNearDoorPos = new BlockPos(this.getWorldX(11, 29 + 14), this.getWorldY(21), this.getWorldZ(11, 29 + 14));
		generateBox(worldIn, boundingBoxIn, 10, 20, 29 + 14, 11, 20, 29 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.defaultBlockState().setValue(StairsBlock.HALF, Half.TOP), MSBlocks.GREEN_STONE_BRICK_STAIRS.defaultBlockState(), false);
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getOrientation().getOpposite(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestNearDoorPos = new BlockPos(this.getWorldX(10, 29 + 14), this.getWorldY(21), this.getWorldZ(10, 29 + 14));
		StructureBlockUtil.placeLootChest(chestNearDoorPos, worldIn, boundingBoxIn, getOrientation().getOpposite(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		Vector3i entityVec = new Vector3i(getEntityXWithOffset(21, 21 + 14), this.getWorldY(50), getEntityZWithOffset(21, 21 + 14)); //BlockPos also suitable instead of Vec3i
		if(!createRan && boundingBoxIn.isInside(entityVec))
		{
			LotusFlowerEntity lotusFlowerEntity = MSEntityTypes.LOTUS_FLOWER.create(worldIn.getLevel());
			if(lotusFlowerEntity == null)
				throw new IllegalStateException("Unable to create a new lotus flower. Entity factory returned null!");
			lotusFlowerEntity.moveTo(entityVec.getX(), entityVec.getY(), entityVec.getZ(), 0F, 0);
			lotusFlowerEntity.setInvulnerable(true);
			worldIn.addFreshEntity(lotusFlowerEntity);
			createRan = true;
		}
	}
	
	private void buildMainPlatform(BlockState block, ISeedReader world, MutableBoundingBox boundingBox, int a)
	{
		generateBox(world, boundingBox, a * 2, 8 * a, a * 2 + 14, (int) (40 * (1F - a / 20F)) + 1, (8 * a) + 8, (int) (40 * (1F - a / 20F)) + 1 + 14, block, block, false);
	}
	
	private void buildStairsAndUnderneath(BlockState block, ISeedReader world, MutableBoundingBox boundingBox)
	{
		int pushUp = 0;
		for(int i = 0; i < 24; i++)
		{
			fillWithBlocksCheckWater(world, boundingBox, 17, pushUp, i, 24, pushUp, i, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.defaultBlockState().setValue(CustomShapeBlock.FACING, this.getOrientation().getOpposite())); //stairs base
			fillWithBlocksCheckWater(world, boundingBox, 17, pushUp + 1, i, 24, pushUp + 1, i, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.defaultBlockState().setValue(CustomShapeBlock.FACING, this.getOrientation().getOpposite())); //stairs top
			generateBox(world, boundingBox, 17, pushUp, i + 1, 24, pushUp, 26, MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), false); //stairs base fill in
			generateBox(world, boundingBox, 17, pushUp + 1, i + 1, 24, pushUp + 1, 26, MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), false); //stairs top fill in
			pushUp = pushUp + 2; //allows the stairs height to increment twice as fast as sideways placement
		}
		
		fillWithBlocksCheckWater(world, boundingBox, 17, 48, 24, 24, 48, 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.defaultBlockState().setValue(CustomShapeBlock.FACING, this.getOrientation().getOpposite())); //stairs base at top
		generateBox(world, boundingBox, 17, -10, 0, 24, -1, 24, MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.defaultBlockState(), false); //underneath stairs
		
		generateBox(world, boundingBox, 0, -10, 14, 41, 0, 55, block, block, false); //underneath main platform
		for(int i = 0; i < 28; i++)
		{
			generateBox(world, boundingBox, i, -i - 10, i + 14, 41 - i, -i - 10, 55 - i, block, block, false); //underneath main platform
		}
	}
	
	private void buildWallsAndFloors(BlockState block, ISeedReader world, MutableBoundingBox boundingBox, Random rand)
	{
		generateBox(world, boundingBox, 14, 48, 27, 27, 48, 41, block, block, false); //lotus room floor
		generateBox(world, boundingBox, 13, 50, 27, 28, 54, 42, true, rand, HIEROGLYPHS); //lotus room hieroglyphs
		generateBox(world, boundingBox, 13, 49, 27, 28, 49, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState(), false); //lotus room trim floor
		generateBox(world, boundingBox, 13, 55, 27, 28, 55, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState(), false); //lotus room trim ceiling
		
		generateBox(world, boundingBox, 6, 6, 20, 35, 15, 49, true, rand, HIEROGLYPHS); //lower room hieroglyphs
		generateBox(world, boundingBox, 6, 5, 20, 35, 5, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState(), false); //lotus room trim floor
		generateBox(world, boundingBox, 6, 16, 20, 35, 16, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.defaultBlockState(), false); //lotus room trim ceiling
	}
	
	private void buildIndoorBlocks(BlockState columnBlock, ISeedReader world, MutableBoundingBox boundingBox)
	{
		//lower room columns
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				generateBox(world, boundingBox, 10 + i * 10, 5, 24 + j * 10, 11 + i * 10, 23, 25 + j * 10, columnBlock.setValue(MSDirectionalBlock.FACING, Direction.UP), columnBlock.setValue(MSDirectionalBlock.FACING, Direction.UP), false);
				generateBox(world, boundingBox, 8 + i * 10, 16, 22 + j * 10, 13 + i * 10, 16, 27 + j * 10, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //platform around columns
			}
		}
		
		//lower room stepping stones
		generateBox(world, boundingBox, 24, 16, 44, 24, 16, 45, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //first step
		generateBox(world, boundingBox, 30, 16, 41, 31, 16, 41, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //second step
		generateBox(world, boundingBox, 27, 16, 34, 27, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //third step
		generateBox(world, boundingBox, 17, 16, 34, 17, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //fourth step
		generateBox(world, boundingBox, 10, 16, 31, 11, 16, 31, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //fifth step
		generateBox(world, boundingBox, 14, 16, 24, 14, 16, 25, MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.defaultBlockState(), false); //sixth step
		
		//lower room ladder
		generateBox(world, boundingBox, 20, 5, 48, 21, 16, 48, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH), Blocks.LADDER.defaultBlockState(), false);
	}
	
	private void carveRooms(ISeedReader world, MutableBoundingBox boundingBox)
	{
		generateAirBox(world, boundingBox, 14, 49, 28, 27, 55, 41); //lotus room
		fillWithAirCheckWater(world, boundingBox, 19, 49, 25, 22, 52, 27); //lotus room entry
		generateAirBox(world, boundingBox, 7, 5, 21, 34, 23, 48); //lower room
		fillWithAirCheckWater(world, boundingBox, 19, 17, 47, 22, 20, 55); //lower room entry //TODO this entrance will occasionally generate below water and cause this to generate awkwardly
	}
	
	private void buildFrog(BlockState block, ISeedReader world, MutableBoundingBox boundingBox)
	{
		generateBox(world, boundingBox, 23, 57, 14 + 14, 27, 58, 31, block, block, false); //right front leg
		generateBox(world, boundingBox, 14, 57, 14 + 14, 18, 58, 31, block, block, false); //left front leg
		generateBox(world, boundingBox, 23, 57, 23 + 14, 27, 61, 40, block, block, false); //right back leg
		generateBox(world, boundingBox, 14, 57, 23 + 14, 18, 61, 40, block, block, false); //left back leg
		generateBox(world, boundingBox, 16, 57, 16 + 14, 25, 65, 39, block, block, false); //body
		generateBox(world, boundingBox, 15, 62, 14 + 14, 26, 67, 35, block, block, false); //head
		generateBox(world, boundingBox, 23, 65, 15 + 14, 27, 68, 33, block, block, false); //right eye
		generateBox(world, boundingBox, 14, 65, 15 + 14, 18, 68, 33, block, block, false); //left eye
	}
	
	protected int getEntityXWithOffset(int x, int z)
	{
		int posX = getWorldX(x, z);
		if(getOrientation() == Direction.WEST) //fixes direction specific shifting which had been moving the lotus flower one block farther towards or away from the entrance
			posX++;
		return posX;
	}
	
	protected int getEntityZWithOffset(int x, int z)
	{
		int posZ = getWorldZ(x, z);
		if(getOrientation() == Direction.NORTH)
			posZ++;
		return posZ;
	}
	
	protected void fillWithBlocksCheckWater(ISeedReader worldIn, MutableBoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					if(this.getBlock(worldIn, x, y, z, boundingboxIn).getFluidState().getType().isSame(Fluids.WATER)) //normal fill command, except that it waterlogs blocks if it is replacing water and has inside vs outside blockstates removed + existingOnly
						blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
					this.placeBlock(worldIn, blockState, x, y, z, boundingboxIn);
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
					BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
					if(structurebb.isInside(pos) && !this.getBlock(worldIn, x, y, z, structurebb).getFluidState().getType().isSame(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
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
		
		@Override
		public void next(Random rand, int x, int y, int z, boolean wall)
		{
			int randomBlock = rand.nextInt(14);
			if(randomBlock == 12 || randomBlock == 13)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_FROG.defaultBlockState();
			} else if(randomBlock == 10 || randomBlock == 11)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_TURTLE.defaultBlockState();
			} else if(randomBlock == 8 || randomBlock == 9)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_SKAIA.defaultBlockState();
			} else if(randomBlock == 6 || randomBlock == 7)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_LOTUS.defaultBlockState();
			} else if(randomBlock == 5)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT.defaultBlockState();
			} else if(randomBlock == 4)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT.defaultBlockState();
			} else if(randomBlock == 3)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_NAK_LEFT.defaultBlockState();
			} else if(randomBlock == 2)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT.defaultBlockState();
			} else if(randomBlock == 1)
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT.defaultBlockState();
			} else
			{
				this.next = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT.defaultBlockState();
			}
		}
	}
}