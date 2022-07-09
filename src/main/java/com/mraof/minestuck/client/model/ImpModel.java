// Date: 31/12/2012 15:51:35
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX
package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.entity.underling.ImpEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ImpModel<T extends ImpEntity> extends SegmentedModel<T>
{
	//fields
	ModelRenderer Head;
	ModelRenderer Body;
	ModelRenderer Armright;
	ModelRenderer Armleft;
	ModelRenderer Legleft;
	ModelRenderer Legright;

	public ImpModel()
	{
		texWidth = 32;
		texHeight = 32;

		Head = new ModelRenderer(this, 0, 0);
		Head.addBox(-3F, -3F, -5F, 5, 5, 5);
		Head.setPos(0F, 15F, 0F);
		Head.mirror = true;
		setRotation(Head, 0F, 0F, 0F); 
		Body = new ModelRenderer(this, 0, 10);
		Body.addBox(-3F, -4F, -2F, 5, 6, 4);
		Body.setPos(0F, 19F, 0F);
		Body.mirror = true;
		setRotation(Body, 0F, 0F, 0F);
		Armright = new ModelRenderer(this, 0, 20);
		Armright.addBox(-1F, 0F, -1F, 1, 5, 1);
		Armright.setPos(-3F, 16F, 0F);
		Armright.mirror = true;
		setRotation(Armright, 0F, 0.0371786F, 0.0371786F); 
		Armleft = new ModelRenderer(this, 0, 20);
		Armleft.addBox(0F, 0F, -1F, 1, 5, 1);
		Armleft.setPos(2F, 16F, 0F);
		Armleft.mirror = true;
		setRotation(Armleft, 0F, 0F, 0F);
		Legleft = new ModelRenderer(this, 4, 20);
		Legleft.addBox(-1F, 0F, 0F, 1, 3, 1);
		Legleft.setPos(-1F, 21F, 0F);
		Legleft.mirror = true;
		setRotation(Legleft, 0F, 0F, 0F);
		Legright = new ModelRenderer(this, 4, 20);
		Legright.addBox(0F, 0F, 0F, 1, 3, 1);
		Legright.setPos(0F, 21F, 0F);
		Legright.mirror = true;
		setRotation(Legright, 0F, 0F, 0F);
	}

	public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		matrixStackIn.scale(1.5f, 1.5f, 1.5f);
		matrixStackIn.translate(0F, -0.5F, 0F);
		super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.Legleft.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.Legright.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.Head, this.Body, this.Armright, this.Armleft, this.Legleft, this.Legright);
	}
}