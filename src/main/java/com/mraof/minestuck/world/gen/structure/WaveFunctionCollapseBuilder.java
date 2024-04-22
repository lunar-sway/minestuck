package com.mraof.minestuck.world.gen.structure;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

final class WaveFunctionCollapseBuilder
{
	private final ConnectionTester connectionTester;
	
	private final Map<ProspitStructure.PiecePos, List<ProspitStructure.PieceEntry>> availablePiecesMap = new HashMap<>();
	private final Set<ProspitStructure.PiecePos> piecesToGenerate = new HashSet<>();
	
	public WaveFunctionCollapseBuilder(Collection<ProspitStructure.PieceEntry> entries, ConnectionTester connectionTester)
	{
		this.connectionTester = connectionTester;
		for(int xIndex = 0; xIndex < ProspitStructure.WIDTH_IN_PIECES; xIndex++)
		{
			for(int zIndex = 0; zIndex < ProspitStructure.WIDTH_IN_PIECES; zIndex++)
			{
				for(int yIndex = 0; yIndex < ProspitStructure.HEIGHT_IN_PIECES; yIndex++)
				{
					ProspitStructure.PiecePos pos = new ProspitStructure.PiecePos(xIndex, yIndex, zIndex);
					this.availablePiecesMap.put(pos, new ArrayList<>(entries));
				}
			}
		}
		this.piecesToGenerate.addAll(availablePiecesMap.keySet());
	}
	
	public void setupTopBounds()
	{
		for(int xIndex = 0; xIndex < ProspitStructure.WIDTH_IN_PIECES; xIndex++)
		{
			for(int zIndex = 0; zIndex < ProspitStructure.WIDTH_IN_PIECES; zIndex++)
			{
				ProspitStructure.PiecePos topPos = new ProspitStructure.PiecePos(xIndex, ProspitStructure.HEIGHT_IN_PIECES - 1, zIndex);
				this.removeConflictsFromConnection(topPos, Direction.UP, Set.of(ProspitStructure.ConnectionType.AIR));
			}
		}
	}
	
	public void setupSideBounds(Direction direction)
	{
		int edgeIndex = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE
				? ProspitStructure.WIDTH_IN_PIECES - 1 : 0;
		for(int xIndex = 0; xIndex < ProspitStructure.WIDTH_IN_PIECES; xIndex++)
		{
			for(int yIndex = 0; yIndex < ProspitStructure.HEIGHT_IN_PIECES; yIndex++)
			{
				ProspitStructure.PiecePos edgePos = direction.getAxis() == Direction.Axis.X
						? new ProspitStructure.PiecePos(edgeIndex, yIndex, xIndex)
						: new ProspitStructure.PiecePos(xIndex, yIndex, edgeIndex);
				
				this.removeConflictsFromConnection(edgePos, direction, Set.of(ProspitStructure.ConnectionType.AIR));
			}
		}
	}
	
	public void collapse(WorldgenRandom random, BiConsumer<ProspitStructure.PiecePos, Function<BlockPos, StructurePiece>> piecePlacer)
	{
		while(!this.piecesToGenerate.isEmpty())
		{
			MinValueSearchResult<ProspitStructure.PiecePos> leastEntropyResult = MinValueSearchResult.search(this.piecesToGenerate,
					pos -> entropy(this.availablePiecesMap.get(pos)));
			
			if(leastEntropyResult.entries.isEmpty())
				break;
			
			ProspitStructure.PiecePos pos = Util.getRandom(leastEntropyResult.entries, random);
			this.piecesToGenerate.remove(pos);
			
			List<ProspitStructure.PieceEntry> availablePieces = this.availablePiecesMap.get(pos);
			var chosenEntry = WeightedRandom.getRandomItem(random, availablePieces);
			if(chosenEntry.isEmpty())
				continue;
			
			piecePlacer.accept(pos, chosenEntry.get().constructor());
			
			if(availablePieces.removeIf(entry -> entry != chosenEntry.get()))
				this.removeConflictingPieces(pos, null);
		}
	}
	
	private void removeConflictingPieces(ProspitStructure.PiecePos pos, @Nullable Direction excluding)
	{
		for(Direction direction : Direction.values())
		{
			if(direction == excluding)
				continue;
			
			pos.tryOffset(direction).ifPresent(adjacentPos ->
			{
				Set<ProspitStructure.ConnectionType> connections = this.availablePiecesMap.get(pos).stream().map(entry -> entry.connections().get(direction)).collect(Collectors.toSet());
				this.removeConflictsFromConnection(adjacentPos, direction.getOpposite(), connections);
			});
		}
	}
	
	private void removeConflictsFromConnection(ProspitStructure.PiecePos pos, Direction direction, Set<ProspitStructure.ConnectionType> connections)
	{
		if(this.availablePiecesMap.get(pos).removeIf(entry -> !connectionTester.canConnect(entry.connections().get(direction), connections)))
			this.removeConflictingPieces(pos, direction);
	}
	
	private static double entropy(List<ProspitStructure.PieceEntry> entries)
	{
		int totalWeight = WeightedRandom.getTotalWeight(entries);
		double entropy = 0;
		for(ProspitStructure.PieceEntry entry : entries)
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
}
