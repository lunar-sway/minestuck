package com.mraof.minestuck.api.alchemy;

import com.google.common.base.Suppliers;
import com.mraof.minestuck.Minestuck;
import net.minecraft.Util;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public final class GristType implements Comparable<GristType>
{
	public static final ResourceLocation DUMMY_ID = new ResourceLocation(Minestuck.MOD_ID, "dummy");
	public static final ResourceLocation DUMMY_ICON_LOCATION = makeIconPath(DUMMY_ID);
	
	public static final String FORMAT = "grist.format";
	
	private final float rarity;
	private final float value;
	private final Supplier<ItemStack> candyItem;
	@Nullable
	private final UnderlingData underlingData;
	@Nullable
	private final ResourceLocation textureOverrideId;
	
	private final Supplier<String> translationKey = Suppliers.memoize(() -> Util.makeDescriptionId("grist", GristType.this.getId()));
	private final Supplier<ResourceLocation> icon = Suppliers.memoize(() -> makeIconPath(GristType.this.getTextureId()));
	
	public GristType(Properties properties)
	{
		rarity = properties.rarity;
		value = properties.value;
		candyItem = properties.candyItem;
		underlingData = properties.underlingData;
		textureOverrideId = properties.textureOverrideId;
	}
	
	public GristAmount amount(long amount)
	{
		return new GristAmount(this, amount);
	}
	
	@Nullable
	public ResourceLocation getId()
	{
		return GristTypes.REGISTRY.getKey(this);
	}
	
	public ResourceLocation getIdOrThrow()
	{
		return Objects.requireNonNull(this.getId());
	}
	
	public Component getNameWithSuffix()
	{
		return Component.translatable(FORMAT, getDisplayName());
	}
	
	public MutableComponent getDisplayName()
	{
		return Component.translatable(getTranslationKey());
	}
	
	public String getTranslationKey()
	{
		return translationKey.get();
	}
	
	/**
	 * Returns the grist's rarity. Is a number from 0.0 to 1.0.
	 */
	public float getRarity()
	{
		return rarity;
	}
	
	/**
	 * Returns a modifier used for the power of underlings of the grist's type.
	 */
	public float getPower()
	{
		return underlingData != null ? underlingData.power : 0;
	}
	
	/**
	 * @return a value estimate for this grist type
	 */
	public float getValue()
	{
		return value;
	}
	
	public boolean isUnderlingType()
	{
		return underlingData != null;
	}
	
	public ResourceLocation getIcon()
	{
		return icon.get();
	}
	
	public ResourceLocation getTextureId()
	{
		if(this.textureOverrideId != null)
			return this.textureOverrideId;
		
		return Objects.requireNonNullElse(this.getId(), DUMMY_ID);
	}
	
	public ItemStack getCandyItem()
	{
		return candyItem.get();
	}
	
	public Optional<HolderSet.Named<GristType>> getSecondaryTypes()
	{
		return GristTypes.REGISTRY.getTag(this.getSecondaryTypesTag());
	}
	
	public TagKey<GristType> getSecondaryTypesTag()
	{
		return TagKey.create(GristTypes.REGISTRY_KEY, this.getIdOrThrow().withPrefix("secondary/"));
	}
	
	public int getUnderlingColor()
	{
		return underlingData != null ? underlingData.color : 0xFFFFFF;
	}
	
	public static final class Properties
	{
		private final float rarity, value;
		private Supplier<ItemStack> candyItem = () -> ItemStack.EMPTY;
		@Nullable
		private UnderlingData underlingData = null;
		@Nullable
		private ResourceLocation textureOverrideId = null;
		
		public Properties(float rarity, float value)
		{
			this.rarity = rarity;
			this.value = value;
		}
		
		public Properties candy(Supplier<Item> item)
		{
			Objects.requireNonNull(item);
			return candyStack(() -> new ItemStack(item.get()));
		}
		
		public Properties candyStack(Supplier<ItemStack> stack)
		{
			candyItem = Objects.requireNonNull(stack);
			return this;
		}
		
		public Properties underlingType(int color)
		{
			return this.underlingType(1 / this.rarity, color);
		}
		
		/**
		 * Call this if an underling should be able to use this grist type.
		 * Does not make underlings with the grist type spawn naturally. See {@link GristTypeSpawnCategory} for that.
		 * @param power a modifier that affects the attack damage and health of underlings with this type.
		 * @param color a color representing this grist type used by the underling renderer.
		 */
		public Properties underlingType(float power, int color)
		{
			this.underlingData = new UnderlingData(color, power);
			return this;
		}
		
		public Properties textureOverride(ResourceLocation textureOverrideId)
		{
			this.textureOverrideId = textureOverrideId;
			return this;
		}
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(this.getId());
	}
	
	@Override
	public int compareTo(GristType gristType)
	{
		if(this.rarity > gristType.rarity)
			return -1;
		else if(this.rarity < gristType.rarity)
			return 1;
		else return this.getIdOrThrow().getPath()
					.compareTo(gristType.getIdOrThrow().getPath());
	}
	
	private static ResourceLocation makeIconPath(ResourceLocation textureId)
	{
		return textureId.withPath("textures/grist/%s.png"::formatted);
	}
	
	private record UnderlingData(int color, float power)
	{}
}
