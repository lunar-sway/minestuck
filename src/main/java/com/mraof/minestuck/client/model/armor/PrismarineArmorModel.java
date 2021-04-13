package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class PrismarineArmorModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer Torso_r1;
	private final ModelRenderer Torso_r2;
	private final ModelRenderer Torso_r3;
	private final ModelRenderer RightArm;
	private final ModelRenderer RightArm_r1;
	private final ModelRenderer LeftArm;
	private final ModelRenderer LeftArm_r1;
	private final ModelRenderer Head;
	private final ModelRenderer RightArm_r2;
	private final ModelRenderer RightArm_r3;
	private final ModelRenderer RightArm_r4;
	
	public PrismarineArmorModel()
	{
		super(1);
		textureWidth = 104;
		textureHeight = 104;
		
		bipedRightLeg = new ModelRenderer(this);
		bipedRightLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setRotationPoint(-1.9F, 0.0F, 0.0F);
		bipedRightLeg.addChild(RightLeg);
		RightLeg.setTextureOffset(36, 36).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		RightLeg.setTextureOffset(3, 78).addBox(-2.125F, 0.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.0F, true);
		RightLeg.setTextureOffset(49, 0).addBox(-2.525F, 1.225F, -1.675F, 2.0F, 3.0F, 4.0F, 0.0F, true);
		RightLeg.setTextureOffset(63, 61).addBox(-2.175F, 0.75F, -1.325F, 2.0F, 3.0F, 3.0F, 0.0F, true);
		RightLeg.setTextureOffset(20, 41).addBox(-2.525F, 1.225F, -2.275F, 2.0F, 3.0F, 4.0F, 0.0F, true);
		RightLeg.setTextureOffset(1, 66).addBox(-2.75F, 0.0F, -2.475F, 3.0F, 3.0F, 5.0F, 0.0F, true);
		RightLeg.setTextureOffset(59, 86).addBox(-2.1F, 10.05F, -2.175F, 4.0F, 2.0F, 4.0F, 0.0F, true);
		RightLeg.setTextureOffset(59, 86).addBox(-2.1F, 10.05F, -1.975F, 4.0F, 2.0F, 4.0F, 0.0F, true);
		
		bipedLeftLeg = new ModelRenderer(this);
		bipedLeftLeg.setRotationPoint(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setRotationPoint(1.9F, 0.0F, 0.0F);
		bipedLeftLeg.addChild(LeftLeg);
		LeftLeg.setTextureOffset(32, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftLeg.setTextureOffset(3, 78).addBox(-1.875F, 0.0F, -2.1F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftLeg.setTextureOffset(49, 0).addBox(0.525F, 1.225F, -1.675F, 2.0F, 3.0F, 4.0F, 0.0F, false);
		LeftLeg.setTextureOffset(63, 61).addBox(0.175F, 0.75F, -1.325F, 2.0F, 3.0F, 3.0F, 0.0F, false);
		LeftLeg.setTextureOffset(20, 41).addBox(0.525F, 1.225F, -2.275F, 2.0F, 3.0F, 4.0F, 0.0F, false);
		LeftLeg.setTextureOffset(1, 66).addBox(-0.25F, 0.0F, -2.475F, 3.0F, 3.0F, 5.0F, 0.0F, false);
		LeftLeg.setTextureOffset(59, 86).addBox(-1.9F, 10.05F, -2.175F, 4.0F, 2.0F, 4.0F, 0.0F, false);
		LeftLeg.setTextureOffset(59, 86).addBox(-1.9F, 10.05F, -1.925F, 4.0F, 2.0F, 4.0F, 0.0F, false);
		
		//bipedBody = new ModelRenderer(this);
		bipedBody = new ModelRenderer(this);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedBody.addChild(Torso);
		Torso.setTextureOffset(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(0, 56).addBox(-4.525F, 0.925F, -2.4F, 9.0F, 4.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(14, 49).addBox(-4.525F, 0.925F, -1.925F, 9.0F, 11.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(39, 57).addBox(-4.525F, 4.075F, -2.25F, 9.0F, 3.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(49, 67).addBox(-4.525F, 5.05F, -2.125F, 9.0F, 3.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(39, 19).addBox(-3.95F, 8.275F, -2.1F, 8.0F, 3.0F, 4.0F, 0.0F, false);
		Torso.setTextureOffset(27, 70).addBox(3.225F, 8.275F, -0.425F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		Torso.setTextureOffset(59, 40).addBox(-4.175F, 6.3F, -2.025F, 4.0F, 6.0F, 4.0F, 0.0F, false);
		
		Torso_r1 = new ModelRenderer(this);
		Torso_r1.setRotationPoint(-3.9F, 9.775F, 0.575F);
		Torso.addChild(Torso_r1);
		setRotationAngle(Torso_r1, 0.0F, 3.1416F, 0.0F);
		Torso_r1.setTextureOffset(27, 70).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		Torso_r2 = new ModelRenderer(this);
		Torso_r2.setRotationPoint(-1.075F, 9.775F, 1.85F);
		Torso.addChild(Torso_r2);
		setRotationAngle(Torso_r2, 0.0F, -1.5708F, 0.0F);
		Torso_r2.setTextureOffset(22, 70).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		Torso_r3 = new ModelRenderer(this);
		Torso_r3.setRotationPoint(0.925F, 9.775F, 1.85F);
		Torso.addChild(Torso_r3);
		setRotationAngle(Torso_r3, 0.0F, -1.5708F, 0.0F);
		Torso_r3.setTextureOffset(27, 70).addBox(-0.5F, -1.5F, -1.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		bipedRightArm = new ModelRenderer(this);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm.addChild(RightArm);
		RightArm.setTextureOffset(0, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		RightArm.setTextureOffset(57, 0).addBox(-3.228F, -2.281F, -2.541F, 4.0F, 4.0F, 5.0F, 0.0F, true);
		
		RightArm_r1 = new ModelRenderer(this);
		RightArm_r1.setRotationPoint(-3.703F, -2.356F, 2.034F);
		RightArm.addChild(RightArm_r1);
		setRotationAngle(RightArm_r1, 0.4451F, -0.2967F, 0.0F);
		RightArm_r1.setTextureOffset(57, 29).addBox(0.0F, -2.0F, -4.0F, 0.0F, 4.0F, 7.0F, 0.0F, true);
		
		bipedLeftArm = new ModelRenderer(this);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedLeftArm.addChild(LeftArm);
		LeftArm.setTextureOffset(24, 24).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftArm.setTextureOffset(57, 0).addBox(-0.772F, -2.281F, -2.541F, 4.0F, 4.0F, 5.0F, 0.0F, false);
		LeftArm.setTextureOffset(57, 12).addBox(-0.772F, 2.919F, -2.541F, 4.0F, 4.0F, 5.0F, 0.0F, false);
		LeftArm.setTextureOffset(27, 70).addBox(2.1F, 1.05F, -1.275F, 1.0F, 3.0F, 2.0F, 0.0F, false);
		
		LeftArm_r1 = new ModelRenderer(this);
		LeftArm_r1.setRotationPoint(1.228F, 8.194F, -0.041F);
		LeftArm.addChild(LeftArm_r1);
		setRotationAngle(LeftArm_r1, 3.1416F, 0.0F, 0.0F);
		LeftArm_r1.setTextureOffset(29, 85).addBox(-2.0F, -2.0F, -2.5F, 4.0F, 4.0F, 5.0F, 0.0F, false);
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(82, 92).addBox(-4.675F, -5.8F, -0.425F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(82, 92).addBox(3.75F, -5.8F, -0.425F, 1.0F, 2.0F, 2.0F, 0.0F, false);
		Head.setTextureOffset(65, 15).addBox(-4.475F, -8.075F, -4.425F, 9.0F, 8.0F, 8.0F, 0.0F, false);
		Head.setTextureOffset(75, 58).addBox(-1.0F, -9.0F, -3.025F, 2.0F, 8.0F, 8.0F, 0.0F, false);
		
		RightArm_r2 = new ModelRenderer(this);
		RightArm_r2.setRotationPoint(4.703F, -8.931F, 2.384F);
		Head.addChild(RightArm_r2);
		setRotationAngle(RightArm_r2, 0.096F, 0.624F, 0.0F);
		RightArm_r2.setTextureOffset(17, 83).addBox(0.0F, -3.0F, -4.0F, 0.0F, 15.0F, 7.0F, 0.0F, false);
		
		RightArm_r3 = new ModelRenderer(this);
		RightArm_r3.setRotationPoint(-4.703F, -8.931F, 2.384F);
		Head.addChild(RightArm_r3);
		setRotationAngle(RightArm_r3, 0.096F, -0.624F, 0.0F);
		RightArm_r3.setTextureOffset(17, 83).addBox(0.0F, -3.0F, -4.0F, 0.0F, 15.0F, 7.0F, 0.0F, true);
		
		RightArm_r4 = new ModelRenderer(this);
		RightArm_r4.setRotationPoint(0.022F, -9.956F, 5.459F);
		Head.addChild(RightArm_r4);
		setRotationAngle(RightArm_r4, 0.1745F, 0.0F, 0.0F);
		RightArm_r4.setTextureOffset(78, 29).addBox(0.0F, -3.0F, -4.0F, 0.0F, 15.0F, 7.0F, 0.0F, true);
		
		bipedHeadwear = new ModelRenderer(this);
		bipedHeadwear.setRotationPoint(0.0F, 24.0F, 0.0F);
		
	}
	
	/*@Override
	public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}
	
	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		bipedRightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedLeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedBody.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedRightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedLeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedHead.render(matrixStack, buffer, packedLight, packedOverlay);
		bipedHeadwear.render(matrixStack, buffer, packedLight, packedOverlay);
	}*/
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}