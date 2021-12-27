package com.mraof.minestuck.util;

import com.google.common.collect.Lists;
import com.mraof.minestuck.Minestuck;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.List;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSSoundEvents
{
	public static final SoundEvent MUSIC_DISC_EMISSARY_OF_DANCE = getNull();
	public static final SoundEvent MUSIC_DISC_DANCE_STAB_DANCE = getNull();
	public static final SoundEvent MUSIC_DISC_RETRO_BATTLE_THEME = getNull();
	
	public static final SoundEvent MUSIC_DEFAULT = getNull();
	public static final SoundEvent MUSIC_FOREST = getNull();
	public static final SoundEvent MUSIC_TAIGA = getNull();
	public static final SoundEvent MUSIC_FROST = getNull();
	public static final SoundEvent MUSIC_FUNGI = getNull();
	public static final SoundEvent MUSIC_HEAT = getNull();
	public static final SoundEvent MUSIC_ROCK = getNull();
	public static final SoundEvent MUSIC_PETRIFICATION = getNull();
	public static final SoundEvent MUSIC_SAND = getNull();
	public static final SoundEvent MUSIC_LUSH_DESERTS = getNull();
	public static final SoundEvent MUSIC_SANDSTONE = getNull();
	public static final SoundEvent MUSIC_SHADE = getNull();
	public static final SoundEvent MUSIC_WOOD = getNull();
	public static final SoundEvent MUSIC_RAINBOW = getNull();
	public static final SoundEvent MUSIC_FLORA = getNull();
	public static final SoundEvent MUSIC_END = getNull();
	public static final SoundEvent MUSIC_RAIN = getNull();
	
	public static final SoundEvent MUSIC_FROGS = getNull();
	public static final SoundEvent MUSIC_WIND = getNull();
	public static final SoundEvent MUSIC_LIGHT = getNull();
	public static final SoundEvent MUSIC_CLOCKWORK = getNull();
	public static final SoundEvent MUSIC_SILENCE = getNull();
	public static final SoundEvent MUSIC_THUNDER = getNull();
	public static final SoundEvent MUSIC_PULSE = getNull();
	public static final SoundEvent MUSIC_THOUGHT = getNull();
	public static final SoundEvent MUSIC_BUCKETS = getNull();
	public static final SoundEvent MUSIC_CAKE = getNull();
	public static final SoundEvent MUSIC_RABBITS = getNull();
	public static final SoundEvent MUSIC_MONSTERS = getNull();
	public static final SoundEvent MUSIC_UNDEAD = getNull();
	public static final SoundEvent MUSIC_TOWERS = getNull();
	
	public static final SoundEvent ENTITY_NAKAGATOR_AMBIENT = getNull();
	public static final SoundEvent ENTITY_NAKAGATOR_HURT = getNull();
	public static final SoundEvent ENTITY_NAKAGATOR_DEATH = getNull();
	public static final SoundEvent ENTITY_SALAMANDER_AMBIENT = getNull();
	public static final SoundEvent ENTITY_SALAMANDER_HURT = getNull();
	public static final SoundEvent ENTITY_SALAMANDER_DEATH = getNull();
	public static final SoundEvent ENTITY_IGUANA_AMBIENT = getNull();
	public static final SoundEvent ENTITY_IGUANA_HURT = getNull();
	public static final SoundEvent ENTITY_IGUANA_DEATH = getNull();
	public static final SoundEvent ENTITY_TURTLE_HURT = getNull();
	public static final SoundEvent ENTITY_TURTLE_DEATH = getNull();
	public static final SoundEvent ENTITY_FROG_AMBIENT = getNull();
	public static final SoundEvent ENTITY_FROG_HURT = getNull();
	public static final SoundEvent ENTITY_FROG_DEATH = getNull();
	public static final SoundEvent ENTITY_IMP_AMBIENT = getNull();
	public static final SoundEvent ENTITY_IMP_HURT = getNull();
	public static final SoundEvent ENTITY_IMP_DEATH = getNull();
	public static final SoundEvent ENTITY_OGRE_AMBIENT = getNull();
	public static final SoundEvent ENTITY_OGRE_HURT = getNull();
	public static final SoundEvent ENTITY_OGRE_DEATH = getNull();
	public static final SoundEvent ENTITY_BASILISK_AMBIENT = getNull();
	public static final SoundEvent ENTITY_BASILISK_HURT = getNull();
	public static final SoundEvent ENTITY_BASILISK_DEATH = getNull();
	public static final SoundEvent ENTITY_LICH_AMBIENT = getNull();
	public static final SoundEvent ENTITY_LICH_HURT = getNull();
	public static final SoundEvent ENTITY_LICH_DEATH = getNull();
	public static final SoundEvent ENTITY_GICLOPS_AMBIENT = getNull();
	public static final SoundEvent ENTITY_GICLOPS_HURT = getNull();
	public static final SoundEvent ENTITY_GICLOPS_DEATH = getNull();
	public static final SoundEvent ITEM_LONG_FORGOTTEN_WARHORN_USE = getNull();
	public static final SoundEvent ITEM_GRIMOIRE_USE = getNull();
	public static final SoundEvent ITEM_HORN_USE = getNull();
	public static final SoundEvent ITEM_EEEEEEEEEEEE_HIT = getNull();
	public static final SoundEvent EVENT_ECHELADDER_INCREASE = getNull();
	public static final SoundEvent EVENT_ELECTRIC_SHOCK = getNull();
	public static final SoundEvent LOOT_BLOCK_OPEN = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_AMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_BMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_BBMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_CMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_DMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_EBMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_EMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_FMAJOR = getNull();
	public static final SoundEvent EVENT_ELECTRIC_AUTOHARP_STROKE_GMAJOR = getNull();
    
    @Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	public static void registerSound(final RegistryEvent.Register<SoundEvent> event)
	{
		List<String> paths = Lists.newArrayList(
				"music_disc.emissary_of_dance", "music_disc.dance_stab_dance", "music_disc.retro_battle_theme",
				"music.default",
				"music.forest", "music.taiga", "music.frost", "music.fungi",
				"music.heat", "music.rock", "music.petrification", "music.sand",
				"music.lush_deserts", "music.sandstone", "music.shade", "music.wood",
				"music.rainbow", "music.flora", "music.end", "music.rain",
				"music.frogs", "music.wind", "music.light", "music.clockwork", "music.silence", "music.thunder", "music.pulse",
				"music.thought", "music.buckets", "music.cake", "music.rabbits", "music.monsters", "music.undead", "music.towers",
				"entity.nakagator.ambient", "entity.nakagator.hurt", "entity.nakagator.death",
				"entity.iguana.ambient", "entity.iguana.hurt", "entity.iguana.death",
				"entity.salamander.ambient", "entity.salamander.hurt", "entity.salamander.death",
				"entity.turtle.hurt", "entity.turtle.death",
				"entity.frog.ambient", "entity.frog.hurt", "entity.frog.death",
				"entity.imp.ambient", "entity.imp.hurt", "entity.imp.death",
				"entity.ogre.ambient", "entity.ogre.hurt", "entity.ogre.death",
				"entity.basilisk.ambient", "entity.basilisk.hurt", "entity.basilisk.death",
				"entity.lich.ambient", "entity.lich.hurt", "entity.lich.death",
				"entity.giclops.ambient", "entity.giclops.hurt", "entity.giclops.death",
				"item.long_forgotten_warhorn.use", "item.grimoire.use",  "item.horn.use",
				"item.eeeeeeeeeeee.hit", "event.echeladder.increase", "event.electric_shock",
				"event.loot_block.open",
				"event.electric_autoharp.stroke.amajor","event.electric_autoharp.stroke.bmajor",
				"event.electric_autoharp.stroke.bbmajor", "event.electric_autoharp.stroke.cmajor",
				"event.electric_autoharp.stroke.dmajor", "event.electric_autoharp.stroke.ebmajor",
				"event.electric_autoharp.stroke.emajor","event.electric_autoharp.stroke.fmajor",
				"event.electric_autoharp.stroke.gmajor");
		
		for(String path : paths)
		{
			ResourceLocation location = new ResourceLocation(Minestuck.MOD_ID, path);
			ResourceLocation name = new ResourceLocation(location.toString().replace('.', '_'));
			event.getRegistry().register(new SoundEvent(location).setRegistryName(name));
		}
	}
}
