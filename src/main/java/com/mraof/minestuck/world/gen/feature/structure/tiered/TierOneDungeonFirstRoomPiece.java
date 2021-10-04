package com.mraof.minestuck.world.gen.feature.structure.tiered;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
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
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonFirstRoomPiece extends ImprovedStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomRoomType;
	private int roomVariable1;
	
	/**/ //these are offset way too much
	private static final int firstRoomMinX = 42;
	private static final int firstRoomMinY = 0;
	private static final int firstRoomMinZ = 20;
	private static final int firstRoomMaxX = 82;
	private static final int firstRoomMaxY = 30;
	private static final int firstRoomMaxZ = 57;
	/**/
	
	/* //using these causes the world to no longer load new chunks
	private static final int firstRoomMinX = 0;
	private static final int firstRoomMinY = 0;
	private static final int firstRoomMinZ = 0;
	private static final int firstRoomMaxX = 40;
	private static final int firstRoomMaxY = 30;
	private static final int firstRoomMaxZ = 37;
	/**/
	
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
	private Template bloodSymbolTemplate;
	
	public TierOneDungeonFirstRoomPiece(TemplateManager templates, ChunkGenerator<?> generator, Random random, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_FIRST_ROOM, 0);
		
		setRandomDirection(random);
		setBounds(x, y, z, 82, 50, 82);
		
		initTemplates(templates);
	}
	
	public TierOneDungeonFirstRoomPiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_FIRST_ROOM, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomRoomType = nbt.getInt("randomRoomType");
		roomVariable1 = nbt.getInt("roomVariable1");
		
		initTemplates(templates);
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("sp1", spawner1); //spawner type room only
		tagCompound.putBoolean("sp2", spawner2); //spawner type room only
		tagCompound.putInt("randomRoomType", randomRoomType);
	}
	
	private void initTemplates(TemplateManager templates)
	{
		bloodSymbolTemplate = templates.getTemplateDefaulted(new ResourceLocation(Minestuck.MOD_ID, "blood_symbol_no_background"));
	}
	
	@Override
	public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGenerator, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn)
	{
		Debug.debugf("TierOneDungeonFirstRoomPiece");
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGenerator.getSettings());
		
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
			randomRoomType = randomIn.nextInt(2);
			roomVariable1 = randomIn.nextInt(6);
			createRan = true;
		}
		spawner1 = false;
		spawner2 = false;
		
		fillWithBlocks(worldIn, boundingBoxIn,
				firstRoomMinX, firstRoomMinY, firstRoomMinZ,
				firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,
				primaryBlock, air, false);
		
		buildAspectThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGenerator);
		
		//buildStructureFoundation(worldIn, boundingBoxIn, randomIn, randomRoomType);
		//buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		//carveRooms(worldIn, boundingBoxIn);
		//buildIndoorBlocks(worldIn, boundingBoxIn, randomIn, randomRoomType);
		
		return true;
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
			StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock, aspectSymbolPos.down(3), 13, 3);
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.down(1), 13, 1);
			StructureBlockUtil.createCylinder(world, boundingBox, primaryBlock, aspectSymbolPos, 13, 1);
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos.offset(getCoordBaseMode().rotateY()), bloodSymbolTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getCoordBaseMode())));
			
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
						getZWithOffset(firstRoomMinX + 2, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate)), true);
				setBlockState(world, Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.EAST), firstRoomMinX + 3, firstRoomMinY + 4 + stairPuzzleIterate, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
				setBlockState(world, secondaryDecorativeBlock, firstRoomMinX + 4, firstRoomMinY + 4 + stairPuzzleIterate, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
			}
			
			//area effect block/how to complete puzzle
			BlockPos areaEffectBlockPos = new BlockPos(
					getXWithOffset(firstRoomMinX + 2, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2),
					getYWithOffset(firstRoomMinY + 9),
					getZWithOffset(firstRoomMinX + 2, (firstRoomMinZ + firstRoomMaxZ) / 2 - 2));
			setBlockState(world, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), areaEffectBlockPos.getX(), areaEffectBlockPos.up().getY(), areaEffectBlockPos.getZ(), boundingBox); //power for area effect block
			StructureBlockUtil.placeAreaEffectBlock(world, boundingBox, areaEffectBlockPos,
					MSEffects.CREATIVE_SHOCK.get(), 0, new BlockPos(
							getXWithOffset(firstRoomMinX - 22, firstRoomMinZ),
							getYWithOffset(firstRoomMinY),
							getZWithOffset(firstRoomMinX - 22, firstRoomMinZ)), new BlockPos(
							getXWithOffset(firstRoomMaxX, firstRoomMaxZ),
							getYWithOffset(58),
							getZWithOffset(firstRoomMaxX, firstRoomMaxZ)));
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
}