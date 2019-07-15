package com.mraof.minestuck.client;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.ModScreenManager;
import com.mraof.minestuck.client.model.ModelBasilisk;
import com.mraof.minestuck.client.model.ModelBishop;
import com.mraof.minestuck.client.model.ModelGiclops;
import com.mraof.minestuck.client.model.ModelIguana;
import com.mraof.minestuck.client.model.ModelImp;
import com.mraof.minestuck.client.model.ModelLich;
import com.mraof.minestuck.client.model.ModelNakagator;
import com.mraof.minestuck.client.model.ModelOgre;
import com.mraof.minestuck.client.model.ModelRook;
import com.mraof.minestuck.client.model.ModelSalamander;
import com.mraof.minestuck.client.model.ModelTurtle;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.client.renderer.RenderMachineOutline;
import com.mraof.minestuck.client.renderer.entity.RenderDecoy;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.client.renderer.entity.RenderGrist;
import com.mraof.minestuck.client.renderer.entity.RenderHangingArt;
import com.mraof.minestuck.client.renderer.entity.RenderHologram;
import com.mraof.minestuck.client.renderer.entity.RenderMetalBoat;
import com.mraof.minestuck.client.renderer.entity.RenderPawn;
import com.mraof.minestuck.client.renderer.entity.RenderShadow;
import com.mraof.minestuck.client.renderer.entity.RenderVitalityGel;
import com.mraof.minestuck.client.renderer.entity.frog.RenderFrog;
import com.mraof.minestuck.client.renderer.tileentity.RenderGate;
import com.mraof.minestuck.client.renderer.tileentity.RenderSkaiaPortal;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.DecoyEntity;
import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import com.mraof.minestuck.entity.carapacian.RookEntity;
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
import com.mraof.minestuck.entity.underling.BasiliskEntity;
import com.mraof.minestuck.entity.underling.GiclopsEntity;
import com.mraof.minestuck.entity.underling.ImpEntity;
import com.mraof.minestuck.entity.underling.LichEntity;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.entity.underling.UnderlingPartEntity;
import com.mraof.minestuck.event.ClientEventHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.tileentity.SkaiaPortalTileEntity;
import com.mraof.minestuck.util.ColorCollector;

import net.minecraft.block.BlockStem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy
{
	
	public static EntityPlayer getClientPlayer()	//Note: can't get the client player directly from FMLClientHandler either, as the server side will still crash because of the return type
	{
		return Minecraft.getInstance().player;	//TODO verify server functionality
	}
	
	public static void addScheduledTask(Runnable runnable)
	{
		Minecraft.getInstance().addScheduledTask(runnable);
	}
	
	private static void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(SkaiaPortalTileEntity.class, new RenderSkaiaPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(GateTileEntity.class, new RenderGate());
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new RenderCard());
	}
	
	@SubscribeEvent
	public static void initBlockColors(ColorHandlerEvent.Block event)
	{
		BlockColors colors = event.getBlockColors();
		colors.register(new BlockColorCruxite(), MinestuckBlocks.ALCHEMITER.TOTEM_PAD, MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD, MinestuckBlocks.CRUXITE_DOWEL);
		colors.register((state, worldIn, pos, tintIndex) ->
		{
			int age = state.get(BlockStem.AGE);
			int red = age * 32;
			int green = 255 - age * 8;
			int blue = age * 4;
			return red << 16 | green << 8 | blue;
		}, MinestuckBlocks.STRAWBERRY_STEM);
	}
	
	@SubscribeEvent
	public static void initItemColors(ColorHandlerEvent.Item event)
	{
		ItemColors colors = event.getItemColors();
		colors.register((stack, tintIndex) -> BlockColorCruxite.handleColorTint(ColorCollector.getColorFromStack(stack, 0) - 1, tintIndex),
				MinestuckBlocks.CRUXITE_DOWEL, MinestuckItems.CRUXITE_APPLE, MinestuckItems.CRUXITE_POTION);
		colors.register(new RenderFrog.FrogItemColor(), MinestuckItems.FROG);
	}
	
	public static void init()
	{
		registerRenderers();
		
		ModScreenManager.registerScreenFactories();
		
		RenderingRegistry.registerEntityRenderingHandler(FrogEntity.class, manager -> new RenderFrog(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(HologramEntity.class, RenderHologram::new);
		RenderingRegistry.registerEntityRenderingHandler(NakagatorEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelNakagator(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(SalamanderEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelSalamander(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(IguanaEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelIguana(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(TurtleEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelTurtle(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(ImpEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelImp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(OgreEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelOgre(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(BasiliskEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelBasilisk(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(LichEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelLich(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(GiclopsEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelGiclops(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(BishopEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelBishop(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(RookEntity.class, manager -> new RenderEntityMinestuck<>(manager, new ModelRook(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(UnderlingPartEntity.class, manager -> new RenderShadow<>(manager, 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new RenderShadow<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(PawnEntity.class, manager -> new RenderPawn(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(GristEntity.class, RenderGrist::new);
		RenderingRegistry.registerEntityRenderingHandler(VitalityGelEntity.class, RenderVitalityGel::new);
		RenderingRegistry.registerEntityRenderingHandler(DecoyEntity.class, RenderDecoy::new);
		RenderingRegistry.registerEntityRenderingHandler(MetalBoatEntity.class, RenderMetalBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(CrewPosterEntity.class, manager -> new RenderHangingArt<>(manager, "midnight_poster"));
		RenderingRegistry.registerEntityRenderingHandler(SbahjPosterEntity.class, manager -> new RenderHangingArt<>(manager, "sbahj_poster"));
		RenderingRegistry.registerEntityRenderingHandler(ShopPosterEntity.class, manager -> new RenderHangingArt<>(manager, "shop_poster"));

		MinestuckKeyHandler.instance.registerKeys();
		MinecraftForge.EVENT_BUS.register(MinestuckKeyHandler.instance);
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		
		MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(new MinestuckConfig());
		MinecraftForge.EVENT_BUS.register(RenderMachineOutline.class);
	}
	
}
