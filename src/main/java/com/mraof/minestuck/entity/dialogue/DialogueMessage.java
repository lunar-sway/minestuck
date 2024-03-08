package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.function.BiFunction;

public record DialogueMessage(String message, List<Argument> arguments)
{
	static Codec<DialogueMessage> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(Codec.STRING.fieldOf("message").forGetter(DialogueMessage::message),
							Codec.list(Argument.CODEC).fieldOf("arguments").forGetter(DialogueMessage::arguments))
					.apply(instance, DialogueMessage::new));
	
	/**
	 * Creates a CompoundTag of every DialogueMessage in a Dialogue (including those in a Response)
	 */
	public static CompoundTag writeAllMessagesToCompound(LivingEntity entity, ServerPlayer serverPlayer, Dialogue dialogue)
	{
		CompoundTag messageArgs = new CompoundTag();
		
		CompoundTag dialogueArgs = new CompoundTag();
		dialogue.message().addToCompound(dialogueArgs, entity, serverPlayer);
		messageArgs.put("dialogue_message", dialogueArgs);
		
		CompoundTag responsesArgs = new CompoundTag();
		dialogue.responses().forEach(response -> {
			CompoundTag responseArgs = new CompoundTag();
			response.response().addToCompound(responseArgs, entity, serverPlayer);
			responsesArgs.put(response.response().message(), responseArgs);
		});
		messageArgs.put("responses", responsesArgs);
		
		return messageArgs;
	}
	
	public static Map<String, List<String>> readResponseArgumentsMap(CompoundTag messageArgs)
	{
		CompoundTag responses = messageArgs.getCompound("responses");
		Map<String, List<String>> responseMap = new HashMap<>();
		responses.getAllKeys().forEach(key -> {
			List<String> arguments = readResponseArguments(responses.getCompound(key));
			responseMap.put(key, arguments);
		});
		return responseMap;
	}
	
	private static List<String> readResponseArguments(CompoundTag response)
	{
		return response.getAllKeys().stream().map(response::getString).toList();
	}
	
	public static List<String> readDialogueArgumentsFromCompound(CompoundTag messageArgs)
	{
		List<String> arguments = new ArrayList<>();
		
		CompoundTag message = messageArgs.getCompound("dialogue_message");
		for(String key : message.getAllKeys())
			arguments.add(message.getString(key));
		
		return arguments;
	}
	
	public void addToCompound(CompoundTag dialogueArgs, LivingEntity entity, ServerPlayer serverPlayer)
	{
		arguments.forEach(argument -> dialogueArgs.putString(argument.getSerializedName(), DialogueMessage.getProcessedArgument(argument, entity, serverPlayer)));
	}
	
	public static String getProcessedArgument(Argument argument, LivingEntity npc, ServerPlayer player)
	{
		return argument.processing.apply(npc, player);
	}
	
	public enum Argument implements StringRepresentable
	{
		PLAYER_NAME_LAND((npc, player) ->
		{
			if(npc instanceof ConsortEntity consort)
			{
				SburbConnection connection = getConnection(consort, player);
				if(connection != null)
					return connection.getClientIdentifier().getUsername();
			}
			
			return "Player name";
		});
		
		public static final Codec<Argument> CODEC = Codec.STRING.xmap(Argument::valueOf, Argument::name);
		
		private final BiFunction<LivingEntity, ServerPlayer, String> processing;
		
		Argument(BiFunction<LivingEntity, ServerPlayer, String> processing)
		{
			this.processing = processing;
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	private static SburbConnection getConnection(ConsortEntity consort, ServerPlayer player)
	{
		return SburbHandler.getConnectionForDimension(player.getServer(), consort.getHomeDimension());
	}
}