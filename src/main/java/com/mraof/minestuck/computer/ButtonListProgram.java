package com.mraof.minestuck.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ButtonListProgram extends ComputerProgram
{
	public static final String CLEAR_BUTTON = "minestuck.clear_button";
	
	private final Map<Button, Runnable> buttonMap = new HashMap<>();
	private final List<Button> buttons = new ArrayList<>(4);
	private Button upButton, downButton;
	private Component message;
	
	private int index = 0;
	
	protected record ButtonData(Component message, Runnable onClick)
	{
	}
	
	public final void onButtonPressed(ComputerScreen screen, Button button)
	{
		Runnable runnable = buttonMap.get(button);
		
		if(runnable != null)
			runnable.run();
		
		screen.updateGui();
	}
	
	public final void onArrowPressed(ComputerScreen screen, boolean reverse)
	{
		if(reverse) index--;
		else index++;
		
		screen.updateGui();
	}
	
	@Override
	public final void onInitGui(ComputerScreen gui)
	{
		var xOffset = (gui.width - ComputerScreen.xSize) / 2;
		var yOffset = (gui.height - ComputerScreen.ySize) / 2;
		
		buttonMap.clear();
		buttons.clear();
		for(int i = 0; i < 4; i++)
		{
			ExtendedButton button = new ExtendedButton(xOffset + 14, yOffset + 60 + i * 24, 120, 20, Component.empty(), clieckedButton -> onButtonPressed(gui, clieckedButton));
			gui.addRenderableWidget(button);
			buttons.add(button);
		}
		
		upButton = gui.addRenderableWidget(new ArrowButton(true, gui));
		downButton = gui.addRenderableWidget(new ArrowButton(false, gui));
	}
	
	protected final void updateMessage(Component message)
	{
		this.message = message;
	}
	
	protected final void updateButtons(List<ButtonData> buttonsData)
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
	
	@Override
	public final void paintGui(GuiGraphics guiGraphics, ComputerScreen gui, ComputerBlockEntity be)
	{
		Font font = Minecraft.getInstance().font;
		
		guiGraphics.drawString(font, this.message,
				(gui.width - ComputerScreen.xSize) / 2 + 15, (gui.height - ComputerScreen.ySize) / 2 + 45,
				gui.getTheme().data().textColor(), false);
	}
	
	protected class ArrowButton extends ExtendedButton
	{
		boolean reverse;
		ComputerScreen gui;
		
		public ArrowButton(boolean reverse, ComputerScreen gui)
		{
			super((gui.width - ComputerScreen.xSize) / 2 + 140, (gui.height - ComputerScreen.ySize) / 2 + (reverse ? 60 : 132), 20, 20, Component.empty(), b -> onArrowPressed(gui, reverse));
			this.reverse = reverse;
			this.gui = gui;
		}
		
		@Override
		public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
		{
			RenderSystem.setShaderColor(1, 1, 1, 1);
			guiGraphics.blit(gui.getTheme().data().texturePath(), getX(), getY(), 158, reverse ? 0 : 20, 20, 20);
		}
	}
}
