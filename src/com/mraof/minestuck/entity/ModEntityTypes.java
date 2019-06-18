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
import com.mraof.minestuck.entity.underling.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public final class ModEntityTypes
{
	public static EntityType<EntityFrog> FROG;
	public static EntityType<EntitySalamander> SALAMANDER;
	public static EntityType<EntityTurtle> TURTLE;
	public static EntityType<EntityNakagator> NAKAGATOR;
	public static EntityType<EntityIguana> IGUANA;
	
	public static EntityType<EntityImp> IMP;
	public static EntityType<EntityOgre> OGRE;
	public static EntityType<EntityBasilisk> BASILISK;
	public static EntityType<EntityLich> LICH;
	public static EntityType<EntityGiclops> GICLOPS;
	public static EntityType<EntityWyrm> WYRM;
	public static EntityType<EntityUnderlingPart> UNDERLING_PART;
	public static EntityType<EntityBigPart> BIG_PART;
	
	public static EntityType<EntityBlackPawn> DERSITE_PAWN;
	public static EntityType<EntityWhitePawn> PROSPITIAN_PAWN;
	public static EntityType<EntityBlackBishop> DERSITE_BISHOP;
	public static EntityType<EntityWhiteBishop> PROSPITIAN_BISHOP;
	public static EntityType<EntityBlackRook> DERSITE_ROOK;
	public static EntityType<EntityWhiteRook> PROSPITIAN_ROOK;
	
	public static EntityType<EntityGrist> GRIST;
	public static EntityType<EntityVitalityGel> VITALITY_GEL;
	public static EntityType<EntityDecoy> DECOY;
	
	public static EntityType<EntityMetalBoat> METAL_BOAT;
	public static EntityType<EntityCrewPoster> CREW_POSTER;
	public static EntityType<EntitySbahjPoster> SBAHJ_POSTER;
	public static EntityType<EntityShopPoster> SHOP_POSTER;
	public static EntityType<EntityHologram> HOLOGRAM;

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		FROG = register(registry, "frog", EntityType.Builder.create(EntityFrog.class, EntityFrog::new));
		SALAMANDER = register(registry, "salamander", EntityType.Builder.create(EntitySalamander.class, EntitySalamander::new));
		TURTLE = register(registry, "turtle", EntityType.Builder.create(EntityTurtle.class, EntityTurtle::new));
		NAKAGATOR = register(registry, "nakagator", EntityType.Builder.create(EntityNakagator.class, EntityNakagator::new));
		IGUANA = register(registry, "iguana", EntityType.Builder.create(EntityIguana.class, EntityIguana::new));
		
		IMP = register(registry, "imp", EntityType.Builder.create(EntityImp.class, EntityImp::new));
		OGRE = register(registry, "ogre", EntityType.Builder.create(EntityOgre.class, EntityOgre::new));
		BASILISK = register(registry, "basilisk", EntityType.Builder.create(EntityBasilisk.class, EntityBasilisk::new));
		LICH = register(registry, "lich", EntityType.Builder.create(EntityLich.class, EntityLich::new));
		GICLOPS = register(registry, "giclops", EntityType.Builder.create(EntityGiclops.class, EntityGiclops::new));
		WYRM = register(registry, "wyrm", EntityType.Builder.create(EntityWyrm.class, EntityWyrm::new));
		UNDERLING_PART = register(registry, "underling_part", EntityType.Builder.createNothing(EntityUnderlingPart.class).disableSerialization().disableSummoning());
		BIG_PART = register(registry, "big_part", EntityType.Builder.createNothing(EntityBigPart.class).disableSerialization().disableSummoning());
		
		DERSITE_PAWN = register(registry, "dersite_pawn", EntityType.Builder.create(EntityBlackPawn.class, EntityBlackPawn::new));
		PROSPITIAN_PAWN = register(registry, "prospitian_pawn", EntityType.Builder.create(EntityWhitePawn.class, EntityWhitePawn::new));
		DERSITE_BISHOP = register(registry, "dersite_bishop", EntityType.Builder.create(EntityBlackBishop.class, EntityBlackBishop::new));
		PROSPITIAN_BISHOP = register(registry, "prospitian_bishop", EntityType.Builder.create(EntityWhiteBishop.class, EntityWhiteBishop::new));
		DERSITE_ROOK = register(registry, "dersite_rook", EntityType.Builder.create(EntityBlackRook.class, EntityBlackRook::new));
		PROSPITIAN_ROOK = register(registry, "prospitian_rook", EntityType.Builder.create(EntityWhiteRook.class, EntityWhiteRook::new));
		
		GRIST = register(registry, "grist", EntityType.Builder.create(EntityGrist.class, EntityGrist::new).tracker(512, 1, true));
		VITALITY_GEL = register(registry, "vitality_gel", EntityType.Builder.create(EntityVitalityGel.class, EntityVitalityGel::new).tracker(512, 1, true));
		DECOY = register(registry, "player_decoy", EntityType.Builder.createNothing(EntityDecoy.class).disableSerialization().disableSummoning());
		
		METAL_BOAT = register(registry, "metal_boat", EntityType.Builder.create(EntityMetalBoat.class, EntityMetalBoat::new));
		CREW_POSTER = register(registry, "midnight_crew_poster", EntityType.Builder.create(EntityCrewPoster.class, EntityCrewPoster::new));
		SBAHJ_POSTER = register(registry, "sbahj_poster", EntityType.Builder.create(EntitySbahjPoster.class, EntitySbahjPoster::new));
		SHOP_POSTER = register(registry, "shop_poster", EntityType.Builder.create(EntityShopPoster.class, EntityShopPoster::new));
		HOLOGRAM = register(registry, "hologram", EntityType.Builder.create(EntityHologram.class, EntityHologram::new));
		
	}
	
	private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, String name, EntityType.Builder<T> builder)
	{
		EntityType<T> type = builder.build(name);
		registry.register(type.setRegistryName(name));
		return type;
	}
}