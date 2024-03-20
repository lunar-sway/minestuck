package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.io.IOException;
import java.io.InputStream;

/**
 * A renderer that applies the correct grist texture/color to the underlings
 */
@MethodsReturnNonnullByDefault
public class UnderlingRenderer<T extends UnderlingEntity> extends GeoEntityRenderer<T>
{
	private final String underlingName;
	
	public UnderlingRenderer(EntityRendererProvider.Context context, String underlingName)
	{
		// this renderer does two simple things :
		// render the entity with the base layer merged with a grist texture
		super(context, new DefaultedEntityGeoModel<>(Minestuck.id("underlings/" + underlingName), true));
		// render a second layer with details (eyes, mouth, etc)
		this.addRenderLayer(new UnderlingDetailsLayer<>(this, underlingName));
		this.underlingName = underlingName;
	}
	
	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public Color getRenderColor(T animatable, float partialTick, int packedLight)
	{
		return Color.ofOpaque(animatable.getGristType().getUnderlingColor());
	}
	
	/**
	 * Used to merge the grist texture with the base underling layer while keeping the transparent bits
	 *
	 * @return a merged resource location - ready for rendering
	 */
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		ResourceLocation resource = entity.getGristType().getTextureId().withPath(gristName -> "textures/entity/underlings/%s_%s.png".formatted(this.underlingName, gristName));
		
		// the texture manager will cache the computed textures so they're effectively computed once (at least in theory)
		SimpleTexture nullTexture = new SimpleTexture(resource);
		if(Minecraft.getInstance().textureManager.getTexture(resource, nullTexture) == nullTexture)
		{
			DynamicTexture texture = createLayeredTexture(entity);
			
			// save the computed texture to the texture manager's cache
			Minecraft.getInstance().textureManager.register(resource, texture);
		}
		
		return resource;
	}
	
	private DynamicTexture createLayeredTexture(T entity) {
		ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
		NativeImage base = loadImage(resourceManager, super.getTextureLocation(entity));
		NativeImage texture = loadImage(resourceManager, getGristTexture(entity));
		NativeImage computed = new NativeImage(base.getWidth(), base.getHeight(), false);
		
		// loop through pixels on the base, apply textures & keep transparency
		// TODO possibility to bake the details layer directly in the texture when/if the renderer stops coloring the vertex
		for(int i = 0; i < base.getWidth(); i++)
		{
			for(int j = 0; j < base.getHeight(); j++)
			{
				if(FastColor.ARGB32.alpha(base.getPixelRGBA(i, j)) == 0)
				{
					computed.setPixelRGBA(i, j, 0);
				} else
				{
					computed.setPixelRGBA(i, j, texture.getPixelRGBA(i % texture.getWidth(), j % texture.getHeight()));
				}
			}
		}
		
		return new DynamicTexture(computed);
	}
	
	private NativeImage loadImage(ResourceManager resourceManager, ResourceLocation location) {
		Resource resource = resourceManager.getResource(location).orElseThrow(() -> new RuntimeException("Couldn't find image %s".formatted(location)));
		try(InputStream inputStream = resource.open()) {
			return NativeImage.read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load image %s".formatted(location), e);
		}
	}
	
	private ResourceLocation getGristTexture(T entity)
	{
		var textureLocation = entity.getGristType().getTextureId().withPath(textureName -> "textures/entity/underlings/" + textureName + ".png");
		if (Minecraft.getInstance().getResourceManager().getResource(textureLocation).isEmpty()) {
			return this.model.getTextureResource(entity);
		}
		return textureLocation;
	}
	
	/**
	 * Detail layer for the underling
	 */
	private static class UnderlingDetailsLayer<T extends UnderlingEntity> extends GeoRenderLayer<T>
	{
		private final ResourceLocation textureId;
		
		public UnderlingDetailsLayer(GeoRenderer<T> entityRendererIn, String underlingName)
		{
			super(entityRendererIn);
			this.textureId = Minestuck.id("textures/entity/underlings/" + underlingName + "_details.png");
		}
		
		@Override
		public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay)
		{
			RenderType layerRenderType = RenderType.entityCutoutNoCullZOffset(this.textureId);
			float color = getContrastModifier(animatable);
			poseStack.pushPose();
			
			this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, layerRenderType, bufferSource.getBuffer(layerRenderType),
					partialTick, packedLight, packedOverlay, color, color, color, 1);
			
			poseStack.popPose();
		}
		
		private float getContrastModifier(T entity)
		{
			int threshold = 170;
			int color = entity.getGristType().getUnderlingColor();
			int avg = (FastColor.ABGR32.red(color) + FastColor.ABGR32.green(color) + FastColor.ABGR32.blue(color)) / 3;
			//TODO replace with a light/dark texture to make it look better
			return avg > threshold ? 0.3f : 1.0f;
		}
	}
}