package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.carapacian.BishopEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class BishopModel<T extends BishopEntity> extends SegmentedModel<T>
{
	private ModelRenderer hatKnob;
	private ModelRenderer head;
	private ModelRenderer leftLeg;
	private ModelRenderer rightLeg;
	private ModelRenderer body;
	private ModelRenderer leftArm;
	private ModelRenderer rightArm;
	private int heldItemLeft;
	private int heldItemRight;


	public BishopModel()
	{
		/*addBox(offsetx,offsety,offsetz,sizex,sizey,sizez)
		 * setRotationPoint(x,y,z) 
		 * x is width, y is height (lower numbers are higher), z is length
		 */
		float offsetY = -46;
		this.textureWidth = 128;
		this.textureHeight = 128;
		hatKnob = new ModelRenderer(this, 0, 0);
		hatKnob.addBox(-2F, -34F, -2F, 4, 4, 4);
		hatKnob.setRotationPoint(0F, 33F + offsetY, -4F);

		head = new ModelRenderer(this, 0, 0);
		head.addBox(-8F, -30F, -8F, 16, 30, 16);
		head.setRotationPoint(0F, 33F + offsetY, -4F);

		body = new ModelRenderer(this, 0, 46);
		body.addBox(-15F, 0F, -10F, 30, 32, 20);
		body.setRotationPoint(0F, 29F + offsetY, 0F);

		leftArm = new ModelRenderer(this, 64, 0);
		leftArm.addBox(-3F, 0F, -3F, 6, 24, 6);
		leftArm.setRotationPoint(-15F, 31F + offsetY, 0F);

		rightArm = new ModelRenderer(this, 64, 0);
		rightArm.addBox(-3F, 0F, -3F, 6, 24, 6);
		rightArm.setRotationPoint(15F, 31F + offsetY, 0F);
		rightArm.mirror = true;

		leftLeg = new ModelRenderer(this, 88, 0);
		leftLeg.addBox(-4F, 0F, -5F, 6, 15, 8);
		leftLeg.setRotationPoint(-10F, 55F + offsetY, 0F);

		rightLeg = new ModelRenderer(this, 88, 0);
		rightLeg.addBox(-4F, 0F, -5F, 6, 15, 8);
		rightLeg.setRotationPoint(10F, 55F + offsetY, 0F);
		rightLeg.mirror = true;
	}
	
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.head.rotateAngleX = (headPitch + 20) / (180F / (float)Math.PI);
		this.hatKnob.rotateAngleY = this.head.rotateAngleY;
		this.hatKnob.rotateAngleX = this.head.rotateAngleX;
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;

		if (this.heldItemLeft != 0)
		{
			this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemLeft;
		}

		if (this.heldItemRight != 0)
		{
			this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
		}
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.hatKnob, this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
	}
}
