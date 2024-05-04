package com.mraof.minestuck.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.entity.DecoyEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
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
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
				new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
				context.getModelManager()));
		this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
		this.addLayer(new ArrowLayer<>(context, this));
	}
	
	@Override
	public void render(DecoyEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn)
	{
		PlayerSkin skin = getSkin(entityIn.getPlayerID());
		model = skin.model() == PlayerSkin.Model.SLIM ? SLIM_MODEL : DEFAULT_MODEL;
		
		super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getTextureLocation(DecoyEntity entity)
	{
		return getSkin(entity.getPlayerID()).texture();
	}
	
	private static PlayerSkin getSkin(UUID playerID)
	{
		ClientPacketListener packetListener = Minecraft.getInstance().getConnection();
		PlayerInfo info = packetListener != null ? packetListener.getPlayerInfo(playerID) : null;
		return info != null ? info.getSkin() : DefaultPlayerSkin.get(playerID);
	}
}
