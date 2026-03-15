package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.block.MiniCruxtruderItem;
import com.mraof.minestuck.skaianet.SburbHandler;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.skaianet.SkaianetData;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.world.storage.MSExtraData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
@EventBusSubscriber(modid = Minestuck.MOD_ID, bus= EventBusSubscriber.Bus.GAME)
public final class DeployList
{
	public static final IAvailabilityCondition HAS_NOT_ENTERED = playerData -> !playerData.hasEntered();
	
	private static final ArrayList<DeployEntry> allList = new ArrayList<>();
	private static final ArrayList<DeployEntry> deployList = new ArrayList<>();
	private static final ArrayList<DeployEntry> atheneumList = new ArrayList<>();
	
	public enum EntryLists implements StringRepresentable
	{
		ALL {
			@Override
			public ArrayList<DeployEntry> getList() { return allList; }
		},
		DEPLOY {
			@Override
			public ArrayList<DeployEntry> getList() { return deployList; }
		},
		ATHENEUM {
			@Override
			public ArrayList<DeployEntry> getList() { return atheneumList; }
		};
		
		public ArrayList<DeployEntry> getList() { return new ArrayList<>(); }
		
		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
	
	public static void registerItems()
	{
		//Deployables
		registerItem("cruxtruder", new ItemStack(MSBlocks.CRUXTRUDER), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("totem_lathe", new ItemStack(MSBlocks.TOTEM_LATHE), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("artifact_card", GristSet.EMPTY, null, 0, HAS_NOT_ENTERED,
				(playerData, level) -> CaptchaCardItem.createPunchedCard(SburbHandler.getEntryItem(level, playerData).getItem()), EntryLists.DEPLOY);
		registerItem("alchemiter", new ItemStack(MSBlocks.ALCHEMITER), GristSet.EMPTY, GristTypes.BUILD.get().amount(100), 0, EntryLists.DEPLOY);
		registerItem("punch_designix", 0, null, item(MSBlocks.PUNCH_DESIGNIX),
				(isPrimary, playerData) -> playerData.getBaseGrist().amount(4), EntryLists.DEPLOY);
		registerItem("portable_cruxtruder", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines),
				(playerData, level) -> MiniCruxtruderItem.getCruxtruderWithColor(ColorHandler.getColorForPlayer(playerData.playerId(), level)), EntryLists.DEPLOY);
		registerItem("portable_punch_designix", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_PUNCH_DESIGNIX.get()), EntryLists.DEPLOY);
		registerItem("portable_totem_lathe", GristTypes.BUILD.get().amount(200), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_TOTEM_LATHE.get()), EntryLists.DEPLOY);
		registerItem("portable_alchemiter", GristTypes.BUILD.get().amount(300), 1, config(MinestuckConfig.SERVER.portableMachines), item(MSBlocks.MINI_ALCHEMITER.get()), EntryLists.DEPLOY);
		registerItem("holopad", new ItemStack(MSBlocks.HOLOPAD.get()), GristTypes.BUILD.get().amount(4000), 2, EntryLists.DEPLOY);
		registerItem("intellibeam_laserstation", new ItemStack(MSBlocks.INTELLIBEAM_LASERSTATION.get()), GristTypes.BUILD.get().amount(100000), 2, EntryLists.DEPLOY);
		registerItem("card_punched_card", GristTypes.BUILD.get().amount(25), null, 0, config(MinestuckConfig.SERVER.deployCard),
				(playerData, world) -> CaptchaCardItem.createPunchedCard(MSItems.CAPTCHA_CARD.get()), EntryLists.DEPLOY);
	}
	
	public static void registerItem(String name, ItemStack stack, GristSet.Immutable cost, int tier, EntryLists entryList)
	{
		registerItem(name, stack, cost, cost, tier, entryList);
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
	 * @param entryList Enum defining which list the item is in. (I.E. Deployables or Atheneum).
	 * You cannot directly register items to EntryLists.ALL, as it is simply a list of all entries, regardless of category.
	 */
	public static void registerItem(String name, ItemStack stack, GristSet.Immutable cost1, GristSet.Immutable cost2, int tier, EntryLists entryList)
	{
		registerItem(name, cost1, cost2, tier, null, (connection, world) -> stack, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet.Immutable cost, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, playerData) -> cost, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, GristSet.Immutable cost1, GristSet.Immutable cost2, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item, EntryLists entryList)
	{
		registerItem(name, tier, condition, item, (isPrimary, playerData) -> isPrimary ? cost1 : cost2, entryList);
	}
	
	/**
	 * Not thread-safe. Make sure to only call this on the main thread
	 */
	public static void registerItem(String name, int tier, IAvailabilityCondition condition,
									BiFunction<SburbPlayerData, Level, ItemStack> item,
									BiFunction<Boolean, SburbPlayerData, GristSet> grist, EntryLists entryList)
	{
		if(entryList == EntryLists.ALL)
			throw new IllegalArgumentException("Not allowed to add items to allList directly!");
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		entryList.getList().add(new DeployEntry(name, tier, condition, item, grist, entryList));
		allList.add(new DeployEntry(name, tier, condition, item, grist, entryList));
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbPlayerData playerData)
	{
		return getItemList(server, playerData, EntryLists.ALL);
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbPlayerData playerData, EntryLists entryList)
	{
		int tier = SburbHandler.availableTier(server, playerData.playerId());
		ArrayList<DeployEntry> itemList = new ArrayList<>();
		for(DeployEntry entry : entryList.getList())
			if(entry.isAvailable(playerData, tier))
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
		stack.applyComponents(stack.getItem().components());
		return stack;
	}
	
	public static boolean containsEntry(String name)
	{
		return containsEntry(name, EntryLists.ALL);
	}
	
	public static boolean containsEntry(String name, EntryLists entryList)
	{
		return getEntryForName(name, entryList) != null;
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbPlayerData playerData, Level level)
	{
		return containsItemStack(stack, playerData, level, EntryLists.ALL);
	}
	
	public static boolean containsItemStack(ItemStack stack, SburbPlayerData playerData, Level level, EntryLists entryList)
	{
		return getEntryForItem(stack, playerData, level, entryList) != null;
	}
	
	public static DeployEntry getEntryForName(String name)
	{
		return getEntryForName(name, EntryLists.ALL);
	}
	
	public static DeployEntry getEntryForName(String name, EntryLists entryList)
	{
		for(DeployEntry entry : entryList.getList())
			if(entry.getName().equals(name))
				return entry;
		return null;
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbPlayerData playerData, Level level)
	{
		return getEntryForItem(stack, playerData, level, EntryLists.ALL);
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbPlayerData playerData, Level level, EntryLists entryList)
	{
		stack = cleanStack(stack);
		for(DeployEntry entry : entryList.getList())
			if(ItemStack.matches(stack, entry.getItemStack(playerData, level)))
				return entry;
		return null;
	}
	
	
	public interface IAvailabilityCondition
	{
		boolean test(SburbPlayerData playerData);
	}
	
	public static IAvailabilityCondition config(ModConfigSpec.BooleanValue config)
	{
		return playerData -> config.get();
	}
	
	public static BiFunction<SburbPlayerData, Level, ItemStack> item(ItemLike item)
	{
		return (playerData, world) -> new ItemStack(item);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbPlayerData playerData)
	{
		return getDeployListTag(server, playerData, EntryLists.ALL);
	}
	
	static CompoundTag getDeployListTag(MinecraftServer server, SburbPlayerData playerData, EntryLists entryList)
	{
		CompoundTag nbt = new CompoundTag();
		ListTag tagList = new ListTag();
		nbt.put("l", tagList);
		int tier = SburbHandler.availableTier(server, playerData.playerId());
		for(int i = 0; i < entryList.getList().size(); i++)
		{
			DeployEntry entry = entryList.getList().get(i);
			entry.tryAddDeployTag(playerData, server.getLevel(Level.OVERWORLD), tier, tagList, i);
		}
		return nbt;
	}
	
	private static long lastDay;
	
	@SubscribeEvent
	public static void serverStarting(ServerStartingEvent event)
	{
		lastDay = event.getServer().overworld().getGameTime() / 24000L;
	}
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post event)
	{
		if(!MinestuckConfig.SERVER.hardMode.get())
		{
			MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
			long currentDay = server.overworld().getGameTime() / 24000L;
			if(currentDay != lastDay)
			{
				lastDay = currentDay;
				SkaianetData.get(server).allPlayerData().forEach(SburbPlayerData::resetGivenItems);
			}
		}
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
