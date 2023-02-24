package com.mraof.minestuck.client.model.armor; // Made with Blockbench 4.5.2 by Riotmode

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class NostrildamusModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
		PartDefinition root = mesh.getRoot();
		
		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition model = head.addOrReplaceChild("model", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -4.0F, -6.0F, 2.0F, 4.0F, 2.0F) //nose
				.texOffs(0, 6).addBox(1.5F, -5.0F, -4.5F, 3.0F, 2.0F, 2.0F) //display
				.texOffs(0, 10).addBox(4.0F, -6.0F, -3.0F, 1.0F, 3.0F, 3.0F), //computer
				PartPose.offset(0.0F, 1.0F, 0.0F));
		
		return LayerDefinition.create(mesh, 16, 16);
	}
}