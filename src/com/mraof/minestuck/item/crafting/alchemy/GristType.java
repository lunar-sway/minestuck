package com.mraof.minestuck.item.crafting.alchemy;

import com.mraof.minestuck.Minestuck;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

@ObjectHolder(Minestuck.MOD_ID)
public class GristType extends ForgeRegistryEntry<GristType> implements Comparable<GristType>
{
	public static String FORMAT = "grist.format";	//TODO Readd final once the ObjectHolder use has been sorted out
	
	private static final ResourceLocation DUMMY_ICON_LOCATION = new ResourceLocation(Minestuck.MOD_ID, "textures/grist/dummy.png");
	
	@Deprecated	//These references are being moved to GristTypes
	public static final GristType BUILD = getNull();
	@Deprecated
	public static final GristType AMBER = getNull();
	@Deprecated
	public static final GristType CAULK = getNull();
	@Deprecated
	public static final GristType CHALK = getNull();
	@Deprecated
	public static final GristType IODINE = getNull();
	@Deprecated
	public static final GristType SHALE = getNull();
	@Deprecated
	public static final GristType TAR = getNull();
	@Deprecated
	public static final GristType COBALT = getNull();
	@Deprecated
	public static final GristType MARBLE = getNull();
	@Deprecated
	public static final GristType MERCURY = getNull();
	@Deprecated
	public static final GristType QUARTZ = getNull();
	@Deprecated
	public static final GristType SULFUR = getNull();
	@Deprecated
	public static final GristType AMETHYST = getNull();
	@Deprecated
	public static final GristType GARNET = getNull();
	@Deprecated
	public static final GristType RUBY = getNull();
	@Deprecated
	public static final GristType RUST = getNull();
	@Deprecated
	public static final GristType DIAMOND = getNull();
	@Deprecated
	public static final GristType GOLD = getNull();
	@Deprecated
	public static final GristType URANIUM = getNull();
	@Deprecated
	public static final GristType ARTIFACT = getNull();
	@Deprecated
	public static final GristType ZILLIUM = getNull();
	
	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T getNull()
	{
		return null;
	}
	
	private final float rarity;
	private final float value;
	private ItemStack candyItem = ItemStack.EMPTY;
	private String translationKey;
	private ResourceLocation icon;
	
	public GristType(float rarity)
	{
		this(rarity, 2);
	}
	
	public GristType(float rarity, float value)
	{
		this.rarity = rarity;
		this.value = value;
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
	 * Returns the grist's full unlocalized name.
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
		return candyItem == null ? null : candyItem.copy();
	}
	
	public GristType setCandyItem(ItemStack stack)	//TODO Put this in the constructor instead
	{
		candyItem = stack;
		return this;
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
	
	static class DummyType extends GristType
	{
		DummyType()
		{
			super(0.3F, 3);
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
}