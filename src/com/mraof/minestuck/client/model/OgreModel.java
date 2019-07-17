package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.underling.OgreEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class OgreModel<T extends OgreEntity> extends EntityModel<T>
{
	private RendererModel head;
	private RendererModel leftLeg;
	private RendererModel rightLeg;
	private RendererModel body;
	private RendererModel leftArm;
	private RendererModel rightArm;

	public OgreModel()
	{
		float offsetY = -72 + 24;
		this.textureWidth = 192;
		this.textureHeight = 128;
		//setTextureOffset("null.Tusk", 84, 0);
		head = new RendererModel(this, 0, 0);
		head.addBox(-9F, -12F, -9F, 18, 12, 18);
		head.func_217178_a("Tusk", -10F, -3F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.func_217178_a("Tusk", 4F, -3F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.func_217178_a("Tusk", -14F, 0F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.func_217178_a("Tusk", 8F, 0F, -12F, 6, 3, 3, 0.0f, 84, 0);
		head.func_217178_a("Tusk", -15F, -3F, -12F, 2, 3, 3, 0.0f, 84, 0);
		head.func_217178_a("Tusk", 13F, -3F, -12F, 2, 3, 3, 0.0f, 84, 0);
		head.setRotationPoint(0F, 12F + offsetY, -6F);
		body = new RendererModel(this, 0, 30);
		body.addBox(-21, 0, -13, 42, 40, 26);
		body.setRotationPoint(0F, 12F + offsetY, 0F);
		leftArm = new RendererModel(this, 136, 40);
		leftArm.addBox(0, 0, -5, 10, 32, 10);
		leftArm.setRotationPoint(21F, 14F + offsetY, 0F);
		rightArm = new RendererModel(this, 136, 40);
		rightArm.addBox(-10, 0, -5, 10, 32, 10);
		rightArm.setRotationPoint(-21F, 14F + offsetY, 0F);
		rightArm.mirror = true;
		leftLeg = new RendererModel(this, 110, 0);
		leftLeg.addBox(-8F, -4F, -8F, 16, 24, 16);
		leftLeg.setRotationPoint(13F, 52F + offsetY, 0F);
		rightLeg = new RendererModel(this, 110, 0);
		rightLeg.addBox(-8F, -4F, -8F, 16, 24, 16);
		rightLeg.setRotationPoint(-13F, 52F + offsetY, 0F);
		rightLeg.mirror = true;
	}
	@Override
	public void render(T entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(entity, par2, par3, par4, par5, par6, par7);
		this.body.render(par7);
		this.rightArm.render(par7);
		this.leftArm.render(par7);
		this.rightLeg.render(par7);
		this.leftLeg.render(par7);
		this.head.render(par7);
	}
	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(T par7Entity, float par1, float par2, float par3, float par4, float par5, float par6)
	{
		this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (par5) / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.7F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.7F * par2;
		this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;
	}

}
