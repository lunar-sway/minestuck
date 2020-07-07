package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.EntityBullet;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBullet extends ArrowRenderer<EntityBullet>
{

	private static final ResourceLocation ENERGY_ARROW_BLUE = new ResourceLocation("minestuck", "textures/entity/projectiles/energy_arrow_blue.png");
	
	public RenderBullet(EntityRendererManager renderManagerIn)
	{
		
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity) 
	{
		return entity.getTexture();
	}

}
