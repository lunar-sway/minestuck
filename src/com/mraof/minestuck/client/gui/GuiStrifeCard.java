package com.mraof.minestuck.client.gui;

import java.util.ArrayList;
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
	private float scale = 1;
	private static final int columnWidth = 50,columns = 2;
	private static EntityPlayer player;
	private static final FontRenderer font = Minestuck.fontSpecibus;
	
	private boolean canScroll;
	private float scrollPos = 0F;
	private boolean isClicking = false;
	private int extraLines = 0;
	
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
		
		this.drawRect(xOffset+27, yOffset+23, xOffset+127, yOffset+178, 0xFF000000);
		
		int listOffsetX = xOffset + 16;
		int listOffsetY = yOffset + 59 - extraLines*font.FONT_HEIGHT;
		
		List<KindAbstratusType> list = KindAbstratusList.getTypeList();
		
		
		list = new ArrayList<>();
		for(int i = 0; i < 40; i++)
			list.add(new KindAbstratusType("testkind"+i));
		
		canScroll = list.size() > 25;
		
		int itemMin = 0;
		
		int i = 0, count = 0;
		for(KindAbstratusType type : list)
		{
			if(!type.getSelectable()) continue;	
			count++;
			if(count-1 < itemMin) continue;
			i++;
			String typeName = type.getDisplayName();
			typeName = type.getUnlocalizedName();
			
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
			if(i > 25)break;
		}
		

		this.mc.getTextureManager().bindTexture(guiStrifeSelector);
		this.drawTexturedModalRect(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		this.drawTexturedModalRect(xOffset+128, yOffset+23, canScroll ? 232 : 244, 0, 12, 15);
		
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
