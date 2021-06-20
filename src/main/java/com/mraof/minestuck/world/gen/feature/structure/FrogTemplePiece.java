package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.DecorBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.JunglePyramidPiece;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class FrogTemplePiece extends ScatteredStructurePiece
{
	private final boolean[] torches = new boolean[4];
	private boolean placedChest;
	private int randomDepth;
	private boolean createRan = false;
	private static final FrogTemplePiece.Selector HIEROGLYPHS = new FrogTemplePiece.Selector();
	
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
		BlockState wallBlock = MSBlocks.GREEN_STONE_BRICKS.getDefaultState();
		BlockState columnBlock = MSBlocks.GREEN_STONE_COLUMN.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP);
		BlockState floorBlock = MSBlocks.POLISHED_GREEN_STONE.getDefaultState();
		BlockState stoneBlock = MSBlocks.GREEN_STONE.getDefaultState();
		
		randomDepth = randomIn.nextInt(5);
		
		Debug.debugf("create function has run");
		for(int a = 0; a < 7; a++)
		{
			buildMainPlatform(wallBlock, worldIn, randomIn, boundingBoxIn, a);
		}
		
		buildStairsAndUnderneath(wallBlock, worldIn, boundingBoxIn);
		buildInsides(floorBlock, worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		generateLoot(worldIn, boundingBoxIn, randomIn, this.getCoordBaseMode().getOpposite(), chunkPosIn);
		buildPillars(columnBlock, worldIn, boundingBoxIn, randomIn);
		buildFrog(stoneBlock, worldIn, boundingBoxIn);
		
		return true;
	}
	
	private void generateLoot(IWorld worldIn, MutableBoundingBox boundingBoxIn, Random randomIn, Direction direction, ChunkPos chunkPos)
	{
		Debug.debugf("generateLoot");
		//Direction modifiedDirection = direction.rotateYCCW();
		//setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, this.getCoordBaseMode()), 20 + 20, 17, 11 + 38 + 20, boundingBox);
		//setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, this.getCoordBaseMode()), 19 + 20, 17, 11 + 38 + 20, boundingBox);
		BlockPos chestPos = new BlockPos(this.getXWithOffset(19 + 20, 11 + 38 + 20), this.getYWithOffset(17), this.getZWithOffset(19 + 20, 11 + 38 + 20));
		StructureBlockUtil.placeLootChest(chestPos, worldIn, boundingBoxIn, getCoordBaseMode(), ChestType.RIGHT, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		chestPos = new BlockPos(this.getXWithOffset(18 + 20, 11 + 38 + 20), this.getYWithOffset(17), this.getZWithOffset(18 + 20, 11 + 38 + 20));
		StructureBlockUtil.placeLootChest(chestPos, worldIn, boundingBoxIn, getCoordBaseMode(), ChestType.LEFT, MSLootTables.FROG_TEMPLE_CHEST, randomIn);
		//setBlockState(worldIn, Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, direction), 20 + 20, 17, 11 + 38 + 20, boundingBox);
		
		/*
		//TODO There is only a percent chance of the rotateYCCW applying correctly
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.NORTH), 21 + 20, 49, 23 + 38 + 20, boundingBox);
		//modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.EAST), 21 + 20, 49, 22 + 38 + 20, boundingBox);
		//modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.SOUTH), 20 + 20, 49, 22 + 38 + 20, boundingBox);
		//modifiedDirection = modifiedDirection.rotateYCCW();
		setBlockState(worldIn, MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.getMainBlock().getDefaultState().with(LotusTimeCapsuleBlock.FACING, Direction.WEST), 20 + 20, 49, 23 + 38 + 20, boundingBox);
		*/
		
		BlockPos lotusBlockPos = new BlockPos(this.getXWithOffset(20 + 20, 22 + 38 + 20), this.getYWithOffset(49), this.getZWithOffset(20 + 20, 22 + 38 + 20)); //TODO temples whose main entrance opens south(and presumedly west) generate one block closer to entrance
		MSBlocks.LOTUS_TIME_CAPSULE_BLOCK.placeWithRotation(worldIn, lotusBlockPos, getRotation());
		
		//TODO Everything seems to generate several times over, first noticed with lotus flower entity, find better fix than below
		
		if(!createRan)
		{
			LotusFlowerEntity lotusFlowerEntity = MSEntityTypes.LOTUS_FLOWER.create(worldIn.getWorld());
			if(lotusFlowerEntity == null)
				throw new IllegalStateException("Unable to create a new lotus flower. Entity factory returned null!");
			lotusFlowerEntity.enablePersistence();
			lotusFlowerEntity.setNoAI(true);
			lotusFlowerEntity.setLocationAndAngles(this.getXWithOffset(20 + 20, 22 + 38 + 20) + 0.5D, this.getYWithOffset(50), this.getZWithOffset(20 + 20, 22 + 38 + 20) + 0.5D, 0F, 0);
			worldIn.addEntity(lotusFlowerEntity);
			createRan = true;
		}
		
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
		fillWithBlocks(world, boundingBox, a * 2 + 20, 8 * a, a * 2 + 38 + 20, (int) (40 * (1F - a / 20F)) + 1 + 20, (8 * a) + 8, (int) (40 * (1F - a / 20F)) + 1 + 38 + 20, block, block, false);
		//fillWithBlocks(world, boundingBox, a * 2 + 20, 8 * a, a * 2 + 37 + 20, (int) (45 * (1F - a / 20F)) + 20, (8 * a) + 8, (int) (45 * (1F - a / 20F)) + 36 + 20, block, block, false);
		
		if(a == 0)
		{
			//fillWithBlocks(world, boundingBox, 0, 0, 0, 0, 16, 0, Blocks.DIAMOND_BLOCK.getDefaultState(), Blocks.DIAMOND_BLOCK.getDefaultState(), false);
		}
	}
	
	private void buildStairsAndUnderneath(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		int pushUp = 0;
		for(int i = 0; i < 24; i++)
		{
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp, i + 20 + 24, 24 + 20, pushUp, i + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), false); //stairs base
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp + 1, i + 20 + 24, 24 + 20, pushUp + 1, i + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_TOP.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), false); //stairs top
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp, i + 20 + 1 + 24, 24 + 20, pushUp, 50 + 20, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(),false); //stairs base fill in
			fillWithBlocks(world, boundingBox, 17 + 20, pushUp + 1, i + 20 + 1 + 24, 24 + 20, pushUp + 1, 50 + 20, MSBlocks.GREEN_STONE_BRICKS.getDefaultState(), MSBlocks.GREEN_STONE_BRICKS.getDefaultState(),false); //stairs top fill in
			pushUp = pushUp + 2;
		}
		
		fillWithBlocks(world, boundingBox, 17 + 20, 48, 24 + 20 + 24, 24 + 20, 48, 24 + 20 + 24, MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), MSBlocks.STEEP_GREEN_STONE_BRICK_STAIRS_BASE.getDefaultState().with(DecorBlock.FACING, this.getCoordBaseMode().getOpposite()), false); //stairs base
		
		/*for(int i = 0; i < 28; i++)
		{
			fillWithBlocks(world, boundingBox, i + 20, -i, i + 38 + 20, 41 - i + 20, -i, 75 - i + 20, block, block, false); //underneath main platform
		}*/
	}
	
	private void buildInsides(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		fillWithBlocks(world, boundingBox, 14 + 20, 48, 51 + 20, 27 + 20, 48, 65 + 20, block, block, false); //lotus room floor
		fillWithRandomizedBlocks(world, boundingBox, 13 + 20, 50, 51 + 20, 28 + 20, 54, 66 + 20, true, rand, HIEROGLYPHS); //lotus room hieroglyphs
		fillWithBlocks(world, boundingBox, 13 + 20, 49, 51 + 20, 28 + 20, 49, 66 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 13 + 20, 55, 51 + 20, 28 + 20, 55, 66 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
		
		fillWithRandomizedBlocks(world, boundingBox, 6 + 20, 18, 44 + 20, 35 + 20, 22, 73 + 20, true, rand, HIEROGLYPHS); //lower room hieroglyphs
		fillWithBlocks(world, boundingBox, 6 + 20, 17, 44 + 20, 35 + 20, 17, 73 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.UP), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim floor
		fillWithBlocks(world, boundingBox, 6 + 20, 23, 44 + 20, 35 + 20, 23, 73 + 20, MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState().with(MSDirectionalBlock.FACING, Direction.DOWN), MSBlocks.GREEN_STONE_BRICK_TRIM.getDefaultState(), false); //lotus room trim ceiling
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		//fillWithBlocks(world, boundingBox, 15, 48, 15, 30, 55, 32, Blocks.GOLD_BLOCK.getDefaultState(), Blocks.IRON_BLOCK.getDefaultState(), false); //underneath stairs
		/*fillWithAir(world, boundingBox, 13, 79, 51, 27, 85, 65); //lotus room
		fillWithAir(world, boundingBox, 19, 79, 50, 21, 82, 50); //lotus room entry
		fillWithAir(world, boundingBox, 8+20, 46, 46+20, 32+20, 54, 70+20); //lower room
		fillWithAir(world, boundingBox, 20+20, 47, 70+20, 22+20, 50, 75+20); //lower room entry*/
		
		fillWithAir(world, boundingBox, 14 + 20, 49, 52 + 20, 27 + 20, 55, 65 + 20); //lotus room
		fillWithAir(world, boundingBox, 19 + 20, 49, 49 + 20, 22 + 20, 52, 51 + 20); //lotus room entry
		fillWithAir(world, boundingBox, 7 + 20, 17, 45 + 20, 34 + 20, 23, 72 + 20); //lower room
		fillWithAir(world, boundingBox, 19 + 20, 17, 71 + 20, 22 + 20, 20, 79 + 20); //lower room entry
	}
	
	private void buildPillars(BlockState block, IWorld world, MutableBoundingBox boundingBox, Random random)
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
	
	private void buildFrog(BlockState block, IWorld world, MutableBoundingBox boundingBox)
	{
		//fillWithAir(world, boundingBox, 14 + 20, 49, 52 + 20, 27 + 20, 55, 65 + 20); //lotus room
		//fillWithBlocks(world, boundingBox, 23 + 20, 57, 14 + 38 + 20, 27 + 20, 59, 55 + 20, block, block, false); //right front leg
		//fillWithBlocks(world, boundingBox, 14 + 20, 57, 14 + 38 + 20, 18 + 20, 59, 55 + 20, block, block, false); //left front leg
		fillWithBlocks(world, boundingBox, 23 + 20, 57, 14 + 38 + 20, 27 + 20, 58, 55 + 20, block, block, false); //right front leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 14 + 38 + 20, 18 + 20, 58, 55 + 20, block, block, false); //left front leg
		fillWithBlocks(world, boundingBox, 23 + 20, 57, 23 + 38 + 20, 27 + 20, 61, 64 + 20, block, block, false); //right back leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 23 + 38 + 20, 18 + 20, 61, 64 + 20, block, block, false); //left back leg
		fillWithBlocks(world, boundingBox, 16 + 20, 57, 16 + 38 + 20, 25 + 20, 67, 63 + 20, block, block, false); //body
		fillWithBlocks(world, boundingBox, 15 + 20, 65, 14 + 38 + 20, 26 + 20, 72, 59 + 20, block, block, false); //head
		
		/*fillWithBlocks(world, boundingBox, 23 + 20, 57, 12 + 38 + 20, 27 + 20, 59, 55 + 20, block, block, false); //right front leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 12 + 38 + 20, 18 + 20, 59, 55 + 20, block, block, false); //left front leg
		fillWithBlocks(world, boundingBox, 23 + 20, 57, 22 + 38 + 20, 27 + 20, 59, 65 + 20, block, block, false); //right back leg
		fillWithBlocks(world, boundingBox, 14 + 20, 57, 22 + 38 + 20, 18 + 20, 59, 65 + 20, block, block, false); //left back leg
		fillWithBlocks(world, boundingBox, 16 + 20, 57, 16 + 38 + 20, 25 + 20, 64, 65 + 20, block, block, false); //body
		fillWithBlocks(world, boundingBox, 15 + 20, 62, 14 + 38 + 20, 26 + 20, 70, 57 + 20, block, block, false); //head
		 */
	}
	
	static class Selector extends StructurePiece.BlockSelector {
		private Selector() {
		}
		
		public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
		{
			
			if(rand.nextFloat() >= .9)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_FROG.getDefaultState();
			} else if(rand.nextFloat() >= .8)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_IGUANA_LEFT.getDefaultState();
			} else if(rand.nextFloat() >= .7)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_IGUANA_RIGHT.getDefaultState();
			} else if(rand.nextFloat() >= .6)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_LOTUS.getDefaultState();
			} else if(rand.nextFloat() >= .5)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_NAK_LEFT.getDefaultState();
			} else if(rand.nextFloat() >= .4)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_NAK_RIGHT.getDefaultState();
			} else if(rand.nextFloat() >= .3)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_LEFT.getDefaultState();
			} else if(rand.nextFloat() >= .2)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SALAMANDER_RIGHT.getDefaultState();
			} else if(rand.nextFloat() >= .1)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_SKAIA.getDefaultState();
			} else if(rand.nextFloat() >= 0)
			{
				this.blockstate = MSBlocks.GREEN_STONE_BRICK_TURTLE.getDefaultState();
			}
		}
	}
}