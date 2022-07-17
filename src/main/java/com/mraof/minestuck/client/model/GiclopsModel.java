package com.mraof.minestuck.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.entity.underling.GiclopsEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class GiclopsModel<T extends GiclopsEntity> extends HierarchicalModel<T>
{
	
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart leftArm, rightArm;

	public GiclopsModel(ModelPart root)
	{
		this.root = root;
		head = root.getChild("head");
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		leftArm = root.getChild("left_arm");
		rightArm = root.getChild("right_arm");
	}
	
	private static final float scaling = 2.0F;
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		float offsetY = -96 + 24 / scaling;
		
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-21, -26, -25, 42, 26, 50)
						.addBox("spike", -2, -34, -6, 4, 8, 4, 0, 0)
						.addBox("spike", -2, -34, -16, 4, 8, 4, 0, 0),
				PartPose.offset(0, 26 + offsetY, -14));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 76)
						.addBox(-32, 0, -20, 64, 64, 40),
				PartPose.offset(0, 16 + offsetY, -8));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(184, 0)
						.addBox(-8, 0, -4, 8, 52, 8),
				PartPose.offset(-32, 28 + offsetY, -8));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(184, 0).mirror()
						.addBox(0, 0, -4, 8, 52, 8),
				PartPose.offset(32, 28 + offsetY, -8));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(120, 180)
						.addBox(-12, 0, -12, 24, 16, 24),
				PartPose.offset(-20, 80 + offsetY, -8));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(120, 180).mirror()
						.addBox(-12, 0, -12, 24, 16, 24),
				PartPose.offset(20, 80 + offsetY, -8));
		root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 180)
						.addBox(-20, -6, -10, 40, 44, 20),
				PartPose.offsetAndRotation(0, 56 + offsetY, 0, Mth.TWO_PI/12, 0, 0));
		
		return LayerDefinition.create(mesh, 256, 256);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = (headPitch + 20) / (180F / (float)Math.PI);
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		super.renderToBuffer(poseStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
}
