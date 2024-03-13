package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
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
		PLAYER_NAME_LAND((npc, player) -> homeLandClientPlayer(npc)
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
				.map(title -> title.getHeroClass().asTextComponent())
				.orElseGet(() -> Component.literal("Player class"))),
		LAND_ASPECT((npc, player) -> homeLandTitle(npc)
				.map(title -> title.getHeroAspect().asTextComponent())
				.orElseGet(() -> Component.literal("Player aspect"))),
		LAND_DENIZEN((npc, player) -> homeLandTitle(npc)
				.map(title -> Component.translatable("denizen." + title.getHeroAspect().getTranslationKey()))
				.orElseGet(() -> Component.literal("Denizen"))),
		ENTITY_SOUND((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".sound")),
		ENTITY_SOUND_2((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".sound.2")),
		ENTITY_TYPE((npc, player) -> npc.getType().getDescription()),
		ENTITY_TYPES((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".plural")),
		PLAYER_TITLE((npc, player) -> {
			PlayerIdentifier identifier = Objects.requireNonNull(IdentifierHandler.encode(player));
			Title playerTitle = PlayerSavedData.getData(identifier, player.server).getTitle();
			if(playerTitle != null)
				return playerTitle.asTextComponent();
			else
				return player.getName();
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
				.flatMap(land -> Optional.ofNullable(SburbHandler.getConnectionForDimension(Objects.requireNonNull(entity.getServer()), land)))
				.map(SburbConnection::getClientIdentifier);
	}
	
	private static Optional<Title> homeLandTitle(LivingEntity entity)
	{
		return homeLandClientPlayer(entity)
				.flatMap(clientPlayer -> Optional.ofNullable(PlayerSavedData.getData(clientPlayer, Objects.requireNonNull(entity.getServer())).getTitle()));
	}
}