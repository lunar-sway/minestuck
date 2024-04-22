package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Contains components for an implementation of Wave Function Collapse.
 * The main component being {@link Builder}.
 */
public final class WFC
{
	public static final class Builder
	{
		private final Dimensions dimensions;
		private final ConnectionTester connectionTester;
		
		private final Map<PiecePos, List<PieceEntry>> availablePiecesMap = new HashMap<>();
		private final Set<PiecePos> piecesToGenerate = new HashSet<>();
		
		public Builder(Dimensions dimensions, EntriesData entriesData)
		{
			this.dimensions = dimensions;
			this.connectionTester = entriesData.connectionTester();
			
			for(int xIndex = 0; xIndex < this.dimensions.widthInPieces(); xIndex++)
			{
				for(int zIndex = 0; zIndex < this.dimensions.widthInPieces(); zIndex++)
				{
					for(int yIndex = 0; yIndex < this.dimensions.heightInPieces(); yIndex++)
					{
						PiecePos pos = new PiecePos(xIndex, yIndex, zIndex);
						this.availablePiecesMap.put(pos, new ArrayList<>(entriesData.entries()));
					}
				}
			}
			this.piecesToGenerate.addAll(availablePiecesMap.keySet());
		}
		
		public void setupTopBounds()
		{
			for(int xIndex = 0; xIndex < this.dimensions.widthInPieces(); xIndex++)
			{
				for(int zIndex = 0; zIndex < this.dimensions.widthInPieces(); zIndex++)
				{
					PiecePos topPos = new PiecePos(xIndex, this.dimensions.topEdge(), zIndex);
					this.removeConflictsFromConnection(topPos, Direction.UP, Set.of(ConnectorType.AIR));
				}
			}
		}
		
		public void setupSideBounds(Direction direction)
		{
			int edgeIndex = this.dimensions.horizontalEdge(direction.getAxisDirection());
			for(int xIndex = 0; xIndex < this.dimensions.widthInPieces(); xIndex++)
			{
				for(int yIndex = 0; yIndex < this.dimensions.heightInPieces(); yIndex++)
				{
					PiecePos edgePos = direction.getAxis() == Direction.Axis.X
							? new PiecePos(edgeIndex, yIndex, xIndex)
							: new PiecePos(xIndex, yIndex, edgeIndex);
					
					this.removeConflictsFromConnection(edgePos, direction, Set.of(ConnectorType.AIR));
				}
			}
		}
		
		public void collapse(WorldgenRandom random, BiConsumer<PiecePos, Function<BlockPos, StructurePiece>> piecePlacer)
		{
			while(!this.piecesToGenerate.isEmpty())
			{
				MinValueSearchResult<PiecePos> leastEntropyResult = MinValueSearchResult.search(this.piecesToGenerate,
						pos -> entropy(this.availablePiecesMap.get(pos)));
				
				if(leastEntropyResult.entries.isEmpty())
					break;
				
				PiecePos pos = Util.getRandom(leastEntropyResult.entries, random);
				this.piecesToGenerate.remove(pos);
				
				List<PieceEntry> availablePieces = this.availablePiecesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(random, availablePieces);
				if(chosenEntry.isEmpty())
					continue;
				
				piecePlacer.accept(pos, chosenEntry.get().constructor());
				
				if(availablePieces.removeIf(entry -> entry != chosenEntry.get()))
					this.removeConflictingPieces(pos, null);
			}
		}
		
		private void removeConflictingPieces(PiecePos pos, @Nullable Direction excluding)
		{
			for(Direction direction : Direction.values())
			{
				if(direction == excluding)
					continue;
				
				pos.tryOffset(direction, this.dimensions).ifPresent(adjacentPos ->
				{
					Set<ConnectorType> connections = this.availablePiecesMap.get(pos).stream().map(entry -> entry.connections().get(direction)).collect(Collectors.toSet());
					this.removeConflictsFromConnection(adjacentPos, direction.getOpposite(), connections);
				});
			}
		}
		
		private void removeConflictsFromConnection(PiecePos pos, Direction direction, Set<ConnectorType> connections)
		{
			if(this.availablePiecesMap.get(pos).removeIf(entry -> !connectionTester.canConnect(entry.connections().get(direction), connections)))
				this.removeConflictingPieces(pos, direction);
		}
	}
	
	private static double entropy(List<PieceEntry> entries)
	{
		int totalWeight = WeightedRandom.getTotalWeight(entries);
		double entropy = 0;
		for(PieceEntry entry : entries)
		{
			if(entry.weight().asInt() == 0)
				continue;
			
			double probability = (double) entry.weight().asInt() / totalWeight;
			entropy += -probability * Math.log(probability);
		}
		return entropy;
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
	
	public interface ConnectionTester
	{
		boolean canConnect(ConnectorType connection, Set<ConnectorType> connections);
	}
	
	public record Dimensions(int widthInPieces, int heightInPieces)
	{
		public int horizontalEdge(Direction.AxisDirection direction)
		{
			return direction == Direction.AxisDirection.POSITIVE
					? this.widthInPieces() - 1 : 0;
		}
		
		public int topEdge()
		{
			return this.heightInPieces() - 1;
		}
		
		public boolean isInBounds(int x, int y, int z)
		{
			return 0 <= x && x < this.widthInPieces()
					&& 0 <= y && y < this.heightInPieces()
					&& 0 <= z && z < this.widthInPieces();
		}
	}
	
	public record PiecePos(int x, int y, int z)
	{
		public BlockPos toBlockPos(BlockPos cornerPos, int pieceWidth, int pieceHeight)
		{
			return cornerPos.offset(this.x * pieceWidth, this.y * pieceHeight, this.z * pieceWidth);
		}
		
		public Optional<PiecePos> tryOffset(Direction direction, Dimensions dimensions)
		{
			int newX = this.x + direction.getStepX();
			int newY = this.y + direction.getStepY();
			int newZ = this.z + direction.getStepZ();
			
			if(!dimensions.isInBounds(newX, newY, newZ))
				return Optional.empty();
			
			return Optional.of(new PiecePos(newX, newY, newZ));
		}
	}
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor, Map<Direction, ConnectorType> connections, Weight weight) implements WeightedEntry
	{
		@Override
		public Weight getWeight()
		{
			return weight;
		}
	}
	
	public record ConnectorType(String name)
	{
		public static final ConnectorType AIR = new ConnectorType("air"),
				SOLID = new ConnectorType("solid"),
				WALL = new ConnectorType("wall"),
				ROOF_SIDE = new ConnectorType("roof_side"),
				BRIDGE = new ConnectorType("bridge"),
				LEDGE_FRONT = new ConnectorType("ledge/front"),
				LEDGE_LEFT = new ConnectorType("ledge/left"),
				LEDGE_RIGHT = new ConnectorType("ledge/right"),
				LEDGE_BACK = new ConnectorType("ledge/back");
		
		public static ConnectionsBuilder getBuilderWithCoreConnections()
		{
			ConnectionsBuilder builder = new ConnectionsBuilder();
			
			builder.connectSelf(ConnectorType.AIR);
			builder.connectSelf(ConnectorType.SOLID);
			builder.connect(ConnectorType.AIR, ConnectorType.WALL);
			builder.connectSelf(ConnectorType.WALL);
			builder.connect(ConnectorType.ROOF_SIDE, ConnectorType.WALL);
			builder.connect(ConnectorType.ROOF_SIDE, ConnectorType.AIR);
			builder.connectSelf(ConnectorType.BRIDGE);
			builder.connect(ConnectorType.BRIDGE, ConnectorType.WALL);
			builder.connect(ConnectorType.LEDGE_FRONT, ConnectorType.AIR);
			builder.connect(ConnectorType.LEDGE_LEFT, ConnectorType.LEDGE_RIGHT);
			builder.connectSelf(ConnectorType.LEDGE_BACK);
			builder.connect(ConnectorType.LEDGE_LEFT, ConnectorType.WALL);
			builder.connect(ConnectorType.LEDGE_RIGHT, ConnectorType.WALL);
			builder.connect(ConnectorType.LEDGE_BACK, ConnectorType.WALL);
			
			return builder;
		}
	}
	
	public static final class ConnectionsBuilder
	{
		private final Map<ConnectorType, ImmutableSet.Builder<ConnectorType>> builderMap = new HashMap<>();
		
		public void connectSelf(ConnectorType type)
		{
			this.connect(type, type);
		}
		
		public void connect(ConnectorType type1, ConnectorType type2)
		{
			builderMap.computeIfAbsent(type1, ignored -> ImmutableSet.builder()).add(type2);
			builderMap.computeIfAbsent(type2, ignored -> ImmutableSet.builder()).add(type1);
		}
		
		ConnectionTester build()
		{
			Map<ConnectorType, Set<ConnectorType>> map = builderMap.entrySet().stream()
					.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
			return (type, otherTypes) -> {
				Set<ConnectorType> supportedTypes = map.get(type);
				if(supportedTypes == null)
					return false;
				return otherTypes.stream().anyMatch(supportedTypes::contains);
			};
		}
	}
	
	public record EntriesData(Collection<PieceEntry> entries, ConnectionTester connectionTester)
	{
	}
	
	public static final class EntriesBuilder
	{
		private final ImmutableList.Builder<PieceEntry> builder = ImmutableList.builder();
		
		public void add(Function<BlockPos, StructurePiece> constructor, Map<Direction, ConnectorType> connections, int weight)
		{
			this.builder.add(new PieceEntry(constructor, connections, Weight.of(weight)));
		}
		
		public void addSymmetric(Function<BlockPos, StructurePiece> constructor, ConnectorType down, ConnectorType up, ConnectorType side, int weight)
		{
			this.add(constructor, Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, side,
					Direction.EAST, side,
					Direction.SOUTH, side,
					Direction.WEST, side
			), weight);
		}
		
		public void addAxisSymmetric(BiFunction<BlockPos, Direction.Axis, StructurePiece> constructor,
									 ConnectorType down, ConnectorType up, ConnectorType front, ConnectorType side, int individualWeight)
		{
			Map<Direction, ConnectorType> connections = Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, side,
					Direction.EAST, front,
					Direction.SOUTH, side,
					Direction.WEST, front
			);
			this.add(pos -> constructor.apply(pos, Direction.Axis.X), connections, individualWeight);
			this.add(pos -> constructor.apply(pos, Direction.Axis.Z), rotateConnections(connections, Rotation.CLOCKWISE_90), individualWeight);
		}
		
		public void addRotating(BiFunction<BlockPos, Rotation, StructurePiece> constructor,
										Map<Direction, ConnectorType> connections, int individualWeight)
		{
			for(Rotation rotation : Rotation.values())
				this.add(pos -> constructor.apply(pos, rotation), rotateConnections(connections, rotation), individualWeight);
		}
		
		private static Map<Direction, ConnectorType> rotateConnections(Map<Direction, ConnectorType> connections, Rotation rotation)
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
		
		public Collection<PieceEntry> build()
		{
			return this.builder.build();
		}
	}
}
