package com.mraof.minestuck.client.model.armor; // Made with Blockbench 4.5.2

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AmphibeanieModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 10);
		//MeshDefinition mesh = new MeshDefinition(); "cannot find "hat""
		PartDefinition root = mesh.getRoot();
		
		PartDefinition head = root.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -3.25F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-5.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(1.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.75F, 0.0F, -0.0873F, 0.0F, 0.0F));
		
		PartDefinition leftponytail = head.addOrReplaceChild("leftponytail", CubeListBuilder.create().texOffs(9, 17).addBox(-1.3867F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(23, 21).addBox(-1.3867F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(7, 18).addBox(-0.8867F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 3.75F, 4.25F, -0.2618F, 0.3054F, 0.0F));
		
		PartDefinition rightponytail = head.addOrReplaceChild("rightponytail", CubeListBuilder.create().texOffs(9, 17).mirror().addBox(-1.6133F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 21).mirror().addBox(-1.6133F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 18).mirror().addBox(-1.1133F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 3.75F, 4.25F, -0.2618F, -0.3054F, 0.0F));

		
		/*PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.5F, -3.25F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(-5.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 13).addBox(1.5F, -6.25F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 50.0F, 0.0F));
		
		head.addOrReplaceChild("leftponytail", CubeListBuilder.create()
						.texOffs(9, 17).addBox(-1.3867F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(23, 21).addBox(-1.3867F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(7, 18).addBox(-0.8867F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(3.0F, 3.75F, 4.25F, -0.2618F, 0.3054F, 0.0F));
		
		head.addOrReplaceChild("rightponytail", CubeListBuilder.create()
						.texOffs(9, 17).mirror().addBox(-1.6133F, -2.1354F, 1.0285F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 21).mirror().addBox(-1.6133F, -2.1354F, 4.0285F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(6, 18).mirror().addBox(-1.1133F, -1.6354F, 0.0285F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offsetAndRotation(-3.0F, 3.75F, 4.25F, -0.2618F, -0.3054F, 0.0F));*/
		
		return LayerDefinition.create(mesh, 48, 48);
	}
}