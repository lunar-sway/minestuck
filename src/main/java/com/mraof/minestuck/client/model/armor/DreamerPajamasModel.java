package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class DreamerPajamasModel extends BipedModel<LivingEntity>
{
	//private final ModelRenderer bipedRightLeg;
	private final ModelRenderer RightLeg;
	//private final ModelRenderer bipedLeftLeg;
	private final ModelRenderer LeftLeg;
	//private final ModelRenderer bipedBody;
	private final ModelRenderer Torso;
	//private final ModelRenderer bipedRightArm;
	private final ModelRenderer RightArm;
	//private final ModelRenderer bipedLeftArm;
	private final ModelRenderer LeftArm;
	//private final ModelRenderer bipedHead;
	private final ModelRenderer Head;
	//private final ModelRenderer bipedHeadwear2;
	
	public DreamerPajamasModel()
	{
		super(1);
		texWidth = 64;
		texHeight = 64;
		
		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(-1.9F, 0.0F, 0.0F);
		rightLeg.addChild(RightLeg);
		RightLeg.texOffs(16, 32).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		
		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(1.9F, 0.0F, 0.0F);
		leftLeg.addChild(LeftLeg);
		LeftLeg.texOffs(32, 0).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		
		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(Torso);
		Torso.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.texOffs(40, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.texOffs(40, 49).addBox(-4.0F, 11.85F, -2.0F, 8.0F, 2.0F, 4.0F, 0.2F, false);
		
		rightArm = new ModelRenderer(this);
		rightArm.setPos(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setPos(0.0F, 0.0F, 0.0F);
		rightArm.addChild(RightArm);
		RightArm.texOffs(0, 32).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightArm.texOffs(0, 0).addBox(-3.4F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		RightArm.texOffs(0, 0).addBox(-3.4F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, false);
		RightArm.texOffs(0, 0).addBox(9.4F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, true);
		
		leftArm = new ModelRenderer(this);
		leftArm.setPos(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(0.0F, 0.0F, 0.0F);
		leftArm.addChild(LeftArm);
		LeftArm.texOffs(24, 16).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftArm.texOffs(16, 0).addBox(-0.6F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(Head);
		
		
		hat = new ModelRenderer(this);
		//hat.setPos(0.0F, 24.0F, 0.0F);
		hat.setPos(0.0F, 24.0F, 0.0F);
		hat.texOffs(0, 51).addBox(-4.0F, -36.375F, -4.0F, 8.0F, 5.0F, 8.0F, 0.17F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
