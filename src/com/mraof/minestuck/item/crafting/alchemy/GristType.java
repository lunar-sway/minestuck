package com.mraof.minestuck.item.crafting.alchemy;

import com.google.common.collect.ImmutableList;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.MSNBTUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GristType extends ForgeRegistryEntry<GristType> implements Comparable<GristType>
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation DUMMY_ICON_LOCATION = new ResourceLocation(Minestuck.MOD_ID, "textures/grist/dummy.png");
	
	public static final String FORMAT = "grist.format";
	
	private final float rarity;
	private final float value;
	private final ItemStack candyItem;
	private final List<Supplier<GristType>> secondaryTypes;
	private String translationKey;
	private ResourceLocation icon;
	
	public GristType(Properties properties)
	{
		rarity = properties.rarity;
		value = properties.value;
		candyItem = properties.candyItem;
		secondaryTypes = ImmutableList.copyOf(properties.secondaryGristTypes);
	}
	
	public ITextComponent getNameWithSuffix()
	{
		return new TranslationTextComponent(FORMAT, getDisplayName());
	}
	
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent(getTranslationKey());
	}
	/**
	 * Returns the grist's translation key
	 */
	public String getTranslationKey()
	{
		if(translationKey == null)
			translationKey = Util.makeTranslationKey("grist", GristTypes.REGISTRY.getKey(this));
		
		return translationKey;
	}

	/**
	 * Returns the grist's rarity. Is a number from 0.0 to 1.0.
	 *
	 * @return the rarity
	 */
	public float getRarity()
	{
		return rarity;
	}

	/**
	 * Returns the power level of a underling of a grist's type. Don't call this with grists like Zillium or Build.
	 */
	public float getPower()
	{
		return rarity != 0 ? 1 / rarity : 100;
	}
	
	/**
	 * @return a value estimate for this grist type
	 */
	public float getValue()
	{
		return value;
	}
	
	public ResourceLocation getIcon()
	{
		if(icon == null)
			icon = makeIconPath(getEffectiveName());
		
		return icon;
	}
	
	public ResourceLocation getEffectiveName()
	{
		ResourceLocation name = GristTypes.REGISTRY.getKey(this);
		if(name == null)
			return new ResourceLocation(Minestuck.MOD_ID, "dummy");
		else return name;
	}
	
	public ItemStack getCandyItem()
	{
		return candyItem.copy();
	}
	
	public List<GristType> getSecondaryTypes()
	{
		return secondaryTypes.stream().map(Supplier::get).collect(Collectors.toList());
	}
	
	public int getId()
	{
		return ((ForgeRegistry<GristType>) GristTypes.REGISTRY).getID(this);	//TODO Not ideal. Find a better solution
	}
	
	/**
	 * Returns the resource location to the dummy grist icon texture
	 * The actual field is private as to not get caught by the ObjectHolder on the class (because it checks all public static final fields).
	 */
	public static ResourceLocation getDummyIcon()
	{
		return DUMMY_ICON_LOCATION;
	}
	
	private static ResourceLocation makeIconPath(ResourceLocation entry)
	{
		if(entry == null)
			return DUMMY_ICON_LOCATION;
		else return new ResourceLocation(entry.getNamespace(), "textures/grist/"+entry.getPath()+".png");
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(getRegistryName());
	}
	
	@Override
	public int compareTo(GristType gristType)
	{
		return this.getId() - gristType.getId();
	}
	
	public final void write(CompoundNBT nbt, String key)
	{
		ResourceLocation name = this.getRegistryName();
		if(name == null)
			LOGGER.error("Trying to save grist type {} that is lacking a registry name!", this);
		else MSNBTUtil.writeResourceLocation(nbt, key, name);
	}
	
	public static GristType read(CompoundNBT nbt, String key)
	{
		return read(nbt, key, GristTypes.BUILD);
	}
	
	public static GristType read(CompoundNBT nbt, String key, GristType fallback)
	{
		ResourceLocation name = MSNBTUtil.tryReadResourceLocation(nbt, key);
		if(name != null)
		{
			GristType type = GristTypes.REGISTRY.getValue(name);
			if(type != null)
				return type;
			else LOGGER.warn("Couldn't find grist type by name {}  while reading from nbt. Will fall back to {} instead.", name, fallback);
		}
		return fallback;
	}
	
	static class DummyType extends GristType
	{
		DummyType()
		{
			super(new Properties(0.3F, 3));
		}
		
		@Override
		public ResourceLocation getIcon()
		{
			return DUMMY_ICON_LOCATION;
		}
		
		@Override
		public ResourceLocation getEffectiveName()
		{
			return new ResourceLocation(Minestuck.MOD_ID, "dummy");
		}
	}
	
	public static class Properties
	{
		private final float rarity, value;
		private ItemStack candyItem = ItemStack.EMPTY;
		private List<Supplier<GristType>> secondaryGristTypes = new ArrayList<>();
		
		public Properties(float rarity)
		{
			this(rarity, 2);
		}
		
		public Properties(float rarity, float value)
		{
			this.rarity = rarity;
			this.value = value;
		}
		
		public Properties candy(Item item)
		{
			return candy(new ItemStack(item));
		}
		
		public Properties candy(ItemStack stack)
		{
			candyItem = Objects.requireNonNull(stack);
			return this;
		}
		
		public Properties secondary(Supplier<GristType> type)
		{
			secondaryGristTypes.add(type);
			return this;
		}
	}
}