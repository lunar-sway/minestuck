package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public enum EnumCassetteType implements StringRepresentable
{
	NONE(() -> null, null),
	FIVE(() -> SoundEvents.MUSIC_DISC_5, onHitEffect(() -> new MobEffectInstance(MobEffects.DARKNESS, 500, 0),
			0.90F)),
	OTHERSIDE(() -> SoundEvents.MUSIC_DISC_OTHERSIDE, userEffect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1,
			false, false, false))),
	ELEVEN(() -> SoundEvents.MUSIC_DISC_11, onHitEffect(() -> new MobEffectInstance(MobEffects.WITHER, 160, 0),
			0.10F)),
	THIRTEEN(() -> SoundEvents.MUSIC_DISC_13, onHitEffect(() -> new MobEffectInstance(MobEffects.HUNGER, 500, 0), 0.30F)),
	BLOCKS(() -> SoundEvents.MUSIC_DISC_BLOCKS, onHitEffect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 100, 0),
			0.15F)),
	CAT(() -> SoundEvents.MUSIC_DISC_CAT, userEffect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0,
			false, false, false))),
	CHIRP(() -> SoundEvents.MUSIC_DISC_CHIRP, userEffect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0,
			false, false, false))),
	DANCE_STAB_DANCE(MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, onHitEffect(() -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 3),
			0.3F)),
	EMISSARY_OF_DANCE(MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, onHitEffect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0),
			0.1F)),
	FAR(() -> SoundEvents.MUSIC_DISC_FAR, userEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0,
			false, false, false))),
	MALL(() -> SoundEvents.MUSIC_DISC_MALL, userEffect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0,
			false, false, false))),
	MELLOHI(() -> SoundEvents.MUSIC_DISC_MELLOHI, onHitEffect(() -> new MobEffectInstance(MobEffects.LEVITATION, 60, 0),
			0.20F)),
	PIGSTEP(() -> SoundEvents.MUSIC_DISC_PIGSTEP, userEffect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0,
			false, false, false))),
	RETRO_BATTLE_THEME(MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, userEffect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 100, 2,
			false, false, false))),
	STAL(() -> SoundEvents.MUSIC_DISC_STAL, userEffect(() -> new MobEffectInstance(MobEffects.JUMP, 100, 1,
			false, false, false))),
	STRAD(() -> SoundEvents.MUSIC_DISC_STRAD, onHitEffect(() -> new MobEffectInstance(MobEffects.UNLUCK, 200, 0),
			0.10F)),
	WAIT(() -> SoundEvents.MUSIC_DISC_WAIT, onHitEffect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0),
			0.30F)),
	WARD(() -> SoundEvents.MUSIC_DISC_WARD, onHitEffect(() -> new MobEffectInstance(MobEffects.GLOWING, 750, 0), 1F));
	
	private final Supplier<SoundEvent> soundEvent;
	@Nullable
	private final EffectContainer effectContainer;
	
	EnumCassetteType(Supplier<SoundEvent> soundEvent, @Nullable EffectContainer effectContainer)
	{
		this.soundEvent = soundEvent;
		this.effectContainer = effectContainer;
	}
	
	@Override
	public @Nonnull String getSerializedName()
	{
		return name().toLowerCase();
	}
	
	public SoundEvent getSoundEvent()
	{
		return this.soundEvent.get();
	}
	
	@Nonnull
	public EffectContainer getEffectContainer()
	{
		return Objects.requireNonNull(effectContainer);
	}
	
	public record EffectContainer(Supplier<MobEffectInstance> effect, float applyingChance, boolean onHit) {}
	
	private static EffectContainer userEffect(Supplier<MobEffectInstance> effect)
	{
		return new EffectContainer(effect, 1F, false);
	}
	
	private static EffectContainer onHitEffect(Supplier<MobEffectInstance> effect, float chance)
	{
		return new EffectContainer(effect, chance, true);
	}
}
