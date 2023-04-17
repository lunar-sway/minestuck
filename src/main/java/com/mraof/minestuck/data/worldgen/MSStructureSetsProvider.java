package com.mraof.minestuck.data.worldgen;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.structure.MSConfiguredStructures;
import com.mraof.minestuck.world.gen.structure.gate.LandGatePlacement;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class MSStructureSetsProvider implements DataProvider
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final DataGenerator generator;
	protected final ExistingFileHelper existingFileHelper;
	
	public MSStructureSetsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
	{
		this.generator = generator;
		this.existingFileHelper = existingFileHelper;
	}
	
	protected void generate(BiConsumer<ResourceKey<StructureSet>, StructureSet> consumer)
	{
		// Overworld
		consumer.accept(key("frog_temple"), new StructureSet(MSConfiguredStructures.FROG_TEMPLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(140, 92, RandomSpreadType.LINEAR, 41361201)));
		
		// Land
		consumer.accept(key("land_gate"), new StructureSet(MSConfiguredStructures.LAND_GATE.getHolder().orElseThrow(), new LandGatePlacement()));
		consumer.accept(key("small_ruin"), new StructureSet(MSConfiguredStructures.SMALL_RUIN.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 59273643)));
		consumer.accept(key("imp_dungeon"), new StructureSet(MSConfiguredStructures.IMP_DUNGEON.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(16, 4, RandomSpreadType.LINEAR, 34527185)));
		consumer.accept(key("consort_village"), new StructureSet(MSConfiguredStructures.CONSORT_VILLAGE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(24, 5, RandomSpreadType.LINEAR, 10387312)));
		
		// Skaia
		consumer.accept(key("skaia_castle"), new StructureSet(MSConfiguredStructures.SKAIA_CASTLE.getHolder().orElseThrow(), new RandomSpreadStructurePlacement(50, 40, RandomSpreadType.LINEAR, 6729346)));
	}
	
	protected ResourceKey<StructureSet> key(String name)
	{
		return ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, new ResourceLocation(Minestuck.MOD_ID, name));
	}
	
	@Override
	public void run(CachedOutput cache) throws IOException
	{
		Path path = this.generator.getOutputFolder();
		Set<ResourceLocation> writtenSets = Sets.newHashSet();
		DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, BuiltinRegistries.ACCESS);
		
		generate((key, set) -> {
			ResourceLocation location = key.location();
			if(!writtenSets.add(location))
				throw new IllegalStateException("Duplicate structure set " + location);
			else
			{
				existingFileHelper.trackGenerated(location, PackType.SERVER_DATA, ".json", "worldgen/structure_set");
				try
				{
					Optional<JsonElement> result = StructureSet.DIRECT_CODEC.encodeStart(ops, set)
							.resultOrPartial(message -> LOGGER.error("Couldn't serialize structure set {}: {}", location, message));
					if(result.isPresent())
					{
						Path biomePath = path.resolve("data/" + location.getNamespace() + "/worldgen/structure_set/" + location.getPath() + ".json");
						DataProvider.saveStable(cache, result.get(), biomePath);
					}
				} catch(IOException e)
				{
					LOGGER.error("Couldn't save structure set {}", location, e);
				}
			}
		});
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Structure Sets";
	}
}
