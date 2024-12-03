package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public record DialogueMessage(String key, List<Argument> arguments)
{
	static Codec<DialogueMessage> DIRECT_CODEC = RecordCodecBuilder.create(instance ->
			instance.group(Codec.STRING.fieldOf("key").forGetter(DialogueMessage::key),
							Codec.list(Argument.CODEC).fieldOf("arguments").forGetter(DialogueMessage::arguments))
					.apply(instance, DialogueMessage::new));
	static Codec<DialogueMessage> CODEC = Codec.either(Codec.STRING, DIRECT_CODEC)
			.xmap(either -> either.map(DialogueMessage::new, Function.identity()),
					message -> message.arguments.isEmpty() ? Either.left(message.key) : Either.right(message));
	
	public DialogueMessage(String message)
	{
		this(message, List.of());
	}
	
	public MutableComponent evaluateComponent(LivingEntity entity, ServerPlayer serverPlayer)
	{
		return Component.translatable(this.key, this.arguments.stream().map(argument -> argument.processing.apply(entity, serverPlayer)).toArray());
	}
	
	@MethodsReturnNonnullByDefault
	public enum Argument implements StringRepresentable
	{
		LAND_PLAYER_NAME((npc, player) -> homeLandClientPlayer(npc)
				.map(clientPlayer -> Component.literal(clientPlayer.getUsername()))
				.orElseGet(() -> Component.literal("Player name"))),
		LAND_NAME((npc, player) -> homeDimension(npc)
				.flatMap(land -> LandTypePair.getNamed(player.server, land))
				.map(LandTypePair.Named::asComponent)
				.orElseGet(() -> Component.literal("Land name"))),
		LAND_TITLE((npc, player) -> homeLandTitle(npc)
				.map(Title::asTextComponent)
				.orElseGet(() -> Component.literal("Player title"))),
		LAND_CLASS((npc, player) -> homeLandTitle(npc)
				.map(title -> title.heroClass().asTextComponent())
				.orElseGet(() -> Component.literal("Player class"))),
		LAND_ASPECT((npc, player) -> homeLandTitle(npc)
				.map(title -> title.heroAspect().asTextComponent())
				.orElseGet(() -> Component.literal("Player aspect"))),
		LAND_DENIZEN((npc, player) -> homeLandTitle(npc)
				.map(title -> Component.translatable("denizen." + title.heroAspect().getTranslationKey()))
				.orElseGet(() -> Component.literal("Denizen"))),
		ENTITY_SOUND((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".sound")),
		ENTITY_SOUND_2((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".sound.2")),
		ENTITY_TYPE((npc, player) -> npc.getType().getDescription()),
		ENTITY_TYPES((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".plural")),
		PLAYER_TITLE((npc, player) -> {
			PlayerIdentifier identifier = Objects.requireNonNull(IdentifierHandler.encode(player));
			return Title.getTitle(identifier, player.server)
					.map(Title::asTextComponent)
					.orElseGet(player::getName);
		}),
		/**
		 * Becomes the name of the item that was matched by a {@link com.mraof.minestuck.entity.dialogue.condition.Condition.ItemTagMatch}.
		 */
		MATCHED_ITEM((npc, player) -> {
			DialogueComponent component = ((DialogueEntity) npc).getDialogueComponent();
			return component.getMatchedItem(player).map(item -> item.getName(item.getDefaultInstance()))
					.orElseGet(() -> Component.literal("???"));
		}),
		;
		
		public static final Codec<Argument> CODEC = StringRepresentable.fromEnum(Argument::values);
		
		private final BiFunction<LivingEntity, ServerPlayer, Component> processing;
		
		Argument(BiFunction<LivingEntity, ServerPlayer, Component> processing)
		{
			this.processing = processing;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	private static Optional<ResourceKey<Level>> homeDimension(LivingEntity entity)
	{
		if(entity instanceof ConsortEntity consort)
			return Optional.of(consort.getHomeDimension());
		else
			return Optional.empty();
	}
	
	private static Optional<PlayerIdentifier> homeLandClientPlayer(LivingEntity entity)
	{
		return homeDimension(entity)
				.flatMap(land -> SburbPlayerData.getForLand(land, Objects.requireNonNull(entity.getServer())))
				.map(SburbPlayerData::playerId);
	}
	
	private static Optional<Title> homeLandTitle(LivingEntity entity)
	{
		return homeLandClientPlayer(entity)
				.flatMap(clientPlayer -> Title.getTitle(clientPlayer, Objects.requireNonNull(entity.getServer())));
	}
}