package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class FrogEyesLayer extends LayerRenderer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel = new FrogModel<>();
	private float colorMin = 0;
	private String name;
	
	public FrogEyesLayer(IEntityRenderer<FrogEntity, FrogModel<FrogEntity>> renderIn)
	{
		super(renderIn);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(!frog.isInvisible())
		{
			if (frog.getFrogType() == 6)
	        {
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

				renderCopyCutoutModel(this.getEntityModel(), this.frogModel, this.getEntityTexture(frog), matrixStackIn, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
	        }
			else if (frog.getFrogType() > FrogEntity.maxTypes() || frog.getFrogType() < 1)
	        {
				int eyeColor = frog.getEyeColor();
				
				float r = (float) ((eyeColor & 16711680) >> 16) / 255f;
				float g = (float) ((eyeColor & 65280) >> 8) / 255f;
				float b = (float) ((eyeColor & 255)) / 255f;
	
				if(r < this.colorMin) r = this.colorMin;
				if(g < this.colorMin) g = this.colorMin;
				if(b < this.colorMin) b = this.colorMin;

				renderCopyCutoutModel(this.getEntityModel(), this.frogModel, this.getEntityTexture(frog), matrixStackIn, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
	        }
		}
	}

	@Override
	public ResourceLocation getEntityTexture(FrogEntity frog)
	{
		int id = frog.getEyeType();

		if(frog.getFrogType() == 6) return new ResourceLocation("minestuck:textures/entity/frog/susan_eyes.png");
		else if(id < 0) id = 0;
		else if(id > 3) id = 3;

		return new ResourceLocation("minestuck:textures/entity/frog/eyes_" + id + ".png");
	}

}
