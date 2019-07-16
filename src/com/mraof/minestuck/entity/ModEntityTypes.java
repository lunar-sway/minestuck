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
	public static EntityType<FrogEntity> FROG = EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE).size(0.51F, 0.51F).build(Minestuck.MOD_ID+":frog");
	public static EntityType<SalamanderEntity> SALAMANDER = EntityType.Builder.create(SalamanderEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":salamander");
	public static EntityType<TurtleEntity> TURTLE = EntityType.Builder.create(TurtleEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":turtle");
	public static EntityType<NakagatorEntity> NAKAGATOR = EntityType.Builder.create(NakagatorEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":nakagator");
	public static EntityType<IguanaEntity> IGUANA = EntityType.Builder.create(IguanaEntity::new, EntityClassification.CREATURE).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":iguana");
	
	public static EntityType<ImpEntity> IMP = EntityType.Builder.create(ImpEntity::new, EntityClassification.MONSTER).size(0.75F, 1.5F).build(Minestuck.MOD_ID+":imp");
	public static EntityType<OgreEntity> OGRE = EntityType.Builder.create(OgreEntity::new, EntityClassification.MONSTER).size(3.0F, 4.5F).build(Minestuck.MOD_ID+":ogre");
	public static EntityType<BasiliskEntity> BASILISK = EntityType.Builder.create(BasiliskEntity::new, EntityClassification.MONSTER).size(3F, 2F).build(Minestuck.MOD_ID+":basilisk");
	public static EntityType<LichEntity> LICH = EntityType.Builder.create(LichEntity::new, EntityClassification.MONSTER).size(1.0F, 2.0F).build(Minestuck.MOD_ID+":lich");
	public static EntityType<GiclopsEntity> GICLOPS = EntityType.Builder.create(GiclopsEntity::new, EntityClassification.MONSTER).size(8.0F, 12.0F).build(Minestuck.MOD_ID+":giclops");
	public static EntityType<WyrmEntity> WYRM = EntityType.Builder.create(WyrmEntity::new, EntityClassification.MONSTER).build(Minestuck.MOD_ID+":wyrm");
	
	public static EntityType<BlackPawnEntity> DERSITE_PAWN = EntityType.Builder.create(BlackPawnEntity::new, EntityClassification.MISC).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":dersite_pawn");
	public static EntityType<WhitePawnEntity> PROSPITIAN_PAWN = EntityType.Builder.create(WhitePawnEntity::new, EntityClassification.MISC).size(0.6F, 1.5F).build(Minestuck.MOD_ID+":prospitian_pawn");
	public static EntityType<BlackBishopEntity> DERSITE_BISHOP = EntityType.Builder.create(BlackBishopEntity::new, EntityClassification.MISC).size(1.9F, 4.1F).build(Minestuck.MOD_ID+":dersite_bishop");
	public static EntityType<WhiteBishopEntity> PROSPITIAN_BISHOP = EntityType.Builder.create(WhiteBishopEntity::new, EntityClassification.MISC).size(1.9F, 4.1F).build(Minestuck.MOD_ID+":prospitian_bishop");
	public static EntityType<BlackRookEntity> DERSITE_ROOK = EntityType.Builder.create(BlackRookEntity::new, EntityClassification.MISC).size(3.5F, 3.5F).build(Minestuck.MOD_ID+":dersite_rook");
	public static EntityType<WhiteRookEntity> PROSPITIAN_ROOK = EntityType.Builder.create(WhiteRookEntity::new, EntityClassification.MISC).size(3.5F, 3.5F).build(Minestuck.MOD_ID+":prospitian_rook");
	
	public static EntityType<GristEntity> GRIST = EntityType.Builder.<GristEntity>create(GristEntity::new, EntityClassification.MISC).size(1/3F, 1/3F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID+":grist");
	public static EntityType<VitalityGelEntity> VITALITY_GEL = EntityType.Builder.<VitalityGelEntity>create(VitalityGelEntity::new, EntityClassification.MISC).size(1/4F, 1/4F)/*.tracker(512, 1, true)*/.immuneToFire().build(Minestuck.MOD_ID+":vitality_gel");
	public static EntityType<DecoyEntity> DECOY = EntityType.Builder.<DecoyEntity>create(EntityClassification.MISC).disableSerialization().disableSummoning().build(Minestuck.MOD_ID+":decoy");
	
	public static EntityType<MetalBoatEntity> METAL_BOAT = EntityType.Builder.<MetalBoatEntity>create(MetalBoatEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":metal_boat");
	public static EntityType<CrewPosterEntity> CREW_POSTER = EntityType.Builder.<CrewPosterEntity>create(CrewPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":midnight_crew_poster");
	public static EntityType<SbahjPosterEntity> SBAHJ_POSTER = EntityType.Builder.<SbahjPosterEntity>create(SbahjPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":sbahj_poster");
	public static EntityType<ShopPosterEntity> SHOP_POSTER = EntityType.Builder.<ShopPosterEntity>create(ShopPosterEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":shop_poster");
	public static EntityType<HologramEntity> HOLOGRAM =EntityType.Builder.<HologramEntity>create(HologramEntity::new, EntityClassification.MISC).build(Minestuck.MOD_ID+":hologram");

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