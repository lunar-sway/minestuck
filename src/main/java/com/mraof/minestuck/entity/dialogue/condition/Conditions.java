package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.mraof.minestuck.entity.dialogue.condition.Condition.*;

public final class Conditions
{
	public static final DeferredRegister<MapCodec<? extends Condition>> REGISTER = DeferredRegister.create(Minestuck.id("dialogue_condition"), Minestuck.MOD_ID);
	public static final Registry<MapCodec<? extends Condition>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static {
		REGISTER.register("always_true", () -> AlwaysTrue.CODEC);
		REGISTER.register("first_time_generating", () -> FirstTimeGenerating.CODEC);
		REGISTER.register("list", () -> ListCondition.CODEC);
		REGISTER.register("carapacian", () -> IsCarapacian.CODEC);
		REGISTER.register("is_from_kingdom", () -> IsFromKingdom.CODEC);
		REGISTER.register("entity_type", () -> IsEntityType.CODEC);
		REGISTER.register("is_is_land", () -> IsInLand.CODEC);
		REGISTER.register("is_consort_from_land", () -> IsConsortFromLand.CODEC);
		REGISTER.register("is_consort_in_home_land", () -> IsConsortInHomeLand.CODEC);
		REGISTER.register("terrain_land_type", () -> InTerrainLandType.CODEC);
		REGISTER.register("terrain_land_type_tag", () -> InTerrainLandTypeTag.CODEC);
		REGISTER.register("consort_terrain_land_type", () -> InConsortTerrainLandType.CODEC);
		REGISTER.register("title_land_type", () -> InTitleLandType.CODEC);
		REGISTER.register("title_land_type_tag", () -> InTitleLandTypeTag.CODEC);
		REGISTER.register("at_or_above_y", () -> AtOrAboveY.CODEC);
		REGISTER.register("npc_holding_item", () -> NPCIsHoldingItem.CODEC);
		REGISTER.register("player_item", () -> PlayerHasItem.CODEC);
		REGISTER.register("item_tag_match", () -> ItemTagMatch.CODEC);
		REGISTER.register("item_tag_match_exclude", () -> ItemTagMatchExclude.CODEC);
		REGISTER.register("has_matched_item", () -> HasMatchedItem.CODEC);
		REGISTER.register("player_class", () -> PlayerIsClass.CODEC);
		REGISTER.register("player_aspect", () -> PlayerIsAspect.CODEC);
		REGISTER.register("player_reputation", () -> PlayerHasReputation.CODEC);
		REGISTER.register("player_boondollars", () -> PlayerHasBoondollars.CODEC);
		REGISTER.register("player_entered", () -> PlayerHasEntered.CODEC);
		REGISTER.register("player_advancement", () -> PlayerHasAdvancement.CODEC);
		REGISTER.register("custom_score", () -> CustomHasScore.CODEC);
		REGISTER.register("custom_tag", () -> CustomHasTag.CODEC);
		REGISTER.register("dialogue_exists", () -> DialogueExists.CODEC);
		REGISTER.register("move_restriction", () -> HasMoveRestriction.CODEC);
		REGISTER.register("flag", () -> Flag.CODEC);
		REGISTER.register("near_spawn", () -> NearSpawn.CODEC);
		REGISTER.register("is_in_skaia", () -> IsInSkaia.CODEC);
		REGISTER.register("consort_visited_skaia", () -> ConsortVisitedSkaia.CODEC);
	}
	
	public static Condition alwaysTrue()
	{
		return AlwaysTrue.INSTANCE;
	}
	
	public static Condition all(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ALL);
	}
	
	public static Condition any(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ANY);
	}
	
	public static Condition one(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.ONE);
	}
	
	public static Condition none(Condition... conditions)
	{
		return new ListCondition(List.of(conditions), ListCondition.ListType.NONE);
	}
	
	public static Condition isInLand()
	{
		return IsInLand.INSTANCE;
	}
	
	public static Condition isFromLand()
	{
		return IsConsortFromLand.INSTANCE;
	}
	
	public static Condition isInHomeLand()
	{
		return IsConsortInHomeLand.INSTANCE;
	}
	
	public static Condition isInTerrainLand(Supplier<TerrainLandType> landType)
	{
		return new InTerrainLandType(landType.get());
	}
	
	public static Condition isInTerrainLand(TagKey<TerrainLandType> tag)
	{
		return new InTerrainLandTypeTag(tag);
	}
	
	public static Condition isInConsortLand(EnumConsort consort)
	{
		return new InConsortTerrainLandType(consort);
	}
	
	public static Condition isInTitleLand(Supplier<TitleLandType> landType)
	{
		return new InTitleLandType(landType.get());
	}
	
	public static Condition isInTitleLand(TagKey<TitleLandType> tag)
	{
		return new InTitleLandTypeTag(tag);
	}
	
	public static Condition isInSkaia()
	{
		return IsInSkaia.INSTANCE;
	}
	
	public static Condition isProspitian()
	{
		return new IsFromKingdom(EnumEntityKingdom.PROSPITIAN);
	}
	
	public static Condition isDersite()
	{
		return new IsFromKingdom(EnumEntityKingdom.DERSITE);
	}
	
	public static PlayerOnlyCondition hasEntered()
	{
		return PlayerHasEntered.INSTANCE;
	}
	public static PlayerOnlyCondition hasAdvancement(String name)
	{
		return new PlayerHasAdvancement(Minestuck.id(name.replace(".", "/")));
	}
	
	public static Condition isHolding(Item item)
	{
		return new NPCIsHoldingItem(item);
	}
	
	@SafeVarargs
	public static Condition isAnyEntityType(Supplier<EntityType<ConsortEntity>>... entityType)
	{
		return isAnyEntityType(Arrays.stream(entityType).map(Supplier::get).toArray(EntityType<?>[]::new));
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new IsEntityType(entityTypes[0]);
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(IsEntityType::new).toList(), ListCondition.ListType.ANY);
	}
}
