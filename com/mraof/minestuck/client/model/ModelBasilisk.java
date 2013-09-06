package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBasilisk extends ModelBase
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
	public ModelBasilisk()
	{
		float offsetY = 24;
		setTextureOffset("null.Tongue", 0, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-15F, -16F, -6F, 30, 16, 12);
		head.addBox("Tongue", -2, -3, -8, 4, 8, 2);
		head.setRotationPoint(0F, -16 + offsetY, -24F);
		body = new ModelRenderer(this, 0, 12);
		body.addBox(-7, 0, -20, 14, 14, 40);
		body.setRotationPoint(0F, -24 + offsetY, 0F);
		leftFrontLeg = new ModelRenderer(this, 0, 12);
		leftFrontLeg.addBox(-2, 0, -2, 4, 14, 4);
		leftFrontLeg.setRotationPoint(-9F, -14 + offsetY, -14);
		rightFrontLeg = new ModelRenderer(this, 0, 12);
		rightFrontLeg.addBox(-2, 0, -2, 4, 14, 4);
		rightFrontLeg.setRotationPoint(9F, -14 + offsetY, -14);
		leftBackLeg = new ModelRenderer(this, 0, 12);
		leftBackLeg.addBox(-2, 0, -2, 4, 14, 4);
		leftBackLeg.setRotationPoint(-9F, -14 + offsetY, 14);
		rightBackLeg = new ModelRenderer(this, 0, 12);
		rightBackLeg.addBox(-2, 0, -2, 4, 14, 4);
		rightBackLeg.setRotationPoint(9F, -14 + offsetY, 14);
		tail0 = new ModelRenderer(this, 0, 14);
		tail0.addBox(-5, 0, -2, 10, 10, 20);
		tail0.setRotationPoint(0F, -22 + offsetY, 18F);
		tail1 = new ModelRenderer(this, 0, 14);
		tail1.addBox(-3, 0, -2, 6, 6, 20);
		tail1.setRotationPoint(0F, -18 + offsetY, 36F);
		tail2 = new ModelRenderer(this, 0, 14);
		tail2.addBox(-3, 0, -2, 4, 4, 20);
		tail2.setRotationPoint(0F, -15 + offsetY, 54F);
	}
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		this.head.render(par7);
		this.body.render(par7);
		this.leftFrontLeg.render(par7);
		this.rightFrontLeg.render(par7);
		this.leftBackLeg.render(par7);
		this.rightBackLeg.render(par7);
		this.tail0.render(par7);
		this.tail1.render(par7);
		this.tail2.render(par7);
	}
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		this.head.rotateAngleY = par4 / 2 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (par5) / (180F / (float)Math.PI);
		this.leftFrontLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.7F * par2;
		this.rightFrontLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.7F * par2;
		this.leftBackLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
		this.rightBackLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;
		this.tail0.rotateAngleY = MathHelper.cos(par1 + (float)Math.PI) * par2 / 3;
		this.tail1.rotationPointX = this.tail0.rotationPointX + 18 * MathHelper.sin(this.tail0.rotateAngleY);
		this.tail1.rotationPointZ = this.tail0.rotationPointZ + 18 * MathHelper.cos(this.tail0.rotateAngleY);
//		this.tail1.rotateAngleY = MathHelper.cos(par1 + (float)Math.PI) * -par2 / 2;
		this.tail2.rotationPointX = this.tail1.rotationPointX + 18 * MathHelper.sin(this.tail1.rotateAngleY);
		this.tail2.rotationPointZ = this.tail1.rotationPointZ + 18 * MathHelper.cos(this.tail1.rotateAngleY);
		this.tail2.rotateAngleY = MathHelper.cos(par1 + (float)Math.PI) * -par2;
	}
}
