package com.mraof.minestuck.computer.editmode;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.computer.editmode.DeployList.EntryLists;
import com.mraof.minestuck.computer.editmode.DeployList.IAvailabilityCondition;
import com.mraof.minestuck.entity.dialogue.condition.Condition;
import com.mraof.minestuck.entity.dialogue.condition.Condition.PlayerOnlyCondition;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * This class deals with loading the deploylist from datapacks
 * <p>
 * FIXME grist costs are only updated on rejoin
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
@ParametersAreNonnullByDefault
public class DeployListLoader extends SimpleJsonResourceReloadListener
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final List<String> added_with_datapack = new ArrayList<>();
	
	@SubscribeEvent
	private static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(new DeployListLoader());
	}
	
	public DeployListLoader()
	{
		super(new GsonBuilder().create(), "minestuck/deploy_list");
	}
	
	@Override
	protected void apply(Map<ResourceLocation, JsonElement> jsonEntries, ResourceManager resourceManager,
						 ProfilerFiller profiler)
	{
		this.clearAddedEntries();
		
		List<Map.Entry<ResourceLocation, DeployDataList>> entries = new ArrayList<>();
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			DeployDataList.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
					.resultOrPartial(message -> LOGGER.error("Couldn't entirely parse deploylist entry {}: {}", entry.getKey(), message))
					.ifPresent(list -> entries.add(Map.entry(entry.getKey(), list)));
		}
		
		entries.sort((left, right) -> -Float.compare(left.getValue().priority, right.getValue().priority));
		
		for(Map.Entry<ResourceLocation, DeployDataList> entry : entries)
		{
			String baseName = entry.getKey().toString() + "-";
			List<DeployDataEntry> list = entry.getValue().entries;
			for(int i = 0; i < list.size(); i++)
			{
				DeployDataEntry data = list.get(i);
				String name = baseName + i;
				added_with_datapack.add(name);
				
				DeployList.registerItem(name, data.tier, entryCondition(data), entryItemStack(data), entryGristSet(data), data.category);
			}
		}
	}
	
	private void clearAddedEntries()
	{
		for(EntryLists list : EntryLists.values())
		{
			list.getList().removeIf(entry -> added_with_datapack.contains(entry.getName()));
		}
		added_with_datapack.clear();
	}
	
	private IAvailabilityCondition entryCondition(DeployDataEntry entry)
	{
		return playerData -> {
			// Will default to the item's cost
			if(entry.cost.isEmpty()) return true;
			
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(server == null) return false;
			ServerPlayer player = playerData.playerId().getPlayer(server);
			if(player == null) return false;
			
			for(GristCost cost : entry.cost)
			{
				if(cost.test(this.getContext(), player)) return true;
			}
			return false;
		};
	}
	
	private BiFunction<SburbPlayerData, Level, ItemStack> entryItemStack(DeployDataEntry entry)
	{
		return (data, level) -> {
			ItemStack stack = entry.stack.copy();
			stack.setCount(1);
			if(entry.punched)
			{
				stack = CaptchaCardItem.createPunchedCard(stack.getItem());
			}
			return stack;
		};
	}
	
	private BiFunction<Boolean, SburbPlayerData, GristSet> entryGristSet(DeployDataEntry entry)
	{
		return (primary, playerData) -> {
			MutableGristSet set = MutableGristSet.newDefault();
			if(entry.cost.isEmpty())
			{
				ItemStack stack = entry.stack.copy();
				stack.setCount(1);
				set = GristCostRecipe.findCostForItem(stack, null, false, ServerLifecycleHooks.getCurrentServer().overworld()).mutableCopy();
			}
			
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			if(server == null) return set.asImmutable();
			ServerPlayer player = playerData.playerId().getPlayer(server);
			if(player == null) return set.asImmutable();
			
			for(GristCost cost : entry.cost)
			{
				if(cost.test(this.getContext(), player))
				{
					if(cost.grist.isPresent())
					{
						set.add(cost.grist.get());
					}
					if(cost.primary != 0)
					{
						set.add(playerData.getBaseGrist(), cost.primary);
					}
					break;
				}
			}
			return set.asImmutable();
		};
	}
	
	public record GristCost(Optional<GristSet.Immutable> grist, int primary,
							List<ICondition> neoforgeConditions, List<PlayerOnlyCondition> clientConditions)
	{
		public static final Codec<GristCost> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				GristSet.Codecs.MAP_CODEC.optionalFieldOf("grist").forGetter(GristCost::grist),
				Codec.INT.optionalFieldOf("primary_grist", 0).forGetter(GristCost::primary),
				// Makes the difference between neoforge conditions and client conditions clear
				ICondition.LIST_CODEC.optionalFieldOf("neoforge:conditions", List.of()).forGetter(GristCost::neoforgeConditions),
				Condition.PLAYER_ONLY_CODEC.listOf().optionalFieldOf("client_conditions", List.of()).forGetter(GristCost::clientConditions)
		).apply(instance, GristCost::new));
		public static final Codec<List<GristCost>> LIST_CODEC = CODEC.listOf();
		
		public GristCost(GristSet grist, int primary, List<ICondition> conditions)
		{
			this(Optional.of(grist.asImmutable()), primary, conditions, List.of());
		}
		
		public GristCost(GristSet grist)
		{
			this(Optional.of(grist.asImmutable()), 0, List.of(), List.of());
		}
		
		public boolean test(ICondition.IContext context, ServerPlayer player)
		{
			for(ICondition condition : neoforgeConditions)
			{
				if(!condition.test(context)) return false;
			}
			for(PlayerOnlyCondition condition : clientConditions)
			{
				if(!condition.test(player)) return false;
			}
			return true;
		}
	}
	
	public record DeployDataEntry(ItemStack stack, int tier, DeployList.EntryLists category,
								  List<GristCost> cost, boolean punched)
	{
		public static final Codec<DeployDataEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.CODEC.fieldOf("item").validate(stack -> {
					if(!stack.isComponentsPatchEmpty())
						return DataResult.error(() -> "Deploylist item does not support additional components");
					return DataResult.success(stack);
				}).forGetter(DeployDataEntry::stack),
				Codec.INT.optionalFieldOf("tier", 0).forGetter(DeployDataEntry::tier),
				StringRepresentable.fromValues(EntryLists::values).optionalFieldOf("category", DeployList.EntryLists.ATHENEUM)
						.validate(entry -> entry == EntryLists.ALL ? DataResult.error(() -> "Cannot add a deploylist entry to all") : DataResult.success(entry))
						.forGetter(DeployDataEntry::category),
				GristCost.LIST_CODEC.optionalFieldOf("cost", List.of()).forGetter(DeployDataEntry::cost),
				Codec.BOOL.optionalFieldOf("punched", false).forGetter(DeployDataEntry::punched)
		).apply(instance, DeployDataEntry::new));
		public static final Codec<List<DeployDataEntry>> LIST_CODEC = CODEC.listOf();
		
		public DeployDataEntry(ItemStack stack, int tier, DeployList.EntryLists category, GristCost cost, boolean punched)
		{
			this(stack, tier, category, List.of(cost), punched);
		}
	}
	
	/**
	 * @param priority Priority for adding to the atheneum
	 *                 Higher priority means it's placed earlier
	 *                 Float allows for datapacks to add more items inbetween (e.g. new stained glass from dye mods)
	 * @param entries
	 */
	public record DeployDataList(float priority, List<DeployDataEntry> entries)
	{
		public static final Codec<DeployDataList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.FLOAT.optionalFieldOf("priority", 0F).forGetter(DeployDataList::priority),
				DeployDataEntry.LIST_CODEC.fieldOf("entries").forGetter(DeployDataList::entries)
		).apply(instance, DeployDataList::new));
		
		public DeployDataList(float priority, DeployDataEntry... entries)
		{
			this(priority, List.of(entries));
		}
	}
}
