package com.mraof.minestuck.client.gui.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.List;

/**
 * A class that handles Grist Notification popups whenever you gain or lose grist.
 * Utilizes vanilla Minecraft's Toasts system, which is what the advancement and recipe popups use.
 * @author Caldw3ll
 */
public class GristToast implements Toast
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("minestuck", "textures/gui/toasts.png");
	
	private static final long DISPLAY_TIME = 5000L;
	private static final float SCALE_X = 0.6F;
	private static final float SCALE_Y = 0.6F;
	private GristType type;
	private long difference;
	private GristHelper.EnumSource source;
	private boolean increase;
	private long lastChanged;
	private boolean changed;
	
	public GristToast (GristType pType, long pDifference, GristHelper.EnumSource pSource, boolean pIncrease)
	{
		this.type = pType;
		this.difference = pDifference;
		this.source = pSource;
		this.increase = pIncrease;
	}
	
	public int width() { return Mth.floor(160.0F * SCALE_X); }
	
	public int height() { return Mth.floor(32.0F * SCALE_Y); }
	
	/**
	 *
	 * @param pTimeSinceLastVisible time in milliseconds
	 */
	public Toast.Visibility render(PoseStack pPoseStack, ToastComponent pToastComponent, long pTimeSinceLastVisible)
	{
		if (this.changed)
		{
			this.lastChanged = pTimeSinceLastVisible;
			this.changed = false;
		}
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		//Scales the width and height of the toast's background.
		posestack.scale(SCALE_X, SCALE_Y, 1.0F);
		RenderSystem.applyModelViewMatrix();
		posestack.popPose();
		
		//draws the background.
		pToastComponent.blit(pPoseStack, 0, 0, 0, 0, 160, 32);
		
		//Draws the icon indication the grist toast's "source".
		switch (this.source)
		{
			case CLIENT:
				pToastComponent.blit(pPoseStack, 5, 5, 196, 0, 20, 20);
				break;
			case SERVER:
				pToastComponent.blit(pPoseStack, 5, 5, 196, 20, 20, 20);
				break;
			case SENDGRIST:
				pToastComponent.blit(pPoseStack, 5, 5, 216, 0, 20, 20);
				break;
			case CONSOLE:
				pToastComponent.blit(pPoseStack, 5, 5, 216, 20, 20, 20);
				break;
		}
		
		//Changes the colors depending on whether the grist amount is gained or lost.
		if(this.increase)
		{
			pToastComponent.blit(pPoseStack, 0, 17, 176, 20, 20, 20);
			pToastComponent.getMinecraft().font.draw(pPoseStack, this.type.getDisplayName(), 30.0F, 7.0F, 0x06c31c);
			pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent("+" + String.valueOf(this.difference)), 30.0F, 18.0F, 0x000000);
		} else
		{
			pToastComponent.blit(pPoseStack, 0, 17, 176, 0, 20, 20);
			pToastComponent.getMinecraft().font.draw(pPoseStack, this.type.getDisplayName(), 30.0F, 7.0F, 0xff0000);
			pToastComponent.getMinecraft().font.draw(pPoseStack, new TextComponent("-" + String.valueOf(this.difference)), 30.0F, 18.0F, 0x000000);
		}
		
		posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		
		//scale for the grist-icon since it doesn't inherit the background's scale for some reason.
		posestack.scale(0.8F, 0.8F, 1.0F);
		
		RenderSystem.applyModelViewMatrix();
		
		//draws the grist icon.
		this.drawIcon(100, 4, type.getIcon());
		
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		
		return pTimeSinceLastVisible - this.lastChanged >= DISPLAY_TIME ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
		
		
	}
	
	//modified version of drawIcon() from MinestuckScreen.java
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
	
	//adds pDifference to the toast's current grist value.
	private void addGrist(long pDifference)
	{
		
		this.difference += pDifference;
		this.changed = true;
		
	}
	
	//Updates the grist value of any existing toasts, and if there aren't any of the same type, it instantiates a new one. NEVER use addToast() directly when adding a grist toast, ALWAYS use this method.
	public static void addOrUpdate(ToastComponent pToastGui, GristType pType, long pDifference, GristHelper.EnumSource pSource, boolean pIncrease)
	{
		
		//try to find an existing toast with the same properties as the one being added.
		GristToast gristToast = pToastGui.getToast(GristToast.class, pType.getTranslationKey() + pSource + String.valueOf(pIncrease));
		
		//if none can be found, add a new toast. If an existing toast *can* be found, update its grist amount using pDifference.
		if (gristToast == null)
			pToastGui.addToast(new GristToast(pType, pDifference, pSource, pIncrease));
		else
			gristToast.addGrist(pDifference);
	}
	
	public static void sendGristMessage(GristSet set, GristHelper.EnumSource source, boolean increase)
	{
		
		List<GristAmount> reqs = set.getAmounts();
		for(GristAmount pairs : reqs)
		{
			//the pair has to be split into two new variables because Map.Entry is immutable.
			GristType type = pairs.getType();
			long difference = pairs.getAmount();
			
			//ALWAYS use addOrUpdate(), and not addToast, or else grist toasts won't leave a running tally of the amount.
			if (difference >= 0)
				GristToast.addOrUpdate(Minecraft.getInstance().getToasts(), type, difference, source, increase);
			else
				GristToast.addOrUpdate(Minecraft.getInstance().getToasts(), type, Math.abs(difference), source, !(increase));
		}
	}
	
	//returns a single token that contains the grist type, the source, and whether its positive or negative. Used with getToast() to retrieve toasts that can be merged.
	public String getToken()
	{
		return this.type.getTranslationKey() + this.source + String.valueOf(this.increase);
	}
}