package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.AbstractIterator;
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
 * The main component being {@link Generator}.
 */
public final class WFC
{
	public static final class Generator
	{
		private final Dimensions dimensions;
		private final ConnectionTester connectionTester;
		
		private final Map<PiecePos, List<PieceEntry>> availablePiecesMap = new HashMap<>();
		
		public Generator(Dimensions dimensions, EntriesData entriesData)
		{
			this.dimensions = dimensions;
			this.connectionTester = entriesData.connectionTester();
			
			for(PiecePos pos : this.dimensions.iterateAll())
			{
				this.availablePiecesMap.put(pos, new ArrayList<>(entriesData.entries()));
			}
		}
		
		public void setupFixedEdgeBounds(Direction direction, Set<ConnectorType> connections)
		{
			for(PiecePos pos : this.dimensions.iterateEdge(direction))
			{
				this.removeConflictsFromConnection(pos, direction, connections);
			}
		}
		
		public void collapse(WorldgenRandom random, BiConsumer<PiecePos, Function<BlockPos, StructurePiece>> piecePlacer)
		{
			Set<PiecePos> piecesToGenerate = new HashSet<>(availablePiecesMap.keySet());
			
			while(!piecesToGenerate.isEmpty())
			{
				MinValueSearchResult<PiecePos> leastEntropyResult = MinValueSearchResult.search(piecesToGenerate,
						pos -> entropy(this.availablePiecesMap.get(pos)));
				
				if(leastEntropyResult.entries.isEmpty())
					break;
				
				PiecePos pos = Util.getRandom(leastEntropyResult.entries, random);
				piecesToGenerate.remove(pos);
				
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
		public boolean isInBounds(int x, int y, int z)
		{
			return 0 <= x && x < this.widthInPieces()
					&& 0 <= y && y < this.heightInPieces()
					&& 0 <= z && z < this.widthInPieces();
		}
		
		public Iterable<PiecePos> iterateAll()
		{
			return PiecePos.iterateBox(0, 0, 0,
					this.widthInPieces() - 1, this.heightInPieces() - 1, this.widthInPieces() - 1);
		}
		
		public Iterable<PiecePos> iterateEdge(Direction direction)
		{
			int minX = direction != Direction.EAST ? 0 : this.widthInPieces() - 1;
			int minY = direction != Direction.UP ? 0 : this.heightInPieces() - 1;
			int minZ = direction != Direction.SOUTH ? 0 : this.widthInPieces() - 1;
			int maxX = direction != Direction.WEST ? this.widthInPieces() - 1 : 0;
			int maxY = direction != Direction.DOWN ? this.heightInPieces() - 1 : 0;
			int maxZ = direction != Direction.NORTH ? this.widthInPieces() - 1 : 0;
			return PiecePos.iterateBox(minX, minY, minZ, maxX, maxY, maxZ);
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
		
		public static Iterable<PiecePos> iterateBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
		{
			return () -> new AbstractIterator<>()
			{
				@Nullable
				PiecePos pos = null;
				
				@Nullable
				@Override
				protected PiecePos computeNext()
				{
					if(pos == null)
						pos = new PiecePos(minX, minY, minZ);
					else if(pos.y < maxY)
						pos = new PiecePos(pos.x, pos.y + 1, pos.z);
					else if(pos.z < maxZ)
						pos = new PiecePos(pos.x, minY, pos.z + 1);
					else if(pos.x < maxX)
						pos = new PiecePos(pos.x + 1, minY, minZ);
					else
						return this.endOfData();
					
					return pos;
				}
			};
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
		
		public static void addCoreConnections(EntriesBuilder builder)
		{
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
		}
	}
	
	public record EntriesData(Collection<PieceEntry> entries, ConnectionTester connectionTester)
	{
	}
	
	public static final class EntriesBuilder
	{
		private final Map<ConnectorType, ImmutableSet.Builder<ConnectorType>> connections = new HashMap<>();
		private final ImmutableList.Builder<PieceEntry> pieceEntries = ImmutableList.builder();
		
		public void connectSelf(ConnectorType type)
		{
			this.connect(type, type);
		}
		
		public void connect(ConnectorType type1, ConnectorType type2)
		{
			connections.computeIfAbsent(type1, ignored -> ImmutableSet.builder()).add(type2);
			connections.computeIfAbsent(type2, ignored -> ImmutableSet.builder()).add(type1);
		}
		
		
		public void add(Function<BlockPos, StructurePiece> constructor, Map<Direction, ConnectorType> connections, int weight)
		{
			this.pieceEntries.add(new PieceEntry(constructor, connections, Weight.of(weight)));
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
		
		
		public EntriesData build()
		{
			return new EntriesData(this.pieceEntries.build(), this.buildConnectionTester());
		}
		
		private ConnectionTester buildConnectionTester()
		{
			Map<ConnectorType, Set<ConnectorType>> map = connections.entrySet().stream()
					.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
			return (type, otherTypes) -> {
				Set<ConnectorType> supportedTypes = map.get(type);
				if(supportedTypes == null)
					return false;
				return otherTypes.stream().anyMatch(supportedTypes::contains);
			};
		}
	}
}
