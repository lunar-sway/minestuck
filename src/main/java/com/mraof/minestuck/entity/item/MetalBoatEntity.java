package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.CustomBoatItem;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MetalBoatEntity extends Boat implements IEntityWithComplexSpawn
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nonnull
	private Type type = Type.IRON;
	
	public MetalBoatEntity(EntityType<? extends MetalBoatEntity> type, Level level)
	{
		super(type, level);
	}
	
	public MetalBoatEntity(Level level, double x, double y, double z, @Nonnull Type type)
	{
		super(MSEntityTypes.METAL_BOAT.get(), level);
		this.blocksBuilding = false;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.type = type;
	}
	
	public Type boatType()
	{
		return type;
	}
	
	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		return super.hurt(source, amount * type.damageModifier);
	}
	
	@Override
	public void tick()
	{
		Status status = getStatus();
		if(status == Status.IN_WATER)
			setDeltaMovement(getDeltaMovement().add(0, -0.1, 0));
		else if(status == Status.UNDER_WATER)
			setDeltaMovement(getDeltaMovement().add(0, -0.02, 0));
		super.tick();
	}
	
	@Override
	public void setDeltaMovement(Vec3 motionIn)
	{
		super.setDeltaMovement(new Vec3(motionIn.x, -Math.abs(motionIn.y), motionIn.z));
	}
	
	private final List<ItemEntity> captureDropsCache = new ArrayList<>(5);
	
	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos)
	{
		Collection<ItemEntity> prevCapture = captureDrops(captureDropsCache);
		
		super.checkFallDamage(y, onGroundIn, state, pos);
		
		//If the boat is broken from the fall, capture the vanilla drops and drop ingots instead
		if(!captureDrops(prevCapture).isEmpty())
		{
			captureDropsCache.clear();
			for(int i = 0; i < 3; i++)
				spawnAtLocation(getDroppedItem());
		}
	}
	
	private Item getDroppedItem()
	{
		return type.droppedItem.get();
	}
	
	@Override
	public Item getDropItem()
	{
		return type.boatItem.get();
	}
	
	@Override
	public void setVariant(Boat.Type pBoatType)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.putString("Type", type.asString());
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		this.type = Type.fromString(compound.getString("Type"));
	}
	
	@Override
	public void writeSpawnData(FriendlyByteBuf buffer)
	{
		buffer.writeUtf(type.asString());
	}
	
	@Override
	public void readSpawnData(FriendlyByteBuf additionalData)
	{
		this.type = Type.fromString(additionalData.readUtf(16));
	}
	
	public enum Type implements CustomBoatItem.BoatProvider
	{
		IRON(1 / 1.5F, () -> Items.IRON_INGOT, MSItems.IRON_BOAT, new ResourceLocation("minestuck", "textures/entity/iron_boat.png")),
		GOLD(1.0F, () -> Items.GOLD_INGOT, MSItems.GOLD_BOAT, new ResourceLocation("minestuck", "textures/entity/gold_boat.png"));
		
		private final float damageModifier;
		private final Supplier<Item> droppedItem, boatItem;
		private final ResourceLocation boatTexture;
		
		Type(float damageModifier, Supplier<Item> droppedItem, Supplier<Item> boatItem, ResourceLocation boatTexture)
		{
			this.damageModifier = damageModifier;
			this.droppedItem = droppedItem;
			this.boatItem = boatItem;
			this.boatTexture = boatTexture;
		}
		
		public String asString()
		{
			return toString().toLowerCase();
		}
		
		public static Type fromString(String name)
		{
			for(Type type : values())
			{
				if(type.asString().equals(name))
					return type;
			}
			LOGGER.error("No minestuck boat type matching string \"{}\"", name);
			return IRON;
		}
		
		public ResourceLocation getBoatTexture()
		{
			return boatTexture;
		}
		
		@Override
		public Entity createBoat(ItemStack stack, Level level, double x, double y, double z)
		{
			return new MetalBoatEntity(level, x, y, z, this);
		}
	}
}