package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import net.minecraft.entity.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class MSEntityTypes
{
	public static EntityClassification UNDERLING = EntityClassification.create("UNDERLING", "underling", 35, false, false, 128);
	public static EntityClassification CONSORT = EntityClassification.create("CONSORT", "consort", 10, true, false, 128);
	
	public static final EntityType<FrogEntity> FROG = getNull();
	public static final EntityType<ConsortEntity> SALAMANDER = getNull();
	public static final EntityType<ConsortEntity> TURTLE = getNull();
	public static final EntityType<ConsortEntity> NAKAGATOR = getNull();
	public static final EntityType<ConsortEntity> IGUANA = getNull();
	
	public static final EntityType<ImpEntity> IMP = getNull();
	public static final EntityType<OgreEntity> OGRE = getNull();
	public static final EntityType<BasiliskEntity> BASILISK = getNull();
	public static final EntityType<LichEntity> LICH = getNull();
	public static final EntityType<GiclopsEntity> GICLOPS = getNull();
	public static final EntityType<WyrmEntity> WYRM = getNull();
	
	public static final EntityType<PawnEntity> DERSITE_PAWN = getNull();
	public static final EntityType<PawnEntity> PROSPITIAN_PAWN = getNull();
	public static final EntityType<BishopEntity> DERSITE_BISHOP = getNull();
	public static final EntityType<BishopEntity> PROSPITIAN_BISHOP = getNull();
	public static final EntityType<RookEntity> DERSITE_ROOK = getNull();
	public static final EntityType<RookEntity> PROSPITIAN_ROOK = getNull();
	
	public static final EntityType<GristEntity> GRIST = getNull();
	public static final EntityType<VitalityGelEntity> VITALITY_GEL = getNull();
	public static final EntityType<DecoyEntity> PLAYER_DECOY = getNull();
	
	public static final EntityType<MetalBoatEntity> METAL_BOAT = getNull();
	public static final EntityType<BarbasolBombEntity> BARBASOL_BOMB = getNull();
	public static final EntityType<CrewPosterEntity> MIDNIGHT_CREW_POSTER = getNull();
	public static final EntityType<SbahjPosterEntity> SBAHJ_POSTER = getNull();
	public static final EntityType<ShopPosterEntity> SHOP_POSTER = getNull();
	public static final EntityType<HologramEntity> HOLOGRAM = getNull();
	public static final EntityType<LotusFlowerEntity> LOTUS_FLOWER = getNull();
	
	public static final EntityType<ConsumableProjectileEntity> CONSUMABLE_PROJECTILE = getNull();
	public static final EntityType<ReturningProjectileEntity> RETURNING_PROJECTILE = getNull();
	public static final EntityType<BouncingProjectileEntity> BOUNCING_PROJECTILE = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	@SubscribeEvent
	@SuppressWarnings("unused")
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		register(registry, EntityType.Builder.<FrogEntity>of(FrogEntity::new, EntityClassification.CREATURE).sized(0.51F, 0.51F), "frog");
		register(registry, EntityType.Builder.of(EnumConsort.SALAMANDER::create, CONSORT).sized(0.52F, 1.2F), "salamander");
		register(registry, EntityType.Builder.of(EnumConsort.TURTLE::create, CONSORT).sized(0.52F, 1.3F), "turtle");
		register(registry, EntityType.Builder.of(EnumConsort.NAKAGATOR::create, CONSORT).sized(0.52F, 1.2F), "nakagator");
		register(registry, EntityType.Builder.of(EnumConsort.IGUANA::create, CONSORT).sized(0.52F, 1.2F), "iguana");
		
		register(registry, EntityType.Builder.of(ImpEntity::new, UNDERLING).sized(0.7F, 1.2F), "imp"); //TODO adjust hitboxes for all underlings
		register(registry, EntityType.Builder.of(OgreEntity::new, UNDERLING).sized(2F, 3.3F), "ogre");
		register(registry, EntityType.Builder.of(BasiliskEntity::new, UNDERLING).sized(2F, 2F), "basilisk");
		register(registry, EntityType.Builder.of(LichEntity::new, UNDERLING).sized(0.8F, 1.9F), "lich");
		register(registry, EntityType.Builder.of(GiclopsEntity::new, UNDERLING).sized(8.0F, 12.0F), "giclops");
		register(registry, EntityType.Builder.of(WyrmEntity::new, UNDERLING).sized(1.0F, 1.0F), "wyrm");
		
		register(registry, EntityType.Builder.of(PawnEntity::createDersite, EntityClassification.MONSTER).sized(0.6F, 2.1F), "dersite_pawn");
		register(registry, EntityType.Builder.of(PawnEntity::createProspitian, EntityClassification.MONSTER).sized(0.6F, 2.1F), "prospitian_pawn");
		register(registry, EntityType.Builder.of(BishopEntity::createDersite, EntityClassification.MONSTER).sized(1.9F, 4.1F), "dersite_bishop");
		register(registry, EntityType.Builder.of(BishopEntity::createProspitian, EntityClassification.MONSTER).sized(1.9F, 4.1F), "prospitian_bishop");
		register(registry, EntityType.Builder.of(RookEntity::createDersite, EntityClassification.MONSTER).sized(3.5F, 3.5F), "dersite_rook");
		register(registry, EntityType.Builder.of(RookEntity::createProspitian, EntityClassification.MONSTER).sized(3.5F, 3.5F), "prospitian_rook");
		
		register(registry, EntityType.Builder.<GristEntity>of(GristEntity::new, EntityClassification.MISC).sized(1 / 3F, 1 / 3F).setTrackingRange(4).setUpdateInterval(20).fireImmune(), "grist");
		register(registry, EntityType.Builder.<VitalityGelEntity>of(VitalityGelEntity::new, EntityClassification.MISC).sized(1 / 4F, 1 / 4F).setTrackingRange(4).setUpdateInterval(20).fireImmune(), "vitality_gel");
		register(registry, EntityType.Builder.<DecoyEntity>createNothing(EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new DecoyEntity(world)).noSave().noSummon(), "player_decoy");
		
		register(registry, EntityType.Builder.<MetalBoatEntity>of(MetalBoatEntity::new, EntityClassification.MISC).sized(1.375F, 0.5625F), "metal_boat");
		register(registry, EntityType.Builder.<BarbasolBombEntity>of(BarbasolBombEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10), "barbasol_bomb");
		register(registry, EntityType.Builder.<CrewPosterEntity>of(CrewPosterEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "midnight_crew_poster");
		register(registry, EntityType.Builder.<SbahjPosterEntity>of(SbahjPosterEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "sbahj_poster");
		register(registry, EntityType.Builder.<ShopPosterEntity>of(ShopPosterEntity::new, EntityClassification.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "shop_poster");
		register(registry, EntityType.Builder.<HologramEntity>of(HologramEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "hologram");
		register(registry, EntityType.Builder.<LotusFlowerEntity>of(LotusFlowerEntity::new, EntityClassification.MISC).sized(2F, 2F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10), "lotus_flower");
		
		register(registry, EntityType.Builder.<ConsumableProjectileEntity>of(ConsumableProjectileEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10), "consumable_projectile");
		register(registry, EntityType.Builder.<ReturningProjectileEntity>of(ReturningProjectileEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2), "returning_projectile"); //TODO smaller update interval value is temporary solution to improve client rendering
		register(registry, EntityType.Builder.<BouncingProjectileEntity>of(BouncingProjectileEntity::new, EntityClassification.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2), "bouncing_projectile");
	}
	
	/**
	 * Currently (1.15), this is not thread safe and need to be deferred
	 */
	public static void registerPlacements()
	{
		EntitySpawnPlacementRegistry.register(DERSITE_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(DERSITE_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(DERSITE_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::checkMobSpawnRules);
		EntitySpawnPlacementRegistry.register(IMP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(OGRE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(BASILISK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(LICH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(GICLOPS, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		
		EntitySpawnPlacementRegistry.register(FROG, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FrogEntity::canFrogSpawnOn);
		
		EntitySpawnPlacementRegistry.register(SALAMANDER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		EntitySpawnPlacementRegistry.register(TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		EntitySpawnPlacementRegistry.register(NAKAGATOR, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		EntitySpawnPlacementRegistry.register(IGUANA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
	}
	
	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event)
	{
		event.put(FROG, FrogEntity.frogAttributes().build());
		event.put(SALAMANDER, ConsortEntity.consortAttributes().build());
		event.put(TURTLE, ConsortEntity.consortAttributes().build());
		event.put(NAKAGATOR, ConsortEntity.consortAttributes().build());
		event.put(IGUANA, ConsortEntity.consortAttributes().build());
		
		event.put(IMP, ImpEntity.impAttributes().build());
		event.put(OGRE, OgreEntity.ogreAttributes().build());
		event.put(BASILISK, BasiliskEntity.basiliskAttributes().build());
		event.put(LICH, LichEntity.lichAttributes().build());
		event.put(GICLOPS, GiclopsEntity.giclopsAttributes().build());
		event.put(WYRM, WyrmEntity.wyrmAttributes().build());
		
		event.put(DERSITE_PAWN, PawnEntity.pawnAttributes().build());
		event.put(PROSPITIAN_PAWN, PawnEntity.pawnAttributes().build());
		event.put(DERSITE_BISHOP, BishopEntity.bishopAttributes().build());
		event.put(PROSPITIAN_BISHOP, BishopEntity.bishopAttributes().build());
		event.put(DERSITE_ROOK, RookEntity.rookAttributes().build());
		event.put(PROSPITIAN_ROOK, RookEntity.rookAttributes().build());
		
		event.put(PLAYER_DECOY, MobEntity.createMobAttributes().build());
		
		event.put(LOTUS_FLOWER, LivingEntity.createLivingAttributes().build());
	}
	
	private static void register(IForgeRegistry<EntityType<?>> registry, EntityType.Builder<?> builder, String name)
	{
		EntityType<?> type = builder.build(Minestuck.MOD_ID + ":" + name);
		registry.register(type.setRegistryName(name));
	}
}