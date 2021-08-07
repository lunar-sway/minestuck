package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSDirectionalBlock;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class TierOneDungeonFirstRoomPiece extends ScatteredStructurePiece
{
	private boolean createRan = false; //boolean check to prevent certain aspects from generating several times over or changing
	private boolean spawner1, spawner2;
	private int randomRoomType;
	
	private final int firstRoomMinX = 0;
	private final int firstRoomMinY = 0;
	private final int firstRoomMinZ = 0;
	private final int firstRoomMaxX = 40;
	private final int firstRoomMaxY = 30;
	private final int firstRoomMaxZ = 40;
	private final BlockState air = Blocks.AIR.getDefaultState();
	//private BlockState ground;
	private BlockState primaryBlock;
	private BlockState primaryCrackedBlock;
	private BlockState primaryDecorativeBlock;
	private BlockState primaryPillarBlock;
	private BlockState secondaryBlock;
	private BlockState secondaryDecorativeBlock;
	private BlockState aspectSapling;
	private BlockState fluid;
	
	public TierOneDungeonFirstRoomPiece(ChunkGenerator<?> generator, Random random, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_FIRST_ROOM, random, x - 20, y - 15, z - 20, 40, 30, 40);
		/*eroded = random.nextBoolean();
		uraniumFilled = random.nextBoolean();
		randReduction = random.nextInt(10);*/
	}
	
	public TierOneDungeonFirstRoomPiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_FIRST_ROOM, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomRoomType = nbt.getInt("randomRoomType");
	}
	
	@Override
	protected void readAdditional(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("sp1", spawner1); //spawner type room only
		tagCompound.putBoolean("sp2", spawner2); //spawner type room only
		tagCompound.putInt("randomRoomType", randomRoomType);
		super.readAdditional(tagCompound);
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
		secondaryBlock = blocks.getBlockState("structure_secondary");
		secondaryDecorativeBlock = blocks.getBlockState("structure_secondary_decorative");
		aspectSapling = blocks.getBlockState("aspect_sapling");
		fluid = blocks.getBlockState("fall_fluid");
		
		if(!createRan)
		{
			randomRoomType = randomIn.nextInt(2);
			createRan = true;
		}
		spawner1 = false;
		spawner2 = false;
		
		fillWithBlocks(worldIn, boundingBox,
				firstRoomMinX, firstRoomMinY, firstRoomMinZ,
				firstRoomMaxX, firstRoomMaxY, firstRoomMaxZ,
				primaryBlock, air, false);
		
		buildAspectThemedPuzzle(worldIn, boundingBox, randomIn);
		
		//buildStructureFoundation(worldIn, boundingBoxIn, randomIn, randomRoomType);
		//buildWallsAndFloors(worldIn, boundingBoxIn, randomIn);
		//carveRooms(worldIn, boundingBoxIn);
		//buildIndoorBlocks(worldIn, boundingBoxIn, randomIn, randomRoomType);
		
		return true;
	}
	
	private void buildAspectThemedPuzzle(IWorld world, MutableBoundingBox boundingBox, Random rand)
	{
		
		if(aspectSapling == MSBlocks.BREATH_ASPECT_SAPLING.getDefaultState()) //parkour like frog temple lower room
		{
		
		} else if(aspectSapling == MSBlocks.LIFE_ASPECT_SAPLING.getDefaultState())
		{
			for(int i = 0; i < 3; i++) //TODO will be for Breath
			{
				for(int j = 0; j < 3; j++)
				{
					fillWithBlocks(world, boundingBox, 10 + i * 10, 0, 10 + j * 10, 11 + i * 10, 30, 11 + j * 10, primaryPillarBlock.with(MSDirectionalBlock.FACING, Direction.UP), primaryPillarBlock.with(MSDirectionalBlock.FACING, Direction.UP), false);
					fillWithBlocks(world, boundingBox, 8 + i * 10, 15, 22 + j * 10, 13 + i * 10, 15, 27 + j * 10, primaryDecorativeBlock, primaryDecorativeBlock, false); //platform around columns
				}
			}
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
}