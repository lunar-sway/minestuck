package com.mraof.minestuck.client.model.entity;

import com.mraof.minestuck.entity.FrogEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * FrogModel - Cibernet
 * Created using Tabula 7.0.0
 */
public class FrogModel<T extends FrogEntity> extends HierarchicalModel<T>
{
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart left_eye, right_eye;
	private final ModelPart left_top_leg, right_top_leg;
	private final ModelPart left_bottom_leg, right_bottom_leg;
	private final ModelPart left_foot, right_foot;
	private final ModelPart left_arm, right_arm;
	private float jumpRotation;
	
	public FrogModel(ModelPart root)
	{
		this.root = root;
		head = root.getChild("head");
		left_eye = root.getChild("left_eye");
		right_eye = root.getChild("right_eye");
		left_top_leg = root.getChild("left_top_leg");
		right_top_leg = root.getChild("right_top_leg");
		left_bottom_leg = root.getChild("left_bottom_leg");
		right_bottom_leg = root.getChild("right_bottom_leg");
		left_foot = root.getChild("left_foot");
		right_foot = root.getChild("right_foot");
		left_arm = root.getChild("left_arm");
		right_arm = root.getChild("right_arm");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
						.addBox(-3.0F, -2.0F, -8.0F, 6, 5, 8),
				PartPose.offsetAndRotation(0, 19, 8, Mth.TWO_PI * -1 / 18, 0, 0));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 0)
						.addBox(-3.5F, -4.0F, -5.0F, 7, 4, 5),
				PartPose.offset(0, 17, 1));
		root.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(52, 5)
						.addBox(1.5F, -5.0F, -4.5F, 3, 2, 3),
				PartPose.offset(0, 17, 1));
		root.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(52, 5)
						.addBox(-4.5F, -5.0F, -4.5F, 3, 2, 3),
				PartPose.offset(0, 17, 1));
		root.addOrReplaceChild("left_top_leg", CubeListBuilder.create().texOffs(30, 16)
						.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5),
				PartPose.offsetAndRotation(3, 17.5F, 3.7F, Mth.TWO_PI * 313 / 7200, 0, 0));
		root.addOrReplaceChild("right_top_leg", CubeListBuilder.create().texOffs(16, 16)
						.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5),
				PartPose.offsetAndRotation(-3, 17.5F, 3.7F, Mth.TWO_PI * 313 / 7200, 0, 0));
		root.addOrReplaceChild("left_bottom_leg", CubeListBuilder.create().texOffs(28, 24)
						.addBox(-1.1F, 3.5F, -4.3F, 2, 2, 6),
				PartPose.offsetAndRotation(3, 17.5F, 3.7F, Mth.TWO_PI / 6, 0, 0));
		root.addOrReplaceChild("right_bottom_leg", CubeListBuilder.create().texOffs(44, 24)
						.addBox(-0.9F, 3.5F, -4.3F, 2, 2, 6),
				PartPose.offsetAndRotation(-3, 17.5F, 3.7F, Mth.TWO_PI / 6, 0, Mth.TWO_PI / 1800));
		root.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(14, 26)
						.addBox(-1.1F, 5.4F, -2.5F, 2, 1, 5),
				PartPose.offset(3, 17.5F, 3.7F));
		root.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(0, 26)
						.addBox(-0.9F, 5.4F, -2.5F, 2, 1, 5),
				PartPose.offset(-3, 17.5F, 3.7F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(8, 15)
						.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2),
				PartPose.offsetAndRotation(3, 17, 1, Mth.TWO_PI * -11 / 360, 0, 0));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 15)
						.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2),
				PartPose.offsetAndRotation(-3, 17, 1, Mth.TWO_PI * -11 / 360, 0, 0));
		
		return LayerDefinition.create(mesh, 64, 32);
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float f = ageInTicks - (float)entityIn.tickCount;
		
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = headPitch / (180F / (float)Math.PI);
		this.left_eye.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.left_eye.xRot = headPitch / (180F / (float)Math.PI);
		this.right_eye.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.right_eye.xRot = headPitch / (180F / (float)Math.PI);
		this.jumpRotation = Mth.sin(entityIn.setJumpCompletion(f) * (float)Math.PI);
		this.left_top_leg.xRot = this.jumpRotation * 50.0F * 0.017453292F + 0.27314402793711257F;
		this.right_top_leg.xRot = this.jumpRotation * 50.0F * 0.017453292F + 0.27314402793711257F;
		this.left_bottom_leg.xRot = this.jumpRotation * 50.0F * 0.017453292F + 1.0471975511965976F;
		this.right_bottom_leg.xRot = this.jumpRotation * 50.0F * 0.017453292F + 1.0471975511965976F;
		this.left_foot.xRot = this.jumpRotation * 50.0F * 0.017453292F;
		this.right_foot.xRot = this.jumpRotation * 50.0F * 0.017453292F;
		this.left_arm.xRot = this.jumpRotation * -50.0F * 0.017453292F - 0.19198621771937624F;
		this.right_arm.xRot = this.jumpRotation * -50.0F * 0.017453292F - 0.19198621771937624F;
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTickTime)
	{
		super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTickTime);
		this.jumpRotation = Mth.sin(entity.setJumpCompletion(partialTickTime) * (float)Math.PI);
	}
}
