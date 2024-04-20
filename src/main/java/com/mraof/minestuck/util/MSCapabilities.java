package com.mraof.minestuck.util;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.blockentity.machine.*;
import com.mraof.minestuck.computer.editmode.EditTools;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.fluid.MSFluidType;
import com.mraof.minestuck.inventory.musicplayer.MusicPlaying;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Minestuck.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSCapabilities
{
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Minestuck.MOD_ID);
	
	public static final Supplier<AttachmentType<Integer>> PLAYER_COLOR = ATTACHMENT_REGISTER.register("player_color",
			() -> AttachmentType.builder(() -> ColorHandler.BuiltinColors.DEFAULT_COLOR).serialize(Codec.INT).build());
	public static final Supplier<AttachmentType<Long>> BOONDOLLARS = ATTACHMENT_REGISTER.register("boondollars",
			() -> AttachmentType.builder(() -> 0L).serialize(Codec.LONG).build());
	
	public static final Supplier<AttachmentType<ItemStackHandler>> MUSIC_PLAYER_INVENTORY_ATTACHMENT = ATTACHMENT_REGISTER.register("music_player_inventory",
			() -> AttachmentType.serializable(() -> new ItemStackHandler(1)).build());
	public static final Supplier<AttachmentType<MusicPlaying>> MUSIC_PLAYING_ATTACHMENT = ATTACHMENT_REGISTER.register("music_playing",
			() -> AttachmentType.builder(MusicPlaying::new).build());
	public static final Supplier<AttachmentType<EditTools>> EDIT_TOOLS_ATTACHMENT = ATTACHMENT_REGISTER.register("edit_tools",
			() -> AttachmentType.builder(EditTools::new).build());
	public static final Supplier<AttachmentType<MSFluidType.LastFluidTickData>> LAST_FLUID_TICK_ATTACHMENT = ATTACHMENT_REGISTER.register("last_fluid_tick",
			() -> AttachmentType.builder(MSFluidType.LastFluidTickData::new).build());
	public static final Supplier<AttachmentType<DialogueComponent.CurrentDialogue>> CURRENT_DIALOGUE_ATTACHMENT = ATTACHMENT_REGISTER.register("current_dialogue",
			() -> AttachmentType.builder(DialogueComponent.CurrentDialogue::new).build());
	
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
