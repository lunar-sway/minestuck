package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Leaf - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelLeaf extends ModelBase {
    public ModelRenderer leafrightback;
    public ModelRenderer stem;
    public ModelRenderer leafleftback;
    public ModelRenderer leafrightfront;
    public ModelRenderer leafleftfront;

    public ModelLeaf() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.leafrightfront = new ModelRenderer(this, 0, 9);
        this.leafrightfront.setRotationPoint(1.0F, 0.0F, 3.0F);
        this.leafrightfront.addBox(0.0F, 0.0F, 0.0F, 5, 1, 8, 0.0F);
        this.setRotateAngle(leafrightfront, 0.0F, 0.0F, -0.22689280275926282F);
        this.leafleftback = new ModelRenderer(this, 0, 25);
        this.leafleftback.setRotationPoint(0.0F, 0.0F, -3.0F);
        this.leafleftback.addBox(-6.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.setRotateAngle(leafleftback, 0.0F, 0.0F, 0.22689280275926282F);
        this.stem = new ModelRenderer(this, 20, 0);
        this.stem.setRotationPoint(0.0F, 0.0F, -8.0F);
        this.stem.addBox(0.0F, 0.0F, 0.0F, 1, 1, 21, 0.0F);
        this.leafleftfront = new ModelRenderer(this, 0, 0);
        this.leafleftfront.setRotationPoint(0.0F, 0.0F, 3.0F);
        this.leafleftfront.addBox(-5.0F, 0.0F, 0.0F, 5, 1, 8, 0.0F);
        this.setRotateAngle(leafleftfront, 0.0F, 0.0F, 0.22689280275926282F);
        this.leafrightback = new ModelRenderer(this, 24, 25);
        this.leafrightback.setRotationPoint(1.0F, 0.0F, -3.0F);
        this.leafrightback.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6, 0.0F);
        this.setRotateAngle(leafrightback, 0.0F, 0.0F, -0.22689280275926282F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leafrightfront.render(f5);
        this.leafleftback.render(f5);
        this.stem.render(f5);
        this.leafleftfront.render(f5);
        this.leafrightback.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
