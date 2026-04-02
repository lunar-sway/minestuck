package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.SlotRanges;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static com.mraof.minestuck.entity.dialogue.condition.Condition.*;

public final class Conditions
{
	public static final DeferredRegister<MapCodec<? extends Condition>> REGISTER = DeferredRegister.create(Minestuck.id("dialogue_condition"), Minestuck.MOD_ID);
	public static final Registry<MapCodec<? extends Condition>> REGISTRY = REGISTER.makeRegistry(builder -> {});
	
	static
	{
		REGISTER.register("always_true", () -> AlwaysTrue.CODEC);
		REGISTER.register("first_time_generating", () -> FirstTimeGenerating.CODEC);
		REGISTER.register("list", () -> ListCondition.CODEC);
		REGISTER.register("is_in_land", () -> IsInLand.CODEC);
		REGISTER.register("is_consort_from_land", () -> IsConsortFromLand.CODEC);
		REGISTER.register("is_consort_in_home_land", () -> IsConsortInHomeLand.CODEC);
		REGISTER.register("terrain_land_type", () -> InTerrainLandType.CODEC);
		REGISTER.register("terrain_land_type_tag", () -> InTerrainLandTypeTag.CODEC);
		REGISTER.register("consort_terrain_land_type", () -> InConsortTerrainLandType.CODEC);
		REGISTER.register("title_land_type", () -> InTitleLandType.CODEC);
		REGISTER.register("title_land_type_tag", () -> InTitleLandTypeTag.CODEC);
		REGISTER.register("npc_near_block_predicate", () -> NPCNearBlockPredicate.CODEC);
		REGISTER.register("npc_near_entity_predicate", () -> NPCNearEntityPredicate.CODEC);
		REGISTER.register("npc_location_predicate", () -> NPCLocationPredicate.CODEC);
		REGISTER.register("npc_entity_predicate", () -> NPCEntityPredicate.CODEC);
		REGISTER.register("player_location_predicate", () -> PlayerLocationPredicate.CODEC);
		REGISTER.register("player_entity_predicate", () -> PlayerEntityPredicate.CODEC);
		REGISTER.register("player_predicate_condition", () -> PlayerPredicateCondition.CODEC);
		REGISTER.register("npc_in_structure", () -> NPCInStructure.CODEC);
		REGISTER.register("item_tag_match", () -> ItemTagMatch.CODEC);
		REGISTER.register("item_tag_match_exclude", () -> ItemTagMatchExclude.CODEC);
		REGISTER.register("has_matched_item", () -> HasMatchedItem.CODEC);
		REGISTER.register("player_class", () -> PlayerIsClass.CODEC);
		REGISTER.register("player_aspect", () -> PlayerIsAspect.CODEC);
		REGISTER.register("player_reputation", () -> PlayerHasReputation.CODEC);
		REGISTER.register("player_boondollars", () -> PlayerHasBoondollars.CODEC);
		REGISTER.register("player_entered", () -> PlayerHasEntered.CODEC);
		REGISTER.register("custom_score", () -> CustomHasScore.CODEC);
		REGISTER.register("dialogue_exists", () -> DialogueExists.CODEC);
		REGISTER.register("move_restriction", () -> HasMoveRestriction.CODEC);
		REGISTER.register("flag", () -> Flag.CODEC);
		REGISTER.register("near_spawn", () -> NearSpawn.CODEC);
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
	
	public static Condition isNearBlock(Block block, int radius, int count)
	{
		return new NPCNearBlockPredicate(BlockPredicate.Builder.block().of(block).build(), radius, count);
	}
	
	public static Condition isNearBlockTag(TagKey<Block> blockTag, int radius, int count)
	{
		return new NPCNearBlockPredicate(BlockPredicate.Builder.block().of(blockTag).build(), radius, count);
	}
	
	public static Condition isNearEntity(EntityType<?> entityType, int radius, int count)
	{
		return new NPCNearEntityPredicate(EntityPredicate.Builder.entity().of(entityType).build(), radius, count);
	}
	
	public static Condition isNearEntityTag(TagKey<EntityType<?>> entityTypeTag, int radius, int count)
	{
		return new NPCNearEntityPredicate(EntityPredicate.Builder.entity().of(entityTypeTag).build(), radius, count);
	}
	
	public static Condition isInSkaia()
	{
		return new NPCLocationPredicate(LocationPredicate.Builder.location().setDimension(MSDimensions.SKAIA).build());
	}
	
	public static Condition isInStructure(Holder<Structure> structureHolder)
	{
		return new NPCLocationPredicate(LocationPredicate.Builder.inStructure(structureHolder).build());
	}
	
	/*public static Condition isInSkaia(HolderLookup.Provider holderLookupProvider)
	{
		AtomicReference<Holder<Structure>> structureHolder;
		//Holder<Structure>[] structures = new Holder<Structure>[].;
		return new NPCLocationPredicate(LocationPredicate.Builder.location().setStructures(HolderSet.direct(holderLookupProvider.lookup(Registries.STRUCTURE).flatMap(lookup -> lookup.get(MSStructures.DERSE_BUNKER)).get().getDelegate())).build());
	}*/
	
	public static Condition isAtOrAboveY(int minY)
	{
		return new NPCLocationPredicate(LocationPredicate.Builder.location().setY(MinMaxBounds.Doubles.atLeast(minY)).build());
	}
	
	public static Condition isProspitian()
	{
		return new NPCEntityPredicate(EntityPredicate.Builder.entity().of(MSTags.EntityTypes.PROSPITIAN_CARAPACIANS).build());
	}
	
	public static Condition isDersite()
	{
		return new NPCEntityPredicate(EntityPredicate.Builder.entity().of(MSTags.EntityTypes.DERSITE_CARAPACIANS).build());
	}
	
	public static PlayerOnlyCondition hasEntered()
	{
		return PlayerHasEntered.INSTANCE;
	}
	
	public static PlayerOnlyCondition hasAdvancement(String name)
	{
		return new PlayerPredicateCondition(PlayerPredicate.Builder.player().checkAdvancementDone(Minestuck.id(name.replace(".", "/")), true).build());
	}
	
	public static PlayerOnlyCondition playerHasItem(Item item, int count)
	{
		return new PlayerEntityPredicate(EntityPredicate.Builder.entity().slots(new SlotsPredicate(Map.of(SlotRanges.nameToIds("inventory.*"), ItemPredicate.Builder.item().of(item).withCount(MinMaxBounds.Ints.exactly(count)).build()))).build());
	}
	
	public static Condition isHolding(ItemLike... items)
	{
		return new NPCEntityPredicate(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(items))).build());
	}
	
	public static Condition hasVisitedSkaia()
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putBoolean("Skaia", true);
		
		return new NPCEntityPredicate(new EntityPredicate.Builder().nbt(new NbtPredicate(nbt)).build());
	}
	
	@SafeVarargs
	public static Condition isAnyEntityType(Supplier<EntityType<ConsortEntity>>... entityType)
	{
		return isAnyEntityType(Arrays.stream(entityType).map(Supplier::get).toArray(EntityType<?>[]::new));
	}
	
	public static Condition isAnyEntityType(EntityType<?>... entityTypes)
	{
		if(entityTypes.length == 1)
			return new NPCEntityPredicate(EntityPredicate.Builder.entity().of(MSTags.EntityTypes.DERSITE_CARAPACIANS).build());
		else
			return new ListCondition(Arrays.stream(entityTypes).<Condition>map(entityType -> new NPCEntityPredicate(EntityPredicate.Builder.entity().of(entityType).build())).toList(), ListCondition.ListType.ANY);
	}
}
