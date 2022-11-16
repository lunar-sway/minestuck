package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.*;
import com.mraof.minestuck.world.gen.structure.gate.GateMushroomPiece;
import com.mraof.minestuck.world.gen.structure.gate.GatePillarPiece;
import com.mraof.minestuck.world.gen.structure.village.*;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSStructurePieces
{
	public static final DeferredRegister<StructurePieceType> REGISTER = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, Minestuck.MOD_ID);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> FROG_TEMPLE = REGISTER.register("frog_temple", () -> FrogTemplePiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> FROG_TEMPLE_PILLAR = REGISTER.register("frog_temple_pillar", () -> FrogTemplePillarPiece::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> GATE_PILLAR = REGISTER.register("gate_pillar", () -> GatePillarPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> GATE_MUSHROOM = REGISTER.register("gate_mushroom", () -> GateMushroomPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SMALL_RUIN = REGISTER.register("small_ruin", () -> SmallRuinPiece::new);
	
	
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_ENTRY = REGISTER.register("imp_entry", () -> ImpDungeonEntryPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_ENTRY_CORRIDOR = REGISTER.register("imp_entry_corridor", () -> ImpDungeonPieces.EntryCorridor::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_STRAIGHT_CORRIDOR = REGISTER.register("imp_straight_corridor", () -> ImpDungeonPieces.StraightCorridor::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_CROSS_CORRIDOR = REGISTER.register("imp_cross_corridor", () -> ImpDungeonPieces.CrossCorridor::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_TURN_CORRIDOR = REGISTER.register("imp_turn_corridor", () -> ImpDungeonPieces.TurnCorridor::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_RETURN_ROOM = REGISTER.register("imp_return_room", () -> ImpDungeonPieces.ReturnRoom::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_ALT_RETURN_ROOM = REGISTER.register("imp_alt_return_room", () -> ImpDungeonPieces.ReturnRoomAlt::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_SPAWNER_ROOM = REGISTER.register("imp_spawner_room", () -> ImpDungeonPieces.SpawnerRoom::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_SPAWNER_CORRIDOR = REGISTER.register("imp_spawner_corridor", () -> ImpDungeonPieces.SpawnerCorridor::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_LARGE_SPAWNER_CORRIDOR = REGISTER.register("imp_large_spawner_corridor", () -> ImpDungeonPieces.LargeSpawnerCorridor::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_BOOKCASE_ROOM = REGISTER.register("imp_bookcase_room", () -> ImpDungeonPieces.BookcaseRoom::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> IMP_OGRE_CORRIDOR = REGISTER.register("imp_ogre_corridor", () -> ImpDungeonPieces.OgreCorridor::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> VILLAGE_PATH = REGISTER.register("village_path", () -> ConsortVillagePieces.VillagePath::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> MARKET_CENTER = REGISTER.register("market_center", () -> ConsortVillageCenter.VillageMarketCenter::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> ROCK_CENTER = REGISTER.register("rock_center", () -> ConsortVillageCenter.RockCenter::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> CACTUS_PYRAMID_CENTER = REGISTER.register("cactus_pyramid_center", () -> ConsortVillageCenter.CactusPyramidCenter::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> MUSHROOM_TOWER_CENTER = REGISTER.register("mushroom_tower_center", () -> SalamanderVillagePieces.RuinedTowerMushroomCenter::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> TURTLE_WELL_CENTER = REGISTER.register("turtle_well_center", () -> TurtleVillagePieces.TurtleWellCenter::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> RADIO_TOWER_CENTER = REGISTER.register("radio_tower_center", () -> NakagatorVillagePieces.RadioTowerCenter::new);
	
	
	public static final RegistryObject<StructurePieceType.ContextlessType> PIPE_HOUSE_1 = REGISTER.register("pipe_house_1", () -> SalamanderVillagePieces.PipeHouse1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> HIGH_PIPE_HOUSE_1 = REGISTER.register("high_pipe_house_1", () -> SalamanderVillagePieces.HighPipeHouse1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SMALL_TOWER_STORE = REGISTER.register("small_tower_store", () -> SalamanderVillagePieces.SmallTowerStore::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> SHELL_HOUSE_1 = REGISTER.register("village_shell_house_1", () -> TurtleVillagePieces.ShellHouse1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> TURTLE_MARKET_1 = REGISTER.register("turtle_market_1", () -> TurtleVillagePieces.TurtleMarket1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> TURTLE_TEMPLE_1 = REGISTER.register("turtle_temple_1", () -> TurtleVillagePieces.TurtleTemple1::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> SMALL_VILLAGE_TENT_1 = REGISTER.register("small_village_tent_1", () -> IguanaVillagePieces.SmallTent1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> LARGE_VILLAGE_TENT_1 = REGISTER.register("large_village_tent_1", () -> IguanaVillagePieces.LargeTent1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SMALL_TENT_STORE = REGISTER.register("small_tent_store", () -> IguanaVillagePieces.SmallTentStore::new);
	
	public static final RegistryObject<StructurePieceType.ContextlessType> HIGH_NAK_HOUSING_1 = REGISTER.register("high_nak_housing_1", () -> NakagatorVillagePieces.HighNakHousing1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> HIGH_NAK_MARKET = REGISTER.register("high_nak_market", () -> NakagatorVillagePieces.HighNakMarket1::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> HIGH_NAK_INN = REGISTER.register("high_nak_inn", () -> NakagatorVillagePieces.HighNakInn1::new);
	
	
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_START = REGISTER.register("skaia_castle_start", () -> CastleStartPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_SOLID = REGISTER.register("skaia_castle_solid", () -> CastleSolidPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_WALL = REGISTER.register("skaia_castle_wall", () -> CastleWallPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_ROOM = REGISTER.register("skaia_castle_room", () -> CastleRoomPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_LIBRARY = REGISTER.register("skaia_castle_library", () -> CastleLibraryPiece::new);
	public static final RegistryObject<StructurePieceType.ContextlessType> SKAIA_CASTLE_STAIRCASE = REGISTER.register("skaia_castle_staircase", () -> CastleStaircasePiece::new);
	
}