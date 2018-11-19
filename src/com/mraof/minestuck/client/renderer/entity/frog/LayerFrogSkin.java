package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFrogSkin implements LayerRenderer<EntityFrog> {
	private final ModelFrog frogModel = new ModelFrog();
	private final RenderFrog frogRender;
	private float colorMin = 0.25f;
	private String name;

	public LayerFrogSkin(RenderFrog renderIn) {
		this.frogRender = renderIn;
	}

	@Override
	public void doRenderLayer(EntityFrog frog, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!frog.isInvisible()) {
			int type = frog.getType();
			this.frogRender.bindTexture(this.getTexture(type));
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

				GlStateManager.color(r, g, b, 1f);
			}
			this.frogModel.setModelAttributes(this.frogRender.getMainModel());
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
			path = "minestuck:textures/mobs/frog/skin.png";
			break;
		case 1:
			path = "minestuck:textures/mobs/frog/totally_normal_frog.png";
			break;
		case 2:
			path = "minestuck:textures/mobs/frog/ruby_contraband.png";
			break;
		case 3:
			path = "minestuck:textures/mobs/frog/genesis_frog.png";
			break;
		case 4:
			path = "minestuck:textures/mobs/frog/null_frog.png";
			break;
		case 5:
			path = "minestuck:textures/mobs/frog/golden_frog.png";
			break;
		case 6:
			path = "minestuck:textures/mobs/frog/susan.png";
			break;
		}
		return new ResourceLocation(path);
	}

	@Override
	public boolean shouldCombineTextures() {

		return false;
	}

}
