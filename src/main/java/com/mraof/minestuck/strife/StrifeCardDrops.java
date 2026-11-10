package com.mraof.minestuck.strife;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;
import com.mraof.minestuck.player.StrifePortfolioData;
import com.mraof.minestuck.player.StrifeSpecibus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.*;

/**
 * Lets hostile mobs drop strife cards when they are killed by a player.
 *
 * <ul>
 *   <li>A weapon that a mob would drop is turned into a card of its kind that already holds the weapon.</li>
 *   <li>Underlings can drop cards of a random kind (or even a blank card).</li>
 *   <li>Some cards come with a weapon inside. The weapon is picked at random, weighted by the inverse of its grist cost:
 *   cheap weapons are common, expensive ones are extremely rare.</li>
 * </ul>
 */
public final class StrifeCardDrops
{
	private record Candidate(Item item, double weight)
	{
	}
	
	/**
	 * Cost-weighted weapon lists per kind. The cache is thrown away whenever the recipe manager is reloaded.
	 */
	private static final Map<String, List<Candidate>> CANDIDATE_CACHE = new HashMap<>();
	private static RecipeManager cachedFor = null;
	
	public static void onMobDrops(LivingDropsEvent event)
	{
		LivingEntity victim = event.getEntity();
		if(victim instanceof Player || !(victim instanceof Enemy)) return;
		if(!event.isRecentlyHit()) return;
		if(!(event.getSource().getEntity() instanceof ServerPlayer player) || player instanceof FakePlayer) return;
		if(!(victim.level() instanceof ServerLevel level)) return;
		
		StrifePortfolioData data = StrifePortfolioHandler.getData(player);
		if(!canDropCards(player, data)) return;
		
		RandomSource random = player.getRandom();
		boolean underling = victim instanceof UnderlingEntity;
		
		double baseChance = data.getDroppedCards() <= 0 && underling
				? MinestuckConfig.SERVER.strifeCardFirstUnderlingDropChance.get()
				: MinestuckConfig.SERVER.strifeCardDropChance.get();
		if(random.nextDouble() >= baseChance * (getLootingLevel(player, level) + 1)) return;
		
		//1: weapon that the mob drops turns into a card
		for(ItemEntity itemEntity : event.getDrops())
		{
			ItemStack stack = itemEntity.getItem();
			List<KindAbstratusType> kinds = getKindsOf(stack);
			if(kinds.isEmpty()) continue;
			
			StrifeSpecibus specibus = new StrifeSpecibus(kinds.get(random.nextInt(kinds.size())).getUnlocalizedName());
			specibus.putItemStack(stack);
			itemEntity.setItem(StrifePortfolioHandler.createStrifeCard(specibus));
			data.addDroppedCard();
			StrifePortfolioHandler.syncToClient(player);
			return;
		}
		
		//2: underlings drop a random card
		if(!underling) return;
		
		ItemStack card = createRandomCard(level, random);
		ItemEntity cardEntity = new ItemEntity(level, victim.getX(), victim.getY(), victim.getZ(), card);
		cardEntity.setDefaultPickUpDelay();
		event.getDrops().add(cardEntity);
		data.addDroppedCard();
		StrifePortfolioHandler.syncToClient(player);
	}
	
	private static boolean canDropCards(ServerPlayer player, StrifePortfolioData data)
	{
		int baseLimit = MinestuckConfig.SERVER.strifeCardMobDrops.get();
		if(baseLimit <= 0) return false;
		return data.getDroppedCards() < Math.max(baseLimit, Echeladder.get(player).getRung() / 6);
	}
	
	private static int getLootingLevel(ServerPlayer player, ServerLevel level)
	{
		return EnchantmentHelper.getEnchantmentLevel(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING), player);
	}
	
	private static List<KindAbstratusType> getKindsOf(ItemStack stack)
	{
		List<KindAbstratusType> result = new ArrayList<>();
		for(KindAbstratusType type : KindAbstratusList.getTypeList())
			if(!KindAbstratusList.HALF_SWORD.equals(type.getUnlocalizedName()) && type.partOf(stack))
				result.add(type);
		return result;
	}
	
	/**
	 * Creates a card of a random allowed kind (sometimes blank ones)
	 */
	public static ItemStack createRandomCard(ServerLevel level, RandomSource random)
	{
		List<KindAbstratusType> allowed = getAllowedKinds();
		
		// Blank cards are as likely as one additional kind
		int roll = random.nextInt(allowed.size() + 1);
		if(roll == allowed.size())
			return StrifePortfolioHandler.createStrifeCard(StrifeSpecibus.empty());
		
		KindAbstratusType kind = allowed.get(roll);
		StrifeSpecibus specibus = new StrifeSpecibus(kind.getUnlocalizedName());
		
		double contentChance = MinestuckConfig.SERVER.strifeCardContentChance.get();
		if(random.nextDouble() < contentChance)
		{
			int max = MinestuckConfig.SERVER.strifeCardMaxContents.get();
			int count = 1;
			while(count < max && random.nextDouble() < contentChance) count++;
			
			Set<Item> picked = new HashSet<>();
			for(int tries = 0; tries < count * 4 && picked.size() < count; tries++)
			{
				Item weapon = pickWeightedWeapon(level, kind, random);
				if(weapon != null && picked.add(weapon))
					specibus.putItemStack(new ItemStack(weapon));
			}
		}
		
		return StrifePortfolioHandler.createStrifeCard(specibus);
	}
	
	private static List<KindAbstratusType> getAllowedKinds()
	{
		List<? extends String> whitelist = MinestuckConfig.SERVER.strifeCardDropKinds.get();
		List<KindAbstratusType> result = new ArrayList<>();
		for(KindAbstratusType type : KindAbstratusList.getTypeList())
		{
			if(KindAbstratusList.HALF_SWORD.equals(type.getUnlocalizedName())) continue; // can only be reached by evolving
			if(whitelist.isEmpty() || whitelist.contains(type.getUnlocalizedName()))
				result.add(type);
		}
		return result;
	}
	
	/**
	 * Picks a weapon of the kind; The chance of a weapon is proportional to (grist value + 1)^-1.5, so the cheapest weapons
	 * are hundreds of times more common than the most expensive ones. Weapons without a grist cost are never picked!!!
	 */
	private static Item pickWeightedWeapon(ServerLevel level, KindAbstratusType kind, RandomSource random)
	{
		if(cachedFor != level.getRecipeManager())
		{
			CANDIDATE_CACHE.clear();
			cachedFor = level.getRecipeManager();
		}
		
		List<Candidate> candidates = CANDIDATE_CACHE.computeIfAbsent(kind.getUnlocalizedName(), name -> findCandidates(level, kind));
		if(candidates.isEmpty()) return null;
		
		double total = 0;
		for(Candidate candidate : candidates)
			total += candidate.weight();
		
		double roll = random.nextDouble() * total;
		for(Candidate candidate : candidates)
		{
			roll -= candidate.weight();
			if(roll <= 0) return candidate.item();
		}
		return candidates.get(candidates.size() - 1).item();
	}
	
	private static List<Candidate> findCandidates(ServerLevel level, KindAbstratusType kind)
	{
		List<Candidate> result = new ArrayList<>();
		for(Item item : BuiltInRegistries.ITEM)
		{
			ItemStack stack = new ItemStack(item);
			if(stack.isEmpty() || !kind.partOf(stack)) continue;
			
			GristSet cost = GristCostRecipe.findCostForItem(stack, null, false, level);
			if(cost == null) continue;
			
			double value = Math.max(0, cost.getValue());
			result.add(new Candidate(item, Math.pow(value + 1, -1.5)));
		}
		return result;
	}
}
