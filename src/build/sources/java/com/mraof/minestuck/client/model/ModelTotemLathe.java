package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelRenderer;

public class ModelTotemLathe extends ModelMachine
{
	//fields
	ModelRenderer LeftLowerPillar;
	ModelRenderer LeftUpperPillar;
	ModelRenderer UpperBar;
	ModelRenderer MiddlePillar;
	ModelRenderer LowerBar;
	ModelRenderer UpperSupport;
	ModelRenderer LatheKnife;
	ModelRenderer LowerSupport;
	ModelRenderer CuttingBoard;
	ModelRenderer MiddleBar;
	ModelRenderer LatheBase;
	ModelRenderer LatheTop;
	ModelRenderer LatheWheel;
	ModelRenderer RightHolder;
	ModelRenderer LeftHolder;

	public ModelTotemLathe()
	{
		textureWidth = 128;
		textureHeight = 128;

		LeftLowerPillar = new ModelRenderer(this, 32, 0);
		LeftLowerPillar.addBox(0F, 0F, 0F, 6, 20, 11);
		LeftLowerPillar.setRotationPoint(26F, -20F, 10F);
		LeftLowerPillar.setTextureSize(128, 128);
		LeftLowerPillar.mirror = true;
		setRotation(LeftLowerPillar, 0F, 0F, 0F);
		LeftUpperPillar = new ModelRenderer(this, 66, 0);
		LeftUpperPillar.addBox(0F, 0F, 0F, 6, 8, 9);
		LeftUpperPillar.setRotationPoint(26F, -28F, 10F);
		LeftUpperPillar.setTextureSize(128, 128);
		LeftUpperPillar.mirror = true;
		setRotation(LeftUpperPillar, 0F, 0F, 0F);
		UpperBar = new ModelRenderer(this, 0, 38);
		UpperBar.addBox(0F, 0F, 0F, 20, 4, 12);
		UpperBar.setRotationPoint(12F, -32F, 10F);
		UpperBar.setTextureSize(128, 128);
		UpperBar.mirror = true;
		setRotation(UpperBar, 0F, 0F, 0F);
		MiddlePillar = new ModelRenderer(this, 0, 0);
		MiddlePillar.addBox(0F, 0F, 0F, 4, 26, 12);
		MiddlePillar.setRotationPoint(22F, -28F, 10F);
		MiddlePillar.setTextureSize(128, 128);
		MiddlePillar.mirror = true;
		setRotation(MiddlePillar, 0F, 0F, 0F);
		LowerBar = new ModelRenderer(this, 36, 79);
		LowerBar.addBox(0F, 0F, 0F, 26, 2, 12);
		LowerBar.setRotationPoint(0F, -2F, 10F);
		LowerBar.setTextureSize(128, 128);
		LowerBar.mirror = true;
		setRotation(LowerBar, 0F, 0F, 0F);
		UpperSupport = new ModelRenderer(this, 66, 17);
		UpperSupport.addBox(0F, 0F, 0F, 8, 4, 12);
		UpperSupport.setRotationPoint(14F, -28F, 10F);
		UpperSupport.setTextureSize(128, 128);
		UpperSupport.mirror = true;
		setRotation(UpperSupport, 0F, 0F, 0F);
		LatheKnife = new ModelRenderer(this, 36, 67);
		LatheKnife.addBox(0F, 0F, 0F, 2, 2, 10);
		LatheKnife.setRotationPoint(15F, -24F, 11F);
		LatheKnife.setTextureSize(128, 128);
		LatheKnife.mirror = true;
		setRotation(LatheKnife, 0F, 0F, 0F);
		LowerSupport = new ModelRenderer(this, 64, 33);
		LowerSupport.addBox(0F, 0F, 0F, 10, 3, 10);
		LowerSupport.setRotationPoint(12F, -5F, 11F);
		LowerSupport.setTextureSize(128, 128);
		LowerSupport.mirror = true;
		setRotation(LowerSupport, 0F, 0F, 0F);
		CuttingBoard = new ModelRenderer(this, 0, 54);
		CuttingBoard.addBox(0F, 0F, 0F, 12, 1, 12);
		CuttingBoard.setRotationPoint(10F, -6F, 10F);
		CuttingBoard.setTextureSize(128, 128);
		CuttingBoard.mirror = true;
		setRotation(CuttingBoard, 0F, 0F, 0F);
		MiddleBar = new ModelRenderer(this, 96, 0);
		MiddleBar.addBox(0F, 0F, 0F, 4, 2, 6);
		MiddleBar.setRotationPoint(8F, -4F, 13F);
		MiddleBar.setTextureSize(128, 128);
		MiddleBar.mirror = true;
		setRotation(MiddleBar, 0F, 0F, 0F);
		LatheBase = new ModelRenderer(this, 64, 46);
		LatheBase.addBox(0F, 0F, 0F, 4, 8, 10);
		LatheBase.setRotationPoint(4F, -10F, 11F);
		LatheBase.setTextureSize(128, 128);
		LatheBase.mirror = true;
		setRotation(LatheBase, 0F, 0F, 0F);
		LatheTop = new ModelRenderer(this, 0, 67);
		LatheTop.addBox(0F, 0F, 0F, 8, 4, 10);
		LatheTop.setRotationPoint(2F, -14F, 11F);
		LatheTop.setTextureSize(128, 128);
		LatheTop.mirror = true;
		setRotation(LatheTop, 0F, 0F, 0F);
		LatheWheel = new ModelRenderer(this, 48, 54);
		LatheWheel.addBox(0F, 0F, 0F, 1, 6, 6);
		LatheWheel.setRotationPoint(1F, -15F, 13F);
		LatheWheel.setTextureSize(128, 128);
		LatheWheel.mirror = true;
		setRotation(LatheWheel, 0F, 0F, 0F);
		RightHolder = new ModelRenderer(this, 40, 31);
		RightHolder.addBox(0F, 0F, 0F, 2, 2, 2);
		RightHolder.setRotationPoint(10F, -13F, 15F);
		RightHolder.setTextureSize(128, 128);
		RightHolder.mirror = true;
		setRotation(RightHolder, 0F, 0F, 0F);
		LeftHolder = new ModelRenderer(this, 32, 31);
		LeftHolder.addBox(0F, 0F, 0F, 2, 2, 2);
		LeftHolder.setRotationPoint(20F, -13F, 15F);
		LeftHolder.setTextureSize(128, 128);
		LeftHolder.mirror = true;
		setRotation(LeftHolder, 0F, 0F, 0F);
	}

	@Override
	public void render(float scale)
	{
		LeftLowerPillar.render(scale);
		LeftUpperPillar.render(scale);
		UpperBar.render(scale);
		MiddlePillar.render(scale);
		LowerBar.render(scale);
		UpperSupport.render(scale);
		LatheKnife.render(scale);
		LowerSupport.render(scale);
		CuttingBoard.render(scale);
		MiddleBar.render(scale);
		LatheBase.render(scale);
		LatheTop.render(scale);
		LatheWheel.render(scale);
		RightHolder.render(scale);
		LeftHolder.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
