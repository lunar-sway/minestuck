package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;

public final class MSStructureSets
{
	public static final DeferredRegister<StructureSet> REGISTER = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Minestuck.MOD_ID);
	
	static {
		// Overworld
		REGISTER.register("frog_temple", () -> new StructureSet(MSConfiguredStructures.FROG_TEMPLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		REGISTER.register("land_gate", () -> new StructureSet(MSConfiguredStructures.LAND_GATE.getHolder().orElseThrow(), LandGatePlacement.INSTANCE));
		REGISTER.register("small_ruin", () -> new StructureSet(MSConfiguredStructures.SMALL_RUIN.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643)));
		REGISTER.register("imp_dungeon", () -> new StructureSet(MSConfiguredStructures.IMP_DUNGEON.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185)));
		REGISTER.register("consort_village", () -> new StructureSet(MSConfiguredStructures.CONSORT_VILLAGE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312)));
		
		// Skaia
		REGISTER.register("skaia_castle", () -> new StructureSet(MSConfiguredStructures.SKAIA_CASTLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
}