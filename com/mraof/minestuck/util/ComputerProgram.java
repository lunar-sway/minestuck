package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.tileentity.TileEntityComputer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//TODO Add javadoc to interface methods before pull request
@SideOnly(Side.CLIENT)
public abstract class ComputerProgram {
	
	private static HashMap<Integer, Class<? extends ComputerProgram>> programs = new HashMap();
	
	private static HashMap<Integer, ItemStack> disks = new HashMap();
	
	public static void registerProgram(int id, Class<? extends ComputerProgram> program, ItemStack disk) {
		if(programs.containsKey(id) && id != -1)
			throw new IllegalArgumentException("Program id "+id+" is already used!");
		programs.put(id, program);
		disks.put(id, disk);
	}
	
	public static ComputerProgram getProgram(int id) {
		try {
			return programs.get(id).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @return -2 if the item does not correspond to any program.
	 */
	public static int getProgramID(ItemStack item) {
		if(item.getItem().equals(Item.record11))
			return -1;
		item.stackSize = 1;
		for(int id : programs.keySet())
			if(disks.get(id).isItemEqual(item))
				return id;
		return -2;
	}
	
	public final int getId() {
		for(Entry<Integer, Class<? extends ComputerProgram>> entry : programs.entrySet())
			if(entry.getValue() == this.getClass())
				return entry.getKey();
		return -1;
	}
	
	public void onButtonPressed(TileEntityComputer te, GuiButton button) {}
	
	public void onInitGui(GuiComputer gui, List buttonList, ComputerProgram prevProgram) {}
	
	public void onUpdateGui(GuiComputer gui, List buttonList) {}
	
	//TODO I believe that this needs to be improved, but I may be wrong.
	public abstract void paintGui(GuiComputer gui, TileEntityComputer te);
	
	public abstract String getName();
	
}
