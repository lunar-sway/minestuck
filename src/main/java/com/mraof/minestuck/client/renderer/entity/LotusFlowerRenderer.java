package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.entity.LotusFlowerModel;
import com.mraof.minestuck.entity.LotusFlowerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class LotusFlowerRenderer extends GeoEntityRenderer<LotusFlowerEntity>
{
	public LotusFlowerRenderer(EntityRendererProvider.Context context) {
		super(context, new LotusFlowerModel());
	}
	
	@Override
	public RenderType getRenderType(LotusFlowerEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
	{
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
}