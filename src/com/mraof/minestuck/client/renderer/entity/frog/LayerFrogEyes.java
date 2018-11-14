package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFrogEyes implements LayerRenderer<EntityFrog>
{
	private final ModelBase frogModel = new ModelFrog();
	private final RenderFrog frogRender;
	private float colorMin = 0;
	private int type = 0;
	private String name;
	
	public LayerFrogEyes(RenderFrog renderIn)
	{
		this.frogRender = renderIn;
	}
	
	@Override
	public void doRenderLayer(EntityFrog frog, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if (!frog.isInvisible() && (frog.getType() > frog.maxTypes() || frog.getType() < 1))
        {
			this.frogRender.bindTexture(this.getTexture());
			int eyeColor = frog.getEyeColor();
			type = frog.getEyeType();
			
			float r = (float) ((eyeColor & 16711680) >> 16) / 255f;
			float g = (float) ((eyeColor & 65280) >> 8) / 255f;
			float b = (float) ((eyeColor & 255) >> 0) / 255f;

			if(r < this.colorMin) r = this.colorMin;
			if(g < this.colorMin) g = this.colorMin;
			if(b < this.colorMin) b = this.colorMin;
			
			GlStateManager.color(r, g, b, 1f);
			
			this.frogModel.setModelAttributes(this.frogRender.getMainModel());
	        this.frogModel.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			GlStateManager.disableBlend();
        }
	}

	public ResourceLocation getTexture() 
	{
		int id = this.type;
		
		if(id < 0) id = 0;
		else if(id > 3) id = 3;
		
		return new ResourceLocation("minestuck:textures/mobs/frog/eyes_" + id + ".png");
	}
	
	@Override
	public boolean shouldCombineTextures() {
		
		return false;
	}

}
