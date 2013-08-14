
package com.mraof.minestuck.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPunchDesignex extends ModelBase
{
  //fields
    ModelRenderer Leg1;
    ModelRenderer Leg2;
    ModelRenderer Base;
    ModelRenderer Lip;
    ModelRenderer Top;
    ModelRenderer Keyboard;
  
  public ModelPunchDesignex()
  {
    textureWidth = 128;
    textureHeight = 128;
    
      Leg1 = new ModelRenderer(this, 0, 24);
      Leg1.addBox(0F, 0F, 0F, 4, 12, 16);
      Leg1.setRotationPoint(0F, -12F, 0F);
      Leg1.setTextureSize(128, 128);
      Leg1.mirror = true;
      setRotation(Leg1, 0F, 0F, 0F);
      Leg2 = new ModelRenderer(this, 0, 24);
      Leg2.addBox(0F, 0F, 0F, 4, 12, 16);
      Leg2.setRotationPoint(28F, -12F, 0F);
      Leg2.setTextureSize(128, 128);
      Leg2.mirror = true;
      setRotation(Leg2, 0F, 0F, 0F);
      Base = new ModelRenderer(this, 0, 0);
      Base.addBox(0F, 0F, 0F, 32, 8, 16);
      Base.setRotationPoint(0F, -20F, 0F);
      Base.setTextureSize(128, 128);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Lip = new ModelRenderer(this, 0, 72);
      Lip.addBox(0F, 0F, 0F, 32, 2, 20);
      Lip.setRotationPoint(0F, -22F, 0F);
      Lip.setTextureSize(128, 128);
      Lip.mirror = true;
      setRotation(Lip, 0F, 0F, 0F);
      Top = new ModelRenderer(this, 0, 52);
      Top.addBox(0F, 0F, 0F, 32, 10, 10);
      Top.setRotationPoint(0F, -32F, 0F);
      Top.setTextureSize(128, 128);
      Top.mirror = true;
      setRotation(Top, 0F, 0F, 0F);
      Keyboard = new ModelRenderer(this, 40, 24);
      Keyboard.addBox(0F, 0F, 0F, 17, 4, 8);
      Keyboard.setRotationPoint(2F, -26F, 11F);
      Keyboard.setTextureSize(128, 128);
      Keyboard.mirror = true;
      setRotation(Keyboard, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    Leg1.render(f5);
    Leg2.render(f5);
    Base.render(f5);
    Lip.render(f5);
    Top.render(f5);
    Keyboard.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5,Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
