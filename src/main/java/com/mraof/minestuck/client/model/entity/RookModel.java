package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.entity.carapacian.RookEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class RookModel<T extends RookEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart leftArm, rightArm;

	public RookModel(ModelPart root)
	{
		this.root = root;
		head = root.getChild("head");
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		leftArm = root.getChild("left_arm");
		rightArm = root.getChild("right_arm");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		float offsetY = 24;
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-9, -6, -2, 18, 16, 12),
				PartPose.offset(0, -45 + offsetY, -22));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 30)
						.addBox(-20, 0, -16, 40, 40, 26)
						.addBox("tower", 2, -12, -6, 16, 12, 16, 60, 0)
						.addBox("lump", -18, 4, -21, 8, 16, 8, 124, 0),
				PartPose.offset(0, -52 + offsetY, 0));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(60, 0)
						.addBox(-2, 0, -2, 4, 12, 4),
				PartPose.offset(18, -12 + offsetY, -6));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(60, 0)
						.addBox(-2, 0, -2, 4, 12, 4),
				PartPose.offset(-18, -12 + offsetY, -6));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(156, 0)
						.addBox(0, 0, -8, 16, 48, 16),
				PartPose.offsetAndRotation(13, -50 + offsetY, 0, 0, 0, -.26F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(156, 0)
						.addBox(-16, 0, -8, 16, 48, 16),
				PartPose.offsetAndRotation(-13, -50 + offsetY, 0, 0, 0, .26F));
		
		return LayerDefinition.create(mesh, 256, 128);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (360F / (float)Math.PI);
		this.head.xRot = (headPitch) / (360F / (float)Math.PI);
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}
}
