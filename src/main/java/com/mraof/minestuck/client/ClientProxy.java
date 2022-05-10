package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.model.*;
import com.mraof.minestuck.client.model.armor.CrumplyHatModel;
import com.mraof.minestuck.client.model.armor.DreamerPajamasModel;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.entity.frog.FrogRenderer;
import com.mraof.minestuck.client.renderer.tileentity.GateRenderer;
import com.mraof.minestuck.client.renderer.tileentity.HolopadRenderer;
import com.mraof.minestuck.client.renderer.tileentity.SkaiaPortalRenderer;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.computer.SburbClient;
import com.mraof.minestuck.computer.SburbServer;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.StoneTabletItem;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.world.MSDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy
{
	private static void registerRenderers()
	{
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.SKAIA_PORTAL.get(), SkaiaPortalRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.GATE.get(), GateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.HOLOPAD.get(), HolopadRenderer::new);
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new CardRenderer());
	}
	
	public static void init()
	{
		registerRenderers();
		
		MSScreenFactories.registerScreenFactories();

		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.FROG, FrogRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.HOLOGRAM, HologramRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.LOTUS_FLOWER, LotusFlowerRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.NAKAGATOR, manager -> new SimpleTexturedEntityRenderer<>(manager, new NakagatorModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SALAMANDER, manager -> new SimpleTexturedEntityRenderer<>(manager, new SalamanderModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.IGUANA, manager -> new SimpleTexturedEntityRenderer<>(manager, new IguanaModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.TURTLE, manager -> new SimpleTexturedEntityRenderer<>(manager, new TurtleModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.IMP, manager -> new UnderlingEntityRenderer<>(manager, new ImpModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.OGRE, manager -> new UnderlingEntityRenderer<>(manager, new OgreModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.BASILISK, manager -> new UnderlingEntityRenderer<>(manager, new BasiliskModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.LICH, manager -> new UnderlingEntityRenderer<>(manager, new LichModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.GICLOPS, manager -> new UnderlingEntityRenderer<>(manager, new GiclopsModel<>(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.WYRM, manager -> new ShadowRenderer<>(manager, 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_BISHOP, manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_BISHOP, manager -> new SimpleTexturedEntityRenderer<>(manager, new BishopModel<>(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_ROOK, manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_ROOK, manager -> new SimpleTexturedEntityRenderer<>(manager, new RookModel<>(), 2.5F));
		//RenderingRegistry.registerEntityRenderingHandler(UnderlingPartEntity.class, manager -> new ShadowRenderer<>(manager, 2.8F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new ShadowRenderer<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_PAWN, manager -> new PawnRenderer(manager, new BipedModel<>(1.0F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_PAWN, manager -> new PawnRenderer(manager, new BipedModel<>(1.0F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.GRIST, GristRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.VITALITY_GEL, VitalityGelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PLAYER_DECOY, DecoyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.METAL_BOAT, MetalBoatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.BARBASOL_BOMB, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.CONSUMABLE_PROJECTILE, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.RETURNING_PROJECTILE, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.BOUNCING_PROJECTILE, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.MIDNIGHT_CREW_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:midnight_poster")));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SBAHJ_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:sbahj_poster")));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SHOP_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:shop_poster")));
		
		RenderTypeLookup.setRenderLayer(MSBlocks.ALCHEMITER.TOTEM_PAD.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.TOTEM_LATHE.CARD_SLOT.get(), RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.HOLOPAD, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CRUXITE_DOWEL, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLENDER, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CHESSBOARD, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.SPIKES, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MINI_FROG_STATUE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MINI_WIZARD_STATUE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MINI_TYPHEUS_STATUE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.NAKAGATOR_STATUE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CASSETTE_PLAYER, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PIPE, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PARCEL_PYXIS, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWYSTONE_DUST, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GOLD_SEEDS, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.RAINBOW_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.END_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BREATH_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.LIFE_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.LIGHT_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.TIME_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.HEART_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.RAGE_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLOOD_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.DOOM_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.VOID_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.SPACE_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MIND_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.HOPE_ASPECT_SAPLING, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWING_MUSHROOM, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.DESERT_BUSH, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLOOMING_CACTUS, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PETRIFIED_GRASS, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PETRIFIED_POPPY, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.TALL_END_GRASS, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWFLOWER, RenderType.cutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CHECKERED_STAINED_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLACK_CROWN_STAINED_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLACK_PAWN_STAINED_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.WHITE_CROWN_STAINED_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.WHITE_PAWN_STAINED_GLASS, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.LUNCHTOP, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.PLATFORM_BLOCK, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.ITEM_MAGNET, RenderType.translucent());
		
		MSKeyHandler.registerKeys();
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);
		
		registerArmorModels();

		IItemPropertyGetter content = (stack, world, holder) -> AlchemyHelper.hasDecodedItem(stack) ? 1 : 0;
		ResourceLocation contentName = new ResourceLocation(Minestuck.MOD_ID, "content");
		
		ItemModelsProperties.register(MSItems.CAPTCHA_CARD, contentName, content);
		ItemModelsProperties.register(MSItems.CRUXITE_DOWEL, contentName, content);
		ItemModelsProperties.register(MSItems.SHUNT, contentName, content);
		ItemModelsProperties.register(MSItems.CAPTCHA_CARD, new ResourceLocation(Minestuck.MOD_ID, "punched"), (stack, world, holder) -> AlchemyHelper.isPunchedCard(stack) ? 1 : 0);
		ItemModelsProperties.register(MSItems.CAPTCHA_CARD, new ResourceLocation(Minestuck.MOD_ID, "ghost"), (stack, world, holder) -> AlchemyHelper.isGhostCard(stack) ? 1 : 0);
		
		ItemModelsProperties.register(MSItems.BOONDOLLARS, new ResourceLocation(Minestuck.MOD_ID, "count"), (stack, world, holder) -> BoondollarsItem.getCount(stack));
		ItemModelsProperties.register(MSItems.FROG, new ResourceLocation(Minestuck.MOD_ID, "type"), (stack, world, holder) -> !stack.hasTag() ? 0 : stack.getTag().getInt("Type"));
		ItemModelsProperties.register(MSItems.STONE_SLAB, new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, world, holder) -> StoneTabletItem.hasText(stack) ? 1 : 0);
		
		DimensionRenderInfo.EFFECTS.put(MSDimensions.LAND_EFFECTS, new LandRenderInfo());
	}
	
	private static void registerArmorModels()
	{
		MSItems.CRUMPLY_HAT.setArmorModel(new CrumplyHatModel());
		DreamerPajamasModel pajamasModel = new DreamerPajamasModel();
		MSItems.PROSPIT_CIRCLET.setArmorModel(pajamasModel);
		MSItems.PROSPIT_SHIRT.setArmorModel(pajamasModel);
		MSItems.PROSPIT_PANTS.setArmorModel(pajamasModel);
		MSItems.PROSPIT_SHOES.setArmorModel(pajamasModel);
		MSItems.DERSE_CIRCLET.setArmorModel(pajamasModel);
		MSItems.DERSE_SHIRT.setArmorModel(pajamasModel);
		MSItems.DERSE_PANTS.setArmorModel(pajamasModel);
		MSItems.DERSE_SHOES.setArmorModel(pajamasModel);
	}
	
	/**
	 * Used to prevent a crash in PlayToClientPackets when loading ClientPlayerEntity on a dedicated server
	 */
	public static PlayerEntity getClientPlayer()
	{
		Minecraft mc = Minecraft.getInstance();
		return mc.player;
	}
}