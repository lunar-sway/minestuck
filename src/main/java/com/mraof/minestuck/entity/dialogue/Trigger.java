package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortReputation;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A Trigger allows for new code to be called when a dialogue option is picked
 */
@MethodsReturnNonnullByDefault
public sealed interface Trigger
{
	Codec<Trigger> CODEC = Triggers.REGISTRY.byNameCodec().dispatch(Trigger::codec, Function.identity());
	Codec<List<Trigger>> LIST_CODEC = Trigger.CODEC.listOf();
	
	Codec<? extends Trigger> codec();
	
	void triggerEffect(LivingEntity entity, ServerPlayer player);
	
	record SetDialogue(ResourceLocation newPath) implements Trigger
	{
		static final Codec<SetDialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("new_path").forGetter(SetDialogue::newPath)
		).apply(instance, SetDialogue::new));
		
		@Override
		public Codec<SetDialogue> codec()
		{
			return CODEC;
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
		public Codec<SetDialogueFromList> codec()
		{
			return CODEC;
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
		public Codec<SetPlayerDialogue> codec()
		{
			return CODEC;
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
		public Codec<OpenConsortMerchantGui> codec()
		{
			return CODEC;
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
				
				player.openMenu(new SimpleMenuProvider(consortEntity, Component.literal("Consort shop")), consortEntity::writeShopMenuBuffer);
			}
		}
	}
	
	record Command(String commandText) implements Trigger
	{
		static final Codec<Command> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("command").forGetter(Command::commandText)
		).apply(instance, Command::new));
		
		@Override
		public Codec<Command> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return;
			
			//TODO using the entity for this instead of the player failed
			CommandSourceStack sourceStack = player.createCommandSourceStack().withSuppressedOutput().withPermission(Commands.LEVEL_GAMEMASTERS);
			player.server.getCommands().performPrefixedCommand(sourceStack, this.commandText);
		}
	}
	
	record TakeItem(Item item, int amount) implements Trigger
	{
		static final Codec<TakeItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(TakeItem::item),
				ExtraCodecs.strictOptionalField(Codec.INT, "amount", 1).forGetter(TakeItem::amount)
		).apply(instance, TakeItem::new));
		
		public TakeItem(Item item)
		{
			this(item, 1);
		}
		
		@Override
		public Codec<TakeItem> codec()
		{
			return CODEC;
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
		public Codec<TakeMatchedItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> matchedItem = component.getMatchedItem(player);
			matchedItem.ifPresent(item -> {
				ItemStack matchedStack = Condition.PlayerHasItem.findPlayerItem(item, player, 1);
				if(matchedStack != null)
					matchedStack.shrink(1);
			});
		}
	}
	
	static DataResult<EquipmentSlot> parseSlot(String slotName)
	{
		try
		{
			return DataResult.success(EquipmentSlot.byName(slotName));
		} catch(IllegalArgumentException e)
		{
			return DataResult.error(() -> "Not a valid name for an EquipmentSlot: " + slotName);
		}
	}
	
	Codec<EquipmentSlot> EQUIPMENT_CODEC = Codec.STRING.comapFlatMap(
			Trigger::parseSlot,
			EquipmentSlot::getName
	);
	
	record SetNPCItem(Item item, EquipmentSlot slot) implements Trigger
	{
		static final Codec<SetNPCItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(SetNPCItem::item),
				ExtraCodecs.strictOptionalField(EQUIPMENT_CODEC, "slot", EquipmentSlot.MAINHAND).forGetter(SetNPCItem::slot)
		).apply(instance, SetNPCItem::new));
		
		@Override
		public Codec<SetNPCItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			entity.setItemSlot(slot, new ItemStack(item));
		}
	}
	
	/**
	 * If a matched item is found in the player inventory, it is removed from there and then added to the specified slot in the npc inventory
	 */
	record SetNPCMatchedItem(EquipmentSlot slot) implements Trigger
	{
		static final Codec<SetNPCMatchedItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.strictOptionalField(EQUIPMENT_CODEC, "slot", EquipmentSlot.MAINHAND).forGetter(SetNPCMatchedItem::slot)
		).apply(instance, SetNPCMatchedItem::new));
		
		@Override
		public Codec<SetNPCMatchedItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> matchedItem = component.getMatchedItem(player);
			matchedItem.ifPresent(item -> {
				ItemStack matchedStack = Condition.PlayerHasItem.findPlayerItem(item, player, 1);
				if(matchedStack != null)
				{
					entity.setItemSlot(slot, new ItemStack(item));
					matchedStack.shrink(1);
				}
			});
		}
	}
	
	record GiveItem(Item item, int amount) implements Trigger
	{
		static final Codec<GiveItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(GiveItem::item),
				ExtraCodecs.strictOptionalField(Codec.INT, "amount", 1).forGetter(GiveItem::amount)
		).apply(instance, GiveItem::new));
		
		public GiveItem(Item item)
		{
			this(item, 1);
		}
		
		@Override
		public Codec<GiveItem> codec()
		{
			return CODEC;
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
		public Codec<GiveFromLootTable> codec()
		{
			return CODEC;
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
					MSCriteriaTriggers.CONSORT_ITEM.get().trigger(player, lootTable.toString(), itemstack, consortEntity);
			}
		}
	}
	
	record AddConsortReputation(int reputation) implements Trigger
	{
		static final Codec<AddConsortReputation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("reputation").forGetter(AddConsortReputation::reputation)
		).apply(instance, AddConsortReputation::new));
		
		@Override
		public Codec<AddConsortReputation> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(!(entity instanceof ConsortEntity consortEntity))
				return;
			
			PlayerData.get(player).ifPresent(playerData ->
					ConsortReputation.get(playerData).addConsortReputation(this.reputation, consortEntity.getHomeDimension())
			);
		}
	}
	
	record AddBoondollars(int boondollars) implements Trigger
	{
		static final Codec<AddBoondollars> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("boondollars").forGetter(AddBoondollars::boondollars)
		).apply(instance, AddBoondollars::new));
		
		@Override
		public Codec<AddBoondollars> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			Optional<PlayerData> data = PlayerData.get(player);
			if(data.isPresent() && boondollars != 0)
			{
				if(boondollars > 0)
					PlayerBoondollars.addBoondollars(data.get(), boondollars);
				else
					PlayerBoondollars.takeBoondollars(data.get(), -boondollars);
			}
		}
	}
	
	record Explode() implements Trigger
	{
		static final Codec<Explode> CODEC = Codec.unit(Explode::new);
		
		@Override
		public Codec<Explode> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof ConsortEntity consortEntity)
				consortEntity.setExplosionTimer();
		}
	}
	
	record SetFlag(String flag, boolean isPlayerSpecific) implements Trigger
	{
		static final Codec<SetFlag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.fieldOf("flag").forGetter(SetFlag::flag),
				Codec.BOOL.fieldOf("player_specific").forGetter(SetFlag::isPlayerSpecific)
		).apply(instance, SetFlag::new));
		
		@Override
		public Codec<SetFlag> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				DialogueComponent component = dialogueEntity.getDialogueComponent();
				Set<String> flags = this.isPlayerSpecific ? component.playerFlags(player) : component.flags();
				flags.add(this.flag);
			}
		}
	}
	
	record SetRandomFlag(List<String> flags, boolean isPlayerSpecific) implements Trigger
	{
		static final Codec<SetRandomFlag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.STRING.listOf().fieldOf("flags").forGetter(SetRandomFlag::flags),
				Codec.BOOL.fieldOf("player_specific").forGetter(SetRandomFlag::isPlayerSpecific)
		).apply(instance, SetRandomFlag::new));
		
		@Override
		public Codec<SetRandomFlag> codec()
		{
			return CODEC;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(this.flags.isEmpty())
				return;
			
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				DialogueComponent component = dialogueEntity.getDialogueComponent();
				Set<String> flags = this.isPlayerSpecific ? component.playerFlags(player) : component.flags();
				if(this.flags.stream().noneMatch(flags::contains))
					flags.add(Util.getRandom(this.flags, entity.getRandom()));
			}
		}
	}
}
