package com.mraof.minestuck.entity.dialogue;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.carapacian.CarapacianEntity;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.player.ClientPlayerData;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import com.mraof.minestuck.util.PreservingOptionalFieldCodec;
import com.mraof.minestuck.world.MSDimensions;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
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
		IS_CONSORT(() -> IsConsort.CODEC, IsConsort::readCondition, "NPC is not consort"),
		IS_CARAPACIAN(() -> IsCarapacian.CODEC, IsCarapacian::readCondition, "NPC is not carapacian"),
		IS_ENTITY_TYPE(() -> IsEntityType.CODEC, IsEntityType::readCondition, "NPC is wrong entity type"),
		IN_ANY_LAND(() -> InAnyLand.CODEC, InAnyLand::readCondition, "Is not in a Land"),
		IN_TERRAIN_LAND_TYPE(() -> InTerrainLandType.CODEC, InTerrainLandType::readCondition, "Is not in specific Land"),
		PLAYER_HAS_ITEM(() -> PlayerHasItem.CODEC, PlayerHasItem::readCondition, "You don't have the required item(s)"),
		PLAYER_HAS_REPUTATION(() -> PlayerHasReputation.CODEC, PlayerHasReputation::readCondition, "Your reputation doesn't meet requirement");
		
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
	
	//TODO since activation of these conditions occurs from a client packet to the server, we may want to check validity
	boolean testCondition(LivingEntity entity, Player player);
	
	static boolean matchesAllConditions(LivingEntity entity, Player player, List<Condition> conditions)
	{
		if(conditions == null)
			return false;
		
		for(Condition condition : conditions)
		{
			if(!condition.testCondition(entity, player))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 *
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
		public boolean testCondition(LivingEntity entity, Player player)
		{
			return true;
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
		public boolean testCondition(LivingEntity entity, Player player)
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
		public boolean testCondition(LivingEntity entity, Player player)
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
		public boolean testCondition(LivingEntity entity, Player player)
		{
			return entityType != null && entity.getType().equals(entityType);
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
		public boolean testCondition(LivingEntity entity, Player player)
		{
			if(!entity.level().isClientSide)
			{
				return MSDimensions.isLandDimension(entity.getServer(), entity.level().dimension());
			}
			
			return false;
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
		public boolean testCondition(LivingEntity entity, Player player)
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
		public boolean testCondition(LivingEntity entity, Player player)
		{
			ItemStack stack = Dialogue.findPlayerItem(this.item, player, this.amount);
			return stack != null;
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
		public boolean testCondition(LivingEntity entity, Player player)
		{
			if(player == null)
				return false;
			
			Level level = player.level();
			
			if(level.isClientSide)
			{
				return greaterThan ? ClientPlayerData.getConsortReputation() > amount : ClientPlayerData.getConsortReputation() < amount;
			} else
			{
				PlayerData data = PlayerSavedData.getData((ServerPlayer) player);
				if(data != null)
					return greaterThan ? data.getConsortReputation(level.dimension()) > amount : data.getConsortReputation(level.dimension()) < amount;
			}
			
			return false;
		}
	}
}