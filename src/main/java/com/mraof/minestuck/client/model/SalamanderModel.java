package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SalamanderModel<T extends ConsortEntity> extends SegmentedModel<T>
{
	ModelRenderer body;
	ModelRenderer rightLeg;
	ModelRenderer leftLeg;
	ModelRenderer head;
	ModelRenderer upperTail;
	ModelRenderer lowerTail;
	ModelRenderer upperJaw;
	ModelRenderer lowerJaw;
	ModelRenderer hood;


	public SalamanderModel()
	{
		this(false);
	}

	public SalamanderModel(boolean hasArms)
	{
		/*ModelRenderer(this, textureOffsetX, textureOffsetY)
		 * When figuring out the texture offset, the width will be 2 * sizeZ + 2 * sizeX, and the height will be sizeY + sizeZ
		 * addBox(offsetX,offsetY,offsetZ,sizeX,sizeY,sizeZ)
		 * setRotationPoint(x,y,z) 
		 * x is width, y is height (lower numbers are higher), z is length
		 * 16 is 1 meter (width of one block)
		 * bottom is 24
		 */
		texWidth = 64;
		texHeight = 32;
		
		this.body = new ModelRenderer(this, 10, 18);
		this.body.setPos(0.0F, 12.0F, 0.0F);
		this.body.addBox(-3.0F, 0.0F, -3.0F, 6, 8, 6, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 18);
		this.rightLeg.setPos(-1.0F, 20.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 25);
		this.leftLeg.setPos(3.0F, 20.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 4, 3, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setPos(0.0F, 12.0F, 0.0F);
		this.head.addBox(-3.0F, -4.0F, -3.5F, 6, 4, 7, 0.0F);
		this.upperJaw = new ModelRenderer(this, 0, 11);
		this.upperJaw.setPos(0.0F, 11.0F, 0.0F);
		this.upperJaw.addBox(-2.0F, -2.0F, -6.0F, 4, 2, 3, 0.0F);
		this.lowerJaw = new ModelRenderer(this, 14, 11);
		this.lowerJaw.setPos(0.0F, 13.0F, 0.0F);
		this.lowerJaw.addBox(-2.0F, -2.0F, -6.0F, 4, 1, 3, 0.0F);
		this.hood = new ModelRenderer(this, 26, 0);
		this.hood.setPos(0.0F, 12.0F, 0.0F);
		this.hood.addBox(-4.0F, -5.0F, -4.0F, 8, 5, 8, 0.0F);
		this.upperTail = new ModelRenderer(this, 34, 18);
		this.upperTail.setPos(0.0F, 18.0F, 3.0F);
		this.upperTail.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotation(upperTail, 0.22307169437408447F, 0.0F, 0.0F);
		this.lowerTail = new ModelRenderer(this, 34, 24);
		this.lowerTail.setPos(0.0F, 22.0F, 6.0F);
		this.lowerTail.addBox(-1.0F, 0.0F, -3.0F, 2, 2, 6, 0.0F);
		
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}

	@Override
	public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float angleY = netHeadYaw / (180F / (float)Math.PI);
		float angleX = headPitch / (180F / (float)Math.PI);
		head.yRot = hood.yRot = upperJaw.yRot = lowerJaw.yRot = angleY;
		head.xRot = hood.xRot = upperJaw.xRot = lowerJaw.xRot = angleX;
		this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.head, this.upperTail, this.lowerTail, this.upperJaw, this.lowerJaw, this.hood);
	}

}
