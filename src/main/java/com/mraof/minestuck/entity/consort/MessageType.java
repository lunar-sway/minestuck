package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.network.DialogueUpdatePacket;
import com.mraof.minestuck.network.MSPacketHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandInfo;
import com.mraof.minestuck.world.storage.PlayerData;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class where message content is defined. Also things such as if it's a chain
 * message or perhaps a message one could reply to
 * There is plenty of room for improvement and to make things more robust
 *
 * @author Kirderf1
 */
public abstract class MessageType
{
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MISSING_ITEM = "consort.missing_item";
	
	public abstract String getString();
	
	public abstract void showMessage(ConsortEntity consort, ServerPlayerEntity player);
	
	public abstract List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player);
	
	protected static ITextComponent createMessage(ConsortEntity consort, ServerPlayerEntity player, String unlocalizedMessage, String[] args)
	{
		String translationKey = consort.getType().getTranslationKey();
		
		Object[] arguments = new Object[args.length];
		SburbConnection connection = SburbHandler.getConnectionForDimension(player.getServer(), consort.homeDimension);
		Title worldTitle = connection == null ? null : PlayerSavedData.getData(connection.getClientIdentifier(), player.server).getTitle();
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("player_name_land"))    //TODO How about extendable objects or enums instead of type strings for args?
			{
				if(connection != null)
					arguments[i] = connection.getClientIdentifier().getUsername();
				else
					arguments[i] = "Player name";
			} else if(args[i].equals("player_name"))
			{
				arguments[i] = player.getName();
			} else if(args[i].equals("land_name"))
			{
				LandInfo landInfo = MSDimensions.getLandInfo(consort.getServer(), consort.homeDimension);
				if(landInfo != null)
				{
					arguments[i] = landInfo.landAsTextComponent();
				} else
					arguments[i] = "Land name";
			} else if(args[i].equals("player_title_land"))
			{
				if(worldTitle != null)
					arguments[i] = worldTitle.asTextComponent();
				else
					arguments[i] = "Player title";
			} else if(args[i].equals("player_class_land"))
			{
				if(worldTitle != null)
					arguments[i] = worldTitle.getHeroClass().asTextComponent();
				else
					arguments[i] = "Player class";
			} else if(args[i].equals("player_aspect_land"))
			{
				if(worldTitle != null)
					arguments[i] = worldTitle.getHeroAspect().asTextComponent();
				else
					arguments[i] = "Player aspect";
			} else if(args[i].equals("consort_sound"))
			{
				arguments[i] = new TranslationTextComponent(translationKey + ".sound");
			} else if(args[i].equals("consort_sound_2"))
			{
				arguments[i] = new TranslationTextComponent(translationKey + ".sound.2");
			} else if(args[i].equals("consort_type"))
			{
				arguments[i] = new TranslationTextComponent(translationKey);
			} else if(args[i].equals("consort_types"))
			{
				arguments[i] = new TranslationTextComponent(translationKey + ".plural");
			} else if(args[i].equals("player_title"))
			{
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				Title playerTitle = PlayerSavedData.getData(identifier, player.server).getTitle();
				if(playerTitle != null)
					arguments[i] = playerTitle.asTextComponent();
				else
					arguments[i] = player.getName();
			} else if(args[i].equals("denizen"))
			{
				if(worldTitle != null)
					arguments[i] = new TranslationTextComponent("denizen." + worldTitle.getHeroAspect().getTranslationKey());
				else
					arguments[i] = "Denizen";
			} else if(args[i].startsWith("nbt_item:"))
			{
				CompoundNBT nbt = consort.getMessageTagForPlayer(player);
				ItemStack stack = ItemStack.read(nbt.getCompound(args[i].substring(9)));
				if(!stack.isEmpty())
					arguments[i] = new TranslationTextComponent(stack.getTranslationKey());
				else arguments[i] = "Item";
			}
		}
		
		return new TranslationTextComponent("consort." + unlocalizedMessage, arguments);
	}
	
	public static class SingleMessage extends MessageType
	{
		protected String unlocalizedMessage;
		protected String[] args;
		
		public SingleMessage(String message, String... args)
		{
			this.unlocalizedMessage = message;
			this.args = args;
		}
		
		@Override
		public String getString()
		{
			return unlocalizedMessage;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			PlayerSavedData.getData(player).setDialogue(consort, this);
			MSPacketHandler.sendToPlayer(new DialogueUpdatePacket(getDialogueCards(consort, player), null), player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			ITextComponent message = createMessage(consort, player, unlocalizedMessage, args);
			String resourcePath = consort.getConsortType().getDialogueSpriteResourcePath();
			return Collections.singletonList(new DialogueCard(message.getFormattedText(), resourcePath, consort.getConsortType().getColor()));
		}
	}
	
	/* Works like SingleMessage but theres no entity portrait and text is in italics*/
	public static class DescriptionMessage extends SingleMessage
	{
		public DescriptionMessage(String message, String... args)
		{
			super(message, args);
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			PlayerSavedData.getData(player).setDialogue(consort, this);
			MSPacketHandler.sendToPlayer(new DialogueUpdatePacket(getDialogueCards(consort, player), null), player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			ITextComponent message = createMessage(consort, player, unlocalizedMessage, args);
			message.getStyle().setItalic(true);
			return Collections.singletonList(new DialogueCard(message.getFormattedText(), null, 0));
		}
	}
	
	/*
	 * ChainMessage will take several messages and iterate through the list as it is accessed further.
	 * repeatIndex is the index of the message to which the consort will loop back when it hits the end of the chain.
	 * By making repeatIndex 0, the whole chain will be repeated. By making it messages.length-1, only the last line will be repeated.
	 * Keep in mind that a ChainMessage will always begin with the first message in the chain,
	 * And that the next message to be said is stored in the NBT of the consort itself.
	 */
	public static class ChainMessage extends MessageType
	{
		protected String nbtName;
		protected SingleMessage[] messages;
		protected int repeatIndex; // TODO: fix this
		
		public ChainMessage(SingleMessage... messages)
		{
			this(0, messages);
		}
		
		public ChainMessage(int repeatIndex, SingleMessage... messages)
		{
			this(repeatIndex, messages[0].getString(), messages);
		}
		
		public ChainMessage(int repeatIndex, String name, SingleMessage... messages)
		{
			this.repeatIndex = repeatIndex;
			this.messages = messages;
			this.nbtName = name;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			List<DialogueCard> dialogueCards = getDialogueCards(consort, player);
			PlayerSavedData.getData(player).setDialogue(consort, this);
			MSPacketHandler.sendToPlayer(new DialogueUpdatePacket(dialogueCards, null), player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			List<DialogueCard> messages = new LinkedList<>();
			for(SingleMessage chainable : this.messages)
			{
				messages.addAll(chainable.getDialogueCards(consort, player));
			}
			
			return messages;
		}
	}
	
	public static class ConditionedMessage extends MessageType
	{
		String nbtName;
		Condition condition;
		MessageType message1, message2;
		
		public ConditionedMessage(Condition condition, MessageType message1, MessageType message2)
		{
			this(message1.getString(), condition, message1, message2);
		}
		
		public ConditionedMessage(String nbtName, Condition condition, MessageType message1, MessageType message2)
		{
			this.nbtName = nbtName;
			this.condition = condition;
			this.message1 = message1;
			this.message2 = message2;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			if(condition.testFor(consort, player))
				message1.showMessage(consort, player);
			else
				message2.showMessage(consort, player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			if(condition.testFor(consort, player))
				return message1.getDialogueCards(consort, player);
			else
				return message2.getDialogueCards(consort, player);
		}
		
		public interface Condition
		{
			boolean testFor(ConsortEntity consort, ServerPlayerEntity player);
		}
	}
	
	//This class functions like a chain message, except that it will select a single entry randomly each time, instead of looping.
	public static class RandomMessage extends SingleMessage
	{
		protected String nbtName;
		protected MessageType[] messages;
		
		public RandomMessage(String name, MessageType... messages)
		{
			super("");
			this.nbtName = name;
			this.messages = messages;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			int i = consort.getRNG().nextInt(messages.length);
			messages[i].showMessage(consort, player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			int i = consort.getRNG().nextInt(messages.length);
			return messages[i].getDialogueCards(consort, player);
		}
	}
	
	public static class ChoiceMessage extends OptionMessage
	{
		public ChoiceMessage(MessageType message, SingleMessage[] options, MessageType[] results)
		{
			super(message, options, createDialogueActions(results));
		}
		
		static private IOptionAction[] createDialogueActions(MessageType[] messages)
		{
			List<IOptionAction> actions = new LinkedList<>();
			for(MessageType result : messages)
			{
				actions.add((player, consort) -> {
					PlayerSavedData.getData(player).setDialogue(consort, result);
					result.showMessage(consort, player);
				});
			}
			
			return actions.toArray(new IOptionAction[0]);
		}
	}
	
	public static class OptionMessage extends MessageType
	{
		protected MessageType message;
		protected SingleMessage[] options;
		protected IOptionAction[] actions;
		
		public OptionMessage(MessageType message, SingleMessage[] options, IOptionAction[] actions)
		{
			if(options.length != actions.length)
				throw new IllegalArgumentException("Option and result arrays must be of equal size!");
			
			this.message = message;
			this.options = options;
			this.actions = actions;
		}
		
		@Override
		public String getString()
		{
			return message.getString();
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			List<String> localizedOptions = Arrays.stream(options).map(option -> createMessage(consort, player, option.unlocalizedMessage, option.args).getFormattedText()).collect(Collectors.toList());
			PlayerSavedData.getData(player).setDialogue(consort, this);
			MSPacketHandler.sendToPlayer(new DialogueUpdatePacket(message.getDialogueCards(consort, player), localizedOptions), player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			return message.getDialogueCards(consort, player);
		}
		
		public void executeAction(int index, ServerPlayerEntity player, ConsortEntity consort)
		{
			actions[index].execute(player, consort);
		}
	}
	
	public interface IOptionAction
	{
		void execute(ServerPlayerEntity player, ConsortEntity consort);
	}
	
	public static class PurchaseMessage extends MessageType
	{
		protected String nbtName;
		protected ResourceLocation lootTableId;
		protected int cost;
		protected int rep;
		protected MessageType message;
		
		public PurchaseMessage(ResourceLocation lootTableId, int cost, MessageType message)
		{
			this(lootTableId, cost, 0, message.getString(), message);
		}
		
		/**
		 * Make sure to use this constructor with a unique name, if the message
		 * is of a type that uses it's own stored data
		 */
		public PurchaseMessage(ResourceLocation lootTableId, int cost, int rep, String name, MessageType message)
		{
			this.nbtName = name;
			this.lootTableId = lootTableId;
			this.cost = cost;
			this.message = message;
			this.rep = rep;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			PlayerData data = PlayerSavedData.getData(player);
			
			if(data.tryTakeBoondollars(cost))
			{
				LootContext.Builder contextBuilder = new LootContext.Builder((ServerWorld) consort.world).withRandom(consort.world.rand)
						.withParameter(LootParameters.THIS_ENTITY, consort).withParameter(LootParameters.POSITION, consort.getPosition());
				List<ItemStack> loot = consort.getServer().getLootTableManager().getLootTableFromLocation(lootTableId)
						.generate(contextBuilder.build(LootParameterSets.GIFT));
				
				if(loot.isEmpty())
					LOGGER.warn("Tried to generate loot from {}, but no items were generated!", lootTableId);
				
				for(ItemStack itemstack : loot)
				{
					player.entityDropItem(itemstack, 0.0F);
					MSCriteriaTriggers.CONSORT_ITEM.trigger(player, lootTableId.toString(), itemstack, consort);
				}
				if(rep != 0)
					data.addConsortReputation(rep, consort.homeDimension);
				message.showMessage(consort, player);
			} else
			{
				new SingleMessage("cant_afford").showMessage(consort, player);
			}
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			return message.getDialogueCards(consort, player);
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
	}
	
	public static class ItemRequirement extends MessageType
	{
		private final String nbtName;
		private final boolean random, held, repeat;
		private final MessageType defaultMessage;
		private final MessageType conditionedMessage;
		private final MessageType repeatMessage;
		private final List<ItemStack> possibleItems;
		
		public ItemRequirement(List<ItemStack> list, boolean random, boolean held, MessageType defaultMessage, MessageType nextMessage, MessageType repeatMessage)
		{
			this(defaultMessage.getString(), list, random, held, false, defaultMessage, nextMessage, repeatMessage);
		}
		
		/**
		 * Used to require the player to possess a certain item to proceed.
		 *
		 * @param name           Name used used for nbt data
		 * @param list           List of potential item requirements
		 * @param random         If the item required should be picked at random or be
		 *                       based on what the player has
		 * @param held           If the item has to be held by the player to count
		 * @param repeat         If the requirement will be rechecked every time this message is reached
		 * @param defaultMessage Message when requirement is not met
		 * @param nextMessage    Message when requirement is met
		 * @param repeatMessage  Message when the dialogue is exhausted
		 */
		public ItemRequirement(String name, List<ItemStack> list, boolean random, boolean held, boolean repeat, MessageType defaultMessage, MessageType nextMessage, MessageType repeatMessage)
		{
			this.nbtName = name;
			this.possibleItems = list;
			this.defaultMessage = defaultMessage;
			this.conditionedMessage = nextMessage;
			this.repeatMessage = repeatMessage;
			this.random = random;
			this.held = held;
			this.repeat = repeat;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.contains(this.getString()))
			{
				repeatMessage.showMessage(consort, player);
				return;
			}
			
			boolean hasItem = false;
			if(random || repeat && nbt.contains(this.getString()))
			{
				int index;
				if(nbt.contains(this.getString()))
					index = nbt.getInt(this.getString());
				else
				{
					index = consort.world.rand.nextInt(possibleItems.size());
					nbt.putInt(this.getString(), index);
				}
				
				ItemStack stack = possibleItems.get(index);
				nbt.put(this.getString() + ".item", stack.write(new CompoundNBT()));
				
				hasItem = lookFor(stack, player);
			} else
			{
				List<ItemStack> list = new ArrayList<>(possibleItems);
				while(!list.isEmpty())
				{
					ItemStack stack = list.remove(consort.world.rand.nextInt(list.size()));
					if(lookFor(stack, player))
					{
						nbt.putInt(this.getString(), possibleItems.indexOf(stack));
						nbt.put(this.getString() + ".item", stack.write(new CompoundNBT()));
						hasItem = true;
						break;
					}
				}
			}
			
			if(hasItem)
			{
				conditionedMessage.showMessage(consort, player);
				return;
			}
			
			defaultMessage.showMessage(consort, player);
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			return defaultMessage.getDialogueCards(consort, player);
		}
		
		private boolean lookFor(ItemStack stack, ServerPlayerEntity player)
		{
			for(ItemStack held : player.getHeldEquipment())
				if(ItemStack.areItemsEqual(held, stack))
					return true;
			
			if(!held)
				for(ItemStack held : player.inventory.mainInventory)
					if(ItemStack.areItemsEqual(held, stack))
						return true;
			
			return false;
		}
	}
	
	public static class GiveItemMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType message;
		protected String itemData;
		protected int boondollars;
		protected int rep;
		
		public GiveItemMessage(String nbtName, String itemData, int boondollars, int rep, MessageType message)
		{
			this.nbtName = nbtName;
			this.itemData = itemData;
			this.boondollars = boondollars;
			this.rep = rep;
			this.message = message;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(nbt.getBoolean(this.getString()))
			{
				message.showMessage(consort, player);
				return;
			}
			
			ItemStack stack = ItemStack.read(nbt.getCompound(itemData));
			
			boolean foundItem = false;
			for(Hand hand : Hand.values())
			{
				ItemStack heldItem = player.getHeldItem(hand);
				if(ItemStack.areItemsEqual(heldItem, stack))
				{
					foundItem = true;
					heldItem.shrink(1);
					break;
				}
			}
			
			if(!foundItem)
			{
				for(ItemStack invItem : player.inventory.mainInventory)
				{
					if(ItemStack.areItemsEqual(invItem, stack))
					{
						foundItem = true;
						invItem.shrink(1);
						break;
					}
				}
			}
			
			if(foundItem)
			{
				if(boondollars != 0)
				{
					PlayerSavedData.getData(player).addBoondollars(boondollars);
				}
				if(rep != 0)
					PlayerSavedData.getData(player).addConsortReputation(rep, consort.homeDimension);
				
				nbt.putBoolean(this.getString(), true);
				message.showMessage(consort, player);
			}
		}
		
		@Override
		public List<DialogueCard> getDialogueCards(ConsortEntity consort, ServerPlayerEntity player)
		{
			return message.getDialogueCards(consort, player);
		}
	}
	
	public static class MerchantGuiMessage extends OptionMessage
	{
		public MerchantGuiMessage(MessageType message, ResourceLocation lootTable)
		{
			super(message, new SingleMessage[]{new SingleMessage("show_shop")}, createShopAction(lootTable));
		}
		
		static private IOptionAction[] createShopAction(ResourceLocation lootTable)
		{
			IOptionAction action = (player, consort) -> {
				if(consort.stocks == null)
				{
					consort.stocks = new ConsortMerchantInventory(consort, ConsortRewardHandler.generateStock(lootTable, consort, consort.world.rand));
				}
				
				NetworkHooks.openGui(player, new SimpleNamedContainerProvider(consort, new StringTextComponent("Consort shop")), consort::writeShopContainerBuffer);
			};
			
			return new IOptionAction[]{action};
		}
	}
	
	public static class ExplosionMessage extends SingleMessage
	{
		public ExplosionMessage(String message, String... args)
		{
			super(message, args);
		}
		
		@Override
		public void showMessage(ConsortEntity consort, ServerPlayerEntity player)
		{
			consort.setExplosionTimer();
			super.showMessage(consort, player);
		}
	}
}