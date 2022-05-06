package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FrogRenderer extends MobRenderer<FrogEntity, FrogModel<FrogEntity>>
{

	public FrogRenderer(EntityRendererManager manager)
	{
		super(manager, new FrogModel<>(), 0.5f);
		this.addLayer(new FrogSkinLayer(this));
		this.addLayer(new FrogEyesLayer(this));
		this.addLayer(new FrogBellyLayer(this));
	}

	@Override
	public void render(FrogEntity frog, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.scale(frog.getFrogSize(), frog.getFrogSize(), frog.getFrogSize());
		super.render(frog, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getTextureLocation(FrogEntity entity)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/frog/base.png");
	}
	
	protected boolean shouldShowName(FrogEntity entity)
    {
        return super.shouldShowName(entity) && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }
	

}
