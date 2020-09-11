package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.underling.GiclopsEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class GiclopsModel<T extends GiclopsEntity> extends SegmentedModel<T>
{

	private ModelRenderer head;
	private ModelRenderer leftLeg;
	private ModelRenderer rightLeg;
	private ModelRenderer body;
	private ModelRenderer leftArm;
	private ModelRenderer rightArm;
	private ModelRenderer tail;
	float scaling = 2.0F;

	public GiclopsModel()
	{
		float offsetY = -96 + 24 / scaling;
		this.textureWidth = 256;
		this.textureHeight = 256;
		//setTextureOffset("null.Spike", 0, 0);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-21F, -26F, -25F, 42, 26, 50);
		head.addBox("Spike", -2F, -34F, -6F, 4, 8, 4, 0.0f, 0, 0);
		head.addBox("Spike", -2, -34, -16, 4, 8, 4, 0.0f, 0, 0);
		head.setRotationPoint(0F, 26F + offsetY, -14F);
		body = new ModelRenderer(this, 0, 76);
		body.addBox(-32F, 0F, -20F, 64, 64, 40);
		body.setRotationPoint(0F, 16F + offsetY, -8F);
		leftArm = new ModelRenderer(this, 184, 0);
		leftArm.addBox(-8F, 0F, -4F, 8, 52, 8);
		leftArm.setRotationPoint(-32F, 28F + offsetY, -8F);
		rightArm = new ModelRenderer(this, 184, 0);
		rightArm.addBox(0F, 0F, -4F, 8, 52, 8);
		rightArm.setRotationPoint(32F, 28F + offsetY, -8F);
		rightArm.mirror = true;
		leftLeg = new ModelRenderer(this, 120, 180);
		leftLeg.addBox(-12F, 0F, -12F, 24, 16, 24);
		leftLeg.setRotationPoint(-20, 80F + offsetY, -8F);
		rightLeg = new ModelRenderer(this, 120, 180);
		rightLeg.addBox(-12F, 0F, -12F, 24, 16, 24);
		rightLeg.setRotationPoint(20, 80F + offsetY, -8F);
		rightLeg.mirror = true;
		tail = new ModelRenderer(this, 0, 180);
		tail.addBox(-20F, -6F, -10F, 40, 44, 20);
		tail.setRotationPoint(0F, 56F + offsetY, 0F);
		tail.rotateAngleX = 30F / (180F / (float)Math.PI);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.head.rotateAngleX = (headPitch + 20) / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;
		this.leftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount * 0.5F;
		this.rightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.2F * limbSwingAmount * 0.5F;
	}

	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	/*
	 	public void render(T entity, float par2, float par3, float par4, float par5, float par6, float par7)
	 	{
	 	GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, par7 * scaling, 0.0F);
		this.body.render(par7 * scaling);
		this.rightArm.render(par7 * scaling);
		this.leftArm.render(par7 * scaling);
		this.rightLeg.render(par7 * scaling);
		this.leftLeg.render(par7 * scaling);
		this.head.render(par7 * scaling);
		this.tail.render(par7 * scaling);
		GL11.glPopMatrix();
	 	}
	 */

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg, this.tail);
	}
}
