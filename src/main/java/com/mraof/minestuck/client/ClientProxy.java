package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.client.model.armor.*;
import com.mraof.minestuck.client.model.entity.BishopModel;
import com.mraof.minestuck.client.model.entity.RookModel;
import com.mraof.minestuck.client.particles.ExhaustParticle;
import com.mraof.minestuck.client.particles.PlasmaParticle;
import com.mraof.minestuck.client.particles.TransportalizerParticle;
import com.mraof.minestuck.client.renderer.blockentity.*;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.entity.frog.FrogRenderer;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.*;
import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.StructureScannerItem;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.StoneTabletTextComponent;
import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = Minestuck.MOD_ID)
public class ClientProxy
{
	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event)
	{
		MSKeyHandler.registerKeys(event);
	}
	
	@SubscribeEvent
	public static void init(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(MSEntityTypes.FROG.get(), FrogRenderer::new);
		EntityRenderers.register(MSEntityTypes.LOTUS_FLOWER.get(), LotusFlowerRenderer::new);
		EntityRenderers.register(MSEntityTypes.SERVER_CURSOR.get(), ServerCursorRenderer::new);
		EntityRenderers.register(MSEntityTypes.NAKAGATOR.get(), context -> new ConsortRenderer<>(context, EnumConsort.NAKAGATOR));
		EntityRenderers.register(MSEntityTypes.SALAMANDER.get(), context -> new ConsortRenderer<>(context, EnumConsort.SALAMANDER));
		EntityRenderers.register(MSEntityTypes.IGUANA.get(), context -> new ConsortRenderer<>(context, EnumConsort.IGUANA));
		EntityRenderers.register(MSEntityTypes.TURTLE.get(), context -> new ConsortRenderer<>(context, EnumConsort.TURTLE));
		EntityRenderers.register(MSEntityTypes.IMP.get(), context -> new UnderlingRenderer<>(context, "imp"));
		EntityRenderers.register(MSEntityTypes.OGRE.get(), context -> new UnderlingRenderer<>(context, "ogre"));
		EntityRenderers.register(MSEntityTypes.BASILISK.get(), context -> new UnderlingRenderer<>(context, "basilisk"));
		EntityRenderers.register(MSEntityTypes.LICH.get(), context -> new UnderlingRenderer<>(context, "lich"));
		EntityRenderers.register(MSEntityTypes.GICLOPS.get(), context -> new UnderlingRenderer<>(context, "giclops"));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_BISHOP.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(manager.bakeLayer(MSModelLayers.BISHOP)), 1.8F, MSEntityTypes.PROSPITIAN_BISHOP.get()));
		EntityRenderers.register(MSEntityTypes.DERSITE_BISHOP.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(manager.bakeLayer(MSModelLayers.BISHOP)), 1.8F, MSEntityTypes.DERSITE_BISHOP.get()));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_ROOK.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(manager.bakeLayer(MSModelLayers.ROOK)), 2.5F, MSEntityTypes.PROSPITIAN_ROOK.get()));
		EntityRenderers.register(MSEntityTypes.DERSITE_ROOK.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(manager.bakeLayer(MSModelLayers.ROOK)), 2.5F, MSEntityTypes.DERSITE_ROOK.get()));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_PAWN.get(), context -> new PawnRenderer(context, EnumEntityKingdom.PROSPITIAN));
		EntityRenderers.register(MSEntityTypes.DERSITE_PAWN.get(), context -> new PawnRenderer(context, EnumEntityKingdom.DERSITE));
		EntityRenderers.register(MSEntityTypes.GRIST.get(), GristRenderer::new);
		EntityRenderers.register(MSEntityTypes.VITALITY_GEL.get(), VitalityGelRenderer::new);
		EntityRenderers.register(MSEntityTypes.PLAYER_DECOY.get(), DecoyRenderer::new);
		EntityRenderers.register(MSEntityTypes.METAL_BOAT.get(), MetalBoatRenderer::new);
		EntityRenderers.register(MSEntityTypes.BARBASOL_BOMB.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.CONSUMABLE_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.RETURNING_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.BOUNCING_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.POSTER.get(), PosterRenderer::new);
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);
		ComputerProgram.registerProgramClass(2, DiskBurner.class);
		ComputerProgram.registerProgramClass(3, SettingsApp.class);
		
		registerArmorModels();

		ItemBlockRenderTypes.setRenderLayer(MSFluids.OIL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.FLOWING_OIL.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.BLOOD.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.FLOWING_BLOOD.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.BRAIN_JUICE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.FLOWING_BRAIN_JUICE.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.WATER_COLORS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.FLOWING_WATER_COLORS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.LIGHT_WATER.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSFluids.FLOWING_LIGHT_WATER.get(), RenderType.translucent());
		//ender fluid has non-transparent texture
		
		ItemPropertyFunction encoded = (stack, level, holder, seed) -> stack.has(MSItemComponents.ENCODED_ITEM) ? 1 : 0;
		
		ItemProperties.register(MSItems.CRUXITE_DOWEL.get(), Minestuck.id("content"), encoded);
		ItemProperties.register(MSItems.SHUNT.get(), Minestuck.id("content"), encoded);
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), Minestuck.id("punched"), encoded);
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), Minestuck.id("content"), (stack, level, holder, seed) -> stack.has(MSItemComponents.CARD_STORED_ITEM) ? 1 : 0);
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), Minestuck.id("ghost"), (stack, level, holder, seed) -> stack.getOrDefault(MSItemComponents.CARD_STORED_ITEM, CardStoredItemComponent.EMPTY).isGhostItem() ? 1 : 0);
		
		ItemProperties.register(MSItems.BOONDOLLARS.get(), Minestuck.id("count"), (stack, level, holder, seed) -> BoondollarsItem.getCount(stack));
		ItemProperties.register(MSItems.FROG.get(), Minestuck.id("type"), (stack, level, holder, seed) -> stack.has(MSItemComponents.FROG_TRAITS) ? stack.get(MSItemComponents.FROG_TRAITS).variant().orElse(FrogEntity.FrogVariants.DEFAULT).ordinal() : FrogEntity.FrogVariants.DEFAULT.ordinal());
		ItemProperties.register(MSItems.STONE_TABLET.get(), Minestuck.id("carved"), (stack, level, holder, seed) -> StoneTabletTextComponent.hasText(stack) ? 1 : 0);
		ItemProperties.register(MSItems.MUSIC_SWORD.get(), Minestuck.id("has_cassette"), (stack, level, holder, seed) -> MusicPlayerWeapon.hasCassette(stack) ? 1 : 0);
		ItemProperties.register(MSItems.BOOMBOX_BEATER.get(), Minestuck.id("has_cassette"), (stack, level, holder, seed) -> MusicPlayerWeapon.hasCassette(stack) ? 1 : 0);
		
		ItemProperties.register(MSItems.TEMPLE_SCANNER.get(), Minestuck.id("angle"), new CompassItemPropertyFunction((level, stack, entity) -> stack.get(MSItemComponents.TARGET_LOCATION)));
		ItemProperties.register(MSItems.TEMPLE_SCANNER.get(), Minestuck.id("powered"), (stack, level, entity, seed) -> StructureScannerItem.isPowered(stack) ? 1 : 0);
	}
	
	@SubscribeEvent
	public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
		
		event.registerBlockEntityRenderer(MSBlockEntityTypes.SKAIA_PORTAL.get(), SkaiaPortalRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.GATE.get(), GateRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.RETURN_NODE.get(), ReturnNodeRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.HOLOPAD.get(), HolopadRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.TOTEM_LATHE_DOWEL.get(), TotemLatheRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.ALCHEMITER.get(), AlchemiterRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.HORSE_CLOCK.get(), HorseClockRenderer::new);
		
		event.registerBlockEntityRenderer(MSBlockEntityTypes.SIGN.get(), SignRenderer::new);
		event.registerBlockEntityRenderer(MSBlockEntityTypes.HANGING_SIGN.get(), HangingSignRenderer::new);
	}
	
	@SubscribeEvent
	public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event)
	{
		event.register(MSDimensions.LAND_EFFECTS, new LandRenderInfo());
	}
	
	private static void registerArmorModels()
	{
		ArmorModels.register(MSItems.CRUMPLY_HAT.get(), new HumanoidModel<>(CrumplyHatModel.createBodyLayer().bakeRoot()));
		ArmorModels.register(MSItems.AMPHIBEANIE.get(), new HumanoidModel<>(AmphibeanieModel.createBodyLayer().bakeRoot()));
		ArmorModels.register(MSItems.NOSTRILDAMUS.get(), new HumanoidModel<>(NostrildamusModel.createBodyLayer().bakeRoot()));
		ArmorModels.register(MSItems.PONYTAIL.get(), new HumanoidModel<>(PonytailModel.createBodyLayer().bakeRoot()));
		
		HumanoidModel<?> pajamasModel = new HumanoidModel<>(DreamerPajamasModel.createBodyLayer().bakeRoot());
		ArmorModels.register(MSItems.PROSPIT_CIRCLET.get(), pajamasModel);
		ArmorModels.register(MSItems.PROSPIT_SHIRT.get(), pajamasModel);
		ArmorModels.register(MSItems.PROSPIT_PANTS.get(), pajamasModel);
		ArmorModels.register(MSItems.PROSPIT_SHOES.get(), pajamasModel);
		ArmorModels.register(MSItems.DERSE_CIRCLET.get(), pajamasModel);
		ArmorModels.register(MSItems.DERSE_SHIRT.get(), pajamasModel);
		ArmorModels.register(MSItems.DERSE_PANTS.get(), pajamasModel);
		ArmorModels.register(MSItems.DERSE_SHOES.get(), pajamasModel);
	}
	
	@SubscribeEvent
	public static void registerFactories(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(MSParticleType.TRANSPORTALIZER.get(), TransportalizerParticle.Provider::new);
		event.registerSpriteSet(MSParticleType.PLASMA.get(), PlasmaParticle.Provider::new);
		event.registerSpriteSet(MSParticleType.EXHAUST.get(), ExhaustParticle.Provider::new);
	}
	
	/**
	 * Used to prevent a crash in PlayToClientPackets when loading ClientPlayerEntity on a dedicated server
	 */
	public static Player getClientPlayer()
	{
		Minecraft mc = Minecraft.getInstance();
		return mc.player;
	}
}
