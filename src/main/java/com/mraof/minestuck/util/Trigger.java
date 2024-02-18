package com.mraof.minestuck.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
public sealed abstract class Trigger
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
			
			//TODO may throw errors when its not filled in correctly
			String triggerTypeName = triggerTypeProvider.toUpperCase(Locale.ROOT);
			if(Dialogue.enumExists(Type.class, triggerTypeName))
			{
				Type type = Type.valueOf(triggerTypeName);
				triggers.add(type.deserializer.apply(triggerObject));
			}
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
			trigger.serialize(triggerObject);
			
			triggersObject.add(triggerObject);
		}
		return triggersObject;
	}
	
	abstract void serialize(JsonObject triggerObject);
	
	public static Trigger read(FriendlyByteBuf buffer)
	{
		Type type = Type.fromInt(buffer.readInt());
		return type.bufferReader.apply(buffer);
	}
	
	public final void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.getType().ordinal());
		this.writeTrigger(buffer);
	}
	
	abstract void writeTrigger(FriendlyByteBuf buffer);
	
	public enum Type
	{
		COMMAND(Command::deserialize, Command::readTrigger),
		TAKE_ITEM(TakeItem::deserialize, TakeItem::readTrigger),
		SET_DIALOGUE(SetDialogue::deserialize, SetDialogue::readTrigger),
		ADD_CONSORT_REPUTATION(AddConsortReputation::deserialize, AddConsortReputation::readTrigger);
		
		private final Function<JsonObject, Trigger> deserializer;
		private final Function<FriendlyByteBuf, Trigger> bufferReader;
		
		Type(Function<JsonObject, Trigger> deserializer, Function<FriendlyByteBuf, Trigger> bufferReader)
		{
			this.deserializer = deserializer;
			this.bufferReader = bufferReader;
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
	
	public Trigger(Type type)
	{
		this.type = type;
	}
	
	public Type getType()
	{
		return type;
	}
	
	//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
	public abstract void testConditions(LivingEntity entity, Player player);
	
	public static final class Command extends Trigger
	{
		private final String command;
		
		public Command(String command)
		{
			super(Type.COMMAND);
			this.command = command;
		}
		
		static Command deserialize(JsonObject triggerObject)
		{
			String contentString = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
			return new Command(contentString);
		}
		
		@Override
		void serialize(JsonObject triggerObject)
		{
			triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, this.command).getOrThrow(false, LOGGER::error));
		}
		
		static Command readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			return new Command(contentString);
		}
		
		@Override
		void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(this.command, 500);
		}
		
		@Override
		public void testConditions(LivingEntity entity, Player player)
		{
			//TODO has been causing server side crashes the second time it is run
			if(player != null)
			{
				try(Level level = player.level())
				{
					if(!level.isClientSide)
					{
						//TODO using the entity for this instead of the player failed
						level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), this.command);
					}
				} catch(IOException e)
				{
					LOGGER.debug("Trigger in Dialogue tried to get null level from player");
				}
			}
		}
	}
	
	public static final class TakeItem extends Trigger
	{
		private final Item item;
		private final int amount;
		
		public TakeItem(Item item)
		{
			this(item, 1);
		}
		
		public TakeItem(Item item, int amount)
		{
			super(Type.TAKE_ITEM);
			this.item = item;
			this.amount = amount;
		}
		
		static TakeItem deserialize(JsonObject triggerObject)
		{
			String contentString = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
			String extraContentString = GsonHelper.getAsString(triggerObject, "content_extra", "");
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(contentString));
			int amount = Dialogue.parseIntFromString(extraContentString, 1);
			return new TakeItem(item, amount);
		}
		
		@Override
		void serialize(JsonObject triggerObject)
		{
			triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, String.valueOf(ForgeRegistries.ITEMS.getKey(this.item))).getOrThrow(false, LOGGER::error));
			if(this.amount != 1)
				triggerObject.addProperty("content_extra", String.valueOf(this.amount));
		}
		
		static TakeItem readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			String extraContentString = buffer.readUtf(500);
			Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(contentString));
			int amount = Dialogue.parseIntFromString(extraContentString, 1);
			return new TakeItem(item, amount);
		}
		
		@Override
		void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(String.valueOf(ForgeRegistries.ITEMS.getKey(this.item)), 500);
			buffer.writeUtf(String.valueOf(this.amount), 500);
		}
		
		@Override
		public void testConditions(LivingEntity entity, Player player)
		{
			if(player != null)
			{
				ItemStack stack = Dialogue.findPlayerItem(this.item, player, this.amount);
				if(stack != null)
				{
					stack.shrink(this.amount);
				}
			}
		}
	}
	
	public static final class SetDialogue extends Trigger
	{
		private final ResourceLocation newPath;
		
		public SetDialogue(ResourceLocation newPath)
		{
			super(Type.SET_DIALOGUE);
			this.newPath = newPath;
		}
		
		static SetDialogue deserialize(JsonObject triggerObject)
		{
			String contentString = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
			return new SetDialogue(ResourceLocation.tryParse(contentString));
		}
		
		@Override
		void serialize(JsonObject triggerObject)
		{
			triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, String.valueOf(this.newPath)).getOrThrow(false, LOGGER::error));
		}
		
		static SetDialogue readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			return new SetDialogue(ResourceLocation.tryParse(contentString));
		}
		
		@Override
		void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(String.valueOf(this.newPath), 500);
		}
		
		@Override
		public void testConditions(LivingEntity entity, Player player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				if(this.newPath != null)
					dialogueEntity.setDialoguePath(this.newPath);
			}
		}
	}
	
	public static final class AddConsortReputation extends Trigger
	{
		private final int reputation;
		
		public AddConsortReputation(int reputation)
		{
			super(Type.ADD_CONSORT_REPUTATION);
			this.reputation = reputation;
		}
		
		static AddConsortReputation deserialize(JsonObject triggerObject)
		{
			String contentString = Codec.STRING.parse(JsonOps.INSTANCE, triggerObject.get("content")).getOrThrow(false, LOGGER::error);
			return new AddConsortReputation(Integer.parseInt(contentString));
		}
		
		@Override
		void serialize(JsonObject triggerObject)
		{
			triggerObject.add("content", Codec.STRING.encodeStart(JsonOps.INSTANCE, String.valueOf(this.reputation)).getOrThrow(false, LOGGER::error));
		}
		
		static AddConsortReputation readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			return new AddConsortReputation(Integer.parseInt(contentString));
		}
		
		@Override
		void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(String.valueOf(this.reputation), 500);
		}
		
		@Override
		public void testConditions(LivingEntity entity, Player player)
		{
			//TODO has been causing server side crashes the second time it is run
			if(entity instanceof ConsortEntity consortEntity && player instanceof ServerPlayer serverPlayer)
			{
				PlayerData data = PlayerSavedData.getData(serverPlayer);
				if(data != null)
				{
					data.addConsortReputation(this.reputation, consortEntity.getHomeDimension());
				}
			}
		}
	}
}
