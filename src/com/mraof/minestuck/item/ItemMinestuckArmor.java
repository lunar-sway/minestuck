package com.mraof.minestuck.item;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemMinestuckArmor extends ArmorItem
{
	protected final BipedModel<?> model;
	
	public ItemMinestuckArmor(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, BipedModel<?> model, Item.Properties builder)
	{
		super(materialIn, equipmentSlotIn, builder);
		this.model = model;
	}
	
	public ItemMinestuckArmor(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties builder)
	{
		this(materialIn, equipmentSlotIn, null, builder);
	}
	
	@Nullable
	@Override
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlotType armorSlot, A _default)
	{
		if(model == null) return super.getArmorModel(entity, stack, slot, _default);
		
		if(!stack.isEmpty())
		{
			if(stack.getItem() instanceof ItemMinestuckArmor)
			{
				model.bipedRightLeg.showModel = slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET;
				model.bipedLeftLeg.showModel = slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET;
				
				model.bipedBody.showModel = slot == EquipmentSlotType.CHEST;
				model.bipedLeftArm.showModel = slot == EquipmentSlotType.CHEST;
				model.bipedRightArm.showModel = slot == EquipmentSlotType.CHEST;
				
				model.bipedHead.showModel = slot == EquipmentSlotType.HEAD;
				model.bipedHeadwear.showModel = slot == EquipmentSlotType.HEAD;
				
				
				model.isSneak = _default.isSneak;
				//model.isRiding = _default.isRiding;
				model.isChild = _default.isChild;

				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;
				
				return (A) model;
			}
		}
		
		return null;
	}
}