package com.mraof.minestuck.world.gen.feature.structure.tiered.tier1;

import com.mojang.serialization.Codec;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class TierOneDungeonStructure extends Structure<NoFeatureConfig>
{
	public TierOneDungeonStructure(Codec<NoFeatureConfig> codec)
	{
		super(codec);
	}
	
	//TODO limit natural spawning inside the structure
	/*@Override
	public boolean getDefaultRestrictsSpawnsToInside()
	{
		return super.getDefaultRestrictsSpawnsToInside();
	}*/
	
	@Override
	public GenerationStage.Decoration step()
	{
		return GenerationStage.Decoration.SURFACE_STRUCTURES;
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory()
	{
		return Start::new;
	}
	
	public static class Start extends StructureStart<NoFeatureConfig>
	{
		private Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager templates, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config)
		{
			int x = chunkX * 16 + random.nextInt(16);
			int z = chunkZ * 16 + random.nextInt(16);
			
			Layout layout = Layout.pickLayout(random); //picks one of the room generation patterns
			int modularRoomCount = 5 + random.nextInt(2); //how many rooms there are between beginning and end, 5-7 range
			boolean organicDesign = random.nextBoolean(); //true means it will have a cavernous appearance with a natural block like stone/ice/sandstone coating the walls. false means it will have a structured shaped and use bricks
			
			TierOneDungeonEntryPiece entryPiece = new TierOneDungeonEntryPiece(templates, organicDesign, generator, random, x, z); //TODO input layout to determine if the entry piece should build a structure to connect straight into the end piece(should have a room connecting the two, with doors to go into optional area)
			pieces.add(entryPiece);
			
			/*if(modularRoomCount > 5) //TODO increase variability by creating alternative orders in which rooms can appear by modifying the enum integer array
			{
				random.nextInt(modularRoomCount - 5);
			}*/
			
			//TODO include each of these components into the structure as a whole or as a parameter into each structure piece following Entry so that they dont have to be rechecked each time. On top of that, add check whether the room style will be organic(meaning its shaped to match the terrain aesthetics) or structured(meaning it primarily follows the aspect aesthetic)
			//add each back into each piece separately, this information will not be recorded until after the rest have been initialized
			entryPiece.getWorldAspect();
			entryPiece.getWorldClass();
			entryPiece.getWorldTerrain();
			
			int roomOffset = 32; //each modular room is 32x32 wide and 16 deep
			boolean endPieceStarted = false;
			
			for(int roomX = 0; roomX < 3; roomX++) //x iterate
			{
				for(int roomZ = 0; roomZ < 3; roomZ++) //z iterate
				{
					int ordinalCombo = roomX + (roomZ * 3); //combines the x and z components and lists each of the components in a 3x3 grid as values 1-9(1 at top left, 9 at bottom right, left to right then top to bottom)
					
					//finds the center of the structure, then subtracts or adds to the location. roomX/roomZ:0 = -32, 1 = 0, 2 = 32
					int newX = x + roomOffset * (roomX - 1);
					int newZ = z + roomOffset * (roomZ - 1);
					
					if(layout.roomPlacementOrder[ordinalCombo] == modularRoomCount + 1) //the end piece, it gets placed down at the point after all possible modules are situated
					{
						TierOneDungeonEndPiece endPiece = new TierOneDungeonEndPiece(templates, layout, entryPiece.getOrientation(), newX, entryPiece.getBoundingBox().y0 - 16, newZ); //leaving out organic design value for now
						pieces.add(endPiece);
						endPieceStarted = true;
					}
					
					if(layout.roomPlacementOrder[ordinalCombo] == 1 || layout.roomPlacementOrder[ordinalCombo] == 5 || (!endPieceStarted && (layout.roomPlacementOrder[ordinalCombo] == 6 || layout.roomPlacementOrder[ordinalCombo] == 7)))
					{
						TierOneDungeonCombatModulePiece combatModulePiece = new TierOneDungeonCombatModulePiece(templates, organicDesign, layout, entryPiece.getOrientation(), calculateDirection(layout.roomsEntryDirection[ordinalCombo], entryPiece.getOrientation()), newX, entryPiece.getBoundingBox().y0, newZ);
						pieces.add(combatModulePiece);
					}
					
					if(layout.roomPlacementOrder[ordinalCombo] == 2)
					{
						TierOneDungeonPuzzleModulePiece puzzleModulePiece = new TierOneDungeonPuzzleModulePiece(templates, organicDesign, layout, entryPiece.getOrientation(), calculateDirection(layout.roomsEntryDirection[ordinalCombo], entryPiece.getOrientation()), newX, entryPiece.getBoundingBox().y0, newZ);
						pieces.add(puzzleModulePiece);
					}
					
					if(layout.roomPlacementOrder[ordinalCombo] == 3) //return node
					{
						TierOneDungeonReturnModulePiece returnModulePiece = new TierOneDungeonReturnModulePiece(templates, organicDesign, layout, entryPiece.getOrientation(), calculateDirection(layout.roomsEntryDirection[ordinalCombo], entryPiece.getOrientation()), newX, entryPiece.getBoundingBox().y0, newZ);
						pieces.add(returnModulePiece);
					}
					
					if(layout.roomPlacementOrder[ordinalCombo] == 4)
					{
						TierOneDungeonTreasureModulePiece treasureModulePiece = new TierOneDungeonTreasureModulePiece(templates, organicDesign, layout, entryPiece.getOrientation(), calculateDirection(layout.roomsEntryDirection[ordinalCombo], entryPiece.getOrientation()), newX, entryPiece.getBoundingBox().y0, newZ);
						pieces.add(treasureModulePiece);
					}
					
					//TODO create additional room types of: trap, lore, external puzzle(multi-structure puzzles like seen with the reflected mirrors on Jane's land), npc quest(fetch quest), land quest(fixing local instance of land's issue)
				}
			}
			
			calculateBoundingBox();
		}
		
		/**
		 * orients the roomsEntryDirection value to match the direction of the rest of the structure which is decided through entryPieceDirection)
		 */
		public Direction calculateDirection(Direction roomsEntryDirection, Direction entryPieceDirection)
		{
			if(roomsEntryDirection == null)
				return null;
			
			//TODO these are mirrored in about half of circumstances
			if(entryPieceDirection == Direction.NORTH)
				return roomsEntryDirection;
			else if(entryPieceDirection == Direction.EAST)
				return roomsEntryDirection.getClockWise();
			else if(entryPieceDirection == Direction.SOUTH)
				return roomsEntryDirection.getOpposite();
			else //if west
				return roomsEntryDirection.getCounterClockWise();
		}
		
		/*public enum Module
		{
			PUZZLE,
			COMBAT,
			RETURN,
			TREASURE,
			GENERIC;
			
			public static Module[] getArrangement(int roomCount, Random random)
			{
				switch(roomCount)
				{
					case 5 :
						return new Module[]{};
				}
				return new Module[]
			}
		}*/
		
		public enum Layout
		{
			LINEAR(new Direction[]{
					Direction.EAST, Direction.SOUTH, Direction.SOUTH,
					Direction.NORTH, null, Direction.SOUTH,
					Direction.NORTH, Direction.WEST, Direction.WEST},
					new Integer[]{
							2, 1, 8,
							3, 0, 7,
							4, 5, 6
					}),
			
			BRANCHED(new Direction[]{
					Direction.EAST, Direction.EAST, Direction.SOUTH,
					Direction.EAST, null, Direction.WEST,
					Direction.NORTH, Direction.WEST, Direction.WEST},
					new Integer[]{
							8, 6, 3,
							1, 0, 2,
							4, 5, 7
					}),
			
			OPTIONAL(new Direction[]{
					Direction.EAST, Direction.SOUTH, Direction.SOUTH,
					Direction.EAST, null, Direction.WEST,
					Direction.NORTH, Direction.NORTH, Direction.WEST},
					new Integer[]{
							7, 1, 5,
							4, 8, 2,
							6, 3, 7
					}); //rooms between
			
			private final Direction[] roomsEntryDirection;
			private final Integer[] roomPlacementOrder;
			
			Layout(Direction[] directions, Integer[] order)
			{
				this.roomsEntryDirection = directions;
				this.roomPlacementOrder = order;
			}
			
			public static Layout pickLayout(Random random)
			{
				int randomInt = random.nextInt(values().length);
				for(Layout type : values())
				{
					if(type.ordinal() == randomInt)
						return type;
				}
				
				return LINEAR; //if not found in for loop, LINEAR used as generic
			}
		}
	}
	
	/*public TierOneDungeonStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory)
	{
		super(configFactory);
	}
	
	@Override
	protected int getSeedModifier()
	{
		return 41361202;
	}
	
	@Override
	public IStartFactory getStartFactory()
	{
		return Start::new;
	}
	
	@Override
	public String getStructureName()
	{
		return Minestuck.MOD_ID + ":tier_one_dungeon";
	}
	
	@Override
	public int getSize()
	{
		return 24;
	}
	
	@Override
	protected int getBiomeFeatureDistance(ChunkGenerator<?> generator)
	{
		return 10;
	}
	
	@Override
	protected int getBiomeFeatureSeparation(ChunkGenerator<?> generator)
	{
		return 4;
	}
	
	public static class Start extends StructureStart
	{
		private Start(Structure<?> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int reference, long seed)
		{
			super(structure, chunkX, chunkZ, boundingBox, reference, seed);
		}
		
		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			TierOneDungeonEntryPiece mainPiece = new TierOneDungeonEntryPiece(templateManagerIn, generator, rand, x, z);
			components.add(mainPiece);
			
			TierOneDungeonEndPiece secondaryPiece = new TierOneDungeonEndPiece(templateManagerIn, mainPiece.getCoordBaseMode(), x, mainPiece.getBoundingBox().minY, z);
			
			components.add(secondaryPiece);
			
			recalculateStructureSize();
		}
	}*/
}