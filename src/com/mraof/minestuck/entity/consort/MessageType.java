package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.network.MinestuckChannelHandler;
import com.mraof.minestuck.network.MinestuckPacket;
import com.mraof.minestuck.network.PlayerDataPacket;
import com.mraof.minestuck.network.MinestuckPacket.Type;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

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
					ChunkProviderLands chunkProvider = (ChunkProviderLands) world.provider
							.createChunkGenerator();
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
					obj[i] = new TextComponentTranslation("denizen."+MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroAspect().toString()+".name");
				else obj[i] = "Denizen";
			}
		}
		
		TextComponentTranslation message = new TextComponentTranslation("consort." + unlocalizedMessage, obj);
		if(consortPrefix)
		{
			TextComponentTranslation entity = new TextComponentTranslation("entity." + s + ".name");
			entity.appendText(": ");
			entity.appendSibling(message);
			
			return entity;
		} else
			return message;
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
	
	public static class ChainMessage extends MessageType
	{
		protected String name;
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
			this.name = name;
		}
		
		@Override
		public String getString()
		{
			return name;
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
			
			if(!chainIdentifier.isEmpty())
				chainIdentifier += ':';
			chainIdentifier += message.getString();
			
			ITextComponent text = message.getMessage(consort, player, chainIdentifier);
			
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
			String messageName = fromChain.substring(0, i + 1);
			fromChain = i == fromChain.length() ? "" : fromChain.substring(i);
			
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
			
			if(!chainIdentifier.isEmpty())
				chainIdentifier += ':';
			chainIdentifier += message.getString();
			
			ITextComponent text = message.getFromChain(consort, player, chainIdentifier, fromChain);
			if(text != null) //Only update if everything is correctly performed
				nbt.setInteger(this.getString(), index);
			return text;
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
					if(!chainIdentifier.isEmpty())
						chainIdentifier += ':';
					chainIdentifier += results[index].getString();
					
					return results[index].getMessage(consort, player, chainIdentifier);
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
						
						if(!chainIdentifier.isEmpty())
							chainIdentifier += ':';
						chainIdentifier += message.getString();
						
						ITextComponent text = message.getMessage(consort, player, chainIdentifier);
						
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
					
					if(!chainIdentifier.isEmpty())
						chainIdentifier += ':';
					chainIdentifier += message.getString();
					
					return message.getFromChain(consort, player, chainIdentifier, fromChain);
				}
			
			return null;
		}
		
	}
	
	public static class TradeMessage extends MessageType
	{
		protected String name;
		protected boolean repeat;
		protected ResourceLocation item;
		protected int cost;
		protected MessageType message;
		
		public TradeMessage(ResourceLocation item, int cost, MessageType message)
		{
			this(false, item, cost, message.getString(), message);
		}
		
		/**
		 * Make sure to use this constructor with a unique name, if the message is of a type that uses it's own stored data
		 */
		public TradeMessage(boolean repeat, ResourceLocation item, int cost, String name, MessageType message)
		{
			this.name = name;
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
			
			if(!repeat && nbt.getBoolean(name))
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
					nbt.setBoolean(name, true);
				
				LootContext.Builder contextBuilder = new LootContext.Builder((WorldServer) consort.world);
				for(ItemStack itemstack : consort.world.getLootTableManager()
						.getLootTableFromLocation(item)
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
			return name;
		}
	}
}