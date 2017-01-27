package com.mraof.minestuck.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.client.gui.GuiComputer;
import com.mraof.minestuck.tileentity.TileEntityComputer;

/**
 * The static interface will probably later be merged with DeployList,
 * GristStorage and other similar classes that store static data.
 * 
 * @author Kirderf1
 */
public abstract class ComputerProgram
{ //This is an unnecessary abstract way of handling programs. Should be replaced
	
	private static HashMap<Integer, Class<? extends ComputerProgram>> programs = new HashMap<Integer, Class<? extends ComputerProgram>>();
	
	private static HashMap<Integer, ItemStack> disks = new HashMap<Integer, ItemStack>();
	
	/**
	 * Registers a program class to the list.
	 * 
	 * @param id
	 *            The program id. If it is already used, the method will throw
	 *            an IllegalArgumentException.
	 * @param program
	 *            The class of the program to be registered.
	 * @param disk
	 *            The item that will serve as the disk that installs the
	 *            program.
	 */
	public static void registerProgram(int id, Class<? extends ComputerProgram> program, ItemStack disk)
	{
		if(programs.containsKey(id) || id == -1 || id == -2)
			throw new IllegalArgumentException("Program id " + id + " is already used!");
		programs.put(id, program);
		disks.put(id, disk);
	}
	
	public static ComputerProgram getProgram(int id)
	{
		try
		{
			return programs.get(id).newInstance();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns the id of the program. Note that it returns -2 if the item does
	 * not correspond to any program, as -1 is used for an easter egg.
	 */
	public static int getProgramID(ItemStack item)
	{
		if(item.isEmpty())
			return -2;
		item = item.copy();
		if(item.getItem().equals(Items.RECORD_11))
			return -1;
		item.setCount(1);
		for(int id : disks.keySet())
			if(disks.get(id).isItemEqual(item))
				return id;
		return -2;
	}
	
	@Nonnull
	public static ItemStack getItem(int id)
	{
		if(id == -1)
			return new ItemStack(Items.RECORD_11);
		return disks.get(id).copy();
	}
	
	public final int getId()
	{
		for(Entry<Integer, Class<? extends ComputerProgram>> entry : programs.entrySet())
			if(entry.getValue() == this.getClass())
				return entry.getKey();
		return -1;
	}
	
	public void onButtonPressed(TileEntityComputer te, GuiButton button)
	{
	}
	
	/**
	 * Called when the gui is created or if the player pressed the switch
	 * program button.
	 * 
	 * @param buttonList
	 *            The button list. Note that the list isn't cleared if
	 *            prevProgram isn't null, so you have to clear it and re-add the
	 *            program button if you're not going to re-use them. (which you
	 *            probably won't unless the previous program had a similar
	 *            layout.)
	 * @param prevProgram
	 *            The previous program, or null if the gui was just created.
	 */
	public void onInitGui(GuiComputer gui, List<GuiButton> buttonList, ComputerProgram prevProgram)
	{
	}
	
	/**
	 * Called when some related data have changed that may affect the program.
	 */
	public void onUpdateGui(GuiComputer gui, List<GuiButton> buttonList)
	{
	}
	
	/**
	 * Called when something breaks the computer block. (or if the disk is
	 * ejected when that feature is added)
	 */
	public void onClosed(TileEntityComputer te)
	{
	}
	
	/**
	 * Called when the gui is painted. This may not be a good way of doing this,
	 * but I do not know since I do not know very much about minecraft graphics.
	 */
	public abstract void paintGui(GuiComputer gui, TileEntityComputer te);
	
	/**
	 * Returns an unlocalized string of the name of the program. Used for the
	 * program button.
	 */
	public abstract String getName();
	
}
