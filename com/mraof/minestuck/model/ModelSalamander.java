package com.mraof.minestuck.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSalamander extends ModelBase 
{
	public boolean isGlubbing;
	public boolean hasArms;
    private ModelRenderer tail;
    private ModelRenderer head;
    private ModelRenderer jaw;
    private ModelRenderer leftLeg;
    private ModelRenderer rightLeg;
    private ModelRenderer body;
    private ModelRenderer leftArm;
    private ModelRenderer rightArm;

    
    public ModelSalamander()
    {
    	this(false);
    }
    public ModelSalamander(boolean hasArms)
    {
    	super(); //argument one seems to be fatness, two seems 

        //constructor:

    	/*addBox(offsetx,offsety,offsetz,sizex,sizey,sizez)
    	 * setRotationPoint(x,y,z) 
    	 * x is length, y is height (0 is top of the model), z is width
    	 */
    	head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, 0F, -4F, 8, 3, 7);
        head.setRotationPoint(0F, 4F, 0F);
    	
        jaw = new ModelRenderer(this, 0, 10);
        jaw.addBox(-4F, 0F, -4F, 8, 1, 7);
        jaw.setRotationPoint(0F, 7F, 0F);
                
        body = new ModelRenderer(this, 0, 19);
        body.addBox(0F, 0F, 0F, 8, 7, 6);
        body.setRotationPoint(-4F, 8F, -3F);
        
        leftLeg = new ModelRenderer(this, 30, 0);
        leftLeg.addBox(-4F, 0F, -2F, 3, 8, 4);
        leftLeg.setRotationPoint(0F, 14F, 0F);

        rightLeg = new ModelRenderer(this, 30, 0);
        rightLeg.addBox(1F, 0F, -2F, 3, 8, 4);
        rightLeg.setRotationPoint(0F, 14F, 0F);

        rightArm = new ModelRenderer(this, 30, 12);
        rightArm.addBox(0F, 0F, 0F, 2, 4, 2);
        rightArm.setRotationPoint(4F, 10F, 0F);

        leftArm = new ModelRenderer(this, 30, 12);
        leftArm.addBox(0F, 0F, 0F, 2, 4, 2);
        leftArm.setRotationPoint(-6F, 10F, 0F);
        
    }
    public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);

        if (this.isChild)
        {
            float var8 = 2.0F;
            GL11.glPushMatrix();
            GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
            GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
            this.jaw.render(par7);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
            GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
            this.body.render(par7);
            if(hasArms)
            {
                this.rightArm.render(par7);
                this.leftArm.render(par7);
            }
            this.rightLeg.render(par7);
            this.leftLeg.render(par7);
            this.head.render(par7);
            GL11.glPopMatrix();
        }
        else
        {
            this.jaw.render(par7);
            this.body.render(par7);
            if(hasArms)
            {
                this.rightArm.render(par7);
                this.leftArm.render(par7);
            }
            this.rightLeg.render(par7);
            this.leftLeg.render(par7);
            this.head.render(par7);
        }
    }
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
    	this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.head.rotateAngleX = par5 / (180F / (float)Math.PI);
    	this.jaw.rotateAngleY = par4 / (180F / (float)Math.PI);
        this.jaw.rotateAngleX = par5 / (180F / (float)Math.PI);
        this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
        this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
    }

}
