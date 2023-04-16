package com.mraof.minestuck.client.gui.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mraof.minestuck.alchemy.GristAmount;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.GristToastPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A class that handles Grist Notification popups whenever you gain or lose grist.
 * Utilizes vanilla Minecraft's Toasts system, which is what the advancement and recipe popups use.
 * @author Caldw3ll
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GristToast implements Toast
{
	private static final ResourceLocation TEXTURE = new ResourceLocation("minestuck", "textures/gui/toasts.png");
	
	
	private static final int GRIST_VIAL_OUTLINE_OFFSETX = 29;
	private static final int GRIST_VIAL_OUTLINE_OFFSETY = 13;
	private static final int GRIST_VIAL_OUTLINE_WIDTH = 101;
	private static final int GRIST_VIAL_OUTLINE_HEIGHT = 10;
	
	private static final int GRIST_VIAL_INSIDE_OFFSETX = 31;
	private static final int GRIST_VIAL_INSIDE_OFFSETY = 15;
	private static final int GRIST_VIAL_INSIDE_WIDTH = 97;
	private static final int GRIST_VIAL_INSIDE_HEIGHT = 6;
	
	private static final long DISPLAY_TIME = 5000L;
	
	// At the time of writing (mc1.19.2), vanilla does not properly support toast sizes smaller than the usual toast size7
	private static final float SCALE_X = 1;//0.6F;
	private static final float SCALE_Y = 1;//0.6F;
	
	private final GristType type;
	private long difference;
	private long cacheLimit;
	private long gristCache;
	private final GristHelper.EnumSource source;
	private final boolean increase;
	
	private long lastChanged;
	private boolean changed;
	private int animationTimer;
	
	public GristToast (GristType pType, long pDifference, GristHelper.EnumSource pSource, boolean pIncrease, long pCacheLimit, long pGristCache)
	{
		this.type = pType;
		this.difference = pDifference;
		this.source = pSource;
		this.increase = pIncrease;
		this.cacheLimit = pCacheLimit;
		this.gristCache = pGristCache;
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
			if(this.gristCache >= this.cacheLimit)
				this.animationTimer = 20;
			else
				this.animationTimer = 0;
			
			this.lastChanged = pTimeSinceLastVisible;
			this.changed = false;
		}
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		//Scales the width and height of the toast's background.
		PoseStack posestack = RenderSystem.getModelViewStack();
		posestack.pushPose();
		//Scales the width and height of the toast's background.
		posestack.scale(SCALE_X, SCALE_Y, 1.0F);
		RenderSystem.applyModelViewMatrix();
		
		//draws the background.
		pToastComponent.blit(pPoseStack, 0, 0, 0, 0, 160, 32);
		
		//Draws the icon indication the grist toast's "source".
		switch(this.source)
		{
			case CLIENT -> pToastComponent.blit(pPoseStack, 133, 7, 196, 0, 20, 20);
			case SERVER -> pToastComponent.blit(pPoseStack, 133, 7, 196, 20, 20, 20);
			case SENDGRIST -> pToastComponent.blit(pPoseStack, 133	, 7, 216, 0, 20, 20);
			case CONSOLE -> pToastComponent.blit(pPoseStack, 133, 7, 216, 20, 20, 20);
		}
		
		posestack.pushPose();
		posestack.scale(4f/3f, 4f/3f, 1.0f);
		RenderSystem.applyModelViewMatrix();
		this.drawIcon(5, 4, type.getIcon()); //draws the grist icon.
		
		posestack.popPose();
		RenderSystem.applyModelViewMatrix();
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		//Basically determines the y coord of the torrent icon when bumping up and down.
		int animationOffset = (this.animationTimer/2) <= 5 ? (this.animationTimer/2)%5 : 5 - ((this.animationTimer/2)%5);
		
		//going to grist gutter
		if(source == GristHelper.EnumSource.GUTTER)
		{
			pToastComponent.blit(pPoseStack, 0, 17 - animationOffset, 176, 60, 20, 20); //grist gutter icon
			pToastComponent.blit(pPoseStack, 40, 24 - animationOffset, 176, 80, 67, 8); //going to gutter message
		}
			
		//draw meter
		double gristFraction = Math.min(1, (double) this.gristCache / this.cacheLimit);
		GuiComponent.fill(pPoseStack,GRIST_VIAL_INSIDE_OFFSETX, GRIST_VIAL_INSIDE_OFFSETY, GRIST_VIAL_INSIDE_OFFSETX + (int)(GRIST_VIAL_INSIDE_WIDTH * gristFraction), GRIST_VIAL_INSIDE_OFFSETY + GRIST_VIAL_INSIDE_HEIGHT, 0xff19B3EF); //the grist bar (has two extra digits in pColor because fill has opacity.
		pToastComponent.blit(pPoseStack, GRIST_VIAL_OUTLINE_OFFSETX, GRIST_VIAL_OUTLINE_OFFSETY, 0, 128, GRIST_VIAL_OUTLINE_WIDTH, GRIST_VIAL_OUTLINE_HEIGHT); //Bar outline
		
		
		drawText(pPoseStack, pToastComponent.getMinecraft().font);
		
		
		if(this.animationTimer > 0)
			this.animationTimer =- 1;
		
		posestack.popPose();
		
		return pTimeSinceLastVisible - this.lastChanged >= DISPLAY_TIME ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
		
		
	}
	
	private void drawText(PoseStack poseStack, Font font)
	{
		int textColor = this.increase ? 0x06c31c : 0xff0000;
		
		//amount being added/subtracted, above the cache bar. If one grist is added it looks like "(+1)"
		font.draw(poseStack, "(%s%s)".formatted(this.increase ? "+" : "-", GuiUtil.addSuffix(this.difference)), 30.0F, 5.0F, textColor);
		
		//ignore if its a grist gutter toast
		if(source != GristHelper.EnumSource.GUTTER)
		{
			//proportion of current cache filled, below the cache bar. If there was no grist with a cache size of 60 and now there is one it looks like "1 / 60"
			String cacheText = GuiUtil.addSuffix(this.gristCache);
			String limitText = " / " + GuiUtil.addSuffix(this.cacheLimit);
			int fullBottomTextWidth = font.width(cacheText) + font.width(limitText);
			if(fullBottomTextWidth <= GRIST_VIAL_INSIDE_WIDTH)
			{
				float xLeft = 31.0F + (GRIST_VIAL_INSIDE_WIDTH - fullBottomTextWidth)/2F;
				font.draw(poseStack, cacheText, xLeft, 24.0F, textColor);
				font.draw(poseStack, limitText, xLeft + font.width(cacheText), 24.0F, 0x000000);
			} else
			{
				font.draw(poseStack, cacheText, 31.0F + (GRIST_VIAL_INSIDE_WIDTH - font.width(cacheText))/2F, 24.0F, textColor);
			}
		}
		
	}
	
	//modified version of drawIcon() from MinestuckScreen.java
	private void drawIcon(int x, int y, ResourceLocation icon)
	{
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
	
	/**
	 * adds difference to the toast's current grist value.
	 */
	private void addGrist(long difference, long newLimit, long newCacheAmount)
	{
		this.difference += difference;
		this.cacheLimit = newLimit;
		this.gristCache = newCacheAmount;
		this.changed = true;
	}
	
	//Updates the grist value of any existing toasts, and if there aren't any of the same type, it instantiates a new one. NEVER use addToast() directly when adding a grist toast, ALWAYS use this method.
	public static void addOrUpdate(ToastComponent toastGui, GristType type, long difference, GristHelper.EnumSource source, boolean isIncrease, long cacheLimit, long cacheAmount)
	{
		//try to find an existing toast with the same properties as the one being added.
		GristToast gristToast = toastGui.getToast(GristToast.class, new Token(type, source, isIncrease));
		
		//if none can be found, add a new toast. If an existing toast *can* be found, update its grist amount using difference.
		if (gristToast == null)
			toastGui.addToast(new GristToast(type, difference, source, isIncrease, cacheLimit, cacheAmount));
		else
			gristToast.addGrist(difference, cacheLimit, cacheAmount);
	}
	
	private record Token(GristType type, GristHelper.EnumSource source, boolean isIncrease)
	{}
	
	public static void handlePacket(GristToastPacket packet)
	{
		ClientPlayerData.ClientCache cache = ClientPlayerData.getGristCache(packet.isCacheOwner() ? ClientPlayerData.CacheSource.PLAYER : ClientPlayerData.CacheSource.EDITMODE);
		GristHelper.EnumSource source = packet.source();
		ToastComponent toasts = Minecraft.getInstance().getToasts();
		
		for(GristAmount pair : packet.gristValue().getAmounts())
		{
			//the pair has to be split into two new variables because Map.Entry is immutable.
			GristType type = pair.getType();
			long difference = pair.getAmount();
			long total = cache.set().getGrist(type);
			
			if(difference == 0)
				continue;
			
			//ALWAYS use addOrUpdate(), and not addToast, or else grist toasts won't leave a running tally of the amount.
			if (difference >= 0)
				GristToast.addOrUpdate(toasts, type, difference, source, true, cache.limit(), total);
			else
				GristToast.addOrUpdate(toasts, type, Math.abs(difference), source, false, cache.limit(), total);
		}
	}
	
	//returns a single token that contains the grist type, the source, and whether its positive or negative. Used with getToast() to retrieve toasts that can be merged.
	@Override
	public Token getToken()
	{
		return new Token(this.type, this.source, this.increase);
	}
}