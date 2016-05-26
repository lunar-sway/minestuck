package com.mraof.minestuck.item;

import net.minecraft.item.ItemAxe;

public class ItemMinestuckAxe extends ItemAxe
{
	public ItemMinestuckAxe(ToolMaterial material)
	{
		super(ToolMaterial.IRON);
		this.toolMaterial = material;
		this.damageVsEntity = material.getDamageVsEntity() + 3.0F;	//TODO update this
		this.setMaxDamage(material.getMaxUses());
		this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
	}
}