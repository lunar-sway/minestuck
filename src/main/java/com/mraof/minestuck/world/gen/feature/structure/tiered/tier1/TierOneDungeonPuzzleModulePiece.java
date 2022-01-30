package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class TierOneDungeonPuzzleModulePiece extends ImprovedStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomType;
	private int roomVariable1;
	
	/**/ //these are offset way too much
	private static final int pieceMinX = 0;
	private static final int pieceMinY = 0;
	private static final int pieceMinZ = 0;
	private static final int pieceMaxX = 32 - 1;
	private static final int pieceMaxY = 16 - 1;
	private static final int pieceMaxZ = 32 - 1;
	
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
	
	//private final TemplateManager templates;
	TierOneDungeonStructure.Start.Layout layout;
	boolean organicDesign;
	Direction entryDirection;
	
	public TierOneDungeonPuzzleModulePiece(TemplateManager templates, boolean organicDesign, TierOneDungeonStructure.Start.Layout layout, Direction orientation, Direction entryDirection, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_PUZZLE_MODULE, 0);
		
		setOrientation(orientation);
		setBounds(x, y, z, 32, 16, 32);
		
		//this.templates = templates;
		this.layout = layout;
		this.organicDesign = organicDesign;
		this.entryDirection = entryDirection;
	}
	
	public TierOneDungeonPuzzleModulePiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_PUZZLE_MODULE, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomType = nbt.getInt("randomType");
		roomVariable1 = nbt.getInt("roomVariable1");
		
		//this.templates = templates;
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("sp1", spawner1); //spawner type room only
		tagCompound.putBoolean("sp2", spawner2); //spawner type room only
		tagCompound.putInt("randomType", randomType);
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
		
		if(!createRan)
		{
			randomType = randomIn.nextInt(9);
			roomVariable1 = randomIn.nextInt(6);
			createRan = true;
		}
		
		generateBox(worldIn, boundingBoxIn, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMaxY, pieceMaxZ, Blocks.YELLOW_STAINED_GLASS.defaultBlockState(), air, false);
		generateBox(worldIn, boundingBoxIn, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMinY, pieceMaxZ, lightBlock, lightBlock, false);
		//generateAirBox(worldIn, boundingBoxIn, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMaxY, pieceMaxZ);
		placeBlock(worldIn, lightBlock, pieceMinX, pieceMinY, pieceMinZ, boundingBoxIn);
		placeBlock(worldIn, lightBlock, pieceMaxX, pieceMaxY, pieceMaxZ, boundingBoxIn);
		placeBlock(worldIn, primaryBlock, pieceMaxX - 5, pieceMaxY - 5, pieceMaxZ - 5, boundingBoxIn);
		
		if(entryDirection != null && entryDirection != Direction.UP && entryDirection != Direction.DOWN)
		{
			if(entryDirection == Direction.NORTH)
				generateAirBox(worldIn, boundingBoxIn, pieceMinX + 15, pieceMaxY - 10, pieceMaxZ, pieceMaxX - 15, pieceMaxY - 6, pieceMaxZ);
			else if(entryDirection == Direction.SOUTH)
				generateAirBox(worldIn, boundingBoxIn, pieceMinX + 15, pieceMaxY - 10, pieceMinZ, pieceMaxX - 15, pieceMaxY - 6, pieceMinZ);
			else if(entryDirection == Direction.EAST)
				generateAirBox(worldIn, boundingBoxIn, pieceMinX, pieceMaxY - 10, pieceMinZ + 15, pieceMinX, pieceMaxY - 6, pieceMaxZ - 15);
			else if(entryDirection == Direction.WEST)
				generateAirBox(worldIn, boundingBoxIn, pieceMaxX, pieceMaxY - 10, pieceMinZ + 15, pieceMaxX, pieceMaxY - 6, pieceMaxZ - 15);
			
			worldIn.setBlock(getActualPos(pieceMaxX / 2, pieceMinY + 1, pieceMaxZ / 2).relative(entryDirection, 5), secondaryDecorativeBlock, Constants.BlockFlags.BLOCK_UPDATE);
		}
		
		if(randomType == 9) //1 in 10 chance of opposite aspect style puzzle
			buildCounterpartAspectThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		else if(randomType > 4) //4 in 10 chance of aspect style puzzle
			buildAspectThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		/*
		else if(randomType > 2)
			buildClassThemedPuzzle(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		 */
		else // 5 in 10 chance of generic puzzle(assuming class themed puzzle is not included)
			buildGenericPuzzle(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		
		return true;
	}
	
	private void buildAspectThemedPuzzle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
		//TODO for each nbt template, have Structure Blocks in the component that makes the floors/walls/ceilings(the blocks may be spread between the puzzle template and the module terrain template). From there, apply the relevant aesthetic feature to the area.
		// That is to say, if a structure block is created on the ceiling of a module between the puzzle and room templates for a rock and bunnies land, if the ceilings are designated to be shaped by the aesthetics of the terrain then the structure block would be replaced with a stalactite
		//StructureBlockUtil.placeCenteredTemplate(world, getActualPos(pieceMinX + 16, pieceMinY + 8, pieceMinZ + 16), templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "aspect/breath_symbol_no_background")), new PlacementSettings().setBoundingBox(boundingBox).setRotation(MSRotationUtil.fromDirection(getOrientation())).addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(chunkGeneratorIn))));
	}
	
	private void buildCounterpartAspectThemedPuzzle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
	
	}
	
	private void buildClassThemedPuzzle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn) //TODO add class puzzles, as well as lunar sway puzzles if that can be checked in the future
	{
	
	}
	
	private void buildGenericPuzzle(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
	
	}
}