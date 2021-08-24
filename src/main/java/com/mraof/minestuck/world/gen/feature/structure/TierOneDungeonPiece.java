package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.tileentity.DungeonDoorInterfaceTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.MobSpawnerTileEntity;
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
import org.lwjgl.system.CallbackI;

import java.util.Random;

public class TierOneDungeonPiece /*extends ImprovedStructurePiece*/ extends ScatteredStructurePiece
{
	private final TierOneDungeonPiece.Selector DECAYED_BLOCKS = new TierOneDungeonPiece.Selector();
	
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean bottomRoomSpawner1, bottomRoomSpawner2;
	private int randomRoomType;
	
	private final int entryRoomMinX = 20;
	private final int entryRoomMinY = 0;
	private final int entryRoomMinZ = 30;
	private final int entryRoomMaxX = 37;
	private final int entryRoomMaxY = 8;
	private final int entryRoomMaxZ = 47;
	
	private final int lowerRoomMinX = entryRoomMinX - 5;
	private final int lowerRoomMinY = entryRoomMinY - 42;
	private final int lowerRoomMinZ = entryRoomMinZ - 5;
	private final int lowerRoomMaxX = entryRoomMaxX + 5;
	private final int lowerRoomMaxY = entryRoomMinY - 30;
	private final int lowerRoomMaxZ = entryRoomMaxZ + 5;
	
	private final int firstRoomMinX = entryRoomMaxX + 5;
	private final int firstRoomMinY = entryRoomMinY - 50;
	private final int firstRoomMinZ = 20;
	private final int firstRoomMaxX = entryRoomMaxX + 45; //37+45 = 82
	private final int firstRoomMaxY = entryRoomMinY - 30;
	private final int firstRoomMaxZ = 57;
	private final BlockState air = Blocks.AIR.getDefaultState();
	//private BlockState ground;
	private BlockState primaryBlock;
	private BlockState primaryCrackedBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState primarySlabBlock;
	private BlockState primaryStairBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	private BlockState aspectSapling;
	private BlockState fluid;
	private BlockState lightBlock;
	
	public TierOneDungeonPiece(ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, random, x, 64, z, 82, 60, 82); //x = 42, z = 32
		/*
		int posHeightPicked = Integer.MAX_VALUE;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.getHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG); //posHeight picks the first solid block, ignoring water
				posHeightPicked = Math.min(posHeightPicked, posHeight); //with each new x/z coord it checks whether or not it is lower than the previous
			}
		boundingBox.offset(0, posHeightPicked - boundingBox.minY, 0); //takes the lowest Ocean Floor gen viable height + 5*/
		boundingBox.offset(0, generator.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG) - boundingBox.minY - 1, 0); //takes the Ocean Floor gen viable height of the spot
	}
	
	public TierOneDungeonPiece(TemplateManager templates, CompoundNBT nbt)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, nbt);
		bottomRoomSpawner1 = nbt.getBoolean("bottomRoomSpawner1");
		bottomRoomSpawner2 = nbt.getBoolean("bottomRoomSpawner2");
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
		primarySlabBlock = blocks.getBlockState("structure_primary_slab");
		primaryStairBlock = blocks.getBlockState("structure_primary_stairs");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		aspectSapling = blocks.getBlockState("aspect_sapling");
		fluid = blocks.getBlockState("fall_fluid");
		lightBlock = blocks.getBlockState("light_block");
		
		if(!createRan)
		{
			randomRoomType = randomIn.nextInt(8);
			createRan = true;
		}
		bottomRoomSpawner1 = false;
		bottomRoomSpawner2 = false;
		
		buildStructureFoundation(worldIn, boundingBoxIn, randomIn, randomRoomType);
		buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		buildIndoorBlocks(worldIn, boundingBoxIn, randomIn, randomRoomType);
		
		return true;
	}
	
	private void buildStructureFoundation(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		StructureBlockUtil.createSphere(world, boundingBox, air, new BlockPos(
						getXWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ),
						getYWithOffset(entryRoomMaxY + 2),
						getZWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ)),
				10, fluid); //clears area around structure in a more organic fashion
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX, entryRoomMinY, entryRoomMinZ,
				entryRoomMaxX, entryRoomMaxY, entryRoomMaxZ,
				primaryBlock, primaryBlock, false); //plain entrance before aspect modification
		fillWithBlocks(world, boundingBox,
				entryRoomMinX - 3, lowerRoomMaxY, entryRoomMinZ - 3,
				entryRoomMaxX + 3, entryRoomMinY, entryRoomMaxZ + 3,
				primaryBlock, primaryBlock, false); //fills out section between rooms
		
		buildAspectThemedEntrance(world, boundingBox, rand);
		/*fillWithBlocks(world, boundingBox,
				entryRoomMinX, entryRoomMinY, entryRoomMinZ,
				entryRoomMaxX, entryRoomMaxY, entryRoomMaxZ,
				primaryBlock, air, false);*/ //exposed entry structure
		
		//fillWithAir(world, boundingBox, entryRoomMinX - 10, entryRoomMinY - 12, entryRoomMinZ - 5, entryRoomMaxX + 5, entryRoomMinY, entryRoomMaxZ + 5);
		
		if(randomRoomType != 4) //chamber below-connected to entry, not filled with air because of thick floor
		{
			fillWithBlocks(world, boundingBox,
					lowerRoomMinX, lowerRoomMinY, lowerRoomMinZ,
					lowerRoomMaxX, lowerRoomMaxY, lowerRoomMaxZ,
					primaryBlock, primaryBlock, false);
		} else
		{
			fillWithRandomizedBlocks(world, boundingBox,
					lowerRoomMinX, lowerRoomMinY, lowerRoomMinZ,
					lowerRoomMaxX, lowerRoomMaxY, lowerRoomMaxZ,
					true, rand, DECAYED_BLOCKS);
		}
		
		buildTreasureAndEscapeChamber(world, boundingBox, rand);
		buildAspectThemedPuzzle(world, boundingBox, rand);
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(getXWithOffset(entryRoomMinX + 5, entryRoomMinZ), getYWithOffset(entryRoomMinY + 3), getZWithOffset(entryRoomMinX + 5, entryRoomMinZ));
		if(boundingBox.isVecInside(doorInterfacePos)/* || getBoundingBox().isVecInside(doorInterfacePos)*/) //TODO needs to work with just one bounding box
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
				entryRoomMaxX - 6, entryRoomMaxY - 3, entryRoomMinZ,
				MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX + 1, entryRoomMinY, entryRoomMinZ + 1,
				entryRoomMaxX - 1, entryRoomMinY, entryRoomMaxZ - 1,
				secondaryBlock, air, false); //floor
		
		//fillWithBlocks(world, boundingBox, entryRoomMinX + 5, lowerRoomMaxY + 1, entryRoomMinZ + 5, entryRoomMaxX - 5, entryRoomMinY - 1, entryRoomMaxZ - 5, primaryBlock, air, false); //stairway walls
	}
	
	private void carveRooms(IWorld world, MutableBoundingBox boundingBox)
	{
		fillWithAir(world, boundingBox, entryRoomMinX + 1, entryRoomMinY + 1, entryRoomMinZ + 1, entryRoomMaxX - 1, entryRoomMaxY - 1, entryRoomMaxZ - 1); //entry room
		fillWithAir(world, boundingBox, entryRoomMinX + 6, lowerRoomMaxY, entryRoomMinZ + 6, entryRoomMaxX - 6, entryRoomMinY, entryRoomMaxZ - 6); //stairway hole
		fillWithAir(world, boundingBox, lowerRoomMinX + 1, lowerRoomMinY + 3, lowerRoomMinZ + 1, lowerRoomMaxX - 1, lowerRoomMaxY - 1, lowerRoomMaxZ - 1); //lower secondary chamber
		fillWithAir(world, boundingBox, lowerRoomMaxX - 3, lowerRoomMinY + 3, lowerRoomMinZ + 11, lowerRoomMaxX, lowerRoomMinY + 7, lowerRoomMaxZ - 11); //first puzzle entrance
	}
	
	private void buildTreasureAndEscapeChamber(IWorld world, MutableBoundingBox boundingBox, Random rand/*, int randomRoomType*/)
	{
		fillWithBlocks(world, boundingBox,
				lowerRoomMinX - 10, lowerRoomMinY + 2, lowerRoomMinZ + 8,
				lowerRoomMinX, lowerRoomMinY + 9, lowerRoomMaxZ - 8,
				primaryBlock, air, false); //room itself
		
		BlockState ironBar = Blocks.IRON_BARS.getDefaultState().with(PaneBlock.EAST, true).with(PaneBlock.WEST, true);
		if(this.getCoordBaseMode() == Direction.EAST || this.getCoordBaseMode() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			ironBar = Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, true).with(PaneBlock.SOUTH, true);
		}
		fillWithBlocks(world, boundingBox,
				lowerRoomMinX, lowerRoomMinY + 3, lowerRoomMinZ + 12,
				lowerRoomMinX, lowerRoomMinY + 6, lowerRoomMaxZ - 12,
				ironBar, ironBar, false); //iron bars
		
		ChestType leftChestType = ChestType.LEFT;
		ChestType rightChestType = ChestType.RIGHT;
		if(this.getCoordBaseMode() == Direction.SOUTH || this.getCoordBaseMode() == Direction.WEST) //flips which side is left or right to prevent them from being two single chests
		{
			leftChestType = ChestType.RIGHT;
			rightChestType = ChestType.LEFT;
		}
		
		BlockPos chestPosLeft = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9));
		StructureBlockUtil.placeLootChest(chestPosLeft, world, boundingBox, getCoordBaseMode(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosLeft = new BlockPos(this.getXWithOffset(lowerRoomMinX - 1, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMinZ + 9));
		StructureBlockUtil.placeLootChest(chestPosLeft, world, boundingBox, getCoordBaseMode(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		BlockPos chestPosRight = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeLootChest(chestPosRight, world, boundingBox, getCoordBaseMode().getOpposite(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosRight = new BlockPos(this.getXWithOffset(lowerRoomMinX - 1, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeLootChest(chestPosRight, world, boundingBox, getCoordBaseMode().getOpposite(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		StructureBlockUtil.placeReturnNode(world, boundingBox, new BlockPos(getXWithOffset(lowerRoomMinX - 4, (lowerRoomMaxZ + lowerRoomMinZ) / 2), getYWithOffset(lowerRoomMinY + 3), getZWithOffset(lowerRoomMinX - 4, (lowerRoomMaxZ + lowerRoomMinZ) / 2)), getCoordBaseMode());
		StructureBlockUtil.placeReturnNode(world, boundingBox, new BlockPos(getXWithOffset(entryRoomMinX - 8, (entryRoomMaxZ + entryRoomMinZ) / 2), getYWithOffset(lowerRoomMinY + 3), getZWithOffset(entryRoomMinX - 8, (entryRoomMaxZ + entryRoomMinZ) / 2)), getCoordBaseMode());
		//placeReturnNode(world, new BlockPos(getXWithOffset(entryRoomMinX - 8, (entryRoomMaxZ + entryRoomMinZ) / 2 - 1), getYWithOffset(entryRoomMinY - 19), getZWithOffset(entryRoomMinX - 8, (entryRoomMaxZ + entryRoomMinZ) / 2) - 1), boundingBox);
		
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMinZ + 10, boundingBox); //left side light
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMaxZ - 10, boundingBox); //right side light
	}
	
	private void buildIndoorBlocks(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		//setBlockState(world, Blocks.DIRT.getDefaultState(), entryRoomMaxX - 3,entryRoomMinY, entryRoomMaxZ - 3, boundingBox);
		//setBlockState(world, aspectSapling, entryRoomMaxX - 3,entryRoomMinY + 1, entryRoomMaxZ - 3, boundingBox);
		
		//PlacementFunctionsUtil.createSphere(world, boundingBox, primaryDecorativeBlock, new BlockPos(getXWithOffset(entryRoomMaxX - 5, entryRoomMaxZ - 5), getYWithOffset(entryRoomMaxY + 15), getZWithOffset(entryRoomMaxX - 5, entryRoomMaxZ - 5)), 10);
		//PlacementFunctionsUtil.createCylinder(world, boundingBox, primaryDecorativeBlock, new BlockPos(getXWithOffset(entryRoomMaxX - 5, entryRoomMaxZ - 5), getYWithOffset(entryRoomMaxY + 15), getZWithOffset(entryRoomMaxX - 5, entryRoomMaxZ - 5)), 5, 10);
		
		BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6), getYWithOffset(lowerRoomMinY + 2), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6));
		BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6), getYWithOffset(entryRoomMinY), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6));
		
		//PlacementFunctionsUtil.fillWithBlocksFromPos(world, boundingBox, secondaryDecorativeBlock, PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos).up(30), PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos).up(30));
		if(randomRoomType != 4) //decrepit room types have broken down stairs
		{
			//Debug.debugf("buildIndoorBlocks. staircaseMinPos = %s, isPosMinInsideBounding = %s, boundingBox = %s, getBoundingBox = %s", staircaseMinPos, boundingBox.isVecInside(staircaseMinPos), boundingBox, getBoundingBox());
			StructureBlockUtil.createPlainSpiralStaircase(staircaseMinPos, staircaseMaxPos, primaryDecorativeBlock, world, boundingBox/*, getBoundingBox()*/);
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
		
		if(fluid == Blocks.LAVA.getDefaultState())
			fillWithBlocks(world, boundingBox, entryRoomMinX + 6, lowerRoomMinY + 1, entryRoomMinZ + 6, entryRoomMaxX - 6, lowerRoomMinY + 2, entryRoomMaxZ - 6, Blocks.WATER.getDefaultState(), Blocks.WATER.getDefaultState(), false);
		else
			fillWithBlocks(world, boundingBox, entryRoomMinX + 6, lowerRoomMinY + 1, entryRoomMinZ + 6, entryRoomMaxX - 6, lowerRoomMinY + 2, entryRoomMaxZ - 6, fluid, fluid, false);
		
		
		/*for(int i = 0; i <= 6; i++)
		{
			if(rand.nextBoolean())
			{
				/*BlockPos webPos = new BlockPos(
						getXWithOffset(entryRoomMaxX / 2 + 10 - rand.nextInt(20), entryRoomMaxZ / 2 + 10 - rand.nextInt(20)),
						getYWithOffset(entryRoomMinY - 9),
						getZWithOffset(entryRoomMaxX / 2 + 10 - rand.nextInt(20), entryRoomMaxZ / 2 + 10 - rand.nextInt(20)));*/
				
				/*setBlockState(world, Blocks.COBWEB.getDefaultState(), entryRoomMaxX / 2 + 10 - rand.nextInt(20),entryRoomMinY - 9, entryRoomMaxX / 2 + 10 - rand.nextInt(20), boundingBox);
				//world.setBlockState(webPos, Blocks.COBWEB.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}*/
	}
	
	private void bottomRoomOrnateType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomOrnateType");
		
		placePillars(world, boundingBox, rand, false);
		StructureBlockUtil.createCylinder(world, boundingBox, primarySlabBlock.with(SlabBlock.TYPE, SlabType.BOTTOM), new BlockPos(
						getXWithOffset(lowerRoomMinX + 14, lowerRoomMinZ + 13),
						getYWithOffset(lowerRoomMinY + 3),
						getZWithOffset(lowerRoomMinX + 14, lowerRoomMinZ + 13)),
				6, 1);
		StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock, new BlockPos(
						getXWithOffset(lowerRoomMinX + 14, lowerRoomMinZ + 13),
						getYWithOffset(lowerRoomMinY + 3),
						getZWithOffset(lowerRoomMinX + 14, lowerRoomMinZ + 13)),
				5, 1);
	}
	
	private void bottomRoomSpawnersType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomSpawnersType");
		
		placePillars(world, boundingBox, rand, false);
		
		bottomRoomSpawner1 = rand.nextBoolean();
		bottomRoomSpawner2 = rand.nextBoolean();
		
		if(bottomRoomSpawner1)
		{
			//fillWithBlocks(world, boundingBox, entryRoomMinX - 2, entryRoomMinY - 11, entryRoomMinZ - 2, entryRoomMinX - 2, entryRoomMinY - 8, entryRoomMinZ - 2, primaryDecorativeBlock, primaryDecorativeBlock, false);
			BlockPos spawnerPos = new BlockPos(this.getXWithOffset(lowerRoomMinX + 4, lowerRoomMinZ + 4), this.getYWithOffset(lowerRoomMinY + 6), this.getZWithOffset(lowerRoomMinX + 4, lowerRoomMinZ + 4));
			bottomRoomSpawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
		}
		if(bottomRoomSpawner2)
		{
			//fillWithBlocks(world, boundingBox, entryRoomMaxX - 2, entryRoomMinY - 11, entryRoomMaxZ - 2, entryRoomMaxX - 2, entryRoomMinY - 8, entryRoomMaxZ - 2, primaryDecorativeBlock, primaryDecorativeBlock, false);
			BlockPos spawnerPos = new BlockPos(this.getXWithOffset(lowerRoomMaxX - 4, lowerRoomMaxZ - 4), this.getYWithOffset(lowerRoomMinY + 6), this.getZWithOffset(lowerRoomMaxX - 4, lowerRoomMaxZ - 4));
			bottomRoomSpawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
		}
	}
	
	private void bottomRoomSkippingStonesType(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		Debug.debugf("bottomRoomSkippingStonesType");
		
		fillWithBlocks(world, boundingBox, lowerRoomMinX + 2, lowerRoomMinY + 1, lowerRoomMinZ + 2, lowerRoomMaxX - 2, lowerRoomMinY + 2, lowerRoomMaxZ - 2, fluid, fluid, false);
		fillWithBlocks(world, boundingBox, lowerRoomMinX + 11, lowerRoomMinY + 1, lowerRoomMinZ + 11, lowerRoomMaxX - 11, lowerRoomMinY + 2, lowerRoomMaxZ - 11, primaryBlock, primaryBlock, false);
		
		placePillars(world, boundingBox, rand, false);
	}
	
	private void bottomRoomTrappedType(IWorld world, MutableBoundingBox boundingBox, Random rand) //looks same as plain
	{
		Debug.debugf("bottomRoomTrappedType");
		
		placePillars(world, boundingBox, rand, false);
	}
	
	private void placePillars(IWorld world, MutableBoundingBox boundingBox, Random rand, boolean breakDown)
	{
		if(breakDown) //TODO the following booleans may need to be saved to nbt
		{
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						lowerRoomMaxX - 4, lowerRoomMinY + 1, lowerRoomMaxZ - 4,
						lowerRoomMaxX - 2, lowerRoomMaxY - 1, lowerRoomMaxZ - 2,
						primaryPillarBlock, primaryPillarBlock, false); //max max
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						lowerRoomMinX + 4, lowerRoomMinY + 1, lowerRoomMinZ + 4,
						lowerRoomMinX + 6, lowerRoomMaxY - 1, lowerRoomMinZ + 6,
						primaryPillarBlock, primaryPillarBlock, false); //min min
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						lowerRoomMinX + 4, lowerRoomMinY + 1, lowerRoomMinZ + 4,
						lowerRoomMinX + 6, lowerRoomMaxY - 1, lowerRoomMinZ + 6,
						primaryPillarBlock, primaryPillarBlock, false); //max min
			}
			if(rand.nextBoolean())
			{
				fillWithBlocks(world, boundingBox,
						lowerRoomMaxX - 4, lowerRoomMinY + 1, lowerRoomMaxZ - 4,
						lowerRoomMaxX - 2, lowerRoomMaxY - 1, lowerRoomMaxZ - 2,
						primaryPillarBlock, primaryPillarBlock, false); //min max
			}
		} else
		{
			fillWithBlocks(world, boundingBox,
					lowerRoomMaxX - 4, lowerRoomMinY + 1, lowerRoomMaxZ - 4,
					lowerRoomMaxX - 2, lowerRoomMaxY - 1, lowerRoomMaxZ - 2,
					primaryPillarBlock, primaryPillarBlock, false); //max max //TODO not appearing
			fillWithBlocks(world, boundingBox,
					lowerRoomMinX + 4, lowerRoomMinY + 1, lowerRoomMinZ + 4,
					lowerRoomMinX + 6, lowerRoomMaxY - 1, lowerRoomMinZ + 6,
					primaryPillarBlock, primaryPillarBlock, false); //min min
			fillWithBlocks(world, boundingBox,
					lowerRoomMinX + 4, lowerRoomMinY + 1, lowerRoomMinZ + 4,
					lowerRoomMinX + 6, lowerRoomMaxY - 1, lowerRoomMinZ + 6,
					primaryPillarBlock, primaryPillarBlock, false); //max min //TODO not appearing
			fillWithBlocks(world, boundingBox,
					lowerRoomMaxX - 2, lowerRoomMinY + 1, lowerRoomMaxZ - 4,
					lowerRoomMaxX - 4, lowerRoomMaxY - 1, lowerRoomMaxZ - 2,
					primaryPillarBlock, primaryPillarBlock, false); //min max //TODO not appearing
		}
	}
	
	private void buildAspectThemedEntrance(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		if(aspectSapling == MSBlocks.BREATH_ASPECT_SAPLING.getDefaultState()) //pipes moving around
		{
		
		} else if(aspectSapling == MSBlocks.LIFE_ASPECT_SAPLING.getDefaultState()) //rabbit statue
		{
			StructureBlockUtil.placeLargeAspectSymbol(new BlockPos(getXWithOffset(entryRoomMinX, entryRoomMinZ), getYWithOffset(entryRoomMaxY + 20), getZWithOffset(entryRoomMinX, entryRoomMinZ)), world, boundingBox, primaryBlock, EnumAspect.BLOOD);
			
			fillWithBlocks(world, boundingBox, entryRoomMinX - 2, entryRoomMaxY - 10, entryRoomMinZ + 6, entryRoomMinX - 2, entryRoomMaxY - 3, entryRoomMinZ + 6, MSBlocks.PIPE.getDefaultState().with(PipeBlock.FACING, Direction.UP), MSBlocks.PIPE.getDefaultState(), false); //pipe 1
			setBlockState(world, MSBlocks.PIPE_INTERSECTION.getDefaultState(), entryRoomMinX - 2, entryRoomMaxY - 2, entryRoomMinZ + 6, boundingBox);
			fillWithBlocks(world, boundingBox, entryRoomMinX - 1, entryRoomMaxY - 2, entryRoomMinZ + 6, entryRoomMinX + 3, entryRoomMaxY - 2, entryRoomMinZ + 6, MSBlocks.PIPE.getDefaultState().with(PipeBlock.FACING, Direction.NORTH), MSBlocks.PIPE.getDefaultState(), false);
			
			/* //TODO Will be for Life
			fillWithBlocks(world, boundingBox, entryRoomMinX + 6, entryRoomMaxY + 4, entryRoomMinZ + 4, entryRoomMaxX - 6, entryRoomMaxY + 8, entryRoomMaxZ - 4, secondaryBlock, secondaryBlock, false); //body
			fillWithBlocks(world, boundingBox, entryRoomMinX + 7, entryRoomMaxY + 7, entryRoomMinZ + 2, entryRoomMaxX - 7, entryRoomMaxY + 10, entryRoomMinZ + 5, secondaryBlock, secondaryBlock, false); //head
			fillWithBlocks(world, boundingBox, entryRoomMinX + 7, entryRoomMaxY + 11, entryRoomMinZ + 5, entryRoomMinX + 7, entryRoomMaxY + 14, entryRoomMinZ + 5, secondaryBlock, secondaryBlock, false); //left ear
			fillWithBlocks(world, boundingBox, entryRoomMaxX - 7, entryRoomMaxY + 11, entryRoomMinZ + 5, entryRoomMaxX - 7, entryRoomMaxY + 14, entryRoomMinZ + 5, secondaryBlock, secondaryBlock, false); //right ear
			fillWithBlocks(world, boundingBox, entryRoomMinX + 6, entryRoomMaxY + 1, entryRoomMinZ + 4, entryRoomMinX + 7, entryRoomMaxY + 4, entryRoomMinZ + 5, secondaryBlock, secondaryBlock, false); //front left leg
			fillWithBlocks(world, boundingBox, entryRoomMaxX - 7, entryRoomMaxY + 1, entryRoomMinZ + 4, entryRoomMaxX - 6, entryRoomMaxY + 4, entryRoomMinZ + 5, secondaryBlock, secondaryBlock, false); //front right leg
			fillWithBlocks(world, boundingBox, entryRoomMinX + 5, entryRoomMaxY + 1, entryRoomMaxZ - 5, entryRoomMinX + 7, entryRoomMaxY + 6, entryRoomMaxZ - 4, secondaryBlock, secondaryBlock, false); //back left leg
			fillWithBlocks(world, boundingBox, entryRoomMaxX - 7, entryRoomMaxY + 1, entryRoomMaxZ - 5, entryRoomMaxX - 5, entryRoomMaxY + 6, entryRoomMaxZ - 4, secondaryBlock, secondaryBlock, false); //back right leg
			*/
		} else if(aspectSapling == MSBlocks.LIGHT_ASPECT_SAPLING.getDefaultState())
		{
		
		} else if(aspectSapling == MSBlocks.TIME_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.HEART_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.RAGE_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.BLOOD_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.DOOM_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.VOID_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.SPACE_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.MIND_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.HOPE_ASPECT_SAPLING.getDefaultState())
		{
		}
		
	}
	
	private void buildAspectThemedPuzzle(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		fillWithBlocks(world, boundingBox,
				firstRoomMinX, firstRoomMinY, firstRoomMinZ,
				firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,
				primaryBlock, primaryBlock, false);
		
		if(aspectSapling == MSBlocks.BREATH_ASPECT_SAPLING.getDefaultState()) //parkour like frog temple lower room
		{
		
		} else if(aspectSapling == MSBlocks.LIFE_ASPECT_SAPLING.getDefaultState())
		{
			//TODO will be for Blood
			fillWithAir(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 5, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 1, firstRoomMaxZ - 1); //ceiling down to raised areas
			fillWithAir(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 9, firstRoomMinZ + 4,
					firstRoomMaxX - 1, firstRoomMaxY - 5, firstRoomMaxZ - 4); //makes walls of raised areas
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 10, firstRoomMinZ + 4,
					firstRoomMaxX - 4, firstRoomMaxY - 10, firstRoomMaxZ - 4,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false);
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 8, firstRoomMaxY - 10, firstRoomMinZ + 8,
					firstRoomMinX + 8, firstRoomMaxY - 6, firstRoomMaxZ - 4,
					primaryBlock, primaryBlock, false); //first barrier
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 16, firstRoomMaxY - 10, firstRoomMinZ + 4,
					firstRoomMinX + 16, firstRoomMaxY - 6, firstRoomMaxZ - 8,
					primaryBlock, primaryBlock, false); //second barrier
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 24, firstRoomMaxY - 10, firstRoomMinZ + 8,
					firstRoomMinX + 24, firstRoomMaxY - 6, firstRoomMaxZ - 4,
					primaryBlock, primaryBlock, false); //third barrier
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 32, firstRoomMaxY - 10, firstRoomMinZ + 4,
					firstRoomMinX + 32, firstRoomMaxY - 6, firstRoomMaxZ - 8,
					primaryBlock, primaryBlock, false); //fourth barrier
			
			BlockPos stairsAreaMin = new BlockPos(getXWithOffset(firstRoomMaxX - 2, firstRoomMinZ + 4), getYWithOffset(firstRoomMaxY - 19), getZWithOffset(firstRoomMaxX - 2, firstRoomMinZ + 4));
			BlockPos stairsAreaMax = new BlockPos(getXWithOffset(firstRoomMaxX - 1, firstRoomMaxZ - 5), getYWithOffset(firstRoomMaxY - 5), getZWithOffset(firstRoomMaxX - 1, firstRoomMaxZ - 5));
			
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, air, stairsAreaMin, stairsAreaMax);
			
			//fillWithAir(world, boundingBox, firstRoomMaxX - 1, firstRoomMaxY - 20, firstRoomMinZ + 5, firstRoomMaxX - 2, firstRoomMaxY - 5, firstRoomMaxZ - 5);
			StructureBlockUtil.createStairs(world, boundingBox, primaryBlock, primaryStairBlock.with(StairsBlock.FACING, getCoordBaseMode()), stairsAreaMin.offset(getCoordBaseMode(), 19), 10, 2, getCoordBaseMode(), false);
			
			BlockPos spawnerPos;
			for(int xIterate = 0; xIterate < 4; xIterate++)
			{
				CompoundNBT spawnerNBT = new CompoundNBT();
				//spawnerNBT.("SpawnData", 4);
				
				spawnerPos = new BlockPos(
						getXWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMinZ + 2),
						getYWithOffset(firstRoomMaxY - 5),
						getZWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMinZ + 2));
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				TileEntity spawnerTE = world.getTileEntity(spawnerPos); //TODO figure out how to change potion effects of spawned entities(give them speed) and increase range at which they spawn
				if((spawnerTE instanceof MobSpawnerTileEntity))
				{
					((MobSpawnerTileEntity) spawnerTE).write(spawnerNBT);
				}
				
				spawnerPos = new BlockPos(
						getXWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMaxZ - 2),
						getYWithOffset(firstRoomMaxY - 5),
						getZWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMaxZ - 2));
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				
				fillWithAir(world, boundingBox, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 7, firstRoomMaxZ - 3, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 5, firstRoomMaxZ - 1);
				fillWithAir(world, boundingBox, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 7, firstRoomMinZ + 1, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 5, firstRoomMinZ + 3);
			}
			
			fillWithBlocks(world, boundingBox, firstRoomMinX + 5, firstRoomMaxY - 10, firstRoomMaxZ, firstRoomMinX + 20, firstRoomMaxY - 3, firstRoomMaxZ + 12, primaryBlock, air, false); //first side room
			fillWithAir(world, boundingBox, firstRoomMinX + 9, firstRoomMaxY - 9, firstRoomMaxZ - 3, firstRoomMinX + 10, firstRoomMaxY - 7, firstRoomMaxZ); //first side room entrance
			
			/*//TODO will be for Breath
			fillWithAir(world, boundingBox,
					firstRoomMinX + 1, firstRoomMinY + 1, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 1, firstRoomMaxZ - 1);
			
			for(int i = 0; i < 3; i++)
			{
				for(int j = 0; j < 3; j++)
				{
					fillWithBlocks(world, boundingBox,
							firstRoomMinX + 6 + i * 9, firstRoomMinY + 1, firstRoomMinZ + 9 + j * 9,
							firstRoomMinX + 7 + i * 9, firstRoomMaxY - 1, firstRoomMinZ + 10 + j * 9,
							primaryPillarBlock.with(RotatedPillarBlock.AXIS, Direction.Axis.Y), primaryPillarBlock.with(RotatedPillarBlock.AXIS, Direction.Axis.Y), false);
					fillWithBlocks(world, boundingBox,
							firstRoomMinX + 4 + i * 9, firstRoomMinY + 10, firstRoomMinZ + 7 + j * 9,
							firstRoomMinX + 9 + i * 9, firstRoomMinY + 10, firstRoomMinZ + 12 + j * 9,
							primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
							
							//TODO do a lower or higher level of platforms to expand puzzle
					fillWithAir(world, boundingBox,
							firstRoomMinX + 4 + 9, firstRoomMinY + 1, firstRoomMinZ + 7 + 9,
							firstRoomMinX + 9 + 9, firstRoomMaxY - 1, firstRoomMinZ + 12 + 9);
				}
			}
			
			fillWithBlocks(world, boundingBox, //TODO not appearing
					firstRoomMinX + 14, firstRoomMinY + 10, firstRoomMinZ,
					firstRoomMinX + 22, firstRoomMinY + 16, firstRoomMinZ - 20,
					primaryBlock, air, false); //TODO side rooms will require fighting past ogres to get to a switch
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 1, firstRoomMinY + 1, firstRoomMinZ + 18,
					firstRoomMinX + 1, firstRoomMinY + 10, firstRoomMinZ + 19,
					Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.EAST), Blocks.LADDER.getDefaultState(), false); //ladder*/
			
		} else if(aspectSapling == MSBlocks.LIGHT_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.TIME_ASPECT_SAPLING.getDefaultState()) //spikes that shoot up on a timed interval so you have to match the rythym to pass
		{
		} else if(aspectSapling == MSBlocks.HEART_ASPECT_SAPLING.getDefaultState()) //stairs going all throughout the structure leading to different small rooms with levers that all need pulling in order to pass
		{
		} else if(aspectSapling == MSBlocks.RAGE_ASPECT_SAPLING.getDefaultState()) //difficult terrain made by odd geometric shapes blocking path
		{
		} else if(aspectSapling == MSBlocks.BLOOD_ASPECT_SAPLING.getDefaultState()) //nonhazardous liquid that player has to trudge through while enemies approach on all sides
		{
		} else if(aspectSapling == MSBlocks.DOOM_ASPECT_SAPLING.getDefaultState())
		{
		} else if(aspectSapling == MSBlocks.VOID_ASPECT_SAPLING.getDefaultState()) //invisible platforms or barriers
		{
		} else if(aspectSapling == MSBlocks.SPACE_ASPECT_SAPLING.getDefaultState()) //portal 2 gel puzzles
		{
		} else if(aspectSapling == MSBlocks.MIND_ASPECT_SAPLING.getDefaultState()) //maze
		{
		} else if(aspectSapling == MSBlocks.HOPE_ASPECT_SAPLING.getDefaultState())
		{
		}
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound)
	{
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("bottomRoomSpawner1", bottomRoomSpawner1); //spawner type room only
		tagCompound.putBoolean("bottomRoomSpawner2", bottomRoomSpawner2); //spawner type room only
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