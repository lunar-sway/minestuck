package com.mraof.minestuck.client.gui.captchalouge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.network.CaptchaDeckPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;

public abstract class SylladexScreen extends Screen
{
	public static final String TITLE = "minestuck.sylladex";
	public static final String EMPTY_SYLLADEX_1 = "minestuck.empty_sylladex.1";
	public static final String EMPTY_SYLLADEX_2 = "minestuck.empty_sylladex.2";
	public static final String EMPTY_SYLLADEX_BUTTON = "minestuck.empty_sylladex.button";
	
	protected static final ResourceLocation sylladexFrame = new ResourceLocation("minestuck", "textures/gui/sylladex_frame.png");
	protected static final ResourceLocation cardTexture = new ResourceLocation("minestuck", "textures/gui/icons.png");
	protected static final int GUI_WIDTH = 256, GUI_HEIGHT= 202;
	protected static final int MAP_WIDTH = 224, MAP_HEIGHT = 153;
	protected static final int X_OFFSET = 16, Y_OFFSET = 17;
	protected static final int CARD_WIDTH = 21, CARD_HEIGHT = 26;
	
	protected ArrayList<GuiCard> cards = new ArrayList<>();
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
	
	protected Button emptySylladex;
	
	public SylladexScreen()
	{
		super(Component.translatable(TITLE));
	}
	
	@Override
	public void init()
	{
		emptySylladex = new ExtendedButton((width - GUI_WIDTH)/2 + 140, (height - GUI_HEIGHT)/2 + 175, 100, 20, Component.translatable(EMPTY_SYLLADEX_BUTTON), button -> emptySylladex());
		addRenderableWidget(emptySylladex);
		updateContent();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f)
	{
		this.renderBackground(guiGraphics);
		
		int xOffset = (width - GUI_WIDTH)/2;
		int yOffset = (height - GUI_HEIGHT)/2;
		
		emptySylladex.setX(xOffset + 140);
		emptySylladex.setY(yOffset + 175);
		
		if(mousePressed)
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
			mousePosX = -1;
			mousePosY = -1;
		}
		
		PoseStack modelPoseStack = RenderSystem.getModelViewStack();
		modelPoseStack.pushPose();
		modelPoseStack.translate(xOffset + X_OFFSET, yOffset + Y_OFFSET, 0);
		modelPoseStack.scale(1 / this.scroll, 1 / this.scroll, 1);
		RenderSystem.applyModelViewMatrix();
		
		drawGuiMap(guiGraphics, xcor, ycor);
		
		ArrayList<GuiCard> visibleCards = new ArrayList<>();
		for(GuiCard card : cards)
			if(card.xPos + CARD_WIDTH > mapX && card.xPos < mapX + mapWidth
					&& card.yPos + CARD_HEIGHT > mapY && card.yPos < mapY + mapHeight)
				visibleCards.add(card);
		
		for(GuiCard card : visibleCards)
			card.drawItemBackground(guiGraphics);

		for(GuiCard card : visibleCards)
			card.drawItem(guiGraphics);
		
		modelPoseStack.popPose();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.disableDepthTest();
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		guiGraphics.blit(sylladexFrame, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
		
		guiGraphics.drawString(font, getTitle().getString(), xOffset + 15, yOffset + 5, 0x404040, false);
		
		String str = ClientPlayerData.getModus().getName().getString();
		guiGraphics.drawString(font, str, xOffset + GUI_WIDTH - font.width(str) - 16, yOffset + 5, 0x404040, false);
		
		super.render(guiGraphics, xcor, ycor, f);
		
		if(isMouseInContainer(xcor, ycor))
		{
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiCard card : visibleCards)
				if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
						translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
				{
					card.drawTooltip(guiGraphics, xcor, ycor);
					break;
				}
		}
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scroll)
	{
		float prevScroll = this.scroll;
		
		if (scroll < 0)
			this.scroll += 0.25F;
		else if (scroll > 0)
			this.scroll -= 0.25F;
		this.scroll = Mth.clamp(this.scroll, 1.0F, 2.0F);
		
		if(prevScroll != this.scroll)
		{
			double i1 = mapX + ((double)mapWidth)/2;
			double i2 = mapY + ((double)mapHeight)/2;
			mapWidth = Math.round(MAP_WIDTH* this.scroll);
			mapHeight = Math.round(MAP_HEIGHT* this.scroll);
			mapX = (int) (i1 - ((double)mapWidth)/2);
			mapY = (int) (i2 - ((double)mapHeight)/2);
			updatePosition();
			mapX = Math.max(0, Math.min(maxWidth - mapWidth, mapX));
			mapY = Math.max(0, Math.min(maxHeight - mapHeight, mapY));
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, scroll);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		if(mousePressed)
		{
			mousePressed = false;
			return true;
		}
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(isMouseInContainer(mouseX, mouseY))
		{
			int xOffset = (width - GUI_WIDTH)/2;
			int yOffset = (height - GUI_HEIGHT)/2;
			int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);
			for(GuiCard card : this.cards)
				if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
						translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
				{
					card.onClick(mouseButton);
					return true;
				}
			mousePressed = true;
			return true;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private void emptySylladex()
	{
		minecraft.screen = new ConfirmScreen(this::onEmptyConfirm, Component.translatable(EMPTY_SYLLADEX_1), Component.translatable(EMPTY_SYLLADEX_2));
		minecraft.screen.init(minecraft, width, height);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int i)
	{
		if(MSKeyHandler.sylladexKey.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))
		{
			minecraft.setScreen(null);
			return true;
		}
		return super.keyPressed(keyCode, scanCode, i);
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
	
	public void onEmptyConfirm(boolean result)
	{
		if(result)
			MSPacketHandler.sendToServer(CaptchaDeckPacket.get(CaptchaDeckHandler.EMPTY_SYLLADEX, false));
		minecraft.screen = this;
	}
	
	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
	
	public void drawGuiMap(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		guiGraphics.fill(0, 0, mapWidth, mapHeight, 0xFF8B8B8B);
	}
	
	private boolean isMouseInContainer(double xcor, double ycor)
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
		
		protected SylladexScreen gui;
		public ItemStack item;
		public int index;
		public int xPos, yPos;
		
		protected GuiCard()
		{
			item = ItemStack.EMPTY;
		}
		
		public GuiCard(ItemStack item, SylladexScreen gui, int index, int xPos, int yPos)
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
				CaptchaDeckPacket packet = CaptchaDeckPacket.get(toSend, mouseButton != 0);
				MSPacketHandler.sendToServer(packet);
			}
		}

		protected void drawItemBackground(GuiGraphics guiGraphics)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			int minX = 0, maxX = CARD_WIDTH, minY = 0, maxY = CARD_HEIGHT;
			if(this.xPos + minX < gui.mapX)
				minX += gui.mapX - (this.xPos + minX);
			else if(this.xPos + maxX > gui.mapX + gui.mapWidth)
				maxX -= (this.xPos + maxX) - (gui.mapX + gui.mapWidth);
			if(this.yPos + minY < gui.mapY)
				minY += gui.mapY - (this.yPos + minY);
			else if(this.yPos + maxY > gui.mapY + gui.mapHeight)
				maxY -= (this.yPos + maxY) - (gui.mapY + gui.mapHeight);
			guiGraphics.blit(gui.getCardTexture(this), this.xPos + minX - gui.mapX, this.yPos + minY - gui.mapY,	//Gui pos
					gui.getCardTextureX(this) + minX, gui.getCardTextureY(this) + minY,	//Texture pos
					maxX - minX, maxY - minY);	//Size
		}
		
		protected void drawItem(GuiGraphics guiGraphics)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			if(!this.item.isEmpty())
			{
				int x = this.xPos +2 - gui.mapX;
				int y = this.yPos +7 - gui.mapY;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + 16 < 0 || y + 16 < 0)
					return;
				guiGraphics.renderItem(item, x, y);
				guiGraphics.renderItemDecorations(gui.font, item, x, y);
			}
		}
		
		protected void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
		{
			if(!item.isEmpty())
				guiGraphics.renderTooltip(gui.font, item, mouseX, mouseY);
		}
		
	}
	
	public static class ModusSizeCard extends GuiCard
	{
		public int size;
		
		public ModusSizeCard(SylladexScreen gui, int size, int xPos, int yPos)
		{
			this.gui = gui;
			this.index = -1;
			this.size = size;
			this.xPos = xPos;
			this.yPos = yPos;
		}
		
		@Override
		protected void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {}
		
		@Override
		protected void drawItem(GuiGraphics guiGraphics)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			if(size > 1)
			{
				String stackSize = String.valueOf(size);
				int x = this.xPos - gui.mapX + 18 - gui.font.width(stackSize);
				int y = this.yPos - gui.mapY + 15;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + gui.font.width(stackSize) < 0 || y + gui.font.lineHeight < 0)
					return;
				RenderSystem.disableDepthTest();
				RenderSystem.disableBlend();
				guiGraphics.drawString(gui.font, stackSize, x, y, 0xC6C6C6);
				RenderSystem.enableDepthTest();
				RenderSystem.enableBlend();
			}
		}
		
	}
}
