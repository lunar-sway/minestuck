package com.mraof.minestuck.block;

import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public enum EnumCassetteType implements StringRepresentable
{
	NONE(() -> null, null),
	ELEVEN(() -> SoundEvents.MUSIC_DISC_11, onHitEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0),
			0.10F)),
	THIRTEEN(() -> SoundEvents.MUSIC_DISC_13, onHitEffect(new MobEffectInstance(MobEffects.HUNGER, 50, 0),
			0.30F)),
	BLOCKS(() -> SoundEvents.MUSIC_DISC_BLOCKS, onHitEffect(new MobEffectInstance(MobEffects.CONFUSION, 50, 0),
			0.15F)),
	CAT(() -> SoundEvents.MUSIC_DISC_CAT, userEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100, 0,
			false, false, false))),
	CHIRP(() -> SoundEvents.MUSIC_DISC_CHIRP, userEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0,
			false, false, false))),
	DANCE_STAB_DANCE(() -> MSSoundEvents.MUSIC_DISC_DANCE_STAB_DANCE, userEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0,
			false, false, false))),
	EMISSARY_OF_DANCE(() -> MSSoundEvents.MUSIC_DISC_EMISSARY_OF_DANCE, onHitEffect(new MobEffectInstance(MobEffects.POISON, 400, 0),
			0.1F)),
	FAR(() -> SoundEvents.MUSIC_DISC_FAR, userEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0,
			false, false, false))),
	MALL(() -> SoundEvents.MUSIC_DISC_MALL, userEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0,
			false, false, false))),
	MELLOHI(() -> SoundEvents.MUSIC_DISC_MELLOHI, onHitEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0),
			0.20F)),
	PIGSTEP(() -> SoundEvents.MUSIC_DISC_PIGSTEP, userEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0,
			false, false, false))),
	RETRO_BATTLE_THEME(() -> MSSoundEvents.MUSIC_DISC_RETRO_BATTLE_THEME, userEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0,
			false, false, false))),
	STAL(() -> SoundEvents.MUSIC_DISC_STAL, userEffect(new MobEffectInstance(MobEffects.JUMP, 100, 1,
			false, false, false))),
	STRAD(() -> SoundEvents.MUSIC_DISC_STRAD, onHitEffect(new MobEffectInstance(MobEffects.UNLUCK, 200, 0),
			0.10F)),
	WAIT(() -> SoundEvents.MUSIC_DISC_WAIT, onHitEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0),
			0.30F)),
	WARD(() -> SoundEvents.MUSIC_DISC_WARD, onHitEffect(new MobEffectInstance(MobEffects.GLOWING, 750, 0), 1F));
	
	private final Supplier<SoundEvent> soundEvent;
	@Nullable
	private final EffectContainer effectContainer;
	
	EnumCassetteType(Supplier<SoundEvent> soundEvent, EffectContainer effectContainer)
	{
		this.soundEvent = soundEvent;
		this.effectContainer = effectContainer;
	}
	
	@Override
	public @NotNull String getSerializedName()
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
	public record EffectContainer(MobEffectInstance effect, float applyingChance, boolean onHit)
	{
	}
	private static EffectContainer userEffect(MobEffectInstance effect){
		return new EffectContainer(effect, 1F, false);
	}
	private static EffectContainer onHitEffect(MobEffectInstance effect, float chance){
		return new EffectContainer(effect, chance, true);
	}
}
