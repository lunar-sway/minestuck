package com.mraof.minestuck.util;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.editmode.EditTools;
import com.mraof.minestuck.computer.editmode.EditmodeLocations;
import com.mraof.minestuck.entity.consort.ConsortReputation;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.fluid.MSFluidType;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.musicplayer.MusicPlaying;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.GristCache;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.Title;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public final class MSAttachments
{
	public static final DeferredRegister<AttachmentType<?>> REGISTER = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Minestuck.MOD_ID);
	
	public static final Supplier<AttachmentType<CaptchaDeckHandler.ModusHolder>> MODUS_HOLDER = REGISTER.register("modus_holder",
			() -> AttachmentType.serializable(restricted(CaptchaDeckHandler.ModusHolder::new, PlayerData.class)).build());
	public static final Supplier<AttachmentType<Integer>> PLAYER_COLOR = REGISTER.register("player_color",
			() -> AttachmentType.builder(restricted(() -> ColorHandler.BuiltinColors.DEFAULT_COLOR, PlayerData.class)).serialize(Codec.INT).build());
	public static final Supplier<AttachmentType<Long>> BOONDOLLARS = REGISTER.register("boondollars",
			() -> AttachmentType.builder(restricted(() -> 0L, PlayerData.class)).serialize(Codec.LONG).build());
	public static final Supplier<AttachmentType<GristCache>> GRIST_CACHE = REGISTER.register("grist_cache",
			() -> AttachmentType.serializable(restricted(GristCache::new, PlayerData.class)).build());
	public static final Supplier<AttachmentType<Double>> GUTTER_MULTIPLIER = REGISTER.register("gutter_multiplier",
			() -> AttachmentType.builder(restricted(() -> 1D, PlayerData.class)).serialize(Codec.DOUBLE).build());
	public static final Supplier<AttachmentType<Echeladder>> ECHELADDER = REGISTER.register("echeladder",
			() -> AttachmentType.serializable(restricted(Echeladder::new, PlayerData.class)).build());
	
	public static final Supplier<AttachmentType<Title>> TITLE = REGISTER.register("title",
			() -> AttachmentType.<Title>builder(noDefault()).serialize(Title.CODEC).build());
	public static final Supplier<AttachmentType<Boolean>> EFFECT_TOGGLE = REGISTER.register("effect_toggle",
			() -> AttachmentType.builder(restricted(() -> false, ServerPlayer.class)).serialize(Codec.BOOL).build());
	
	public static final Supplier<AttachmentType<EditmodeLocations>> EDITMODE_LOCATIONS = REGISTER.register("editmode_locations",
			() -> AttachmentType.serializable(restricted(EditmodeLocations::new, PlayerData.class)).build());
	public static final Supplier<AttachmentType<EditTools>> EDIT_TOOLS = REGISTER.register("edit_tools",
			() -> AttachmentType.builder(restricted(EditTools::new, Player.class)).build());
	
	public static final Supplier<AttachmentType<ConsortReputation>> CONSORT_REPUTATION = REGISTER.register("consort_reputation",
			() -> AttachmentType.serializable(restricted(ConsortReputation::new, PlayerData.class)).build());
	public static final Supplier<AttachmentType<DialogueComponent.CurrentDialogue>> CURRENT_DIALOGUE = REGISTER.register("current_dialogue",
			() -> AttachmentType.builder(restricted(DialogueComponent.CurrentDialogue::new, ServerPlayer.class)).build());
	
	public static final Supplier<AttachmentType<ItemStackHandler>> MUSIC_PLAYER_INVENTORY = REGISTER.register("music_player_inventory",
			() -> AttachmentType.serializable(restricted(() -> new ItemStackHandler(1), ItemStack.class)).build());
	public static final Supplier<AttachmentType<MusicPlaying>> MUSIC_PLAYING = REGISTER.register("music_playing",
			() -> AttachmentType.builder(restricted(MusicPlaying::new, LivingEntity.class)).build());
	
	public static final Supplier<AttachmentType<MSFluidType.LastFluidTickData>> LAST_FLUID_TICK = REGISTER.register("last_fluid_tick",
			() -> AttachmentType.builder(restricted(MSFluidType.LastFluidTickData::new, LivingEntity.class)).build());
	
	private static <T> Supplier<T> noDefault()
	{
		return () -> {
			throw new UnsupportedOperationException("This attachment does not support default values. Use 'getExistingData()' instead.");
		};
	}
	
	private static <H extends IAttachmentHolder, T> Function<IAttachmentHolder, T> restricted(Supplier<T> defaultValueSupplier, Class<H> permittedHolder)
	{
		return restricted(holder -> defaultValueSupplier.get(), permittedHolder);
	}
	
	private static <H extends IAttachmentHolder, T> Function<IAttachmentHolder, T> restricted(Function<H, T> defaultValueSupplier, Class<H> permittedHolder)
	{
		return holder -> {
			if(!permittedHolder.isInstance(holder))
				throw new UnsupportedOperationException("Only a holder of class " + permittedHolder + " is permitted.");
			return defaultValueSupplier.apply(permittedHolder.cast(holder));
		};
	}
}
