package com.mraof.minestuck.client.gui.captchalouge;

import com.mraof.minestuck.client.settings.MinestuckKeyHandler;
import com.mraof.minestuck.inventory.captchalouge.CaptchaDeckHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public abstract class SylladexGuiHandler extends GuiScreen implements GuiYesNoCallback
{
	
	
	protected static final ResourceLocation sylladexFrame = new ResourceLocation("minestuck", "textures/gui/sylladex_frame.png");
	protected static final ResourceLocation cardTexture = new ResourceLocation("minestuck", "textures/gui/icons.png");
	protected static final int GUI_WIDTH = 256, GUI_HEIGHT= 202;
	protected static final int MAP_WIDTH = 224, MAP_HEIGHT = 153;
	protected static final int X_OFFSET = 16, Y_OFFSET = 17;
	protected static final int CARD_WIDTH = 21, CARD_HEIGHT = 26;
	
	protected RenderItem itemRender;
	protected ArrayList<GuiCard> cards = new ArrayList<GuiCard>();
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
	
	protected GuiButton emptySylladex;
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	public SylladexGuiHandler()
	{
		itemRender = mc.getRenderItem();
	}
	
	@Override
	public void initGui()
	{
		emptySylladex = new GuiButton(0, (width - GUI_WIDTH)/2 + 140, (height - GUI_HEIGHT)/2 + 175, 100, 20, I18n.format("gui.emptySylladexButton"));
		buttonList.add(emptySylladex);
	}
	
	@Override
	public void drawScreen(int xcor, int ycor, float f)
	{
		this.drawDefaultBackground();
		
		emptySylladex.x = (width - GUI_WIDTH)/2 + 140;
		emptySylladex.y = (height - GUI_HEIGHT)/2 + 175;
		
		int mouseWheel = Mouse.getDWheel();
		float prevScroll = scroll;
		
		if (mouseWheel < 0)
			this.scroll += 0.25F;
		else if (mouseWheel > 0)
			this.scroll -= 0.25F;
		this.scroll = MathHelper.clamp(this.scroll, 1.0F, 2.0F);
		
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
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		ArrayList<GuiCard> visibleCards = new ArrayList<GuiCard>();
		for(GuiCard card : cards)
			if(card.xPos + CARD_WIDTH > mapX && card.xPos < mapX + mapWidth 
					&& card.yPos + CARD_HEIGHT > mapY && card.yPos < mapY + mapHeight)
				visibleCards.add(card);
		
		for(GuiCard card : visibleCards)
			card.drawItemBackground();
		
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		for(GuiCard card : visibleCards)
			card.drawItem();
		GlStateManager.disableDepth();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		finishMap();
		
		mc.getTextureManager().bindTexture(sylladexFrame);
		drawTexturedModalRect(xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		mc.fontRenderer.drawString(I18n.format("gui.sylladex"), xOffset + 15, yOffset + 5, 0x404040);
		
		String str = CaptchaDeckHandler.clientSideModus.getName();
		mc.fontRenderer.drawString(str, xOffset + GUI_WIDTH - mc.fontRenderer.getStringWidth(str) - 16, yOffset + 5, 0x404040);
		
		super.drawScreen(xcor, ycor, f);
		
		if(isMouseInContainer(xcor, ycor))
		{
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiCard card : visibleCards)
				if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
						translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
				{
					card.drawTooltip(xcor, ycor);
					break;
				}
		}
	}
	
	@Override
	protected void mouseClicked(int xcor, int ycor, int mouseButton) throws IOException
	{
		if(isMouseInContainer(xcor, ycor))
		{
			int xOffset = (width - GUI_WIDTH)/2;
			int yOffset = (height - GUI_HEIGHT)/2;
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiCard card : this.cards)
				if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
						translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
				{
					card.onClick(mouseButton);
					return;
				}
			return;
		}
		super.mouseClicked(xcor, ycor, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if(button == emptySylladex)
		{
			mc.currentScreen = new GuiYesNo(this, I18n.format("gui.emptySylladex1"), I18n.format("gui.emptySylladex2"), 0);
			mc.currentScreen.setWorldAndResolution(mc, width, height);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		if(MinestuckKeyHandler.instance.sylladexKey.isActiveAndMatches(keyCode))
			mc.displayGuiScreen(null);
		/*if(Loader.isModLoaded("NotEnoughItems"))
		{
			boolean usage = keyCode == NEIClientConfig.getKeyBinding("gui.usage") || (keyCode == NEIClientConfig.getKeyBinding("gui.recipe") && NEIClientUtils.shiftKey());
			boolean recipe = keyCode == NEIClientConfig.getKeyBinding("gui.recipe");
			
			if(usage || recipe)
			{
				Point mousePos = GuiDraw.getMousePosition();
				int xcor = mousePos.x;
				int ycor = mousePos.y;
				
				if(isMouseInContainer(xcor, ycor))
				{
					int translX = (int) ((xcor - (width - GUI_WIDTH)/2 - X_OFFSET) * scroll);
					int translY = (int) ((ycor - (height - GUI_HEIGHT)/2 - Y_OFFSET) * scroll);
					for(GuiCard card : cards)
						if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
								translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
						{
							if(card.item != null)
								if(usage)
									GuiUsageRecipe.openRecipeGui("item", card.item.copy());
								else GuiCraftingRecipe.openRecipeGui("item", card.item.copy());
							return;
						}
				}
			}
		}*/
	}
	
	@Override
	public void confirmClicked(boolean result, int id)
	{
		if(result)
			MinestuckChannelHandler.sendToServer(MinestuckPacket.makePacket(Type.CAPTCHA, CaptchaDeckPacket.GET, CaptchaDeckHandler.EMPTY_SYLLADEX, false));
		mc.currentScreen = this;
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
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)xOffset, (float)yOffset, 0F);
		GlStateManager.scale(1.0F / this.scroll, 1.0F / this.scroll, 1.0F);
	}
	
	private void finishMap()
	{
		GlStateManager.popMatrix();
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
	
	public ResourceLocation getCardTexture(GuiCard card)
	{
		return cardTexture;
	}
	
	public int getCardTextureX(GuiCard card)
	{
		return textureIndex*CARD_WIDTH;
	}
	
	public int getCardTextureY(GuiCard card)
	{
		return 96;
	}
	
	public static class GuiCard
	{
		
		protected SylladexGuiHandler gui;
		public ItemStack item;
		public int index;
		public int xPos, yPos;
		
		protected GuiCard()
		{
			item = ItemStack.EMPTY;
		}
		
		public GuiCard(ItemStack item, SylladexGuiHandler gui, int index, int xPos, int yPos)
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
			if(this.item.isEmpty() && mouseButton == 1)
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
			gui.mc.getTextureManager().bindTexture(gui.getCardTexture(this));
			int minX = 0, maxX = CARD_WIDTH, minY = 0, maxY = CARD_HEIGHT;
			if(this.xPos + minX < gui.mapX)
				minX += gui.mapX - (this.xPos + minX);
			else if(this.xPos + maxX > gui.mapX + gui.mapWidth)
				maxX -= (this.xPos + maxX) - (gui.mapX + gui.mapWidth);
			if(this.yPos + minY < gui.mapY)
				minY += gui.mapY - (this.yPos + minY);
			else if(this.yPos + maxY > gui.mapY + gui.mapHeight)
				maxY -= (this.yPos + maxY) - (gui.mapY + gui.mapHeight);
			gui.drawTexturedModalRect(this.xPos + minX - gui.mapX, this.yPos + minY - gui.mapY,	//Gui pos
					gui.getCardTextureX(this) + minX, gui.getCardTextureY(this) + minY,	//Texture pos
					maxX - minX, maxY - minY);	//Size
		}
		
		protected void drawItem()
		{
			GlStateManager.color(1F, 1F, 1F, 1F);
			if(!this.item.isEmpty())
			{
				int x = this.xPos +2 - gui.mapX;
				int y = this.yPos +7 - gui.mapY;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + 16 < 0 || y + 16 < 0)
					return;
				gui.itemRender.renderItemAndEffectIntoGUI(item, x, y);
				if(item.getCount() > 1)
				{
					String stackSize = String.valueOf(item.getCount());
					GlStateManager.disableLighting();
					GlStateManager.disableDepth();
					GlStateManager.disableBlend();
					gui.mc.fontRenderer.drawStringWithShadow(stackSize, x + 16 - gui.mc.fontRenderer.getStringWidth(stackSize), y + 8, 0xC6C6C6);
					GlStateManager.enableLighting();
					GlStateManager.enableDepth();
					GlStateManager.enableBlend();
				}
				gui.itemRender.renderItemOverlayIntoGUI(gui.mc.fontRenderer, item, x, y, "");
			}
		}
		
		protected void drawTooltip(int mouseX, int mouseY)
		{
			if(!item.isEmpty())
				gui.renderToolTip(item, mouseX, mouseY);
		}
		
	}
	
	public static class ModusSizeCard extends GuiCard
	{
		public int size;
		
		public ModusSizeCard(SylladexGuiHandler gui, int size, int xPos, int yPos)
		{
			this.gui = gui;
			this.index = -1;
			this.size = size;
			this.xPos = xPos;
			this.yPos = yPos;
		}
		
		@Override
		protected void drawTooltip(int mouseX, int mouseY) {}
		
		@Override
		protected void drawItem()
		{
			GlStateManager.color(1F, 1F, 1F, 1F);
			if(size > 1)
			{
				String stackSize = String.valueOf(size);
				int x = this.xPos - gui.mapX + 18 - gui.mc.fontRenderer.getStringWidth(stackSize);
				int y = this.yPos - gui.mapY + 15;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + gui.mc.fontRenderer.getStringWidth(stackSize) < 0 || y + gui.fontRenderer.FONT_HEIGHT < 0)
					return;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				gui.mc.fontRenderer.drawStringWithShadow(stackSize, x, y, 0xC6C6C6);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.enableBlend();
			}
		}
		
	}
}
