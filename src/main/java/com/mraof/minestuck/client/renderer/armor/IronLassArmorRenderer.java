package com.mraof.minestuck.client.renderer.armor;

import com.mraof.minestuck.client.model.armor.IronLassArmorModel;
import com.mraof.minestuck.item.armor.IronLassArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class IronLassArmorRenderer extends GeoArmorRenderer<IronLassArmorItem>
{
	public IronLassArmorRenderer()
	{
		super(new IronLassArmorModel());
	}
}
