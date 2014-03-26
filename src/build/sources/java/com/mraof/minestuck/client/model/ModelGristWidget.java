package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelGristWidget extends ModelMachine {

	//fields
	ModelRenderer Base;

	public ModelGristWidget()
	{
		textureWidth = 128;
		textureHeight = 128;

		Base = new ModelRenderer(this, 0, 0);
		Base.addBox(0F, 0F, 8F, 32, 8, 16);
		Base.setRotationPoint(0F, -8F, 0F);
		Base.setTextureSize(128, 128);
		Base.mirror = true;
		setRotation(Base, 0F, 0F, 0F);
	}

	@Override
	public void render(float scale)
	{
		Base.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
