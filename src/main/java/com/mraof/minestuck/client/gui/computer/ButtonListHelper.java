package com.mraof.minestuck.client.gui.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ButtonListHelper
{
	public static final String CLEAR_BUTTON = "minestuck.clear_button";
	
	private final Map<Button, Runnable> buttonMap = new HashMap<>();
	private List<ButtonData> buttonsData = List.of();
	private final List<Button> buttons = new ArrayList<>(4);
	private Button upButton, downButton;
	
	private int index = 0;
	
	public record ButtonData(Component message, Runnable onClick)
	{
	}
	
	private void onButtonPressed(Button button)
	{
		Runnable runnable = buttonMap.get(button);
		
		if(runnable != null)
			runnable.run();
		
		updateButtons();
	}
	
	private void onArrowPressed(boolean reverse)
	{
		if(reverse) index--;
		else index++;
		
		updateButtons();
	}
	
	public void init(ThemedScreen gui)
	{
		gui.setOffsets();
		
		buttonMap.clear();
		buttons.clear();
		for(int i = 0; i < 4; i++)
		{
			ExtendedButton button = new ExtendedButton(gui.xOffset + 14, gui.yOffset + 60 + i * 24, 120, 20, Component.empty(), this::onButtonPressed);
			gui.addRenderableWidget(button);
			buttons.add(button);
		}
		
		upButton = gui.addRenderableWidget(new ArrowButton(true, gui));
		downButton = gui.addRenderableWidget(new ArrowButton(false, gui));
	}
	
	public void updateButtons(List<ButtonData> buttonsDataIn)
	{
		this.buttonsData = buttonsDataIn;
		updateButtons();
	}
	
	private void updateButtons()
	{
		downButton.active = buttonsData.size() >= index + 4;
		upButton.active = index > 0;
		
		for(int i = 0; i < 4; i++)
		{
			Button button = buttons.get(i);
			if(index + i < buttonsData.size())
			{
				ButtonData buttonData = buttonsData.get(index + i);
				button.active = true;
				button.setMessage(buttonData.message);
				buttonMap.put(button, buttonData.onClick);
			} else
			{
				button.active = false;
				button.setMessage(Component.empty());
				buttonMap.remove(button);
			}
		}
	}
	
	private class ArrowButton extends ExtendedButton
	{
		boolean reverse;
		ThemedScreen gui;
		
		ArrowButton(boolean reverse, ThemedScreen gui)
		{
			super((gui.width - ComputerScreen.GUI_WIDTH) / 2 + 140, (gui.height - ComputerScreen.GUI_HEIGHT) / 2 + (reverse ? 60 : 132), 20, 20, Component.empty(), b -> onArrowPressed(reverse));
			this.reverse = reverse;
			this.gui = gui;
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			guiGraphics.blit(gui.selectedTheme.data().texturePath(), getX(), getY(), 158, reverse ? 0 : 20, 20, 20);
		}
	}
}
