package com.mraof.minestuck.client.gui;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.List;

@MethodsReturnNonnullByDefault
public class DialogueButton extends ExtendedButton
{
	private final ResourceLocation gui;
	private final List<FormattedCharSequence> messageLines;
	public final int trueHeight;
	public boolean wasHoveredOrFocused = false;
	
	public DialogueButton(ResourceLocation gui, int xPos, int yPos, int width, int defaultHeight, Component displayString, OnPress handler)
	{
		super(xPos, yPos, width, defaultHeight, displayString, handler);
		this.gui = gui;
		
		Minecraft mc = Minecraft.getInstance();
		this.messageLines = mc.font.split(this.getMessage(), this.width - 8);
		
		this.trueHeight = messageLines.size() * 14;
	}
	
	@Override
	protected boolean clicked(double pMouseX, double pMouseY)
	{
		return activeAndVisible() && mouseOvertop(pMouseX, pMouseY);
	}
	
	@Override
	public boolean isMouseOver(double pMouseX, double pMouseY)
	{
		return activeAndVisible() && mouseOvertop(pMouseX, pMouseY);
	}
	
	private boolean mouseOvertop(double pMouseX, double pMouseY)
	{
		return pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + this.width && pMouseY < this.getY() + trueHeight;
	}
	
	private boolean activeAndVisible()
	{
		return this.active && this.visible;
	}
	
	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick)
	{
		super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
		
		//occurs after renderWidget where the value is checked
		wasHoveredOrFocused = isMouseOver(pMouseX, pMouseY);
	}
	
	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		//overrides setting of boolean from AbstractWidget which uses normal height value
		this.isHovered = mouseOvertop(mouseX, mouseY);
		
		Minecraft mc = Minecraft.getInstance();
		boolean hoveredOrFocused = this.isHoveredOrFocused();
		int k = !this.active ? 0 : (hoveredOrFocused ? 2 : 1);
		int hoverFocusShift = hoveredOrFocused ? 7 : 0;
		
		if(hoveredOrFocused && !wasHoveredOrFocused && this.active)
			Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.get(), 0.25F, 2.0F);
		
		guiGraphics.blitWithBorder(gui, this.getX() + hoverFocusShift, this.getY(), 0, 176 + k * 20, this.width, trueHeight, 200, 20, 3, 3, 3, 3);
		
		int pY = this.getY() + (this.height - 8) / 2;
		for(FormattedCharSequence line : messageLines)
		{
			//TODO fix poor centering of text
			guiGraphics.drawString(mc.font, line, this.getX() + 10 + hoverFocusShift, pY, getFGColor(), false);
			pY += 9;
		}
	}
}