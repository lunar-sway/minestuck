package com.mraof.minestuck.item;

import com.mraof.minestuck.client.model.armor.ArmorModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MSArmorItem extends ArmorItem
{
    private final String texture;
    
    public MSArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties properties)
    {
        this("", material, slot, properties);
    }

    public MSArmorItem(String texture, ArmorMaterial material, EquipmentSlot slot, Properties properties)
    {
        super(material, slot, properties);
        this.texture = texture;
    }
	
	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer)
	{
		consumer.accept(new IItemRenderProperties()
		{
			@Nullable
			@Override
			public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default)
			{
				if(armorSlot == slot)
				{
					HumanoidModel<?> model = ArmorModels.get(MSArmorItem.this);
					if (model != null)
					{
						model.rightLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
						model.leftLeg.visible = slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
						
						model.body.visible = slot == EquipmentSlot.CHEST;
						model.leftArm.visible = slot == EquipmentSlot.CHEST;
						model.rightArm.visible = slot == EquipmentSlot.CHEST;
						
						model.head.visible = slot == EquipmentSlot.HEAD;
						model.hat.visible = slot == EquipmentSlot.HEAD;
						
						
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
		});
	}
	
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
        return getRegistryName().getNamespace() + ":textures/models/armor/" + (texture.isEmpty() ? getRegistryName().getPath() : texture) + ".png";
    }
}
