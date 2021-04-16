package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class GrayCatMaskModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer Head;
	private final ModelRenderer ear_right_inner_r1;
	private final ModelRenderer ear_left_inner_r1;
	
	public GrayCatMaskModel() {
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 56).addBox(-4.0F, -7.425F, -4.075F, 8.0F, 7.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(33, 33).addBox(-4.0F, -7.425F, -4.25F, 8.0F, 7.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(0, 49).addBox(-4.0F, -7.425F, 4.025F, 8.0F, 7.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(17, 49).addBox(-4.0F, -7.425F, 4.175F, 8.0F, 7.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(18, 48).addBox(4.075F, -7.425F, -4.075F, 0.0F, 7.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(18, 48).addBox(-4.025F, -7.425F, -4.075F, 0.0F, 7.0F, 8.0F, 0.0F, false);
		
		ear_right_inner_r1 = new ModelRenderer(this);
		ear_right_inner_r1.setRotationPoint(-3.1F, -7.95F, -4.25F);
		Head.addChild(ear_right_inner_r1);
		setRotationAngle(ear_right_inner_r1, -0.2618F, -0.0611F, -0.5716F);
		ear_right_inner_r1.setTextureOffset(42, 45).addBox(-4.075F, -1.65F, 0.25F, 8.0F, 3.0F, 0.0F, 0.0F, false);
		ear_right_inner_r1.setTextureOffset(33, 41).addBox(-4.075F, -2.65F, 0.15F, 8.0F, 4.0F, 0.0F, 0.0F, false);
		
		ear_left_inner_r1 = new ModelRenderer(this);
		ear_left_inner_r1.setRotationPoint(3.1F, -7.95F, -4.25F);
		Head.addChild(ear_left_inner_r1);
		setRotationAngle(ear_left_inner_r1, -0.2618F, 0.0611F, 0.5716F);
		ear_left_inner_r1.setTextureOffset(42, 45).addBox(-3.925F, -1.65F, 0.25F, 8.0F, 3.0F, 0.0F, 0.0F, true);
		ear_left_inner_r1.setTextureOffset(33, 41).addBox(-3.925F, -2.65F, 0.15F, 8.0F, 4.0F, 0.0F, 0.0F, true);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}