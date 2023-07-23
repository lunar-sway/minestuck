package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.client.model.blockentity.AlchemiterModel;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AlchemiterRenderer extends GeoBlockRenderer<AlchemiterBlockEntity>
{
	public AlchemiterRenderer(BlockEntityRendererProvider.Context ignored)
	{
		super(new AlchemiterModel());
	}
}