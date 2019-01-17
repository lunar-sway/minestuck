package com.mraof.minestuck;

import com.mraof.minestuck.advancements.MinestuckCriteriaTriggers;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.client.gui.GuiHandler;
import com.mraof.minestuck.editmode.DeployList;
import com.mraof.minestuck.editmode.ServerEditHandler;
import com.mraof.minestuck.entity.MinestuckEntities;
import com.mraof.minestuck.entity.consort.ConsortDialogue;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.event.MinestuckFluidHandler;
import com.mraof.minestuck.event.ServerEventHandler;
import com.mraof.minestuck.item.ItemMinestuckCandy;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.skaianet.SessionHandler;
import com.mraof.minestuck.tileentity.TileEntityAlchemiter;
import com.mraof.minestuck.tileentity.TileEntityComputer;
import com.mraof.minestuck.tileentity.TileEntityCrockerMachine;
import com.mraof.minestuck.tileentity.TileEntityCruxtruder;
import com.mraof.minestuck.tileentity.TileEntityGate;
import com.mraof.minestuck.tileentity.TileEntityHolopad;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.tileentity.TileEntityPunchDesignix;
import com.mraof.minestuck.tileentity.TileEntitySburbMachine;
import com.mraof.minestuck.tileentity.TileEntitySkaiaPortal;
import com.mraof.minestuck.tileentity.TileEntityTotemLathe;
import com.mraof.minestuck.tileentity.TileEntityTransportalizer;
import com.mraof.minestuck.tileentity.TileEntityUraniumCooker;
import com.mraof.minestuck.tracker.ConnectionListener;
import com.mraof.minestuck.tracker.MinestuckPlayerTracker;
import com.mraof.minestuck.util.ComputerProgram;
import com.mraof.minestuck.util.CraftingRecipes;
import com.mraof.minestuck.util.KindAbstratusList;
import com.mraof.minestuck.util.MinestuckSoundHandler;
import com.mraof.minestuck.util.SburbClient;
import com.mraof.minestuck.util.SburbServer;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.biome.BiomeMinestuck;
import com.mraof.minestuck.world.gen.OreHandler;
import com.mraof.minestuck.world.gen.structure.StructureCastlePieces;
import com.mraof.minestuck.world.gen.structure.StructureCastleStart;
import com.mraof.minestuck.world.gen.structure.temple.StructureTempleStart;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.structure.MapGenLandStructure;
import com.mraof.minestuck.world.lands.structure.village.ConsortVillageComponents;
import com.mraof.minestuck.world.storage.MinestuckSaveHandler;
import com.mraof.minestuck.world.storage.loot.MinestuckLoot;

import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(MinestuckSoundHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckBlocks.class);
		MinecraftForge.EVENT_BUS.register(MinestuckItems.class);
		MinecraftForge.EVENT_BUS.register(BiomeMinestuck.class);
		
		MinestuckSoundHandler.initSound();
		
		MinestuckCriteriaTriggers.register();
	}
	
	public void init()
	{
		MinestuckDimensionHandler.register();
		
		//register ore generation
		OreHandler oreHandler = new OreHandler();
		GameRegistry.registerWorldGenerator(oreHandler, 0);
		
		//register GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(Minestuck.instance, new GuiHandler());
		
		MinestuckEntities.registerEntities();
		//register Tile Entities
		GameRegistry.registerTileEntity(TileEntitySkaiaPortal.class, "minestuck:gate_portal");
		GameRegistry.registerTileEntity(TileEntitySburbMachine.class, "minestuck:sburb_machine");
		GameRegistry.registerTileEntity(TileEntityPunchDesignix.class, "Minestuck:punch_designix");
		GameRegistry.registerTileEntity(TileEntityTotemLathe.class, "Minestuck:totem_lathe");
		GameRegistry.registerTileEntity(TileEntityAlchemiter.class,"Minestuck:alchemiter");
		GameRegistry.registerTileEntity(TileEntityCruxtruder.class, "Minestuck:cruxtruder");
		GameRegistry.registerTileEntity(TileEntityItemStack.class, "Minestuck:item_stack");
		GameRegistry.registerTileEntity(TileEntityCrockerMachine.class, "minestuck:crocker_machine");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "minestuck:computer_sburb");
		GameRegistry.registerTileEntity(TileEntityTransportalizer.class, "minestuck:transportalizer");
		GameRegistry.registerTileEntity(TileEntityGate.class, "minestuck:gate");
		GameRegistry.registerTileEntity(TileEntityUraniumCooker.class, "minestuck:uranium_cooker");
		GameRegistry.registerTileEntity(TileEntityHolopad.class, "minestuck:holopad");
		
		//Register event handlers
		MinecraftForge.EVENT_BUS.register(new MinestuckSaveHandler());
		MinecraftForge.EVENT_BUS.register(new MinestuckFluidHandler());
		MinecraftForge.EVENT_BUS.register(ServerEditHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckPlayerTracker.instance);
		MinecraftForge.EVENT_BUS.register(ServerEventHandler.instance);
		MinecraftForge.EVENT_BUS.register(MinestuckChannelHandler.instance);
		MinecraftForge.EVENT_BUS.register(new ConnectionListener());
		
		
		//register channel handler
		MinestuckChannelHandler.setupChannel();
		
		//Register structures
		MapGenStructureIO.registerStructure(StructureTempleStart.class, "FrogTemple");
		MapGenStructureIO.registerStructure(StructureCastleStart.class, "SkaiaCastle");
		StructureCastlePieces.registerComponents();
		MapGenLandStructure.registerStructures();
		ConsortVillageComponents.registerComponents();
		
		//update candy
		((ItemMinestuckCandy) MinestuckItems.candy).updateCandy();
		
		//register grist costs and combination recipes
		AlchemyRecipes.registerVanillaRecipes();
		AlchemyRecipes.registerMinestuckRecipes();
		AlchemyRecipes.registerModRecipes();
		
		//register smelting recipes and oredictionary
		CraftingRecipes.registerSmelting();
		CraftingRecipes.addOredictionary();

		//register consort shop prices
		ConsortRewardHandler.registerMinestuckPrices();
		
		//Register loot functionality objects
		MinestuckLoot.registerLootClasses();
		
		LandAspectRegistry.registerLandAspects();
		ConsortDialogue.init();
		
		KindAbstratusList.registerTypes();
		DeployList.registerItems();
		
		ComputerProgram.registerProgram(0, SburbClient.class, new ItemStack(MinestuckItems.disk, 1, 0));	//This idea was kind of bad and should be replaced
		ComputerProgram.registerProgram(1, SburbServer.class, new ItemStack(MinestuckItems.disk, 1, 1));
		
		SessionHandler.maxSize = 144;//acceptTitleCollision?(generateSpecialClasses?168:144):12;
	}
}
