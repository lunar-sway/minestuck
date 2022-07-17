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

public class NakagatorModel<T extends ConsortEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart head;
	private final ModelPart upperJaw, lowerJaw;
	
	public NakagatorModel(ModelPart root)
	{
		this.root = root;
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		head = root.getChild("head");
		upperJaw = root.getChild("upper_jaw");
		lowerJaw = root.getChild("lower_jaw");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(10, 17)
						.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6),
				PartPose.offset(0, 10, 0));
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 24)
						.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3),
				PartPose.offset(3, 19, 0));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16)
						.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3),
				PartPose.offset(-1, 19, 0));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-3.0F, -1.3F, -3.0F, 6, 3, 7),
				PartPose.offset(0, 8.3F, 0));
		root.addOrReplaceChild("upper_jaw", CubeListBuilder.create().texOffs(34, 0)
						.addBox(-2.0F, 0.0F, -12.0F, 4, 1, 11),
				PartPose.offset(0, 8.1F, 0));
		root.addOrReplaceChild("lower_jaw", CubeListBuilder.create().texOffs(34, 12)
						.addBox(-2.0F, 0.0F, -10.0F, 4, 1, 11),
				PartPose.offset(0, 9, 0));
		root.addOrReplaceChild("upper_tail", CubeListBuilder.create().texOffs(26, 9)
						.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2),
				PartPose.offsetAndRotation(0, 18, 3, 0.22307169437408447F, 0, 0));
		root.addOrReplaceChild("lower_tail", CubeListBuilder.create().texOffs(26, 0)
						.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 7),
				PartPose.offset(0, 22, 4));
		
		return LayerDefinition.create(mesh, 64, 32);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = headPitch / (180F / (float)Math.PI);
		this.upperJaw.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.upperJaw.xRot = headPitch / (180F / (float)Math.PI);
		this.lowerJaw.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.lowerJaw.xRot = headPitch / (180F / (float)Math.PI);
		this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}
}
