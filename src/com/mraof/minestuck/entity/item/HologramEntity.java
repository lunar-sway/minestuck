package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.entity.ModEntityTypes;
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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkHooks;

public class HologramEntity extends Entity
{

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(HologramEntity.class, DataSerializers.ITEMSTACK);
	public int innerRotation;
	
	public HologramEntity(World worldIn, ItemStack item)
	{
		this(ModEntityTypes.HOLOGRAM, worldIn, item);
	}
	
	public HologramEntity(EntityType<? extends HologramEntity> type, World worldIn, ItemStack item)
	{
		super(type, worldIn);
		this.innerRotation = this.rand.nextInt(100000);
		dataManager.set(ITEM, item);
	}

	public HologramEntity(EntityType<? extends HologramEntity> type, World worldIn)
	{
		this(type, worldIn, new ItemStack(MinestuckBlocks.GENERIC_OBJECT));
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
	protected void readAdditional(CompoundNBT compound)
	{
		if(compound.contains("Item"))
			setItem(ItemStack.read(compound.getCompound("Item")));
	}
	
	@Override
	protected void writeAdditional(CompoundNBT compound)
	{
		compound.put("Item", this.getItem().write(new CompoundNBT()));
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
	
	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}