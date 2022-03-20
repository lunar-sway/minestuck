package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.EnumClass;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.StructureBlockRegistryProcessor;
import com.mraof.minestuck.world.gen.feature.structure.ImprovedStructurePiece;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.tiered.CommonTemplateProcessors;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

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
	private BlockState ground;
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
	
	private Template naturalDesignTemplate;
	
	//private final TemplateManager templates;
	TierOneDungeonStructure.Start.Layout layout;
	Direction entryDirection;
	
	public TierOneDungeonCombatModulePiece(TemplateManager templates, TierOneDungeonStructure.Start.Layout layout, Direction orientation, Direction entryDirection, int x, int y, int z) //this constructor is used when the structure is first initialized
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_COMBAT_MODULE, 0);
		
		setOrientation(orientation);
		setBounds(x, y, z, 32, 16, 32);
		
		initTemplates(templates);
		this.layout = layout;
		this.entryDirection = entryDirection;
	}
	
	public TierOneDungeonCombatModulePiece(TemplateManager templates, CompoundNBT nbt) //this constructor is used for reading from data
	{
		super(MSStructurePieces.TIER_ONE_DUNGEON_COMBAT_MODULE, nbt);
		spawner1 = nbt.getBoolean("sp1");
		spawner2 = nbt.getBoolean("sp2");
		randomType = nbt.getInt("randomType");
		roomVariable1 = nbt.getInt("roomVariable1");
		
		initTemplates(templates);
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT tagCompound) //actually writeAdditional
	{
		tagCompound.putBoolean("sp1", spawner1); //spawner type room only
		tagCompound.putBoolean("sp2", spawner2); //spawner type room only
		tagCompound.putInt("randomType", randomType);
	}
	
	private void initTemplates(TemplateManager templates)
	{
		naturalDesignTemplate = templates.getOrCreate(new ResourceLocation(Minestuck.MOD_ID, "dungeons/tier_1_combat_module_natural"));
	}
	
	@Override
	public boolean postProcess(ISeedReader worldIn, StructureManager structureManagerIn, ChunkGenerator chunkGeneratorIn, Random randomIn, MutableBoundingBox boundingBoxIn, ChunkPos chunkPosIn, BlockPos blockPosIn)
	{
		StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
		
		ground = blocks.getBlockState("ground");
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
			randomType = randomIn.nextInt(9);
			roomVariable1 = randomIn.nextInt(6);
			createRan = true;
		}
		
		buildGenericCombatRoom(worldIn, boundingBoxIn, randomIn, chunkGeneratorIn);
		
		return true;
	}
	
	private void buildGenericCombatRoom(ISeedReader world, MutableBoundingBox boundingBox, Random rand, ChunkGenerator chunkGeneratorIn)
	{
		BlockPos templatePos = new BlockPos(getActualPos(pieceMaxX, pieceMinY, pieceMaxZ));
		PlacementSettings settings = new PlacementSettings().setChunkPos(new ChunkPos(templatePos)).setRandom(rand).setBoundingBox(boundingBox).addProcessor(new StructureBlockRegistryProcessor(StructureBlockRegistry.getOrDefault(chunkGeneratorIn)));
		//settings.setRotation(getRotation());
		settings.setRotation(MSRotationUtil.fromDirection(getOrientation()));
		
		//BlockPos structurePos = organicDesignTemplate.getZeroPositionWithTransform(templatePos, Mirror.NONE, rotation);
		//BlockPos center = new BlockPos((organicDesignTemplate.getSize().getX() - 1) / 2, 0, (organicDesignTemplate.getSize().getZ() - 1) / 2);
		
		//StructureBlockUtil.placeCenteredTemplate(world, templatePos, organicDesignTemplate, boundingBox, chunkGeneratorIn, MSRotationUtil.fromDirection(getOrientation()), true);
		CommonTemplateProcessors.placeAndProcessTemplate(world, boundingBox, naturalDesignTemplate, rand, chunkGeneratorIn, templatePos, settings);
		
		//generateBox(world, boundingBox, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMaxY, pieceMaxZ, Blocks.RED_STAINED_GLASS.defaultBlockState(), air, false);
		//generateBox(world, boundingBox, pieceMinX, pieceMinY, pieceMinZ, pieceMaxX, pieceMinY, pieceMaxZ, lightBlock, lightBlock, false);
		placeBlock(world, lightBlock, pieceMinX, pieceMinY, pieceMinZ, boundingBox);
		placeBlock(world, lightBlock, pieceMaxX, pieceMaxY, pieceMaxZ, boundingBox);
		placeBlock(world, secondaryBlock, pieceMaxX - 5, pieceMaxY - 5, pieceMaxZ - 5, boundingBox);
		
		if(entryDirection != null && entryDirection != Direction.UP && entryDirection != Direction.DOWN)
		{
			if(entryDirection == Direction.NORTH)
				generateAirBox(world, boundingBox, pieceMinX + 15, pieceMaxY - 10, pieceMaxZ, pieceMaxX - 15, pieceMaxY - 6, pieceMaxZ);
			else if(entryDirection == Direction.SOUTH)
				generateAirBox(world, boundingBox, pieceMinX + 15, pieceMaxY - 10, pieceMinZ, pieceMaxX - 15, pieceMaxY - 6, pieceMinZ);
			else if(entryDirection == Direction.EAST)
				generateAirBox(world, boundingBox, pieceMinX, pieceMaxY - 10, pieceMinZ + 15, pieceMinX, pieceMaxY - 6, pieceMaxZ - 15);
			else if(entryDirection == Direction.WEST)
				generateAirBox(world, boundingBox, pieceMaxX, pieceMaxY - 10, pieceMinZ + 15, pieceMaxX, pieceMaxY - 6, pieceMaxZ - 15);
			
			world.setBlock(getActualPos(pieceMaxX / 2, pieceMinY + 1, pieceMaxZ / 2).relative(entryDirection, 5), secondaryDecorativeBlock, Constants.BlockFlags.BLOCK_UPDATE);
		}
	}
	
	
}