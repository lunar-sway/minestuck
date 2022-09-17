package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class MSEntityTypes
{
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITIES, Minestuck.MOD_ID);
	
	public static MobCategory UNDERLING = MobCategory.create("UNDERLING", "underling", 35, false, false, 128);
	public static MobCategory CONSORT = MobCategory.create("CONSORT", "consort", 10, true, false, 128);
	
	public static final RegistryObject<EntityType<FrogEntity>> FROG = REGISTER.register("frog", () -> EntityType.Builder.<FrogEntity>of(FrogEntity::new, MobCategory.CREATURE).sized(0.51F, 0.51F).build(null));
	public static final RegistryObject<EntityType<ConsortEntity>> SALAMANDER = REGISTER.register("salamander", () -> EntityType.Builder.of(EnumConsort.SALAMANDER::create, CONSORT).sized(0.45F, 1.0F).build(null));
	public static final RegistryObject<EntityType<ConsortEntity>> TURTLE = REGISTER.register("turtle", () -> EntityType.Builder.of(EnumConsort.TURTLE::create, CONSORT).sized(0.45F, 1.0F).build(null));
	public static final RegistryObject<EntityType<ConsortEntity>> NAKAGATOR = REGISTER.register("nakagator", () -> EntityType.Builder.of(EnumConsort.NAKAGATOR::create, CONSORT).sized(0.45F, 1.1F).build(null));
	public static final RegistryObject<EntityType<ConsortEntity>> IGUANA = REGISTER.register("iguana", () -> EntityType.Builder.of(EnumConsort.IGUANA::create, CONSORT).sized(0.45F, 1.0F).build(null));
	
	public static final RegistryObject<EntityType<ImpEntity>> IMP = REGISTER.register("imp", () -> EntityType.Builder.of(ImpEntity::new, UNDERLING).sized(0.7F, 1.2F).build(null));
	public static final RegistryObject<EntityType<OgreEntity>> OGRE = REGISTER.register("ogre", () -> EntityType.Builder.of(OgreEntity::new, UNDERLING).sized(2.8F, 4.3F).build(null));
	public static final RegistryObject<EntityType<BasiliskEntity>> BASILISK = REGISTER.register("basilisk", () -> EntityType.Builder.of(BasiliskEntity::new, UNDERLING).sized(3F, 2F).build(null));
	public static final RegistryObject<EntityType<LichEntity>> LICH = REGISTER.register("lich", () -> EntityType.Builder.of(LichEntity::new, UNDERLING).sized(0.8F, 2.0F).build(null));
	public static final RegistryObject<EntityType<GiclopsEntity>> GICLOPS = REGISTER.register("giclops", () -> EntityType.Builder.of(GiclopsEntity::new, UNDERLING).sized(8.0F, 12.0F).build(null));
	
	public static final RegistryObject<EntityType<PawnEntity>> DERSITE_PAWN = REGISTER.register("dersite_pawn", () -> EntityType.Builder.of(PawnEntity::createDersite, MobCategory.MONSTER).sized(0.6F, 2.1F).build(null));
	public static final RegistryObject<EntityType<PawnEntity>> PROSPITIAN_PAWN = REGISTER.register("prospitian_pawn", () -> EntityType.Builder.of(PawnEntity::createProspitian, MobCategory.MONSTER).sized(0.6F, 2.1F).build(null));
	public static final RegistryObject<EntityType<BishopEntity>> DERSITE_BISHOP = REGISTER.register("dersite_bishop", () -> EntityType.Builder.of(BishopEntity::createDersite, MobCategory.MONSTER).sized(1.9F, 4.1F).build(null));
	public static final RegistryObject<EntityType<BishopEntity>> PROSPITIAN_BISHOP = REGISTER.register("prospitian_bishop", () -> EntityType.Builder.of(BishopEntity::createProspitian, MobCategory.MONSTER).sized(1.9F, 4.1F).build(null));
	public static final RegistryObject<EntityType<RookEntity>> DERSITE_ROOK = REGISTER.register("dersite_rook", () -> EntityType.Builder.of(RookEntity::createDersite, MobCategory.MONSTER).sized(3.5F, 3.5F).build(null));
	public static final RegistryObject<EntityType<RookEntity>> PROSPITIAN_ROOK = REGISTER.register("prospitian_rook", () -> EntityType.Builder.of(RookEntity::createProspitian, MobCategory.MONSTER).sized(3.5F, 3.5F).build(null));
	
	public static final RegistryObject<EntityType<GristEntity>> GRIST = REGISTER.register("grist", () -> EntityType.Builder.<GristEntity>of(GristEntity::new, MobCategory.MISC).sized(1 / 3F, 1 / 3F).setTrackingRange(4).setUpdateInterval(20).fireImmune().build(null));
	public static final RegistryObject<EntityType<VitalityGelEntity>> VITALITY_GEL = REGISTER.register("vitality_gel", () -> EntityType.Builder.<VitalityGelEntity>of(VitalityGelEntity::new, MobCategory.MISC).sized(1 / 4F, 1 / 4F).setTrackingRange(4).setUpdateInterval(20).fireImmune().build(null));
	public static final RegistryObject<EntityType<DecoyEntity>> PLAYER_DECOY = REGISTER.register("player_decoy", () -> EntityType.Builder.<DecoyEntity>createNothing(MobCategory.MISC).setCustomClientFactory((spawnEntity, world) -> new DecoyEntity(world)).noSave().noSummon().build(null));
	
	public static final RegistryObject<EntityType<MetalBoatEntity>> METAL_BOAT = REGISTER.register("metal_boat", () -> EntityType.Builder.<MetalBoatEntity>of(MetalBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).build(null));
	public static final RegistryObject<EntityType<BarbasolBombEntity>> BARBASOL_BOMB = REGISTER.register("barbasol_bomb", () -> EntityType.Builder.<BarbasolBombEntity>of(BarbasolBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).build(null));
	public static final RegistryObject<EntityType<CrewPosterEntity>> MIDNIGHT_CREW_POSTER = REGISTER.register("midnight_crew_poster", () -> EntityType.Builder.<CrewPosterEntity>of(CrewPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(null));
	public static final RegistryObject<EntityType<SbahjPosterEntity>> SBAHJ_POSTER = REGISTER.register("sbahj_poster", () -> EntityType.Builder.<SbahjPosterEntity>of(SbahjPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(null));
	public static final RegistryObject<EntityType<ShopPosterEntity>> SHOP_POSTER = REGISTER.register("shop_poster", () -> EntityType.Builder.<ShopPosterEntity>of(ShopPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(null));
	public static final RegistryObject<EntityType<HologramEntity>> HOLOGRAM = REGISTER.register("hologram", () -> EntityType.Builder.<HologramEntity>of(HologramEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(null));
	public static final RegistryObject<EntityType<LotusFlowerEntity>> LOTUS_FLOWER = REGISTER.register("lotus_flower", () -> EntityType.Builder.<LotusFlowerEntity>of(LotusFlowerEntity::new, MobCategory.MISC).sized(2F, 2F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).build(null));
	
	public static final RegistryObject<EntityType<ConsumableProjectileEntity>> CONSUMABLE_PROJECTILE = REGISTER.register("consumable_projectile", () -> EntityType.Builder.<ConsumableProjectileEntity>of(ConsumableProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).build(null));
	public static final RegistryObject<EntityType<ReturningProjectileEntity>> RETURNING_PROJECTILE = REGISTER.register("returning_projectile", () -> EntityType.Builder.<ReturningProjectileEntity>of(ReturningProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2).build(null)); //TODO smaller update interval value is temporary solution to improve client rendering
	public static final RegistryObject<EntityType<BouncingProjectileEntity>> BOUNCING_PROJECTILE = REGISTER.register("bouncing_projectile", () -> EntityType.Builder.<BouncingProjectileEntity>of(BouncingProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2).build(null));
	
	/*@Nonnull
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
		register(registry, EntityType.Builder.<FrogEntity>of(FrogEntity::new, MobCategory.CREATURE).sized(0.51F, 0.51F), "frog");
		register(registry, EntityType.Builder.of(EnumConsort.SALAMANDER::create, CONSORT).sized(0.45F, 1.0F), "salamander");
		register(registry, EntityType.Builder.of(EnumConsort.TURTLE::create, CONSORT).sized(0.45F, 1.0F), "turtle");
		register(registry, EntityType.Builder.of(EnumConsort.NAKAGATOR::create, CONSORT).sized(0.45F, 1.1F), "nakagator");
		register(registry, EntityType.Builder.of(EnumConsort.IGUANA::create, CONSORT).sized(0.45F, 1.0F), "iguana");
		
		register(registry, EntityType.Builder.of(ImpEntity::new, UNDERLING).sized(0.7F, 1.2F), "imp");
		register(registry, EntityType.Builder.of(OgreEntity::new, UNDERLING).sized(2.8F, 4.3F), "ogre");
		register(registry, EntityType.Builder.of(BasiliskEntity::new, UNDERLING).sized(3F, 2F), "basilisk");
		register(registry, EntityType.Builder.of(LichEntity::new, UNDERLING).sized(0.8F, 2.0F), "lich");
		register(registry, EntityType.Builder.of(GiclopsEntity::new, UNDERLING).sized(8.0F, 12.0F), "giclops");
		
		register(registry, EntityType.Builder.of(PawnEntity::createDersite, MobCategory.MONSTER).sized(0.6F, 2.1F), "dersite_pawn");
		register(registry, EntityType.Builder.of(PawnEntity::createProspitian, MobCategory.MONSTER).sized(0.6F, 2.1F), "prospitian_pawn");
		register(registry, EntityType.Builder.of(BishopEntity::createDersite, MobCategory.MONSTER).sized(1.9F, 4.1F), "dersite_bishop");
		register(registry, EntityType.Builder.of(BishopEntity::createProspitian, MobCategory.MONSTER).sized(1.9F, 4.1F), "prospitian_bishop");
		register(registry, EntityType.Builder.of(RookEntity::createDersite, MobCategory.MONSTER).sized(3.5F, 3.5F), "dersite_rook");
		register(registry, EntityType.Builder.of(RookEntity::createProspitian, MobCategory.MONSTER).sized(3.5F, 3.5F), "prospitian_rook");
		
		register(registry, EntityType.Builder.<GristEntity>of(GristEntity::new, MobCategory.MISC).sized(1 / 3F, 1 / 3F).setTrackingRange(4).setUpdateInterval(20).fireImmune(), "grist");
		register(registry, EntityType.Builder.<VitalityGelEntity>of(VitalityGelEntity::new, MobCategory.MISC).sized(1 / 4F, 1 / 4F).setTrackingRange(4).setUpdateInterval(20).fireImmune(), "vitality_gel");
		register(registry, EntityType.Builder.<DecoyEntity>createNothing(MobCategory.MISC).setCustomClientFactory((spawnEntity, world) -> new DecoyEntity(world)).noSave().noSummon(), "player_decoy");
		
		register(registry, EntityType.Builder.<MetalBoatEntity>of(MetalBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F), "metal_boat");
		register(registry, EntityType.Builder.<BarbasolBombEntity>of(BarbasolBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10), "barbasol_bomb");
		register(registry, EntityType.Builder.<CrewPosterEntity>of(CrewPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "midnight_crew_poster");
		register(registry, EntityType.Builder.<SbahjPosterEntity>of(SbahjPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "sbahj_poster");
		register(registry, EntityType.Builder.<ShopPosterEntity>of(ShopPosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "shop_poster");
		register(registry, EntityType.Builder.<HologramEntity>of(HologramEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "hologram");
		register(registry, EntityType.Builder.<LotusFlowerEntity>of(LotusFlowerEntity::new, MobCategory.MISC).sized(2F, 2F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10), "lotus_flower");
		
		register(registry, EntityType.Builder.<ConsumableProjectileEntity>of(ConsumableProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10), "consumable_projectile");
		register(registry, EntityType.Builder.<ReturningProjectileEntity>of(ReturningProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2), "returning_projectile"); //TODO smaller update interval value is temporary solution to improve client rendering
		register(registry, EntityType.Builder.<BouncingProjectileEntity>of(BouncingProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2), "bouncing_projectile");
	}*/
	
	/**
	 * Currently (1.15), this is not thread safe and need to be deferred
	 */
	public static void registerPlacements()
	{
		SpawnPlacements.register(DERSITE_PAWN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(PROSPITIAN_PAWN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(DERSITE_BISHOP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(PROSPITIAN_BISHOP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(DERSITE_ROOK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(PROSPITIAN_ROOK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
		SpawnPlacements.register(IMP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		SpawnPlacements.register(OGRE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		SpawnPlacements.register(BASILISK.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		SpawnPlacements.register(LICH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		SpawnPlacements.register(GICLOPS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		
		SpawnPlacements.register(FROG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FrogEntity::canFrogSpawnOn);
		
		SpawnPlacements.register(SALAMANDER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		SpawnPlacements.register(TURTLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		SpawnPlacements.register(NAKAGATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
		SpawnPlacements.register(IGUANA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn);
	}
	
	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event)
	{
		event.put(FROG.get(), FrogEntity.frogAttributes().build());
		event.put(SALAMANDER.get(), ConsortEntity.consortAttributes().build());
		event.put(TURTLE.get(), ConsortEntity.consortAttributes().build());
		event.put(NAKAGATOR.get(), ConsortEntity.consortAttributes().build());
		event.put(IGUANA.get(), ConsortEntity.consortAttributes().build());
		
		event.put(IMP.get(), ImpEntity.impAttributes().build());
		event.put(OGRE.get(), OgreEntity.ogreAttributes().build());
		event.put(BASILISK.get(), BasiliskEntity.basiliskAttributes().build());
		event.put(LICH.get(), LichEntity.lichAttributes().build());
		event.put(GICLOPS.get(), GiclopsEntity.giclopsAttributes().build());
		
		event.put(DERSITE_PAWN.get(), PawnEntity.pawnAttributes().build());
		event.put(PROSPITIAN_PAWN.get(), PawnEntity.pawnAttributes().build());
		event.put(DERSITE_BISHOP.get(), BishopEntity.bishopAttributes().build());
		event.put(PROSPITIAN_BISHOP.get(), BishopEntity.bishopAttributes().build());
		event.put(DERSITE_ROOK.get(), RookEntity.rookAttributes().build());
		event.put(PROSPITIAN_ROOK.get(), RookEntity.rookAttributes().build());
		
		event.put(PLAYER_DECOY.get(), Mob.createMobAttributes().build());
		
		event.put(LOTUS_FLOWER.get(), LivingEntity.createLivingAttributes().build());
	}
	
	/*private static void register(IForgeRegistry<EntityType<?>> registry, EntityType.Builder<?> builder, String name)
	{
		EntityType<?> type = builder.build(Minestuck.MOD_ID + ":" + name);
		registry.register(type.setRegistryName(name));
	}*/
}