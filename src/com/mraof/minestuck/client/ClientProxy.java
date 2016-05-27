package com.mraof.minestuck.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.client.model.ModelBasilisk;
import com.mraof.minestuck.client.model.ModelBishop;
import com.mraof.minestuck.client.model.ModelGiclops;
import com.mraof.minestuck.client.model.ModelIguana;
import com.mraof.minestuck.client.model.ModelImp;
import com.mraof.minestuck.client.model.ModelNakagator;
import com.mraof.minestuck.client.model.ModelOgre;
import com.mraof.minestuck.client.model.ModelRook;
import com.mraof.minestuck.client.model.ModelSalamander;
import com.mraof.minestuck.client.renderer.entity.RenderDecoy;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.client.renderer.entity.RenderGrist;
import com.mraof.minestuck.client.renderer.entity.RenderMetalBoat;
import com.mraof.minestuck.client.renderer.entity.RenderPawn;
import com.mraof.minestuck.client.renderer.entity.RenderShadow;
import com.mraof.minestuck.client.renderer.entity.RenderVitalityGel;
import com.mraof.minestuck.client.renderer.tileentity.RenderGate;
import com.mraof.minestuck.client.renderer.tileentity.RenderSkaiaPortal;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBishop;
import com.mraof.minestuck.entity.carapacian.EntityPawn;
import com.mraof.minestuck.entity.carapacian.EntityRook;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.item.EntityMetalBoat;
import com.mraof.minestuck.entity.item.EntityVitalityGel;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderlingPart;
import com.mraof.minestuck.event.ClientEventHandler;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.util.ColorCollector;

public class ClientProxy extends CommonProxy
{
	
	public static EntityPlayer getClientPlayer()	//Note: can't get the client player directly from FMLClientHandler either, as the server side will still crash because of the return type
	{
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerPreInitRenderers() 
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderers() 
	{
		Minecraft mc = Minecraft.getMinecraft();
		RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelNakagator(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelSalamander(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelIguana(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelImp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityOgre.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelOgre(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBasilisk.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelBasilisk(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderlingPart.class, new RenderShadow(mc.getRenderManager(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiclops.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelGiclops(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new RenderPawn(mc.getRenderManager(), new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelBishop(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, new RenderEntityMinestuck(mc.getRenderManager(), new ModelRook(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGrist.class, new RenderGrist(mc.getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityVitalityGel.class, new RenderVitalityGel(mc.getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDecoy.class, new RenderDecoy(mc.getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalBoat.class, new RenderMetalBoat(mc.getRenderManager()));
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkaiaPortal.class, new RenderSkaiaPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGate.class, new RenderGate());
//		MinecraftForgeClient.registerItemRenderer(Minestuck.captchaCard, new RenderCard());
		mc.getItemColors().registerItemColorHandler(new IItemColor()
		{
			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex)
			{
				if(tintIndex == 0 || tintIndex == 1)
				{
					int color = stack.getMetadata() == 0 ? -1 : ColorCollector.getColor(stack.getMetadata() - 1);
					if(tintIndex == 1)
					{
						int i0 = ((color & 255) + 255)/2;
						int i1 = (((color >> 8) & 255) + 255)/2;
						int i2 = (((color >> 16) & 255) + 255)/2;
						color = i0 | (i1 << 8) | (i2 << 16);
					}
					return color;
				}
				else return -1;
			}
		}, MinestuckItems.cruxiteDowel, MinestuckItems.cruxiteApple, MinestuckItems.cruxitePotion);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerSided()
	{
		
		FMLCommonHandler.instance().bus().register(new MinestuckKeyHandler());
		FMLCommonHandler.instance().bus().register(new ClientEventHandler());
	}
	
}
