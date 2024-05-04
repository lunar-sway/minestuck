package com.mraof.minestuck.util;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	public static Set<Block> readBlockSet(CompoundTag nbt, String key)
	{
		return nbt.getList(key, Tag.TAG_STRING).stream().map(Tag::getAsString)
				//Turn the Strings into ResourceLocations
				.flatMap(blockName -> Stream.ofNullable(ResourceLocation.tryParse(blockName)))
				//Turn the ResourceLocations into Blocks
				.flatMap(blockId -> Stream.ofNullable(BuiltInRegistries.BLOCK.get(blockId)))
				//Gather the blocks into a set
				.collect(Collectors.toSet());
	}
	
	public static void writeBlockSet(CompoundTag nbt, String key, @Nonnull Set<Block> blocks)
	{
		ListTag listTag = new ListTag();
		for(Block blockIterate : blocks)
		{
			String blockName = String.valueOf(BuiltInRegistries.BLOCK.getKey(blockIterate));
			listTag.add(StringTag.valueOf(blockName));
		}
		
		nbt.put(key, listTag);
	}
	
	public static boolean tryAddBlockToSet(CompoundTag nbt, String key, Block block)
	{
		StringTag blockIdTag = StringTag.valueOf(String.valueOf(BuiltInRegistries.BLOCK.getKey(block)));
		
		if(!nbt.contains(key, Tag.TAG_LIST))
		{
			writeBlockSet(nbt, key, Collections.singleton(block));
			return true;
		} else
		{
			ListTag listTag = nbt.getList(key, Tag.TAG_STRING);
			if(!listTag.contains(blockIdTag))
			{
				listTag.add(blockIdTag);
				return true;
			} else
				return false;
		}
	}
	
	public static void writeGristType(CompoundTag nbt, String key, GristType gristType)
	{
		ResourceLocation id = gristType.getId();
		if(id == null)
			LOGGER.error("Trying to save grist type {} that is lacking a registry id!", gristType);
		else writeResourceLocation(nbt, key, id);
	}
	
	public static GristType readGristType(CompoundTag nbt, String key)
	{
		return readGristType(nbt, key, GristTypes.BUILD);
	}
	
	public static GristType readGristType(CompoundTag nbt, String key, Supplier<GristType> fallback)
	{
		ResourceLocation name = tryReadResourceLocation(nbt, key);
		if(name != null)
		{
			GristType type = GristTypes.REGISTRY.get(name);
			if(type != null)
				return type;
			else
				LOGGER.warn("Couldn't find grist type by name {}  while reading from nbt. Will fall back to {} instead.", name, fallback);
		}
		return fallback.get();
	}
}