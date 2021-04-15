package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class FlameDecalGasMaskModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer Head;
	private final ModelRenderer side_right_inner_r1;
	private final ModelRenderer side_left_inner_r1;
	private final ModelRenderer filter;
	private final ModelRenderer top_r1;
	private final ModelRenderer left_r1;
	
	public FlameDecalGasMaskModel()
	{
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		Head.setTextureOffset(18, 9).addBox(-4.0F, -6.0F, -4.1F, 8.0F, 6.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(28, 39).addBox(-5.95F, -6.325F, -4.2F, 12.0F, 4.0F, 0.0F, 0.0F, false);
		Head.setTextureOffset(0, 44).addBox(4.35F, -5.0F, -4.05F, 0.0F, 4.0F, 5.0F, 0.0F, false);
		Head.setTextureOffset(0, 44).addBox(-4.3F, -5.0F, -4.05F, 0.0F, 4.0F, 5.0F, 0.0F, false);
		
		side_right_inner_r1 = new ModelRenderer(this);
		side_right_inner_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
		Head.addChild(side_right_inner_r1);
		setRotationAngle(side_right_inner_r1, 0.0F, -0.0209F, 0.0F);
		side_right_inner_r1.setTextureOffset(27, 20).addBox(-4.1F, -30.0F, -4.05F, 0.0F, 6.0F, 5.0F, 0.0F, false);
		
		side_left_inner_r1 = new ModelRenderer(this);
		side_left_inner_r1.setRotationPoint(0.0F, 24.0F, 0.0F);
		Head.addChild(side_left_inner_r1);
		setRotationAngle(side_left_inner_r1, 0.0F, 0.0209F, 0.0F);
		side_left_inner_r1.setTextureOffset(27, 20).addBox(4.1F, -30.0F, -4.05F, 0.0F, 6.0F, 5.0F, 0.0F, false);
		
		filter = new ModelRenderer(this);
		filter.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(filter);
		filter.setTextureOffset(21, 20).addBox(-0.95F, -2.325F, -4.425F, 2.0F, 2.0F, 0.0F, 0.0F, false);
		
		top_r1 = new ModelRenderer(this);
		top_r1.setRotationPoint(-2.0023F, -2.3375F, -3.5754F);
		filter.addChild(top_r1);
		setRotationAngle(top_r1, 0.0F, 1.5708F, 0.0F);
		top_r1.setTextureOffset(19, 16).addBox(0.0F, -0.9875F, 1.025F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		top_r1.setTextureOffset(19, 16).addBox(0.0F, 0.0375F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		
		left_r1 = new ModelRenderer(this);
		left_r1.setRotationPoint(1.9977F, -1.3125F, -3.5764F);
		filter.addChild(left_r1);
		setRotationAngle(left_r1, 0.0F, 1.5708F, 0.0F);
		left_r1.setTextureOffset(19, 16).addBox(0.0F, -0.9875F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}