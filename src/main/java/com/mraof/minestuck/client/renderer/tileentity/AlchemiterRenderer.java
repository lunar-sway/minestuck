package com.mraof.minestuck.client.renderer.tileentity;

import com.mraof.minestuck.client.model.machine.AlchemiterModel;
import com.mraof.minestuck.tileentity.machine.AlchemiterTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AlchemiterRenderer extends GeoBlockRenderer<AlchemiterTileEntity>
{
	public AlchemiterRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
	{
		super(rendererDispatcherIn, new AlchemiterModel());
	}
}