package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.carapacian.RookEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class RookModel<T extends RookEntity> extends SegmentedModel<T>
{
	private ModelRenderer head;
	private ModelRenderer leftLeg;
	private ModelRenderer rightLeg;
	private ModelRenderer body;
	private ModelRenderer leftArm;
	private ModelRenderer rightArm;

	public RookModel()
	{
		float offsetY = 24;
		textureHeight = 128;
		textureWidth = 256;
		//setTextureOffset("null.Tower", 60, 0);
		//setTextureOffset("null.Lump", 124, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-9F, -6F, -2F, 18, 16, 12);
		head.setRotationPoint(0F, -45F + offsetY, -22F);
		body = new ModelRenderer(this, 0, 30);
		body.addBox(-20F, 0F, -16F, 40, 40, 26);
		body.addBox("Tower", 2F, -12F, -6F, 16, 12, 16, 0.0f, 60, 0);
		body.addBox("Lump", -18F, 4F, -21F, 8, 16, 8, 0.0f, 124, 0);
		body.setRotationPoint(0F, -52F + offsetY, 0F);
		leftLeg = new ModelRenderer(this, 60, 0);
		leftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		leftLeg.setRotationPoint(18F, -12 + offsetY, -6F);
		rightLeg = new ModelRenderer(this, 60, 0);
		rightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightLeg.setRotationPoint(-18F, -12 + offsetY, -6F);
		rightArm = new ModelRenderer(this, 156, 0);
		rightArm.addBox(-16F, 0F, -8F, 16, 48, 16);
		rightArm.setRotationPoint(-13F, -50F + offsetY, 0F);
		rightArm.rotateAngleZ = .26F;
		leftArm = new ModelRenderer(this, 156, 0);
		leftArm.addBox(0F, 0F, -8F, 16, 48, 16);
		leftArm.setRotationPoint(13F, -50F + offsetY, 0F);
		leftArm.rotateAngleZ = -.26F;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw / (360F / (float)Math.PI);
		this.head.rotateAngleX = (headPitch) / (360F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}
}
