package com.mraof.minestuck.entity.consort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.ILandAspect;
import com.mraof.minestuck.world.lands.LandAspectRegistry;

import net.minecraft.entity.player.EntityPlayer;

public class ConsortDialogue
{
	
	private static final List<Message> messages = new LinkedList<Message>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		
		addMessage(LandAspectRegistry.fromNameTitle("wind"), "dadWind");
		addMessage(LandAspectRegistry.fromNameTitle("pulse"), "koolaid");
		addMessage(LandAspectRegistry.fromNameTitle("pulse"), "murderRain");
		addMessage(LandAspectRegistry.fromNameTitle("storm"), "skeletonHorse");
		addMessage(LandAspectRegistry.fromNameTitle("storm"), "blueMoon");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "bunnyBirthday");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "rabbitEating");
		addMessage(LandAspectRegistry.fromNameTitle("monsters"), "petZombie");
		addMessage(LandAspectRegistry.fromNameTitle("monsters_dead"), "petZombie");
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
		
		addMessage("denizenMention");
		addMessage("hungry");
		addMessage("floatingIsland");
		addMessage("ringFishing");
		addMessage("frogWalk");
		addMessage("deliciousHair");
		addMessage("village");
		addMessage("lazyKing");
		addMessage("musicInvention");
		addMessage("rapBattle");
	}
	
	public static void addMessage(String message, String... args)
	{
		addMessage(null, null, message, args);
	}
	
	public static void addMessage(Class<EntityConsort> consort, String message, String... args)
	{
		addMessage(null, consort, message, args);
	}
	
	public static void addMessage(ILandAspect aspect, String message, String... args)
	{
		addMessage(aspect, null, message, args);
	}
	
	public static void addMessage(ILandAspect aspect, Class<EntityConsort> consort, String message, String... args)
	{
		Message msg = new Message();
		msg.unlocalizedMessage = message;
		msg.args = args;
		msg.aspectRequirement = aspect;
		msg.consortRequirement = consort;
		messages.add(msg);
	}
	
	public static Message getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.dimension);	//Change to a consort home dimension variable, as the current won't work as intended when the consort is moved from one dimension to another
		
		List<Message> list = new ArrayList<Message>();
		
		for(Message message : messages)
		{
			if(message.consortRequirement != null && !message.consortRequirement.isAssignableFrom(consort.getClass()))
				continue;
			if(message.aspectRequirement != null && (aspects == null || !(message.aspectRequirement == aspects.aspectTerrain || message.aspectRequirement == aspects.aspectTitle)))
				continue;
			list.add(message);
		}
		
		return list.get(consort.world.rand.nextInt(list.size()));
	}
	
	public static class Message
	{
		private Message()
		{}
		
		String unlocalizedMessage;
		String[] args;
		
		ILandAspect aspectRequirement;
		Class<EntityConsort> consortRequirement;
		//More conditions
	}
}