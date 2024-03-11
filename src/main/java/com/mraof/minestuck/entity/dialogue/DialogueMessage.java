package com.mraof.minestuck.entity.dialogue;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
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
	
	public Component evaluateComponent(LivingEntity entity, ServerPlayer serverPlayer)
	{
		return Component.translatable(this.key, this.arguments.stream().map(argument -> argument.processing.apply(entity, serverPlayer)).toArray());
	}
	
	public enum Argument implements StringRepresentable
	{
		PLAYER_NAME_LAND((npc, player) ->
		{
			if(npc instanceof ConsortEntity consort)
			{
				SburbConnection connection = getConnection(consort, player);
				if(connection != null)
					return Component.literal(connection.getClientIdentifier().getUsername());
			}
			
			return Component.literal("Player name");
		}),
		ENTITY_TYPE((npc, player) -> npc.getType().getDescription()),
		ENTITY_TYPES((npc, player) -> Component.translatable(npc.getType().getDescriptionId() + ".plural")),
		;
		
		public static final Codec<Argument> CODEC = Codec.STRING.xmap(Argument::valueOf, Argument::name);
		
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
	
	@Nullable
	private static SburbConnection getConnection(ConsortEntity consort, ServerPlayer player)
	{
		return SburbHandler.getConnectionForDimension(player.getServer(), consort.getHomeDimension());
	}
}