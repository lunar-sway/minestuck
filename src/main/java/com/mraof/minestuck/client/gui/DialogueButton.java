package com.mraof.minestuck.client.gui;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.List;

@MethodsReturnNonnullByDefault
public class DialogueButton extends ExtendedButton
{
	private final ResourceLocation gui;
	private final List<FormattedCharSequence> messageLines;
	public final int trueHeight;
	
	public DialogueButton(ResourceLocation gui, int xPos, int yPos, int width, int defaultHeight, Component displayString, OnPress handler)
	{
		super(xPos, yPos, width, defaultHeight, displayString, handler);
		this.gui = gui;
		
		Minecraft mc = Minecraft.getInstance();
		this.messageLines = mc.font.split(this.getMessage(), this.width - 8);
		
		this.trueHeight = messageLines.size() * 14;
	}
	
	//TODO default height value is used to determine if hovered over
	
	//TODO allow the box and text to shift to the right when selected to match original strife flashes
	
	
	
	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		Minecraft mc = Minecraft.getInstance();
		int k = !this.active ? 0 : (this.isHoveredOrFocused() ? 2 : 1);
		
		//final FormattedText buttonText = mc.font.substrByWidth(this.getMessage(), this.width);
		//final FormattedText buttonText = mc.font.split(this.getMessage(), this.width);
		//int wordWrapHeight = mc.font.wordWrapHeight(buttonText, this.width);
		
		guiGraphics.blitWithBorder(gui, this.getX(), this.getY(), 0, 176 + k * 20, this.width, trueHeight, 200, 20, 3, 3, 3, 3);
		
		//guiGraphics.drawWordWrap(mc.font, buttonText, this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, this.width, getFGColor());
		//guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
		int pY = this.getY() + (this.height - 8) / 2;
		for(FormattedCharSequence line : messageLines)
		{
			//TODO fix poor centering of text
			guiGraphics.drawString(mc.font, line, this.getX() + 10, pY, getFGColor(), false);
			pY += 9;
		}
	}
}
