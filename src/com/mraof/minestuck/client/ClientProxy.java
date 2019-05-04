package com.mraof.minestuck.client;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
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
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.carapacian.EntityBishop;
import com.mraof.minestuck.entity.carapacian.EntityPawn;
import com.mraof.minestuck.entity.carapacian.EntityRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.consort.EntityTurtle;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityHologram;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntitySbahjPoster;
import com.mraof.minestuck.entity.item.EntityShopPoster;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityLich;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderlingPart;
import com.mraof.minestuck.event.ClientEventHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkaiaPortal.class, new RenderSkaiaPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGate.class, new RenderGate());
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new RenderCard());
	}
	
	@SubscribeEvent
	public static void initBlockColors(ColorHandlerEvent.Block event)
	{
		BlockColors colors = event.getBlockColors();
		colors.register(new BlockColorCruxite(), MinestuckBlocks.ALCHEMITER_TOTEM_PAD, MinestuckBlocks.TOTEM_LATHE_DOWEL_ROD, MinestuckBlocks.CRUXITE_DOWEL);
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
		
		RenderingRegistry.registerEntityRenderingHandler(EntityFrog.class, manager -> new RenderFrog(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityHologram.class, RenderHologram::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, manager -> new RenderEntityMinestuck<>(manager, new ModelNakagator(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, manager -> new RenderEntityMinestuck<>(manager, new ModelSalamander(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, manager -> new RenderEntityMinestuck<>(manager, new ModelIguana(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, manager -> new RenderEntityMinestuck<>(manager, new ModelTurtle(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, manager -> new RenderEntityMinestuck<>(manager, new ModelImp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityOgre.class, manager -> new RenderEntityMinestuck<>(manager, new ModelOgre(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBasilisk.class, manager -> new RenderEntityMinestuck<>(manager, new ModelBasilisk(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLich.class, manager -> new RenderEntityMinestuck<>(manager, new ModelLich(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiclops.class, manager -> new RenderEntityMinestuck<>(manager, new ModelGiclops(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, manager -> new RenderEntityMinestuck<>(manager, new ModelBishop(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, manager -> new RenderEntityMinestuck<>(manager, new ModelRook(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderlingPart.class, manager -> new RenderShadow<>(manager, 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new RenderShadow<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, manager -> new RenderPawn(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGrist.class, RenderGrist::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVitalityGel.class, RenderVitalityGel::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDecoy.class, RenderDecoy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalBoat.class, RenderMetalBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCrewPoster.class, manager -> new RenderHangingArt<>(manager, "midnight_poster"));
		RenderingRegistry.registerEntityRenderingHandler(EntitySbahjPoster.class, manager -> new RenderHangingArt<>(manager, "sbahj_poster"));
		RenderingRegistry.registerEntityRenderingHandler(EntityShopPoster.class, manager -> new RenderHangingArt<>(manager, "shop_poster"));

		MinestuckKeyHandler.instance.registerKeys();
		MinecraftForge.EVENT_BUS.register(MinestuckKeyHandler.instance);
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(MinestuckModelManager.class);
		
		MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(new MinestuckConfig());
		MinecraftForge.EVENT_BUS.register(RenderMachineOutline.class);
	}
	
}
