package com.mraof.minestuck.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Helper class for reading and writing objects (not provided by minestuck) from and to NBT.
 * @author kirderf1
 */
@SuppressWarnings("unused")
public class MSNBTUtil
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Reads a resource location or throws a {@link net.minecraft.util.ResourceLocationException} if it is invalid.
	 */
	public static ResourceLocation readResourceLocation(CompoundNBT nbt, String key)
	{
		return new ResourceLocation(nbt.getString(key));
	}
	
	/**
	 * Reads a resource location or returns null if it is invalid.
	 */
	public static ResourceLocation tryReadResourceLocation(CompoundNBT nbt, String key)
	{
		ResourceLocation resourceLocation = ResourceLocation.tryCreate(nbt.getString(key));
		if(resourceLocation == null)
			LOGGER.warn("Unable to read resource location from string {}.", nbt.getString(key));
		return resourceLocation;
	}
	
	public static CompoundNBT writeResourceLocation(CompoundNBT nbt, String key, ResourceLocation resourceLocation)
	{
		Objects.requireNonNull(resourceLocation);
		nbt.putString(key, resourceLocation.toString());
		return nbt;
	}
	
	/**
	 * When reading data very early (such as when using a {@link net.minecraftforge.fml.WorldPersistenceHooks.WorldPersistenceHook}, some dimension types might not have loaded yet.
	 * In that scenario, you should instead lazily load the dimension type by just reading the resource location.
	 * Reads a dimension type or throws an {@link IllegalStateException} if it is unable to do so.
	 */
	public static DimensionType readDimensionType(CompoundNBT nbt, String key)
	{
		ResourceLocation name = readResourceLocation(nbt, key);
		DimensionType type = DimensionType.byName(name);
		if(type == null)
			throw new IllegalStateException("Unable to read dimension type. Found no dimension by name: " + name);
		return null;
	}
	
	/**
	 * When reading data very early (such as when using a {@link net.minecraftforge.fml.WorldPersistenceHooks.WorldPersistenceHook}, some dimension types might not have loaded yet.
	 * In that scenario, you should instead lazily load the dimension type by just reading the resource location.
	 * Reads a dimension type or returns null if it is unable to do so.
	 */
	public static DimensionType tryReadDimensionType(CompoundNBT nbt, String key)
	{
		ResourceLocation name = tryReadResourceLocation(nbt, key);
		if(name != null)
		{
			DimensionType type = DimensionType.byName(name);
			if(type == null)
				LOGGER.warn("Unable to find dimension type by name {}.", name);
			return type;
		}
		return null;
	}
	
	/**
	 * Writes a dimension type or throws {@link IllegalStateException} if it is unable to do so.
	 */
	public static CompoundNBT writeDimensionType(CompoundNBT nbt, String key, DimensionType dimension)
	{
		ResourceLocation name = dimension.getRegistryName();
		if(name != null)
			return writeResourceLocation(nbt, key, name);
		else throw new IllegalStateException("Unable to get registry name from dimension type "+dimension);
	}
	
	/**
	 * Tries to write a dimension type and returns true if it is able to do so.
	 */
	public static boolean tryWriteDimensionType(CompoundNBT nbt, String key, DimensionType dimension)
	{
		ResourceLocation name = dimension.getRegistryName();
		if(name != null)
			writeResourceLocation(nbt, key, name);
		else LOGGER.warn("Unable to get registry name from dimension type {}.", dimension);
		return name != null;
	}
}