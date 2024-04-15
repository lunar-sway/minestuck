package com.mraof.minestuck.item.armor;

import com.mraof.minestuck.client.model.armor.ArmorModels;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class MSArmorItem extends ArmorItem
{
    private final String texture;
    
    public MSArmorItem(ArmorMaterial material, Type type, Properties properties)
    {
        this("", material, type, properties);
    }

    public MSArmorItem(String texture, ArmorMaterial material, Type type, Properties properties)
    {
        super(material, type, properties);
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
				if(equipmentSlot == type.getSlot())
				{
					HumanoidModel<?> model = ArmorModels.get(MSArmorItem.this);
					if (model != null)
					{
						model.rightLeg.visible = type == Type.LEGGINGS || type == Type.BOOTS;
						model.leftLeg.visible = type == Type.LEGGINGS || type == Type.BOOTS;
						
						model.body.visible = type == Type.CHESTPLATE;
						model.leftArm.visible = type == Type.CHESTPLATE;
						model.rightArm.visible = type == Type.CHESTPLATE;
						
						model.head.visible = type == Type.HELMET;
						model.hat.visible = type == Type.HELMET;
						
						
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
		ResourceLocation name = BuiltInRegistries.ITEM.getKey(this);
        return name.getNamespace() + ":textures/models/armor/" + (texture.isEmpty() ? name.getPath() : texture) + ".png";
    }
}
