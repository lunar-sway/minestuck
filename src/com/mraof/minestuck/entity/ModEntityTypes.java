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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntityTypes
{
	public static EntityType<FrogEntity> FROG;
	public static EntityType<SalamanderEntity> SALAMANDER;
	public static EntityType<TurtleEntity> TURTLE;
	public static EntityType<NakagatorEntity> NAKAGATOR;
	public static EntityType<IguanaEntity> IGUANA;
	
	public static EntityType<ImpEntity> IMP;
	public static EntityType<OgreEntity> OGRE;
	public static EntityType<BasiliskEntity> BASILISK;
	public static EntityType<LichEntity> LICH;
	public static EntityType<GiclopsEntity> GICLOPS;
	public static EntityType<WyrmEntity> WYRM;
	
	public static EntityType<BlackPawnEntity> DERSITE_PAWN;
	public static EntityType<WhitePawnEntity> PROSPITIAN_PAWN;
	public static EntityType<BlackBishopEntity> DERSITE_BISHOP;
	public static EntityType<WhiteBishopEntity> PROSPITIAN_BISHOP;
	public static EntityType<BlackRookEntity> DERSITE_ROOK;
	public static EntityType<WhiteRookEntity> PROSPITIAN_ROOK;
	
	public static EntityType<GristEntity> GRIST;
	public static EntityType<VitalityGelEntity> VITALITY_GEL;
	public static EntityType<DecoyEntity> DECOY;
	
	public static EntityType<MetalBoatEntity> METAL_BOAT;
	public static EntityType<CrewPosterEntity> CREW_POSTER;
	public static EntityType<SbahjPosterEntity> SBAHJ_POSTER;
	public static EntityType<ShopPosterEntity> SHOP_POSTER;
	public static EntityType<HologramEntity> HOLOGRAM;

	@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		registry.register((FROG = EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE).size(0.51F, 0.51F).build(Minestuck.MOD_ID+":frog")).setRegistryName("frog"));
		registry.register((SALAMANDER = EntityType.Builder.create(SalamanderEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":salamander")).setRegistryName("salamander"));
		registry.register((TURTLE = EntityType.Builder.create(TurtleEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":turtle")).setRegistryName("turtle"));
		registry.register((NAKAGATOR = EntityType.Builder.create(NakagatorEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":nakagator")).setRegistryName("nakagator"));
		registry.register((IGUANA = EntityType.Builder.create(IguanaEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":iguana")).setRegistryName("iguana"));
		
		registry.register((IMP = EntityType.Builder.create(ImpEntity::new, EntityClassification.MONSTER).size(0.75F, 1.5F).build(Minestuck.MOD_ID+":imp")).setRegistryName("imp"));
		registry.register((OGRE = EntityType.Builder.create(OgreEntity::new, EntityClassification.MONSTER).size(3.0F, 4.5F).build(Minestuck.MOD_ID+":ogre")).setRegistryName("ogre"));
		registry.register((BASILISK = EntityType.Builder.create(BasiliskEntity::new, EntityClassification.MONSTER).size(3F, 2F).build(Minestuck.MOD_ID+":basilisk")).setRegistryName("basilisk"));
		registry.register((LICH = EntityType.Builder.create(LichEntity::new, EntityClassification.MONSTER).size(1.0F, 2.0F).build(Minestuck.MOD_ID+":lich")).setRegistryName("lich"));
		registry.register((GICLOPS = EntityType.Builder.create(GiclopsEntity::new, EntityClassification.MONSTER).size(8.0F, 12.0F).build(Minestuck.MOD_ID+":giclops")).setRegistryName("giclops"));
		registry.register((WYRM = EntityType.Builder.create(WyrmEntity::new, EntityClassification.MONSTER).build(Minestuck.MOD_ID+":wyrm")).setRegistryName("wyrm"));
		
		registry.register((DERSITE_PAWN = EntityType.Builder.create(BlackPawnEntity::new, EntityClassification.MISC).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":dersite_pawn")).setRegistryName("dersite_pawn"));
		registry.register((PROSPITIAN_PAWN = EntityType.Builder.create(WhitePawnEntity::new, EntityClassification.MISC).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":prospitian_pawn")).setRegistryName("prospitian_pawn"));
		registry.register((DERSITE_BISHOP = EntityType.Builder.create(BlackBishopEntity::new, EntityClassification.MISC).size(1.9F, 4.1F).build(Minestuck.MOD_ID+":dersite_bishop")).setRegistryName("dersite_bishop"));
		registry.register((PROSPITIAN_BISHOP = EntityType.Builder.create(WhiteBishopEntity::new, EntityClassification.MISC).size(1.9F, 4.1F).build(Minestuck.MOD_ID+":prospitian_bishop")).setRegistryName("prospitian_bishop"));
		registry.register((DERSITE_ROOK = EntityType.Builder.create(BlackRookEntity::new, EntityClassification.MISC).size(3.5F, 3.5F).build(Minestuck.MOD_ID+":dersite_rook")).setRegistryName("dersite_rook"));
		registry.register((PROSPITIAN_ROOK = EntityType.Builder.create(WhiteRookEntity::new, EntityClassification.MISC).size(3.5F, 3.5F).build(Minestuck.MOD_ID+":prospitian_rook")).setRegistryName("prospitian_rook"));
		
		registry.register((GRIST = EntityType.Builder.<GristEntity>create(GristEntity::new, EntityClassification.MISC).size(1/3F, 1/3F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID+":grist")).setRegistryName("grist"));
		registry.register((VITALITY_GEL = EntityType.Builder.<VitalityGelEntity>create(VitalityGelEntity::new, EntityClassification.MISC).size(1/4F, 1/4F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID+":vitality_gel")).setRegistryName("vitality_gel"));
		registry.register((DECOY = EntityType.Builder.<DecoyEntity>create(EntityClassification.MISC).disableSerialization().disableSummoning().build(Minestuck.MOD_ID+":decoy")).setRegistryName("player_decoy"));
		
		registry.register((METAL_BOAT = EntityType.Builder.<MetalBoatEntity>create(MetalBoatEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":metal_boat")).setRegistryName("metal_boat"));
		registry.register((CREW_POSTER = EntityType.Builder.<CrewPosterEntity>create(CrewPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":midnight_crew_poster")).setRegistryName("midnight_crew_poster"));
		registry.register((SBAHJ_POSTER = EntityType.Builder.<SbahjPosterEntity>create(SbahjPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":sbahj_poster")).setRegistryName("sbahj_poster"));
		registry.register((SHOP_POSTER = EntityType.Builder.<ShopPosterEntity>create(ShopPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":shop_poster")).setRegistryName("shop_poster"));
		registry.register((HOLOGRAM = EntityType.Builder.<HologramEntity>create(HologramEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":hologram")).setRegistryName("hologram"));
		
		/*EntitySpawnPlacementRegistry.register(DERSITE_PAWN, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null); TODO Wait for https://github.com/MinecraftForge/MinecraftForge/pull/5918
		EntitySpawnPlacementRegistry.register(PROSPITIAN_PAWN, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(DERSITE_BISHOP, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_BISHOP, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(DERSITE_ROOK, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);
		EntitySpawnPlacementRegistry.register(PROSPITIAN_ROOK, EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, null);*/
	}
	
	private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, String name, EntityType.Builder<T> builder)
	{
		EntityType<T> type = builder.build(name);
		registry.register(type.setRegistryName(name));
		return type;
	}
}