package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MetalBoatEntity extends BoatEntity implements IEntityAdditionalSpawnData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private Type type;
	
	public MetalBoatEntity(EntityType<? extends MetalBoatEntity> type, World world)
	{
		super(type, world);
	}
	
	public MetalBoatEntity(World world, double x, double y, double z, Type type)
	{
		super(MSEntityTypes.METAL_BOAT, world);
		this.preventEntitySpawning = false;
		this.setPosition(x, y, z);
		this.setMotion(Vec3d.ZERO);
		this.prevPosX = x;
		this.prevPosY = y;
		this.prevPosZ = z;
		this.type = type;
	}
	
	public Type boatType()
	{
		return type;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return super.attackEntityFrom(source, amount*type.damageModifier);
	}
	
	@Override
	public void tick()
	{
		Status status = getBoatStatus();
		if(status == Status.IN_WATER)
			setMotion(getMotion().add(0, -0.1, 0));
		else if(status == Status.UNDER_WATER)
			setMotion(getMotion().add(0, -0.02, 0));
		super.tick();
	}
	
	@Override
	public void setMotion(Vec3d motionIn)
	{
		super.setMotion(new Vec3d(motionIn.x, -Math.abs(motionIn.y), motionIn.z));
	}
	
	private final List<ItemEntity> captureDropsCache = new ArrayList<>(5);
	
	@Override
	protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos)
	{
		Collection<ItemEntity> prevCapture = captureDrops(captureDropsCache);
		
		super.updateFallState(y, onGroundIn, state, pos);
		
		//If the boat is broken from the fall, capture the vanilla drops and drop ingots instead
		if(!captureDrops(prevCapture).isEmpty())
		{
			captureDropsCache.clear();
			for(int i = 0; i < 3; i++)
				entityDropItem(getDroppedItem());
		}
	}
	
	private Item getDroppedItem()
	{
		return type.droppedItem.get();
	}
	
	@Override
	public Item getItemBoat()
	{
		return type.boatItem.get();
	}
	
	@Override
	public void setBoatType(BoatEntity.Type boatType)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.putString("Type", type.asString());
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.type = Type.fromString(compound.getString("Type"));
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeString(type.asString());
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		this.type = Type.fromString(additionalData.readString(16));
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
	
	public enum Type
	{
		IRON(1/1.5F, () -> Items.IRON_INGOT, () -> MSItems.IRON_BOAT, new ResourceLocation("minestuck", "textures/entity/iron_boat.png")),
		GOLD(1.0F, () -> Items.GOLD_INGOT, () -> MSItems.GOLD_BOAT, new ResourceLocation("minestuck", "textures/entity/gold_boat.png"));
		
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
	}
}