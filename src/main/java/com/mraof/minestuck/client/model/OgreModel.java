package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class OgreModel<T extends OgreEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart leftArm, rightArm;

	public OgreModel(ModelPart root)
	{
		this.root = root;
		head = root.getChild("head");
		leftArm = root.getChild("left_arm");
		rightArm = root.getChild("right_arm");
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		
		float offsetY = -72 + 24;
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-9, -12, -9, 18, 12, 18)
						.addBox("tusk", -10, -3, -12, 6, 3, 3, 84, 0)
						.addBox("tusk", 4, -3, -12, 6, 3, 3, 84, 0)
						.addBox("tusk", -14, 0, -12, 6, 3, 3, 84, 0)
						.addBox("tusk", 8, 0, -12, 6, 3, 3, 84, 0)
						.addBox("tusk", -15, -3, -12, 2, 3, 3, 84, 0)
						.addBox("tusk", 13, -3, -12, 2, 3, 3, 84, 0),
				PartPose.offset(0, 12 + offsetY, -6));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 30)
						.addBox(-21, 0, -13, 42, 40, 26),
				PartPose.offset(0, 12 + offsetY, 0));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(136, 40)
						.addBox(0, 0, -5, 10, 32, 10),
				PartPose.offset(21, 14 + offsetY, 0));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(136, 40).mirror()
						.addBox(-10, 0, -5, 10, 32, 10),
				PartPose.offset(-21, 14 + offsetY, 0));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(110, 0)
						.addBox(-8, -4, -8, 16, 24, 16),
				PartPose.offset(13, 52 + offsetY, 0));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(110, 0).mirror()
						.addBox(-8, -4, -8, 16, 24, 16),
				PartPose.offset(-13, 52 + offsetY, 0));
		return LayerDefinition.create(mesh, 192, 128);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = (headPitch) / (180F / (float)Math.PI);
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}
}
