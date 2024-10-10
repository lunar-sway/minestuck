package com.mraof.minestuck.client.renderer.entity.frog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.entity.FrogModel;
import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import static com.mraof.minestuck.entity.FrogEntity.maxTypes;

public class FrogBellyLayer extends RenderLayer<FrogEntity, FrogModel<FrogEntity>>
{
	private final FrogModel<FrogEntity> frogModel;
	
	public FrogBellyLayer(RenderLayerParent<FrogEntity, FrogModel<FrogEntity>> renderer, EntityModelSet modelSet)
	{
		super(renderer);
		frogModel = new FrogModel<>(modelSet.bakeLayer(MSModelLayers.FROG));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, FrogEntity frog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if (!frog.isInvisible() && frog.getFrogVariant() == FrogEntity.FrogVariants.DEFAULT)
        {
			int bellyColor;
			FrogEntity.BellyTypes type = frog.getBellyType();
			if(type == FrogEntity.BellyTypes.NONE)
				bellyColor = frog.getSkinColor();
			else bellyColor = FastColor.ARGB32.color(255, frog.getBellyColor());
			
			coloredCutoutModelCopyLayerRender(this.getParentModel(), this.frogModel, this.getTextureLocation(frog), poseStack, bufferIn, packedLightIn, frog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, bellyColor);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(FrogEntity frog)
	{
		return Minestuck.id("textures/entity/frog/belly_" + frog.getBellyType().getSerializedName() + ".png");
	}
}
