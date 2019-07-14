package com.mraof.minestuck.editmode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.item.block.MiniCruxtruderItem;
import com.mraof.minestuck.network.skaianet.SburbConnection;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.item.ItemStack;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class will be used to keep track of all deployable
 * items accessible by the server.
 * @author kirderf1
 */
public class DeployList
{
	
	private static final ArrayList<DeployEntry> list = new ArrayList<>();
	
	public static void registerItems()
	{
		
		registerItem("cruxtruder", new ItemStack(MinestuckBlocks.CRUXTRUDER), new GristSet(), new GristSet(GristType.BUILD, 100), 0);
		registerItem("totem_lathe", new ItemStack(MinestuckBlocks.TOTEM_LATHE), new GristSet(), new GristSet(GristType.BUILD, 100), 0);
		registerItem("artifact_card", new GristSet(), null, 0, connection -> !connection.hasEntered(),
				(connection, world) -> AlchemyRecipes.createCard(SburbHandler.getEntryItem(world, connection), true));
		registerItem("alchemiter", new ItemStack(MinestuckBlocks.ALCHEMITER), new GristSet(), new GristSet(GristType.BUILD, 100), 0);
		registerItem("punch_designix", 0,null, (connection, world) -> new ItemStack(MinestuckBlocks.PUNCH_DESIGNIX),
				(isPrimary, connection) -> new GristSet(SburbHandler.getPrimaryGristType(connection.getClientIdentifier()), 4));
		/*registerItem("jumper_block_extension", new ItemStack(MinestuckBlocks.jumperBlockExtension[0]), new GristSet(GristType.Build, 1000), 1);
		registerItem("punch_card_shunt", new ItemStack(MinestuckItems.shunt), new GristSet(GristType.Build, 100), 1);
		registerItem("holopad", new ItemStack(MinestuckBlocks.holopad), new GristSet(GristType.Build, 10000), 2);*/
	}
	
	public static void registerItem(String name, ItemStack stack, GristSet cost, int tier)
	{
		registerItem(name, stack, cost, cost, tier);
	}
	
	/**
	 * Register the specific item as deployable.
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
	
	public static void registerItem(String name, GristSet cost, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, World, ItemStack> item)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> cost);
	}
	
	public static void registerItem(String name, GristSet cost1, GristSet cost2, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, World, ItemStack> item)
	{
		registerItem(name, tier, condition, item, (isPrimary, connection) -> isPrimary ? cost1 : cost2);
	}
	
	public static void registerItem(String name, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, World, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist)
	{
		if(containsEntry(name))
			throw new IllegalStateException("Item stack already added to the deploy list: "+name);
		list.add(new DeployEntry(name, tier, condition, item, grist));
	}
	
	public static void removeEntry(String name)
	{
		Iterator<DeployEntry> iterator = list.iterator();
		while(iterator.hasNext())
			if(iterator.next().name.equals(name))
			{
				iterator.remove();
				return;
			}
	}
	
	public static List<DeployEntry> getItemList(MinecraftServer server, SburbConnection c)
	{
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		ArrayList<DeployEntry> itemList = new ArrayList<>();
		for(DeployEntry entry : list)
			if(entry.tier <= tier && (entry.condition == null || entry.condition.test(c)))
				itemList.add(entry);
		
		return itemList;
	}
	
	@Nonnull
	private static ItemStack cleanStack(ItemStack stack)
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
	
	public static boolean containsItemStack(ItemStack stack, SburbConnection c, World world)
	{
		return getEntryForItem(stack, c, world) != null;
	}
	
	public static int getOrdinal(String name)
	{
		for(int i = 0; i < list.size(); i++)
			if(list.get(i).name.equals(name))
				return i;
		return -1;
	}
	
	public static int getOrdinal(ItemStack stack, SburbConnection c, World world)
	{
		stack = cleanStack(stack);
		for(int i = 0; i < list.size(); i++)
			if(ItemStack.areItemStacksEqual(list.get(i).item.apply(c, world), stack))
				return i;
		return -1;
	}
	
	public static String[] getNameList()
	{
		String[] str = new String[list.size()];
		for(int i = 0; i < list.size(); i++)
			str[i] = list.get(i).getName();
		return str;
	}
	
	public static int getEntryCount()
	{
		return list.size();
	}
	
	public static void applyConfigValues(boolean[] booleans)
	{
		if(booleans[0] != containsEntry("card_punched_card"))
		{
			if(booleans[0])
				registerItem("card_punched_card", AlchemyRecipes.createCard(new ItemStack(MinestuckItems.CAPTCHA_CARD), true), new GristSet(GristType.BUILD, 25), null, 0);
			else removeEntry("card_punched_card");
		}
		if(booleans[1] != containsEntry("portable_cruxtuder"))
		{
			if(booleans[1])
			{
				registerItem("portable_cruxtruder", new GristSet(GristType.BUILD, 200), 1, null,
						(connection, world) -> MiniCruxtruderItem.getCruxtruderWithColor(PlayerSavedData.get(world).getData(connection.getClientIdentifier()).color));
				registerItem("portable_punch_designix", new ItemStack(MinestuckBlocks.MINI_PUNCH_DESIGNIX), new GristSet(GristType.BUILD, 200), 1);
				registerItem("portable_totem_lathe", new ItemStack(MinestuckBlocks.MINI_TOTEM_LATHE), new GristSet(GristType.BUILD, 200), 1);
				registerItem("portable_alchemiter", new ItemStack(MinestuckBlocks.MINI_ALCHEMITER), new GristSet(GristType.BUILD, 300), 1);
			} else
			{
				removeEntry("portable_cruxtruder");
				removeEntry("portable_totem_lathe");
				removeEntry("portable_alchemiter");
				removeEntry("portable_punch_designix");
			}
		}
	}
	
	public static DeployEntry getEntryForName(String name)
	{
		for(DeployEntry entry : list)
			if(entry.name.equals(name))
				return entry;
		return null;
	}
	
	public static DeployEntry getEntryForItem(ItemStack stack, SburbConnection c, World world)
	{
		stack = cleanStack(stack);
		for(DeployEntry entry : list)
			if(ItemStack.areItemStacksEqual(stack, entry.item.apply(c, world)))
				return entry;
		return null;
	}
	
	public static class DeployEntry
	{
		private String name;
		
		private int tier;
		private IAvailabilityCondition condition;
		private BiFunction<SburbConnection, World, ItemStack> item;
		private BiFunction<Boolean, SburbConnection, GristSet> grist;
		
		private DeployEntry(String name, int tier, IAvailabilityCondition condition, BiFunction<SburbConnection, World, ItemStack> item, BiFunction<Boolean, SburbConnection, GristSet> grist)
		{
			this.name  = name;
			this.tier = tier;
			this.condition = condition;
			this.item = item;
			this.grist = grist;
		}
		
		public String getName()
		{
			return name;
		}
		
		public int getTier()
		{
			return tier;
		}
		
		public boolean isAvailable(SburbConnection c, int tier)
		{
			return (condition == null || condition.test(c)) && this.tier <= tier;
		}
		
		public int getOrdinal()
		{
			return list.indexOf(this);
		}
		
		public ItemStack getItemStack(SburbConnection c, World world)
		{
			return item.apply(c, world).copy();
		}
		
		public GristSet getPrimaryGristCost(SburbConnection c)
		{
			return grist.apply(true, c);
		}
		
		public GristSet getSecondaryGristCost(SburbConnection c)
		{
			return grist.apply(false, c);
		}
	}
	
	public interface IAvailabilityCondition
	{
		boolean test(SburbConnection connection);
	}
	static NBTTagCompound getDeployListTag(MinecraftServer server, SburbConnection c)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		nbt.put("l", tagList);
		int tier = SburbHandler.availableTier(server, c.getClientIdentifier());
		for(int i = 0; i < list.size(); i++)
		{
			DeployEntry entry = list.get(i);
			if(entry.isAvailable(c, tier))
			{
				ItemStack stack = entry.getItemStack(c, server.getWorld(DimensionType.OVERWORLD));
				GristSet primary = entry.getPrimaryGristCost(c);
				GristSet secondary = entry.getSecondaryGristCost(c);
				NBTTagCompound tag = new NBTTagCompound();
				stack.write(tag);
				tag.putInt("i", i);
				NBTTagList listPrimary = new NBTTagList();
				for (GristType type : GristType.values())
				{
					if(primary.getGrist(type) == 0)
						continue;
					NBTTagCompound gristTag = new NBTTagCompound();
					gristTag.putString("id", String.valueOf(type.getRegistryName()));
					gristTag.putInt("amount", primary.getGrist(type));
					listPrimary.add(gristTag);
				}
				tag.put("primary", listPrimary);
				if(secondary != null)
				{
					NBTTagList listSecondary = new NBTTagList();
					for(GristType type : GristType.values())
					{
						if(secondary.getGrist(type) == 0)
							continue;
						NBTTagCompound gristTag = new NBTTagCompound();
						gristTag.putString("id", String.valueOf(type.getRegistryName()));
						gristTag.putInt("amount", secondary.getGrist(type));
						listSecondary.add(gristTag);
					}
					tag.put("secondary", listSecondary);
				}
				tagList.add(tag);
			}
		}
		return nbt;
	}
	
	//Clientside
	
	@OnlyIn(Dist.CLIENT)
	static void loadClientDeployList(NBTTagCompound nbt)
	{
		if(clientDeployList == null)
			clientDeployList = new ArrayList<>();
		else clientDeployList.clear();
		NBTTagList list = nbt.getList("l", 10);
		for(int i = 0; i < list.size(); i++)
		{
			NBTTagCompound tag = list.getCompound(i);
			ClientDeployEntry entry = new ClientDeployEntry();
			entry.item = ItemStack.read(tag);
			entry.index = tag.getInt("i");
			entry.cost1 = new GristSet();
			for (INBTBase nbtBase : tag.getList("primary", 10))
			{
				NBTTagCompound gristTag = (NBTTagCompound) nbtBase;
				GristType type = GristType.getTypeFromString(gristTag.getString("id"));
				if(type != null)
					entry.cost1.setGrist(type, gristTag.getInt("amount"));
			}
			if(tag.contains("secondary", 9))
			{
				entry.cost2 = new GristSet();
				for(INBTBase nbtBase : tag.getList("secondary", 10))
				{
					NBTTagCompound gristTag = (NBTTagCompound) nbtBase;
					GristType type = GristType.getTypeFromString(gristTag.getString("id"));
					if(type != null)
						entry.cost2.setGrist(type, gristTag.getInt("amount"));
				}
			} else entry.cost2 = null;
			clientDeployList.add(entry);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	private static List<ClientDeployEntry> clientDeployList;
	
	@OnlyIn(Dist.CLIENT)
	public static ClientDeployEntry getEntryClient(ItemStack stack)
	{
		stack = cleanStack(stack);
		for(ClientDeployEntry entry : clientDeployList)
			if(ItemStack.areItemStacksEqual(entry.item, stack))
				return entry;
		return null;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class ClientDeployEntry
	{
		private ItemStack item;
		private GristSet cost1;
		private GristSet cost2;
		private int index;
		
		public GristSet getPrimaryCost()
		{
			return cost1;
		}
		
		public GristSet getSecondaryCost()
		{
			return cost2;
		}
		
		public int getIndex()
		{
			return index;
		}
	}
}