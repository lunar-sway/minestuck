package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.IdentifierHandler;
import com.mraof.minestuck.util.IdentifierHandler.PlayerIdentifier;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.MinestuckPlayerData.PlayerData;
import com.mraof.minestuck.util.Title;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class where message content is defined. Also things such as if it's a chain
 * message or perhaps a message one could reply to
 * 
 * @author Kirderf1
 */
public abstract class MessageType
{
	
	public abstract String getString();
	
	public abstract ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier);
	
	public abstract ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
			String fromChain);
	
	private static ITextComponent createMessage(EntityConsort consort, EntityPlayer player, String unlocalizedMessage,
			String[] args, boolean consortPrefix)
	{
		String s = EntityList.getEntityString(consort);
		if(s == null)
		{
			s = "generic";
		}
		
		Object[] obj = new Object[args.length];
		SburbConnection c = SburbHandler.getConnectionForDimension(consort.homeDimension);
		Title title = c == null ? null : MinestuckPlayerData.getData(c.getClientIdentifier()).title;
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("playerNameLand"))
			{
				if(c != null)
					obj[i] = c.getClientIdentifier().getUsername();
				else
					obj[i] = "Player name";
			} else if(args[i].equals("playerName"))
			{
				obj[i] = player.getName();
			} else if(args[i].equals("landName"))
			{
				World world = consort.getServer().worldServerForDimension(consort.homeDimension);
				if(world != null && consort.world.provider instanceof WorldProviderLands)
				{
					ChunkProviderLands chunkProvider = (ChunkProviderLands) world.provider.createChunkGenerator();
					ITextComponent aspect1 = new TextComponentTranslation(
							"land." + chunkProvider.aspect1.getNames()[chunkProvider.nameIndex1]);
					ITextComponent aspect2 = new TextComponentTranslation(
							"land." + chunkProvider.aspect2.getNames()[chunkProvider.nameIndex2]);
					if(chunkProvider.nameOrder)
						obj[i] = new TextComponentTranslation("land.format", aspect1, aspect2);
					else
						obj[i] = new TextComponentTranslation("land.format", aspect2, aspect1);
				} else
					obj[i] = "Land name";
			} else if(args[i].equals("playerTitleLand"))
			{
				if(title != null)
					obj[i] = title.asTextComponent();
				else
					obj[i] = "Player title";
			} else if(args[i].equals("playerClassLand"))
			{
				if(title != null)
					obj[i] = title.getHeroClass().asTextComponent();
				else
					obj[i] = "Player class";
			} else if(args[i].equals("playerAspectLand"))
			{
				if(title != null)
					obj[i] = title.getHeroAspect().asTextComponent();
				else
					obj[i] = "Player aspect";
			} else if(args[i].equals("consortSound"))
			{
				obj[i] = new TextComponentTranslation("consort.sound." + s);
			} else if(args[i].equals("consortType"))
			{
				obj[i] = new TextComponentTranslation("entity." + s + ".name");
			} else if(args[i].equals("consortTypes"))
			{
				obj[i] = new TextComponentTranslation("entity." + s + ".plural.name");
			} else if(args[i].equals("playerTitle"))
			{
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				if(MinestuckPlayerData.getTitle(identifier) != null)
					obj[i] = MinestuckPlayerData.getTitle(identifier).asTextComponent();
				else
					obj[i] = player.getName();
			} else if(args[i].equals("denizen"))
			{
				if(title != null)
					obj[i] = new TextComponentTranslation("denizen."
							+ MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroAspect().toString()
							+ ".name");
				else
					obj[i] = "Denizen";
			} else if(args[i].startsWith("nbtItem:"))
			{
				NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
				ItemStack stack = new ItemStack(nbt.getCompoundTag(args[i].substring(8)));
				if(stack != null)
					obj[i] = new TextComponentTranslation(stack.getUnlocalizedName() + ".name");
				else obj[i] = "Item";
			}
		}
		
		TextComponentTranslation message = new TextComponentTranslation("consort." + unlocalizedMessage, obj);
		if(consortPrefix)
		{
			message.getStyle().setColor(consort.getConsortType().getColor());
			TextComponentTranslation entity = new TextComponentTranslation("entity." + s + ".name");
			
			return new TextComponentTranslation("chat.type.text", entity, message);
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
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			consort.getMessageTagForPlayer(player).setString("currentMessage", this.getString());
			return createMessage(consort, player, unlocalizedMessage, args, true);
		}
		
		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			return null;
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
		protected String[] args;
		
		public DoubleMessage(MessageType messageOne, MessageType messageTwo)
		{
			this.messageOne = messageOne;
			this.messageTwo = messageTwo;
		}

		@Override
		public String getString() {
			return addTo(messageOne.getString(), messageTwo.getString());
		}

		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player,
				String chainIdentifier) {
			ITextComponent message = messageOne.getMessage(consort, player, chainIdentifier);
			message.appendText("\n");
			message.appendSibling(messageTwo.getMessage(consort, player, chainIdentifier));
			return message;
		}

		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player,
				String chainIdentifier, String fromChain)
		{
			ITextComponent message = messageTwo.getFromChain(consort, player, chainIdentifier, fromChain);
			return message;
		}
	}
	
	public static class DescriptionMessage extends SingleMessage
	{
		
		public DescriptionMessage(String message, String... args)
		{
			super(message, args);
		}
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			ITextComponent message = super.getMessage(consort, player, chainIdentifier);
			message.appendText("\n");
			ITextComponent desc = createMessage(consort, player, unlocalizedMessage + ".desc", args, false);
			desc.getStyle().setItalic(true).setColor(TextFormatting.GRAY);
			message.appendSibling(desc);
			return message;
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
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			ITextComponent desc = createMessage(consort, player, unlocalizedMessage + ".desc", args, false);
			desc.getStyle().setItalic(true).setColor(TextFormatting.GRAY);
			
			ITextComponent message = new TextComponentString("");
			message.appendSibling(desc);
			return message;
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
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			int index = nbt.getInteger(this.getString());
			MessageType message = messages[index];
			index++;
			if(index >= messages.length)
				index = repeatIndex;
			
			ITextComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
			
			if(text != null)
				nbt.setInteger(this.getString(), index);
			
			return text;
		}
		
		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
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
			
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			int prevIndex = nbt.getInteger(this.getString());
			
			if(message == null && !(prevIndex + 1 == index || index == messages.length - 1 && prevIndex == repeatIndex))
				return null;
			
			ITextComponent text = message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
			if(text != null) //Only update if everything is correctly performed
				nbt.setInteger(this.getString(), index);
			return text;
		}
	}
	
	//This class functions like a chain message, except that it will select a single entry randomly each time, instead of looping.
	public static class RandomMessage extends ChainMessage
	{
		Random random = new Random();
		
		public RandomMessage(MessageType... messages)
		{
			super(messages);
		}
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			nbt.setInteger(this.getString(), random.nextInt(messages.length));
			
			ITextComponent out = super.getMessage(consort, player, chainIdentifier);
			return out;
		}

		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			ITextComponent out = super.getFromChain(consort, player, chainIdentifier, fromChain);
			return out;
		}
	}
	
	public static class ChoiceMessage extends MessageType
	{
		protected boolean repeat;
		protected SingleMessage message;
		protected SingleMessage[] options;
		protected MessageType[] results;
		
		public ChoiceMessage(SingleMessage message, SingleMessage[] options, MessageType[] results)
		{
			this(false, message, options, results);
		}
		
		public ChoiceMessage(boolean repeat, SingleMessage message, SingleMessage[] options, MessageType[] results)
		{
			if(options.length != results.length)
				throw new IllegalArgumentException("Option and result arrays must be of equal size!");
			this.repeat = repeat;
			this.message = message;
			this.options = options;
			this.results = results;
		}
		
		@Override
		public String getString()
		{
			return message.getString();
		}
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.hasKey(this.getString(), 99))
			{
				int index = nbt.getInteger(this.getString());
				if(index >= 0 && index < options.length)
				{
					return results[index].getMessage(consort, player,
							addTo(chainIdentifier, results[index].getString()));
				} else
				{
					nbt.removeTag(this.getString());
					return this.getMessage(consort, player, chainIdentifier);
				}
			} else
			{
				ITextComponent question = message.getMessage(consort, player, chainIdentifier);
				String commandStart = "/consortReply " + consort.getEntityId() + " "
						+ (chainIdentifier.isEmpty() ? "" : chainIdentifier + ":");
				question.appendText("\n");
				for(int i = 0; i < options.length; i++)
				{
					question.appendText("\n");
					ITextComponent option = new TextComponentString(">");
					option.appendSibling(
							createMessage(consort, player, options[i].unlocalizedMessage, options[i].args, false));
					option.getStyle().setClickEvent(
							new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandStart + options[i].getString()));
					question.appendSibling(option);
				}
				
				nbt.setString("currentMessage", this.getString());
				
				return question;
			}
		}
		
		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			
			MessageType message = null;
			if(nbt.getString("currentMessage").equals(this.getString()) && !nbt.hasKey(this.getString())
					&& fromChain.isEmpty())
				for(int index = 0; index < options.length; index++)
					if(options[index].getString().equals(messageName))
					{
						message = results[index];
						
						ITextComponent text = message.getMessage(consort, player, addTo(chainIdentifier, message.getString()));
						
						if(text != null)
						{
							if(!repeat)
								nbt.setInteger(this.getString(), index);
							
							player.sendMessage(new TextComponentTranslation("chat.type.text", player.getDisplayName(),
									createMessage(consort, player, options[index].unlocalizedMessage + ".reply",
											options[index].args, false)));
						}
						
						return text;
					}
				
			for(int index = 0; index < results.length; index++)
				if(results[index].getString().equals(messageName))
				{
					message = results[index];
					
					if(!repeat && (!nbt.hasKey(this.getString(), 99) || nbt.getInteger(this.getString()) != index))
						return null;
					
					return message.getFromChain(consort, player, addTo(chainIdentifier, message.getString()), fromChain);
				}
			
			return null;
		}
		
	}
	
	public static class PurchaseMessage extends MessageType
	{
		protected String nbtName;
		protected boolean repeat;
		protected ResourceLocation item;
		protected int cost;
		protected MessageType message;
		
		public PurchaseMessage(ResourceLocation item, int cost, MessageType message)
		{
			this(false, item, cost, message.getString(), message);
		}
		
		/**
		 * Make sure to use this constructor with a unique name, if the message
		 * is of a type that uses it's own stored data
		 */
		public PurchaseMessage(boolean repeat, ResourceLocation item, int cost, String name, MessageType message)
		{
			this.nbtName = name;
			this.repeat = repeat;
			this.item = item;
			this.cost = cost;
			this.message = message;
		}
		
		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			return message.getFromChain(consort, player, chainIdentifier, fromChain);
		}
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			
			if(!repeat && nbt.getBoolean(nbtName))
				return message.getMessage(consort, player, chainIdentifier);
			
			PlayerData data = MinestuckPlayerData.getData(player);
			if(data.boondollars < cost)
			{
				player.sendMessage(createMessage(consort, player, "cantAfford", new String[0], false));
				
				return null;
			} else
			{
				data.boondollars -= cost;
				if(!repeat)
					nbt.setBoolean(nbtName, true);
				
				LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) consort.world);
				for(ItemStack itemstack : consort.world.getLootTableManager().getLootTableFromLocation(item)
						.generateLootForPools(consort.world.rand, contextBuilder.build()))
				{
					player.entityDropItem(itemstack, 0.0F);
				}
				
				MinestuckChannelHandler.sendToPlayer(
						MinestuckPacket.makePacket(Type.PLAYER_DATA, PlayerDataPacket.BOONDOLLAR, data.boondollars),
						player);
				
				return message.getMessage(consort, player, chainIdentifier);
			}
		}
		
		@Override
		public String getString()
		{
			return nbtName;
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
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			if(!repeat && nbt.hasKey(this.getString()))
				return conditionedMessage.getMessage(consort, player,
						addTo(chainIdentifier, conditionedMessage.getString()));
			
			boolean hasItem = false;
			if(random || repeat && nbt.hasKey(this.getString()))
			{
				int index;
				if(nbt.hasKey(this.getString()))
					index = nbt.getInteger(this.getString());
				else
				{
					index = consort.world.rand.nextInt(possibleItems.size());
					nbt.setInteger(this.getString(), index);
				}
				
				ItemStack stack = possibleItems.get(index);
				nbt.setTag(this.getString() + ".item", stack.writeToNBT(new NBTTagCompound()));
				
				hasItem = lookFor(stack, player);
			} else
			{
				List<ItemStack> list = new ArrayList<ItemStack>(possibleItems);
				while (!list.isEmpty())
				{
					ItemStack stack = list.remove(consort.world.rand.nextInt(list.size()));
					if(lookFor(stack, player))
					{
						nbt.setInteger(this.getString(), possibleItems.indexOf(stack));
						nbt.setTag(this.getString() + ".item", stack.writeToNBT(new NBTTagCompound()));
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
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			if(fromChain.isEmpty())
				return null;
			int i = fromChain.indexOf(':');
			if(i == -1)
				i = fromChain.length();
			String messageName = fromChain.substring(0, i);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i + 1);
			
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			if(messageName.equals(conditionedMessage.getString()))
				return conditionedMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			else if(messageName.equals(defaultMessage.getString()))
				return !repeat && nbt.hasKey(this.getString()) ? null
						: defaultMessage.getFromChain(consort, player, addTo(chainIdentifier, conditionedMessage.getString()), fromChain);
			return null;
		}
		
		private boolean lookFor(ItemStack stack, EntityPlayer player)
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
		protected MessageType next;
		protected String itemData;
		protected int boondollars;
		
		public GiveItemMessage(String itemData, int boondollars, MessageType next)
		{
			this(next.getString(), itemData, boondollars, next);
		}
		
		public GiveItemMessage(String nbtName, String itemData, int boondollars, MessageType next)
		{
			this.nbtName = nbtName;
			this.itemData = itemData;
			this.boondollars = boondollars;
			this.next = next;
		}
		
		@Override
		public String getString()
		{
			return nbtName;
		}
		
		@Override
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String chainIdentifier)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			if(nbt.getBoolean(this.getString()))
				return next.getMessage(consort, player, chainIdentifier);
			
			ItemStack stack = new ItemStack(nbt.getCompoundTag(itemData));
			
			boolean foundItem = false;
			for(EnumHand hand : EnumHand.values())
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
					MinestuckPlayerData.getData(player).boondollars += boondollars;
					MinestuckChannelHandler.sendToPlayer(MinestuckPacket.makePacket(Type.PLAYER_DATA,
							PlayerDataPacket.BOONDOLLAR, MinestuckPlayerData.getData(player).boondollars), player);
				}
				nbt.setBoolean(this.getString(), true);
				return next.getMessage(consort, player, chainIdentifier);
			} else
			{
				player.sendMessage(
						createMessage(consort, player, "missingItem", new String[] { "nbtItem:" + itemData }, false).setStyle(new Style().setColor(TextFormatting.RED)));
				return null;
			}
		}
		
		@Override
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String chainIdentifier,
				String fromChain)
		{
			return next.getFromChain(consort, player, chainIdentifier, fromChain);
		}
	}
}