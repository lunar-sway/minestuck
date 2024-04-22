package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.gen.structure.MSStructureTypes.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final int PIECE_SIZE = 8;
	public static final WFC.Dimensions WFC_DIMENSIONS = new WFC.Dimensions(16, 16);
	public static final int WIDTH_IN_CHUNKS = (PIECE_SIZE * WFC_DIMENSIONS.widthInPieces()) / 16;
	
	public static void init()
	{
	}
	
	public static final ResourceKey<Structure> STRUCTURE = ResourceKey.create(Registries.STRUCTURE, Minestuck.id("prospit_terrain"));
	
	public static final Supplier<StructurePlacementType<FixedPlacement>> FIXED_PLACEMENT_TYPE = MSStructurePlacements.REGISTER.register("fixed",
			() -> () -> FixedPlacement.CODEC);
	
	public static final class FixedPlacement extends StructurePlacement
	{
		public static final Codec<FixedPlacement> CODEC = Codec.unit(FixedPlacement::new);
		
		public FixedPlacement()
		{
			super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 0, Optional.empty());
		}
		
		@Override
		public StructurePlacementType<FixedPlacement> type()
		{
			return FIXED_PLACEMENT_TYPE.get();
		}
		
		@Override
		protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z)
		{
			return Math.floorMod(x, WIDTH_IN_CHUNKS) == 0 && Math.floorMod(z, WIDTH_IN_CHUNKS) == 0;
		}
	}
	
	public static final Supplier<StructureType<TerrainStructure>> STRUCTURE_TYPE = MSStructureTypes.REGISTER.register("prospit_terrain",
			() -> asType(TerrainStructure.CODEC));
	
	public static final class TerrainStructure extends Structure
	{
		public static final Codec<TerrainStructure> CODEC = simpleCodec(TerrainStructure::new);
		
		public TerrainStructure(StructureSettings settings)
		{
			super(settings);
		}
		
		@Override
		public StructureType<TerrainStructure> type()
		{
			return STRUCTURE_TYPE.get();
		}
		
		@Override
		protected Optional<GenerationStub> findGenerationPoint(GenerationContext context)
		{
			return Optional.of(new GenerationStub(context.chunkPos().getWorldPosition(), builder -> generatePieces(builder, context)));
		}
		
		private void generatePieces(StructurePiecesBuilder piecesBuilder, GenerationContext context)
		{
			BlockPos cornerPos = context.chunkPos().getWorldPosition().offset(-(WIDTH_IN_CHUNKS * 8), 0, -(WIDTH_IN_CHUNKS * 8));
			
			WFC.Builder builder = new WFC.Builder(ProspitStructure.WFC_DIMENSIONS, ProspitStructure.ENTRIES_DATA);
			
			builder.setupTopBounds();
			for(Direction direction : Direction.Plane.HORIZONTAL)
				builder.setupSideBounds(direction);
			
			builder.collapse(context.random(), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(cornerPos, PIECE_SIZE, PIECE_SIZE));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
		}
	}
	
	private static final WFC.EntriesData ENTRIES_DATA = Util.make(() -> {
		ImmutableList.Builder<WFC.PieceEntry> builder = ImmutableList.builder();
		WFC.ConnectionsBuilder connectionsBuilder = WFC.ConnectorType.getBuilderWithCoreConnections();
		
		builder.add(new WFC.PieceEntry(pos -> null, symmetric(WFC.ConnectorType.AIR, WFC.ConnectorType.AIR, WFC.ConnectorType.AIR), Weight.of(10)));
		builder.add(new WFC.PieceEntry(SolidPiece::new, symmetric(WFC.ConnectorType.SOLID, WFC.ConnectorType.SOLID, WFC.ConnectorType.WALL), Weight.of(10)));
		builder.add(new WFC.PieceEntry(PyramidPiece::new, symmetric(WFC.ConnectorType.SOLID, WFC.ConnectorType.AIR, WFC.ConnectorType.ROOF_SIDE), Weight.of(2)));
		addAxisSymmetric(builder::add, BridgePiece::new, WFC.ConnectorType.AIR, WFC.ConnectorType.AIR, WFC.ConnectorType.BRIDGE, WFC.ConnectorType.AIR, Weight.of(4));
		addRotating(builder::add, LedgePiece::new, Map.of(
				Direction.DOWN, WFC.ConnectorType.SOLID,
				Direction.UP, WFC.ConnectorType.AIR,
				Direction.NORTH, WFC.ConnectorType.LEDGE_FRONT,
				Direction.EAST, WFC.ConnectorType.LEDGE_RIGHT,
				Direction.SOUTH, WFC.ConnectorType.LEDGE_BACK,
				Direction.WEST, WFC.ConnectorType.LEDGE_LEFT
		), Weight.of(3));
		addRotating(builder::add, LedgeCornerPiece::new, Map.of(
				Direction.DOWN, WFC.ConnectorType.SOLID,
				Direction.UP, WFC.ConnectorType.AIR,
				Direction.NORTH, WFC.ConnectorType.LEDGE_FRONT,
				Direction.EAST, WFC.ConnectorType.LEDGE_RIGHT,
				Direction.SOUTH, WFC.ConnectorType.LEDGE_LEFT,
				Direction.WEST, WFC.ConnectorType.LEDGE_FRONT
		), Weight.of(3));
		
		return new WFC.EntriesData(builder.build(), connectionsBuilder.build());
	});
	
	private static Map<Direction, WFC.ConnectorType> symmetric(WFC.ConnectorType down, WFC.ConnectorType up, WFC.ConnectorType side)
	{
		return Map.of(
				Direction.DOWN, down,
				Direction.UP, up,
				Direction.NORTH, side,
				Direction.EAST, side,
				Direction.SOUTH, side,
				Direction.WEST, side
		);
	}
	
	private static void addAxisSymmetric(Consumer<WFC.PieceEntry> consumer, BiFunction<BlockPos, Direction.Axis, StructurePiece> constructor,
										 WFC.ConnectorType down, WFC.ConnectorType up, WFC.ConnectorType front, WFC.ConnectorType side, Weight individualWeight)
	{
		Map<Direction, WFC.ConnectorType> connections = Map.of(
				Direction.DOWN, down,
				Direction.UP, up,
				Direction.NORTH, side,
				Direction.EAST, front,
				Direction.SOUTH, side,
				Direction.WEST, front
		);
		consumer.accept(new WFC.PieceEntry(pos -> constructor.apply(pos, Direction.Axis.X), connections, individualWeight));
		consumer.accept(new WFC.PieceEntry(pos -> constructor.apply(pos, Direction.Axis.Z), rotateConnections(connections, Rotation.CLOCKWISE_90), individualWeight));
	}
	
	private static void addRotating(Consumer<WFC.PieceEntry> consumer, BiFunction<BlockPos, Rotation, StructurePiece> constructor,
									Map<Direction, WFC.ConnectorType> connections, Weight individualWeight)
	{
		for(Rotation rotation : Rotation.values())
			consumer.accept(new WFC.PieceEntry(pos -> constructor.apply(pos, rotation), rotateConnections(connections, rotation), individualWeight));
	}
	
	private static Map<Direction, WFC.ConnectorType> rotateConnections(Map<Direction, WFC.ConnectorType> connections, Rotation rotation)
	{
		if(rotation == Rotation.NONE)
			return connections;
		
		return Map.of(
				Direction.DOWN, connections.get(Direction.DOWN),
				Direction.UP, connections.get(Direction.UP),
				rotation.rotate(Direction.NORTH), connections.get(Direction.NORTH),
				rotation.rotate(Direction.EAST), connections.get(Direction.EAST),
				rotation.rotate(Direction.SOUTH), connections.get(Direction.SOUTH),
				rotation.rotate(Direction.WEST), connections.get(Direction.WEST)
		);
	}
	
	
	public static final Supplier<StructurePieceType.ContextlessType> SOLID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_solid",
			() -> SolidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> PYRAMID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_pyramid",
			() -> PyramidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> BRIDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_bridge",
			() -> BridgePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge",
			() -> LedgePiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> LEDGE_CORNER_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_ledge_corner",
			() -> LedgeCornerPiece::new);
	
	public static final class SolidPiece extends ImprovedStructurePiece
	{
		public SolidPiece(BlockPos bottomCornerPos)
		{
			super(SOLID_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.SOUTH);
		}
		
		public SolidPiece(CompoundTag tag)
		{
			super(SOLID_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class PyramidPiece extends ImprovedStructurePiece
	{
		public PyramidPiece(BlockPos bottomCornerPos)
		{
			super(PYRAMID_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(Direction.SOUTH);
		}
		
		public PyramidPiece(CompoundTag tag)
		{
			super(PYRAMID_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 1, 1, 1, 6, 1, 6,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 2, 2, 2, 5, 2, 5,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 3, 3, 3, 4, 3, 4,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class BridgePiece extends ImprovedStructurePiece
	{
		public BridgePiece(BlockPos bottomCornerPos, Direction.Axis axis)
		{
			super(BRIDGE_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH);
		}
		
		public BridgePiece(CompoundTag tag)
		{
			super(BRIDGE_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 2, 1, 0, 5, 1, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 3, 0, 0, 4, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 2, 2, 0, 2, 2, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			generateBox(level, box, 5, 2, 0, 5, 2, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
		}
	}
	
	public static final class LedgePiece extends ImprovedStructurePiece
	{
		public LedgePiece(BlockPos bottomCornerPos, Rotation rotation)
		{
			super(LEDGE_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(rotation.rotate(Direction.SOUTH));
		}
		
		public LedgePiece(CompoundTag tag)
		{
			super(LEDGE_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 3, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 4, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 7, 1, 0, box);
		}
	}
	
	public static final class LedgeCornerPiece extends ImprovedStructurePiece
	{
		public LedgeCornerPiece(BlockPos bottomCornerPos, Rotation rotation)
		{
			super(LEDGE_CORNER_PIECE_TYPE.get(), 0, BoundingBox.fromCorners(bottomCornerPos,
					bottomCornerPos.offset(PIECE_SIZE - 1, PIECE_SIZE - 1, PIECE_SIZE - 1)));
			setOrientation(rotation.rotate(Direction.SOUTH));
		}
		
		public LedgeCornerPiece(CompoundTag tag)
		{
			super(LEDGE_CORNER_PIECE_TYPE.get(), tag);
		}
		
		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag)
		{
		}
		
		@Override
		public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos)
		{
			generateBox(level, box, 0, 0, 0, 7, 0, 7,
					Blocks.GOLD_BLOCK.defaultBlockState(), Blocks.GOLD_BLOCK.defaultBlockState(), false);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 7, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 4, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 3, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 0, 2, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 3, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 4, 1, 0, box);
			placeBlock(level, Blocks.GOLD_BLOCK.defaultBlockState(), 7, 1, 0, box);
		}
	}
}
