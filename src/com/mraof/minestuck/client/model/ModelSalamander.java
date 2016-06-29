package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.opengl.GL11;

public class ModelSalamander extends ModelBase 
{
	ModelRenderer body;
	ModelRenderer rightLeg;
	ModelRenderer leftLeg;
	ModelRenderer head;
	ModelRenderer upperTail;
	ModelRenderer lowerTail;
	ModelRenderer upperJaw;
	ModelRenderer lowerJaw;
	ModelRenderer hood;


	public ModelSalamander()
	{
		this(false);
	}
	public ModelSalamander(boolean hasArms)
	{
		/*ModelRenderer(this, textureOffsetX, textureOffsetY)
		 * When figuring out the texture offset, the width will be 2 * sizeZ + 2 * sizeX, and the height will be sizeY + sizeZ
		 * addBox(offsetX,offsetY,offsetZ,sizeX,sizeY,sizeZ)
		 * setRotationPoint(x,y,z) 
		 * x is width, y is height (lower numbers are higher), z is length
		 * 16 is 1 meter (width of one block)
		 * bottom is 24
		 */
		textureWidth = 64;
		textureHeight = 32;

		body = new ModelRenderer(this, 16, 16);
		body.addBox(-3F, 0F, -3F, 6, 8, 6);
		body.setRotationPoint(0F, 12F, 0F);
		rightLeg = new ModelRenderer(this, 0, 16);
		rightLeg.addBox(-2F, 0F, -2F, 2, 4, 3);
		rightLeg.setRotationPoint(-1F, 20F, 0F);
		leftLeg = new ModelRenderer(this, 0, 16);
		leftLeg.addBox(-2F, 0F, -2F, 2, 4, 3);
		leftLeg.setRotationPoint(3F, 20F, 0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3F, -8F, -3.5F, 6, 4, 7);
		head.setRotationPoint(0F, 16F, 0F);
		upperTail = new ModelRenderer(this, 0, 16);
		upperTail.addBox(-1F, 0F, -1F, 2, 4, 2);
		upperTail.setRotationPoint(0F, 18F, 3F);
		setRotation(upperTail, 0.2230717F, 0F, 0F);
		lowerTail = new ModelRenderer(this, 0, 16);
		lowerTail.addBox(-1F, 0F, -3F, 2, 2, 6);
		lowerTail.setRotationPoint(0F, 22F, 6F);
		upperJaw = new ModelRenderer(this, 0, 0);
		upperJaw.addBox(-2F, -7F, -6F, 4, 2, 3);
		upperJaw.setRotationPoint(0F, 16F, 0F);
		lowerJaw = new ModelRenderer(this, 48, 26);
		lowerJaw.addBox(-2F, -5F, -6F, 4, 1, 3);
		lowerJaw.setRotationPoint(0F, 16F, 0F);
		hood = new ModelRenderer(this, 32, 0);
		hood.addBox(-4F, -9F, -4F, 8, 5, 8);
		hood.setRotationPoint(0F, 16F, 0F);
		
	}
	
	@Override
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		
		if (this.isChild)
		{
			float var8 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
			GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
			head.render(par7);
			upperJaw.render(par7);
			lowerJaw.render(par7);
			hood.render(par7);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
//			if(hasArms)
//			{
//				this.rightArm.render(par7);
//				this.leftArm.render(par7);
//			}
			this.body.render(par7);
			this.rightLeg.render(par7);
			this.leftLeg.render(par7);
			this.upperTail.render(par7);
			this.lowerTail.render(par7);
			GL11.glPopMatrix();
		}
		else
		{
			head.render(par7);
			upperJaw.render(par7);
			lowerJaw.render(par7);
			hood.render(par7);
//			if(hasArms)
//			{
//				this.rightArm.render(par7);
//				this.leftArm.render(par7);
//			}
			this.body.render(par7);
			this.rightLeg.render(par7);
			this.leftLeg.render(par7);
			this.upperTail.render(par7);
			this.lowerTail.render(par7);
		}
	}
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
	}

}
