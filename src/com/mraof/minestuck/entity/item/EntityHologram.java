package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.EntityFrog;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityHologram extends Entity implements IEntityAdditionalSpawnData{

	private static final DataParameter<Integer> ITEM = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
	public int innerRotation;
	
	public EntityHologram(World worldIn) 
	{
		super(worldIn);
		this.innerRotation = this.rand.nextInt(100000);
	}

	public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;

        if (!this.world.isRemote)
        {
            BlockPos blockpos = new BlockPos(this);

            if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(blockpos).getBlock() != Blocks.FIRE)
            {
                this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }
	
	@Override
	public void writeSpawnData(ByteBuf data) {
		data.writeInt(dataManager.get(ITEM));
		
	}

	@Override
	public void readSpawnData(ByteBuf data) {
		setItem(data.readInt());
		
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(ITEM, 1);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		if(compound.hasKey("Item")) setItem(new ItemStack(compound.getCompoundTag("Item")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setTag("Item", new ItemStack(this.getItem(),1).writeToNBT(new NBTTagCompound()));
		
	}

	public int getItemId()
	{
		return dataManager.get(ITEM);
	}
	
	public Item getItem()
	{
		return Item.getItemById(dataManager.get(ITEM));
	}
	
	public void setItem(Item item)
	{
		dataManager.set(ITEM,Item.getIdFromItem(item));
	}
	
	public void setItem(int id)
	{
	}
	
	public void setItem(ItemStack stack)
	{
		setItem(stack.getItem());
	}
}
