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
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

import java.io.IOException;

public class UnderlingRenderer<T extends UnderlingEntity> extends GeoEntityRenderer<T> {
    public UnderlingRenderer(EntityRendererManager renderManager) {
        super(renderManager, new UnderlingModel<>());
        this.addLayer(new UnderlingDetailsLayer(this));
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }

    @Override
    public Color getRenderColor(T animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn) {
        return Color.ofOpaque(animatable.getGristType().getColor());
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
//        if (instance.getGristType() == MARBLE.get()) {
//            return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/marble.png");
//        }
//        if (instance.getGristType() == DIAMOND.get()) {
//            return new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/diamond.png");
//        }
//        return super.getTextureLocation(instance);

        String textureName = "marble";
        ResourceLocation resource = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entity) + "_" + textureName + ".png");
        if (Minecraft.getInstance().textureManager.getTexture(resource) == null) {
            ResourceLocation textureResource = new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + textureName + ".png");
            try {
                NativeImage base = SimpleTexture.TextureData.load(Minecraft.getInstance().getResourceManager(), super.getTextureLocation(entity)).getImage();
                NativeImage texture = SimpleTexture.TextureData.load(Minecraft.getInstance().getResourceManager(), textureResource).getImage();
                NativeImage computed = new NativeImage(base.getWidth(), base.getHeight(), false);

                for (int i = 0; i < base.getWidth(); i++) {
                    for (int j = 0; j < base.getHeight(); j++) {
                        if (NativeImage.getA(base.getPixelRGBA(i, j)) == 0) {
                            computed.setPixelRGBA(i, j, 0);
                        } else {
                            computed.setPixelRGBA(i, j, texture.getPixelRGBA(i % texture.getWidth(), j % texture.getHeight()));
                        }
                    }
                }

                Minecraft.getInstance().textureManager.register(resource, new DynamicTexture(computed));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return resource;
    }

    public class UnderlingDetailsLayer extends GeoLayerRenderer<T> {
        public UnderlingDetailsLayer(IGeoRenderer<T> entityRendererIn) {
            super(entityRendererIn);
        }

        @Override
        public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            RenderType renderType = RenderType.armorCutoutNoCull(new ResourceLocation(Minestuck.MOD_ID, "textures/entity/underlings/" + UnderlingModel.getName(entityLivingBaseIn) + "_details.png"));
            matrixStackIn.pushPose();

            GeoModel model = modelProvider.getModel(modelProvider.getModelLocation(entityLivingBaseIn));
            this.getRenderer().render(model, entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

            matrixStackIn.popPose();
        }
    }
}