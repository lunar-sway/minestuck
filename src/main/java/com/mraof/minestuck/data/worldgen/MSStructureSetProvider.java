package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSStructures;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.function.BiConsumer;

public final class MSStructureSetProvider
{
	public static DataProvider create(RegistryAccess registryAccess, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		DataEntriesBuilder<StructureSet> sets = new DataEntriesBuilder<>();
		generate(registryAccess.ownedRegistryOrThrow(Registry.STRUCTURE_REGISTRY), sets.consumerForNamespace(Minestuck.MOD_ID));
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				RegistryOps.create(JsonOps.INSTANCE, registryAccess), Registry.STRUCTURE_SET_REGISTRY, sets.getMap());
	}
	
	private static void generate(Registry<Structure> structures, BiConsumer<String, StructureSet> consumer)
	{
		// Overworld
		consumer.accept("frog_temple", new StructureSet(structures.getHolderOrThrow(MSStructures.FROG_TEMPLE), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		consumer.accept("land_gate", new StructureSet(structures.getHolderOrThrow(MSStructures.LAND_GATE), new LandGatePlacement()));
		consumer.accept("small_ruin", new StructureSet(structures.getHolderOrThrow(MSStructures.SMALL_RUIN), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643)));
		consumer.accept("imp_dungeon", new StructureSet(structures.getHolderOrThrow(MSStructures.IMP_DUNGEON), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185)));
		consumer.accept("consort_village", new StructureSet(structures.getHolderOrThrow(MSStructures.CONSORT_VILLAGE), new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312)));
		
		// Skaia
		consumer.accept("skaia_castle", new StructureSet(structures.getHolderOrThrow(MSStructures.SKAIA_CASTLE), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
}
