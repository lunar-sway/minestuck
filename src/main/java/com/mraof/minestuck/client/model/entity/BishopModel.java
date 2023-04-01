package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.entity.carapacian.BishopEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BishopModel<T extends BishopEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart hatKnob;
	private final ModelPart head;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart leftArm, rightArm;
	private int heldItemLeft;
	private int heldItemRight;
	
	public BishopModel(ModelPart root)
	{
		this.root = root;
		hatKnob = root.getChild("hat_knob");
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
		float offsetY = -46;
		root.addOrReplaceChild("hat_knob", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-2, -34, -2, 4, 4, 4),
				PartPose.offset(0, 33 + offsetY, -4));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-8, -30, -8, 16, 30, 16),
				PartPose.offset(0, 33 + offsetY, -4));
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 46)
						.addBox(-15, 0, -10, 30, 32, 20),
				PartPose.offset(0, 29 + offsetY, 0));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(64, 0)
						.addBox(-3, 0, -3, 6, 24, 6),
				PartPose.offset(-15, 31 + offsetY, 0));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(64, 0).mirror()
						.addBox(-3, 0, -3, 6, 24, 6),
				PartPose.offset(15, 31 + offsetY, 0));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(88, 0)
						.addBox(-4, 0, -5, 6, 15, 8),
				PartPose.offset(-10, 55 + offsetY, 0));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(88, 0).mirror()
						.addBox(-4, 0, -5, 6, 15, 8),
				PartPose.offset(10, 55 + offsetY, 0));
		
		return LayerDefinition.create(mesh, 128, 128);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = (headPitch + 20) / (180F / (float)Math.PI);
		this.hatKnob.yRot = this.head.yRot;
		this.hatKnob.xRot = this.head.xRot;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;

		if (this.heldItemLeft != 0)
		{
			this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemLeft;
		}

		if (this.heldItemRight != 0)
		{
			this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
		}
	}
}
