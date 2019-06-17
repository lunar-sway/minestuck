package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EntityBlackBishop;
import com.mraof.minestuck.entity.carapacian.EntityBlackPawn;
import com.mraof.minestuck.entity.carapacian.EntityBlackRook;
import com.mraof.minestuck.entity.carapacian.EntityWhiteBishop;
import com.mraof.minestuck.entity.carapacian.EntityWhitePawn;
import com.mraof.minestuck.entity.carapacian.EntityWhiteRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.consort.EntityTurtle;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.entity.item.EntityShopPoster;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityLich;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityWyrm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModEntityTypes
{
	public static final EntityType<EntityFrog> FROG = new EntityType<>(EntityFrog.class, EntityFrog::new, true, true, null);
	public static final EntityType<EntitySalamander> SALAMANDER = new EntityType<>(EntitySalamander.class, EntitySalamander::new, true, true, null);
	public static final EntityType<EntityTurtle> TURTLE = new EntityType<>(EntityTurtle.class, EntityTurtle::new, true, true, null);
	public static final EntityType<EntityNakagator> NAKAGATOR = new EntityType<>(EntityNakagator.class, EntityNakagator::new, true, true, null);
	public static final EntityType<EntityIguana> IGUANA = new EntityType<>(EntityIguana.class, EntityIguana::new, true, true, null);
	
	public static final EntityType<EntityGrist> GRIST = new EntityType<>(EntityGrist.class, EntityGrist::new, true, true, null, true, null, false, 512, 1, true);
	public static final EntityType<EntityCrewPoster> CREW_POSTER = new EntityType<>(EntityCrewPoster.class, EntityCrewPoster::new, true, true, null);
	public static final EntityType<EntitySbahjPoster> SBAHJ_POSTER = new EntityType<>(EntitySbahjPoster.class, EntitySbahjPoster::new, true, true, null);
	public static int currentEntityIdOffset = 0;

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
	{
		event.getRegistry().register(FROG.setRegistryName("frog"));
		event.getRegistry().register(SALAMANDER.setRegistryName("salamander"));
		event.getRegistry().register(TURTLE.setRegistryName("turtle"));
		event.getRegistry().register(NAKAGATOR.setRegistryName("nakagator"));
		event.getRegistry().register(IGUANA.setRegistryName("iguana"));
		
		event.getRegistry().register(GRIST.setRegistryName("grist"));
		event.getRegistry().register(CREW_POSTER.setRegistryName("midnight_crew_poster"));
		event.getRegistry().register(SBAHJ_POSTER.setRegistryName("sbahj_poster"));
		
		registerEntity(EntityImp.class, "imp");
		registerEntity(EntityOgre.class, "ogre");
		registerEntity(EntityBasilisk.class, "basilisk");
		registerEntity(EntityLich.class, "lich");
		registerEntity(EntityGiclops.class, "giclops");
		registerEntity(EntityWyrm.class, "wyrm");
		registerEntity(EntityBlackPawn.class, "dersitePawn", "dersite_pawn");
		registerEntity(EntityWhitePawn.class, "prospitianPawn", "prospitian_pawn");
		registerEntity(EntityBlackBishop.class, "dersiteBishop", "dersite_bishop");
		registerEntity(EntityWhiteBishop.class, "prospitianBishop", "prospitian_bishop");
		registerEntity(EntityBlackRook.class, "dersiteRook", "dersite_rook");
		registerEntity(EntityWhiteRook.class, "prospitianRook", "prospitian_rook");
		registerEntity(EntityDecoy.class, "playerDecoy",  "player_decoy");
		registerEntity(EntityMetalBoat.class, "metalBoat", "metal_boat");
		registerEntity(EntityVitalityGel.class, "vitalityGel", "vitality_gel", 512, 1, true);
		registerEntity(EntityShopPoster.class, "shopPoster", "shop_poster");
		registerEntity(EntityHologram.class, "holoItem", "holo_item");
		
	}
	
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name)
	{
		registerEntity(entityClass, name, name, 80, 3, true);
	}
	
	public static void registerEntity(Class<? extends Entity> entityClass, String name, int eggPrimary, int eggSecondary)
	{
		registerEntity(entityClass, name, name, 80, 3, true, eggPrimary, eggSecondary);
	}
	
	//registers entity with forge and minecraft, and increases currentEntityIdOffset by one in order to prevent id collision
	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName)
	{
		registerEntity(entityClass, name, registryName, 80, 3, true);
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("minestuck", registryName), entityClass, "minestuck." + name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		currentEntityIdOffset++;
	}

	public static void registerEntity(Class<? extends Entity> entityClass, String name, String registryName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("minestuck", registryName), entityClass, "minestuck." + name, currentEntityIdOffset, Minestuck.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
		currentEntityIdOffset++;
	}
	
}
