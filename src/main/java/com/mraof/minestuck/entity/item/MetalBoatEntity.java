package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class MetalBoatEntity extends BoatEntity implements IEntityAdditionalSpawnData
{	//TODO vanilla boat functions differently now. This class will probably need to be updated
	
	public int type;
	private boolean isDropping = false;
	
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
	
	@Override
	public void setDamageTaken(float damage)
	{
		if(type == 0)
			super.setDamageTaken(damage/1.5F);
		else if(type == 1)
			super.setDamageTaken(damage);
	}
	
	/*TODO Look at this and compare to the boat entity
	@Override
	public void tick()
	{
		double pos = posY;
		double motion = motionY;
		captureDrops(new ArrayList<>());
		
		super.tick();
		
		Collection<EntityItem> capturedDrops = captureDrops(null);
		if(isDropping || !capturedDrops.isEmpty())
		{
			double prevMotionX = posX - prevPosX, prevMotionZ = posZ - prevPosZ;
			double maxMotion = type == 0 ? 0.3 : 0.2;
			if(isDropping || Math.sqrt(prevMotionX * prevMotionX + prevMotionZ * prevMotionZ) > maxMotion)
				for(int i = 0; i < 3; i++)
					entityDropItem(getTypeItem(), 1);
			else
			{
				revive();
				this.motionX *= 0.9900000095367432D;
				this.motionY *= 0.949999988079071D;
				this.motionZ *= 0.9900000095367432D;
			}
		}
		
		capturedDrops.clear();
		
		if(!this.world.isMaterialInBB(this.getBoundingBox(), Material.WATER))
			return;
		
		this.motionY = motion;
		setPosition(posX, pos, posZ);
		this.motionY -= type == 0 ? 0.03D : 0.04D;
		motionX /= 1.5;
		motionY /= 1.5;
		motionZ /= 1.5;
		
		move(MoverType.SELF, 0, motionY, 0);
		
	}
	
	@Override
	protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
	{
		if (onGroundIn)
		{
			float fall = type == 0 ? 5.0F : 3.0F;
			if (this.fallDistance > fall)
			{
				this.fall(this.fallDistance, 1.0F);
				
				if (!this.world.isRemote && !this.removed)
				{
					this.remove();
					
					isDropping = true;
				}
				
				this.fallDistance = 0.0F;
			}
		}
		else if (this.world.getBlockState((new BlockPos(this)).down()).getMaterial() != Material.WATER && y < 0.0D)
		{
			this.fallDistance = (float)((double)this.fallDistance - y);
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isInvulnerableTo(source))
			return false;
		else if (!this.world.isRemote && !this.removed)
		{
			if (this.getPassengers().contains(source.getTrueSource()) && source instanceof EntityDamageSourceIndirect)
				return false;
			else
			{
				this.setForwardDirection(-this.getForwardDirection());
				this.setTimeSinceHit(10);
				this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
				this.markVelocityChanged();
				boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).abilities.isCreativeMode;
				
				if (flag || this.getDamageTaken() > 40.0F)
				{
					this.removePassengers();
					
					if (!flag)
						this.entityDropItem(new ItemStack(getBoatItem()), 0.0F);
					
					this.remove();
				}
				
				return true;
			}
		}
		else return true;
	}*/
	
	private Item getTypeItem()
	{
		if(this.type == 0)
			return Items.IRON_INGOT;
		else if(this.type == 1)
			return Items.GOLD_INGOT;
		return null;
	}
	
	private Item getBoatItem()
	{
		if(this.type == 0)
			return MSItems.IRON_BOAT;
		else if(this.type == 1)
			return MSItems.GOLD_BOAT;
		return null;
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