package com.mraof.minestuck.api.alchemy;

import com.mraof.minestuck.Minestuck;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.stream.Stream;

/**
 * Defines the different grist type categories used by grist layers to determine grist types for underlings spawned naturally.
 * Each category have a grist type tag associated with it that you can add grist types to.
 * The {@link GristTypeSpawnCategory#ANY} category automatically contains all grist types in the two other categories.
 */
public enum GristTypeSpawnCategory    //Which categories can a certain grist type appear under (for spawning underlings)
{
	COMMON("common"),
	UNCOMMON("uncommon"),
	ANY("any");
	
	private final TagKey<GristType> tagKey;
	
	GristTypeSpawnCategory(String name)
	{
		this.tagKey = TagKey.create(GristTypes.REGISTRY_KEY, new ResourceLocation(Minestuck.MOD_ID, "spawnable_" + name));
	}
	
	public TagKey<GristType> getTagKey()
	{
		return this.tagKey;
	}
	
	public Stream<GristType> gristTypes()
	{
		return GristTypes.REGISTRY.getTag(this.tagKey).stream()
				.flatMap(HolderSet.ListBacked::stream)
				.map(Holder::value)
				.filter(GristType::isUnderlingType);
	}
}
