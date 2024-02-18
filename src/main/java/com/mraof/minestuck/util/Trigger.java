package com.mraof.minestuck.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.entity.DialogueEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.util.TriConsumer;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
public class Trigger
{
	private static final Logger LOGGER = LogUtils.getLogger();
	
	public static List<Trigger> deserializeTriggers(JsonObject responseObject)
	{
		JsonArray triggersObject = responseObject.getAsJsonArray("triggers");
		List<Trigger> triggers = new ArrayList<>();
		triggersObject.forEach(triggerElement ->
		{
			JsonObject triggerObject = triggerElement.getAsJsonObject();
			
			String triggerTypeProvider = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("type")).getOrThrow(false, LOGGER::error);
			String triggerContentProvider = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
			String triggerContentExtraProvider = GsonHelper.getAsString(triggerObject, "content_extra", null);
			
			//TODO may throw errors when its not filled in correctly
			String triggerTypeName = triggerTypeProvider.toUpperCase(Locale.ROOT);
			if(Dialogue.enumExists(Type.class, triggerTypeName))
				triggers.add(new Trigger(Type.valueOf(triggerTypeName), triggerContentProvider, triggerContentExtraProvider));
		});
		return triggers;
	}
	
	public static JsonArray serializeTriggers(List<Trigger> triggers)
	{
		JsonArray triggersObject = new JsonArray(triggers.size());
		for(Trigger trigger : triggers)
		{
			JsonObject triggerObject = new JsonObject();
			
			String triggerType = trigger.type.toString().toLowerCase(Locale.ROOT);
			triggerObject.add("type", Codec.STRING.encodeStart(JsonOps.INSTANCE, triggerType).getOrThrow(false, LOGGER::error));
			triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, trigger.content).getOrThrow(false, LOGGER::error));
			triggerObject.addProperty("content_extra", trigger.contentExtra);
			
			triggersObject.add(triggerObject);
		}
		return triggersObject;
	}
	
	public static Trigger read(FriendlyByteBuf buffer)
	{
		return new Trigger(Trigger.Type.fromInt(buffer.readInt()), buffer.readUtf(500), buffer.readUtf(500));
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		int type = this.getType().ordinal();
		String content = this.getContent();
		String contentExtra = this.getContentExtra();
		
		buffer.writeInt(type);
		buffer.writeUtf(content, 500);
		buffer.writeUtf(contentExtra, 500);
	}
	
	public enum Type
	{
		COMMAND((entity, player, s) ->
		{
			//TODO has been causing server side crashes the second time it is run
			if(player != null)
			{
				try(Level level = player.level())
				{
					if(!level.isClientSide)
					{
						//TODO using the entity for this instead of the player failed
						level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), s.getFirst());
					}
				} catch(IOException e)
				{
					LOGGER.debug("Trigger in Dialogue tried to get null level from player");
				}
			}
		}),
		TAKE_ITEM((entity, player, s) ->
		{
			if(player != null)
			{
				int amount = Dialogue.parseIntFromString(s.getSecond(), 1);
				ItemStack stack = Dialogue.findPlayerItem(s.getFirst(), player, amount);
				if(stack != null)
				{
					stack.shrink(amount);
				}
			}
		}),
		SET_DIALOGUE((entity, player, s) ->
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				ResourceLocation newPath = ResourceLocation.tryParse(s.getFirst());
				if(newPath != null)
					dialogueEntity.setDialoguePath(newPath);
			}
		}),
		ADD_CONSORT_REPUTATION((entity, player, s) ->
		{
			//TODO has been causing server side crashes the second time it is run
			if(entity instanceof ConsortEntity consortEntity && player instanceof ServerPlayer serverPlayer)
			{
				PlayerData data = PlayerSavedData.getData(serverPlayer);
				if(data != null)
				{
					try
					{
						data.addConsortReputation(Integer.parseInt(s.getFirst()), consortEntity.getHomeDimension());
					} catch(NumberFormatException ignored)
					{
						LOGGER.debug("Failed to parse string from a Dialogue into an integer");
					}
				}
			}
		});
		
		private final TriConsumer<LivingEntity, Player, Pair<String, String>> conditions;
		
		Type(TriConsumer<LivingEntity, Player, Pair<String, String>> conditions)
		{
			this.conditions = conditions;
		}
		
		public static Type fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < Type.values().length)
				return Type.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for Trigger type!");
		}
	}
	
	private final Type type;
	private final String content;
	@Nullable
	private final String contentExtra;
	
	public Trigger(Type type, String content)
	{
		this(type, content, null);
	}
	
	public Trigger(Type type, String content, @Nullable String contentExtra)
	{
		this.type = type;
		this.content = content;
		this.contentExtra = contentExtra;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public String getContentExtra()
	{
		return contentExtra == null ? "" : contentExtra;
	}
	
	//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
	public void testConditions(LivingEntity entity, Player player)
	{
		type.conditions.accept(entity, player, Pair.of(content, getContentExtra()));
	}
}
