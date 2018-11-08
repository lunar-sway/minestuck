package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.client.renderer.entity.RenderEntityMinestuck;
import com.mraof.minestuck.entity.EntityDecoy;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderFrog extends RenderEntityMinestuck<EntityFrog>
{

	public RenderFrog(RenderManager manager, ModelBase par1ModelBase, float par2) 
	{
		super(Minecraft.getMinecraft().getRenderManager(), new ModelFrog(), par2);
		this.addLayer(new LayerFrogSkin(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityFrog frog) 
	{
		return frog.getTextureResource();
	}
	
	

}
