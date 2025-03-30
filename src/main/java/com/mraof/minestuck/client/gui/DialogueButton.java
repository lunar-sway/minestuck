package com.mraof.minestuck.client.gui;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

@MethodsReturnNonnullByDefault
public class DialogueButton extends Button
{
	public static final int TEXT_SPACING = 9;
	public static final int NORMAL_DEFAULT_HEIGHT = 17;
	
	private final ResourceLocation gui;
	private final boolean isResponse;
	private final List<FormattedCharSequence> messageLines;
	public final int trueHeight;
	public boolean wasHoveredOrFocused = false;
	
	public DialogueButton(ResourceLocation gui, boolean isResponse, int xPos, int yPos, int width, int defaultHeight, Component displayString, OnPress handler)
	{
		super(xPos, yPos, width, defaultHeight, displayString, handler, DEFAULT_NARRATION);
		this.gui = gui;
		this.isResponse = isResponse;
		
		Minecraft mc = Minecraft.getInstance();
		this.messageLines = mc.font.split(this.getMessage(), this.width - 12);
		
		//uses default height to start and with each successive line adds new width equal to the text spacing
		this.trueHeight = defaultHeight + ((messageLines.size() - 1) * TEXT_SPACING);
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
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		//overrides setting of boolean from AbstractWidget which uses normal height value
		this.isHovered = mouseOvertop(mouseX, mouseY);
		
		Minecraft mc = Minecraft.getInstance();
		boolean hoveredOrFocused = this.isHoveredOrFocused();
		int k = !this.active ? 0 : (hoveredOrFocused ? 2 : 1);
		int hoverFocusShift = isResponse ? (hoveredOrFocused ? 7 : 0) : 0;
		
		if(hoveredOrFocused && !wasHoveredOrFocused && this.active)
			Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.2F, 2.0F);
		wasHoveredOrFocused = isMouseOver(mouseX, mouseY) || isFocused();
		
		guiGraphics.blitWithBorder(gui, this.getX() + hoverFocusShift, this.getY(), 0, 176 + k * 20, this.width, trueHeight, 200, 20, 3, 3, 3, 3);
		
		int textX = this.getX() + hoverFocusShift + (isResponse ? 12 : 4);
		int pY = this.getY() + 5;
		
		if(this.getMessage().getString().equals("=>"))
		{
			guiGraphics.drawString(mc.font, this.getMessage(), textX , pY, getFGColor(), false);
			return;
		}
		
		for(int i = 0 ; i < messageLines.size(); i++)
		{
			//prepends a ">" to the first line of a response
			if(isResponse && i == 0)
				guiGraphics.drawString(mc.font, Component.literal(">"), textX - 6 , pY, getFGColor(), false);
			
			guiGraphics.drawString(mc.font, messageLines.get(i), textX , pY, getFGColor(), false);
			pY += TEXT_SPACING;
		}
	}
	
}