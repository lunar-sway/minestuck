package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.inventory.ConsortMerchantInventory;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.player.Title;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.Optional;

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