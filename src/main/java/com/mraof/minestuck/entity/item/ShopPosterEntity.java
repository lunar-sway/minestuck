package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.FrogEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.Set;

public class ShopPosterEntity extends HangingArtEntity<ShopPosterEntity.ShopArt>
{
	protected int dmg = 0;
	private static final EntityDataAccessor<Integer> TYPE = SynchedEntityData.defineId(FrogEntity.class, EntityDataSerializers.INT);
	
	public ShopPosterEntity(EntityType<? extends ShopPosterEntity> type, Level level)
	{
		super(type, level);
	}
	
	public ShopPosterEntity(Level level, BlockPos pos, Direction direction, ItemStack stack)
	{
		super(MSEntityTypes.SHOP_POSTER.get(), level, pos, direction);
		//setPosterType(meta);
	}
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(TYPE, 0);
		
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
		return new ItemStack(MSItems.SHOP_POSTER.get());//, 1, getPosterType()); TODO poster subtypes
	}
	
	private void setPosterType(int i)
	{
		this.entityData.set(TYPE, i);
	}
	
	public int getPosterType()
	{
		return this.entityData.get(TYPE);
	}
	
	//NBT
	
	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("Type", this.getPosterType());
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
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
		{
			return title;
		}
		
		@Override
		public int getSizeX()
		{
			return sizeX;
		}
		
		@Override
		public int getSizeY()
		{
			return sizeY;
		}
		
		@Override
		public int getOffsetX()
		{
			return offsetX;
		}
		
		@Override
		public int getOffsetY()
		{
			return offsetY;
		}
	}
}