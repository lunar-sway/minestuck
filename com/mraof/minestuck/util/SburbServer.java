package com.mraof.minestuck.util;

import java.util.HashMap;

import com.mraof.minestuck.tileentity.TileEntityComputer;

public class SburbServer extends ComputerProgram {
	
	@Override
	public boolean isButtonListProgram() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public HashMap<String, Object[]> getStringList(TileEntityComputer computer) {
		// TODO Auto-generated method stub
		return super.getStringList(computer);
	}
	
	@Override
	public void onButtonPressed(TileEntityComputer computer, String buttonName,
			Object[] data) {
		// TODO Auto-generated method stub
		super.onButtonPressed(computer, buttonName, data);
	}
	
}
