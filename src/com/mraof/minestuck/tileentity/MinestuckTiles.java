package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MinestuckTiles
{
	public static final TileEntityType<ComputerTileEntity> COMPUTER = TileEntityType.Builder.create(ComputerTileEntity::new, MinestuckBlocks.COMPUTER_ON, MinestuckBlocks.CROCKERTOP_ON, MinestuckBlocks.HUBTOP_ON, MinestuckBlocks.LAPTOP_ON, MinestuckBlocks.LUNCHTOP_ON).build(null);
	public static final TileEntityType<MiniCruxtruderTileEntity> MINI_CRUXTRUDER = TileEntityType.Builder.create(MiniCruxtruderTileEntity::new, MinestuckBlocks.MINI_CRUXTRUDER).build(null);
	public static final TileEntityType<MiniTotemLatheTileEntity> MINI_TOTEM_LATHE = TileEntityType.Builder.create(MiniTotemLatheTileEntity::new, MinestuckBlocks.MINI_TOTEM_LATHE).build(null);
	public static final TileEntityType<MiniAlchemiterTileEntity> MINI_ALCHEMITER = TileEntityType.Builder.create(MiniAlchemiterTileEntity::new, MinestuckBlocks.MINI_ALCHEMITER).build(null);
	public static final TileEntityType<MiniPunchDesignixTileEntity> MINI_PUNCH_DESIGNIX = TileEntityType.Builder.create(MiniPunchDesignixTileEntity::new, MinestuckBlocks.MINI_PUNCH_DESIGNIX).build(null);
	public static final TileEntityType<CruxtruderTileEntity> CRUXTRUDER = TileEntityType.Builder.create(CruxtruderTileEntity::new, MinestuckBlocks.CRUXTRUDER.CENTER).build(null);
	public static final TileEntityType<TotemLatheTileEntity> TOTEM_LATHE = TileEntityType.Builder.create(TotemLatheTileEntity::new, MinestuckBlocks.TOTEM_LATHE.CARD_SLOT).build(null);
	public static final TileEntityType<AlchemiterTileEntity> ALCHEMITER = TileEntityType.Builder.create(AlchemiterTileEntity::new, MinestuckBlocks.ALCHEMITER.TOTEM_PAD).build(null);
	public static final TileEntityType<PunchDesignixTileEntity> PUNCH_DESIGNIX = TileEntityType.Builder.create(PunchDesignixTileEntity::new, MinestuckBlocks.PUNCH_DESIGNIX.SLOT).build(null);
	public static final TileEntityType<GristWidgetTileEntity> GRIST_WIDGET = TileEntityType.Builder.create(GristWidgetTileEntity::new, MinestuckBlocks.GRIST_WIDGET).build(null);
	public static final TileEntityType<TransportalizerTileEntity> TRANSPORTALIZER = TileEntityType.Builder.create(TransportalizerTileEntity::new, MinestuckBlocks.TRANSPORTALIZER).build(null);
	public static final TileEntityType<ItemStackTileEntity> ITEM_STACK = TileEntityType.Builder.create(ItemStackTileEntity::new, MinestuckBlocks.CRUXITE_DOWEL, MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD).build(null);
	public static final TileEntityType<UraniumCookerTileEntity> URANIUM_COOKER = TileEntityType.Builder.create(UraniumCookerTileEntity::new, MinestuckBlocks.URANIUM_COOKER).build(null);
	public static final TileEntityType<TileEntityJumperBlock> JUMPER_BLOCK = TileEntityType.Builder.create(TileEntityJumperBlock::new).build(null);
	public static final TileEntityType<TileEntityAlchemiterUpgrade> ALCHEMITER_UPGRADE = TileEntityType.Builder.create(TileEntityAlchemiterUpgrade::new).build(null);
	public static final TileEntityType<TileEntityUpgradedAlchemiter> UPGRADE_ALCHEMITER = TileEntityType.Builder.create(TileEntityUpgradedAlchemiter::new).build(null);
	public static final TileEntityType<HolopadTileEntity> HOLOPAD = TileEntityType.Builder.create(HolopadTileEntity::new, MinestuckBlocks.HOLOPAD).build(null);
	public static final TileEntityType<SkaiaPortalTileEntity> SKAIA_PORTAL = TileEntityType.Builder.create(SkaiaPortalTileEntity::new, MinestuckBlocks.SKAIA_PORTAL).build(null);
	public static final TileEntityType<GateTileEntity> GATE = TileEntityType.Builder.create(GateTileEntity::new, MinestuckBlocks.GATE, MinestuckBlocks.RETURN_NODE).build(null);
	
	@SubscribeEvent
	public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event)
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