package com.mraof.minestuck.client.model.armor; // Made with Blockbench 4.5.2 by Riotmode

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AmphibeanieModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
		PartDefinition root = mesh.getRoot();
		
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition beanie = head.addOrReplaceChild("beanie", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.5F, -3.25F, -4.5F, 9.0F, 4.0F, 9.0F) //beanie itself
				.texOffs(0, 13).addBox(-5.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F) //right eye
				.texOffs(0, 13).addBox(1.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F), //left eye
				PartPose.offsetAndRotation(0.0F, -5.25F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		PartDefinition leftpigtail = beanie.addOrReplaceChild("leftpigtail", CubeListBuilder.create()
				.texOffs(9, 17).addBox(-1.3867F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F)
				.texOffs(23, 21).addBox(-1.3867F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F)
				.texOffs(7, 18).addBox(-0.8867F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(3.0F, 3.75F, 4.25F, -0.2618F, 0.3054F, 0.0F));
		
		PartDefinition rightpigtail = beanie.addOrReplaceChild("rightpigtail", CubeListBuilder.create()
				.texOffs(9, 17).addBox(-1.6133F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F)
				.texOffs(23, 21).addBox(-1.6133F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F)
				.texOffs(7, 18).addBox(-1.1133F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F),
				PartPose.offsetAndRotation(-3.0F, 3.75F, 4.25F, -0.2618F, -0.3054F, 0.0F));
		
		return LayerDefinition.create(mesh, 48, 48);
	}
}