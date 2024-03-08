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

import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

public record DialogueMessage(String message, List<Argument> arguments)
{
	static Codec<DialogueMessage> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(Codec.STRING.fieldOf("message").forGetter(DialogueMessage::message),
							Codec.list(Argument.CODEC).fieldOf("arguments").forGetter(DialogueMessage::arguments))
					.apply(instance, DialogueMessage::new));
	
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