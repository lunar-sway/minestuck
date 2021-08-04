package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.PlacementFunctionsUtil;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.common.util.Constants;

import java.util.Random;

import static com.mraof.minestuck.block.ReturnNodeBlock.placeReturnNode;

public class TierOneDungeonPiece /*extends ImprovedStructurePiece*/ extends ScatteredStructurePiece
{
	private final TierOneDungeonPiece.Selector DECAYED_BLOCKS = new TierOneDungeonPiece.Selector();
	
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomRoomType;
	
	private final int entryRoomMinX = 10;
	private final int entryRoomMinY = 0;
	private final int entryRoomMinZ = 10;
	private final int entryRoomMaxX = 27;
	private final int entryRoomMaxY = 12;
	private final int entryRoomMaxZ = 27;
	private final BlockState air = Blocks.AIR.getDefaultState();
	//private BlockState ground;
	private BlockState primaryBlock;
	private BlockState primaryCrackedBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	private BlockState fluid;
	
	public TierOneDungeonPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 50, 50, 50);
		
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		boundingBox.offset(0, posHeightPicked + 5 - boundingBox.minY, 0); //takes the lowest Ocean Floor gen viable height + 5
	}
	
	public TierOneDungeonPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomRoomType = nbt.getInt("randomRoomType");
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
		
		//ground = blocks.getBlockState("ground");
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryCrackedBlock = blocks.getBlockState("structure_primary_cracked");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		primaryPillarBlock = blocks.getBlockState("structure_primary_pillar");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		fluid = blocks.getBlockState("fall_fluid");
		
		if(!createRan)
		{
			randomRoomType = randomIn.nextInt(8);
			createRan = true;
		}
		spawner1 = false;
		spawner2 = false;
		
		buildStructureFoundation(worldIn, boundingBoxIn, randomIn, randomRoomType);
		buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		buildIndoorBlocks(worldIn, boundingBoxIn, randomIn, randomRoomType);
		
		return true;
	}
	
	private void buildStructureFoundation(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		fillWithBlocks(world, boundingBox,
				entryRoomMinX, entryRoomMinY, entryRoomMinZ,
				entryRoomMaxX, entryRoomMaxY, entryRoomMaxZ,
				primaryBlock, air, false); //exposed entry structure
		if(randomRoomType != 4)
		{
			fillWithBlocks(world, boundingBox,
					entryRoomMinX - 5, entryRoomMinY - 12, entryRoomMinZ - 5,
					entryRoomMaxX + 5, entryRoomMinY, entryRoomMaxZ + 5,
					primaryBlock, primaryBlock, false); //chamber below-connected to entry, not filled with air because of thick floor
		} else
		{
			fillWithRandomizedBlocks(world, boundingBox,
					entryRoomMinX - 5, entryRoomMinY - 12, entryRoomMinZ - 5,
					entryRoomMaxX + 5, entryRoomMinY, entryRoomMaxZ + 5,
					true, rand, DECAYED_BLOCKS);
		}
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(getXWithOffset(entryRoomMinX + 5, entryRoomMinZ), getYWithOffset(entryRoomMinY + 3), getZWithOffset(entryRoomMinX + 5, entryRoomMinZ));
		if(boundingBox.isVecInside(doorInterfacePos) || getBoundingBox().isVecInside(doorInterfacePos)) //TODO needs to work with just one bounding box
		{
			Debug.debugf("buildWallsAndFloors. doorInterfacePos = %s", doorInterfacePos);
			BlockState doorInterfaceBlockState = MSBlocks.DUNGEON_DOOR_INTERFACE.getDefaultState();
			doorInterfaceBlockState.createTileEntity(world);
			world.setBlockState(doorInterfacePos, doorInterfaceBlockState, Constants.BlockFlags.BLOCK_UPDATE);
			TileEntity interfaceTE = world.getTileEntity(doorInterfacePos);
			if(!(interfaceTE instanceof DungeonDoorInterfaceTileEntity) && interfaceTE != null)
			{
				interfaceTE = new DungeonDoorInterfaceTileEntity();
				world.getWorld().setTileEntity(doorInterfacePos, interfaceTE);
			}
			if(interfaceTE != null)
			{
				((DungeonDoorInterfaceTileEntity) interfaceTE).setKey(EnumKeyType.tier_1_key);
			} else
				throw new IllegalStateException("Unable to create a new dungeon door interface tile entity. Returned null!");
		}
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX + 6, entryRoomMinY + 1, entryRoomMinZ,
				entryRoomMaxX - 6, entryRoomMaxY - 6, entryRoomMinZ,
				MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX - 4, entryRoomMinY, entryRoomMinZ - 4,
				entryRoomMaxX + 4, entryRoomMinY, entryRoomMaxZ + 4,
				secondaryBlock, air, false); //floor
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithAir(world, boundingBox, entryRoomMinX + 6, entryRoomMinY, entryRoomMinZ + 6, entryRoomMaxX - 6, entryRoomMinY, entryRoomMaxZ - 6); //stairway hole
		fillWithAir(world, boundingBox, entryRoomMinX - 4, entryRoomMinY - 9, entryRoomMinZ - 4, entryRoomMaxX + 4, entryRoomMinY - 1, entryRoomMaxZ + 4); //lower secondary chamber
	}
	
	private void buildIndoorBlocks(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		placeReturnNode(world, new BlockPos(getXWithOffset(entryRoomMaxX / 2, entryRoomMaxZ / 2), getYWithOffset(entryRoomMinY + 4), getZWithOffset(entryRoomMaxX / 2, entryRoomMaxZ / 2)), boundingBox);
		
		BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6), getYWithOffset(entryRoomMinY - 10), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6));
		BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6), getYWithOffset(entryRoomMinY + 40), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6));
		
		//PlacementFunctionsUtil.fillWithBlocksFromPos(world, boundingBox, secondaryDecorativeBlock, PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos).up(30), PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos).up(30));
		if(randomRoomType != 4) //decrepit room types have broken down stairs
		{
			Debug.debugf("buildIndoorBlocks. staircaseMinPos = %s, isPosMinInsideBounding = %s, boundingBox = %s, getBoundingBox = %s", staircaseMinPos, boundingBox.isVecInside(staircaseMinPos), boundingBox, getBoundingBox());
			PlacementFunctionsUtil.createPlainSpiralStaircase(
					PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos),
					PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos),
					primaryDecorativeBlock, world, boundingBox, getBoundingBox());
		}
		
		if(randomRoomType == 5 || randomRoomType == 6 || randomRoomType == 7)
		{
			bottomRoomPlainType(world, boundingBox, rand);
		} else if(randomRoomType == 4)
		{
			bottomRoomDecrepitType(world, boundingBox, rand);
		} else if(randomRoomType == 3)
		{
			bottomRoomOrnateType(world, boundingBox, rand);
		} else if(randomRoomType == 2)
		{
			bottomRoomSpawnersType(world, boundingBox, rand);
		} else if(randomRoomType == 1)
		{
			bottomRoomSkippingStonesType(world, boundingBox, rand);
		} else
		{
			bottomRoomTrappedType(world, boundingBox, rand);
		}
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getCoordBaseMode() == Direction.SOUTH || this.getCoordBaseMode() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestPos = new BlockPos(this.getXWithOffset(entryRoomMinX + 2, entryRoomMinZ + 2), this.getYWithOffset(entryRoomMinY + 1), this.getZWithOffset(entryRoomMinX + 2, entryRoomMinZ + 2));
		StructureBlockUtil.placeLootChest(chestPos, world, boundingBox, getCoordBaseMode(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPos = new BlockPos(this.getXWithOffset(entryRoomMinX + 3, entryRoomMinZ + 2), this.getYWithOffset(entryRoomMinY + 1), this.getZWithOffset(entryRoomMinX + 3, entryRoomMinZ + 2));
		StructureBlockUtil.placeLootChest(chestPos, world, boundingBox, getCoordBaseMode(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		/*PlacementFunctionsUtil.fillWithBlocksCheckWater(world, boundingBox,
				getXWithOffset(entryRoomMinX + 9, entryRoomMinZ + 9), getYWithOffset(entryRoomMinY - 11), getZWithOffset(entryRoomMinX + 9, entryRoomMinZ + 9),
				getXWithOffset(entryRoomMaxX - 9, entryRoomMaxZ - 9), getYWithOffset(entryRoomMinY - 11), getZWithOffset(entryRoomMaxX - 9, entryRoomMaxZ - 9),
				MSBlocks.MINI_FROG_STATUE.getDefaultState());*/
	}
	
	private void bottomRoomPlainType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomPlainType");
		
		placePillars(world, boundingBox, rand, false);
		
	}
	
	private void bottomRoomDecrepitType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomDecrepitType");
		
		placePillars(world, boundingBox, rand, true);
		
		for(int i = 0; i <= 6; i++)
		{
			if(rand.nextBoolean())
			{
				BlockPos webPos = new BlockPos(
						getXWithOffset(entryRoomMaxX / 2 + 10 - rand.nextInt(20), entryRoomMaxZ / 2 + 10 - rand.nextInt(20)),
						getYWithOffset(entryRoomMinY - 9),
						getZWithOffset(entryRoomMaxX / 2 + 10 - rand.nextInt(20), entryRoomMaxZ / 2 + 10 - rand.nextInt(20)));
				world.setBlockState(webPos, Blocks.COBWEB.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}
	
	private void bottomRoomOrnateType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomOrnateType");
		
		placePillars(world, boundingBox, rand, false);
		
	}
	
	private void bottomRoomSpawnersType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomSpawnersType");
		
		placePillars(world, boundingBox, rand, false);
		
		spawner1 = rand.nextBoolean();
		spawner2 = rand.nextBoolean();
		
		if(spawner1)
		{
			//fillWithBlocks(world, boundingBox, entryRoomMinX - 2, entryRoomMinY - 11, entryRoomMinZ - 2, entryRoomMinX - 2, entryRoomMinY - 8, entryRoomMinZ - 2, primaryDecorativeBlock, primaryDecorativeBlock, false);
			BlockPos spawnerPos = new BlockPos(this.getXWithOffset(entryRoomMinX - 1, entryRoomMinZ - 1), this.getYWithOffset(entryRoomMinY - 7), this.getZWithOffset(entryRoomMinX - 1, entryRoomMinZ - 1));
			spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
		}
		if(spawner2)
		{
			//fillWithBlocks(world, boundingBox, entryRoomMaxX - 2, entryRoomMinY - 11, entryRoomMaxZ - 2, entryRoomMaxX - 2, entryRoomMinY - 8, entryRoomMaxZ - 2, primaryDecorativeBlock, primaryDecorativeBlock, false);
			BlockPos spawnerPos = new BlockPos(this.getXWithOffset(entryRoomMaxX + 1, entryRoomMaxZ + 1), this.getYWithOffset(entryRoomMinY - 7), this.getZWithOffset(entryRoomMaxX + 1, entryRoomMaxZ + 1));
			spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
		}
	}
	
	private void bottomRoomSkippingStonesType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomSkippingStonesType");
		
		fillWithBlocks(world, boundingBox, entryRoomMinX - 3, entryRoomMinY - 11, entryRoomMinZ - 3, entryRoomMaxX + 3, entryRoomMinY - 10, entryRoomMaxZ + 3, fluid, fluid, false);
		fillWithBlocks(world, boundingBox, entryRoomMinX + 2, entryRoomMinY - 11, entryRoomMinZ + 2, entryRoomMaxX - 2, entryRoomMinY - 10, entryRoomMaxZ - 2, primaryBlock, primaryBlock, false);
		
		placePillars(world, boundingBox, rand, false);
	}
	
	private void bottomRoomTrappedType(IWorld world, MutableBoundingBox boundingBox, Random rand) //looks same as plain
	{
		Debug.debugf("bottomRoomTrappedType");
		
		placePillars(world, boundingBox, rand, false);
	}
	
	private void placePillars(IWorld world, MutableBoundingBox boundingBox, Random rand, boolean breakDown)
	{
		if(breakDown)
		{
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						entryRoomMaxX - 1, entryRoomMinY - 11, entryRoomMaxZ - 1,
						entryRoomMaxX + 1, entryRoomMinY - 1, entryRoomMaxZ + 1,
						primaryPillarBlock, primaryPillarBlock, false); //max max
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						entryRoomMinX - 1, entryRoomMinY - 11, entryRoomMinZ - 1,
						entryRoomMinX + 1, entryRoomMinY - 1, entryRoomMinZ + 1,
						primaryPillarBlock, primaryPillarBlock, false); //min min
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						entryRoomMaxX - 1, entryRoomMinY - 11, entryRoomMinZ - 1,
						entryRoomMaxX + 1, entryRoomMinY - 1, entryRoomMinZ + 1,
						primaryPillarBlock, primaryPillarBlock, false); //max min
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						entryRoomMinX - 1, entryRoomMinY - 11, entryRoomMaxZ - 1,
						entryRoomMinX + 1, entryRoomMinY - 1, entryRoomMaxZ + 1,
						primaryPillarBlock, primaryPillarBlock, false); //min max
			}
		} else
		{
			fillWithBlocks(world, boundingBox,
					entryRoomMaxX - 1, entryRoomMinY - 11, entryRoomMaxZ - 1,
					entryRoomMaxX + 1, entryRoomMinY - 1, entryRoomMaxZ + 1,
					primaryPillarBlock, primaryPillarBlock, false); //max max
			fillWithBlocks(world, boundingBox,
					entryRoomMinX - 1, entryRoomMinY - 11, entryRoomMinZ - 1,
					entryRoomMinX + 1, entryRoomMinY - 1, entryRoomMinZ + 1,
					primaryPillarBlock, primaryPillarBlock, false); //min min
			fillWithBlocks(world, boundingBox,
					entryRoomMaxX - 1, entryRoomMinY - 11, entryRoomMinZ - 1,
					entryRoomMaxX + 1, entryRoomMinY - 1, entryRoomMinZ + 1,
					primaryPillarBlock, primaryPillarBlock, false); //max min
			fillWithBlocks(world, boundingBox,
					entryRoomMinX - 1, entryRoomMinY - 11, entryRoomMaxZ - 1,
					entryRoomMinX + 1, entryRoomMinY - 1, entryRoomMaxZ + 1,
					primaryPillarBlock, primaryPillarBlock, false); //min max
		}
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound)
	{
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("sp1", spawner1);
		tagCompound.putBoolean("sp2", spawner2);
		tagCompound.putInt("randomRoomType", randomRoomType);
	}
	
	public class Selector extends StructurePiece.BlockSelector
	{
		public Selector()
		{
		}
		
		public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
		{
			int randomBlock = rand.nextInt(6);
			if(randomBlock >= 1)
			{
				this.blockstate = primaryBlock;
			} else
			{
				this.blockstate = primaryCrackedBlock;
			}
		}
	}
}