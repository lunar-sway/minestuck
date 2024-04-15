package com.mraof.minestuck.item.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record LandTypeLootCondition(HolderSet<TerrainLandType> terrainTypes, HolderSet<TitleLandType> titleTypes, boolean inverted) implements LootItemCondition
{
	public static final Codec<LandTypeLootCondition> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					RegistryCodecs.homogeneousList(LandTypes.TERRAIN_KEY).fieldOf("terrain_type").forGetter(LandTypeLootCondition::terrainTypes),
					RegistryCodecs.homogeneousList(LandTypes.TITLE_KEY).fieldOf("title_type").forGetter(LandTypeLootCondition::titleTypes),
					ExtraCodecs.strictOptionalField(Codec.BOOL, "inverted", false).forGetter(LandTypeLootCondition::inverted)
			).apply(instance, LandTypeLootCondition::new));
	
	@Override
	public LootItemConditionType getType()
	{
		return MSLootTables.LAND_TYPE_CONDITION.get();
	}
	
	@Override
	public boolean test(LootContext context)
	{
		ServerLevel level = context.getLevel();
		
		LandTypePair aspects = LandTypePair.getTypes(level).orElse(null);
		
		if(aspects != null &&
				(aspects.getTerrain().is(terrainTypes)
						|| aspects.getTitle().is(titleTypes)))
			return !inverted;
		
		return inverted;
	}
}
