package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class FrogBellyLayer extends LayerRenderer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel frogModel = new FrogModel();
	private float colorMin = 0;
	private int type = 0;
	private String name;
	
	public FrogBellyLayer(IEntityRenderer<FrogEntity, FrogModel<FrogEntity>> renderIn)
	{
		super(renderIn);
	}
	@Override
	public void render(FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if (!frog.isInvisible() && (frog.getFrogType() > frog.maxTypes() || frog.getFrogType() < 1))
        {
			this.bindTexture(this.getTexture(frog));
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
			
			GlStateManager.color4f(r, g, b, 1f);

			this.getEntityModel().setModelAttributes(this.frogModel);
            this.frogModel.setLivingAnimations(frog, limbSwing, limbSwingAmount, partialTicks);
            this.frogModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1f, frog);
	        this.frogModel.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
	        
			GlStateManager.disableBlend();
        }
	}

	public ResourceLocation getTexture(FrogEntity frog)
	{
		int id = frog.getBellyType();
		
		if(id <= 0) id = 1;
		else if(id > 3) id = 3;
		
		return new ResourceLocation("minestuck:textures/mobs/frog/belly_" + id + ".png");
	}
	
	@Override
	public boolean shouldCombineTextures() {
		
		return false;
	}

}
