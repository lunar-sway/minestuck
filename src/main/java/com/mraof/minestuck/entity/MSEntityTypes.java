package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.*;
import com.mraof.minestuck.entity.consort.IguanaEntity;
import com.mraof.minestuck.entity.consort.NakagatorEntity;
import com.mraof.minestuck.entity.consort.SalamanderEntity;
import com.mraof.minestuck.entity.consort.TurtleEntity;
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
	public static final EntityType<FrogEntity> FROG = getNull();
	public static final EntityType<SalamanderEntity> SALAMANDER = getNull();
	public static final EntityType<TurtleEntity> TURTLE = getNull();
	public static final EntityType<NakagatorEntity> NAKAGATOR = getNull();
	public static final EntityType<IguanaEntity> IGUANA = getNull();
	
	public static final EntityType<ImpEntity> IMP = getNull();
	public static final EntityType<OgreEntity> OGRE = getNull();
	public static final EntityType<BasiliskEntity> BASILISK = getNull();
	public static final EntityType<LichEntity> LICH = getNull();
	public static final EntityType<GiclopsEntity> GICLOPS = getNull();
	public static final EntityType<WyrmEntity> WYRM = getNull();
	
	public static final EntityType<BlackPawnEntity> DERSITE_PAWN = getNull();
	public static final EntityType<WhitePawnEntity> PROSPITIAN_PAWN = getNull();
	public static final EntityType<BlackBishopEntity> DERSITE_BISHOP = getNull();
	public static final EntityType<WhiteBishopEntity> PROSPITIAN_BISHOP = getNull();
	public static final EntityType<BlackRookEntity> DERSITE_ROOK = getNull();
	public static final EntityType<WhiteRookEntity> PROSPITIAN_ROOK = getNull();
	
	public static final EntityType<GristEntity> GRIST = getNull();
	public static final EntityType<VitalityGelEntity> VITALITY_GEL = getNull();
	public static final EntityType<DecoyEntity> PLAYER_DECOY = getNull();
	
	public static final EntityType<MetalBoatEntity> METAL_BOAT = getNull();
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
		register(registry, EntityType.Builder.create(SalamanderEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F), "salamander");
		register(registry, EntityType.Builder.create(TurtleEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F), "turtle");
		register(registry, EntityType.Builder.create(NakagatorEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F), "nakagator");
		register(registry, EntityType.Builder.create(IguanaEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F), "iguana");
		
		register(registry, EntityType.Builder.create(ImpEntity::new, EntityClassification.MONSTER).size(0.75F, 1.5F), "imp");
		register(registry, EntityType.Builder.create(OgreEntity::new, EntityClassification.MONSTER).size(3.0F, 4.5F), "ogre");
		register(registry, EntityType.Builder.create(BasiliskEntity::new, EntityClassification.MONSTER).size(3F, 2F), "basilisk");
		register(registry, EntityType.Builder.create(LichEntity::new, EntityClassification.MONSTER).size(1.0F, 2.0F), "lich");
		register(registry, EntityType.Builder.create(GiclopsEntity::new, EntityClassification.MONSTER).size(8.0F, 12.0F), "giclops");
		register(registry, EntityType.Builder.create(WyrmEntity::new, EntityClassification.MONSTER), "wyrm");
		
		register(registry, EntityType.Builder.create(BlackPawnEntity::new, EntityClassification.MONSTER).size(0.6F, 1.5F), "dersite_pawn");
		register(registry, EntityType.Builder.create(WhitePawnEntity::new, EntityClassification.MONSTER).size(0.6F, 1.5F), "prospitian_pawn");
		register(registry, EntityType.Builder.create(BlackBishopEntity::new, EntityClassification.MONSTER).size(1.9F, 4.1F), "dersite_bishop");
		register(registry, EntityType.Builder.create(WhiteBishopEntity::new, EntityClassification.MONSTER).size(1.9F, 4.1F), "prospitian_bishop");
		register(registry, EntityType.Builder.create(BlackRookEntity::new, EntityClassification.MONSTER).size(3.5F, 3.5F), "dersite_rook");
		register(registry, EntityType.Builder.create(WhiteRookEntity::new, EntityClassification.MONSTER).size(3.5F, 3.5F), "prospitian_rook");
		
		register(registry, EntityType.Builder.<GristEntity>create(GristEntity::new, EntityClassification.MISC).size(1 / 3F, 1 / 3F).immuneToFire(), "grist");
		register(registry, EntityType.Builder.<VitalityGelEntity>create(VitalityGelEntity::new, EntityClassification.MISC).size(1 / 4F, 1 / 4F).immuneToFire(), "vitality_gel");
		register(registry, EntityType.Builder.<DecoyEntity>create(EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new DecoyEntity(world)).disableSerialization().disableSummoning(), "player_decoy");
		
		register(registry, EntityType.Builder.<MetalBoatEntity>create(MetalBoatEntity::new, EntityClassification.MISC), "metal_boat");
		register(registry, EntityType.Builder.<CrewPosterEntity>create(CrewPosterEntity::new, EntityClassification.MISC), "midnight_crew_poster");
		register(registry, EntityType.Builder.<SbahjPosterEntity>create(SbahjPosterEntity::new, EntityClassification.MISC), "sbahj_poster");
		register(registry, EntityType.Builder.<ShopPosterEntity>create(ShopPosterEntity::new, EntityClassification.MISC), "shop_poster");
		register(registry, EntityType.Builder.<HologramEntity>create(HologramEntity::new, EntityClassification.MISC), "hologram");
	}
	
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