package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.entity.EntityFrog;

/**
 * ModelFrog - Cibernet
 * Created using Tabula 7.0.0
 */
public class ModelFrog extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer left_eye;
    public ModelRenderer right_foot;
    public ModelRenderer left_top_leg;
    public ModelRenderer body;
    public ModelRenderer right_arm;
    public ModelRenderer left_arm;
    public ModelRenderer right_eye;
    public ModelRenderer left_bottom_leg;
    public ModelRenderer right_top_leg;
    public ModelRenderer right_bottom_leg;
    public ModelRenderer left_foot;
    private float jumpRotation;

    public ModelFrog() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.right_foot = new ModelRenderer(this, 0, 26);
        this.right_foot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_foot.addBox(-0.9F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.left_bottom_leg = new ModelRenderer(this, 28, 24);
        this.left_bottom_leg.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_bottom_leg.addBox(-1.1F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(left_bottom_leg, 1.0471975511965976F, 0.0F, 0.0F);
        this.right_bottom_leg = new ModelRenderer(this, 44, 24);
        this.right_bottom_leg.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_bottom_leg.addBox(-0.9F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(right_bottom_leg, 1.0471975511965976F, 0.0F, 0.003490658503988659F);
        this.left_eye = new ModelRenderer(this, 52, 5);
        this.left_eye.setRotationPoint(0.0F, 17.0F, 1.0F);
        this.left_eye.addBox(1.5F, -5.0F, -4.5F, 3, 2, 3, 0.0F);
        this.left_foot = new ModelRenderer(this, 14, 26);
        this.left_foot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_foot.addBox(-1.1F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.right_eye = new ModelRenderer(this, 52, 0);
        this.right_eye.setRotationPoint(0.0F, 17.0F, 1.0F);
        this.right_eye.addBox(-4.5F, -5.0F, -4.5F, 3, 2, 3, 0.0F);
        this.right_top_leg = new ModelRenderer(this, 16, 16);
        this.right_top_leg.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_top_leg.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(right_top_leg, 0.27314402793711257F, 0.0F, 0.0F);
        this.left_arm = new ModelRenderer(this, 8, 15);
        this.left_arm.setRotationPoint(3.0F, 17.0F, 1.0F);
        this.left_arm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(left_arm, -0.19198621771937624F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 28, 0);
        this.head.setRotationPoint(0.0F, 17.0F, 1.0F);
        this.head.addBox(-3.5F, -4.0F, -5.0F, 7, 4, 5, 0.0F);
        this.left_top_leg = new ModelRenderer(this, 30, 16);
        this.left_top_leg.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_top_leg.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(left_top_leg, 0.27314402793711257F, 0.0F, 0.0F);
        this.right_arm = new ModelRenderer(this, 0, 15);
        this.right_arm.setRotationPoint(-3.0F, 17.0F, 1.0F);
        this.right_arm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(right_arm, -0.19198621771937624F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.body.addBox(-3.0F, -2.0F, -8.0F, 6, 5, 8, 0.0F);
        this.setRotateAngle(body, -0.3490658503988659F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.right_foot.render(f5);
        this.left_bottom_leg.render(f5);
        this.right_bottom_leg.render(f5);
        this.left_eye.render(f5);
        this.left_foot.render(f5);
        this.right_eye.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.right_top_leg.offsetX, this.right_top_leg.offsetY, this.right_top_leg.offsetZ);
        GlStateManager.translate(this.right_top_leg.rotationPointX * f5, this.right_top_leg.rotationPointY * f5, this.right_top_leg.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.right_top_leg.offsetX, -this.right_top_leg.offsetY, -this.right_top_leg.offsetZ);
        GlStateManager.translate(-this.right_top_leg.rotationPointX * f5, -this.right_top_leg.rotationPointY * f5, -this.right_top_leg.rotationPointZ * f5);
        this.right_top_leg.render(f5);
        GlStateManager.popMatrix();
        this.left_arm.render(f5);
        this.head.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.left_top_leg.offsetX, this.left_top_leg.offsetY, this.left_top_leg.offsetZ);
        GlStateManager.translate(this.left_top_leg.rotationPointX * f5, this.left_top_leg.rotationPointY * f5, this.left_top_leg.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.left_top_leg.offsetX, -this.left_top_leg.offsetY, -this.left_top_leg.offsetZ);
        GlStateManager.translate(-this.left_top_leg.rotationPointX * f5, -this.left_top_leg.rotationPointY * f5, -this.left_top_leg.rotationPointZ * f5);
        this.left_top_leg.render(f5);
        GlStateManager.popMatrix();
        this.right_arm.render(f5);
        this.body.render(f5);
    }

   
    
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
        
    }
    @Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
    	
    	float f = ageInTicks - (float)entityIn.ticksExisted;
    	EntityFrog frog = (EntityFrog) entityIn;
    	
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.left_eye.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.left_eye.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.right_eye.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
		this.right_eye.rotateAngleX = headPitch / (180F / (float)Math.PI);
		this.jumpRotation = MathHelper.sin(frog.setJumpCompletion(f) * (float)Math.PI);
		this.left_top_leg.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F + 0.27314402793711257F;
		this.right_top_leg.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F + 0.27314402793711257F;
		this.left_bottom_leg.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F + 1.0471975511965976F;
		this.right_bottom_leg.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F + 1.0471975511965976F;
		this.left_foot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
		this.right_foot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
		this.left_arm.rotateAngleX = this.jumpRotation * -50.0F * 0.017453292F - 0.19198621771937624F;
		this.right_arm.rotateAngleX = this.jumpRotation * -50.0F * 0.017453292F - 0.19198621771937624F;
		
		
	}
    
    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
        this.jumpRotation = MathHelper.sin(((EntityFrog)entitylivingbaseIn).setJumpCompletion(partialTickTime) * (float)Math.PI);
    }
    
    //MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
    /*
     * top: 	MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + 0.27314402793711257F
     * bottom:	MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount + 1.0471975511965976F
     */
}
