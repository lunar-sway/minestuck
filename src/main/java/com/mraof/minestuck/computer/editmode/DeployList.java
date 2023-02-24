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
		
		registerItem("cruxtruder", new ItemStack(MSBlocks.CRUXTRUDER), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0);
		registerItem("totem_lathe", new ItemStack(MSBlocks.TOTEM_LATHE), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0);
		registerItem("artifact_card", new GristSet(), null, 0, HAS_NOT_ENTERED,
				(connection, level) -> AlchemyHelper.createCard(SburbHandler.getEntryItem(level, connection), true));
		registerItem("alchemiter", new ItemStack(MSBlocks.ALCHEMITER), new GristSet(), new GristSet(GristTypes.BUILD, 100), 0);
		registerItem("punch_designix", 0, null, item(MSBlocks.PUNCH_DESIGNIX),
				(isPrimary, connection) -> new GristSet(connection.getBaseGrist(), 4));
		registerItem("portable_cruxtruder", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines),
				(connection, level) -> MiniCruxtruderItem.getCruxtruderWithColor(ColorHandler.getColorForPlayer(connection.getClientIdentifier(), level)));
		registerItem("portable_punch_designix", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_PUNCH_DESIGNIX.get()));
		registerItem("portable_totem_lathe", new GristSet(GristTypes.BUILD, 200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_TOTEM_LATHE.get()));
		registerItem("portable_alchemiter", new GristSet(GristTypes.BUILD, 300), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_ALCHEMITER.get()));
		registerItem("holopad", new ItemStack(MSBlocks.HOLOPAD.get()), new GristSet(GristTypes.BUILD, 4000), 2);
		registerItem("card_punched_card", new GristSet(GristTypes.BUILD, 25), null, 0, config(MinestuckConfig.SERVER.deployCard), (sburbConnection, world) -> AlchemyHelper.createCard(new ItemStack(MSItems.CAPTCHA_CARD.get()), true));
		
	}
	
	public static void registerItem(String name, ItemStack stack, GristSet cost, int tier)
	{
		registerItem(name, stack, cost, cost, tier);
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
	 */
	public static void registerItem(String name, ItemStack stack, GristSet cost1, GristSet cost2, int tier)
	{
		registerItem(name, cost1, cost2, tier, null, (connection, world) -> stack);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet cost, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> cost);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet cost1, GristSet cost2, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> isPrimary ? cost1 : cost2);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, Level, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist)
	{
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		list.add(new DeployEntry(name, tier, condition, item, grist));
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