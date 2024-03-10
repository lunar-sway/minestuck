package com.mraof.minestuck.entity.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortRewardHandler;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.DialogueManager;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Locale;
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
		SET_RANDOM_DIALOGUE(() -> SetRandomDialogue.CODEC),
		OPEN_CONSORT_MERCHANT_GUI(() -> OpenConsortMerchantGui.CODEC),
		COMMAND(() -> Command.CODEC),
		TAKE_ITEM(() -> TakeItem.CODEC),
		ADD_CONSORT_REPUTATION(() -> AddConsortReputation.CODEC),
		ADD_BOONDOLLARS(() -> AddBoondollars.CODEC),
		EXPLODE(() -> Explode.CODEC);
		
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
				dialogueEntity.setDialoguePath(this.newPath);
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
				dialogueEntity.setDialoguePath(newPaths.get(entity.level().random.nextInt(newPaths().size())));
			}
		}
	}
	
	record SetRandomDialogue() implements Trigger
	{
		static final Codec<SetRandomDialogue> CODEC = Codec.unit(SetRandomDialogue::new);
		
		@Override
		public Type getType()
		{
			return Type.SET_RANDOM_DIALOGUE;
		}
		
		@Override
		public void triggerEffect(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				Dialogue dialogue = DialogueManager.getInstance().doRandomDialogue(entity, entity.getRandom());
				if(dialogue != null)
					dialogueEntity.setDialoguePath(dialogue.lookupId());
			}
		}
	}
	
	record OpenConsortMerchantGui(ResourceLocation lootTable, EnumConsort.MerchantType merchantType) implements Trigger
	{
		static final Codec<OpenConsortMerchantGui> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ResourceLocation.CODEC.fieldOf("loot_table").forGetter(OpenConsortMerchantGui::lootTable),
				EnumConsort.MerchantType.CODEC.fieldOf("merchant_type").forGetter(OpenConsortMerchantGui::merchantType)
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
				//TODO if(consortEntity.merchantType == EnumConsort.MerchantType.NONE) ?
				consortEntity.merchantType = this.merchantType;
				
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
					data.takeBoondollars(boondollars);
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
}
