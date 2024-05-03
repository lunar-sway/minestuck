package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.*;
import com.mraof.minestuck.world.gen.structure.gate.GateMushroomPiece;
import com.mraof.minestuck.world.gen.structure.gate.GatePillarPiece;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.village.*;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@FieldsAreNonnullByDefault
public final class MSStructures
{
	public static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_REGISTER = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Minestuck.MOD_ID);
	public static final DeferredRegister<StructureType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, Minestuck.MOD_ID);
	public static final DeferredRegister<StructurePieceType> PIECE_REGISTER = DeferredRegister.create(Registries.STRUCTURE_PIECE, Minestuck.MOD_ID);
	
	// Generic
	public static final Supplier<StructurePieceType> SIMPLE_TEMPLATE_PIECE =
			PIECE_REGISTER.register("simple_template", () -> template(SimpleTemplatePiece::new));
	
	// Overworld
	public static final Supplier<StructureType<FrogTempleStructure>> FROG_TEMPLE_TYPE =
			TYPE_REGISTER.register("frog_temple", () -> asType(FrogTempleStructure.CODEC));
	public static final ResourceKey<Structure> FROG_TEMPLE = key("frog_temple");
	public static final Supplier<StructurePieceType>
			FROG_TEMPLE_PIECE = PIECE_REGISTER.register("frog_temple", () -> contextless(FrogTemplePiece::new)),
			FROG_TEMPLE_PILLAR_PIECE = PIECE_REGISTER.register("frog_temple/pillar", () -> contextless(FrogTemplePillarPiece::new));
	
	// Land
	public static final Supplier<StructurePlacementType<LandGatePlacement>> LAND_GATE_PLACEMENT =
			PLACEMENT_REGISTER.register("land_gate", () -> () -> LandGatePlacement.CODEC);
	public static final Supplier<StructureType<GateStructure>> LAND_GATE_TYPE =
			TYPE_REGISTER.register("land_gate", () -> asType(GateStructure.CODEC));
	public static final ResourceKey<Structure> LAND_GATE = key("land_gate");
	public static final Supplier<StructurePieceType>
			GATE_PILLAR_PIECE = PIECE_REGISTER.register("land_gate/pillar", () -> contextless(GatePillarPiece::new)),
			GATE_MUSHROOM_PIECE = PIECE_REGISTER.register("land_gate/mushroom", () -> contextless(GateMushroomPiece::new));
	
	public static final Supplier<StructureType<SmallRuinStructure>> SMALL_RUIN_TYPE =
			TYPE_REGISTER.register("small_ruin", () -> asType(SmallRuinStructure.CODEC));
	public static final ResourceKey<Structure> SMALL_RUIN = key("small_ruin");
	public static final Supplier<StructurePieceType> SMALL_RUIN_PIECE =
			PIECE_REGISTER.register("small_ruin", () -> contextless(SmallRuinPiece::new));
	
	public static final class ImpDungeon
	{
		public static final Supplier<StructureType<ImpDungeonStructure>> TYPE =
				TYPE_REGISTER.register("imp_dungeon", () -> asType(ImpDungeonStructure.CODEC));
		public static final ResourceKey<Structure> KEY = key("imp_dungeon");
		
		public static final Supplier<StructurePieceType.ContextlessType> ENTRY_PIECE =
				PIECE_REGISTER.register("imp_dungeon/entry", () -> ImpDungeonEntryPiece::new);
		public static final Supplier<StructurePieceType.ContextlessType> ENTRY_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/entry_corridor", () -> ImpDungeonPieces.EntryCorridor::new);
		public static final Supplier<StructurePieceType.ContextlessType> STRAIGHT_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/straight_corridor", () -> ImpDungeonPieces.StraightCorridor::new);
		public static final Supplier<StructurePieceType.ContextlessType> CROSS_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/cross_corridor", () -> ImpDungeonPieces.CrossCorridor::new);
		public static final Supplier<StructurePieceType.ContextlessType> TURN_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/turn_corridor", () -> ImpDungeonPieces.TurnCorridor::new);
		
		public static final Supplier<StructurePieceType.ContextlessType> RETURN_ROOM_PIECE =
				PIECE_REGISTER.register("imp_dungeon/return_room", () -> ImpDungeonPieces.ReturnRoom::new);
		public static final Supplier<StructurePieceType.ContextlessType> ALT_RETURN_ROOM_PIECE =
				PIECE_REGISTER.register("imp_dungeon/alt_return_room", () -> ImpDungeonPieces.ReturnRoomAlt::new);
		public static final Supplier<StructurePieceType.ContextlessType> SPAWNER_ROOM_PIECE =
				PIECE_REGISTER.register("imp_dungeon/spawner_room", () -> ImpDungeonPieces.SpawnerRoom::new);
		public static final Supplier<StructurePieceType.ContextlessType> SPAWNER_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/spawner_corridor", () -> ImpDungeonPieces.SpawnerCorridor::new);
		public static final Supplier<StructurePieceType.ContextlessType> LARGE_SPAWNER_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/large_spawner_corridor", () -> ImpDungeonPieces.LargeSpawnerCorridor::new);
		public static final Supplier<StructurePieceType.ContextlessType> BOOKCASE_ROOM_PIECE =
				PIECE_REGISTER.register("imp_dungeon/bookcase_room", () -> ImpDungeonPieces.BookcaseRoom::new);
		public static final Supplier<StructurePieceType.ContextlessType> OGRE_CORRIDOR_PIECE =
				PIECE_REGISTER.register("imp_dungeon/ogre_corridor", () -> ImpDungeonPieces.OgreCorridor::new);
		
		private static void init()
		{
		}
	}
	
	static
	{
		ImpDungeon.init();
	}
	
	public static final class ConsortVillage
	{
		public static final Supplier<StructureType<ConsortVillageStructure>> TYPE =
				TYPE_REGISTER.register("consort_village", () -> asType(ConsortVillageStructure.CODEC));
		public static final ResourceKey<Structure> KEY = key("consort_village");
		
		public static final Supplier<StructurePieceType>
				VILLAGE_PATH_PIECE = PIECE_REGISTER.register("village_path", () -> contextless(ConsortVillagePieces.VillagePath::new)),
				MARKET_CENTER_PIECE = PIECE_REGISTER.register("market_center", () -> contextless(ConsortVillageCenter.VillageMarketCenter::new)),
				ROCK_CENTER_PIECE = PIECE_REGISTER.register("rock_center", () -> contextless(ConsortVillageCenter.RockCenter::new)),
				CACTUS_PYRAMID_CENTER_PIECE = PIECE_REGISTER.register("cactus_pyramid_center", () -> contextless(ConsortVillageCenter.CactusPyramidCenter::new));
		
		public static final Supplier<StructurePieceType>
				MUSHROOM_TOWER_CENTER_PIECE = PIECE_REGISTER.register("mushroom_tower_center", () -> contextless(SalamanderVillagePieces.RuinedTowerMushroomCenter::new)),
				PIPE_HOUSE_1_PIECE = PIECE_REGISTER.register("pipe_house_1", () -> contextless(SalamanderVillagePieces.PipeHouse1::new)),
				HIGH_PIPE_HOUSE_1_PIECE = PIECE_REGISTER.register("high_pipe_house_1", () -> contextless(SalamanderVillagePieces.HighPipeHouse1::new)),
				SMALL_TOWER_STORE_PIECE = PIECE_REGISTER.register("small_tower_store", () -> contextless(SalamanderVillagePieces.SmallTowerStore::new));
		
		public static final Supplier<StructurePieceType>
				TURTLE_WELL_CENTER_PIECE = PIECE_REGISTER.register("turtle_well_center", () -> contextless(TurtleVillagePieces.TurtleWellCenter::new)),
				SHELL_HOUSE_1_PIECE = PIECE_REGISTER.register("village_shell_house_1", () -> contextless(TurtleVillagePieces.ShellHouse1::new)),
				TURTLE_MARKET_1_PIECE = PIECE_REGISTER.register("turtle_market_1", () -> contextless(TurtleVillagePieces.TurtleMarket1::new)),
				TURTLE_TEMPLE_1_PIECE = PIECE_REGISTER.register("turtle_temple_1", () -> contextless(TurtleVillagePieces.TurtleTemple1::new));
		
		public static final Supplier<StructurePieceType>
				SMALL_VILLAGE_TENT_1_PIECE = PIECE_REGISTER.register("small_village_tent_1", () -> contextless(IguanaVillagePieces.SmallTent1::new)),
				LARGE_VILLAGE_TENT_1_PIECE = PIECE_REGISTER.register("large_village_tent_1", () -> contextless(IguanaVillagePieces.LargeTent1::new)),
				SMALL_TENT_STORE_PIECE = PIECE_REGISTER.register("small_tent_store", () -> contextless(IguanaVillagePieces.SmallTentStore::new));
		
		public static final Supplier<StructurePieceType>
				RADIO_TOWER_CENTER_PIECE = PIECE_REGISTER.register("radio_tower_center", () -> contextless(NakagatorVillagePieces.RadioTowerCenter::new)),
				HIGH_NAK_HOUSING_1_PIECE = PIECE_REGISTER.register("high_nak_housing_1", () -> contextless(NakagatorVillagePieces.HighNakHousing1::new)),
				HIGH_NAK_MARKET_PIECE = PIECE_REGISTER.register("high_nak_market", () -> contextless(NakagatorVillagePieces.HighNakMarket1::new)),
				HIGH_NAK_INN_PIECE = PIECE_REGISTER.register("high_nak_inn", () -> contextless(NakagatorVillagePieces.HighNakInn1::new));
		
		private static void init()
		{
		}
	}
	
	static
	{
		ConsortVillage.init();
	}
	
	public static final Supplier<StructureType<LargeWoodObjectStructure>> LARGE_WOOD_OBJECT_TYPE =
			TYPE_REGISTER.register("large_wood_object", () -> asType(LargeWoodObjectStructure.CODEC));
	public static final ResourceKey<Structure> LARGE_WOOD_OBJECT = key("large_wood_object");
	
	public static final Supplier<StructureType<PinkTowerStructure>> PINK_TOWER_TYPE =
			TYPE_REGISTER.register("pink_tower", () -> asType(PinkTowerStructure.CODEC));
	public static final ResourceKey<Structure> PINK_TOWER = key("pink_tower");
	public static final Supplier<StructurePieceType> PINK_TOWER_PIECE =
			PIECE_REGISTER.register("pink_tower", () -> template(PinkTowerPiece::new));
	
	// Skaia
	public static final class SkaiaCastle
	{
		public static final Supplier<StructureType<CastleStructure>> TYPE =
				TYPE_REGISTER.register("skaia_castle", () -> asType(CastleStructure.CODEC));
		public static final ResourceKey<Structure> KEY = key("skaia_castle");
		
		public static final Supplier<StructurePieceType>
				START_PIECE = PIECE_REGISTER.register("skaia_castle/start", () -> contextless(CastleStartPiece::new)),
				SOLID_PIECE = PIECE_REGISTER.register("skaia_castle/solid", () -> contextless(CastleSolidPiece::new)),
				WALL_PIECE = PIECE_REGISTER.register("skaia_castle/wall", () -> contextless(CastleWallPiece::new)),
				ROOM_PIECE = PIECE_REGISTER.register("skaia_castle/room", () -> contextless(CastleRoomPiece::new)),
				LIBRARY_PIECE = PIECE_REGISTER.register("skaia_castle/library", () -> contextless(CastleLibraryPiece::new)),
				STAIRCASE_PIECE = PIECE_REGISTER.register("skaia_castle/staircase", () -> contextless(CastleStaircasePiece::new));
		
		private static void init()
		{
		}
	}
	
	static
	{
		SkaiaCastle.init();
	}
	
	
	private static <S extends Structure> StructureType<S> asType(Codec<S> codec)
	{
		return () -> codec;
	}
	
	private static ResourceKey<Structure> key(String name)
	{
		return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	private static StructurePieceType contextless(StructurePieceType.ContextlessType type)
	{
		return type;
	}
	
	private static StructurePieceType template(StructurePieceType.StructureTemplateType type)
	{
		return type;
	}
}
