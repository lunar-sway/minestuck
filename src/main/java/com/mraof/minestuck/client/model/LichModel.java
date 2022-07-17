package com.mraof.minestuck.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mraof.minestuck.entity.underling.LichEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Lich - Caldw3ll
 * Created using Tabula 6.0.0
 */
public class LichModel<T extends LichEntity> extends HierarchicalModel<T>
{
	public float[] modelScale = { 0.9F, 1.5F, 0.9F };
	private final ModelPart root;
	private final ModelPart leftLeg, rightLeg;
	private final ModelPart lowerBody, midBody, upperBody;
	private final ModelPart leftArm, rightArm;
	private final ModelPart leftForearm, rightForearm;
	private final ModelPart leftHand, rightHand;
	private final ModelPart neck;
	private final ModelPart head, jaw;
	private final ModelPart leftHornBase, rightHornBase;
	private final ModelPart leftHornSpikeBase, rightHornSpikeBase;
	private final ModelPart leftHornSpike, rightHornSpike;

	public LichModel(ModelPart root)
	{
		this.root = root;
		leftLeg = root.getChild("left_leg");
		rightLeg = root.getChild("right_leg");
		lowerBody = root.getChild("lower_body");
		midBody = root.getChild("mid_body");
		upperBody = root.getChild("upper_body");
		leftArm = root.getChild("left_arm");
		rightArm = root.getChild("right_arm");
		leftForearm = root.getChild("left_forearm");
		rightForearm = root.getChild("right_forearm");
		leftHand = root.getChild("left_hand");
		rightHand = root.getChild("right_hand");
		neck = root.getChild("neck");
		head = root.getChild("head");
		jaw = root.getChild("jaw");
		leftHornBase = root.getChild("left_horn_base");
		rightHornBase = root.getChild("right_horn_base");
		leftHornSpikeBase = root.getChild("left_horn_spike_base");
		rightHornSpikeBase = root.getChild("right_horn_spike_base");
		leftHornSpike = root.getChild("left_horn_spike");
		rightHornSpike = root.getChild("right_horn_spike");
	}
	
	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();
		
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(8, 0)
						.addBox(0, 0, 0, 2, 18, 2),
				PartPose.offset(2, 23, -5));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0)
						.addBox(0, 0, 0, 2, 18, 2),
				PartPose.offset(2, 23, 3));
		root.addOrReplaceChild("lower_body", CubeListBuilder.create().texOffs(16, 0)
						.addBox(0, 0, 0, 6, 9, 10),
				PartPose.offsetAndRotation(-3.9F, 15.6F, -5, 0, 0, -0.22759093446006054F));
		root.addOrReplaceChild("mid_body", CubeListBuilder.create().texOffs(6, 19)
						.addBox(0, 0, 0, 6, 9, 10),
				PartPose.offset(-3.9F, 7.6F, -5));
		root.addOrReplaceChild("upper_body", CubeListBuilder.create().texOffs(28, 28)
						.addBox(0, 0, 0, 6, 8, 10),
				PartPose.offsetAndRotation(-1.7F, -0.1F, -5, 0, 0, 0.27314402793711257F));
		root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(20, 0)
						.addBox(0, 0, 0, 1, 9, 1),
				PartPose.offsetAndRotation(4.6F, 4.8F, -6, 0, 0, 0.136659280431156F));
		root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(16, 0)
						.addBox(0, 0, 0, 1, 9, 1),
				PartPose.offsetAndRotation(4.6F, 4.8F, 5, 0, 0, 0.136659280431156F));
		root.addOrReplaceChild("left_forearm", CubeListBuilder.create().texOffs(56, 0)
						.addBox(0, 0, 0, 1, 12, 1),
				PartPose.offsetAndRotation(0.2F, 2.4F, -6, 0, 0, -0.2792526803190927F));
		root.addOrReplaceChild("right_forearm", CubeListBuilder.create().texOffs(52, 0)
						.addBox(0, 0, 0, 1, 12, 1),
				PartPose.offsetAndRotation(0.2F, 2.4F, 5, 0, 0, -0.2792526803190927F));
		root.addOrReplaceChild("left_hand", CubeListBuilder.create().texOffs(48, 13)
						.addBox(0, 0, 0, 2, 1, 2),
				PartPose.offsetAndRotation(5, 4.3F, -6.4F, 0, 0, 0.5462880558742251F));
		root.addOrReplaceChild("right_hand", CubeListBuilder.create().texOffs(54, 14)
						.addBox(0, 0, 0, 2, 1, 2),
				PartPose.offsetAndRotation(5, 3.7F, 4.5F, 0, 0, 0.5462880558742251F));
		root.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(38, 0)
						.addBox(0, 0, 0, 3, 5, 4),
				PartPose.offset(1.1F, -3.1F, -2));
		root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 19)
						.addBox(0, 0, 0, 9, 4, 4),
				PartPose.offsetAndRotation(3, -7.1F, -2, 0, 0, 1.0016444577195458F));
		root.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(48, 16)
						.addBox(0, 0, 0, 2, 1, 2),
				PartPose.offsetAndRotation(7.7F, 0.2F, -1, 0, 0, 1.0471975511965976F));
		root.addOrReplaceChild("left_horn_base", CubeListBuilder.create().texOffs(0, 38)
						.addBox(0, 0, 0, 2, 3, 6),
				PartPose.offset(2, -4, -6.1F));
		root.addOrReplaceChild("right_horn_base", CubeListBuilder.create().texOffs(0, 20)
						.addBox(0, 0, 0, 2, 3, 6),
				PartPose.offset(2, -4, 0));
		root.addOrReplaceChild("left_horn_spike_base", CubeListBuilder.create().texOffs(50, 20)
						.addBox(0, 0, 0, 2, 1, 2),
				PartPose.offset(2, -5, -6.1F));
		root.addOrReplaceChild("right_horn_spike_base", CubeListBuilder.create().texOffs(54, 17)
						.addBox(0, 0, 0, 2, 1, 2),
				PartPose.offset(2, -5, 4));
		root.addOrReplaceChild("left_horn_spike", CubeListBuilder.create().texOffs(48, 0)
						.addBox(0, 0, 0, 1, 3, 1),
				PartPose.offset(2.5F, -8, -5.6F));
		root.addOrReplaceChild("right_horn_spike", CubeListBuilder.create().texOffs(38, 0)
						.addBox(0, 0, 0, 1, 3, 1),
				PartPose.offset(2.5F, -8, -4.5F));
		
		return LayerDefinition.create(mesh, 64, 64);
	}
	
	@Override
	public ModelPart root()
	{
		return root;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		poseStack.scale(1F / modelScale[0], 1F / modelScale[1], 1F / modelScale[2]);
		poseStack.translate(0F, -0.5F, 0F);
		poseStack.mulPose(new Quaternion(Vector3f.YP, 90.0F, true));
		super.renderToBuffer(poseStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		//TO-DO: Add head movement and fix head and jaw rotation point
		this.leftLeg.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.zRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}
}
