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
		
		PartDefinition ponytail_top = head.addOrReplaceChild("ponytail_top", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 2.0F), //has hairband
				PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.3491F, 0.0F, 0.0F));
		
		PartDefinition ponytail_middle = ponytail_top.addOrReplaceChild("ponytail_middle", CubeListBuilder.create()
				.texOffs(0, 4).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F),
				PartPose.offsetAndRotation(0.0F, 1.0F, 1.0F, -0.1745F, 0.0F, 0.0F));
		
		PartDefinition ponytail_bottom = ponytail_middle.addOrReplaceChild("ponytail_bottom", CubeListBuilder.create()
				.texOffs(0, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F)
				.texOffs(0, 4).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 0.0F, 2.0F), //inserted inside the other box, 2D
				PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.3927F, 0.0F, 0.0F));
		
		return LayerDefinition.create(mesh, 64, 64);
	}
}