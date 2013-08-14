package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCruxtruder extends ModelBase {

	//fields
	ModelRenderer Base;
	ModelRenderer Middle;
	ModelRenderer Tube;
	ModelRenderer Timer1;
	ModelRenderer Timer2;
	ModelRenderer Timer3;
	ModelRenderer Timer4;

	public ModelCruxtruder()
	{
		textureWidth = 128;
		textureHeight = 128;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(0F, 0F, 0F, 32, 8, 32);
		Base.setRotationPoint(0F, 0F, 0F);
		Base.setTextureSize(128, 128);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
		Middle = new ModelRenderer(this, 0, 40);
		Middle.addBox(0F, 0F, 0F, 20, 10, 20);
		Middle.setRotationPoint(6F, -10F, 6F);
		Middle.setTextureSize(128, 128);
		Middle.mirror = true;
		setRotation(Middle, 0F, 0F, 0F);
		Tube = new ModelRenderer(this, 60, 40);
		Tube.addBox(0F, 0F, 0F, 8, 12, 8);
		Tube.setRotationPoint(12F, -22F, 12F);
		Tube.setTextureSize(128, 128);
		Tube.mirror = true;
		setRotation(Tube, 0F, 0F, 0F);
		Timer1 = new ModelRenderer(this, 0, 12);
		Timer1.addBox(0F, 0F, 0F, 8, 4, 4);
		Timer1.setRotationPoint(12F, -4F, 2F);
		Timer1.setTextureSize(128, 128);
		Timer1.mirror = true;
		setRotation(Timer1, 0F, 0F, 0F);
		Timer2 = new ModelRenderer(this, 0, 12);
		Timer2.addBox(0F, 0F, 0F, 8, 4, 4);
		Timer2.setRotationPoint(12F, -4F, 26F);
		Timer2.setTextureSize(128, 128);
		Timer2.mirror = true;
		setRotation(Timer2, 0F, 0F, 0F);
		Timer3 = new ModelRenderer(this, 0, 0);
		Timer3.addBox(0F, 0F, 0F, 4, 4, 8);
		Timer3.setRotationPoint(2F, -4F, 12F);
		Timer3.setTextureSize(128, 128);
		Timer3.mirror = true;
		setRotation(Timer3, 0F, 0F, 0F);
		Timer4 = new ModelRenderer(this, 0, 0);
		Timer4.addBox(0F, 0F, 0F, 4, 4, 8);
		Timer4.setRotationPoint(26F, -4F, 12F);
		Timer4.setTextureSize(128, 128);
		Timer4.mirror = true;
		setRotation(Timer4, 0F, 0F, 0F);
	}

	public void render(float f5)
	{
		Base.render(f5);
		Middle.render(f5);
		Tube.render(f5);
		Timer1.render(f5);
		Timer2.render(f5);
		Timer3.render(f5);
		Timer4.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	

}
