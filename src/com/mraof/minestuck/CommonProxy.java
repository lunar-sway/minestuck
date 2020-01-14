package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.SburbClient;
import com.mraof.minestuck.util.SburbServer;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import net.minecraft.item.ItemStack;

public class CommonProxy
{
	public static void init()
	{
		MSCriteriaTriggers.register();
		MSEntityTypes.registerPlacements();
		MSFillerBlockTypes.init();
		
		//register channel handler
		MSPacketHandler.setupChannel();
		
		//register grist costs and combination recipes
		AlchemyRecipes.registerVanillaRecipes();
		AlchemyRecipes.registerMinestuckRecipes();
		AlchemyRecipes.registerModRecipes();

		//register consort shop prices
		ConsortRewardHandler.registerMinestuckPrices();
		
		MSBiomes.init();
		ConsortDialogue.init();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(MSItems.CLIENT_DISK));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(MSItems.SERVER_DISK));
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}
}
