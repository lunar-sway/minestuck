package com.mraof.minestuck.client.gui.playerStats;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.util.KindAbstratusType;

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
	
	public GuiStrifeSpecibus()
	{
		super();
		guiWidth = 226;
		guiHeight = 188;
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float par3) {
		super.drawScreen(xcor, ycor, par3);
		this.drawDefaultBackground();
		scale = 1;
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		drawTabs();
		
		this.mc.getTextureManager().bindTexture(guiStrifePortfolio);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		setScale(1);
		drawActiveTabAndOther(xcor, ycor);
		
		ArrayList<StrifeSpecibus> portfolio = new ArrayList<StrifeSpecibus>();
		for(int i = 0; i < 10; i++)
		{
			portfolio.add(new StrifeSpecibus(i));
			for(int j = 0; j < 10; j++)
			{
				portfolio.get(i).forceItemStack(new ItemStack(Items.IRON_SWORD));
			}
		}
		

		
		int i = 0;
		for(StrifeSpecibus specibus : portfolio)
		{

			setScale(1);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			
			KindAbstratusType abstratus = specibus.getAbstratus();
			String unlocalizedName = abstratus.getUnlocalizedName();
			String displayName = abstratus.getDisplayName();
			//unlocalizedName = "a";
			ResourceLocation icon = new ResourceLocation("minestuck", iconsLoc+unlocalizedName+".png");
			
			
			
			drawCard(70 + (int)(20*i),180 - (int)(20*i),0.25F, specibus);			
			
			setScale(16/256F);
			drawTexturedModalRect((xOffset+iconsOffX+(i*20))/scale, (yOffset+iconsOffY)/scale, 0, 0, 256, 256);
			
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
		setScale(cardScale*2.6F);
		mc.fontRenderer.drawString(displayName, (int)(((75 - txOffset))+(cardX/scale)), (int)((88)+(cardY/scale)), 0x00E371);
		mc.getTextureManager().bindTexture(icon);
		setScale(cardScale/0.5f*(0.1875f));
		drawTexturedModalRect(140 + cardX/scale, 148 + cardY/scale, 0, 0, 256, 256);
		
		setScale(1);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		for(ItemStack stack : specibus.getItems())
		{
			//System.out.println(stack);
			//drawItemStack(stack, cardX, cardY, "");
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
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (8), altText);
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
