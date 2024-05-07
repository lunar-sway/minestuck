package com.mraof.minestuck.util;

import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper class for reading and writing objects (not provided by minestuck) from and to NBT.
 * @author kirderf1
 */
public final class MSNBTUtil
{
	private static final Logger LOGGER = LogManager.getLogger();
	
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
	
	public static Tag encodeGristType(GristType gristType)
	{
		return GristTypes.REGISTRY.byNameCodec().encodeStart(NbtOps.INSTANCE, gristType).getOrThrow(false, LOGGER::error);
	}
	
	public static Optional<GristType> parseGristType(Tag tag)
	{
		return GristTypes.REGISTRY.byNameCodec().parse(NbtOps.INSTANCE, tag).resultOrPartial(LOGGER::error);
	}
}
