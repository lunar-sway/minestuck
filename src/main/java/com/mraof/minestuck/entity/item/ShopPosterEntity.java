package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public class ShopPosterEntity extends HangingArtEntity<ShopPosterEntity.ShopArt>
{
	protected int dmg = 0;
	private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(FrogEntity.class, DataSerializers.VARINT);
	
	public ShopPosterEntity(EntityType<? extends ShopPosterEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public ShopPosterEntity(World worldIn, BlockPos pos, Direction direction, ItemStack stack, int meta)
	{
		super(MSEntityTypes.SHOP_POSTER, worldIn, pos, direction, stack, meta, false);
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
		return new ItemStack(MSItems.SHOP_POSTER);//, 1, getPosterType()); TODO poster subtypes
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
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("Type", this.getPosterType());
	}
	
	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		if(compound.contains("Type")) setPosterType(compound.getInt("Type"));
		else setPosterType(0);
	}
	
	public enum ShopArt implements HangingArtEntity.IArt
	{
		FRAYMOTIFS("Fraymotifs", 48, 48, 0, 0),
		FOOD("Food", 48, 48, 48, 0),
		HATS("Hats", 48, 48, 96, 0),
		GENERAL("General", 48, 48, 144, 0),
		CANDY("Candy", 48, 48, 0, 48);
		
		private final String title;
		private final int sizeX, sizeY;
		private final int offsetX, offsetY;
		
		ShopArt(String title, int sizeX, int sizeY, int offsetX, int offsetY)
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