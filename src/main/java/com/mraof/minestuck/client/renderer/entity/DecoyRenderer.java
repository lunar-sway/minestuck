package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.entity.DecoyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class DecoyRenderer extends MobRenderer<DecoyEntity, PlayerModel<DecoyEntity>>
{
	private final PlayerModel<DecoyEntity> DEFAULT_MODEL;
	private final PlayerModel<DecoyEntity> SLIM_MODEL;
	
	public DecoyRenderer(EntityRendererProvider.Context context)
	{
		super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0F);
		DEFAULT_MODEL = getModel();
		SLIM_MODEL = new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
		this.addLayer(new HumanoidArmorLayer<>(this,
				new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
				new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR))));
		this.addLayer(new ItemInHandLayer<>(this));
		this.addLayer(new ArrowLayer<>(context, this));
	}
	
	@Override
	public void render(DecoyEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn)
	{
		PlayerInfo info = getPlayerInfo(entityIn.getPlayerID());
		model = getModelForType(info != null ? info.getModelName() : DefaultPlayerSkin.getSkinModelName(entityIn.getPlayerID()));
		
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(DecoyEntity entity)
	{
		PlayerInfo info = getPlayerInfo(entity.getPlayerID());
		return info != null ? info.getSkinLocation() : DefaultPlayerSkin.getDefaultSkin(entity.getPlayerID());
	}
	
	private PlayerModel<DecoyEntity> getModelForType(String type)
	{
		return type.equals("slim") ? SLIM_MODEL : DEFAULT_MODEL;
	}
	
	private static PlayerInfo getPlayerInfo(UUID id)
	{
		ClientPacketListener packetListener = Minecraft.getInstance().getConnection();
		return packetListener != null ? packetListener.getPlayerInfo(id) : null;
	}
}