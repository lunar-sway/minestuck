package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.client.model.entity.FrogModel;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class FrogSkinLayer extends RenderLayer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel;
	
	public FrogSkinLayer(RenderLayerParent<FrogEntity, FrogModel<FrogEntity>> renderer, EntityModelSet modelSet)
	{
		super(renderer);
		frogModel = new FrogModel<>(modelSet.bakeLayer(MSModelLayers.FROG));
	}
	
	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(frog.isInvisible())
			return;
		
		FrogEntity.FrogVariants variant = frog.getFrogVariant();
		
		if(variant == null)
			variant = FrogEntity.FrogVariants.DEFAULT;
		
		int color = variant == FrogEntity.FrogVariants.DEFAULT ? frog.getSkinColor() : 0xFFFFFFFF;
		
		coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, Minestuck.id("textures/entity/frog/skin_" + variant.getSerializedName() + ".png"),
				poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, color);
	}
}
