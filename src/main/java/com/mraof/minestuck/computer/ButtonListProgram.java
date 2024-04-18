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
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public abstract class ButtonListProgram extends ComputerProgram
{
	public static final String CLEAR_BUTTON = "minestuck.clear_button";
	
	private final LinkedHashMap<Button, UnlocalizedString> buttonMap = new LinkedHashMap<>();
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
	protected abstract ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be);
	
	/**
	 * Performs the action caused by pressing a button.
	 *
	 * @param be         The computer, if needed.
	 * @param buttonName The unlocalized string from getStringList() associated with the pressed button.
	 * @param data       Format data provided by getStringList().
	 */
	protected abstract void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data);
	
	public final void onButtonPressed(ComputerScreen screen, Button button)
	{
		UnlocalizedString data = buttonMap.get(button);
		
		if(data != null)
		{
			if(!screen.be.latestmessage.get(this.getId()).isEmpty())
				PacketDistributor.SERVER.noArg().send(new ClearMessagePacket(screen.be.getBlockPos(), this.getId()));
			onButtonPressed(screen.be, data.string, data.formatData);
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
		for(int i = 0; i < 4; i++)
			buttonMap.put(gui.addRenderableWidget(
					new ExtendedButton(xOffset + 14, yOffset + 60 + i * 24, 120, 20, Component.empty(), button -> onButtonPressed(gui, button))), new UnlocalizedString(""));
		
		upButton = gui.addRenderableWidget(new ArrowButton(true, gui));
		downButton = gui.addRenderableWidget(new ArrowButton(false, gui));
	}
	
	@Override
	public final void onUpdateGui(ComputerScreen gui)
	{
		downButton.active = false;
		upButton.active = index > 0;
		ArrayList<UnlocalizedString> list = getStringList(gui.be);
		if(!gui.be.latestmessage.get(this.getId()).isEmpty())
			list.add(1, new UnlocalizedString(CLEAR_BUTTON));
		
		int pos = -1;
		for(UnlocalizedString s : list)
		{
			if(pos == -1)
			{
				message = s.translate();
			} else
			{
				if(index > pos)
				{
					pos++;
					continue;
				}
				if(pos == index + 4)
				{
					downButton.active = true;
					break;
				}
				buttonMap.put((Button) buttonMap.keySet().toArray()[pos - index], s);
			}
			pos++;
		}
		
		if(index == 0 && pos != 4)
			for(; pos < 4; pos++)
			{
				if(pos >= 0) //can still be -1 in some instances, causing a crash
					buttonMap.put((Button) buttonMap.keySet().toArray()[pos - index], new UnlocalizedString(""));
			}
		
		for(Entry<Button, UnlocalizedString> entry : buttonMap.entrySet())
		{
			UnlocalizedString data = entry.getValue();
			entry.getKey().active = !data.string.isEmpty();
			entry.getKey().setMessage(data.asTextComponent());
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
	 * @see ButtonListProgram#getStringList
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
