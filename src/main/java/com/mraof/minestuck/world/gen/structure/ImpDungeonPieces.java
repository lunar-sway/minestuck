package com.mraof.minestuck.world.gen.structure;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.underling.OgreEntity;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
public final class ImpDungeonPieces
{
	public static class EntryCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked, isBackBlocked;
		
		public static EntryCorridor create(Direction orientation, int posX, int posZ, RandomSource rand)
		{
			int offset = orientation.getAxisDirection().equals(Direction.AxisDirection.POSITIVE) ? 4 : -3;
			int x = posX + (orientation.getAxis().equals(Direction.Axis.Z) ? 0 : offset);
			int y = 45 - rand.nextInt(8);
			int z = posZ + (orientation.getAxis().equals(Direction.Axis.X) ? 0 : offset);
			return new EntryCorridor(orientation, x, y, z);
		}
		
		private EntryCorridor(Direction orientation, int x, int y, int z)
		{
			super(MSStructures.ImpDungeon.ENTRY_CORRIDOR_PIECE.get(), 0, makeBoundingBox(x, y, z, orientation, 6, 7, 10));
			setOrientation(orientation);
		}
		
		public EntryCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.ENTRY_CORRIDOR_PIECE.get(), nbt);
			this.isFrontBlocked = nbt.getBoolean("bl0");
			this.isBackBlocked = nbt.getBoolean("bl1");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isFrontBlocked);
			tagCompound.putBoolean("bl1", isBackBlocked);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing))
				isFrontBlocked = false;
			else if(getOrientation().getOpposite().equals(facing))
				isBackBlocked = false;
			return getOrientation().getAxis().equals(facing.getAxis());
		}
		
		@Override
		public void addChildren(StructurePiece piece, StructurePieceAccessor builder, RandomSource rand)
		{
			BlockPos compoPos = new BlockPos(boundingBox.minX() + (boundingBox.getXSpan() / 2 - 1), boundingBox.minY(), boundingBox.minZ() + (boundingBox.getZSpan() / 2 - 1));
			
			StructureContext ctxt = new StructureContext(compoPos, builder, rand);
			
			Direction orientation = Objects.requireNonNull(getOrientation());
			if(rand.nextBoolean())
			{
				isFrontBlocked = !generatePartInDirection(0, 0, orientation, 0, ctxt);
				isBackBlocked = !generatePartInDirection(0, 0, orientation.getOpposite(), 0, ctxt);
			} else
			{
				isBackBlocked = !generatePartInDirection(0, 0, orientation.getOpposite(), 0, ctxt);
				isFrontBlocked = !generatePartInDirection(0, 0, orientation, 0, ctxt);
			}
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
			
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 2, 1, 9, 3, 3, 9, wallBlock, wallBlock, false);
			if(isBackBlocked)
				generateBox(level, structureBoundingBoxIn, 2, 1, 0, 3, 3, 0, wallBlock, wallBlock, false);
		}
	}
	
	static boolean generatePartInDirection(int xIndex, int zIndex, Direction direction, int generationDepth, StructureContext ctxt)
	{
		return generatePartAt(xIndex + direction.getStepX(), zIndex + direction.getStepZ(), direction, generationDepth, ctxt);
	}
	
	static boolean generatePartAt(int xIndex, int zIndex, Direction direction, int generationDepth, StructureContext ctxt)
	{
		if(Math.abs(xIndex) > 6 || Math.abs(zIndex) > 6)
			return false;
		
		if(ctxt.findPieceInGridSlot(xIndex, zIndex) instanceof ConnectablePiece piece)
			return piece.connectFrom(direction.getOpposite());
		
		int corridors = ctxt.corridors;
		
		BlockPos pos = ctxt.centerPosForGridSlot(xIndex, zIndex);
		Optional<StructurePiece> optionalPiece = nextPiece(pos, direction, generationDepth, ctxt);
		
		optionalPiece.ifPresent(piece -> {
			ctxt.builder.addPiece(piece);
			if(piece instanceof ConnectablePiece connectablePiece)
				connectablePiece.generateAdjacentPieces(
						(genDirection, depthIncrement) -> generatePartInDirection(xIndex, zIndex, genDirection, generationDepth + depthIncrement, ctxt), ctxt.rand);
		});
		
		ctxt.corridors = corridors;
		
		return optionalPiece.isPresent();
	}
	
	private static Optional<StructurePiece> nextPiece(BlockPos pos, Direction direction, int generationDepth, StructureContext ctxt)
	{
		if(ctxt.rand.nextDouble() >= (1.4 - generationDepth * 0.1))
		{
			if(ctxt.rand.nextDouble() < 1 / 3D)
				return Optional.of(pickRoom(pos, direction, ctxt));
			else
				return Optional.empty();
		}
		
		double i = ctxt.rand.nextDouble();
		if(i < 1.2 - ctxt.corridors * 0.12)    //Cross corridor
		{
			ctxt.corridors += 3;
			return Optional.of(new CrossCorridor(direction, pos, ctxt.rand));
		} else if(i < 0.96 - ctxt.corridors * 0.06)    //Any room
		{
			return Optional.of(pickRoom(pos, direction, ctxt));
		} else    //Straight or corner corridor
		{
			ctxt.corridors -= 1;
			return Optional.of(pickCorridor(pos, direction, ctxt));
		}
	}
	
	private static StructurePiece pickCorridor(BlockPos pos, Direction direction, StructureContext ctxt)
	{
		if(ctxt.rand.nextBoolean())
			return TurnCorridor.create(direction, pos, ctxt.rand);
		else
		{    //Corridor
			double i = ctxt.rand.nextFloat();
			if(i < 0.2)
				return new SpawnerCorridor(direction, pos, ctxt.rand);
			else if(i < 0.3 && !ctxt.generatedOgreRoom)
			{
				ctxt.generatedOgreRoom = true;
				return new OgreCorridor(direction, pos, ctxt.rand);
			} else if(i < 0.4)
				return new LargeSpawnerCorridor(direction, pos, ctxt.rand);
			else
				return new StraightCorridor(direction, pos, ctxt.rand);
		}
	}
	
	private static StructurePiece pickRoom(BlockPos pos, Direction direction, StructureContext ctxt)
	{
		float i = ctxt.rand.nextFloat();
		if(!ctxt.generatedReturn || i < 0.2)
		{
			ctxt.generatedReturn = true;
			if(ctxt.rand.nextBoolean())
				return new ReturnRoom(direction, pos);
			else
				return new ReturnRoomAlt(direction, pos);
		} else if(i < 0.5)
			return new BookcaseRoom(direction, pos, ctxt.rand);
		else
			return new SpawnerRoom(direction, pos, ctxt.rand);
	}
	
	public static class StructureContext
	{
		final BlockPos zeroPos;
		final StructurePieceAccessor builder;
		final RandomSource rand;
		int corridors = 3;
		boolean generatedReturn = false;
		boolean generatedOgreRoom = false;
		
		public StructureContext(BlockPos centerPos, StructurePieceAccessor builder, RandomSource rand)
		{
			this.zeroPos = centerPos;
			this.builder = builder;
			this.rand = rand;
		}
		
		BlockPos centerPosForGridSlot(int xIndex, int zIndex)
		{
			return this.zeroPos.offset(10 * xIndex, 0, 10 * zIndex);
		}
		
		@Nullable
		StructurePiece findPieceInGridSlot(int xIndex, int zIndex)
		{
			BlockPos centerPos = this.centerPosForGridSlot(xIndex, zIndex);
			BoundingBox box = makeGridBoundingBox(0, 0, 0, 10, 2, 10, centerPos, Direction.NORTH);
			return this.builder.findCollisionPiece(box);
		}
	}
	
	public interface ConnectablePiece
	{
		boolean connectFrom(Direction facing);
		
		default void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
		}
	}
	
	public interface LocalPieceGenerator
	{
		boolean tryGenerateInDirection(Direction direction, int depthIncrement);
	}
	
	public static class StraightCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked;
		private final boolean light;
		private byte lightPos;
		
		StraightCorridor(Direction coordBaseMode, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.STRAIGHT_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(3, 0, 0, 4, 5, 10, pos, coordBaseMode));
			setOrientation(coordBaseMode);
			
			light = rand.nextFloat() < 0.4F;
			if(light)
				lightPos = (byte) rand.nextInt(4);
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			isFrontBlocked = !generator.tryGenerateInDirection(orientation, 1);
		}
		
		public StraightCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.STRAIGHT_CORRIDOR_PIECE.get(), nbt);
			this.isFrontBlocked = nbt.getBoolean("bl0");
			light = nbt.getBoolean("l");
			if(light)
				lightPos = nbt.getByte("lpos");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isFrontBlocked);
			tagCompound.putBoolean("l", light);
			if(light)
				tagCompound.putByte("lpos", lightPos);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
		{
			StructureBlockRegistry blocks = StructureBlockRegistry.getOrDefault(chunkGeneratorIn);
			
			BlockState wallBlock = blocks.getBlockState("structure_primary");
			BlockState floorBlock = blocks.getBlockState("structure_secondary");
			
			generateBox(level, structureBoundingBoxIn, 1, 0, 0, 2, 0, 9, floorBlock, floorBlock, false);
			generateBox(level, structureBoundingBoxIn, 1, 4, 0, 2, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 0, 0, 0, 0, 4, 9, wallBlock, wallBlock, false);
			generateBox(level, structureBoundingBoxIn, 3, 0, 0, 3, 4, 9, wallBlock, wallBlock, false);
			generateAirBox(level, structureBoundingBoxIn, 1, 1, 0, 2, 3, 9);
			
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 1, 1, 9, 2, 3, 9, wallBlock, wallBlock, false);
			
			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				if(lightPos / 2 == 0)
					placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 2, 2, 4 + lightPos % 2, structureBoundingBoxIn);
				else
					placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.EAST), 1, 2, 4 + lightPos % 2, structureBoundingBoxIn);
			}
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing))
				isFrontBlocked = false;
			return getOrientation().getAxis().equals(facing.getAxis());
		}
	}
	
	public static class CrossCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked, isLeftBlocked, isRightBlocked;
		boolean light;
		
		CrossCorridor(Direction direction, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.CROSS_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(0, 0, 0, 10, 6, 10, pos, direction));
			setOrientation(direction);
			
			light = rand.nextFloat() < 0.3F;
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			if(rand.nextBoolean())
			{
				isRightBlocked = !generator.tryGenerateInDirection(orientation.getClockWise(), 1);
				isLeftBlocked = !generator.tryGenerateInDirection(orientation.getCounterClockWise(), 1);
			} else
			{
				isLeftBlocked = !generator.tryGenerateInDirection(orientation.getCounterClockWise(), 1);
				isRightBlocked = !generator.tryGenerateInDirection(orientation.getClockWise(), 1);
			}
			isFrontBlocked = !generator.tryGenerateInDirection(orientation, 2);
		}
		
		public CrossCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.CROSS_CORRIDOR_PIECE.get(), nbt);
			this.isRightBlocked = nbt.getBoolean("bl0");
			this.isFrontBlocked = nbt.getBoolean("bl1");
			this.isLeftBlocked = nbt.getBoolean("bl2");
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isRightBlocked);
			tagCompound.putBoolean("bl1", isFrontBlocked);
			tagCompound.putBoolean("bl2", isLeftBlocked);
			tagCompound.putBoolean("l", light);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().getClockWise().equals(facing))
				isRightBlocked = false;
			else if(getOrientation().equals(facing))
				isFrontBlocked = false;
			else if(getOrientation().getCounterClockWise().equals(facing))
				isLeftBlocked = false;
			return true;
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
			
			if(isRightBlocked)
				generateBox(level, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 4, 1, 9, 5, 3, 9, wallBlock, wallBlock, false);
			if(isLeftBlocked)
				generateBox(level, structureBoundingBoxIn, 9, 1, 4, 9, 3, 5, wallBlock, wallBlock, false);
			
			if(light)
			{
				BlockState lightBlock = blocks.getBlockState("light_block");
				generateBox(level, structureBoundingBoxIn, 4, 5, 4, 5, 5, 5, lightBlock, lightBlock, false);
			}
		}
	}
	
	public static class TurnCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isBackBlocked, isRightBlocked;
		private final boolean light;
		private boolean direction;
		
		static TurnCorridor create(Direction orientation, BlockPos pos, RandomSource rand)
		{
			boolean direction = rand.nextBoolean();
			return new TurnCorridor(direction, direction ? orientation.getClockWise() : orientation, pos, rand);
		}
		
		private TurnCorridor(boolean direction, Direction orientation, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.TURN_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(0, 0, 0, 7, 5, 7, pos, orientation));
			
			setOrientation(orientation);
			this.direction = direction;
			
			light = rand.nextFloat() < 0.2F;
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			
			Direction newFacing = direction ? orientation.getOpposite() : orientation.getClockWise();
			boolean isBlocked = !generator.tryGenerateInDirection(newFacing, 1);
			if(direction)
				isBackBlocked = isBlocked;
			else
				isRightBlocked = isBlocked;
		}
		
		public TurnCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.TURN_CORRIDOR_PIECE.get(), nbt);
			this.isBackBlocked = nbt.getBoolean("bl0");
			this.isRightBlocked = nbt.getBoolean("bl1");
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isBackBlocked);
			tagCompound.putBoolean("bl1", isRightBlocked);
			tagCompound.putBoolean("l", light);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().getClockWise().equals(facing))
				isRightBlocked = false;
			else if(getOrientation().getOpposite().equals(facing))
				isBackBlocked = false;
			else return false;
			return true;
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
			
			if(isBackBlocked)
				generateBox(level, structureBoundingBoxIn, 4, 1, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(isRightBlocked)
				generateBox(level, structureBoundingBoxIn, 0, 1, 4, 0, 3, 5, wallBlock, wallBlock, false);
			
			if(light)
			{
				BlockState torch = blocks.getBlockState("wall_torch");
				placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.WEST), 5, 2, 3, structureBoundingBoxIn);
				placeBlock(level, torch.setValue(WallTorchBlock.FACING, Direction.NORTH), 3, 2, 5, structureBoundingBoxIn);
			}
		}
	}
	
	public static class ReturnRoom extends ImprovedStructurePiece implements ConnectablePiece
	{
		ReturnRoom(Direction orientation, BlockPos pos)
		{
			super(MSStructures.ImpDungeon.RETURN_ROOM_PIECE.get(), 0, makeGridBoundingBox(2, 0, 0, 6, 11, 8, pos, orientation));
			setOrientation(orientation);
		}
		
		public ReturnRoom(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.RETURN_ROOM_PIECE.get(), nbt);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
	
	public static class ReturnRoomAlt extends ImprovedStructurePiece implements ConnectablePiece
	{
		ReturnRoomAlt(Direction coordBaseMode, BlockPos pos)
		{
			super(MSStructures.ImpDungeon.ALT_RETURN_ROOM_PIECE.get(), 0, makeGridBoundingBox(1, -1, 0, 8, 8, 10, pos, coordBaseMode));
			setOrientation(coordBaseMode);
		}
		
		public ReturnRoomAlt(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.ALT_RETURN_ROOM_PIECE.get(), nbt);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
	
	public static class SpawnerRoom extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean spawner1, spawner2;
		
		SpawnerRoom(Direction orientation, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.SPAWNER_ROOM_PIECE.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 7, pos, orientation));
			setOrientation(orientation);
			
			if(rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = rand.nextBoolean();
				spawner2 = !spawner1;
			}
		}
		
		public SpawnerRoom(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.SPAWNER_ROOM_PIECE.get(), nbt);
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(6, 2), this.getWorldY(1), this.getWorldZ(6, 2));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			
			BlockPos chestPos = new BlockPos(this.getWorldX(3, 5), this.getWorldY(1), this.getWorldZ(3, 5));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getWorldX(4, 5), this.getWorldY(1), this.getWorldZ(4, 5));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
		}
	}
	
	public static class BookcaseRoom extends ImprovedStructurePiece implements ConnectablePiece
	{
		float bookChance;
		boolean light;
		
		BookcaseRoom(Direction direction, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.BOOKCASE_ROOM_PIECE.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 8, pos, direction));
			setOrientation(direction);
			
			light = rand.nextFloat() < 0.4F;
			bookChance = rand.nextBoolean() ? 0 : 0.8F;
		}
		
		public BookcaseRoom(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.BOOKCASE_ROOM_PIECE.get(), nbt);
			bookChance = nbt.getFloat("b");
			light = nbt.getBoolean("l");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putFloat("b", bookChance);
			tagCompound.putBoolean("l", light);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			return getOrientation().getOpposite().equals(facing);
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
	
	public static class SpawnerCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked, isBackBlocked;
		private boolean spawner1, spawner2;
		private final boolean chestPos;
		
		SpawnerCorridor(Direction orientation, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.SPAWNER_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(2, 0, 0, 6, 5, 10, pos, orientation));
			
			setOrientation(orientation);
			
			if(rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = rand.nextBoolean();
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			
			boolean mirror = rand.nextBoolean();
			if(mirror)
				setOrientation(orientation.getOpposite());
			
			boolean isBlocked = !generator.tryGenerateInDirection(orientation, 1);
			if(mirror)
				isBackBlocked = isBlocked;
			else
				isFrontBlocked = isBlocked;
		}
		
		public SpawnerCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.SPAWNER_CORRIDOR_PIECE.get(), nbt);
			this.isBackBlocked = nbt.getBoolean("bl0");
			this.isFrontBlocked = nbt.getBoolean("bl1");
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isBackBlocked);
			tagCompound.putBoolean("bl1", isFrontBlocked);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().getAxis().equals(facing.getAxis()))
			{
				if(getOrientation().equals(facing))
					isFrontBlocked = false;
				else
					isBackBlocked = false;
				return true;
			}
			return false;
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(1, 5), this.getWorldY(1), this.getWorldZ(1, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			
			int z = chestPos ? 4 : 5;
			BlockPos chestPos = new BlockPos(this.getWorldX(4, z), this.getWorldY(1), this.getWorldZ(4, z));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getClockWise(), MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(isBackBlocked)
				generateBox(level, structureBoundingBoxIn, 2, 0, 0, 3, 3, 0, wallBlock, wallBlock, false);
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 2, 0, 9, 3, 3, 9, wallBlock, wallBlock, false);
		}
	}
	
	public static class OgreCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked;
		private final boolean chestPos;
		private boolean ogreSpawned;
		
		OgreCorridor(Direction orientation, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.OGRE_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(1, 0, 0, 8, 5, 10, pos, orientation));
			setOrientation(orientation);
			
			chestPos = rand.nextBoolean();
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			isFrontBlocked = !generator.tryGenerateInDirection(orientation, 1);
		}
		
		public OgreCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.OGRE_CORRIDOR_PIECE.get(), nbt);
			isFrontBlocked = nbt.getBoolean("bl0");
			chestPos = nbt.getBoolean("ch");
			ogreSpawned = nbt.getBoolean("spwn");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isFrontBlocked);
			tagCompound.putBoolean("ch", chestPos);
			tagCompound.putBoolean("spwn", ogreSpawned);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().equals(facing.getOpposite()))
			{
				isFrontBlocked = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
			
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 3, 0, 9, 4, 3, 9, wallBlock, wallBlock, false);
		}
		
		@SuppressWarnings("SameParameterValue")
		private void spawnOgre(int xPos, int yPos, int zPos, WorldGenLevel level, RandomSource rand)
		{
			BlockPos pos = new BlockPos(getWorldX(xPos, zPos), getWorldY(yPos), getWorldZ(xPos, zPos));
			OgreEntity ogre = MSEntityTypes.OGRE.get().create(level.getLevel());
			ogre.moveTo(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat() * 360F, 0);
			ogre.finalizeSpawn(level, null, MobSpawnType.STRUCTURE, null, null);
			ogre.restrictTo(pos, 2);
			level.addFreshEntity(ogre);
		}
	}
	
	public static class LargeSpawnerCorridor extends ImprovedStructurePiece implements ConnectablePiece
	{
		private boolean isFrontBlocked, isBackBlocked;
		private boolean spawner1, spawner2;
		private final boolean chestPos;
		
		LargeSpawnerCorridor(Direction orientation, BlockPos pos, RandomSource rand)
		{
			super(MSStructures.ImpDungeon.LARGE_SPAWNER_CORRIDOR_PIECE.get(), 0, makeGridBoundingBox(0, 0, 0, 10, 5, 10, pos, orientation));
			
			setOrientation(orientation);
			
			if(rand.nextBoolean())
			{
				spawner1 = true;
				spawner2 = true;
			} else
			{
				spawner1 = rand.nextBoolean();
				spawner2 = !spawner1;
			}
			chestPos = rand.nextBoolean();
		}
		
		@Override
		public void generateAdjacentPieces(LocalPieceGenerator generator, RandomSource rand)
		{
			Direction orientation = Objects.requireNonNull(this.getOrientation());
			
			boolean mirror = rand.nextBoolean();
			if(mirror)
				setOrientation(orientation.getOpposite());
			
			boolean isBlocked = !generator.tryGenerateInDirection(orientation, 1);
			if(mirror)
				isBackBlocked = isBlocked;
			else
				isFrontBlocked = isBlocked;
		}
		
		public LargeSpawnerCorridor(CompoundTag nbt)
		{
			super(MSStructures.ImpDungeon.LARGE_SPAWNER_CORRIDOR_PIECE.get(), nbt);
			this.isBackBlocked = nbt.getBoolean("bl0");
			this.isFrontBlocked = nbt.getBoolean("bl1");
			spawner1 = nbt.getBoolean("sp1");
			spawner2 = nbt.getBoolean("sp2");
			chestPos = nbt.getBoolean("ch");
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tagCompound)
		{
			tagCompound.putBoolean("bl0", isBackBlocked);
			tagCompound.putBoolean("bl1", isFrontBlocked);
			tagCompound.putBoolean("sp1", spawner1);
			tagCompound.putBoolean("sp2", spawner2);
			tagCompound.putBoolean("ch", chestPos);
		}
		
		@Override
		public boolean connectFrom(Direction facing)
		{
			if(getOrientation().getAxis().equals(facing.getAxis()))
			{
				if(getOrientation().equals(facing))
					isFrontBlocked = false;
				else
					isBackBlocked = false;
				return true;
			}
			return false;
		}
		
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGeneratorIn, RandomSource randomIn, BoundingBox structureBoundingBoxIn, ChunkPos chunkPosIn, BlockPos pos)
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
				spawner1 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			if(spawner2)
			{
				BlockPos spawnerPos = new BlockPos(this.getWorldX(8, 5), this.getWorldY(1), this.getWorldZ(8, 5));
				spawner2 = !StructureBlockUtil.placeSpawner(spawnerPos, level, structureBoundingBoxIn, getTypeForSpawners(), randomIn);
			}
			
			BlockPos chestPos = new BlockPos(this.getWorldX(4, 4), this.getWorldY(1), this.getWorldZ(4, 4));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.LEFT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			chestPos = new BlockPos(this.getWorldX(5, 4), this.getWorldY(1), this.getWorldZ(5, 4));
			StructureBlockUtil.placeLootChest(chestPos, level, structureBoundingBoxIn, getOrientation().getOpposite(), ChestType.RIGHT, MSLootTables.BASIC_MEDIUM_CHEST, randomIn);
			
			if(isBackBlocked)
				generateBox(level, structureBoundingBoxIn, 4, 0, 0, 5, 3, 0, wallBlock, wallBlock, false);
			if(isFrontBlocked)
				generateBox(level, structureBoundingBoxIn, 4, 0, 9, 5, 3, 9, wallBlock, wallBlock, false);
		}
	}
	
	private static EntityType<?> getTypeForSpawners()
	{
		return MinestuckConfig.SERVER.hardMode.get() ? MSEntityTypes.LICH.get() : MSEntityTypes.IMP.get();
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
			case SOUTH ->
					new BoundingBox(startX + minX, y + minY, startZ + minZ, startX + maxX, y + maxY, startZ + maxZ);
			case NORTH -> new BoundingBox(endX - maxX, y + minY, endZ - maxZ, endX - minX, y + maxY, endZ - minZ);
			case EAST -> new BoundingBox(startX + minZ, y + minY, endZ - maxX, startX + maxZ, y + maxY, endZ - minX);
			case WEST -> new BoundingBox(endX - maxZ, y + minY, startZ + minX, endX - minZ, y + maxY, startZ + maxX);
			default -> throw new IllegalArgumentException("Invalid orientation");
		};
	}
}
