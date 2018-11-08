package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFrogSkin implements LayerRenderer<EntityFrog> 
{

	private final RenderLivingBase<?> renderer;
	private final ModelBase model;
	private float offset;
	private String name;
	
	public LayerFrogSkin(RenderLivingBase<?> renderer) {
		this.renderer = renderer;
		this.model = renderer.getMainModel();
		this.offset = 0F;
		this.name = "Frog";
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	@Override
	public void doRenderLayer(EntityFrog frog, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		this.renderer.bindTexture(this.getTexture());
		int skinColor = frog.getSkinColor();
		
		float r = (float) ((skinColor & 16711680) >> 16) / 255f;
		float g = (float) ((skinColor & 65280) >> 8) / 255f;
		float b = (float) ((skinColor & 255) >> 0) / 255f;
		
		GlStateManager.color(r+ this.offset, g + this.offset, b + this.offset, 1f);
		
		this.model.setModelAttributes(this.renderer.getMainModel());
        this.model.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.disableBlend();
	}

	public ResourceLocation getTexture() 
	{
		return new ResourceLocation("minestuck:textures/mobs/frog/skin.png");
	}
	
}
