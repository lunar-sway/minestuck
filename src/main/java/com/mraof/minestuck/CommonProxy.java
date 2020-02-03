package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.computer.editmode.DeployList;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.skaianet.SessionHandler;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.computer.SburbClient;
import com.mraof.minestuck.computer.SburbServer;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import net.minecraft.item.ItemStack;

import static com.mraof.minestuck.world.gen.OreGeneration.setupOverworldOreGeneration;

public class CommonProxy
{
	public static void init()
	{
		MSCriteriaTriggers.register();
		MSEntityTypes.registerPlacements();
		MSFillerBlockTypes.init();
		
		//register ore generation
		setupOverworldOreGeneration();
		//GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register channel handler
		MSPacketHandler.setupChannel();
		
		//register grist costs and combination recipes
		AlchemyRecipes.registerVanillaRecipes();
		AlchemyRecipes.registerMinestuckRecipes();

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
