package com.mraof.minestuck.entity.item;

import java.util.EnumSet;
import java.util.Set;

import com.mraof.minestuck.entity.ModEntityTypes;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrewPosterEntity extends HangingArtEntity<CrewPosterEntity.MidnightCrewArt>
{
	public CrewPosterEntity(EntityType<? extends CrewPosterEntity> type, World worldIn)
	{
		super(type, worldIn);
	}
	
	public CrewPosterEntity(World worldIn, BlockPos pos, Direction direction)
	{
		super(ModEntityTypes.MIDNIGHT_CREW_POSTER, worldIn, pos, direction);
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
		return new ItemStack(MinestuckItems.CREW_POSTER);
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