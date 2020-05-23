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
	public static final SoundEvent MUSIC_DISC_TAVROS_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_ERIDAN_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_FEFERI_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_EQUIUS_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_KANAYA_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_KARKAT_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_GAMZEE_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_NEPETA_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_ARADIA_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_SOLLUX_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_TEREZI_THEME = getNull();
	public static final SoundEvent MUSIC_DISC_VRISKA_THEME = getNull();
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
				"music_disc.tavros_theme", "music_disc.eridan_theme", "music_disc.feferi_theme",
				"music_disc.equius_theme", "music_disc.kanaya_theme", "music_disc.karkat_theme",
				"music_disc.gamzee_theme", "music_disc.nepeta_theme", "music_disc.aradia_theme",
				"music_disc.sollux_theme", "music_disc.terezi_theme", "music_disc.vriska_theme",
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
				"item.eeeeeeeeeeee.hit", "event.echeladder.increase");
		
		for(String path : paths)
		{
			ResourceLocation location = new ResourceLocation(Minestuck.MOD_ID, path);
			ResourceLocation name = new ResourceLocation(location.toString().replace('.', '_'));
			event.getRegistry().register(new SoundEvent(location).setRegistryName(name));
		}
	}
}
