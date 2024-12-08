package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.carapacian.EnumEntityKingdom;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.ConsortReputation;
import com.mraof.minestuck.entity.consort.EnumConsort;
import com.mraof.minestuck.entity.dialogue.DialogueComponent;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.skaianet.SburbPlayerData;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.Scoreboard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A Condition controls whether a piece of dialogue will show up
 */
public interface Condition
{
	Codec<Condition> CODEC = Conditions.REGISTRY.byNameCodec().dispatch(Condition::codec, Function.identity());
	Codec<Condition> NPC_ONLY_CODEC = Condition.CODEC.validate(
			condition -> condition.isNpcOnly() ? DataResult.success(condition) : DataResult.error(() -> "Player condition not supported here"));
	
	MapCodec<? extends Condition> codec();
	
	boolean test(LivingEntity entity, ServerPlayer player);
	
	//todo condition tooltips should be made better: translatable text should be used over literals, and the fields of the condition can be reflected in the tooltip. (like saying which class specifically that the player need to have)
	default Component getFailureTooltip()
	{
		return Component.empty();
	}
	
	default boolean isNpcOnly()
	{
		return false;
	}
	
	interface NpcOnlyCondition extends Condition
	{
		boolean test(LivingEntity entity);
		
		@Override
		default boolean test(LivingEntity entity, ServerPlayer player)
		{
			return test(entity);
		}
		
		@Override
		default boolean isNpcOnly()
		{
			return true;
		}
	}
	
	/**
	 * CONDITIONS
	 */
	
	enum AlwaysTrue implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<AlwaysTrue> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<AlwaysTrue> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return true;
		}
	}
	
	enum FirstTimeGenerating implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<FirstTimeGenerating> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<FirstTimeGenerating> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
				return !dialogueEntity.getDialogueComponent().hasGeneratedOnce();
			else
				return false;
		}
	}
	
	record IsCarapacian() implements NpcOnlyCondition
	{
		static final MapCodec<IsCarapacian> CODEC = MapCodec.unit(IsCarapacian::new);
		
		@Override
		public MapCodec<IsCarapacian> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof CarapacianEntity;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not carapacian");
		}
	}
	
	record IsFromKingdom(EnumEntityKingdom kingdom) implements NpcOnlyCondition
	{
		static final MapCodec<IsFromKingdom> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EnumEntityKingdom.CODEC.fieldOf("kingdom").forGetter(IsFromKingdom::kingdom)
		).apply(instance, IsFromKingdom::new));
		
		@Override
		public MapCodec<IsFromKingdom> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof CarapacianEntity carapacianEntity && carapacianEntity.getKingdom() == kingdom;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not from the correct kingdom");
		}
	}
	
	record IsEntityType(EntityType<?> entityType) implements NpcOnlyCondition
	{
		static final MapCodec<IsEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(IsEntityType::entityType)
		).apply(instance, IsEntityType::new));
		
		@Override
		public MapCodec<IsEntityType> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entityType != null && entity.getType().equals(entityType);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is wrong entity type");
		}
	}
	
	enum IsInLand implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<IsInLand> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<IsInLand> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return MSDimensions.isLandDimension(entity.getServer(), entity.level().dimension());
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in a Land");
		}
	}
	
	enum IsConsortFromLand implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<IsConsortFromLand> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<IsConsortFromLand> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof ConsortEntity consort
					&& MSDimensions.isLandDimension(entity.getServer(), consort.getHomeDimension());
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not from a Land");
		}
	}
	
	enum IsConsortInHomeLand implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<IsConsortInHomeLand> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<IsConsortInHomeLand> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof ConsortEntity consort
					&& MSDimensions.isLandDimension(entity.getServer(), consort.getHomeDimension())
					&& consort.level().dimension().equals(consort.getHomeDimension());
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in home Land");
		}
	}
	
	record InTerrainLandType(TerrainLandType landType) implements NpcOnlyCondition
	{
		static final MapCodec<InTerrainLandType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LandTypes.TERRAIN_REGISTRY.byNameCodec().fieldOf("land_type").forGetter(InTerrainLandType::landType)
		).apply(instance, InTerrainLandType::new));
		
		@Override
		public MapCodec<InTerrainLandType> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(landType == null)
				return false;
			
			if(entity.level() instanceof ServerLevel serverLevel)
			{
				Optional<LandTypePair.Named> potentialLandTypes = LandTypePair.getNamed(serverLevel);
				if(potentialLandTypes.isPresent())
					return potentialLandTypes.get().landTypes().getTerrain() == landType;
			}
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in specific Land");
		}
	}
	
	record InTerrainLandTypeTag(TagKey<TerrainLandType> landTypeTag) implements NpcOnlyCondition
	{
		static final MapCodec<InTerrainLandTypeTag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				TagKey.codec(LandTypes.TERRAIN_REGISTRY.key()).fieldOf("land_type_tag").forGetter(InTerrainLandTypeTag::landTypeTag)
		).apply(instance, InTerrainLandTypeTag::new));
		
		@Override
		public MapCodec<InTerrainLandTypeTag> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(landTypeTag == null)
				return false;
			
			if(entity.level() instanceof ServerLevel serverLevel)
			{
				Optional<LandTypePair.Named> potentialLandTypes = LandTypePair.getNamed(serverLevel);
				if(potentialLandTypes.isPresent())
					return potentialLandTypes.get().landTypes().getTerrain().is(landTypeTag);
			}
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in specific Land tag");
		}
	}
	
	record InConsortTerrainLandType(EnumConsort consort) implements NpcOnlyCondition
	{
		static final MapCodec<InConsortTerrainLandType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EnumConsort.CODEC.fieldOf("consort").forGetter(InConsortTerrainLandType::consort)
		).apply(instance, InConsortTerrainLandType::new));
		
		@Override
		public MapCodec<InConsortTerrainLandType> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(entity.level() instanceof ServerLevel serverLevel)
			{
				Optional<LandTypePair.Named> potentialLandTypes = LandTypePair.getNamed(serverLevel);
				if(potentialLandTypes.isPresent())
				{
					return potentialLandTypes.get().landTypes().getTerrain().getConsortType() == consort.getConsortType();
				}
			}
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in specific Land");
		}
	}
	
	record InTitleLandType(TitleLandType landType) implements NpcOnlyCondition
	{
		static final MapCodec<InTitleLandType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LandTypes.TITLE_REGISTRY.byNameCodec().fieldOf("land_type").forGetter(InTitleLandType::landType)
		).apply(instance, InTitleLandType::new));
		
		@Override
		public MapCodec<InTitleLandType> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(landType == null)
				return false;
			
			if(entity.level() instanceof ServerLevel serverLevel)
			{
				Optional<LandTypePair.Named> potentialLandTypes = LandTypePair.getNamed(serverLevel);
				if(potentialLandTypes.isPresent())
					return potentialLandTypes.get().landTypes().getTitle() == landType;
			}
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in specific Land");
		}
	}
	
	record InTitleLandTypeTag(TagKey<TitleLandType> landTypeTag) implements NpcOnlyCondition
	{
		static final MapCodec<InTitleLandTypeTag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				TagKey.codec(LandTypes.TITLE_REGISTRY.key()).fieldOf("land_type_tag").forGetter(InTitleLandTypeTag::landTypeTag)
		).apply(instance, InTitleLandTypeTag::new));
		
		@Override
		public MapCodec<InTitleLandTypeTag> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			if(landTypeTag == null)
				return false;
			
			if(entity.level() instanceof ServerLevel serverLevel)
			{
				Optional<LandTypePair.Named> potentialLandTypes = LandTypePair.getNamed(serverLevel);
				if(potentialLandTypes.isPresent())
					return potentialLandTypes.get().landTypes().getTitle().is(landTypeTag);
			}
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in specific Land tag");
		}
	}
	
	record AtOrAboveY(double y) implements NpcOnlyCondition
	{
		static final MapCodec<AtOrAboveY> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.DOUBLE.fieldOf("y").forGetter(AtOrAboveY::y)
		).apply(instance, AtOrAboveY::new));
		@Override
		public MapCodec<AtOrAboveY> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return this.y <= entity.getY();
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not at the right height");
		}
	}
	
	record NPCIsHoldingItem(Item item) implements NpcOnlyCondition
	{
		static final MapCodec<NPCIsHoldingItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(NPCIsHoldingItem::item)
		).apply(instance, NPCIsHoldingItem::new));
		
		@Override
		public MapCodec<NPCIsHoldingItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity.getMainHandItem().is(item) || entity.getOffhandItem().is(item);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not holding the right item");
		}
	}
	
	record PlayerHasItem(Item item, int amount) implements Condition
	{
		static final MapCodec<PlayerHasItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(PlayerHasItem::item),
				Codec.INT.optionalFieldOf("amount", 1).forGetter(PlayerHasItem::amount)
		).apply(instance, PlayerHasItem::new));
		
		@Override
		public MapCodec<PlayerHasItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			ItemStack stack = findPlayerItem(this.item, player, this.amount);
			return stack != null;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You don't have the required item(s)");
		}
		
		@Nullable
		public static ItemStack findPlayerItem(Item item, Player player, int minAmount)
		{
			for(ItemStack invItem : player.getInventory().items)
			{
				if(invItem.is(item))
				{
					if(invItem.getCount() >= minAmount)
						return invItem;
				}
			}
			
			return null;
		}
	}
	
	/**
	 * Looks for an item in the player inventory that matches the tag.
	 * If an item matches, it will be set for this player to be available for
	 * the {@link com.mraof.minestuck.entity.dialogue.DialogueMessage.Argument#MATCHED_ITEM} argument
	 * and {@link com.mraof.minestuck.entity.dialogue.Trigger.TakeMatchedItem} trigger.
	 */
	record ItemTagMatch(TagKey<Item> itemTag) implements Condition
	{
		static final MapCodec<ItemTagMatch> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(ItemTagMatch::itemTag)
		).apply(instance, ItemTagMatch::new));
		@Override
		public MapCodec<ItemTagMatch> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> lastMatched = component.getMatchedItem(player);
			if(lastMatched.isPresent() && lastMatched.get().builtInRegistryHolder().is(this.itemTag) && PlayerHasItem.findPlayerItem(lastMatched.get(), player, 1) != null)
				return true;
			
			Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(this.itemTag);
			List<Item> items = tag.stream().flatMap(HolderSet.ListBacked::stream).map(Holder::value)
					.collect(Collectors.toCollection(ArrayList::new));
			while(!items.isEmpty())
			{
				Item nextItem = items.remove(entity.getRandom().nextInt(items.size()));
				if(PlayerHasItem.findPlayerItem(nextItem, player, 1) != null)
				{
					component.setMatchedItem(nextItem, player);
					return true;
				}
			}
			
			component.clearMatchedItem(player);
			return false;
		}
	}
	
	record ItemTagMatchExclude(TagKey<Item> itemTag, Item exclusionItem) implements Condition
	{
		static final MapCodec<ItemTagMatchExclude> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(ItemTagMatchExclude::itemTag),
				BuiltInRegistries.ITEM.byNameCodec().fieldOf("exclusion_item").forGetter(ItemTagMatchExclude::exclusionItem)
		).apply(instance, ItemTagMatchExclude::new));
		
		@Override
		public MapCodec<ItemTagMatchExclude> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> lastMatched = component.getMatchedItem(player);
			if(lastMatched.isPresent() && lastMatched.get().builtInRegistryHolder().is(this.itemTag) && PlayerHasItem.findPlayerItem(lastMatched.get(), player, 1) != null)
				return true;
			
			Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(this.itemTag);
			List<Item> items = tag.stream().flatMap(HolderSet.ListBacked::stream).map(Holder::value)
					.filter(item -> item != exclusionItem).collect(Collectors.toCollection(ArrayList::new));
			while(!items.isEmpty())
			{
				Item nextItem = items.remove(entity.getRandom().nextInt(items.size()));
				if(PlayerHasItem.findPlayerItem(nextItem, player, 1) != null)
				{
					component.setMatchedItem(nextItem, player);
					return true;
				}
			}
			
			component.clearMatchedItem(player);
			return false;
		}
	}
	
	/**
	 * Specifically checks the item set by {@link ItemTagMatch} to make sure that it's still present.
	 */
	enum HasMatchedItem implements Condition
	{
		INSTANCE;
		static final MapCodec<HasMatchedItem> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<HasMatchedItem> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			DialogueComponent component = ((DialogueEntity) entity).getDialogueComponent();
			Optional<Item> lastMatched = component.getMatchedItem(player);
			return lastMatched.isPresent() && PlayerHasItem.findPlayerItem(lastMatched.get(), player, 1) != null;
		}
	}
	
	record PlayerIsClass(EnumClass enumClass) implements Condition
	{
		static final MapCodec<PlayerIsClass> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EnumClass.CODEC.fieldOf("class").forGetter(PlayerIsClass::enumClass)
		).apply(instance, PlayerIsClass::new));
		
		@Override
		public MapCodec<PlayerIsClass> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			return Title.getTitle(player)
					.map(value -> value.heroClass().equals(enumClass))
					.orElse(false);
			
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You aren't the required Class");
		}
	}
	
	record PlayerIsAspect(EnumAspect enumAspect) implements Condition
	{
		static final MapCodec<PlayerIsAspect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				EnumAspect.CODEC.fieldOf("aspect").forGetter(PlayerIsAspect::enumAspect)
		).apply(instance, PlayerIsAspect::new));
		
		@Override
		public MapCodec<PlayerIsAspect> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			return Title.isPlayerOfAspect(player, this.enumAspect);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You aren't the required Aspect");
		}
	}
	
	record PlayerHasReputation(int amount, boolean greaterThan) implements Condition
	{
		static final MapCodec<PlayerHasReputation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf("amount", 1).forGetter(PlayerHasReputation::amount),
				Codec.BOOL.optionalFieldOf("greater_than", true).forGetter(PlayerHasReputation::greaterThan)
		).apply(instance, PlayerHasReputation::new));
		
		@Override
		public MapCodec<PlayerHasReputation> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			Level level = player.level();
			
			Optional<ConsortReputation> reputation = PlayerData.get(player).map(ConsortReputation::get);
			if(reputation.isEmpty())
				return false;
			
			return greaterThan
					? reputation.get().getConsortReputation(level.dimension()) > amount
					: reputation.get().getConsortReputation(level.dimension()) < amount;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Your reputation doesn't meet requirement");
		}
	}
	
	record PlayerHasBoondollars(int amount) implements Condition
	{
		static final MapCodec<PlayerHasBoondollars> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf("amount", 1).forGetter(PlayerHasBoondollars::amount)
		).apply(instance, PlayerHasBoondollars::new));
		
		@Override
		public MapCodec<PlayerHasBoondollars> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			Optional<PlayerData> data = PlayerData.get(player);
			if(data.isPresent())
				return PlayerBoondollars.getBoondollars(data.get()) >= amount;
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Your porkhollow doesn't meet requirement");
		}
	}
	
	enum PlayerHasEntered implements Condition
	{
		INSTANCE;
		
		static final MapCodec<PlayerHasEntered> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<PlayerHasEntered> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			return SburbPlayerData.get(player).hasEntered();
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Player has not Entered");
		}
	}
	
	record CustomHasScore(int value, String ownerName, String objectiveName) implements Condition
	{
		static final MapCodec<CustomHasScore> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.optionalFieldOf("value", 0).forGetter(CustomHasScore::value),
				Codec.STRING.fieldOf("owner_name").forGetter(CustomHasScore::ownerName),
				Codec.STRING.fieldOf("objective_name").forGetter(CustomHasScore::objectiveName)
		).apply(instance, CustomHasScore::new));
		
		@Override
		public MapCodec<CustomHasScore> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			Scoreboard scoreboard = player.getScoreboard();
			
			Objective objective = scoreboard.getObjective(objectiveName);
			if(objective == null)
				return false;
			
			Collection<PlayerScoreEntry> scores = scoreboard.listPlayerScores(objective);
			
			//go with originally written scoreboard name if not "player" or "npc"
			String modOwnerName = switch(ownerName)
			{
				case "player" -> player.getScoreboardName();
				case "npc" -> entity.getScoreboardName();
				default -> ownerName;
			};
			
			return scores.stream().filter(score -> score.owner().equals(modOwnerName)).anyMatch(score -> score.value() == value);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("A custom scoreboard value does not match");
		}
	}
	
	enum HasMoveRestriction implements NpcOnlyCondition
	{
		INSTANCE;
		
		static final MapCodec<HasMoveRestriction> CODEC = MapCodec.unit(INSTANCE);
		
		@Override
		public MapCodec<HasMoveRestriction> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof Mob mob && mob.hasRestriction();
		}
	}
	
	record Flag(String flag) implements Condition
	{
		static final MapCodec<Flag> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.STRING.fieldOf("flag").forGetter(Flag::flag)
		).apply(instance, Flag::new));
		
		@Override
		public MapCodec<Flag> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(entity instanceof DialogueEntity dialogueEntity)
			{
				DialogueComponent component = dialogueEntity.getDialogueComponent();
				return component.flags().contains(this.flag) || component.playerFlags(player).contains(this.flag);
			}
			return false;
		}
	}
	
	record NearSpawn(int maxDistance) implements NpcOnlyCondition
	{
		static final MapCodec<NearSpawn> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.INT.fieldOf("max_distance").forGetter(NearSpawn::maxDistance)
		).apply(instance, NearSpawn::new));
		
		@Override
		public MapCodec<NearSpawn> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			LevelData levelData = entity.level().getLevelData();
			Vec3 spawn = Vec3.atCenterOf(levelData.getSpawnPos());
			return entity.distanceToSqr(spawn) <= maxDistance * maxDistance;
		}
	}
	
	enum HasPlayerEntered implements Condition
	{
		INSTANCE;
		static final MapCodec<HasPlayerEntered> CODEC = MapCodec.unit(INSTANCE);
		
		
		@Override
		public MapCodec<HasPlayerEntered> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return SburbPlayerData.get(player).hasEntered();
		}
	}
	
	enum IsInSkaia implements NpcOnlyCondition
	{
		INSTANCE;
		static final MapCodec<IsInSkaia> CODEC = MapCodec.unit(INSTANCE);
		
		
		@Override
		public MapCodec<IsInSkaia> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return MSDimensions.isSkaia(entity.level().dimension());
		}
	}
	
	enum ConsortVisitedSkaia implements NpcOnlyCondition
	{
		INSTANCE;
		static final MapCodec<ConsortVisitedSkaia> CODEC = MapCodec.unit(INSTANCE);
		
		
		@Override
		public MapCodec<ConsortVisitedSkaia> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof ConsortEntity consort && consort.visitedSkaia();
		}
	}
	
}
