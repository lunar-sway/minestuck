package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class where message content is defined. Also things such as if it's a chain
 * message or perhaps a message one could reply to
 * There is plenty of room for improvement and to make things more robust
 * @author Kirderf1
 */
public abstract class MessageType
{
	
	public static final String MISSING_ITEM = "consort.missing_item";
	
	public abstract String getString();
	
	public abstract MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier);
	
	public abstract MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
														   String fromChain);
	
	protected abstract void debugAddAllMessages(List<Component> list);
	
	private static MutableComponent createMessage(ConsortEntity consort, ServerPlayer player, String unlocalizedMessage,
												String[] args, boolean consortPrefix)
	{
		String s = consort.getType().getDescriptionId();
		
		Object[] obj = new Object[args.length];
		SburbConnection c = SburbHandler.getConnectionForDimension(player.getServer(), consort.homeDimension);
		Title worldTitle = c == null ? null : PlayerSavedData.getData(c.getClientIdentifier(), player.server).getTitle();
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("player_name_land"))	//TODO How about extendable objects or enums instead of type strings for args?
			{
				if(c != null)
					obj[i] = c.getClientIdentifier().getUsername();
				else
					obj[i] = "Player name";
			} else if(args[i].equals("player_name"))
			{
				obj[i] = player.getName();
			} else if(args[i].equals("land_name"))
			{
				Optional<LandTypePair.Named> landTypes = LandTypePair.getNamed(consort.getServer(), consort.homeDimension);
				if(landTypes.isPresent())
					obj[i] = landTypes.get().asComponent();
				else
					obj[i] = "Land name";
			} else if(args[i].equals("player_title_land"))
			{
				if(worldTitle != null)
					obj[i] = worldTitle.asTextComponent();
				else
					obj[i] = "Player title";
			} else if(args[i].equals("player_class_land"))
			{
				if(worldTitle != null)
					obj[i] = worldTitle.getHeroClass().asTextComponent();
				else
					obj[i] = "Player class";
			} else if(args[i].equals("player_aspect_land"))
			{
				if(worldTitle != null)
					obj[i] = worldTitle.getHeroAspect().asTextComponent();
				else
					obj[i] = "Player aspect";
			} else if(args[i].equals("consort_sound"))
			{
				obj[i] = Component.translatable(s + ".sound");
			} else if(args[i].equals("consort_sound_2"))
			{
				obj[i] = Component.translatable(s + ".sound.2");
			} else if(args[i].equals("consort_type"))
			{
				obj[i] = Component.translatable(s);
			} else if(args[i].equals("consort_types"))
			{
				obj[i] = Component.translatable(s + ".plural");
			} else if(args[i].equals("player_title"))
			{
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				Title playerTitle = PlayerSavedData.getData(identifier, player.server).getTitle();
				if(playerTitle != null)
					obj[i] = playerTitle.asTextComponent();
				else
					obj[i] = player.getName();
			} else if(args[i].equals("denizen"))
			{
				if(worldTitle != null)
					obj[i] = Component.translatable("denizen." + worldTitle.getHeroAspect().getTranslationKey());
				else
					obj[i] = "Denizen";
			} else if(args[i].startsWith("nbt_item:"))
			{
				CompoundTag nbt = consort.getMessageTagForPlayer(player);
				ItemStack stack = ItemStack.of(nbt.getCompound(args[i].substring(9)));
				if(!stack.isEmpty())
					obj[i] = Component.translatable(stack.getDescriptionId());
				else obj[i] = "Item";
			}
		}
		
		MutableComponent message = Component.translatable("consort." + unlocalizedMessage, obj);
		if(consortPrefix)
		{
			message.withStyle(consort.getConsortType().getColor());
			Component entity = Component.translatable(s);
			
			return Component.translatable("chat.type.text", entity, message);
		} else
			return message;
	}
	
	private static String addTo(String chain, String name)
	{
		if(!chain.isEmpty())
			chain += ':';
		return chain + name;
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
		
		/**
		 * Like getMessage(), but does not set the consort's message tag for the player.
		 * Useful for comparing values in post-localized, post-formatted messages.
		 * Note that this may format the text with information subject to change, like the name of the current Land.
		 * @return The value getMessage() would return
		 */
		public Component getMessageForTesting(ConsortEntity consort, ServerPlayer player)
		{
			return createMessage(consort, player, unlocalizedMessage, args, true);
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			consort.getMessageTagForPlayer(player).putString("currentMessage", this.getString());
			return createMessage(consort, player, unlocalizedMessage, args, true);
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			//noinspection RedundantCast
			list.add(Component.translatable("consort." + unlocalizedMessage, (Object[]) args));
		}
	}
	
	//This class takes two separate messages and treats them as one.
	//This allows several messages to group together and be sent to the chat simultaneously.
	//Used in rap battles.
	//Note that only the last option in a DoubleMessage can be a ChoiceMessage.
	public static class DoubleMessage extends MessageType
	{
		protected MessageType messageOne;
		protected MessageType messageTwo;
		protected String nbtName;
		protected boolean firstOnce;
		
		public DoubleMessage(MessageType messageOne, MessageType messageTwo)
		{
			this.messageOne = messageOne;
			this.messageTwo = messageTwo;
			nbtName = messageOne.getString();
		}
		
		public DoubleMessage(MessageType messageOne, MessageType messageTwo, String s)
		{
			this(messageOne, messageTwo);
			nbtName = s;
		}
		
		/**
		 * Will make the first of the two messages only appear the first time
		 */
		public DoubleMessage setSayFirstOnce()
		{
			firstOnce = true;
			return this;
		}
		
		@Override
		public String getString() {
			return nbtName;
		}

		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player,
										 String chainIdentifier)
		{
			if(firstOnce)
			{
				CompoundTag nbt = consort.getMessageTagForPlayer(player);
				if(!nbt.contains(this.getString()) || !nbt.getBoolean(this.getString()))
					nbt.putBoolean(this.getString(), true);
				else return messageTwo.getMessage(consort, player, chainIdentifier);
			}
			
			MutableComponent message = messageOne.getMessage(consort, player, chainIdentifier);
			message.append("\n");
			message.append(messageTwo.getMessage(consort, player, chainIdentifier));
			return message;
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player,
										   String chainIdentifier, String fromChain)
		{
			MutableComponent message = messageTwo.getFromChain(consort, player, chainIdentifier, fromChain);
			return message;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			messageOne.debugAddAllMessages(list);
			messageTwo.debugAddAllMessages(list);
		}
	}
	
	/**
	 * Message wrapper that adds a description at the end of the message when the return isn't null
	 */
	public static class DescriptionMessage extends MessageType
	{
		protected MessageType message;
		protected String unlocalizedMessage;
		protected String[] args;
		
		public DescriptionMessage(String message, String... args)
		{
			this(new SingleMessage(message, args), message + ".desc", args);
		}
		
		public DescriptionMessage(MessageType message, String desc, String... args)
		{
			this.message = message;
			this.unlocalizedMessage = desc;
			this.args = args;
		}
		
		@Override
		public String getString()
		{
			return unlocalizedMessage;
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			MutableComponent message = this.message.getMessage(consort, player, chainIdentifier);
			if(message == null)
				return null;
			
			message.append("\n");
			MutableComponent desc = createMessage(consort, player, unlocalizedMessage, args, false);
			desc.setStyle(desc.getStyle().withItalic(true).applyFormat(ChatFormatting.GRAY));
			message.append(desc);
			return message;
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier, String fromChain)
		{
			return message.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			message.debugAddAllMessages(list);
			//noinspection RedundantCast
			list.add(Component.translatable("consort." + unlocalizedMessage, (Object[]) args));
		}
	}

	/* Works like DescriptionMessage, except that it will NOT automatically include the prompt to a description.
	 *  An example of when this would be used is when a Consort is introduced with narration rather than a quote, 
	 *  like "This consort is silent and aloof. What do you do?"
	 */
	public static class DescriptiveMessage extends SingleMessage
	{
		public DescriptiveMessage(String message, String... args)
		{
			super(message, args);
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			MutableComponent desc = createMessage(consort, player, unlocalizedMessage, args, false);
			desc.setStyle(desc.getStyle().withItalic(true).applyFormat(ChatFormatting.GRAY));
			
			return desc;
		}
		
		@Override
		public String getString()
		{
			return unlocalizedMessage;
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
		protected MessageType[] messages;
		protected int repeatIndex;
		
		public ChainMessage(MessageType... messages)
		{
			this(0, messages);
		}
		
		public ChainMessage(int repeatIndex, MessageType... messages)
		{
			this(repeatIndex, messages[0].getString(), messages);
		}
		
		public ChainMessage(int repeatIndex, String name, MessageType... messages)
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
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			int index = nbt.getInt(this.getString());
			MessageType message = messages[index];
			index++;
			if(index >= messages.length)
				index = repeatIndex;
			
			MutableComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
			
			if(text != null)
				nbt.putInt(this.getString(), index);
			
			return text;
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			int index = 0;
			MessageType message = null;
			for(; index < messages.length; index++)
				if(messages[index].getString().equals(messageName))
				{
					message = messages[index];
					break;
				}
			
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			int prevIndex = nbt.getInt(this.getString());
			
			if(message == null && !(prevIndex + 1 == index || index == messages.length - 1 && prevIndex == repeatIndex))
				return null;
			
			MutableComponent text = message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
			if(text != null) //Only update if everything is correctly performed
				nbt.putInt(this.getString(), index);
			return text;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			for(MessageType message : messages)
				message.debugAddAllMessages(list);
		}
	}
	
	//This class functions like a chain message, except that it will select a single entry randomly each time, instead of looping.
	public static class RandomMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType[] messages;
		protected RandomKeepResult keepMethod;
		
		public RandomMessage(String name, RandomKeepResult keepMethod, MessageType... messages)
		{
			this.nbtName = name;
			this.keepMethod = keepMethod;
			this.messages = messages;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			CompoundTag nbt;
			if(keepMethod.equals(RandomKeepResult.KEEP_CONSORT))
				nbt = consort.getMessageTag();
			else nbt = consort.getMessageTagForPlayer(player);
			
			if(!keepMethod.equals(RandomKeepResult.SKIP))
				if(nbt.contains(this.getString()))
				{
					int i = nbt.getInt(this.getString());
					return messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
				}
			
			int i = consort.getRandom().nextInt(messages.length);
			
			if(!keepMethod.equals(RandomKeepResult.SKIP))
				nbt.putInt(this.getString(), i);
			
			return messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
		}

		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			for(MessageType message : messages)
				if(message.getString().equals(messageName))
					return message.getFromChain(consort, player, MessageType.addTo(chainIdentifier, messageName), fromChain);
			
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			for(MessageType message : messages)
				message.debugAddAllMessages(list);
		}
	}
	
	public enum RandomKeepResult
	{
		SKIP,
		KEEP_PLAYER,
		KEEP_CONSORT
	}
	
	public static class ChoiceMessage extends MessageType
	{
		protected boolean repeat;
		protected MessageType message;
		protected SingleMessage[] options;
		protected MessageType[] results;
		protected boolean acceptNull = false;
		
		public ChoiceMessage(MessageType message, SingleMessage[] options, MessageType[] results)
		{
			this(false, message, options, results);
		}
		
		public ChoiceMessage(boolean repeat, MessageType message, SingleMessage[] options, MessageType[] results)
		{
			if(options.length != results.length)
				throw new IllegalArgumentException("Option and result arrays must be of equal size!");
			this.repeat = repeat;
			this.message = message;
			this.options = options;
			this.results = results;
		}
		
		/**
		 * Has to be used when one of the options is a delay message. Will slightly mess with PurchaseMessage, ItemRequirement, or GiveItemMessage.
		 * If you need to do a combination of those two, ask Kirderf, as a certain addition to the system is needed. (Note to self, use exceptions instead of null for the three latter to distinguish their return from delay messages)
		 */
		public ChoiceMessage setAcceptNull()
		{
			acceptNull = true;
			return this;
		}
		
		@Override
		public String getString()
		{
			return message.getString();
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.contains(this.getString(), Tag.TAG_ANY_NUMERIC))
			{
				int index = nbt.getInt(this.getString());
				if(index >= 0 && index < options.length)
				{
					return results[index].getMessage(consort, player,
							addTo(chainIdentifier, results[index].getString()));
				} else
				{
					nbt.remove(this.getString());
					return this.getMessage(consort, player, chainIdentifier);
				}
			} else
			{
				MutableComponent question = message.getMessage(consort, player, chainIdentifier);
				if(question == null)
					return null;
				
				String commandStart = "/consortreply " + consort.getId() + " "
						+ (chainIdentifier.isEmpty() ? "" : chainIdentifier + ":");
				question.append("\n");
				for (SingleMessage optionText : options)
				{
					question.append("\n");
					MutableComponent option = Component.literal("> ");
					option.append(
							createMessage(consort, player, optionText.unlocalizedMessage, optionText.args, false));
					option.withStyle(style -> style.withClickEvent(
							new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandStart + optionText.getString())));
					option.withStyle(ChatFormatting.GRAY);
					question.append(option);
				}
				
				nbt.putString("currentMessage", this.getString());
				
				return question;
			}
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			
			MessageType message;
			if(nbt.getString("currentMessage").equals(this.getString()) && !nbt.contains(this.getString())
					&& fromChain.isEmpty())
				for(int index = 0; index < options.length; index++)
					if(options[index].getString().equals(messageName))
					{
						message = results[index];
						
						
						if(acceptNull) //Stuff has to be done before the message, as if it's null, it will send from the getMessage function
						{
							if(!repeat)
								nbt.putInt(this.getString(), index);
							
							Component innerMessage = createMessage(consort, player, options[index].unlocalizedMessage + ".reply", options[index].args, false);
							
							Component out = Component.translatable("chat.type.text", player.getDisplayName(), innerMessage);
							
							player.sendSystemMessage(out);
						}
						MutableComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
						
						if(text != null && !acceptNull)
						{
							if(!repeat)
								nbt.putInt(this.getString(), index);
							
							Component innerMessage = createMessage(consort, player, options[index].unlocalizedMessage + ".reply", options[index].args, false);
							
							Component out = Component.translatable("chat.type.text", player.getDisplayName(), innerMessage);
							
							player.sendSystemMessage(out);
						}
						
						return text;
					}
				
			for(int index = 0; index < results.length; index++)
				if(results[index].getString().equals(messageName))
				{
					message = results[index];
					
					if(!repeat && (!nbt.contains(this.getString(), Tag.TAG_ANY_NUMERIC) || nbt.getInt(this.getString()) != index))
						return null;
					
					return message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
				}
			
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			message.debugAddAllMessages(list);
			for(SingleMessage message : options)
			{
				message.debugAddAllMessages(list);
				//noinspection RedundantCast
				list.add(Component.translatable("consort." + message.unlocalizedMessage + ".reply", (Object[]) message.args));
			}
			for(MessageType message : results)
				message.debugAddAllMessages(list);
		}
	}
	
	/**
	 * Used with a list of messages to make those messages send separately, with a delay in between
	 * Note: Will always repeat right now, so pay attention to what is happening if you want to use nested messages
	 */
	public static class DelayMessage extends MessageType
	{
		protected String name;
		protected MessageType[] messages;
		protected int[] delay;
		
		/**
		 * Used to create a delay message. Name will default to that of the first message.
		 * @param delay A list of delays in tick-form. First entry is the time between the first and second message, and so on.
		 *                    Will loop back to the beginning of the array when going out of bounds, so the array do not need to be of full size if you want to use the same value constantly
		 * @param messages The messages that will be sent when triggered
		 */
		public DelayMessage(int[] delay, MessageType... messages)
		{
			this(messages[0].getString(), delay, messages);
		}
		
		/**
		 * Used to create a delay message.
		 * @param name Text used to identify the component and to be used as a key for nbt
		 * @param delay A list of delays in tick-form. First entry is the time between the first and second message, and so on.
		 *                    Will loop back to the beginning of the array when going out of bounds, so the array do not need to be of full size if you want to use the same value constantly
		 * @param messages The messages that will be sent when triggered
		 */
		public DelayMessage(String name, int[] delay, MessageType... messages)
		{
			this.name = name;
			this.delay = delay;
			this.messages = messages;
		}
		
		@Override
		public String getString()
		{
			return name;
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			consort.getMessageTag().putString(this.getString(), chainIdentifier);
			
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			
			if(!nbt.contains(this.getString()) || consort.updatingMessage != this)
			{
				nbt.putInt(this.getString(), consort.ticksUntilDialogueReset - delay[0]);
				nbt.putInt(this.getString()+".i", 0);
				consort.updatingMessage = this;
				Component text = messages[0].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[0].getString()));
				if(text != null)
					player.sendSystemMessage(text);
				
			} else if(nbt.getInt(this.getString()+".i") == messages.length - 1)
			{
				return messages[messages.length - 1].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[messages.length - 1].getString()));
			}
			
			return null;
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier, String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			for(MessageType message : messages)
				if(message.getString().equals(messageName))
					return message.getFromChain(consort, player, MessageType.addTo(chainIdentifier, messageName), fromChain);
			
			return null;
		}
		
		public void onTickUpdate(ConsortEntity consort)
		{
			CompoundTag messageTags = consort.getMessageTag();
			String chainIdentifier = messageTags.getString(this.getString());
			boolean update = false;
			for(String key : messageTags.getAllKeys())
			{
				CompoundTag nbt = messageTags.getCompound(key);
				if(!nbt.contains(this.getString()))
					continue;
				
				ServerPlayer player = consort.getServer().getPlayerList().getPlayer(UUID.fromString(key));
				if(player == null)
					nbt.remove(this.getString());
				else
				{
					int i = nbt.getInt(this.getString()+".i");
					int time = nbt.getInt(this.getString());
					if(time >= consort.ticksUntilDialogueReset)
					{
						i++;
						time -= delay[i % delay.length];
						nbt.putInt(this.getString(), time);
						nbt.putInt(this.getString() + ".i", i);
						
						if(i == messages.length - 1)
						{
							Component text = consort.message.getMessage(consort, player);
							if(text != null)
								player.sendSystemMessage(text);
							
							nbt.remove(this.getString());
							nbt.remove(this.getString() + ".i");
						} else
						{
							
							update = true;
							Component text = messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
							if(text != null)
								player.sendSystemMessage(text);
						}
					} else update = true;
				}
			}
			
			if(!update)
				consort.updatingMessage = null;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			for(MessageType message : messages)
				message.debugAddAllMessages(list);
		}
	}
	
	public static class ItemRequirement extends MessageType
	{
		protected String nbtName;
		protected boolean random, held, repeat;
		protected MessageType defaultMessage;
		protected MessageType conditionedMessage;
		protected TagKey<Item> itemTag;
		
		public ItemRequirement(TagKey<Item> itemTag, MessageType defaultMessage, MessageType nextMessage)
		{
			this(defaultMessage.getString(), itemTag, true, true, false, defaultMessage, nextMessage);
		}
		
		public ItemRequirement(TagKey<Item> itemTag, boolean random, boolean held, MessageType defaultMessage,
							   MessageType nextMessage)
		{
			this(defaultMessage.getString(), itemTag, random, held, false, defaultMessage, nextMessage);
		}
		
		/**
		 * Used to require the player to possess a certain item to proceed.
		 * 
		 * @param name
		 *            Name used used for nbt data
		 * @param itemTag
		 *            TagKey(an item tag) of items that are allowed
		 * @param random
		 *            If the item required should be picked at random or be
		 *            based on what the player has
		 * @param held
		 *            If the item has to be held by the player to count
		 * @param repeat
		 *            If the requirement will be rechecked every time this
		 *            message is reached
		 * @param defaultMessage
		 *            Message when requirement is not met
		 * @param nextMessage
		 *            Message when requirement is met
		 */
		public ItemRequirement(String name, TagKey<Item> itemTag, boolean random, boolean held, boolean repeat,
				MessageType defaultMessage, MessageType nextMessage)
		{
			this.nbtName = name;
			this.itemTag = itemTag;
			this.defaultMessage = defaultMessage;
			this.conditionedMessage = nextMessage;
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
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.contains(this.getString()))
				return conditionedMessage.getMessage(consort, player,
						addTo(chainIdentifier, conditionedMessage.getString()));
			
			boolean hasItem = false;
			List<ItemStack> stackListFromTag = MSTags.getItemStacksFromTag(itemTag);
			
			if(random || repeat && nbt.contains(this.getString()))
			{
				String nbtString;
				if(nbt.contains(this.getString()))
				{
					nbtString = nbt.getString(this.getString());
				}
				else
				{
					int index = consort.level().random.nextInt(stackListFromTag.size());
					ItemStack randomStack = stackListFromTag.get(index);
					nbtString = String.valueOf(ForgeRegistries.ITEMS.getKey(randomStack.getItem()));
					nbt.putString(this.getString(), nbtString);
				}
				
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbtString));
				if(item != null)
				{
					ItemStack stack = new ItemStack(item);
					nbt.put(this.getString() + ".item", stack.save(new CompoundTag()));
					
					hasItem = lookFor(stack, player);
				}
			} else
			{
				List<ItemStack> list = new ArrayList<>(stackListFromTag);
				while (!list.isEmpty())
				{
					ItemStack stack = list.remove(consort.level().random.nextInt(list.size()));
					if(lookFor(stack, player))
					{
						nbt.putString(this.getString(), String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())));
						nbt.put(this.getString() + ".item", stack.save(new CompoundTag()));
						hasItem = true;
						break;
					}
				}
			}
			
			if(hasItem)
			{
				return conditionedMessage.getMessage(consort, player, addTo(chainIdentifier, conditionedMessage.getString()));
			}
			
			player.sendSystemMessage(defaultMessage.getMessage(consort, player, addTo(chainIdentifier, defaultMessage.getString())));
			return null;
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			if(messageName.equals(conditionedMessage.getString()))
				return conditionedMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			else if(messageName.equals(defaultMessage.getString()))
				return !repeat && nbt.contains(this.getString()) ? null
						: defaultMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			return null;
		}
		
		private boolean lookFor(ItemStack stack, ServerPlayer player)
		{
			for(ItemStack held : player.getHandSlots()) //prioritizes items in hands before items from the rest of the inventory
			{
				if(ItemStack.isSameItem(held, stack))
				{
					return true;
				}
			}
			
			if(!held)
			{
				for(ItemStack held : player.getInventory().items)
				{
					if(ItemStack.isSameItem(held, stack))
					{
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			conditionedMessage.debugAddAllMessages(list);
			defaultMessage.debugAddAllMessages(list);
		}
	}
	
	public static class GiveItemMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType next;
		protected String itemData;
		protected int boondollars;
		protected int rep;
		
		public GiveItemMessage(String itemData, int boondollars, MessageType next)
		{
			this(next.getString(), itemData, boondollars, 0, next);
		}
		
		public GiveItemMessage(String nbtName, String itemData, int boondollars, int rep, MessageType next)
		{
			this.nbtName = nbtName;
			this.itemData = itemData;
			this.boondollars = boondollars;
			this.rep = rep;
			this.next = next;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			CompoundTag nbt = consort.getMessageTagForPlayer(player);
			if(nbt.getBoolean(this.getString()))
				return next.getMessage(consort, player, chainIdentifier);
			
			ItemStack stack = ItemStack.of(nbt.getCompound(itemData));
			
			boolean foundItem = false;
			for(InteractionHand hand : InteractionHand.values())
			{
				ItemStack heldItem = player.getItemInHand(hand);
				if(ItemStack.isSameItem(heldItem, stack))
				{
					foundItem = true;
					heldItem.shrink(1);
					break;
				}
			}
			
			for(ItemStack invItem : player.getInventory().items)
			{
				if(ItemStack.isSameItem(invItem, stack))
				{
					foundItem = true;
					invItem.shrink(1);
					break;
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
				return next.getMessage(consort, player, chainIdentifier);
			} else
			{
				player.sendSystemMessage(
						createMessage(consort, player, MISSING_ITEM, new String[] { "nbtItem:" + itemData }, false).withStyle(ChatFormatting.RED));
				return null;
			}
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier,
										   String fromChain)
		{
			return next.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			next.debugAddAllMessages(list);
		}
	}
	
	public static class MerchantGuiMessage extends MessageType
	{
		protected String nbtName;
		protected MessageType initMessage;
		protected ResourceLocation lootTable;
		
		public MerchantGuiMessage(MessageType message, ResourceLocation location)
		{
			this(message.getString(), message, location);
		}
		
		public MerchantGuiMessage(String name, MessageType message, ResourceLocation location)
		{
			nbtName = name;
			initMessage = message;
			lootTable = location;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public MutableComponent getMessage(ConsortEntity consort, ServerPlayer player, String chainIdentifier)
		{
			if(consort.stocks == null)
			{
				consort.stocks = new ConsortMerchantInventory(consort, ConsortRewardHandler.generateStock(lootTable, consort, consort.level().random));
			}
			
			NetworkHooks.openScreen(player, new SimpleMenuProvider(consort, Component.literal("Consort shop")), consort::writeShopMenuBuffer);
			
			return initMessage.getMessage(consort, player, chainIdentifier);
		}
		
		@Override
		public MutableComponent getFromChain(ConsortEntity consort, ServerPlayer player, String chainIdentifier, String fromChain)
		{
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<Component> list)
		{
			initMessage.debugAddAllMessages(list);
		}
	}
	
}