package com.mraof.minestuck.entity.item;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityShopPoster extends EntityHangingArt<EntityShopPoster.ShopArt>
{
	protected int dmg = 0;
	public EntityShopPoster(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityShopPoster(World worldIn, BlockPos pos, EnumFacing facing, ItemStack stack, int meta)
	{
		super(worldIn, pos, facing, stack, meta, false);
		dmg = meta;
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
		return new ItemStack(MinestuckItems.shopPoster, 1, dmg);
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