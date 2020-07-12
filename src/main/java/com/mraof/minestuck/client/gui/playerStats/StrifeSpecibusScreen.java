package com.mraof.minestuck.client.gui.playerStats;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrifeSpecibusScreen extends PlayerStatsScreen
{
	public static final String TITLE = "minestuck.strife_specibus";
	public static final String KIND_ABSTRATUS = "minestuck.kind_abstratus";
	
	private static final ResourceLocation guiStrifePortfolio = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_portfolio.png");
	private static final ResourceLocation guiPortfolioTabs = new ResourceLocation("minestuck", "textures/gui/strife_specibus/portfolio_tabs.png");
	private static final ResourceLocation guiStrifePortfolioBg = new ResourceLocation("minestuck", "textures/gui/strife_specibus/portfolio_bg.png");
	private static final ResourceLocation guiStrifeCard = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_card.png");
	private static final String iconsLoc = "textures/gui/strife_specibus/icons/";
	
	private static final int columnWidth = 70, columns = 3;
	
	private static final int iconsOffX = 23, iconsOffY = 166;
	private static float scale;
	private static List<StrifeSpecibus> portfolio;
	private static int blankCardCount = 0;
	private static int selectedCard;
	private static boolean isSelectingCard = false;
	private static int mouseX;
	private static int mouseY;
	
	public StrifeSpecibusScreen()
	{
		super(new TranslationTextComponent(TITLE));
		guiWidth = 226;
		guiHeight = 188;
		portfolio = ClientPlayerData.playerPortfolio;
		if(portfolio == null) portfolio = new ArrayList<>();
	}
	
	@Override
	public void render(int xcor, int ycor, float par3)
	{
		super.render(xcor, ycor, par3);
		
		isSelectingCard = false;
		mouseX = xcor;
		mouseY = ycor;
		
		this.renderBackground();
		
		scale = 1;
		float cardScale = 0.25f;
		blankCardCount = 0;
		portfolio = ClientPlayerData.playerPortfolio;
		if(portfolio == null) portfolio = new ArrayList<>();
		int i;
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(guiStrifePortfolioBg);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset, yOffset, 20, 58, 98, 94);
		
		drawCard(11, 9, cardScale, 6);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+2, yOffset+6, 4, 44, 132, 120);
		
		drawCard(59, 7, cardScale, 7);
		drawCard(12, 50, cardScale, 9);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+21, yOffset+4, 0, 18, 152, 134);
		
		drawCard(107, 7, cardScale, 8);
		drawCard(56, 40, cardScale, 0);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+45, yOffset+10, 0, 0, 164, 152);
		this.blit(xOffset+173, yOffset+6, 124, 0, 7, 4);
		
		drawCard(107, 33, cardScale, 5);
		drawCard(56, 80, cardScale, 4);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+81, yOffset+28, 0, 8, 137, 120);
		this.blit(xOffset+218, yOffset+46, 142, 22, 2, 10);
		
		drawCard(159, 25, cardScale, 3);
		drawCard(107, 77, cardScale, 1);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+124, yOffset+52, 0, 32, 96, 96);
		
		drawCard(159, 69, cardScale, 2);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.blit(xOffset+168, yOffset+96, 204, 0, 52, 52);
		
		setScale(1);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiStrifePortfolio);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		drawActiveTabAndOther(xcor, ycor);
		
		i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			setScale((float)1/16);
			this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck", iconsLoc+specibus.getAbstratus().getUnlocalizedName()+".png"));
			this.blit((int) ((xOffset+23+(20*i))/scale),(int) ((yOffset+166)/scale),0,0,256,256);
			i++;
		}
		setScale(1);
		
		if(!isSelectingCard) selectedCard = -1;
	}
	
	public void drawCard(int cardX, int cardY, float cardScale, int index)
	{
		if(index < portfolio.size()) drawCard(cardX, cardY, cardScale, index, portfolio.get(index));
	}
	
	public void drawCard(int cardX, int cardY, float cardScale, int index, StrifeSpecibus specibus)
	{
		if(specibus == null) return;
		
		//specibus.putItemStack(new ItemStack(Items.IRON_SWORD));
		
		KindAbstratusType abstratus = specibus.getAbstratus();
		String unlocalizedName = abstratus.getUnlocalizedName();
		String displayName = abstratus.getDisplayName().getFormattedText();
		int txOffset = mc.fontRenderer.getStringWidth(displayName);
		List<ItemStack> items = specibus.getItems();
		int totalItems = items.size();
		
		int x = (xOffset+cardX) - (index == selectedCard ? 5 : 0);
		int y = (yOffset+cardY) - (index == selectedCard ? 5 : 0);
		
		ResourceLocation icon = new ResourceLocation(Minestuck.MOD_ID, iconsLoc+unlocalizedName+".png");
		setScale(cardScale);
		this.mc.getTextureManager().bindTexture(guiStrifeCard);
		this.blit((int) (x/scale), (int) (y/scale), 28, 0, 200, 256);
		setScale(cardScale/2.5f);
		this.mc.getTextureManager().bindTexture(icon);
		this.blit((int) (x/scale+57), (int) (y/scale+148), 0, 0, 256, 256);
		
		setScale(cardScale*2);
		font.drawString("strife specibus", x/scale+5, y/scale+4, 0xFF00E371);
		setScale(cardScale*2.5f);
		mc.fontRenderer.drawString(displayName, x/scale+52-font.getStringWidth(displayName), y/scale+91, 0xFF00E371);
		setScale(cardScale);
		font.drawString("sylladex :: strife deck", x/scale+16, y/scale+179, 0xFFFFFFFF);
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int deckxpos = 68 - 23*(Math.min(items.size(),5)/2);
		int n = 0;
		for(ItemStack i : items)
		{
			int ixoff = (int)(deckxpos + (n%5)*(23)) - (int)(n/5);
			int iyoff = 194 - (int)(n/5)*2;
			setScale(cardScale);
			mc.getTextureManager().bindTexture(icons);
			blit((int) (x/scale + ixoff), (int) (y/scale + iyoff), 0, 122, 21, 26);
			drawItemStack(i, (int)(x/scale) + ixoff+2, (int)(y/scale) + iyoff+4, i.getDisplayName().getFormattedText());
			n++;
		}
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		setScale(cardScale);
		int x1 = (int)(x);
		int y1 = (int)(y);
		int xs = (int)((200+(selectedCard == index ? 20 : 0))*scale);
		int ys = (int)((256+(selectedCard == index ? 20 : 0))*scale);
		int x2 = x1+xs;
		int y2 = y1+ys;
		if(isPointInRegion(x1, y1, xs, ys, mouseX, mouseY))
		{
			selectedCard = index;
			isSelectingCard = true;
		}
		setScale(1);
	}
	
	//TODO Define cards better, and use the data when drawing them too
	private static final List<Pair<Integer, Integer>> cards = Arrays.asList(Pair.of(56, 40), Pair.of(107, 77),
			Pair.of(159, 69), Pair.of(159, 25), Pair.of(56, 80), Pair.of(107, 33),
			Pair.of(11, 9), Pair.of(59, 7), Pair.of(12, 50));
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		for(int index = 0; index < cards.size(); index++)
		{
			Pair<Integer, Integer> coords = cards.get(index);
			int cardX = coords.getLeft();
			int cardY = coords.getRight();
			
			int x1 = (xOffset+cardX) - (index == selectedCard ? 5 : 0);
			int y1 = (yOffset+cardY) - (index == selectedCard ? 5 : 0);
			int xs = (int)((200+(selectedCard == index ? 20 : 0))*scale);
			int ys = (int)((256+(selectedCard == index ? 20 : 0))*scale);
			if(isPointInRegion(x1, y1, xs, ys, (int) mouseX, (int) mouseY))
			{
				StrifePortfolioHandler.retrieveSpecibus(index);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Draws an ItemStack.
	 *
	 */
	private void drawItemStack(ItemStack stack, int x, int y, String altText)
	{
		
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translatef(x, y, 0);
		mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, 0, 0);
		GlStateManager.popMatrix();
		RenderHelper.disableStandardItemLighting();
		
	}
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
}
