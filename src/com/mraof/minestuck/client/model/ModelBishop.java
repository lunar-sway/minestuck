package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBishop extends ModelBase 
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


	public ModelBishop()
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
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

		this.body.render(par7);
		this.rightArm.render(par7);
		this.leftArm.render(par7);
		this.rightLeg.render(par7);
		this.leftLeg.render(par7);
		this.head.render(par7);
		this.hatKnob.render(par7);
	}
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) 
	{
		this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (par5 + 20) / (180F / (float)Math.PI);
		this.hatKnob.rotateAngleY = this.head.rotateAngleY;
		this.hatKnob.rotateAngleX = this.head.rotateAngleX;
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
		this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 2.0F * par2 * 0.5F;

		if (this.heldItemLeft != 0)
		{
			this.leftArm.rotateAngleX = this.leftArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemLeft;
		}

		if (this.heldItemRight != 0)
		{
			this.rightArm.rotateAngleX = this.rightArm.rotateAngleX * 0.5F - ((float)Math.PI / 10F) * (float)this.heldItemRight;
		}
	}
}
