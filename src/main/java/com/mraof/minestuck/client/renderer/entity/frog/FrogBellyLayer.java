package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.model.entity.FrogModel;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

import static com.mraof.minestuck.entity.FrogEntity.maxTypes;

public class FrogBellyLayer extends RenderLayer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel;
	private float colorMin = 0;
	private int type = 0;
	
	public FrogBellyLayer(RenderLayerParent<FrogEntity, FrogModel<FrogEntity>> renderer, EntityModelSet modelSet)
	{
		super(renderer);
		frogModel = new FrogModel<>(modelSet.bakeLayer(MSModelLayers.FROG));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if (!frog.isInvisible() && (frog.getFrogType() > maxTypes() || frog.getFrogType() < 1))
        {
			int bellyColor;
			type = frog.getBellyType();
			if(type == 0)
			{
				bellyColor = frog.getSkinColor();
				colorMin = 0.25f;
			}
			else bellyColor = frog.getBellyColor();

			float r = (float) ((bellyColor & 16711680) >> 16) / 255f;
			float g = (float) ((bellyColor & 65280) >> 8) / 255f;
			float b = (float) ((bellyColor & 255)) / 255f;

			if (r < this.colorMin) r = this.colorMin;
			if (g < this.colorMin) g = this.colorMin;
			if (b < this.colorMin) b = this.colorMin;

			coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTextureLocation(frog), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(FrogEntity frog)
	{
		int id = frog.getBellyType();

		if(id <= 0) id = 1;
		else if(id > 3) id = 3;

		return new ResourceLocation("minestuck:textures/entity/frog/belly_" + id + ".png");
	}
}
