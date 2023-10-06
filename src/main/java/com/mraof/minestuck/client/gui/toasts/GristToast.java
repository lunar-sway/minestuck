package com.mraof.minestuck.client.gui.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.network.GristToastPacket;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.resources.ResourceLocation;

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
	
	
	private final GristType gristType;
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
		this.gristType = pType;
		this.difference = pDifference;
		this.source = pSource;
		this.increase = pIncrease;
		this.cacheLimit = pCacheLimit;
		this.gristCache = pGristCache;
	}
	
	/**
	 *
	 * @param pTimeSinceLastVisible time in milliseconds
	 */
	@Override
	public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent pToastComponent, long pTimeSinceLastVisible)
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
		
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		//draws the background.
		guiGraphics.blit(TEXTURE, 0, 0, 0, 0, 160, 32);
		
		//Draws the icon indication the grist toast's "source".
		switch(this.source)
		{
			case CLIENT -> guiGraphics.blit(TEXTURE, 133, 7, 196, 0, 20, 20);
			case SERVER -> guiGraphics.blit(TEXTURE, 133, 7, 196, 20, 20, 20);
			case SENDGRIST -> guiGraphics.blit(TEXTURE, 133	, 7, 216, 0, 20, 20);
			case CONSOLE -> guiGraphics.blit(TEXTURE, 133, 7, 216, 20, 20, 20);
		}
		
		PoseStack posestack = guiGraphics.pose();
		posestack.pushPose();
		posestack.scale(4f/3f, 4f/3f, 1.0f);
		
		guiGraphics.blit(this.gristType.getIcon(), 5, 4, 0, 0, 0, 16, 16, 16, 16);
		
		posestack.popPose();
		
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		//Basically determines the y coord of the torrent icon when bumping up and down.
		int animationOffset = (this.animationTimer/2) <= 5 ? (this.animationTimer/2)%5 : 5 - ((this.animationTimer/2)%5);
		
		//going to grist gutter
		if(source == GristHelper.EnumSource.GUTTER)
		{
			guiGraphics.blit(TEXTURE, 0, 17 - animationOffset, 176, 60, 20, 20); //grist gutter icon
			guiGraphics.blit(TEXTURE, 40, 24 - animationOffset, 176, 80, 67, 8); //going to gutter message
		}
			
		//draw meter
		double gristFraction = Math.min(1, (double) this.gristCache / this.cacheLimit);
		guiGraphics.fill(GRIST_VIAL_INSIDE_OFFSETX, GRIST_VIAL_INSIDE_OFFSETY, GRIST_VIAL_INSIDE_OFFSETX + (int)(GRIST_VIAL_INSIDE_WIDTH * gristFraction), GRIST_VIAL_INSIDE_OFFSETY + GRIST_VIAL_INSIDE_HEIGHT, 0xff19B3EF); //the grist bar (has two extra digits in pColor because fill has opacity.
		guiGraphics.blit(TEXTURE, GRIST_VIAL_OUTLINE_OFFSETX, GRIST_VIAL_OUTLINE_OFFSETY, 0, 128, GRIST_VIAL_OUTLINE_WIDTH, GRIST_VIAL_OUTLINE_HEIGHT); //Bar outline
		
		
		drawText(guiGraphics, pToastComponent.getMinecraft().font);
		
		
		if(this.animationTimer > 0)
			this.animationTimer =- 1;
		
		return pTimeSinceLastVisible - this.lastChanged >= DISPLAY_TIME ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
		
		
	}
	
	private void drawText(GuiGraphics guiGraphics, Font font)
	{
		int textColor = this.increase ? 0x06c31c : 0xff0000;
		
		//amount being added/subtracted, above the cache bar. If one grist is added it looks like "(+1)"
		guiGraphics.drawString(font, "(%s%s)".formatted(this.increase ? "+" : "-", GuiUtil.addSuffix(this.difference)), 30.0F, 5.0F, textColor, false);
		
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
				guiGraphics.drawString(font, cacheText, xLeft, 24.0F, textColor, false);
				guiGraphics.drawString(font, limitText, xLeft + font.width(cacheText), 24.0F, 0x000000, false);
			} else
			{
				guiGraphics.drawString(font, cacheText, 31.0F + (GRIST_VIAL_INSIDE_WIDTH - font.width(cacheText))/2F, 24.0F, textColor, false);
			}
		}
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
		
		for(GristAmount pair : packet.gristValue().asAmounts())
		{
			//the pair has to be split into two new variables because Map.Entry is immutable.
			GristType type = pair.type();
			long difference = pair.amount();
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
		return new Token(this.gristType, this.source, this.increase);
	}
}