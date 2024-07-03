package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MSSoundEvents
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Minestuck.MOD_ID);
	
	public static final Supplier<SoundEvent> MUSIC_DISC_EMISSARY_OF_DANCE = register("music_disc.emissary_of_dance");
	public static final Supplier<SoundEvent> MUSIC_DISC_DANCE_STAB_DANCE = register("music_disc.dance_stab_dance");
	public static final Supplier<SoundEvent> MUSIC_DISC_RETRO_BATTLE_THEME = register("music_disc.retro_battle_theme");
	
	public static final Supplier<SoundEvent> MUSIC_DEFAULT = register("music.default");
	public static final Supplier<SoundEvent> MUSIC_FOREST = register("music.forest");
	public static final Supplier<SoundEvent> MUSIC_TAIGA = register("music.taiga");
	public static final Supplier<SoundEvent> MUSIC_FROST = register("music.frost");
	public static final Supplier<SoundEvent> MUSIC_FUNGI = register("music.fungi");
	public static final Supplier<SoundEvent> MUSIC_HEAT = register("music.heat");
	public static final Supplier<SoundEvent> MUSIC_ROCK = register("music.rock");
	public static final Supplier<SoundEvent> MUSIC_PETRIFICATION = register("music.petrification");
	public static final Supplier<SoundEvent> MUSIC_SAND = register("music.sand");
	public static final Supplier<SoundEvent> MUSIC_LUSH_DESERTS = register("music.lush_deserts");
	public static final Supplier<SoundEvent> MUSIC_SANDSTONE = register("music.sandstone");
	public static final Supplier<SoundEvent> MUSIC_SHADE = register("music.shade");
	public static final Supplier<SoundEvent> MUSIC_WOOD = register("music.wood");
	public static final Supplier<SoundEvent> MUSIC_RAINBOW = register("music.rainbow");
	public static final Supplier<SoundEvent> MUSIC_FLORA = register("music.flora");
	public static final Supplier<SoundEvent> MUSIC_END = register("music.end");
	public static final Supplier<SoundEvent> MUSIC_RAIN = register("music.rain");
	
	public static final Supplier<SoundEvent> MUSIC_FROGS = register("music.frogs");
	public static final Supplier<SoundEvent> MUSIC_WIND = register("music.wind");
	public static final Supplier<SoundEvent> MUSIC_LIGHT = register("music.light");
	public static final Supplier<SoundEvent> MUSIC_CLOCKWORK = register("music.clockwork");
	public static final Supplier<SoundEvent> MUSIC_SILENCE = register("music.silence");
	public static final Supplier<SoundEvent> MUSIC_THUNDER = register("music.thunder");
	public static final Supplier<SoundEvent> MUSIC_PULSE = register("music.pulse");
	public static final Supplier<SoundEvent> MUSIC_THOUGHT = register("music.thought");
	public static final Supplier<SoundEvent> MUSIC_BUCKETS = register("music.buckets");
	public static final Supplier<SoundEvent> MUSIC_CAKE = register("music.cake");
	public static final Supplier<SoundEvent> MUSIC_RABBITS = register("music.rabbits");
	public static final Supplier<SoundEvent> MUSIC_MONSTERS = register("music.monsters");
	public static final Supplier<SoundEvent> MUSIC_UNDEAD = register("music.undead");
	public static final Supplier<SoundEvent> MUSIC_TOWERS = register("music.towers");
	
	public static final Supplier<SoundEvent> MUSIC_UNIVERSAL = register("music.universal"); //play in all lands
	public static final Supplier<SoundEvent> MUSIC_RISE_UP = register("music.rise_up"); //is intended for use during godtiering
	
	public static final Supplier<SoundEvent> ENTITY_SWOOSH = register("entity.swoosh");
	public static final Supplier<SoundEvent> ENTITY_SLAM = register("entity.slam");
	public static final Supplier<SoundEvent> ENTITY_BITE = register("entity.bite");
	public static final Supplier<SoundEvent> ENTITY_NAKAGATOR_AMBIENT = register("entity.nakagator.ambient");
	public static final Supplier<SoundEvent> ENTITY_NAKAGATOR_HURT = register("entity.nakagator.hurt");
	public static final Supplier<SoundEvent> ENTITY_NAKAGATOR_DEATH = register("entity.nakagator.death");
	public static final Supplier<SoundEvent> ENTITY_SALAMANDER_AMBIENT = register("entity.salamander.ambient");
	public static final Supplier<SoundEvent> ENTITY_SALAMANDER_HURT = register("entity.salamander.hurt");
	public static final Supplier<SoundEvent> ENTITY_SALAMANDER_DEATH = register("entity.salamander.death");
	public static final Supplier<SoundEvent> ENTITY_IGUANA_AMBIENT = register("entity.iguana.ambient");
	public static final Supplier<SoundEvent> ENTITY_IGUANA_HURT = register("entity.iguana.hurt");
	public static final Supplier<SoundEvent> ENTITY_IGUANA_DEATH = register("entity.iguana.death");
	public static final Supplier<SoundEvent> ENTITY_TURTLE_HURT = register("entity.turtle.hurt");
	public static final Supplier<SoundEvent> ENTITY_TURTLE_DEATH = register("entity.turtle.death");
	public static final Supplier<SoundEvent> ENTITY_FROG_AMBIENT = register("entity.frog.ambient");
	public static final Supplier<SoundEvent> ENTITY_FROG_HURT = register("entity.frog.hurt");
	public static final Supplier<SoundEvent> ENTITY_FROG_DEATH = register("entity.frog.death");
	public static final Supplier<SoundEvent> ENTITY_IMP_AMBIENT = register("entity.imp.ambient");
	public static final Supplier<SoundEvent> ENTITY_IMP_HURT = register("entity.imp.hurt");
	public static final Supplier<SoundEvent> ENTITY_IMP_DEATH = register("entity.imp.death");
	public static final Supplier<SoundEvent> ENTITY_OGRE_AMBIENT = register("entity.ogre.ambient");
	public static final Supplier<SoundEvent> ENTITY_OGRE_HURT = register("entity.ogre.hurt");
	public static final Supplier<SoundEvent> ENTITY_OGRE_DEATH = register("entity.ogre.death");
	public static final Supplier<SoundEvent> ENTITY_BASILISK_AMBIENT = register("entity.basilisk.ambient");
	public static final Supplier<SoundEvent> ENTITY_BASILISK_HURT = register("entity.basilisk.hurt");
	public static final Supplier<SoundEvent> ENTITY_BASILISK_DEATH = register("entity.basilisk.death");
	public static final Supplier<SoundEvent> ENTITY_LICH_AMBIENT = register("entity.lich.ambient");
	public static final Supplier<SoundEvent> ENTITY_LICH_HURT = register("entity.lich.hurt");
	public static final Supplier<SoundEvent> ENTITY_LICH_DEATH = register("entity.lich.death");
	public static final Supplier<SoundEvent> ENTITY_GICLOPS_AMBIENT = register("entity.giclops.ambient");
	public static final Supplier<SoundEvent> ENTITY_GICLOPS_HURT = register("entity.giclops.hurt");
	public static final Supplier<SoundEvent> ENTITY_GICLOPS_DEATH = register("entity.giclops.death");
	public static final Supplier<SoundEvent> ITEM_LONG_FORGOTTEN_WARHORN_USE = register("item.long_forgotten_warhorn.use");
	public static final Supplier<SoundEvent> ITEM_GRIMOIRE_USE = register("item.grimoire.use");
	public static final Supplier<SoundEvent> ITEM_HORN_USE = register("item.horn.use");
	public static final Supplier<SoundEvent> ITEM_EEEEEEEEEEEE_HIT = register("item.eeeeeeeeeeee.hit");
	public static final Supplier<SoundEvent> ITEM_ELECTRIC_AUTOHARP_STROKE = register("item.electric_autoharp.stroke");
	public static final Supplier<SoundEvent> ITEM_MAGIC_CAST = register("item.magic.cast"); //based on evoker cast
	public static final Supplier<SoundEvent> ITEM_MAGIC_HIT = register("item.magic.hit"); //identical to shulker bullet hit
	public static final Supplier<SoundEvent> ITEM_PROJECTILE_THROW = register("item.projectile.throw"); //identical to trident throw
	public static final Supplier<SoundEvent> ITEM_PROJECTILE_BOUNCE = register("item.projectile.bounce"); //identical to shield block
	public static final Supplier<SoundEvent> ITEM_BATON_ORCHESTRA = register("item.baton.orchestra");
	public static final Supplier<SoundEvent> ITEM_JETPACK_FLIGHT = register("item.jetpack.flight");
	public static final Supplier<SoundEvent> EVENT_ECHELADDER_INCREASE = register("event.echeladder.increase");
	public static final Supplier<SoundEvent> EVENT_ELECTRIC_SHOCK = register("event.electric_shock");
	public static final Supplier<SoundEvent> EVENT_LOTUS_FLOWER_LOOT_SPAWN = register("event.lotus_flower.loot_spawn"); //based on boat paddle land and item pop
	public static final Supplier<SoundEvent> EVENT_LOTUS_FLOWER_OPEN = register("event.lotus_flower.open"); //identical to composter ready
	public static final Supplier<SoundEvent> EVENT_LOTUS_FLOWER_RESTORE = register("event.lotus_flower.restore"); //identical to beehive exit
	public static final Supplier<SoundEvent> EVENT_EDIT_TOOL_REVISE = register("event.edit_tools.revise");
	public static final Supplier<SoundEvent> EVENT_EDIT_TOOL_RECYCLE = register("event.edit_tools.recycle");
	public static final Supplier<SoundEvent> CRUXTRUDER_DOWEL = register("block.cruxtruder.dowel");
	public static final Supplier<SoundEvent> ALCHEMITER_RESONATE = register("block.alchemiter.resonate");
	public static final Supplier<SoundEvent> TRANSPORTALIZER_TELEPORT = register("block.transportalizer.teleport");
	public static final Supplier<SoundEvent> TOTEM_LATHE_LATHE = register("block.totem_lathe.lathe");
	public static final Supplier<SoundEvent> INTELLIBEAM_LASERSTATION_EXP_GATHER = register("block.intellibeam_laserstation.exp_gather");
	public static final Supplier<SoundEvent> INTELLIBEAM_LASERSTATION_REMOVE_CARD = register("block.intellibeam_laserstation.remove_card");
	public static final Supplier<SoundEvent> BLOCK_HORSE_CLOCK_CHIME = register("block.horse_clock.chime");
	public static final Supplier<SoundEvent> BLOCK_CLOCK_TICK = register("block.clock.tick");
	public static final Supplier<SoundEvent> BLOCK_CLOCK_TOCK = register("block.clock.tock");
	
	
	private static Supplier<SoundEvent> register(String name)
	{
		return REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(Minestuck.id(name)));
	}
}
