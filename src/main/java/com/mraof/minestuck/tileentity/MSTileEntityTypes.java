package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class MSTileEntityTypes	//TODO Use @ObjectHolder or DeferredRegister here
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
	public static TileEntityType<CassettePlayerTileEntity> CASSETTE_PLAYER;
	
	@SubscribeEvent
	public static void registerTileEntityType(final RegistryEvent.Register<TileEntityType<?>> event)
	{
		event.getRegistry().register((COMPUTER = TileEntityType.Builder.create(ComputerTileEntity::new, MSBlocks.COMPUTER, MSBlocks.CROCKERTOP, MSBlocks.HUBTOP, MSBlocks.LAPTOP, MSBlocks.LUNCHTOP).build(null)).setRegistryName("computer"));
		event.getRegistry().register((MINI_CRUXTRUDER = TileEntityType.Builder.create(MiniCruxtruderTileEntity::new, MSBlocks.MINI_CRUXTRUDER).build(null)).setRegistryName("mini_cruxtruder"));
		event.getRegistry().register((MINI_TOTEM_LATHE = TileEntityType.Builder.create(MiniTotemLatheTileEntity::new, MSBlocks.MINI_TOTEM_LATHE).build(null)).setRegistryName("mini_totem_lathe"));
		event.getRegistry().register((MINI_ALCHEMITER = TileEntityType.Builder.create(MiniAlchemiterTileEntity::new, MSBlocks.MINI_ALCHEMITER).build(null)).setRegistryName("mini_alchemiter"));
		event.getRegistry().register((MINI_PUNCH_DESIGNIX = TileEntityType.Builder.create(MiniPunchDesignixTileEntity::new, MSBlocks.MINI_PUNCH_DESIGNIX).build(null)).setRegistryName("mini_punch_designix"));
		event.getRegistry().register((CRUXTRUDER = TileEntityType.Builder.create(CruxtruderTileEntity::new, MSBlocks.CRUXTRUDER.CENTER.get()).build(null)).setRegistryName("cruxtruder"));
		event.getRegistry().register((TOTEM_LATHE = TileEntityType.Builder.create(TotemLatheTileEntity::new, MSBlocks.TOTEM_LATHE.CARD_SLOT.get()).build(null)).setRegistryName("totem_lathe"));
		event.getRegistry().register((ALCHEMITER = TileEntityType.Builder.create(AlchemiterTileEntity::new, MSBlocks.ALCHEMITER.TOTEM_PAD.get()).build(null)).setRegistryName("alchemiter"));
		event.getRegistry().register((PUNCH_DESIGNIX = TileEntityType.Builder.create(PunchDesignixTileEntity::new, MSBlocks.PUNCH_DESIGNIX.SLOT.get()).build(null)).setRegistryName("punch_designix"));
		event.getRegistry().register((GRIST_WIDGET = TileEntityType.Builder.create(GristWidgetTileEntity::new, MSBlocks.GRIST_WIDGET).build(null)).setRegistryName("grist_widget"));
		event.getRegistry().register((TRANSPORTALIZER = TileEntityType.Builder.create(TransportalizerTileEntity::new, MSBlocks.TRANSPORTALIZER).build(null)).setRegistryName("transportalizer"));
		event.getRegistry().register((ITEM_STACK = TileEntityType.Builder.create(ItemStackTileEntity::new, MSBlocks.CRUXITE_DOWEL, MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null)).setRegistryName("item_stack"));
		event.getRegistry().register((URANIUM_COOKER = TileEntityType.Builder.create(UraniumCookerTileEntity::new, MSBlocks.URANIUM_COOKER).build(null)).setRegistryName("uranium_cooker"));
		//event.getRegistry().register((JUMPER_BLOCK = TileEntityType.Builder.create(TileEntityJumperBlock::new).build(null)).setRegistryName("jumper_block"));
		//event.getRegistry().register((ALCHEMITER_UPGRADE = TileEntityType.Builder.create(TileEntityAlchemiterUpgrade::new).build(null)).setRegistryName("alchemiter_upgrade"));
		//event.getRegistry().register((UPGRADE_ALCHEMITER = TileEntityType.Builder.create(TileEntityUpgradedAlchemiter::new).build(null)).setRegistryName("upgrade_alchemiter"));
		event.getRegistry().register((HOLOPAD = TileEntityType.Builder.create(HolopadTileEntity::new, MSBlocks.HOLOPAD).build(null)).setRegistryName("holopad"));
		event.getRegistry().register((SKAIA_PORTAL = TileEntityType.Builder.create(SkaiaPortalTileEntity::new, MSBlocks.SKAIA_PORTAL).build(null)).setRegistryName("skaia_portal"));
		event.getRegistry().register((GATE = TileEntityType.Builder.create(GateTileEntity::new, MSBlocks.GATE, MSBlocks.RETURN_NODE).build(null)).setRegistryName("gate"));
		event.getRegistry().register((CASSETTE_PLAYER = TileEntityType.Builder.create(CassettePlayerTileEntity::new, MSBlocks.CASSETTE_PLAYER).build(null)).setRegistryName("cassette_player"));
	}
}