package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
import net.minecraft.util.random.WeightedEntry;
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
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.mraof.minestuck.world.gen.structure.MSStructureTypes.asType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ProspitStructure
{
	public static final int PIECE_SIZE = 8;
	public static final int WIDTH_IN_PIECES = 16, HEIGHT_IN_PIECES = 16;
	public static final int WIDTH_IN_CHUNKS = (PIECE_SIZE * WIDTH_IN_PIECES) / 16;
	
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
			
			WaveFunctionCollapseBuilder builder = new WaveFunctionCollapseBuilder(ProspitStructure.PIECE_ENTRIES);
			
			builder.setupTopBounds();
			for(Direction direction : Direction.Plane.HORIZONTAL)
				builder.setupSideBounds(direction);
			
			builder.collapse(context.random(), (piecePos, pieceConstructor) -> {
				StructurePiece piece = pieceConstructor.apply(piecePos.toBlockPos(cornerPos));
				if(piece != null)
					piecesBuilder.addPiece(piece);
			});
		}
	}
	
	public record PiecePos(int x, int y, int z)
	{
		public BlockPos toBlockPos(BlockPos cornerPos)
		{
			return cornerPos.offset(this.x * PIECE_SIZE, this.y * PIECE_SIZE, this.z * PIECE_SIZE);
		}
		
		public Optional<PiecePos> tryOffset(Direction direction)
		{
			int newX = this.x + direction.getStepX();
			int newY = this.y + direction.getStepY();
			int newZ = this.z + direction.getStepZ();
			
			if(newX < 0 || WIDTH_IN_PIECES <= newX
					|| newY < 0 || HEIGHT_IN_PIECES <= newY
					|| newZ < 0 || WIDTH_IN_PIECES <= newZ)
				return Optional.empty();
			
			return Optional.of(new PiecePos(newX, newY, newZ));
		}
	}
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor, Map<Direction, ConnectionType> connections, Weight weight) implements WeightedEntry
	{
		@Override
		public Weight getWeight()
		{
			return weight;
		}
	}
	
	private static final Collection<PieceEntry> PIECE_ENTRIES = Util.make(() -> {
		ImmutableList.Builder<PieceEntry> builder = ImmutableList.builder();
		
		builder.add(new PieceEntry(pos -> null, symmetric(ConnectionType.AIR, ConnectionType.AIR, ConnectionType.AIR), Weight.of(10)));
		builder.add(new PieceEntry(SolidPiece::new, symmetric(ConnectionType.SOLID, ConnectionType.SOLID, ConnectionType.WALL), Weight.of(10)));
		builder.add(new PieceEntry(PyramidPiece::new, symmetric(ConnectionType.SOLID, ConnectionType.AIR, ConnectionType.ROOF_SIDE), Weight.of(2)));
		addAxisSymmetric(builder::add, BridgePiece::new, ConnectionType.AIR, ConnectionType.AIR, ConnectionType.BRIDGE, ConnectionType.AIR, Weight.of(4));
		addRotating(builder::add, LedgePiece::new, Map.of(
				Direction.DOWN, ConnectionType.SOLID,
				Direction.UP, ConnectionType.AIR,
				Direction.NORTH, ConnectionType.LEDGE_FRONT,
				Direction.EAST, ConnectionType.LEDGE_RIGHT,
				Direction.SOUTH, ConnectionType.LEDGE_BACK,
				Direction.WEST, ConnectionType.LEDGE_LEFT
		), Weight.of(3));
		addRotating(builder::add, LedgeCornerPiece::new, Map.of(
				Direction.DOWN, ConnectionType.SOLID,
				Direction.UP, ConnectionType.AIR,
				Direction.NORTH, ConnectionType.LEDGE_FRONT,
				Direction.EAST, ConnectionType.LEDGE_RIGHT,
				Direction.SOUTH, ConnectionType.LEDGE_LEFT,
				Direction.WEST, ConnectionType.LEDGE_FRONT
		), Weight.of(3));
		
		return builder.build();
	});
	
	private static Map<Direction, ConnectionType> symmetric(ConnectionType down, ConnectionType up, ConnectionType side)
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
	
	private static void addAxisSymmetric(Consumer<PieceEntry> consumer, BiFunction<BlockPos, Direction.Axis, StructurePiece> constructor,
										 ConnectionType down, ConnectionType up, ConnectionType front, ConnectionType side, Weight individualWeight)
	{
		Map<Direction, ConnectionType> connections = Map.of(
				Direction.DOWN, down,
				Direction.UP, up,
				Direction.NORTH, side,
				Direction.EAST, front,
				Direction.SOUTH, side,
				Direction.WEST, front
		);
		consumer.accept(new PieceEntry(pos -> constructor.apply(pos, Direction.Axis.X), connections, individualWeight));
		consumer.accept(new PieceEntry(pos -> constructor.apply(pos, Direction.Axis.Z), rotateConnections(connections, Rotation.CLOCKWISE_90), individualWeight));
	}
	
	private static void addRotating(Consumer<PieceEntry> consumer, BiFunction<BlockPos, Rotation, StructurePiece> constructor,
										 Map<Direction, ConnectionType> connections, Weight individualWeight)
	{
		for(Rotation rotation : Rotation.values())
			consumer.accept(new PieceEntry(pos -> constructor.apply(pos, rotation), rotateConnections(connections, rotation), individualWeight));
	}
	
	private static Map<Direction, ConnectionType> rotateConnections(Map<Direction, ConnectionType> connections, Rotation rotation)
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
	
	public enum ConnectionType
	{
		AIR,
		SOLID,
		WALL,
		ROOF_SIDE,
		BRIDGE,
		LEDGE_FRONT,
		LEDGE_LEFT,
		LEDGE_RIGHT,
		LEDGE_BACK,
		;
		
		public boolean cannotConnect(Set<ConnectionType> otherTypes)
		{
			Set<ConnectionType> supportedTypes = SUPPORTED_CONNECTIONS.get(this);
			if(supportedTypes == null)
				return true;
			return otherTypes.stream().noneMatch(supportedTypes::contains);
		}
		
		private static final Map<ConnectionType, Set<ConnectionType>> SUPPORTED_CONNECTIONS = Util.make(() -> {
			ConnectionsBuilder builder = new ConnectionsBuilder();
			
			builder.connectSelf(ConnectionType.AIR);
			builder.connectSelf(ConnectionType.SOLID);
			builder.connect(ConnectionType.AIR, ConnectionType.WALL);
			builder.connectSelf(ConnectionType.WALL);
			builder.connect(ConnectionType.ROOF_SIDE, ConnectionType.WALL);
			builder.connect(ConnectionType.ROOF_SIDE, ConnectionType.AIR);
			builder.connectSelf(ConnectionType.BRIDGE);
			builder.connect(ConnectionType.BRIDGE, ConnectionType.WALL);
			builder.connect(ConnectionType.LEDGE_FRONT, ConnectionType.AIR);
			builder.connect(ConnectionType.LEDGE_LEFT, ConnectionType.LEDGE_RIGHT);
			builder.connectSelf(ConnectionType.LEDGE_BACK);
			builder.connect(ConnectionType.LEDGE_LEFT, ConnectionType.WALL);
			builder.connect(ConnectionType.LEDGE_RIGHT, ConnectionType.WALL);
			builder.connect(ConnectionType.LEDGE_BACK, ConnectionType.WALL);
			
			return builder.build();
		});
	}
	
	private static final class ConnectionsBuilder
	{
		private final Map<ConnectionType, ImmutableSet.Builder<ConnectionType>> builderMap = new HashMap<>();
		
		public void connectSelf(ConnectionType type)
		{
			this.connect(type, type);
		}
		
		public void connect(ConnectionType type1, ConnectionType type2)
		{
			builderMap.computeIfAbsent(type1, ignored -> ImmutableSet.builder()).add(type2);
			builderMap.computeIfAbsent(type2, ignored -> ImmutableSet.builder()).add(type1);
		}
		
		Map<ConnectionType, Set<ConnectionType>> build()
		{
			return builderMap.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
		}
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
