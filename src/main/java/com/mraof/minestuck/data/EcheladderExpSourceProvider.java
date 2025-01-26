package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EcheladderExpSource;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EcheladderExpSourceProvider implements DataProvider
{
	private final PackOutput output;
	private final Map<String, EcheladderExpSource> expSources = new HashMap<>();
	
	public EcheladderExpSourceProvider(PackOutput output)
	{
		this.output = output;
	}
	
	protected void createNewSources()
	{
		add(EntityType.SILVERFISH, 1);
		add(EntityType.SLIME, 1);
		add(EntityType.MAGMA_CUBE, 1);
		add(EntityType.PHANTOM, 1);
		add(EntityType.VEX, 1);
		add(EntityTypeTags.ZOMBIES, 2);
		add(EntityTypeTags.SKELETONS, 2);
		add(EntityType.CREEPER, 2);
		add(EntityType.SPIDER, 2);
		add(EntityType.CAVE_SPIDER, 2);
		add(EntityType.PIGLIN, 2);
		add(EntityType.ZOMBIFIED_PIGLIN, 2);
		add(EntityTypeTags.ILLAGER, 3);
		add(EntityType.WITCH, 3);
		add(EntityType.ENDERMAN, 3);
		add(EntityType.BLAZE, 3);
		add(EntityType.BREEZE, 3);
		add(EntityType.SHULKER, 3);
		add(EntityType.GUARDIAN, 3);
		add(EntityType.GHAST, 3);
		add(EntityType.PIGLIN_BRUTE, 6);
		add(EntityType.HOGLIN, 6);
		add(EntityType.ZOGLIN, 6);
		add(EntityType.RAVAGER, 10);
		add(EntityType.ELDER_GUARDIAN, 20);
		add(EntityType.WARDEN, 200);
		add(EntityType.WITHER, 300);
		add(EntityType.ENDER_DRAGON, 400);
	}
	
	private void add(EntityType<?> entityType, int amount)
	{
		expSources.put("kill_" + entityType.builtInRegistryHolder().getKey().location().getPath(), new EcheladderExpSource.KillEntity(entityType, amount));
	}
	
	private void add(TagKey<EntityType<?>> entityTypeTag, int amount)
	{
		expSources.put("kill_" + entityTypeTag.location().getPath(), new EcheladderExpSource.KillEntityTag(entityTypeTag, amount));
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		createNewSources();
		List<CompletableFuture<?>> futures = new ArrayList<>(expSources.size());
		
		for(Map.Entry<String, EcheladderExpSource> source : expSources.entrySet())
		{
			Path path = getPath(source.getKey());
			JsonElement jsonData = EcheladderExpSource.CODEC.encodeStart(JsonOps.INSTANCE, source.getValue()).getOrThrow();
			futures.add(DataProvider.saveStable(cache, jsonData, path));
		}
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private Path getPath(String id)
	{
		return this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Minestuck.MOD_ID).resolve("minestuck/exp_source/" + id + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Echeladder Exp Sources";
	}
}
