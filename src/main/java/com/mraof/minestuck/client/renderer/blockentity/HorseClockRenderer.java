package com.mraof.minestuck.client.renderer.blockentity;

import com.mraof.minestuck.blockentity.HorseClockBlockEntity;
import com.mraof.minestuck.client.model.blockentity.HorseClockModel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HorseClockRenderer extends GeoBlockRenderer<HorseClockBlockEntity>
{
	public HorseClockRenderer(BlockEntityRendererProvider.Context ignored)
	{
		super(new HorseClockModel());
	}
	
	@Override
	public AABB getRenderBoundingBox(HorseClockBlockEntity blockEntity)
	{
		return INFINITE_EXTENT_AABB; //keeps the model rendered even when the blockpos is no longer viewed by the camera
	}
}