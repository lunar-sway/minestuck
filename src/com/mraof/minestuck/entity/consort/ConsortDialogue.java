package com.mraof.minestuck.entity.consort;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.WorldProviderLands;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

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
		addMessage(LandAspectRegistry.fromNameTitle("thunder"), "skeletonHorse");
		addMessage(LandAspectRegistry.fromNameTitle("thunder"), "blueMoon");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "bunnyBirthday");
		addMessage(LandAspectRegistry.fromNameTitle("rabbits"), "rabbitEating");
		addMessage(null, Sets.newHashSet(LandAspectRegistry.fromNameTitle("monsters").getVariations()), null, "petZombie");
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
		addMessage("lazyKing");
		addMessage("musicInvention");
		addMessage("rapBattle");
		
		addMessage(true, "awaitHero", "landName", "consortTypes", "playerTitleLand");
	}
	
	public static void addMessage(String message, String... args)
	{
		addMessage(false, null, message, args);
	}
	
	public static void addMessage(boolean reqLand, String message, String... args)
	{
		addMessage(reqLand, null, message, args);
	}
	
	public static void addMessage(EnumSet<EnumConsort> consort, String message, String... args)
	{
		addMessage(false, consort, message, args);
	}
	
	public static void addMessage(boolean reqLand, EnumSet<EnumConsort> consort, String message, String... args)
	{
		Message msg = new Message();
		msg.unlocalizedMessage = message;
		msg.args = args;
		msg.reqLand = reqLand;
		msg.consortRequirement = consort;
		messages.add(msg);
	}
	
	public static void addMessage(TerrainLandAspect aspect, String message, String... args)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message "+message+", this is probably not intended");
		addMessage(aspect == null ? null : Sets.newHashSet(aspect), null, null, message, args);
	}
	
	public static void addMessage(TitleLandAspect aspect, String message, String... args)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message "+message+", this is probably not intended");
		addMessage(null, aspect == null ? null : Sets.newHashSet(aspect), null, message, args);
	}
	
	public static void addMessage(Set<TerrainLandAspect> aspects1, Set<TitleLandAspect> aspects2, EnumSet<EnumConsort> consort, String message, String... args)
	{
		Message msg = new Message();
		msg.unlocalizedMessage = message;
		msg.args = args;
		msg.reqLand = true;
		msg.aspect1Requirement = aspects1;
		msg.aspect2Requirement = aspects2;
		msg.consortRequirement = consort;
		messages.add(msg);
	}
	
	public static Message getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.dimension);	//Change to a consort home dimension variable, as the current won't work as intended when the consort is moved from one dimension to another
		
		List<Message> list = new ArrayList<Message>();
		
		for(Message message : messages)
		{
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.aspect1Requirement != null && !message.aspect1Requirement.contains(aspects.aspectTerrain))
				continue;
			if(message.aspect2Requirement != null && !message.aspect2Requirement.contains(aspects.aspectTitle))
				continue;
			list.add(message);
		}
		
		return list.get(consort.world.rand.nextInt(list.size()));
	}
	
	public static class Message
	{
		private Message()
		{}
		
		boolean reqLand;
		
		String unlocalizedMessage;
		String[] args;
		
		Set<TerrainLandAspect> aspect1Requirement;
		Set<TitleLandAspect> aspect2Requirement;
		EnumSet<EnumConsort> consortRequirement;
		//More conditions
		
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			String s = EntityList.getEntityString(consort);
			if (s == null)
			{
				s = "generic";
			}
			
			Object[] args = new Object[this.args.length];
			for(int i = 0; i < this.args.length; i++)
			{
				if(this.args[i].equals("playerNameLand"))
				{
					SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
					if(c != null)
						args[i] = c.getClientIdentifier().getUsername();
					else args[i] = "Player name";
				}
				else if(this.args[i].equals("playerName"))
				{
					args[i] = player.getName();
				}
				else if(this.args[i].equals("landName"))
				{
					if(consort.world.provider instanceof WorldProviderLands)	//TODO make land name translate on the client side
						args[i] = ((WorldProviderLands) consort.world.provider).getDimensionName();
					else args[i] = "Land name";
				}
				else if(this.args[i].equals("playerTitleLand"))
				{
					SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
					if(c != null)
						args[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.toString();
					else args[i] = "Player title";
				}
				else if(this.args[i].equals("playerClassLand"))
				{
					SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
					if(c != null)
						args[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroClass().toString();
					else args[i] = "Player class";
				}
				else if(this.args[i].equals("playerAspectLand"))
				{
					SburbConnection c = SburbHandler.getConnectionForDimension(consort.dimension);
					if(c != null)
						args[i] = MinestuckPlayerData.getData(c.getClientIdentifier()).title.getHeroAspect().toString();
					else args[i] = "Player aspect";
				}
				else if(this.args[i].equals("consortType"))
				{
					args[i] = new TextComponentTranslation("entity." + s + ".name");
				}
				else if(this.args[i].equals("consortTypes"))
				{
					args[i] = new TextComponentTranslation("entity." + s + ".plural.name");
				}
			}
			
			TextComponentTranslation message = new TextComponentTranslation("consort."+unlocalizedMessage, args);
			TextComponentTranslation entity = new TextComponentTranslation("entity." + s + ".name");
			
			return new TextComponentTranslation("%s: %s", entity, message);
		}
	}
}