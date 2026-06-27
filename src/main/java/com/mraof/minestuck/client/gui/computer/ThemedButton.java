package com.mraof.minestuck.client.gui.computer;

import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

import com.mraof.minestuck.computer.theme.ComputerTheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.locale.Language;

/**
 * Themed version of ExtendedButton
 * <p>
 * The textures must be nine slice type to be rendered
 */
public class ThemedButton extends ExtendedButton
{
	protected WidgetSprites sprites;
	
	public ThemedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, ComputerTheme theme)
	{
		this(xPos, yPos, width, height, displayString, handler, DEFAULT_NARRATION, theme);
	}
	
	public ThemedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler, CreateNarration createNarration, ComputerTheme theme)
	{
		super(xPos, yPos, width, height, displayString, handler, createNarration);
		
		this.sprites = widgetSpritesFromTheme(theme);
	}
	
	public ThemedButton(Button.Builder builder)
	{
		super(builder);
		this.sprites = SPRITES;
	}
	
	public static WidgetSprites widgetSpritesFromTheme(ComputerTheme theme)
	{
		ComputerTheme.Data data = theme.data();
		ResourceLocation button = data.buttonPath().orElse(SPRITES.enabled());
		ResourceLocation disabled = data.buttonDisabledPath().orElse(SPRITES.disabled());
		ResourceLocation highlighted = data.buttonHighlitedPath().orElse(SPRITES.enabledFocused());
		return new WidgetSprites(button, disabled, highlighted);
	}
	
	public void setTheme(ComputerTheme theme)
	{
		sprites = widgetSpritesFromTheme(theme);
	}
	
	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
	{
		Minecraft mc = Minecraft.getInstance();
		guiGraphics.blitSprite(sprites.get(this.active, this.isHoveredOrFocused()), this.getX(), this.getY(), this.getWidth(), this.getHeight());
		
		final FormattedText buttonText = mc.font.ellipsize(this.getMessage(), this.width - 6); // Remove 6 pixels so that the text is always contained within the button's borders
		guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, getFGColor());
	}
}
