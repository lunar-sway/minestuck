package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.client.model.PawnModel;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PawnRenderer extends GeoEntityRenderer<PawnEntity>
{
	private PawnEntity entity;
	
	public PawnRenderer(EntityRendererManager renderManager)
	{
		super(renderManager, new PawnModel());
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