package com.mraof.minestuck.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.computer.ClearMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

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
	private String message;
	
	private int index = 0;
	
	/**
	 * Creates an ArrayList of UnlocalizedString and returns it.
	 * The first item in the list must be the message above the buttons, and then it continues with the topmost
	 * button and down.
	 *
	 * @param be The {@link ComputerBlockEntity} this program is associated with, for access to related data.
	 */
	protected abstract InterfaceData getInterfaceData(ComputerBlockEntity be);
	
	protected record InterfaceData(UnlocalizedString message, List<ButtonData> buttonData)
	{
	}
	
	protected record ButtonData(UnlocalizedString message, Runnable onClick)
	{
	}
	
	public final void onButtonPressed(ComputerScreen screen, Button button)
	{
		Runnable runnable = buttonMap.get(button);
		
		if(runnable != null)
		{
			if(!screen.be.latestmessage.get(this.getId()).isEmpty())
				PacketDistributor.sendToServer(new ClearMessagePacket(screen.be.getBlockPos(), this.getId()));
			runnable.run();
		}
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
	
	@Override
	public final void onUpdateGui(ComputerScreen gui)
	{
		InterfaceData data = getInterfaceData(gui.be);
		
		message = data.message.translate();
		
		downButton.active = data.buttonData.size() >= index + 4;
		upButton.active = index > 0;
		
		for(int i = 0; i < 4; i++)
		{
			Button button = buttons.get(i);
			if(index + i < data.buttonData.size())
			{
				ButtonData buttonData = data.buttonData.get(index + i);
				button.active = true;
				button.setMessage(buttonData.message.asTextComponent());
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
		if(be.latestmessage.get(be.programSelected) == null || be.latestmessage.get(be.programSelected).isEmpty())
		{
			guiGraphics.drawString(font, message, (gui.width - ComputerScreen.xSize) / 2F + 15, (gui.height - ComputerScreen.ySize) / 2F + 45, gui.getTheme().data().textColor(), false);
		} else
		{
			guiGraphics.drawString(font, I18n.get(be.latestmessage.get(be.programSelected)), (gui.width - ComputerScreen.xSize) / 2F + 15, (gui.height - ComputerScreen.ySize) / 2F + 45, gui.getTheme().data().textColor(), false);
		}
	}
	
	/**
	 * Represents an unlocalized string and the possible format parameters.
	 * Is used to represent the value on the buttons, but also the message shown above the buttons.
	 *
	 * @see ButtonListProgram#getInterfaceData
	 */
	protected static class UnlocalizedString
	{
		String string;
		Object[] formatData;
		
		public UnlocalizedString(String str, Object... obj)
		{
			string = str;
			formatData = obj;
		}
		
		public String translate()
		{
			return I18n.get(string, formatData);
		}
		
		public Component asTextComponent()
		{
			return Component.translatable(string, formatData);
		}
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
