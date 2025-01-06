package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.entity.item.PosterEntity;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.PosterComponent;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;

public class PosterRenderer extends PaintingRenderer
{
	public PosterRenderer(EntityRendererProvider.Context pContext)
	{
		super(pContext);
	}
	
	@Override
	public ResourceLocation getTextureLocation(Painting pEntity)
	{
		if(pEntity instanceof PosterEntity poster && poster.getItem().has(MSItemComponents.POSTER))
			return poster.getItem().get(MSItemComponents.POSTER).backSprite();
			
		return PosterComponent.DEFAULT_BACK_SPRITE;
	}
}
