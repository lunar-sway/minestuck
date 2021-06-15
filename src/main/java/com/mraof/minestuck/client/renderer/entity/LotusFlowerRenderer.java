package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.client.model.LotusFlowerModel;
import com.mraof.minestuck.entity.item.LotusFlowerEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.example.client.model.entity.ExampleEntityModel;
import software.bernie.example.entity.GeoExampleEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LotusFlowerRenderer extends GeoEntityRenderer<LotusFlowerEntity> {
	public LotusFlowerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new LotusFlowerModel());
	}
	
	@Override
	public RenderType getRenderType(LotusFlowerEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}
}

/*public class LotusFlowerRenderer extends EntityRenderer<LotusFlowerEntity>
{
	
	public LotusFlowerRenderer(EntityRendererManager manager)
	{
		super(manager/*, new LotusFlowerModel(), 1F*//*);
		this.shadowSize = 0.15F;
		this.shadowOpaque = .75F;
	}
	
	@Override
	public ResourceLocation getEntityTexture(LotusFlowerEntity entity)
	{
		return new ResourceLocation("minestuck", "textures/entity/lotus_flower.png");
	}

}*/

/*public class LotusFlowerRenderer extends GeoEntityRenderer<LotusFlowerEntity>
{
	public LotusFlowerRenderer(EntityRendererManager renderManager) {
		super(renderManager, new LotusFlowerModel());
	}
	
	@Override
	public RenderType getRenderType(LotusFlowerEntity animatable, float partialTicks, MatrixStack stack,
									IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}
}*/