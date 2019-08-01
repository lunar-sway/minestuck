package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class FrogSkinLayer extends LayerRenderer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel frogModel = new FrogModel();
	private float colorMin = 0.25f;
	private String name;

	public FrogSkinLayer(IEntityRenderer<FrogEntity, FrogModel<FrogEntity>> renderIn)
	{
		super(renderIn);
	}

	@Override
	public void render(FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!frog.isInvisible()) {
			int type = frog.getFrogType();
			this.bindTexture(this.getTexture(type));
			if ((type > frog.maxTypes() || type < 1)) {
				int skinColor = frog.getSkinColor();

				float r = (float) ((skinColor & 16711680) >> 16) / 255f;
				float g = (float) ((skinColor & 65280) >> 8) / 255f;
				float b = (float) ((skinColor & 255) >> 0) / 255f;

				if (r < this.colorMin)
					r = this.colorMin;
				if (g < this.colorMin)
					g = this.colorMin;
				if (b < this.colorMin)
					b = this.colorMin;

				GlStateManager.color4f(r, g, b, 1f);
			}
			this.getEntityModel().setModelAttributes(this.frogModel);
            this.frogModel.setLivingAnimations(frog, limbSwing, limbSwingAmount, partialTicks);
            this.frogModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1f, frog);
	        this.frogModel.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	        
			GlStateManager.disableBlend();
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

	@Override
	public boolean shouldCombineTextures() {

		return false;
	}

}
