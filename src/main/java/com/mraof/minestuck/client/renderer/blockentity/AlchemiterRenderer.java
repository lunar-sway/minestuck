package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.client.model.block.AlchemiterModel;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AlchemiterRenderer extends GeoBlockRenderer<AlchemiterBlockEntity>
{
	public AlchemiterRenderer(BlockEntityRendererProvider.Context context)
	{
		super(context, new AlchemiterModel());
	}
}