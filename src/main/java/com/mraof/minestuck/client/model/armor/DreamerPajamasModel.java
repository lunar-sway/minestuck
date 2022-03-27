package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class DreamerPajamasModel extends BipedModel<LivingEntity>
{
	//if making a model from Blockbench, do not include the variables that start with biped. Instead replace those instances with the public BipedModel variables
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Head;
	
	public DreamerPajamasModel()
	{
		super(1);
		texWidth = 64;
		texHeight = 64;
		
		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(0.0F, 12.0F, 0.0F);
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(0.0F, 0.0F, 0.0F);
		rightLeg.addChild(RightLeg);
		RightLeg.texOffs(32, 0).addBox(-2.05F, -0.01F, -2.0F, 4.0F, 10F, 4.0F, 0.075F, false); //pant leg
		RightLeg.texOffs(48, 10).addBox(-2.2F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, 0.2F, false); //shoe
		
		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(0.0F, 0.0F, 0.0F);
		leftLeg.addChild(LeftLeg);
		LeftLeg.texOffs(32, 0).addBox(-1.95F, -0.01F, -2.0F, 4.0F, 10F, 4.0F, 0.075F, false); //pant leg
		LeftLeg.texOffs(48, 10).addBox(-1.8F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, 0.2F, false); //shoe
		
		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(Torso);
		Torso.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.texOffs(40, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.texOffs(40, 49).addBox(-4.0F, 11.85F, -2.0F, 8.0F, 2.0F, 4.0F, 0.21F, false);
		
		rightArm = new ModelRenderer(this);
		rightArm.setPos(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setPos(0.0F, 0.0F, 0.0F);
		rightArm.addChild(RightArm);
		RightArm.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, false);
		RightArm.texOffs(0, 32).addBox(-3.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.13F, false);
		RightArm.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		leftArm = new ModelRenderer(this);
		leftArm.setPos(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(0.0F, 0.0F, 0.0F);
		leftArm.addChild(LeftArm);
		LeftArm.texOffs(0, 0).addBox(-0.7F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, true);
		LeftArm.texOffs(24, 16).addBox(-0.9F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.13F, false);
		LeftArm.texOffs(16, 0).addBox(-0.7F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(Head);
		Head.texOffs(0, 51).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 5.0F, 8.0F, 0.17F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
