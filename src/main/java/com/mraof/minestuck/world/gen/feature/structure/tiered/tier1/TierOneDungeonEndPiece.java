package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.redstone.WirelessRedstoneReceiverBlock;
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
import com.mraof.minestuck.world.gen.feature.StructureBlockRegistryProcessor;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonEndPiece extends ImprovedStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomRoomType;
	private int roomVariable1;
	
	/**/ //these are offset way too much
	private static final int pieceMinX = 0; //was 42
	private static final int pieceMinY = 0;
	private static final int pieceMinZ = 0; //was 20
	private static final int pieceMaxX = 32 - 1; //was 82, then 40
	private static final int pieceMaxY = 32 - 1;
	private static final int pieceMaxZ = 32 - 1; //was 58, then 38
	
	private static final BlockState air = Blocks.AIR.defaultBlockState();
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
	private Template breathSymbolTemplate;
	private Template breathPuzzleDoorTemplate;
	private Template breathFirstSideRoomTemplate;
	private Template breathSecondSideRoomBarrierTemplate;
	private Template breathAreaEffectChamberTemplate;
	private Template bloodSymbolTemplate;
	private Template bloodFirstSideRoomTemplate;
	private Template bloodSecondSideRoomTemplate;
	private Template bloodWallFountainTemplate;
	
	TierOneDungeonStructure.Start.Layout layout;
	
	public TierOneDungeonEndPiece(TemplateManager templates, TierOneDungeonStructure.Start.Layout layout, Direction direction, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_END, 0);
		
		setOrientation(direction);
		setBounds(x, y, z, 32, 32, 32); //was 82, 48, 82
		
		initTemplates(templates);
		this.layout = layout;
	}
	
	public TierOneDungeonEndPiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_END, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomRoomType = nbt.getInt("randomRoomType");
		roomVariable1 = nbt.getInt("roomVariable1");
		
		initTemplates(templates);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("sp1", spawner1); //spawner type room only
		tagCompound.putBoolean("sp2", spawner2); //spawner type room only
		tagCompound.putInt("randomRoomType", randomRoomType);
	}
	
	private void initTemplates(TemplateManager templates)
	{
		breathSymbolTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "aspect/breath_symbol_no_background"));
		breathPuzzleDoorTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/breath_puzzle_door"));
		breathFirstSideRoomTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/breath_first_side_room"));
		breathSecondSideRoomBarrierTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/breath_second_side_room_barrier"));
		breathAreaEffectChamberTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/breath_area_effect_chamber"));
		bloodSymbolTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "aspect/blood_symbol_no_background"));
		bloodFirstSideRoomTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/blood_first_side_room"));
		bloodSecondSideRoomTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/blood_second_side_room"));
		bloodWallFountainTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/blood_wall_fountain")); //getOrCreate was getTemplateDefaulted
	}
	
	@Override
	public boolean postProcess(ISeedReader worldIn, StructureManager structureManagerIn, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos blockPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
		
		primaryBlock = blocks.getBlockState("structure_primary");
		primaryDecorativeBlock = blocks.getBlockState("structure_primary_decorative");
		primaryPillarBlock = blocks.getBlockState("structure_primary_pillar");
		primaryStairBlock = blocks.getBlockState("structure_primary_stairs");
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		lightBlock = blocks.getBlockState("light_block");
		
		SburbConnection sburbConnection = SburbHandler.getConnectionForDimension(worldIn.getLevel().getServer(), worldIn.getLevel().dimension());
		Title worldTitle = sburbConnection == null ? null : PlayerSavedData.getData(sburbConnection.getClientIdentifier(), worldIn.getLevel().getServer()).getTitle();
		worldAspect = worldTitle == null ? null : worldTitle.getHeroAspect(); //aspect of this land's player
		worldClass = worldTitle == null ? null : worldTitle.getHeroClass(); //class of this land's player
		LandInfo landInfo = MSDimensions.getLandInfo(worldIn.getLevel().getServer(), worldIn.getLevel().dimension());
		worldTerrain = landInfo.getLandAspects().getTerrain();
		
		if(!createRan)
		{
			randomRoomType = randomIn.nextInt(2);
			roomVariable1 = randomIn.nextInt(6);
			createRan = true;
		}
		spawner1 = false;
		spawner2 = false;
		
		//generateBox(worldIn, boundingBoxIn,firstRoomMinX, firstRoomMinY, firstRoomMinZ,firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,primaryBlock, air, false);
		
		if(layout != TierOneDungeonStructure.Start.Layout.OPTIONAL)
		{
			generateBox(worldIn, boundingBoxIn, pieceMinX, pieceMaxY - 15, pieceMinZ, pieceMaxX, pieceMaxY, pieceMaxZ, Blocks.GLASS.defaultBlockState(), air, false);
		}
		
		generateAirBox(worldIn, boundingBoxIn, pieceMinX + 12, pieceMinY + 2, pieceMinZ + 12, pieceMaxX - 12, pieceMaxY - 16 + 2, pieceMaxZ - 12);
		BlockPos staircaseMinPos = getActualPos(pieceMinX + 12, pieceMinY + 2, pieceMinZ + 12);
		BlockPos staircaseMaxPos = getActualPos(pieceMaxX - 12, pieceMaxY - 16 + 2, pieceMaxZ - 12);
		StructureBlockUtil.placeSpiralStaircase(worldIn, boundingBoxIn, staircaseMinPos, staircaseMaxPos, secondaryBlock);
		
		//TODO use the saved layout variable to determine where the puzzle and end rooms should go within the 3x3 grid of 32x32 modular components built above this structure. This should not jut out beyond the grid
		//linear: end piece entry in bottom right/puzzle in bottom middle/end in bottom left
		//branched: end piece entry in bottom middle/puzzle in true middle/end in top middle
		//optional: end piece entry in true middle/puzzle(BENT NORTH) in middle right/end in top right
		//buildAspectThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		
		return true;
	}
	
	private void buildAspectThemedPuzzle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
		generateBox(world, boundingBox,
				pieceMinX, pieceMinY, pieceMinZ,
				pieceMaxX, pieceMaxY, pieceMaxZ,
				secondaryBlock, secondaryBlock, false); //filling area with bricks to carve out from
		
		generateAirBox(world, boundingBox, pieceMinX - 3, pieceMaxY - 9, pieceMinZ + 16, pieceMinX, pieceMaxY - 5, pieceMaxZ - 16); //entrance
		
		//lighting at entrance
		placeBlock(world, lightBlock, pieceMinX, pieceMaxY - 7, pieceMinZ + 15, boundingBox);
		placeBlock(world, lightBlock, pieceMinX, pieceMaxY - 7, pieceMaxZ - 15, boundingBox);
		
		if(worldAspect == EnumAspect.BREATH) //parkour like frog temple lower room
		{
		
		} else if(worldAspect == EnumAspect.LIFE)
		{
			/**///TODO will be for Hope
			generateAirBox(world, boundingBox,
					pieceMinX + 15, pieceMinY + 16, pieceMinZ + 4,
					pieceMaxX - 9, pieceMaxY - 1, pieceMaxZ - 4); //upper room
			
			
			/**/
			
			/*//TODO will be for Breath
			generateAirBox(world, boundingBox,
					pieceMinX + 1, pieceMinY + 16, pieceMinZ + 4,
					pieceMaxX - 9, pieceMaxY - 1, pieceMaxZ - 4); //upper room
			
			//pathway connecting each pillar, made by filling an area and then carving off everything except one block on the edge as well as excess,
			//excludes pathways for entrance and exit as they are placed further down to prevent override
			generateBox(world, boundingBox,
					pieceMinX + 7, pieceMaxY - 10, pieceMinZ + 10,
					pieceMinX + 7 + 2 * 9, pieceMaxY - 10, pieceMinZ + 10 + 2 * 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			generateAirBox(world, boundingBox,
					pieceMinX + 8, pieceMaxY - 10, pieceMinZ + 11,
					pieceMinX + 6 + 2 * 9, pieceMaxY - 10, pieceMinZ + 9 + 2 * 9);
			generateAirBox(world, boundingBox,
					pieceMinX + 7 + 9, pieceMaxY - 10, pieceMinZ + 10,
					pieceMinX + 7 + 2 * 9, pieceMaxY - 10, pieceMinZ + 10 + 9); //removes section of rectangle that does not immediately connect pillars
			generateBox(world, boundingBox,
					pieceMinX + 7 + 9, pieceMaxY - 10, pieceMinZ + 4,
					pieceMinX + 7 + 9, pieceMaxY - 10, pieceMinZ + 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //pathway connecting to side room
			
			//first row
			generateBox(world, boundingBox,
					pieceMinX + 6, pieceMinY + 16, pieceMinZ + 9,
					pieceMinX + 8, pieceMaxY - 1, pieceMinZ + 11,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 9, pieceMaxY - 10, pieceMinZ + 12,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			generateBox(world, boundingBox,
					pieceMinX + 6, pieceMinY + 16, pieceMinZ + 9 + 9,
					pieceMinX + 8, pieceMaxY - 1, pieceMinZ + 11 + 9,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5, pieceMaxY - 10, pieceMinZ + 8 + 9,
					pieceMinX + 9, pieceMaxY - 10, pieceMinZ + 12 + 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			StructureBlockUtil.placeSpawner(getActualPos(pieceMinX + 7,pieceMinY + 16,pieceMinZ + 10 + 9), world, boundingBox, MSEntityTypes.IMP);
			generateBox(world, boundingBox,
					pieceMinX + 6, pieceMinY + 16, pieceMinZ + 9 + 2 * 9,
					pieceMinX + 8, pieceMaxY - 1, pieceMinZ + 11 + 2 * 9,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5, pieceMaxY - 10, pieceMinZ + 8 + 2 * 9,
					pieceMinX + 9, pieceMaxY - 10, pieceMinZ + 12 + 2 * 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			
			//second row
			generateBox(world, boundingBox,
					pieceMinX + 6 + 9, pieceMinY + 16, pieceMinZ + 9,
					pieceMinX + 8 + 9, pieceMaxY - 1, pieceMinZ + 11,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5 + 9, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 9 + 9, pieceMaxY - 10, pieceMinZ + 12,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			generateBox(world, boundingBox,
					pieceMinX + 6 + 9, pieceMinY + 16, pieceMinZ + 9 + 2 * 9,
					pieceMinX + 8 + 9, pieceMaxY - 1, pieceMinZ + 11 + 2 * 9,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5 + 9, pieceMaxY - 10, pieceMinZ + 8 + 2 * 9,
					pieceMinX + 9 + 9, pieceMaxY - 10, pieceMinZ + 12 + 2 * 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			StructureBlockUtil.placeSpawner(getActualPos(pieceMinX + 7 + 9,pieceMinY + 16,pieceMinZ + 10 + 2 * 9), world, boundingBox, MSEntityTypes.IMP);
			
			//third row
			generateBox(world, boundingBox,
					pieceMinX + 6 + 9 * 2, pieceMinY + 16, pieceMinZ + 9 + 9,
					pieceMinX + 8 + 9 * 2, pieceMaxY - 1, pieceMinZ + 11 + 9,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5 + 9 * 2, pieceMaxY - 10, pieceMinZ + 8 + 9,
					pieceMinX + 9 + 9 * 2, pieceMaxY - 10, pieceMinZ + 12 + 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			StructureBlockUtil.placeSpawner(getActualPos(pieceMinX + 7 + 9 * 2,pieceMinY + 16,pieceMinZ + 10 + 9), world, boundingBox, MSEntityTypes.IMP);
			generateBox(world, boundingBox,
					pieceMinX + 6 + 9 * 2, pieceMinY + 16, pieceMinZ + 9 + 2 * 9,
					pieceMinX + 8 + 9 * 2, pieceMaxY - 1, pieceMinZ + 11 + 2 * 9,
					MSBlocks.PIPE.defaultBlockState(), MSBlocks.PIPE.defaultBlockState(), false);
			generateBox(world, boundingBox,
					pieceMinX + 5 + 9 * 2, pieceMaxY - 10, pieceMinZ + 8 + 2 * 9,
					pieceMinX + 9 + 9 * 2, pieceMaxY - 10, pieceMinZ + 12 + 2 * 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
			
			StructureBlockUtil.fillWithBlocksReplaceState(world, boundingBox,
					getActualPos(pieceMinX + 4, pieceMaxY - 9, pieceMinZ + 4),
					getActualPos(pieceMaxX - 4, pieceMaxY - 9, pieceMaxZ - 4),
					lightBlock, MSBlocks.PIPE.defaultBlockState()); //above platform lighting
			StructureBlockUtil.fillWithBlocksReplaceState(world, boundingBox,
					getActualPos(pieceMinX + 4, pieceMaxY - 11, pieceMinZ + 4),
					getActualPos(pieceMaxX - 4, pieceMaxY - 11, pieceMaxZ - 4),
					lightBlock, MSBlocks.PIPE.defaultBlockState()); //below platform lighting
			generateBox(world, boundingBox,
					pieceMinX + 1, pieceMinY + 17, pieceMinZ + 18,
					pieceMinX + 1, pieceMaxY - 9, pieceMinZ + 20,
					Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.EAST), Blocks.LADDER.defaultBlockState(), false); //ladder
			
			//first side room, opens path for second side room
			StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMinX + 7 + 9, pieceMaxY - 11, pieceMaxZ - 4), breathFirstSideRoomTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			
			//second side room, ogres separate player from timed switch
			generateBox(world, boundingBox,
					pieceMinX + 12, pieceMaxY - 3, pieceMinZ - 13,
					pieceMinX + 20, pieceMaxY - 3, pieceMinZ + 3,
					lightBlock, air, false);
			generateBox(world, boundingBox,
					pieceMinX + 12, pieceMaxY - 10, pieceMinZ - 13,
					pieceMinX + 20, pieceMaxY - 2, pieceMinZ + 3,
					secondaryBlock, air, false);
			generateAirBox(world, boundingBox,
					pieceMinX + 14, pieceMaxY - 9, pieceMinZ + 3,
					pieceMinX + 18, pieceMaxY - 6, pieceMinZ + 3); //room entrance
			StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMinX + 7 + 9, pieceMaxY - 11, pieceMinZ + 1), breathSecondSideRoomBarrierTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getCounterClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE)); //platform generators block path until first room is complete
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, getActualPos(pieceMinX + 7 + 9, pieceMaxY - 4, pieceMaxZ - 10), getActualPos(pieceMinX + 7 + 9, pieceMaxY - 11, pieceMinZ), false); //wireless relay from first room piece to second
			StructureBlockUtil.placeRemoteObserver(world, boundingBox, getActualPos(pieceMinX + 16, pieceMaxY - 11, pieceMinZ - 11), RemoteObserverTileEntity.ActiveType.IS_ENTITY_PRESENT); //checks for presence of player
			StructureBlockUtil.placeSummoner(world, boundingBox, getActualPos(pieceMinX + 15, pieceMaxY - 11, pieceMinZ - 11), MSEntityTypes.OGRE);
			StructureBlockUtil.placeSummoner(world, boundingBox, getActualPos(pieceMinX + 17, pieceMaxY - 11, pieceMinZ - 11), MSEntityTypes.OGRE);
			
			StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMaxX - 7, pieceMaxY - 11, pieceMinZ + 19), breathPuzzleDoorTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getCounterClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, getActualPos(pieceMaxX - 5, pieceMaxY - 10, pieceMinZ + 16), getActualPos(pieceMaxX - 7, pieceMaxY - 3, pieceMinZ + 16), true);
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, getActualPos(pieceMinX + 16, pieceMaxY - 8, pieceMinZ - 14), getActualPos(pieceMaxX - 6, pieceMaxY - 10, pieceMinZ + 16), true);
			world.setBlock(getActualPos(pieceMinX + 16, pieceMaxY - 8, pieceMinZ - 13), MSBlocks.ONE_SECOND_INTERVAL_TIMED_SOLID_SWITCH.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE); //power for wireless relay
			
			generateBox(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 10, pieceMinZ + 10 + 9,
					pieceMinX + 4, pieceMaxY - 10, pieceMinZ + 10 + 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //pathway connecting to entrance
			generateBox(world, boundingBox,
					pieceMinX + 10 + 9 * 2, pieceMaxY - 10, pieceMinZ + 10 + 9,
					pieceMinX + 13 + 9 * 2, pieceMaxY - 10, pieceMinZ + 10 + 9,
					primaryDecorativeBlock, primaryDecorativeBlock, false); //pathway connecting to exit
			
			//staircase down
			generateAirBox(world, boundingBox, pieceMaxX - 4, pieceMaxY - 8, pieceMinZ + 4, pieceMaxX - 3, pieceMaxY - 6, pieceMaxZ - 9);
			BlockPos stairsStart = getActualPos(pieceMaxX - 1, pieceMinY + 2, pieceMinZ + 10);
			generateAirBox(world, boundingBox, pieceMaxX - 2, pieceMinY + 2, pieceMinZ + 4, pieceMaxX - 1, pieceMaxY - 6, pieceMaxZ - 9);
			StructureBlockUtil.createStairs(world, boundingBox, primaryBlock, primaryStairBlock.setValue(StairsBlock.FACING, getOrientation()), stairsStart, 20, 2, getOrientation(), false);
			generateAirBox(world, boundingBox, pieceMaxX - 8, pieceMinY + 2, pieceMinZ + 6, pieceMaxX - 3, pieceMinY + 4, pieceMinZ + 7);
			
			//lower room
			generateAirBox(world, boundingBox,
					pieceMinX + 1, pieceMinY + 2, pieceMinZ + 4,
					pieceMaxX - 9, pieceMinY + 5, pieceMaxZ - 4);
			generateBox(world, boundingBox,
					pieceMinX + 1, pieceMinY + 1, pieceMinZ + 4,
					pieceMaxX - 9, pieceMinY + 1, pieceMaxZ - 4,
					lightBlock, lightBlock, false);
			generateBox(world, boundingBox,
					pieceMinX + 2, pieceMinY + 1, pieceMinZ + 5,
					pieceMaxX - 10, pieceMinY + 1, pieceMaxZ - 5,
					secondaryBlock, secondaryBlock, false);
			
			BlockPos aspectSymbolPos = getActualPos(pieceMinX + 16, pieceMinY + 2, pieceMinZ + 19);
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos.below(), breathSymbolTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getOpposite())).addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(chunkGeneratorIn))));
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 7), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8).offset(getOrientation()), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8).offset(getOrientation().getOpposite()), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 9), MSEntityTypes.LICH);
			StructureBlockUtil.placeRemoteObserver(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8), RemoteObserverTileEntity.ActiveType.IS_ENTITY_PRESENT); //checks for presence of player
			StructureBlockUtil.placeStatStorer(world, boundingBox, aspectSymbolPos.below(2), StatStorerTileEntity.ActiveType.DEATHS, 1); //counts how many deaths there have been(need 10 kills to activate all 5 pistons)
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.defaultBlockState(), aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise()), aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), 10));
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.defaultBlockState(), aspectSymbolPos.below(2).offset(getOrientation().getClockWise()), aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 5));
			for(int summonerIterate = 0; summonerIterate < 5; summonerIterate++)
			{
				//StructureBlockUtil.placeWirelessRelay(world, boundingBox, aspectSymbolPos.below(3).offset(getOrientation().getCounterClockWise(), summonerIterate * 2 + 1), getActualPos(pieceMinX + 2, pieceMinY + 4 + summonerIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + summonerIterate), false);
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), summonerIterate * 2 + 2).offset(getOrientation(), 2), MSEntityTypes.LICH); //wedged between wireless transmitters
				world.setBlock(aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), summonerIterate * 2 + 2).offset(getOrientation(), 1), Blocks.REPEATER.defaultBlockState().setValue(RedstoneDiodeBlock.HORIZONTAL_FACING, getOrientation().getOpposite()), Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), summonerIterate * 2 + 2).offset(getOrientation().getOpposite(), 2), MSEntityTypes.LICH); //wedged between wireless transmitters
				world.setBlock(aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), summonerIterate * 2 + 2).offset(getOrientation().getOpposite(), 1), Blocks.REPEATER.defaultBlockState().setValue(RedstoneDiodeBlock.HORIZONTAL_FACING, getOrientation()), Constants.BlockFlags.BLOCK_UPDATE);
			}
			
			//area effect block/how to complete puzzle
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos, breathAreaEffectChamberTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			StructureBlockUtil.placeAreaEffectBlock(world, boundingBox, aspectSymbolPos, MSEffects.CREATIVE_SHOCK.get(), 2,
					getActualPos(pieceMinX - 35, pieceMinY, pieceMinZ - 13),
					getActualPos(pieceMaxX, pieceMaxY + 58, pieceMaxZ + 13)); //TODO it is possible to bomb the structure from this distance or mine into the treasure room (-x direction then?)
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, aspectSymbolPos.below(3).offset(getOrientation().getCounterClockWise(), 4 * 2 + 1), aspectSymbolPos.up(5), false);
			world.setBlock(aspectSymbolPos.up(5), MSBlocks.WIRELESS_REDSTONE_RECEIVER.defaultBlockState().setValue(WirelessRedstoneReceiverBlock.POWER, 15), Constants.BlockFlags.UPDATE_NEIGHBORS);
			/**/
			
			/*
			//TODO will be for Space
			generateAirBox(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 12, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 1, firstRoomMaxZ - 1); //ceiling down to main area
			generateAirBox(world, boundingBox,
					firstRoomMinX + 15, firstRoomMaxY - 25, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 12, firstRoomMaxZ - 1); //pit area
			
			generateBox(world, boundingBox,
					firstRoomMinX + 10, firstRoomMaxY - 13, (firstRoomMinZ + firstRoomMaxZ) / 2,
					firstRoomMinX + 14, firstRoomMaxY - 13, (firstRoomMinZ + firstRoomMaxZ) / 2,
					MSBlocks.TRAJECTORY_BLOCK.defaultBlockState()
				.setValue(TrajectoryBlock.FACING, getOrientation().getCounterClockWise()), MSBlocks.TRAJECTORY_BLOCK.defaultBlockState()
				, false); //sideways facing trajectory blocks
			 */
			
			/*generateBox(world, boundingBox,
					firstRoomMinX + 1, firstRoomMaxY - 4, firstRoomMinZ + 1,
					firstRoomMaxX - 1, firstRoomMaxY - 4, firstRoomMaxZ - 1,
					primaryDecorativeBlock, primaryDecorativeBlock, false);
			generateAirBox(world, boundingBox,
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
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos, getRotation(), getOrientation(), rand, new ResourceLocation(Minestuck.MOD_ID, "blood_symbol_no_background"));
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos.below(3), getRotation(), getOrientation(), rand, new ResourceLocation(Minestuck.MOD_ID, "breath_symbol_no_background"));
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos.below(9), getRotation(), getOrientation(), rand, Feature.ICE_SPIKE.getRegistryName());
			/**/
			
			//TODO will be for Blood
			/*
			
			//roomVariable1 = rand.nextInt(6); //blood diving to flick switch
			
			generateAirBox(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 5, pieceMinZ + 1,
					pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1); //ceiling down to raised areas
			generateAirBox(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 9, pieceMinZ + 4,
					pieceMaxX - 1, pieceMaxY - 5, pieceMaxZ - 4); //makes walls of raised areas
			
			generateBox(world, boundingBox,
					pieceMinX + 1, pieceMaxY - 10, pieceMinZ + 4,
					pieceMaxX - 4, pieceMaxY - 10, pieceMaxZ - 4,
					MSBlocks.BLOOD.defaultBlockState()
				, MSBlocks.BLOOD.defaultBlockState()
				, false);
			 */
			
			/*generateBox(world, boundingBox,
					pieceMinX + 8, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 8, pieceMaxY - 6, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //first barrier
			
			generateBox(world, boundingBox,
					pieceMinX + 16, pieceMaxY - 10, pieceMinZ + 4,
					pieceMinX + 16, pieceMaxY - 7, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //second barrier, ends close to edge wall to form barrier with wireless piston setup below!
			generateBox(world, boundingBox,
					pieceMinX + 16, pieceMaxY - 6, pieceMinZ + 4,
					pieceMinX + 16, pieceMaxY - 6, pieceMaxZ - 4,
					secondaryBlock, secondaryBlock, false); //line of secondary blocks that are used as a path for the player to travel across once passing theses barriers from the floor
			
			generateBox(world, boundingBox,
					pieceMinX + 24, pieceMaxY - 10, pieceMinZ + 8,
					pieceMinX + 24, pieceMaxY - 6, pieceMaxZ - 4,
					primaryBlock, primaryBlock, false); //third barrier
			
			generateBox(world, boundingBox,
					pieceMinX + 32, pieceMaxY - 10, pieceMinZ + 4,
					pieceMinX + 32, pieceMaxY - 6, pieceMaxZ - 8,
					primaryBlock, primaryBlock, false); //fourth barrier
			
			generateBox(world, boundingBox,
					pieceMinX + 37, pieceMaxY - 10, pieceMinZ + 1,
					pieceMinX + 37, pieceMaxY - 1, pieceMaxZ - 1,
					secondaryBlock, secondaryBlock, false); //false wall
			
			generateBox(world, boundingBox,
					pieceMinX + 34, pieceMaxY - 11, pieceMinZ + 8,
					pieceMinX + 35, pieceMaxY - 11, pieceMaxZ - 8,
					lightBlock, lightBlock, false); //lighting between false wall and fourth barrier
			
			//lighting between barriers
			for(int lightIterate = 0; lightIterate < 4; lightIterate++)
			{
				generateBox(world, boundingBox,
						pieceMinX + 4 + (lightIterate * 8), pieceMaxY - 11, pieceMinZ + 8,
						pieceMinX + 4 + (lightIterate * 8), pieceMaxY - 11, pieceMaxZ - 8,
						lightBlock, lightBlock, false);
			}
			
			BlockPos spawnerPos;
			for(int xIterate = 0; xIterate < 4; xIterate++) //TODO figure out how to change potion effects of spawned entities(give them speed) and increase range at which they spawn
			{
				spawnerPos = getActualPos(pieceMinX + 5 + xIterate * 10, pieceMaxY - 5, pieceMinZ + 2);
				world.setBlock(spawnerPos.below(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
				spawnerPos = getActualPos(pieceMinX + 5 + xIterate * 10, pieceMaxY - 5, pieceMaxZ - 2);
				world.setBlock(spawnerPos.below(), lightBlock, Constants.BlockFlags.BLOCK_UPDATE);
				StructureBlockUtil.placeSpawner(spawnerPos, world, boundingBox, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			
			//first side room
			StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMinX + 12 , pieceMaxY - 20, pieceMaxZ + 4), bloodFirstSideRoomTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getCounterClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			//generateBox(world, boundingBox, pieceMinX + 5, pieceMaxY - 20, pieceMaxZ, pieceMinX + 20, pieceMaxY - 3, pieceMaxZ + 12, secondaryBlock, air, false);
			//generateBox(world, boundingBox, pieceMinX + 6, pieceMaxY - 19, pieceMaxZ + 1, pieceMinX + 19, pieceMaxY - 10, pieceMaxZ + 11, MSBlocks.BLOOD.defaultBlockState()
			//, MSBlocks.BLOOD.defaultBlockState()
			//, false); //first side room liquid
			//generateBox(world, boundingBox, pieceMinX + 6, pieceMaxY - 19, pieceMaxZ + 1, pieceMinX + 20, pieceMaxY - 10, pieceMaxZ + 1, primaryBlock, primaryBlock, false); //ledge into liquid
			//generateAirBox(world, boundingBox, pieceMinX + 9, pieceMaxY - 9, pieceMaxZ - 3, pieceMinX + 10, pieceMaxY - 7, pieceMaxZ); //first side room entrance
			//setBlock
			//(world, lightBlock, pieceMinX + 5 , pieceMaxY - 7, pieceMaxZ + 6, boundingBox); //lighting
			//setBlock
			//(world, lightBlock, pieceMinX + 20 , pieceMaxY - 7, pieceMaxZ + 6, boundingBox); //lighting
			
			//blood diving challenge associated with first side room
			BlockPos transmitterPos = getActualPos(pieceMinX + 12 + (roomVariable1 - 3), pieceMaxY - 21, pieceMaxZ + 6);
			BlockPos receiverPos = getActualPos(pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 1);
			StructureBlockUtil.placeWirelessRelay(world, boundingBox, transmitterPos, receiverPos, true);
			setBlock(world, MSBlocks.SOLID_SWITCH.defaultBlockState().setValue(SolidSwitchBlock.POWERED, true), pieceMinX + 12 + (roomVariable1 - 3), pieceMaxY - 20, pieceMaxZ + 6, boundingBox); //power for transmitter
			setBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 1, boundingBox); //wire above receiver, both power pistons
			generateAirBox(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 3, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 3); //hole for piston
			generateBox(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 2, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 2, Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBlock.FACING, Direction.NORTH), Blocks.STICKY_PISTON.defaultBlockState(), false);
			generateBox(world, boundingBox, pieceMinX + 16, pieceMaxY - 9, pieceMaxZ - 4, pieceMinX + 16, pieceMaxY - 8, pieceMaxZ - 4, MSBlocks.DUNGEON_DOOR.defaultBlockState(), MSBlocks.DUNGEON_DOOR.defaultBlockState(), false);
			
			//second side room
			StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMaxX - 7, pieceMaxY - 10, pieceMinZ - 4), bloodSecondSideRoomTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getCounterClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			//generateBox(world, boundingBox, pieceMaxX - 10, pieceMaxY - 10, pieceMinZ - 10, pieceMaxX - 5, pieceMaxY, pieceMinZ, secondaryBlock, air, false);
			//generateAirBox(world, boundingBox, pieceMaxX - 7, pieceMaxY - 9, pieceMinZ - 3, pieceMaxX - 6, pieceMaxY - 7, pieceMinZ + 3); //second side room entrance
			//generateAirBox(world, boundingBox, pieceMaxX - 7, pieceMaxY - 5, pieceMinZ - 3, pieceMaxX - 6, pieceMaxY - 3, pieceMinZ + 3); //second side room exit
			//StructureBlockUtil.placeSpiralStaircase(world, boundingBox, getActualPos(pieceMaxX - 9, pieceMaxY - 9, pieceMinZ - 9), getActualPos(pieceMaxX - 6, pieceMaxY - 6, pieceMinZ + 1), primaryDecorativeBlock);
			//generateBox(world, boundingBox, pieceMaxX - 9, pieceMaxY - 6, pieceMinZ - 8, pieceMaxX - 6, pieceMaxY - 6, pieceMinZ + 1, secondaryBlock, air, false); //second level floor
			//generateBox(world, boundingBox, pieceMaxX - 9, pieceMaxY - 6, pieceMinZ - 10, pieceMaxX - 6, pieceMaxY - 6, pieceMinZ - 10, lightBlock, lightBlock, false); //second level floor
			//StructureBlockUtil.placeLootBlock(getActualPos(pieceMaxX - 6, pieceMaxY - 9, pieceMinZ - 5), world, boundingBox, MSBlocks.LOOT_CHEST.defaultBlockState()
			//.setValue(BlockStateProperties.HORIZONTAL_FACING, getOrientation().getClockWise()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
			
			generateBox(world, boundingBox,
					pieceMinX, pieceMinY + 10, pieceMinZ + 2,
					pieceMaxX, pieceMinY + 10, pieceMaxZ - 2,
					lightBlock, lightBlock, false); //light around wall in lower chamber, placed here so other functions can carve it
			
			//stairs leading from puzzle room to lower level
			generateAirBox(world, boundingBox, pieceMaxX - 3, pieceMaxY - 5, pieceMaxZ - 3, pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1); //entrance to stairs
			BlockPos stairsStart = getActualPos(pieceMaxX - 1, pieceMaxY - 25, pieceMinZ + 17);
			generateAirBox(world, boundingBox, pieceMaxX - 2, pieceMaxY - 25, pieceMinZ + 4, pieceMaxX - 1, pieceMaxY - 1, pieceMaxZ - 1);
			StructureBlockUtil.createStairs(world, boundingBox, primaryBlock, primaryStairBlock.setValue(StairsBlock.FACING, getOrientation()), stairsStart, 20, 2, getOrientation(), false);
			generateBox(world, boundingBox,
					pieceMaxX - 2, pieceMaxY - 25, pieceMaxZ - 1,
					pieceMaxX - 1, pieceMaxY - 6, pieceMaxZ - 1,
					primaryBlock, primaryBlock, false);
			generateBox(world, boundingBox,
					pieceMaxX - 1, pieceMaxY - 9, pieceMinZ + 12,
					pieceMaxX - 1, pieceMaxY - 8, pieceMinZ + 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlock(world, lightBlock, pieceMaxX - 1, pieceMaxY - 7, pieceMinZ + 12, boundingBox);
			setBlock(world, primaryStairBlock.setValue(StairsBlock.FACING, Direction.EAST).setValue(StairsBlock.HALF, Half.TOP), pieceMaxX - 1, pieceMaxY - 10, pieceMinZ + 12, boundingBox);
			generateBox(world, boundingBox,
					pieceMaxX - 1, pieceMaxY - 9, pieceMaxZ - 12,
					pieceMaxX - 1, pieceMaxY - 8, pieceMaxZ - 12,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlock(world, lightBlock, pieceMaxX - 1, pieceMaxY - 7, pieceMaxZ - 12, boundingBox);
			setBlock(world, primaryStairBlock.setValue(StairsBlock.FACING, Direction.EAST).setValue(StairsBlock.HALF, Half.TOP), pieceMaxX - 1, pieceMaxY - 10, pieceMaxZ - 12, boundingBox);
			
			//edges of lower level
			generateAirBox(world, boundingBox,
					pieceMinX + 1, pieceMinY + 10, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 14, pieceMaxZ - 3); //lower section ceiling
			generateAirBox(world, boundingBox,
					pieceMinX + 5, pieceMinY + 5, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 9, pieceMaxZ - 3); //lower section chamber(exception of blood fluid)
			generateBox(world, boundingBox,
					pieceMinX + 5, pieceMinY + 1, pieceMinZ + 3,
					pieceMaxX - 3, pieceMinY + 4, pieceMaxZ - 3,
					MSBlocks.BLOOD.defaultBlockState(), MSBlocks.BLOOD.defaultBlockState(), false); //liquid of floor
			//generateBox(world, boundingBox, pieceMinX + 9, pieceMinY + 11, pieceMinZ + 1, pieceMaxX - 9, pieceMinY + 11, pieceMinZ + 2, MSBlocks.BLOOD.defaultBlockState()
			//, MSBlocks.BLOOD.defaultBlockState()
			//, false); //side waterfall
			//generateBox(world, boundingBox, pieceMinX + 9, pieceMinY + 11, pieceMaxZ - 2, pieceMaxX - 9, pieceMinY + 11, pieceMaxZ - 1, MSBlocks.BLOOD.defaultBlockState()
			//, MSBlocks.BLOOD.defaultBlockState()
			//, false); //side waterfall
			
			//lighting
			generateBox(world, boundingBox,
					pieceMinX + 3, pieceMinY + 10, pieceMinZ + 8,
					pieceMinX + 3, pieceMinY + 11, pieceMinZ + 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlock(world, lightBlock, pieceMinX + 3, pieceMinY + 12, pieceMinZ + 8, boundingBox);
			generateBox(world, boundingBox,
					pieceMinX + 3, pieceMinY + 10, pieceMaxZ - 8,
					pieceMinX + 3, pieceMinY + 11, pieceMaxZ - 8,
					primaryPillarBlock, primaryPillarBlock, false);
			setBlock(world, lightBlock, pieceMinX + 3, pieceMinY + 12, pieceMaxZ - 8, boundingBox);
			
			//waterfall light
			BlockPos side1SwitchLampPos = getActualPos((pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMinZ + 2);
			StructureBlockUtil.placeCenteredTemplate(world, side1SwitchLampPos.below(9), bloodWallFountainTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getCounterClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			//world.setBlock
			//(side1SwitchLampPos.offset(getOrientation(), 4).below(8).offset(getOrientation().getClockWise(), 2), Blocks.STICKY_PISTON.defaultBlockState()
			//.setValue(PistonBlock.FACING, Direction.WEST), Constants.BlockFlags.BLOCK_UPDATE);
			//world.setBlock
			//(side1SwitchLampPos.offset(getOrientation(), 3).below(8), MSBlocks.BLOOD.defaultBlockState()
			//, Constants.BlockFlags.BLOCK_UPDATE);
			//StructureBlockUtil.placeWirelessRelay(world, boundingBox, side1SwitchLampPos.offset(getOrientation().getOpposite()), side1SwitchLampPos.offset(getOrientation(), 4).below(9).offset(getOrientation().getClockWise(), 2), true);
			//StructureBlockUtil.placeLootBlock(side1SwitchLampPos.offset(getOrientation(), 5).below(8), world, boundingBox, MSBlocks.LOOT_CHEST.defaultBlockState()
			//.setValue(BlockStateProperties.HORIZONTAL_FACING, getOrientation().getClockWise()), MSLootTables.TIER_ONE_MEDIUM_CHEST);
			//world.setBlock
			//(side1SwitchLampPos.offset(getOrientation(), 3).below(7), MSBlocks.BLOOD.defaultBlockState()
			//, Constants.BlockFlags.BLOCK_UPDATE);
			BlockPos side2SwitchLampPos = getActualPos((pieceMinX + pieceMaxX) / 2, pieceMinY + 10, pieceMaxZ - 2);
			StructureBlockUtil.placeCenteredTemplate(world, side2SwitchLampPos.below(9), bloodWallFountainTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getClockWise())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			//StructureBlockUtil.placeWirelessRelay(world, boundingBox, side2SwitchLampPos.offset(getOrientation()), side2SwitchLampPos.offset(getOrientation(), 2).below(5), true);
			//world.setBlock
			//(side2SwitchLampPos, MSBlocks.SOLID_SWITCH.defaultBlockState()
			//.setValue(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE);
			//world.setBlock
			//(side2SwitchLampPos.offset(getOrientation().getOpposite()), Blocks.REDSTONE_LAMP.defaultBlockState()
			//.setValue(RedstoneLampBlock.LIT, true), Constants.BlockFlags.BLOCK_UPDATE);
			
			//aspect symbol platform
			BlockPos aspectSymbolPos = getActualPos(pieceMaxX - 19, pieceMinY + 4, pieceMinZ + 19); //TODO get this pos to be dependent on rotation or adjust piece size by 1
			//BlockPos aspectSymbolPos = getActualPos((pieceMinX + 5 + pieceMaxX - 3) / 2, pieceMinY + 4, (pieceMinZ + pieceMaxZ) / 2); //middle of lower room on top of blood
			StructureBlockUtil.createCylinder(world, boundingBox, secondaryBlock, aspectSymbolPos.below(3), 13, 3);
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.below(1), 13, 1);
			StructureBlockUtil.createCylinder(world, boundingBox, primaryBlock, aspectSymbolPos, 13, 1);
			StructureBlockUtil.placeCenteredTemplate(world, aspectSymbolPos.offset(getOrientation().getCounterClockWise(), 2), bloodSymbolTemplate, new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation().getOpposite())).addProcessor(StructureBlockRegistryProcessor.INSTANCE));
			//StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, getActualPos((firstRoomMinX + firstRoomMaxX) / 2 - 10, firstRoomMinY + 14, (firstRoomMinZ + firstRoomMaxZ) / 2), 6, 1); //ceiling light
			StructureBlockUtil.createCylinder(world, boundingBox, lightBlock, aspectSymbolPos.up(10), 6, 1); //ceiling light
			
			//redstone components for lich fight and piston stairway unlock, inside aspect platform
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 7), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8).offset(getOrientation()), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8).offset(getOrientation().getOpposite()), MSEntityTypes.LICH);
			StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 9), MSEntityTypes.LICH);
			StructureBlockUtil.placeRemoteObserver(world, boundingBox, aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 8), RemoteObserverTileEntity.ActiveType.IS_PLAYER_PRESENT); //checks for presence of player
			StructureBlockUtil.placeStatStorer(world, boundingBox, aspectSymbolPos.below(2), StatStorerTileEntity.ActiveType.DEATHS, 1); //counts how many deaths there have been(need 10 kills to activate all 5 pistons)
			//generateBox(world, boundingBox, aspectSymbolPos.getX(), aspectSymbolPos.below(2).getY(), aspectSymbolPos.offset(getOrientation().getCounterClockWise()).getZ(), aspectSymbolPos.getX(), aspectSymbolPos.below(2).getY(), aspectSymbolPos.offset(getOrientation().getCounterClockWise()).getZ());
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.defaultBlockState(), aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise()), aspectSymbolPos.below(2).offset(getOrientation().getCounterClockWise(), 10));
			StructureBlockUtil.fillWithBlocksFromPos(world, boundingBox, Blocks.REDSTONE_WIRE.defaultBlockState(), aspectSymbolPos.below(2).offset(getOrientation().getClockWise()), aspectSymbolPos.below(2).offset(getOrientation().getClockWise(), 5));
			//world.setBlock
			//(aspectSymbolPos.below(2), Blocks.REDSTONE_WIRE.defaultBlockState()
			//.setValue(RedstoneWireBlock.NORTH, RedstoneSide.SIDE).setValue(RedstoneWireBlock.WEST, RedstoneSide.SIDE), Constants.BlockFlags.BLOCK_UPDATE);
			
			generateBox(world, boundingBox, pieceMinX + 3, pieceMinY + 4, (pieceMinZ + pieceMaxZ) / 2 - 3, pieceMinX + 3, pieceMinY + 8, (pieceMinZ + pieceMaxZ) / 2 + 2, lightBlock, lightBlock, false);
			for(int stairPuzzleIterate = 0; stairPuzzleIterate < 5; stairPuzzleIterate++)
			{
				StructureBlockUtil.placeWirelessRelay(world, boundingBox, aspectSymbolPos.below(3).offset(getOrientation().getCounterClockWise(), stairPuzzleIterate * 2 + 1),
						getActualPos(pieceMinX + 2, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate), false);
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(3).offset(getOrientation().getCounterClockWise(), stairPuzzleIterate * 2 + 2), MSEntityTypes.LICH); //wedged between wireless transmitters
				StructureBlockUtil.placeSummoner(world, boundingBox, aspectSymbolPos.below(3).offset(getOrientation().getClockWise(), stairPuzzleIterate + 1), MSEntityTypes.LICH); //on other side of wireless transmitters
				setBlock(world, Blocks.STICKY_PISTON.defaultBlockState().setValue(PistonBlock.FACING, Direction.EAST), pieceMinX + 3, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
				setBlock(world, secondaryDecorativeBlock, pieceMinX + 4, pieceMinY + 4 + stairPuzzleIterate, (pieceMinZ + pieceMaxZ) / 2 - 2 + stairPuzzleIterate, boundingBox);
			}
			
			//area effect block/how to complete puzzle
			generateBox(world, boundingBox, pieceMinX + 1, pieceMinY + 10, (pieceMinZ + pieceMaxZ) / 2 + 1, pieceMinX + 1, pieceMinY + 11, (pieceMinZ + pieceMaxZ) / 2 + 1, MSBlocks.COARSE_STONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.NORTH), MSBlocks.COARSE_STONE_STAIRS.defaultBlockState(), false);
			generateBox(world, boundingBox, pieceMinX + 1, pieceMinY + 10, (pieceMinZ + pieceMaxZ) / 2 - 1, pieceMinX + 1, pieceMinY + 11, (pieceMinZ + pieceMaxZ) / 2 - 1, MSBlocks.COARSE_STONE_STAIRS.defaultBlockState().setValue(StairsBlock.FACING, Direction.SOUTH), MSBlocks.COARSE_STONE_STAIRS.defaultBlockState(), false);
			BlockPos areaEffectBlockPos = getActualPos(pieceMinX, pieceMinY + 11, (pieceMinZ + pieceMaxZ) / 2);
			world.setBlock(areaEffectBlockPos.below().offset(getOrientation().getCounterClockWise()), MSBlocks.CHISELED_COARSE_STONE.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			world.setBlock(areaEffectBlockPos.offset(getOrientation().getCounterClockWise()), MSBlocks.SOLID_SWITCH.defaultBlockState().setValue(SolidSwitchBlock.POWERED, true), Constants.BlockFlags.BLOCK_UPDATE); //power for area effect block
			StructureBlockUtil.placeAreaEffectBlock(world, boundingBox, areaEffectBlockPos, MSEffects.CREATIVE_SHOCK.get(), 2,
					getActualPos(pieceMinX - 35, pieceMinY, pieceMinZ - 13),
					getActualPos(pieceMaxX, pieceMaxY + 58, pieceMaxZ + 13));
			
			/**/
			
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