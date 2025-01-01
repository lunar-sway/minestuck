package com.mraof.minestuck.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The static interface will probably later be merged with DeployList,
 * GristStorage and other similar classes that store static data.
 *
 * @author Kirderf1
 */
public abstract class ComputerProgram
{ //This is overall a bad way of handling programs. Should be rewritten
	
	private static final HashMap<ProgramType, Supplier<? extends ComputerProgram>> programs = new HashMap<>();
	
	/**
	 * Should only be used client-side
	 */
	public static void registerProgramGui(ProgramType programType, Supplier<? extends ComputerProgram> program)
	{
		if(programs.containsKey(programType))
			throw new IllegalArgumentException("Program type " + programType + " is already registered!");
		programs.put(programType, program);
	}
	
	/**
	 * Creates and returns a new computer program for the given id (or null if there is none)
	 * Should only be used in a client-side context due to gui sidedness!
	 */
	public static ComputerProgram getProgram(ProgramType programType)
	{
		return programs.get(programType).get();
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
}
