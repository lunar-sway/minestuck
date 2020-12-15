package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mraof.minestuck.entity.DecoyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class DecoyRenderer extends MobRenderer<DecoyEntity, PlayerModel<DecoyEntity>>
{
	private final PlayerModel<DecoyEntity> DEFAULT_MODEL = new PlayerModel<>(0F, false);
	private final PlayerModel<DecoyEntity> SLIM_MODEL = new PlayerModel<>(0F, true);
	
	public DecoyRenderer(EntityRendererManager manager)
	{
		super(manager, new PlayerModel<>(0F, false), 0F);
		this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
		this.addLayer(new HeldItemLayer<>(this));
		this.addLayer(new ArrowLayer<>(this));
	}
	
	@Override
	public void render(DecoyEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		NetworkPlayerInfo info = getPlayerInfo(entityIn.getPlayerID());
		entityModel = getModelForType(info != null ? info.getSkinType() : DefaultPlayerSkin.getSkinType(entityIn.getPlayerID()));
		
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getEntityTexture(DecoyEntity entity)
	{
		NetworkPlayerInfo info = getPlayerInfo(entity.getPlayerID());
		return info != null ? info.getLocationSkin() : DefaultPlayerSkin.getDefaultSkin(entity.getPlayerID());
	}
	
	private PlayerModel<DecoyEntity> getModelForType(String type)
	{
		return type.equals("slim") ? SLIM_MODEL : DEFAULT_MODEL;
	}
	
	private static NetworkPlayerInfo getPlayerInfo(UUID id)
	{
		ClientPlayNetHandler playNetHandler = Minecraft.getInstance().getConnection();
		return playNetHandler != null ? playNetHandler.getPlayerInfo(id) : null;
	}
}