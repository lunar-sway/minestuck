package com.mraof.minestuck.client.renderer.armor;

import com.mraof.minestuck.client.model.armor.JetpackModel;
import com.mraof.minestuck.item.armor.JetPackItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class JetpackModelRenderer  extends GeoArmorRenderer<JetPackItem>
{
	public JetpackModelRenderer()
	{
		super(new JetpackModel());
	}
}
