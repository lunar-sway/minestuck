package com.mraof.minestuck.client.gui.playerStats;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.Rungs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author Kirderf1
 */
public class EcheladderScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.echeladder";
	public static final String ATTACK = "minestuck.echeladder.attack";
	public static final String HEALTH = "minestuck.echeladder.health";
	public static final String CACHE = "minestuck.echeladder.cache";
	public static final String DAMAGE_UNDERLING = "minestuck.echeladder.damage_underling";
	public static final String DAMAGE_UNDERLING_INCREASE = "minestuck.echeladder.damage_underling.increase";
	public static final String PROTECTION_UNDERLING = "minestuck.echeladder.protection_underling";
	public static final String PROTECTION_UNDERLING_INCREASE = "minestuck.echeladder.protection_underling.increase";
	
	private static final ResourceLocation guiEcheladder = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/echeladder.png");
	
	
	private static final int LADDER_X_OFFSET = 163, ladderYOffset = 25;
	private static final int ROWS = 12;
	private static final int RUNG_Y = 14;
	
	private static final int GREY = 0x404040;
	private static final int BLUE = 0x0094FF;
	
	private static final int TIME_BEFORE_ANIMATION = 10, TIME_BEFORE_NEXT = 16, TIME_FOR_RUNG = 4, TIME_FOR_SHOW_ONLY = 65;
	private static final int TIME_TILL_NEXT = TIME_BEFORE_NEXT + TIME_FOR_RUNG;
	
	private final int maxScroll;
	private int scrollIndex;
	private boolean isScrolling;
	
	public static int lastRung = -1;    //The current rung last time the gui was opened. Used to determine which rung to display increments from next time the gui is opened
	public static int animatedRung;    //The rung animated to or the latest to be animated
	private final int finalRung;
	private boolean showLastRung = true;
	private int fromRung;    //First rung to display increments from; (actually the one right before that one)
	private int animationCycle;    //Ticks left on the animation cycle
	private int animatedRungs;    //The amount of rungs to animate
	
	public EcheladderScreen()
	{
		super(Component.translatable(TITLE));
		guiWidth = 250;
		guiHeight = 202;
		
		finalRung = Rungs.finalRung();
		
		maxScroll = (finalRung + 1) * RUNG_Y - 154;
	}
	
	@Override
	public void init()
	{
		super.init();
		scrollIndex = Mth.clamp((ClientPlayerData.getRung() - 8) * RUNG_Y, 0, maxScroll);
		animatedRung = Math.max(animatedRung, lastRung);    //If you gain a rung while the gui is open, the animated rung might get higher than the lastRung. Otherwise they're always the same value.
		fromRung = lastRung;
		lastRung = ClientPlayerData.getRung();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
	{
		updateScrollAndAnimation(mouseY);
		
		int speedFactor = MinestuckConfig.CLIENT.echeladderAnimation.get().getSpeed();
		int currentRung;
		
		currentRung = getCurrentRung(speedFactor);
		
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		
		drawTabs(guiGraphics);
		
		drawLadder(guiGraphics, currentRung);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		
		guiGraphics.blit(guiEcheladder, xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		guiGraphics.blit(guiEcheladder, xOffset + 80, yOffset + 42 + (int) (130 * (1 - scrollIndex / (float) maxScroll)), 0, 243, 7, 13);
		
		List<Component> tooltip = drawEffectIconsAndText(guiGraphics, currentRung, mouseX, mouseY);
		
		if(fromRung > currentRung)
		{
			for(int rung = Math.max(fromRung, currentRung - 4); rung <= currentRung; rung++)
			{
				int index = rung - 1 - Math.max(fromRung, currentRung - 4);
				List<Component> newTooltip = drawGainedRungBonuses(guiGraphics, rung, index, mouseX, mouseY);
				if(newTooltip != null)
					tooltip = newTooltip;
			}
		}
		
		drawActiveTabAndOther(guiGraphics, mouseX, mouseY);
		
		if(tooltip != null)
			guiGraphics.renderComponentTooltip(font, tooltip, mouseX, mouseY);
	}
	
	private int getCurrentRung(int speedFactor)
	{
		int currentRung;
		if(animationCycle == 0)
		{
			currentRung = animatedRung;
			if(animatedRung < ClientPlayerData.getRung())
			{
				animatedRungs = ClientPlayerData.getRung() - animatedRung;
				animationCycle = TIME_BEFORE_ANIMATION + getTicksForRungAnimation(animatedRungs) * speedFactor;
				animatedRung = ClientPlayerData.getRung();
			}
		} else
		{
			int rungTicks = getTicksForRungAnimation(animatedRungs) * speedFactor;
			if(animationCycle - rungTicks >= 0)    //Awaiting animation start
				currentRung = animatedRung - animatedRungs;
			else
			{
				if(animatedRungs < 5)    //The animation type where the rungs flicker in
				{
					int rung = (animationCycle / speedFactor + TIME_BEFORE_NEXT) / (TIME_TILL_NEXT);
					currentRung = animatedRung - rung;
					if((animationCycle / speedFactor + TIME_BEFORE_NEXT) % (TIME_TILL_NEXT) >= TIME_BEFORE_NEXT)
						showLastRung = (animationCycle / speedFactor + TIME_BEFORE_NEXT) % (TIME_TILL_NEXT) - TIME_BEFORE_NEXT >= TIME_FOR_RUNG / 2;
				} else    //The animation type where the animation just goes through all rungs
				{
					int rung = animationCycle * animatedRungs / (TIME_FOR_SHOW_ONLY * speedFactor) + 1;
					currentRung = animatedRung - rung;
				}
			}
		}
		return currentRung;
	}
	
	private void drawLadder(GuiGraphics guiGraphics, int currentRung)
	{
		RenderSystem.setShaderColor(1, 1, 1, 1);
		int scroll = scrollIndex % RUNG_Y;
		
		for(int i = 0; i < ROWS; i++)
		{
			guiGraphics.blit(guiEcheladder, xOffset + 90, yOffset + 175 + scroll - i * RUNG_Y, 7, 242, 146, RUNG_Y);
			
			int y = yOffset + 177 + scroll - i * RUNG_Y;
			int rung = scrollIndex / RUNG_Y + i;
			if(rung > (finalRung + 1))
				break;
			
			//TODO broke the animation for previous rung flashing
			int textColor = currentRung >= rung ? textColor(rung) : 0xFFFFFF;
			int backgroundColor = backgroundColor(rung, textColor);
			if(rung <= currentRung - (showLastRung ? 0 : 1))
			{
				//full bar
				guiGraphics.fill(xOffset + 90, y, xOffset + 236, y + 12, backgroundColor);
			} else if(rung == currentRung + 1 && animationCycle == 0)
			{
				//progress bar
				float brightness = (((backgroundColor >> 16) & 0xFF) + ((backgroundColor >> 8) & 0xFF) + (backgroundColor & 0xFF)) / 765F;
				boolean isDark = brightness < 0.2;
				guiGraphics.fill(xOffset + 90, y + 10, xOffset + 90 + (int) (146 * ClientPlayerData.getRungProgress()), y + 12, isDark ? 0xFFFFFFFF : backgroundColor);
			}
			
			String s = I18n.exists("echeladder.rung." + rung) ? I18n.get("echeladder.rung." + rung) : "Rung " + (rung + 1);
			guiGraphics.drawString(font, s, xOffset + LADDER_X_OFFSET - mc.font.width(s) / 2, y + 2, textColor, false);
		}
	}
	
	@Nullable
	private List<Component> drawEffectIconsAndText(GuiGraphics guiGraphics, int currentRung, int mouseX, int mouseY)
	{
		MobEffectTextureManager sprites = this.minecraft.getMobEffectTextures();
		TextureAtlasSprite strengthSprite = sprites.get(MobEffects.DAMAGE_BOOST);
		TextureAtlasSprite healthSprite = sprites.get(MobEffects.HEALTH_BOOST);
		
		int textOffset = xOffset + 24;
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(xOffset + 5, yOffset + 30, 0, 18, 18, strengthSprite);
		guiGraphics.blit(xOffset + 5, yOffset + 84, 0, 18, 18, healthSprite);
		guiGraphics.blit(PlayerStatsScreen.icons, xOffset + 6, yOffset + 139, 48, 64, 16, 16);
		guiGraphics.blit(PlayerStatsScreen.icons, xOffset + 5, yOffset + 7, 238, 16, 18, 18);
		
		String msg = title.getString();
		guiGraphics.drawString(font, msg, xOffset + 168 - mc.font.width(msg) / 2F, yOffset + 12, GREY, false);
		
		int attack = (int) Math.round(100 * (1 + Echeladder.attackBonus(currentRung)));
		guiGraphics.drawString(font, I18n.get(ATTACK), textOffset, yOffset + 30, GREY, false);
		guiGraphics.drawString(font, attack + "%", textOffset + 2, yOffset + 39, BLUE, false);
		
		double health = mc.player.getMaxHealth() / 2;    //10 + Echeladder.healthBoost(currentRung)/2.0;
		guiGraphics.drawString(font, I18n.get(HEALTH), textOffset, yOffset + 84, GREY, false);
		guiGraphics.drawString(font, "+" + String.format(Locale.ROOT, "%.1f", health), textOffset + 2, yOffset + 93, BLUE, false);
		
		guiGraphics.drawString(font, "=", textOffset + 1, yOffset + 12, GREY, false);    //Should this be black, or the same blue as the numbers?
		guiGraphics.drawString(font, String.valueOf(ClientPlayerData.getBoondollars()), textOffset + 3 + mc.font.width("="), yOffset + 12, BLUE, false);
		//guiGraphics.drawString("Rep: " + ClientPlayerData.getConsortReputation(), xOffset + 75 + mc.fontRenderer.getCharWidth('='), yOffset + 12, BLUE);
		
		guiGraphics.drawString(font, I18n.get(CACHE), textOffset, yOffset + 138, GREY, false);
		guiGraphics.drawString(font, String.valueOf(Rungs.getGristCapacity(currentRung)), textOffset + 2, yOffset + 147, BLUE, false);
		
		if(mouseInBounds(mouseY, yOffset + 39, mouseX, textOffset + 2, mc.font.width(attack + "%")))
			return ImmutableList.of(Component.translatable(DAMAGE_UNDERLING), Component.literal(Math.round(attack * Echeladder.getUnderlingDamageModifier(currentRung)) + "%"));
		if(mouseInBounds(mouseY, yOffset + 93, mouseX, textOffset + 2, mc.font.width(String.valueOf(health))))
			return ImmutableList.of(Component.translatable(PROTECTION_UNDERLING), Component.literal(String.format(Locale.ROOT, "%.1f", 100 * Echeladder.getUnderlingProtectionModifier(currentRung)) + "%"));
		return null;
	}
	
	@Nullable
	private List<Component> drawGainedRungBonuses(GuiGraphics guiGraphics, int rung, int index, int mouseX, int mouseY)
	{
		int textColor = textColor(rung);
		int bg = backgroundColor(rung, textColor);
		
		int xMod = 32 * (index % 2);
		int yMod = 15 * (index / 2);
		int minX = xOffset + 5 + xMod;
		int maxX = xOffset + 35 + xMod;
		
		String str = "+" + (Math.round(100 * Echeladder.attackBonus(rung)) - Math.round(100 * Echeladder.attackBonus(rung - 1))) + "%!";
		guiGraphics.fill(minX, yOffset + 50 + yMod, maxX, yOffset + 62 + yMod, bg);
		int strX = xOffset + 20 + xMod - mc.font.width(str) / 2, strY = yOffset + 52 + yMod;
		guiGraphics.drawString(font, str, strX, strY, textColor, false);
		
		double d = (Echeladder.healthBoost(rung) - Echeladder.healthBoost(rung - 1)) / 2D;
		str = "+" + (d == 0 ? d : d + "!");
		guiGraphics.fill(minX, yOffset + 104 + yMod, maxX, yOffset + 116 + yMod, bg);
		strX = xOffset + 20 + xMod - mc.font.width(str) / 2;
		strY = yOffset + 106 + yMod;
		guiGraphics.drawString(font, str, strX, strY, textColor, false);
		
		if(mouseInBounds(mouseY, strY, mouseX, strX, mc.font.width(str)))
		{
			int diff = (int) Math.round(100 * Echeladder.attackBonus(rung) * Echeladder.getUnderlingDamageModifier(rung));
			diff -= Math.round(100 * Echeladder.attackBonus(rung - 1) * Echeladder.getUnderlingDamageModifier(rung - 1));
			return Collections.singletonList(Component.translatable(DAMAGE_UNDERLING_INCREASE, diff));
		}
		
		if(mouseInBounds(mouseY, strY, mouseX, strX, mc.font.width(str)))
		{
			int diff = (int) Math.round(1000 * Echeladder.getUnderlingProtectionModifier(rung - 1));
			diff -= Math.round(1000 * Echeladder.getUnderlingProtectionModifier(rung));
			return Collections.singletonList(Component.translatable(PROTECTION_UNDERLING_INCREASE, diff / 10D));
		}
		
		return null;
	}
	
	private boolean mouseInBounds(int mouseY, int minY, int mouseX, int minX, int xDiff)
	{
		return (mouseY >= minY && mouseY < minY + mc.font.lineHeight) && (mouseX >= minX && mouseX < minX + xDiff);
	}
	
	private int textColor(int rung)
	{
		return finalRung > rung ? Rungs.getTextColor(rung) : 0xFFFFFF;
	}
	
	private int backgroundColor(int rung, int textColor)
	{
		return finalRung > rung ? Rungs.getBackgroundColor(rung) : ~textColor;
	}
	
	private void updateScrollAndAnimation(int ycor)
	{
		if(isScrolling)
		{
			scrollIndex = (int) (maxScroll * (ycor - yOffset - 179) / -130F);
			scrollIndex = Mth.clamp(scrollIndex, 0, maxScroll);
		}
		
		if(animationCycle > 0)
			if(MinestuckConfig.CLIENT.echeladderAnimation.get() != MinestuckConfig.AnimationSpeed.NOTHING)
				animationCycle--;
			else animationCycle = 0;
	}
	
	private int getTicksForRungAnimation(int rungs)
	{
		if(rungs < 5) return TIME_FOR_RUNG + (rungs - 1) * (TIME_TILL_NEXT);
		else return TIME_FOR_SHOW_ONLY;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && animationCycle > 0)
		{
			animationCycle = 0;
			return true;
		}
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
	{
		if(scrollY != 0)
		{
			if(scrollY > 0) scrollIndex += RUNG_Y;
			else scrollIndex -= RUNG_Y;
			scrollIndex = Mth.clamp(scrollIndex, 0, maxScroll);
			return true;
		} else return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(mouseButton == 0 && xcor >= xOffset + 80 && xcor < xOffset + 87)
		{
			if(ycor >= yOffset + 35 && ycor < yOffset + 42)
			{
				scrollIndex = Mth.clamp(scrollIndex + RUNG_Y, 0, maxScroll);
				isScrolling = true;
				return true;
			} else if(ycor >= yOffset + 185 && ycor < yOffset + 192)
			{
				scrollIndex = Mth.clamp(scrollIndex - RUNG_Y, 0, maxScroll);
				isScrolling = true;
				return true;
			}
		}
		return super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		if(isScrolling)
		{
			isScrolling = false;
			return false;
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
}