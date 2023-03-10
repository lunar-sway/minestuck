package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MiniCruxtruderItem;
import com.mraof.minestuck.alchemy.AlchemyHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristTypes;
import com.mraof.minestuck.skaianet.SburbConnection;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.MSExtraData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
public final class DeployList
{
	public static final IAvailabilityCondition HAS_NOT_ENTERED = connection -> !connection.hasEntered();
	
	private static final ArrayList<DeployEntry> list = new ArrayList<>();
	
	public static void registerItems()
	{
		//Deployables
		registerItem("cruxtruder", new ItemStack(MSBlocks.CRUXTRUDER), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0, false);
		registerItem("totem_lathe", new ItemStack(MSBlocks.TOTEM_LATHE), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0, false);
		registerItem("artifact_card", new GristSet(), null, 0, HAS_NOT_ENTERED,
				(connection, level) -> AlchemyHelper.createCard(SburbHandler.getEntryItem(level, connection), true), false);
		registerItem("alchemiter", new ItemStack(MSBlocks.ALCHEMITER), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0, false);
		registerItem("punch_designix", 0, null, item(MSBlocks.PUNCH_DESIGNIX),
				(isPrimary, connection) -> new GristSet(connection.getBaseGrist(), 4), false);
		registerItem("portable_cruxtruder", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines),
				(connection, level) -> MiniCruxtruderItem.getCruxtruderWithColor(ColorHandler.getColorForPlayer(connection.getClientIdentifier(), level)), false);
		registerItem("portable_punch_designix", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_PUNCH_DESIGNIX.get()), false);
		registerItem("portable_totem_lathe", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_TOTEM_LATHE.get()), false);
		registerItem("portable_alchemiter", new GristSet(GristTypes.BUILD, 300), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_ALCHEMITER.get()), false);
		registerItem("holopad", new ItemStack(MSBlocks.HOLOPAD.get()), new GristSet(GristTypes.BUILD, 4000), 2, false);
		registerItem("card_punched_card", new GristSet(GristTypes.BUILD, 25), null, 0, config(MinestuckConfig.SERVER.deployCard), (sburbConnection, world) -> AlchemyHelper.createCard(new ItemStack(MSItems.CAPTCHA_CARD.get()), true), false);
		
		//Atheneum
		registerItem("stone", new ItemStack(Blocks.STONE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("cobblestone", new ItemStack(Blocks.COBBLESTONE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("oak_planks", new ItemStack(Blocks.OAK_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("oak_slab", new ItemStack(Blocks.OAK_SLAB), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("oak_stairs", new ItemStack(Blocks.OAK_STAIRS), new GristSet(GristTypes.BUILD, 2), 0, true);
		registerItem("oak_fence", new ItemStack(Blocks.OAK_FENCE), new GristSet(GristTypes.BUILD, 2), 0, true);
		registerItem("oak_log", new ItemStack(Blocks.OAK_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("ladder", new ItemStack(Blocks.LADDER), new GristSet(GristTypes.BUILD, 16), 0, true);
		registerItem("birch_planks", new ItemStack(Blocks.BIRCH_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("spruce_planks", new ItemStack(Blocks.SPRUCE_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("dark_oak_planks", new ItemStack(Blocks.DARK_OAK_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("acacia_planks", new ItemStack(Blocks.ACACIA_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("jungle_planks", new ItemStack(Blocks.JUNGLE_PLANKS), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("birch_log", new ItemStack(Blocks.BIRCH_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("spruce_log", new ItemStack(Blocks.SPRUCE_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("dark_oak_log", new ItemStack(Blocks.DARK_OAK_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("acacia_log", new ItemStack(Blocks.ACACIA_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("jungle_log", new ItemStack(Blocks.JUNGLE_LOG), new GristSet(GristTypes.BUILD, 4), 0, true);
		registerItem("andesite", new ItemStack(Blocks.ANDESITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("diorite", new ItemStack(Blocks.DIORITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("granite", new ItemStack(Blocks.GRANITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("polished_andesite", new ItemStack(Blocks.POLISHED_ANDESITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("polished_diorite", new ItemStack(Blocks.POLISHED_DIORITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		registerItem("polished_granite", new ItemStack(Blocks.POLISHED_GRANITE), new GristSet(GristTypes.BUILD, 1), 0, true);
		
	}
	
	public static void registerItem(String name, ItemStack stack, GristSet cost, int tier, boolean atheneum)
	{
		registerItem(name, stack, cost, cost, tier, atheneum);
	}
	
	/**
	 * Register the specific item as deployable.
	 * Note: Not thread-safe. Make sure to only call this on the main thread
	 * @param stack The item to be registered.
	 * The itemstack can have nbt tags, with the exception of the display tag.
	 * @param cost1 How much it costs the first time deployed.
	 * @param cost2 How much it costs after the first times. Null if only deployable once.
	 * First cost will always be used when not in hardmode.
	 * @param tier The tier of the item; what connection position required in an unfinished chain to deploy.
	 * All will be available to all players when the chain is complete.
	 * @param atheneum Whether the item is a deployable (ex: sburb machines and such) or an atheneum item (ex: basic building blocks like oak planks or stone).
	 */
	public static void registerItem(String name, ItemStack stack, GristSet cost1, GristSet cost2, int tier, boolean atheneum)
	{
		registerItem(name, cost1, cost2, tier, null, (connection, world) -> stack, atheneum);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet cost, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, boolean atheneum)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> cost, atheneum);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet cost1, GristSet cost2, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, boolean atheneum)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> isPrimary ? cost1 : cost2, atheneum);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist, boolean atheneum)
	{
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		list.add(new DeployEntry(name, tier, condition, item, grist, atheneum));
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbConnection c)
	{
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		ArrayList<DeployEntry> itemList = new ArrayList<>();
		for(DeployEntry entry : list)
			if(entry.isAvailable(c, tier))
				itemList.add(entry);
		
		return itemList;
	}
	
	@Nonnull
	static ItemStack cleanStack(ItemStack stack)
	{
		if(stack.isEmpty())
			return ItemStack.EMPTY;
		stack = stack.copy();
		stack.setCount(1);
		if(stack.hasTag() && stack.getTag().isEmpty())
			stack.setTag(null);
		return stack;
	}
	
	public static boolean containsEntry(String name)
	{
		return getEntryForName(name) != null;
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbConnection c, Level level)
	{
		return getEntryForItem(stack, c, level) != null;
	}
	
	public static DeployEntry getEntryForName(String name)
	{
		for(DeployEntry entry : list)
			if(entry.getName().equals(name))
				return entry;
		return null;
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbConnection c, Level level)
	{
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.matches(stack, entry.getItemStack(c, level)))
				return entry;
		return null;
	}
	
	
	public interface IAvailabilityCondition
	{
		boolean test(SburbConnection connection);
	}
	
	public static IAvailabilityCondition config(ForgeConfigSpec.BooleanValue config)
	{
		return connection -> config.get();
	}
	
	public static BiFunction<SburbConnection, Level, ItemStack> item(ItemLike item)
	{
		return (sburbConnection, world) -> new ItemStack(item);
	}
	
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbConnection c)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag tagList = new ListTag();
		nbt.put("l", tagList);
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		for(int i = 0; i < list.size(); i++)
		{
			DeployEntry entry = list.get(i);
			entry.tryAddDeployTag(c, server.getLevel(Level.OVERWORLD), tier, tagList, i);
		}
		return nbt;
	}
	
	/**
	 * Should be called any time that the conditions of deploy list entries might have changed for players.
	 */
	public static void onConditionsUpdated(MinecraftServer server)
	{
		MSExtraData.get(server).forEach(EditData::sendGivenItemsToEditor);
	}
	
	public static List<ItemStack> getEditmodeTools()
	{
		return Collections.emptyList();
	}
}