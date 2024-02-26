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
import net.minecraft.network.FriendlyByteBuf;
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

import java.util.*;
import java.util.function.Function;
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
	
	static Condition read(FriendlyByteBuf buffer)
	{
		Type type = Type.fromInt(buffer.readInt());
		return type.bufferReader.apply(buffer);
	}
	
	default void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(this.getType().ordinal());
		this.writeCondition(buffer);
	}
	
	enum Type implements StringRepresentable
	{
		CONDITIONLESS(() -> Conditionless.CODEC, Conditionless::readCondition, ""),
		HAS_CONDITIONS(() -> HasConditions.CODEC, HasConditions::readCondition, ""),
		IS_CONSORT(() -> IsConsort.CODEC, IsConsort::readCondition, "NPC is not consort"),
		IS_CARAPACIAN(() -> IsCarapacian.CODEC, IsCarapacian::readCondition, "NPC is not carapacian"),
		IS_ENTITY_TYPE(() -> IsEntityType.CODEC, IsEntityType::readCondition, "NPC is wrong entity type"),
		IS_ONE_OF_ENTITY_TYPE(() -> IsOneOfEntityType.CODEC, IsOneOfEntityType::readCondition, "NPC is wrong entity type"),
		IN_ANY_LAND(() -> InAnyLand.CODEC, InAnyLand::readCondition, "Is not in a Land"),
		IN_TERRAIN_LAND_TYPE(() -> InTerrainLandType.CODEC, InTerrainLandType::readCondition, "Is not in specific Land"),
		IN_TERRAIN_LAND_TYPE_TAG(() -> InTerrainLandTypeTag.CODEC, InTerrainLandTypeTag::readCondition, "Is not in specific Land tag"),
		IN_TITLE_LAND_TYPE(() -> InTitleLandType.CODEC, InTitleLandType::readCondition, "Is not in specific Land"),
		IN_TITLE_LAND_TYPE_TAG(() -> InTitleLandTypeTag.CODEC, InTitleLandTypeTag::readCondition, "Is not in specific Land tag"),
		PLAYER_HAS_ITEM(() -> PlayerHasItem.CODEC, PlayerHasItem::readCondition, "You don't have the required item(s)"),
		PLAYER_IS_CLASS(() -> PlayerIsClass.CODEC, PlayerIsClass::readCondition, "You aren't the required Class"),
		PLAYER_IS_ASPECT(() -> PlayerIsAspect.CODEC, PlayerIsAspect::readCondition, "You aren't the required Aspect"),
		PLAYER_HAS_REPUTATION(() -> PlayerHasReputation.CODEC, PlayerHasReputation::readCondition, "Your reputation doesn't meet requirement"),
		PLAYER_HAS_BOONDOLLARS(() -> PlayerHasBoondollars.CODEC, PlayerHasBoondollars::readCondition, "Your porkhollow doesn't meet requirement");
		
		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		
		private final Supplier<Codec<? extends Condition>> codec;
		private final Function<FriendlyByteBuf, Condition> bufferReader;
		private final String failureTooltip;
		
		Type(Supplier<Codec<? extends Condition>> codec, Function<FriendlyByteBuf, Condition> bufferReader, String failureTooltip)
		{
			this.codec = codec;
			this.bufferReader = bufferReader;
			this.failureTooltip = failureTooltip;
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
	
	void writeCondition(FriendlyByteBuf buffer);
	
	Type getType();
	
	default String getFailureTooltip()
	{
		return getType().failureTooltip;
	}
	
	boolean testCondition(LivingEntity entity, ServerPlayer player);
	
	
	/**
	 * CONDITIONS
	 */
	
	record Conditionless() implements Condition
	{
		static final Codec<Conditionless> CODEC = Codec.unit(Conditionless::new);
		
		@Override
		public Type getType()
		{
			return Type.CONDITIONLESS;
		}
		
		static Conditionless readCondition(FriendlyByteBuf buffer)
		{
			return new Conditionless();
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
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
		
		static HasConditions readCondition(FriendlyByteBuf buffer)
		{
			int ordinal = buffer.readInt();
			
			List<Condition> conditionList = new ArrayList<>();
			while(buffer.isReadable())
			{
				conditionList.add(Condition.read(buffer));
			}
			
			Conditions conditions = new Conditions(conditionList, Conditions.Type.fromInt(ordinal));
			
			return new HasConditions(conditions);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeInt(conditions.type().ordinal());
			conditions.conditionList().forEach(condition -> condition.write(buffer));
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			if(conditions.conditionList().isEmpty())
				return true;
			
			return conditions.testWithContext(entity, player);
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
		
		static IsConsort readCondition(FriendlyByteBuf buffer)
		{
			return new IsConsort();
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			return entity instanceof ConsortEntity;
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
		
		static IsCarapacian readCondition(FriendlyByteBuf buffer)
		{
			return new IsCarapacian();
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			return entity instanceof CarapacianEntity;
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
		
		static IsEntityType readCondition(FriendlyByteBuf buffer)
		{
			EntityType<?> entityType = buffer.readRegistryIdSafe(EntityType.class);
			return new IsEntityType(entityType);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeRegistryId(ForgeRegistries.ENTITY_TYPES, this.entityType);
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			return entityType != null && entity.getType().equals(entityType);
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
		
		static IsOneOfEntityType readCondition(FriendlyByteBuf buffer)
		{
			List<EntityType<?>> entityTypes = new ArrayList<>();
			while(buffer.isReadable())
			{
				entityTypes.add(buffer.readRegistryIdSafe(EntityType.class));
			}
			
			return new IsOneOfEntityType(entityTypes);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			for(EntityType<?> entityType : entityTypes)
			{
				buffer.writeRegistryId(ForgeRegistries.ENTITY_TYPES, entityType);
			}
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			return !entityTypes.isEmpty() && entityTypes.contains(entity.getType());
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
		
		static InAnyLand readCondition(FriendlyByteBuf buffer)
		{
			return new InAnyLand();
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			return MSDimensions.isLandDimension(entity.getServer(), entity.level().dimension());
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
		
		static InTerrainLandType readCondition(FriendlyByteBuf buffer)
		{
			return new InTerrainLandType(LandTypes.TERRAIN_REGISTRY.get().getValue(buffer.readResourceLocation()));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			//TODO is it safe to require non null here?
			buffer.writeResourceLocation(Objects.requireNonNull(LandTypes.TERRAIN_REGISTRY.get().getKey(landType)));
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
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
		
		static InTerrainLandTypeTag readCondition(FriendlyByteBuf buffer)
		{
			return new InTerrainLandTypeTag(TagKey.create(LandTypes.TERRAIN_REGISTRY.get().getRegistryKey(), buffer.readResourceLocation()));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(landTypeTag.location());
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
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
		
		static InTitleLandType readCondition(FriendlyByteBuf buffer)
		{
			return new InTitleLandType(LandTypes.TITLE_REGISTRY.get().getValue(buffer.readResourceLocation()));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			//TODO is it safe to require non null here?
			buffer.writeResourceLocation(Objects.requireNonNull(LandTypes.TITLE_REGISTRY.get().getKey(landType)));
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
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
		
		static InTitleLandTypeTag readCondition(FriendlyByteBuf buffer)
		{
			return new InTitleLandTypeTag(TagKey.create(LandTypes.TITLE_REGISTRY.get().getRegistryKey(), buffer.readResourceLocation()));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeResourceLocation(landTypeTag.location());
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
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
		
		static PlayerHasItem readCondition(FriendlyByteBuf buffer)
		{
			Item item = buffer.readRegistryIdSafe(Item.class);
			int amount = buffer.readInt();
			return new PlayerHasItem(item, amount);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeRegistryId(ForgeRegistries.ITEMS, this.item);
			buffer.writeInt(this.amount);
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			ItemStack stack = Dialogue.findPlayerItem(this.item, player, this.amount);
			return stack != null;
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
		
		static PlayerIsClass readCondition(FriendlyByteBuf buffer)
		{
			int ordinal = buffer.readInt();
			return new PlayerIsClass(EnumClass.getClassFromInt(ordinal));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.enumClass.ordinal());
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return data.getTitle().getHeroClass().equals(enumClass);
			
			return false;
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
		
		static PlayerIsAspect readCondition(FriendlyByteBuf buffer)
		{
			int ordinal = buffer.readInt();
			return new PlayerIsAspect(EnumAspect.getAspectFromInt(ordinal));
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.enumAspect.ordinal());
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return data.getTitle().getHeroAspect().equals(enumAspect);
			
			return false;
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
		
		static PlayerHasReputation readCondition(FriendlyByteBuf buffer)
		{
			int amount = buffer.readInt();
			boolean greaterThan = buffer.readBoolean();
			return new PlayerHasReputation(amount, greaterThan);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.amount);
			buffer.writeBoolean(this.greaterThan);
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			Level level = player.level();
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return greaterThan ? data.getConsortReputation(level.dimension()) > amount : data.getConsortReputation(level.dimension()) < amount;
			
			return false;
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
		
		static PlayerHasBoondollars readCondition(FriendlyByteBuf buffer)
		{
			int amount = buffer.readInt();
			boolean greaterThan = buffer.readBoolean();
			return new PlayerHasBoondollars(amount, greaterThan);
		}
		
		@Override
		public void writeCondition(FriendlyByteBuf buffer)
		{
			buffer.writeInt(this.amount);
			buffer.writeBoolean(this.greaterThan);
		}
		
		@Override
		public boolean testCondition(LivingEntity entity, ServerPlayer player)
		{
			if(player == null)
				return false;
			
			PlayerData data = PlayerSavedData.getData(player);
			if(data != null)
				return greaterThan ? data.getBoondollars() > amount : data.getBoondollars() < amount;
			
			return false;
		}
	}
}