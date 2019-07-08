package com.mraof.minestuck.util;

import com.mraof.minestuck.client.gui.GuiButtonImpl;
import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.network.ClearMessagePacket;
import com.mraof.minestuck.network.MinestuckPacketHandler;
import com.mraof.minestuck.network.skaianet.ComputerData;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public abstract class ButtonListProgram extends ComputerProgram {
	
	private LinkedHashMap<Button, UnlocalizedString> buttonMap = new LinkedHashMap<>();
	private Button upButton, downButton;
	private String message;
	
	private int index = 0;
	
	/**
	 * Creates an ArrayList of UnlocalizedString and returns it.
	 * The first item in the list must be the message above the buttons, and then it continues with the topmost
	 * button and down.
	 * @param te The TileEntityComputer this program is associated with, for access to related data.
	 */
	protected abstract ArrayList<UnlocalizedString> getStringList(TileEntityComputer te);
	
	/**
	 * Performs the action caused by pressing a button.
	 * @param te The computer, if needed.
	 * @param buttonName The unlocalized string from getStringList() associated with the pressed button.
	 * @param data Format data provided by getStringList().
	 */
	protected abstract void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data);
	
	@Override
	public final void onButtonPressed(TileEntityComputer te, Button button) {
		UnlocalizedString data = buttonMap.get(button);
		if (button == upButton)
			index--;
		else if (button == downButton)
			index++;
		else if(data != null) {
			if(!te.latestmessage.get(this.getId()).isEmpty())
				MinestuckPacketHandler.sendToServer(new ClearMessagePacket(ComputerData.createData(te).getLocation(), this.getId()));
			onButtonPressed(te, data.string, data.formatData);
		}
	}
	
	@Override
	public final void onInitGui(GuiComputer gui, ComputerProgram prevProgram)
	{
		if(prevProgram instanceof ButtonListProgram) 
		{
			ButtonListProgram program = (ButtonListProgram) prevProgram;
			buttonMap = program.buttonMap;
			downButton = program.downButton;
			upButton = program.upButton;
		}
	       	else 
		{
			if(prevProgram != null)
		       	{
				gui.clearButtons();
				gui.addButton(gui.programButton);
			}
			buttonMap.clear();
			for (int i = 0; i < 4; i++) {
				Button button = new GuiButtonImpl(gui, i+2, (gui.width - GuiComputer.xSize) / 2 +14, (gui.height - GuiComputer.ySize) / 2 +60 + i*24, 120, 20,"");
				buttonMap.put(button, new UnlocalizedString(""));
				gui.addButton(button);
			}
			
			upButton = new GuiButtonImpl(gui, -1, (gui.width - GuiComputer.xSize) / 2 +140, (gui.height - GuiComputer.ySize) / 2 +60, 20, 20,"^");
			gui.addButton(upButton);
			downButton = new GuiButtonImpl(gui, -1, (gui.width - GuiComputer.xSize) / 2 +140, (gui.height - GuiComputer.ySize) / 2 +132, 20, 20,"v");
			gui.addButton(downButton);
		}
	}
	
	@Override
	public final void onUpdateGui(GuiComputer gui)
	{
		downButton.active = false;
		upButton.active = index > 0;
		ArrayList<UnlocalizedString> list = getStringList(gui.te);
		if(!gui.te.latestmessage.get(this.getId()).isEmpty())
			list.add(1, new UnlocalizedString("computer.buttonClear"));
		int pos = -1;
		for(UnlocalizedString s : list) 
		{
			if(pos == -1) 
			{
				message = s.translate();
			}
		       	else
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
				buttonMap.put((Button) buttonMap.keySet().toArray()[pos-index], s);
			}
			pos++;
		}
		if(index == 0 && pos != 4)
			for(; pos < 4; pos++)
				buttonMap.put((Button) buttonMap.keySet().toArray()[pos-index], new UnlocalizedString(""));
		
		for(Entry<Button, UnlocalizedString> entry : buttonMap.entrySet()) {
			UnlocalizedString data = entry.getValue();
			entry.getKey().active = !data.string.isEmpty();
			entry.getKey().setMessage(data.translate());
		}
	}
	
	@Override
	public final void paintGui(GuiComputer gui, TileEntityComputer te) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindTexture(GuiComputer.guiBackground);
		int yOffset = (gui.height / 2) - (GuiComputer.ySize / 2);
		gui.blit((gui.width / 2) - (GuiComputer.xSize / 2), yOffset, 0, 0, GuiComputer.xSize, GuiComputer.ySize);
		if(te.latestmessage.get(te.programSelected) == null || te.latestmessage.get(te.programSelected).isEmpty())
			mc.fontRenderer.drawString(message, (gui.width - GuiComputer.xSize) / 2 + 15, (gui.height - GuiComputer.ySize) / 2 + 45, 4210752);
		else 
			mc.fontRenderer.drawString(I18n.format(te.latestmessage.get(te.programSelected)), (gui.width - GuiComputer.xSize) / 2  + 15, (gui.height - GuiComputer.ySize) / 2 + 45, 4210752);
	}
	
	/**
	 * Represents an unlocalized string and the possible format parameters.
	 * Is used to represent the value on the buttons, but also the message shown above the buttons.
	 * See getStringList().
	 */
	protected static class UnlocalizedString {
		String string;
		Object[] formatData;
		public UnlocalizedString(String str, Object... obj) {
			string = str;
			formatData = obj;
		}
		
		public String translate() {
			return I18n.format(string, formatData);
		}
		
	}
	
}
