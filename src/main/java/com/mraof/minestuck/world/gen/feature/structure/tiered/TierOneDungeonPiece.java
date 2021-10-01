package com.mraof.minestuck.world.gen.feature.structure.tiered;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.block.redstone.SolidSwitchBlock;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.tileentity.redstone.RemoteObserverTileEntity;
import com.mraof.minestuck.tileentity.redstone.StatStorerTileEntity;
import com.mraof.minestuck.tileentity.redstone.SummonerTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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

public class TierOneDungeonPiece extends ScatteredStructurePiece
{
	private final TierOneDungeonPiece.Selector DECAYED_BLOCKS = new TierOneDungeonPiece.Selector();
	
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean bottomRoomSpawner1, bottomRoomSpawner2;
	private int randomRoomType;
	private int roomVariable1;
	
	private static final int entryRoomMinX = 20;
	private static final int entryRoomMinY = 0;
	private static final int entryRoomMinZ = 30;
	private static final int entryRoomMaxX = 37;
	private static final int entryRoomMaxY = 8;
	private static final int entryRoomMaxZ = 47;
	
	private static final int lowerRoomMinX = entryRoomMinX - 5;
	private static final int lowerRoomMinY = entryRoomMinY - 32;
	private static final int lowerRoomMinZ = entryRoomMinZ - 5;
	private static final int lowerRoomMaxX = entryRoomMaxX + 5;
	private static final int lowerRoomMaxY = entryRoomMinY - 20;
	private static final int lowerRoomMaxZ = entryRoomMaxZ + 5;
	
	private static final int firstRoomMinX = entryRoomMaxX + 5;
	private static final int firstRoomMinY = entryRoomMinY - 50;
	private static final int firstRoomMinZ = 20;
	private static final int firstRoomMaxX = entryRoomMaxX + 45; //37+45 = 82
	private static final int firstRoomMaxY = entryRoomMinY - 20;
	private static final int firstRoomMaxZ = 57;
	private static final BlockState air = Blocks.AIR.getDefaultState();
	//private BlockState ground; //dont use because ores get embedded in it
	private BlockState primaryBlock;
	private BlockState primaryCrackedBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState primarySlabBlock;
	private BlockState primaryStairBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	//private BlockState aspectSapling;
	private BlockState fluid;
	private BlockState lightBlock;
	
	private EnumAspect worldAspect;
	private EnumClass worldClass;
	private TerrainLandType worldTerrain;
	
	
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
		roomVariable1 = nbt.getInt("roomVariable1");
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings()); //creates set of blocks relevant to the terrain and aspect of a player's land(primarily terrain)
		
		//ground = blocks.getBlockState("ground");
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryCrackedBlock = blocks.getBlockState("structure_primary_cracked");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		primaryPillarBlock = blocks.getBlockState("structure_primary_pillar");
		primarySlabBlock = blocks.getBlockState("structure_primary_slab");
		primaryStairBlock = blocks.getBlockState("structure_primary_stairs");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		//aspectSapling = blocks.getBlockState("aspect_sapling");
		fluid = blocks.getBlockState("fall_fluid");
		lightBlock = blocks.getBlockState("light_block");
		
		SburbConnection sburbConnection = SburbHandler.getConnectionForDimension(worldIn.getWorld().getServer(), worldIn.getDimension().getType());
		Title worldTitle = sburbConnection == null ? null : PlayerSavedData.getData(sburbConnection.getClientIdentifier(), worldIn.getWorld().getServer()).getTitle();
		worldAspect = worldTitle == null ? null : worldTitle.getHeroAspect(); //aspect of this land's player
		worldClass = worldTitle == null ? null : worldTitle.getHeroClass(); //class of this land's player
		LandInfo landInfo = MSDimensions.getLandInfo(worldIn.getWorld().getServer(), worldIn.getDimension().getType());
		worldTerrain = landInfo.getLandAspects().terrain;
		
		if(!createRan)
		{
			randomRoomType = randomIn.nextInt(8);
			roomVariable1 = randomIn.nextInt(6);
			createRan = true;
		}
		bottomRoomSpawner1 = false;
		bottomRoomSpawner2 = false;
		
		buildStructureFoundation(worldIn, boundingBoxIn, randomIn, randomRoomType, chunkGeneratorIn);
		buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		carveRooms(worldIn, boundingBoxIn);
		buildIndoorBlocks(worldIn, boundingBoxIn, randomIn, randomRoomType);
		
		return true;
	}
	
	private void buildStructureFoundation(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType, ChunkGenerator<?> chunkGeneratorIn)
	{
		StructureBlockUtil.createSphere(world, boundingBox, air, new BlockPos(
						getXWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ),
						getYWithOffset(entryRoomMaxY + 2),
						getZWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ)),
				10, fluid); //clears area around structure in a more organic fashion
		
		BlockPos segsg = new BlockPos(
				getXWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ),
				getYWithOffset(entryRoomMaxY + 8),
				getZWithOffset((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMinZ));
		StructureBlockUtil.fillAsGrid(world, boundingBox, primaryBlock, segsg, segsg.up(15).west(15).south(15), 3);
		
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
		buildAspectThemedPuzzle(world, boundingBox, rand, chunkGeneratorIn);
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = new BlockPos(
				getXWithOffset(entryRoomMinX + 5, entryRoomMinZ),
				getYWithOffset(entryRoomMinY + 3),
				getZWithOffset(entryRoomMinX + 5, entryRoomMinZ));
		
		BlockPos minDoorPos = new BlockPos(
				getXWithOffset(entryRoomMinX + 6, entryRoomMinZ),
				getYWithOffset(entryRoomMinY + 1),
				getZWithOffset(entryRoomMinX + 6, entryRoomMinZ));
		BlockPos maxDoorPos = new BlockPos(
				getXWithOffset(entryRoomMaxX - 6, entryRoomMinZ),
				getYWithOffset(entryRoomMaxY - 3),
				getZWithOffset(entryRoomMaxX - 6, entryRoomMinZ));
		
		StructureBlockUtil.placeDungeonDoor(world, boundingBox, doorInterfacePos, minDoorPos, maxDoorPos, EnumKeyType.tier_1_key);
		
		fillWithBlocks(world, boundingBox,
				entryRoomMinX + 1, entryRoomMinY, entryRoomMinZ + 1,
				entryRoomMaxX - 1, entryRoomMinY, entryRoomMaxZ - 1,
				secondaryBlock, air, false); //floor
		
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
		
		BlockPos chestPosLeft = new BlockPos(this.getXWithOffset(lowerRoomMinX - 4, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9));
		StructureBlockUtil.placeLootBlock(chestPosLeft, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		chestPosLeft = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMinZ + 9));
		StructureBlockUtil.placeLootBlock(chestPosLeft, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		
		BlockPos chestPosRight = new BlockPos(this.getXWithOffset(lowerRoomMinX - 4, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeLootBlock(chestPosRight, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().getOpposite()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		chestPosRight = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 3), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeLootBlock(chestPosRight, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().getOpposite()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		
		BlockPos chestPosLeftb = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 5), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMinZ + 9));
		StructureBlockUtil.placeChest(chestPosLeftb, world, boundingBox, getCoordBaseMode(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosLeftb = new BlockPos(this.getXWithOffset(lowerRoomMinX - 1, lowerRoomMinZ + 9), this.getYWithOffset(lowerRoomMinY + 5), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMinZ + 9));
		StructureBlockUtil.placeChest(chestPosLeftb, world, boundingBox, getCoordBaseMode(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		BlockPos chestPosRightb = new BlockPos(this.getXWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 5), this.getZWithOffset(lowerRoomMinX - 2, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeChest(chestPosRightb, world, boundingBox, getCoordBaseMode().getOpposite(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosRightb = new BlockPos(this.getXWithOffset(lowerRoomMinX - 1, lowerRoomMaxZ - 9), this.getYWithOffset(lowerRoomMinY + 5), this.getZWithOffset(lowerRoomMinX - 1, lowerRoomMaxZ - 9));
		StructureBlockUtil.placeChest(chestPosRightb, world, boundingBox, getCoordBaseMode().getOpposite(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		StructureBlockUtil.placeReturnNode(world, boundingBox, new BlockPos(getXWithOffset(lowerRoomMinX - 7, (lowerRoomMaxZ + lowerRoomMinZ) / 2), getYWithOffset(lowerRoomMinY + 3), getZWithOffset(lowerRoomMinX - 7, (lowerRoomMaxZ + lowerRoomMinZ) / 2)), getCoordBaseMode());
		
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMinZ + 10, boundingBox); //left side light
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMaxZ - 10, boundingBox); //right side light
	}
	
	private void buildIndoorBlocks(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		BlockPos staircaseMinPos = new BlockPos(getXWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6), getYWithOffset(lowerRoomMinY + 2), getZWithOffset(entryRoomMinX + 6, entryRoomMinZ + 6));
		BlockPos staircaseMaxPos = new BlockPos(getXWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6), getYWithOffset(entryRoomMinY), getZWithOffset(entryRoomMaxX - 6, entryRoomMaxZ - 6));
		
		//PlacementFunctionsUtil.fillWithBlocksFromPos(world, boundingBox, secondaryDecorativeBlock, PlacementFunctionsUtil.axisAlignBlockPosGetMin(staircaseMinPos, staircaseMaxPos).up(30), PlacementFunctionsUtil.axisAlignBlockPosGetMax(staircaseMinPos, staircaseMaxPos).up(30));
		if(randomRoomType != 4) //decrepit room types have broken down stairs
		{
			//Debug.debugf("buildIndoorBlocks. staircaseMinPos = %s, isPosMinInsideBounding = %s, boundingBox = %s, getBoundingBox = %s", staircaseMinPos, boundingBox.isVecInside(staircaseMinPos), boundingBox, getBoundingBox());
			StructureBlockUtil.placeSpiralStaircase(world, boundingBox, staircaseMinPos, staircaseMaxPos, primaryDecorativeBlock);
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
		if(worldAspect == EnumAspect.BREATH) //pipes moving around
		{
		
		} else if(worldAspect == EnumAspect.LIFE) //rabbit statue
		{
			//StructureBlockUtil.placeLargeAspectSymbol(new BlockPos(getXWithOffset(entryRoomMinX, entryRoomMinZ), getYWithOffset(entryRoomMaxY + 20), getZWithOffset(entryRoomMinX, entryRoomMinZ)), world, boundingBox, primaryBlock, EnumAspect.BLOOD);
			
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
		} else if(worldAspect == EnumAspect.LIGHT)
		{
		} else if(worldAspect == EnumAspect.TIME)
		{
		} else if(worldAspect == EnumAspect.HEART)
		{
		} else if(worldAspect == EnumAspect.RAGE)
		{
		} else if(worldAspect == EnumAspect.BLOOD)
		{
		} else if(worldAspect == EnumAspect.DOOM)
		{
		} else if(worldAspect == EnumAspect.VOID)
		{
		} else if(worldAspect == EnumAspect.SPACE)
		{
		} else if(worldAspect == EnumAspect.MIND)
		{
		} else if(worldAspect == EnumAspect.HOPE)
		{
		}
		
	}
	
	private void buildAspectThemedPuzzle(IWorld world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator<?> chunkGeneratorIn)
	{
		fillWithBlocks(world, boundingBox,
				firstRoomMinX, firstRoomMinY, firstRoomMinZ,
				firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,
				secondaryBlock, secondaryBlock, false);
		
		if(worldAspect == EnumAspect.BREATH) //parkour like frog temple lower room
		{
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
		} else if(worldAspect == EnumAspect.LIFE)
		{
			/*//TODO will be for Space
			fillWithAir(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 12, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 1, firstRoomMaxZ - 1); //ceiling down to main area
			fillWithAir(world, boundingBox,
					firstRoomMinX + 15, firstRoomMaxY - 25, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 12, firstRoomMaxZ - 1); //pit area
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 10, firstRoomMaxY - 13, (firstRoomMinZ + firstRoomMaxZ) / 2,
					firstRoomMinX + 14, firstRoomMaxY - 13, (firstRoomMinZ + firstRoomMaxZ) / 2,
					MSBlocks.TRAJECTORY_BLOCK.getDefaultState().with(TrajectoryBlock.FACING, getCoordBaseMode().rotateYCCW()), MSBlocks.TRAJECTORY_BLOCK.getDefaultState(), false); //sideways facing trajectory blocks
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 4, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 4, firstRoomMaxZ - 1,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			fillWithAir(world, boundingBox,
					firstRoomMinX + 2, firstRoomMaxY - 4, firstRoomMinZ + 2,
					firstRoomMaxX - 2, firstRoomMaxY - 4, firstRoomMaxZ - 2);
			
			StructureBlockUtil.fillAsGrid(world, boundingBox, primaryBlock,
					new BlockPos(firstRoomMinX + 1, firstRoomMaxY - 3, firstRoomMinZ + 1),
					new BlockPos(firstRoomMinX + 1, firstRoomMaxY - 3, firstRoomMaxZ - 1),
					3); //TODO may not be working yet
			
			BlockPos aspectSymbolPos = new BlockPos(
					getXWithOffset((firstRoomMinX + 5 + firstRoomMaxX - 3) / 2, (firstRoomMinZ + firstRoomMaxZ) / 2),
					getYWithOffset(firstRoomMaxY - 10),
					getZWithOffset((firstRoomMinX + 5 + firstRoomMaxX - 3) / 2, (firstRoomMinZ + firstRoomMaxZ) / 2));
			StructureBlockUtil.placeFeature(world, boundingBox, aspectSymbolPos, getRotation(), getCoordBaseMode(), rand, new ResourceLocation(Minestuck.MOD_ID, "blood_symbol_no_background"));
			StructureBlockUtil.placeFeature(world, boundingBox, aspectSymbolPos.down(3), getRotation(), getCoordBaseMode(), rand, new ResourceLocation(Minestuck.MOD_ID, "breath_symbol_no_background"));
			StructureBlockUtil.placeFeature(world, boundingBox, aspectSymbolPos.down(9), getRotation(), getCoordBaseMode(), rand, Feature.PILLAGER_OUTPOST.getRegistryName());
			*/
			
			//TODO will be for Blood
			//roomVariable1 = rand.nextInt(6); //blood diving to flick switch
			
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
					firstRoomMinX + 16, firstRoomMaxY - 6, firstRoomMaxZ - 5,
					primaryBlock, primaryBlock, false); //second barrier, ends close to edge wall to form barrier with wireless piston setup below!
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 24, firstRoomMaxY - 10, firstRoomMinZ + 8,
					firstRoomMinX + 24, firstRoomMaxY - 6, firstRoomMaxZ - 4,
					primaryBlock, primaryBlock, false); //third barrier
			
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 32, firstRoomMaxY - 10, firstRoomMinZ + 4,
					firstRoomMinX + 32, firstRoomMaxY - 6, firstRoomMaxZ - 8,
					primaryBlock, primaryBlock, false); //fourth barrier
			
			//lighting between barriers
			for(int lightIterate = 0; lightIterate < 4; lightIterate++)
			{
				fillWithBlocks(world, boundingBox,
						firstRoomMinX + 4 + (lightIterate * 8), firstRoomMaxY - 11, firstRoomMinZ + 8,
						firstRoomMinX + 4 + (lightIterate * 8), firstRoomMaxY - 11, firstRoomMaxZ - 8,
						lightBlock, lightBlock, false);
			}
			
			BlockPos spawnerPos;
			for(int xIterate = 0; xIterate < 4; xIterate++)
			{
				//CompoundNBT spawnerNBT = new CompoundNBT();
				//spawnerNBT.("SpawnData", 4);
				
				spawnerPos = new BlockPos(
						getXWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMinZ + 2),
						getYWithOffset(firstRoomMaxY - 5),
						getZWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMinZ + 2));
				world.setBlockState(spawnerPos.down(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				//TileEntity spawnerTE = world.getTileEntity(spawnerPos); //TODO figure out how to change potion effects of spawned entities(give them speed) and increase range at which they spawn
				//				if((spawnerTE instanceof MobSpawnerTileEntity))
				//				{
				//					((MobSpawnerTileEntity) spawnerTE).write(spawnerNBT);
				//				}
				//TODO every maxY value below this line has been shifted down, shift down the above
				spawnerPos = new BlockPos(
						getXWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMaxZ - 2),
						getYWithOffset(firstRoomMaxY - 5),
						getZWithOffset(firstRoomMinX + 5 + xIterate * 10, firstRoomMaxZ - 2));
				world.setBlockState(spawnerPos.down(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				
				fillWithAir(world, boundingBox, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 7, firstRoomMaxZ - 3, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 5, firstRoomMaxZ - 1);
				fillWithAir(world, boundingBox, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 7, firstRoomMinZ + 1, firstRoomMinX + 3 + xIterate * 10, firstRoomMaxY - 5, firstRoomMinZ + 3);
			}
			
			//first side room
			fillWithBlocks(world, boundingBox, firstRoomMinX + 5, firstRoomMaxY - 20, firstRoomMaxZ, firstRoomMinX + 20, firstRoomMaxY - 3, firstRoomMaxZ + 12, secondaryBlock, air, false);
			fillWithBlocks(world, boundingBox, firstRoomMinX + 6, firstRoomMaxY - 19, firstRoomMaxZ + 1, firstRoomMinX + 19, firstRoomMaxY - 10, firstRoomMaxZ + 11, MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //first side room liquid
			fillWithBlocks(world, boundingBox, firstRoomMinX + 6, firstRoomMaxY - 19, firstRoomMaxZ + 1, firstRoomMinX + 20, firstRoomMaxY - 10, firstRoomMaxZ + 1, primaryBlock, primaryBlock, false); //ledge into liquid
			fillWithAir(world, boundingBox, firstRoomMinX + 9, firstRoomMaxY - 9, firstRoomMaxZ - 3, firstRoomMinX + 10, firstRoomMaxY - 7, firstRoomMaxZ); //first side room entrance
			
			//blood diving challenge associated with first side room
			BlockPos transmitterPos = new BlockPos(
					getXWithOffset(firstRoomMinX + 12 + (roomVariable1 - 3), firstRoomMaxZ + 6),
					getYWithOffset(firstRoomMaxY - 21),
					getZWithOffset(firstRoomMinX + 12 + (roomVariable1 - 3), firstRoomMaxZ + 6));
			BlockPos receiverPos = new BlockPos(
					getXWithOffset(firstRoomMinX + 16, firstRoomMaxZ - 1),
					getYWithOffset(firstRoomMaxY - 9),
					getZWithOffset(firstRoomMinX + 16, firstRoomMaxZ - 1));
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, transmitterPos, receiverPos, false);
			setBlockState(world, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), firstRoomMinX + 12 + (roomVariable1 - 3), firstRoomMaxY - 20, firstRoomMaxZ + 6, boundingBox); //power for transmitter
			setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), firstRoomMinX + 16, firstRoomMaxY - 8, firstRoomMaxZ - 1, boundingBox); //wire above receiver, both power pistons
			fillWithAir(world, boundingBox, firstRoomMinX + 16, firstRoomMaxY - 9, firstRoomMaxZ - 3, firstRoomMinX + 16, firstRoomMaxY - 8, firstRoomMaxZ - 3); //hole for piston
			fillWithBlocks(world, boundingBox, firstRoomMinX + 16, firstRoomMaxY - 9, firstRoomMaxZ - 2, firstRoomMinX + 16, firstRoomMaxY - 8, firstRoomMaxZ - 2, Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.SOUTH), Blocks.STICKY_PISTON.getDefaultState(), false);
			fillWithBlocks(world, boundingBox, firstRoomMinX + 16, firstRoomMaxY - 9, firstRoomMaxZ - 4, firstRoomMinX + 16, firstRoomMaxY - 8, firstRoomMaxZ - 4, MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
			
			//stairs leading from puzzle room to lower level
			BlockPos stairsAreaMin = new BlockPos(getXWithOffset(firstRoomMaxX - 2, firstRoomMinZ + 4), getYWithOffset(firstRoomMaxY - 25), getZWithOffset(firstRoomMaxX - 2, firstRoomMinZ + 4));
			BlockPos stairsAreaMax = new BlockPos(getXWithOffset(firstRoomMaxX - 1, firstRoomMaxZ - 5), getYWithOffset(firstRoomMaxY - 7), getZWithOffset(firstRoomMaxX - 1, firstRoomMaxZ - 5));
			//StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, air, stairsAreaMin, stairsAreaMax);
			fillWithAir(world, boundingBox, firstRoomMaxX - 2, firstRoomMaxY - 25, firstRoomMinZ + 4, firstRoomMaxX - 1, firstRoomMaxY - 7, firstRoomMaxZ - 5);
			StructureBlockUtil.createStairs(world, boundingBox, primaryBlock, primaryStairBlock.with(StairsBlock.FACING, getCoordBaseMode()), stairsAreaMin.offset(getCoordBaseMode(), 13), 16, 2, getCoordBaseMode(), false);
			fillWithBlocks(world, boundingBox,
					firstRoomMaxX - 1, firstRoomMaxY - 9, firstRoomMinZ + 12,
					firstRoomMaxX - 1, firstRoomMaxY - 8, firstRoomMinZ + 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, firstRoomMaxX - 1, firstRoomMaxY - 7, firstRoomMinZ + 12, boundingBox);
			setBlockState(world, primaryStairBlock.with(StairsBlock.FACING, getCoordBaseMode().rotateY()).with(StairsBlock.HALF, Half.TOP), firstRoomMaxX - 1, firstRoomMaxY - 10, firstRoomMinZ + 12, boundingBox);
			fillWithBlocks(world, boundingBox,
					firstRoomMaxX - 1, firstRoomMaxY - 9, firstRoomMaxZ - 12,
					firstRoomMaxX - 1, firstRoomMaxY - 8, firstRoomMaxZ - 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, firstRoomMaxX - 1, firstRoomMaxY - 7, firstRoomMaxZ - 12, boundingBox);
			setBlockState(world, primaryStairBlock.with(StairsBlock.FACING, getCoordBaseMode().rotateY()).with(StairsBlock.HALF, Half.TOP), firstRoomMaxX - 1, firstRoomMaxY - 10, firstRoomMaxZ - 12, boundingBox);
			
			//edges of lower level
			fillWithAir(world, boundingBox,
					firstRoomMinX + 1, firstRoomMinY + 10, firstRoomMinZ + 3,
					firstRoomMaxX - 3, firstRoomMinY + 14, firstRoomMaxZ - 3); //lower section ceiling
			fillWithAir(world, boundingBox,
					firstRoomMinX + 5, firstRoomMinY + 5, firstRoomMinZ + 3,
					firstRoomMaxX - 3, firstRoomMinY + 9, firstRoomMaxZ - 3); //lower section chamber(exception of blood fluid)
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 5, firstRoomMinY + 1, firstRoomMinZ + 3,
					firstRoomMaxX - 3, firstRoomMinY + 4, firstRoomMaxZ - 3,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //liquid of floor
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 9, firstRoomMinY + 11, firstRoomMinZ + 1,
					firstRoomMaxX - 9, firstRoomMinY + 11, firstRoomMinZ + 2,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //side waterfall
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 9, firstRoomMinY + 11, firstRoomMaxZ - 2,
					firstRoomMaxX - 9, firstRoomMinY + 11, firstRoomMaxZ - 1,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //side waterfall
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, new BlockPos(
							getXWithOffset((firstRoomMinX + firstRoomMaxX) / 2 - 10, (firstRoomMinZ + firstRoomMaxZ) / 2),
							getYWithOffset(firstRoomMinY + 14),
							getZWithOffset((firstRoomMinX + firstRoomMaxX) / 2 - 10, (firstRoomMinZ + firstRoomMaxZ) / 2)),
					6, 1); //ceiling light
			
			//lighting
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 3, firstRoomMinY + 10, firstRoomMinZ + 8,
					firstRoomMinX + 3, firstRoomMinY + 11, firstRoomMinZ + 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, firstRoomMinX + 3, firstRoomMinY + 12, firstRoomMinZ + 8, boundingBox);
			fillWithBlocks(world, boundingBox,
					firstRoomMinX + 3, firstRoomMinY + 10, firstRoomMaxZ - 8,
					firstRoomMinX + 3, firstRoomMinY + 11, firstRoomMaxZ - 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, firstRoomMinX + 3, firstRoomMinY + 12, firstRoomMaxZ - 8, boundingBox);
			
			//secret redstone lamp setup //TODO create thing this puzzle actually does
			fillWithBlocks(world, boundingBox,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 10, firstRoomMinZ + 1,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 12, firstRoomMinZ + 4,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			fillWithBlocks(world, boundingBox,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 1, firstRoomMinZ + 1,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 9, firstRoomMinZ + 4,
					primaryBlock, primaryBlock, false);
			BlockPos side1SwitchLampPos = new BlockPos(
					getXWithOffset((firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinZ + 3),
					getYWithOffset(firstRoomMinY + 10),
					getZWithOffset((firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinZ + 3));
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, side1SwitchLampPos.offset(getCoordBaseMode().getOpposite()), side1SwitchLampPos.offset(getCoordBaseMode().getOpposite(), 2).down(5), true);
			world.setBlockState(side1SwitchLampPos, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side1SwitchLampPos.offset(getCoordBaseMode()), Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true), Constants.BlockFlags.BLOCK_UPDATE);
			fillWithBlocks(world, boundingBox,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 10, firstRoomMaxZ - 4,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 12, firstRoomMaxZ - 1,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			fillWithBlocks(world, boundingBox,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 1, firstRoomMaxZ - 4,
					(firstRoomMinX + firstRoomMaxX) / 2, firstRoomMinY + 9, firstRoomMaxZ - 1,
					primaryBlock, primaryBlock, false);
			BlockPos side2SwitchLampPos = new BlockPos(
					getXWithOffset((firstRoomMinX + firstRoomMaxX) / 2, firstRoomMaxZ - 3),
					getYWithOffset(firstRoomMinY + 10),
					getZWithOffset((firstRoomMinX + firstRoomMaxX) / 2, firstRoomMaxZ - 3));
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, side2SwitchLampPos.offset(getCoordBaseMode()), side2SwitchLampPos.offset(getCoordBaseMode(), 2).down(5), true);
			world.setBlockState(side2SwitchLampPos, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side2SwitchLampPos.offset(getCoordBaseMode().getOpposite()), Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true), Constants.BlockFlags.BLOCK_UPDATE);
			
			//aspect symbol platform, lower level
			BlockPos aspectSymbolPos = new BlockPos(
					getXWithOffset((firstRoomMinX + 5 + firstRoomMaxX - 3) / 2, (firstRoomMinZ + firstRoomMaxZ) / 2),
					getYWithOffset(firstRoomMinY + 4),
					getZWithOffset((firstRoomMinX + 5 + firstRoomMaxX - 3) / 2, (firstRoomMinZ + firstRoomMaxZ) / 2)); //middle of lower room on top of blood
			StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock, aspectSymbolPos.down(3), 12, 3);
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.down(1), 12, 1);
			StructureBlockUtil.createCylinder(world, boundingBox, primaryBlock, aspectSymbolPos, 12, 1);
			StructureBlockUtil.placeFeature(world, boundingBox, aspectSymbolPos, getCoordBaseMode(), rand, new ResourceLocation(Minestuck.MOD_ID, "blood_aspect_symbol"));
			
			//redstone components for lich fight and piston stairway unlock, inside aspect platform
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 7), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8).offset(getCoordBaseMode()), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8).offset(getCoordBaseMode().getOpposite()), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 9), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeRemoteObserver(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8), RemoteObserverTileEntity.ActiveType.IS_PLAYER_PRESENT); //checks for presence of player
			StructureBlockUtil.placeStatStorer(world, boundingBox, aspectSymbolPos.down(2), StatStorerTileEntity.ActiveType.DEATHS, 1); //counts how many deaths there have been(need 10 kills to activate all 5 pistons)
			//fillWithBlocks(world, boundingBox, aspectSymbolPos.getX(), aspectSymbolPos.down(2).getY(), aspectSymbolPos.offset(getCoordBaseMode().rotateYCCW()).getZ(), aspectSymbolPos.getX(), aspectSymbolPos.down(2).getY(), aspectSymbolPos.offset(getCoordBaseMode().rotateYCCW()).getZ());
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.getDefaultState(), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateYCCW()), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateYCCW(), 9));
			//world.setBlockState(aspectSymbolPos.down(2), Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.NORTH, RedstoneSide.SIDE).with(RedstoneWireBlock.WEST, RedstoneSide.SIDE), Constants.BlockFlags.BLOCK_UPDATE);
			
			fillWithBlocks(world, boundingBox, firstRoomMinX + 3, firstRoomMinY + 4, (firstRoomMinZ + firstRoomMaxZ) / 2 - 3, firstRoomMinX + 3, firstRoomMinY + 8, (firstRoomMinZ + firstRoomMaxZ) / 2 + 2, lightBlock, lightBlock, false);
			//TODO Add remote observer closer to far edge on player detect mode connected to mob summoning blocks, and set stat storer near center on death mode and add wireless relays at different distances using for loop for each summoned entity(allows players to see progress)
			for(int stairPuzzleIterate = 0; stairPuzzleIterate < 5; stairPuzzleIterate++)
			{
				StructureBlockUtil.placeWirelessRelay(world, boundingBox, aspectSymbolPos.down(3).offset(getCoordBaseMode().rotateYCCW(), stairPuzzleIterate * 2 + 1), new BlockPos(
						getXWithOffset(firstRoomMinX + 2, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate),
						getYWithOffset(firstRoomMinY + 4 + stairPuzzleIterate),
						getZWithOffset(firstRoomMinX + 2, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate)), false);
				setBlockState(world, Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.EAST), firstRoomMinX + 3, firstRoomMinY + 4 + stairPuzzleIterate, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
				setBlockState(world, secondaryDecorativeBlock, firstRoomMinX + 4, firstRoomMinY + 4 + stairPuzzleIterate, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
			}
		} else if(worldAspect == EnumAspect.LIGHT)
		{
		
		} else if(worldAspect == EnumAspect.TIME) //spikes that shoot up on a timed interval so you have to match the rhythm to pass
		{
		} else if(worldAspect == EnumAspect.HEART) //stairs going all throughout the structure leading to different small rooms with levers that all need pulling in order to pass
		{
		} else if(worldAspect == EnumAspect.RAGE) //difficult terrain made by odd geometric shapes blocking path
		{
		} else if(worldAspect == EnumAspect.BLOOD) //nonhazardous liquid that player has to trudge through while enemies approach on all sides
		{
		} else if(worldAspect == EnumAspect.DOOM)
		{
		} else if(worldAspect == EnumAspect.VOID) //invisible platforms or barriers? Somewhat overlapping with hope there
		{
		} else if(worldAspect == EnumAspect.SPACE) //portal 2 gel puzzles(jumping), may make two block tall barriers for aspect effect
		{
		} else if(worldAspect == EnumAspect.MIND) //maze, may make it dark for aspect effect
		{
		} else if(worldAspect == EnumAspect.HOPE) //indiana jones leap of faith bridge with invisible blocks that dissapear if you crouch
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
		tagCompound.putInt("roomVariable1", roomVariable1);
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