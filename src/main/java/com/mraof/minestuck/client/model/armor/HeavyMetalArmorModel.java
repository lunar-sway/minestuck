package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class HeavyMetalArmorModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Head;
	private final ModelRenderer Head_r1;
	private final ModelRenderer Head_r2;
	
	public HeavyMetalArmorModel()
	{
		super(1);
		textureWidth = 74;
		textureHeight = 74;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		RightLeg.setTextureOffset(32, 0).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		LeftLeg.setTextureOffset(16, 32).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		Torso.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.setTextureOffset(0, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.19F, false);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(0, 32).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightArm.setTextureOffset(48, 0).addBox(-3.38F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.19F, false);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(24, 16).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftArm.setTextureOffset(48, 16).addBox(-0.62F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.19F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.2F, -4.0F, 8.0F, 8.0F, 8.0F, 0.1F, false);
		Head.setTextureOffset(0, 0).addBox(-0.525F, -6.6F, 3.4F, 1.0F, 1.0F, 1.0F, 0.1F, false);
		Head.setTextureOffset(55, 60).addBox(-4.525F, -6.6F, 4.4F, 9.0F, 13.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(24, 40).addBox(-4.575F, -8.5F, -4.6F, 9.0F, 8.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(46, 37).addBox(-4.575F, -0.5F, -4.601F, 9.0F, 2.0F, 0.0F, 0.0F, false);
		
		Head_r1 = new ModelRenderer(this);
		Head_r1.setRotationPoint(2.7F, -11.35F, -4.6F);
		Head.addChild(Head_r1);
		setRotationAngle(Head_r1, 0.0F, -0.5061F, 0.0F);
		Head_r1.setTextureOffset(0, 48).addBox(1.475F, -3.225F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, true);
		
		Head_r2 = new ModelRenderer(this);
		Head_r2.setRotationPoint(-2.7F, -11.35F, -4.6F);
		Head.addChild(Head_r2);
		setRotationAngle(Head_r2, 0.0F, 0.5061F, 0.0F);
		Head_r2.setTextureOffset(0, 48).addBox(-1.475F, -3.225F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, false);
		
		bipedHeadwear = new ModelRenderer(this);
		bipedHeadwear.setRotationPoint(0.0F, 24.0F, 0.0F);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}