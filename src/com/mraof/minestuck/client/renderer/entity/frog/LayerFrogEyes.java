package com.mraof.minestuck.client.renderer.entity.frog;

import com.mraof.minestuck.client.model.ModelFrog;
import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;

public class LayerFrogEyes implements LayerRenderer<EntityFrog>
{
	private final ModelFrog frogModel = new ModelFrog();
	private final RenderFrog frogRender;
	private float colorMin = 0;
	private String name;
	
	public LayerFrogEyes(RenderFrog renderIn)
	{
		this.frogRender = renderIn;
	}
	
	@Override
	public void doRenderLayer(EntityFrog frog, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if(!frog.isInvisible())
		{

			this.frogRender.bindTexture(this.getTexture(frog));
			if (frog.getType() == 6)
	        {
	            /*
	            int i1 = 25;
	            int i = frog.ticksExisted / 25 + frog.getEntityId();
	            int j = EnumDyeColor.values().length;
	            int k = i % j;
	            int l = (i + 1) % j;
	            float f = ((float)(frog.ticksExisted % 25) + partialTicks) / 25.0F;
	            float[] afloat1 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
	            float[] afloat2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
	           
	            GlStateManager.color(afloat1[0] * (1.0F - f) + afloat2[0] * f, afloat1[1] * (1.0F - f) + afloat2[1] * f, afloat1[2] * (1.0F - f) + afloat2[2] * f);
	             */
				
				float r, g, b;
				switch(frog.ticksExisted % 4)
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
				
				GlStateManager.color(r, g, b);
				
				this.frogModel.setModelAttributes(this.frogRender.getMainModel());
	            this.frogModel.setLivingAnimations(frog, limbSwing, limbSwingAmount, partialTicks);
	            this.frogModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1f, frog);
		        this.frogModel.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		        
				GlStateManager.disableBlend();
	        }
			else if (frog.getType() > frog.maxTypes() || frog.getType() < 1)
	        {
				int eyeColor = frog.getEyeColor();
				
				float r = (float) ((eyeColor & 16711680) >> 16) / 255f;
				float g = (float) ((eyeColor & 65280) >> 8) / 255f;
				float b = (float) ((eyeColor & 255) >> 0) / 255f;
	
				if(r < this.colorMin) r = this.colorMin;
				if(g < this.colorMin) g = this.colorMin;
				if(b < this.colorMin) b = this.colorMin;
				
				GlStateManager.color(r, g, b, 1f);

				this.frogModel.setModelAttributes(this.frogRender.getMainModel());
	            this.frogModel.setLivingAnimations(frog, limbSwing, limbSwingAmount, partialTicks);
	            this.frogModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, 1f, frog);
		        this.frogModel.render(frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		        
				GlStateManager.disableBlend();
	        }
			
		}
	}

	public ResourceLocation getTexture(EntityFrog frog) 
	{
		int id = frog.getEyeType();
		
		if(frog.getType() == 6) return new ResourceLocation("minestuck:textures/mobs/frog/susan_eyes.png");
		else if(id < 0) id = 0;
		else if(id > 3) id = 3;
		
		return new ResourceLocation("minestuck:textures/mobs/frog/eyes_" + id + ".png");
	}
	
	@Override
	public boolean shouldCombineTextures() {
		
		return false;
	}

}
