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

public class CrewPosterEntity extends HangingArtEntity<CrewPosterEntity.MidnightCrewArt>
{
	public CrewPosterEntity(EntityType<? extends CrewPosterEntity> type, Level level)
	{
		super(type, level);
	}
	
	public CrewPosterEntity(Level level, BlockPos pos, Direction direction)
	{
		super(MSEntityTypes.MIDNIGHT_CREW_POSTER, level, pos, direction);
	}
	
	@Override
	public Set<MidnightCrewArt> getArtSet()
	{
		return EnumSet.allOf(MidnightCrewArt.class);
	}
	
	@Override
	public MidnightCrewArt getDefault()
	{
		return MidnightCrewArt.SPADE;
	}
	
	@Override
	public ItemStack getStackDropped()
	{
		return new ItemStack(MSItems.CREW_POSTER.get());
	}
	
	public enum MidnightCrewArt implements HangingArtEntity.IArt
	{
		SPADE("Spade", 16, 16, 0, 0),
		DIAMOND("Diamond", 16, 16, 16, 0),
		CLUB("Club", 16, 16, 32, 0),
		HEART("Heart", 16, 16, 48, 0),
		CREW_MEDIUM("CrewMedium", 32, 32, 0, 128),
		CREW_LARGE("CrewLarge", 64, 64, 0, 192);
		
		private final String title;
		private final int sizeX, sizeY;
		private final int offsetX, offsetY;
		
		MidnightCrewArt(String title, int sizeX, int sizeY, int offsetX, int offsetY)
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