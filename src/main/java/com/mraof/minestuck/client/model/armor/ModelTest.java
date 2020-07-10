package com.mraof.minestuck.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.1
 */
public class ModelTest extends BipedModel<LivingEntity>
{
    public RendererModel field_78116_c;

    public ModelTest() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.field_78116_c = new RendererModel(this, 0, 15);
        this.field_78116_c.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.field_78116_c.addBox(-4.0F, -9.0F, -4.0F, 8, 9, 8, 0.0F);
        
        this.bipedHead.addChild(field_78116_c);
    } 

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
