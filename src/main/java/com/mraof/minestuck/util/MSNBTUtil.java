package com.mraof.minestuck.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
	 * Reads a resource location or throws a {@link net.minecraft.util.ResourceLocationException} if it is invalid.
	 */
	@Nonnull
	public static ResourceLocation readResourceLocation(CompoundNBT nbt, String key)
	{
		return ResourceLocation.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get(key)).getOrThrow(false, LOGGER::error);
	}
	
	/**
	 * Reads a resource location or returns null if it is invalid.
	 */
	@Nullable
	public static ResourceLocation tryReadResourceLocation(CompoundNBT nbt, String key)
	{
		return ResourceLocation.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get(key)).resultOrPartial(LOGGER::error).orElse(null);
	}
	
	public static CompoundNBT writeResourceLocation(CompoundNBT nbt, String key, ResourceLocation resourceLocation)
	{
		Objects.requireNonNull(resourceLocation);
		nbt.put(key, ResourceLocation.CODEC.encodeStart(NBTDynamicOps.INSTANCE, resourceLocation).getOrThrow(false, LOGGER::error));
		return nbt;
	}
	
	/**
	 * When reading data very early (such as when using a {@link net.minecraftforge.fml.WorldPersistenceHooks.WorldPersistenceHook}, some dimension types might not have loaded yet.
	 * In that scenario, you should instead lazily load the dimension type by just reading the resource location.
	 * Reads a dimension type or throws an exception if it is unable to do so.
	 */
	@Nonnull
	public static RegistryKey<World> readDimensionType(CompoundNBT nbt, String key)
	{
		return World.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get(key)).getOrThrow(false, LOGGER::error);
	}
	
	/**
	 * When reading data very early (such as when using a {@link net.minecraftforge.fml.WorldPersistenceHooks.WorldPersistenceHook}, some dimension types might not have loaded yet.
	 * In that scenario, you should instead lazily load the dimension type by just reading the resource location.
	 * Reads a dimension type or returns null if it is unable to do so.
	 */
	@Nullable
	public static RegistryKey<World> tryReadDimensionType(CompoundNBT nbt, String key)
	{
		return World.CODEC.parse(NBTDynamicOps.INSTANCE, nbt.get(key)).resultOrPartial(LOGGER::error).orElse(null);
	}
	
	/**
	 * Writes a dimension type or throws an exception if it is unable to do so.
	 */
	public static CompoundNBT writeDimensionType(CompoundNBT nbt, String key, RegistryKey<World> dimension)
	{
		nbt.put(key, World.CODEC.encodeStart(NBTDynamicOps.INSTANCE, dimension).getOrThrow(false, LOGGER::error));
		return nbt;
	}
	
	/**
	 * Tries to write a dimension type and returns true if it is able to do so.
	 */
	public static boolean tryWriteDimensionType(CompoundNBT nbt, String key, RegistryKey<World> dimension)
	{
		Optional<INBT> optional = World.CODEC.encodeStart(NBTDynamicOps.INSTANCE, dimension).resultOrPartial(LOGGER::error);
		optional.ifPresent(inbt -> nbt.put(key, inbt));
		return optional.isPresent();
	}
}