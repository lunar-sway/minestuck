package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15 - 1.16
// Paste this class into your mod and generate all required imports


import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class CrumplyHatModel extends BipedModel<LivingEntity> {
	private final ModelRenderer Head;
	private final ModelRenderer msCrumpled;
	private final ModelRenderer bone;
	private final ModelRenderer cube_r1;
	private final ModelRenderer cube_r2;
	private final ModelRenderer bone2;
	private final ModelRenderer cube_r3;
	private final ModelRenderer cube_r4;
	private final ModelRenderer bone3;
	private final ModelRenderer cube_r5;
	private final ModelRenderer bone4;
	private final ModelRenderer cube_r6;
	private final ModelRenderer bone5;
	private final ModelRenderer cube_r7;

	public CrumplyHatModel()
	{
		super(1);
		texWidth = 64;
		texHeight = 64;

		Head = new ModelRenderer(this);
		Head.setPos(0.0F, 0.0F, 0.0F);
		head = new ModelRenderer(this);
		head.addChild(Head);

		msCrumpled = new ModelRenderer(this);
		msCrumpled.setPos(0.0F, -8.0F, 0.0F);
		Head.addChild(msCrumpled);
		msCrumpled.texOffs(0, 55).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, 0.25F, false);

		bone = new ModelRenderer(this);
		bone.setPos(-2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone);
		

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(3.5F, 0.0F, 0.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.3491F);
		cube_r1.texOffs(0, 15).addBox(-1.5F, -4.0F, -3.0F, 2.0F, 3.0F, 6.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(0.0F, 0.0F, 0.0F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3491F);
		cube_r2.texOffs(0, 24).addBox(-1.0F, -2.0F, -3.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setPos(0.0F, 0.0F, 3.0F);
		msCrumpled.addChild(bone2);
		

		cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(0.0F, 0.0F, 0.0F);
		bone2.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0873F, 0.0F, 0.0F);
		cube_r3.texOffs(24, 10).addBox(-2.0F, -5.0F, -0.5F, 4.0F, 5.0F, 1.0F, 0.0F, false);

		cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, 0.0F, -6.0F);
		bone2.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0873F, 0.0F, 0.0F);
		cube_r4.texOffs(10, 15).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setPos(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone3);
		

		cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(0.0F, 0.0F, 0.0F);
		bone3.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, -0.3491F);
		cube_r5.texOffs(18, 2).addBox(0.0F, -2.0F, -3.0F, 1.0F, 2.0F, 6.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setPos(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone4);
		

		cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(-3.5F, -1.0F, 0.0F);
		bone4.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, -0.2182F);
		cube_r6.texOffs(16, 16).addBox(-0.5F, -4.0F, -3.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);

		bone5 = new ModelRenderer(this);
		bone5.setPos(2.0F, 0.0F, 0.0F);
		msCrumpled.addChild(bone5);
		

		cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(-2.0F, -5.0F, 0.0F);
		bone5.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.0F, 0.1745F);
		cube_r7.texOffs(0, 8).addBox(-2.5F, -0.5F, -3.25F, 6.0F, 1.0F, 6.0F, 0.5F, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}