package com.mraof.minestuck.client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraftforge.client.MinecraftForgeClient;

import com.mraof.minestuck.CommonProxy;
import com.mraof.minestuck.client.renderer.entity.RenderImp;
import com.mraof.minestuck.client.renderer.entity.RenderPawn;
import com.mraof.minestuck.client.renderer.entity.RenderSalamander;
import com.mraof.minestuck.client.renderer.tileentity.RenderGatePortal;
import com.mraof.minestuck.entity.EntityImp;
import com.mraof.minestuck.entity.EntityPawn;
import com.mraof.minestuck.entity.EntitySalamander;
import com.mraof.minestuck.model.ModelImp;
import com.mraof.minestuck.model.ModelSalamander;
import com.mraof.minestuck.tileentity.TileEntityGatePortal;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{
	@SideOnly(Side.CLIENT)
    public static void registerRenderers() 
	{
//            MinecraftForgeClient.preloadTexture(ITEMS_PNG);
//            MinecraftForgeClient.preloadTexture(BLOCKS_PNG);
            RenderingRegistry.registerEntityRenderingHandler(EntitySalamander.class, new RenderSalamander(new ModelSalamander(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityImp.class, new RenderImp(new ModelImp(), 0.5F));
            RenderingRegistry.registerEntityRenderingHandler(EntityPawn.class, new RenderBiped(new ModelBiped(), 0.5F));
            ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGatePortal.class, new RenderGatePortal());
    }
}