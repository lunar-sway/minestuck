package com.mraof.minestuck.client.gui;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

@MethodsReturnNonnullByDefault
public class DialogueButton extends ExtendedButton
{
	private final ResourceLocation gui;
	
	public DialogueButton(ResourceLocation gui, int xPos, int yPos, int width, int height, Component displayString, OnPress handler)
	{
		super(xPos, yPos, width, height, displayString, handler);
		this.gui = gui;
	}
	
	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		Minecraft mc = Minecraft.getInstance();
		int k = !this.active ? 0 : (this.isHoveredOrFocused() ? 2 : 1);
		guiGraphics.blitWithBorder(gui, this.getX(), this.getY(), 0, 176 + k * 20, this.width, this.height, 200, 20, 3, 3, 3, 3);
		
		final FormattedText buttonText = mc.font.ellipsize(this.getMessage(), this.width - 6); // Remove 6 pixels so that the text is always contained within the button's borders
		guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
	}
}
