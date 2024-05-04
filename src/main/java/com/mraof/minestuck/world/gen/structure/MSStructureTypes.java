package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MSStructureTypes
{
	public static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, Minestuck.MOD_ID);
	
	// Overworld
	public static final Supplier<StructureType<FrogTempleStructure>> FROG_TEMPLE = REGISTER.register("frog_temple", () -> asType(FrogTempleStructure.CODEC));
	
	// Land
	public static final Supplier<StructureType<GateStructure>> LAND_GATE = REGISTER.register("land_gate", () -> asType(GateStructure.CODEC));
	public static final Supplier<StructureType<SmallRuinStructure>> SMALL_RUIN = REGISTER.register("small_ruin", () -> asType(SmallRuinStructure.CODEC));
	public static final Supplier<StructureType<ImpDungeonStructure>> IMP_DUNGEON = REGISTER.register("imp_dungeon", () -> asType(ImpDungeonStructure.CODEC));
	public static final Supplier<StructureType<ConsortVillageStructure>> CONSORT_VILLAGE = REGISTER.register("consort_village", () -> asType(ConsortVillageStructure.CODEC));
	
	public static final Supplier<StructureType<LargeWoodObjectStructure>> LARGE_WOOD_OBJECT = REGISTER.register("large_wood_object", () -> asType(LargeWoodObjectStructure.CODEC));
	public static final Supplier<StructureType<PinkTowerStructure>> PINK_TOWER = REGISTER.register("pink_tower", () -> asType(PinkTowerStructure.CODEC));
	
	// Skaia
	public static final Supplier<StructureType<CastleStructure>> SKAIA_CASTLE = REGISTER.register("skaia_castle", () -> asType(CastleStructure.CODEC));
	
	private static <S extends Structure> StructureType<S> asType(Codec<S> codec)
	{
		return () -> codec;
	}
}