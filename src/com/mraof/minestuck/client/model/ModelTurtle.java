package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Turtle_Consort - Cal
 * Created using Tabula 5.1.0
 */
public class ModelTurtle extends ModelBase
{
	public ModelRenderer shape1;
	public ModelRenderer shape2;
	public ModelRenderer shape3;
	public ModelRenderer shape4;
	public ModelRenderer shape5;
	public ModelRenderer shape6;
	public ModelRenderer shape7;
	public ModelRenderer shape8;
	public ModelRenderer shape9;
	public ModelRenderer shape10;
	public ModelRenderer shape11;
	public ModelRenderer shape12;
	public ModelRenderer shape13;
	public ModelRenderer shape14;
	public ModelRenderer shape15;
	public ModelRenderer shape16;
	
	public ModelTurtle()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.shape5 = new ModelRenderer(this, 34, 8);
		this.shape5.setRotationPoint(-2.5F, 4.5F, -4.0F);
		this.shape5.addBox(0.0F, 0.0F, 0.0F, 5, 5, 5, 0.0F);
		this.shape16 = new ModelRenderer(this, 0, 19);
		this.shape16.setRotationPoint(-2.8F, 6.0F, -4.4F);
		this.shape16.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		this.shape10 = new ModelRenderer(this, 0, 0);
		this.shape10.setRotationPoint(-0.5F, 8.2F, -6.9F);
		this.shape10.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
		this.setRotateAngle(shape10, -0.136659280431156F, 0.0F, 0.0F);
		this.shape8 = new ModelRenderer(this, 0, 0);
		this.shape8.setRotationPoint(-1.5F, 8.7F, -7.0F);
		this.shape8.addBox(0.0F, 0.0F, 0.0F, 3, 1, 2, 0.0F);
		this.shape14 = new ModelRenderer(this, 0, 14);
		this.shape14.setRotationPoint(-0.5F, 18.0F, 3.8F);
		this.shape14.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		this.setRotateAngle(shape14, -0.7740535232594852F, 0.0F, 0.0F);
		this.shape9 = new ModelRenderer(this, 0, 0);
		this.shape9.setRotationPoint(-1.5F, 7.0F, -6.6F);
		this.shape9.addBox(0.0F, 0.0F, 0.0F, 3, 2, 2, 0.0F);
		this.setRotateAngle(shape9, -0.17453292519943295F, 0.0F, 0.0F);
		this.shape3 = new ModelRenderer(this, 10, 22);
		this.shape3.setRotationPoint(-4.0F, 9.5F, -1.7F);
		this.shape3.addBox(0.0F, 0.0F, 0.0F, 8, 8, 2, 0.0F);
		this.setRotateAngle(shape3, 0.1308996938995747F, 0.0F, 0.0F);
		this.shape1 = new ModelRenderer(this, 0, 26);
		this.shape1.setRotationPoint(-3.5F, 17.0F, -0.6F);
		this.shape1.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(shape1, 0.0F, 0.0F, 0.06981317007977318F);
		this.shape15 = new ModelRenderer(this, 0, 19);
		this.shape15.setRotationPoint(1.8F, 6.0F, -4.4F);
		this.shape15.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
		this.shape2 = new ModelRenderer(this, 0, 26);
		this.shape2.setRotationPoint(1.5F, 17.0F, -0.6F);
		this.shape2.addBox(0.0F, 0.0F, 0.0F, 2, 4, 2, 0.0F);
		this.setRotateAngle(shape2, 0.0F, 0.0F, -0.06981317007977318F);
		this.shape11 = new ModelRenderer(this, 0, 0);
		this.shape11.setRotationPoint(-1.5F, 9.1F, -5.5F);
		this.shape11.addBox(0.0F, 0.0F, 0.0F, 3, 1, 6, 0.0F);
		this.setRotateAngle(shape11, -0.045553093477052F, 0.0F, 0.0F);
		this.shape6 = new ModelRenderer(this, 0, 0);
		this.shape6.setRotationPoint(-2.0F, 5.7F, -5.3F);
		this.shape6.addBox(0.0F, 0.0F, 0.0F, 4, 4, 2, 0.0F);
		this.setRotateAngle(shape6, 0.1308996938995747F, 0.0F, 0.0F);
		this.shape12 = new ModelRenderer(this, 20, 15);
		this.shape12.setRotationPoint(-1.0F, 16.0F, 2.0F);
		this.shape12.addBox(0.0F, 0.0F, 0.0F, 2, 2, 3, 0.0F);
		this.setRotateAngle(shape12, -0.7285004297824331F, 0.0F, 0.0F);
		this.shape4 = new ModelRenderer(this, 32, 20);
		this.shape4.setRotationPoint(-3.5F, 10.1F, -2.6F);
		this.shape4.addBox(0.0F, 0.0F, 0.0F, 7, 7, 5, 0.0F);
		this.setRotateAngle(shape4, 0.1308996938995747F, 0.0F, 0.0F);
		this.shape13 = new ModelRenderer(this, 8, 13);
		this.shape13.setRotationPoint(-0.5F, 18.4F, 3.0F);
		this.shape13.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
		this.setRotateAngle(shape13, -0.22759093446006054F, 0.0F, 0.0F);
		this.shape7 = new ModelRenderer(this, 0, 0);
		this.shape7.setRotationPoint(-1.5F, 7.0F, -6.6F);
		this.shape7.addBox(0.0F, 0.0F, 0.0F, 3, 3, 2, 0.0F);
		this.setRotateAngle(shape7, 0.3490658503988659F, 0.0F, 0.0F);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		this.shape5.render(f5);
		this.shape16.render(f5);
		this.shape10.render(f5);
		this.shape8.render(f5);
		this.shape14.render(f5);
		this.shape9.render(f5);
		this.shape3.render(f5);
		this.shape1.render(f5);
		this.shape15.render(f5);
		this.shape2.render(f5);
		this.shape11.render(f5);
		this.shape6.render(f5);
		this.shape12.render(f5);
		this.shape4.render(f5);
		this.shape13.render(f5);
		this.shape7.render(f5);
	}
	
	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}