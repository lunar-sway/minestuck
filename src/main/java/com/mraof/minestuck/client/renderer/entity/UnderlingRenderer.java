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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;
import software.bernie.geckolib.util.RenderUtil;

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
	public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight)
	{
		super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
	}
	
	@Override
	public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour)
	{
		if(bone.getName().equals("head"))
		{
			poseStack.pushPose();
			
			//anchored to body direction, not head. at feet
			//Quaternionf quat = new Quaternionf();
			//poseStack.rotateAround(quat, bone.getPosX(), bone.getPosY(), bone.getPosZ());
			
			//changes in scale but not direction. anchored to body direction, not head. at feet
			//Quaternionf quat = new Quaternionf(bone.getPosX(), bone.getPosY(), bone.getPosZ(), (float) (Minecraft.getInstance().level.getGameTime() % 100) / 100);
			//poseStack.rotateAround(quat, bone.getPosX(), bone.getPosY(), bone.getPosZ());
			
			//anchored to body direction, not head. at feet
			//Quaternionf quat = new Quaternionf(bone.getPosX(), bone.getPosY(), bone.getPosZ(), 1);
			//poseStack.mulPose(quat);
			
			//centered around and follows head but is warped
			//poseStack.mulPose(bone.getLocalSpaceMatrix());
			
			//flies around in a wide circle around the entity, even when static
			//poseStack.mulPose(bone.getModelRotationMatrix());
			
			//was visible momentarily, vibrating around body, before flying away
			//poseStack.mulPose(bone.getModelSpaceMatrix());
			
			//see nothing
			//poseStack.mulPose(bone.getWorldSpaceMatrix());
			
			//anchored to body position, around head
			//RenderUtil.translateToPivotPoint(poseStack, bone);
			
			//anchored to body position, around feet
			//RenderUtil.translateMatrixToBone(poseStack, bone);
			
			//Changes in scale, stretches, and warps, but is near head. The rotation of other mobs seems to change how it looks
			//poseStack.translate(bone.getPosX(), bone.getPosY(), bone.getPosZ());
			//RenderUtil.translateMatrixToBone(poseStack, bone);
			//RenderUtil.translateToPivotPoint(poseStack, bone);
			//poseStack.mulPose(bone.getLocalSpaceMatrix());
			
			//factors in head rotation, but at strange angles
			//RenderUtil.faceRotation(poseStack, animatable, partialTick);
			
			RenderUtil.translateAndRotateMatrixForBone(poseStack, bone);
			poseStack.translate(0.0, 0.8, 0.0);
			
			BakedGeoModel model = GeckoLibCache.getBakedModels().get(Minestuck.id("geo/entity/prototyping/chicken/torso_side.geo.json"));
			if(buffer != null && model != null)
				actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
			poseStack.popPose();
		}
		
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
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
		if(Minecraft.getInstance().getTextureManager().getTexture(resource, nullTexture) == nullTexture)
		{
			DynamicTexture texture = createLayeredTexture(entity);
			
			// save the computed texture to the texture manager's cache
			Minecraft.getInstance().getTextureManager().register(resource, texture);
		}
		
		return resource;
	}
	
	private DynamicTexture createLayeredTexture(T entity)
	{
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
	
	private NativeImage loadImage(ResourceManager resourceManager, ResourceLocation location)
	{
		Resource resource = resourceManager.getResource(location).orElseThrow(() -> new RuntimeException("Couldn't find image %s".formatted(location)));
		try(InputStream inputStream = resource.open())
		{
			return NativeImage.read(inputStream);
		} catch(IOException e)
		{
			throw new RuntimeException("Failed to load image %s".formatted(location), e);
		}
	}
	
	private ResourceLocation getGristTexture(T entity)
	{
		var textureLocation = entity.getGristType().getTextureId().withPath(textureName -> "textures/entity/underlings/" + textureName + ".png");
		if(Minecraft.getInstance().getResourceManager().getResource(textureLocation).isEmpty())
		{
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
			float contrast = getContrastModifier(animatable);
			
			poseStack.pushPose();
			
			this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, layerRenderType, bufferSource.getBuffer(layerRenderType),
					partialTick, packedLight, packedOverlay, FastColor.ARGB32.colorFromFloat(1, contrast, contrast, contrast));
			
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