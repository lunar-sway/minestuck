package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.client.model.entity.PawnModel;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PawnRenderer extends GeoEntityRenderer<PawnEntity>
{
	private PawnEntity entity;
	
	public PawnRenderer(EntityRendererProvider.Context context)
	{
		super(context, new PawnModel());
	}
	
	@Override
	protected float getDeathMaxRotation(PawnEntity entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public void renderEarly(PawnEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		this.entity = animatable;
		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
	}
	
	@Override
	public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("right_hand"))
		{
			stack.pushPose();
			stack.translate(0.36D, 0.58, -0.22D);
			stack.mulPose(Vector3f.XP.rotationDegrees(-80));
			Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, rtb, entity.getId());
			stack.popPose();
			bufferIn = rtb.getBuffer(RenderType.entitySolid(whTexture));
		}
		super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}