package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Random;

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
	
	private static final ResourceLocation guiEcheladder = new ResourceLocation("minestuck", "textures/gui/echeladder.png");
	
	private static final int MAX_SCROLL = Echeladder.RUNG_COUNT*14 - 154;
														//0			1			2			3			4			5			6			7			8			9			10			11			12			13			14			15			16			17			18			19			20			21			22			23			24			25			26			27			28			29			30			31			32			33			34			35			36			37			38			39			40			41			42			43			44			45			46			47			48			49
	private static final int[] backgrounds = new int[] {0xFF4FD400, 0xFFFF0000, 0xFF956C4C, 0xFF7DB037, 0xFFD8A600, 0xFF7F0000, 0xFF007F0E, 0xFF808080, 0xFF00FF21, 0xFF4800FF, 0xFF404040, 0xFFE4FF00, 0xFFDFBB6C, 0xFFCECECE, 0xFFFF0000, 0xFFC68E4D, 0xFF60E554, 0xFF88CE88, 0xFF006EBC, 0xFFF12B26, 0xFFC11000, 0xFFBA8B34, 0xFF5134A8, 0xFF92CC00, 0xFF93613B, 0xFF111121, 0xFFD61B28, 0xFFEF8181, 0xFFED5C1A, 0xFFDBDBDB, 0xFFEFC300, 0xFF3529A5, 0xFF634021, 0xFFBCBCBC, 0xFFBA1500, 0xFF42A3B5, 0xFF3C6354, 0xFFC4B681, 0xFF969696, 0xFF6B6B6B, 0xFFAD1BA3, 0xFF0021FF, 0xFF000000, 0xFF294F9B, 0xFFADA87B, 0xFF439E35, 0xFF8E583E, 0xFF606060, 0xFFDDC852, 0xFFFFFFFF};
	private static final int[] textColors  = new int[] {  0xFDFF2B,   0x404040,   0xB6FF00,   0x775716,   0xFFFFFF,   0xFF6A00,   0x0094FF,   0x3F3F3F,   0x007F7F,   0xB200FF,   0x7B9CB5,   0x6D9A00,   0x219621,   0x7F743F,   0xFF7F7F,   0xAF0A8C,   0x2A9659,   0xFFD8F2,   0xFFFFFF,   0xDAFF7F,   0x3459BC,   0xDFE868,   0x2AA3D3,   0x4C4C4C,   0x00D318,   0x6F22A5,   0xC4AA29,   0x237C00,   0x2D2D2D,   0xFF6721,   0x8487E0,   0x000000,   0xADADAD,   0xE24400,   0xE27609,   0x0A08A0,   0xC6A623,   0x38C151,   0xF9A640,   0x368E4A,   0xFFFFFF,   0x00A1C1,   0x6B699E,   0x2D2D2D,   0x18117A,   0xFF9028,   0x1F7C8E,   0xE25012,   0x9721E0,   0x000000};
	
	private static final int ladderXOffset = 163, ladderYOffset = 25;
	private static final int rows = 12;
	
	private static final int timeBeforeAnimation = 10, timeBeforeNext = 16, timeForRung = 4, timeForShowOnly = 65;
	
	private int scrollIndex;
	private boolean isScrolling;
	
	public static int lastRung = -1;	//The current rung last time the gui was opened. Used to determine which rung to display increments from next time the gui is opened
	public static int animatedRung;	//The rung animated to or the latest to be animated
	private int fromRung;	//First rung to display increments from; (actually the one right before that one)
	private int animationCycle;	//Ticks left on the animation cycle
	private int animatedRungs;	//The amount of rungs to animate
	
	public EcheladderScreen()
	{
		super(new TranslationTextComponent(TITLE));
		guiWidth = 250;
		guiHeight = 202;
	}
	
	@Override
	public void init()
	{
		super.init();
		scrollIndex = MathHelper.clamp((ClientPlayerData.getRung() - 8)*14, 0, MAX_SCROLL);
		animatedRung = Math.max(animatedRung, lastRung);	//If you gain a rung while the gui is open, the animated rung might get higher than the lastRung. Otherwise they're always the same value.
		fromRung = lastRung;
		lastRung = ClientPlayerData.getRung();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		updateScrollAndAnimation(mouseX, mouseY);
		
		int speedFactor = MinestuckConfig.CLIENT.echeladderAnimation.get().getSpeed();
		int currentRung;
		boolean showLastRung = true;
		if(animationCycle == 0)
		{
			currentRung = animatedRung;
			if(animatedRung < ClientPlayerData.getRung())
			{
				animatedRungs = ClientPlayerData.getRung() - animatedRung;
				animationCycle = timeBeforeAnimation + getTicksForRungAnimation(animatedRungs)*speedFactor;
				animatedRung = ClientPlayerData.getRung();
			}
		} else
		{
			int rungTicks = getTicksForRungAnimation(animatedRungs)*speedFactor;
			if(animationCycle - rungTicks >= 0)	//Awaiting animation start
				currentRung = animatedRung - animatedRungs;
			else
			{
				if(animatedRungs < 5)	//The animation type where the rungs flicker in
				{
					int rung = (animationCycle/speedFactor + timeBeforeNext)/(timeForRung + timeBeforeNext);
					currentRung = animatedRung - rung;
					if((animationCycle/speedFactor + timeBeforeNext)%(timeForRung + timeBeforeNext) >= timeBeforeNext)
						showLastRung = (animationCycle/speedFactor + timeBeforeNext)%(timeForRung + timeBeforeNext) - timeBeforeNext >= timeForRung/2;
				} else	//The animation type where the animation just goes through all rungs
				{
					currentRung = animatedRung;
					int rung = animationCycle*animatedRungs/(timeForShowOnly*speedFactor) + 1;
					currentRung = animatedRung - rung;
				}
			}
		}
		
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderBackground(matrixStack);
		
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs(matrixStack);
		
		drawLadder(matrixStack, currentRung, showLastRung);
		
		RenderSystem.color3f(1,1,1);
		
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		this.blit(matrixStack, xOffset, yOffset, 0, 0, guiWidth, guiHeight);

		this.blit(matrixStack, xOffset + 80, yOffset + 42 + (int) (130*(1 - scrollIndex/(float) MAX_SCROLL)), 0, 243, 7, 13);
		
		ITextComponent tooltip = drawEffectIconsAndText(matrixStack, currentRung, mouseX, mouseY);
		
		if(fromRung < currentRung)
		{
			Random rand = new Random(452619373);
			int rung;
			for(rung = 0; rung <= Math.max(fromRung, currentRung - 4); rung++)
				rand.nextInt(0xFFFFFF);
			for(; rung <= currentRung; rung++)
			{
				int index = rung - 1 - Math.max(fromRung, currentRung - 4);
				ITextComponent newTooltip = drawGainedRungBonuses(matrixStack, rung, index, rand, mouseX, mouseY);
				if(newTooltip != null)
					tooltip = newTooltip;
			}
		}
		
		drawActiveTabAndOther(matrixStack, mouseX, mouseY);
		
		if(tooltip != null)
			renderTooltip(matrixStack, tooltip, mouseX, mouseY);
	}
	
	private void drawLadder(MatrixStack matrixStack, int currentRung, boolean showLastRung)
	{
		this.mc.getTextureManager().bindTexture(guiEcheladder);
		int scroll = scrollIndex % 14;
		for(int i = 0; i < rows; i++)
			blit(matrixStack, xOffset + 90, yOffset + 175 + scroll - i*14, 7, 242, 146, 14);
		
		Random rand = new Random(452619373);
		for(int i = 0; i < scrollIndex/14; i++)
			rand.nextInt(0xFFFFFF);
		for(int i = 0; i < rows; i++)
		{
			int y = yOffset + 177 + scroll - i*14;
			int rung = scrollIndex/14 + i;
			if(rung > Echeladder.RUNG_COUNT)
				break;
			
			int textColor = 0xFFFFFF;
			if(rung <= currentRung - (showLastRung ? 0 : 1))
			{
				textColor = rand.nextInt(0xFFFFFF);
				if(textColors.length > rung)
					textColor = textColors[rung];
				fill(matrixStack, xOffset + 90, y, xOffset + 236, y + 12, backgrounds.length > rung ? backgrounds[rung] : (textColor^0xFFFFFFFF));
			} else if(rung == currentRung + 1 && animationCycle == 0)
			{
				int bg = ~rand.nextInt(0xFFFFFF);
				if(backgrounds.length > rung)
					bg = backgrounds[rung];
				else if(textColors.length > rung)
					bg = ~textColors[rung];
				fill(matrixStack, xOffset + 90, y + 10, xOffset + 90 + (int)(146* ClientPlayerData.getRungProgress()), y + 12, bg);
			} else rand.nextInt(0xFFFFFF);
			
			String s = I18n.hasKey("echeladder.rung."+rung) ? I18n.format("echeladder.rung."+rung) : "Rung "+(rung+1);
			mc.fontRenderer.drawString(matrixStack, s, xOffset+ladderXOffset - mc.fontRenderer.getStringWidth(s) / 2, y + 2, textColor);
		}
	}
	
	@Nullable
	private ITextComponent drawEffectIconsAndText(MatrixStack matrixStack, int currentRung, int mouseX, int mouseY)
	{
		boolean gristLimit = true;
		PotionSpriteUploader sprites = this.minecraft.getPotionSpriteUploader();
		TextureAtlasSprite strengthSprite = sprites.getSprite(Effects.STRENGTH);
		TextureAtlasSprite healthSprite = sprites.getSprite(Effects.HEALTH_BOOST);
		
		minecraft.getTextureManager().bindTexture(strengthSprite.getAtlasTexture().getTextureLocation());
		blit(matrixStack, xOffset + 5, yOffset + 30, getBlitOffset(), 18, 18, strengthSprite);
		minecraft.getTextureManager().bindTexture(healthSprite.getAtlasTexture().getTextureLocation());
		blit(matrixStack, xOffset + 5, yOffset + 84, getBlitOffset(), 18, 18, healthSprite);
		this.mc.getTextureManager().bindTexture(PlayerStatsScreen.icons);
		this.blit(matrixStack, xOffset + 6, yOffset + 139, 48, 64, 16, 16);
		this.blit(matrixStack, xOffset + 5, yOffset + 7, 238, 16, 18, 18);
		
		String msg = title.getString();
		mc.fontRenderer.drawString(matrixStack, msg, xOffset + 168 - mc.fontRenderer.getStringWidth(msg)/2F, yOffset + 12, 0x404040);
		
		int attack = (int) Math.round(100*(1 + Echeladder.attackBonus(currentRung)));
		mc.fontRenderer.drawString(matrixStack, I18n.format(ATTACK), xOffset + 24, yOffset + 30, 0x404040);
		mc.fontRenderer.drawString(matrixStack, attack+"%", xOffset + 26, yOffset + 39, 0x0094FF);
		
		double health = mc.player.getMaxHealth()/2;	//10 + Echeladder.healthBoost(currentRung)/2.0;
		mc.fontRenderer.drawString(matrixStack, I18n.format(HEALTH), xOffset + 24, yOffset + 84, 0x404040);
		mc.fontRenderer.drawString(matrixStack, String.valueOf(health), xOffset + 26, yOffset + 93, 0x0094FF);
		
		mc.fontRenderer.drawString(matrixStack, "=", xOffset + 25, yOffset + 12, 0x404040);	//Should this be black, or the same blue as the numbers?
		mc.fontRenderer.drawString(matrixStack, String.valueOf(ClientPlayerData.getBoondollars()), xOffset + 27 + mc.fontRenderer.getStringWidth("="), yOffset + 12, 0x0094FF);
		//mc.fontRenderer.drawString("Rep: " + ClientPlayerData.getConsortReputation(), xOffset + 75 + mc.fontRenderer.getCharWidth('='), yOffset + 12, 0x0094FF);
		
		mc.fontRenderer.drawString(matrixStack, I18n.format(CACHE), xOffset + 24, yOffset + 138, 0x404040);
		mc.fontRenderer.drawString(matrixStack, "Unlimited", xOffset + 26, yOffset + 147, 0x0094FF);
		
		if(mouseY >= yOffset + 39 && mouseY < yOffset + 39 + mc.fontRenderer.FONT_HEIGHT && mouseX >= xOffset + 26 && mouseX < xOffset + 26 + mc.fontRenderer.getStringWidth(attack+"%"))
			return new TranslationTextComponent(DAMAGE_UNDERLING).appendString("\n" + Math.round(attack*Echeladder.getUnderlingDamageModifier(currentRung)) + "%");
		if(mouseY >= yOffset + 93 && mouseY < yOffset + 93 + mc.fontRenderer.FONT_HEIGHT && mouseX >= xOffset + 26 && mouseX < xOffset + 26 + mc.fontRenderer.getStringWidth(String.valueOf(health)))
			return new TranslationTextComponent(PROTECTION_UNDERLING).appendString("\n" + String.format("%.1f", 100*Echeladder.getUnderlingProtectionModifier(currentRung))+"%");
		return null;
	}
	
	@Nullable
	private ITextComponent drawGainedRungBonuses(MatrixStack matrixStack, int rung, int index, Random rand, int mouseX, int mouseY)
	{
		int textColor = rand.nextInt(0xFFFFFF);
		if(textColors.length > rung)
			textColor = textColors[rung];
		int bg = backgrounds.length > rung ? backgrounds[rung] : (textColor^0xFFFFFFFF);
		
		String str = "+"+(Math.round(100*Echeladder.attackBonus(rung)) - Math.round(100*Echeladder.attackBonus(rung - 1)))+"%!";
		fill(matrixStack, xOffset + 5 + 32*(index%2), yOffset + 50 + 15*(index/2), xOffset + 35 + 32*(index%2), yOffset + 62 + 15*(index/2), bg);
		int strX = xOffset + 20 + 32*(index%2) - mc.fontRenderer.getStringWidth(str)/2, strY = yOffset + 52 + 15*(index/2);
		mc.fontRenderer.drawString(matrixStack, str, strX, strY, textColor);
		
		double d = (Echeladder.healthBoost(rung) - Echeladder.healthBoost(rung - 1))/2D;
		str = "+"+(d == 0 ? d : d+"!");
		fill(matrixStack, xOffset + 5 + 32*(index%2), yOffset + 104 + 15*(index/2), xOffset + 35 + 32*(index%2), yOffset + 116 + 15*(index/2), bg);
		strX = xOffset + 20 + 32*(index%2) - mc.fontRenderer.getStringWidth(str)/2;
		strY = yOffset + 106 + 15*(index/2);
		mc.fontRenderer.drawString(matrixStack, str, strX, strY, textColor);
		
		if(mouseY >= strY && mouseY < strY + mc.fontRenderer.FONT_HEIGHT && mouseX >= strX && mouseX < strX + mc.fontRenderer.getStringWidth(str))
		{
			int diff = (int) Math.round(100*Echeladder.attackBonus(rung)*Echeladder.getUnderlingDamageModifier(rung));
			diff -= Math.round(100*Echeladder.attackBonus(rung - 1)*Echeladder.getUnderlingDamageModifier(rung - 1));
			return new TranslationTextComponent(DAMAGE_UNDERLING_INCREASE, diff);
		}
		
		if(mouseY >= strY && mouseY < strY + mc.fontRenderer.FONT_HEIGHT && mouseX >= strX && mouseX < strX + mc.fontRenderer.getStringWidth(str))
		{
			int diff = (int) Math.round(1000*Echeladder.getUnderlingProtectionModifier(rung - 1));
			diff -= Math.round(1000*Echeladder.getUnderlingProtectionModifier(rung));
			return new TranslationTextComponent(PROTECTION_UNDERLING_INCREASE, diff/10D);
		}
		
		return null;
	}
	
	private void updateScrollAndAnimation(int xcor, int ycor)
	{
		if(isScrolling)
		{
			scrollIndex = (int) (MAX_SCROLL*(ycor - yOffset - 179)/-130F);
			scrollIndex = MathHelper.clamp(scrollIndex, 0, MAX_SCROLL);
		}
		
		if(animationCycle > 0)
			if(MinestuckConfig.CLIENT.echeladderAnimation.get() != MinestuckConfig.AnimationSpeed.NOTHING)
				animationCycle--;
			else animationCycle = 0;
	}
	
	private int getTicksForRungAnimation(int rungs)
	{
		if(rungs < 5)
			return timeForRung + (rungs - 1)*(timeForRung + timeBeforeNext);
		else return timeForShowOnly;
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
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
	{
		if(scroll != 0)
		{
			if(scroll > 0)
				scrollIndex += 14;
			else scrollIndex -= 14;
			scrollIndex = MathHelper.clamp(scrollIndex, 0, MAX_SCROLL);
			return true;
		}
		else return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	@Override
	public boolean mouseClicked(double xcor, double ycor, int mouseButton)
	{
		if(mouseButton == 0&& xcor >= xOffset + 80 && xcor < xOffset + 87)
		{
			if(ycor >= yOffset + 35 && ycor < yOffset + 42)
			{
				scrollIndex = MathHelper.clamp(scrollIndex + 14, 0, MAX_SCROLL);
				isScrolling = true;
				return true;
			} else if(ycor >= yOffset + 185 && ycor < yOffset + 192)
			{
				scrollIndex = MathHelper.clamp(scrollIndex - 14, 0, MAX_SCROLL);
				isScrolling = true;
				return true;
			}
		}
		return	super.mouseClicked(xcor, ycor, mouseButton);
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