package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class MSEntityTypes
{
	public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Minestuck.MOD_ID);
	
	public static final MobCategory UNDERLING = MobCategory.valueOf("MINESTUCK_UNDERLING");
	public static final MobCategory CONSORT = MobCategory.valueOf("MINESTUCK_CONSORT");
	
	public static final Supplier<EntityType<FrogEntity>> FROG = REGISTER.register("frog", () -> EntityType.Builder.<FrogEntity>of(FrogEntity::new, MobCategory.CREATURE).sized(0.51F, 0.51F).build(Minestuck.id("frog").toString()));
	public static final Supplier<EntityType<ConsortEntity>> SALAMANDER = REGISTER.register("salamander", () -> EntityType.Builder.of(EnumConsort.SALAMANDER::create, CONSORT).sized(0.52F, 1.2F).build(Minestuck.id("salamander").toString()));
	public static final Supplier<EntityType<ConsortEntity>> TURTLE = REGISTER.register("turtle", () -> EntityType.Builder.of(EnumConsort.TURTLE::create, CONSORT).sized(0.52F, 1.3F).build(Minestuck.id("turtle").toString()));
	public static final Supplier<EntityType<ConsortEntity>> NAKAGATOR = REGISTER.register("nakagator", () -> EntityType.Builder.of(EnumConsort.NAKAGATOR::create, CONSORT).sized(0.52F, 1.2F).build(Minestuck.id("nakagator").toString()));
	public static final Supplier<EntityType<ConsortEntity>> IGUANA = REGISTER.register("iguana", () -> EntityType.Builder.of(EnumConsort.IGUANA::create, CONSORT).sized(0.52F, 1.2F).build(Minestuck.id("iguana").toString()));
	
	public static final Supplier<EntityType<ImpEntity>> IMP = REGISTER.register("imp", () -> EntityType.Builder.of(ImpEntity::new, UNDERLING).sized(0.7F, 1.5F).build(Minestuck.id("imp").toString()));
	public static final Supplier<EntityType<OgreEntity>> OGRE = REGISTER.register("ogre", () -> EntityType.Builder.of(OgreEntity::new, UNDERLING).sized(2F, 3.3F).build(Minestuck.id("ogre").toString()));
	public static final Supplier<EntityType<BasiliskEntity>> BASILISK = REGISTER.register("basilisk", () -> EntityType.Builder.of(BasiliskEntity::new, UNDERLING).sized(1.9F, 1.9F).fireImmune().build(Minestuck.id("basilisk").toString()));
	public static final Supplier<EntityType<LichEntity>> LICH = REGISTER.register("lich", () -> EntityType.Builder.of(LichEntity::new, UNDERLING).sized(0.8F, 1.7F).build(Minestuck.id("lich").toString()));
	public static final Supplier<EntityType<GiclopsEntity>> GICLOPS = REGISTER.register("giclops", () -> EntityType.Builder.of(GiclopsEntity::new, UNDERLING).sized(5.0F, 6.0F).build(Minestuck.id("giclops").toString()));
	
	public static final Supplier<EntityType<PawnEntity>> DERSITE_PAWN = REGISTER.register("dersite_pawn", () -> EntityType.Builder.of(PawnEntity::createDersite, MobCategory.MONSTER).sized(0.6F, 2.1F).build(Minestuck.id("dersite_pawn").toString()));
	public static final Supplier<EntityType<PawnEntity>> PROSPITIAN_PAWN = REGISTER.register("prospitian_pawn", () -> EntityType.Builder.of(PawnEntity::createProspitian, MobCategory.MONSTER).sized(0.6F, 2.1F).build(Minestuck.id("prospitian_pawn").toString()));
	public static final Supplier<EntityType<BishopEntity>> DERSITE_BISHOP = REGISTER.register("dersite_bishop", () -> EntityType.Builder.of(BishopEntity::createDersite, MobCategory.MONSTER).sized(1.9F, 4.1F).build(Minestuck.id("dersite_bishop").toString()));
	public static final Supplier<EntityType<BishopEntity>> PROSPITIAN_BISHOP = REGISTER.register("prospitian_bishop", () -> EntityType.Builder.of(BishopEntity::createProspitian, MobCategory.MONSTER).sized(1.9F, 4.1F).build(Minestuck.id("prospitian_bishop").toString()));
	public static final Supplier<EntityType<RookEntity>> DERSITE_ROOK = REGISTER.register("dersite_rook", () -> EntityType.Builder.of(RookEntity::createDersite, MobCategory.MONSTER).sized(3.5F, 3.5F).build(Minestuck.id("dersite_rook").toString()));
	public static final Supplier<EntityType<RookEntity>> PROSPITIAN_ROOK = REGISTER.register("prospitian_rook", () -> EntityType.Builder.of(RookEntity::createProspitian, MobCategory.MONSTER).sized(3.5F, 3.5F).build(Minestuck.id("prospitian_rook").toString()));
	
	public static final Supplier<EntityType<GristEntity>> GRIST = REGISTER.register("grist", () -> EntityType.Builder.<GristEntity>of(GristEntity::new, MobCategory.MISC).sized(1 / 3F, 1 / 3F).setTrackingRange(4).setUpdateInterval(20).build(Minestuck.id("grist").toString()));
	public static final Supplier<EntityType<VitalityGelEntity>> VITALITY_GEL = REGISTER.register("vitality_gel", () -> EntityType.Builder.<VitalityGelEntity>of(VitalityGelEntity::new, MobCategory.MISC).sized(1 / 4F, 1 / 4F).setTrackingRange(4).setUpdateInterval(20).build(Minestuck.id("vitality_gel").toString()));
	public static final Supplier<EntityType<DecoyEntity>> PLAYER_DECOY = REGISTER.register("player_decoy", () -> EntityType.Builder.<DecoyEntity>of(DecoyEntity::new, MobCategory.MISC).noSave().noSummon().build(Minestuck.id("player_decoy").toString()));
	public static final Supplier<EntityType<ServerCursorEntity>> SERVER_CURSOR = REGISTER.register("server_cursor", () -> EntityType.Builder.of(ServerCursorEntity::new, MobCategory.MISC).noSave().noSummon().sized(0.1F, 0.1F).setShouldReceiveVelocityUpdates(false).setTrackingRange(4).fireImmune().build(Minestuck.id("server_cursor").toString()));
	
	public static final Supplier<EntityType<MetalBoatEntity>> METAL_BOAT = REGISTER.register("metal_boat", () -> EntityType.Builder.<MetalBoatEntity>of(MetalBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).build(Minestuck.id("metal_boat").toString()));
	public static final Supplier<EntityType<BarbasolBombEntity>> BARBASOL_BOMB = REGISTER.register("barbasol_bomb", () -> EntityType.Builder.<BarbasolBombEntity>of(BarbasolBombEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).fireImmune().build(Minestuck.id("barbasol_bomb").toString()));
	public static final Supplier<EntityType<PosterEntity>> POSTER = REGISTER.register("poster", () -> EntityType.Builder.<PosterEntity>of(PosterEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(Minestuck.id("sbahj_poster").toString()));
	public static final Supplier<EntityType<LotusFlowerEntity>> LOTUS_FLOWER = REGISTER.register("lotus_flower", () -> EntityType.Builder.of(LotusFlowerEntity::new, MobCategory.MISC).sized(1.25F, 1.6F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).fireImmune().build(Minestuck.id("lotus_flower").toString()));
	
	public static final Supplier<EntityType<ConsumableProjectileEntity>> CONSUMABLE_PROJECTILE = REGISTER.register("consumable_projectile", () -> EntityType.Builder.<ConsumableProjectileEntity>of(ConsumableProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10).fireImmune().build(Minestuck.id("consumable_projectile").toString()));
	public static final Supplier<EntityType<ReturningProjectileEntity>> RETURNING_PROJECTILE = REGISTER.register("returning_projectile", () -> EntityType.Builder.<ReturningProjectileEntity>of(ReturningProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2).fireImmune().build(Minestuck.id("returning_projectile").toString())); //TODO smaller update interval value is temporary solution to improve client rendering
	public static final Supplier<EntityType<BouncingProjectileEntity>> BOUNCING_PROJECTILE = REGISTER.register("bouncing_projectile", () -> EntityType.Builder.<BouncingProjectileEntity>of(BouncingProjectileEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).setTrackingRange(6).setUpdateInterval(2).fireImmune().build(Minestuck.id("bouncing_projectile").toString()));
	
	@SubscribeEvent
	public static void registerPlacements(RegisterSpawnPlacementsEvent event)
	{
		event.register(DERSITE_PAWN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(PROSPITIAN_PAWN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(DERSITE_BISHOP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(PROSPITIAN_BISHOP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(DERSITE_ROOK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(PROSPITIAN_ROOK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
		
		event.register(IMP.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(OGRE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(BASILISK.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(LICH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(GICLOPS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful, RegisterSpawnPlacementsEvent.Operation.OR);
		
		event.register(FROG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FrogEntity::canFrogSpawnOn, RegisterSpawnPlacementsEvent.Operation.OR);
		
		event.register(SALAMANDER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(TURTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(NAKAGATOR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn, RegisterSpawnPlacementsEvent.Operation.OR);
		event.register(IGUANA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ConsortEntity::canConsortSpawnOn, RegisterSpawnPlacementsEvent.Operation.OR);
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
	}
}
