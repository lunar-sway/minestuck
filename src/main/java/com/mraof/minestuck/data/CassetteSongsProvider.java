package com.mraof.minestuck.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong;
import com.mraof.minestuck.inventory.musicplayer.CassetteSong.EffectContainer;
import com.mraof.minestuck.item.MSCassetteSongs;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongs;

import javax.annotation.ParametersAreNonnullByDefault;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CassetteSongsProvider implements DataProvider
{
	private final Map<ResourceLocation, CassetteSong> songs = new HashMap<>();
	private final PackOutput output;
	private final String modid;
	
	public CassetteSongsProvider(PackOutput output, String modid)
	{
		this.output = output;
		this.modid = modid;
	}
	
	protected void registerSongs()
	{
		addHitEffect(JukeboxSongs.FIVE, .9F, new MobEffectInstance(MobEffects.DARKNESS, 500, 0), MSCassetteSongs.FIVE.song());
		addSelfEffect(JukeboxSongs.OTHERSIDE, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, false), MSCassetteSongs.OTHERSIDE.song());
		addHitEffect(JukeboxSongs.ELEVEN, .1F, new MobEffectInstance(MobEffects.WITHER, 160, 0), MSCassetteSongs.ELEVEN.song());
		addHitEffect(JukeboxSongs.THIRTEEN, .3F, new MobEffectInstance(MobEffects.HUNGER, 500, 0), MSCassetteSongs.THIRTEEN.song());
		addHitEffect(JukeboxSongs.BLOCKS, .15F, new MobEffectInstance(MobEffects.BLINDNESS, 100, 0), MSCassetteSongs.BLOCKS.song());
		addSelfEffect(JukeboxSongs.CAT, new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0, false, false, false), MSCassetteSongs.CAT.song());
		addSelfEffect(JukeboxSongs.CHIRP, new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0, false, false, false), MSCassetteSongs.CHIRP.song());
		addSelfEffect(JukeboxSongs.FAR, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, false, false), MSCassetteSongs.FAR.song());
		addSelfEffect(JukeboxSongs.MALL, new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, false, false, false), MSCassetteSongs.MALL.song());
		addHitEffect(JukeboxSongs.MELLOHI, .2F, new MobEffectInstance(MobEffects.LEVITATION, 60, 0), MSCassetteSongs.MELLOHI.song());
		addSelfEffect(JukeboxSongs.PIGSTEP, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, false, false, false), MSCassetteSongs.PIGSTEP.song());
		addSelfEffect(JukeboxSongs.STAL, new MobEffectInstance(MobEffects.JUMP, 100, 0, false, false, false), MSCassetteSongs.STAL.song());
		addHitEffect(JukeboxSongs.STRAD, .1F, new MobEffectInstance(MobEffects.UNLUCK, 200, 0), MSCassetteSongs.STRAD.song());
		addHitEffect(JukeboxSongs.WAIT, .3F, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0), MSCassetteSongs.WAIT.song());
		addHitEffect(JukeboxSongs.WARD, new MobEffectInstance(MobEffects.GLOWING, 750, 0), MSCassetteSongs.WARD.song());
		addHitEffect(JukeboxSongs.RELIC, .2F, new MobEffectInstance(MobEffects.WEAKNESS, 100), MSCassetteSongs.RELIC.song());
		addHitEffect(JukeboxSongs.PRECIPICE, .4F, new MobEffectInstance(MobEffects.GLOWING, 100), MSCassetteSongs.PRECIPICE.song());
		addSelfEffect(JukeboxSongs.CREATOR, new MobEffectInstance(MobEffects.ABSORPTION, 100, 0, false, false, false), MSCassetteSongs.CREATOR.song());
		addSelfEffect(JukeboxSongs.CREATOR_MUSIC_BOX, new MobEffectInstance(MobEffects.HEALTH_BOOST, 100, 0, false, false, false), MSCassetteSongs.CREATOR_MUSIC_BOX.song());
		
		addHitEffect(MSSoundEvents.JUKEBOX_SONG_DANCE_STAB_DANCE, .3F, new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3), MSCassetteSongs.DANCE_STAB_DANCE.song());
		addHitEffect(MSSoundEvents.JUKEBOX_SONG_EMISSARY_OF_DANCE, .1F, new MobEffectInstance(MobEffects.POISON, 200, 0), MSCassetteSongs.EMISSARY_OF_DANCE.song());
		addSelfEffect(MSSoundEvents.JUKEBOX_SONG_RETRO_BATTLE_THEME, new MobEffectInstance(MobEffects.DIG_SPEED, 100, 2, false, false, false), MSCassetteSongs.RETRO_BATTLE_THEME.song());
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, ResourceLocation name)
	{
		addHitEffect(song, 1, effect, name);
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, float chance, MobEffectInstance effect, ResourceLocation name)
	{
		add(new CassetteSong(song, new EffectContainer(effect, chance, true)), name);
	}
	
	protected void addSelfEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, ResourceLocation name)
	{
		add(new CassetteSong(song, new EffectContainer(effect, 1, false)), name);
	}
	
	protected void add(CassetteSong song, String name)
	{
		add(song, ResourceLocation.fromNamespaceAndPath(modid, name));
	}
	
	protected void add(CassetteSong song, ResourceLocation name)
	{
		songs.put(name, song);
	}
	
	@Override
	public CompletableFuture<?> run(CachedOutput cache)
	{
		registerSongs();
		
		Path outputPath = output.getOutputFolder();
		List<CompletableFuture<?>> futures = new ArrayList<>(songs.size());
		
		for(Map.Entry<ResourceLocation, CassetteSong> entry : songs.entrySet())
		{
			Path songPath = getPath(outputPath, entry.getKey());
			JsonElement jsonData = CassetteSong.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
			sortCures(jsonData);
			futures.add(DataProvider.saveStable(cache, jsonData, songPath));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/cassette_effects/" + id.getPath() + ".json");
	}
	
	/**
	 * neoforge:cures has no determined sorting, so the datachecker has a chance to fail because of it
	 * <p>
	 * This method sorts the cures to prevent the issue from rising
	 */
	private static void sortCures(JsonElement jsonData)
	{
		JsonObject effect = jsonData.getAsJsonObject().get("effect").getAsJsonObject();
		JsonObject subeffect = effect.get("effect").getAsJsonObject();
		JsonArray cures = subeffect.get("neoforge:cures").getAsJsonArray();
		List<String> values = new ArrayList<>();
		for(int i = cures.size() - 1; i >= 0; i--)
		{
			values.add(cures.get(i).getAsString());
			cures.remove(i);
		}
		Collections.sort(values);
		for(int i = 0; i < values.size(); i++)
		{
			cures.add(values.get(i));
		}
		subeffect.add("neoforge:cures", cures);
		effect.add("effect", subeffect);
	}
	
	@Override
	public String getName()
	{
		return "Cassette songs";
	}
}
