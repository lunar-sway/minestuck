package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.block.GateBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.tileentity.GateTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.world.GateHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;

import java.util.Random;

public abstract class GatePiece extends ScatteredStructurePiece
{
	public GatePiece(IStructurePieceType type, ChunkGenerator<?> generator, Random random, int x, int z, int width, int height, int depth, int heightOffset)
	{
		super(type, random, x, 64, z, width, height, depth);
		
		int count = 0;
		int heightSum = 0;
		for(int xPos = boundingBox.minX; xPos <= boundingBox.maxX; xPos++)
			for(int zPos = boundingBox.minZ; zPos <= boundingBox.maxZ; zPos++)
			{
				int posHeight = generator.func_222532_b(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG);
				heightSum += posHeight;
				count++;
			}
		
		if(count > 0)
			boundingBox.offset(0, heightSum/count + heightOffset - boundingBox.minY, 0);
	}
	
	public GatePiece(IStructurePieceType type, CompoundNBT nbt)
	{
		super(type, nbt);
	}
	
	protected final BlockPos getGatePos()
	{
		BlockPos relativePos = getRelativeGatePos();
		int x = relativePos.getX(), y = relativePos.getY(), z = relativePos.getZ();
		return new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
	}
	
	protected abstract BlockPos getRelativeGatePos();
	
	@Override
	protected boolean isInsideBounds(IWorld worldIn, MutableBoundingBox boundsIn, int heightIn)
	{
		throw new UnsupportedOperationException("Shouldn't change the bounding box after creating the gate piece. Look at other gate pieces for an example of what to do instead.");
	}
	
	@Override
	public boolean addComponentParts(IWorld worldIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn)
	{
		placeGate(worldIn, boundingBox);
		return true;
	}
	
	private void placeGate(IWorld worldIn, MutableBoundingBox boundingBoxIn)
	{
		BlockPos gatePos = getGatePos();
		if (boundingBoxIn.isVecInside(gatePos))
		{
			for(int offsetX = -1; offsetX <= 1; offsetX++)
				for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
				{
					if(offsetX == 0 && offsetZ == 0)
					{
						worldIn.setBlockState(gatePos, MSBlocks.GATE.getDefaultState().with(GateBlock.MAIN, true), 2);
						TileEntity tileEntity = worldIn.getTileEntity(gatePos);
						if(tileEntity instanceof GateTileEntity)
							((GateTileEntity) tileEntity).gateType = GateHandler.Type.LAND_GATE;
						else Debug.errorf("Expected a gate tile entity after placing a gate block, but got %s!", tileEntity);
					} else worldIn.setBlockState(gatePos.add(offsetX, 0, offsetZ), MSBlocks.GATE.getDefaultState(), 2);
				}
		}
	}
}