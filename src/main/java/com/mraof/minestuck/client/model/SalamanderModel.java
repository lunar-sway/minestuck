package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class SalamanderModel<T extends ConsortEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart head;
	private final ModelPart upperJaw, lowerJaw;
	private final ModelPart hood;


	public SalamanderModel(ModelPart root)
	{
		this.root = root;
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		head = root.getChild("head");
		upperJaw = root.getChild("upper_jaw");
		lowerJaw = root.getChild("lower_jaw");
		hood = root.getChild("hood");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(10, 18)
						.addBox(-3, 0, -3, 6, 8, 6),
				PartPose.offset(0, 12, 0));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 25)
						.addBox(-2, 0, -2, 2, 4, 3),
				PartPose.offset(3, 20, 0));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 18)
						.addBox(-2, 0, -2, 2, 4, 3),
				PartPose.offset(-1, 20, 0));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-3, -4, -3.5F, 6, 4, 7),
				PartPose.offset(0, 12, 0));
		root.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(0, 11)
						.addBox(-2, -2, -6, 4, 2, 3),
				PartPose.offset(0, 11, 0));
		root.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(14, 11)
						.addBox(-2, -2, -6, 4, 1, 3),
				PartPose.offset(0, 13, 0));
		root.addOrReplaceChild("upper_tail", CubeListBuilder.create().texOffs(34, 18)
						.addBox(-1, 0, -1, 2, 4, 2),
				PartPose.offsetAndRotation(0, 18, 3, 0.22307169437408447F, 0, 0));
		root.addOrReplaceChild("lower_tail", CubeListBuilder.create().texOffs(34, 24)
						.addBox(-1, 0, -3, 2, 2, 6),
				PartPose.offset(0, 22, 6));
		root.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(26, 0)
						.addBox(-4, -5, -4, 8, 5, 8),
				PartPose.offset(0, 12, 0));
		
		return LayerDefinition.create(mesh, 64, 32);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float angleY = netHeadYaw / (180F / (float)Math.PI);
		float angleX = headPitch / (180F / (float)Math.PI);
		head.yRot = hood.yRot = upperJaw.yRot = lowerJaw.yRot = angleY;
		head.xRot = hood.xRot = upperJaw.xRot = lowerJaw.xRot = angleX;
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}
}
