package com.mraof.minestuck.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MSArmorItem extends ArmorItem
{
    @OnlyIn(Dist.CLIENT)
    private BipedModel model;
    private final String texture;
    
    public MSArmorItem(IArmorMaterial material, EquipmentSlotType slot, Properties properties) 
    {
        this("", material, slot, properties);
    }

    public MSArmorItem(String texture, IArmorMaterial material, EquipmentSlotType slot, Properties properties)
    {
        super(material, slot, properties);
        this.texture = texture;
    }

    @OnlyIn(Dist.CLIENT)
    public void setArmorModel(BipedModel model)
    {
        this.model = model;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type)
    {
        return getRegistryName().getNamespace() + ":textures/models/armor/" + (texture.isEmpty() ? getRegistryName().getPath() : texture) + ".png";
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public BipedModel getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType armorSlot, BipedModel _default)
    {
        if(model == null) return super.getArmorModel(entity, stack, slot, _default);

        if(!stack.isEmpty())
        {
            if(stack.getItem() instanceof MSArmorItem)
            {
                BipedModel model = this.model;

                model.rightLeg.visible = slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET;
                model.leftLeg.visible = slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET;

                model.body.visible = slot == EquipmentSlotType.CHEST;
                model.leftArm.visible = slot == EquipmentSlotType.CHEST;
                model.rightArm.visible = slot == EquipmentSlotType.CHEST;

                model.head.visible = slot == EquipmentSlotType.HEAD;
                model.hat.visible = slot == EquipmentSlotType.HEAD;


                model.crouching = _default.crouching;
                model.riding = _default.riding;
                model.young = _default.young;

                model.rightArmPose = _default.rightArmPose;
                model.leftArmPose = _default.leftArmPose;

                return model;
            }
        }

        return null;
    }
}
