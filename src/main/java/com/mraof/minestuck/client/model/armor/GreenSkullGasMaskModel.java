package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class GreenSkullGasMaskModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer Head;
	private final ModelRenderer left_filter_r1;
	private final ModelRenderer right_filter_r1;
	
	public GreenSkullGasMaskModel()
	{
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(0, 49).addBox(-4.1F, -8.1F, -4.2F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(1, 42).addBox(-4.1F, -8.11F, -4.18F, 8.0F, 0.0F, 6.0F, 0.0F, false);
		Head.setTextureOffset(37, 36).addBox(-1.6F, -3.1F, -5.1F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Head.setTextureOffset(15, 21).addBox(-4.6F, -8.1F, -4.19F, 9.0F, 7.0F, 6.0F, 0.0F, false);
		
		left_filter_r1 = new ModelRenderer(this);
		left_filter_r1.setRotationPoint(0.0F, 25.0F, 0.0F);
		Head.addChild(left_filter_r1);
		setRotationAngle(left_filter_r1, 0.0F, 0.0F, 0.2618F);
		left_filter_r1.setTextureOffset(23, 58).addBox(-7.25F, -26.25F, -4.4F, 5.0F, 2.0F, 0.0F, 0.0F, true);
		
		right_filter_r1 = new ModelRenderer(this);
		right_filter_r1.setRotationPoint(0.0F, 25.0F, 0.0F);
		Head.addChild(right_filter_r1);
		setRotationAngle(right_filter_r1, 0.0F, 0.0F, -0.2618F);
		right_filter_r1.setTextureOffset(23, 58).addBox(2.05F, -26.25F, -4.4F, 5.0F, 2.0F, 0.0F, 0.0F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}