package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
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
		addHitEffect(JukeboxSongs.FIVE, .9F, new MobEffectInstance(MobEffects.DARKNESS, 500, 0), MSCassetteSongs.FIVE);
		addSelfEffect(JukeboxSongs.OTHERSIDE, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1, false, false, false), MSCassetteSongs.OTHERSIDE);
		addHitEffect(JukeboxSongs.ELEVEN, .1F, new MobEffectInstance(MobEffects.WITHER, 160, 0), MSCassetteSongs.ELEVEN);
		addHitEffect(JukeboxSongs.THIRTEEN, .3F, new MobEffectInstance(MobEffects.HUNGER, 500, 0), MSCassetteSongs.THIRTEEN);
		addHitEffect(JukeboxSongs.BLOCKS, .15F, new MobEffectInstance(MobEffects.BLINDNESS, 100, 0), MSCassetteSongs.BLOCKS);
		addSelfEffect(JukeboxSongs.CAT, new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0, false, false, false), MSCassetteSongs.CAT);
		addSelfEffect(JukeboxSongs.CHIRP, new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0, false, false, false), MSCassetteSongs.CHIRP);
		addSelfEffect(JukeboxSongs.FAR, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, false, false), MSCassetteSongs.FAR);
		addSelfEffect(JukeboxSongs.MALL, new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, false, false, false), MSCassetteSongs.MALL);
		addHitEffect(JukeboxSongs.MELLOHI, .2F, new MobEffectInstance(MobEffects.LEVITATION, 60, 0), MSCassetteSongs.MELLOHI);
		addSelfEffect(JukeboxSongs.PIGSTEP, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, false, false, false), MSCassetteSongs.PIGSTEP);
		addSelfEffect(JukeboxSongs.STAL, new MobEffectInstance(MobEffects.JUMP, 100, 0, false, false, false), MSCassetteSongs.STAL);
		addHitEffect(JukeboxSongs.STRAD, .1F, new MobEffectInstance(MobEffects.UNLUCK, 200, 0), MSCassetteSongs.STRAD);
		addHitEffect(JukeboxSongs.WAIT, .3F, new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0), MSCassetteSongs.WAIT);
		addHitEffect(JukeboxSongs.WARD, new MobEffectInstance(MobEffects.GLOWING, 750, 0), MSCassetteSongs.WARD);
		
		addHitEffect(MSSoundEvents.JUKEBOX_SONG_DANCE_STAB_DANCE, .3F, new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3), MSCassetteSongs.DANCE_STAB_DANCE);
		addHitEffect(MSSoundEvents.JUKEBOX_SONG_EMISSARY_OF_DANCE, .1F, new MobEffectInstance(MobEffects.POISON, 200, 0), MSCassetteSongs.EMISSARY_OF_DANCE);
		addSelfEffect(MSSoundEvents.JUKEBOX_SONG_RETRO_BATTLE_THEME, new MobEffectInstance(MobEffects.DIG_SPEED, 100, 2, false, false, false), MSCassetteSongs.RETRO_BATTLE_THEME);
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, String name)
	{
		addHitEffect(song, 1, effect, name);
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, float chance, MobEffectInstance effect, String name)
	{
		add(song, chance, effect, true, name);
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, ResourceLocation name)
	{
		addHitEffect(song, 1, effect, name);
	}
	
	protected void addHitEffect(ResourceKey<JukeboxSong> song, float chance, MobEffectInstance effect, ResourceLocation name)
	{
		add(new CassetteSong(song, new EffectContainer(effect, chance, true)), name);
	}
	
	protected void addSelfEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, String name)
	{
		add(song, 1, effect, false, name);
	}
	
	protected void addSelfEffect(ResourceKey<JukeboxSong> song, MobEffectInstance effect, ResourceLocation name)
	{
		add(new CassetteSong(song, new EffectContainer(effect, 1, false)), name);
	}
	
	protected void add(ResourceKey<JukeboxSong> song, MobEffectInstance effect, boolean onHit, String name)
	{
		add(song, 1, effect, onHit, name);
	}
	
	protected void add(ResourceKey<JukeboxSong> song, float chance, MobEffectInstance effect, boolean onHit, String name)
	{
		add(new CassetteSong(song, new EffectContainer(effect, chance, onHit)), name);
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
			futures.add(DataProvider.saveStable(cache, jsonData, songPath));
		}
		
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	private static Path getPath(Path outputPath, ResourceLocation id)
	{
		return outputPath.resolve("data/" + id.getNamespace() + "/minestuck/cassette_songs/" + id.getPath() + ".json");
	}
	
	@Override
	public String getName()
	{
		return "Cassette songs";
	}
}
