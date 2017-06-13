package com.mraof.minestuck.client;

import com.mraof.minestuck.client.renderer.entity.*;
import com.mraof.minestuck.entity.EntityBigPart;
import com.mraof.minestuck.entity.item.EntityCrewPoster;
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

import com.mraof.minestuck.client.model.ModelBasilisk;
import com.mraof.minestuck.client.model.ModelBishop;
import com.mraof.minestuck.client.model.ModelGiclops;
import com.mraof.minestuck.client.model.ModelIguana;
import com.mraof.minestuck.client.model.ModelImp;
import com.mraof.minestuck.client.model.ModelNakagator;
import com.mraof.minestuck.client.model.ModelOgre;
import com.mraof.minestuck.client.model.ModelRook;
import com.mraof.minestuck.client.model.ModelSalamander;
import com.mraof.minestuck.client.model.ModelTurtle;
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
import com.mraof.minestuck.entity.consort.EntityTurtle;
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

public class ClientProxy
{
	
	public static EntityPlayer getClientPlayer()	//Note: can't get the client player directly from FMLClientHandler either, as the server side will still crash because of the return type
	{
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenderers() 
	{
		Minecraft mc = Minecraft.getMinecraft();
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
		RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, RenderEntityMinestuck.getFactory(new ModelNakagator(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, RenderEntityMinestuck.getFactory(new ModelSalamander(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, RenderEntityMinestuck.getFactory(new ModelIguana(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityTurtle.class, RenderEntityMinestuck.getFactory(new ModelTurtle(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, RenderEntityMinestuck.getFactory(new ModelImp(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityOgre.class, RenderEntityMinestuck.getFactory(new ModelOgre(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBasilisk.class, RenderEntityMinestuck.getFactory(new ModelBasilisk(), 2.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityGiclops.class, RenderEntityMinestuck.getFactory(new ModelGiclops(), 7.6F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, RenderEntityMinestuck.getFactory(new ModelBishop(), 1.8F));
		RenderingRegistry.registerEntityRenderingHandler(EntityRook.class, RenderEntityMinestuck.getFactory(new ModelRook(), 2.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityUnderlingPart.class, new IRenderFactory<EntityUnderlingPart>()
		{
			@Override
			public Render<EntityUnderlingPart> createRenderFor(RenderManager manager)
			{
				return new RenderShadow(manager, 2.8F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityBigPart.class, new IRenderFactory<EntityBigPart>()
		{
			@Override
			public Render<EntityBigPart> createRenderFor(RenderManager manager)
			{
				return new RenderShadow(manager, 0F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new IRenderFactory<EntityPawn>()
		{
			@Override
			public Render<EntityPawn> createRenderFor(RenderManager manager)
			{
				return new RenderPawn(manager, new ModelBiped(), 0.5F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGrist.class, new IRenderFactory<EntityGrist>()
		{
			@Override
			public Render<EntityGrist> createRenderFor(RenderManager manager)
			{
				return new RenderGrist(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityVitalityGel.class, new IRenderFactory<EntityVitalityGel>()
		{
			@Override
			public Render<EntityVitalityGel> createRenderFor(RenderManager manager)
			{
				return new RenderVitalityGel(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityDecoy.class, new IRenderFactory<EntityDecoy>()
		{
			@Override
			public Render<EntityDecoy> createRenderFor(RenderManager manager)
			{
				return new RenderDecoy(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalBoat.class, new IRenderFactory<EntityBoat>()
		{
			@Override
			public Render<EntityBoat> createRenderFor(RenderManager manager)
			{
				return new RenderMetalBoat(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityCrewPoster.class, new IRenderFactory<EntityCrewPoster>()
		{
			@Override
			public Render<EntityCrewPoster> createRenderFor(RenderManager manager)
			{
				return new RenderHangingArt(manager, "midnight_poster");
			}
		});
		
		MinecraftForge.EVENT_BUS.register(new MinestuckKeyHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
}
