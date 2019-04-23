package com.mraof.minestuck.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MinestuckTiles
{
	public static final TileEntityType<TileEntityComputer> COMPUTER = new TileEntityType<>(TileEntityComputer::new,null);
	public static final TileEntityType<TileEntityMiniCruxtruder> MINI_CRUXTRUDER = new TileEntityType<>(TileEntityMiniCruxtruder::new,null);
	public static final TileEntityType<TileEntityMiniTotemLathe> MINI_TOTEM_LATHE = new TileEntityType<>(TileEntityMiniTotemLathe::new,null);
	public static final TileEntityType<TileEntityMiniAlchemiter> MINI_ALCHEMITER = new TileEntityType<>(TileEntityMiniAlchemiter::new,null);
	public static final TileEntityType<TileEntityMiniPunchDesignix> MINI_PUNCH_DESIGNIX = new TileEntityType<>(TileEntityMiniPunchDesignix::new,null);
	public static final TileEntityType<TileEntityCruxtruder> CRUXTRUDER = new TileEntityType<>(TileEntityCruxtruder::new,null);
	public static final TileEntityType<TileEntityTotemLathe> TOTEM_LATHE = new TileEntityType<>(TileEntityTotemLathe::new,null);
	public static final TileEntityType<TileEntityAlchemiter> ALCHEMITER = new TileEntityType<>(TileEntityAlchemiter::new,null);
	public static final TileEntityType<TileEntityPunchDesignix> PUNCH_DESIGNIX = new TileEntityType<>(TileEntityPunchDesignix::new,null);
	public static final TileEntityType<TileEntityGristWidget> GRIST_WIDGET = new TileEntityType<>(TileEntityGristWidget::new,null);
	public static final TileEntityType<TileEntityTransportalizer> TRANSPORTALIZER = new TileEntityType<>(TileEntityTransportalizer::new,null);
	public static final TileEntityType<TileEntityItemStack> ITEM_STACK = new TileEntityType<>(TileEntityItemStack::new,null);
	public static final TileEntityType<TileEntityUraniumCooker> URANIUM_COOKER = new TileEntityType<>(TileEntityUraniumCooker::new,null);
	public static final TileEntityType<TileEntityJumperBlock> JUMPER_BLOCK = new TileEntityType<>(TileEntityJumperBlock::new,null);
	public static final TileEntityType<TileEntityAlchemiterUpgrade> ALCHEMITER_UPGRADE = new TileEntityType<>(TileEntityAlchemiterUpgrade::new,null);
	public static final TileEntityType<TileEntityUpgradedAlchemiter> UPGRADE_ALCHEMITER = new TileEntityType<>(TileEntityUpgradedAlchemiter::new,null);
	public static final TileEntityType<TileEntityHolopad> HOLOPAD = new TileEntityType<>(TileEntityHolopad::new,null);
	public static final TileEntityType<TileEntitySkaiaPortal> SKAIA_PORTAL = new TileEntityType<>(TileEntitySkaiaPortal::new,null);
	public static final TileEntityType<TileEntityGate> GATE = new TileEntityType<>(TileEntityGate::new,null);
	
	@SubscribeEvent
	public static void registerTileEntityType(RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register(COMPUTER.setRegistryName("computer"));
		event.getRegistry().register(MINI_CRUXTRUDER.setRegistryName("mini_cruxtruder"));
		event.getRegistry().register(MINI_TOTEM_LATHE.setRegistryName("mini_totem_lathe"));
		event.getRegistry().register(MINI_ALCHEMITER.setRegistryName("mini_alchemiter"));
		event.getRegistry().register(MINI_PUNCH_DESIGNIX.setRegistryName("mini_punch_designix"));
		event.getRegistry().register(CRUXTRUDER.setRegistryName("cruxtruder"));
		event.getRegistry().register(TOTEM_LATHE.setRegistryName("totem_lathe"));
		event.getRegistry().register(ALCHEMITER.setRegistryName("alchemiter"));
		event.getRegistry().register(PUNCH_DESIGNIX.setRegistryName("punch_designix"));
		event.getRegistry().register(GRIST_WIDGET.setRegistryName("grist_widget"));
		event.getRegistry().register(TRANSPORTALIZER.setRegistryName("transportalizer"));
		event.getRegistry().register(ITEM_STACK.setRegistryName("item_stack"));
		event.getRegistry().register(URANIUM_COOKER.setRegistryName("uranium_cooker"));
		event.getRegistry().register(JUMPER_BLOCK.setRegistryName("jumper_block"));
		event.getRegistry().register(ALCHEMITER_UPGRADE.setRegistryName("alchemiter_upgrade"));
		event.getRegistry().register(UPGRADE_ALCHEMITER.setRegistryName("upgrade_alchemiter"));
		event.getRegistry().register(HOLOPAD.setRegistryName("holopad"));
		event.getRegistry().register(SKAIA_PORTAL.setRegistryName("skaia_portal"));
		event.getRegistry().register(GATE.setRegistryName("gate"));
	}
}