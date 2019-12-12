package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.GateMushroomPiece;
import com.mraof.minestuck.world.gen.feature.structure.GatePillarPiece;
import com.mraof.minestuck.world.gen.feature.structure.SmallRuinPiece;
import com.mraof.minestuck.world.gen.feature.structure.village.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public final class MSStructurePieces
{
	public static IStructurePieceType GATE_PILLAR;
	public static IStructurePieceType GATE_MUSHROOM;
	public static IStructurePieceType SMALL_RUIN;
	
	public static IStructurePieceType VILLAGE_PATH;
	public static IStructurePieceType MARKET_CENTER;
	public static IStructurePieceType ROCK_CENTER;
	public static IStructurePieceType CACTUS_PYRAMID_CENTER;
	public static IStructurePieceType MUSHROOM_TOWER_CENTER;
	public static IStructurePieceType TURTLE_WELL_CENTER;
	public static IStructurePieceType RADIO_TOWER_CENTER;
	public static IStructurePieceType PIPE_HOUSE_1, HIGH_PIPE_HOUSE_1, SMALL_TOWER_STORE;
	public static IStructurePieceType SHELL_HOUSE_1, TURTLE_MARKET_1, TURTLE_TEMPLE_1;
	public static IStructurePieceType SMALL_VILLAGE_TENT_1, LARGE_VILLAGE_TENT_1, SMALL_TENT_STORE;
	public static IStructurePieceType HIGH_NAK_HOUSING_1, HIGH_NAK_MARKET, HIGH_NAK_INN;
	
	/**
	 * Should only be called by {@link com.mraof.minestuck.world.gen.feature.MSFeatures} on feature registry.
	 */
	static void init()
	{
		GATE_PILLAR = register(GatePillarPiece::new, Minestuck.MOD_ID+":gate_pillar");
		GATE_MUSHROOM = register(GateMushroomPiece::new, Minestuck.MOD_ID+":gate_mushroom");
		SMALL_RUIN = register(SmallRuinPiece::new, Minestuck.MOD_ID+":small_ruin");
		
		VILLAGE_PATH = register(ConsortVillagePieces.VillagePath::new, Minestuck.MOD_ID+":village_path");
		MARKET_CENTER = register(ConsortVillageCenter.VillageMarketCenter::new, Minestuck.MOD_ID+":market_center");
		ROCK_CENTER = register(ConsortVillageCenter.RockCenter::new, Minestuck.MOD_ID+":rock_center");
		CACTUS_PYRAMID_CENTER = register(ConsortVillageCenter.CactusPyramidCenter::new, Minestuck.MOD_ID+":cactus_pyramid_center");
		MUSHROOM_TOWER_CENTER = register(SalamanderVillagePieces.RuinedTowerMushroomCenter::new, Minestuck.MOD_ID+":mushroom_tower_center");
		TURTLE_WELL_CENTER = register(TurtleVillagePieces.TurtleWellCenter::new, Minestuck.MOD_ID+":turtle_well_center");
		RADIO_TOWER_CENTER = register(NakagatorVillagePieces.RadioTowerCenter::new, Minestuck.MOD_ID+":turtle_well_center");
		PIPE_HOUSE_1 = register(SalamanderVillagePieces.PipeHouse1::new, Minestuck.MOD_ID+":pipe_house_1");
		HIGH_PIPE_HOUSE_1 = register(SalamanderVillagePieces.HighPipeHouse1::new, Minestuck.MOD_ID+":high_pipe_house_1");
		SMALL_TOWER_STORE = register(SalamanderVillagePieces.SmallTowerStore::new, Minestuck.MOD_ID+":small_tower_store");
		SHELL_HOUSE_1 = register(TurtleVillagePieces.ShellHouse1::new, Minestuck.MOD_ID+":village_shell_house_1");
		TURTLE_MARKET_1 = register(TurtleVillagePieces.TurtleMarket1::new, Minestuck.MOD_ID+":turtle_market_1");
		TURTLE_TEMPLE_1 = register(TurtleVillagePieces.TurtleTemple1::new, Minestuck.MOD_ID+":turtle_temple_1");
		HIGH_NAK_HOUSING_1 = register(NakagatorVillagePieces.HighNakHousing1::new, Minestuck.MOD_ID+":high_nak_housing_1");
		HIGH_NAK_MARKET = register(NakagatorVillagePieces.HighNakMarket1::new, Minestuck.MOD_ID+":high_nak_market");
		HIGH_NAK_INN = register(NakagatorVillagePieces.HighNakInn1::new, Minestuck.MOD_ID+":high_nak_inn");
		SMALL_VILLAGE_TENT_1 = register(IguanaVillagePieces.SmallTent1::new, Minestuck.MOD_ID+":small_village_tent_1");
		LARGE_VILLAGE_TENT_1 = register(IguanaVillagePieces.LargeTent1::new, Minestuck.MOD_ID+":large_village_tent_1");
		SMALL_TENT_STORE = register(IguanaVillagePieces.SmallTentStore::new, Minestuck.MOD_ID+":small_tent_store");
	}
	
	private static IStructurePieceType register(IStructurePieceType type, String name) {
		return Registry.register(Registry.STRUCTURE_PIECE, name, type);
	}
}