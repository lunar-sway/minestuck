package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.client.model.machine.AlchemiterModel;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AlchemiterRenderer extends GeoBlockRenderer<AlchemiterBlockEntity>
{
	public AlchemiterRenderer(BlockEntityRenderDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new AlchemiterModel());
	}
}