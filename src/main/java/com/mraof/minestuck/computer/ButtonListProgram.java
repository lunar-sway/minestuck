package com.mraof.minestuck.computer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.computer.ClearMessagePacket;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.gui.widget.ExtendedButton;

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
	 * @param be The {@link ComputerBlockEntity} this program is associated with, for access to related data.
	 */
	protected abstract ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be);
	
	/**
	 * Performs the action caused by pressing a button.
	 * @param be The computer, if needed.
	 * @param buttonName The unlocalized string from getStringList() associated with the pressed button.
	 * @param data Format data provided by getStringList().
	 */
	protected abstract void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data);
	
	public final void onButtonPressed(ComputerScreen screen, Button button)
	{
		UnlocalizedString data = buttonMap.get(button);
		if(button == upButton)
			index--;
		else if(button == downButton)
			index++;
		else if(data != null)
		{
			if(!screen.be.latestmessage.get(this.getId()).isEmpty())
				MSPacketHandler.sendToServer(new ClearMessagePacket(screen.be.getBlockPos(), this.getId()));
			onButtonPressed(screen.be, data.string, data.formatData);
		}
		screen.updateGui();
	}
	
	@Override
	public final void onInitGui(ComputerScreen gui)
	{
		buttonMap.clear();
		for(int i = 0; i < 4; i++)
		{
			Button button = new ExtendedButton((gui.width - ComputerScreen.xSize) / 2 + 14, (gui.height - ComputerScreen.ySize) / 2 + 60 + i * 24, 120, 20, TextComponent.EMPTY, button1 -> onButtonPressed(gui, button1));
			buttonMap.put(button, new UnlocalizedString(""));
			gui.addRenderableWidget(button);
		}
		
		upButton = new ExtendedButton((gui.width - ComputerScreen.xSize) / 2 + 140, (gui.height - ComputerScreen.ySize) / 2 + 60, 20, 20, new TextComponent("^"), button1 -> onButtonPressed(gui, button1));
		gui.addRenderableWidget(upButton);
		downButton = new ExtendedButton((gui.width - ComputerScreen.xSize) / 2 + 140, (gui.height - ComputerScreen.ySize) / 2 + 132, 20, 20, new TextComponent("v"), button1 -> onButtonPressed(gui, button1));
		gui.addRenderableWidget(downButton);
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
	public final void paintGui(PoseStack poseStack, ComputerScreen gui, ComputerBlockEntity be)
	{
		int yOffset = (gui.height / 2) - (ComputerScreen.ySize / 2);
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, ComputerScreen.guiBackground);
		gui.blit(poseStack, (gui.width / 2) - (ComputerScreen.xSize / 2), yOffset, 0, 0, ComputerScreen.xSize, ComputerScreen.ySize);
		
		Font font = Minecraft.getInstance().font;
		if(be.latestmessage.get(be.programSelected) == null || be.latestmessage.get(be.programSelected).isEmpty())
			font.draw(poseStack, message, (gui.width - ComputerScreen.xSize) / 2 + 15, (gui.height - ComputerScreen.ySize) / 2 + 45, 4210752);
		else
			font.draw(poseStack, I18n.get(be.latestmessage.get(be.programSelected)), (gui.width - ComputerScreen.xSize) / 2  + 15, (gui.height - ComputerScreen.ySize) / 2 + 45, 4210752);
	}
	
	/**
	 * Represents an unlocalized string and the possible format parameters.
	 * Is used to represent the value on the buttons, but also the message shown above the buttons.
	 * See getStringList().
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
			return new TranslatableComponent(string, formatData);
		}
	}
	
}
