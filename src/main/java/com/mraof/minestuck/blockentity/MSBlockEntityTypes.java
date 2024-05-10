package com.mraof.minestuck.blockentity;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.AspectTreeBlocks;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.SkaiaBlocks;
import com.mraof.minestuck.blockentity.machine.*;
import com.mraof.minestuck.blockentity.redstone.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSBlockEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Minestuck.MOD_ID);
	
	public static final Supplier<BlockEntityType<ComputerBlockEntity>> COMPUTER = REGISTER.register("computer", () -> BlockEntityType.Builder.of(ComputerBlockEntity::new, MSBlocks.COMPUTER.get(), MSBlocks.CROCKERTOP.get(), MSBlocks.HUBTOP.get(), MSBlocks.LAPTOP.get(), MSBlocks.LUNCHTOP.get()).build(null));
	public static final Supplier<BlockEntityType<MiniCruxtruderBlockEntity>> MINI_CRUXTRUDER = REGISTER.register("mini_cruxtruder", () -> BlockEntityType.Builder.of(MiniCruxtruderBlockEntity::new, MSBlocks.MINI_CRUXTRUDER.get()).build(null));
	public static final Supplier<BlockEntityType<MiniTotemLatheBlockEntity>> MINI_TOTEM_LATHE = REGISTER.register("mini_totem_lathe", () -> BlockEntityType.Builder.of(MiniTotemLatheBlockEntity::new, MSBlocks.MINI_TOTEM_LATHE.get()).build(null));
	public static final Supplier<BlockEntityType<MiniAlchemiterBlockEntity>> MINI_ALCHEMITER = REGISTER.register("mini_alchemiter", () -> BlockEntityType.Builder.of(MiniAlchemiterBlockEntity::new, MSBlocks.MINI_ALCHEMITER.get()).build(null));
	public static final Supplier<BlockEntityType<MiniPunchDesignixBlockEntity>> MINI_PUNCH_DESIGNIX = REGISTER.register("mini_punch_designix", () -> BlockEntityType.Builder.of(MiniPunchDesignixBlockEntity::new, MSBlocks.MINI_PUNCH_DESIGNIX.get()).build(null));
	public static final Supplier<BlockEntityType<CruxtruderBlockEntity>> CRUXTRUDER = REGISTER.register("cruxtruder", () -> BlockEntityType.Builder.of(CruxtruderBlockEntity::new, MSBlocks.CRUXTRUDER.CENTER.get()).build(null));
	public static final Supplier<BlockEntityType<TotemLatheBlockEntity>> TOTEM_LATHE = REGISTER.register("totem_lathe", () -> BlockEntityType.Builder.of(TotemLatheBlockEntity::new, MSBlocks.TOTEM_LATHE.CARD_SLOT.get()).build(null));
	public static final Supplier<BlockEntityType<TotemLatheDowelBlockEntity>> TOTEM_LATHE_DOWEL = REGISTER.register("totem_lathe_dowel", () -> BlockEntityType.Builder.of(TotemLatheDowelBlockEntity::new, MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null));
	public static final Supplier<BlockEntityType<AlchemiterBlockEntity>> ALCHEMITER = REGISTER.register("alchemiter", () -> BlockEntityType.Builder.of(AlchemiterBlockEntity::new, MSBlocks.ALCHEMITER.TOTEM_PAD.get()).build(null));
	public static final Supplier<BlockEntityType<PunchDesignixBlockEntity>> PUNCH_DESIGNIX = REGISTER.register("punch_designix", () -> BlockEntityType.Builder.of(PunchDesignixBlockEntity::new, MSBlocks.PUNCH_DESIGNIX.SLOT.get()).build(null));
	public static final Supplier<BlockEntityType<SendificatorBlockEntity>> SENDIFICATOR = REGISTER.register("sendificator", () -> BlockEntityType.Builder.of(SendificatorBlockEntity::new, MSBlocks.SENDIFICATOR.get()).build(null));
	public static final Supplier<BlockEntityType<GristWidgetBlockEntity>> GRIST_WIDGET = REGISTER.register("grist_widget", () -> BlockEntityType.Builder.of(GristWidgetBlockEntity::new, MSBlocks.GRIST_WIDGET.get()).build(null));
	public static final Supplier<BlockEntityType<TransportalizerBlockEntity>> TRANSPORTALIZER = REGISTER.register("transportalizer", () -> BlockEntityType.Builder.of(TransportalizerBlockEntity::new, MSBlocks.TRANSPORTALIZER.get(), MSBlocks.TRANS_PORTALIZER.get()).build(null));
	public static final Supplier<BlockEntityType<ItemStackBlockEntity>> ITEM_STACK = REGISTER.register("item_stack", () -> BlockEntityType.Builder.of(ItemStackBlockEntity::new, MSBlocks.CRUXITE_DOWEL.get(), MSBlocks.EMERGING_CRUXITE_DOWEL.get(), MSBlocks.TOTEM_LATHE.DOWEL_ROD.get()).build(null));
	public static final Supplier<BlockEntityType<UraniumCookerBlockEntity>> URANIUM_COOKER = REGISTER.register("uranium_cooker", () -> BlockEntityType.Builder.of(UraniumCookerBlockEntity::new, MSBlocks.URANIUM_COOKER.get()).build(null));
	public static final Supplier<BlockEntityType<GristCollectorBlockEntity>> GRIST_COLLECTOR = REGISTER.register("grist_collector", () -> BlockEntityType.Builder.of(GristCollectorBlockEntity::new, MSBlocks.GRIST_COLLECTOR.get()).build(null));
	public static final Supplier<BlockEntityType<AnthvilBlockEntity>> ANTHVIL = REGISTER.register("anthvil", () -> BlockEntityType.Builder.of(AnthvilBlockEntity::new, MSBlocks.ANTHVIL.get()).build(null));
	public static final Supplier<BlockEntityType<HolopadBlockEntity>> HOLOPAD = REGISTER.register("holopad", () -> BlockEntityType.Builder.of(HolopadBlockEntity::new, MSBlocks.HOLOPAD.get()).build(null));
	public static final Supplier<BlockEntityType<IntellibeamLaserstationBlockEntity>> INTELLIBEAM_LASERSTATION = REGISTER.register("intellibeam_laserstation", () -> BlockEntityType.Builder.of(IntellibeamLaserstationBlockEntity::new, MSBlocks.INTELLIBEAM_LASERSTATION.get()).build(null));
	public static final Supplier<BlockEntityType<PowerHubBlockEntity>> POWER_HUB = REGISTER.register("power_hub", () -> BlockEntityType.Builder.of(PowerHubBlockEntity::new, MSBlocks.POWER_HUB.get()).build(null));
	
	public static final Supplier<BlockEntityType<SkaiaPortalBlockEntity>> SKAIA_PORTAL = REGISTER.register("skaia_portal", () -> BlockEntityType.Builder.of(SkaiaPortalBlockEntity::new, SkaiaBlocks.SKAIA_PORTAL.asBlock()).build(null));
	public static final Supplier<BlockEntityType<GateBlockEntity>> GATE = REGISTER.register("gate", () -> BlockEntityType.Builder.of(GateBlockEntity::new, MSBlocks.GATE_MAIN.get()).build(null));
	public static final Supplier<BlockEntityType<ReturnNodeBlockEntity>> RETURN_NODE = REGISTER.register("return_node", () -> BlockEntityType.Builder.of(ReturnNodeBlockEntity::new, MSBlocks.RETURN_NODE_MAIN.get()).build(null));
	
	public static final Supplier<BlockEntityType<CassettePlayerBlockEntity>> CASSETTE_PLAYER = REGISTER.register("cassette_player", () -> BlockEntityType.Builder.of(CassettePlayerBlockEntity::new, MSBlocks.CASSETTE_PLAYER.get()).build(null));
	public static final Supplier<BlockEntityType<HorseClockBlockEntity>> HORSE_CLOCK = REGISTER.register("horse_clock", () -> BlockEntityType.Builder.of(HorseClockBlockEntity::new, MSBlocks.HORSE_CLOCK.BOTTOM.get()).build(null));
	
	public static final Supplier<BlockEntityType<StatStorerBlockEntity>> STAT_STORER = REGISTER.register("stat_storer", () -> BlockEntityType.Builder.of(StatStorerBlockEntity::new, MSBlocks.STAT_STORER.get()).build(null));
	public static final Supplier<BlockEntityType<RemoteObserverBlockEntity>> REMOTE_OBSERVER = REGISTER.register("remote_observer", () -> BlockEntityType.Builder.of(RemoteObserverBlockEntity::new, MSBlocks.REMOTE_OBSERVER.get()).build(null));
	public static final Supplier<BlockEntityType<WirelessRedstoneTransmitterBlockEntity>> WIRELESS_REDSTONE_TRANSMITTER = REGISTER.register("wireless_redstone_transmitter", () -> BlockEntityType.Builder.of(WirelessRedstoneTransmitterBlockEntity::new, MSBlocks.WIRELESS_REDSTONE_TRANSMITTER.get()).build(null));
	public static final Supplier<BlockEntityType<WirelessRedstoneReceiverBlockEntity>> WIRELESS_REDSTONE_RECEIVER = REGISTER.register("wireless_redstone_receiver", () -> BlockEntityType.Builder.of(WirelessRedstoneReceiverBlockEntity::new, MSBlocks.WIRELESS_REDSTONE_RECEIVER.get()).build(null));
	public static final Supplier<BlockEntityType<SummonerBlockEntity>> SUMMONER = REGISTER.register("summoner", () -> BlockEntityType.Builder.of(SummonerBlockEntity::new, MSBlocks.SUMMONER.get()).build(null));
	public static final Supplier<BlockEntityType<AreaEffectBlockEntity>> AREA_EFFECT = REGISTER.register("area_effect", () -> BlockEntityType.Builder.of(AreaEffectBlockEntity::new, MSBlocks.AREA_EFFECT_BLOCK.get()).build(null));
	public static final Supplier<BlockEntityType<PlatformGeneratorBlockEntity>> PLATFORM_GENERATOR = REGISTER.register("platform_generator", () -> BlockEntityType.Builder.of(PlatformGeneratorBlockEntity::new, MSBlocks.PLATFORM_GENERATOR.get()).build(null));
	public static final Supplier<BlockEntityType<ItemMagnetBlockEntity>> ITEM_MAGNET = REGISTER.register("item_magnet", () -> BlockEntityType.Builder.of(ItemMagnetBlockEntity::new, MSBlocks.ITEM_MAGNET.get()).build(null));
	public static final Supplier<BlockEntityType<RedstoneClockBlockEntity>> REDSTONE_CLOCK = REGISTER.register("redstone_clock", () -> BlockEntityType.Builder.of(RedstoneClockBlockEntity::new, MSBlocks.REDSTONE_CLOCK.get()).build(null));
	public static final Supplier<BlockEntityType<RemoteComparatorBlockEntity>> REMOTE_COMPARATOR = REGISTER.register("remote_comparator", () -> BlockEntityType.Builder.of(RemoteComparatorBlockEntity::new, MSBlocks.REMOTE_COMPARATOR.get()).build(null));
	public static final Supplier<BlockEntityType<StructureCoreBlockEntity>> STRUCTURE_CORE = REGISTER.register("structure_core", () -> BlockEntityType.Builder.of(StructureCoreBlockEntity::new, MSBlocks.STRUCTURE_CORE.get()).build(null));
	public static final Supplier<BlockEntityType<BlockTeleporterBlockEntity>> BLOCK_TELEPORTER = REGISTER.register("block_teleporter", () -> BlockEntityType.Builder.of(BlockTeleporterBlockEntity::new, MSBlocks.BLOCK_TELEPORTER.get()).build(null));
	
	public static final Supplier<BlockEntityType<SignBlockEntity>> SIGN =
			REGISTER.register("sign", () ->
					BlockEntityType.Builder.of((pos, state) -> new SignBlockEntity(MSBlockEntityTypes.SIGN.get(), pos, state),
									MSBlocks.PERFECTLY_GENERIC_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_SIGN.get(),
									MSBlocks.CARVED_SIGN.get(), MSBlocks.CARVED_WALL_SIGN.get(),
									MSBlocks.DEAD_SIGN.get(), MSBlocks.DEAD_WALL_SIGN.get(),
									MSBlocks.END_SIGN.get(), MSBlocks.END_WALL_SIGN.get(),
									MSBlocks.FROST_SIGN.get(), MSBlocks.FROST_WALL_SIGN.get(),
									MSBlocks.GLOWING_SIGN.get(), MSBlocks.GLOWING_WALL_SIGN.get(),
									MSBlocks.RAINBOW_SIGN.get(), MSBlocks.RAINBOW_WALL_SIGN.get(),
									MSBlocks.SHADEWOOD_SIGN.get(), MSBlocks.SHADEWOOD_WALL_SIGN.get(),
									MSBlocks.TREATED_SIGN.get(), MSBlocks.TREATED_WALL_SIGN.get(),
									MSBlocks.LACQUERED_SIGN.get(), MSBlocks.LACQUERED_WALL_SIGN.get(),
									MSBlocks.CINDERED_SIGN.get(), MSBlocks.CINDERED_WALL_SIGN.get(),
									AspectTreeBlocks.BLOOD_ASPECT_SIGN.get(), AspectTreeBlocks.BLOOD_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.BREATH_ASPECT_SIGN.get(), AspectTreeBlocks.BREATH_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.DOOM_ASPECT_SIGN.get(), AspectTreeBlocks.DOOM_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.HEART_ASPECT_SIGN.get(), AspectTreeBlocks.HEART_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.HOPE_ASPECT_SIGN.get(), AspectTreeBlocks.HOPE_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.LIFE_ASPECT_SIGN.get(), AspectTreeBlocks.LIFE_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.LIGHT_ASPECT_SIGN.get(), AspectTreeBlocks.LIGHT_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.MIND_ASPECT_SIGN.get(), AspectTreeBlocks.MIND_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.RAGE_ASPECT_SIGN.get(), AspectTreeBlocks.RAGE_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.SPACE_ASPECT_SIGN.get(), AspectTreeBlocks.SPACE_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.TIME_ASPECT_SIGN.get(), AspectTreeBlocks.TIME_ASPECT_WALL_SIGN.get(),
									AspectTreeBlocks.VOID_ASPECT_SIGN.get(), AspectTreeBlocks.VOID_ASPECT_WALL_SIGN.get())
							.build(null));
	
	public static final Supplier<BlockEntityType<MSHangingSignBlockEntity>> HANGING_SIGN =
			REGISTER.register("hanging_sign", () ->
					BlockEntityType.Builder.of(MSHangingSignBlockEntity::new,
									MSBlocks.PERFECTLY_GENERIC_HANGING_SIGN.get(), MSBlocks.PERFECTLY_GENERIC_WALL_HANGING_SIGN.get(),
									MSBlocks.CARVED_HANGING_SIGN.get(), MSBlocks.CARVED_WALL_HANGING_SIGN.get(),
									MSBlocks.DEAD_HANGING_SIGN.get(), MSBlocks.DEAD_WALL_HANGING_SIGN.get(),
									MSBlocks.END_HANGING_SIGN.get(), MSBlocks.END_WALL_HANGING_SIGN.get(),
									MSBlocks.FROST_HANGING_SIGN.get(), MSBlocks.FROST_WALL_HANGING_SIGN.get(),
									MSBlocks.GLOWING_HANGING_SIGN.get(), MSBlocks.GLOWING_WALL_HANGING_SIGN.get(),
									MSBlocks.RAINBOW_HANGING_SIGN.get(), MSBlocks.RAINBOW_WALL_HANGING_SIGN.get(),
									MSBlocks.SHADEWOOD_HANGING_SIGN.get(), MSBlocks.SHADEWOOD_WALL_HANGING_SIGN.get(),
									MSBlocks.TREATED_HANGING_SIGN.get(), MSBlocks.TREATED_WALL_HANGING_SIGN.get(),
									MSBlocks.LACQUERED_HANGING_SIGN.get(), MSBlocks.LACQUERED_WALL_HANGING_SIGN.get(),
									MSBlocks.CINDERED_HANGING_SIGN.get(), MSBlocks.CINDERED_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.BLOOD_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.BLOOD_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.BREATH_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.BREATH_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.DOOM_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.DOOM_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.HEART_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.HEART_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.HOPE_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.HOPE_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.LIFE_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.LIFE_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.LIGHT_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.LIGHT_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.MIND_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.MIND_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.RAGE_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.RAGE_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.SPACE_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.SPACE_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.TIME_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.TIME_ASPECT_WALL_HANGING_SIGN.get(),
									AspectTreeBlocks.VOID_ASPECT_HANGING_SIGN.get(), AspectTreeBlocks.VOID_ASPECT_WALL_HANGING_SIGN.get())
							.build(null));
	
	
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.MINI_CRUXTRUDER.get(), MiniCruxtruderBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.MINI_TOTEM_LATHE.get(), MiniTotemLatheBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.MINI_ALCHEMITER.get(), MiniAlchemiterBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.MINI_PUNCH_DESIGNIX.get(), MiniPunchDesignixBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.SENDIFICATOR.get(), SendificatorBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.GRIST_WIDGET.get(), GristWidgetBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.URANIUM_COOKER.get(), UraniumCookerBlockEntity::getItemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, MSBlockEntityTypes.ANTHVIL.get(), AnthvilBlockEntity::getItemHandler);
	}
}