package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockUtil;
import com.mraof.minestuck.item.loot.MSLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Objects;
import java.util.Random;

public class ImpDungeonPieces
{
	public static class EntryCorridor extends ImpDungeonPiece
	{
		public static EntryCorridor create(Direction orientation, int posX, int posZ, Random rand)
		{
			int offset = orientation.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 4 : -3;
			int x = posX + (orientation.getAxis().equals(Direction.Axis.Z) ? 0 : offset);
			int y = 45 - rand.nextInt(8);
			int z = posZ + (orientation.getAxis().equals(Direction.Axis.X) ? 0 : offset);
			return new EntryCorridor(orientation, x, y, z);
		}
		
		private EntryCorridor(Direction orientation, int x, int y, int z)
		{
			super(MSStructurePieces.IMP_ENTRY_CORRIDOR.get(), 0, makeBoundingBox(x, y, z, orientation, 6, 7, 10), 2);
			setOrientation(orientation);
		}
		
		public EntryCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_ENTRY_CORRIDOR.get(), nbt, 2);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing))
				corridors[0] = false;
			else if(getOrientation().getOpposite().equals(facing))
				corridors[1] = false;
			return getOrientation().getAxis().equals(facing.getAxis());
		}
		
		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor builder, Random rand)
		{
			BlockPos compoPos = new BlockPos(boundingBox.minX() + (boundingBox.getXSpan()/2 - 1), boundingBox.minY(), boundingBox.minZ() + (boundingBox.getZSpan()/2 - 1));
			
			StructureContext ctxt = new StructureContext(builder, rand);
			ctxt.compoGen[6][6] = this;
			
			Direction orientation = Objects.requireNonNull(getOrientation());
			int xOffset = orientation.getStepX();
			int zOffset = orientation.getStepZ();
			if(rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.offset(xOffset*10, 0, zOffset*10), orientation, 0);
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.offset(-xOffset*10, 0, -zOffset*10), orientation.getOpposite(), 0);
			} else
			{
				corridors[1] = !generatePart(ctxt, 6 - xOffset, 6 - zOffset, compoPos.offset(-xOffset*10, 0, -zOffset*10), orientation.getOpposite(), 0);
				corridors[0] = !generatePart(ctxt, 6 + xOffset, 6 + zOffset, compoPos.offset(xOffset*10, 0, zOffset*10), orientation, 0);
			}
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState fluid = blocks.getBlockState("fall_fluid");

			generateBox(level, structureBoundingBoxIn, 1, 0, 4, 4, 0, 6, floorBlock, floorBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 4, 4, 4, 6);
			generateBox(level, structureBoundingBoxIn, 2, 0, 5, 3, 0, 5, fluid, fluid, false);
			generateBox(level, structureBoundingBoxIn, 2, -1, 5, 3, -1, 5, floorDecor, floorDecor, false);

			generateBox(level, structureBoundingBoxIn, 0, 0, 3, 0, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 3, 5, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 3, 4, 5, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 7, 4, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 3, 1, 3, 3, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 7, 1, 3, 7, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 3, 4, 3, 3, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 7, 4, 3, 7, wallDecor, wallDecor, false);

			generateBox(level, structureBoundingBoxIn, 2, 0, 0, 3, 0, 3, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 7, 3, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 8, 3, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 1, 0, 1, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 1, 0, 4, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 1, 8, 1, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 1, 8, 4, 4, 9, wallBlock, wallBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 2, 1, 0, 3, 3, 3);
			generateAirBox(level, structureBoundingBoxIn, 2, 1, 7, 3, 3, 9);

			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 2, 1, 9, 3, 3, 9, wallBlock, wallBlock, false);
			if(corridors[1])
				generateBox(level, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);
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
				ctxt.builder.addPiece(genRoom(facing, pos, xIndex, zIndex, index, ctxt));
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
				component = TurnCorridor.create(facing, pos, xIndex, zIndex, index, ctxt);
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
		ctxt.builder.addPiece(component);
		
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
		StructurePieceAccessor builder;
		Random rand;
		int corridors = 3;
		boolean generatedReturn = false;
		boolean generatedOgreRoom = false;
		
		public StructureContext(StructurePieceAccessor builder, Random rand)
		{
			this.builder = builder;
			this.rand = rand;
		}
	}
	
	public static abstract class ImpDungeonPiece extends ImprovedStructurePiece
	{
		protected final boolean[] corridors;
		
		public ImpDungeonPiece(StructurePieceType structurePieceTypeIn, int componentTypeIn, BoundingBox boundingBox, int corridors)
		{
			super(structurePieceTypeIn, componentTypeIn, boundingBox);
			this.corridors = new boolean[corridors];
		}
		
		public ImpDungeonPiece(StructurePieceType structurePieceTypeIn, CompoundTag nbt, int corridors)
		{
			super(structurePieceTypeIn, nbt);
			this.corridors = new boolean[corridors];
			for(int i = 0; i < corridors; i++)
				this.corridors[i] = nbt.getBoolean("bl"+i);
		}
		
		protected abstract boolean connectFrom(Direction facing);
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
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
			super(MSStructurePieces.IMP_STRAIGHT_CORRIDOR.get(), 0, makeGridBoundingBox(3, 0, 0, 4, 5, 10, pos, coordBaseMode), 1);
			setOrientation(coordBaseMode);
			
			light = true;//ctxt.rand.nextFloat() < 0.1F;
			if(light)
				lightPos = (byte) ctxt.rand.nextInt(4);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getStepX();
			int zOffset = coordBaseMode.getStepZ();
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		public StraightCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_STRAIGHT_CORRIDOR.get(), nbt, 1);
			light = nbt.getBoolean("l");
			if(light)
				lightPos = nbt.getByte("lpos");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("l", light);
			if(light)
				tagCompound.putByte("lpos", lightPos);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			generateBox(level, structureBoundingBoxIn, 1, 0, 0, 2, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 0, 2, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 0, 0, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 3, 4, 9, wallBlock, wallBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 0, 2, 3, 9);

			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 1, 1, 9, 2, 3, 9, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				if(lightPos/2 == 0)
					placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 2, 2, 4 + lightPos%2, structureBoundingBoxIn);
				else placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 4 + lightPos%2, structureBoundingBoxIn);
			}
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing))
				corridors[0] = false;
			return getOrientation().getAxis().equals(facing.getAxis());
		}
	}
	
	public static class CrossCorridor extends ImpDungeonPiece
	{
		boolean light;
		
		public CrossCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_CROSS_CORRIDOR.get(), 0, makeGridBoundingBox(0, 0, 0, 10, 6, 10, pos, coordBaseMode), 3);
			setOrientation(coordBaseMode);
			
			light = ctxt.rand.nextFloat() < 0.3F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			int xOffset = coordBaseMode.getStepX();
			int zOffset = coordBaseMode.getStepZ();
			
			if(ctxt.rand.nextBoolean())
			{
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.offset(-zOffset*10, 0, xOffset*10), coordBaseMode.getClockWise(), index + 1);
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.offset(zOffset*10, 0, -xOffset*10), coordBaseMode.getCounterClockWise(), index + 1);
			} else
			{
				corridors[2] = !generatePart(ctxt, xIndex + zOffset, zIndex - xOffset, pos.offset(zOffset*10, 0, -xOffset*10), coordBaseMode.getCounterClockWise(), index + 1);
				corridors[0] = !generatePart(ctxt, xIndex - zOffset, zIndex + xOffset, pos.offset(-zOffset*10, 0, xOffset*10), coordBaseMode.getClockWise(), index + 1);
			}
			corridors[1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), coordBaseMode, index + 2);
		}
		
		public CrossCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_CROSS_CORRIDOR.get(), nbt, 3);
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("l", light);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			generateBox(level, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 4, 9, 0, 5, floorBlock, floorBlock, false);

			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 0, 6, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 6, 3, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 6, 6, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 6, 2, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 3, 9, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 6, 9, 4, 6, wallBlock, wallBlock, false);

			generateAirBox(level, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			generateAirBox(level, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);
			generateAirBox(level, structureBoundingBoxIn, 6, 1, 4, 9, 3, 5);
			generateAirBox(level, structureBoundingBoxIn, 4, 4, 4, 5, 4, 5);

			generateBox(level, structureBoundingBoxIn, 4, 4, 0, 5, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 3, 5, 4, 3, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 7, 5, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 6, 5, 4, 6, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 0, 4, 4, 2, 4, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 4, 4, 3, 4, 5, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 7, 4, 4, 9, 4, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 4, 4, 6, 4, 5, wallDecor, wallDecor, false);

			generateBox(level, structureBoundingBoxIn, 3, 5, 3, 6, 5, 6, wallBlock, wallBlock, false);

			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			if(corridors[1])
				generateBox(level, structureBoundingBoxIn, 4, 1, 9, 5, 3, 9, wallBlock, wallBlock, false);
			if(corridors[2])
				generateBox(level, structureBoundingBoxIn, 9, 1, 4, 9, 3, 5, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState lightBlock = blocks.getBlockState("light_block");
				generateBox(level, structureBoundingBoxIn, 4, 5, 4, 5, 5, 5, lightBlock, lightBlock, false);
			}
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().getClockWise().equals(facing))
				corridors[0] = false;
			else if(getOrientation().equals(facing))
				corridors[1] = false;
			else if(getOrientation().getCounterClockWise().equals(facing))
				corridors[2] = false;
			return true;
		}
	}
	
	public static class TurnCorridor extends ImpDungeonPiece
	{
		boolean light;
		
		public static ImpDungeonPiece create(Direction orientation, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			boolean direction = ctxt.rand.nextBoolean();
			return new TurnCorridor(direction, direction ? orientation.getClockWise() : orientation, pos, xIndex, zIndex, index, ctxt);
		}
		
		private TurnCorridor(boolean direction, Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_TURN_CORRIDOR.get(), 0, makeGridBoundingBox(0, 0, 0, 7, 5, 7, pos, coordBaseMode), 2);
			
			setOrientation(coordBaseMode);
			
			light = ctxt.rand.nextFloat() < 0.2F;
			
			ctxt.compoGen[xIndex][zIndex] = this;
			Direction newFacing = direction ? coordBaseMode.getOpposite() : coordBaseMode.getClockWise();
			int xOffset = newFacing.getStepX();
			int zOffset = newFacing.getStepZ();
			corridors[direction ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), newFacing, index + 1);
		}
		
		public TurnCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_TURN_CORRIDOR.get(), nbt, 2);
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("l", light);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{

			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");

			generateBox(level, structureBoundingBoxIn, 4, 0, 0, 5, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 4, 3, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 0, 6, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 3, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 3, 2, 4, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 6, 5, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 0, 5, 4, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 4, 4, 3, 4, 5, wallBlock, wallBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 4, 1, 0, 5, 3, 5);
			generateAirBox(level, structureBoundingBoxIn, 0, 1, 4, 3, 3, 5);

			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 4, 1, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				generateBox(level, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);

			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 3, structureBoundingBoxIn);
				placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.NORTH), 3, 2, 5, structureBoundingBoxIn);
			}
		}

		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().getClockWise().equals(facing))
				corridors[1] = false;
			else if(getOrientation().getOpposite().equals(facing))
				corridors[0] = false;
			else return false;
			return true;
		}
	}
	
	public static class ReturnRoom extends ImpDungeonPiece
	{
		public ReturnRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_RETURN_ROOM.get(), 0, makeGridBoundingBox(2, 0, 0, 6, 11, 8, pos, coordBaseMode), 0);
			setOrientation(coordBaseMode);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		public ReturnRoom(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_RETURN_ROOM.get(), nbt, 0);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}

		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);

			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");

			generateBox(level, structureBoundingBoxIn, 2, 0, 0, 3, 0, 2, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 4, 3, 0, 5, floorDecor, floorDecor, false);
			generateAirBox(level, structureBoundingBoxIn, 2, 1, 0, 3, 3, 2);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 3, 4, 4, 6);
			generateAirBox(level, structureBoundingBoxIn, 2, 5, 4, 3, 9, 5);

			generateBox(level, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 2, 0, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 2, 5, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 7, 4, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 0, 3, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 5, 2, 4, 5, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 3, 4, 0, 3, 5, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 5, 3, 4, 5, 3, 5, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 2, 3, 7, 3, 3, 7, wallDecor, wallDecor, false);

			generateBox(level, structureBoundingBoxIn, 1, 5, 3, 4, 10, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 5, 6, 4, 10, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 5, 4, 1, 10, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 5, 4, 4, 10, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 10, 4, 3, 10, 5, light, light, false);

			placeReturnNode(level, structureBoundingBoxIn, 2, 1, 4);
		}
	}
	
	public static class ReturnRoomAlt extends ImpDungeonPiece
	{
		public ReturnRoomAlt(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_ALT_RETURN_ROOM.get(), 0, makeGridBoundingBox(1, -1, 0, 8, 8, 10, pos, coordBaseMode), 0);
			setOrientation(coordBaseMode);
			
			ctxt.generatedReturn = true;
			ctxt.compoGen[xIndex][zIndex] = this;
		}
		
		public ReturnRoomAlt(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_ALT_RETURN_ROOM.get(), nbt, 0);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorStairsFront = blocks.getStairs("structure_secondary_stairs", Direction.SOUTH, false);
			BlockState floorStairsBack = blocks.getStairs("structure_secondary_stairs", Direction.NORTH, false);
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");
			
			generateBox(level, structureBoundingBoxIn, 3, 1, 0, 4, 1, 1, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 1, 2, 7, 1, 2, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 4, 1, 0, 5, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 4, 5, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 4, 6, 0, 5, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 1, 1, 7, 6, 1, 8, floorBlock, floorBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 3, 2, 0, 4, 4, 1);
			generateAirBox(level, structureBoundingBoxIn, 1, 2, 2, 6, 5, 3);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 3, 6, 6, 5);
			generateAirBox(level, structureBoundingBoxIn, 1, 2, 6, 6, 6, 8);
			generateBox(level, structureBoundingBoxIn, 1, 1, 3, 6, 1, 3, floorStairsBack, floorStairsBack, false);
			generateBox(level, structureBoundingBoxIn, 1, 1, 6, 6, 1, 6, floorStairsFront, floorStairsFront, false);
			
			generateBox(level, structureBoundingBoxIn, 2, 2, 0, 2, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 2, 0, 5, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 2, 1, 1, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 2, 1, 7, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 5, 1, 7, 6, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 1, 2, 0, 6, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 1, 2, 7, 6, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 2, 9, 7, 6, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 5, 0, 4, 5, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 6, 2, 6, 6, 3, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 7, 4, 6, 7, 8, wallBlock, wallBlock, false);
			
			generateBox(level, structureBoundingBoxIn, 1, 7, 5, 1, 7, 6, light, light, false);
			generateBox(level, structureBoundingBoxIn, 6, 7, 5, 6, 7, 6, light, light, false);
			
			placeReturnNode(level, structureBoundingBoxIn, 3, 2, 7);
		}
	}
	public static class SpawnerRoom extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2;
		
		public SpawnerRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_SPAWNER_ROOM.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 7, pos, coordBaseMode), 0);
			setOrientation(coordBaseMode);
			
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
		
		public SpawnerRoom(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_SPAWNER_ROOM.get(), nbt, 0);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 4, 0, 2, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 3, 6, 0, 5, floorBlock, floorBlock, false);
			placeBlock(level, floorBlock, 1, 0, 2, structureBoundingBoxIn);
			placeBlock(level, floorBlock, 6, 0, 2, structureBoundingBoxIn);
			generateBox(level, structureBoundingBoxIn, 3, 0, 5, 4, 0, 5, floorDecor, floorDecor, false);
			generateAirBox(level, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 3, 6, 3, 5);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 2, 1, 3, 2);
			generateAirBox(level, structureBoundingBoxIn, 6, 1, 2, 6, 3, 2);
			generateBox(level, structureBoundingBoxIn, 2, 0, 0, 2, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 0, 5, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 2, 2, 4, 2, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 2, 5, 4, 2, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 1, 1, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 1, 6, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 1, 0, 4, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 1, 7, 4, 5, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 6, 7, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 4, 0, 4, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 3, 6, 4, 5, wallBlock, wallBlock, false);
			placeBlock(level, wallBlock, 1, 4, 2, structureBoundingBoxIn);
			placeBlock(level, wallBlock, 6, 4, 2, structureBoundingBoxIn);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(1, 2), this.getWorldY(1), this.getWorldZ(1, 2));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(6, 2), this.getWorldY(1), this.getWorldZ(6, 2));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			
			BlockPos chestPos = new BlockPos(this.getWorldX(3, 5), this.getWorldY(1), this.getWorldZ(3, 5));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getWorldX(4, 5), this.getWorldY(1), this.getWorldZ(4, 5));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
		}
	}
	
	public static class BookcaseRoom extends ImpDungeonPiece
	{
		float bookChance;
		boolean light;
		
		public BookcaseRoom(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_BOOKCASE_ROOM.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 8, pos, coordBaseMode), 0);
			setOrientation(coordBaseMode);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			
			light = ctxt.rand.nextFloat() < 0.4F;
			bookChance = ctxt.rand.nextBoolean() ? 0 : 0.8F;
		}
		
		public BookcaseRoom(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_BOOKCASE_ROOM.get(), nbt, 0);
			bookChance = nbt.getFloat("b");
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putFloat("b", bookChance);
			tagCompound.putBoolean("l", light);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 4, 0, 0, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 1, 6, 0, 1, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 6, 6, 0, 6, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 2, 1, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 2, 6, 0, 5, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 3, 4, 0, 4, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 2, 5, 0, 2, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 5, 5, 0, 5, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 3, 2, 0, 4, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 3, 5, 0, 4, floorDecor, floorDecor, false);
			generateAirBox(level, structureBoundingBoxIn, 3, 1, 0, 4, 3, 0);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 1, 6, 4, 6);
			generateAirBox(level, structureBoundingBoxIn, 2, 5, 2, 5, 5, 5);
			generateBox(level, structureBoundingBoxIn, 0, 0, 0, 2, 5, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 0, 7, 5, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 1, 0, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 1, 7, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 7, 6, 5, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 4, 0, 4, 5, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 5, 1, 1, 5, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 5, 1, 6, 5, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 5, 1, 5, 5, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 5, 6, 5, 5, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 6, 1, 6, 6, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 6, 3, 4, 6, 4, wallDecor, wallDecor, false);
			
			BlockState bookshelf = Blocks.BOOKSHELF.defaultBlockState();
			
			generateMaybeBox(level, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 1, 1, 4, 2, bookshelf, bookshelf, false, false);
			generateMaybeBox(level, structureBoundingBoxIn, randomIn, bookChance, 1, 1, 5, 1, 4, 6, bookshelf, bookshelf, false, false);
			generateMaybeBox(level, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 1, 6, 4, 2, bookshelf, bookshelf, false, false);
			generateMaybeBox(level, structureBoundingBoxIn, randomIn, bookChance, 6, 1, 5, 6, 4, 6, bookshelf, bookshelf, false, false);
			generateMaybeBox(level, structureBoundingBoxIn, randomIn, bookChance, 3, 1, 6, 4, 4, 6, bookshelf, bookshelf, false, false);
			
			if(light)
			{
				BlockState torch = blocks.getBlockState("torch");
				if(randomIn.nextBoolean())
					placeBlock(level, torch, 2, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					placeBlock(level, torch, 5, 1, 2, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					placeBlock(level, torch, 2, 1, 5, structureBoundingBoxIn);
				if(randomIn.nextBoolean())
					placeBlock(level, torch, 5, 1, 5, structureBoundingBoxIn);
			}
		}
	}
	
	public static class SpawnerCorridor extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2, chestPos;
		
		public SpawnerCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_SPAWNER_CORRIDOR.get(), 0, makeGridBoundingBox(2, 0, 0, 6, 5, 10, pos, coordBaseMode), 2);
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setOrientation(coordBaseMode.getOpposite());
			else setOrientation(coordBaseMode);
			
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
			
			int xOffset = coordBaseMode.getStepX();
			int zOffset = coordBaseMode.getStepZ();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		public SpawnerCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_SPAWNER_CORRIDOR.get(), nbt, 2);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().getAxis().equals(facing.getAxis()))
			{
				corridors[getOrientation().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			
			generateBox(level, structureBoundingBoxIn, 2, 0, 0, 3, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 3, 1, 0, 6, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 3, 4, 0, 6, floorBlock, floorBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 2, 1, 0, 3, 3, 9);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 3, 1, 3, 6);
			generateAirBox(level, structureBoundingBoxIn, 4, 1, 3, 4, 3, 6);
			generateBox(level, structureBoundingBoxIn, 1, 0, 0, 1, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 0, 4, 4, 2, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 2, 0, 4, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 2, 5, 4, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 7, 1, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 0, 7, 4, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 0, 3, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 3, 1, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 3, 4, 4, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 2, 4, 0, 2, 5, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 5, 2, 4, 5, 2, 5, wallDecor, wallDecor, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(1, 4), this.getWorldY(1), this.getWorldZ(1, 4));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(1, 5), this.getWorldY(1), this.getWorldZ(1, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			
			int z = chestPos ? 4 : 5;
			BlockPos chestPos = new BlockPos(this.getWorldX(4, z), this.getWorldY(1), this.getWorldZ(4, z));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getClockWise(), MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 2, 0, 0, 3, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				generateBox(level, structureBoundingBoxIn, 2, 0, 9, 3, 3, 9, wallBlock, wallBlock, false);
		}
	}
	
	public static class OgreCorridor extends ImpDungeonPiece
	{
		private boolean chestPos;
		private boolean ogreSpawned;
		
		public OgreCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_OGRE_CORRIDOR.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 10, pos, coordBaseMode), 1);
			setOrientation(coordBaseMode);
			
			ctxt.compoGen[xIndex][zIndex] = this;
			ctxt.generatedOgreRoom = true;
			
			chestPos = ctxt.rand.nextBoolean();
			
			int xOffset = coordBaseMode.getStepX();
			int zOffset = coordBaseMode.getStepZ();
			
			corridors[0] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
		}
		
		public OgreCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_OGRE_CORRIDOR.get(), nbt, 1);
			chestPos = nbt.getBoolean("ch");
			ogreSpawned = nbt.getBoolean("spwn");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("ch", chestPos);
			tagCompound.putBoolean("spwn", ogreSpawned);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing.getOpposite()))
			{
				corridors[0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			BlockState floorDecor = blocks.getBlockState("structure_secondary_decorative");
			BlockState light = blocks.getBlockState("light_block");
			
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 4, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 2, 2, 0, 7, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 2, 5, 0, 7, floorDecor, floorDecor, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 2, 1, 0, 7, light, light, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 2, 6, 0, 7, light, light, false);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 2, 6, 5, 7);
			generateAirBox(level, structureBoundingBoxIn, 2, 6, 3, 5, 6, 6);
			generateAirBox(level, structureBoundingBoxIn, 3, 1, 0, 4, 3, 2);
			generateAirBox(level, structureBoundingBoxIn, 3, 1, 7, 4, 3, 9);
			generateBox(level, structureBoundingBoxIn, 1, 0, 1, 2, 3, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 0, 2, 3, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 1, 6, 3, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 1, 0, 5, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 1, 7, 5, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 8, 2, 3, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 0, 9, 2, 3, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 8, 6, 3, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 5, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 1, 6, 6, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 0, 5, 4, 0, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 8, 6, 6, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 4, 9, 5, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 6, 2, 6, 6, 2, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 1, 6, 3, 1, 6, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 6, 7, 6, 6, 7, wallDecor, wallDecor, false);
			generateBox(level, structureBoundingBoxIn, 6, 6, 3, 6, 6, 6, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 2, 7, 3, 5, 7, 6, wallBlock, wallBlock, false);
			
			
			int x = chestPos ? 1 : 6;
			Direction chestDirection = chestPos ? getOrientation().getCounterClockWise() : getOrientation().getClockWise();
			BlockPos chestPos = new BlockPos(this.getWorldX(x, 4), this.getWorldY(1), this.getWorldZ(x, 4));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, chestDirection, this.chestPos ? ChestType.LEFT : ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getWorldX(x, 5), this.getWorldY(1), this.getWorldZ(x, 5));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, chestDirection, this.chestPos ? ChestType.RIGHT : ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			if(!ogreSpawned && structureBoundingBoxIn.isInside(new BlockPos(getWorldX(3, 4), getWorldY(1), getWorldZ(3, 4))))
				spawnOgre(3, 1, 4, level, randomIn);
			
			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 3, 0, 9, 4, 3, 9, wallBlock, wallBlock, false);
		}
		
		private void spawnOgre(int xPos, int yPos, int zPos, WorldGenLevel level, Random rand)
		{
			BlockPos pos = new BlockPos(getWorldX(xPos, zPos), getWorldY(yPos), getWorldZ(xPos, zPos));
			OgreEntity ogre = MSEntityTypes.OGRE.create(level.getLevel());
			ogre.moveTo(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat()*360F, 0);
			ogre.finalizeSpawn(level, null, MobSpawnType.STRUCTURE, null, null);
			ogre.restrictTo(pos, 2);
			level.addFreshEntity(ogre);
		}
	}
	
	public static class LargeSpawnerCorridor extends ImpDungeonPiece
	{
		private boolean spawner1, spawner2, chestPos;
		
		public LargeSpawnerCorridor(Direction coordBaseMode, BlockPos pos, int xIndex, int zIndex, int index, StructureContext ctxt)
		{
			super(MSStructurePieces.IMP_LARGE_SPAWNER_CORRIDOR.get(), 0, makeGridBoundingBox(0, 0, 0, 10, 5, 10, pos, coordBaseMode), 2);
			boolean mirror = ctxt.rand.nextBoolean();
			if(mirror)
				setOrientation(coordBaseMode.getOpposite());
			else setOrientation(coordBaseMode);
			
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
			
			int xOffset = coordBaseMode.getStepX();
			int zOffset = coordBaseMode.getStepZ();
			corridors[mirror ? 0 : 1] = !generatePart(ctxt, xIndex + xOffset, zIndex + zOffset, pos.offset(xOffset*10, 0, zOffset*10), coordBaseMode, index + 1);
			
		}
		
		public LargeSpawnerCorridor(CompoundTag nbt)
		{
			super(MSStructurePieces.IMP_LARGE_SPAWNER_CORRIDOR.get(), nbt, 2);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			super.addAdditionalSaveData(context, tagCompound);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		protected boolean connectFrom(Direction facing)
		{
			if(getOrientation().getAxis().equals(facing.getAxis()))
			{
				corridors[getOrientation().equals(facing)?1:0] = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGeneratorIn, Random randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState wallDecor = blocks.getBlockState("structure_primary_decorative");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			
			generateBox(level, structureBoundingBoxIn, 4, 0, 0, 5, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 2, 3, 0, 7, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 2, 8, 0, 7, floorBlock, floorBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 4, 1, 0, 5, 3, 9);
			generateAirBox(level, structureBoundingBoxIn, 0, 1, 2, 3, 3, 7);
			generateAirBox(level, structureBoundingBoxIn, 6, 1, 2, 9, 3, 7);
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 3, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 1, 2, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 0, 7, 2, 4, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 0, 6, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 1, 8, 4, 1, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 7, 0, 7, 8, 4, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 1, 0, 4, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 9, 0, 1, 9, 4, 8, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 7, 3, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 0, 7, 6, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 4, 4, 0, 5, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 2, 3, 4, 7, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 6, 4, 2, 8, 4, 7, wallBlock, wallBlock, false);
			
			if(spawner1)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(1, 3), this.getWorldY(1), this.getWorldZ(1, 3));
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(8, 5), this.getWorldY(1), this.getWorldZ(8, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners());
			}
			
			BlockPos chestPos = new BlockPos(this.getWorldX(4, 4), this.getWorldY(1), this.getWorldZ(4, 4));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getWorldX(5, 4), this.getWorldY(1), this.getWorldZ(5, 4));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(corridors[0])
				generateBox(level, structureBoundingBoxIn, 4, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(corridors[1])
				generateBox(level, structureBoundingBoxIn, 4, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
		}	
	}
	
	private static EntityType<?> getTypeForSpawners()
	{
		return MinestuckConfig.SERVER.hardMode.get() ? MSEntityTypes.LICH : MSEntityTypes.IMP;
	}
	
	/**
	 * A helper function for creating a bounding box rotated and offset based on imp dungeon's 10x10 grid pieces
	 */
	@SuppressWarnings("SameParameterValue")
	private static BoundingBox makeGridBoundingBox(int minX, int minY, int minZ, int sizeX, int sizeY, int sizeZ, BlockPos centerPos, Direction orientation)
	{
		int maxX = minX + sizeX - 1, maxY = minY + sizeY - 1, maxZ = minZ + sizeZ - 1;
		
		int startX = centerPos.getX() - 4, startZ = centerPos.getZ() - 4;
		int endX = startX + 10 - 1, endZ = startZ + 10 - 1;
		int y = centerPos.getY();
		
		return switch(orientation)
				{
					case SOUTH -> new BoundingBox(startX + minX, y + minY, startZ + minZ, startX + maxX, y + maxY, startZ + maxZ);
					case NORTH -> new BoundingBox(endX - maxX, y + minY, endZ - maxZ, endX - minX, y + maxY, endZ - minZ);
					case EAST -> new BoundingBox(startX + minZ, y + minY, endZ - maxX, startX + maxZ, y + maxY, endZ - minX);
					case WEST -> new BoundingBox(endX - maxZ, y + minY, startZ + minX, endX - minZ, y + maxY, startZ + maxX);
					default -> throw new IllegalArgumentException("Invalid orientation");
				};
	}
}