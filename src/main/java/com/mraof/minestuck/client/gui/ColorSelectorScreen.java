package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.network.ColorSelectPacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.ClientPlayerData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class ColorSelectorScreen extends Screen
{
	public static final String TITLE = "minestuck.color_selector";
	public static final String SELECT_COLOR = "minestuck.select_color";
	public static final String COLOR_SELECTED = "minestuck.color_selected";
	public static final String DEFAULT_COLOR_SELECTED = "minestuck.default_color_selected";
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/color_selector.png");
	private static final int guiWidth = 176, guiHeight = 157;
	private int selectedIndex = -1;
	private boolean firstTime;
	
	public ColorSelectorScreen(boolean firstTime)
	{
		super(new TranslationTextComponent(TITLE));
		this.firstTime = firstTime;
		for(int i = 0; i < ColorHandler.getColorSize(); i++)
		{
			if(ColorHandler.getColor(i) == ClientPlayerData.playerColor)
			{
				selectedIndex = i;
			}
		}
	}
	
	@Override
	public void init()
	{
		addButton(new ExtendedButton((width - guiWidth)/2 + 50, (height - guiHeight)/2 + 132, 76, 20, "Choose", button -> selectColor()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground();
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage = I18n.format(SELECT_COLOR);
		minecraft.fontRenderer.drawString(cacheMessage, (this.width / 2F) - font.getStringWidth(cacheMessage) / 2F, yOffset + 12, 0x404040);
		
		renderColorBoxes();
		
		super.render(mouseX, mouseY, partialTicks);
		
		renderSelectionBox();
		
		int index = getColorIndexAtMouse(mouseX, mouseY);
		if(index != -1)
			renderTooltip(ColorHandler.getName(index).getFormattedText(), mouseX, mouseY);
	}
	
	private void renderColorBoxes()
	{
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		//Beta colors
		for(int i = 0; i < 4; i++)
		{
			int color = ColorHandler.getColor(i) | 0xFF000000;
			int x = 21 + 34*i;
			fill(xOffset + x, yOffset + 32, xOffset + x + 32, yOffset + 48, color);
		}
		//Alpha colors
		for(int i = 0; i < 4; i++)
		{
			int color = ColorHandler.getColor(i + 4) | 0xFF000000;
			int x = 21 + 34*i;
			fill(xOffset + x, yOffset + 53, xOffset + x + 32, yOffset + 69, color);
		}
		//Troll colors
		for(int xIndex = 0; xIndex < 4; xIndex++)
			for(int yIndex = 0; yIndex < 3; yIndex++)
			{
				int color = ColorHandler.getColor(yIndex*4 + xIndex + 8) | 0xFF000000;
				int x = 21 + 34*xIndex;
				int y = 74 + 18*yIndex;
				fill(xOffset + x, yOffset + y, xOffset + x + 32, yOffset + y + 16, color);
			}
	}
	
	private void renderSelectionBox()
	{
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		if(selectedIndex != -1)
		{
			int x = 19 + (selectedIndex % 4)*34;
			int y = 30 + (selectedIndex /4)*18;
			if(selectedIndex >= 4)
				y += 3;
			if(selectedIndex >= 8)
				y += 3;
			GlStateManager.color3f(1F, 1F, 1F);
			this.minecraft.getTextureManager().bindTexture(guiBackground);
			this.blit(xOffset + x, yOffset + y, guiWidth, 0, 36, 20);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		if(mouseButton == 0)
		{
			int index = getColorIndexAtMouse(mouseX, mouseY);
			if(index != -1)
			{
				selectedIndex = index != selectedIndex ? index : -1;
				return true;
			}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	private int getColorIndexAtMouse(double mouseX, double mouseY)
	{
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		for(int x = 0; x < 4; x++)
			for(int y = 0; y < 5; y++)
			{
				int xPos = xOffset + 21 + x*34;
				int yPos = yOffset + 32 + y*18;
				if(y > 0)
					yPos += 3;
				if(y > 1)
					yPos += 3;
				if(mouseX >= xPos && mouseX < xPos + 32 && mouseY >= yPos && mouseY < yPos + 16)
				{
					return y*4 + x;
				}
			}
		return -1;
	}
	
	private void selectColor()
	{
		MSPacketHandler.sendToServer(new ColorSelectPacket(selectedIndex));
		ClientPlayerData.playerColor = ColorHandler.getColor(selectedIndex);
		this.minecraft.displayGuiScreen(null);
	}
	
	@Override
	public void removed()
	{
		if(firstTime && minecraft != null && minecraft.player != null)
		{
			ITextComponent message;
			if(ClientPlayerData.playerColor == ColorHandler.DEFAULT_COLOR)
				message = new TranslationTextComponent(DEFAULT_COLOR_SELECTED);
			else message = new TranslationTextComponent(COLOR_SELECTED);
			this.minecraft.player.sendMessage(new StringTextComponent("[Minestuck] ").appendSibling(message));
		}
	}
	
}