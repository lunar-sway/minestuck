package com.mraof.minestuck.world.lands.gen;

import java.util.Random;

import net.minecraft.block.BlockColored;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ChunkPrimer;

/*
 * For when regular terrain gen isn't enough.
 */
public class SpecialTerrainGen extends DefaultTerrainGen
{
	private int width = 2;
	private EnumDyeColor[] colors = {EnumDyeColor.GRAY, EnumDyeColor.SILVER, EnumDyeColor.WHITE, EnumDyeColor.PINK,
			EnumDyeColor.RED, EnumDyeColor.ORANGE, EnumDyeColor.YELLOW, EnumDyeColor.LIME,
			EnumDyeColor.GREEN, EnumDyeColor.CYAN, EnumDyeColor.LIGHT_BLUE, EnumDyeColor.BLUE,
			EnumDyeColor.PURPLE, EnumDyeColor.MAGENTA, EnumDyeColor.BROWN, EnumDyeColor.BLACK};
	private SpecialAction action = SpecialAction.COLOR_STRIPE;
	
	public SpecialTerrainGen(ChunkProviderLands chunkProvider, Random rand)
	{
		super(chunkProvider, rand);
	}
	
	@Override
	public ChunkPrimer createChunk(int chunkX, int chunkZ)
	{
		ChunkPrimer out = super.createChunk(chunkX, chunkZ);
		switch(action)
		{
		case COLOR_STRIPE:
			colorStripe(out, chunkX, chunkZ);
			break;
		}
		return out;
	}

	private void colorStripe(ChunkPrimer c, int chunkX, int chunkZ)
	{
		int metamod = Math.floorMod((chunkX + chunkZ) * 16, width*colors.length);		//Modifies the color based on the position of the chunk in the world. Thus, meta-modifier, or metamod.
		
		//Chunk length is a universal constant: 16. This is the length of a Minecraft chunk, along the x or z axis.
		//The pattern length is equal to width*colors.length. This is the distance between two iterations of the pattern, along the x or z axis.
		//The superchunk length is equal to the lcm of the pattern length and chunk length. This is length required to repeat the pattern until it repeats as a new chunk starts.
		//Chunk value is equal to (chunk.x + chunk.z) modulo superchunk length. Chunks with this value should be modified in a way to reflect their position in the superchunk.
		//Metamod is equal to the (chunk length * chunk value) modulo pattern length. This is the offset of the pattern in a given chunk to ensure the chunk aligns with the pattern.
		
		for(int x=0; x<16; x++)
		{
			for(int z=0; z<16; z++)
			{
				int y = c.findGroundBlockIdx(x, z==0?z:z-1) + 1;	//Subtracting 1 from Z is a hacky workaround to fix an issue in ChunkPrimer.getHeightValue itself. keyword.
				//TODO: Find or create an implementation that doesn't involve deprecated identity maps. See if net.minecraftforge.fml.common.registry.GameRegistry has what we need.
				
				IBlockState oldState = c.getBlockState(x, y, z);
				while(!(oldState.getBlock() instanceof BlockColored) && y>0)
				{
					y--;
					oldState = c.getBlockState(x, y, z);
				}
				
				while(oldState.getBlock() instanceof BlockColored)
				{
					long color = (metamod + (x + y + z)) / width;
					c.setBlockState(x, y, z, oldState.withProperty(BlockColored.COLOR, colors[(int) Math.floorMod(color, colors.length)]));
					y--;
					oldState = c.getBlockState(x, y, z);
				}
			}
		}
	}
	

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public EnumDyeColor[] getColors() {
		return colors;
	}

	public void setColors(EnumDyeColor[] colors) {
		this.colors = colors;
	}

	public enum SpecialAction
	{
		COLOR_STRIPE();
		SpecialAction() {}
	}
}