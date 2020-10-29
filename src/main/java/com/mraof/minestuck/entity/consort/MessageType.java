package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.advancements.MSCriteriaTriggers;
import com.mraof.minestuck.inventory.ConsortMerchantInventory;
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
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Class where message content is defined. Also things such as if it's a chain
 * message or perhaps a message one could reply to
 * There is plenty of room for improvement and to make things more robust
 * @author Kirderf1
 */
public abstract class MessageType
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String MISSING_ITEM = "consort.missing_item";
	
	public abstract String getString();
	
	public abstract ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier);
	
	public abstract ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
												String fromChain);
	
	protected abstract void debugAddAllMessages(List<ITextComponent> list);
	
	private static ITextComponent createMessage(ConsortEntity consort, ServerPlayerEntity player, String unlocalizedMessage,
												String[] args, boolean consortPrefix)
	{
		String s = consort.getType().getTranslationKey();
		
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
				LandInfo landInfo = MSDimensions.getLandInfo(consort.getServer(), consort.homeDimension);
				if(landInfo != null)
				{
					obj[i] = landInfo.landAsTextComponent();
				} else
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
				obj[i] = new TranslationTextComponent(s + ".sound");
			} else if(args[i].equals("consort_sound_2"))
			{
				obj[i] = new TranslationTextComponent(s + ".sound.2");
			} else if(args[i].equals("consort_type"))
			{
				obj[i] = new TranslationTextComponent(s);
			} else if(args[i].equals("consort_types"))
			{
				obj[i] = new TranslationTextComponent(s + ".plural");
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
					obj[i] = new TranslationTextComponent("denizen." + worldTitle.getHeroAspect().getTranslationKey());
				else
					obj[i] = "Denizen";
			} else if(args[i].startsWith("nbt_item:"))
			{
				CompoundNBT nbt = consort.getMessageTagForPlayer(player);
				ItemStack stack = ItemStack.read(nbt.getCompound(args[i].substring(9)));
				if(!stack.isEmpty())
					obj[i] = new TranslationTextComponent(stack.getTranslationKey());
				else obj[i] = "Item";
			}
		}
		
		TranslationTextComponent message = new TranslationTextComponent("consort." + unlocalizedMessage, obj);
		if(consortPrefix)
		{
			message.getStyle().setColor(consort.getConsortType().getColor());
			TranslationTextComponent entity = new TranslationTextComponent(s);
			
			return new TranslationTextComponent("chat.type.text", entity, message);
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
		public ITextComponent getMessageForTesting(ConsortEntity consort, ServerPlayerEntity player)
		{
			return createMessage(consort, player, unlocalizedMessage, args, true);
		}
		
		@Override
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			consort.getMessageTagForPlayer(player).putString("currentMessage", this.getString());
			return createMessage(consort, player, unlocalizedMessage, args, true);
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
										   String fromChain)
		{
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			//noinspection RedundantCast
			list.add(new TranslationTextComponent("consort." + unlocalizedMessage, (Object[]) args));
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player,
										 String chainIdentifier)
		{
			if(firstOnce)
			{
				CompoundNBT nbt = consort.getMessageTagForPlayer(player);
				if(!nbt.contains(this.getString()) || !nbt.getBoolean(this.getString()))
					nbt.putBoolean(this.getString(), true);
				else return messageTwo.getMessage(consort, player, chainIdentifier);
			}
			
			ITextComponent message = messageOne.getMessage(consort, player, chainIdentifier);
			message.appendText("\n");
			message.appendSibling(messageTwo.getMessage(consort, player, chainIdentifier));
			return message;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player,
										   String chainIdentifier, String fromChain)
		{
			ITextComponent message = messageTwo.getFromChain(consort, player, chainIdentifier, fromChain);
			return message;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			ITextComponent message = this.message.getMessage(consort, player, chainIdentifier);
			if(message == null)
				return null;
			
			message.appendText("\n");
			ITextComponent desc = createMessage(consort, player, unlocalizedMessage, args, false);
			desc.getStyle().setItalic(true).setColor(TextFormatting.GRAY);
			message.appendSibling(desc);
			return message;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier, String fromChain)
		{
			return message.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			message.debugAddAllMessages(list);
			//noinspection RedundantCast
			list.add(new TranslationTextComponent("consort." + unlocalizedMessage, (Object[]) args));
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			ITextComponent desc = createMessage(consort, player, unlocalizedMessage, args, false);
			desc.getStyle().setItalic(true).setColor(TextFormatting.GRAY);
			
			ITextComponent message = new StringTextComponent("");
			message.appendSibling(desc);
			return message;
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			int index = nbt.getInt(this.getString());
			MessageType message = messages[index];
			index++;
			if(index >= messages.length)
				index = repeatIndex;
			
			ITextComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
			
			if(text != null)
				nbt.putInt(this.getString(), index);
			
			return text;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
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
			
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			int prevIndex = nbt.getInt(this.getString());
			
			if(message == null && !(prevIndex + 1 == index || index == messages.length - 1 && prevIndex == repeatIndex))
				return null;
			
			ITextComponent text = message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
			if(text != null) //Only update if everything is correctly performed
				nbt.putInt(this.getString(), index);
			return text;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			for(MessageType message : messages)
				message.debugAddAllMessages(list);
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			if (condition.testFor(consort, player))
				return message1.getMessage(consort, player, chainIdentifier);
			else return message2.getMessage(consort, player, chainIdentifier);
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier, String fromChain)
		{
			if (condition.testFor(consort, player))
				return message1.getFromChain(consort, player, chainIdentifier, fromChain);
			else return message2.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			message1.debugAddAllMessages(list);
			message2.debugAddAllMessages(list);
		}
		
		public interface Condition
		{
			boolean testFor(ConsortEntity consort, ServerPlayerEntity player);
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt;
			if(keepMethod.equals(RandomKeepResult.KEEP_CONSORT))
				nbt = consort.getMessageTag();
			else nbt = consort.getMessageTagForPlayer(player);
			
			if(!keepMethod.equals(RandomKeepResult.SKIP))
				if(nbt.contains(this.getString()))
				{
					int i = nbt.getInt(this.getString());
					return messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
				}
			
			int i = consort.getRNG().nextInt(messages.length);
			
			if(!keepMethod.equals(RandomKeepResult.SKIP))
				nbt.putInt(this.getString(), i);
			
			return messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
		}

		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
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
		protected void debugAddAllMessages(List<ITextComponent> list)
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.contains(this.getString(), Constants.NBT.TAG_ANY_NUMERIC))
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
				ITextComponent question = message.getMessage(consort, player, chainIdentifier);
				if(question == null)
					return null;
				
				String commandStart = "/consortreply " + consort.getEntityId() + " "
						+ (chainIdentifier.isEmpty() ? "" : chainIdentifier + ":");
				question.appendText("\n");
				for(int i = 0; i < options.length; i++)
				{
					question.appendText("\n");
					ITextComponent option = new StringTextComponent(">");
					option.appendSibling(
							createMessage(consort, player, options[i].unlocalizedMessage, options[i].args, false));
					option.getStyle().setClickEvent(
							new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandStart + options[i].getString()));
					option.getStyle().setColor(TextFormatting.GRAY);
					question.appendSibling(option);
				}
				
				nbt.putString("currentMessage", this.getString());
				
				return question;
			}
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			
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
							
							ITextComponent innerMessage = createMessage(consort, player, options[index].unlocalizedMessage + ".reply", options[index].args, false);
							
							ITextComponent out = new TranslationTextComponent("chat.type.text", player.getDisplayName(), innerMessage);
							
							player.sendMessage(out);
						}
						ITextComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
						
						if(text != null && !acceptNull)
						{
							if(!repeat)
								nbt.putInt(this.getString(), index);
							
							ITextComponent innerMessage = createMessage(consort, player, options[index].unlocalizedMessage + ".reply", options[index].args, false);
							
							ITextComponent out = new TranslationTextComponent("chat.type.text", player.getDisplayName(), innerMessage);
							
							player.sendMessage(out);
						}
						
						return text;
					}
				
			for(int index = 0; index < results.length; index++)
				if(results[index].getString().equals(messageName))
				{
					message = results[index];
					
					if(!repeat && (!nbt.contains(this.getString(), Constants.NBT.TAG_ANY_NUMERIC) || nbt.getInt(this.getString()) != index))
						return null;
					
					return message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
				}
			
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			message.debugAddAllMessages(list);
			for(SingleMessage message : options)
			{
				message.debugAddAllMessages(list);
				//noinspection RedundantCast
				list.add(new TranslationTextComponent("consort." + message.unlocalizedMessage + ".reply", (Object[]) message.args));
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			consort.getMessageTag().putString(this.getString(), chainIdentifier);
			
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			
			if(!nbt.contains(this.getString()) || consort.updatingMessage != this)
			{
				nbt.putInt(this.getString(), consort.messageTicksLeft - delay[0]);
				nbt.putInt(this.getString()+".i", 0);
				consort.updatingMessage = this;
				ITextComponent text = messages[0].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[0].getString()));
				if(text != null)
					player.sendMessage(text);
				
			} else if(nbt.getInt(this.getString()+".i") == messages.length - 1)
			{
				return messages[messages.length - 1].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[messages.length - 1].getString()));
			}
			
			return null;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier, String fromChain)
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
			CompoundNBT messageTags = consort.getMessageTag();
			String chainIdentifier = messageTags.getString(this.getString());
			boolean update = false;
			for(String key : messageTags.keySet())
			{
				CompoundNBT nbt = messageTags.getCompound(key);
				if(!nbt.contains(this.getString()))
					continue;
				
				ServerPlayerEntity player = consort.getServer().getPlayerList().getPlayerByUUID(UUID.fromString(key));
				if(player == null)
					nbt.remove(this.getString());
				else
				{
					int i = nbt.getInt(this.getString()+".i");
					int time = nbt.getInt(this.getString());
					if(time >= consort.messageTicksLeft)
					{
						i++;
						time -= delay[i % delay.length];
						nbt.putInt(this.getString(), time);
						nbt.putInt(this.getString() + ".i", i);
						
						if(i == messages.length - 1)
						{
							ITextComponent text = consort.message.getMessage(consort, player);
							if(text != null)
								player.sendMessage(text);
							
							nbt.remove(this.getString());
							nbt.remove(this.getString() + ".i");
						} else
						{
							
							update = true;
							ITextComponent text = messages[i].getMessage(consort, player, MessageType.addTo(chainIdentifier, messages[i].getString()));
							if(text != null)
								player.sendMessage(text);
						}
					} else update = true;
				}
			}
			
			if(!update)
				consort.updatingMessage = null;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			for(MessageType message : messages)
				message.debugAddAllMessages(list);
		}
	}
	
	public static class PurchaseMessage extends MessageType
	{
		protected String nbtName;
		protected boolean repeat;
		protected ResourceLocation lootTableId;
		protected int cost;
		protected int rep;
		protected MessageType message;
		
		public PurchaseMessage(ResourceLocation lootTableId, int cost, MessageType message)
		{
			this(false, lootTableId, cost, 0, message.getString(), message);
		}
		/**
		 * Make sure to use this constructor with a unique name, if the message
		 * is of a type that uses it's own stored data
		 */
		public PurchaseMessage(boolean repeat, ResourceLocation lootTableId, int cost, int rep, String name, MessageType message)
		{
			this.nbtName = name;
			this.repeat = repeat;
			this.lootTableId = lootTableId;
			this.cost = cost;
			this.message = message;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
										   String fromChain)
		{
			return message.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			PlayerData data = PlayerSavedData.getData(player);
			if(!repeat && nbt.getBoolean(nbtName))
				return message.getMessage(consort, player, chainIdentifier);
			
			if(data.tryTakeBoondollars(cost))
			{
				if(!repeat)
					nbt.putBoolean(nbtName, true);
				
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
					data.addConsortReputation(rep);
				
				return message.getMessage(consort, player, chainIdentifier);
			} else
			{
				player.sendMessage(createMessage(consort, player, "cant_afford", new String[0], false));
				
				return null;
			}
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			message.debugAddAllMessages(list);
		}
	}
	
	public static class ItemRequirement extends MessageType
	{
		protected String nbtName;
		protected boolean random, held, repeat;
		protected MessageType defaultMessage;
		protected MessageType conditionedMessage;
		protected List<ItemStack> possibleItems;
		
		public ItemRequirement(List<ItemStack> list, MessageType defaultMessage, MessageType nextMessage)
		{
			this(defaultMessage.getString(), list, true, true, false, defaultMessage, nextMessage);
		}
		
		public ItemRequirement(List<ItemStack> list, boolean random, boolean held, MessageType defaultMessage,
				MessageType nextMessage)
		{
			this(defaultMessage.getString(), list, random, held, false, defaultMessage, nextMessage);
		}
		
		/**
		 * Used to require the player to possess a certain item to proceed.
		 * 
		 * @param name
		 *            Name used used for nbt data
		 * @param list
		 *            List of potential item requirements
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
		public ItemRequirement(String name, List<ItemStack> list, boolean random, boolean held, boolean repeat,
				MessageType defaultMessage, MessageType nextMessage)
		{
			this.nbtName = name;
			this.possibleItems = list;
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.contains(this.getString()))
				return conditionedMessage.getMessage(consort, player,
						addTo(chainIdentifier, conditionedMessage.getString()));
			
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
				while (!list.isEmpty())
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
				return conditionedMessage.getMessage(consort, player, addTo(chainIdentifier, conditionedMessage.getString()));
			}
			
			player.sendMessage(defaultMessage.getMessage(consort, player, addTo(chainIdentifier, defaultMessage.getString())));
			return null;
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
										   String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(messageName.equals(conditionedMessage.getString()))
				return conditionedMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			else if(messageName.equals(defaultMessage.getString()))
				return !repeat && nbt.contains(this.getString()) ? null
						: defaultMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			return null;
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
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			CompoundNBT nbt = consort.getMessageTagForPlayer(player);
			if(nbt.getBoolean(this.getString()))
				return next.getMessage(consort, player, chainIdentifier);
			
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
			
			for(ItemStack invItem : player.inventory.mainInventory)
			{
				if(ItemStack.areItemsEqual(invItem, stack))
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
					PlayerSavedData.getData(player).addConsortReputation(rep);
				
				nbt.putBoolean(this.getString(), true);
				return next.getMessage(consort, player, chainIdentifier);
			} else
			{
				player.sendMessage(
						createMessage(consort, player, MISSING_ITEM, new String[] { "nbtItem:" + itemData }, false).setStyle(new Style().setColor(TextFormatting.RED)));
				return null;
			}
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier,
										   String fromChain)
		{
			return next.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
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
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			if(consort.stocks == null)
			{
				consort.stocks = new ConsortMerchantInventory(consort, ConsortRewardHandler.generateStock(lootTable, consort, consort.world.rand));
			}
			
			NetworkHooks.openGui(player, new SimpleNamedContainerProvider(consort, new StringTextComponent("Consort shop")), consort::writeShopContainerBuffer);
			
			return initMessage.getMessage(consort, player, chainIdentifier);
		}
		
		@Override
		public ITextComponent getFromChain(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier, String fromChain)
		{
			return null;
		}
		
		@Override
		protected void debugAddAllMessages(List<ITextComponent> list)
		{
			initMessage.debugAddAllMessages(list);
		}
	}
	
	public static class ExplosionMessage extends SingleMessage
	{
		public ExplosionMessage(String message, String... args)
		{
			super(message, args);
		}
		
		@Override
		public ITextComponent getMessage(ConsortEntity consort, ServerPlayerEntity player, String chainIdentifier)
		{
			consort.setExplosionTimer();
			return super.getMessage(consort, player, chainIdentifier);
		}
	}
}