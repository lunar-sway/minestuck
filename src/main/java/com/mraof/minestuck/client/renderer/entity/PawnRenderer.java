package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.MSModelLayers;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.client.model.PawnModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PawnRenderer extends GeoEntityRenderer<PawnEntity>
{
	
	public PawnRenderer(EntityRendererProvider.Context context)
	{
		super(context, new HumanoidModel<>(context.bakeLayer(MSModelLayers.PAWN)), 0.5F);
	}
	
	@Override
	protected float getDeathMaxRotation(PawnEntity entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public void renderEarly(PawnEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		this.entity = animatable;
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
	
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("right_hand"))
		{
			stack.pushPose();
			stack.translate(0.36D, 0.58, -0.22D);
			stack.mulPose(Vector3f.XP.rotationDegrees(-80));
			Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlotType.MAINHAND), ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, rtb);
			stack.popPose();
			bufferIn = rtb.getBuffer(RenderType.entitySolid(whTexture));
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}