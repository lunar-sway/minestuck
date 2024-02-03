package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.armor.IronLassArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class IronLassArmorModel extends GeoModel<IronLassArmorItem>
{
	@Override
	public ResourceLocation getModelResource(IronLassArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/iron_lass_armor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(IronLassArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/models/armor/iron_lass.png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(IronLassArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/iron_lass.animation.json");
	}
}
