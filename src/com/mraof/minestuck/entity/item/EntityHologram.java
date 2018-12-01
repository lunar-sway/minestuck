package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.EntityFrog;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityHologram extends Entity{

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityHologram.class, DataSerializers.ITEM_STACK);
	public int innerRotation;
	
	public EntityHologram(World worldIn, ItemStack item) 
	{
		super(worldIn);
		this.innerRotation = this.rand.nextInt(100000);
		dataManager.set(ITEM, item);
	}

	public EntityHologram(World worldIn) 
	{
		this(worldIn, new ItemStack(MinestuckBlocks.genericObject));
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
	protected void entityInit() {
		this.dataManager.register(ITEM, new ItemStack(MinestuckBlocks.genericObject));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		if(compound.hasKey("Item")) setItem(new ItemStack(compound.getCompoundTag("Item")));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
		
	}

	public ItemStack getItem()
	{
		return dataManager.get(ITEM);
	}
	
	public int getItemId()
	{
		return Item.getIdFromItem(dataManager.get(ITEM).getItem());
	}
	
	public void setItem(ItemStack item)
	{
		dataManager.set(ITEM,item);
	}
	
	public void setItem(int id)
	{
	}
}
