package com.mraof.minestuck.client.gui.captchalouge;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL12;

import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.util.Debug;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class SylladexGuiHandler extends GuiScreen
{
	
	
	protected static final ResourceLocation sylladexFrame = new ResourceLocation("minestuck", "textures/gui/SylladexFrame.png");
	protected static final ResourceLocation cardTexture = new ResourceLocation("minestuck", "textures/gui/icons.png");
	protected static final int GUI_WIDTH = 256, GUI_HEIGHT= 202;
	protected static final int MAP_WIDTH = 224, MAP_HEIGHT = 153;
	protected static final int X_OFFSET = 16, Y_OFFSET = 17;
	protected static final int CARD_WIDTH = 21, CARD_HEIGHT = 26;
	
	protected RenderItem itemRender = new RenderItem();
	protected ArrayList<GuiItem> items = new ArrayList<GuiItem>();
	protected int textureIndex;
	protected int maxWidth, maxHeight;
	
	/**
	 * Position of the map
	 */
	protected int mapX, mapY;
	protected int mapWidth = MAP_WIDTH, mapHeight = MAP_HEIGHT;
	/**
	 * The scrolling
	 */
	protected float scroll = 1F;
	
	protected int mousePosX, mousePosY;
	protected boolean mousePressed;
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public void drawScreen(int xcor, int ycor, float f)
	{
		super.drawScreen(xcor, ycor, f);
		this.drawDefaultBackground();
		
		int mouseWheel = Mouse.getDWheel();
		float prevScroll = scroll;
		
		if (mouseWheel < 0)
			this.scroll += 0.25F;
		else if (mouseWheel > 0)
			this.scroll -= 0.25F;
		this.scroll = MathHelper.clamp_float(this.scroll, 1.0F, 2.0F);
		
		if(prevScroll != scroll)
		{
			double i1 = mapX + ((double)mapWidth)/2;
			double i2 = mapY + ((double)mapHeight)/2;
			mapWidth = Math.round(MAP_WIDTH*scroll);
			mapHeight = Math.round(MAP_HEIGHT*scroll);
			mapX = (int) (i1 - ((double)mapWidth)/2);
			mapY = (int) (i2 - ((double)mapHeight)/2);
			updatePosition();
			mapX = Math.max(0, Math.min(maxWidth - mapWidth, mapX));
			mapY = Math.max(0, Math.min(maxHeight - mapHeight, mapY));
		}
		
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		
		if(Mouse.isButtonDown(0))
		{
			
			if(isMouseInContainer(xcor, ycor))
			{
				if(isMouseInContainer(mousePosX, mousePosY))
				{
					mapX = Math.max(0, Math.min(maxWidth - mapWidth, mapX + mousePosX - xcor));
					mapY = Math.max(0, Math.min(maxHeight - mapHeight, mapY + mousePosY - ycor));
				}
				mousePosX = xcor;
				mousePosY = ycor;
			}
			
		}
		else
		{
			mousePressed = false;
			mousePosX = -1;
			mousePosY = -1;
		}
		
		prepareMap(xOffset + X_OFFSET, yOffset + Y_OFFSET);
		
		drawGuiMap(xcor, ycor);
		
		glColor4f(1F, 1F, 1F, 1F);
		
		ArrayList<GuiItem> visibleItems = new ArrayList<GuiItem>();
		for(GuiItem item : items)
			if(item.xPos + CARD_WIDTH > mapX && item.xPos < mapX + mapWidth 
					&& item.yPos + CARD_HEIGHT > mapY && item.yPos < mapY + mapHeight)
				visibleItems.add(item);
		
		for(GuiItem item : visibleItems)
			item.drawItemBackground();
		
		RenderHelper.enableGUIStandardItemLighting();
		glEnable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		for(GuiItem item : visibleItems)
			item.drawItem();
		glDisable(GL_DEPTH_TEST);
		RenderHelper.disableStandardItemLighting();
		
		finishMap();
		
		mc.getTextureManager().bindTexture(sylladexFrame);
		drawTexturedModalRect(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		mc.fontRenderer.drawString(StatCollector.translateToLocal("gui.sylladex"), xOffset + 15, yOffset + 5, 0x404040);
		
		String str = CaptchaDeckHandler.clientSideModus.getName();
		mc.fontRenderer.drawString(str, xOffset + GUI_WIDTH - mc.fontRenderer.getStringWidth(str) - 16, yOffset + 5, 0x404040);
		
		if(isMouseInContainer(xcor, ycor))
		{
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiItem item : visibleItems)
				if(translX >= item.xPos + 2 - mapX && translX < item.xPos + 18 - mapX &&
						translY >= item.yPos + 7 - mapY && translY < item.yPos + 23 - mapY)
				{
					item.drawTooltip(xcor, ycor);
					break;
				}
		}
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton)
	{
		if(isMouseInContainer(xcor, ycor))
		{
			int xOffset = (width - GUI_WIDTH)/2;
			int yOffset = (height - GUI_HEIGHT)/2;
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiItem item : this.items)
				if(translX >= item.xPos + 2 - mapX && translX < item.xPos + 18 - mapX &&
						translY >= item.yPos + 7 - mapY && translY < item.yPos + 23 - mapY)
				{
					item.onClick(mouseButton);
					return;
				}
			return;
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
	public void drawGuiMap(int xcor, int ycor)
	{
		drawRect(0, 0, mapWidth, mapHeight, 0xFF8B8B8B);
	}
	
	private void prepareMap(int xOffset, int yOffset)
	{
		glPushMatrix();
		glTranslatef((float)xOffset, (float)yOffset, 0F);
		glScalef(1.0F / this.scroll, 1.0F / this.scroll, 1.0F);
	}
	
	private void finishMap()
	{
		glPopMatrix();
	}
	
	private boolean isMouseInContainer(int xcor, int ycor)
	{
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		return xcor >= xOffset + X_OFFSET && xcor < xOffset + X_OFFSET + MAP_WIDTH &&
				ycor >= yOffset + Y_OFFSET && ycor < yOffset + Y_OFFSET + MAP_HEIGHT;
	}
	
	public void updateContent()
	{
		mapX = Math.min(mapX, maxWidth - mapWidth);
		mapY = Math.min(mapY, maxHeight - mapHeight);
	}
	
	/**
	 * Called when the player zooms in or out.
	 */
	public abstract void updatePosition();
	
	protected static class GuiItem
	{
		
		protected SylladexGuiHandler gui;
		protected ItemStack item;
		protected int index;
		protected int xPos, yPos;
		
		protected GuiItem()
		{}
		
		public GuiItem(ItemStack item, SylladexGuiHandler gui, int index, int xPos, int yPos)
		{
			this.gui = gui;
			this.item = item;
			this.index = index;
			this.xPos = xPos;
			this.yPos = yPos;
		}
		
		public void onClick(int mouseButton)
		{
			int toSend = -1;
			if(this.item == null && mouseButton == 1)
				toSend = CaptchaDeckHandler.EMPTY_CARD;
			else if(this.index != -1 && (mouseButton == 0 || mouseButton == 1))
				toSend = this.index;
			
			if(toSend != -1)
			{
				MinestuckPacket packet = MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.GET, toSend, mouseButton != 0);
				MinestuckChannelHandler.sendToServer(packet);
			}
		}

		protected void drawItemBackground()
		{
			gui.mc.getTextureManager().bindTexture(gui.cardTexture);
			int minX = 0, maxX = CARD_WIDTH, minY = 0, maxY = CARD_HEIGHT;
			if(this.xPos + minX < gui.mapX)
				minX += gui.mapX - (this.xPos + minX);
			else if(this.xPos + maxX > gui.mapX + gui.mapWidth)
				maxX -= (this.xPos + maxX) - (gui.mapX + gui.mapWidth);
			if(this.yPos + minY < gui.mapY)
				minY += gui.mapY - (this.yPos + minY);
			else if(this.yPos + maxY > gui.mapY + gui.mapHeight)
				maxX -= (this.yPos + maxY) - (gui.mapY + gui.mapHeight);
			gui.drawTexturedModalRect(this.xPos + minX - gui.mapX, this.yPos + minY - gui.mapY,	//Gui pos
					gui.textureIndex*CARD_WIDTH + minX, 96 + minY,	//Texture pos
					maxX - minX, maxY - minY);	//Size
		}
		
		protected void drawItem()
		{
			glColor4f(1F, 1F, 1F, 1F);
			if(this.item != null)
			{
				int x = this.xPos +2 - gui.mapX;
				int y = this.yPos +7 - gui.mapY;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + 16 < 0 || y + 16 < 0)
					return;
				gui.itemRender.renderItemAndEffectIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), item, x, y);
				if(item.stackSize > 1)
				{
					String stackSize = String.valueOf(item.stackSize);
					glDisable(GL_LIGHTING);
					glDisable(GL_DEPTH_TEST);
					glDisable(GL_BLEND);
					gui.mc.fontRenderer.drawStringWithShadow(stackSize, x + 16 - gui.mc.fontRenderer.getStringWidth(stackSize), y + 8, 0xC6C6C6);
					glEnable(GL_LIGHTING);
					glEnable(GL_DEPTH_TEST);
					glEnable(GL_BLEND);
				}
				gui.itemRender.renderItemOverlayIntoGUI(gui.mc.fontRenderer, gui.mc.getTextureManager(), item, x, y, "");
			}
		}
		
		protected void drawTooltip(int mouseX, int mouseY)
		{
			if(item != null)
				gui.renderToolTip(item, mouseX, mouseY);
		}
		
	}
	
}
