package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.entity.consort.MessageType.MerchantGuiMessage;
import com.mraof.minestuck.entity.consort.MessageType.SingleMessage;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypeConditions;
import com.mraof.minestuck.world.lands.LandTypePair;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import static com.mraof.minestuck.item.loot.MSLootTables.CONSORT_GENERAL_STOCK;
import static com.mraof.minestuck.world.lands.LandTypeConditions.terrainLand;
import static com.mraof.minestuck.world.lands.LandTypeConditions.titleLand;
import static com.mraof.minestuck.world.lands.LandTypes.*;

/**
 * Handles message registry, message selection and contains the main message
 * class, which combines conditioning and a MessageType
 *
 * @author Kirderf1
 */
public class ConsortDialogue
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final List<DialogueWrapper> messages = new LinkedList<>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		addMessage(new MerchantGuiMessage(new SingleMessage("general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("got_the_goods"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rising_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("breeze_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(WIND)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("blood_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(PULSE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("life_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(RABBITS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("doom_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(THUNDER)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("frog_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(FROGS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("time_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(CLOCKWORK)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("book_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(THOUGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("cake_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(CAKE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("light_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(LIGHT)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("silence_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(SILENCE)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("rage_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(MSTags.TitleLandTypes.MONSTERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("tower_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(TOWERS)).lockToConsort();
		addMessage(new MerchantGuiMessage(new SingleMessage("buckets_general_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(titleLand(BUCKETS)).lockToConsort();
		
		addMessage(new MerchantGuiMessage(new SingleMessage("boring_shop"), CONSORT_GENERAL_STOCK)).type(MerchantType.GENERAL).condition(terrainLand(RAINBOW));
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static DialogueWrapper addMessage(MessageType message)
	{
		return addMessage(10, message);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static DialogueWrapper addMessage(int weight, MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper(weight);
		msg.messageStart = message;
		messages.add(msg);
		return msg;
	}
	
	public static DialogueWrapper getRandomMessage(ConsortEntity consort, boolean hasHadMessage)
	{
		LandTypePair aspects = LandTypePair.getTypes(consort.getServer(), consort.homeDimension).orElse(null);
		
		List<DialogueWrapper> list = new ArrayList<>();
		
		for(DialogueWrapper message : messages)
		{
			if(message.lockToConsort && hasHadMessage)
				continue;
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.terrainCondition != null && !(aspects != null && message.terrainCondition.test(aspects.getTerrain())))
				continue;
			if(message.titleCondition != null && !(aspects != null && message.titleCondition.test(aspects.getTitle())))
				continue;
			if(message.merchantRequirement == null && consort.merchantType != EnumConsort.MerchantType.NONE
					|| message.merchantRequirement != null && !message.merchantRequirement.contains(consort.merchantType))
				continue;
			if(message.additionalRequirement != null && !message.additionalRequirement.apply(consort))
				continue;
			list.add(message);
		}
		
		return WeightedRandom.getRandomItem(consort.level().random, list).orElseThrow();
	}
	
	public static DialogueWrapper getMessageFromString(String name)
	{
		for(DialogueWrapper message : messages)
			if(message.getString().equals(name))
				return message;
		return null;
	}
	
	public static class DialogueWrapper extends WeightedEntry.IntrusiveBase
	{
		
		private DialogueWrapper(int weight)
		{
			super(weight);
		}
		
		private boolean reqLand;
		private boolean lockToConsort;
		
		private MessageType messageStart;
		
		@Nullable
		private LandTypeConditions.Terrain terrainCondition;
		@Nullable
		private LandTypeConditions.Title titleCondition;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<MerchantType> merchantRequirement;
		private ConsortRequirement additionalRequirement;
		
		public DialogueWrapper reqLand()
		{
			reqLand = true;
			return this;
		}
		
		public DialogueWrapper lockToConsort()
		{
			lockToConsort = true;
			return this;
		}
		
		public boolean isLockedToConsort()
		{
			return lockToConsort;
		}
		
		public DialogueWrapper condition(LandTypeConditions.Terrain condition)
		{
			this.terrainCondition = condition;
			return this.reqLand();
		}
		
		public DialogueWrapper condition(LandTypeConditions.Title condition)
		{
			this.titleCondition = condition;
			return this.reqLand();
		}
		
		public DialogueWrapper consort(EnumConsort... types)
		{
			consortRequirement = EnumSet.of(types[0], types);
			return this;
		}
		
		public DialogueWrapper type(MerchantType... types)
		{
			merchantRequirement = EnumSet.of(types[0], types);
			return this;
		}
		
		public DialogueWrapper consortReq(ConsortRequirement req)
		{
			additionalRequirement = req;
			return this;
		}
		
		public Component getMessage(ConsortEntity consort, ServerPlayer player)
		{
			return messageStart.getMessage(consort, player, "");
		}
		
		public Component getFromChain(ConsortEntity consort, ServerPlayer player, String fromChain)
		{
			return messageStart.getFromChain(consort, player, "", fromChain);
		}
		
		public String getString()
		{
			return messageStart.getString();
		}
		
	}
	
	public interface ConsortRequirement
	{
		boolean apply(ConsortEntity consort);
	}
	
	public static void serverStarting()
	{
		//debugPrintAll();
	}
	
	@SuppressWarnings("unused")
	private static void debugPrintAll()
	{
		List<Component> list = new ArrayList<>();
		for(DialogueWrapper wrapper : messages)
		{
			wrapper.messageStart.debugAddAllMessages(list);
		}
		
		for(Component textComponent : list)
			LOGGER.info(textComponent.getString());
	}
}