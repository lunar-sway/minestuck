package com.mraof.minestuck.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MSArmorItem extends ArmorItem
{
    private final String texture;
    
    public MSArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties)
    {
        this("", material, type, properties);
    }

    public MSArmorItem(String texture, Holder<ArmorMaterial> material, Type type, Properties properties)
    {
        super(material, type, properties);
        this.texture = texture;
    }
	
	@Nullable
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel)
	{
		ResourceLocation name = BuiltInRegistries.ITEM.getKey(this);
        return name.withPath("textures/models/armor/" + (texture.isEmpty() ? name.getPath() : texture) + ".png");
    }
}
