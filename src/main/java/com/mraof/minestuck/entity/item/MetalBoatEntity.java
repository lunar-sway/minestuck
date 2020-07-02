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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MetalBoatEntity extends BoatEntity implements IEntityAdditionalSpawnData
{
	
	private int type;	//TODO Replace with enum, or other object-based type
	
	public MetalBoatEntity(EntityType<? extends MetalBoatEntity> type, World world)
	{
		super(type, world);
	}
	
	public MetalBoatEntity(World world, double x, double y, double z, int type)
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
	
	public int boatType()
	{
		return type;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(type == 0)
			return super.attackEntityFrom(source, amount/1.5F);
		else
			return super.attackEntityFrom(source, amount);
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
		if(this.type == 0)
			return Items.IRON_INGOT;
		else if(this.type == 1)
			return Items.GOLD_INGOT;
		throw new IllegalStateException("Unexpected metal boat type: "+type);
	}
	
	@Override
	public Item getItemBoat()
	{
		if(this.type == 0)
			return MSItems.IRON_BOAT;
		else if(this.type == 1)
			return MSItems.GOLD_BOAT;
		throw new IllegalStateException("Unexpected metal boat type: "+type);
	}
	
	@Override
	public void setBoatType(Type boatType)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.putByte("Type", (byte) type);
	}
	
	@Override
	protected void readAdditional(CompoundNBT compound)
	{
		this.type = compound.getByte("Type");
	}
	
	@Override
	public void writeSpawnData(PacketBuffer buffer)
	{
		buffer.writeByte(type);
	}
	
	@Override
	public void readSpawnData(PacketBuffer additionalData)
	{
		this.type = additionalData.readByte();
	}
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}