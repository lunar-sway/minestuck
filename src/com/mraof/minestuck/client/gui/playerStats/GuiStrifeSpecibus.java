package com.mraof.minestuck.client.gui.playerStats;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import io.netty.util.concurrent.GlobalEventExecutor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import scala.swing.event.MouseButtonEvent;

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
	private static int selectedCard = -1;
	
	public GuiStrifeSpecibus()
	{
		super();
		guiWidth = 226;
		guiHeight = 188;
		portfolio = MinestuckPlayerData.getClientPortfolio();
		System.out.println(portfolio);
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
		
		drawCard(11, 9, cardScale, 6);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		//this.drawTexturedModalRect(xOffset+6, yOffset+6, 14, 54, 98, 98);
		
		drawCard(59, 7, cardScale, 7);
		drawCard(12, 50, cardScale, 9);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		//this.drawTexturedModalRect(xOffset+8, yOffset+6, 0, 38, 136, 114);
		
		drawCard(107, 7, cardScale, 8);
		drawCard(56, 40, cardScale, 0);
		this.mc.getTextureManager().bindTexture(guiPortfolioTabs);
		this.drawTexturedModalRect(xOffset+28, yOffset+12, 0, 38, 152, 142);
		
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
			
			i++;
		}
		
		System.out.println("ba");
	}
	
	public void drawCard(int cardX, int cardY, float cardScale, int index)
	{
		if(index < portfolio.size()) drawCard(cardX, cardY, cardScale, portfolio.get(index));
	}
	
	public void drawCard(int cardX, int cardY, float cardScale, StrifeSpecibus specibus)
	{
		
		
		if(specibus == null) return;
		
		//specibus.putItemStack(new ItemStack(Items.IRON_SWORD));
		
		KindAbstratusType abstratus = specibus.getAbstratus();
		String unlocalizedName = abstratus.getUnlocalizedName();
		String displayName = abstratus.getDisplayName();
		int txOffset = mc.fontRenderer.getStringWidth(displayName);
		
		int x = (int) ((xOffset+cardX));
		int y = (int) ((yOffset+cardY));
		
		ResourceLocation icon = new ResourceLocation("minestuck", iconsLoc+unlocalizedName+".png");
		
		setScale(cardScale);
		this.mc.getTextureManager().bindTexture(guiStrifeCard);
		this.drawTexturedModalRect(x/scale, y/scale, 28, 0, 200, 256);
		setScale(cardScale/4);
		this.mc.getTextureManager().bindTexture(icon);
		this.drawTexturedModalRect((x+60)/scale, (y+52)/scale, 0, 0, 256, 256);
		
		setScale(1);
		
	}
	
	
    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GlStateManager.enableLighting();
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        //this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        }
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
	
}
