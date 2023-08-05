package com.mraof.minestuck.alchemy;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.Objects;
import java.util.stream.Stream;

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
		return Objects.requireNonNull(GristTypes.getRegistry().tags()).getTag(this.tagKey).stream()
				.filter(GristType::isUnderlingType);
	}
}
