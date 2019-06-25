package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.util.ResourceLocation;

import com.mraof.minestuck.entity.EntityMinestuck;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEntityMinestuck<T extends EntityMinestuck> extends RenderLiving<T>
{
	
	public RenderEntityMinestuck(RenderManager manager, ModelBase par1ModelBase, float par2)
	{
		super(manager, par1ModelBase, par2);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) 
	{
		return entity.getTextureResource();
	}
}