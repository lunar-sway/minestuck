package com.mraof.minestuck.client.gui.playerStats;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.KindAbstratusType;
import com.mraof.minestuck.util.MinestuckPlayerData;

import io.netty.util.concurrent.GlobalEventExecutor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiStrifeSpecibus extends GuiPlayerStats
{
	
	private static final ResourceLocation guiStrifePortfolio = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_portfolio.png");
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
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		scale = 1;
		blankCardCount = 0;
		portfolio = MinestuckPlayerData.getClientPortfolio();
		if(portfolio == null) portfolio = new ArrayList<StrifeSpecibus>();
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiStrifePortfolio);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		setScale(1);
		drawActiveTabAndOther(xcor, ycor);
		/*portfolio = new ArrayList<StrifeSpecibus>();
		for(int i = 0; i < 10; i++)
		{
			portfolio.add(new StrifeSpecibus(i));
			for(int j = 0; j < 10; j++)
			{
				portfolio.get(i).forceItemStack(new ItemStack(Items.IRON_SWORD));
			}
		}
		*/
		

		
		int i = 0;
		int pSize = portfolio.size();
		for(StrifeSpecibus specibus : portfolio)
		{

			setScale(1);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			KindAbstratusType abstratus = specibus.getAbstratus();
			String unlocalizedName = abstratus.getUnlocalizedName();
			String displayName = abstratus.getDisplayName();
			//unlocalizedName = "a";
			ResourceLocation icon = new ResourceLocation("minestuck", iconsLoc+unlocalizedName+".png");
			int cardX = 128 + (int)(48*(i%5));
			int cardY = 64 - (i*5) + (int)(48*(i/5));
			float cardScale = 0.25F;
			
			float c = 1f;
			//if(selectedCard != i && selectedCard != -1)
				GlStateManager.color(c, c, c, 1F);
			drawCard(cardX,cardY,cardScale, specibus);			
			
			mc.getTextureManager().bindTexture(icon);
			if(specibus.getAbstratusIndex() > 0)
			{
				setScale(16/256F);
				drawTexturedModalRect((xOffset+iconsOffX+((i-blankCardCount)*20))/scale, (yOffset+iconsOffY)/scale, 0, 0, 256, 256);
			}
			
			if(isPointInRegion(cardX, cardY, (int)(64*cardScale), (int)(64*cardScale), xcor, ycor))
				selectedCard = i;
			i++;
		}

		
		
	}
	
	public void drawCard(int cardX, int cardY, float cardScale, StrifeSpecibus specibus)
	{
		
		//specibus.putItemStack(new ItemStack(Items.IRON_SWORD));
		
		KindAbstratusType abstratus = specibus.getAbstratus();
		String unlocalizedName = abstratus.getUnlocalizedName();
		String displayName = abstratus.getDisplayName();
		int txOffset = mc.fontRenderer.getStringWidth(displayName);
		ResourceLocation icon = new ResourceLocation("minestuck", iconsLoc+unlocalizedName+".png");
		
		mc.getTextureManager().bindTexture(guiStrifeCard);
		setScale(cardScale);
		drawTexturedModalRect(cardX/scale, cardY/scale, 0, 0, 256, 256);
		setScale(cardScale*1.5f);
		mc.fontRenderer.drawString("strife specibus", (int)((26)+(cardX/scale)), (int)((8)+(cardY/scale)), 0x00E371);
		setScale(cardScale*0.75f);
		mc.fontRenderer.drawString("sylladex :: strife deck", (int)((60)+(cardX/scale)), (int)((240)+(cardY/scale)), 0xFFFFFF);
		if(specibus.getAbstratusIndex() > 0)
		{
			setScale(cardScale*2.6F);
			mc.fontRenderer.drawString(displayName, (int)(((75 - txOffset))+(cardX/scale)), (int)((88)+(cardY/scale)), 0x00E371);
			mc.getTextureManager().bindTexture(icon);
			setScale(cardScale/0.5f*(0.1875f));
			drawTexturedModalRect(140 + cardX/scale, 148 + cardY/scale, 0, 0, 256, 256);
		}
		else blankCardCount++;
		setScale(1);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		ItemStack[] testStack = {new ItemStack(MinestuckItems.beverage), new ItemStack(MinestuckItems.itemFrog), new ItemStack(MinestuckBlocks.genericObject)};
		
		int i = 0;
		for(ItemStack stack : specibus.getItems())
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			mc.getTextureManager().bindTexture(icons);
			drawTexturedModalRect(cardX+(i*28), cardY, 0, 122, 21, 26);
			//System.out.println(stack);
			drawItemStack(stack, cardX+(i*28), cardY, "");
			i++;
		}
		
		setScale(1);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	
    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText)
    {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        //this.zLevel = 200.0F;
        //this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        GlStateManager.enableLighting();
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        //this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (8), altText);
        //this.zLevel = 0.0F;
        //this.itemRender.zLevel = 0.0F;
        }
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
	
}
