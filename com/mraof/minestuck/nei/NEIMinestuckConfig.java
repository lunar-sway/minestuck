package com.mraof.minestuck.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.asm.NEIModContainer;

import com.mraof.minestuck.util.Debug;

public class NEIMinestuckConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		// TODO Auto-generated method stub
		API.registerRecipeHandler(new AlchemiterHandler());
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Minestuck";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

}
