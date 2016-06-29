package com.mraof.minestuck.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

import com.mraof.minestuck.util.Debug;

public class ComponentCastleWallPiece extends ComponentCastlePiece 
{
	boolean cornerPiece;
	public ComponentCastleWallPiece() {}
	protected ComponentCastleWallPiece(int par1, ComponentCastleStartPiece startPiece, StructureBoundingBox structureboundingbox, int direction, boolean cornerPiece) 
	{
		super(par1, startPiece);
		this.boundingBox = structureboundingbox;
		this.direction = direction;
		this.componentType = 1;
		this.cornerPiece = cornerPiece;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildComponent(StructureComponent par1StructureComponent,
			@SuppressWarnings("rawtypes") List components, Random random)
	{
//		if(Math.abs(this.startPiece.x - this.boundingBox.minX) >= this.startPiece.castleWidth && ((this.direction & 1) == 0) || (this.startPiece.z - this.boundingBox.minZ >= this.startPiece.castleLength && ((this.direction & 1) == 1)))
//			direction++;
		boolean incrementDirection = false;
		switch(this.direction)
		{
		case 0:
			if(this.boundingBox.minX - this.startPiece.x >= this.startPiece.castleWidth)incrementDirection = true;
			break;
		case 1:
			if(Math.abs(this.startPiece.z - this.boundingBox.minZ) >= this.startPiece.castleLength)incrementDirection = true;
			break;
		case 2:
			if(this.startPiece.x - this.boundingBox.minX >= this.startPiece.castleWidth)incrementDirection = true;
			break;
		case 3:
			if(this.boundingBox.minZ <= this.startPiece.z)incrementDirection = true;
			break;
		case 4:
			if(this.boundingBox.minX >= this.startPiece.x)incrementDirection = true;
			break;
		}
		if(incrementDirection)
		{
			direction++;
			this.cornerPiece = true;
		}
		if(this.startPiece.bottom)
		{
			this.componentType = 0;
			this.getNextComponentNormal(startPiece, components, random, 0, -8, 0, true);
		}
		if(this.direction == 5 && startPiece.castleLength > 16 && startPiece.castleWidth > 8)
		{
			this.componentType = 3;
			for(int depth = 8; depth < startPiece.castleLength; depth += 8)
				for(int row = -startPiece.castleWidth + 8; row < startPiece.castleWidth; row += 8)
					this.getNextComponentNormal(startPiece, components, random,  row, depth, true);//TODO change this so it generates the whole floor smartly
			this.cornerPiece = false;
			this.startPiece.z += 8;
			this.startPiece.castleWidth -= 8;
			this.startPiece.castleLength -= 16;
			this.startPiece.averageGroundLevel += 8;
			this.startPiece.bottom = false;
			this.direction = 0;
			this.boundingBox.offset(0, 8, 8);
		}
		this.componentType = 1;
		switch(this.direction)
		{
		case 4:
		case 0:
			this.getNextComponentNormal(startPiece, components, random,  8, 0, true);
			break;
		case 1:
			this.getNextComponentNormal(startPiece, components, random, 0, 8, true);
			break;
		case 2:
			this.getNextComponentNormal(startPiece, components, random,  -8, 0, true);
			break;
		case 3:
			this.getNextComponentNormal(startPiece, components, random, 0, -8, true);
			break;
		default:
//			Debug.print("Wall done");
		}
		this.componentType = 3;
//		if(!this.cornerPiece)
//			if((this.direction & 3) == 0)
				
	}

	public static ComponentCastleWallPiece findValidPlacement(List<?> par0List, ComponentCastleStartPiece startPiece, int x, int y, int z, int par5, int par6, boolean cornerPiece)
	{
		StructureBoundingBox structureboundingbox = new StructureBoundingBox(x + 0, y + 0, z + 0, x + 8 + 0, 0 + 8 + 0, z + 8 + 0);
		return new ComponentCastleWallPiece(par6, startPiece, structureboundingbox, par5, cornerPiece);
	}
	
	@Override
	public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox structureBoundingBox)
	{
		IBlockState chessTile = getChessBlockState(startPiece.isBlack ? 0 : 1);
    	IBlockState chessTile1 = getChessBlockState(startPiece.isBlack ? 2 : 3);
//		Debug.print("addComponentParts in ComponentCastleWallPiece running");
		if (startPiece.averageGroundLevel < 0)
		{
			startPiece.averageGroundLevel = startPiece.getAverageGroundLevel(par1World);

			if (startPiece.averageGroundLevel < 0)
			{
				return true;
			}

			Debug.debug(startPiece.averageGroundLevel);
		}
		if(this.boundingBox.minY < startPiece.averageGroundLevel - 1)
			this.boundingBox.offset(0, startPiece.averageGroundLevel - 1, 0);
		if (this.isLiquidInStructureBoundingBox(par1World, structureBoundingBox))
		{
			return false;
		}
		else
		{
			Debug.debug("CCWP: " + startPiece.averageGroundLevel + " " + this.boundingBox.minX + " " + this.boundingBox.minY);
			if(!(this.direction == 5 && this.cornerPiece))
			{
				this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 0, 7 ,6, 7, chessTile, chessTile1, false);
				this.fillWithAir(par1World, structureBoundingBox, 0, 7, 0, 7, 6, 7);
			}
			if(!this.cornerPiece)
				switch(this.direction)
				{
				case 0:
				case 2:
				case 4:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				case 1:
				case 3:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
				}
			else 
				switch(this.direction)
				{
				case 0:
				case 4:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					break;
				case 2:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				case 1:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 0, 7 ,7, 0, chessTile, chessTile1, false);
					break;
				case 3:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 0, 7, 7 ,7, 7, chessTile, chessTile1, false);
					break;
				default:
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 7 ,7, 0, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 7, 7 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 0, 7, 0, 0 ,7, 7, chessTile, chessTile1, false);
					this.fillWithAlternatingBlocks(par1World, structureBoundingBox, 7, 7, 0, 7 ,7, 7, chessTile, chessTile1, false);
				}
				return true;
		}
	}
	@Override
	protected void writeStructureToNBT(NBTTagCompound p_143012_1_) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void readStructureFromNBT(NBTTagCompound p_143011_1_) {
		// TODO Auto-generated method stub
		
	}

}
