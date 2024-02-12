package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.armor.PrismarineArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PrismarineArmorModel extends GeoModel<PrismarineArmorItem>
{
	@Override
	public ResourceLocation getModelResource(PrismarineArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "geo/prismarine_armor.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(PrismarineArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "textures/models/armor/prismarine_armor.png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(PrismarineArmorItem animatable)
	{
		return new ResourceLocation(Minestuck.MOD_ID, "animations/prismarine_armor.animation.json");
	}
}
