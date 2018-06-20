package com.mraof.minestuck.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.mraof.minestuck.entity.EntityMinestuck;

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
	
	public static<T extends EntityMinestuck> Factory<T> getFactory(ModelBase model, float shadowSize)
	{
		Factory<T> factory = new Factory<>();
		factory.modelBase = model;
		factory.shadowSize = shadowSize;
		return factory;
	}
	
	protected static class Factory<T extends EntityMinestuck> implements IRenderFactory<T>
	{
		protected ModelBase modelBase;
		protected float shadowSize;
		@Override
		public Render<? super T> createRenderFor(RenderManager manager)
		{
			return new RenderEntityMinestuck<>(manager, modelBase, shadowSize);
		}
	}
}