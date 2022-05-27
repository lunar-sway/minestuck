package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.UnderlingModel;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * A renderer that applies the correct grist texture/color to the underlings
 */
public class UnderlingRenderer<T extends UnderlingEntity> extends GeoEntityRenderer<T>
{
	public UnderlingRenderer(EntityRendererManager renderManager)
	{
		// this renderer does two simple things :
		super(renderManager, new UnderlingModel<>()); // render the entity with the base layer merged with a grist texture
		this.addLayer(new UnderlingDetailsLayer(this)); // render a second layer with details (eyes, mouth, etc)
	}
	
	@Override
	protected float getDeathMaxRotation(T entityLivingBaseIn)
	{
		return 0;
	}
	
	@Override
	public Color getRenderColor(T animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn)
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
		String textureName = entity.getGristType().getRegistryName().getPath();
		ResourceLocation resource = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entity) + "_" + textureName + ".png");
		
		// the texture manager will cache the computed textures so theyre effectively computed once (at least in theory)
		if(Minecraft.getInstance().textureManager.getTexture(resource) == null)
		{
			try
			{
				IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
				NativeImage base = SimpleTexture.TextureData.load(resourceManager, super.getTextureLocation(entity)).getImage();
				NativeImage texture = SimpleTexture.TextureData.load(resourceManager, getGristTexture(entity)).getImage();
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
				
				// save the computed texture to the texture manager's cache
				Minecraft.getInstance().textureManager.register(resource, new DynamicTexture(computed));
			} catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		return resource;
	}
	
	private ResourceLocation getGristTexture(T entity)
	{
		String textureName = entity.getGristType().getRegistryName().getPath();
		if(!textureName.equals("marble") && !textureName.equals("diamond"))
		{
			return this.modelProvider.getTextureLocation(entity); //default
		}
		return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + textureName + ".png");
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
		public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
		{
			RenderType renderType = RenderType.armorCutoutNoCull(new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entityLivingBaseIn) + "_details.png"));
			float color = getContrastModifier(entityLivingBaseIn);
			matrixStackIn.pushPose();
			
			GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entityLivingBaseIn));
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