package com.mraof.minestuck.world.gen.feature.structure;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.world.gen.feature.MSStructurePieces;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.feature.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.world.storage.loot.MSLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class ImpDungeonPieces
{
	public static class EntryCorridor extends ImpDungeonPiece
	{
		public EntryCorridor(Direction coordBaseMode, int posX, int posZ, Random rand, List<StructurePiece> componentList)
		{
			super(MSStructurePieces.IMP_ENTRY_CORRIDOR, 0, 2);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 10 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 10 : 6;
			
			int height = 45 - rand.nextInt(8);
			int offset = getCoordBaseMode().getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 4 : -3;
			int x = posX + (getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 0 : offset);
			int z = posZ + (getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 0 : offset);
			
			this.boundingBox = new MutableBoundingBox(x, height, z, x + xWidth - 1, height + 6, z + zWidth - 1);
			
			BlockPos compoPos = new BlockPos(x + (xWidth/2 - 1), height, z + (zWidth/2 - 1));
			
			StructureContext ctxt = new StructureContext(componentList, rand);
			ctxt.compoGen[6][6] = this;
			
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			if(rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*10, 0, zOffset*10), coordBaseMode, 0);
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*10, 0, -zOffset*10), coordBaseMode.getOpposite(), 0);
			} else
			{
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.add(-xOffset*10, 0, -zOffset*10), coordBaseMode.getOpposite(), 0);
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.add(xOffset*10, 0, zOffset*10), coordBaseMode, 0);
			}
		}
		
		public EntryCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_ENTRY_CORRIDOR, nbt, 2);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().equals(facing))
				corridors[0] = false;
			else if(getCoordBaseMode().getOpposite().equals(facing))
				corridors[1] = false;
			return getCoordBaseMode().getAxis().equals(facing.getAxis());
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState fluid = blocks.getBlockState("fall_fluid");

			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 4, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 4, 4, 4, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 3, 0, 5, fluid, fluid, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 5, 3, -1, 5, floorDecor, floorDecor, false);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 5, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 4, 5, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 7, 4, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 1, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 4, 3, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 7, 4, 3, 7, wallDecor, wallDecor, false);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 3, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 7, 3, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 8, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 8, 1, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 8, 4, 4, 9, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 3);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 7, 3, 3, 9);

			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 9, 3, 3, 9, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);

			return true;
		}
	}
	
	public static boolean generatePart(StructureContext ctxt, int xIndex, int zIndex, BlockPos pos, Direction facing, int index)
	{
		if(xIndex >= ctxt.compoGen.length || zIndex >= ctxt.compoGen[0].length
				|| xIndex < 0 || zIndex < 0)
			return false;
		
		if(ctxt.compoGen[xIndex][zIndex] != null)
			return ctxt.compoGen[xIndex][zIndex].connectFrom(facing.getOpposite());
		
		if(ctxt.rand.nextDouble() >= (1.4 - index*0.1))
			if(ctxt.rand.nextDouble() < 1/3D)
			{
				ctxt.compoList.add(genRoom(facing, pos, xIndex, zIndex, index, ctxt));
				return true;
			} else return false;
		
		ImpDungeonPiece component;
		
		int corridors = ctxt.corridors;
		double i = ctxt.rand.nextDouble();
		if(i < 1.2 - corridors*0.12)	//Cross corridor
		{
			ctxt.corridors += 3;
			component = new CrossCorridor(facing, pos, xIndex, zIndex, index, ctxt);
		} else if(i < 0.96 - corridors*0.06)	//Any room
		{
			component = genRoom(facing, pos, xIndex, zIndex, index, ctxt);
		} else	//Straight or corner corridor
		{
			ctxt.corridors -= 1;
			if(ctxt.rand.nextBoolean())
				component = new TurnCorridor(facing, pos, xIndex, zIndex, index, ctxt);
			else
			{	//Corridor
				i = ctxt.rand.nextFloat();
				if(i < 0.2)
					component = new SpawnerCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				else if (i < 0.3 && !ctxt.generatedOgreRoom)
				{
					component = new OgreCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				}
				else if (i < 0.4)
				{
					component = new LargeSpawnerCorridor(facing, pos, xIndex, zIndex, index, ctxt);
				}
				else component = new StraightCorridor(facing, pos, xIndex, zIndex, index, ctxt);
			}
		}
		
		ctxt.corridors = corridors;
		ctxt.compoList.add(component);
		
		return true;
	}
	
	protected static ImpDungeonPiece genRoom(Direction facing, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
	{
		float i = ctxt.rand.nextFloat();
		if(i < 0.2 || !ctxt.generatedReturn)
		{
			if(ctxt.rand.nextBoolean())
				return new ReturnRoom(facing, pos, xIndex, zIndex, index, ctxt);
			else return new ReturnRoomAlt(facing, pos, xIndex, zIndex, index, ctxt);
		}
		else if(i < 0.5)
			return new BookcaseRoom(facing, pos, xIndex, zIndex, index, ctxt);
		else return new SpawnerRoom(facing, pos, xIndex, zIndex, index, ctxt);
	}
	
	protected static class StructureContext
	{
		ImpDungeonPiece[][] compoGen = new ImpDungeonPiece[13][13];
		List<StructurePiece> compoList;
		Random rand;
		int corridors = 3;
		boolean generatedReturn = false;
		boolean generatedOgreRoom = false;
		
		public StructureContext(List<StructurePiece> compoList, Random rand)
		{
			this.compoList = compoList;
			this.rand = rand;
		}
	}
	
	public static abstract class ImpDungeonPiece extends ImprovedStructurePiece
	{
		protected final boolean[] corridors;
		
		public ImpDungeonPiece(IStructurePieceType structurePieceTypeIn, int componentTypeIn, int corridors)
		{
			super(structurePieceTypeIn, componentTypeIn);
			this.corridors = new boolean[corridors];
		}
		
		public ImpDungeonPiece(IStructurePieceType structurePieceTypeIn, CompoundNBT nbt, int corridors)
		{
			super(structurePieceTypeIn, nbt);
			this.corridors = new boolean[corridors];
			for(int i = 0; i < corridors; i++)
				this.corridors[i] = nbt.getBoolean("bl"+i);
		}
		
		protected abstract boolean connectFrom(Direction facing);
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			for(int i = 0; i < corridors.length; i++)
				tagCompound.putBoolean("bl"+i, corridors[i]);
		}
	}
	
	public static class StraightCorridor extends ImpDungeonPiece
	{
		
		boolean light;
		byte lightPos;
		
		public StraightCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_STRAIGHT_CORRIDOR, 0, 1);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 10 : 4;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 10 : 4;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = true;//ctxt.rand.nextFloat() < 0.1F;
			if(light)
				lightPos = (byte) ctxt.rand.nextInt(4);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		public StraightCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_STRAIGHT_CORRIDOR, nbt, 1);
			light = nbt.getBoolean("l");
			if(light)
				lightPos = nbt.getByte("lpos");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("l", light);
			if(light)
				tagCompound.putByte("lpos", lightPos);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 2, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 0, 2, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 0, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 0, 2, 3, 9);

			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 9, 2, 3, 9, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				if(lightPos/2 == 0)
					setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.WEST), 2, 2, 4 + lightPos%2, structureBoundingBoxIn);
				else setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.EAST), 1, 2, 4 + lightPos%2, structureBoundingBoxIn);
			}

			return true;
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().equals(facing))
				corridors[0] = false;
			return getCoordBaseMode().getAxis().equals(facing.getAxis());
		}
	}
	
	public static class CrossCorridor extends ImpDungeonPiece
	{
		boolean light;
		
		public CrossCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_CROSS_CORRIDOR, 0, 3);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = 10;
			int zWidth = 10;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 5, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.3F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			
			if(ctxt.rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*10, 0, xOffset*10), coordBaseMode.rotateY(), index + 1);
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*10, 0, -xOffset*10), coordBaseMode.rotateYCCW(), index + 1);
			} else
			{
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.add(zOffset*10, 0, -xOffset*10), coordBaseMode.rotateYCCW(), index + 1);
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.add(-zOffset*10, 0, xOffset*10), coordBaseMode.rotateY(), index + 1);
			}
			corridors[1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 2);
		}
		
		public CrossCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_CROSS_CORRIDOR, nbt, 3);
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("l", light);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {


			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 4, 9, 0, 5, floorBlock, floorBlock, false);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 6, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 6, 6, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 2, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 3, 9, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 6, 9, 4, 6, wallBlock, wallBlock, false);

			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 4, 9, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 4, 4, 5, 4, 5);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 3, 5, 4, 3, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 7, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 6, 5, 4, 6, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 2, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 4, 3, 4, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 4, 4, 9, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 4, 6, 4, 5, wallDecor, wallDecor, false);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 5, 3, 6, 5, 6, wallBlock, wallBlock, false);

			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 9, 5, 3, 9, wallBlock, wallBlock, false);
			if(corridors[2])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 1, 4, 9, 3, 5, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState lightBlock = blocks.getBlockState("light_block");
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 5, 5, 5, lightBlock, lightBlock, false);
			}

			return true;
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().rotateY().equals(facing))
				corridors[0] = false;
			else if(getCoordBaseMode().equals(facing))
				corridors[1] = false;
			else if(getCoordBaseMode().rotateYCCW().equals(facing))
				corridors[2] = false;
			return true;
		}
	}
	
	public static class TurnCorridor extends ImpDungeonPiece
	{
		boolean light;
		
		public TurnCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_TURN_CORRIDOR, 0, 2);
			boolean direction = ctxt.rand.nextBoolean();
			if(direction)
				setCoordBaseMode(coordBaseMode.rotateY());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = 7;
			int zWidth = 7;
			
			int i = coordBaseMode.getAxisDirection().getOffset() + 1;
			int j = direction^(coordBaseMode.getAxis() == Direction.Axis.Z)^(coordBaseMode.getAxisDirection() == Direction.AxisDirection.POSITIVE)?2:0;
			int x = pos.getX() - 4 + (getCoordBaseMode() == Direction.NORTH || getCoordBaseMode() == Direction.WEST ? 3 : 0);
			int z = pos.getZ() - 4 + (getCoordBaseMode() == Direction.NORTH || getCoordBaseMode() == Direction.EAST ? 3 : 0);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			light = ctxt.rand.nextFloat() < 0.2F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			Direction newFacing = direction ? coordBaseMode.rotateYCCW() : coordBaseMode.rotateY();
			int xOffset = newFacing.getXOffset();
			int zOffset = newFacing.getZOffset();
			corridors[direction ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), newFacing, index + 1);
		}
		
		public TurnCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_TURN_CORRIDOR, nbt, 2);
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("l", light);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {


			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 5, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 4, 3, 4, 5, wallBlock, wallBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);

			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.WEST), 5, 2, 3, structureBoundingBoxIn);
				setBlockState(worldIn, torch.with(WallTorchBlock.HORIZONTAL_FACING, Direction.NORTH), 3, 2, 5, structureBoundingBoxIn);
			}

			return true;
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().rotateY().equals(facing))
				corridors[1] = false;
			else if(getCoordBaseMode().getOpposite().equals(facing))
				corridors[0] = false;
			else return false;
			return true;
		}
	}
	
	public static class ReturnRoom extends ImpDungeonPiece
	{
		public ReturnRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_RETURN_ROOM, 0, 0);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 8 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 8 : 6;
			
			int x = pos.getX() - (xWidth/2 - 1) - coordBaseMode.getXOffset();
			int z = pos.getZ() - (zWidth/2 - 1) - coordBaseMode.getZOffset();
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 10, z + zWidth - 1);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		public ReturnRoom(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_RETURN_ROOM, nbt, 0);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}

		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");

			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 4, 3, 0, 5, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 4, 4, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 4, 3, 9, 5);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 4, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 4, 5, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 4, 0, 3, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 3, 4, 5, 3, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 3, 7, 3, 3, 7, wallDecor, wallDecor, false);

			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 3, 4, 10, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 6, 4, 10, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 4, 1, 10, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 5, 4, 4, 10, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 10, 4, 3, 10, 5, light, light, false);

			placeReturnNode(worldIn, structureBoundingBoxIn, 2, 1, 4);

			return true;
		}
	}
	
	public static class ReturnRoomAlt extends ImpDungeonPiece
	{
		public ReturnRoomAlt(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_ALT_RETURN_ROOM, 0, 0);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = coordBaseMode.getAxis().equals(Direction.Axis.X) ? 10 : 8;
			int zWidth = coordBaseMode.getAxis().equals(Direction.Axis.Z) ? 10 : 8;
			
			int x = pos.getX() - (xWidth/2 - 1);	//This method works for symmetrical and centered componenets
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 10, z + zWidth - 1);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		public ReturnRoomAlt(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_ALT_RETURN_ROOM, nbt, 0);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorStairsFront = blocks.getStairs("structure_secondary_stairs", Direction.SOUTH, false);
			BlockState floorStairsBack = blocks.getStairs("structure_secondary_stairs", Direction.NORTH, false);
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 1, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 7, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, -1, 4, 1, -1, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, -1, 4, 5, -1, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, -1, 4, 6, -1, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 6, 0, 8, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 1);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 4, 3);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 5, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 6, 6, 5, 8);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 0, 3, floorStairsBack, floorStairsBack, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 6, 0, 6, floorStairsFront, floorStairsFront, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 2, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 1, 0, 5, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 1, 1, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 1, 1, 7, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 4, 1, 7, 5, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 2, 7, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 9, 7, 5, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 2, 6, 5, 3, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 4, 6, 6, 8, wallBlock, wallBlock, false);
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 5, 1, 6, 6, light, light, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 5, 6, 6, 6, light, light, false);
			
			placeReturnNode(worldIn, structureBoundingBoxIn, 3, 1, 7);
			
			return true;
		}
	}
	public static class SpawnerRoom extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2;
		
		public SpawnerRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_SPAWNER_ROOM, 0, 0);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 7 : 8;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 7 : 8;
			
			int x = pos.getX() - (getCoordBaseMode().equals(Direction.WEST)?2:3) - coordBaseMode.getXOffset();
			int z = pos.getZ() - (getCoordBaseMode().equals(Direction.NORTH)?2:3) - coordBaseMode.getZOffset();
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
		}
		
		public SpawnerRoom(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_SPAWNER_ROOM, nbt, 0);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 2, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 6, 0, 5, floorBlock, floorBlock, false);
			setBlockState(worldIn, floorBlock, 1, 0, 2, structureBoundingBoxIn);
			setBlockState(worldIn, floorBlock, 6, 0, 2, structureBoundingBoxIn);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 5, 4, 0, 5, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 6, 3, 5);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 1, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 2, 6, 3, 2);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 2, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 4, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 1, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 1, 6, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 4, 5, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 6, 7, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 6, 4, 5, wallBlock, wallBlock, false);
			setBlockState(worldIn, wallBlock, 1, 4, 2, structureBoundingBoxIn);
			setBlockState(worldIn, wallBlock, 6, 4, 2, structureBoundingBoxIn);
			
			if(spawner1)
			{
				BlockPos spawnerPos = getActualPos(1, 1, 2);
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = getActualPos(6, 1, 2);
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			
			BlockPos chestPos = getActualPos(3, 1, 5);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = getActualPos(4, 1, 5);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			return true;
		}
	}
	
	public static class BookcaseRoom extends ImpDungeonPiece
	{
		float bookChance;
		boolean light;
		
		public BookcaseRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_BOOKCASE_ROOM, 0, 0);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = 8;
			int zWidth = 8;
			
			int x = pos.getX() - (xWidth/2 - 1) - coordBaseMode.getXOffset();
			int z = pos.getZ() - (zWidth/2 - 1) - coordBaseMode.getZOffset();
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			light = ctxt.rand.nextFloat() < 0.4F;
			bookChance = ctxt.rand.nextBoolean() ? 0 : 0.8F;
		}
		
		public BookcaseRoom(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_BOOKCASE_ROOM, nbt, 0);
			bookChance = nbt.getFloat("b");
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putFloat("b", bookChance);
			tagCompound.putBoolean("l", light);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getCoordBaseMode().getOpposite().equals(facing);
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 0, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 6, 0, 1, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 6, 6, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 6, 0, 5, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 3, 4, 0, 4, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 5, 0, 2, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 5, 5, 0, 5, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 3, 2, 0, 4, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 3, 5, 0, 4, floorDecor, floorDecor, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 0);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 1, 6, 4, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 5, 2, 5, 5, 5);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 7, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 6, 5, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, 0, 4, 5, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 5, 1, 1, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 5, 1, 6, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 1, 5, 5, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 5, 6, 5, 5, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 1, 6, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 6, 3, 4, 6, 4, wallDecor, wallDecor, false);
			
			BlockState bookshelf = Blocks.BOOKSHELF.getDefaultState();
			
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 1, 1, 4, 2, bookshelf, bookshelf, false, false);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 5, 1, 4, 6, bookshelf, bookshelf, false, false);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 1, 6, 4, 2, bookshelf, bookshelf, false, false);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 5, 6, 4, 6, bookshelf, bookshelf, false, false);
			generateMaybeBox(worldIn, structureBoundingBoxIn, randomIn, bookChance, 3, 1, 6, 4, 4, 6, bookshelf, bookshelf, false, false);
			
			if(light)
			{
				BlockState torch = blocks.getBlockState("torch");
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 2, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 5, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 2, 1, 5, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					setBlockState(worldIn, torch, 5, 1, 5, structureBoundingBoxIn);
			}
			
			return true;
		}
	}
	
	public static class SpawnerCorridor extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2, chestPos;
		
		public SpawnerCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_SPAWNER_CORRIDOR, 0, 2);
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setCoordBaseMode(coordBaseMode.getOpposite());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 10 : 6;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 10 : 6;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		public SpawnerCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_SPAWNER_CORRIDOR, nbt, 2);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().getAxis().equals(facing.getAxis()))
			{
				corridors[getCoordBaseMode().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 3, 1, 0, 6, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 1, 0, 3, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 3, 1, 3, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 3, 4, 3, 6);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 2, 0, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 1, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 7, 4, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 3, 1, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 3, 4, 4, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 4, 0, 2, 5, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 2, 4, 5, 2, 5, wallDecor, wallDecor, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = getActualPos(1, 1, 4);
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = getActualPos(1, 1, 5);
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			
			int z = chestPos ? 4 : 5;
			BlockPos chestPos = getActualPos(4, 1, z);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().rotateY(), MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 3, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 9, 3, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}
	}
	
	public static class OgreCorridor extends ImpDungeonPiece
	{
		private boolean chestPos;
		private boolean ogreSpawned;
		
		public OgreCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_OGRE_CORRIDOR, 0, 1);
			setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 10 : 8;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 10 : 8;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			ctxt.generatedOgreRoom = true;
			
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		public OgreCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_OGRE_CORRIDOR, nbt, 1);
			chestPos = nbt.getBoolean("ch");
			ogreSpawned = nbt.getBoolean("spwn");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("ch", chestPos);
			tagCompound.putBoolean("spwn", ogreSpawned);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().equals(facing.getOpposite()))
			{
				corridors[0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 4, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 2, 2, 0, 7, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 2, 5, 0, 7, floorDecor, floorDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 1, 0, 7, light, light, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 6, 0, 7, light, light, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 1, 1, 2, 6, 5, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			fillWithAir(worldIn, structureBoundingBoxIn, 3, 1, 7, 4, 3, 9);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 0, 2, 3, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 1, 6, 3, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 7, 5, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 8, 2, 3, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, 9, 2, 3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 8, 6, 3, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 5, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 6, 6, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 0, 5, 4, 0, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 8, 6, 6, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 9, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 2, 6, 6, 2, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 3, 1, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, 7, 6, 6, 7, wallDecor, wallDecor, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 6, 3, 6, 6, 6, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 7, 3, 5, 7, 6, wallBlock, wallBlock, false);
			
			
			int x = chestPos ? 1 : 6;
			Direction chestDirection = chestPos ? getCoordBaseMode().rotateYCCW() : getCoordBaseMode().rotateY();
			BlockPos chestPos = getActualPos(x, 1, 4);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, chestDirection, this.chestPos ? ChestType.LEFT : ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = getActualPos(x, 1, 5);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, chestDirection, this.chestPos ? ChestType.RIGHT : ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			if(!ogreSpawned && structureBoundingBoxIn.isVecInside(getActualPos(3, 1, 4)))
				spawnOgre(3, 1, 4, worldIn, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 9, 4, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}
		
		private void spawnOgre(int xPos, int yPos, int zPos, IWorld worldIn, Random rand)
		{
			BlockPos pos = getActualPos(xPos, yPos, zPos);
			OgreEntity ogre = MSEntityTypes.OGRE.create(worldIn.getWorld());
			ogre.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat()*360F, 0);
			ogre.onInitialSpawn(worldIn, null, SpawnReason.STRUCTURE, null, null);
			ogre.setHomePosAndDistance(pos, 2);
			worldIn.addEntity(ogre);
		}
	}
	
	public static class LargeSpawnerCorridor extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2, chestPos;
		
		public LargeSpawnerCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_LARGE_SPAWNER_CORRIDOR, 0, 2);
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setCoordBaseMode(coordBaseMode.getOpposite());
			else setCoordBaseMode(coordBaseMode);
			
			int xWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.X) ? 10 : 10;
			int zWidth = getCoordBaseMode().getAxis().equals(Direction.Axis.Z) ? 10 : 10;
			
			int x = pos.getX() - (xWidth/2 - 1);
			int z = pos.getZ() - (zWidth/2 - 1);
			
			this.boundingBox = new MutableBoundingBox(x, pos.getY(), z, x + xWidth - 1, pos.getY() + 4, z + zWidth - 1);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			if(ctxt.rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = ctxt.rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getXOffset();
			int zOffset = coordBaseMode.getZOffset();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.add(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		public LargeSpawnerCorridor(TemplateManager templates, CompoundNBT nbt)
		{
			super(MSStructurePieces.IMP_LARGE_SPAWNER_CORRIDOR, nbt, 2);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound)
		{
			super.readAdditional(tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getCoordBaseMode().getAxis().equals(facing.getAxis()))
			{
				corridors[getCoordBaseMode().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn, MutableBoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn) {
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn.getSettings());
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 2, 3, 0, 7, floorBlock, floorBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 2, 8, 0, 7, floorBlock, floorBlock, false);
			fillWithAir(worldIn, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			fillWithAir(worldIn, structureBoundingBoxIn, 0, 1, 2, 3, 3, 7);
			fillWithAir(worldIn, structureBoundingBoxIn, 6, 1, 2, 9, 3, 7);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 0, 3, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 1, 2, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 0, 7, 2, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 0, 6, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 1, 8, 4, 1, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 7, 0, 7, 8, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 0, 4, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 9, 0, 1, 9, 4, 8, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 0, 7, 3, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 0, 7, 6, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 4, 0, 5, 4, 9, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 2, 3, 4, 7, wallBlock, wallBlock, false);
			fillWithBlocks(worldIn, structureBoundingBoxIn, 6, 4, 2, 8, 4, 7, wallBlock, wallBlock, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = getActualPos(1, 1, 3);
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = getActualPos(8, 1, 5);
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, worldIn, structureBoundingBoxIn, MinestuckConfig.SERVER.hardMode ? MSEntityTypes.LICH : MSEntityTypes.IMP);
			}
			
			BlockPos chestPos = getActualPos(4, 1, 4);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = getActualPos(5, 1, 4);
			StructureBlockUtil.placeChest(chestPos, worldIn, structureBoundingBoxIn, getCoordBaseMode().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				fillWithBlocks(worldIn, structureBoundingBoxIn, 4, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
			
			return true;
		}	
	}
}