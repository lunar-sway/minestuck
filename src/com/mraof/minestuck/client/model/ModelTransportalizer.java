package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelTransportalizer extends ModelMachine
{
	ModelRenderer bottom;
	ModelRenderer top;

	public ModelTransportalizer()
	{
		textureWidth = 128;
		textureHeight = 128;

		bottom = new ModelRenderer(this, 0, 0);
		bottom.addBox(0F, 0F, 0F, 16, 6, 16);
		bottom.setRotationPoint(0F, -6, 0F);
		setRotation(bottom, 0F, 0F, 0F);
		top = new ModelRenderer(this, 0, 22);
		top.addBox(0F, 0F, 0F, 14, 2, 14);
		top.setRotationPoint(1F, -8F, 1F);
	}

	@Override
	public void render(float scale)
	{
		bottom.render(scale);
		top.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
