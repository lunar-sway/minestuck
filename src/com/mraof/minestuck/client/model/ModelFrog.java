package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.entity.EntityFrog;

public class ModelFrog extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer left_eye;
    public ModelRenderer right_foot;
    public ModelRenderer left_back_leg_top;
    public ModelRenderer body;
    public ModelRenderer right_front_leg;
    public ModelRenderer left_front_leg;
    public ModelRenderer right_eye;
    public ModelRenderer left_back_leg_bottom;
    public ModelRenderer right_back_leg_top;
    public ModelRenderer right_back_leg_bottom;
    public ModelRenderer left_foot;

    public ModelFrog() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.left_eye = new ModelRenderer(this, 52, 5);
        this.left_eye.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.left_eye.addBox(1.5F, -4.0F, -2.5F, 3, 2, 3, 0.0F);
        this.right_front_leg = new ModelRenderer(this, 0, 15);
        this.right_front_leg.setRotationPoint(-3.0F, 17.0F, 1.0F);
        this.right_front_leg.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(right_front_leg, -0.19198621771937624F, 0.0F, 0.0F);
        this.left_back_leg_bottom = new ModelRenderer(this, 28, 24);
        this.left_back_leg_bottom.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_back_leg_bottom.addBox(-1.0F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(left_back_leg_bottom, 1.0471975511965976F, 0.0F, 0.0F);
        this.right_foot = new ModelRenderer(this, 0, 26);
        this.right_foot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_foot.addBox(-1.0F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.right_back_leg_bottom = new ModelRenderer(this, 44, 24);
        this.right_back_leg_bottom.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_back_leg_bottom.addBox(-1.0F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(right_back_leg_bottom, 1.0471975511965976F, 0.0F, 0.003490658503988659F);
        this.left_back_leg_top = new ModelRenderer(this, 30, 16);
        this.left_back_leg_top.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_back_leg_top.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(left_back_leg_top, 0.27314402793711257F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.body.addBox(-3.0F, -2.0F, -8.0F, 6, 5, 8, 0.0F);
        this.setRotateAngle(body, -0.3490658503988659F, 0.0F, 0.0F);
        this.left_front_leg = new ModelRenderer(this, 8, 15);
        this.left_front_leg.setRotationPoint(3.0F, 17.0F, 1.0F);
        this.left_front_leg.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(left_front_leg, -0.19198621771937624F, 0.0F, 0.0F);
        this.left_foot = new ModelRenderer(this, 14, 26);
        this.left_foot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_foot.addBox(-1.0F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.right_back_leg_top = new ModelRenderer(this, 16, 16);
        this.right_back_leg_top.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_back_leg_top.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(right_back_leg_top, 0.27314402793711257F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 28, 0);
        this.head.setRotationPoint(0.0F, 17.0F, 1.0F);
        this.head.addBox(-3.5F, -4.0F, -5.0F, 7, 4, 5, 0.0F);
        this.right_eye = new ModelRenderer(this, 52, 0);
        this.right_eye.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.right_eye.addBox(-4.5F, -4.0F, -2.5F, 3, 2, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.left_eye.render(f5);
        this.right_front_leg.render(f5);
        this.left_back_leg_bottom.render(f5);
        this.right_foot.render(f5);
        this.right_back_leg_bottom.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.left_back_leg_top.offsetX, this.left_back_leg_top.offsetY, this.left_back_leg_top.offsetZ);
        GlStateManager.translate(this.left_back_leg_top.rotationPointX * f5, this.left_back_leg_top.rotationPointY * f5, this.left_back_leg_top.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.left_back_leg_top.offsetX, -this.left_back_leg_top.offsetY, -this.left_back_leg_top.offsetZ);
        GlStateManager.translate(-this.left_back_leg_top.rotationPointX * f5, -this.left_back_leg_top.rotationPointY * f5, -this.left_back_leg_top.rotationPointZ * f5);
        this.left_back_leg_top.render(f5);
        GlStateManager.popMatrix();
        this.body.render(f5);
        this.left_front_leg.render(f5);
        this.left_foot.render(f5);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.right_back_leg_top.offsetX, this.right_back_leg_top.offsetY, this.right_back_leg_top.offsetZ);
        GlStateManager.translate(this.right_back_leg_top.rotationPointX * f5, this.right_back_leg_top.rotationPointY * f5, this.right_back_leg_top.rotationPointZ * f5);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.right_back_leg_top.offsetX, -this.right_back_leg_top.offsetY, -this.right_back_leg_top.offsetZ);
        GlStateManager.translate(-this.right_back_leg_top.rotationPointX * f5, -this.right_back_leg_top.rotationPointY * f5, -this.right_back_leg_top.rotationPointZ * f5);
        this.right_back_leg_top.render(f5);
        GlStateManager.popMatrix();
        this.head.render(f5);
        this.right_eye.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    
    /*TODO
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = ageInTicks - (float)entityIn.ticksExisted;
        EntityFrog entityfrog = (EntityFrog)entityIn;
        this.rabbitNose.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitHead.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitRightEar.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitLeftEar.rotateAngleX = headPitch * 0.017453292F;
        this.rabbitNose.rotateAngleY = netHeadYaw * 0.017453292F;
        this.rabbitHead.rotateAngleY = netHeadYaw * 0.017453292F;
        this.rabbitRightEar.rotateAngleY = this.rabbitNose.rotateAngleY - 0.2617994F;
        this.rabbitLeftEar.rotateAngleY = this.rabbitNose.rotateAngleY + 0.2617994F;
        this.jumpRotation = MathHelper.sin(entityfrog.setJumpCompletion(f) * (float)Math.PI);
        this.rabbitLeftThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitRightThigh.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.rabbitLeftFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitRightFoot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.rabbitLeftArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
        this.rabbitRightArm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
    }
    

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     *
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
        this.jumpRotation = MathHelper.sin(((EntityFrog)entitylivingbaseIn).setJumpCompletion(partialTickTime) * (float)Math.PI);
    }
    */
}
