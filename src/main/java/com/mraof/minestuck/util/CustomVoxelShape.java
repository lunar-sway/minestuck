package com.mraof.minestuck.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Map;

public class CustomVoxelShape
{
	
	public double[][] parts;
	
	public CustomVoxelShape(double[]... parts)
	{
		this.parts = parts;
	}
	
	public CustomVoxelShape rotate(Direction dir)
	{
		double[][] out = parts.clone();
		for(int i = 0; i < out.length; i++)
		{
			double[] part = out[i];
			switch(dir)
			{
				case WEST:
					out[i] = new double[]{part[2], part[1], 16-part[3], part[5], part[4], 16-part[0]};
				break;
				case SOUTH:
					out[i] = new double[] {16-part[3], part[1], 16-part[5], 16-part[0], part[4], 16-part[2]};
				break;
				case EAST:
					out[i] = new double[] {16-part[5], part[1], part[0], 16-part[2], part[4], part[3]};
				break;
			}
		}
		return new CustomVoxelShape(out);
	}
	
	public CustomVoxelShape translate(double x, double y, double z)
	{
		double[][] out = parts.clone();
		for(int i = 0; i < out.length; i++)
		{
			out[i][0] += x;
			out[i][1] += y;
			out[i][2] += z;
			out[i][3] += x;
			out[i][4] += y;
			out[i][5] += z;
		}
		
		return new CustomVoxelShape(out);
	}
	
	public CustomVoxelShape merge(CustomVoxelShape... shapes)
	{
		double[][] parts = this.parts.clone();
		for(CustomVoxelShape shape : shapes)
			parts = ArrayUtils.addAll(parts, shape.parts);
		return new CustomVoxelShape(parts);
	}
	
	public VoxelShape create(Direction dir)
	{
		CustomVoxelShape shape = this.rotate(dir);
		VoxelShape out = Block.makeCuboidShape(0,0,0,0,0,0);
		
		for(double[] part : shape.parts)
			out = VoxelShapes.or(out, Block.makeCuboidShape(part[0], part[1], part[2], part[3], part[4], part[5]));
		return out;
	}
	
	public Map<Direction, VoxelShape> createRotatedShapes()
	{
		return Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, this.create(Direction.NORTH), Direction.SOUTH, this.create(Direction.SOUTH), Direction.WEST, this.create(Direction.WEST), Direction.EAST, this.create(Direction.EAST)));
	}
}
