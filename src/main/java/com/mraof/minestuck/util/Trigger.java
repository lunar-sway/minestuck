package com.mraof.minestuck.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.DialogueEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
@MethodsReturnNonnullByDefault
public sealed interface Trigger
{
	Codec<Trigger> CODEC = Type.CODEC.dispatch(Trigger::getType, type -> type.codec.get());
	Codec<List<Trigger>> LIST_CODEC = Trigger.CODEC.listOf();
	
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
	
	enum Type implements StringRepresentable
	{
		SET_DIALOGUE(() -> SetDialogue.CODEC, SetDialogue::readTrigger),
		OPEN_CONSORT_MERCHANT_GUI(() -> OpenConsortMerchantGui.CODEC, OpenConsortMerchantGui::readTrigger),
		COMMAND(() -> Command.CODEC, Command::readTrigger),
		TAKE_ITEM(() -> TakeItem.CODEC, TakeItem::readTrigger),
		ADD_CONSORT_REPUTATION(() -> AddConsortReputation.CODEC, AddConsortReputation::readTrigger);
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<Codec<? extends Trigger>> codec;
		private final Function<FriendlyByteBuf, Trigger> bufferReader;
		
		Type(Supplier<Codec<? extends Trigger>> codec, Function<FriendlyByteBuf, Trigger> bufferReader)
		{
			this.codec = codec;
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
	
	void writeTrigger(FriendlyByteBuf buffer);
	
	Type getType();
	
	//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
	void triggerEffect(LivingEntity entity, Player player);
	
	record SetDialogue(ResourceLocation newPath) implements Trigger
	{
		static final Codec<SetDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("new_path").forGetter(SetDialogue::newPath)
		).apply(instance, SetDialogue::new));
		
		@Override
		public Type getType()
		{
			return Type.SET_DIALOGUE;
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
	
	record OpenConsortMerchantGui(ResourceLocation lootTable, String merchantTypeName) implements Trigger
	{
		static final Codec<OpenConsortMerchantGui> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("loot_table").forGetter(OpenConsortMerchantGui::lootTable),
				Codec.STRING.fieldOf("merchant_type").forGetter(OpenConsortMerchantGui::merchantTypeName)
		).apply(instance, OpenConsortMerchantGui::new));
		
		@Override
		public Type getType()
		{
			return Type.OPEN_CONSORT_MERCHANT_GUI;
		}
		
		static OpenConsortMerchantGui readTrigger(FriendlyByteBuf buffer)
		{
			return new OpenConsortMerchantGui(buffer.readResourceLocation(), buffer.readUtf(500));
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(this.lootTable);
			buffer.writeUtf(this.merchantTypeName, 500);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			if(entity instanceof ConsortEntity consortEntity && player instanceof ServerPlayer serverPlayer)
			{
				//TODO if(consortEntity.merchantType == EnumConsort.MerchantType.NONE) ?
				for(EnumConsort.MerchantType type : EnumConsort.MerchantType.values())
					if(type.name().toLowerCase().equals(this.merchantTypeName))
						consortEntity.merchantType = type;
				
				if(consortEntity.stocks == null)
				{
					consortEntity.stocks = new ConsortMerchantInventory(consortEntity, ConsortRewardHandler.generateStock(this.lootTable, consortEntity, consortEntity.level().random));
				}
				
				NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(consortEntity, Component.literal("Consort shop")), consortEntity::writeShopMenuBuffer);
			}
		}
	}
	
	record Command(String commandText) implements Trigger
	{
		static final Codec<Command> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("command").forGetter(Command::commandText)
		).apply(instance, Command::new));
		
		@Override
		public Type getType()
		{
			return Type.COMMAND;
		}
		
		static Command readTrigger(FriendlyByteBuf buffer)
		{
			String contentString = buffer.readUtf(500);
			return new Command(contentString);
		}
		
		@Override
		public void writeTrigger(FriendlyByteBuf buffer)
		{
			buffer.writeUtf(this.commandText, 500);
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, Player player)
		{
			if(player == null)
				return;
			
			Level level = player.level();
			if(!level.isClientSide)
			{
				//TODO using the entity for this instead of the player failed
				level.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack(), this.commandText);
			}
		}
	}
	
	record TakeItem(Item item, int amount) implements Trigger
	{
		static final Codec<TakeItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(TakeItem::item),
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(TakeItem::amount)
		).apply(instance, TakeItem::new));
		
		public TakeItem(Item item)
		{
			this(item, 1);
		}
		
		@Override
		public Type getType()
		{
			return Type.TAKE_ITEM;
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
	
	record AddConsortReputation(int reputation) implements Trigger
	{
		static final Codec<AddConsortReputation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("reputation").forGetter(AddConsortReputation::reputation)
		).apply(instance, AddConsortReputation::new));
		
		@Override
		public Type getType()
		{
			return Type.ADD_CONSORT_REPUTATION;
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
