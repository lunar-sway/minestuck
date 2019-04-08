package com.mraof.minestuck.client.gui;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.KindAbstratusType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import scala.swing.TextComponent;

public class GuiStrifeCard extends GuiScreenMinestuck
{
	private static int guiWidth = 147, guiHeight = 185;
	private static int xOffset, yOffset;
	private static final ResourceLocation guiStrifeSelector = new ResourceLocation("minestuck", "textures/gui/strife_specibus/strife_selector.png");
	private static float scale = 1;
	private static final int columnWidth = 50, columns = 2;
	private static EntityPlayer player;
	private static final FontRenderer font = Minestuck.fontSpecibus;
	
	public GuiStrifeCard(EntityPlayer player) 
	{
		this.player = player;
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xOffset = (width-guiWidth)/2;
		yOffset = (height-guiHeight)/2;
		mc = Minecraft.getMinecraft();
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		scale = 1;
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		int listOffsetX = xOffset + 16;
		int listOffsetY = yOffset + 59;
		

		List<KindAbstratusType> list = KindAbstratusList.getTypeList();
		for(int i = 1; i < list.size(); i++)
		{
			KindAbstratusType type = list.get(i);
			String typeName = type.getDisplayName();
			
			//txPos += 78 (0.1625*width)
			//tyPos += 36 (9540 / height)
			
			int color = 0xFFFFFF;
			int listX = (columnWidth*((i-1) % columns));
			int listY = (font.FONT_HEIGHT*((i-1) / columns));
			int xPos = listOffsetX + listX + 11;
			int yPos = listOffsetY + listY + 1;
			int sxPos = (int)((listOffsetX + listX)/scale);
			int syPos = (int)((listOffsetY + listY)/scale);
			int txPos = (sxPos + columnWidth - font.getStringWidth(typeName))+ (int)(10/scale);
			int tyPos = syPos + (int)(3/scale);
			if(isPointInRegion(xPos, yPos, columnWidth, font.FONT_HEIGHT, mouseX, mouseY))
			{
				drawRect(xPos, yPos, xPos+columnWidth, yPos+font.FONT_HEIGHT, 0xFFAFAFAF);
				color = 0x000000;
				if(Mouse.getEventButtonState() && Mouse.isButtonDown(0))
				{
					EnumHand hand = (player.getHeldItemMainhand().isItemEqual(new ItemStack(MinestuckItems.strifeCard)) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
					ItemStack card = player.getHeldItem(hand);
					
					if(card.isItemEqual(new ItemStack(MinestuckItems.strifeCard)))
					{
						StrifeSpecibus specibus = new StrifeSpecibus(i);
						
						StrifePortfolioHandler.addSpecibus(player, specibus);
						this.mc.displayGuiScreen(null);
						card.shrink(1);	
					}
					
				}
			}
			
			
			
			font.drawString(typeName, txPos, tyPos, color);
		}
		
		//96*265/width
		
		setScale(1.8F);
		String label = I18n.translateToLocal("kind abstrata");
		int xLabel = (int)(-(((height+guiHeight)/2)-8)/scale);
		int yLabel = (int)((((width+guiWidth)/2)-135)/scale);
		//178
		
		GL11.glRotatef(270, 0, 0, 1);
		font.drawString(label, xLabel, yLabel, 0xFFFFFF);
		
	}
	
	public void setScale(float percentage)
	{
		float s = percentage/scale;
		GL11.glScalef(s,s,s);
		scale = percentage;
	}
	
	public void updateScalePos()
	{
		
	}
}
