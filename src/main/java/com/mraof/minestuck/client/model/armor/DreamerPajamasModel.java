package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class DreamerPajamasModel
{
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0);
		PartDefinition root = mesh.getRoot();
		
		root.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 51).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.17F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));
		
		root.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.1F))
						.texOffs(40, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2F))
						.texOffs(40, 49).addBox(-4.0F, 11.85F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.21F))
						.texOffs(40, 58).addBox(-4.0F, 12.1F, -2.0F, 8.0F, 2.0F, 4.0F, new CubeDeformation(0.19F)),
				PartPose.ZERO);
		
		root.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.26F))
						.texOffs(0, 32).addBox(-3.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.13F))
						.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
				PartPose.offset(-5.0F, 2.0F, 0.0F));
		
		root.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(0, 0).mirror().addBox(-0.7F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.26F)).mirror(false)
						.texOffs(24, 16).addBox(-0.9F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.13F))
						.texOffs(16, 0).addBox(-0.7F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.2F)),
				PartPose.offset(5.0F, 2.0F, 0.0F));
		
		root.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(16, 32).mirror().addBox(-2.225F, 0.75F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.27F)).mirror(false)
						.texOffs(32, 0).mirror().addBox(-2.125F, 0.175F, -2.025F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.075F)).mirror(false)
						.texOffs(48, 10).addBox(-2.2F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.2F)),
				PartPose.offset(0.05F, 12.0F, 0.0F));
		
		root.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(32, 0).addBox(-1.875F, 0.175F, -2.025F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.075F))
						.texOffs(16, 32).addBox(-1.775F, 0.75F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.27F))
						.texOffs(48, 10).addBox(-1.8F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.2F)),
				PartPose.offset(-0.05F, 12.0F, 0.0F));
		
		return LayerDefinition.create(mesh, 64, 64);
	}
}