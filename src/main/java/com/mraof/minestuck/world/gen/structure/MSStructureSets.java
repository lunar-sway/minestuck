package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;

public final class MSStructureSets
{
	public static final DeferredRegister<StructureSet> REGISTER = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, Minestuck.MOD_ID);
	
	static {
		REGISTER.register("frog_temple", () -> new StructureSet(MSConfiguredStructures.FROG_TEMPLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		REGISTER.register("skaia_castle", () -> new StructureSet(MSConfiguredStructures.SKAIA_CASTLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
}