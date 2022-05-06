package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.machine.*;
import com.mraof.minestuck.tileentity.redstone.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSTileEntityTypes
{
	public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Minestuck.MOD_ID);
	
	public static final RegistryObject<TileEntityType<ComputerTileEntity>> COMPUTER = REGISTER.register("computer", () -> TileEntityType.Builder.of(ComputerTileEntity::new, MSBlocks.COMPUTER, MSBlocks.CROCKERTOP, MSBlocks.HUBTOP, MSBlocks.LAPTOP, MSBlocks.LUNCHTOP).build(null));
	public static final RegistryObject<TileEntityType<MiniCruxtruderTileEntity>> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> TileEntityType.Builder.of(MiniCruxtruderTileEntity::new, MSBlocks.MINI_CRUXTRUDER).build(null));
	public static final RegistryObject<TileEntityType<MiniTotemLatheTileEntity>> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> TileEntityType.Builder.of(MiniTotemLatheTileEntity::new, MSBlocks.MINI_TOTEM_LATHE).build(null));
	public static final RegistryObject<TileEntityType<MiniAlchemiterTileEntity>> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> TileEntityType.Builder.of(MiniAlchemiterTileEntity::new, MSBlocks.MINI_ALCHEMITER).build(null));
	public static final RegistryObject<TileEntityType<MiniPunchDesignixTileEntity>> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> TileEntityType.Builder.of(MiniPunchDesignixTileEntity::new, MSBlocks.MINI_PUNCH_DESIGNIX).build(null));
	public static final RegistryObject<TileEntityType<CruxtruderTileEntity>> CRUXTRUDER = REGISTER.register("cruxtruder", () -> TileEntityType.Builder.of(CruxtruderTileEntity::new, MSBlocks.CRUXTRUDER.CENTER.get()).build(null));
	public static final RegistryObject<TileEntityType<TotemLatheTileEntity>> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> TileEntityType.Builder.of(TotemLatheTileEntity::new, MSBlocks.TOTEM_LATHE.CARD_SLOT.get()).build(null));
	public static final RegistryObject<TileEntityType<AlchemiterTileEntity>> ALCHEMITER = REGISTER.register("alchemiter", () -> TileEntityType.Builder.of(AlchemiterTileEntity::new, MSBlocks.ALCHEMITER.TOTEM_PAD.get()).build(null));
	public static final RegistryObject<TileEntityType<PunchDesignixTileEntity>> PUNCH_DESIGNIX = REGISTER.register("punch_designix", () -> TileEntityType.Builder.of(PunchDesignixTileEntity::new, MSBlocks.PUNCH_DESIGNIX.SLOT.get()).build(null));
	public static final RegistryObject<TileEntityType<GristWidgetTileEntity>> GRIST_WIDGET = REGISTER.register("grist_widget", () -> TileEntityType.Builder.of(GristWidgetTileEntity::new, MSBlocks.GRIST_WIDGET).build(null));
	public static final RegistryObject<TileEntityType<TransportalizerTileEntity>> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> TileEntityType.Builder.of(TransportalizerTileEntity::new, MSBlocks.TRANSPORTALIZER, MSBlocks.TRANS_PORTALIZER).build(null));
	public static final RegistryObject<TileEntityType<ItemStackTileEntity>> ITEM_STACK = REGISTER.register("item_stack", () -> TileEntityType.Builder.of(ItemStackTileEntity::new, MSBlocks.CRUXITE_DOWEL, MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null));
	public static final RegistryObject<TileEntityType<UraniumCookerTileEntity>> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> TileEntityType.Builder.of(UraniumCookerTileEntity::new, MSBlocks.URANIUM_COOKER).build(null));
	public static final RegistryObject<TileEntityType<HolopadTileEntity>> HOLOPAD = REGISTER.register("holopad", () -> TileEntityType.Builder.of(HolopadTileEntity::new, MSBlocks.HOLOPAD).build(null));
	public static final RegistryObject<TileEntityType<SkaiaPortalTileEntity>> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> TileEntityType.Builder.of(SkaiaPortalTileEntity::new, MSBlocks.SKAIA_PORTAL).build(null));
	public static final RegistryObject<TileEntityType<GateTileEntity>> GATE = REGISTER.register("gate", () -> TileEntityType.Builder.of(GateTileEntity::new, MSBlocks.GATE, MSBlocks.RETURN_NODE).build(null));
	public static final RegistryObject<TileEntityType<CassettePlayerTileEntity>> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> TileEntityType.Builder.of(CassettePlayerTileEntity::new, MSBlocks.CASSETTE_PLAYER).build(null));
	public static final RegistryObject<TileEntityType<StatStorerTileEntity>> STAT_STORER = REGISTER.register("stat_storer", () -> TileEntityType.Builder.of(StatStorerTileEntity::new, MSBlocks.STAT_STORER).build(null));
	public static final RegistryObject<TileEntityType<RemoteObserverTileEntity>> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> TileEntityType.Builder.of(RemoteObserverTileEntity::new, MSBlocks.REMOTE_OBSERVER).build(null));
	public static final RegistryObject<TileEntityType<WirelessRedstoneTransmitterTileEntity>> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> TileEntityType.Builder.of(WirelessRedstoneTransmitterTileEntity::new, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER).build(null));
	public static final RegistryObject<TileEntityType<WirelessRedstoneReceiverTileEntity>> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> TileEntityType.Builder.of(WirelessRedstoneReceiverTileEntity::new, MSBlocks.WIRELESS_REDSTONE_RECEIVER).build(null));
	public static final RegistryObject<TileEntityType<SummonerTileEntity>> SUMMONER = REGISTER.register("summoner", () -> TileEntityType.Builder.of(SummonerTileEntity::new, MSBlocks.SUMMONER).build(null));
	public static final RegistryObject<TileEntityType<AreaEffectTileEntity>> AREA_EFFECT = REGISTER.register("area_effect", () -> TileEntityType.Builder.of(AreaEffectTileEntity::new, MSBlocks.AREA_EFFECT_BLOCK).build(null));
	public static final RegistryObject<TileEntityType<PlatformGeneratorTileEntity>> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> TileEntityType.Builder.of(PlatformGeneratorTileEntity::new, MSBlocks.PLATFORM_GENERATOR).build(null));
	public static final RegistryObject<TileEntityType<ItemMagnetTileEntity>> ITEM_MAGNET = REGISTER.register("item_magnet", () -> TileEntityType.Builder.of(ItemMagnetTileEntity::new, MSBlocks.ITEM_MAGNET).build(null));
	public static final RegistryObject<TileEntityType<RedstoneClockTileEntity>> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> TileEntityType.Builder.of(RedstoneClockTileEntity::new, MSBlocks.REDSTONE_CLOCK).build(null));
}