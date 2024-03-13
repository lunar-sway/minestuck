package com.mraof.minestuck.entity.dialogue.condition;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.dialogue.Dialogue;
import com.mraof.minestuck.entity.dialogue.DialogueEntity;
import com.mraof.minestuck.player.*;
import com.mraof.minestuck.util.CodecUtil;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * A Condition controls whether a piece of dialogue will show up
 */
public interface Condition
{
	Logger LOGGER = LogUtils.getLogger();
	
	Codec<Condition> CODEC = CodecUtil.registryCodec(Conditions.REGISTRY).dispatch(Condition::codec, Function.identity());
	Codec<Condition> NPC_ONLY_CODEC = ExtraCodecs.validate(Condition.CODEC,
			condition -> condition.isNpcOnly() ? DataResult.success(condition) : DataResult.error(() -> "Player condition not supported here"));
	
	Codec<? extends Condition> codec();
	
	boolean test(LivingEntity entity, ServerPlayer player);
	
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
		
		static final Codec<AlwaysTrue> CODEC = Codec.unit(INSTANCE);
		
		@Override
		public Codec<AlwaysTrue> codec()
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
		
		static final Codec<FirstTimeGenerating> CODEC = Codec.unit(INSTANCE);
		
		@Override
		public Codec<FirstTimeGenerating> codec()
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
	
	record IsConsort() implements NpcOnlyCondition
	{
		static final Codec<IsConsort> CODEC = Codec.unit(IsConsort::new);
		
		@Override
		public Codec<IsConsort> codec()
		{
			return CODEC;
		}
		
		@Override
		public boolean test(LivingEntity entity)
		{
			return entity instanceof ConsortEntity;
		}
		
		@Override
		public Component getFailureTooltip()
		{
			return Component.literal("NPC is not consort");
		}
	}
	
	record IsCarapacian() implements NpcOnlyCondition
	{
		static final Codec<IsCarapacian> CODEC = Codec.unit(IsCarapacian::new);
		
		@Override
		public Codec<IsCarapacian> codec()
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
	
	record IsEntityType(EntityType<?> entityType) implements NpcOnlyCondition
	{
		static final Codec<IsEntityType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entity_type").forGetter(IsEntityType::entityType)
		).apply(instance, IsEntityType::new));
		
		@Override
		public Codec<IsEntityType> codec()
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
	
	record InAnyLand() implements NpcOnlyCondition
	{
		static final Codec<InAnyLand> CODEC = Codec.unit(InAnyLand::new);
		
		@Override
		public Codec<InAnyLand> codec()
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
	
	//TODO handle land type tags
	record InTerrainLandType(TerrainLandType landType) implements NpcOnlyCondition
	{
		static final Codec<InTerrainLandType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LandTypes.TERRAIN_REGISTRY.get().getCodec().fieldOf("land_type").forGetter(InTerrainLandType::landType)
		).apply(instance, InTerrainLandType::new));
		
		@Override
		public Codec<InTerrainLandType> codec()
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
		static final Codec<InTerrainLandTypeTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(LandTypes.TERRAIN_REGISTRY.get().getRegistryKey()).fieldOf("land_type_tag").forGetter(InTerrainLandTypeTag::landTypeTag)
		).apply(instance, InTerrainLandTypeTag::new));
		
		@Override
		public Codec<InTerrainLandTypeTag> codec()
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
	
	record InTitleLandType(TitleLandType landType) implements NpcOnlyCondition
	{
		static final Codec<InTitleLandType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LandTypes.TITLE_REGISTRY.get().getCodec().fieldOf("land_type").forGetter(InTitleLandType::landType)
		).apply(instance, InTitleLandType::new));
		
		@Override
		public Codec<InTitleLandType> codec()
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
		static final Codec<InTitleLandTypeTag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(LandTypes.TITLE_REGISTRY.get().getRegistryKey()).fieldOf("land_type_tag").forGetter(InTitleLandTypeTag::landTypeTag)
		).apply(instance, InTitleLandTypeTag::new));
		
		@Override
		public Codec<InTitleLandTypeTag> codec()
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
		static final Codec<AtOrAboveY> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.DOUBLE.fieldOf("y").forGetter(AtOrAboveY::y)
		).apply(instance, AtOrAboveY::new));
		@Override
		public Codec<AtOrAboveY> codec()
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
	
	record PlayerHasItem(Item item, int amount) implements Condition
	{
		static final Codec<PlayerHasItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(PlayerHasItem::item),
				PreservingOptionalFieldCodec.withDefault(Codec.INT, "amount", 1).forGetter(PlayerHasItem::amount)
		).apply(instance, PlayerHasItem::new));
		
		@Override
		public Codec<PlayerHasItem> codec()
		{
			return CODEC;
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
		public Codec<PlayerIsClass> codec()
		{
			return CODEC;
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
		public Codec<PlayerIsAspect> codec()
		{
			return CODEC;
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
		public Codec<PlayerHasReputation> codec()
		{
			return CODEC;
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
		public Codec<PlayerHasBoondollars> codec()
		{
			return CODEC;
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