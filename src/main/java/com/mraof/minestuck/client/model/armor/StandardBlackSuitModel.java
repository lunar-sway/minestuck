package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Dweblenod

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class StandardBlackSuitModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer bipedRightLeg;
	private final ModelRenderer RightLeg;
	private final ModelRenderer RightLeg_r1;
	private final ModelRenderer bipedLeftLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer bipedBody;
	private final ModelRenderer Torso;
	private final ModelRenderer Torso_r1;
	private final ModelRenderer bipedRightArm;
	private final ModelRenderer RightArm;
	private final ModelRenderer bipedLeftArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer bipedHead;
	private final ModelRenderer Head;
	private final ModelRenderer bipedHeadwear;
	
	public StandardBlackSuitModel()
	{
		super(1);
		textureWidth = 52;
		textureHeight = 52;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		
		
		RightLeg_r1 = new ModelRenderer(this);
		RightLeg_r1.setRotationPoint(1.9F, 12.0F, 0.0F);
		RightLeg.addChild(RightLeg_r1);
		setRotationAngle(RightLeg_r1, 0.0F, 3.1416F, 0.0F);
		RightLeg_r1.setTextureOffset(36, 36).addBox(0.0F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		setRotationAngle(LeftLeg, 0.0F, 3.1416F, 0.0F);
		LeftLeg.setTextureOffset(32, 0).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		
		
		Torso_r1 = new ModelRenderer(this);
		Torso_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
		Torso.addChild(Torso_r1);
		setRotationAngle(Torso_r1, 0.0F, 3.1416F, 0.0F);
		Torso_r1.setTextureOffset(0, 16).addBox(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, true);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(24, 24).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(65, 15).addBox(-4.475F, -8.075F, -4.425F, 9.0F, 8.0F, 8.0F, 0.0F, false);
		
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