package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
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
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonSecondaryPiece extends ImprovedStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomRoomType;
	private int roomVariable1;
	
	/**/ //these are offset way too much
	private static final int pieceMinX = 42;
	private static final int pieceMinY = -50;
	private static final int pieceMinZ = 20;
	private static final int pieceMaxX = 82;
	private static final int pieceMaxY = -20;
	private static final int pieceMaxZ = 57;
	
	private static final BlockState air = Blocks.AIR.getDefaultState();
	private BlockState primaryBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState primaryStairBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	private BlockState lightBlock;
	
	private EnumAspect worldAspect;
	private EnumClass worldClass;
	private TerrainLandType worldTerrain;
	private Template bloodSymbolTemplate;
	
	public TierOneDungeonSecondaryPiece(TemplateManager templates, Direction direction, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_SECONDARY, 0);
		
		setCoordBaseMode(direction);
		setBounds(x, y, z, 82, 50, 82);
		
		initTemplates(templates);
	}
	
	public TierOneDungeonSecondaryPiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_SECONDARY, nbt);
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
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGenerator.getSettings());
		
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		primaryPillarBlock = blocks.getBlockState("structure_primary_pillar");
		primaryStairBlock = blocks.getBlockState("structure_primary_stairs");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
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
		
		//fillWithBlocks(worldIn, boundingBoxIn,firstRoomMinX, firstRoomMinY, firstRoomMinZ,firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,primaryBlock, air, false);
		
		buildAspectThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGenerator);
		
		return true;
	}
	
	private void buildAspectThemedPuzzle(IWorld world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator<?> chunkGeneratorIn)
	{
		fillWithBlocks(world, boundingBox,
				pieceMinX, pieceMinY, pieceMinZ,
				pieceMaxX, pieceMaxY, pieceMaxZ,
				secondaryBlock, secondaryBlock, false); //filling area with bricks to carve out from
		
		fillWithAir(world, boundingBox, pieceMinX - 3, pieceMaxY - 9, pieceMinZ + 16, pieceMinX, pieceMaxY - 5, pieceMaxZ - 16); //entrance
		
		//lighting at entrance
		setBlockState(world, lightBlock, pieceMinX, pieceMaxY - 7, pieceMinZ + 15, boundingBox);
		setBlockState(world, lightBlock, pieceMinX, pieceMaxY - 7, pieceMaxZ - 15, boundingBox);
		
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
					pieceMinX + 1, pieceMaxY - 5, pieceMinZ + 1,
					pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1); //ceiling down to raised areas
			fillWithAir(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 9, pieceMinZ + 4,
					pieceMaxX - 1, pieceMaxY - 5, pieceMaxZ - 4); //makes walls of raised areas
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 10, pieceMinZ + 4,
					pieceMaxX - 4, pieceMaxY - 10, pieceMaxZ - 4,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false);
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 8, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 8, pieceMaxY - 6, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //first barrier
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 16, pieceMaxY - 10, pieceMinZ + 4,
					pieceMinX + 16, pieceMaxY - 6, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //second barrier, ends close to edge wall to form barrier with wireless piston setup below!
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 24, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 24, pieceMaxY - 6, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //third barrier
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 32, pieceMaxY - 10, pieceMinZ + 4,
					pieceMinX + 32, pieceMaxY - 6, pieceMaxZ - 8,
					primaryBlock, primaryBlock, false); //fourth barrier
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 37, pieceMaxY - 10, pieceMinZ + 1,
					pieceMinX + 37, pieceMaxY - 1, pieceMaxZ - 1,
					secondaryBlock, secondaryBlock, false); //false wall
			
			fillWithBlocks(world, boundingBox,
					pieceMinX + 34, pieceMaxY - 11, pieceMinZ + 8,
					pieceMinX + 35, pieceMaxY - 11, pieceMaxZ - 8,
					lightBlock, lightBlock, false); //lighting between false wall and fourth barrier
			
			//lighting between barriers
			for(int lightIterate = 0; lightIterate < 4; lightIterate++)
			{
				fillWithBlocks(world, boundingBox,
						pieceMinX + 4 + (lightIterate * 8), pieceMaxY - 11, pieceMinZ + 8,
						pieceMinX + 4 + (lightIterate * 8), pieceMaxY - 11, pieceMaxZ - 8,
						lightBlock, lightBlock, false);
			}
			
			BlockPos spawnerPos;
			for(int xIterate = 0; xIterate < 4; xIterate++)
			{
				//CompoundNBT spawnerNBT = new CompoundNBT();
				//spawnerNBT.("SpawnData", 4);
				
				spawnerPos = getActualPos(pieceMinX + 5 + xIterate * 10, pieceMaxY - 5, pieceMinZ + 2);
				world.setBlockState(spawnerPos.down(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				//TileEntity spawnerTE = world.getTileEntity(spawnerPos); //TODO figure out how to change potion effects of spawned entities(give them speed) and increase range at which they spawn
				//				if((spawnerTE instanceof MobSpawnerTileEntity))
				//				{
				//					((MobSpawnerTileEntity) spawnerTE).write(spawnerNBT);
				//				}
				//TODO every maxY value below this line has been shifted down, shift down the above
				spawnerPos = getActualPos(pieceMinX + 5 + xIterate * 10, pieceMaxY - 5, pieceMaxZ - 2);
				world.setBlockState(spawnerPos.down(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				
				//fillWithAir(world, boundingBox, pieceMinX + 3 + xIterate * 10, pieceMaxY - 7, pieceMaxZ - 3, pieceMinX + 3 + xIterate * 10, pieceMaxY - 5, pieceMaxZ - 1);
				//fillWithAir(world, boundingBox, pieceMinX + 3 + xIterate * 10, pieceMaxY - 7, pieceMinZ + 1, pieceMinX + 3 + xIterate * 10, pieceMaxY - 5, pieceMinZ + 3);
			}
			
			//first side room
			fillWithBlocks(world, boundingBox, pieceMinX + 5, pieceMaxY - 20, pieceMaxZ, pieceMinX + 20, pieceMaxY - 3, pieceMaxZ + 12, secondaryBlock, air, false);
			fillWithBlocks(world, boundingBox, pieceMinX + 6, pieceMaxY - 19, pieceMaxZ + 1, pieceMinX + 19, pieceMaxY - 10, pieceMaxZ + 11, MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //first side room liquid
			fillWithBlocks(world, boundingBox, pieceMinX + 6, pieceMaxY - 19, pieceMaxZ + 1, pieceMinX + 20, pieceMaxY - 10, pieceMaxZ + 1, primaryBlock, primaryBlock, false); //ledge into liquid
			fillWithAir(world, boundingBox, pieceMinX + 9, pieceMaxY - 9, pieceMaxZ - 3, pieceMinX + 10, pieceMaxY - 7, pieceMaxZ); //first side room entrance
			setBlockState(world, lightBlock, pieceMinX + 5 , pieceMaxY - 7, pieceMaxZ + 6, boundingBox); //lighting
			setBlockState(world, lightBlock, pieceMinX + 20 , pieceMaxY - 7, pieceMaxZ + 6, boundingBox); //lighting
			
			//blood diving challenge associated with first side room
			BlockPos transmitterPos = getActualPos(pieceMinX + 12 + (roomVariable1 - 3), pieceMaxY - 21, pieceMaxZ + 6);
			BlockPos receiverPos = getActualPos(pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 1);
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, transmitterPos, receiverPos, true);
			setBlockState(world, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), pieceMinX + 12 + (roomVariable1 - 3), pieceMaxY - 20, pieceMaxZ + 6, boundingBox); //power for transmitter
			setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 1, boundingBox); //wire above receiver, both power pistons
			fillWithAir(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 3, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 3); //hole for piston
			fillWithBlocks(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 2, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 2, Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.NORTH), Blocks.STICKY_PISTON.getDefaultState(), false);
			fillWithBlocks(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 4, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 4, MSBlocks.DUNGEON_DOOR.getDefaultState(), MSBlocks.DUNGEON_DOOR.getDefaultState(), false);
			
			//second side room
			fillWithBlocks(world, boundingBox, pieceMaxX - 10, pieceMaxY - 10, pieceMinZ - 10, pieceMaxX - 5, pieceMaxY, pieceMinZ, secondaryBlock, air, false);
			fillWithAir(world, boundingBox, pieceMaxX - 7, pieceMaxY - 9, pieceMinZ - 3, pieceMaxX - 6, pieceMaxY - 7, pieceMinZ + 3); //second side room entrance
			fillWithAir(world, boundingBox, pieceMaxX - 7, pieceMaxY - 5, pieceMinZ - 3, pieceMaxX - 6, pieceMaxY - 3, pieceMinZ + 3); //second side room exit
			StructureBlockUtil.placeSpiralStaircase(world, boundingBox, getActualPos(pieceMaxX - 9, pieceMaxY - 9, pieceMinZ - 9), getActualPos(pieceMaxX - 6, pieceMaxY - 6, pieceMinZ + 1), primaryDecorativeBlock);
			fillWithBlocks(world, boundingBox, pieceMaxX - 9, pieceMaxY - 6, pieceMinZ - 8, pieceMaxX - 6, pieceMaxY - 6, pieceMinZ + 1, secondaryBlock, air, false); //second level floor
			fillWithBlocks(world, boundingBox, pieceMaxX - 9, pieceMaxY - 6, pieceMinZ - 10, pieceMaxX - 6, pieceMaxY - 6, pieceMinZ - 10, lightBlock, lightBlock, false); //second level floor
			StructureBlockUtil.placeLootBlock(getActualPos(pieceMaxX - 6, pieceMaxY - 9, pieceMinZ - 5), world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().rotateY()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
			
			fillWithBlocks(world, boundingBox,
					pieceMinX, pieceMinY + 10, pieceMinZ + 2,
					pieceMaxX, pieceMinY + 10, pieceMaxZ - 2,
					lightBlock, lightBlock, false); //light around wall in lower chamber, placed here so other functions can carve it
			
			//stairs leading from puzzle room to lower level
			fillWithAir(world, boundingBox, pieceMaxX - 3, pieceMaxY - 5, pieceMaxZ - 3, pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1); //entrance to stairs
			BlockPos stairsStart = getActualPos(pieceMaxX - 1, pieceMaxY - 25, pieceMinZ + 17);
			fillWithAir(world, boundingBox, pieceMaxX - 2, pieceMaxY - 25, pieceMinZ + 4, pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1);
			StructureBlockUtil.createStairs(world, boundingBox, primaryBlock, primaryStairBlock.with(StairsBlock.FACING, getCoordBaseMode()), stairsStart, 20, 2, getCoordBaseMode(), false);
			fillWithBlocks(world, boundingBox,
					pieceMaxX - 1, pieceMaxY - 9, pieceMinZ + 12,
					pieceMaxX - 1, pieceMaxY - 8, pieceMinZ + 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, pieceMaxX - 1, pieceMaxY - 7, pieceMinZ + 12, boundingBox);
			setBlockState(world, primaryStairBlock.with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP), pieceMaxX - 1, pieceMaxY - 10, pieceMinZ + 12, boundingBox);
			fillWithBlocks(world, boundingBox,
					pieceMaxX - 1, pieceMaxY - 9, pieceMaxZ - 12,
					pieceMaxX - 1, pieceMaxY - 8, pieceMaxZ - 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, pieceMaxX - 1, pieceMaxY - 7, pieceMaxZ - 12, boundingBox);
			setBlockState(world, primaryStairBlock.with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP), pieceMaxX - 1, pieceMaxY - 10, pieceMaxZ - 12, boundingBox);
			
			//edges of lower level
			fillWithAir(world, boundingBox,
					pieceMinX + 1, pieceMinY + 10, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 14, pieceMaxZ - 3); //lower section ceiling
			fillWithAir(world, boundingBox,
					pieceMinX + 5, pieceMinY + 5, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 9, pieceMaxZ - 3); //lower section chamber(exception of blood fluid)
			fillWithBlocks(world, boundingBox,
					pieceMinX + 5, pieceMinY + 1, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 4, pieceMaxZ - 3,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //liquid of floor
			fillWithBlocks(world, boundingBox,
					pieceMinX + 9, pieceMinY + 11, pieceMinZ + 1,
					pieceMaxX - 9, pieceMinY + 11, pieceMinZ + 2,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //side waterfall
			fillWithBlocks(world, boundingBox,
					pieceMinX + 9, pieceMinY + 11, pieceMaxZ - 2,
					pieceMaxX - 9, pieceMinY + 11, pieceMaxZ - 1,
					MSBlocks.BLOOD.getDefaultState(), MSBlocks.BLOOD.getDefaultState(), false); //side waterfall
			
			//lighting
			fillWithBlocks(world, boundingBox,
					pieceMinX + 3, pieceMinY + 10, pieceMinZ + 8,
					pieceMinX + 3, pieceMinY + 11, pieceMinZ + 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, pieceMinX + 3, pieceMinY + 12, pieceMinZ + 8, boundingBox);
			fillWithBlocks(world, boundingBox,
					pieceMinX + 3, pieceMinY + 10, pieceMaxZ - 8,
					pieceMinX + 3, pieceMinY + 11, pieceMaxZ - 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlockState(world, lightBlock, pieceMinX + 3, pieceMinY + 12, pieceMaxZ - 8, boundingBox);
			
			//secret redstone lamp setup //TODO create thing this puzzle actually does
			fillWithBlocks(world, boundingBox,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMinZ + 1,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 12, pieceMinZ + 4,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			fillWithBlocks(world, boundingBox,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 1, pieceMinZ + 1,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 9, pieceMinZ + 4,
					primaryBlock, primaryBlock, false);
			BlockPos side1SwitchLampPos = getActualPos((pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMinZ + 3);
			//TODO receiver does not seem to be generating
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, side1SwitchLampPos.offset(getCoordBaseMode().getOpposite()), side1SwitchLampPos.offset(getCoordBaseMode(), 4).down(9).offset(getCoordBaseMode().rotateY(), 2), true);
			world.setBlockState(side1SwitchLampPos.offset(getCoordBaseMode(), 4).down(8).offset(getCoordBaseMode().rotateY(), 2), Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.SOUTH), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side1SwitchLampPos.offset(getCoordBaseMode(), 3).down(8), MSBlocks.BLOOD.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			StructureBlockUtil.placeLootBlock(side1SwitchLampPos.offset(getCoordBaseMode(), 5).down(8), world, boundingBox, MSBlocks.LOOT_CHEST.getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, getCoordBaseMode().rotateY()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
			world.setBlockState(side1SwitchLampPos.offset(getCoordBaseMode(), 3).down(7), MSBlocks.BLOOD.getDefaultState(), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side1SwitchLampPos, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side1SwitchLampPos.offset(getCoordBaseMode()), Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true), Constants.BlockFlags.BLOCK_UPDATE);
			fillWithBlocks(world, boundingBox,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMaxZ - 4,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 12, pieceMaxZ - 1,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			fillWithBlocks(world, boundingBox,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 1, pieceMaxZ - 4,
					(pieceMinX + pieceMaxX) / 2, pieceMinY + 9, pieceMaxZ - 1,
					primaryBlock, primaryBlock, false);
			BlockPos side2SwitchLampPos = getActualPos((pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMaxZ - 3);
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, side2SwitchLampPos.offset(getCoordBaseMode()), side2SwitchLampPos.offset(getCoordBaseMode(), 2).down(5), true);
			world.setBlockState(side2SwitchLampPos, MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlockState(side2SwitchLampPos.offset(getCoordBaseMode().getOpposite()), Blocks.REDSTONE_LAMP.getDefaultState().with(RedstoneLampBlock.LIT, true), Constants.BlockFlags.BLOCK_UPDATE);
			
			//aspect symbol platform
			BlockPos aspectSymbolPos = getActualPos((pieceMinX + 5 + pieceMaxX - 3) / 2, pieceMinY + 4, (pieceMinZ + pieceMaxZ) / 2); //middle of lower room on top of blood
			StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock, aspectSymbolPos.down(3), 13, 3);
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.down(1), 13, 1);
			StructureBlockUtil.createCylinder(world, boundingBox, primaryBlock, aspectSymbolPos, 13, 1);
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos, bloodSymbolTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getCoordBaseMode().getOpposite())));
			//StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, getActualPos((firstRoomMinX + firstRoomMaxX) / 2 - 10, firstRoomMinY + 14, (firstRoomMinZ + firstRoomMaxZ) / 2), 6, 1); //ceiling light
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.up(10), 6, 1); //ceiling light
			
			//redstone components for lich fight and piston stairway unlock, inside aspect platform
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 7), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8).offset(getCoordBaseMode()), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8).offset(getCoordBaseMode().getOpposite()), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 9), SummonerTileEntity.SummonType.LICH);
			StructureBlockUtil.placeRemoteObserver(world, boundingBox, aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 8), RemoteObserverTileEntity.ActiveType.IS_PLAYER_PRESENT); //checks for presence of player
			StructureBlockUtil.placeStatStorer(world, boundingBox, aspectSymbolPos.down(2), StatStorerTileEntity.ActiveType.DEATHS, 1); //counts how many deaths there have been(need 10 kills to activate all 5 pistons)
			//fillWithBlocks(world, boundingBox, aspectSymbolPos.getX(), aspectSymbolPos.down(2).getY(), aspectSymbolPos.offset(getCoordBaseMode().rotateYCCW()).getZ(), aspectSymbolPos.getX(), aspectSymbolPos.down(2).getY(), aspectSymbolPos.offset(getCoordBaseMode().rotateYCCW()).getZ());
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.getDefaultState(), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateYCCW()), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateYCCW(), 10));
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.getDefaultState(), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY()), aspectSymbolPos.down(2).offset(getCoordBaseMode().rotateY(), 5));
			//world.setBlockState(aspectSymbolPos.down(2), Blocks.REDSTONE_WIRE.getDefaultState().with(RedstoneWireBlock.NORTH, RedstoneSide.SIDE).with(RedstoneWireBlock.WEST, RedstoneSide.SIDE), Constants.BlockFlags.BLOCK_UPDATE);
			
			fillWithBlocks(world, boundingBox, pieceMinX + 3, pieceMinY + 4, (pieceMinZ + pieceMaxZ) / 2 - 3, pieceMinX + 3, pieceMinY + 8, (pieceMinZ + pieceMaxZ) / 2 + 2, lightBlock, lightBlock, false);
			for(int stairPuzzleIterate = 0; stairPuzzleIterate < 5; stairPuzzleIterate++)
			{
				StructureBlockUtil.placeWirelessRelay(world, boundingBox, aspectSymbolPos.down(3).offset(getCoordBaseMode().rotateYCCW(), stairPuzzleIterate * 2 + 1),
						getActualPos(pieceMinX + 2, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate), false);
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(3).offset(getCoordBaseMode().rotateYCCW(), stairPuzzleIterate * 2 + 2), SummonerTileEntity.SummonType.LICH); //wedged between wireless transmitters
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.down(3).offset(getCoordBaseMode().rotateY(), stairPuzzleIterate + 1), SummonerTileEntity.SummonType.LICH); //on other side of wireless transmitters
				setBlockState(world, Blocks.STICKY_PISTON.getDefaultState().with(PistonBlock.FACING, Direction.EAST), pieceMinX + 3, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
				setBlockState(world, secondaryDecorativeBlock, pieceMinX + 4, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
			}
			
			//area effect block/how to complete puzzle
			BlockPos areaEffectBlockPos = getActualPos(pieceMinX + 2, pieceMinY + 10, (pieceMinZ + pieceMaxZ) / 2);
			world.setBlockState(areaEffectBlockPos.up(), MSBlocks.SOLID_SWITCH.getDefaultState().with(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE); //power for area effect block
			StructureBlockUtil.placeAreaEffectBlock(world, boundingBox, areaEffectBlockPos, MSEffects.CREATIVE_SHOCK.get(), 2,
					getActualPos(pieceMinX - 35, pieceMinY, pieceMinZ - 13),
					getActualPos(pieceMaxX, pieceMaxY + 58, pieceMaxZ + 13));
			
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