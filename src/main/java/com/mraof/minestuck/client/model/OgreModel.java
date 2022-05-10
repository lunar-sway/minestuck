package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class OgreModel<T extends OgreEntity> extends SegmentedModel<T>
{
	private ModelRenderer head;
	private ModelRenderer leftLeg;
	private ModelRenderer rightLeg;
	private ModelRenderer body;
	private ModelRenderer leftArm;
	private ModelRenderer rightArm;

	public OgreModel()
	{
		float offsetY = -72 + 24;
		this.texWidth = 192;
		this.texHeight = 128;
		//setTextureOffset("null.Tusk", 84, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-9F, -12F, -9F, 18, 12, 18);
		head.addBox("Tusk", -10F, -3F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.addBox("Tusk", 4F, -3F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.addBox("Tusk", -14F, 0F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.addBox("Tusk", 8F, 0F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.addBox("Tusk", -15F, -3F, -12F, 2, 3, 3, 0.0f, 84, 0);
		head.addBox("Tusk", 13F, -3F, -12F, 2, 3, 3, 0.0f, 84, 0);
		head.setPos(0F, 12F + offsetY, -6F);
		body = new ModelRenderer(this, 0, 30);
		body.addBox(-21, 0, -13, 42, 40, 26);
		body.setPos(0F, 12F + offsetY, 0F);
		leftArm = new ModelRenderer(this, 136, 40);
		leftArm.addBox(0, 0, -5, 10, 32, 10);
		leftArm.setPos(21F, 14F + offsetY, 0F);
		rightArm = new ModelRenderer(this, 136, 40);
		rightArm.addBox(-10, 0, -5, 10, 32, 10);
		rightArm.setPos(-21F, 14F + offsetY, 0F);
		rightArm.mirror = true;
		leftLeg = new ModelRenderer(this, 110, 0);
		leftLeg.addBox(-8F, -4F, -8F, 16, 24, 16);
		leftLeg.setPos(13F, 52F + offsetY, 0F);
		rightLeg = new ModelRenderer(this, 110, 0);
		rightLeg.addBox(-8F, -4F, -8F, 16, 24, 16);
		rightLeg.setPos(-13F, 52F + offsetY, 0F);
		rightLeg.mirror = true;
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw / (180F / (float)Math.PI);
		this.head.xRot = (headPitch) / (180F / (float)Math.PI);
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}

}
