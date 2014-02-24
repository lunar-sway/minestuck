package com.mraof.minestuck.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.tileentity.TileEntityComputer;

public abstract class ButtonListProgram extends ComputerProgram {
	
	private LinkedHashMap<GuiButton,ButtonData> buttonMap = new LinkedHashMap();
	private GuiButton upButton, downButton;
	private String string;
	
	private int index = 0;
	
	public abstract LinkedHashMap<String, Object[]> getStringList(TileEntityComputer te);
	
	public abstract void onButtonPressed(TileEntityComputer te, String buttonName, Object[] data);
	
	@Override
	public final void onButtonPressed(TileEntityComputer te, GuiButton button) {
		ButtonData data = buttonMap.get(button);
		if (button == upButton)
			index--;
		else if (button == downButton)
			index++;
		else if(data != null)
			onButtonPressed(te, data.buttonName, data.formatData);
	}
	
	@Override
	public final void onInitGui(GuiComputer gui, List buttonList, ComputerProgram prevProgram) {
		if(prevProgram instanceof ButtonListProgram) {
			ButtonListProgram program = (ButtonListProgram) prevProgram;
		} else {
			
			for (int i = 0; i < 4; i++) {
				GuiButton button = new GuiButton(i+2, (gui.width - gui.xSize) / 2 +14, (gui.height - gui.ySize) / 2 +60 + i*24, 120, 20,"");
				buttonMap.put(button, null);
				buttonList.add(button);
			}
			
			upButton = new GuiButton(-1, (gui.width - gui.xSize) / 2 +140, (gui.height - gui.ySize) / 2 +60, 20, 20,"^");
			buttonList.add(upButton);
			downButton = new GuiButton(-1, (gui.width - gui.xSize) / 2 +140, (gui.height - gui.ySize) / 2 +132, 20, 20,"v");
			buttonList.add(downButton);
		}
	}
	
	@Override
	public final void onUpdateGui(GuiComputer gui, List buttonList) {
		downButton.enabled = false;
		upButton.enabled = index > 0;
		LinkedHashMap<String, Object[]> map = getStringList(gui.te);
		int pos = 0;
		for(Entry<String, Object[]> entry : map.entrySet())
			if(entry.getKey().equals("displayMessage")) {
				Object[] newArray = new Object[entry.getValue().length-1];
				System.arraycopy(entry.getValue(), 1, newArray, 0, newArray.length);
				string = StatCollector.translateToLocalFormatted((String) entry.getValue()[0], newArray);
			} else {
				if(index > pos) {
					pos++;
					continue;
				}
				if(pos >= index+4) {
					downButton.enabled = true;
					break;
				}
				buttonMap.put((GuiButton) buttonMap.keySet().toArray()[pos-index], new ButtonData(entry.getKey(), entry.getValue()));
			}
	}
	
	private static class ButtonData {
		String buttonName;
		Object[] formatData;
		ButtonData(String str, Object[] obj) {
			buttonName = str;
			formatData = obj;
		}
	}
	
}
