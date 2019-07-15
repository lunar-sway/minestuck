package com.mraof.minestuck.client.model;

import com.mraof.minestuck.entity.consort.NakagatorEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class NakagatorModel<T extends NakagatorEntity> extends EntityModel<T>
{
	public boolean hasArms;
	RendererModel body;
	RendererModel rightLeg;
	RendererModel leftLeg;
	RendererModel head;
	RendererModel upperTail;
	RendererModel lowerTail;
	RendererModel upperJaw;
	RendererModel lowerJaw;

    
    public NakagatorModel()
    {
    	this(false);
    }
    
    public NakagatorModel(boolean hasArms)
    {
    	textureWidth = 64;
        textureHeight = 32;
	
		this.body = new RendererModel(this, 10, 17);
		this.body.setRotationPoint(0.0F, 10.0F, 0.0F);
		this.body.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6, 0.0F);
		this.rightLeg = new RendererModel(this, 0, 16);
		this.rightLeg.setRotationPoint(-1.0F, 19.0F, 0.0F);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3, 0.0F);
		this.leftLeg = new RendererModel(this, 0, 24);
		this.leftLeg.setRotationPoint(3.0F, 19.0F, 0.0F);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 2, 5, 3, 0.0F);
		this.head = new RendererModel(this, 0, 0);
		this.head.setRotationPoint(0.0F, 8.3F, 0.0F);
		this.head.addBox(-3.0F, -1.3F, -3.0F, 6, 3, 7, 0.0F);
		this.upperJaw = new RendererModel(this, 34, 0);
		this.upperJaw.setRotationPoint(0.0F, 8.1F, 0.0F);
		this.upperJaw.addBox(-2.0F, 0.0F, -12.0F, 4, 1, 11, 0.0F);
		this.lowerJaw = new RendererModel(this, 34, 12);
		this.lowerJaw.setRotationPoint(0.0F, 9.0F, -0.0F);
		this.lowerJaw.addBox(-2.0F, 0.0F, -10.0F, 4, 1, 11, 0.0F);
		this.lowerTail = new RendererModel(this, 26, 0);
		this.lowerTail.setRotationPoint(0.0F, 22.0F, 4.0F);
		this.lowerTail.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 7, 0.0F);
		this.upperTail = new RendererModel(this, 26, 9);
		this.upperTail.setRotationPoint(0.0F, 18.0F, 3.0F);
		this.upperTail.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.setRotation(upperTail, 0.22307169437408447F, 0.0F, 0.0F);
        
	}
	
	@Override
	public void render(T entity, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.setRotationAngles(entity, par2, par3, par4, par5, par6, par7);

		if (this.isChild)
		{
			float var8 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
			GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
			head.render(par7);
			upperJaw.render(par7);
			lowerJaw.render(par7);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
//			if(hasArms)
//			{
//				this.rightArm.render(par7);
//				this.leftArm.render(par7);
//			}
			this.body.render(par7);
			this.rightLeg.render(par7);
			this.leftLeg.render(par7);
			this.upperTail.render(par7);
			this.lowerTail.render(par7);
			GL11.glPopMatrix();
		}
		else
		{
			head.render(par7);
			upperJaw.render(par7);
			lowerJaw.render(par7);
//			if(hasArms)
//			{
//				this.rightArm.render(par7);
//				this.leftArm.render(par7);
//			}
			this.body.render(par7);
			this.rightLeg.render(par7);
			this.leftLeg.render(par7);
			this.upperTail.render(par7);
			this.lowerTail.render(par7);
		}
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    private void setRotation(RendererModel model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(T par7Entity, float par1, float par2, float par3, float par4, float par5, float par6) {
		this.head.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.head.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.upperJaw.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.lowerJaw.rotateAngleX = par5 / (180F / (float)Math.PI);
		this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
		this.rightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
	}
}
