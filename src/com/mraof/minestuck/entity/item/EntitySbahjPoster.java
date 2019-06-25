package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public class EntitySbahjPoster extends EntityHangingArt<EntitySbahjPoster.SbahjArt>
{
	
	public EntitySbahjPoster(World worldIn)
	{
		super(ModEntityTypes.SBAHJ_POSTER, worldIn);
	}
	
	public EntitySbahjPoster(World worldIn, BlockPos pos, EnumFacing facing)
	{
		super(ModEntityTypes.SBAHJ_POSTER, worldIn, pos, facing);
	}
	
	@Override
	public Set<SbahjArt> getArtSet()
	{
		return EnumSet.allOf(SbahjArt.class);
	}
	
	@Override
	public SbahjArt getDefault()
	{
		return SbahjArt.HELLA_JEFF;
	}
	
	@Override
	public ItemStack getStackDropped()
	{
		return new ItemStack(MinestuckItems.SBAHJ_POSTER);
	}
	
	public static enum SbahjArt implements EntityHangingArt.IArt
	{
		HELLA_JEFF("HJeff", 16, 16, 0, 0),
		SWEET_BRO("SBro", 16, 16, 16, 0),
		STAIRS("Stairs", 32, 32, 0, 128),
		BRO_HUG("BroHug", 64, 32, 0, 96);
		
		private final String title;
		private final int sizeX, sizeY;
		private final int offsetX, offsetY;
		
		private SbahjArt(String title, int sizeX, int sizeY, int offsetX, int offsetY)
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