package com.mraof.minestuck.world.lands;

import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;
import net.minecraft.resources.ResourceLocation;

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
	}
	
	public interface Title extends Predicate<TitleLandType>
	{
		@Nonnull
		@Override
		default Title negate()
		{
			return landType -> !test(landType);
		}}
	
	@SafeVarargs
	public static Terrain terrainLand(Supplier<TerrainLandType>... landTypes)
	{
		List<TerrainLandType> landTypeList = Arrays.stream(landTypes).map(Supplier::get).toList();
		return landTypeList::contains;
	}
	
	@SafeVarargs
	public static Terrain terrainLandByGroup(Supplier<TerrainLandType>... landTypes)
	{
		List<ResourceLocation> landTypeList = Arrays.stream(landTypes).map(Supplier::get).map(ILandType::getGroup).toList();
		return landType -> landTypeList.contains(landType.getGroup());
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
		List<ResourceLocation> landTypeList = Arrays.stream(landTypes).map(Supplier::get).map(ILandType::getGroup).toList();
		return landType -> landTypeList.contains(landType.getGroup());
	}
}
