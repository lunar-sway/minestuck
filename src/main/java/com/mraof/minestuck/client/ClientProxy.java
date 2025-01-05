package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.computer.*;
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
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.StructureScannerItem;
import com.mraof.minestuck.item.armor.MSArmorItem;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.StoneTabletTextComponent;
import com.mraof.minestuck.item.weapon.MusicPlayerWeapon;
import com.mraof.minestuck.util.MSParticleType;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
		
		ProgramGui.Registry.register(ProgramType.SBURB_CLIENT, SburbClientGui::new);
		ProgramGui.Registry.register(ProgramType.SBURB_SERVER, SburbServerGui::new);
		ProgramGui.Registry.register(ProgramType.DISK_BURNER, DiskBurnerGui::new);
		ProgramGui.Registry.register(ProgramType.SETTINGS, SettingsAppGui::new);
		
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
	public static void registerBER(EntityRenderersEvent.RegisterRenderers event)
	{
		
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
	
	@SubscribeEvent
	private static void registerExtensions(RegisterClientExtensionsEvent event)
	{
		event.registerBlock(new IClientBlockExtensions()
		{
			@Override
			public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine manager)
			{
				return true;
			}
		}, MSBlocks.GATE, MSBlocks.GATE_MAIN, MSBlocks.RETURN_NODE, MSBlocks.RETURN_NODE_MAIN);
		
		for(DeferredHolder<FluidType, FluidType> fluidType : Arrays.asList(MSFluids.OIL_TYPE, MSFluids.LIGHT_WATER_TYPE,
				MSFluids.BLOOD_TYPE, MSFluids.BRAIN_JUICE_TYPE, MSFluids.WATER_COLORS_TYPE, MSFluids.ENDER_TYPE,
				MSFluids.CAULK_TYPE, MSFluids.MOLTEN_AMBER_TYPE))
		{
			event.registerFluidType(new IClientFluidTypeExtensions()
			{
				private final ResourceLocation stillTexture = fluidType.getId().withPrefix("block/still_");
				private final ResourceLocation flowingTexture = fluidType.getId().withPrefix("block/flowing_");
				
				@Override
				public ResourceLocation getStillTexture()
				{
					return stillTexture;
				}
				
				@Override
				public ResourceLocation getFlowingTexture()
				{
					return flowingTexture;
				}
			}, fluidType);
		}
		
		for(DeferredItem<MSArmorItem> armorItem : Arrays.asList(
				MSItems.PROSPIT_CIRCLET, MSItems.PROSPIT_SHIRT, MSItems.PROSPIT_PANTS, MSItems.PROSPIT_SHOES,
				MSItems.DERSE_CIRCLET, MSItems.DERSE_SHIRT, MSItems.DERSE_PANTS, MSItems.DERSE_SHOES,
				MSItems.AMPHIBEANIE, MSItems.NOSTRILDAMUS, MSItems.PONYTAIL, MSItems.CRUMPLY_HAT))
		{
			event.registerItem(new IClientItemExtensions()
			{
				@Override
				public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
				{
					ArmorItem.Type type = armorItem.get().getType();
					if(equipmentSlot != type.getSlot())
						return original;
					HumanoidModel<?> model = ArmorModels.get(armorItem.get());
					if(model == null)
						return original;
					
					model.rightLeg.visible = type == ArmorItem.Type.LEGGINGS || type == ArmorItem.Type.BOOTS;
					model.leftLeg.visible = type == ArmorItem.Type.LEGGINGS || type == ArmorItem.Type.BOOTS;
					
					model.body.visible = type == ArmorItem.Type.CHESTPLATE;
					model.leftArm.visible = type == ArmorItem.Type.CHESTPLATE;
					model.rightArm.visible = type == ArmorItem.Type.CHESTPLATE;
					
					model.head.visible = type == ArmorItem.Type.HELMET;
					model.hat.visible = type == ArmorItem.Type.HELMET;
					
					
					model.crouching = original.crouching;
					model.riding = original.riding;
					model.young = original.young;
					
					model.rightArmPose = original.rightArmPose;
					model.leftArmPose = original.leftArmPose;
					
					return model;
					
				}
			}, armorItem);
		}
		
		event.registerItem(new IClientItemExtensions()
		{
			private GeoArmorRenderer<?> renderer;
			
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				if(this.renderer == null)
					this.renderer = new GeoArmorRenderer<>(new PrismarineArmorModel());
				
				Minecraft mc = Minecraft.getInstance();
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original, mc.renderBuffers().bufferSource(),
						mc.getTimer().getGameTimeDeltaPartialTick(true), 0, 0, 0, 0);
				return this.renderer;
			}
		}, MSItems.PRISMARINE_HELMET, MSItems.PRISMARINE_CHESTPLATE, MSItems.PRISMARINE_LEGGINGS, MSItems.PRISMARINE_BOOTS);
		
		event.registerItem(new IClientItemExtensions()
		{
			private GeoArmorRenderer<?> renderer;
			
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				if(this.renderer == null)
					this.renderer = new GeoArmorRenderer<>(new IronLassArmorModel());
				
				Minecraft mc = Minecraft.getInstance();
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original, mc.renderBuffers().bufferSource(),
						mc.getTimer().getGameTimeDeltaPartialTick(true), 0, 0, 0, 0);
				return this.renderer;
			}
		}, MSItems.IRON_LASS_GLASSES, MSItems.IRON_LASS_CHESTPLATE, MSItems.IRON_LASS_SKIRT, MSItems.IRON_LASS_SHOES);
	}
	
	/**
	 * Used to prevent a crash in PlayToClientPackets when loading ClientPlayerEntity on a dedicated server
	 */
	@Nullable
	public static Player getClientPlayer()
	{
		Minecraft mc = Minecraft.getInstance();
		return mc.player;
	}
}
