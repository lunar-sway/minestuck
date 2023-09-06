package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public final class MSStructureSetProvider
{
	public static void register(BootstapContext<StructureSet> context)
	{
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
		
		// Overworld
		context.register(key("frog_temple"), new StructureSet(structures.getOrThrow(MSStructures.FROG_TEMPLE), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		context.register(key("land_gate"), new StructureSet(structures.getOrThrow(MSStructures.LAND_GATE), new LandGatePlacement()));
		context.register(key("small_ruin"), new StructureSet(structures.getOrThrow(MSStructures.SMALL_RUIN), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643)));
		context.register(key("imp_dungeon"), new StructureSet(structures.getOrThrow(MSStructures.IMP_DUNGEON), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185)));
		context.register(key("consort_village"), new StructureSet(structures.getOrThrow(MSStructures.CONSORT_VILLAGE), new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312)));
		
		// Skaia
		context.register(key("skaia_castle"), new StructureSet(structures.getOrThrow(MSStructures.SKAIA_CASTLE), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
	
	private static ResourceKey<StructureSet> key(String path)
	{
		return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(Minestuck.MOD_ID, path));
	}
}
