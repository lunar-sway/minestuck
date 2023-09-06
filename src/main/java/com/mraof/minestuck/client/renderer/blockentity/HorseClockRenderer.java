package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import com.mraof.minestuck.client.model.blockentity.HorseClockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HorseClockRenderer extends GeoBlockRenderer<HorseClockBlockEntity>
{
	public HorseClockRenderer(BlockEntityRendererProvider.Context ignored)
	{
		super(new HorseClockModel());
	}
}