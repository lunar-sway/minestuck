package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.gen.ChunkProviderLands;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Class where message content is defined. Also things such as if it's a chain
 * message or perhaps a message one could reply to
 * 
 * @author Kirderf1
 */
public abstract class MessageType
{
	
	public abstract String getString();
	
	public abstract ITextComponent getMessage(EntityConsort consort, EntityPlayer player);
	
	private static ITextComponent getMessage(EntityConsort consort, EntityPlayer player, String unlocalizedMessage,
			String[] args)
	{
		String s = EntityList.getEntityString(consort);
		if(s == null)
		{
			s = "generic";
		}
		
		Object[] obj = new Object[args.length];
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("playerNameLand"))
			{
				SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
				if(c != null)
					obj[i] = c.getClientIdentifier().getUsername();
				else
					obj[i] = "Player name";
			} else if(args[i].equals("playerName"))
			{
				obj[i] = player.getName();
			} else if(args[i].equals("landName"))
			{
				if(consort.world.provider instanceof WorldProviderLands) //TODO make land name translate on the client side
				{
					ChunkProviderLands chunkProvider = (ChunkProviderLands) player.world.provider
							.createChunkGenerator();
					ITextComponent aspect1 = new TextComponentTranslation(
							"land." + chunkProvider.aspect1.getNames()[chunkProvider.nameIndex1]);
					ITextComponent aspect2 = new TextComponentTranslation(
							"land." + chunkProvider.aspect2.getNames()[chunkProvider.nameIndex2]);
					if(chunkProvider.nameOrder)
						obj[i] = new TextComponentTranslation("land.message.check", aspect1, aspect2);
					else
						obj[i] = new TextComponentTranslation("land.message.check", aspect2, aspect1);
				} else
					obj[i] = "Land name";
			} else if(args[i].equals("playerTitleLand"))
			{
				SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
				if(c != null)
					obj[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.toString();
				else
					obj[i] = "Player title";
			} else if(args[i].equals("playerClassLand"))
			{
				SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
				if(c != null)
					obj[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroClass().toString();
				else
					obj[i] = "Player class";
			} else if(args[i].equals("playerAspectLand"))
			{
				SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
				if(c != null)
					obj[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroAspect().toString();
				else
					obj[i] = "Player aspect";
			} else if(args[i].equals("consortType"))
			{
				obj[i] = new TextComponentTranslation("entity." + s + ".name");
			} else if(args[i].equals("consortTypes"))
			{
				obj[i] = new TextComponentTranslation("entity." + s + ".plural.name");
			}
		}
		
		TextComponentTranslation message = new TextComponentTranslation("consort." + unlocalizedMessage, obj);
		TextComponentTranslation entity = new TextComponentTranslation("entity." + s + ".name");
		
		return new TextComponentTranslation("%s: %s", entity, message);
	}
	
	public static class SingleMessage extends MessageType
	{
		private String unlocalizedMessage;
		private String[] args;
		
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
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			return getMessage(consort, player, unlocalizedMessage, args);
		}
	}
	
	public static class ChainMessage extends MessageType
	{
		private String name;
		private MessageType[] messages;
		private int repeatIndex;
		
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
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			NBTTagCompound nbt = consort.getMessageTagForPlayer(player);
			int index = nbt.getInteger(this.getString());
			MessageType message = messages[index];
			index++;
			if(index >= messages.length)
				index = repeatIndex;
			
			nbt.setInteger(this.getString(), index);
			
			return message.getMessage(consort, player);
		}
	}
}