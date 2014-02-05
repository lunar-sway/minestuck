package com.mraof.minestuck.util;

import java.util.HashMap;

import com.mraof.minestuck.tileentity.TileEntityComputer;

public abstract class ComputerProgram {
	
	private static HashMap<Integer, ComputerProgram> programs = new HashMap();
	
	public static void registerProgram(int id, ComputerProgram program) {
		if(programs.containsKey(id))
			throw new IllegalArgumentException("Program id "+id+" is already used!");
		programs.put(id, program);
	}
	
	public static ComputerProgram getProgram(int id) {
		return programs.get(id);
	}
	
	//Should be moved into an abstract underclass instead, when I figure out more on how non-button list programs should works
	public abstract boolean isButtonListProgram();
	
	/**
	 * Used by button list programs.
	 * @param computer The tile entity representing the computer.
	 * @return An array with the current message at position 0, and the content of the buttons at 1+.
	 * Only the first string should be localized.
	 */
	public HashMap<String, Object[]> getStringList(TileEntityComputer computer) {return new HashMap();}
	
	public void onButtonPressed(TileEntityComputer computer, String buttonName, Object[] data) {}
	
}
