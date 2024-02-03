package com.mraof.minestuck.client.renderer.armor;

import com.mraof.minestuck.client.model.armor.PrismarineArmorModel;
import com.mraof.minestuck.item.armor.PrismarineArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class PrismarineArmorRenderer extends GeoArmorRenderer<PrismarineArmorItem>
{
	
	public PrismarineArmorRenderer()
	{
		super(new PrismarineArmorModel());
	}
}
