package com.mraof.minestuck.entity.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mraof.minestuck.entity.EntityFrog;
import com.mraof.minestuck.entity.ModEntityTypes;
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
		super(ModEntityTypes.SHOP_POSTER, worldIn);
	}
	
	
	public EntityShopPoster(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
	{
		super(ModEntityTypes.SHOP_POSTER, worldIn, pos, facing, stack, meta, false);
		setPosterType(meta);
	}
	
	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(TYPE, 0);
		
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
		return new ItemStack(MinestuckItems.SHOP_POSTER);//, 1, getPosterType()); TODO poster subtypes
	}
	
	private void setPosterType(int i)
    {
    	this.dataManager.set(TYPE, i);
	}

	public int getPosterType()
	{
		return this.dataManager.get(TYPE);
	}
	
	//NBT
	
	@Override
	public void writeAdditional(NBTTagCompound compound)
	{
		super.writeAdditional(compound);
		compound.setInt("Type", this.getPosterType());
	}
	
	@Override
	public void readAdditional(NBTTagCompound compound)
	{
		super.readAdditional(compound);
		if(compound.hasKey("Type")) setPosterType(compound.getInt("Type"));
		else setPosterType(0);
	}
	
	public enum ShopArt implements EntityHangingArt.IArt
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