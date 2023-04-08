package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.tags.TagKey;

import javax.annotation.Nonnull;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class LandTypeConditions
{
	public interface Terrain extends Predicate<TerrainLandType>
	{
		@Nonnull
		@Override
		default Terrain negate()
		{
			return landType -> !test(landType);
		}
		
		default Terrain or(Supplier<TerrainLandType> landTypeSupplier)
		{
			return this.or(terrainLand(landTypeSupplier));
		}
		
		default Terrain or(TagKey<TerrainLandType> tag)
		{
			return this.or(terrainLand(tag));
		}
		
		default Terrain or(Terrain otherCondition)
		{
			return landType -> this.test(landType) || otherCondition.test(landType);
		}
	}
	
	public interface Title extends Predicate<TitleLandType>
	{
		@Nonnull
		@Override
		default Title negate()
		{
			return landType -> !test(landType);
		}
		
		default Title or(Supplier<TitleLandType> landTypeSupplier)
		{
			return this.or(titleLand(landTypeSupplier));
		}
		
		default Title or(TagKey<TitleLandType> tag)
		{
			return this.or(titleLand(tag));
		}
		
		default Title or(Title otherCondition)
		{
			return landType -> this.test(landType) || otherCondition.test(landType);
		}}
	
	public static Terrain terrainLand(Supplier<TerrainLandType> landTypeSupplier)
	{
		return landType -> landType == landTypeSupplier.get();
	}
	
	public static Terrain terrainLand(TagKey<TerrainLandType> landTypeTag)
	{
		return landType -> landType.is(landTypeTag);
	}
	
	public static Title titleLand(Supplier<TitleLandType> landTypeSupplier)
	{
		return landType -> landType == landTypeSupplier.get();
	}
	
	public static Title titleLand(TagKey<TitleLandType> landTypeTag)
	{
		return landType -> landType.is(landTypeTag);
	}
}
