package com.mraof.minestuck.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIMinestuckConfig implements IConfigureNEI {
	
	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new AlchemiterHandler());
		
		API.registerRecipeHandler(new DesignixHandler());
		API.registerUsageHandler(new DesignixHandler());
	}

	@Override
	public String getName() {
		return "Minestuck";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

}
