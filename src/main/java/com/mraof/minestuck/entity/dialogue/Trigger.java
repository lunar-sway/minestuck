package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
@MethodsReturnNonnullByDefault
public sealed interface Trigger
{
	Codec<Trigger> CODEC = Type.CODEC.dispatch(Trigger::getType, type -> type.codec.get());
	Codec<List<Trigger>> LIST_CODEC = Trigger.CODEC.listOf();
	
	enum Type implements StringRepresentable
	{
		SET_DIALOGUE(() -> SetDialogue.CODEC),
		SET_DIALOGUE_FROM_LIST(() -> SetDialogueFromList.CODEC),
		SET_PLAYER_DIALOGUE(() -> SetPlayerDialogue.CODEC),
		OPEN_CONSORT_MERCHANT_GUI(() -> OpenConsortMerchantGui.CODEC),
		COMMAND(() -> Command.CODEC),
		TAKE_ITEM(() -> TakeItem.CODEC),
		TAKE_MATCHED_ITEM(() -> TakeMatchedItem.CODEC),
		GIVE_ITEM(() -> GiveItem.CODEC),
		GIVE_FROM_LOOT_TABLE(() -> GiveFromLootTable.CODEC),
		ADD_CONSORT_REPUTATION(() -> AddConsortReputation.CODEC),
		ADD_BOONDOLLARS(() -> AddBoondollars.CODEC),
		EXPLODE(() -> Explode.CODEC),
		SET_PLAYER_FLAG(() -> SetPlayerFlag.CODEC),
		SET_RANDOM_PLAYER_FLAG(() -> SetRandomPlayerFlag.CODEC),
		;
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<Codec<? extends Trigger>> codec;
		
		Type(Supplier<Codec<? extends Trigger>> codec)
		{
			this.codec = codec;
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
	
	void triggerEffect(LivingEntity entity, ServerPlayer player);
	
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
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
				dialogueEntity.getDialogueComponent().setDialogue(this.newPath, false);
		}
	}
	
	record SetDialogueFromList(List<ResourceLocation> newPaths) implements Trigger
	{
		static final Codec<SetDialogueFromList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.list(ResourceLocation.CODEC).fieldOf("new_paths").forGetter(SetDialogueFromList::newPaths)
		).apply(instance, SetDialogueFromList::new));
		
		@Override
		public Type getType()
		{
			return Type.SET_DIALOGUE_FROM_LIST;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				dialogueEntity.getDialogueComponent().setDialogue(Util.getRandom(this.newPaths, entity.level().random), false);
			}
		}
	}
	
	record SetPlayerDialogue(ResourceLocation dialogueId) implements Trigger
	{
		static final Codec<SetPlayerDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("dialogue").forGetter(SetPlayerDialogue::dialogueId)
		).apply(instance, SetPlayerDialogue::new));
		
		@Override
		public Type getType()
		{
			return Type.SET_PLAYER_DIALOGUE;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			((DialogueEntity) entity).getDialogueComponent().setDialogueForPlayer(player, this.dialogueId);
		}
	}
	
	record OpenConsortMerchantGui(ResourceLocation lootTable) implements Trigger
	{
		static final Codec<OpenConsortMerchantGui> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("loot_table").forGetter(OpenConsortMerchantGui::lootTable)
		).apply(instance, OpenConsortMerchantGui::new));
		
		@Override
		public Type getType()
		{
			return Type.OPEN_CONSORT_MERCHANT_GUI;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof ConsortEntity consortEntity)
			{
				if(consortEntity.stocks == null)
				{
					consortEntity.stocks = new ConsortMerchantInventory(consortEntity, ConsortRewardHandler.generateStock(this.lootTable, consortEntity, consortEntity.level().random));
				}
				
				NetworkHooks.openScreen(player, new SimpleMenuProvider(consortEntity, Component.literal("Consort shop")), consortEntity::writeShopMenuBuffer);
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
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
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
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return;
			
			ItemStack stack = Condition.PlayerHasItem.findPlayerItem(this.item, player, this.amount);
			if(stack != null)
				stack.shrink(this.amount);
		}
	}
	
	/**
	 * Take one of the item that was matched by a {@link com.mraof.minestuck.entity.dialogue.condition.Condition.ItemTagMatch}.
	 */
	enum TakeMatchedItem implements Trigger
	{
		INSTANCE;
		static final Codec<TakeMatchedItem> CODEC = Codec.unit(INSTANCE);
		
		@Override
		public Type getType()
		{
			return Type.TAKE_MATCHED_ITEM;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			PlayerIdentifier playerId = IdentifierHandler.encode(player);
			if(playerId == null)
				return;
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> matchedItem = component.getMatchedItem(playerId);
			matchedItem.ifPresent(item -> {
				ItemStack matchedStack = Condition.PlayerHasItem.findPlayerItem(item, player, 1);
				if(matchedStack != null)
					matchedStack.shrink(1);
			});
		}
	}
	
	record GiveItem(Item item, int amount) implements Trigger
	{
		static final Codec<GiveItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(GiveItem::item),
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(GiveItem::amount)
		).apply(instance, GiveItem::new));
		
		@Override
		public Type getType()
		{
			return Type.GIVE_ITEM;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return;
			
			player.addItem(new ItemStack(item, amount));
		}
	}
	
	record GiveFromLootTable(ResourceLocation lootTable) implements Trigger
	{
		private static final Logger LOGGER = LogManager.getLogger();
		static final Codec<GiveFromLootTable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("loot_table").forGetter(GiveFromLootTable::lootTable)
		).apply(instance, GiveFromLootTable::new));
		
		@Override
		public Type getType()
		{
			return Type.GIVE_FROM_LOOT_TABLE;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return;
			
			LootParams.Builder builder = new LootParams.Builder((ServerLevel) entity.level())
					.withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position());
			List<ItemStack> loot = entity.getServer().getLootData().getLootTable(lootTable)
					.getRandomItems(builder.create(LootContextParamSets.GIFT));
			
			if(loot.isEmpty())
				LOGGER.warn("Tried to generate loot from {}, but no items were generated!", lootTable);
			
			for(ItemStack itemstack : loot)
			{
				player.spawnAtLocation(itemstack, 0.0F);
				if(entity instanceof ConsortEntity consortEntity)
					MSCriteriaTriggers.CONSORT_ITEM.trigger(player, lootTable.toString(), itemstack, consortEntity);
			}
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
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(!(entity instanceof ConsortEntity consortEntity))
				return;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				data.addConsortReputation(this.reputation, consortEntity.getHomeDimension());
		}
	}
	
	record AddBoondollars(int boondollars) implements Trigger
	{
		static final Codec<AddBoondollars> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("boondollars").forGetter(AddBoondollars::boondollars)
		).apply(instance, AddBoondollars::new));
		
		@Override
		public Type getType()
		{
			return Type.ADD_BOONDOLLARS;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null && boondollars != 0)
			{
				if(boondollars > 0)
					data.addBoondollars(boondollars);
				else
					data.takeBoondollars(-boondollars);
			}
		}
	}
	
	record Explode() implements Trigger
	{
		static final Codec<Explode> CODEC = Codec.unit(Explode::new);
		
		@Override
		public Type getType()
		{
			return Type.EXPLODE;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof ConsortEntity consortEntity)
				consortEntity.setExplosionTimer();
		}
	}
	
	record SetPlayerFlag(String flag) implements Trigger
	{
		static final Codec<SetPlayerFlag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("flag").forGetter(SetPlayerFlag::flag)
		).apply(instance, SetPlayerFlag::new));
		
		@Override
		public Type getType()
		{
			return Type.SET_PLAYER_FLAG;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			PlayerIdentifier playerId = IdentifierHandler.encode(player);
			if(playerId != null && entity instanceof DialogueEntity dialogueEntity)
				dialogueEntity.getDialogueComponent().playerFlags(playerId).add(this.flag);
		}
	}
	
	record SetRandomPlayerFlag(List<String> flags) implements Trigger
	{
		static final Codec<SetRandomPlayerFlag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.listOf().fieldOf("flags").forGetter(SetRandomPlayerFlag::flags)
		).apply(instance, SetRandomPlayerFlag::new));
		
		@Override
		public Type getType()
		{
			return Type.SET_RANDOM_PLAYER_FLAG;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(this.flags.isEmpty())
				return;
			PlayerIdentifier playerId = IdentifierHandler.encode(player);
			if(playerId != null && entity instanceof DialogueEntity dialogueEntity)
			{
				Set<String> playerFlags = dialogueEntity.getDialogueComponent().playerFlags(playerId);
				if(flags.stream().noneMatch(playerFlags::contains))
					playerFlags.add(Util.getRandom(this.flags, entity.getRandom()));
			}
		}
	}
}
