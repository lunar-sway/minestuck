package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class MSEntityTypes
{
	public static EntityClassification UNDERLING = EntityClassification.create("UNDERLING", "underling", 35, false, false);
	
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
		register(registry, EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE).size(0.51F, 0.51F), "frog");
		register(registry, EntityType.Builder.create(EnumConsort.SALAMANDER::create, EntityClassification.CREATURE).size(0.45F, 1.0F), "salamander");
		register(registry, EntityType.Builder.create(EnumConsort.TURTLE::create, EntityClassification.CREATURE).size(0.45F, 1.0F), "turtle");
		register(registry, EntityType.Builder.create(EnumConsort.NAKAGATOR::create, EntityClassification.CREATURE).size(0.45F, 1.1F), "nakagator");
		register(registry, EntityType.Builder.create(EnumConsort.IGUANA::create, EntityClassification.CREATURE).size(0.45F, 1.0F), "iguana");
		
		register(registry, EntityType.Builder.create(ImpEntity::new, UNDERLING).size(0.7F, 1.2F), "imp");
		register(registry, EntityType.Builder.create(OgreEntity::new, UNDERLING).size(2.8F, 4.3F), "ogre");
		register(registry, EntityType.Builder.create(BasiliskEntity::new, UNDERLING).size(3F, 2F), "basilisk");
		register(registry, EntityType.Builder.create(LichEntity::new, UNDERLING).size(0.8F, 2.0F), "lich");
		register(registry, EntityType.Builder.create(GiclopsEntity::new, UNDERLING).size(4.0F, 6.0F), "giclops");
		register(registry, EntityType.Builder.create(WyrmEntity::new, UNDERLING), "wyrm");
		
		register(registry, EntityType.Builder.create(PawnEntity::createDersite, EntityClassification.MONSTER).size(0.6F, 1.5F), "dersite_pawn");
		register(registry, EntityType.Builder.create(PawnEntity::createProspitian, EntityClassification.MONSTER).size(0.6F, 1.5F), "prospitian_pawn");
		register(registry, EntityType.Builder.create(BishopEntity::createDersite, EntityClassification.MONSTER).size(1.9F, 4.1F), "dersite_bishop");
		register(registry, EntityType.Builder.create(BishopEntity::createProspitian, EntityClassification.MONSTER).size(1.9F, 4.1F), "prospitian_bishop");
		register(registry, EntityType.Builder.create(RookEntity::createDersite, EntityClassification.MONSTER).size(3.5F, 3.5F), "dersite_rook");
		register(registry, EntityType.Builder.create(RookEntity::createProspitian, EntityClassification.MONSTER).size(3.5F, 3.5F), "prospitian_rook");
		
		register(registry, EntityType.Builder.<GristEntity>create(GristEntity::new, EntityClassification.MISC).size(1 / 3F, 1 / 3F).setTrackingRange(4).setUpdateInterval(20).immuneToFire(), "grist");
		register(registry, EntityType.Builder.<VitalityGelEntity>create(VitalityGelEntity::new, EntityClassification.MISC).size(1 / 4F, 1 / 4F).setTrackingRange(4).setUpdateInterval(20).immuneToFire(), "vitality_gel");
		register(registry, EntityType.Builder.<DecoyEntity>create(EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new DecoyEntity(world)).disableSerialization().disableSummoning(), "player_decoy");
		
		register(registry, EntityType.Builder.<MetalBoatEntity>create(MetalBoatEntity::new, EntityClassification.MISC).size(1.375F, 0.5625F), "metal_boat");
		register(registry, EntityType.Builder.<BarbasolBombEntity>create(BarbasolBombEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(4).setUpdateInterval(10), "barbasol_bomb");
		register(registry, EntityType.Builder.<CrewPosterEntity>create(CrewPosterEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "midnight_crew_poster");
		register(registry, EntityType.Builder.<SbahjPosterEntity>create(SbahjPosterEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "sbahj_poster");
		register(registry, EntityType.Builder.<ShopPosterEntity>create(ShopPosterEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "shop_poster");
		register(registry, EntityType.Builder.<HologramEntity>create(HologramEntity::new, EntityClassification.MISC).size(0.25F, 0.25F).setShouldReceiveVelocityUpdates(false).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE), "hologram");
	}
	
	/**
	 * Currently (1.15), this is not thread safe and need to be deferred
	 */
	public static void registerPlacements()
	{
		EntitySpawnPlacementRegistry.register(DERSITE_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(DERSITE_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(DERSITE_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
		EntitySpawnPlacementRegistry.register(IMP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(OGRE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(BASILISK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(LICH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
		EntitySpawnPlacementRegistry.register(GICLOPS, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UnderlingEntity::canSpawnOnAndNotPeaceful);
	}
	
	private static void register(IForgeRegistry<EntityType<?>> registry, EntityType.Builder<?> builder, String name)
	{
		EntityType<?> type = builder.build(Minestuck.MOD_ID + ":" + name);
		registry.register(type.setRegistryName(name));
	}
}