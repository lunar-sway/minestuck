package com.mraof.minestuck.client.model.armor;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.item.armor.JetPackItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class JetpackModel extends GeoModel<JetPackItem>
{
	
	@Override
	public ResourceLocation getModelResource(JetPackItem animatable)
	{
		return new ResourceLocation("minestuck", "geo/animated_armor/jetpack.geo.json");
	}
	
	@Override
	public ResourceLocation getTextureResource(JetPackItem animatable)
	{
		return new ResourceLocation("minestuck", "textures/models.armor/jetpack.png");
	}
	
	@Override
	public ResourceLocation getAnimationResource(JetPackItem animatable)
	{
		return new ResourceLocation("minestuck", "animations/armor/jetpack_anims.animation.json");
	}
}
