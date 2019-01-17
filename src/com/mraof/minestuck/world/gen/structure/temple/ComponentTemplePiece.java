package com.mraof.minestuck.world.gen.structure.temple;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class ComponentTemplePiece extends StructureComponent
{
	protected int direction;
	protected ComponentTempleStartPiece startPiece;
	
	public ComponentTemplePiece() {}
	
	protected ComponentTemplePiece(int type, int direction, ComponentTempleStartPiece startPiece)
	{
		super(type);
		this.startPiece = startPiece;
		this.direction = direction;
	}
	
	protected ComponentTemplePiece(int type, ComponentTempleStartPiece startPiece)
	{
		this(type, 0, startPiece);
	}
	
	/**
	 * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox. (A median of all the
	 * levels in the BB's horizontal rectangle).
	 */
	protected int getAverageGroundLevel(World par1World, StructureBoundingBox par2StructureBoundingBox)
	{
		int var3 = 0;
		int var4 = 0;

		for (int var5 = this.boundingBox.minZ; var5 <= this.boundingBox.maxZ; ++var5)
		{
			for (int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6)
			{
				if (par2StructureBoundingBox.isVecInside(new BlockPos(var6, 64, var5)))	//isVecInside
				{
					var3 += par1World.getTopSolidOrLiquidBlock(new BlockPos(var6, 0, var5)).getY();
					++var4;
				}
			}
		}
		
		if (var4 == 0)
		{
			return -1;
		}
		else
		{
			if(var3 / var4 - 8 < 255)
				return var3 / var4;
			else
				return 255 - 8;
		}
	}
	
	protected int getAverageGroundLevel(World world)
	{
		return this.getAverageGroundLevel(world, this.boundingBox);
	}
}

