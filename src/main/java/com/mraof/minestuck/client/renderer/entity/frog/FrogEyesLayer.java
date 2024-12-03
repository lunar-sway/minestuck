package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.entity.FrogModel;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class FrogEyesLayer extends RenderLayer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel;
	private float colorMin = 0;
	
	public FrogEyesLayer(RenderLayerParent<FrogEntity, FrogModel<FrogEntity>> renderer, EntityModelSet modelSet)
	{
		super(renderer);
		frogModel = new FrogModel<>(modelSet.bakeLayer(MSModelLayers.FROG));
	}
	
	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!frog.isInvisible())
		{
			if (frog.getFrogVariant() == FrogEntity.FrogVariants.SUSAN)
	        {
				float r, g, b;
				switch(frog.tickCount % 4)
				{
					default:
					{
						r = 1f;
						g = 0f;
						b = 0f;
					} break;

					case 1:
					{
						r = 0f;
						g = 1f;
						b = 0f;
					} break;

					case 2:
					{
						r = 0f;
						g = 0.25f;
						b = 1f;
					} break;
					
					case 3:
					{
						r = 0.5f;
						g = 0f;
						b = 1f;
					} break;
				}

				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTextureLocation(frog), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, FastColor.ARGB32.colorFromFloat(1, r, g, b));
	        }
			else if (frog.getFrogVariant() == FrogEntity.FrogVariants.DEFAULT)
	        {
				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTextureLocation(frog), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, frog.getEyeColor());
	        }
		}
	}

	@Override
	public ResourceLocation getTextureLocation(FrogEntity frog)
	{
		if(frog.getFrogVariant() == FrogEntity.FrogVariants.SUSAN) return Minestuck.id("textures/entity/frog/eyes_susan.png");
		return Minestuck.id("textures/entity/frog/eyes_" + frog.getEyeType().getSerializedName() + ".png");
	}

}
