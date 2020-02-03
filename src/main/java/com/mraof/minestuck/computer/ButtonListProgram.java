package com.mraof.minestuck.computer;

import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.network.ClearMessagePacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public abstract class ButtonListProgram extends ComputerProgram
{
	public static final String CLEAR_BUTTON = "minestuck.clear_button";
	
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
	protected abstract ArrayList<UnlocalizedString> getStringList(ComputerTileEntity te);
	
	/**
	 * Performs the action caused by pressing a button.
	 * @param te The computer, if needed.
	 * @param buttonName The unlocalized string from getStringList() associated with the pressed button.
	 * @param data Format data provided by getStringList().
	 */
	protected abstract void onButtonPressed(ComputerTileEntity te, String buttonName, Object[] data);
	
	public final void onButtonPressed(ComputerTileEntity te, Button button) {
		UnlocalizedString data = buttonMap.get(button);
		if (button == upButton)
			index--;
		else if (button == downButton)
			index++;
		else if(data != null) {
			if(!te.latestmessage.get(this.getId()).isEmpty())
				MSPacketHandler.sendToServer(new ClearMessagePacket(te.getPos(), this.getId()));
			onButtonPressed(te, data.string, data.formatData);
		}
	}
	
	@Override
	public final void onInitGui(ComputerScreen gui, ComputerProgram prevProgram)
	{
		if(prevProgram != null)
		{
			gui.clearButtons();
			gui.addButton(gui.programButton);
		}
		buttonMap.clear();
		for(int i = 0; i < 4; i++)
		{
			Button button = new GuiButtonExt((gui.width - ComputerScreen.xSize) / 2 + 14, (gui.height - ComputerScreen.ySize) / 2 + 60 + i * 24, 120, 20, "", button1 -> onButtonPressed(gui.te, button1));
			buttonMap.put(button, new UnlocalizedString(""));
			gui.addButton(button);
		}
		
		upButton = new GuiButtonExt((gui.width - ComputerScreen.xSize) / 2 + 140, (gui.height - ComputerScreen.ySize) / 2 + 60, 20, 20, "^", button1 -> onButtonPressed(gui.te, button1));
		gui.addButton(upButton);
		downButton = new GuiButtonExt((gui.width - ComputerScreen.xSize) / 2 + 140, (gui.height - ComputerScreen.ySize) / 2 + 132, 20, 20, "v", button1 -> onButtonPressed(gui.te, button1));
		gui.addButton(downButton);
	}
	
	@Override
	public final void onUpdateGui(ComputerScreen gui)
	{
		downButton.active = false;
		upButton.active = index > 0;
		ArrayList<UnlocalizedString> list = getStringList(gui.te);
		if(!gui.te.latestmessage.get(this.getId()).isEmpty())
			list.add(1, new UnlocalizedString(CLEAR_BUTTON));
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
	public final void paintGui(ComputerScreen gui, ComputerTileEntity te) {
		Minecraft mc = Minecraft.getInstance();
		mc.getTextureManager().bindTexture(ComputerScreen.guiBackground);
		int yOffset = (gui.height / 2) - (ComputerScreen.ySize / 2);
		gui.blit((gui.width / 2) - (ComputerScreen.xSize / 2), yOffset, 0, 0, ComputerScreen.xSize, ComputerScreen.ySize);
		if(te.latestmessage.get(te.programSelected) == null || te.latestmessage.get(te.programSelected).isEmpty())
			mc.fontRenderer.drawString(message, (gui.width - ComputerScreen.xSize) / 2 + 15, (gui.height - ComputerScreen.ySize) / 2 + 45, 4210752);
		else 
			mc.fontRenderer.drawString(I18n.format(te.latestmessage.get(te.programSelected)), (gui.width - ComputerScreen.xSize) / 2  + 15, (gui.height - ComputerScreen.ySize) / 2 + 45, 4210752);
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
