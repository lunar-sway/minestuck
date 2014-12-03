package com.mraof.minestuck.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;

import com.mraof.minestuck.Minestuck;

public class ItemTextures
{
	
	public static void registerTextures()
	{
		Minestuck ms = Minestuck.instance;
		ItemModelMesher modelRegistry = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		modelRegistry.register(Minestuck.clawHammer, 0, new ModelResourceLocation("minestuck:claw_hammer", "inventory"));
		
	}
	
}
