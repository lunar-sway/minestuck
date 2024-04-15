package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.Codec;
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

public final class Conditions
{
	public static final DeferredRegister<Codec<? extends Condition>> REGISTER = DeferredRegister.create(Minestuck.id("dialogue_condition"), Minestuck.MOD_ID);
	public static final Registry<Codec<? extends Condition>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static {
		REGISTER.register("always_true", () -> Condition.AlwaysTrue.CODEC);
		REGISTER.register("first_time_generating", () -> Condition.FirstTimeGenerating.CODEC);
		REGISTER.register("list", () -> ListCondition.CODEC);
		REGISTER.register("carapacian", () -> Condition.IsCarapacian.CODEC);
		REGISTER.register("is_from_kingdom", () -> Condition.IsFromKingdom.CODEC);
		REGISTER.register("entity_type", () -> Condition.IsEntityType.CODEC);
		REGISTER.register("is_is_land", () -> Condition.IsInLand.CODEC);
		REGISTER.register("is_consort_from_land", () -> Condition.IsConsortFromLand.CODEC);
		REGISTER.register("is_consort_in_home_land", () -> Condition.IsConsortInHomeLand.CODEC);
		REGISTER.register("terrain_land_type", () -> Condition.InTerrainLandType.CODEC);
		REGISTER.register("terrain_land_type_tag", () -> Condition.InTerrainLandTypeTag.CODEC);
		REGISTER.register("consort_terrain_land_type", () -> Condition.InConsortTerrainLandType.CODEC);
		REGISTER.register("title_land_type", () -> Condition.InTitleLandType.CODEC);
		REGISTER.register("title_land_type_tag", () -> Condition.InTitleLandTypeTag.CODEC);
		REGISTER.register("at_or_above_y", () -> Condition.AtOrAboveY.CODEC);
		REGISTER.register("npc_holding_item", () -> Condition.NPCIsHoldingItem.CODEC);
		REGISTER.register("player_item", () -> Condition.PlayerHasItem.CODEC);
		REGISTER.register("item_tag_match", () -> Condition.ItemTagMatch.CODEC);
		REGISTER.register("item_tag_match_exclude", () -> Condition.ItemTagMatchExclude.CODEC);
		REGISTER.register("has_matched_item", () -> Condition.HasMatchedItem.CODEC);
		REGISTER.register("player_class", () -> Condition.PlayerIsClass.CODEC);
		REGISTER.register("player_aspect", () -> Condition.PlayerIsAspect.CODEC);
		REGISTER.register("player_reputation", () -> Condition.PlayerHasReputation.CODEC);
		REGISTER.register("player_boondollars", () -> Condition.PlayerHasBoondollars.CODEC);
		REGISTER.register("player_entered", () -> Condition.PlayerHasEntered.CODEC);
		REGISTER.register("custom_score", () -> Condition.CustomHasScore.CODEC);
		REGISTER.register("move_restriction", () -> Condition.HasMoveRestriction.CODEC);
		REGISTER.register("flag", () -> Condition.Flag.CODEC);
		REGISTER.register("near_spawn", () -> Condition.NearSpawn.CODEC);
		REGISTER.register("has_player_entered", () -> Condition.HasPlayerEntered.CODEC);
		REGISTER.register("is_in_skaia", () -> Condition.IsInSkaia.CODEC);
		REGISTER.register("consort_visited_skaia", () -> Condition.ConsortVisitedSkaia.CODEC);
	}
	
	public static Condition alwaysTrue()
	{
		return Condition.AlwaysTrue.INSTANCE;
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
		return Condition.IsInLand.INSTANCE;
	}
	
	public static Condition isFromLand()
	{
		return Condition.IsConsortFromLand.INSTANCE;
	}
	
	public static Condition isInHomeLand()
	{
		return Condition.IsConsortInHomeLand.INSTANCE;
	}
	
	public static Condition isInTerrainLand(Supplier<TerrainLandType> landType)
	{
		return new Condition.InTerrainLandType(landType.get());
	}
	
	public static Condition isInTerrainLand(TagKey<TerrainLandType> tag)
	{
		return new Condition.InTerrainLandTypeTag(tag);
	}
	
	public static Condition isInConsortLand(EnumConsort consort)
	{
		return new Condition.InConsortTerrainLandType(consort);
	}
	
	public static Condition isInTitleLand(Supplier<TitleLandType> landType)
	{
		return new Condition.InTitleLandType(landType.get());
	}
	
	public static Condition isInTitleLand(TagKey<TitleLandType> tag)
	{
		return new Condition.InTitleLandTypeTag(tag);
	}
	
	public static Condition isInSkaia()
	{
		return Condition.IsInSkaia.INSTANCE;
	}
	
	public static Condition isProspitian()
	{
		return new Condition.IsFromKingdom(EnumEntityKingdom.PROSPITIAN);
	}
	
	public static Condition isDersite()
	{
		return new Condition.IsFromKingdom(EnumEntityKingdom.DERSITE);
	}
	
	public static Condition hasEntered()
	{
		return Condition.PlayerHasEntered.INSTANCE;
	}
	
	public static Condition isHolding(Item item)
	{
		return new Condition.NPCIsHoldingItem(item);
	}
	
	@SafeVarargs
	public static Condition isAnyEntityType(Supplier<EntityType<ConsortEntity>>... entityType)
	{
		return isAnyEntityType(Arrays.stream(entityType).map(Supplier::get).toArray(EntityType<?>[]::new));
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new Condition.IsEntityType(entityTypes[0]);
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(Condition.IsEntityType::new).toList(), ListCondition.ListType.ANY);
	}
}
