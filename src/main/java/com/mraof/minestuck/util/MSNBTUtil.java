package com.mraof.minestuck.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * Helper class for reading and writing objects (not provided by minestuck) from and to NBT.
 * @author kirderf1
 */
@SuppressWarnings("unused")
public class MSNBTUtil
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * Reads a resource location or throws an exception if it is invalid.
	 */
	@Nonnull
	public static ResourceLocation readResourceLocation(CompoundTag nbt, String key)
	{
		return ResourceLocation.CODEC.parse(NbtOps.INSTANCE, nbt.get(key)).getOrThrow(false, LOGGER::error);
	}
	
	/**
	 * Reads a resource location or returns null if it is invalid.
	 */
	@Nullable
	public static ResourceLocation tryReadResourceLocation(CompoundTag nbt, String key)
	{
		return ResourceLocation.CODEC.parse(NbtOps.INSTANCE, nbt.get(key)).resultOrPartial(LOGGER::error).orElse(null);
	}
	
	public static CompoundTag writeResourceLocation(CompoundTag nbt, String key, ResourceLocation resourceLocation)
	{
		Objects.requireNonNull(resourceLocation);
		nbt.put(key, ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, resourceLocation).getOrThrow(false, LOGGER::error));
		return nbt;
	}
	
	/**
	 * Reads a dimension type or throws an exception if it is unable to do so.
	 */
	@Nonnull
	public static ResourceKey<Level> readDimensionType(CompoundTag nbt, String key)
	{
		return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get(key)).getOrThrow(false, LOGGER::error);
	}
	
	/**
	 * Reads a dimension type or returns null if it is unable to do so.
	 */
	@Nullable
	public static ResourceKey<Level> tryReadDimensionType(CompoundTag nbt, String key)
	{
		return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt.get(key)).resultOrPartial(LOGGER::error).orElse(null);
	}
	
	/**
	 * Writes a dimension type or throws an exception if it is unable to do so.
	 */
	public static CompoundTag writeDimensionType(CompoundTag nbt, String key, ResourceKey<Level> dimension)
	{
		nbt.put(key, Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, dimension).getOrThrow(false, LOGGER::error));
		return nbt;
	}
	
	/**
	 * Tries to write a dimension type and returns true if it is able to do so.
	 */
	public static boolean tryWriteDimensionType(CompoundTag nbt, String key, ResourceKey<Level> dimension)
	{
		Optional<Tag> optional = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, dimension).resultOrPartial(LOGGER::error);
		optional.ifPresent(inbt -> nbt.put(key, inbt));
		return optional.isPresent();
	}
}