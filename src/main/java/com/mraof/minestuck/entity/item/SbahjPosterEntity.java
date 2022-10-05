package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.Set;

public class SbahjPosterEntity extends HangingArtEntity<SbahjPosterEntity.SbahjArt>
{
	public SbahjPosterEntity(EntityType<? extends SbahjPosterEntity> type, Level level)
	{
		super(type, level);
	}
	
	public SbahjPosterEntity(Level level, BlockPos pos, Direction direction)
	{
		super(MSEntityTypes.SBAHJ_POSTER.get(), level, pos, direction);
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
		return new ItemStack(MSItems.SBAHJ_POSTER.get());
	}
	
	public enum SbahjArt implements HangingArtEntity.IArt
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