package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;

import java.util.Random;

public abstract class GatePiece extends ScatteredStructurePiece
{
	public GatePiece(IStructurePieceType type, Random random, int x, int y, int z, int width, int height, int depth)
	{
		super(type, random, x, y, z, width, height, depth);
	}
	
	public GatePiece(IStructurePieceType type, CompoundNBT nbt)
	{
		super(type, nbt);
	}
	
	protected void placeGate(IWorld worldIn, MutableBoundingBox boundingBoxIn, int x, int y, int z)
	{
		BlockPos gatePos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
		if (boundingBoxIn.isVecInside(gatePos))
		{
			GateHandler.setDefiniteGatePos(worldIn.getWorld().getServer(), GateHandler.Type.LAND_GATE, worldIn.getDimension().getType(), gatePos);	//TODO Must be thread safe! (And perhaps done through the tile entity?)
			//Potential idea: keep the gate position in this class only!
			
			for(int offsetX = -1; offsetX <= 1; offsetX++)
				for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
				{
					if(offsetX == 0 && offsetZ == 0)
					{
						worldIn.setBlockState(gatePos, MSBlocks.GATE.getDefaultState().with(GateBlock.MAIN, true), 2);
						GateTileEntity tileEntity = (GateTileEntity) worldIn.getTileEntity(gatePos);	//TODO Make this safer
						tileEntity.gateType = GateHandler.Type.LAND_GATE;
					} else worldIn.setBlockState(gatePos.add(x, 0, z), MSBlocks.GATE.getDefaultState(), 2);
				}
		}
	}
}