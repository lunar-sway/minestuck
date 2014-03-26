package com.mraof.minestuck.world.gen.lands;

import net.minecraft.block.Block;

public class BlockWithMetadata 
{
	public Block block;
	public byte metadata;
	public BlockWithMetadata(Block block, byte metadata) 
	{
		this.block = block;
		this.metadata = metadata;
	}
	public BlockWithMetadata(Block block)
	{
		this(block, (byte) 0);
	}
}
