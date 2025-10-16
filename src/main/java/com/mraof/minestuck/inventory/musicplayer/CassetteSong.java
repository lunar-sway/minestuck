package com.mraof.minestuck.inventory.musicplayer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Represents a cassette song's file, similarly to a jukebox song
 */
public record CassetteSong(ResourceKey<JukeboxSong> jukeboxSong, EffectContainer effectContainer)
{
	public static final Codec<CassetteSong> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ResourceKey.codec(Registries.JUKEBOX_SONG).fieldOf("jukebox_song").forGetter(CassetteSong::jukeboxSong),
			EffectContainer.CODEC.fieldOf("effect").forGetter(CassetteSong::effectContainer)
	).apply(builder, CassetteSong::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, CassetteSong> STREAM_CODEC = StreamCodec.composite(
			ResourceKey.streamCodec(Registries.JUKEBOX_SONG), CassetteSong::jukeboxSong,
			EffectContainer.STREAM_CODEC, CassetteSong::effectContainer,
			CassetteSong::new
	);
	
	@Nullable
	public Holder<SoundEvent> getSoundEvent(Level level)
	{
		Registry<JukeboxSong> jukeboxRegistry = level.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG);
		if(this.jukeboxSong == null)
			return null;
		JukeboxSong jukeboxSongObject = jukeboxRegistry.get(this.jukeboxSong);
		if(jukeboxSongObject == null)
			return null;
		return jukeboxSongObject.soundEvent();
	}
	
	@Nullable
	public ResourceKey<JukeboxSong> getJukeboxSong()
	{
		return this.jukeboxSong;
	}
	
	@Nonnull
	public EffectContainer getEffectContainer()
	{
		return Objects.requireNonNull(effectContainer);
	}
	
	public int getComparatorValue(Level level)
	{
		Registry<JukeboxSong> jukeboxRegistry = level.registryAccess().registryOrThrow(Registries.JUKEBOX_SONG);
		if(this.jukeboxSong == null)
			return 0;
		JukeboxSong jukeboxSongObject = jukeboxRegistry.get(this.jukeboxSong);
		if(jukeboxSongObject == null)
			return 0;
		return jukeboxSongObject.comparatorOutput();
	}
	
	public record EffectContainer(MobEffectInstance effect, float applyingChance, boolean onHit)
	{
		public static final Codec<EffectContainer> CODEC = RecordCodecBuilder.create(builder -> builder.group(
				MobEffectInstance.CODEC.fieldOf("effect").forGetter(EffectContainer::effect),
				Codec.FLOAT.fieldOf("applying_chance").forGetter(EffectContainer::applyingChance),
				Codec.BOOL.fieldOf("on_hit").forGetter(EffectContainer::onHit)
		).apply(builder, EffectContainer::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, EffectContainer> STREAM_CODEC = StreamCodec.composite(
				MobEffectInstance.STREAM_CODEC, EffectContainer::effect,
				ByteBufCodecs.FLOAT, EffectContainer::applyingChance,
				ByteBufCodecs.BOOL, EffectContainer::onHit,
				EffectContainer::new);
	}
}
