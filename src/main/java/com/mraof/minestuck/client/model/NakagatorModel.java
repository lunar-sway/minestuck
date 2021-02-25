package com.mraof.minestuck.client.model;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class NakagatorModel<T extends ConsortEntity> extends SegmentedModel<T>
{
	public boolean hasArms;
	ModelRenderer body;
	ModelRenderer rightLeg;
	ModelRenderer leftLeg;
	ModelRenderer head;
	ModelRenderer upperTail;
	ModelRenderer lowerTail;
	ModelRenderer upperJaw;
	ModelRenderer lowerJaw;

    public NakagatorModel()
    {
    	this(false);
    }
    
    public NakagatorModel(boolean hasArms)
    {
    	textureWidth = 64;
        textureHeight = 32;
	
		this.body = new ModelRenderer(this, 10, 17);
		this.body.setRotationPoint(0.0F, 10.0F, 0.0F);
		this.body.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.setRotationPoint(-1.0F, 19.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 24);
		this.leftLeg.setRotationPoint(3.0F, 19.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3, 0.0F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 8.3F, 0.0F);
		this.head.addBox(-3.0F, -1.3F, -3.0F, 6, 3, 7, 0.0F);
		this.upperJaw = new ModelRenderer(this, 34, 0);
		this.upperJaw.setRotationPoint(0.0F, 8.1F, 0.0F);
		this.upperJaw.addBox(-2.0F, 0.0F, -12.0F, 4, 1, 11, 0.0F);
		this.lowerJaw = new ModelRenderer(this, 34, 12);
		this.lowerJaw.setRotationPoint(0.0F, 9.0F, -0.0F);
		this.lowerJaw.addBox(-2.0F, 0.0F, -10.0F, 4, 1, 11, 0.0F);
		this.lowerTail = new ModelRenderer(this, 26, 0);
		this.lowerTail.setRotationPoint(0.0F, 22.0F, 4.0F);
		this.lowerTail.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 7, 0.0F);
		this.upperTail = new ModelRenderer(this, 26, 9);
		this.upperTail.setRotationPoint(0.0F, 18.0F, 3.0F);
		this.upperTail.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotation(upperTail, 0.22307169437408447F, 0.0F, 0.0F);
        
	}

    private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}

	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(this.body, this.rightLeg, this.leftLeg, this.head, this.upperTail, this.lowerTail, this.upperJaw, this.lowerJaw);
	}
}
