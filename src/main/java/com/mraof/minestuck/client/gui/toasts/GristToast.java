package com.mraof.minestuck.client.gui.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.RecipeToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Map;
import java.util.Map.Entry;

@OnlyIn(Dist.CLIENT)
public class GristToast implements Toast
{
	private static final long DISPLAY_TIME = 5000L;
	private Entry<GristType, Long> difference;
	private String source;
	private long lastChanged;
	private boolean changed;
	
	public GristToast (Entry<GristType, Long> pDifference, String pSource) {
		this.difference = pDifference;
		this.source = pSource;
	}
	
	public GristType getToken() {
		return this.difference.getKey();
	}
	
	public int width() { return 100; }
	
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
		pToastComponent.blit(pPoseStack, 0, 0, 0, 0, this.width(), this.height());
		if(this.source.equals("Client"))
			pToastComponent.getMinecraft().font.draw(pPoseStack, difference.getKey().getDisplayName(), 30.0F, 7.0F, 0xffff55);
		else
			pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent(this.source), 30.0F, 7.0F, 0xffff55);
		pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent(String.valueOf(difference.getValue())), 30.0F, 18.0F, 0xffffff);
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		posestack.scale(1.0F, 1.0F, 1.0F);
		RenderSystem.applyModelViewMatrix();
		this.drawIcon(8, 8, difference.getKey().getIcon());
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
	
	private void addGrist(Entry<GristType, Long> pDifference) {
		if(this.difference.getKey().equals(pDifference.getKey())) {
			this.difference.setValue(this.difference.getValue() + pDifference.getValue());
			this.changed = true;
		} else {
			this.difference = pDifference;
			this.changed = true;
		}
	}
	
	public static void addOrUpdate(ToastComponent pToastGui, Entry<GristType, Long> pDifference, String pSource) {
		GristToast gristToast = pToastGui.getToast(GristToast.class, pDifference.getKey());
		if (gristToast == null || (!gristToast.difference.getKey().equals(pDifference.getKey()) && pSource.equals(gristToast.source))) {
			pToastGui.addToast(new GristToast(pDifference, pSource));
		} else {
			gristToast.addGrist(pDifference);
		}
		
	}
}
