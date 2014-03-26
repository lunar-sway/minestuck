package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelAlchemiter extends ModelMachine
{
	//fields
	ModelRenderer LowerBase;
	ModelRenderer MiddleBase;
	ModelRenderer UpperBase;
	ModelRenderer DowelPlatform;
	ModelRenderer LongRod;
	ModelRenderer Hinge1;
	ModelRenderer Hinge2;
	ModelRenderer ShortRod1;
	ModelRenderer ShortRod2;
	ModelRenderer Reader;
	ModelRenderer SideThing1;
	ModelRenderer SideThing2;
	ModelRenderer SideThing3;
	ModelRenderer SideThing4;

	public ModelAlchemiter()
	{
		textureWidth = 128;
		textureHeight = 128;

		LowerBase = new ModelRenderer(this, 0, 0);
		LowerBase.addBox(0F, 0F, 0F, 32, 8, 32);
		LowerBase.setRotationPoint(0F, -8F, 0F);
		LowerBase.setTextureSize(128, 128);
		LowerBase.mirror = true;
		setRotation(LowerBase, 0F, 0F, 0F);
		MiddleBase = new ModelRenderer(this, 0, 40);
		MiddleBase.addBox(0F, 0F, 0F, 28, 4, 28);
		MiddleBase.setRotationPoint(2F, -12F, 2F);
		MiddleBase.setTextureSize(128, 128);
		MiddleBase.mirror = true;
		setRotation(MiddleBase, 0F, 0F, 0F);
		UpperBase = new ModelRenderer(this, 0, 72);
		UpperBase.addBox(0F, 0F, 0F, 24, 4, 24);
		UpperBase.setRotationPoint(4F, -16F, 4F);
		UpperBase.setTextureSize(128, 128);
		UpperBase.mirror = true;
		setRotation(UpperBase, 0F, 0F, 0F);
		DowelPlatform = new ModelRenderer(this, 112, 40);
		DowelPlatform.addBox(0F, 0F, 0F, 4, 10, 4);
		DowelPlatform.setRotationPoint(27F, -18F, 1F);
		DowelPlatform.setTextureSize(128, 128);
		DowelPlatform.mirror = true;
		setRotation(DowelPlatform, 0F, 0F, 0F);
		LongRod = new ModelRenderer(this, 112, 54);
		LongRod.addBox(0F, 0F, 0F, 2, 22, 2);
		LongRod.setRotationPoint(23F, -30F, 1F);
		LongRod.setTextureSize(128, 128);
		LongRod.mirror = true;
		setRotation(LongRod, 0F, 0F, 0F);
		Hinge1 = new ModelRenderer(this, 96, 72);
		Hinge1.addBox(0F, 0F, 0F, 3, 1, 2);
		Hinge1.setRotationPoint(24F, -31F, 1F);
		Hinge1.setTextureSize(128, 128);
		Hinge1.mirror = true;
		setRotation(Hinge1, 0F, 0F, 0F);
		Hinge2 = new ModelRenderer(this, 96, 72);
		Hinge2.addBox(0F, 0F, 0F, 3, 1, 2);
		Hinge2.setRotationPoint(27F, -22F, 1F);
		Hinge2.setTextureSize(128, 128);
		Hinge2.mirror = true;
		setRotation(Hinge2, 0F, 0F, 0F);
		ShortRod2 = new ModelRenderer(this, 120, 54);
		ShortRod2.addBox(0F, 0F, 0F, 2, 8, 2);
		ShortRod2.setRotationPoint(29F, -30F, 1F);
		ShortRod2.setTextureSize(128, 128);
		ShortRod2.mirror = true;
		setRotation(ShortRod2, 0F, 0F, 0F);
		Reader = new ModelRenderer(this, 96, 75);
		Reader.addBox(0F, 0F, 0F, 4, 4, 4);
		Reader.setRotationPoint(30F, -34F, 0F);
		Reader.setTextureSize(128, 128);
		Reader.mirror = true;
		setRotation(Reader, 0F, 0F, 0F);
		SideThing1 = new ModelRenderer(this, 96, 86);
		SideThing1.addBox(0F, 0F, 0F, 1, 2, 5);
		SideThing1.setRotationPoint(1F, -10F, 14F);
		SideThing1.setTextureSize(128, 128);
		SideThing1.mirror = true;
		setRotation(SideThing1, 0F, 0F, 0F);
		SideThing2 = new ModelRenderer(this, 96, 86);
		SideThing2.addBox(0F, 0F, 0F, 1, 2, 5);
		SideThing2.setRotationPoint(30F, -10F, 14F);
		SideThing2.setTextureSize(128, 128);
		SideThing2.mirror = true;
		setRotation(SideThing2, 0F, 0F, 0F);
		SideThing3 = new ModelRenderer(this, 96, 83);
		SideThing3.addBox(0F, 0F, 0F, 5, 2, 1);
		SideThing3.setRotationPoint(14F, -10F, 30F);
		SideThing3.setTextureSize(128, 128);
		SideThing3.mirror = true;
		setRotation(SideThing3, 0F, 0F, 0F);
		SideThing4 = new ModelRenderer(this, 96, 83);
		SideThing4.addBox(0F, 0F, 0F, 5, 2, 1);
		SideThing4.setRotationPoint(12F, -10F, 1F);
		SideThing4.setTextureSize(128, 128);
		SideThing4.mirror = true;
		setRotation(SideThing4, 0F, 0F, 0F);
		ShortRod1 = new ModelRenderer(this, 120, 54);
		ShortRod1.addBox(0F, 0F, 0F, 2, 8, 2);
		ShortRod1.setRotationPoint(26F, -30F, 1F);
		ShortRod1.setTextureSize(128, 128);
		ShortRod1.mirror = true;
		setRotation(ShortRod1, 0F, 0F, 0F);
	}

	@Override
	public void render(float scale)
	{
		LowerBase.render(scale);
		MiddleBase.render(scale);
		UpperBase.render(scale);
		DowelPlatform.render(scale);
		LongRod.render(scale);
		Hinge1.render(scale);
		Hinge2.render(scale);
		ShortRod2.render(scale);
		Reader.render(scale);
		SideThing1.render(scale);
		SideThing2.render(scale);
		SideThing3.render(scale);
		SideThing4.render(scale);
		ShortRod1.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
