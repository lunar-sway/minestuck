package com.mraof.minestuck.computer;

import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.tileentity.ComputerTileEntity;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * The static interface will probably later be merged with DeployList,
 * GristStorage and other similar classes that store static data.
 * 
 * @author Kirderf1
 */
public abstract class ComputerProgram
{ //This is overall a bad way of handling programs. Should be rewritten
	
	private static HashMap<Integer, Class<? extends ComputerProgram>> programs = new HashMap<Integer, Class<? extends ComputerProgram>>();
	
	/**
	 * Should only be used client-side
	 */
	public static void registerProgramClass(int id, Class<? extends ComputerProgram> program)
	{
		if(programs.containsKey(id) || id == -1 || id == -2)
			throw new IllegalArgumentException("Program id " + id + " is already used!");
		programs.put(id, program);
	}
	
	/**
	 * Creates and returns a new computer program for the given id (or null if there is none)
	 * Should only be used in a client-side context due to gui sidedness!
	 */
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
	
	public final int getId()
	{
		for(Entry<Integer, Class<? extends ComputerProgram>> entry : programs.entrySet())
			if(entry.getValue() == this.getClass())
				return entry.getKey();
		return -1;
	}
	
	/**
	 * Called when the gui is created or if the player pressed the switch
	 * program button.
	 *
	 * @param prevProgram
	 *            The previous program, or null if the gui was just created.
	 */
	public void onInitGui(ComputerScreen gui, ComputerProgram prevProgram)
	{
	}
	
	/**
	 * Called when some related data have changed that may affect the program.
	 */
	public void onUpdateGui(ComputerScreen gui)
	{
	}
	
	/**
	 * Called when the gui is painted. This may not be a good way of doing this,
	 * but I do not know since I do not know very much about minecraft graphics.
	 */
	public abstract void paintGui(ComputerScreen gui, ComputerTileEntity te);
	
	/**
	 * Returns an unlocalized string of the name of the program. Used for the
	 * program button.
	 */
	public abstract String getName();
	
}
