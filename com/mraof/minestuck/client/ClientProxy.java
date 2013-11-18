package com.mraof.minestuck.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.client.event.MinestuckClientEventHandler;
import com.mraof.minestuck.client.gui.GuiGristCache;
import com.mraof.minestuck.client.model.ModelBasilisk;
import com.mraof.minestuck.client.model.ModelBishop;
import com.mraof.minestuck.client.model.ModelGiclops;
import com.mraof.minestuck.client.model.ModelIguana;
import com.mraof.minestuck.client.model.ModelImp;
import com.mraof.minestuck.client.model.ModelNakagator;
import com.mraof.minestuck.client.model.ModelOgre;
import com.mraof.minestuck.client.model.ModelSalamander;
import com.mraof.minestuck.client.renderer.entity.RenderDecoy;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.client.renderer.entity.RenderGrist;
import com.mraof.minestuck.client.renderer.entity.RenderPawn;
import com.mraof.minestuck.client.renderer.entity.RenderShadow;
import com.mraof.minestuck.client.renderer.tileentity.RenderGatePortal;
import com.mraof.minestuck.client.renderer.tileentity.RenderMachine;
import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.carapacian.EntityBishop;
import com.mraof.minestuck.entity.carapacian.EntityPawn;
import com.mraof.minestuck.entity.consort.EntityIguana;
import com.mraof.minestuck.entity.consort.EntityNakagator;
import com.mraof.minestuck.entity.consort.EntitySalamander;
import com.mraof.minestuck.entity.item.EntityGrist;
import com.mraof.minestuck.entity.underling.EntityBasilisk;
import com.mraof.minestuck.entity.underling.EntityGiclops;
import com.mraof.minestuck.entity.underling.EntityImp;
import com.mraof.minestuck.entity.underling.EntityOgre;
import com.mraof.minestuck.entity.underling.EntityUnderlingPart;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;
import com.mraof.minestuck.tileentity.TileEntityMachine;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{
	@SideOnly(Side.CLIENT)
    public static void registerRenderers() 
	{
            RenderingRegistry.registerEntityRenderingHandler(EntityNakagator.class, new RenderEntityMinestuck(new ModelNakagator(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, new RenderEntityMinestuck(new ModelSalamander(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityIguana.class, new RenderEntityMinestuck(new ModelIguana(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, new RenderEntityMinestuck(new ModelImp(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityOgre.class, new RenderEntityMinestuck(new ModelOgre(), 2.8F));
            RenderingRegistry.registerEntityRenderingHandler(EntityBasilisk.class, new RenderEntityMinestuck(new ModelBasilisk(), 2.8F));
            RenderingRegistry.registerEntityRenderingHandler(EntityUnderlingPart.class, new RenderShadow(2.8F));
            RenderingRegistry.registerEntityRenderingHandler(EntityGiclops.class, new RenderEntityMinestuck(new ModelGiclops(), 7.6F));
            RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new RenderPawn(new ModelBiped(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityBishop.class, new RenderEntityMinestuck(new ModelBishop(), 1.8F));
            RenderingRegistry.registerEntityRenderingHandler(EntityGrist.class, new RenderGrist());
            RenderingRegistry.registerEntityRenderingHandler(EntityDecoy.class, new RenderDecoy());
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGatePortal.class, new RenderGatePortal());
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachine.class, new RenderMachine());
    }
	@SideOnly(Side.CLIENT)
	public static void registerSided()
	{
		KeyBinding[] bindings;
		boolean[] keyRepeatings;
		MinestuckKeyHandler keyHandler;
		bindings = new KeyBinding[2];
		keyRepeatings = new boolean[bindings.length];
		bindings[0] = new KeyBinding("key.gristCache", 34);
		keyRepeatings[0] = false;
		bindings[1] = new KeyBinding("key.exitEdit", 45);
		keyRepeatings[1] = false;
		keyHandler = new MinestuckKeyHandler(bindings, keyRepeatings);
		KeyBindingRegistry.registerKeyBinding(keyHandler);
		MinecraftForge.EVENT_BUS.register(new GuiGristCache(Minecraft.getMinecraft()));
		MinecraftForge.EVENT_BUS.register(new MinestuckClientEventHandler());
	}
}