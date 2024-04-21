package com.mraof.minestuck.data.worldgen;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.ProspitStructure;
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
		
		// Skaia
		context.register(key("skaia_castle"), new StructureSet(structures.getOrThrow(MSStructures.SKAIA_CASTLE), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
		
		context.register(key("prospit_terrain"), new StructureSet(structures.getOrThrow(ProspitStructure.STRUCTURE), new ProspitStructure.FixedPlacement()));
	}
	
	private static ResourceKey<StructureSet> key(String path)
	{
		return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(Minestuck.MOD_ID, path));
	}
}
