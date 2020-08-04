package com.mraof.minestuck.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.SkaiaPortalBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.client.model.*;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.entity.frog.FrogRenderer;
import com.mraof.minestuck.client.renderer.tileentity.GateRenderer;
import com.mraof.minestuck.client.renderer.tileentity.HolopadRenderer;
import com.mraof.minestuck.client.renderer.tileentity.SkaiaPortalRenderer;
import com.mraof.minestuck.client.settings.MSKeyHandler;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.computer.SburbClient;
import com.mraof.minestuck.computer.SburbServer;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.IguanaEntity;
import com.mraof.minestuck.entity.consort.NakagatorEntity;
import com.mraof.minestuck.entity.consort.SalamanderEntity;
import com.mraof.minestuck.entity.consort.TurtleEntity;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.tileentity.HolopadTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy
{
	private static void registerRenderers()
	{
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.SKAIA_PORTAL, SkaiaPortalRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.GATE, GateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MSTileEntityTypes.HOLOPAD, HolopadRenderer::new);
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
		RenderTypeLookup.setRenderLayer(MSBlocks.HOLOPAD.getBlock(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(MSBlocks.CRUXITE_DOWEL.getBlock(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(MSBlocks.BLENDER.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.CHESSBOARD.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.MINI_FROG_STATUE.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWYSTONE_DUST.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GOLD_SEEDS.getBlock(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(MSBlocks.GLOWY_GOOP.getBlock(), RenderType.getSolid());
		RenderTypeLookup.setRenderLayer(MSBlocks.COAGULATED_BLOOD.getBlock(), RenderType.getSolid());

		MSKeyHandler.registerKeys();
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);

		//MinecraftForge.EVENT_BUS.register(new MinestuckConfig()); Does not currently use any events to reload config
	}
}