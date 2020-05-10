package com.mraof.minestuck.client;

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
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import com.mraof.minestuck.entity.consort.IguanaEntity;
import com.mraof.minestuck.entity.consort.NakagatorEntity;
import com.mraof.minestuck.entity.consort.SalamanderEntity;
import com.mraof.minestuck.entity.consort.TurtleEntity;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.tileentity.HolopadTileEntity;
import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy
{
	private static void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(SkaiaPortalTileEntity.class, new SkaiaPortalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(GateTileEntity.class, new GateRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(HolopadTileEntity.class, new HolopadRenderer());
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new CardRenderer());
	}
	
	public static void init()
	{
		registerRenderers();
		
		MSScreenFactories.registerScreenFactories();
		
		RenderingRegistry.registerEntityRenderingHandler(FrogEntity.class, FrogRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(HologramEntity.class, HologramRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(NakagatorEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new NakagatorModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(SalamanderEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new SalamanderModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(IguanaEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new IguanaModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(TurtleEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new TurtleModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ImpEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new ImpModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(OgreEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new OgreModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(BasiliskEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new BasiliskModel<>(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(LichEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new LichModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(GiclopsEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new GiclopsModel<>(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(BishopEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new BishopModel<>(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(RookEntity.class, manager -> new MinestuckEntityRenderer<>(manager, new RookModel<>(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(UnderlingPartEntity.class, manager -> new ShadowRenderer<>(manager, 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new ShadowRenderer<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(PawnEntity.class, manager -> new PawnRenderer(manager, new BipedModel<>(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(GristEntity.class, GristRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(VitalityGelEntity.class, VitalityGelRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(DecoyEntity.class, DecoyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(MetalBoatEntity.class, MetalBoatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(BarbasolBombEntity.class, manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(CrewPosterEntity.class, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:midnight_poster")));
		RenderingRegistry.registerEntityRenderingHandler(SbahjPosterEntity.class, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:sbahj_poster")));
		RenderingRegistry.registerEntityRenderingHandler(ShopPosterEntity.class, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:shop_poster")));
		RenderingRegistry.registerEntityRenderingHandler(FurryPosterEntity.class, manager -> new RenderHangingArt<>(manager, new ResourceLocation("minestuck:furry_poster")));

		MSKeyHandler.registerKeys();
		
		ComputerProgram.registerProgramClass(0, SburbClient.class);
		ComputerProgram.registerProgramClass(1, SburbServer.class);
		
		//MinecraftForge.EVENT_BUS.register(new MinestuckConfig()); Does not currently use any events to reload config
	}
}