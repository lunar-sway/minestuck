package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.model.MiniMeteorModel;
import com.mraof.minestuck.entity.MiniMeteorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MiniMeteorRenderer extends GeoEntityRenderer<MiniMeteorEntity>
{
	private static final ResourceLocation[] TEXTURES = {Minestuck.id("textures/entity/meteor_0.png"), Minestuck.id("textures/entity/meteor_1.png"), Minestuck.id("textures/entity/meteor_2.png")};
	
	public MiniMeteorRenderer(EntityRendererProvider.Context context)
	{
		super(context, new MiniMeteorModel());
	}
	
	@Override
	public ResourceLocation getTextureLocation(MiniMeteorEntity entity)
	{
		int ticksPerFrame = 2;
		int frame = (entity.tickCount / ticksPerFrame) % TEXTURES.length;
		return TEXTURES[frame];
	}
	
	@Override
	protected void applyRotations(MiniMeteorEntity entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick, float nativeScale)
	{
	}
	
	@Override
	public void preRender(PoseStack poseStack, MiniMeteorEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour)
	{
		poseStack.scale(1.2f, 1.2f, 1.2f);
		
		poseStack.mulPose(Axis.YP.rotationDegrees(-180));
		
		super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}
	
	@Override
	public void render(MiniMeteorEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, 0xF000F0);
	}
}
