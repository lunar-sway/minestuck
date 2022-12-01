package com.mraof.minestuck.client.gui.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Map;
import java.util.Map.Entry;

@OnlyIn(Dist.CLIENT)
public class GristToast implements Toast
{
	ResourceLocation TEXTURE = new ResourceLocation("minestuck", "textures/gui/toasts.png");
	
	private static final long DISPLAY_TIME = 5000L;
	private GristType type;
	private long difference;
	private String source;
	private boolean increase;
	private long lastChanged;
	private boolean changed;
	
	public GristToast (GristType pType, long pDifference, String pSource, boolean pIncrease) {
		this.type = pType;
		this.difference = pDifference;
		this.source = pSource;
		this.increase = pIncrease;
	}
	
	public String getToken() {
		return this.type.getTranslationKey() + this.source + String.valueOf(this.increase);
	}
	
	public int width() { return 96; }
	
	public int height() { return 19; }
	
	/**
	 *
	 * @param pTimeSinceLastVisible time in milliseconds
	 */
	public Toast.Visibility render(PoseStack pPoseStack, ToastComponent pToastComponent, long pTimeSinceLastVisible) {
		if (this.changed) {
			this.lastChanged = pTimeSinceLastVisible;
			this.changed = false;
		}
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.scale(0.6F, 0.6F, 1.0F);
		RenderSystem.applyModelViewMatrix();
		posestack.popPose();
		
		pToastComponent.blit(pPoseStack, 0, 0, 0, 0, 160, 32);
		
		if (this.source.equals("Client"))
			pToastComponent.blit(pPoseStack, 5, 5, 196, 0, 20, 20);
		if (this.source.equals("Server"))
			pToastComponent.blit(pPoseStack, 5, 5, 196, 20, 20, 20);
		if (this.source.equals("SendGrist"))
			pToastComponent.blit(pPoseStack, 5, 5, 216, 0, 20, 20);
		
		if(this.increase == true)
		{
			pToastComponent.blit(pPoseStack, 0, 17, 176, 20, 20, 20);
			pToastComponent.getMinecraft().font.draw(pPoseStack, this.type.getDisplayName(), 30.0F, 7.0F, 0x06c31c);
			pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent("+" + String.valueOf(this.difference)), 30.0F, 18.0F, 0x000000);
		} else {
			pToastComponent.blit(pPoseStack, 0, 17, 176, 0, 20, 20);
			pToastComponent.getMinecraft().font.draw(pPoseStack, this.type.getDisplayName(), 30.0F, 7.0F, 0xff0000);
			pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent("-" + String.valueOf(this.difference)), 30.0F, 18.0F, 0x000000);
		}
		
		posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.scale(0.8F, 0.8F, 1.0F);
		RenderSystem.applyModelViewMatrix();
		this.drawIcon(100, 4, type.getIcon());
		posestack.popPose();
		
		RenderSystem.applyModelViewMatrix();
		
		return pTimeSinceLastVisible - this.lastChanged >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
		
		
	}
	
	private void drawIcon(int x, int y, ResourceLocation icon)
	{
		if(icon == null || Minecraft.getInstance() == null)
			return;
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, icon);
		
		float scale = (float) 1 / 16;
		
		int iconX = 16;
		int iconY = 16;
		int iconU = 0;
		int iconV = 0;
		
		BufferBuilder render = Tesselator.getInstance().getBuilder();
		render.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		render.vertex(x, y + iconY, 0D).uv((iconU) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y + iconY, 0D).uv((iconU + iconX) * scale, (iconV + iconY) * scale).endVertex();
		render.vertex(x + iconX, y, 0D).uv((iconU + iconX) * scale, (iconV) * scale).endVertex();
		render.vertex(x, y, 0D).uv((iconU) * scale, (iconV) * scale).endVertex();
		Tesselator.getInstance().end();
	}
	
	private void addGrist(long pDifference) {
		
		this.difference += pDifference;
		this.changed = true;
		
	}
	
	public static void addOrUpdate(ServerPlayer player, ToastComponent pToastGui, GristType pType, long pDifference, String pSource, boolean pIncrease) {
		GristToast gristToast = pToastGui.getToast(GristToast.class, pType.getTranslationKey() + pSource + String.valueOf(pIncrease));
		
		
		if (gristToast == null) {
			pToastGui.addToast(new GristToast(pType, pDifference, pSource, pIncrease));
		} else {
			gristToast.addGrist(pDifference);
		}
		
	}
}