package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class DreamerPajamasModel extends BipedModel<LivingEntity>
{
	//if making a model from Blockbench, do not include the variables that start with biped(as per Doro and Riotmode's blockbench exports so far). Instead replace those use cases with the public BipedModel variables(such as "rightLeg") and delete the now unused biped variables
	//TODO figure out if these variables can be rebased on to the model variables
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;
	private final ModelRenderer Torso;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer Headwear;
	
	public DreamerPajamasModel()
	{
		super(1); //Does not generate with BlockBench, done manually
		texWidth = 64;
		texHeight = 64;
		
		rightLeg = new ModelRenderer(this);
		rightLeg.setPos(0.0F, 12.0F, 0.0F);
		
		
		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(0.05F, 0.0F, 0.0F); //switch to 0.0F for all if BlockBench changes it, ideally the model should be fixed so it doesnt have to be tweaked to 0.05 in other cases
		rightLeg.addChild(RightLeg);
		RightLeg.texOffs(16, 32).addBox(-2.225F, 0.75F, -2.0F, 4.0F, 11.0F, 4.0F, 0.27F, true);
		RightLeg.texOffs(32, 0).addBox(-2.125F, 0.175F, -2.025F, 4.0F, 11.0F, 4.0F, 0.075F, true);
		RightLeg.texOffs(48, 10).addBox(-2.2F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, 0.2F, false);
		
		leftLeg = new ModelRenderer(this);
		leftLeg.setPos(0.0F, 12.0F, 0.0F);
		
		
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(-0.05F, 0.0F, 0.0F); //switch to 0.0F for all if BlockBench changes it
		leftLeg.addChild(LeftLeg);
		LeftLeg.texOffs(32, 0).addBox(-1.875F, 0.175F, -2.025F, 4.0F, 11.0F, 4.0F, 0.075F, false);
		LeftLeg.texOffs(16, 32).addBox(-1.775F, 0.75F, -2.0F, 4.0F, 11.0F, 4.0F, 0.27F, false);
		LeftLeg.texOffs(48, 10).addBox(-1.8F, 10.2F, -2.0F, 4.0F, 2.0F, 4.0F, 0.2F, false);
		
		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		
		
		Torso = new ModelRenderer(this);
		Torso.setPos(0.0F, 0.0F, 0.0F);
		body.addChild(Torso);
		Torso.texOffs(0, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.1F, false);
		Torso.texOffs(40, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.2F, false);
		Torso.texOffs(40, 49).addBox(-4.0F, 11.85F, -2.0F, 8.0F, 2.0F, 4.0F, 0.21F, false);
		Torso.texOffs(40, 58).addBox(-4.0F, 12.1F, -2.0F, 8.0F, 2.0F, 4.0F, 0.19F, false);
		
		rightArm = new ModelRenderer(this);
		rightArm.setPos(-5.0F, 2.0F, 0.0F);
		
		
		RightArm = new ModelRenderer(this);
		RightArm.setPos(0.0F, 0.0F, 0.0F);
		rightArm.addChild(RightArm);
		RightArm.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, false);
		RightArm.texOffs(0, 32).addBox(-3.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.13F, false);
		RightArm.texOffs(0, 0).addBox(-3.3F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		leftArm = new ModelRenderer(this);
		leftArm.setPos(5.0F, 2.0F, 0.0F);
		
		
		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(0.0F, 0.0F, 0.0F);
		leftArm.addChild(LeftArm);
		LeftArm.texOffs(0, 0).addBox(-0.7F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.26F, true);
		LeftArm.texOffs(24, 16).addBox(-0.9F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.13F, false);
		LeftArm.texOffs(16, 0).addBox(-0.7F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.2F, false);
		
		head = new ModelRenderer(this);
		head.setPos(0.0F, 24.0F, 0.0F);
		
		
		Headwear = new ModelRenderer(this);
		Headwear.setPos(0.0F, 0.0F, 0.0F);
		head.addChild(Headwear);
		Headwear.texOffs(0, 51).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 5.0F, 8.0F, 0.17F, false); //by default BlockBench may try to make the second value of .addBox much higher than -9.0F, make sure to tweak this value to get it to the right height
	}
}
