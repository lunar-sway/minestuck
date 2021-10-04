package com.mraof.minestuck.world.gen.feature.structure.tiered;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.*;
import com.mraof.minestuck.block.redstone.SolidSwitchBlock;
import com.mraof.minestuck.effects.MSEffects;
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
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonPiece extends ImprovedStructurePiece
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
	
	private static final BlockState air = Blocks.AIR.getDefaultState();
	//private BlockState ground; //dont use because ores get embedded in it
	private BlockState primaryBlock;
	private BlockState primaryCrackedBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState primarySlabBlock;
	private BlockState secondaryBlock;
	//private BlockState aspectSapling;
	private BlockState fluid;
	private BlockState lightBlock;
	
	private EnumAspect worldAspect;
	private EnumClass worldClass;
	private TerrainLandType worldTerrain;
	
	
	public TierOneDungeonPiece(TemplateManager templates, ChunkGenerator<?> generator, Random random, int x, int z)
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON, 0);
		
		setRandomDirection(random);
		setBoundsWithWorldHeight(generator, x, z, 82, 60, 82, -1, Heightmap.Type.OCEAN_FLOOR_WG); //x = 42, z = 32
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
		
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryCrackedBlock = blocks.getBlockState("structure_primary_cracked");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		primaryPillarBlock = blocks.getBlockState("structure_primary_pillar");
		primarySlabBlock = blocks.getBlockState("structure_primary_slab");
		secondaryBlock = blocks.getBlockState("structure_secondary");
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
		StructureBlockUtil.createSphere(world, boundingBox, air, getActualPos((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMaxY + 2, entryRoomMinZ),
				10, fluid); //clears area around structure in a more organic fashion
		
		BlockPos segsg = getActualPos((entryRoomMaxX + entryRoomMinX) / 2, entryRoomMaxY + 8, entryRoomMinZ);
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
		//buildAspectThemedPuzzle(world, boundingBox, rand, chunkGeneratorIn);
	}
	
	private void buildWallsAndFloors(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		BlockPos doorInterfacePos = getActualPos(entryRoomMinX + 5, entryRoomMinY + 3, entryRoomMinZ);
		
		BlockPos minDoorPos = getActualPos(entryRoomMinX + 6, entryRoomMinY + 1, entryRoomMinZ);
		BlockPos maxDoorPos = getActualPos(entryRoomMaxX - 6, entryRoomMaxY - 3, entryRoomMinZ);
		
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
		
		BlockPos chestPosLeft = getActualPos(lowerRoomMinX - 4, lowerRoomMinY + 3, lowerRoomMinZ + 9);
		StructureBlockUtil.placeLootBlock(chestPosLeft, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		chestPosLeft = getActualPos(lowerRoomMinX - 2, lowerRoomMinY + 3, lowerRoomMinZ + 9);
		StructureBlockUtil.placeLootBlock(chestPosLeft, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		
		BlockPos chestPosRight = getActualPos(lowerRoomMinX - 4, lowerRoomMinY + 3, lowerRoomMaxZ - 9);
		StructureBlockUtil.placeLootBlock(chestPosRight, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().getOpposite()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		chestPosRight = getActualPos(lowerRoomMinX - 2, lowerRoomMinY + 3, lowerRoomMaxZ - 9);
		StructureBlockUtil.placeLootBlock(chestPosRight, world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().getOpposite()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
		
		BlockPos chestPosLeftb = getActualPos(lowerRoomMinX - 2, lowerRoomMinY + 5, lowerRoomMinZ + 9);
		StructureBlockUtil.placeChest(chestPosLeftb, world, boundingBox, getCoordBaseMode(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosLeftb = getActualPos(lowerRoomMinX - 1, lowerRoomMinY + 5, lowerRoomMinZ + 9);
		StructureBlockUtil.placeChest(chestPosLeftb, world, boundingBox, getCoordBaseMode(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		BlockPos chestPosRightb = getActualPos(lowerRoomMinX - 2, lowerRoomMinY + 5, lowerRoomMaxZ - 9);
		StructureBlockUtil.placeChest(chestPosRightb, world, boundingBox, getCoordBaseMode().getOpposite(), rightChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		chestPosRightb = getActualPos(lowerRoomMinX - 1, lowerRoomMinY + 5, lowerRoomMaxZ - 9);
		StructureBlockUtil.placeChest(chestPosRightb, world, boundingBox, getCoordBaseMode().getOpposite(), leftChestType, MSLootTables.TIER_ONE_MEDIUM_CHEST, rand);
		
		StructureBlockUtil.placeReturnNode(world, boundingBox, getActualPos(lowerRoomMinX - 7, lowerRoomMinY + 3, (lowerRoomMaxZ + lowerRoomMinZ) / 2), getCoordBaseMode());
		
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMinZ + 10, boundingBox); //left side light
		setBlockState(world, lightBlock, lowerRoomMinX, lowerRoomMinY + 5, lowerRoomMaxZ - 10, boundingBox); //right side light
	}
	
	private void buildIndoorBlocks(IWorld world, MutableBoundingBox boundingBox, Random rand, int randomRoomType)
	{
		BlockPos staircaseMinPos = getActualPos(entryRoomMinX + 6, lowerRoomMinY + 2, entryRoomMinZ + 6);
		BlockPos staircaseMaxPos = getActualPos(entryRoomMaxX - 6, entryRoomMinY, entryRoomMaxZ - 6);
		
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
		StructureBlockUtil.createCylinder(world, boundingBox, primarySlabBlock.with(SlabBlock.TYPE, SlabType.BOTTOM),
				getActualPos(lowerRoomMinX + 14, lowerRoomMinY + 3, lowerRoomMinZ + 13),
				6, 1);
		StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock,
				getActualPos(lowerRoomMinX + 14, lowerRoomMinY + 3, lowerRoomMinZ + 13),
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
			BlockPos spawnerPos = getActualPos(lowerRoomMinX + 4, lowerRoomMinY + 6, lowerRoomMinZ + 4);
			bottomRoomSpawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
		}
		if(bottomRoomSpawner2)
		{
			//fillWithBlocks(world, boundingBox, entryRoomMaxX - 2, entryRoomMinY - 11, entryRoomMaxZ - 2, entryRoomMaxX - 2, entryRoomMinY - 8, entryRoomMaxZ - 2, primaryDecorativeBlock, primaryDecorativeBlock, false);
			BlockPos spawnerPos = getActualPos(lowerRoomMaxX - 4, lowerRoomMinY + 6, lowerRoomMaxZ - 4);
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
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound)
	{
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