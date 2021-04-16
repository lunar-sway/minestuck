package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class GumbrawllersGakuranModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer Torso_r1;
	private final ModelRenderer Torso_r2;
	private final ModelRenderer Torso_r3;
	private final ModelRenderer Torso_r4;
	private final ModelRenderer Torso_r5;
	private final ModelRenderer Torso_r6;
	private final ModelRenderer Torso_r7;
	private final ModelRenderer Torso_r8;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Torso_r9;
	private final ModelRenderer Torso_r10;
	private final ModelRenderer Torso_r11;
	private final ModelRenderer Torso_r12;
	private final ModelRenderer Head;
	private final ModelRenderer Head_r1;
	private final ModelRenderer Head_r2;
	private final ModelRenderer Head_r3;
	private final ModelRenderer Head_r4;
	private final ModelRenderer Head_r5;
	
	public GumbrawllersGakuranModel()
	{
		super(1);
		textureWidth = 90;
		textureHeight = 90;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		RightLeg.setTextureOffset(32, 0).addBox(-2.1F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		RightLeg.setTextureOffset(16, 32).addBox(-2.275F, 0.725F, -2.0F, 4.0F, 5.0F, 4.0F, 0.2F, true);
		RightLeg.setTextureOffset(0, 42).addBox(-2.15F, 7.35F, -2.0F, 4.0F, 5.0F, 4.0F, 0.15F, true);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		LeftLeg.setTextureOffset(16, 32).addBox(-1.9F, 0.2F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftLeg.setTextureOffset(16, 32).addBox(-1.725F, 0.725F, -2.0F, 4.0F, 5.0F, 4.0F, 0.2F, false);
		LeftLeg.setTextureOffset(0, 42).addBox(-1.85F, 7.35F, -2.0F, 4.0F, 5.0F, 4.0F, 0.15F, true);
		
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		Torso.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.setTextureOffset(50, 19).addBox(-4.0F, 11.8F, -2.0F, 8.0F, 1.0F, 4.0F, 0.15F, false);
		Torso.setTextureOffset(40, 45).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.setTextureOffset(46, 66).addBox(-9.0F, -0.2F, -1.775F, 18.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.setTextureOffset(17, 78).addBox(1.825F, 0.275F, -2.125F, 1.0F, 2.0F, 0.0F, 0.0F, false);
		Torso.setTextureOffset(21, 78).addBox(-0.175F, -0.125F, 2.5F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		Torso.setTextureOffset(29, 78).addBox(-3.45F, 0.0F, 2.55F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		Torso.setTextureOffset(66, 80).addBox(-4.0F, 5.4F, -2.0F, 8.0F, 6.0F, 4.0F, 0.15F, false);
		
		Torso_r1 = new ModelRenderer(this);
		Torso_r1.setRotationPoint(0.0F, 24.0F, 0.225F);
		Torso.addChild(Torso_r1);
		setRotationAngle(Torso_r1, 0.0F, 0.0F, -0.0524F);
		Torso_r1.setTextureOffset(29, 83).addBox(-1.025F, -19.025F, 2.425F, 4.0F, 4.0F, 0.0F, 0.0F, false);
		Torso_r1.setTextureOffset(34, 83).addBox(-2.35F, -18.55F, 2.575F, 7.0F, 4.0F, 0.0F, 0.0F, false);
		
		Torso_r2 = new ModelRenderer(this);
		Torso_r2.setRotationPoint(10.1F, 2.125F, -0.85F);
		Torso.addChild(Torso_r2);
		setRotationAngle(Torso_r2, 0.0F, -1.5708F, 0.0F);
		Torso_r2.setTextureOffset(0, 78).addBox(-1.05F, -2.0F, 1.525F, 3.0F, 4.0F, 0.0F, 0.0F, false);
		
		Torso_r3 = new ModelRenderer(this);
		Torso_r3.setRotationPoint(-4.2669F, 11.6287F, 0.1F);
		Torso.addChild(Torso_r3);
		setRotationAngle(Torso_r3, -1.9111F, -1.5708F, -3.1416F);
		Torso_r3.setTextureOffset(55, 8).addBox(-2.125F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, 0.0F, true);
		
		Torso_r4 = new ModelRenderer(this);
		Torso_r4.setRotationPoint(-1.1919F, -0.0713F, -0.2F);
		Torso.addChild(Torso_r4);
		setRotationAngle(Torso_r4, -1.9111F, -1.5708F, -3.1416F);
		Torso_r4.setTextureOffset(50, 8).addBox(-2.125F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, 0.0F, true);
		
		Torso_r5 = new ModelRenderer(this);
		Torso_r5.setRotationPoint(1.1919F, -0.0713F, -0.2F);
		Torso.addChild(Torso_r5);
		setRotationAngle(Torso_r5, -1.9111F, 1.5708F, 3.1416F);
		Torso_r5.setTextureOffset(50, 8).addBox(0.125F, 0.0F, -0.5F, 2.0F, 0.0F, 1.0F, 0.0F, false);
		
		Torso_r6 = new ModelRenderer(this);
		Torso_r6.setRotationPoint(4.2669F, 11.6287F, 0.1F);
		Torso.addChild(Torso_r6);
		setRotationAngle(Torso_r6, -1.9111F, 1.5708F, 3.1416F);
		Torso_r6.setTextureOffset(55, 8).addBox(-1.875F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, 0.0F, false);
		
		Torso_r7 = new ModelRenderer(this);
		Torso_r7.setRotationPoint(-0.375F, 11.6287F, 2.2669F);
		Torso.addChild(Torso_r7);
		setRotationAngle(Torso_r7, 1.2305F, 0.0F, 0.0F);
		Torso_r7.setTextureOffset(50, 8).addBox(-3.6F, 0.0F, -0.825F, 8.0F, 0.0F, 1.0F, 0.0F, false);
		
		Torso_r8 = new ModelRenderer(this);
		Torso_r8.setRotationPoint(-0.375F, 11.6287F, -2.2669F);
		Torso.addChild(Torso_r8);
		setRotationAngle(Torso_r8, -1.2305F, 0.0F, 0.0F);
		Torso_r8.setTextureOffset(54, 8).addBox(-3.675F, 0.0F, -0.5F, 8.0F, 0.0F, 1.0F, 0.0F, false);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(0, 32).addBox(-3.2F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.1F, false);
		RightArm.setTextureOffset(74, 53).addBox(-3.25F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		RightArm.setTextureOffset(11, 67).addBox(-3.2F, 2.325F, -2.0F, 4.0F, 1.0F, 4.0F, 0.3F, false);
		RightArm.setTextureOffset(11, 67).addBox(-3.2F, 0.5F, -2.0F, 4.0F, 1.0F, 4.0F, 0.3F, false);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(24, 16).addBox(-0.8F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.1F, false);
		LeftArm.setTextureOffset(48, 25).addBox(-0.75F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.2F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso_r9 = new ModelRenderer(this);
		Torso_r9.setRotationPoint(-1.45F, -7.5F, 4.275F);
		bipedHead.addChild(Torso_r9);
		setRotationAngle(Torso_r9, 0.3622F, 0.0F, 0.0F);
		Torso_r9.setTextureOffset(76, 3).addBox(-2.0F, -1.05F, 0.0F, 7.0F, 10.0F, 0.0F, 0.0F, false);
		
		Torso_r10 = new ModelRenderer(this);
		Torso_r10.setRotationPoint(7.5F, -4.9884F, 4.3425F);
		bipedHead.addChild(Torso_r10);
		setRotationAngle(Torso_r10, 0.1265F, 1.5752F, 0.0F);
		Torso_r10.setTextureOffset(74, 21).addBox(-0.25F, -4.025F, -3.15F, 8.0F, 4.0F, 0.0F, 0.0F, true);
		
		Torso_r11 = new ModelRenderer(this);
		Torso_r11.setRotationPoint(-7.5F, -4.9884F, 4.3425F);
		bipedHead.addChild(Torso_r11);
		setRotationAngle(Torso_r11, 0.1265F, -1.5752F, 0.0F);
		Torso_r11.setTextureOffset(74, 21).addBox(-7.75F, -4.025F, -3.15F, 8.0F, 4.0F, 0.0F, 0.0F, false);
		
		Torso_r12 = new ModelRenderer(this);
		Torso_r12.setRotationPoint(-1.45F, -7.5F, 4.275F);
		bipedHead.addChild(Torso_r12);
		setRotationAngle(Torso_r12, 0.1265F, 0.0F, 0.0F);
		Torso_r12.setTextureOffset(74, 13).addBox(-2.525F, -2.0F, -0.25F, 8.0F, 9.0F, 0.0F, 0.0F, false);
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(48, 0).addBox(-4.0F, -10.275F, -4.025F, 8.0F, 2.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(48, 5).addBox(-4.0F, -10.275F, -4.075F, 8.0F, 2.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(21, 0).addBox(-1.8906F, -8.869F, -6.625F, 4.0F, 0.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(82, 36).addBox(-2.0906F, -8.869F, -6.625F, 1.0F, 0.0F, 3.0F, 0.0F, false);
		Head.setTextureOffset(48, 0).addBox(-4.0F, -10.275F, 4.05F, 8.0F, 2.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(48, 53).addBox(-4.0F, -10.25F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(48, 53).addBox(4.025F, -10.275F, -4.0F, 0.0F, 2.0F, 8.0F, 0.0F, false);
		
		Head_r1 = new ModelRenderer(this);
		Head_r1.setRotationPoint(0.0F, -9.8F, -0.5F);
		Head.addChild(Head_r1);
		setRotationAngle(Head_r1, -0.0785F, 0.0F, 0.0F);
		Head_r1.setTextureOffset(44, 83).addBox(-3.175F, -4.7F, -1.575F, 6.0F, 3.0F, 4.0F, 0.2F, false);
		Head_r1.setTextureOffset(52, 42).addBox(-3.175F, -4.725F, -1.575F, 6.0F, 4.0F, 4.0F, 0.0F, false);
		
		Head_r2 = new ModelRenderer(this);
		Head_r2.setRotationPoint(0.0F, -9.8F, -0.5F);
		Head.addChild(Head_r2);
		setRotationAngle(Head_r2, -0.1396F, 0.0F, 0.0F);
		Head_r2.setTextureOffset(0, 53).addBox(-4.025F, -1.9F, -4.2F, 8.0F, 2.0F, 9.0F, 0.2F, false);
		Head_r2.setTextureOffset(56, 31).addBox(-4.0F, -1.9F, -4.2F, 8.0F, 2.0F, 9.0F, 0.0F, false);
		
		Head_r3 = new ModelRenderer(this);
		Head_r3.setRotationPoint(3.045F, -8.5715F, -4.125F);
		Head.addChild(Head_r3);
		setRotationAngle(Head_r3, 0.0F, 0.0F, 0.3011F);
		Head_r3.setTextureOffset(38, 17).addBox(-1.0F, 0.0F, -2.5F, 2.0F, 0.0F, 5.0F, 0.0F, true);
		
		Head_r4 = new ModelRenderer(this);
		Head_r4.setRotationPoint(-3.0453F, -8.5725F, -4.125F);
		Head.addChild(Head_r4);
		setRotationAngle(Head_r4, 0.0F, 0.0F, -0.3011F);
		Head_r4.setTextureOffset(38, 17).addBox(-1.0F, 0.0F, -2.5F, 2.0F, 0.0F, 5.0F, 0.0F, false);
		
		Head_r5 = new ModelRenderer(this);
		Head_r5.setRotationPoint(0.1094F, -8.869F, -5.125F);
		Head.addChild(Head_r5);
		setRotationAngle(Head_r5, 0.0F, 0.0F, -1.5315F);
		Head_r5.setTextureOffset(76, 0).addBox(-2.45F, 0.0F, -2.7F, 4.0F, 0.0F, 3.0F, 0.0F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}