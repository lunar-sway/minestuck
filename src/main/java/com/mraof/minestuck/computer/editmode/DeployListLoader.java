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
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import net.minecraft.resources.ResourceLocation;
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
		
		for(Map.Entry<ResourceLocation, JsonElement> entry : jsonEntries.entrySet())
		{
			DeployDataEntry.LIST_CODEC.parse(JsonOps.INSTANCE, entry.getValue())
					.resultOrPartial(message -> LOGGER.error("Couldn't entirely parse deploylist entry {}: {}", entry.getKey(), message))
					.ifPresent(list -> {
						for(int i = 0; i < list.size(); i++)
						{
							DeployDataEntry data = list.get(i);
							String name = entry.getKey() + "-" + i;
							added_with_datapack.add(name);
							
							DeployList.registerItem(name, data.tier, entryCondition(data), entryItemStack(data), entryGristSet(data), data.category);
						}
					});
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
		return d -> {
			// Will default to the item's cost
			if(entry.cost.isEmpty()) return true;
			for(GristCost cost : entry.cost)
			{
				if(cost.test(this.getContext())) return true;
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
			for(GristCost cost : entry.cost)
			{
				if(cost.test(this.getContext()))
				{
					if(cost.grist.isPresent())
					{
						set.add(cost.grist.get());
					}
					if(cost.primary != 0)
					{
						set.add(playerData.getBaseGrist(), cost.primary);
					}
				}
			}
			return set.asImmutable();
		};
	}
	
	public record GristCost(Optional<GristSet.Immutable> grist, int primary, List<ICondition> conditions)
	{
		public static final Codec<GristCost> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				GristSet.Codecs.MAP_CODEC.optionalFieldOf("grist").forGetter(GristCost::grist),
				Codec.INT.optionalFieldOf("primary_grist", 0).forGetter(GristCost::primary),
				ICondition.LIST_CODEC.optionalFieldOf("conditions", List.of()).forGetter(GristCost::conditions)
		).apply(instance, GristCost::new));
		public static final Codec<List<GristCost>> LIST_CODEC = CODEC.listOf();
		
		public boolean test(ICondition.IContext context)
		{
			for(ICondition condition : conditions)
			{
				if(!condition.test(context)) return false;
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
	}
}
