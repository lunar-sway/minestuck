package com.mraof.minestuck.client;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.model.*;
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
import com.mraof.minestuck.item.StoneTabletItem;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
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
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.NAKAGATOR, manager -> new MinestuckEntityRenderer<>(manager, new NakagatorModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SALAMANDER, manager -> new MinestuckEntityRenderer<>(manager, new SalamanderModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.IGUANA, manager -> new MinestuckEntityRenderer<>(manager, new IguanaModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.TURTLE, manager -> new MinestuckEntityRenderer<>(manager, new TurtleModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.IMP, manager -> new MinestuckEntityRenderer<>(manager, new ImpModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.OGRE, manager -> new MinestuckEntityRenderer<>(manager, new OgreModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.BASILISK, manager -> new MinestuckEntityRenderer<>(manager, new BasiliskModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.LICH, manager -> new MinestuckEntityRenderer<>(manager, new LichModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.GICLOPS, manager -> new MinestuckEntityRenderer<>(manager, new GiclopsModel<>(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.WYRM, manager -> new ShadowRenderer<>(manager, 1.0F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_BISHOP, manager -> new MinestuckEntityRenderer<>(manager, new BishopModel<>(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_BISHOP, manager -> new MinestuckEntityRenderer<>(manager, new BishopModel<>(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_ROOK, manager -> new MinestuckEntityRenderer<>(manager, new RookModel<>(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_ROOK, manager -> new MinestuckEntityRenderer<>(manager, new RookModel<>(), 2.5F));
		//RenderingRegistry.registerEntityRenderingHandler(UnderlingPartEntity.class, manager -> new ShadowRenderer<>(manager, 2.8F));
		//RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new ShadowRenderer<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PROSPITIAN_PAWN, manager -> new PawnRenderer(manager, new BipedModel<>(1.0F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.DERSITE_PAWN, manager -> new PawnRenderer(manager, new BipedModel<>(1.0F), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.GRIST, GristRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.VITALITY_GEL, VitalityGelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.PLAYER_DECOY, DecoyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.METAL_BOAT, MetalBoatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.BARBASOL_BOMB, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.MIDNIGHT_CREW_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:midnight_poster")));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SBAHJ_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:sbahj_poster")));
		RenderingRegistry.registerEntityRenderingHandler(MSEntityTypes.SHOP_POSTER, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:shop_poster")));

		RenderTypeLookup.setRenderLayer(MSBlocks.ALCHEMITER.TOTEM_PAD.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.TOTEM_LATHE.DOWEL_ROD.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.HOLOPAD, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CRUXITE_DOWEL, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLENDER, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CHESSBOARD, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MINI_FROG_STATUE, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CASSETTE_PLAYER, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWYSTONE_DUST, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GOLD_SEEDS, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.RAINBOW_SAPLING, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.END_SAPLING, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWING_MUSHROOM, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.DESERT_BUSH, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLOOMING_CACTUS, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PETRIFIED_GRASS, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.PETRIFIED_POPPY, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLACK_CROWN_STAINED_GLASS, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLACK_PAWN_STAINED_GLASS, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.WHITE_CROWN_STAINED_GLASS, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(MSBlocks.WHITE_PAWN_STAINED_GLASS, RenderType.getTranslucent());

		MSKeyHandler.registerKeys();
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);
		
		IItemPropertyGetter content = (stack, world, holder) -> AlchemyHelper.hasDecodedItem(stack) ? 1 : 0;
		ResourceLocation contentName = new ResourceLocation(Minestuck.MOD_ID, "content");
		
		ItemModelsProperties.registerProperty(MSItems.CAPTCHA_CARD, contentName, content);
		ItemModelsProperties.registerProperty(MSItems.CRUXITE_DOWEL, contentName, content);
		ItemModelsProperties.registerProperty(MSItems.SHUNT, contentName, content);
		ItemModelsProperties.registerProperty(MSItems.CAPTCHA_CARD, new ResourceLocation(Minestuck.MOD_ID, "punched"), (stack, world, holder) -> AlchemyHelper.isPunchedCard(stack) ? 1 : 0);
		ItemModelsProperties.registerProperty(MSItems.CAPTCHA_CARD, new ResourceLocation(Minestuck.MOD_ID, "ghost"), (stack, world, holder) -> AlchemyHelper.isGhostCard(stack) ? 1 : 0);
		
		ItemModelsProperties.registerProperty(MSItems.BOONDOLLARS, new ResourceLocation(Minestuck.MOD_ID, "count"), (stack, world, holder) -> BoondollarsItem.getCount(stack));
		ItemModelsProperties.registerProperty(MSItems.FROG, new ResourceLocation(Minestuck.MOD_ID, "type"), (stack, world, holder) -> !stack.hasTag() ? 0 : stack.getTag().getInt("Type"));
		ItemModelsProperties.registerProperty(MSItems.STONE_SLAB, new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, world, holder) -> StoneTabletItem.hasText(stack) ? 1 : 0);
	}
}