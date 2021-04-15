package com.mraof.minestuck.client.model.armor;// Made with Blockbench 3.8.3 by Doro

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class LaurelWreathModel extends BipedModel<LivingEntity>
{
	private final ModelRenderer Head;
	private final ModelRenderer wreath_inner;
	private final ModelRenderer wreath_outer;
	
	public LaurelWreathModel()
	{
		super(1);
		textureWidth = 64;
		textureHeight = 64;
		
		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		
		
		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(Head);
		
		
		wreath_inner = new ModelRenderer(this);
		wreath_inner.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(wreath_inner);
		wreath_inner.setTextureOffset(20, 56).addBox(-4.0F, -8.0F, -4.075F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		wreath_inner.setTextureOffset(4, 3).addBox(-4.05F, -8.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, false);
		wreath_inner.setTextureOffset(0, 56).addBox(-4.0F, -8.0F, 4.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		wreath_inner.setTextureOffset(4, 3).addBox(4.05F, -8.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, false);
		
		wreath_outer = new ModelRenderer(this);
		wreath_outer.setRotationPoint(0.0F, 24.0F, 0.0F);
		Head.addChild(wreath_outer);
		wreath_outer.setTextureOffset(39, 56).addBox(-4.0F, -32.0F, -4.275F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		wreath_outer.setTextureOffset(41, 25).addBox(-4.15F, -32.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, false);
		wreath_outer.setTextureOffset(48, 42).addBox(-4.0F, -32.0F, 4.25F, 8.0F, 8.0F, 0.0F, 0.0F, false);
		wreath_outer.setTextureOffset(41, 25).addBox(4.175F, -32.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, false);
	}
	
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}