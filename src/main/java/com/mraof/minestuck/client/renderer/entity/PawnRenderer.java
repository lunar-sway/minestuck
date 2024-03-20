package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.carapacian.PawnEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PawnRenderer extends GeoEntityRenderer<PawnEntity>
{
	
	public PawnRenderer(EntityRendererProvider.Context context, EnumEntityKingdom kingdom)
	{
		super(context, new DefaultedEntityGeoModel<PawnEntity>(Minestuck.id("carapacian/pawn"), true)
				.withAltTexture(Minestuck.id(kingdom == EnumEntityKingdom.DERSITE ? "carapacian/derse_pawn" : "carapacian/prospit_pawn")));
	}
	
	@Override
	protected float getDeathMaxRotation(PawnEntity entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public void renderRecursively(PoseStack poseStack, PawnEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		if(bone.getName().equals("right_hand"))
		{
			poseStack.pushPose();
			poseStack.translate(0.36D, 0.58, -0.22D);
			poseStack.mulPose(Axis.XP.rotationDegrees(-80));
			Minecraft.getInstance().getItemRenderer().renderStatic(animatable.getItemBySlot(EquipmentSlot.MAINHAND), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, poseStack, bufferSource, animatable.level(), animatable.getId());
			poseStack.popPose();
			buffer = bufferSource.getBuffer(RenderType.entitySolid(getTextureLocation(animatable)));
		}
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}
}