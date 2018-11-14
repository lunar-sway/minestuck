package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerFrogBelly implements LayerRenderer<EntityFrog>
{
	private final ModelBase frogModel = new ModelFrog();
	private final RenderFrog frogRender;
	private float colorMin = 0;
	private int type = 0;
	private String name;
	
	public LayerFrogBelly(RenderFrog renderIn)
	{
		this.frogRender = renderIn;
	}
	
	@Override
	public void doRenderLayer(EntityFrog frog, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if (!frog.isInvisible() && (frog.getType() > 2 || frog.getType() < 1))
        {
			this.frogRender.bindTexture(this.getTexture());
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
			float b = (float) ((bellyColor & 255) >> 0) / 255f;

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
		
		if(id <= 0) id = 1;
		else if(id > 3) id = 3;
		
		return new ResourceLocation("minestuck:textures/mobs/frog/belly_" + id + ".png");
	}
	
	@Override
	public boolean shouldCombineTextures() {
		
		return false;
	}

}
