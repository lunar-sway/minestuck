package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.machine.*;
import com.mraof.minestuck.blockentity.redstone.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MSBlockEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Minestuck.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<ComputerBlockEntity>> COMPUTER = REGISTER.register("computer", () -> BlockEntityType.Builder.of(ComputerBlockEntity::new, MSBlocks.COMPUTER.get(), MSBlocks.CROCKERTOP.get(), MSBlocks.HUBTOP.get(), MSBlocks.LAPTOP.get(), MSBlocks.LUNCHTOP.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniCruxtruderBlockEntity>> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> BlockEntityType.Builder.of(MiniCruxtruderBlockEntity::new, MSBlocks.MINI_CRUXTRUDER.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniTotemLatheBlockEntity>> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> BlockEntityType.Builder.of(MiniTotemLatheBlockEntity::new, MSBlocks.MINI_TOTEM_LATHE.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniAlchemiterBlockEntity>> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> BlockEntityType.Builder.of(MiniAlchemiterBlockEntity::new, MSBlocks.MINI_ALCHEMITER.get()).build(null));
	public static final RegistryObject<BlockEntityType<MiniPunchDesignixBlockEntity>> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> BlockEntityType.Builder.of(MiniPunchDesignixBlockEntity::new, MSBlocks.MINI_PUNCH_DESIGNIX.get()).build(null));
	public static final RegistryObject<BlockEntityType<CruxtruderBlockEntity>> CRUXTRUDER = REGISTER.register("cruxtruder", () -> BlockEntityType.Builder.of(CruxtruderBlockEntity::new, MSBlocks.CRUXTRUDER.CENTER.get()).build(null));
	public static final RegistryObject<BlockEntityType<TotemLatheBlockEntity>> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> BlockEntityType.Builder.of(TotemLatheBlockEntity::new, MSBlocks.TOTEM_LATHE.CARD_SLOT.get()).build(null));
	public static final RegistryObject<BlockEntityType<AlchemiterBlockEntity>> ALCHEMITER = REGISTER.register("alchemiter", () -> BlockEntityType.Builder.of(AlchemiterBlockEntity::new, MSBlocks.ALCHEMITER.TOTEM_PAD.get()).build(null));
	public static final RegistryObject<BlockEntityType<PunchDesignixBlockEntity>> PUNCH_DESIGNIX = REGISTER.register("punch_designix", () -> BlockEntityType.Builder.of(PunchDesignixBlockEntity::new, MSBlocks.PUNCH_DESIGNIX.SLOT.get()).build(null));
	public static final RegistryObject<BlockEntityType<SendificatorBlockEntity>> SENDIFICATOR = REGISTER.register("sendificator", () -> BlockEntityType.Builder.of(SendificatorBlockEntity::new, MSBlocks.SENDIFICATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<GristWidgetBlockEntity>> GRIST_WIDGET = REGISTER.register("grist_widget", () -> BlockEntityType.Builder.of(GristWidgetBlockEntity::new, MSBlocks.GRIST_WIDGET.get()).build(null));
	public static final RegistryObject<BlockEntityType<TransportalizerBlockEntity>> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> BlockEntityType.Builder.of(TransportalizerBlockEntity::new, MSBlocks.TRANSPORTALIZER.get(), MSBlocks.TRANS_PORTALIZER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ItemStackBlockEntity>> ITEM_STACK = REGISTER.register("item_stack", () -> BlockEntityType.Builder.of(ItemStackBlockEntity::new, MSBlocks.CRUXITE_DOWEL.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null));
	public static final RegistryObject<BlockEntityType<UraniumCookerBlockEntity>> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> BlockEntityType.Builder.of(UraniumCookerBlockEntity::new, MSBlocks.URANIUM_COOKER.get()).build(null));
	public static final RegistryObject<BlockEntityType<HolopadBlockEntity>> HOLOPAD = REGISTER.register("holopad", () -> BlockEntityType.Builder.of(HolopadBlockEntity::new, MSBlocks.HOLOPAD.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<SkaiaPortalBlockEntity>> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> BlockEntityType.Builder.of(SkaiaPortalBlockEntity::new, MSBlocks.SKAIA_PORTAL.get()).build(null));
	public static final RegistryObject<BlockEntityType<GateBlockEntity>> GATE = REGISTER.register("gate", () -> BlockEntityType.Builder.of(GateBlockEntity::new, MSBlocks.GATE_MAIN.get()).build(null));
	public static final RegistryObject<BlockEntityType<ReturnNodeBlockEntity>> RETURN_NODE = REGISTER.register("return_node", () -> BlockEntityType.Builder.of(ReturnNodeBlockEntity::new, MSBlocks.RETURN_NODE_MAIN.get()).build(null));
	
	public static final RegistryObject<BlockEntityType<CassettePlayerBlockEntity>> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> BlockEntityType.Builder.of(CassettePlayerBlockEntity::new, MSBlocks.CASSETTE_PLAYER.get()).build(null));
	public static final RegistryObject<BlockEntityType<StatStorerBlockEntity>> STAT_STORER = REGISTER.register("stat_storer", () -> BlockEntityType.Builder.of(StatStorerBlockEntity::new, MSBlocks.STAT_STORER.get()).build(null));
	public static final RegistryObject<BlockEntityType<RemoteObserverBlockEntity>> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> BlockEntityType.Builder.of(RemoteObserverBlockEntity::new, MSBlocks.REMOTE_OBSERVER.get()).build(null));
	public static final RegistryObject<BlockEntityType<WirelessRedstoneTransmitterBlockEntity>> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> BlockEntityType.Builder.of(WirelessRedstoneTransmitterBlockEntity::new, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.get()).build(null));
	public static final RegistryObject<BlockEntityType<WirelessRedstoneReceiverBlockEntity>> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> BlockEntityType.Builder.of(WirelessRedstoneReceiverBlockEntity::new, MSBlocks.WIRELESS_REDSTONE_RECEIVER.get()).build(null));
	public static final RegistryObject<BlockEntityType<SummonerBlockEntity>> SUMMONER = REGISTER.register("summoner", () -> BlockEntityType.Builder.of(SummonerBlockEntity::new, MSBlocks.SUMMONER.get()).build(null));
	public static final RegistryObject<BlockEntityType<AreaEffectBlockEntity>> AREA_EFFECT = REGISTER.register("area_effect", () -> BlockEntityType.Builder.of(AreaEffectBlockEntity::new, MSBlocks.AREA_EFFECT_BLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<PlatformGeneratorBlockEntity>> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> BlockEntityType.Builder.of(PlatformGeneratorBlockEntity::new, MSBlocks.PLATFORM_GENERATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<ItemMagnetBlockEntity>> ITEM_MAGNET = REGISTER.register("item_magnet", () -> BlockEntityType.Builder.of(ItemMagnetBlockEntity::new, MSBlocks.ITEM_MAGNET.get()).build(null));
	public static final RegistryObject<BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> BlockEntityType.Builder.of(RedstoneClockBlockEntity::new, MSBlocks.REDSTONE_CLOCK.get()).build(null));
	public static final RegistryObject<BlockEntityType<RemoteComparatorBlockEntity>> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> BlockEntityType.Builder.of(RemoteComparatorBlockEntity::new, MSBlocks.REMOTE_COMPARATOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<StructureCoreBlockEntity>> STRUCTURE_CORE = REGISTER.register("structure_core", () -> BlockEntityType.Builder.of(StructureCoreBlockEntity::new, MSBlocks.STRUCTURE_CORE.get()).build(null));
}