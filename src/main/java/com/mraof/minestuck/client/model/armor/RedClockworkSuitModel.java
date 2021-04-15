package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class RedClockworkSuitModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer Torso_r1;
	private final ModelRenderer Torso_r2;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Head;
	
	public RedClockworkSuitModel()
	{
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		RightLeg.setTextureOffset(32, 0).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightLeg.setTextureOffset(48, 0).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		LeftLeg.setTextureOffset(16, 32).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftLeg.setTextureOffset(32, 32).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		Torso.setTextureOffset(40, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.setTextureOffset(30, 49).addBox(-5.45F, -2.1F, -2.675F, 8.0F, 12.0F, 0.0F, 0.0F, false);
		
		Torso_r1 = new ModelRenderer(this);
		Torso_r1.setRotationPoint(4.8477F, 15.2336F, 2.5114F);
		Torso.addChild(Torso_r1);
		setRotationAngle(Torso_r1, 0.048F, 0.0F, -0.1134F);
		Torso_r1.setTextureOffset(47, 51).addBox(-3.8F, -4.55F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, true);
		
		Torso_r2 = new ModelRenderer(this);
		Torso_r2.setRotationPoint(-4.8477F, 15.2336F, 2.5114F);
		Torso.addChild(Torso_r2);
		setRotationAngle(Torso_r2, 0.048F, 0.0F, 0.1134F);
		Torso_r2.setTextureOffset(47, 51).addBox(-4.2F, -4.55F, 0.0F, 8.0F, 12.0F, 0.0F, 0.0F, false);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(0, 48).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightArm.setTextureOffset(0, 32).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(48, 34).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftArm.setTextureOffset(24, 16).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.225F, -4.0F, 8.0F, 8.0F, 8.0F, 0.1F, false);
		
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