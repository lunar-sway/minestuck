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

public class FrogSkinLayer extends RenderLayer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel;
	private float colorMin = 0.25f;

	public FrogSkinLayer(RenderLayerParent<FrogEntity, FrogModel<FrogEntity>> renderer, EntityModelSet modelSet)
	{
		super(renderer);
		frogModel = new FrogModel<>(modelSet.bakeLayer(MSModelLayers.FROG));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if (!frog.isInvisible()) {
			int type = frog.getFrogType();
			if ((type > FrogEntity.maxTypes() || type < 1)) {
				int skinColor = frog.getSkinColor();

				float r = (float) ((skinColor & 16711680) >> 16) / 255f;
				float g = (float) ((skinColor & 65280) >> 8) / 255f;
				float b = (float) ((skinColor & 255)) / 255f;

				if (r < this.colorMin)
					r = this.colorMin;
				if (g < this.colorMin)
					g = this.colorMin;
				if (b < this.colorMin)
					b = this.colorMin;

				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTexture(type), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
			}
			else
			{
				coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTexture(type), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	public ResourceLocation getTexture(int type) {
		String path;
		switch (type) {
		default:
		case 0:
			path = "minestuck:textures/entity/frog/skin.png";
			break;
		case 1:
			path = "minestuck:textures/entity/frog/totally_normal_frog.png";
			break;
		case 2:
			path = "minestuck:textures/entity/frog/ruby_contraband.png";
			break;
		case 3:
			path = "minestuck:textures/entity/frog/genesis_frog.png";
			break;
		case 4:
			path = "minestuck:textures/entity/frog/null_frog.png";
			break;
		case 5:
			path = "minestuck:textures/entity/frog/golden_frog.png";
			break;
		case 6:
			path = "minestuck:textures/entity/frog/susan.png";
			break;
		}
		return new ResourceLocation(path);
	}
}
