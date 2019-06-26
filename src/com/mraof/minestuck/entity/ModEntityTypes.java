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
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.entity.item.EntityShopPoster;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityTypes
{
	public static EntityType<EntityFrog> FROG = EntityType.Builder.create(EntityFrog.class, EntityFrog::new).build(Minestuck.MOD_ID+":frog");
	public static EntityType<EntitySalamander> SALAMANDER = EntityType.Builder.create(EntitySalamander.class, EntitySalamander::new).build(Minestuck.MOD_ID+":salamander");
	public static EntityType<EntityTurtle> TURTLE = EntityType.Builder.create(EntityTurtle.class, EntityTurtle::new).build(Minestuck.MOD_ID+":turtle");
	public static EntityType<EntityNakagator> NAKAGATOR = EntityType.Builder.create(EntityNakagator.class, EntityNakagator::new).build(Minestuck.MOD_ID+":nakagator");
	public static EntityType<EntityIguana> IGUANA = EntityType.Builder.create(EntityIguana.class, EntityIguana::new).build(Minestuck.MOD_ID+":iguana");
	
	public static EntityType<EntityImp> IMP = EntityType.Builder.create(EntityImp.class, EntityImp::new).build(Minestuck.MOD_ID+":imp");
	public static EntityType<EntityOgre> OGRE = EntityType.Builder.create(EntityOgre.class, EntityOgre::new).build(Minestuck.MOD_ID+":ogre");
	public static EntityType<EntityBasilisk> BASILISK = EntityType.Builder.create(EntityBasilisk.class, EntityBasilisk::new).build(Minestuck.MOD_ID+":basilisk");
	public static EntityType<EntityLich> LICH = EntityType.Builder.create(EntityLich.class, EntityLich::new).build(Minestuck.MOD_ID+":lich");
	public static EntityType<EntityGiclops> GICLOPS = EntityType.Builder.create(EntityGiclops.class, EntityGiclops::new).build(Minestuck.MOD_ID+":giclops");
	public static EntityType<EntityWyrm> WYRM = EntityType.Builder.create(EntityWyrm.class, EntityWyrm::new).build(Minestuck.MOD_ID+":wyrm");
	public static EntityType<EntityUnderlingPart> UNDERLING_PART = EntityType.Builder.createNothing(EntityUnderlingPart.class).disableSerialization().disableSummoning().build(Minestuck.MOD_ID+":underling_part");
	public static EntityType<EntityBigPart> BIG_PART = EntityType.Builder.createNothing(EntityBigPart.class).disableSerialization().disableSummoning().build(Minestuck.MOD_ID+":big_part");
	
	public static EntityType<EntityBlackPawn> DERSITE_PAWN = EntityType.Builder.create(EntityBlackPawn.class, EntityBlackPawn::new).build(Minestuck.MOD_ID+":dersite_pawn");
	public static EntityType<EntityWhitePawn> PROSPITIAN_PAWN = EntityType.Builder.create(EntityWhitePawn.class, EntityWhitePawn::new).build(Minestuck.MOD_ID+":prospitian_pawn");
	public static EntityType<EntityBlackBishop> DERSITE_BISHOP = EntityType.Builder.create(EntityBlackBishop.class, EntityBlackBishop::new).build(Minestuck.MOD_ID+":dersite_bishop");
	public static EntityType<EntityWhiteBishop> PROSPITIAN_BISHOP = EntityType.Builder.create(EntityWhiteBishop.class, EntityWhiteBishop::new).build(Minestuck.MOD_ID+":prospitian_bishop");
	public static EntityType<EntityBlackRook> DERSITE_ROOK = EntityType.Builder.create(EntityBlackRook.class, EntityBlackRook::new).build(Minestuck.MOD_ID+":dersite_rook");
	public static EntityType<EntityWhiteRook> PROSPITIAN_ROOK = EntityType.Builder.create(EntityWhiteRook.class, EntityWhiteRook::new).build(Minestuck.MOD_ID+":prospitian_rook");
	
	public static EntityType<EntityGrist> GRIST = EntityType.Builder.create(EntityGrist.class, EntityGrist::new).tracker(512, 1, true).build(Minestuck.MOD_ID+":grist");
	public static EntityType<EntityVitalityGel> VITALITY_GEL = EntityType.Builder.create(EntityVitalityGel.class, EntityVitalityGel::new).tracker(512, 1, true).build(Minestuck.MOD_ID+":vitality_gel");
	public static EntityType<EntityDecoy> DECOY = EntityType.Builder.createNothing(EntityDecoy.class).disableSerialization().disableSummoning().build(Minestuck.MOD_ID+":decoy");
	
	public static EntityType<EntityMetalBoat> METAL_BOAT = EntityType.Builder.create(EntityMetalBoat.class, EntityMetalBoat::new).build(Minestuck.MOD_ID+":metal_boat");
	public static EntityType<EntityCrewPoster> CREW_POSTER = EntityType.Builder.create(EntityCrewPoster.class, EntityCrewPoster::new).build(Minestuck.MOD_ID+":midnight_crew_poster");
	public static EntityType<EntitySbahjPoster> SBAHJ_POSTER = EntityType.Builder.create(EntitySbahjPoster.class, EntitySbahjPoster::new).build(Minestuck.MOD_ID+":sbahj_poster");
	public static EntityType<EntityShopPoster> SHOP_POSTER = EntityType.Builder.create(EntityShopPoster.class, EntityShopPoster::new).build(Minestuck.MOD_ID+":shop_poster");
	public static EntityType<EntityHologram> HOLOGRAM =EntityType.Builder.create(EntityHologram.class, EntityHologram::new).build(Minestuck.MOD_ID+":hologram");

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		registry.register(FROG.setRegistryName("frog"));
		registry.register(SALAMANDER.setRegistryName("salamander"));
		registry.register(TURTLE.setRegistryName("turtle"));
		registry.register(NAKAGATOR.setRegistryName("nakagator"));
		registry.register(IGUANA.setRegistryName("iguana"));
		
		registry.register(IMP.setRegistryName("imp"));
		registry.register(OGRE.setRegistryName("ogre"));
		registry.register(BASILISK.setRegistryName("basilisk"));
		registry.register(LICH.setRegistryName("lich"));
		registry.register(GICLOPS.setRegistryName("giclops"));
		registry.register(WYRM.setRegistryName("wyrm"));
		registry.register(UNDERLING_PART.setRegistryName("underling_part"));
		registry.register(BIG_PART.setRegistryName("big_part"));
		
		registry.register(DERSITE_PAWN.setRegistryName("dersite_pawn"));
		registry.register(PROSPITIAN_PAWN.setRegistryName("prospitian_pawn"));
		registry.register(DERSITE_BISHOP.setRegistryName("dersite_bishop"));
		registry.register(PROSPITIAN_BISHOP.setRegistryName("prospitian_bishop"));
		registry.register(DERSITE_ROOK.setRegistryName("dersite_rook"));
		registry.register(PROSPITIAN_ROOK.setRegistryName("prospitian_rook"));
		
		registry.register(GRIST.setRegistryName("grist"));
		registry.register(VITALITY_GEL.setRegistryName("vitality_gel"));
		registry.register(DECOY.setRegistryName("player_decoy"));
		
		registry.register(METAL_BOAT.setRegistryName("metal_boat"));
		registry.register(CREW_POSTER.setRegistryName("midnight_crew_poster"));
		registry.register(SBAHJ_POSTER.setRegistryName("sbahj_poster"));
		registry.register(SHOP_POSTER.setRegistryName("shop_poster"));
		registry.register(HOLOGRAM.setRegistryName("hologram"));
		
		EntitySpawnPlacementRegistry.register(DERSITE_PAWN, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_PAWN, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(DERSITE_BISHOP, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_BISHOP, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(DERSITE_ROOK, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_ROOK, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
	}
	
	private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, String name, EntityType.Builder<T> builder)
	{
		EntityType<T> type = builder.build(name);
		registry.register(type.setRegistryName(name));
		return type;
	}
}