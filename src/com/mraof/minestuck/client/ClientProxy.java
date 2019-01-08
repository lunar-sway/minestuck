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
import com.mraof.minestuck.item.ItemFrog;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.util.ColorCollector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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

		mc.getItemColors().registerItemColorHandler(new IItemColor()
        {
            public int colorMultiplier(ItemStack stack, int tintIndex)
            {
            	ItemFrog item = ((ItemFrog)stack.getItem());
            	int color = -1;
            	
            	if((stack.getMetadata() > EntityFrog.maxTypes() || stack.getMetadata() < 1))
            	{
	            	switch(tintIndex)
	            	{
	            	case 0: color = item.getSkinColor(stack); break;
	            	case 1: color = item.getEyeColor(stack); break;
	            	case 2: color = item.getBellyColor(stack); break; 
	            	}
            	}
                return color;
            }
        }, MinestuckItems.itemFrog);
	}
	
	@Override
	public void preInit()
	{
		
		
		super.preInit();
		RenderingRegistry.registerEntityRenderingHandler(EntityFrog.class, manager -> new RenderFrog(manager, new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityHologram.class, manager -> new RenderHologram(manager));
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
		RenderingRegistry.registerEntityRenderingHandler(EntityShopPoster.class, manager -> new RenderHangingArt<>(manager, "shop_poster"));

		
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
