package com.mraof.minestuck.item.crafting.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.ExtraJSONUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GristSet
{
	public static final String MISSING_MESSAGE = "grist.missing";
	public static final String GRIST_COMMA = "grist.comma";
	
	public static final GristSet EMPTY = new GristSet(Collections.emptyMap());

	private final Map<GristType, Long> gristTypes;

	/**
	 * Creates a blank set of grist values, used in setting up the Grist Registry.
	 */
	public GristSet()
	{
		this(new TreeMap<>());
	}
	
	protected GristSet(Map<GristType, Long> map)
	{
		this.gristTypes = map;
	}
	
	public GristSet(Supplier<GristType> type, long amount)
	{
		this(type.get(), amount);
	}
	
	/**
	 * Creates a set of grist values with one grist/amount pair. used in setting up the Grist Registry.
	 */
	public GristSet(GristType type, long amount)
	{
		this();
		this.gristTypes.put(type, amount);
	}
	
	public GristSet(Supplier<GristType>[] type, long[] amount)
	{
		this();
		
		for (int i = 0; i < type.length; i++)
		{
			this.gristTypes.put(type[i].get(), amount[i]);
		}
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
	
	public GristSet(GristSet set)
	{
		this();
		gristTypes.putAll(set.gristTypes);
	}
	
	public ImmutableGristSet asImmutable()
	{
		return new ImmutableGristSet(this);
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
	public double getValue()
	{
		double sum = 0;
		for(GristAmount amount : getAmounts())
			sum += amount.getValue();
		return sum;
	}
	
	/**
	 * Sets the amount of grist, given a type of grist and the new amount.
	 */
	public GristSet setGrist(GristType type, long amount)
	{
		if(type != null)
		{
			if (amount == 0)
			{
				this.gristTypes.remove(type);
			}
			else
			{
				gristTypes.put(type, amount);
			}
		}
		return this;
	}

	/**
	 * Adds an amount of grist to a GristSet, given a grist type and amount.
	 */
	public GristSet addGrist(GristType type, long amount)
	{
		if(type != null)
		{
			this.gristTypes.compute(type, (key, value) -> value == null ? amount : value + amount);
		}
		return this;
	}

	/**
	 * Returns a Hashtable with grist->amount pairs.
	 */
	protected Map<GristType, Long> getMap()
	{
		return this.gristTypes;
	}
	
	public boolean hasType(GristType type)
	{
		return gristTypes.containsKey(type);
	}
	
	/**
	 * Returns a ArrayList containing GristAmount objects representing the set.
	 */
	public List<GristAmount> getAmounts()
	{
		return this.gristTypes.entrySet().stream().map((entry) -> new GristAmount(entry.getKey(), entry.getValue())).collect(Collectors.toList());
	}

	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public GristSet addGrist(GristSet set)
	{
		for (GristAmount grist : set.getAmounts())
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
			if (amount != 0)
			{
				this.gristTypes.put(type, roundDown ? (long) (amount * scale) : roundToNonZero(amount * scale));
			}
		});

		return this;
	}
	
	private int roundToNonZero(float value)
	{
		if(value < 0)
			return Math.min(-1, Math.round(value));
		else return Math.max(1, Math.round(value));
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
	
	public ITextComponent asTextComponent()
	{
		ITextComponent component = null;
		for(GristAmount grist : getAmounts())
		{
			if(component == null)
				component = grist.asTextComponent();
			else component = new TranslationTextComponent(GRIST_COMMA, component, grist.asTextComponent());
		}
		if(component != null)
			return component;
		else return new StringTextComponent("");
	}
	
	public ITextComponent createMissingMessage()
	{
		return new TranslationTextComponent(MISSING_MESSAGE, asTextComponent());
	}
	
	
	public GristSet copy()
	{
		return new GristSet(new TreeMap<>(gristTypes));
	}
	
	public void spawnGristEntities(World world, double x, double y, double z, Random rand, Consumer<GristEntity> postProcessor)
	{
		for(GristAmount amount : getAmounts())
		{
			long countLeft = amount.getAmount();
			for(int i = 0; i < 10 && countLeft > 0; i++)
			{
				long spawnedCount = countLeft <= amount.getAmount()/10 || i == 9 ? countLeft : Math.min(countLeft, (long) world.rand.nextDouble()*countLeft + 1);
				GristAmount spawnedAmount = new GristAmount(amount.getType(), spawnedCount);
				GristEntity entity = new GristEntity(world, x, y, z, spawnedAmount);
				postProcessor.accept(entity);
				world.addEntity(entity);
				countLeft -= spawnedCount;
			}
		}
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
			GristType type = GristTypes.REGISTRY.getValue(gristId);
			if(type == null)
				throw new JsonParseException("'"+entry.getKey()+"' did not match an existing grist type!");
			long amount = ExtraJSONUtils.getLong(entry.getValue(), entry.getKey());
			set.addGrist(type, amount);
		}
		
		return set;
	}
	
	public void write(PacketBuffer buffer)
	{
		List<GristAmount> amounts = getAmounts();
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
	
	public ListNBT write(ListNBT list)
	{
		getAmounts().forEach(gristAmount -> list.add(gristAmount.write(new CompoundNBT(), null)));
		return list;
	}
	
	public static GristSet read(ListNBT list)
	{
		GristSet set = new GristSet();
		for(int i = 0; i < list.size(); i++)
			set.addGrist(GristAmount.read(list.getCompound(i), null));
		
		return set;
	}
}