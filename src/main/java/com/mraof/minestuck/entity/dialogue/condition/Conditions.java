package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class Conditions
{
	public static final DeferredRegister<Codec<? extends Condition>> REGISTER = DeferredRegister.create(new ResourceLocation(Minestuck.MOD_ID, "dialogue_condition"), Minestuck.MOD_ID);
	public static final Supplier<IForgeRegistry<Codec<? extends Condition>>> REGISTRY = REGISTER.makeRegistry(() -> new RegistryBuilder<Codec<? extends Condition>>().disableSaving().disableSync());
	
	static {
		REGISTER.register("always_true", () -> Condition.AlwaysTrue.CODEC);
		REGISTER.register("first_time_generating", () -> Condition.FirstTimeGenerating.CODEC);
		REGISTER.register("list", () -> ListCondition.CODEC);
		REGISTER.register("carapacian", () -> Condition.IsCarapacian.CODEC);
		REGISTER.register("entity_type", () -> Condition.IsEntityType.CODEC);
		REGISTER.register("is_from_land", () -> Condition.IsFromLand.CODEC);
		REGISTER.register("terrain_land_type", () -> Condition.InTerrainLandType.CODEC);
		REGISTER.register("terrain_land_type_tag", () -> Condition.InTerrainLandTypeTag.CODEC);
		REGISTER.register("title_land_type", () -> Condition.InTitleLandType.CODEC);
		REGISTER.register("title_land_type_tag", () -> Condition.InTitleLandTypeTag.CODEC);
		REGISTER.register("at_or_above_y", () -> Condition.AtOrAboveY.CODEC);
		REGISTER.register("player_item", () -> Condition.PlayerHasItem.CODEC);
		REGISTER.register("player_class", () -> Condition.PlayerIsClass.CODEC);
		REGISTER.register("player_aspect", () -> Condition.PlayerIsAspect.CODEC);
		REGISTER.register("player_reputation", () -> Condition.PlayerHasReputation.CODEC);
		REGISTER.register("player_boondollars", () -> Condition.PlayerHasBoondollars.CODEC);
		REGISTER.register("custom_score", () -> Condition.CustomHasScore.CODEC);
		REGISTER.register("move_restriction", () -> Condition.HasMoveRestriction.CODEC);
		REGISTER.register("player_flag", () -> Condition.PlayerFlag.CODEC);
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
	
	public static Condition isFromLand()
	{
		return Condition.IsFromLand.INSTANCE;
	}
	
	public static Condition isInTerrain(RegistryObject<TerrainLandType> landType)
	{
		return new Condition.InTerrainLandType(landType.get());
	}
	
	public static Condition isInTitle(RegistryObject<TitleLandType> landType)
	{
		return new Condition.InTitleLandType(landType.get());
	}
	
	public static Condition isInTerrainLand(TagKey<TerrainLandType> tag)
	{
		return new Condition.InTerrainLandTypeTag(tag);
	}
	
	public static Condition isInTitleLand(TagKey<TitleLandType> tag)
	{
		return new Condition.InTitleLandTypeTag(tag);
	}
	
	@SafeVarargs
	public static Condition isAnyEntityType(RegistryObject<EntityType<ConsortEntity>>... entityType)
	{
		return isAnyEntityType(Arrays.stream(entityType).map(RegistryObject::get).toArray(EntityType<?>[]::new));
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new Condition.IsEntityType(entityTypes[0]);
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(Condition.IsEntityType::new).toList(), ListCondition.ListType.ANY);
	}
}
