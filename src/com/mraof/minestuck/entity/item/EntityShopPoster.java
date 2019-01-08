package com.mraof.minestuck.entity.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityShopPoster extends EntityHangingArt<EntityShopPoster.ShopArt>
{
	protected int dmg = 0;
	private static final DataParameter<Integer> TYPE = EntityDataManager.<Integer>createKey(EntityFrog.class, DataSerializers.VARINT);
	
	public EntityShopPoster(World worldIn)
	{
		super(worldIn);
	}
	
	protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(TYPE, 0);
        
    }
	
	public EntityShopPoster(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
	{
		super(worldIn, pos, facing, stack, meta, false);
		setType(meta);
	}
	
	@Override
	public Set<ShopArt> getArtSet()
	{
		return EnumSet.allOf(ShopArt.class);
	}
	
	@Override
	public ShopArt getDefault()
	{
		return ShopArt.FRAYMOTIFS;
	}
	
	@Override
	public ItemStack getStackDropped()
	{
		return new ItemStack(MinestuckItems.shopPoster, 1, getType());
	}
	
	private void setType(int i) 
    {
    	this.dataManager.set(TYPE, i);
	}

	public int getType() 
	{
		return this.dataManager.get(TYPE);
	}
	
	//NBT
		public void writeEntityToNBT(NBTTagCompound compound)
	    {
	        super.writeEntityToNBT(compound);
	        compound.setInteger("Type", this.getType());
	    }

	    public void readEntityFromNBT(NBTTagCompound compound)
	    {
	        super.readEntityFromNBT(compound);
	        
	        if(compound.hasKey("Type")) setType(compound.getInteger("Type"));
	        else setType(0);
	    }
	
	public static enum ShopArt implements EntityHangingArt.IArt
	{
		FRAYMOTIFS("Fraymotifs", 48, 48, 0, 0),
		FOOD("Food", 48, 48, 48, 0),
		HATS("Hats", 48, 48, 96, 0),
		GENERAL("General", 48, 48, 144, 0),
		CANDY("Candy", 48, 48, 0, 48);
		
		private final String title;
		private final int sizeX, sizeY;
		private final int offsetX, offsetY;
		
		private ShopArt(String title, int sizeX, int sizeY, int offsetX, int offsetY)
		{
			this.title = title;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}
		
		
		
		@Override
		public String getTitle()
		{return title;}
		@Override
		public int getSizeX()
		{return sizeX;}
		@Override
		public int getSizeY()
		{return sizeY;}
		@Override
		public int getOffsetX()
		{return offsetX;}
		@Override
		public int getOffsetY()
		{return offsetY;}
	}
}