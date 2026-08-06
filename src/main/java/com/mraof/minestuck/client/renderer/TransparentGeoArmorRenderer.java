package com.mraof.minestuck.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mraof.minestuck.item.armor.GeoArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class TransparentGeoArmorRenderer extends GeoArmorRenderer<GeoArmorItem>
{
	public TransparentGeoArmorRenderer(GeoModel<GeoArmorItem> model)
	{
		super(model);
	}
	
	/**
	 * Variant of armorCutoutNoCull in {@link RenderType}
	 */
	@Override
	public RenderType getRenderType(GeoArmorItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick)
	{
		return RenderType.create(
				"armor_cutout_no_cull_transparent",
				DefaultVertexFormat.NEW_ENTITY,
				VertexFormat.Mode.QUADS,
				1536,
				RenderType.CompositeState.builder()
						.setShaderState(RenderStateShard.RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER)
						.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
						.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
						.setCullState(RenderStateShard.NO_CULL)
						.setLightmapState(RenderStateShard.LIGHTMAP)
						.setOverlayState(RenderStateShard.OVERLAY)
						.setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
						.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
						.createCompositeState(true)
		);
	}
}
