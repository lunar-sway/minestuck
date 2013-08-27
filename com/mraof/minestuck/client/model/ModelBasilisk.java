package com.mraof.minestuck.client.model;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.entity.underling.EntityGiclops;

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
	}
	public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
		this.head.render(par7);
		this.body.render(par7);
	}
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) 
	{
		this.head.rotateAngleY = par4 / 2 / (180F / (float)Math.PI);
		this.head.rotateAngleX = (par5) / (180F / (float)Math.PI);
//		this.leftFrontLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 0.7F * par2;
//		this.rightFrontLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 0.7F * par2;
//		this.leftBackLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.2F * par2 * 0.5F;
//		this.rightBackLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.2F * par2 * 0.5F;

	}
}
