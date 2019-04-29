package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.1
 */
public class ModelTest extends ModelBiped{
    public ModelRenderer field_78116_c;

    public ModelTest() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_78116_c = new ModelRenderer(this, 0, 15);
        this.field_78116_c.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.field_78116_c.addBox(-4.0F, -9.0F, -4.0F, 8, 9, 8, 0.0F);
        
        this.bipedHead.addChild(field_78116_c);
    } 

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) 
    { 
        super.render(entity, f, f1, f2, f3, f4 ,f5);
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
