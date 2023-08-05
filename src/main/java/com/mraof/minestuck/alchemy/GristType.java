package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.util.LazyInstance;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
public final class GristType implements Comparable<GristType>
{
	static final ResourceLocation DUMMY_ID = new ResourceLocation(Minestuck.MOD_ID, "dummy");
	private static final ResourceLocation DUMMY_ICON_LOCATION = makeIconPath(DUMMY_ID);
	
	public static final String FORMAT = "grist.format";
	
	private final float rarity;
	private final float value;
	private final int color;
	private final boolean underlingType;
	private final Supplier<ItemStack> candyItem;
	@Nullable
	private final ResourceLocation textureOverrideId;
	private final LazyInstance<String> translationKey = new LazyInstance<>(() -> Util.makeDescriptionId("grist", GristTypes.getRegistry().getKey(GristType.this)));
	private final LazyInstance<ResourceLocation> icon = new LazyInstance<>(() -> makeIconPath(GristType.this.getTextureId()));
	
	public GristType(Properties properties)
	{
		rarity = properties.rarity;
		value = properties.value;
		color = properties.color;
		underlingType = properties.isUnderlingType;
		candyItem = properties.candyItem;
		textureOverrideId = properties.textureOverrideId;
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
	
	public boolean isUnderlingType()
	{
		return underlingType;
	}
	
	public ResourceLocation getIcon()
	{
		return icon.get();
	}
	
	public ResourceLocation getTextureId()
	{
		if(this.textureOverrideId != null)
			return this.textureOverrideId;
		
		ResourceLocation name = GristTypes.getRegistry().getKey(this);
		return Objects.requireNonNullElse(name, DUMMY_ID);
	}
	
	public ItemStack getCandyItem()
	{
		return candyItem.get();
	}
	
	public List<GristType> getSecondaryTypes()
	{
		return Objects.requireNonNull(GristTypes.getRegistry().tags())
				.getTag(this.getSecondaryTypesTag()).stream().toList();
	}
	
	public TagKey<GristType> getSecondaryTypesTag()
	{
		ResourceLocation name = GristTypes.getRegistry().getKey(this);
		Objects.requireNonNull(name);
		return GristTypes.GRIST_TYPES.createTagKey(new ResourceLocation(name.getNamespace(), "secondary/" + name.getPath()));
	}
	
	/**
	 * Returns the resource location to the dummy grist icon texture
	 * The actual field is private as to not get caught by the ObjectHolder on the class (because it checks all public static final fields).
	 */
	public static ResourceLocation getDummyIcon()
	{
		return DUMMY_ICON_LOCATION;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(GristTypes.getRegistry().getKey(this));
	}
	
	@Override
	public int compareTo(GristType gristType)
	{
		if(this.rarity > gristType.rarity)
			return -1;
		else if(this.rarity < gristType.rarity)
			return 1;
		else return Objects.requireNonNull(GristTypes.getRegistry().getKey(this)).getPath()
					.compareTo(Objects.requireNonNull(GristTypes.getRegistry().getKey(gristType)).getPath());
	}
	
	public int getColor()
	{
		return color;
	}
	
	public static class Properties
	{
		private final float rarity, value;
		private int color;
		private boolean isUnderlingType = true;
		private Supplier<ItemStack> candyItem = () -> ItemStack.EMPTY;
		@Nullable
		private ResourceLocation textureOverrideId = null;
		
		public Properties(float rarity)
		{
			this(rarity, 2);
		}
		
		public Properties(float rarity, float value)
		{
			this.rarity = rarity;
			this.value = value;
		}
		
		public Properties notUnderlingType()
		{
			isUnderlingType = false;
			return this;
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
		
		public Properties color(int color)
		{
			this.color = color;
			return this;
		}
		
		public Properties textureOverride(ResourceLocation textureOverrideId)
		{
			this.textureOverrideId = textureOverrideId;
			return this;
		}
	}
	
	public enum SpawnCategory    //Which categories can a certain grist type appear under (for spawning underlings)
	{
		COMMON("common"),
		UNCOMMON("uncommon"),
		ANY("any");
		
		private final TagKey<GristType> tagKey;
		
		SpawnCategory(String name)
		{
			this.tagKey = GristTypes.GRIST_TYPES.createTagKey("spawnable_" + name);
		}
		
		public TagKey<GristType> getTagKey()
		{
			return this.tagKey;
		}
		
		public Stream<GristType> gristTypes()
		{
			return Objects.requireNonNull(GristTypes.getRegistry().tags()).getTag(this.tagKey).stream()
					.filter(GristType::isUnderlingType);
		}
	}
	
	private static ResourceLocation makeIconPath(ResourceLocation entry)
	{
		return new ResourceLocation(entry.getNamespace(), "textures/grist/" + entry.getPath() + ".png");
	}
}