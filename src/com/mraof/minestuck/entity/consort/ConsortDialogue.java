package com.mraof.minestuck.entity.consort;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import static com.mraof.minestuck.entity.consort.MessageType.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;

/**
 * Handles message registry, message selection and contains the main message
 * class, which combines conditioning and a MessageType
 * 
 * @author Kirderf1
 */
public class ConsortDialogue
{
	
	private static final List<ConditionedMessage> messages = new LinkedList<ConditionedMessage>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		
		addMessage(LandAspectRegistry.fromNameTitle("wind"), "dadWind");
		addMessage(LandAspectRegistry.fromNameTitle("pulse"), "koolaid");
		addMessage(LandAspectRegistry.fromNameTitle("pulse"), "murderRain");
		addMessage(LandAspectRegistry.fromNameTitle("thunder"), "skeletonHorse");
		addMessage(LandAspectRegistry.fromNameTitle("thunder"), "blueMoon");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "bunnyBirthday");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "rabbitEating");
		addMessage(null, Sets.newHashSet(LandAspectRegistry.fromNameTitle("monsters").getVariations()), null, null,
				new SingleMessage("petZombie"));
		addMessage(LandAspectRegistry.fromNameTitle("monsters"), "spiderRaid");
		addMessage(LandAspectRegistry.fromNameTitle("towers"), "bugTreasure");
		addMessage(LandAspectRegistry.fromNameTitle("towers"), "towerGone");
		addMessage(LandAspectRegistry.fromNameTitle("thought"), "glassBooks");
		addMessage(LandAspectRegistry.fromNameTitle("thought"), "bookFood");
		addMessage(LandAspectRegistry.fromNameTitle("cake"), "cakeRecipe");
		addMessage(LandAspectRegistry.fromNameTitle("cake"), "cakeRegen");
		addMessage(LandAspectRegistry.fromNameTitle("clockwork"), "gearTechnology");
		addMessage(LandAspectRegistry.fromNameTitle("clockwork"), "evilGears");
		addMessage(LandAspectRegistry.frogAspect, "frogCreation");
		addMessage(LandAspectRegistry.frogAspect, "frogImitation");
		addMessage(LandAspectRegistry.fromNameTitle("light"), "blindness");
		addMessage(LandAspectRegistry.fromNameTitle("light"), "doctorsInside");
		addMessage(LandAspectRegistry.fromNameTitle("silence"), "murderSilence");
		addMessage(LandAspectRegistry.fromNameTitle("silence"), "silentUnderlings");
		
		addMessage(true, "denizenMention");
		addMessage("hungry");
		addMessage(true, "floatingIsland");
		addMessage("ringFishing");
		addMessage("frogWalk");
		addMessage("deliciousHair");
		//		addMessage("village"); Did not work as intended
		addMessage(LandAspectRegistry.fromNameTerrain("shade"), "lazyKing");
		addMessage("musicInvention");
		addMessage("rapBattle");
		
		addMessage(true, "awaitHero", "landName", "consortTypes", "playerTitleLand");
		addMessage(true, "watchSkaia");
		addMessage(LandAspectRegistry.fromNameTerrain("shade"),
				new ChainMessage(2, new SingleMessage("mushFarm1"), new SingleMessage("mushFarm2"),
						new SingleMessage("mushFarm3"), new SingleMessage("mushFarm4"), new SingleMessage("mushFarm5"),
						new SingleMessage("mushFarm6"), new SingleMessage("mushFarm7")));
		addMessage(true, null, null, new ChoiceMessage(true, new SingleMessage("titlePresence", "playerTitle"),
				new SingleMessage[] { new SingleMessage("titlePresence.iam", "playerTitle"),
						new SingleMessage("titlePresence.agree") },
				new MessageType[] { new SingleMessage("titlePresence.iamAnswer"), new SingleMessage("thanks") }));
		
		addMessage(false, EnumConsort.MerchantType.SHADY, new ChoiceMessage(new DescriptionMessage("shadyOffer"),
				new SingleMessage[] { new SingleMessage("shadyOffer.buy"), new SingleMessage("shadyOffer.deny") },
				new MessageType[] {
						new TradeMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("shadyOffer.item"),
										new SingleMessage("shadyOffer.purchase"))),
						new ChoiceMessage(new SingleMessage("shadyOffer.next"),
								new SingleMessage[] { new SingleMessage("shadyOffer.denyAgain"),
										new SingleMessage("shadyOffer.buy2") },
								new MessageType[] { new SingleMessage("dots"),
										new TradeMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("shadyOffer.purchase")) }) }));
		
	}
	
	public static void addMessage(String message, String... args)
	{
		addMessage(false, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(boolean reqLand, String message, String... args)
	{
		addMessage(reqLand, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(boolean reqLand, EnumConsort.MerchantType merchantType, MessageType message)
	{
		addMessage(reqLand, null, EnumSet.of(merchantType), message);
	}
	
	public static void addMessage(EnumSet<EnumConsort> consort, MessageType message)
	{
		addMessage(false, consort, null, message);
	}
	
	public static void addMessage(boolean reqLand, EnumSet<EnumConsort> consort,
			EnumSet<EnumConsort.MerchantType> merchantTypes, MessageType message)
	{
		ConditionedMessage msg = new ConditionedMessage();
		msg.messageType = message;
		msg.reqLand = reqLand;
		msg.consortRequirement = consort;
		msg.merchantRequirement = merchantTypes;
		messages.add(msg);
	}
	
	public static void addMessage(TerrainLandAspect aspect, String message, String... args)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(aspect == null ? null : Sets.newHashSet(aspect), null, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(TitleLandAspect aspect, String message, String... args)
	{
		addMessage(null, aspect == null ? null : Sets.newHashSet(aspect), null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(TerrainLandAspect aspect, MessageType message)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(aspect == null ? null : Sets.newHashSet(aspect), null, null, null, message);
	}
	
	public static void addMessage(TitleLandAspect aspect, MessageType message)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(null, aspect == null ? null : Sets.newHashSet(aspect), null, null, message);
	}
	
	public static void addMessage(Set<TerrainLandAspect> aspects1, Set<TitleLandAspect> aspects2,
			EnumSet<EnumConsort> consort, EnumSet<EnumConsort.MerchantType> merchantTypes, MessageType message)
	{
		ConditionedMessage msg = new ConditionedMessage();
		msg.messageType = message;
		msg.reqLand = true;
		msg.aspect1Requirement = aspects1;
		msg.aspect2Requirement = aspects2;
		msg.consortRequirement = consort;
		msg.merchantRequirement = merchantTypes;
		messages.add(msg);
	}
	
	public static ConditionedMessage getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.dimension); //Change to a consort home dimension variable, as the current won't work as intended when the consort is moved from one dimension to another
		
		List<ConditionedMessage> list = new ArrayList<ConditionedMessage>();
		
		for(ConditionedMessage message : messages)
		{
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.aspect1Requirement != null && !message.aspect1Requirement.contains(aspects.aspectTerrain))
				continue;
			if(message.aspect2Requirement != null && !message.aspect2Requirement.contains(aspects.aspectTitle))
				continue;
			if(message.merchantRequirement == null && consort.merchantType != EnumConsort.MerchantType.NONE
					|| message.merchantRequirement != null
							&& !message.merchantRequirement.contains(consort.merchantType))
				continue;
			list.add(message);
		}
		
		return list.get(consort.world.rand.nextInt(list.size()));
	}
	
	public static ConditionedMessage getMessageFromString(String name)
	{
		for(ConditionedMessage message : messages)
			if(message.getString().equals(name))
				return message;
		return null;
	}
	
	public static class ConditionedMessage
	{
		private ConditionedMessage()
		{
		}
		
		private boolean reqLand;
		
		private MessageType messageType;
		
		private Set<TerrainLandAspect> aspect1Requirement;
		private Set<TitleLandAspect> aspect2Requirement;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<EnumConsort.MerchantType> merchantRequirement;
		//More conditions
		
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			return messageType.getMessage(consort, player, "");
		}
		
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String fromChain)
		{
			return messageType.getFromChain(consort, player, "", fromChain);
		}
		
		public String getString()
		{
			return messageType.getString();
		}
	}
}