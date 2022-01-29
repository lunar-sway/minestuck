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

import java.util.Random;

public class TierOneDungeonCombatModulePiece extends ImprovedStructurePiece
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
	
	public TierOneDungeonCombatModulePiece(TemplateManager templates, boolean organicDesign, TierOneDungeonStructure.Start.Layout layout, Direction orientation, Direction exitDirection, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_COMBAT_MODULE, 0);
		
		setOrientation(orientation);
		setBounds(x, y, z, 32, 16, 32);
		
		//this.templates = templates;
		this.layout = layout;
		this.organicDesign = organicDesign;
	}
	
	public TierOneDungeonCombatModulePiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_COMBAT_MODULE, nbt);
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
		
		buildGenericCombatRoom(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		
		return true;
	}
	
	private void buildGenericCombatRoom(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
		generateAirBox(world, boundingBox, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMaxY, pieceMaxZ);
		placeBlock(world, lightBlock, pieceMinX, pieceMinY, pieceMinZ, boundingBox);
		placeBlock(world, lightBlock, pieceMaxX, pieceMaxY, pieceMaxZ, boundingBox);
		placeBlock(world, secondaryBlock, pieceMaxX - 5, pieceMaxY - 5, pieceMaxZ - 5, boundingBox);
	}
}