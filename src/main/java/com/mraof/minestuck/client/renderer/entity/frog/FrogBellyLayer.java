package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import static com.mraof.minestuck.entity.FrogEntity.maxTypes;

public class FrogBellyLayer extends LayerRenderer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel = new FrogModel<>();
	private float colorMin = 0;
	private int type = 0;
	private String name;
	
	public FrogBellyLayer(IEntityRenderer<FrogEntity, FrogModel<FrogEntity>> renderIn)
	{
		super(renderIn);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
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

			coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTextureLocation(frog), matrixStackIn, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, r, g, b);
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
