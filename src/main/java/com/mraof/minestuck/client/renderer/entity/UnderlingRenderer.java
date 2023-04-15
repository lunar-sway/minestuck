package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.entity.UnderlingModel;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.io.IOException;
import java.io.InputStream;

/**
 * A renderer that applies the correct grist texture/color to the underlings
 */
public class UnderlingRenderer<T extends UnderlingEntity> extends GeoEntityRenderer<T>
{
	public UnderlingRenderer(EntityRendererProvider.Context context)
	{
		// this renderer does two simple things :
		super(context, new UnderlingModel<>()); // render the entity with the base layer merged with a grist texture
		this.addLayer(new UnderlingDetailsLayer(this)); // render a second layer with details (eyes, mouth, etc)
	}
	
	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public Color getRenderColor(T animatable, float partialTicks, PoseStack stack, @org.jetbrains.annotations.Nullable MultiBufferSource renderTypeBuffer, @org.jetbrains.annotations.Nullable VertexConsumer vertexBuilder, int packedLightIn)
	{
		return Color.ofOpaque(animatable.getGristType().getColor());
	}
	
	/**
	 * Used to merge the grist texture with the base underling layer while keeping the transparent bits
	 *
	 * @return a merged resource location - ready for rendering
	 */
	@Override
	public ResourceLocation getTextureLocation(T entity)
	{
		String textureName = entity.getGristType().getEffectiveName().getPath();
		ResourceLocation resource = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entity) + "_" + textureName + ".png");
		
		// the texture manager will cache the computed textures so theyre effectively computed once (at least in theory)
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
				if(NativeImage.getA(base.getPixelRGBA(i, j)) == 0)
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
		String textureName = entity.getGristType().getEffectiveName().getPath();
		var textureLocation = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + textureName + ".png");
		if (Minecraft.getInstance().getResourceManager().getResource(textureLocation).isEmpty()) {
			return this.modelProvider.getTextureResource(entity);
		}
		return textureLocation;
	}
	
	/**
	 * Detail layer for the underling
	 */
	public class UnderlingDetailsLayer extends GeoLayerRenderer<T>
	{
		public UnderlingDetailsLayer(IGeoRenderer<T> entityRendererIn)
		{
			super(entityRendererIn);
		}
		
		@Override
		public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
		{
			RenderType renderType = RenderType.armorCutoutNoCull(new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entityLivingBaseIn) + "_details.png"));
			float color = getContrastModifier(entityLivingBaseIn);
			matrixStackIn.pushPose();
			
			GeoModel model = modelProvider.getModel(modelProvider.getModelResource(entityLivingBaseIn));
			this.getRenderer().render(model, entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, color, color, color, 1);
			
			matrixStackIn.popPose();
		}
		
		private float getContrastModifier(T entity)
		{
			int threshold = 170;
			int color = entity.getGristType().getColor();
			int avg = (NativeImage.getR(color) + NativeImage.getG(color) + NativeImage.getB(color)) / 3;
			//TODO replace with a light/dark texture to make it look better
			return avg > threshold ? 0.3f : 1.0f;
		}
	}
}