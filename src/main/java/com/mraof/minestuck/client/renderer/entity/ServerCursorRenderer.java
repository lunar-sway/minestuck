package com.mraof.minestuck.client.renderer.entity;

import com.mraof.minestuck.client.model.ServerCursorModel;
import com.mraof.minestuck.entity.ServerCursorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class ServerCursorRenderer extends GeoEntityRenderer<ServerCursorEntity>
{
	
	public ServerCursorRenderer(EntityRendererProvider.Context context) { super(context, new ServerCursorModel()); }
	
	
	//override method to render model at fullbright always.
	@Override
	protected int getSkyLightLevel(ServerCursorEntity pEntity, BlockPos pPos) {
		return 15;
	}
	
	//override method to prevent model from darkening when inside blocks
	@Override
	protected int getBlockLightLevel(ServerCursorEntity pEntity, BlockPos pPos) {
		return 15;
	}
	
	@Override
	public RenderType getRenderType(ServerCursorEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
	{
		return RenderType.entityTranslucentCull(getTextureLocation(animatable));
	}
}
