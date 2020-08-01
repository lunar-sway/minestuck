package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.underling.BasiliskEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BasiliskModel<T extends BasiliskEntity> extends SegmentedModel<T>
{
	private ModelRenderer head;
	private ModelRenderer body;
	private ModelRenderer leftFrontLeg;
	private ModelRenderer rightFrontLeg;
	private ModelRenderer leftBackLeg;
	private ModelRenderer rightBackLeg;
	private ModelRenderer tail0;
	private ModelRenderer tail1;
	private ModelRenderer tail2;
	public BasiliskModel()
	{
		this.textureWidth = 128;
		this.textureHeight = 128;
		float offsetY = 24;
		//setTextureOffset("null.Tongue", 84, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-15F, -16F, -6F, 30, 16, 12);
		head.addBox("Tongue", -3F, -5F, -8F, 6, 12, 2, 0.0f, 84, 0);
		head.setRotationPoint(0F, -16 + offsetY, -24F);
		body = new ModelRenderer(this, 0, 28);
		body.addBox(-7, 0, -20, 14, 14, 40);
		body.setRotationPoint(0F, -24 + offsetY, 0F);
		leftFrontLeg = new ModelRenderer(this, 100, 0);
		leftFrontLeg.addBox(-2, 0, -2, 4, 14, 4);
		leftFrontLeg.setRotationPoint(-9F, -14 + offsetY, -14);
		rightFrontLeg = new ModelRenderer(this, 100, 0);
		rightFrontLeg.addBox(-2, 0, -2, 4, 14, 4);
		rightFrontLeg.setRotationPoint(9F, -14 + offsetY, -14);
		leftBackLeg = new ModelRenderer(this, 100, 0);
		leftBackLeg.addBox(-2, 0, -2, 4, 14, 4);
		leftBackLeg.setRotationPoint(-9F, -14 + offsetY, 14);
		rightBackLeg = new ModelRenderer(this, 100, 0);
		rightBackLeg.addBox(-2, 0, -2, 4, 14, 4);
		rightBackLeg.setRotationPoint(9F, -14 + offsetY, 14);
		tail0 = new ModelRenderer(this, 0, 82);
		tail0.addBox(-5, 0, -2, 10, 10, 20);
		tail0.setRotationPoint(0F, -22 + offsetY, 18F);
		tail1 = new ModelRenderer(this, 60, 82);
		tail1.addBox(-3, 0, -2, 6, 6, 20);
		tail1.setRotationPoint(0F, -18 + offsetY, 36F);
		tail2 = new ModelRenderer(this, 68, 28);
		tail2.addBox(-3, 0, -2, 4, 4, 20);
		tail2.setRotationPoint(0F, -15 + offsetY, 54F);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.rotateAngleY = netHeadYaw / 2 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (headPitch) / (180F / (float)Math.PI);
		this.leftFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.7F * limbSwingAmount;
		this.rightFrontLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.7F * limbSwingAmount;
		this.leftBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightBackLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
		this.tail0.rotateAngleY = MathHelper.cos(limbSwing + (float)Math.PI) * limbSwingAmount / 2;
		this.tail1.rotationPointX = this.tail0.rotationPointX + 18 * MathHelper.sin(this.tail0.rotateAngleY);
		this.tail1.rotationPointZ = this.tail0.rotationPointZ + 18 * MathHelper.cos(this.tail0.rotateAngleY);
//		this.tail1.rotateAngleY = MathHelper.cos(par1 + (float)Math.PI) * -par2 / 2;
		this.tail2.rotationPointX = this.tail1.rotationPointX + 18 * MathHelper.sin(this.tail1.rotateAngleY);
		this.tail2.rotationPointZ = this.tail1.rotationPointZ + 18 * MathHelper.cos(this.tail1.rotateAngleY);
		this.tail2.rotateAngleY = MathHelper.cos(limbSwing + (float)Math.PI) * -limbSwingAmount;
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head, this.body, this.leftFrontLeg, this.rightFrontLeg, this.leftBackLeg, this.rightBackLeg, this.tail0, this.tail1, this.tail2);
	}
}
