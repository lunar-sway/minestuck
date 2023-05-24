package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class MSStructures
{
	public static final DeferredRegister<StructureType<?>> REGISTER = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Minestuck.MOD_ID);
	
	// Overworld
	public static final RegistryObject<StructureType<FrogTempleStructure>> FROG_TEMPLE = REGISTER.register("frog_temple", () -> () -> FrogTempleStructure.CODEC);
	
	// Land
	public static final RegistryObject<StructureType<GateStructure>> LAND_GATE = REGISTER.register("land_gate", () -> () -> GateStructure.CODEC);
	public static final RegistryObject<StructureType<SmallRuinStructure>> SMALL_RUIN = REGISTER.register("small_ruin", () -> () -> SmallRuinStructure.CODEC);
	public static final RegistryObject<StructureType<ImpDungeonStructure>> IMP_DUNGEON = REGISTER.register("imp_dungeon", () -> () -> ImpDungeonStructure.CODEC);
	public static final RegistryObject<StructureType<ConsortVillageStructure>> CONSORT_VILLAGE = REGISTER.register("consort_village", () -> () -> ConsortVillageStructure.CODEC);
	
	// Skaia
	public static final RegistryObject<StructureType<CastleStructure>> SKAIA_CASTLE = REGISTER.register("skaia_castle", () -> () -> CastleStructure.CODEC);
}