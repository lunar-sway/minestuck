package com.mraof.minestuck.entity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.BlackBishopEntity;
import com.mraof.minestuck.entity.carapacian.BlackPawnEntity;
import com.mraof.minestuck.entity.carapacian.BlackRookEntity;
import com.mraof.minestuck.entity.carapacian.WhiteBishopEntity;
import com.mraof.minestuck.entity.carapacian.WhitePawnEntity;
import com.mraof.minestuck.entity.carapacian.WhiteRookEntity;
import com.mraof.minestuck.entity.consort.IguanaEntity;
import com.mraof.minestuck.entity.consort.NakagatorEntity;
import com.mraof.minestuck.entity.consort.SalamanderEntity;
import com.mraof.minestuck.entity.consort.TurtleEntity;
import com.mraof.minestuck.entity.item.CrewPosterEntity;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.HologramEntity;
import com.mraof.minestuck.entity.item.MetalBoatEntity;
import com.mraof.minestuck.entity.item.SbahjPosterEntity;
import com.mraof.minestuck.entity.item.ShopPosterEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.entity.underling.*;

import net.minecraft.entity.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityTypes
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
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		registry.register(EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE).size(0.51F, 0.51F).build(Minestuck.MOD_ID + ":frog").setRegistryName("frog"));
		registry.register(EntityType.Builder.create(SalamanderEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":salamander").setRegistryName("salamander"));
		registry.register(EntityType.Builder.create(TurtleEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":turtle").setRegistryName("turtle"));
		registry.register(EntityType.Builder.create(NakagatorEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":nakagator").setRegistryName("nakagator"));
		registry.register(EntityType.Builder.create(IguanaEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":iguana").setRegistryName("iguana"));
		
		registry.register(EntityType.Builder.create(ImpEntity::new, EntityClassification.MONSTER).size(0.75F, 1.5F).build(Minestuck.MOD_ID + ":imp").setRegistryName("imp"));
		registry.register(EntityType.Builder.create(OgreEntity::new, EntityClassification.MONSTER).size(3.0F, 4.5F).build(Minestuck.MOD_ID + ":ogre").setRegistryName("ogre"));
		registry.register(EntityType.Builder.create(BasiliskEntity::new, EntityClassification.MONSTER).size(3F, 2F).build(Minestuck.MOD_ID + ":basilisk").setRegistryName("basilisk"));
		registry.register(EntityType.Builder.create(LichEntity::new, EntityClassification.MONSTER).size(1.0F, 2.0F).build(Minestuck.MOD_ID + ":lich").setRegistryName("lich"));
		registry.register(EntityType.Builder.create(GiclopsEntity::new, EntityClassification.MONSTER).size(8.0F, 12.0F).build(Minestuck.MOD_ID + ":giclops").setRegistryName("giclops"));
		registry.register(EntityType.Builder.create(WyrmEntity::new, EntityClassification.MONSTER).build(Minestuck.MOD_ID + ":wyrm").setRegistryName("wyrm"));
		
		registry.register(EntityType.Builder.create(BlackPawnEntity::new, EntityClassification.MONSTER).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":dersite_pawn").setRegistryName("dersite_pawn"));
		registry.register(EntityType.Builder.create(WhitePawnEntity::new, EntityClassification.MONSTER).size(0.6F, 1.5F).build(Minestuck.MOD_ID + ":prospitian_pawn").setRegistryName("prospitian_pawn"));
		registry.register(EntityType.Builder.create(BlackBishopEntity::new, EntityClassification.MONSTER).size(1.9F, 4.1F).build(Minestuck.MOD_ID + ":dersite_bishop").setRegistryName("dersite_bishop"));
		registry.register(EntityType.Builder.create(WhiteBishopEntity::new, EntityClassification.MONSTER).size(1.9F, 4.1F).build(Minestuck.MOD_ID + ":prospitian_bishop").setRegistryName("prospitian_bishop"));
		registry.register(EntityType.Builder.create(BlackRookEntity::new, EntityClassification.MONSTER).size(3.5F, 3.5F).build(Minestuck.MOD_ID + ":dersite_rook").setRegistryName("dersite_rook"));
		registry.register(EntityType.Builder.create(WhiteRookEntity::new, EntityClassification.MONSTER).size(3.5F, 3.5F).build(Minestuck.MOD_ID + ":prospitian_rook").setRegistryName("prospitian_rook"));
		
		registry.register(EntityType.Builder.<GristEntity>create(GristEntity::new, EntityClassification.MISC).size(1 / 3F, 1 / 3F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID + ":grist").setRegistryName("grist"));
		registry.register(EntityType.Builder.<VitalityGelEntity>create(VitalityGelEntity::new, EntityClassification.MISC).size(1 / 4F, 1 / 4F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID + ":vitality_gel").setRegistryName("vitality_gel"));
		registry.register(EntityType.Builder.<DecoyEntity>create(EntityClassification.MISC).disableSerialization().disableSummoning().build(Minestuck.MOD_ID + ":decoy").setRegistryName("player_decoy"));
		
		registry.register(EntityType.Builder.<MetalBoatEntity>create(MetalBoatEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID + ":metal_boat").setRegistryName("metal_boat"));
		registry.register(EntityType.Builder.<CrewPosterEntity>create(CrewPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID + ":midnight_crew_poster").setRegistryName("midnight_crew_poster"));
		registry.register(EntityType.Builder.<SbahjPosterEntity>create(SbahjPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID + ":sbahj_poster").setRegistryName("sbahj_poster"));
		registry.register(EntityType.Builder.<ShopPosterEntity>create(ShopPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID + ":shop_poster").setRegistryName("shop_poster"));
		registry.register(EntityType.Builder.<HologramEntity>create(HologramEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID + ":hologram").setRegistryName("hologram"));
	}
	
	public static void registerPlacements()
	{
		EntitySpawnPlacementRegistry.register(DERSITE_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_PAWN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
		EntitySpawnPlacementRegistry.register(DERSITE_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_BISHOP, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
		EntitySpawnPlacementRegistry.register(DERSITE_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_ROOK, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::func_223315_a);
	}
	
	private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, String name, EntityType.Builder<T> builder)
	{
		EntityType<T> type = builder.build(name);
		registry.register(type.setRegistryName(name));
		return type;
	}
}