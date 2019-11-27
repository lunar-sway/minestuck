package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.tracker.PlayerTracker;
import com.mraof.minestuck.util.*;
import com.mraof.minestuck.world.biome.MSBiomes;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.feature.MSStructureProcessorTypes;
import com.mraof.minestuck.world.gen.feature.MSFillerBlockTypes;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public static void init()
	{
		MSCriteriaTriggers.register();
		MSStructureProcessorTypes.call();
		MSEntityTypes.registerPlacements();
		MSFillerBlockTypes.init();
		
		//register ore generation
		OreHandler oreHandler = new OreHandler();
		//GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register GUI handler
		//NetworkRegistry.INSTANCE.registerGuiHandler(Minestuck.instance, new GuiHandler());
		
		//Register event handlers
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(PlayerTracker.instance);
		MinecraftForge.EVENT_BUS.register(ServerEventHandler.instance);
		
		
		//register channel handler
		MSPacketHandler.setupChannel();
		
		//Register structures
		//MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		//StructureCastlePieces.registerComponents();
		//MapGenLandStructure.registerStructures();
		//ConsortVillageComponents.registerComponents();
		
		//register grist costs and combination recipes
		AlchemyRecipes.registerVanillaRecipes();
		AlchemyRecipes.registerMinestuckRecipes();
		AlchemyRecipes.registerModRecipes();
		
		//register smelting recipes
		CraftingRecipes.registerSmelting();

		//register consort shop prices
		ConsortRewardHandler.registerMinestuckPrices();
		
		//Register loot functionality objects
		MSLootTables.registerLootClasses();
		
		MSBiomes.init();
		ConsortDialogue.init();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(MSItems.CLIENT_DISK));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(MSItems.SERVER_DISK));
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}
}
