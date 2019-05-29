package com.mraof.minestuck.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ItemMinestuckArmor extends ItemArmor
{
	protected final ModelBiped model;
	
	public ItemMinestuckArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, ModelBiped model) 
	{
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.model = model;
	}
	
	public ItemMinestuckArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) 
	{
		this(materialIn, renderIndexIn, equipmentSlotIn, null);
	}
	
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot,
			ModelBiped _default) 
	{
		if(model == null) return super.getArmorModel(entity, stack, slot, _default);
		
		if(!stack.isEmpty())
		{
			if(stack.getItem() instanceof ItemMinestuckArmor)
			{
				ModelBiped model = this.model;
				
				model.bipedRightLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
				model.bipedLeftLeg.showModel = slot == EntityEquipmentSlot.LEGS || slot == EntityEquipmentSlot.FEET;
				
				model.bipedBody.showModel = slot == EntityEquipmentSlot.CHEST;
				model.bipedLeftArm.showModel = slot == EntityEquipmentSlot.CHEST;
				model.bipedRightArm.showModel = slot == EntityEquipmentSlot.CHEST;
				
				model.bipedHead.showModel = slot == EntityEquipmentSlot.HEAD;
				model.bipedHeadwear.showModel = slot == EntityEquipmentSlot.HEAD;
				
				
				model.isSneak = _default.isSneak;
				model.isRiding = _default.isRiding;
				model.isChild = _default.isChild;

				model.rightArmPose = _default.rightArmPose;
				model.leftArmPose = _default.leftArmPose;
				
				return model;
			}
		}
		
		return null;
	}
	
}
