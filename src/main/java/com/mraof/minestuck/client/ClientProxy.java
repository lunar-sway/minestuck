package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.model.*;
import com.mraof.minestuck.client.model.armor.ArmorModels;
import com.mraof.minestuck.client.model.armor.CrumplyHatModel;
import com.mraof.minestuck.client.model.armor.DreamerPajamasModel;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.entity.frog.FrogRenderer;
import com.mraof.minestuck.client.renderer.blockentity.GateRenderer;
import com.mraof.minestuck.client.renderer.blockentity.HolopadRenderer;
import com.mraof.minestuck.client.renderer.blockentity.ReturnNodeRenderer;
import com.mraof.minestuck.client.renderer.blockentity.SkaiaPortalRenderer;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.computer.DiskBurner;
import com.mraof.minestuck.computer.SburbClient;
import com.mraof.minestuck.computer.SburbServer;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.StoneTabletItem;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
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
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new CardRenderer());
	}
	
	public static void init()
	{
		registerRenderers();
		
		MSScreenFactories.registerScreenFactories();

		EntityRenderers.register(MSEntityTypes.FROG, FrogRenderer::new);
		EntityRenderers.register(MSEntityTypes.HOLOGRAM, HologramRenderer::new);
		EntityRenderers.register(MSEntityTypes.LOTUS_FLOWER, LotusFlowerRenderer::new);
		EntityRenderers.register(MSEntityTypes.NAKAGATOR, context -> new SimpleTexturedEntityRenderer<>(context, new NakagatorModel<>(context.bakeLayer(MSModelLayers.NAKAGATOR)), 0.5F));
		EntityRenderers.register(MSEntityTypes.SALAMANDER, context -> new SimpleTexturedEntityRenderer<>(context, new SalamanderModel<>(context.bakeLayer(MSModelLayers.SALAMANDER)), 0.5F));
		EntityRenderers.register(MSEntityTypes.IGUANA, context -> new SimpleTexturedEntityRenderer<>(context, new IguanaModel<>(context.bakeLayer(MSModelLayers.IGUANA)), 0.5F));
		EntityRenderers.register(MSEntityTypes.TURTLE, context -> new SimpleTexturedEntityRenderer<>(context, new TurtleModel<>(context.bakeLayer(MSModelLayers.TURTLE)), 0.5F));
		EntityRenderers.register(MSEntityTypes.IMP, context -> new UnderlingEntityRenderer<>(context, new ImpModel<>(context.bakeLayer(MSModelLayers.IMP)), 0.5F));
		EntityRenderers.register(MSEntityTypes.OGRE, context -> new UnderlingEntityRenderer<>(context, new OgreModel<>(context.bakeLayer(MSModelLayers.OGRE)), 2.8F));
		EntityRenderers.register(MSEntityTypes.BASILISK, context -> new UnderlingEntityRenderer<>(context, new BasiliskModel<>(context.bakeLayer(MSModelLayers.BASILISK)), 2.8F));
		EntityRenderers.register(MSEntityTypes.LICH, context -> new UnderlingEntityRenderer<>(context, new LichModel<>(context.bakeLayer(MSModelLayers.LICH)), 0.5F));
		EntityRenderers.register(MSEntityTypes.GICLOPS, context -> new UnderlingEntityRenderer<>(context, new GiclopsModel<>(context.bakeLayer(MSModelLayers.GICLOPS)), 7.6F));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_BISHOP, context -> new SimpleTexturedEntityRenderer<>(context, new BishopModel<>(context.bakeLayer(MSModelLayers.BISHOP)), 1.8F));
		EntityRenderers.register(MSEntityTypes.DERSITE_BISHOP, context -> new SimpleTexturedEntityRenderer<>(context, new BishopModel<>(context.bakeLayer(MSModelLayers.BISHOP)), 1.8F));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_ROOK, context -> new SimpleTexturedEntityRenderer<>(context, new RookModel<>(context.bakeLayer(MSModelLayers.ROOK)), 2.5F));
		EntityRenderers.register(MSEntityTypes.DERSITE_ROOK, context -> new SimpleTexturedEntityRenderer<>(context, new RookModel<>(context.bakeLayer(MSModelLayers.ROOK)), 2.5F));
		EntityRenderers.register(MSEntityTypes.PROSPITIAN_PAWN, PawnRenderer::new);
		EntityRenderers.register(MSEntityTypes.DERSITE_PAWN, PawnRenderer::new);
		EntityRenderers.register(MSEntityTypes.GRIST, GristRenderer::new);
		EntityRenderers.register(MSEntityTypes.VITALITY_GEL, VitalityGelRenderer::new);
		EntityRenderers.register(MSEntityTypes.PLAYER_DECOY, DecoyRenderer::new);
		EntityRenderers.register(MSEntityTypes.METAL_BOAT, MetalBoatRenderer::new);
		EntityRenderers.register(MSEntityTypes.BARBASOL_BOMB, ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.CONSUMABLE_PROJECTILE, ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.RETURNING_PROJECTILE, ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.BOUNCING_PROJECTILE, ThrownItemRenderer::new);
		EntityRenderers.register(MSEntityTypes.MIDNIGHT_CREW_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:midnight_poster")));
		EntityRenderers.register(MSEntityTypes.SBAHJ_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:sbahj_poster")));
		EntityRenderers.register(MSEntityTypes.SHOP_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:shop_poster")));
		
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.ALCHEMITER.TOTEM_PAD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.TOTEM_LATHE.CARD_SLOT.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.HOLOPAD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CRUXITE_DOWEL.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.GREEN_STONE_BRICK_EMBEDDED_LADDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.BLENDER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.CHESSBOARD.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.MINI_FROG_STATUE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(MSBlocks.MINI_WIZARD_STATUE.get(), RenderType.cutout());
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
		ComputerProgram.registerProgramClass(2, DiskBurner.class);
		
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