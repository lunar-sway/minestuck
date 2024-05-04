package com.mraof.minestuck.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.world.lands.GristLayerInfo;
import com.mraof.minestuck.world.lands.GristTypeLayer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class GristLayerDebugRender
{
	private static final boolean RENDER = false;
	private static final int RADIUS =  30;
	
	@SubscribeEvent
	public static void onRenderTick(RenderLevelStageEvent event)
	{
		if(RENDER && event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)
		{
			Minecraft minecraft = Minecraft.getInstance();
			
			if(minecraft.level == null || minecraft.player == null)
				return;
			
			MinecraftServer server = minecraft.getSingleplayerServer();
			if(server == null)
				return;
			
			ServerLevel level = server.getLevel(minecraft.level.dimension());
			
			if(level == null)
				return;
			
			GristLayerInfo.get(level).ifPresent(gristLayerInfo -> {
				renderGristLayer(event.getPoseStack(), event.getCamera(), minecraft.player, gristLayerInfo.getCommonGristLayer(), 165);
				renderGristLayer(event.getPoseStack(), event.getCamera(), minecraft.player, gristLayerInfo.getUncommonGristLayer(), 150);
				renderGristLayer(event.getPoseStack(), event.getCamera(), minecraft.player, gristLayerInfo.getAnyGristLayer(), 135);
			});
		}
	}
	
	private static void renderGristLayer(PoseStack stack, Camera camera, Player player, GristTypeLayer layer, int y)
	{
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		
		for(int x = player.getBlockX() - RADIUS; x < player.getBlockX() + RADIUS; x++)
		{
			for(int z = player.getBlockZ() - RADIUS; z < player.getBlockZ() + RADIUS; z++)
			{
				GristType type = layer.getTypeAt(x, z);
				RenderSystem.setShaderTexture(0, type.getIcon());
				stack.pushPose();
				stack.translate(x - camera.getPosition().x, y - camera.getPosition().y, z - camera.getPosition().z);
				Matrix4f pose = stack.last().pose();
				
				BufferBuilder render = Tesselator.getInstance().getBuilder();
				render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				render.vertex(pose, 0, 0, 1).uv(0, 1).endVertex();
				render.vertex(pose, 1, 0, 1).uv(1, 1).endVertex();
				render.vertex(pose, 1, 0, 0).uv(1, 0).endVertex();
				render.vertex(pose, 0, 0, 0).uv(0, 0).endVertex();
				Tesselator.getInstance().end();
				stack.popPose();
			}
		}
		
	}
}
