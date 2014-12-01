package com.mraof.minestuck.world.gen.lands;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;

public class BlockWithMetadata extends BlockState
{
	public Block block;
	public byte metadata;
	public BlockWithMetadata(Block block, byte metadata) 
	{
		super(block, PropertyInteger.create("metadata", metadata, 0));	//Not really having any idea
		IProperty property;
		this.block = block;
		this.metadata = metadata;
	}
	public BlockWithMetadata(Block block)
	{
		this(block, (byte) 0);
	}
}
