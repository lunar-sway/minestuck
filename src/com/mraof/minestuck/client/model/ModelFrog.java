package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.EntityFrog;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelFrog extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer left_eye;
    public ModelRenderer right_foot;
    public ModelRenderer left_leg_top;
    public ModelRenderer body;
    public ModelRenderer right_arm;
    public ModelRenderer left_arm;
    public ModelRenderer right_eye;
    public ModelRenderer left_leg_bottom;
    public ModelRenderer right_leg_top;
    public ModelRenderer right_leg_bottom;
    public ModelRenderer left_foot;
    private float jumpRotation;

    public ModelFrog() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.left_eye = new ModelRenderer(this, 52, 5);
        this.left_eye.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.left_eye.addBox(1.5F, -4.0F, -2.5F, 3, 2, 3, 0.0F);
        this.right_arm = new ModelRenderer(this, 0, 15);
        this.right_arm.setRotationPoint(-3.0F, 17.0F, 1.0F);
        this.right_arm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(right_arm, -0.19198621771937624F, 0.0F, 0.0F);
        this.left_leg_bottom = new ModelRenderer(this, 28, 24);
        this.left_leg_bottom.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_leg_bottom.addBox(-1.0F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(left_leg_bottom, 1.0471975511965976F, 0.0F, 0.0F);
        this.right_foot = new ModelRenderer(this, 0, 26);
        this.right_foot.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_foot.addBox(-1.0F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.right_leg_bottom = new ModelRenderer(this, 44, 24);
        this.right_leg_bottom.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_leg_bottom.addBox(-1.0F, 3.5F, -4.3F, 2, 2, 6, 0.0F);
        this.setRotateAngle(right_leg_bottom, 1.0471975511965976F, 0.0F, 0.003490658503988659F);
        this.left_leg_top = new ModelRenderer(this, 30, 16);
        this.left_leg_top.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_leg_top.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(left_leg_top, 0.27314402793711257F, 0.0F, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 19.0F, 8.0F);
        this.body.addBox(-3.0F, -2.0F, -8.0F, 6, 5, 8, 0.0F);
        this.setRotateAngle(body, -0.3490658503988659F, 0.0F, 0.0F);
        this.left_arm = new ModelRenderer(this, 8, 15);
        this.left_arm.setRotationPoint(3.0F, 17.0F, 1.0F);
        this.left_arm.addBox(-1.0F, 0.0F, -1.0F, 2, 7, 2, 0.0F);
        this.setRotateAngle(left_arm, -0.19198621771937624F, 0.0F, 0.0F);
        this.left_foot = new ModelRenderer(this, 14, 26);
        this.left_foot.setRotationPoint(3.0F, 17.5F, 3.7F);
        this.left_foot.addBox(-1.0F, 5.4F, -2.5F, 2, 1, 5, 0.0F);
        this.right_leg_top = new ModelRenderer(this, 16, 16);
        this.right_leg_top.setRotationPoint(-3.0F, 17.5F, 3.7F);
        this.right_leg_top.addBox(-1.0F, 0.0F, 0.0F, 2, 3, 5, 0.0F);
        this.setRotateAngle(right_leg_top, 0.27314402793711257F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 28, 0);
        this.head.setRotationPoint(0.0F, 17.0F, 1.0F);
        this.head.addBox(-3.5F, -4.0F, -5.0F, 7, 4, 5, 0.0F);
        this.right_eye = new ModelRenderer(this, 52, 0);
        this.right_eye.setRotationPoint(0.0F, 16.0F, -1.0F);
        this.right_eye.addBox(-4.5F, -4.0F, -2.5F, 3, 2, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) { 
        this.left_eye.render(scale);
        this.right_arm.render(scale);
        this.left_leg_bottom.render(scale);
        this.right_foot.render(scale);
        this.right_leg_bottom.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.left_leg_top.offsetX, this.left_leg_top.offsetY, this.left_leg_top.offsetZ);
        GlStateManager.translate(this.left_leg_top.rotationPointX * scale, this.left_leg_top.rotationPointY * scale, this.left_leg_top.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.left_leg_top.offsetX, -this.left_leg_top.offsetY, -this.left_leg_top.offsetZ);
        GlStateManager.translate(-this.left_leg_top.rotationPointX * scale, -this.left_leg_top.rotationPointY * scale, -this.left_leg_top.rotationPointZ * scale);
        this.left_leg_top.render(scale);
        GlStateManager.popMatrix();
        this.body.render(scale);
        this.left_arm.render(scale);
        this.left_foot.render(scale);
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.right_leg_top.offsetX, this.right_leg_top.offsetY, this.right_leg_top.offsetZ);
        GlStateManager.translate(this.right_leg_top.rotationPointX * scale, this.right_leg_top.rotationPointY * scale, this.right_leg_top.rotationPointZ * scale);
        GlStateManager.scale(1.0D, 0.9D, 1.0D);
        GlStateManager.translate(-this.right_leg_top.offsetX, -this.right_leg_top.offsetY, -this.right_leg_top.offsetZ);
        GlStateManager.translate(-this.right_leg_top.rotationPointX * scale, -this.right_leg_top.rotationPointY * scale, -this.right_leg_top.rotationPointZ * scale);
        this.right_leg_top.render(scale);
        GlStateManager.popMatrix();
        this.head.render(scale);
        this.right_eye.render(scale);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = ageInTicks - (float)entityIn.ticksExisted;
        EntityFrog entityfrog = (EntityFrog)entityIn;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.right_eye.rotateAngleX = headPitch * 0.017453292F;
        this.left_eye.rotateAngleX = headPitch * 0.017453292F;
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.right_eye.rotateAngleY = netHeadYaw * 0.017453292F;
        this.left_eye.rotateAngleY =  netHeadYaw * 0.017453292F;
        this.jumpRotation = MathHelper.sin(entityfrog.setJumpCompletion(f) * (float)Math.PI);
        this.left_leg_top.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.right_leg_top.rotateAngleX = (this.jumpRotation * 50.0F - 21.0F) * 0.017453292F;
        this.left_leg_bottom.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.right_leg_bottom.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.left_foot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.right_foot.rotateAngleX = this.jumpRotation * 50.0F * 0.017453292F;
        this.left_arm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
        this.right_arm.rotateAngleX = (this.jumpRotation * -40.0F - 11.0F) * 0.017453292F;
    }
    

    /*
    public ModelRenderer head;
    public ModelRenderer left_eye;
    public ModelRenderer right_foot;
    public ModelRenderer left_leg_top;
    public ModelRenderer body;
    public ModelRenderer right_arm;
    public ModelRenderer left_arm;
    public ModelRenderer right_eye;
    public ModelRenderer left_leg_bottom;
    public ModelRenderer right_leg_top;
    public ModelRenderer right_leg_bottom;
    public ModelRenderer left_foot;
     */
    
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
