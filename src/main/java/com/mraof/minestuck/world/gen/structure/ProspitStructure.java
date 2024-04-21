package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

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
		
		private void generatePieces(StructurePiecesBuilder builder, GenerationContext context)
		{
			List<PieceEntry> pieces = List.of(
					new PieceEntry(pos -> null, symmetric(ConnectionType.AIR, ConnectionType.AIR, ConnectionType.AIR), Weight.of(10)),
					new PieceEntry(SolidPiece::new, symmetric(ConnectionType.SOLID, ConnectionType.SOLID, ConnectionType.WALL), Weight.of(10)),
					new PieceEntry(PyramidPiece::new, symmetric(ConnectionType.SOLID, ConnectionType.AIR, ConnectionType.AIR), Weight.of(2)),
					new PieceEntry(pos -> new BridgePiece(pos, Direction.Axis.X), Map.of(
							Direction.DOWN, ConnectionType.AIR,
							Direction.UP, ConnectionType.AIR,
							Direction.NORTH, ConnectionType.AIR,
							Direction.EAST, ConnectionType.BRIDGE,
							Direction.SOUTH, ConnectionType.AIR,
							Direction.WEST, ConnectionType.BRIDGE
					), Weight.of(4)),
					new PieceEntry(pos -> new BridgePiece(pos, Direction.Axis.Z), Map.of(
							Direction.DOWN, ConnectionType.AIR,
							Direction.UP, ConnectionType.AIR,
							Direction.NORTH, ConnectionType.BRIDGE,
							Direction.EAST, ConnectionType.AIR,
							Direction.SOUTH, ConnectionType.BRIDGE,
							Direction.WEST, ConnectionType.AIR
					), Weight.of(4))
			);
			
			BlockPos cornerPos = context.chunkPos().getWorldPosition().offset(-(WIDTH_IN_CHUNKS * 8), 0, -(WIDTH_IN_CHUNKS * 8));
			
			Map<PiecePos, List<PieceEntry>> availablePiecesMap = new HashMap<>();
			for(int xIndex = 0; xIndex < WIDTH_IN_PIECES; xIndex++)
			{
				for(int zIndex = 0; zIndex < WIDTH_IN_PIECES; zIndex++)
				{
					for(int yIndex = 0; yIndex < HEIGHT_IN_PIECES; yIndex++)
					{
						PiecePos pos = new PiecePos(xIndex, yIndex, zIndex);
						availablePiecesMap.put(pos, new ArrayList<>(pieces));
					}
				}
			}
			Set<PiecePos> piecesToGenerate = new HashSet<>(availablePiecesMap.keySet());
			
			for(int xIndex = 0; xIndex < WIDTH_IN_PIECES; xIndex++)
			{
				for(int zIndex = 0; zIndex < WIDTH_IN_PIECES; zIndex++)
				{
					PiecePos topPos = new PiecePos(xIndex, HEIGHT_IN_PIECES - 1, zIndex);
					removeConflictsFromConnection(topPos, Direction.UP, Set.of(ConnectionType.AIR), availablePiecesMap);
				}
			}
			
			while(!piecesToGenerate.isEmpty())
			{
				MinValueSearchResult<PiecePos> leastEntropyResult = MinValueSearchResult.search(piecesToGenerate,
						pos -> entropy(availablePiecesMap.get(pos)));
				
				if(leastEntropyResult.entries.isEmpty())
					break;
				
				PiecePos pos = Util.getRandom(leastEntropyResult.entries, context.random());
				piecesToGenerate.remove(pos);
				
				List<PieceEntry> availablePieces = availablePiecesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(context.random(), availablePieces);
				if(chosenEntry.isEmpty())
					continue;
				
				BlockPos piecePos = pos.toBlockPos(cornerPos);
				StructurePiece piece = chosenEntry.get().constructor().apply(piecePos);
				if(piece != null)
					builder.addPiece(piece);
				
				if(availablePieces.removeIf(entry -> entry != chosenEntry.get()))
					removeConflictingPieces(pos, availablePiecesMap, null);
			}
		}
	}
	
	private static final class MinValueSearchResult<T>
	{
		private final List<T> entries = new ArrayList<>();
		private final double value;
		
		public MinValueSearchResult(T entry, double value)
		{
			this.value = value;
			entries.add(entry);
		}
		
		public MinValueSearchResult()
		{
			this.value = Double.MAX_VALUE;
		}
		
		public static <T> MinValueSearchResult<T> search(Collection<T> collection, ToDoubleFunction<T> valueExtraction)
		{
			return collection.stream().map(entry -> new MinValueSearchResult<>(entry, valueExtraction.applyAsDouble(entry)))
					.reduce(new MinValueSearchResult<>(), MinValueSearchResult::combine);
		}
		
		public MinValueSearchResult<T> combine(MinValueSearchResult<T> other)
		{
			if(this.value < other.value)
				return this;
			if(other.value < this.value)
				return other;
			
			this.entries.addAll(other.entries);
			return this;
		}
	}
	
	private static double entropy(List<PieceEntry> entries)
	{
		int totalWeight = WeightedRandom.getTotalWeight(entries);
		double entropy = 0;
		for(PieceEntry entry : entries)
		{
			if(entry.weight.asInt() == 0)
				continue;
			
			double probability = (double) entry.weight.asInt() / totalWeight;
			entropy += - probability * Math.log(probability);
		}
		return entropy;
	}
	
	private static void removeConflictingPieces(PiecePos pos, Map<PiecePos, List<PieceEntry>> availablePiecesMap, @Nullable Direction excluding)
	{
		for(Direction direction : Direction.values())
		{
			if(direction == excluding)
				continue;
			
			pos.tryOffset(direction).ifPresent(adjacentPos ->
			{
				Set<ConnectionType> connections = availablePiecesMap.get(pos).stream().map(entry -> entry.connections.get(direction)).collect(Collectors.toSet());
				removeConflictsFromConnection(adjacentPos, direction.getOpposite(), connections, availablePiecesMap);
			});
		}
	}
	
	private static void removeConflictsFromConnection(PiecePos pos, Direction direction, Set<ConnectionType> connections, Map<PiecePos, List<PieceEntry>> availablePiecesMap)
	{
		if(availablePiecesMap.get(pos).removeIf(entry -> cannotConnect(entry.connections.get(direction), connections)))
			removeConflictingPieces(pos, availablePiecesMap, direction);
	}
	
	private static boolean cannotConnect(ConnectionType type, Set<ConnectionType> otherTypes)
	{
		Set<ConnectionType> supportedTypes = SUPPORTED_CONNECTIONS.get(type);
		return otherTypes.stream().noneMatch(supportedTypes::contains);
	}
	
	private record PiecePos(int x, int y, int z)
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
	
	private record PieceEntry(Function<BlockPos, StructurePiece> constructor, Map<Direction, ConnectionType> connections, Weight weight) implements WeightedEntry
	{
		@Override
		public Weight getWeight()
		{
			return weight;
		}
	}
	
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
	
	private enum ConnectionType
	{
		AIR,
		SOLID,
		WALL,
		BRIDGE,
	}
	
	private static final Map<ConnectionType, Set<ConnectionType>> SUPPORTED_CONNECTIONS = Util.make(() -> {
		List<Pair<ConnectionType, ConnectionType>> allowedConnections = List.of(
				Pair.of(ConnectionType.AIR, ConnectionType.AIR),
				Pair.of(ConnectionType.SOLID, ConnectionType.SOLID),
				Pair.of(ConnectionType.AIR, ConnectionType.WALL),
				Pair.of(ConnectionType.WALL, ConnectionType.WALL),
				Pair.of(ConnectionType.BRIDGE, ConnectionType.BRIDGE),
				Pair.of(ConnectionType.BRIDGE, ConnectionType.WALL)
		);
		
		ImmutableMap.Builder<ConnectionType, Set<ConnectionType>> mapBuilder = ImmutableMap.builder();
		for(ConnectionType type : ConnectionType.values())
		{
			ImmutableSet.Builder<ConnectionType> setBuilder = ImmutableSet.builder();
			for(Pair<ConnectionType, ConnectionType> pair : allowedConnections)
			{
				if(pair.getFirst() == type)
					setBuilder.add(pair.getSecond());
				if(pair.getSecond() == type)
					setBuilder.add(pair.getFirst());
			}
			mapBuilder.put(type, setBuilder.build());
		}
		return mapBuilder.build();
	});
	
	public static final Supplier<StructurePieceType.ContextlessType> SOLID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_solid",
			() -> SolidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> PYRAMID_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_pyramid",
			() -> PyramidPiece::new);
	public static final Supplier<StructurePieceType.ContextlessType> BRIDGE_PIECE_TYPE = MSStructurePieces.REGISTER.register("prospit_bridge",
			() -> BridgePiece::new);
	
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
}
