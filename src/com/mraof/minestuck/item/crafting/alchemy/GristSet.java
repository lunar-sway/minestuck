package com.mraof.minestuck.item.crafting.alchemy;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.ExtraJSONUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class GristSet
{

	private final Map<GristType, Long> gristTypes;

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public GristSet()
	{
		this.gristTypes = new TreeMap<>();
	}

	private GristSet(Map<GristType, Long> map)
	{
		this.gristTypes = map;
	}
	
	public static GristSet immutable(ImmutableMap<GristType, Long> map)	//Hopefully this doesn't break sorting
	{
		return new GristSet(map);
	}
	
	public GristSet asImmutable()
	{
		if(gristTypes instanceof ImmutableMap)
			return this;
		else return immutable(ImmutableMap.copyOf(this.gristTypes));
	}

	/**
	 * Creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public GristSet(GristType type, long amount)
	{
		this();
		this.gristTypes.put(type, amount);
	}

	/**
	 * Creates a set of grist values with multiple grist/amount pairs. used in setting up the Grist Registry.
	 */
	public GristSet(GristType[] type, long[] amount)
	{
		this();

		for (int i = 0; i < type.length; i++)
		{
			this.gristTypes.put(type[i], amount[i]);
		}
	}

	public GristSet(GristAmount... grist)
	{
		this();
		for (GristAmount amount : grist)
		{
			this.gristTypes.put(amount.getType(), amount.getAmount());
		}
	}

	/**
	 * Gets the amount of grist, given a type of grist.
	 */
	public long getGrist(GristType type)
	{
		return this.gristTypes.getOrDefault(type, 0L);
	}
	
	/**
	 * @return a value estimate for this grist set
	 */
	public float getValue()	//TODO potentially duplicate code here, in GristHelper.getGristValue and in AlchemyRecipes.onAlchemizedItem
	{
		float sum = 0;
		for(GristAmount amount : getArray())
			sum += amount.getValue();
		return sum;
	}
	
	/**
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public GristSet setGrist(GristType type, long amount)
	{
		if(type != null)
			gristTypes.put(type, amount);
		return this;
	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristType type, long amount)
	{
		if (amount == 0)
		{
			this.gristTypes.remove(type);
		}
		else
		{
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
		}
		return this;
	}

	/**
	 * Returns a Hashtable with grist->amount pairs.
	 */
	public Map<GristType, Long> getMap()
	{
		return this.gristTypes;
	}

	/**
	 * Returns a ArrayList containing GristAmount objects representing the set.
	 */
	public List<GristAmount> getArray()
	{
		return this.gristTypes.entrySet().stream().map((entry) -> new GristAmount(entry.getKey(), entry.getValue())).collect(Collectors.toList());
	}

	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public GristSet addGrist(GristSet set)
	{
		for (GristAmount grist : set.getArray())
		{
			this.addGrist(grist);
		}
		return this;

	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristAmount grist)
	{
		this.addGrist(grist.getType(), grist.getAmount());
		return this;
	}
	
	public GristSet scale(int scale)
	{
		return scale(scale, true);
	}
	
	/**
	 * Multiplies all the grist amounts by a factor.
	 */
	public GristSet scale(float scale, boolean roundDown)
	{

		this.gristTypes.forEach((type, amount) -> {
			if (amount > 0)
			{
				this.gristTypes.put(type, roundDown ? (long) (amount * scale) : Math.max(Math.round(amount * scale), 1));
			}
		});

		return this;
	}

	/**
	 * Checks if this grist set is empty.
	 */
	public boolean isEmpty()
	{
		return this.gristTypes.isEmpty();
	}

	@Override
	public String toString()
	{
		StringBuilder build = new StringBuilder();
		build.append("gristSet:[");

		boolean first = true;
		for (Map.Entry<GristType, Long> entry : gristTypes.entrySet())
		{
			if (!first)
				build.append(',');
			build.append(entry.getKey().getRegistryName()).append("=").append(entry.getValue());
			first = false;
		}

		build.append(']');
		return build.toString();
	}

	public GristSet copy()
	{
		return new GristSet(new TreeMap<>(gristTypes));
	}
	
	public JsonElement serialize()
	{
		JsonObject json = new JsonObject();
		for(Map.Entry<GristType, Long> entry : gristTypes.entrySet())
		{
			ResourceLocation id = entry.getKey().getRegistryName();
			if(id == null)
			{
				Debug.warnf("Found grist type without a registry name! (%s)", entry.getKey());
				continue;
			}
			json.addProperty(id.toString(), entry.getValue());
		}
		return json;
	}
	
	public static GristSet deserialize(JsonObject json)
	{
		GristSet set = new GristSet();
		for(Map.Entry<String, JsonElement> entry : json.entrySet())
		{
			ResourceLocation gristId = new ResourceLocation(entry.getKey());
			GristType type = GristType.REGISTRY.getValue(gristId);
			if(type == null)
				throw new JsonParseException("'"+entry.getKey()+"' did not match an existing grist type!");
			long amount = ExtraJSONUtils.getLong(entry.getValue(), entry.getKey());
			set.addGrist(type, amount);
		}
		
		return set;
	}
	
	public void write(PacketBuffer buffer)
	{
		List<GristAmount> amounts = getArray();
		buffer.writeInt(amounts.size());
		amounts.forEach(gristAmount -> gristAmount.write(buffer));
	}
	
	public static GristSet read(PacketBuffer buffer)
	{
		int size = buffer.readInt();
		GristAmount[] amounts = new GristAmount[size];
		for(int i = 0; i < size; i++)
			amounts[i] = GristAmount.read(buffer);
		
		return new GristSet(amounts);
	}
}