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
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
@MethodsReturnNonnullByDefault
public sealed interface Trigger
{
	Logger LOGGER = LogUtils.getLogger();
	
	static List<Trigger> deserializeTriggers(JsonObject responseObject)
	{
		JsonArray triggersObject = responseObject.getAsJsonArray("triggers");
		List<Trigger> triggers = new ArrayList<>();
		triggersObject.forEach(triggerElement ->
		{
			JsonObject triggerObject = triggerElement.getAsJsonObject();
			
			Optional<Type> optionalType = Type.CODEC.parse(JsonOps.INSTANCE, triggerObject.get("type")).resultOrPartial(LOGGER::error);
			
			//TODO may throw errors when its not filled in correctly
			optionalType.ifPresent(type -> triggers.add(type.deserializer.apply(triggerObject)));
		});
		return triggers;
	}
	
	static JsonArray serializeTriggers(List<Trigger> triggers)
	{
		JsonArray triggersObject = new JsonArray(triggers.size());
		for(Trigger trigger : triggers)
		{
			JsonObject triggerObject = new JsonObject();
			
			triggerObject.add("type", Type.CODEC.encodeStart(JsonOps.INSTANCE, trigger.getType()).getOrThrow(false, LOGGER::error));
			trigger.serialize(triggerObject);
			
			triggersObject.add(triggerObject);
		}
		return triggersObject;
	}
	
	void serialize(JsonObject triggerObject);
	
	static Trigger read(FriendlyByteBuf buffer)
	{
		Type type = Type.fromInt(buffer.readInt());
		return type.bufferReader.apply(buffer);
	}
	
	default void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.getType().ordinal());
		this.writeTrigger(buffer);
	}
	
	void writeTrigger(FriendlyByteBuf buffer);
	
	enum Type implements StringRepresentable
	{
		COMMAND(Command::deserialize, Command::readTrigger),
		TAKE_ITEM(TakeItem::deserialize, TakeItem::readTrigger),
		SET_DIALOGUE(SetDialogue::deserialize, SetDialogue::readTrigger),
		ADD_CONSORT_REPUTATION(AddConsortReputation::deserialize, AddConsortReputation::readTrigger);
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
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
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	Type getType();
	
	//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
	void triggerEffect(LivingEntity entity, Player player);
	
	record Command(String command) implements Trigger
	{
		@Override
		public Type getType()
		{
			return Type.COMMAND;
		}
		
		static Command deserialize(JsonObject triggerObject)
		{
			String contentString = GsonHelper.getAsString(triggerObject, "command");
			return new Command(contentString);
		}
		
		@Override
		public void serialize(JsonObject triggerObject)
		{
			triggerObject.addProperty("command", this.command);
		}
		
		static Command readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			return new Command(contentString);
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(this.command, 500);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			//TODO has been causing server side crashes the second time it is run
			if(player == null)
				return;
			
			Level level = player.level();
			if(!level.isClientSide)
			{
				//TODO using the entity for this instead of the player failed
				level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), this.command);
			}
		}
	}
	
	record TakeItem(Item item, int amount) implements Trigger
	{
		public TakeItem(Item item)
		{
			this(item, 1);
		}
		
		@Override
		public Type getType()
		{
			return Type.TAKE_ITEM;
		}
		
		static TakeItem deserialize(JsonObject triggerObject)
		{
			Item item = ForgeRegistries.ITEMS.getCodec().parse(JsonOps.INSTANCE, triggerObject.get("item")).getOrThrow(false, LOGGER::error);
			int amount = GsonHelper.getAsInt(triggerObject, "amount", 1);
			return new TakeItem(item, amount);
		}
		
		@Override
		public void serialize(JsonObject triggerObject)
		{
			triggerObject.add("item", ForgeRegistries.ITEMS.getCodec().encodeStart(JsonOps.INSTANCE, this.item).getOrThrow(false, LOGGER::error));
			if(this.amount != 1)
				triggerObject.addProperty("amount", this.amount);
		}
		
		static TakeItem readTrigger(FriendlyByteBuf buffer)
		{
			Item item = buffer.readRegistryIdSafe(Item.class);
			int amount = buffer.readInt();
			return new TakeItem(item, amount);
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeRegistryId(ForgeRegistries.ITEMS, this.item);
			buffer.writeInt(this.amount);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			if(player == null)
				return;
			
			ItemStack stack = Dialogue.findPlayerItem(this.item, player, this.amount);
			if(stack != null)
				stack.shrink(this.amount);
		}
	}
	
	record SetDialogue(ResourceLocation newPath) implements Trigger
	{
		@Override
		public Type getType()
		{
			return Type.SET_DIALOGUE;
		}
		
		static SetDialogue deserialize(JsonObject triggerObject)
		{
			ResourceLocation newPath = ResourceLocation.CODEC.parse(JsonOps.INSTANCE, triggerObject.get("new_path")).getOrThrow(false, LOGGER::error);
			return new SetDialogue(newPath);
		}
		
		@Override
		public void serialize(JsonObject triggerObject)
		{
			triggerObject.add("new_path", ResourceLocation.CODEC.encodeStart(JsonOps.INSTANCE, this.newPath).getOrThrow(false, LOGGER::error));
		}
		
		static SetDialogue readTrigger(FriendlyByteBuf buffer)
		{
			return new SetDialogue(buffer.readResourceLocation());
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(this.newPath);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
				dialogueEntity.setDialoguePath(this.newPath);
		}
	}
	
	record AddConsortReputation(int reputation) implements Trigger
	{
		@Override
		public Type getType()
		{
			return Type.ADD_CONSORT_REPUTATION;
		}
		
		static AddConsortReputation deserialize(JsonObject triggerObject)
		{
			int reputation = GsonHelper.getAsInt(triggerObject, "reputation");
			return new AddConsortReputation(reputation);
		}
		
		@Override
		public void serialize(JsonObject triggerObject)
		{
			triggerObject.addProperty("reputation", this.reputation);
		}
		
		static AddConsortReputation readTrigger(FriendlyByteBuf buffer)
		{
			return new AddConsortReputation(buffer.readInt());
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.reputation);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			//TODO has been causing server side crashes the second time it is run
			if(!(entity instanceof ConsortEntity consortEntity) || !(player instanceof ServerPlayer serverPlayer))
				return;
			
			PlayerData data = PlayerSavedData.getData(serverPlayer);
			if(data != null)
				data.addConsortReputation(this.reputation, consortEntity.getHomeDimension());
		}
	}
}
