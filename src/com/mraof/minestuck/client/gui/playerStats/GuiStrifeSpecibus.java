package com.mraof.minestuck.client.gui.playerStats;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiStrifeSpecibus extends GuiPlayerStats
{

	private static final ResourceLocation guiStrifePortfolio = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_portfolio.png");
	private static final ResourceLocation guiPortfolioTabs = new ResourceLocation("minestuck", "textures/gui/strife_specibus/portfolio_tabs.png");
	private static final ResourceLocation guiStrifePortfolioBg = new ResourceLocation("minestuck", "textures/gui/strife_specibus/portfolio_bg.png");
	private static final ResourceLocation guiStrifeCard = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_card.png");
	private static final String iconsLoc = "textures/gui/strife_specibus/icons/";
	
	private static final int columnWidth = 70, columns = 3;
	
	private static final int iconsOffX = 23, iconsOffY = 166;
	private static float scale;
	private static ArrayList<StrifeSpecibus> portfolio;
	private static int blankCardCount = 0;
	private static int selectedCard;
	private static boolean isSelectingCard = false;
	private static int mouseX;
	private static int mouseY;
	private static final FontRenderer font = Minestuck.fontSpecibus;
	
	public GuiStrifeSpecibus()
	{
		super();
		guiWidth = 226;
		guiHeight = 188;
		portfolio = MinestuckPlayerData.getClientPortfolio();
		if(portfolio == null) portfolio = new ArrayList<StrifeSpecibus>();
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		portfolio = MinestuckPlayerData.getClientPortfolio();
		if(portfolio == null) portfolio = new ArrayList<StrifeSpecibus>();
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) 
	{
		super.drawScreen(xcor, ycor, par3);
		
		isSelectingCard = false;
		mouseX = xcor;
		mouseY = ycor;
		
		this.drawDefaultBackground();
		scale = 1;
		float cardScale = 0.25f;
		blankCardCount = 0;
		portfolio = MinestuckPlayerData.getClientPortfolio();
		if(portfolio == null) portfolio = new ArrayList<StrifeSpecibus>();
		int i;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifePortfolioBg);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		

		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset, yOffset+0, 20, 58, 98, 94);
		
		drawCard(11, 9, cardScale, 6);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+2, yOffset+6, 4, 44, 132, 120);
		
		drawCard(59, 7, cardScale, 7);
		drawCard(12, 50, cardScale, 9);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+21, yOffset+4, 0, 18, 152, 134);
		
		drawCard(107, 7, cardScale, 8);
		drawCard(56, 40, cardScale, 0);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+45, yOffset+10, 0, 0, 164, 152);
		this.drawTexturedModalRect(xOffset+173, yOffset+6, 124, 0, 7, 4);
		
		drawCard(107, 33, cardScale, 5);
		drawCard(56, 80, cardScale, 4);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+81, yOffset+28, 0, 8, 137, 120);
		this.drawTexturedModalRect(xOffset+218, yOffset+46, 142, 22, 2, 10);
		
		drawCard(159, 25, cardScale, 3);
		drawCard(107, 77, cardScale, 1);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+124, yOffset+52, 0, 32, 96, 96);
		
		drawCard(159, 69, cardScale, 2);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+168, yOffset+96, 204, 0, 52, 52);
		
		setScale(1);
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiStrifePortfolio);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		drawActiveTabAndOther(xcor, ycor);
		i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{
			setScale((float)1/16);
			this.mc.getTextureManager().bindTexture(new ResourceLocation("minestuck", iconsLoc+specibus.getAbstratus().getUnlocalizedName()+".png"));
			this.drawTexturedModalRect((xOffset+23+(20*i))/scale,(yOffset+166)/scale,0,0,256,256);
			i++;
		}
		setScale(1);
		
		//drawCard(110, -10, 1, 0);
		
		//System.out.println("a");
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
		String displayName = abstratus.getDisplayName();
		int txOffset = mc.fontRenderer.getStringWidth(displayName);
		List<ItemStack> items = specibus.getItems();
		int totalItems = items.size();
		
		int x = (int) ((xOffset+cardX)) - (index == selectedCard ? 5 : 0);
		int y = (int) ((yOffset+cardY)) - (index == selectedCard ? 5 : 0);
		
		ResourceLocation icon = new ResourceLocation(Minestuck.MOD_ID, iconsLoc+unlocalizedName+".png");		
		setScale(cardScale);
		this.mc.getTextureManager().bindTexture(guiStrifeCard);
		this.drawTexturedModalRect(x/scale, y/scale, 28, 0, 200, 256);
		setScale(cardScale/2.5f);
		this.mc.getTextureManager().bindTexture(icon);
		this.drawTexturedModalRect(x/scale+57, y/scale+148, 0, 0, 256, 256);
		
		setScale(cardScale*2);
		font.drawString("strife specibus", x/scale+5, y/scale+4, 0xFF00E371, false);
		setScale(cardScale*2.5f);
		mc.fontRenderer.drawString(displayName, x/scale+52-font.getStringWidth(displayName), y/scale+91, 0xFF00E371, false);
		setScale(cardScale);
		font.drawString("sylladex :: strife deck", x/scale+16, y/scale+179, 0xFFFFFFFF, false);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		int deckxpos = 68 - 23*(Math.min(items.size(),5)/2);
		int n = 0;
		for(ItemStack i : items)
		{
			int ixoff = (int)(deckxpos + (n%5)*(23)) - (int)(n/5);
			int iyoff = 194 - (int)(n/5)*2;
			setScale(cardScale);
			mc.getTextureManager().bindTexture(icons);
			drawTexturedModalRect(x/scale + ixoff, y/scale + iyoff, 0, 122, 21, 26);
			drawItemStack(i, (int)(x/scale) + ixoff+2, (int)(y/scale) + iyoff+4, i.getDisplayName());
			n++;
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
			if(Mouse.getEventButtonState() && Mouse.isButtonDown(0))
			{
				//TODO retrieve card
				StrifePortfolioHandler.retrieveSpecibus(index);
			}
		}
		setScale(1);
	}
	
	
    /**
     * Draws an ItemStack.
     * 
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
		
    	RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
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
