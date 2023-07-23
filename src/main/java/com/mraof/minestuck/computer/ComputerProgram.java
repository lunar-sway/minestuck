package com.mraof.minestuck.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

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
	
	private static final HashMap<Integer, Class<? extends ComputerProgram>> programs = new HashMap<>();
	public static final ResourceLocation INVALID_ICON = new ResourceLocation(Minestuck.MOD_ID, "textures/gui/desktop_icon/invalid.png");
	
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
			return programs.get(id).getDeclaredConstructor().newInstance();
		} catch(Exception e)
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
	 */
	public void onInitGui(ComputerScreen gui)
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
	public abstract void paintGui(GuiGraphics guiGraphics, ComputerScreen gui, ComputerBlockEntity be);
	
	/**
	 * @return desktop icon for this program.
	 */
	public ResourceLocation getIcon()
	{
		return INVALID_ICON;
	}
}
