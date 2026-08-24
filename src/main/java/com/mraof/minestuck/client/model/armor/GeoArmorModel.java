package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.armor.GeoArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeoArmorModel extends GeoModel<GeoArmorItem>
{
	private final String name;
	
	public GeoArmorModel(String name)
	{
		this.name = name;
	}
	
	@Override
	public ResourceLocation getModelResource(GeoArmorItem animatable)
	{
		return ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "geo/" + name + ".geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(GeoArmorItem animatable)
	{
		return ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "textures/models/armor/" + name + ".png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(GeoArmorItem animatable)
	{
		return ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "animations/" + name + ".animation.json");
	}
}
