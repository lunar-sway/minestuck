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
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public abstract class GatePiece extends ScatteredStructurePiece
{
	public GatePiece(IStructurePieceType type, ChunkGenerator generator, Random random, int x, int z, int width, int height, int depth, int heightOffset)
	{
		super(type, random, x, 64, z, width, height, depth);
		
		int count = 0;
		int heightSum = 0;
		for(int xPos = boundingBox.x0; xPos <= boundingBox.x1; xPos++)
			for(int zPos = boundingBox.z0; zPos <= boundingBox.z1; zPos++)
			{
				int posHeight = generator.getBaseHeight(xPos, zPos, Heightmap.Type.OCEAN_FLOOR_WG);
				heightSum += posHeight;
				count++;
			}
		
		if(count > 0)
			boundingBox.move(0, heightSum/count + heightOffset - boundingBox.y0, 0);
	}
	
	public GatePiece(IStructurePieceType type, CompoundNBT nbt)
	{
		super(type, nbt);
	}
	
	protected final BlockPos getGatePos()
	{
		BlockPos relativePos = getRelativeGatePos();
		int x = relativePos.getX(), y = relativePos.getY(), z = relativePos.getZ();
		return new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));
	}
	
	protected abstract BlockPos getRelativeGatePos();
	
	@Override
	protected boolean updateAverageGroundHeight(IWorld worldIn, MutableBoundingBox boundsIn, int heightIn)
	{
		throw new UnsupportedOperationException("Shouldn't change the bounding box after creating the gate piece. Look at other gate pieces for an example of what to do instead.");
	}
	
	@Override	//create()
	public boolean postProcess(ISeedReader world, StructureManager manager, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox mutableBoundingBox, ChunkPos chunkPos, BlockPos pos)
	{
		placeGate(world, boundingBox);
		return true;
	}
	
	private void placeGate(IWorld worldIn, MutableBoundingBox boundingBoxIn)
	{
		BlockPos gatePos = getGatePos();
		if (boundingBoxIn.isInside(gatePos))
		{
			for(int offsetX = -1; offsetX <= 1; offsetX++)
				for(int offsetZ = -1; offsetZ <= 1; offsetZ++)
				{
					if(offsetX == 0 && offsetZ == 0)
					{
						worldIn.setBlock(gatePos, MSBlocks.GATE.defaultBlockState().setValue(GateBlock.MAIN, true), Constants.BlockFlags.BLOCK_UPDATE);
						TileEntity tileEntity = worldIn.getBlockEntity(gatePos);
						if(tileEntity instanceof GateTileEntity)
							((GateTileEntity) tileEntity).gateType = GateHandler.Type.LAND_GATE;
						else Debug.errorf("Expected a gate tile entity after placing a gate block, but got %s!", tileEntity);
					} else worldIn.setBlock(gatePos.offset(offsetX, 0, offsetZ), MSBlocks.GATE.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
				}
		}
	}
}