package com.mraof.minestuck.entity.consort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
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
import static com.mraof.minestuck.util.AlchemyRecipeHandler.CONSORT_FOOD_STOCK;
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
		
		addMessage("dadWind").landTitle(fromNameTitle("wind"));
		addMessage(new ChainMessage(new SingleMessage("pyre1"), new SingleMessage("pyre2"))).landTitle(fromNameTitle("wind"));
		addMessage("koolaid").landTitle(fromNameTitle("pulse"));
		addMessage("murderRain").landTitle(fromNameTitle("pulse"));
		addMessage("swimming").landTitle(fromNameTitle("pulse"));
		addMessage("skeletonHorse").landTitle(fromNameTitle("thunder"));
		addMessage("blueMoon").landTitle(fromNameTitle("thunder"));
		addMessage(new ChainMessage(new SingleMessage("reckoning1"), new SingleMessage("reckoning2"), new SingleMessage("reckoning3", "consortType"))).landTitle(fromNameTitle("thunder"));
		addMessage("bunnyBirthday").landTitle(fromNameTitle("rabbits"));
		addMessage("rabbitEating").landTitle(fromNameTitle("rabbits"));
		addMessage("edgyLifeHatred").landTitle(fromNameTitle("rabbits"));
		addMessage(new SingleMessage("petZombie")).landTitle(fromNameTitle("monsters"));
		addMessage("spiderRaid").landTitleSpecific(fromNameTitle("monsters"));
		addMessage("monstersona").landTitle(fromNameTitle("monsters"));
		addMessage("bugTreasure").landTitleSpecific(fromNameTitle("towers"));
		addMessage("towerGone").landTitleSpecific(fromNameTitle("towers"));
		addMessage("noTowerTreasure").landTitle(fromNameTitle("towers"));
		addMessage("glassBooks").landTitleSpecific(fromNameTitle("thought"));
		addMessage("bookFood").landTitleSpecific(fromNameTitle("thought"));
		addMessage("toEat").landTitle(fromNameTitle("thought"));
		addMessage("mysteryRecipe").landTitleSpecific(fromNameTitle("cake"));
		addMessage("cakeRegen").landTitleSpecific(fromNameTitle("cake"));
		addMessage("cakeRecipe").landTitleSpecific(fromNameTitle("cake"));
		addMessage("gearTechnology").landTitleSpecific(fromNameTitle("clockwork"));
		addMessage("evilGears").landTitleSpecific(fromNameTitle("clockwork"));
		addMessage("ticking").landTitleSpecific(fromNameTitle("clockwork"));
		addMessage("frogCreation").landTitleSpecific(LandAspectRegistry.frogAspect);
		addMessage("frogImitation").landTitleSpecific(LandAspectRegistry.frogAspect);
		addMessage("lewdBuckets").landTitleSpecific(fromNameTitle("buckets"));
		addMessage("blindness").landTitleSpecific(fromNameTitle("light"));
		addMessage("doctorsInside").landTitleSpecific(fromNameTitle("light"));
		addMessage("staring").landTitleSpecific(fromNameTitle("light"));
		addMessage("murderSilence").landTitleSpecific(fromNameTitle("silence"));
		addMessage("silentUnderlings").landTitleSpecific(fromNameTitle("silence"));
		
		addMessage(new ChainMessage(new SingleMessage("listening1"), new SingleMessage("listening2"))).landTitle(fromNameTitle("silence"));
		
		addMessage(new ChainMessage(2, new SingleMessage("mushFarm1"), new SingleMessage("mushFarm2"), new SingleMessage("mushFarm3"),
				new SingleMessage("mushFarm4"), new SingleMessage("mushFarm5"), new SingleMessage("mushFarm6"),
				new SingleMessage("mushFarm7"))).landTerrain(fromNameTerrain("shade"));
		addMessage(new ChoiceMessage(new SingleMessage("mushroomPizza"),
				new SingleMessage[]{new SingleMessage("mushroomPizza.on"), new SingleMessage("mushroomPizza.off")},
				new MessageType[]{new SingleMessage("mushroomPizza.on.consortReply"), new SingleMessage("mushroomPizza.off.consortReply")})).landTerrain(fromNameTerrain("shade"));
		addMessage("gettingHot").landTerrain(fromNameTerrain("heat"));
		addMessage("properFuneral").landTerrain(fromNameTerrain("wood"));
		addMessage("sandSurfing").landTerrain(fromNameTerrain("sand"));
		addMessage("knockoff").landTerrain(fromNameTerrain("sandstone"));
		addMessage(new ChainMessage(new SingleMessage("frozen1"), new DescriptionMessage("frozen2"))).landTerrain(fromNameTerrain("frost"));
		addMessage("allOres").landTerrain(fromNameTerrain("rock"));
		addMessage("allTrees").landTerrain(fromNameTerrain("forest"));
		
		addMessage("denizenMention").reqLand();
		addMessage("floatingIsland").reqLand();
		addMessage("ringFishing");
		addMessage("frogWalk");
		addMessage("deliciousHair");
		//		addMessage("village"); Did not work as intended
		addMessage("lazyKing").landTerrain(fromNameTerrain("shade"));
		addMessage("musicInvention");
		addMessage("wyrm");
		addMessage(new ConditionedMessage(new SingleMessage("heroicStench"), new SingleMessage("leechStench"),
				(EntityConsort consort, EntityPlayer player) -> SburbHandler.hasEntered((EntityPlayerMP) player))).reqLand();
		addMessage(new SingleMessage("fireCakes")).landTerrain(fromNameTerrain("heat")).landTitle(fromNameTitle("cake"));
		
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
		
		addMessage("awaitHero", "landName", "consortTypes", "playerTitleLand").reqLand();
		addMessage("watchSkaia").reqLand();
		
		addMessage(new SingleMessage("zazzerpan")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("texasHistory", "landName")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("disks")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("whoops")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("fourthWall")).consort(EnumConsort.IGUANA);
		addMessage(new SingleMessage("hats")).consort(EnumConsort.SALAMANDER);
		addMessage(new SingleMessage("wwizard")).consort(EnumConsort.TURTLE);
		addMessage(new SingleMessage("stockMarket")).consort(EnumConsort.NAKAGATOR);
		addMessage(new SingleMessage("identity", "playerTitleLand", "playerNameLand")).consort(EnumConsort.SALAMANDER);
		addMessage(new ChainMessage(1, new SingleMessage("unknown1"), new SingleMessage("unknown2"))).consort(EnumConsort.TURTLE);
		addMessage(new ChainMessage(1, new SingleMessage("cult1", "playerTitle"), new SingleMessage("cult2")));

		addMessage(new ChoiceMessage(new DescriptionMessage("peppyOffer"),
				new SingleMessage[] { new SingleMessage("peppyOffer.buy"), new SingleMessage("peppyOffer.deny") },
				new MessageType[] {
						new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 1000, "purchase",
								new ChainMessage(1, new SingleMessage("peppyOffer.item"), new SingleMessage("peppyOffer.purchase"))),
						new ChoiceMessage(new SingleMessage("peppyOffer.next"),
								new SingleMessage[] { new SingleMessage("peppyOffer.denyAgain"), new SingleMessage("peppyOffer.buy2") },
								new MessageType[] { new SingleMessage("dots"),
										new PurchaseMessage(false, AlchemyRecipeHandler.CONSORT_JUNK_REWARD, 500, "purchase",
												new SingleMessage("peppyOffer.purchase")) }) })).type(MerchantType.SHADY);


		addMessage(new ChoiceMessage(true, new SingleMessage("titlePresence", "playerTitle"),
				new SingleMessage[] { new SingleMessage("titlePresence.iam", "playerTitle"), new SingleMessage("titlePresence.agree") },
				new MessageType[] { new SingleMessage("titlePresence.iamAnswer"), new SingleMessage("thanks") })).reqLand();
		
		addMessage(new ChoiceMessage(new DescriptionMessage("shadyOffer"),
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
		)).type(MerchantType.SHADY);
		
		addMessage(new ChoiceMessage(true, new SingleMessage("denizen", "denizen"),
				new SingleMessage[] { new SingleMessage("denizen.what"), new SingleMessage("denizen.askAlignment") },
				new MessageType[] { new SingleMessage("denizen.explain", "playerClassLand"), new SingleMessage("denizen.alignment") })).reqLand();
		
		List<ItemStack> hungryList = ImmutableList.of(new ItemStack(Items.COOKIE), new ItemStack(MinestuckItems.bugOnAStick),
				new ItemStack(MinestuckItems.grasshopper), new ItemStack(MinestuckItems.chocolateBeetle),
				new ItemStack(MinestuckItems.coneOfFlies));
		addMessage(new ItemRequirement(hungryList, false, true, new SingleMessage("hungry"),
						new ChoiceMessage(new SingleMessage("hungry.askFood", "nbtItem:hungry.item"),
								new SingleMessage[] { new SingleMessage("hungry.accept"), new SingleMessage("hungry.deny") },
								new MessageType[] { new GiveItemMessage("hungry.item", 0, new SingleMessage("hungry.thanks")),
										new SingleMessage("sadface") })));
		addMessage(new ItemRequirement("hungry2", hungryList, false, true, false,
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
		
		addMessage(new MerchantGuiMessage(new SingleMessage("foodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).reqLand();
		addMessage(new MerchantGuiMessage(new SingleMessage("fastFood"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).reqLand();
		addMessage(new MerchantGuiMessage(new SingleMessage("groceryStore"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).reqLand();
		addMessage(new MerchantGuiMessage(new SingleMessage("breathFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("wind"));
		addMessage(new MerchantGuiMessage(new SingleMessage("bloodFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("pulse"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lifeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("rabbits"));
		addMessage(new MerchantGuiMessage(new SingleMessage("doomFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thunder"));
		addMessage(new MerchantGuiMessage(new SingleMessage("frogFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(LandAspectRegistry.frogAspect);
		addMessage(new MerchantGuiMessage(new SingleMessage("spaceFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("buckets"));
		addMessage(new MerchantGuiMessage(new SingleMessage("timeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"));
		addMessage(new MerchantGuiMessage(new SingleMessage("thymeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("clockwork"), fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("mindFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("thought"));
		addMessage(new MerchantGuiMessage(new SingleMessage("heartFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("cake"));
		addMessage(new MerchantGuiMessage(new SingleMessage("lightFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("light"));
		addMessage(new MerchantGuiMessage(new SingleMessage("voidFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("silence"));
		addMessage(new MerchantGuiMessage(new SingleMessage("rageFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("monsters"));
		addMessage(new MerchantGuiMessage(new DescriptionMessage("hopeFoodShop"), CONSORT_FOOD_STOCK)).type(MerchantType.FOOD).landTitle(fromNameTitle("towers"));
		
	}
	
	public static DialogueWrapper addMessage(String message, String... args)
	{
		return addMessage(new SingleMessage(message, args));
	}
	public static DialogueWrapper addMessage(MessageType message)
	{
		DialogueWrapper msg = new DialogueWrapper();
		msg.messageStart = message;
		messages.add(msg);
		return msg;
	}
	
	public static DialogueWrapper getRandomMessage(EntityConsort consort, EntityPlayer player)
	{
		LandAspectRegistry.AspectCombination aspects = MinestuckDimensionHandler.getAspects(consort.homeDimension);
		
		List<DialogueWrapper> list = new ArrayList<>();
		
		for(DialogueWrapper message : messages)
		{
			if(message.reqLand && aspects == null)
				continue;
			if(message.consortRequirement != null && !message.consortRequirement.contains(consort.getConsortType()))
				continue;
			if(message.aspect1Requirement != null && !message.aspect1Requirement.contains(aspects.aspectTerrain.getPrimaryVariant()))
				continue;
			if(message.aspect2Requirement != null && !message.aspect2Requirement.contains(aspects.aspectTitle.getPrimaryVariant()))
				continue;
			if(message.aspect1RequirementS != null && !message.aspect1RequirementS.contains(aspects.aspectTerrain))
				continue;
			if(message.aspect2RequirementS != null && !message.aspect2RequirementS.contains(aspects.aspectTitle))
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
		private Set<TerrainLandAspect> aspect1RequirementS;
		private Set<TitleLandAspect> aspect2RequirementS;
		private EnumSet<EnumConsort> consortRequirement;
		private EnumSet<MerchantType> merchantRequirement;
		
		public DialogueWrapper reqLand()
		{
			reqLand = true;
			return this;
		}
		
		public DialogueWrapper landTerrain(TerrainLandAspect... aspects)
		{
			for(TerrainLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect1Requirement = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTerrainSpecific(TerrainLandAspect... aspects)
		{
			for(TerrainLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect1RequirementS = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTitle(TitleLandAspect... aspects)
		{
			for(TitleLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect2Requirement = Sets.newHashSet(aspects);
			return this;
		}
		
		public DialogueWrapper landTitleSpecific(TitleLandAspect... aspects)
		{
			for(TitleLandAspect aspect : aspects)
				if(aspect == null)
				{
					Debug.warn("Land aspect is null for consort message " + messageStart.getString() + ", this is probably not intended");
					break;
				}
			reqLand = true;
			aspect2RequirementS = Sets.newHashSet(aspects);
			return this;
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