package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.client.model.blockentity.AlchemiterModel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlchemiterRenderer extends GeoBlockRenderer<AlchemiterBlockEntity>
{
	public AlchemiterRenderer(BlockEntityRendererProvider.Context ignored)
	{
		super(new AlchemiterModel());
	}
	
	@Override
	public AABB getRenderBoundingBox(AlchemiterBlockEntity blockEntity)
	{
		return INFINITE_EXTENT_AABB;
	}
}