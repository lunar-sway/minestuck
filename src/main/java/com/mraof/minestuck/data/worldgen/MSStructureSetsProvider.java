package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSConfiguredStructures;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class MSStructureSetsProvider
{
	public static DataProvider create(DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		Map<ResourceLocation, StructureSet> sets = new HashMap<>();
		generate(BuiltinRegistries.STRUCTURES, (name, set) -> sets.put(new ResourceLocation(Minestuck.MOD_ID, name), set));
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				RegistryOps.create(JsonOps.INSTANCE, BuiltinRegistries.ACCESS), Registry.STRUCTURE_SET_REGISTRY, sets);
	}
	
	private static void generate(@SuppressWarnings("SameParameterValue") Registry<Structure> structures, BiConsumer<String, StructureSet> consumer)
	{
		// Overworld
		consumer.accept("frog_temple", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.FROG_TEMPLE), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		consumer.accept("land_gate", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.LAND_GATE), new LandGatePlacement()));
		consumer.accept("small_ruin", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.SMALL_RUIN), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643)));
		consumer.accept("imp_dungeon", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.IMP_DUNGEON), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185)));
		consumer.accept("consort_village", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.CONSORT_VILLAGE), new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312)));
		
		// Skaia
		consumer.accept("skaia_castle", new StructureSet(structures.getHolderOrThrow(MSConfiguredStructures.SKAIA_CASTLE), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
}
