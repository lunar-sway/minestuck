package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class CrumplyHatModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
		PartDefinition root = mesh.getRoot();
		
		//this will render if not made empty
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		//to make things standard, unless the model fits around the players head like a typical helmet the head is made empty and the rest of the model is made offset from it
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition base = head.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 55)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.25F)),
				PartPose.offset(0, -8, 0));
		PartDefinition bone = base.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(-2, 0, 0));
		bone.addOrReplaceChild("r1", CubeListBuilder.create().texOffs(0, 15)
						.addBox(-1.5F, -4.0F, -3.0F, 2.0F, 3.0F, 6.0F),
				PartPose.offsetAndRotation(3.5F, 0, 0, 0, 0, Mth.TWO_PI / 18));
		bone.addOrReplaceChild("r2", CubeListBuilder.create().texOffs(0, 24)
						.addBox(-1.0F, -2.0F, -3.0F, 1.0F, 2.0F, 6.0F),
				PartPose.rotation(0, 0, Mth.TWO_PI / 18));
		PartDefinition bone2 = base.addOrReplaceChild("bone2", CubeListBuilder.create(), PartPose.offset(0, 0, 3));
		bone2.addOrReplaceChild("r3", CubeListBuilder.create().texOffs(24, 10)
						.addBox(-2.0F, -5.0F, -0.5F, 4.0F, 5.0F, 1.0F),
				PartPose.rotation(Mth.TWO_PI / 72, 0, 0));
		bone2.addOrReplaceChild("r4", CubeListBuilder.create().texOffs(10, 15)
						.addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 1.0F),
				PartPose.offsetAndRotation(0, 0, -6, Mth.TWO_PI / 72, 0, 0));
		PartDefinition bone3 = base.addOrReplaceChild("bone3", CubeListBuilder.create(), PartPose.offset(2, 0, 0));
		bone3.addOrReplaceChild("r5", CubeListBuilder.create().texOffs(18, 2)
						.addBox(0.0F, -2.0F, -3.0F, 1.0F, 2.0F, 6.0F),
				PartPose.rotation(0, 0, Mth.TWO_PI * -1 / 18));
		PartDefinition bone4 = base.addOrReplaceChild("bone4", CubeListBuilder.create(), PartPose.offset(2, 0, 0));
		bone4.addOrReplaceChild("r6", CubeListBuilder.create().texOffs(16, 16)
						.addBox(-0.5F, -4.0F, -3.0F, 1.0F, 3.0F, 6.0F),
				PartPose.offsetAndRotation(-3.5F, -1, 0, 0, 0, Mth.TWO_PI * -5 / 144));
		PartDefinition bone5 = base.addOrReplaceChild("bone5", CubeListBuilder.create(), PartPose.offset(2, 0, 0));
		bone5.addOrReplaceChild("r7", CubeListBuilder.create().texOffs(0, 8)
						.addBox(-2.5F, -0.5F, -3.25F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.5F)),
				PartPose.offsetAndRotation(-2, -5, 0, 0, 0, Mth.TWO_PI / 36));
		
		return LayerDefinition.create(mesh, 64, 64);
	}
}