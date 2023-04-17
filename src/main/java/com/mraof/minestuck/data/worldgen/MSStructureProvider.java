package com.mraof.minestuck.data.worldgen;

import com.mojang.serialization.JsonOps;
import com.mojang.serialization.Lifecycle;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.gen.structure.FrogTempleStructure;
import com.mraof.minestuck.world.gen.structure.ImpDungeonStructure;
import com.mraof.minestuck.world.gen.structure.SmallRuinStructure;
import com.mraof.minestuck.world.gen.structure.castle.CastleStructure;
import com.mraof.minestuck.world.gen.structure.gate.GateStructure;
import com.mraof.minestuck.world.gen.structure.village.ConsortVillageStructure;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.mraof.minestuck.world.gen.structure.MSStructures.*;

public final class MSStructureProvider
{
	public static DataProvider create(RegistryAccess.Writable registryAccess, DataGenerator generator, ExistingFileHelper existingFileHelper)
	{
		var structureRegistry = registryAccess.ownedWritableRegistryOrThrow(Registry.STRUCTURE_REGISTRY);
		Map<ResourceLocation, Structure> structures = new HashMap<>();
		generate(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY), (key, structure) -> {
			structures.put(key.location(), structure);
			structureRegistry.register(key, structure, Lifecycle.stable());
		});
		
		return JsonCodecProvider.forDatapackRegistry(generator, existingFileHelper, Minestuck.MOD_ID,
				RegistryOps.create(JsonOps.INSTANCE, registryAccess), Registry.STRUCTURE_REGISTRY, structures);
	}
	
	private static void generate(Registry<Biome> biomes, BiConsumer<ResourceKey<Structure>, Structure> consumer)
	{
		consumer.accept(FROG_TEMPLE, new FrogTempleStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_FROG_TEMPLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		consumer.accept(LAND_GATE, new GateStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_LAND_GATE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		consumer.accept(SMALL_RUIN, new SmallRuinStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_SMALL_RUIN), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		consumer.accept(IMP_DUNGEON, new ImpDungeonStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_IMP_DUNGEON), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		consumer.accept(CONSORT_VILLAGE, new ConsortVillageStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_CONSORT_VILLAGE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
		
		consumer.accept(SKAIA_CASTLE, new CastleStructure(new Structure.StructureSettings(biomes.getOrCreateTag(MSTags.Biomes.HAS_SKAIA_CASTLE), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
	}
}
