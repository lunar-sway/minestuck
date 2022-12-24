package com.mraof.minestuck.client.model.armor; // Made with Blockbench 4.5.2 by Riotmode

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PonytailModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
		PartDefinition root = mesh.getRoot();
		
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition ponytail = head.addOrReplaceChild("ponytail", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.3491F, 0.0F, 0.0F));
		
		PartDefinition ponytail2 = ponytail.addOrReplaceChild("ponytail2", CubeListBuilder.create()
				.texOffs(0, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -0.1745F, 0.0F, 0.0F));
		
		PartDefinition ponytail3 = ponytail2.addOrReplaceChild("ponytail3", CubeListBuilder.create()
				.texOffs(0, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F)
				.texOffs(0, 4).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 0.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		
		return LayerDefinition.create(mesh, 64, 64);
	}
}