package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.underling.BasiliskEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class BasiliskModel<T extends BasiliskEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart leftFrontLeg, rightFrontLeg;
	private final ModelPart leftBackLeg, rightBackLeg;
	private final ModelPart tail0, tail1, tail2;
	
	public BasiliskModel(ModelPart root)
	{
		this.root = root;
		head = root.getChild("head");
		leftFrontLeg = root.getChild("left_front_leg");
		rightFrontLeg = root.getChild("right_front_leg");
		leftBackLeg = root.getChild("left_back_leg");
		rightBackLeg = root.getChild("right_back_leg");
		tail0 = root.getChild("tail0");
		tail1 = root.getChild("tail1");
		tail2 = root.getChild("tail2");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		float offsetY = 24;
		root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-15F, -16F, -6F, 30, 16, 12)
						.addBox("tongue", -3F, -5F, -8F, 6, 12, 2, 84, 0),
				PartPose.offset(0, -16 + offsetY, -24));
		root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 28).addBox(-7, 0, -20, 14, 14, 40),
				PartPose.offset(0, -24 + offsetY, 0));
		root.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
						.texOffs(100, 0).addBox(-2, 0, -2, 4, 14, 4),
				PartPose.offset(-9, -14 + offsetY, -14));
		root.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
						.texOffs(100, 0).addBox(-2, 0, -2, 4, 14, 4),
				PartPose.offset(9, -14 + offsetY, -14));
		root.addOrReplaceChild("left_back_leg", CubeListBuilder.create()
						.texOffs(100, 0).addBox(-2, 0, -2, 4, 14, 4),
				PartPose.offset(-9, -14 + offsetY, 14));
		root.addOrReplaceChild("right_back_leg", CubeListBuilder.create()
						.texOffs(100, 0).addBox(-2, 0, -2, 4, 14, 4),
				PartPose.offset(9, -14 + offsetY, 14));
		root.addOrReplaceChild("tail0", CubeListBuilder.create()
						.texOffs(0, 82).addBox(-5, 0, -2, 10, 10, 20),
				PartPose.offset(0, -22 + offsetY, 18));
		root.addOrReplaceChild("tail1", CubeListBuilder.create()
						.texOffs(60, 82).addBox(-3, 0, -2, 6, 6, 20),
				PartPose.offset(0, -18 + offsetY, 36));
		root.addOrReplaceChild("tail2", CubeListBuilder.create()
						.texOffs(68, 28).addBox(-3, 0, -2, 4, 4, 20),
				PartPose.offset(0, -15 + offsetY, 54));
		
		
		return LayerDefinition.create(mesh, 128, 128);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.yRot = netHeadYaw / 2 / (180F / (float)Math.PI);
		this.head.xRot = (headPitch) / (180F / (float)Math.PI);
		this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftBackLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightBackLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
		this.tail0.yRot = Mth.cos(limbSwing + (float)Math.PI) * limbSwingAmount / 2;
		this.tail1.x = this.tail0.x + 18 * Mth.sin(this.tail0.yRot);
		this.tail1.z = this.tail0.z + 18 * Mth.cos(this.tail0.yRot);
//		this.tail1.rotateAngleY = Mth.cos(par1 + (float)Math.PI) * -par2 / 2;
		this.tail2.x = this.tail1.x + 18 * Mth.sin(this.tail1.yRot);
		this.tail2.z = this.tail1.z + 18 * Mth.cos(this.tail1.yRot);
		this.tail2.yRot = Mth.cos(limbSwing + (float)Math.PI) * -limbSwingAmount;
	}
}
