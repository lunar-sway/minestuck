package com.mraof.minestuck.world.gen.structure;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
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
		
		public Builder(Dimensions dimensions, Collection<PieceEntry> entries, ConnectionTester connectionTester)
		{
			this.dimensions = dimensions;
			this.connectionTester = connectionTester;
			
			for(int xIndex = 0; xIndex < this.dimensions.widthInPieces(); xIndex++)
			{
				for(int zIndex = 0; zIndex < this.dimensions.widthInPieces(); zIndex++)
				{
					for(int yIndex = 0; yIndex < this.dimensions.heightInPieces(); yIndex++)
					{
						PiecePos pos = new PiecePos(xIndex, yIndex, zIndex);
						this.availablePiecesMap.put(pos, new ArrayList<>(entries));
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
					this.removeConflictsFromConnection(topPos, Direction.UP, Set.of(ProspitStructure.ConnectionType.AIR));
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
					
					this.removeConflictsFromConnection(edgePos, direction, Set.of(ProspitStructure.ConnectionType.AIR));
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
					Set<ProspitStructure.ConnectionType> connections = this.availablePiecesMap.get(pos).stream().map(entry -> entry.connections().get(direction)).collect(Collectors.toSet());
					this.removeConflictsFromConnection(adjacentPos, direction.getOpposite(), connections);
				});
			}
		}
		
		private void removeConflictsFromConnection(PiecePos pos, Direction direction, Set<ProspitStructure.ConnectionType> connections)
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
		boolean canConnect(ProspitStructure.ConnectionType connection, Set<ProspitStructure.ConnectionType> connections);
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
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor, Map<Direction, ProspitStructure.ConnectionType> connections, Weight weight) implements WeightedEntry
	{
		@Override
		public Weight getWeight()
		{
			return weight;
		}
	}
}
