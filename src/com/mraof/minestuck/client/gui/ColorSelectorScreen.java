package com.mraof.minestuck.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.ColorSelectPacket;
import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class ColorSelectorScreen extends Screen
{
	
	private static final ResourceLocation guiBackground = new ResourceLocation("minestuck", "textures/gui/color_selector.png");
	private static final int guiWidth = 176, guiHeight = 157;
	private int selectedColor;
	private boolean firstTime;
	
	public ColorSelectorScreen(boolean firstTime)
	{
		super(new StringTextComponent("Color Selector"));
		this.firstTime = firstTime;
		selectedColor = ColorCollector.playerColor;
	}
	
	@Override
	public void init()
	{
		addButton(new GuiButtonExt((width - guiWidth)/2 + 50, (height - guiHeight)/2 + 132, 76, 20, "Choose", button -> selectColor()));
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		int xOffset = (width - guiWidth)/2;
		int yOffset = (height - guiHeight)/2;
		
		this.renderBackground();
		
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.minecraft.getTextureManager().bindTexture(guiBackground);
		this.blit(xOffset, yOffset, 0, 0, guiWidth, guiHeight);
		
		String cacheMessage = I18n.format("gui.selectColor");
		minecraft.fontRenderer.drawString(cacheMessage, (this.width / 2) - font.getStringWidth(cacheMessage) / 2, yOffset + 12, 0x404040);
		
		for(int i = 0; i < 4; i++)
		{
			int color = ColorCollector.getColor(i) | 0xFF000000;
			int x = 21 + 34*i;
			fill(xOffset + x, yOffset + 32, xOffset + x + 32, yOffset + 48, color);
		}
		for(int i = 0; i < 4; i++)
		{
			int color = ColorCollector.getColor(i + 4) | 0xFF000000;
			int x = 21 + 34*i;
			fill(xOffset + x, yOffset + 53, xOffset + x + 32, yOffset + 69, color);
		}
		for(int xIndex = 0; xIndex < 4; xIndex++)
			for(int yIndex = 0; yIndex < 3; yIndex++)
			{
				int color = ColorCollector.getColor(yIndex*4 + xIndex + 8) | 0xFF000000;
				int x = 21 + 34*xIndex;
				int y = 74 + 18*yIndex;
				fill(xOffset + x, yOffset + y, xOffset + x + 32, yOffset + y + 16, color);
			}
		
		super.render(mouseX, mouseY, partialTicks);
		
		if(selectedColor != -1)
		{
			int x = 19 + (selectedColor % 4)*34;
			int y = 30 + (selectedColor/4)*18;
			if(selectedColor >= 4)
				y += 3;
			if(selectedColor >= 8)
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
						int index = y*4 + x;
						selectedColor = index != selectedColor ? index : -1;
						return true;
					}
				}
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	public void selectColor()
	{
		MSPacketHandler.sendToServer(new ColorSelectPacket(this.selectedColor));
		ColorCollector.playerColor = selectedColor;
		this.minecraft.displayGuiScreen(null);
	}
	
	@Override
	public void onClose()
	{
		if(firstTime && minecraft != null && minecraft.player != null)
		{
			ITextComponent message;
			if(ColorCollector.playerColor == -1)
				message = new TranslationTextComponent("message.selectDefaultColor");
			else message = new TranslationTextComponent("message.selectColor");
			this.minecraft.player.sendMessage(new StringTextComponent("[Minestuck] ").appendSibling(message));
		}
	}
	
}