package com.mraof.minestuck.alchemy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GristSet implements IGristSet
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
			this.gristTypes.put(amount.type(), amount.amount());
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
	
	public long getGrist(Supplier<GristType> type)
	{
		return getGrist(type.get());
	}
	
	/**
	 * @return a value estimate for this grist set
	 */
	public double getValue()
	{
		double sum = 0;
		for(GristAmount amount : asAmounts())
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
	
	public GristSet addGrist(Supplier<GristType> type, long amount)
	{
		return addGrist(type.get(), amount);
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
	@Override
	public List<GristAmount> asAmounts()
	{
		return this.gristTypes.entrySet().stream().map((entry) -> new GristAmount(entry.getKey(), entry.getValue())).toList();
	}

	/**
	 * Adds an amount of grist to a GristSet, given another set of grist.
	 */
	public GristSet addGrist(IGristSet set)
	{
		for (GristAmount grist : set.asAmounts())
			this.addGrist(grist.type(), grist.amount());
		
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
		return this.gristTypes.values().stream().allMatch(amount -> amount == 0);
	}
	
	public boolean equalContent(GristSet other)
	{
		for(GristType type : GristTypes.values())
			if(this.getGrist(type) != other.getGrist(type))
				return false;
		return true;
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
			build.append(entry.getKey()).append("=").append(entry.getValue());
			first = false;
		}

		build.append(']');
		return build.toString();
	}
	
	public Component asTextComponent()
	{
		Component component = null;
		for(GristAmount grist : asAmounts())
		{
			if(component == null)
				component = grist.asTextComponent();
			else component = Component.translatable(GRIST_COMMA, component, grist.asTextComponent());
		}
		if(component != null)
			return component;
		else return Component.empty();
	}
	
	public Component createMissingMessage()
	{
		return Component.translatable(MISSING_MESSAGE, asTextComponent());
	}
	
	
	public GristSet copy()
	{
		return new GristSet(new TreeMap<>(gristTypes));
	}
	
	/**
	 * this is a version of the spawn grist entities function with a delay.
	 */
	public void spawnGristEntities(Level level, double x, double y, double z, RandomSource rand, Consumer<GristEntity> postProcessor, int delay, int gusherCount)
	{
		for(GristAmount amount : asAmounts())
		{
			long countLeft = amount.amount();
			for(int i = 0; i < 10 && countLeft > 0; i++)
			{
				long spawnedCount = countLeft <= amount.amount() / 10 || i ==
						gusherCount - 1 ? countLeft : Math.min(countLeft,
						(long) level.random.nextDouble() * countLeft + 1);
				GristAmount spawnedAmount = new GristAmount(amount.type(), spawnedCount);
				GristEntity entity = new GristEntity(level, x, y, z, spawnedAmount, delay);
				postProcessor.accept(entity);
				level.addFreshEntity(entity);
				countLeft -= spawnedCount;
			}
		}
	}
	
	public void spawnGristEntities(Level level, double x, double y, double z, RandomSource rand, Consumer<GristEntity> postProcessor)
	{
		spawnGristEntities(level, x, y, z, rand, postProcessor, 0, 10);
	}
	
	public JsonElement serialize()
	{
		JsonObject json = new JsonObject();
		for(Map.Entry<GristType, Long> entry : gristTypes.entrySet())
		{
			ResourceLocation id = GristTypes.getRegistry().getKey(entry.getKey());
			if(id == null)
			{
				LOGGER.warn("Found grist type without a registry name! ({})", entry.getKey());
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
			GristType type = GristTypes.getRegistry().getValue(gristId);
			if(type == null)
				throw new JsonParseException("'"+entry.getKey()+"' did not match an existing grist type!");
			long amount = GsonHelper.convertToLong(entry.getValue(), entry.getKey());	//getLong
			set.addGrist(type, amount);
		}
		
		return set;
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		List<GristAmount> amounts = asAmounts();
		buffer.writeInt(amounts.size());
		amounts.forEach(gristAmount -> gristAmount.write(buffer));
	}
	
	public static GristSet read(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		GristAmount[] amounts = new GristAmount[size];
		for(int i = 0; i < size; i++)
			amounts[i] = GristAmount.read(buffer);
		
		return new GristSet(amounts);
	}
	
	public ListTag write(ListTag list)
	{
		asAmounts().forEach(gristAmount -> list.add(gristAmount.write(new CompoundTag(), null)));
		return list;
	}
	
	public static GristSet read(ListTag list)
	{
		GristSet set = new GristSet();
		for(int i = 0; i < list.size(); i++)
			set.addGrist(GristAmount.read(list.getCompound(i), null));
		
		return set;
	}
}