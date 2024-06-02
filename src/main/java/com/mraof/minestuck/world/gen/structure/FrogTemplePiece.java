package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.Lists;
import com.mraof.minestuck.block.CustomShapeBlock;
import com.mraof.minestuck.block.LotusTimeCapsuleBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class FrogTemplePiece extends CoreCompatibleScatteredStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain objects(the lotus flower entity) from spawning several times over
	private static final FrogTemplePiece.Selector HIEROGLYPHS = new FrogTemplePiece.Selector();
	
	public FrogTemplePiece(ChunkGenerator generator, LevelHeightAccessor level, RandomState randomState, RandomSource random, int x, int z)
	{
		super(MSStructures.FROG_TEMPLE_PIECE.get(), x - 21, 64, z - 35, 42, 100, 70, getRandomHorizontalDirection(random));
		
		int posHeightPicked = generator.getBaseHeight((boundingBox.minX() + boundingBox.maxX())/2,
				(boundingBox.minZ() + boundingBox.maxZ())/2, Heightmap.Types.OCEAN_FLOOR_WG, level, randomState);
		
		
		boundingBox = boundingBox.moved(0, posHeightPicked - boundingBox.minY(), 0); //takes the lowest Ocean Floor gen viable height
	}
	
	public FrogTemplePiece(CompoundTag nbt)
	{
		super(MSStructures.FROG_TEMPLE_PIECE.get(), nbt);
		createRan = nbt.getBoolean("createRan");
	}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("createRan", createRan);
		super.addAdditionalSaveData(context, tagCompound);
	}
	
	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
	{
		BlockState wallBlock = MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState();
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.get().defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_GREEN_STONE.get().defaultBlockState();
		BlockState stoneBlock = MSBlocks.GREEN_STONE.get().defaultBlockState();
		
		buildMainPlatform(wallBlock, level, boundingBoxIn);
		
		buildStairsAndUnderneath(wallBlock, level, boundingBoxIn);
		buildWallsAndFloors(floorBlock, level, boundingBoxIn, randomIn);
		carveRooms(level, boundingBoxIn);
		buildIndoorBlocks(columnBlock, level, boundingBoxIn);
		generateLoot(level, boundingBoxIn, randomIn, chunkPosIn);
		buildFrog(stoneBlock, level, boundingBoxIn);
	}
	
	private void generateLoot(WorldGenLevel level, BoundingBox box, RandomSource randomIn, ChunkPos chunkPos)
	{
		placeBlock(level, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.EAST), 21, 49, 20 + 14, box);
		placeBlock(level, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.NORTH), 21, 49, 21 + 14, box);
		placeBlock(level, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.WEST), 20, 49, 21 + 14, box);
		placeBlock(level, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.CORNER.get().defaultBlockState().setValue(LotusTimeCapsuleBlock.FACING, Direction.SOUTH), 20, 49, 20 + 14, box);
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getOrientation() == Direction.SOUTH || this.getOrientation() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestFarPos = new BlockPos(this.getWorldX(21, 12 + 14), this.getWorldY(21), this.getWorldZ(21, 12 + 14));
		generateBox(level, box, 20, 20, 12 + 14, 21, 20, 12 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.get().defaultBlockState().setValue(StairBlock.HALF, Half.TOP).setValue(StairBlock.FACING, Direction.SOUTH), MSBlocks.GREEN_STONE_BRICK_STAIRS.get().defaultBlockState(), false);
		StructureBlockUtil.placeLootChest(chestFarPos, level, box, getOrientation(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestFarPos = new BlockPos(this.getWorldX(20, 12 + 14), this.getWorldY(21), this.getWorldZ(20, 12 + 14));
		StructureBlockUtil.placeLootChest(chestFarPos, level, box, getOrientation(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		BlockPos chestNearDoorPos = new BlockPos(this.getWorldX(11, 29 + 14), this.getWorldY(21), this.getWorldZ(11, 29 + 14));
		generateBox(level, box, 10, 20, 29 + 14, 11, 20, 29 + 14, MSBlocks.GREEN_STONE_BRICK_STAIRS.get().defaultBlockState().setValue(StairBlock.HALF, Half.TOP), MSBlocks.GREEN_STONE_BRICK_STAIRS.get().defaultBlockState(), false);
		StructureBlockUtil.placeLootChest(chestNearDoorPos, level, box, getOrientation().getOpposite(), leftChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestNearDoorPos = new BlockPos(this.getWorldX(10, 29 + 14), this.getWorldY(21), this.getWorldZ(10, 29 + 14));
		StructureBlockUtil.placeLootChest(chestNearDoorPos, level, box, getOrientation().getOpposite(), rightChestType, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		
		Vec3i entityVec = new Vec3i(getEntityXWithOffset(21, 21 + 14), this.getWorldY(50), getEntityZWithOffset(21, 21 + 14)); //BlockPos also suitable instead of Vec3i
		if(!createRan && box.isInside(entityVec))
		{
			LotusFlowerEntity lotusFlowerEntity = MSEntityTypes.LOTUS_FLOWER.get().create(level.getLevel());
			if(lotusFlowerEntity == null)
				throw new IllegalStateException("Unable to create a new lotus flower. Entity factory returned null!");
			lotusFlowerEntity.moveTo(entityVec.getX(), entityVec.getY(), entityVec.getZ(), 0F, 0);
			lotusFlowerEntity.setInvulnerable(true);
			level.addFreshEntity(lotusFlowerEntity);
			createRan = true;
		}
	}
	
	private void buildMainPlatform(BlockState block, WorldGenLevel level, BoundingBox box)
	{
		for(int a = 0; a < 7; a++)
		{
			generateBox(level, box, a * 2, 8 * a, a * 2 + 14, (int) (40 * (1F - a / 20F)) + 1, (8 * a) + 8, (int) (40 * (1F - a / 20F)) + 1 + 14, block, block, false);
		}
		
	}
	
	private void buildStairsAndUnderneath(BlockState block, WorldGenLevel level, BoundingBox boundingBox)
	{
		int pushUp = 0;
		for(int i = 0; i < 24; i++)
		{
			fillWithBlocksCheckWater(level, boundingBox, 17, pushUp, i, 24, pushUp, i, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get().defaultBlockState().setValue(CustomShapeBlock.FACING, Direction.SOUTH)); //stairs base
			fillWithBlocksCheckWater(level, boundingBox, 17, pushUp + 1, i, 24, pushUp + 1, i, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.get().defaultBlockState().setValue(CustomShapeBlock.FACING, Direction.SOUTH)); //stairs top
			generateBox(level, boundingBox, 17, pushUp, i + 1, 24, pushUp, 26, MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), false); //stairs base fill in
			generateBox(level, boundingBox, 17, pushUp + 1, i + 1, 24, pushUp + 1, 26, MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), false); //stairs top fill in
			pushUp = pushUp + 2; //allows the stairs height to increment twice as fast as sideways placement
		}
		
		fillWithBlocksCheckWater(level, boundingBox, 17, 48, 24, 24, 48, 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.get().defaultBlockState().setValue(CustomShapeBlock.FACING, Direction.SOUTH)); //stairs base at top
		generateBox(level, boundingBox, 17, -10, 0, 24, -1, 24, MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.GREEN_STONE_BRICKS.get().defaultBlockState(), false); //underneath stairs
		
		generateBox(level, boundingBox, 0, -10, 14, 41, 0, 55, block, block, false); //underneath main platform
		for(int i = 0; i < 28; i++)
		{
			generateBox(level, boundingBox, i, -i - 10, i + 14, 41 - i, -i - 10, 55 - i, block, block, false); //underneath main platform
		}
	}
	
	private void buildWallsAndFloors(BlockState block, WorldGenLevel level, BoundingBox boundingBox, RandomSource rand)
	{
		generateBox(level, boundingBox, 14, 48, 27, 27, 48, 41, block, block, false); //lotus room floor
		generateBox(level, boundingBox, 13, 50, 27, 28, 54, 42, true, rand, HIEROGLYPHS); //lotus room hieroglyphs
		generateBox(level, boundingBox, 13, 49, 27, 28, 49, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState(), false); //lotus room trim floor
		generateBox(level, boundingBox, 13, 55, 27, 28, 55, 42, MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState(), false); //lotus room trim ceiling
		
		generateBox(level, boundingBox, 6, 6, 20, 35, 15, 49, true, rand, HIEROGLYPHS); //lower room hieroglyphs
		generateBox(level, boundingBox, 6, 5, 20, 35, 5, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState(), false); //lotus room trim floor
		generateBox(level, boundingBox, 6, 16, 20, 35, 16, 49, MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState().setValue(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.get().defaultBlockState(), false); //lotus room trim ceiling
	}
	
	private void buildIndoorBlocks(BlockState columnBlock, WorldGenLevel level, BoundingBox boundingBox)
	{
		//lower room columns
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				generateBox(level, boundingBox, 10 + i * 10, 5, 24 + j * 10, 11 + i * 10, 23, 25 + j * 10, columnBlock.setValue(MSDirectionalBlock.FACING, Direction.UP), columnBlock.setValue(MSDirectionalBlock.FACING, Direction.UP), false);
				generateBox(level, boundingBox, 8 + i * 10, 16, 22 + j * 10, 13 + i * 10, 16, 27 + j * 10, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //platform around columns
			}
		}
		
		//lower room stepping stones
		generateBox(level, boundingBox, 24, 16, 44, 24, 16, 45, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //first step
		generateBox(level, boundingBox, 30, 16, 41, 31, 16, 41, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //second step
		generateBox(level, boundingBox, 27, 16, 34, 27, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //third step
		generateBox(level, boundingBox, 17, 16, 34, 17, 16, 35, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //fourth step
		generateBox(level, boundingBox, 10, 16, 31, 11, 16, 31, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //fifth step
		generateBox(level, boundingBox, 14, 16, 24, 14, 16, 25, MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), MSBlocks.CHISELED_GREEN_STONE_BRICKS.get().defaultBlockState(), false); //sixth step
		
		//lower room ladder
		generateBox(level, boundingBox, 20, 5, 49, 21, 16, 49, MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER.get().defaultBlockState().setValue(CustomShapeBlock.FACING, Direction.SOUTH), MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER.get().defaultBlockState(), false);
	}
	
	private void carveRooms(WorldGenLevel level, BoundingBox boundingBox)
	{
		generateAirBox(level, boundingBox, 14, 49, 28, 27, 55, 41); //lotus room
		fillWithAirCheckWater(level, boundingBox, 19, 49, 25, 22, 52, 27); //lotus room entry
		generateAirBox(level, boundingBox, 7, 5, 21, 34, 23, 48); //lower room
		fillWithAirCheckWater(level, boundingBox, 19, 17, 47, 22, 20, 55); //lower room entry //TODO this entrance will occasionally generate below water and cause this to generate awkwardly
	}
	
	private void buildFrog(BlockState block, WorldGenLevel level, BoundingBox boundingBox)
	{
		generateBox(level, boundingBox, 23, 57, 14 + 14, 27, 58, 31, block, block, false); //right front leg
		generateBox(level, boundingBox, 14, 57, 14 + 14, 18, 58, 31, block, block, false); //left front leg
		generateBox(level, boundingBox, 23, 57, 23 + 14, 27, 61, 40, block, block, false); //right back leg
		generateBox(level, boundingBox, 14, 57, 23 + 14, 18, 61, 40, block, block, false); //left back leg
		generateBox(level, boundingBox, 16, 57, 16 + 14, 25, 65, 39, block, block, false); //body
		generateBox(level, boundingBox, 15, 62, 14 + 14, 26, 67, 35, block, block, false); //head
		generateBox(level, boundingBox, 23, 65, 15 + 14, 27, 68, 33, block, block, false); //right eye
		generateBox(level, boundingBox, 14, 65, 15 + 14, 18, 68, 33, block, block, false); //left eye
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
	
	protected void fillWithBlocksCheckWater(WorldGenLevel level, BoundingBox boundingboxIn, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, BlockState blockState)
	{
		for(int y = yMin; y <= yMax; ++y)
		{
			for(int x = xMin; x <= xMax; ++x)
			{
				for(int z = zMin; z <= zMax; ++z)
				{
					if(this.getBlock(level, x, y, z, boundingboxIn).getFluidState().getType().isSame(Fluids.WATER)) //normal fill command, except that it waterlogs blocks if it is replacing water and has inside vs outside blockstates removed + existingOnly
						blockState = blockState.setValue(BlockStateProperties.WATERLOGGED, true); //only works with waterloggable blocks
					this.placeBlock(level, blockState, x, y, z, boundingboxIn);
				}
			}
		}
	}
	
	protected void fillWithAirCheckWater(LevelAccessor level, BoundingBox structurebb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
	{
		for(int y = minY; y <= maxY; ++y)
		{
			for(int x = minX; x <= maxX; ++x)
			{
				for(int z = minZ; z <= maxZ; ++z)
				{
					BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
					if(structurebb.isInside(pos) && !this.getBlock(level, x, y, z, structurebb).getFluidState().getType().isSame(Fluids.WATER)) //ensures that the chunk is loaded before attempted to remove block, setBlockState already does this check
						level.removeBlock(pos, false);
				}
			}
		}
	}
	
	private static List<WeightedEntry.Wrapper<Block>> buildWeightedList()
	{
		List<WeightedEntry.Wrapper<Block>> weightedBlockList = Lists.newArrayList();
		
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_FROG.get(), 1)); //only one frog hieroglyph in order to have a component more difficult to find
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_TURTLE.get(), 23));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_SKAIA.get(), 23));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_LOTUS.get(), 23));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT.get(), 10));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT.get(), 10));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_NAK_LEFT.get(), 10));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT.get(), 10));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT.get(), 10));
		weightedBlockList.add(WeightedEntry.wrap(MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT.get(), 10));
		
		return weightedBlockList;
	}
	
	static class Selector extends StructurePiece.BlockSelector
	{
		private final List<WeightedEntry.Wrapper<Block>> weightedBlockList = buildWeightedList();
		private final int totalWeight = WeightedRandom.getTotalWeight(weightedBlockList);
		
		private Selector()
		{
		}
		
		@Override
		public void next(RandomSource rand, int x, int y, int z, boolean wall)
		{
			WeightedEntry.Wrapper<Block> wrappedBlock = WeightedRandom.getRandomItem(rand, weightedBlockList, totalWeight).orElseThrow();
			this.next = wrappedBlock.getData().defaultBlockState(); //sets the next blockstate to an element of the weighted list as long as the optional is present
		}
	}
}
