package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.entity.MSEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class HologramEntity extends Entity
{

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.defineId(HologramEntity.class, DataSerializers.ITEM_STACK);
	public int innerRotation;
	
	public HologramEntity(World worldIn, ItemStack item)
	{
		this(MSEntityTypes.HOLOGRAM, worldIn, item);
	}
	
	public HologramEntity(EntityType<? extends HologramEntity> type, World worldIn, ItemStack item)
	{
		super(type, worldIn);
		this.innerRotation = this.random.nextInt(100000);
		entityData.set(ITEM, item);
	}

	public HologramEntity(EntityType<? extends HologramEntity> type, World worldIn)
	{
		this(type, worldIn, new ItemStack(MSBlocks.GENERIC_OBJECT));
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

            if (this.level.dimension() == World.END && this.level.getBlockState(blockpos).getBlock() != Blocks.FIRE)
            {
                this.level.setBlockAndUpdate(blockpos, Blocks.FIRE.defaultBlockState());
            }
        }
    }
	
	@Override
	protected void defineSynchedData()
	{
		this.entityData.define(ITEM, new ItemStack(MSBlocks.GENERIC_OBJECT));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundNBT compound)
	{
		if(compound.contains("Item"))
			setItem(ItemStack.of(compound.getCompound("Item")));
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundNBT compound)
	{
		compound.put("Item", this.getItem().save(new CompoundNBT()));
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
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}