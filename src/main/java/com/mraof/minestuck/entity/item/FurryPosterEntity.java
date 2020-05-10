package com.mraof.minestuck.entity.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.item.MSItems;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public class FurryPosterEntity extends HangingArtEntity<FurryPosterEntity.FurryArt>
{
	public FurryPosterEntity(EntityType<? extends FurryPosterEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public FurryPosterEntity(World worldIn, BlockPos pos, Direction direction)
	{
		super(MSEntityTypes.FURRY_POSTER, worldIn, pos, direction);
	}
	
	@Override
	public Set<FurryArt> getArtSet()
	{
		return EnumSet.allOf(FurryArt.class);
	}
	
	@Override
	public FurryArt getDefault()
	{
		return FurryArt.FURHELLA_JEFF;
	}
	
	@Override
	public ItemStack getStackDropped()
	{
		return new ItemStack(MSItems.FURRY_POSTER);
	}
	
	public enum FurryArt implements HangingArtEntity.IArt
	{
		FURHELLA_JEFF("FHJeff", 16, 16, 0, 0),
		SWEET_PAW("SPAW", 16, 16, 16, 0),
		DRAGON("Dragon", 32, 32, 0, 144),
		FURRYFACE_MEDIUM("Furry_Face_Medium", 48, 48, 0, 208);
		
		private final String title;
		private final int sizeX, sizeY;
		private final int offsetX, offsetY;

		FurryArt(String title, int sizeX, int sizeY, int offsetX, int offsetY)
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