package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.model.entity.BishopModel;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.client.model.entity.RookModel;
import com.mraof.minestuck.client.model.armor.ArmorModels;
import com.mraof.minestuck.client.model.armor.CrumplyHatModel;
import com.mraof.minestuck.client.model.armor.DreamerPajamasModel;
import com.mraof.minestuck.client.renderer.blockentity.*;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.entity.PawnRenderer;
import com.mraof.minestuck.client.renderer.entity.frog.FrogRenderer;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.computer.SburbClient;
import com.mraof.minestuck.computer.SburbServer;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.StoneTabletItem;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ClientProxy
{
	private static void registerRenderers()
	{
		BlockEntityRenderers.register(MSBlockEntityTypes.SKAIA_PORTAL.get(), SkaiaPortalRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.GATE.get(), GateRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.RETURN_NODE.get(), ReturnNodeRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.HOLOPAD.get(), HolopadRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.TOTEM_LATHE_DOWEL.get(), TotemLatheRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.ALCHEMITER.get(), AlchemiterRenderer::new);
		BlockEntityRenderers.register(MSBlockEntityTypes.HORSE_CLOCK.get(), HorseClockRenderer::new);
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new CardRenderer());
	}
	
	public static void init()
	{
		registerRenderers();
		
		MSScreenFactories.registerScreenFactories();

		EntityRenderers.register(MSEntityTypes.FROG.get(), FrogRenderer::new);
		EntityRenderers.register(MSEntityTypes.HOLOGRAM.get(), HologramRenderer::new);
		EntityRenderers.register(MSEntityTypes.LOTUS_FLOWER.get(), LotusFlowerRenderer::new);
		EntityRenderers.register(MSEntityTypes.NAKAGATOR.get(), ConsortRenderer::new);
		EntityRenderers.register(MSEntityTypes.SALAMANDER.get(), ConsortRenderer::new);
		EntityRenderers.register(MSEntityTypes.IGUANA.get(), ConsortRenderer::new);
		EntityRenderers.register(MSEntityTypes.TURTLE.get(), ConsortRenderer::new);
		EntityRenderers.register(MSEntityTypes.IMP.get(), UnderlingRenderer::new);
		EntityRenderers.register(MSEntityTypes.OGRE.get(), UnderlingRenderer::new);
		EntityRenderers.register(MSEntityTypes.BASILISK.get(), UnderlingRenderer::new);
		EntityRenderers.register(MSEntityTypes.LICH.get(), UnderlingRenderer::new);
		EntityRenderers.register(MSEntityTypes.GICLOPS.get(), UnderlingRenderer::new);
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_BISHOP.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(manager.bakeLayer(MSModelLayers.BISHOP)), 1.8F, MSEntityTypes.PROSPITIAN_BISHOP.get()));
		EntityRenderers.register(MSEntityTypes.DERSITE_BISHOP.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(manager.bakeLayer(MSModelLayers.BISHOP)), 1.8F, MSEntityTypes.DERSITE_BISHOP.get()));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_ROOK.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(manager.bakeLayer(MSModelLayers.ROOK)), 2.5F, MSEntityTypes.PROSPITIAN_ROOK.get()));
		EntityRenderers.register(MSEntityTypes.DERSITE_ROOK.get(), manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(manager.bakeLayer(MSModelLayers.ROOK)), 2.5F, MSEntityTypes.DERSITE_ROOK.get()));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_PAWN.get(), PawnRenderer::new);
		EntityRenderers.register(MSEntityTypes.DERSITE_PAWN.get(), PawnRenderer::new);
		EntityRenderers.register(MSEntityTypes.GRIST.get(), GristRenderer::new);
		EntityRenderers.register(MSEntityTypes.VITALITY_GEL.get(), VitalityGelRenderer::new);
		EntityRenderers.register(MSEntityTypes.PLAYER_DECOY.get(), DecoyRenderer::new);
		EntityRenderers.register(MSEntityTypes.METAL_BOAT.get(), MetalBoatRenderer::new);
		EntityRenderers.register(MSEntityTypes.BARBASOL_BOMB.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.CONSUMABLE_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.RETURNING_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.BOUNCING_PROJECTILE.get(), ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.MIDNIGHT_CREW_POSTER.get(), manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:midnight_poster")));
		EntityRenderers.register(MSEntityTypes.SBAHJ_POSTER.get(), manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:sbahj_poster")));
		EntityRenderers.register(MSEntityTypes.SHOP_POSTER.get(), manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:shop_poster")));
		
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PUNCH_DESIGNIX.SLOT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.ALCHEMITER.TOTEM_PAD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.ALCHEMITER.TOTEM_CORNER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TOTEM_LATHE.CARD_SLOT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TOTEM_LATHE.BOTTOM_LEFT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.HOLOPAD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CRUXITE_DOWEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLENDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CHESSBOARD.get(), RenderType.cutout());
		//RenderTypeLookup.setRenderLayer(MSBlocks.SPIKES, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.MINI_FROG_STATUE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.MINI_WIZARD_STATUE.get(), RenderType.cutout());
		//TODO check for these
		//RenderTypeLookup.setRenderLayer(MSBlocks.MINI_TYPHEUS_STATUE, RenderType.cutout());
		//RenderTypeLookup.setRenderLayer(MSBlocks.NAKAGATOR_STATUE, RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CASSETTE_PLAYER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PIPE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PARCEL_PYXIS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.GLOWYSTONE_DUST.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.GOLD_SEEDS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.RAINBOW_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.END_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BREATH_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.LIFE_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.LIGHT_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TIME_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.HEART_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.RAGE_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLOOD_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.DOOM_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.VOID_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.SPACE_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.MIND_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.HOPE_ASPECT_SAPLING.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.GLOWING_MUSHROOM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.DESERT_BUSH.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLOOMING_CACTUS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PETRIFIED_GRASS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PETRIFIED_POPPY.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.ATTACHED_STRAWBERRY_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.STRAWBERRY_STEM.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TALL_END_GRASS.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.GLOWFLOWER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CHECKERED_STAINED_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLACK_CROWN_STAINED_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLACK_PAWN_STAINED_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.WHITE_CROWN_STAINED_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.WHITE_PAWN_STAINED_GLASS.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.LUNCHTOP.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.PLATFORM_BLOCK.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.ITEM_MAGNET.get(), RenderType.translucent());
		
		MSKeyHandler.registerKeys();
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);
		
		registerArmorModels();

		ItemPropertyFunction content = (stack, level, holder, seed) -> AlchemyHelper.hasDecodedItem(stack) ? 1 : 0;
		ResourceLocation contentName = new ResourceLocation(Minestuck.MOD_ID, "content");
		
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), contentName, content);
		ItemProperties.register(MSItems.CRUXITE_DOWEL.get(), contentName, content);
		ItemProperties.register(MSItems.SHUNT.get(), contentName, content);
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), new ResourceLocation(Minestuck.MOD_ID, "punched"), (stack, level, holder, seed) -> AlchemyHelper.isPunchedCard(stack) ? 1 : 0);
		ItemProperties.register(MSItems.CAPTCHA_CARD.get(), new ResourceLocation(Minestuck.MOD_ID, "ghost"), (stack, level, holder, seed) -> AlchemyHelper.isGhostCard(stack) ? 1 : 0);
		
		ItemProperties.register(MSItems.BOONDOLLARS.get(), new ResourceLocation(Minestuck.MOD_ID, "count"), (stack, level, holder, seed) -> BoondollarsItem.getCount(stack));
		ItemProperties.register(MSItems.FROG.get(), new ResourceLocation(Minestuck.MOD_ID, "type"), (stack, level, holder, seed) -> !stack.hasTag() ? 0 : stack.getTag().getInt("Type"));
		ItemProperties.register(MSItems.STONE_TABLET.get(), new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, level, holder, seed) -> StoneTabletItem.hasText(stack) ? 1 : 0);
		
		DimensionSpecialEffects.EFFECTS.put(MSDimensions.LAND_EFFECTS, new LandRenderInfo());
	}
	
	private static void registerArmorModels()
	{
		ArmorModels.register(MSItems.CRUMPLY_HAT.get(), new HumanoidModel<>(CrumplyHatModel.createBodyLayer().bakeRoot()));
		
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
	
	/**
	 * Used to prevent a crash in PlayToClientPackets when loading ClientPlayerEntity on a dedicated server
	 */
	public static Player getClientPlayer()
	{
		Minecraft mc = Minecraft.getInstance();
		return mc.player;
	}
}