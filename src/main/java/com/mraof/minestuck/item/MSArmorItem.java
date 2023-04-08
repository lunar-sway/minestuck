package com.mraof.minestuck.item;

import com.mraof.minestuck.client.model.armor.ArmorModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

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
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				if(equipmentSlot == slot)
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
						
						
						model.crouching = original.crouching;
						model.riding = original.riding;
						model.young = original.young;
						
						model.rightArmPose = original.rightArmPose;
						model.leftArmPose = original.leftArmPose;
						
						return model;
					}
				}
				
				return original;
			}
		});
	}
	
	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		ResourceLocation name = ForgeRegistries.ITEMS.getKey(this);
        return name.getNamespace() + ":textures/models/armor/" + (texture.isEmpty() ? name.getPath() : texture) + ".png";
    }
}
