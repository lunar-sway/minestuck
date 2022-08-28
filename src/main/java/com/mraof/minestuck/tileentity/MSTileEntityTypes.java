package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.machine.*;
import com.mraof.minestuck.tileentity.redstone.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSTileEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Minestuck.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<ComputerTileEntity>> COMPUTER = REGISTER.register("computer", () -> BlockEntityType.Builder.of(ComputerTileEntity::new, MSBlocks.COMPUTER.get(), MSBlocks.CROCKERTOP.get(), MSBlocks.HUBTOP.get(), MSBlocks.LAPTOP.get(), MSBlocks.LUNCHTOP.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniCruxtruderTileEntity>> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> BlockEntityType.Builder.of(MiniCruxtruderTileEntity::new, MSBlocks.MINI_CRUXTRUDER.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniTotemLatheTileEntity>> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> BlockEntityType.Builder.of(MiniTotemLatheTileEntity::new, MSBlocks.MINI_TOTEM_LATHE.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniAlchemiterTileEntity>> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> BlockEntityType.Builder.of(MiniAlchemiterTileEntity::new, MSBlocks.MINI_ALCHEMITER.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniPunchDesignixTileEntity>> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> BlockEntityType.Builder.of(MiniPunchDesignixTileEntity::new, MSBlocks.MINI_PUNCH_DESIGNIX.get()).build(null));
	public static final RegistryObject<BlockEntityType<CruxtruderTileEntity>> CRUXTRUDER = REGISTER.register("cruxtruder", () -> BlockEntityType.Builder.of(CruxtruderTileEntity::new, MSBlocks.CRUXTRUDER.CENTER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TotemLatheTileEntity>> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> BlockEntityType.Builder.of(TotemLatheTileEntity::new, MSBlocks.TOTEM_LATHE.CARD_SLOT.get()).build(null));
	public static final RegistryObject<BlockEntityType<AlchemiterTileEntity>> ALCHEMITER = REGISTER.register("alchemiter", () -> BlockEntityType.Builder.of(AlchemiterTileEntity::new, MSBlocks.ALCHEMITER.TOTEM_PAD.get()).build(null));
	public static final RegistryObject<BlockEntityType<PunchDesignixTileEntity>> PUNCH_DESIGNIX = REGISTER.register("punch_designix", () -> BlockEntityType.Builder.of(PunchDesignixTileEntity::new, MSBlocks.PUNCH_DESIGNIX.SLOT.get()).build(null));
	public static final RegistryObject<BlockEntityType<SendificatorTileEntity>> SENDIFICATOR = REGISTER.register("sendificator", () -> BlockEntityType.Builder.of(SendificatorTileEntity::new, MSBlocks.SENDIFICATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<GristWidgetTileEntity>> GRIST_WIDGET = REGISTER.register("grist_widget", () -> BlockEntityType.Builder.of(GristWidgetTileEntity::new, MSBlocks.GRIST_WIDGET.get()).build(null));
	public static final RegistryObject<BlockEntityType<TransportalizerTileEntity>> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> BlockEntityType.Builder.of(TransportalizerTileEntity::new, MSBlocks.TRANSPORTALIZER.get(), MSBlocks.TRANS_PORTALIZER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ItemStackTileEntity>> ITEM_STACK = REGISTER.register("item_stack", () -> BlockEntityType.Builder.of(ItemStackTileEntity::new, MSBlocks.CRUXITE_DOWEL.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null));
	public static final RegistryObject<BlockEntityType<UraniumCookerTileEntity>> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> BlockEntityType.Builder.of(UraniumCookerTileEntity::new, MSBlocks.URANIUM_COOKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<HolopadTileEntity>> HOLOPAD = REGISTER.register("holopad", () -> BlockEntityType.Builder.of(HolopadTileEntity::new, MSBlocks.HOLOPAD.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SkaiaPortalTileEntity>> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> BlockEntityType.Builder.of(SkaiaPortalTileEntity::new, MSBlocks.SKAIA_PORTAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<GateTileEntity>> GATE = REGISTER.register("gate", () -> BlockEntityType.Builder.of(GateTileEntity::new, MSBlocks.GATE.get()).build(null));
	public static final RegistryObject<BlockEntityType<ReturnNodeBlockEntity>> RETURN_NODE = REGISTER.register("return_node", () -> BlockEntityType.Builder.of(ReturnNodeBlockEntity::new, MSBlocks.RETURN_NODE.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CassettePlayerTileEntity>> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> BlockEntityType.Builder.of(CassettePlayerTileEntity::new, MSBlocks.CASSETTE_PLAYER.get()).build(null));
	public static final RegistryObject<BlockEntityType<StatStorerTileEntity>> STAT_STORER = REGISTER.register("stat_storer", () -> BlockEntityType.Builder.of(StatStorerTileEntity::new, MSBlocks.STAT_STORER.get()).build(null));
	public static final RegistryObject<BlockEntityType<RemoteObserverTileEntity>> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> BlockEntityType.Builder.of(RemoteObserverTileEntity::new, MSBlocks.REMOTE_OBSERVER.get()).build(null));
	public static final RegistryObject<BlockEntityType<WirelessRedstoneTransmitterTileEntity>> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> BlockEntityType.Builder.of(WirelessRedstoneTransmitterTileEntity::new, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.get()).build(null));
	public static final RegistryObject<BlockEntityType<WirelessRedstoneReceiverTileEntity>> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> BlockEntityType.Builder.of(WirelessRedstoneReceiverTileEntity::new, MSBlocks.WIRELESS_REDSTONE_RECEIVER.get()).build(null));
	public static final RegistryObject<BlockEntityType<SummonerTileEntity>> SUMMONER = REGISTER.register("summoner", () -> BlockEntityType.Builder.of(SummonerTileEntity::new, MSBlocks.SUMMONER.get()).build(null));
	public static final RegistryObject<BlockEntityType<AreaEffectTileEntity>> AREA_EFFECT = REGISTER.register("area_effect", () -> BlockEntityType.Builder.of(AreaEffectTileEntity::new, MSBlocks.AREA_EFFECT_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<PlatformGeneratorTileEntity>> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> BlockEntityType.Builder.of(PlatformGeneratorTileEntity::new, MSBlocks.PLATFORM_GENERATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<ItemMagnetTileEntity>> ITEM_MAGNET = REGISTER.register("item_magnet", () -> BlockEntityType.Builder.of(ItemMagnetTileEntity::new, MSBlocks.ITEM_MAGNET.get()).build(null));
	public static final RegistryObject<BlockEntityType<RedstoneClockTileEntity>> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> BlockEntityType.Builder.of(RedstoneClockTileEntity::new, MSBlocks.REDSTONE_CLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<RemoteComparatorTileEntity>> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> BlockEntityType.Builder.of(RemoteComparatorTileEntity::new, MSBlocks.REMOTE_COMPARATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<StructureCoreTileEntity>> STRUCTURE_CORE = REGISTER.register("structure_core", () -> BlockEntityType.Builder.of(StructureCoreTileEntity::new, MSBlocks.STRUCTURE_CORE.get()).build(null));
}