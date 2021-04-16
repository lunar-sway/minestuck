package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class JuniorEctobiologistsLabSuitModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Head;
	
	public JuniorEctobiologistsLabSuitModel()
	{
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		RightLeg.setTextureOffset(24, 16).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightLeg.setTextureOffset(0, 0).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.19F, false);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		LeftLeg.setTextureOffset(0, 32).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftLeg.setTextureOffset(48, 0).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.19F, false);
		
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		Torso.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.setTextureOffset(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.17F, false);
		Torso.setTextureOffset(32, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.24F, false);
		Torso.setTextureOffset(40, 16).addBox(-4.0F, 11.3F, -2.0F, 8.0F, 4.0F, 4.0F, 0.24F, false);
		Torso.setTextureOffset(48, 0).addBox(-0.875F, 8.1F, -2.325F, 2.0F, 3.0F, 0.0F, 0.0F, false);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(16, 32).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightArm.setTextureOffset(0, 48).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.17F, false);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(32, 0).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftArm.setTextureOffset(41, 48).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.17F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		
		
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