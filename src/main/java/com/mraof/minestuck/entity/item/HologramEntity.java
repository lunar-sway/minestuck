package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.network.NetworkHooks;

public class HologramEntity extends Entity
{

	private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(HologramEntity.class, EntityDataSerializers.ITEM_STACK);
	public int innerRotation;
	
	public HologramEntity(Level level, ItemStack item)
	{
		this(MSEntityTypes.HOLOGRAM, level, item);
	}
	
	public HologramEntity(EntityType<? extends HologramEntity> type, Level level, ItemStack item)
	{
		super(type, level);
		this.innerRotation = this.random.nextInt(100000);
		entityData.set(ITEM, item);
	}

	public HologramEntity(EntityType<? extends HologramEntity> type, Level level)
	{
		this(type, level, new ItemStack(MSBlocks.GENERIC_OBJECT.get()));
	}
	
	public void onUpdate()
    {
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        ++this.innerRotation;

        if (!this.level.isClientSide)
        {
            BlockPos blockpos = blockPosition();

            if (this.level.dimension() == Level.END && this.level.getBlockState(blockpos).getBlock() != Blocks.FIRE)
            {
                this.level.setBlockAndUpdate(blockpos, Blocks.FIRE.defaultBlockState());
            }
        }
    }
	
	@Override
	protected void defineSynchedData()
	{
		this.entityData.define(ITEM, new ItemStack(MSBlocks.GENERIC_OBJECT.get()));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag compound)
	{
		if(compound.contains("Item"))
			setItem(ItemStack.of(compound.getCompound("Item")));
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag compound)
	{
		compound.put("Item", this.getItem().save(new CompoundTag()));
	}

	public ItemStack getItem()
	{
		return entityData.get(ITEM);
	}
	
	public int getItemId()
	{
		return Item.getId(entityData.get(ITEM).getItem());
	}
	
	public void setItem(ItemStack item)
	{
		entityData.set(ITEM,item);
	}
	
	public void setItem(int id)
	{
	}
	
	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}