package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @deprecated Use the <code>minestuck:cassette_code</code> components
 */
@Deprecated
public enum EnumCassetteType implements StringRepresentable
{
	NONE(null, null),
	FIVE(JukeboxSongs.FIVE, onHitEffect(() -> new MobEffectInstance(MobEffects.DARKNESS, 500, 0),
			0.90F)),
	OTHERSIDE(JukeboxSongs.OTHERSIDE, userEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1,
			false, false, false))),
	ELEVEN(JukeboxSongs.ELEVEN, onHitEffect(() -> new MobEffectInstance(MobEffects.WITHER, 160, 0),
			0.10F)),
	THIRTEEN(JukeboxSongs.THIRTEEN, onHitEffect(() -> new MobEffectInstance(MobEffects.HUNGER, 500, 0), 0.30F)),
	BLOCKS(JukeboxSongs.BLOCKS, onHitEffect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 100, 0),
			0.15F)),
	CAT(JukeboxSongs.CAT, userEffect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0,
			false, false, false))),
	CHIRP(JukeboxSongs.CHIRP, userEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0,
			false, false, false))),
	DANCE_STAB_DANCE(MSSoundEvents.JUKEBOX_SONG_DANCE_STAB_DANCE, onHitEffect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3),
			0.3F)),
	EMISSARY_OF_DANCE(MSSoundEvents.JUKEBOX_SONG_EMISSARY_OF_DANCE, onHitEffect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0),
			0.1F)),
	FAR(JukeboxSongs.FAR, userEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0,
			false, false, false))),
	MALL(JukeboxSongs.MALL, userEffect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0,
			false, false, false))),
	MELLOHI(JukeboxSongs.MELLOHI, onHitEffect(() -> new MobEffectInstance(MobEffects.LEVITATION, 60, 0),
			0.20F)),
	PIGSTEP(JukeboxSongs.PIGSTEP, userEffect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0,
			false, false, false))),
	RETRO_BATTLE_THEME(MSSoundEvents.JUKEBOX_SONG_RETRO_BATTLE_THEME, userEffect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 100, 2,
			false, false, false))),
	STAL(JukeboxSongs.STAL, userEffect(() -> new MobEffectInstance(MobEffects.JUMP, 100, 1,
			false, false, false))),
	STRAD(JukeboxSongs.STRAD, onHitEffect(() -> new MobEffectInstance(MobEffects.UNLUCK, 200, 0),
			0.10F)),
	WAIT(JukeboxSongs.WAIT, onHitEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0),
			0.30F)),
	WARD(JukeboxSongs.WARD, onHitEffect(() -> new MobEffectInstance(MobEffects.GLOWING, 750, 0), 1F));
	
	@Nullable
	private final ResourceKey<JukeboxSong> jukeboxSong;
	@Nullable
	private final EffectContainer effectContainer;
	
	EnumCassetteType(@Nullable ResourceKey<JukeboxSong> jukeboxSong, @Nullable EffectContainer effectContainer)
	{
		this.jukeboxSong = jukeboxSong;
		this.effectContainer = effectContainer;
	}
	
	@Override
	public @Nonnull String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	@Nullable
	public ResourceKey<JukeboxSong> getJukeboxSong()
	{
		return this.jukeboxSong;
	}
	
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
	
	@Nonnull
	public EffectContainer getEffectContainer()
	{
		return Objects.requireNonNull(effectContainer);
	}
	
	public record EffectContainer(Supplier<MobEffectInstance> effect, float applyingChance, boolean onHit)
	{
	}
	
	private static EffectContainer userEffect(Supplier<MobEffectInstance> effect)
	{
		return new EffectContainer(effect, 1F, false);
	}
	
	private static EffectContainer onHitEffect(Supplier<MobEffectInstance> effect, float chance)
	{
		return new EffectContainer(effect, chance, true);
	}
}
