package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
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
import net.minecraft.world.dimension.DimensionType;

public class EntityHologram extends Entity
{

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack>createKey(EntityHologram.class, DataSerializers.ITEM_STACK);
	public int innerRotation;
	
	public EntityHologram(World worldIn, ItemStack item) 
	{
		super(ModEntityTypes.HOLOGRAM, worldIn);
		this.innerRotation = this.rand.nextInt(100000);
		dataManager.set(ITEM, item);
	}

	public EntityHologram(World worldIn) 
	{
		this(worldIn, new ItemStack(MinestuckBlocks.GENERIC_OBJECT));
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

            if (this.world.getDimension().getType() == DimensionType.THE_END && this.world.getBlockState(blockpos).getBlock() != Blocks.FIRE)
            {
                this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }
	
	@Override
	protected void registerData()
	{
		this.dataManager.register(ITEM, new ItemStack(MinestuckBlocks.GENERIC_OBJECT));
	}
	
	@Override
	protected void readAdditional(NBTTagCompound compound)
	{
		if(compound.hasKey("Item"))
			setItem(ItemStack.read(compound.getCompound("Item")));
	}
	
	@Override
	protected void writeAdditional(NBTTagCompound compound)
	{
		compound.setTag("Item", this.getItem().write(new NBTTagCompound()));
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