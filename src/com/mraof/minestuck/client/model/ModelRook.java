package com.mraof.minestuck.client.model;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelRook extends ModelBase
{
	private ModelRenderer head;
	private ModelRenderer leftLeg;
	private ModelRenderer rightLeg;
	private ModelRenderer body;
	private ModelRenderer leftArm;
	private ModelRenderer rightArm;

	public ModelRook()
	{
		float offsetY = 24;
		textureHeight = 128;
		textureWidth = 256;
		setTextureOffset("null.Tower", 60, 0);
		setTextureOffset("null.Lump", 124, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-9F, -6F, -2F, 18, 16, 12);
		head.setRotationPoint(0F, -45F + offsetY, -22F);
		body = new ModelRenderer(this, 0, 30);
		body.addBox(-20F, 0F, -16F, 40, 40, 26);
		body.addBox("Tower", 2F, -12F, -6F, 16, 12, 16);
		body.addBox("Lump", -18F, 4F, -21F, 8, 16, 8);
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
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		this.body.render(par7);
		this.rightArm.render(par7);
		this.leftArm.render(par7);
		this.rightLeg.render(par7);
		this.leftLeg.render(par7);
		this.head.render(par7);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) 
	{
		this.head.rotateAngleY = par4 / (360F / (float)Math.PI);
		this.head.rotateAngleX = (par5) / (360F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.7F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.7F * par2;
		this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;
	}
}
