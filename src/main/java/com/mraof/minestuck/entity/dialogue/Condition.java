package com.mraof.minestuck.entity.dialogue;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A Condition controls whether a piece of dialogue will show up
 */
@MethodsReturnNonnullByDefault
public sealed interface Condition
{
	Logger LOGGER = LogUtils.getLogger();
	
	Codec<Condition> CODEC = Type.CODEC.dispatch(Condition::getType, type -> type.codec.get());
	Codec<List<Condition>> LIST_CODEC = Condition.CODEC.listOf();
	
	enum Type implements StringRepresentable
	{
		ALWAYS_TRUE(() -> AlwaysTrue.CODEC),
		HAS_CONDITIONS(() -> HasConditions.CODEC),
		IS_CONSORT(() -> IsConsort.CODEC),
		IS_CARAPACIAN(() -> IsCarapacian.CODEC),
		IS_ENTITY_TYPE(() -> IsEntityType.CODEC),
		IS_ONE_OF_ENTITY_TYPE(() -> IsOneOfEntityType.CODEC),
		IN_ANY_LAND(() -> InAnyLand.CODEC),
		IN_TERRAIN_LAND_TYPE(() -> InTerrainLandType.CODEC),
		IN_TERRAIN_LAND_TYPE_TAG(() -> InTerrainLandTypeTag.CODEC),
		IN_TITLE_LAND_TYPE(() -> InTitleLandType.CODEC),
		IN_TITLE_LAND_TYPE_TAG(() -> InTitleLandTypeTag.CODEC),
		PLAYER_HAS_ITEM(() -> PlayerHasItem.CODEC),
		PLAYER_IS_CLASS(() -> PlayerIsClass.CODEC),
		PLAYER_IS_ASPECT(() -> PlayerIsAspect.CODEC),
		PLAYER_HAS_REPUTATION(() -> PlayerHasReputation.CODEC),
		PLAYER_HAS_BOONDOLLARS(() -> PlayerHasBoondollars.CODEC);
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<Codec<? extends Condition>> codec;
		
		Type(Supplier<Codec<? extends Condition>> codec)
		{
			this.codec = codec;
		}
		
		public static Type fromInt(int ordinal) //converts int back into enum
		{
			if(0 <= ordinal && ordinal < Type.values().length)
				return Type.values()[ordinal];
			else
				throw new IllegalArgumentException("Invalid ordinal of " + ordinal + " for Condition type!");
		}
		
		@Override
		public String getSerializedName()
		{
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
	
	Type getType();
	
	Component getFailureTooltip();
	
	boolean test(LivingEntity entity, ServerPlayer player);
	
	
	/**
	 * CONDITIONS
	 */
	
	enum AlwaysTrue implements Condition
	{
		INSTANCE;
		
		static final Codec<AlwaysTrue> CODEC = Codec.unit(INSTANCE);
		
		@Override
		public Type getType()
		{
			return Type.ALWAYS_TRUE;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.empty();
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return true;
		}
	}
	
	record HasConditions(Conditions conditions) implements Condition
	{
		static final Codec<HasConditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Conditions.CODEC.fieldOf("conditions").forGetter(HasConditions::conditions)
		).apply(instance, HasConditions::new));
		
		@Override
		public Type getType()
		{
			return Type.HAS_CONDITIONS;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(conditions.conditionList().isEmpty())
				return true;
			
			return conditions.testWithContext(entity, player);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return this.conditions.getFailureTooltip();
		}
	}
	
	record IsConsort() implements Condition
	{
		static final Codec<IsConsort> CODEC = Codec.unit(IsConsort::new);
		
		@Override
		public Type getType()
		{
			return Type.IS_CONSORT;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return entity instanceof ConsortEntity;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not consort");
		}
	}
	
	record IsCarapacian() implements Condition
	{
		static final Codec<IsCarapacian> CODEC = Codec.unit(IsCarapacian::new);
		
		@Override
		public Type getType()
		{
			return Type.IS_CARAPACIAN;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return entity instanceof CarapacianEntity;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not carapacian");
		}
	}
	
	record IsEntityType(EntityType<?> entityType) implements Condition
	{
		static final Codec<IsEntityType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity_type").forGetter(IsEntityType::entityType)
		).apply(instance, IsEntityType::new));
		
		@Override
		public Type getType()
		{
			return Type.IS_ENTITY_TYPE;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return entityType != null && entity.getType().equals(entityType);
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is wrong entity type");
		}
	}
	
	record IsOneOfEntityType(List<EntityType<?>> entityTypes) implements Condition
	{
		static final Codec<IsOneOfEntityType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.list(ForgeRegistries.ENTITY_TYPES.getCodec()).fieldOf("entity_type").forGetter(IsOneOfEntityType::entityTypes)
		).apply(instance, IsOneOfEntityType::new));
		
		@Override
		public Type getType()
		{
			return Type.IS_ONE_OF_ENTITY_TYPE;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return !entityTypes.isEmpty() && entityTypes.contains(entity.getType());
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is wrong entity type");
		}
	}
	
	record InAnyLand() implements Condition
	{
		static final Codec<InAnyLand> CODEC = Codec.unit(InAnyLand::new);
		
		@Override
		public Type getType()
		{
			return Type.IN_ANY_LAND;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			return MSDimensions.isLandDimension(entity.getServer(), entity.level().dimension());
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Is not in a Land");
		}
	}
	
	//TODO handle land type tags
	record InTerrainLandType(TerrainLandType landType) implements Condition
	{
		static final Codec<InTerrainLandType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LandTypes.TERRAIN_REGISTRY.get().getCodec().fieldOf("land_type").forGetter(InTerrainLandType::landType)
		).apply(instance, InTerrainLandType::new));
		
		@Override
		public Type getType()
		{
			return Type.IN_TERRAIN_LAND_TYPE;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
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
	
	record InTerrainLandTypeTag(TagKey<TerrainLandType> landTypeTag) implements Condition
	{
		static final Codec<InTerrainLandTypeTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(LandTypes.TERRAIN_REGISTRY.get().getRegistryKey()).fieldOf("land_type_tag").forGetter(InTerrainLandTypeTag::landTypeTag)
		).apply(instance, InTerrainLandTypeTag::new));
		
		@Override
		public Type getType()
		{
			return Type.IN_TERRAIN_LAND_TYPE_TAG;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
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
	
	record InTitleLandType(TitleLandType landType) implements Condition
	{
		static final Codec<InTitleLandType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LandTypes.TITLE_REGISTRY.get().getCodec().fieldOf("land_type").forGetter(InTitleLandType::landType)
		).apply(instance, InTitleLandType::new));
		
		@Override
		public Type getType()
		{
			return Type.IN_TITLE_LAND_TYPE;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
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
	
	record InTitleLandTypeTag(TagKey<TitleLandType> landTypeTag) implements Condition
	{
		static final Codec<InTitleLandTypeTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(LandTypes.TITLE_REGISTRY.get().getRegistryKey()).fieldOf("land_type_tag").forGetter(InTitleLandTypeTag::landTypeTag)
		).apply(instance, InTitleLandTypeTag::new));
		
		@Override
		public Type getType()
		{
			return Type.IN_TITLE_LAND_TYPE_TAG;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
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
	
	record PlayerHasItem(Item item, int amount) implements Condition
	{
		static final Codec<PlayerHasItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(PlayerHasItem::item),
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(PlayerHasItem::amount)
		).apply(instance, PlayerHasItem::new));
		
		@Override
		public Type getType()
		{
			return Type.PLAYER_HAS_ITEM;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			ItemStack stack = Dialogue.findPlayerItem(this.item, player, this.amount);
			return stack != null;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You don't have the required item(s)");
		}
	}
	
	record PlayerIsClass(EnumClass enumClass) implements Condition
	{
		static final Codec<PlayerIsClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Title.CLASS_CODEC.fieldOf("class").forGetter(PlayerIsClass::enumClass)
		).apply(instance, PlayerIsClass::new));
		
		@Override
		public Type getType()
		{
			return Type.PLAYER_IS_CLASS;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return data.getTitle().getHeroClass().equals(enumClass);
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You aren't the required Class");
		}
	}
	
	record PlayerIsAspect(EnumAspect enumAspect) implements Condition
	{
		static final Codec<PlayerIsAspect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Title.ASPECT_CODEC.fieldOf("aspect").forGetter(PlayerIsAspect::enumAspect)
		).apply(instance, PlayerIsAspect::new));
		
		@Override
		public Type getType()
		{
			return Type.PLAYER_IS_ASPECT;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return data.getTitle().getHeroAspect().equals(enumAspect);
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("You aren't the required Aspect");
		}
	}
	
	record PlayerHasReputation(int amount, boolean greaterThan) implements Condition
	{
		static final Codec<PlayerHasReputation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(PlayerHasReputation::amount),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "greater_than", true).forGetter(PlayerHasReputation::greaterThan)
		).apply(instance, PlayerHasReputation::new));
		
		@Override
		public Type getType()
		{
			return Type.PLAYER_HAS_REPUTATION;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			Level level = player.level();
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return greaterThan ? data.getConsortReputation(level.dimension()) > amount : data.getConsortReputation(level.dimension()) < amount;
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Your reputation doesn't meet requirement");
		}
	}
	
	record PlayerHasBoondollars(int amount, boolean greaterThan) implements Condition
	{
		static final Codec<PlayerHasBoondollars> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(PlayerHasBoondollars::amount),
				PreservingOptionalFieldCodec.withDefault(Codec.BOOL, "greater_than", true).forGetter(PlayerHasBoondollars::greaterThan)
		).apply(instance, PlayerHasBoondollars::new));
		
		@Override
		public Type getType()
		{
			return Type.PLAYER_HAS_BOONDOLLARS;
		}
		
		@Override
		public boolean test(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return greaterThan ? data.getBoondollars() > amount : data.getBoondollars() < amount;
			
			return false;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("Your porkhollow doesn't meet requirement");
		}
	}
}