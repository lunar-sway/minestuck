package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
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
		}}
	
	public static Terrain terrainLand(Supplier<TerrainLandType> landTypeSupplier)
	{
		return landType -> landType == landTypeSupplier.get();
	}
	
	public static Terrain terrainLand(TagKey<TerrainLandType> landTypeTag)
	{
		return landType -> landType.is(landTypeTag);
	}
	
	@SafeVarargs
	public static Title titleLand(Supplier<TitleLandType>... landTypes)
	{
		List<TitleLandType> landTypeList = Arrays.stream(landTypes).map(Supplier::get).toList();
		return landTypeList::contains;
	}
	
	@SafeVarargs
	public static Title titleLandByGroup(Supplier<TitleLandType>... landTypes)
	{
		List<ResourceLocation> landTypeList = Arrays.stream(landTypes).map(Supplier::get).map(TitleLandType::getGroup).toList();
		return landType -> landTypeList.contains(landType.getGroup());
	}
}
