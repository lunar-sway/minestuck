package com.mraof.minestuck.util;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSSoundEvents
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Minestuck.MOD_ID);
	
	public static final RegistryObject<SoundEvent> MUSIC_DISC_EMISSARY_OF_DANCE = REGISTER.register("music_disc_emissary_of_dance", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music_disc.emissary_of_dance")));
	public static final RegistryObject<SoundEvent> MUSIC_DISC_DANCE_STAB_DANCE = REGISTER.register("music_disc_dance_stab_dance", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music_disc.dance_stab_dance")));
	public static final RegistryObject<SoundEvent> MUSIC_DISC_RETRO_BATTLE_THEME = REGISTER.register("music_disc_retro_battle_theme", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music_disc.retro_battle_theme")));
	
	public static final RegistryObject<SoundEvent> MUSIC_DEFAULT = REGISTER.register("music_default", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.default")));
	public static final RegistryObject<SoundEvent> MUSIC_FOREST = REGISTER.register("music_forest", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.forest")));
	public static final RegistryObject<SoundEvent> MUSIC_TAIGA = REGISTER.register("music_taiga", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.taiga")));
	public static final RegistryObject<SoundEvent> MUSIC_FROST = REGISTER.register("music_frost", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.frost")));
	public static final RegistryObject<SoundEvent> MUSIC_FUNGI = REGISTER.register("music_fungi", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.fungi")));
	public static final RegistryObject<SoundEvent> MUSIC_HEAT = REGISTER.register("music_heat", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.heat")));
	public static final RegistryObject<SoundEvent> MUSIC_ROCK = REGISTER.register("music_rock", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rock")));
	public static final RegistryObject<SoundEvent> MUSIC_PETRIFICATION = REGISTER.register("music_petrification", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.petrification")));
	public static final RegistryObject<SoundEvent> MUSIC_SAND = REGISTER.register("music_sand", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.sand")));
	public static final RegistryObject<SoundEvent> MUSIC_LUSH_DESERTS = REGISTER.register("music_lush_deserts", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.lush_deserts")));
	public static final RegistryObject<SoundEvent> MUSIC_SANDSTONE = REGISTER.register("music_sandstone", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.sandstone")));
	public static final RegistryObject<SoundEvent> MUSIC_SHADE = REGISTER.register("music_shade", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.shade")));
	public static final RegistryObject<SoundEvent> MUSIC_WOOD = REGISTER.register("music_wood", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.wood")));
	public static final RegistryObject<SoundEvent> MUSIC_RAINBOW = REGISTER.register("music_rainbow", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rainbow")));
	public static final RegistryObject<SoundEvent> MUSIC_FLORA = REGISTER.register("music_flora", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.flora")));
	public static final RegistryObject<SoundEvent> MUSIC_END = REGISTER.register("music_end", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.end")));
	public static final RegistryObject<SoundEvent> MUSIC_RAIN = REGISTER.register("music_rain", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rain")));
	
	public static final RegistryObject<SoundEvent> MUSIC_FROGS = REGISTER.register("music_frogs", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.frogs")));
	public static final RegistryObject<SoundEvent> MUSIC_WIND = REGISTER.register("music_wind", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.wind")));
	public static final RegistryObject<SoundEvent> MUSIC_LIGHT = REGISTER.register("music_light", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.light")));
	public static final RegistryObject<SoundEvent> MUSIC_CLOCKWORK = REGISTER.register("music_clockwork", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.clockwork")));
	public static final RegistryObject<SoundEvent> MUSIC_SILENCE = REGISTER.register("music_silence", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.silence")));
	public static final RegistryObject<SoundEvent> MUSIC_THUNDER = REGISTER.register("music_thunder", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.thunder")));
	public static final RegistryObject<SoundEvent> MUSIC_PULSE = REGISTER.register("music_pulse", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.pulse")));
	public static final RegistryObject<SoundEvent> MUSIC_THOUGHT = REGISTER.register("music_thought", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.thought")));
	public static final RegistryObject<SoundEvent> MUSIC_BUCKETS = REGISTER.register("music_buckets", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.buckets")));
	public static final RegistryObject<SoundEvent> MUSIC_CAKE = REGISTER.register("music_cake", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.cake")));
	public static final RegistryObject<SoundEvent> MUSIC_RABBITS = REGISTER.register("music_rabbits", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rabbits")));
	public static final RegistryObject<SoundEvent> MUSIC_MONSTERS = REGISTER.register("music_monsters", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.monsters")));
	public static final RegistryObject<SoundEvent> MUSIC_UNDEAD = REGISTER.register("music_undead", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.undead")));
	public static final RegistryObject<SoundEvent> MUSIC_TOWERS = REGISTER.register("music_towers", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.towers")));
	
	public static final RegistryObject<SoundEvent> MUSIC_SICKEST_FIRES = REGISTER.register("music_sickest_fires", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.sickest_fires")));
	public static final RegistryObject<SoundEvent> MUSIC_RANCOROUS_GAMBLIGANT = REGISTER.register("music_rancorous_gambligant", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rancorous_gambligant")));
	public static final RegistryObject<SoundEvent> MUSIC_WHAT_GOES_UP = REGISTER.register("music_what_goes_up", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.what_goes_up")));
	public static final RegistryObject<SoundEvent> MUSIC_RISE_UP = REGISTER.register("music_rise_up", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "music.rise_up")));
	
	public static final RegistryObject<SoundEvent> ENTITY_NAKAGATOR_AMBIENT = REGISTER.register("entity_nakagator_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.nakagator.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_NAKAGATOR_HURT = REGISTER.register("entity_nakagator_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.nakagator.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_NAKAGATOR_DEATH = REGISTER.register("entity_nakagator_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.nakagator.death")));
	public static final RegistryObject<SoundEvent> ENTITY_SALAMANDER_AMBIENT = REGISTER.register("entity_salamander_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.salamander.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_SALAMANDER_HURT = REGISTER.register("entity_salamander_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.salamander.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_SALAMANDER_DEATH = REGISTER.register("entity_salamander_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.salamander.death")));
	public static final RegistryObject<SoundEvent> ENTITY_IGUANA_AMBIENT = REGISTER.register("entity_iguana_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.iguana.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_IGUANA_HURT = REGISTER.register("entity_iguana_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.iguana.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_IGUANA_DEATH = REGISTER.register("entity_iguana_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.iguana.death")));
	public static final RegistryObject<SoundEvent> ENTITY_TURTLE_HURT = REGISTER.register("entity_turtle_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.turtle.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_TURTLE_DEATH = REGISTER.register("entity_turtle_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.turtle.death")));
	public static final RegistryObject<SoundEvent> ENTITY_FROG_AMBIENT = REGISTER.register("entity_frog_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.frog.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_FROG_HURT = REGISTER.register("entity_frog_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.frog.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_FROG_DEATH = REGISTER.register("entity_frog_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.frog.death")));
	public static final RegistryObject<SoundEvent> ENTITY_IMP_AMBIENT = REGISTER.register("entity_imp_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.imp.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_IMP_HURT = REGISTER.register("entity_imp_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.imp.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_IMP_DEATH = REGISTER.register("entity_imp_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.imp.death")));
	public static final RegistryObject<SoundEvent> ENTITY_OGRE_AMBIENT = REGISTER.register("entity_ogre_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.ogre.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_OGRE_HURT = REGISTER.register("entity_ogre_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.ogre.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_OGRE_DEATH = REGISTER.register("entity_ogre_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.ogre.death")));
	public static final RegistryObject<SoundEvent> ENTITY_BASILISK_AMBIENT = REGISTER.register("entity_basilisk_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.basilisk.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_BASILISK_HURT = REGISTER.register("entity_basilisk_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.basilisk.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_BASILISK_DEATH = REGISTER.register("entity_basilisk_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.basilisk.death")));
	public static final RegistryObject<SoundEvent> ENTITY_LICH_AMBIENT = REGISTER.register("entity_lich_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.lich.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_LICH_HURT = REGISTER.register("entity_lich_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.lich.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_LICH_DEATH = REGISTER.register("entity_lich_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.lich.death")));
	public static final RegistryObject<SoundEvent> ENTITY_GICLOPS_AMBIENT = REGISTER.register("entity_giclops_ambient", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.giclops.ambient")));
	public static final RegistryObject<SoundEvent> ENTITY_GICLOPS_HURT = REGISTER.register("entity_giclops_hurt", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.giclops.hurt")));
	public static final RegistryObject<SoundEvent> ENTITY_GICLOPS_DEATH = REGISTER.register("entity_giclops_death", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "entity.giclops.death")));
	public static final RegistryObject<SoundEvent> ITEM_LONG_FORGOTTEN_WARHORN_USE = REGISTER.register("item_long_forgotten_warhorn_use", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.long_forgotten_warhorn.use")));
	public static final RegistryObject<SoundEvent> ITEM_GRIMOIRE_USE = REGISTER.register("item_grimoire_use", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.grimoire.use")));
	public static final RegistryObject<SoundEvent> ITEM_HORN_USE = REGISTER.register("item_horn_use", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.horn.use")));
	public static final RegistryObject<SoundEvent> ITEM_EEEEEEEEEEEE_HIT = REGISTER.register("item_eeeeeeeeeeee_hit", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.eeeeeeeeeeee.hit")));
	public static final RegistryObject<SoundEvent> ITEM_ELECTRIC_AUTOHARP_STROKE = REGISTER.register("item_electric_autoharp_stroke", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.electric_autoharp.stroke")));
	public static final RegistryObject<SoundEvent> ITEM_MAGIC_CAST = REGISTER.register("item_magic_cast", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.magic.cast"))); //based on evoker cast
	public static final RegistryObject<SoundEvent> ITEM_MAGIC_HIT = REGISTER.register("item_magic_hit", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.magic.hit"))); //identical to shulker bullet hit
	public static final RegistryObject<SoundEvent> ITEM_PROJECTILE_THROW = REGISTER.register("item_projectile_throw", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.projectile.throw"))); //identical to trident throw
	public static final RegistryObject<SoundEvent> ITEM_PROJECTILE_BOUNCE = REGISTER.register("item_projectile_bounce", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "item.projectile.bounce"))); //identical to shield block
	public static final RegistryObject<SoundEvent> EVENT_ECHELADDER_INCREASE = REGISTER.register("event_echeladder_increase", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "event.echeladder.increase")));
	public static final RegistryObject<SoundEvent> EVENT_ELECTRIC_SHOCK = REGISTER.register("event_electric_shock", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "event.electric_shock")));
	public static final RegistryObject<SoundEvent> EVENT_LOTUS_FLOWER_LOOT_SPAWN = REGISTER.register("event_lotus_flower_loot_spawn", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "event.lotus_flower.loot_spawn"))); //based on boat paddle land and item pop
	public static final RegistryObject<SoundEvent> EVENT_LOTUS_FLOWER_OPEN = REGISTER.register("event_lotus_flower_open", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "event.lotus_flower.open"))); //identical to composter ready
	public static final RegistryObject<SoundEvent> EVENT_LOTUS_FLOWER_RESTORE = REGISTER.register("event_lotus_flower_restore", () -> new SoundEvent(new ResourceLocation(Minestuck.MOD_ID, "event.lotus_flower.restore"))); //identical to beehive exit
}
