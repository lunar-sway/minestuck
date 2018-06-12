package com.mraof.minestuck.entity.consort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mraof.minestuck.item.MinestuckItems;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.util.AlchemyRecipeHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.MinestuckDimensionHandler;
import com.mraof.minestuck.world.lands.LandAspectRegistry;
import com.mraof.minestuck.world.lands.terrain.TerrainLandAspect;
import com.mraof.minestuck.world.lands.title.TitleLandAspect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.*;

import static com.mraof.minestuck.entity.consort.MessageType.*;
import static com.mraof.minestuck.world.lands.LandAspectRegistry.fromNameTerrain;
import static com.mraof.minestuck.world.lands.LandAspectRegistry.fromNameTitle;

/**
 * Handles message registry, message selection and contains the main message
 * class, which combines conditioning and a MessageType
 * 
 * @author Kirderf1
 */
public class ConsortDialogue
{
	
	private static final List<DialogueWrapper> messages = new LinkedList<DialogueWrapper>();
	
	/**
	 * Make sure to call after land registry
	 */
	public static void init()
	{
		
		addMessage(fromNameTitle("wind"), "dadWind");
		addMessage(fromNameTitle("wind"), new ChainMessage(new SingleMessage("pyre1"), new SingleMessage("pyre2")));
		addMessage(fromNameTitle("pulse"), "koolaid");
		addMessage(fromNameTitle("pulse"), "murderRain");
		addMessage(fromNameTitle("pulse"), "swimming");
		addMessage(fromNameTitle("thunder"), "skeletonHorse");
		addMessage(fromNameTitle("thunder"), "blueMoon");
		addMessage(fromNameTitle("thunder"), new ChainMessage(new SingleMessage("reckoning1"), new SingleMessage("reckoning2"), new SingleMessage("reckoning3", "consortType")));
		addMessage(fromNameTitle("rabbits"), "bunnyBirthday");
		addMessage(fromNameTitle("rabbits"), "rabbitEating");
		addMessage(fromNameTitle("rabbits"), "edgyLifeHatred");
		addMessage(fromNameTitle("monsters"), "petZombie");
		addMessage(null, Sets.newHashSet(fromNameTitle("monsters")), null, null,
				new SingleMessage("spiderRaid"));
		addMessage(fromNameTitle("monsters"), "monstersona");
		addMessage(fromNameTitle("towers"), "bugTreasure");
		addMessage(fromNameTitle("towers"), "towerGone");
		addMessage(fromNameTitle("towers"), "noTowerTreasure");
		addMessage(fromNameTitle("thought"), "glassBooks");
		addMessage(fromNameTitle("thought"), "bookFood");
		addMessage(fromNameTitle("thought"), "toEat");
		addMessage(fromNameTitle("cake"), "mysteryRecipe");
		addMessage(fromNameTitle("cake"), "cakeRegen");
		addMessage(fromNameTitle("cake"), "cakeRecipe");
		addMessage(fromNameTitle("clockwork"), "gearTechnology");
		addMessage(fromNameTitle("clockwork"), "evilGears");
		addMessage(fromNameTitle("clockwork"), "ticking");
		addMessage(LandAspectRegistry.frogAspect, "frogCreation");
		addMessage(LandAspectRegistry.frogAspect, "frogImitation");
		addMessage(fromNameTitle("buckets"), "lewdBuckets");
		addMessage(fromNameTitle("light"), "blindness");
		addMessage(fromNameTitle("light"), "doctorsInside");
		addMessage(fromNameTitle("light"), "staring");
		addMessage(fromNameTitle("silence"), "murderSilence");
		addMessage(fromNameTitle("silence"), "silentUnderlings");
		addMessage(fromNameTitle("silence"), new ChainMessage(new SingleMessage("listening1"), new SingleMessage("listening2")));
		
		addMessage(fromNameTerrain("shade"),
				new ChainMessage(2, new SingleMessage("mushFarm1"), new SingleMessage("mushFarm2"), new SingleMessage("mushFarm3"),
						new SingleMessage("mushFarm4"), new SingleMessage("mushFarm5"), new SingleMessage("mushFarm6"),
						new SingleMessage("mushFarm7")));
		addMessage(fromNameTerrain("shade"), new ChoiceMessage(new SingleMessage("mushroomPizza"),
				new SingleMessage[]{new SingleMessage("mushroomPizza.on"), new SingleMessage("mushroomPizza.off")},
				new MessageType[]{new SingleMessage("mushroomPizza.on.consortReply"), new SingleMessage("mushroomPizza.off.consortReply")}));
		addMessage(Sets.newHashSet(fromNameTerrain("shade")), allExcept(fromNameTitle("thunder")), null, null, new SingleMessage("fireHazard"));
		addMessage(fromNameTerrain("heat"), "gettingHot");
		addMessage(fromNameTerrain("heat"), "lavaCrickets");
		addMessage(fromNameTerrain("wood"), "woodTreatments");
		addMessage(fromNameTerrain("wood"), new ChainMessage(new SingleMessage("splinters1"), new SingleMessage("splinters2")));
		addMessage(fromNameTerrain("sand"), "sandSurfing");
		addMessage(fromNameTerrain("sand"), new ChoiceMessage(new SingleMessage("camel"), new SingleMessage[]{new SingleMessage("camel.yes"), new SingleMessage("camel.no")},
				new MessageType[]{new SingleMessage("camel.noCamel"), new SingleMessage("camel.dancingCamel")}));
		addMessage(fromNameTerrain("sandstone"), "knockoff");
		addMessage(fromNameTerrain("sandstone"), new ChainMessage(new SingleMessage("sandless1", "denizen"), new SingleMessage("sandless2")));
		addMessage(fromNameTerrain("frost"), new ChainMessage(new SingleMessage("frozen1"), new DescriptionMessage("frozen2")));
		addMessage(fromNameTerrain("frost"), new ChoiceMessage(new SingleMessage("furCoat"), new SingleMessage[]{new SingleMessage("furCoat.pay"), new SingleMessage("furCoat.ignore")},
				new MessageType[]{new PurchaseMessage(AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 100, new ChainMessage(1, new SingleMessage("furCoat.grattitude"), new SingleMessage("thankYou"))),
						new SingleMessage("furCoat.death")}));
		addMessage(fromNameTerrain("rock"), "allOres");
		addMessage(fromNameTerrain("rock"), "rockfu", "landName");
		addMessage(fromNameTerrain("forest"), "allTrees");
		addMessage(fromNameTerrain("forest"), "reallyLikesTrees");
		addMessage(fromNameTerrain("rainbow"), "genericGreen");
		addMessage(fromNameTerrain("rainbow"), "overwhelmingColors");
		addMessage(fromNameTerrain("rainbow"), "sawRainbow");
		addMessage(fromNameTerrain("rainbow"), "sunglasses");
		addMessage(fromNameTerrain("rainbow"), "whatIsWool");
		addMessage(fromNameTerrain("rainbow"), "loveColors");
		addMessage(fromNameTerrain("rainbow"), new ChainMessage(new SingleMessage("typesOfColors1"), new SingleMessage("typesOfColors2"), new SingleMessage("typesOfColors3"),
				new SingleMessage("typesOfColors4"), new SingleMessage("typesOfColors5"), new SingleMessage("typesOfColors6"), new SingleMessage("typesOfColors7"), new SingleMessage("typesOfColors8"),
				new SingleMessage("typesOfColors9"), new SingleMessage("typesOfColors10"), new SingleMessage("typesOfColors11"), new SingleMessage("typesOfColors12"), new SingleMessage("typesOfColors13"),
				new SingleMessage("typesOfColors14"), new SingleMessage("typesOfColors15"), new SingleMessage("typesOfColors16"), new SingleMessage("typesOfColors17"), new SingleMessage("typesOfColors18")));
		addMessage(fromNameTerrain("end"), "atTheEnd");
		addMessage(fromNameTerrain("end"), "chorusFruit");
		addMessage(fromNameTerrain("end"), "endGrass");
		addMessage(fromNameTerrain("end"), "grassCurse", "denizen");
		addMessage(fromNameTerrain("end"), "uselessPogo");
		addMessage(fromNameTerrain("end"), "uselessElytra");
		
		addMessage(true, "denizenMention");
		addMessage(true, "floatingIsland");
		addMessage("ringFishing");
		addMessage("frogWalk");
		addMessage("deliciousHair");
		//		addMessage("village"); Did not work as intended
		addMessage(LandAspectRegistry.fromNameTerrain("shade"), "lazyKing");
		addMessage("musicInvention");
		addMessage("wyrm");
		addMessage(true, new ConditionedMessage(new SingleMessage("heroicStench"), new SingleMessage("leechStench"),
				(EntityConsort consort, EntityPlayer player) -> SburbHandler.hasEntered((EntityPlayerMP) player)));
		addMessage(Sets.newHashSet(fromNameTerrain("heat")), Sets.newHashSet(fromNameTitle("cake")), null, null, new SingleMessage("fireCakes"));
		
		MessageType raps = new RandomMessage("rapBattles", RandomKeepResult.KEEP_CONSORT,
				new DelayMessage(new int[] {17, 17, 30},
					new SingleMessage("rapBattle.A1"), new SingleMessage("rapBattle.A2"),
					new SingleMessage("rapBattle.A3"), new SingleMessage("rapBattle.A4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rapBattle.B1"),new SingleMessage("rapBattle.B2"),
					new SingleMessage("rapBattle.B3"),new SingleMessage("rapBattle.B4")
				), new DelayMessage(new int[] {17},
					new SingleMessage("rapBattle.C1"),new SingleMessage("rapBattle.C2"),
					new SingleMessage("rapBattle.C3", "consortSound"), new SingleMessage("rapBattle.C4")
				), new DelayMessage(new int[] {25, 20, 30},
					new SingleMessage("rapBattle.D1"),new SingleMessage("rapBattle.D2"),
					new SingleMessage("rapBattle.D3"),new SingleMessage("rapBattle.D4")
				), new DelayMessage(new int[] {17, 20, 30},
					new SingleMessage("rapBattle.E1"),new SingleMessage("rapBattle.E2"),
					new SingleMessage("rapBattle.E3"),new SingleMessage("rapBattle.E4")
				), new DelayMessage(new int[] {25},
					new SingleMessage("rapBattle.F1"),new SingleMessage("rapBattle.F2"),
					new SingleMessage("rapBattle.F3"),new SingleMessage("rapBattle.F4")));
		addMessage(new ChoiceMessage(false, new SingleMessage("rapBattle"),
				new SingleMessage[]
				{
					new SingleMessage("rapBattle.accept"),
					new SingleMessage("rapBattle.deny")
				},
				new MessageType[] {
					//If you accepted the challenge
					new ChoiceMessage(false,
							new DescriptionMessage(raps, "rapBattle.rapsDesc"),
							new SingleMessage[] {
									new SingleMessage("rapBattleSchool"),
									new SingleMessage("rapBattleConcede")
							},
							new MessageType[] {
									new DoubleMessage(new DescriptiveMessage("rapBattleSchool.rap", "playerTitle", "landName"),
											new SingleMessage("rapBattleSchool.final", "consortSound")).setSayFirstOnce(),
									new SingleMessage("rapBattleConcede.final", "consortSound")
							}
					),
					//If you didn't accept the challenge
					new SingleMessage("rapBattle.denyAnswer")
				}
			).setAcceptNull()
		);
		
		addMessage(true, "awaitHero", "landName", "consortTypes", "playerTitleLand");
		addMessage(true, "watchSkaia");

		addMessage(EnumSet.of(EnumConsort.TURTLE), new SingleMessage("zazzerpan"));
		addMessage(EnumSet.of(EnumConsort.SALAMANDER), new SingleMessage("texasHistory", "landName"));
		addMessage(EnumSet.of(EnumConsort.IGUANA), new SingleMessage("disks"));
		addMessage(EnumSet.of(EnumConsort.IGUANA), new SingleMessage("whoops"));
		addMessage(EnumSet.of(EnumConsort.IGUANA), new SingleMessage("fourthWall"));
		addMessage(EnumSet.of(EnumConsort.SALAMANDER), new SingleMessage("hats"));
		addMessage(EnumSet.of(EnumConsort.TURTLE), new SingleMessage("wwizard"));
		addMessage(EnumSet.of(EnumConsort.NAKAGATOR), new SingleMessage("stockMarket"));
		addMessage(EnumSet.of(EnumConsort.SALAMANDER), new SingleMessage("identity", "playerTitleLand", "playerNameLand"));
		addMessage(EnumSet.of(EnumConsort.TURTLE),
				new ChainMessage(1, new SingleMessage("unknown1"), new SingleMessage("unknown2")));
		addMessage(EnumSet.of(EnumConsort.TURTLE),
				new ChainMessage(1, new SingleMessage("cult1", "playerTitle"), new SingleMessage("cult2")));

		addMessage(false, EnumConsort.MerchantType.SHADY, new ChoiceMessage(new DescriptionMessage("peppyOffer"),
				new SingleMessage[] { new SingleMessage("peppyOffer.buy"), new SingleMessage("peppyOffer.deny") },
				new MessageType[] {
						new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("peppyOffer.item"), new SingleMessage("peppyOffer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppyOffer.next"),
								new SingleMessage[] { new SingleMessage("peppyOffer.denyAgain"), new SingleMessage("peppyOffer.buy2") },
								new MessageType[] { new SingleMessage("dots"),
										new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("peppyOffer.purchase")) }) }));


		addMessage(true, null, null, new ChoiceMessage(true, new SingleMessage("titlePresence", "playerTitle"),
				new SingleMessage[] { new SingleMessage("titlePresence.iam", "playerTitle"), new SingleMessage("titlePresence.agree") },
				new MessageType[] { new SingleMessage("titlePresence.iamAnswer"), new SingleMessage("thanks") }));
		
		addMessage(false, EnumConsort.MerchantType.SHADY, new ChoiceMessage(new DescriptionMessage("shadyOffer"),
				new SingleMessage[]
						{
								new SingleMessage("shadyOffer.buy"),
								new SingleMessage("shadyOffer.deny")
						},
				new MessageType[] {
						new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1,
										new SingleMessage("shadyOffer.item"),
										new SingleMessage("shadyOffer.purchase")
								)
						),
						new ChoiceMessage(new SingleMessage("shadyOffer.next", "consortSound"),
								new SingleMessage[]
								{
										new SingleMessage("shadyOffer.denyAgain"),
										new SingleMessage("shadyOffer.buy2")
								},
								new MessageType[]
								{
										new SingleMessage("dots"),
										new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("shadyOffer.purchase")
										)
								}
						)
				}
		));
		
		addMessage(true, null, null, new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[] { new SingleMessage("denizen.what"), new SingleMessage("denizen.askAlignment") },
				new MessageType[] { new SingleMessage("denizen.explain", "playerClassLand"), new SingleMessage("denizen.alignment") }));
		
		List<ItemStack> hungryList = ImmutableList.of(new ItemStack(Items.COOKIE), new ItemStack(MinestuckItems.bugOnAStick),
				new ItemStack(MinestuckItems.grasshopper), new ItemStack(MinestuckItems.chocolateBeetle),
				new ItemStack(MinestuckItems.coneOfFlies));
		addMessage((EnumSet<EnumConsort>) null,
				new ItemRequirement(hungryList, false, true, new SingleMessage("hungry"),
						new ChoiceMessage(new SingleMessage("hungry.askFood", "nbtItem:hungry.item"),
								new SingleMessage[] { new SingleMessage("hungry.accept"), new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.item", 0, new SingleMessage("hungry.thanks")),
										new SingleMessage("sadface") })));
		addMessage((EnumSet<EnumConsort>) null,
				new ItemRequirement("hungry2", hungryList, false, true, false,
						new SingleMessage(
								"hungry"),
						new ChoiceMessage(
								new SingleMessage("hungry.askFood",
										"nbtItem:hungry2.item"),
								new SingleMessage[] {
										new SingleMessage(
												"hungry.accept"),
										new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry2.item", 0, new SingleMessage("hungry.thanks")),
										new ChoiceMessage(new SingleMessage("hungry.starving"),
												new SingleMessage[] { new SingleMessage("hungry.agree"),
														new SingleMessage("hungry.tooCheap") },
												new MessageType[] { new GiveItemMessage("hungry.sellItem", "hungry2.item", 10,
														new ChainMessage(1, new DescriptionMessage("hungry.finally", "nbtItem:hungry2.item"),
																new SingleMessage("hungry.finally"))),
														new SingleMessage("hungry.end") }) })));
	}
	
	public static void addMessage(MessageType message, String... args)
	{
		addMessage(false, null, null, message);
	}
	
	public static void addMessage(String message, String... args)
	{
		addMessage(false, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(boolean reqLand, String message, String... args)
	{
		addMessage(reqLand, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(boolean reqLand, MessageType message)
	{
		addMessage(reqLand, null, null, message);
	}
	
	public static void addMessage(boolean reqLand, EnumConsort.MerchantType merchantType, MessageType message)
	{
		addMessage(reqLand, null, EnumSet.of(merchantType), message);
	}
	
	public static void addMessage(EnumSet<EnumConsort> consort, MessageType message)
	{
		addMessage(false, consort, null, message);
	}
	
	public static void addMessage(boolean reqLand, EnumSet<EnumConsort> consort, EnumSet<EnumConsort.MerchantType> merchantTypes,
			MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper();
		msg.messageStart = message;
		msg.reqLand = reqLand;
		msg.consortRequirement = consort;
		msg.merchantRequirement = merchantTypes;
		messages.add(msg);
	}
	
	public static void addMessage(TerrainLandAspect aspect, String message, String... args)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(aspect == null ? null : Sets.newHashSet(aspect.getVariations()), null, null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(TitleLandAspect aspect, String message, String... args)
	{
		addMessage(null, aspect == null ? null : Sets.newHashSet(aspect.getVariations()), null, null, new SingleMessage(message, args));
	}
	
	public static void addMessage(TerrainLandAspect aspect, MessageType message)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(aspect == null ? null : Sets.newHashSet(aspect.getVariations()), null, null, null, message);
	}
	
	public static void addMessage(TitleLandAspect aspect, MessageType message)
	{
		if(aspect == null)
			Debug.warn("Land aspect is null for consort message " + message + ", this is probably not intended");
		addMessage(null, aspect == null ? null : Sets.newHashSet(aspect.getVariations()), null, null, message);
	}
	
	public static void addMessage(Set<TerrainLandAspect> aspects1, Set<TitleLandAspect> aspects2, EnumSet<EnumConsort> consort,
			EnumSet<EnumConsort.MerchantType> merchantTypes, MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper();
		msg.messageStart = message;
		msg.reqLand = true;
		msg.aspect1Requirement = aspects1;
		msg.aspect2Requirement = aspects2;
		msg.consortRequirement = consort;
		msg.merchantRequirement = merchantTypes;
		messages.add(msg);
	}
	
	public static Set<TitleLandAspect> allExcept(TitleLandAspect... aspects)
	{
		Set<TitleLandAspect> set = new HashSet<>();
		names: for (String name : LandAspectRegistry.getNamesTitle())
		{
			for (TitleLandAspect aspect : aspects)
				if (aspect.getPrimaryName().equals(name))
					continue names;
			set.add(LandAspectRegistry.fromNameTitle(name));
		}
		return set;
	}
	
	public static DialogueWrapper getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.homeDimension);
		
		List<DialogueWrapper> list = new ArrayList<DialogueWrapper>();
		
		for(DialogueWrapper message : messages)
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
					|| message.merchantRequirement != null && !message.merchantRequirement.contains(consort.merchantType))
				continue;
			list.add(message);
		}
		
		return list.get(consort.world.rand.nextInt(list.size()));
	}
	
	public static DialogueWrapper getMessageFromString(String name)
	{
		for(DialogueWrapper message : messages)
			if(message.getString().equals(name))
				return message;
		return null;
	}
	
	public static class DialogueWrapper
	{
		private DialogueWrapper()
		{
		}
		
		private boolean reqLand;
		
		private MessageType messageStart;
		
		private Set<TerrainLandAspect> aspect1Requirement;
		private Set<TitleLandAspect> aspect2Requirement;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<EnumConsort.MerchantType> merchantRequirement;
		//More conditions
		
		public ITextComponent getMessage(EntityConsort consort, EntityPlayer player)
		{
			return messageStart.getMessage(consort, player, "");
		}
		
		public ITextComponent getFromChain(EntityConsort consort, EntityPlayer player, String fromChain)
		{
			return messageStart.getFromChain(consort, player, "", fromChain);
		}
		
		public String getString()
		{
			return messageStart.getString();
		}
	}
}