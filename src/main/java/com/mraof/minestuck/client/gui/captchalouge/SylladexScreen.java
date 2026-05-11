package com.mraof.minestuck.client.gui.captchalouge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsContainerScreen;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;
import com.mraof.minestuck.client.util.MSKeyHandler;
import com.mraof.minestuck.inventory.captchalogue.*;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.network.CaptchaDeckPackets;
import com.mraof.minestuck.player.ClientPlayerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.joml.Matrix4fStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SylladexScreen extends PlayerStatsContainerScreen<CaptchaDeckMenu>
{
	public static final String TITLE = "minestuck.sylladex";
	public static final String EMPTY_SYLLADEX_1 = "minestuck.empty_sylladex.1";
	public static final String EMPTY_SYLLADEX_2 = "minestuck.empty_sylladex.2";
	public static final String EMPTY_SYLLADEX_BUTTON = "minestuck.empty_sylladex.button";
	public static final String USE_ITEM = "minestuck.captcha_deck.use_item";
	
	protected static final ResourceLocation sylladexFrame = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/sylladex_gui.png");
	protected static final ResourceLocation cardTexture = ResourceLocation.fromNamespaceAndPath("minestuck", "textures/gui/icons.png");
	protected static final int MAP_WIDTH = 279, MAP_HEIGHT = 124;
	protected static final int X_OFFSET = 16, Y_OFFSET = 17;
	protected static final int CARD_WIDTH = 21, CARD_HEIGHT = 26;
	// Helps for button placements
	protected static final int BUTTON_HEIGHT = 16, BUTTON_WIDTH = 120, BUTTON_Y_OFFSET = 155, BUTTON_X_OFFSET = 176;
	
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
	
	protected Button emptySylladex, modusButton;
	
	// Ugly fix for tooltip text being all the way off
	private boolean shouldRenderTooltip = false;
	protected final Modus modus;
	
	public SylladexScreen(int windowId, Inventory inventory, Modus modus)
	{
		super(new CaptchaDeckMenu(windowId, inventory), inventory, Component.translatable(TITLE));
		this.modus = modus;
		
		imageWidth = guiWidth = 311;
		imageHeight = guiHeight = 238;
		
		titleLabelX = X_OFFSET;
        inventoryLabelY = imageHeight - 94;
	}
	
	@Override
	public void init()
	{
		super.init();
		
		emptySylladex = addRenderableWidget(new ExtendedButton(xOffset + BUTTON_X_OFFSET, yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT + 4, BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable(EMPTY_SYLLADEX_BUTTON), button -> emptySylladex()));
		modusButton = addRenderableWidget(new ExtendedButton(xOffset + BUTTON_X_OFFSET + 20, yOffset + BUTTON_Y_OFFSET, BUTTON_WIDTH - 20, 18, Component.translatable(USE_ITEM), button -> use()));
		
		updateContent();
	}
	
	@Override
	public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f)
	{
		handleDragging(xcor, ycor);
		
		// Tooltips are rendered afterwards, as the text is subject to move from the posestack
		shouldRenderTooltip = false;
		List<GuiCard> visibleCards = getVisibleCards();
		super.render(guiGraphics, xcor, ycor, f);
		
		renderVisibleCards(guiGraphics, visibleCards, xcor, ycor);
		
		shouldRenderTooltip = true;
		renderTooltip(guiGraphics, xcor, ycor);
		
		GuiCard hoveredCard = getCardAt(xcor, ycor, visibleCards);
		if(hoveredCard != null)
		{
			hoveredCard.drawTooltip(guiGraphics, xcor, ycor);
		}
	}
	
	protected List<GuiCard> getVisibleCards()
	{
		ArrayList<GuiCard> visibleCards = new ArrayList<>();
		for(GuiCard card : cards)
			if(card.xPos + CARD_WIDTH > mapX && card.xPos < mapX + mapWidth
					&& card.yPos + CARD_HEIGHT > mapY && card.yPos < mapY + mapHeight)
				visibleCards.add(card);
		return visibleCards;
	}
	
	protected void renderVisibleCards(GuiGraphics guiGraphics, List<GuiCard> visibleCards, int xcor, int ycor)
	{
		guiGraphics.enableScissor(leftPos + X_OFFSET, topPos + Y_OFFSET, leftPos + X_OFFSET + Mth.ceil(mapWidth / scroll), topPos + Y_OFFSET + Mth.ceil(mapHeight / scroll));
		Matrix4fStack modelPoseStack = RenderSystem.getModelViewStack();
		modelPoseStack.pushMatrix();
		modelPoseStack.translate(xOffset + X_OFFSET, yOffset + Y_OFFSET, 0);
		modelPoseStack.scale(1 / this.scroll, 1 / this.scroll, 1);
		RenderSystem.applyModelViewMatrix();
		
		drawGuiMap(guiGraphics, xcor, ycor);
		
		for(GuiCard card : visibleCards)
			card.drawItemBackground(guiGraphics);
		
		for(GuiCard card : visibleCards)
			card.drawItem(guiGraphics);
		
		modelPoseStack.popMatrix();
		RenderSystem.applyModelViewMatrix();
		RenderSystem.disableDepthTest();
		guiGraphics.disableScissor();
	}
	
	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		super.renderLabels(guiGraphics, mouseX, mouseY);
		
		String str = ClientPlayerData.getModus().getName().getString();
		guiGraphics.drawString(font, str, guiWidth - font.width(str) - 16, 5, 0x404040, false);
	}
	
	@Override
	protected void renderTooltip(GuiGraphics guiGraphics, int x, int y)
	{
		if(shouldRenderTooltip)
		{
			super.renderTooltip(guiGraphics, x, y);
			super.drawTabTooltip(guiGraphics, x, y);
		}
	}
	
	@Override
	protected void drawTabTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
	{
		// Manually called, as otherwise the text is off the tooltip
	}
	
	/**
	 *
	 * @param xcor         X position of the (potential) card
	 * @param ycor         Y position of the (potential) card
	 * @param visibleCards
	 * @return The card at a given location, if there is any
	 */
	@Nullable
	protected GuiCard getCardAt(double xcor, double ycor, List<GuiCard> visibleCards)
	{
		if(isMouseInContainer(xcor, ycor))
		{
			int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
			int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);
			for(GuiCard card : visibleCards)
				if(translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
						translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY)
					return card;
		}
		
		return null;
	}
	
	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
	{
		modusButton.active = !menu.getMenuItem().isEmpty();
		
		drawTabs(guiGraphics);
		
		RenderSystem.setShaderColor(1, 1, 1, 1);
		// Required, as (by default) the image is cropped at 256x256
		guiGraphics.blit(sylladexFrame, xOffset, yOffset, 0, 0, guiWidth, guiHeight, 311, 256);
		
		drawActiveTabAndIcons(guiGraphics);
	}
	
	/**
	 * Handles using the mouse to move
	 */
	protected void handleDragging(int xcor, int ycor)
	{
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
			
		} else
		{
			mousePosX = -1;
			mousePosY = -1;
		}
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
	{
		float prevScroll = this.scroll;
		
		if(scrollY < 0)
			this.scroll += 0.25F;
		else if(scrollY > 0)
			this.scroll -= 0.25F;
		this.scroll = Mth.clamp(this.scroll, 1.0F, 2.0F);
		
		if(prevScroll != this.scroll)
		{
			double i1 = mapX + ((double) mapWidth) / 2;
			double i2 = mapY + ((double) mapHeight) / 2;
			mapWidth = Math.round(MAP_WIDTH * this.scroll);
			mapHeight = Math.round(MAP_HEIGHT * this.scroll);
			mapX = (int) (i1 - ((double) mapWidth) / 2);
			mapY = (int) (i2 - ((double) mapHeight) / 2);
			updatePosition();
			mapX = Math.max(0, Math.min(maxWidth - mapWidth, mapX));
			mapY = Math.max(0, Math.min(maxHeight - mapHeight, mapY));
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
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
			GuiCard clickedCard = getCardAt(mouseX, mouseY, cards);
			if(clickedCard != null)
			{
				clickedCard.onClick(mouseButton);
				return true;
			}
			if(!menu.getCarried().isEmpty())
			{
				PacketDistributor.sendToServer(new CaptchaDeckPackets.CaptchalogueCarriedItem());
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
		//TODO? add JEI support (like commented NEI support)
		return super.keyPressed(keyCode, scanCode, i);
	}
	
	public void onEmptyConfirm(boolean result)
	{
		if(result)
			PacketDistributor.sendToServer(new CaptchaDeckPackets.GetItem(CaptchaDeckHandler.EMPTY_SYLLADEX, false));
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
	
	/**
	 * Checks if the position is in the card map
	 *
	 * @param xcor
	 * @param ycor
	 * @return
	 */
	protected boolean isMouseInContainer(double xcor, double ycor)
	{
		return xcor >= xOffset + X_OFFSET && xcor < xOffset + X_OFFSET + MAP_WIDTH &&
				ycor >= yOffset + Y_OFFSET && ycor < yOffset + Y_OFFSET + MAP_HEIGHT;
	}
	
	/**
	 * Checks if the modus is still valid
	 * <p>
	 * If not, reopens the sylladex gui (effectively rendering this instance useless)
	 * @return True if the sylladex gui is reopened
	 */
	@MustBeInvokedByOverriders
	protected boolean checkAndReopen() {
		if(ClientPlayerData.getModus() != this.modus)
		{
			PlayerStatsScreen.openGui(true);
			return true;
		}
		return false;
	}
	
	public void updateContent()
	{
		if(checkAndReopen())
			return;
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
		return textureIndex * CARD_WIDTH;
	}
	
	public int getCardTextureY(GuiCard card)
	{
		return 96;
	}
	
	private void use()
	{
		ItemStack stack = menu.getMenuItem();
		if(!stack.isEmpty())
		{
			if(!(stack.getItem() instanceof CaptchaCardItem))
			{
				ModusType<?> type = ModusTypes.getTypeFromItem(stack.getItem());
				Modus newModus = type.createClientSide();
				Modus modus = ClientPlayerData.getModus();
				if(newModus != null && modus != null && newModus.getClass() != modus.getClass() && !newModus.canSwitchFrom(modus))
				{
					minecraft.screen = new ConfirmScreen(this::onConfirm, Component.translatable(EMPTY_SYLLADEX_1), Component.translatable(EMPTY_SYLLADEX_2))
					{
						@Override
						public void removed()
						{
							minecraft.screen = SylladexScreen.this;
							minecraft.player.closeContainer();
						}
					};
					minecraft.screen.init(minecraft, width, height);
					return;
				}
			}
			PacketDistributor.sendToServer(new CaptchaDeckPackets.TriggerModusButton());
		}
	}
	
	private void onConfirm(boolean result)
	{
		if(result && !menu.getMenuItem().isEmpty())
			PacketDistributor.sendToServer(new CaptchaDeckPackets.TriggerModusButton());
		minecraft.screen = this;
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
				PacketDistributor.sendToServer(new CaptchaDeckPackets.GetItem(toSend, mouseButton != 0));
			}
		}
		
		public void drawItemBackground(GuiGraphics guiGraphics)
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
			guiGraphics.blit(gui.getCardTexture(this), this.xPos + minX - gui.mapX, this.yPos + minY - gui.mapY,    //Gui pos
					gui.getCardTextureX(this) + minX, gui.getCardTextureY(this) + minY,    //Texture pos
					maxX - minX, maxY - minY);    //Size
		}
		
		public void drawItem(GuiGraphics guiGraphics)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			if(!this.item.isEmpty())
			{
				int x = this.xPos + 2 - gui.mapX;
				int y = this.yPos + 7 - gui.mapY;
				if(x >= gui.mapWidth || y >= gui.mapHeight || x + 16 < 0 || y + 16 < 0)
					return;
				guiGraphics.renderItem(item, x, y);
				guiGraphics.renderItemDecorations(gui.font, item, x, y);
			}
		}
		
		public void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
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
		public void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
		{
		}
		
		@Override
		public void drawItem(GuiGraphics guiGraphics)
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
