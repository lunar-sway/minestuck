package com.mraof.minestuck.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@FieldsAreNonnullByDefault
public final class MSStructures
{
	public static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_REGISTER = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Minestuck.MOD_ID);
	public static final DeferredRegister<StructureType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.STRUCTURE_TYPE, Minestuck.MOD_ID);
	
	// Overworld
	public static final Supplier<StructureType<FrogTempleStructure>> FROG_TEMPLE_TYPE =
			TYPE_REGISTER.register("frog_temple", () -> asType(FrogTempleStructure.CODEC));
	public static final ResourceKey<Structure> FROG_TEMPLE = key("frog_temple");
	
	// Land
	public static final Supplier<StructurePlacementType<LandGatePlacement>> LAND_GATE_PLACEMENT =
			PLACEMENT_REGISTER.register("land_gate", () -> () -> LandGatePlacement.CODEC);
	public static final Supplier<StructureType<GateStructure>> LAND_GATE_TYPE =
			TYPE_REGISTER.register("land_gate", () -> asType(GateStructure.CODEC));
	public static final ResourceKey<Structure> LAND_GATE = key("land_gate");
	
	public static final Supplier<StructureType<SmallRuinStructure>> SMALL_RUIN_TYPE =
			TYPE_REGISTER.register("small_ruin", () -> asType(SmallRuinStructure.CODEC));
	public static final ResourceKey<Structure> SMALL_RUIN = key("small_ruin");
	
	public static final Supplier<StructureType<ImpDungeonStructure>> IMP_DUNGEON_TYPE =
			TYPE_REGISTER.register("imp_dungeon", () -> asType(ImpDungeonStructure.CODEC));
	public static final ResourceKey<Structure> IMP_DUNGEON = key("imp_dungeon");
	
	public static final Supplier<StructureType<ConsortVillageStructure>> CONSORT_VILLAGE_TYPE =
			TYPE_REGISTER.register("consort_village", () -> asType(ConsortVillageStructure.CODEC));
	public static final ResourceKey<Structure> CONSORT_VILLAGE = key("consort_village");
	
	public static final Supplier<StructureType<LargeWoodObjectStructure>> LARGE_WOOD_OBJECT_TYPE =
			TYPE_REGISTER.register("large_wood_object", () -> asType(LargeWoodObjectStructure.CODEC));
	public static final ResourceKey<Structure> LARGE_WOOD_OBJECT = key("large_wood_object");
	
	public static final Supplier<StructureType<PinkTowerStructure>> PINK_TOWER_TYPE =
			TYPE_REGISTER.register("pink_tower", () -> asType(PinkTowerStructure.CODEC));
	public static final ResourceKey<Structure> PINK_TOWER = key("pink_tower");
	
	// Skaia
	public static final Supplier<StructureType<CastleStructure>> SKAIA_CASTLE_TYPE =
			TYPE_REGISTER.register("skaia_castle", () -> asType(CastleStructure.CODEC));
	public static final ResourceKey<Structure> SKAIA_CASTLE = key("skaia_castle");
	
	private static <S extends Structure> StructureType<S> asType(Codec<S> codec)
	{
		return () -> codec;
	}
	
	private static ResourceKey<Structure> key(String name)
	{
		return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(Minestuck.MOD_ID, name));
	}
}
