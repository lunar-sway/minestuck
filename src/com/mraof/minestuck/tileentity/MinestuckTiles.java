package com.mraof.minestuck.tileentity;

import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;

public class MinestuckTiles
{
	public static TileEntityType<TileEntityComputer> computer;
	public static TileEntityType<TileEntitySburbMachine> sburbMachine;
	public static TileEntityType<TileEntityCruxtruder> cruxtruder;
	public static TileEntityType<TileEntityTotemLathe> totemLathe;
	public static TileEntityType<TileEntityAlchemiter> alchemiter;
	public static TileEntityType<TileEntityPunchDesignix> punchDesignix;
	public static TileEntityType<TileEntityCrockerMachine> crockerMachine;
	public static TileEntityType<TileEntityTransportalizer> transportalizer;
	public static TileEntityType<TileEntityItemStack> itemStack;
	public static TileEntityType<TileEntityUraniumCooker> uraniumCooker;
	public static TileEntityType<TileEntityJumperBlock> jumperBlock;
	public static TileEntityType<TileEntityAlchemiterUpgrade> alchemiterUpgrade;
	public static TileEntityType<TileEntityUpgradedAlchemiter> upgradeAlchemiter;
	public static TileEntityType<TileEntityHolopad> holopad;
	public static TileEntityType<TileEntitySkaiaPortal> skaiaPortal;
	public static TileEntityType<TileEntityGate> gate;
	
	public static void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register((computer = new TileEntityType<>(TileEntityComputer::new,null)).setRegistryName("computer"));
		event.getRegistry().register((sburbMachine = new TileEntityType<>(TileEntitySburbMachine::new,null)).setRegistryName("computer"));
		event.getRegistry().register((cruxtruder = new TileEntityType<>(TileEntityCruxtruder::new,null)).setRegistryName("computer"));
		event.getRegistry().register((totemLathe = new TileEntityType<>(TileEntityTotemLathe::new,null)).setRegistryName("computer"));
		event.getRegistry().register((alchemiter = new TileEntityType<>(TileEntityAlchemiter::new,null)).setRegistryName("computer"));
		event.getRegistry().register((punchDesignix = new TileEntityType<>(TileEntityPunchDesignix::new,null)).setRegistryName("computer"));
		event.getRegistry().register((crockerMachine = new TileEntityType<>(TileEntityCrockerMachine::new,null)).setRegistryName("computer"));
		event.getRegistry().register((transportalizer = new TileEntityType<>(TileEntityTransportalizer::new,null)).setRegistryName("computer"));
		event.getRegistry().register((itemStack = new TileEntityType<>(TileEntityItemStack::new,null)).setRegistryName("computer"));
		event.getRegistry().register((uraniumCooker = new TileEntityType<>(TileEntityUraniumCooker::new,null)).setRegistryName("computer"));
		event.getRegistry().register((jumperBlock = new TileEntityType<>(TileEntityJumperBlock::new,null)).setRegistryName("computer"));
		event.getRegistry().register((alchemiterUpgrade = new TileEntityType<>(TileEntityAlchemiterUpgrade::new,null)).setRegistryName("computer"));
		event.getRegistry().register((upgradeAlchemiter = new TileEntityType<>(TileEntityUpgradedAlchemiter::new,null)).setRegistryName("computer"));
		event.getRegistry().register((holopad = new TileEntityType<>(TileEntityHolopad::new,null)).setRegistryName("computer"));
		event.getRegistry().register((skaiaPortal = new TileEntityType<>(TileEntitySkaiaPortal::new,null)).setRegistryName("computer"));
		event.getRegistry().register((gate = new TileEntityType<>(TileEntityGate::new,null)).setRegistryName("computer"));
	}
}