package com.mraof.minestuck.client;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.model.*;
import com.mraof.minestuck.client.renderer.BlockColorCruxite;
import com.mraof.minestuck.client.renderer.RenderMachineOutline;
import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.client.renderer.tileentity.RenderGate;
import com.mraof.minestuck.client.renderer.tileentity.RenderSkaiaPortal;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.client.util.MinestuckModelManager;
import com.mraof.minestuck.editmode.ClientEditHandler;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBishop;
import com.mraof.minestuck.entity.carapacian.EntityPawn;
import com.mraof.minestuck.entity.carapacian.EntityRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.consort.EntityTurtle;
import com.mraof.minestuck.entity.item.*;
import com.mraof.minestuck.entity.underling.*;
import com.mraof.minestuck.event.ClientEventHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	
	public static EntityPlayer getClientPlayer()	//Note: can't get the client player directly from FMLClientHandler either, as the server side will still crash because of the return type
	{
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	public static void addScheduledTask(Runnable runnable)
	{
		Minecraft.getMinecraft().addScheduledTask(runnable);
	}
	
	public static void registerRenderers() 
	{
		Minecraft mc = Minecraft.getMinecraft();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkaiaPortal.class, new RenderSkaiaPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGate.class, new RenderGate());
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new RenderCard());
		mc.getItemColors().registerItemColorHandler((stack, tintIndex) -> BlockColorCruxite.handleColorTint(stack.getMetadata() == 0 ? -1 : ColorCollector.getColor(stack.getMetadata() - 1), tintIndex),
				MinestuckItems.cruxiteDowel, MinestuckItems.cruxiteApple, MinestuckItems.cruxitePotion);
		mc.getBlockColors().registerBlockColorHandler(new BlockColorCruxite(), MinestuckBlocks.alchemiter[0], MinestuckBlocks.totemlathe[1], MinestuckBlocks.blockCruxiteDowel);
	}
	
	@Override
	public void preInit()
	{
		super.preInit();
		RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, RenderEntityMinestuck.getFactory(new ModelNakagator(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, RenderEntityMinestuck.getFactory(new ModelSalamander(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, RenderEntityMinestuck.getFactory(new ModelIguana(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, RenderEntityMinestuck.getFactory(new ModelTurtle(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, RenderEntityMinestuck.getFactory(new ModelImp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityOgre.class, RenderEntityMinestuck.getFactory(new ModelOgre(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBasilisk.class, RenderEntityMinestuck.getFactory(new ModelBasilisk(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityLich.class, RenderEntityMinestuck.getFactory(new ModelLich(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiclops.class, RenderEntityMinestuck.getFactory(new ModelGiclops(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, RenderEntityMinestuck.getFactory(new ModelBishop(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, RenderEntityMinestuck.getFactory(new ModelRook(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderlingPart.class, manager -> new RenderShadow<>(manager, 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, manager -> new RenderShadow<>(manager, 0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, manager -> new RenderPawn(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGrist.class, RenderGrist::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVitalityGel.class, RenderVitalityGel::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDecoy.class, RenderDecoy::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalBoat.class, RenderMetalBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCrewPoster.class, manager -> new RenderHangingArt<>(manager, "midnight_poster"));
		RenderingRegistry.registerEntityRenderingHandler(EntitySbahjPoster.class, manager -> new RenderHangingArt<>(manager, "sbahj_poster"));
		
		MinecraftForge.EVENT_BUS.register(new MinestuckKeyHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MinecraftForge.EVENT_BUS.register(MinestuckModelManager.class);
	}
	
	@Override
	public void init()
	{
		super.init();
		MinecraftForge.EVENT_BUS.register(ClientEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(new MinestuckConfig());
		MinecraftForge.EVENT_BUS.register(RenderMachineOutline.class);
	}
}
