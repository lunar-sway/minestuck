package com.mraof.minestuck.computer;

import com.mraof.minestuck.util.MSSoundEvents;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DiskBurnerData implements ProgramType.Data
{
	private final Runnable markDirty;
	
	private Set<Block> hieroglyphsStored = new HashSet<>();
	private boolean hasParadoxInfoStored = false;
	
	public DiskBurnerData(Runnable markDirty)
	{
		this.markDirty = markDirty;
	}
	
	@Override
	public void read(CompoundTag tag)
	{
		this.hieroglyphsStored = readBlockSet(tag, "hieroglyphsStored");
		this.hasParadoxInfoStored = tag.getBoolean("hasParadoxInfoStored");
	}
	
	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		writeBlockSet(tag, "hieroglyphsStored", this.hieroglyphsStored);
		tag.putBoolean("hasParadoxInfoStored", this.hasParadoxInfoStored);
		return tag;
	}
	
	private static Set<Block> readBlockSet(CompoundTag nbt, String key)
	{
		return nbt.getList(key, Tag.TAG_STRING).stream().map(Tag::getAsString)
				//Turn the Strings into ResourceLocations
				.flatMap(blockName -> Stream.ofNullable(ResourceLocation.tryParse(blockName)))
				//Turn the ResourceLocations into Blocks
				.flatMap(blockId -> Stream.ofNullable(BuiltInRegistries.BLOCK.get(blockId)))
				//Gather the blocks into a set
				.collect(Collectors.toSet());
	}
	
	private static void writeBlockSet(CompoundTag nbt, String key, @Nonnull Set<Block> blocks)
	{
		ListTag listTag = new ListTag();
		for(Block blockIterate : blocks)
		{
			String blockName = String.valueOf(BuiltInRegistries.BLOCK.getKey(blockIterate));
			listTag.add(StringTag.valueOf(blockName));
		}
		
		nbt.put(key, listTag);
	}
	
	public Set<Block> getHieroglyphsStored()
	{
		return this.hieroglyphsStored;
	}
	
	public boolean isHasParadoxInfoStored()
	{
		return this.hasParadoxInfoStored;
	}
	
	public boolean hasAllCode()
	{
		return this.isHasParadoxInfoStored() && this.getHieroglyphsStored().containsAll(MSTags.getBlocksFromTag(MSTags.Blocks.GREEN_HIEROGLYPHS));
	}
	
	public boolean recordNewInfo(Level level, BlockPos pos, boolean paradoxInfo, Set<Block> recordedBlocks)
	{
		boolean changed = false;
		
		if(paradoxInfo && !isHasParadoxInfoStored())
		{
			this.hasParadoxInfoStored = true;
			changed = true;
		}
		
		for(Block iterateBlock : recordedBlocks)
		{
			if(iterateBlock.defaultBlockState().is(MSTags.Blocks.GREEN_HIEROGLYPHS))
				changed |= getHieroglyphsStored().add(iterateBlock);
		}
		
		if(changed)
		{
			this.markDirty.run();
			level.playSound(null, pos, MSSoundEvents.COMPUTER_KEYBOARD.get(), SoundSource.BLOCKS);
		}
		
		return changed;
	}
}
