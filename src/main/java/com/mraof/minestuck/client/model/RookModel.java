package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.carapacian.RookEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;

public class RookModel<T extends RookEntity> extends EntityModel<T>
{
	private RendererModel head;
	private RendererModel leftLeg;
	private RendererModel rightLeg;
	private RendererModel body;
	private RendererModel leftArm;
	private RendererModel rightArm;

	public RookModel()
	{
		float offsetY = 24;
		textureHeight = 128;
		textureWidth = 256;
		//setTextureOffset("null.Tower", 60, 0);
		//setTextureOffset("null.Lump", 124, 0);
		head = new RendererModel(this, 0, 0);
		head.addBox(-9F, -6F, -2F, 18, 16, 12);
		head.setRotationPoint(0F, -45F + offsetY, -22F);
		body = new RendererModel(this, 0, 30);
		body.addBox(-20F, 0F, -16F, 40, 40, 26);
		body.func_217178_a("Tower", 2F, -12F, -6F, 16, 12, 16, 0.0f, 60, 0);
		body.func_217178_a("Lump", -18F, 4F, -21F, 8, 16, 8, 0.0f, 124, 0);
		body.setRotationPoint(0F, -52F + offsetY, 0F);
		leftLeg = new RendererModel(this, 60, 0);
		leftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		leftLeg.setRotationPoint(18F, -12 + offsetY, -6F);
		rightLeg = new RendererModel(this, 60, 0);
		rightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
		rightLeg.setRotationPoint(-18F, -12 + offsetY, -6F);
		rightArm = new RendererModel(this, 156, 0);
		rightArm.addBox(-16F, 0F, -8F, 16, 48, 16);
		rightArm.setRotationPoint(-13F, -50F + offsetY, 0F);
		rightArm.rotateAngleZ = .26F;
		leftArm = new RendererModel(this, 156, 0);
		leftArm.addBox(0F, 0F, -8F, 16, 48, 16);
		leftArm.setRotationPoint(13F, -50F + offsetY, 0F);
		leftArm.rotateAngleZ = -.26F;
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

	@Override
	public void setRotationAngles(T par7Entity, float par1, float par2, float par3, float par4, float par5, float par6)
	{
		this.head.rotateAngleY = par4 / (360F / (float)Math.PI);
		this.head.rotateAngleX = (par5) / (360F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.7F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.7F * par2;
		this.leftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;
	}
}
