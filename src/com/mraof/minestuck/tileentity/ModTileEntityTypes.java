package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MinestuckBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTileEntityTypes
{
	public static TileEntityType<ComputerTileEntity> COMPUTER;
	public static TileEntityType<MiniCruxtruderTileEntity> MINI_CRUXTRUDER;
	public static TileEntityType<MiniTotemLatheTileEntity> MINI_TOTEM_LATHE;
	public static TileEntityType<MiniAlchemiterTileEntity> MINI_ALCHEMITER;
	public static TileEntityType<MiniPunchDesignixTileEntity> MINI_PUNCH_DESIGNIX;
	public static TileEntityType<CruxtruderTileEntity> CRUXTRUDER;
	public static TileEntityType<TotemLatheTileEntity> TOTEM_LATHE;
	public static TileEntityType<AlchemiterTileEntity> ALCHEMITER;
	public static TileEntityType<PunchDesignixTileEntity> PUNCH_DESIGNIX;
	public static TileEntityType<GristWidgetTileEntity> GRIST_WIDGET;
	public static TileEntityType<TransportalizerTileEntity> TRANSPORTALIZER;
	public static TileEntityType<ItemStackTileEntity> ITEM_STACK;
	public static TileEntityType<UraniumCookerTileEntity> URANIUM_COOKER;
	public static TileEntityType<TileEntityJumperBlock> JUMPER_BLOCK;
	public static TileEntityType<TileEntityAlchemiterUpgrade> ALCHEMITER_UPGRADE;
	public static TileEntityType<TileEntityUpgradedAlchemiter> UPGRADE_ALCHEMITER;
	public static TileEntityType<HolopadTileEntity> HOLOPAD;
	public static TileEntityType<SkaiaPortalTileEntity> SKAIA_PORTAL;
	public static TileEntityType<GateTileEntity> GATE;
	
	@SubscribeEvent
	public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register((COMPUTER = TileEntityType.Builder.create(ComputerTileEntity::new, MinestuckBlocks.COMPUTER_ON, MinestuckBlocks.CROCKERTOP_ON, MinestuckBlocks.HUBTOP_ON, MinestuckBlocks.LAPTOP_ON, MinestuckBlocks.LUNCHTOP_ON).build(null)).setRegistryName("computer"));
		event.getRegistry().register((MINI_CRUXTRUDER = TileEntityType.Builder.create(MiniCruxtruderTileEntity::new, MinestuckBlocks.MINI_CRUXTRUDER).build(null)).setRegistryName("mini_cruxtruder"));
		event.getRegistry().register((MINI_TOTEM_LATHE = TileEntityType.Builder.create(MiniTotemLatheTileEntity::new, MinestuckBlocks.MINI_TOTEM_LATHE).build(null)).setRegistryName("mini_totem_lathe"));
		event.getRegistry().register((MINI_ALCHEMITER = TileEntityType.Builder.create(MiniAlchemiterTileEntity::new, MinestuckBlocks.MINI_ALCHEMITER).build(null)).setRegistryName("mini_alchemiter"));
		event.getRegistry().register((MINI_PUNCH_DESIGNIX = TileEntityType.Builder.create(MiniPunchDesignixTileEntity::new, MinestuckBlocks.MINI_PUNCH_DESIGNIX).build(null)).setRegistryName("mini_punch_designix"));
		event.getRegistry().register((CRUXTRUDER = TileEntityType.Builder.create(CruxtruderTileEntity::new, MinestuckBlocks.CRUXTRUDER.CENTER).build(null)).setRegistryName("cruxtruder"));
		event.getRegistry().register((TOTEM_LATHE = TileEntityType.Builder.create(TotemLatheTileEntity::new, MinestuckBlocks.TOTEM_LATHE.CARD_SLOT).build(null)).setRegistryName("totem_lathe"));
		event.getRegistry().register((ALCHEMITER = TileEntityType.Builder.create(AlchemiterTileEntity::new, MinestuckBlocks.ALCHEMITER.TOTEM_PAD).build(null)).setRegistryName("alchemiter"));
		event.getRegistry().register((PUNCH_DESIGNIX = TileEntityType.Builder.create(PunchDesignixTileEntity::new, MinestuckBlocks.PUNCH_DESIGNIX.SLOT).build(null)).setRegistryName("punch_designix"));
		event.getRegistry().register((GRIST_WIDGET = TileEntityType.Builder.create(GristWidgetTileEntity::new, MinestuckBlocks.GRIST_WIDGET).build(null)).setRegistryName("grist_widget"));
		event.getRegistry().register((TRANSPORTALIZER = TileEntityType.Builder.create(TransportalizerTileEntity::new, MinestuckBlocks.TRANSPORTALIZER).build(null)).setRegistryName("transportalizer"));
		event.getRegistry().register((ITEM_STACK = TileEntityType.Builder.create(ItemStackTileEntity::new, MinestuckBlocks.CRUXITE_DOWEL, MinestuckBlocks.TOTEM_LATHE.DOWEL_ROD).build(null)).setRegistryName("item_stack"));
		event.getRegistry().register((URANIUM_COOKER = TileEntityType.Builder.create(UraniumCookerTileEntity::new, MinestuckBlocks.URANIUM_COOKER).build(null)).setRegistryName("uranium_cooker"));
		//event.getRegistry().register((JUMPER_BLOCK = TileEntityType.Builder.create(TileEntityJumperBlock::new).build(null)).setRegistryName("jumper_block"));
		//event.getRegistry().register((ALCHEMITER_UPGRADE = TileEntityType.Builder.create(TileEntityAlchemiterUpgrade::new).build(null)).setRegistryName("alchemiter_upgrade"));
		//event.getRegistry().register((UPGRADE_ALCHEMITER = TileEntityType.Builder.create(TileEntityUpgradedAlchemiter::new).build(null)).setRegistryName("upgrade_alchemiter"));
		//event.getRegistry().register((HOLOPAD = TileEntityType.Builder.create(HolopadTileEntity::new, MinestuckBlocks.HOLOPAD).build(null)).setRegistryName("holopad"));
		event.getRegistry().register((SKAIA_PORTAL = TileEntityType.Builder.create(SkaiaPortalTileEntity::new, MinestuckBlocks.SKAIA_PORTAL).build(null)).setRegistryName("skaia_portal"));
		event.getRegistry().register((GATE = TileEntityType.Builder.create(GateTileEntity::new, MinestuckBlocks.GATE, MinestuckBlocks.RETURN_NODE).build(null)).setRegistryName("gate"));
	}
}