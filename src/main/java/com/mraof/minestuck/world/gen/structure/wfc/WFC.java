package com.mraof.minestuck.world.gen.structure.wfc;

import com.google.common.collect.AbstractIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains components for an implementation of Wave Function Collapse.
 * The main component for running WFC is {@link Generator}.
 *
 * @see WFCData
 */
public final class WFC
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final class InfiniteModularGeneration
	{
		public static void generateModule(PositionTransform middleTransform, Dimensions dimensions,
										  WFCData.EntryPalette centerPalette, WFCData.EntryPalette borderPalette,
										  PositionalRandomFactory randomFactory, StructurePieceAccessor pieceAccessor,
										  @Nullable PerformanceMeasurer performanceMeasurer)
		{
			GridTemplate borderTemplate = new GridTemplate(dimensions, borderPalette);
			borderTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.TOP_BORDER));
			borderTemplate.setupFixedEdgeBounds(Direction.DOWN, Set.of(WFCData.ConnectorType.BOTTOM_BORDER));
			
			GridTemplate centerTemplate = new GridTemplate(dimensions, centerPalette);
			centerTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.TOP_BORDER));
			centerTemplate.setupFixedEdgeBounds(Direction.DOWN, Set.of(WFCData.ConnectorType.BOTTOM_BORDER));
			
			PositionTransform northWestTransform = middleTransform.offset(-dimensions.xAxisCells() / 2, -dimensions.zAxisCells() / 2);
			Generator northWestGenerator = cornerGenerator(borderTemplate, dimensions);
			northWestGenerator.collapse(northWestTransform.random(randomFactory),
					PiecePlacer.placeAt(northWestTransform, pieceAccessor));
			
			PositionTransform northEastTransform = northWestTransform.offset(dimensions.xAxisCells(), 0);
			Generator northEastGenerator = cornerGenerator(borderTemplate, dimensions);
			northEastGenerator.collapse(northEastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			PositionTransform southWestTransform = northWestTransform.offset(0, dimensions.zAxisCells());
			Generator southWestGenerator = cornerGenerator(borderTemplate, dimensions);
			southWestGenerator.collapse(southWestTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			PositionTransform southEastTransform = northWestTransform.offset(dimensions.xAxisCells(), dimensions.zAxisCells());
			Generator southEastGenerator = cornerGenerator(borderTemplate, dimensions);
			southEastGenerator.collapse(southEastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			
			Generator northGenerator = zEdgeGenerator(borderTemplate, dimensions, northWestGenerator, northEastGenerator);
			PositionTransform northTransform = northWestTransform.offset(1, 0);
			northGenerator.collapse(northTransform.random(randomFactory),
					PiecePlacer.placeAt(northTransform, pieceAccessor));
			
			Generator westGenerator = xEdgeGenerator(borderTemplate, dimensions, northWestGenerator, southWestGenerator);
			PositionTransform westTransform = northWestTransform.offset(0, 1);
			westGenerator.collapse(westTransform.random(randomFactory),
					PiecePlacer.placeAt(westTransform, pieceAccessor));
			
			PositionTransform southTransform = northWestTransform.offset(1, dimensions.zAxisCells());
			Generator southGenerator = zEdgeGenerator(borderTemplate, dimensions, southWestGenerator, southEastGenerator);
			southGenerator.collapse(southTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			PositionTransform eastTransform = northWestTransform.offset(dimensions.xAxisCells(), 1);
			Generator eastGenerator = xEdgeGenerator(borderTemplate, dimensions, northEastGenerator, southEastGenerator);
			eastGenerator.collapse(eastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			if(performanceMeasurer != null)
				performanceMeasurer.start(PerformanceMeasurer.Type.CENTER);
			PositionTransform centerTransform = northWestTransform.offset(1, 1);
			Generator centerGenerator = centerGenerator(centerTemplate, dimensions,
					northGenerator, westGenerator, southGenerator, eastGenerator);
			centerGenerator.collapse(centerTransform.random(randomFactory),
					PiecePlacer.placeAt(centerTransform, pieceAccessor), performanceMeasurer);
			if(performanceMeasurer != null)
				performanceMeasurer.end(PerformanceMeasurer.Type.CENTER);
		}
		
		private static Generator cornerGenerator(GridTemplate template, Dimensions fullDimensions)
		{
			return new Generator(new Dimensions(1, fullDimensions.yAxisCells(), 1),
					template.grid.connectionTester, template::entriesFromTemplate);
		}
		
		private static Generator xEdgeGenerator(GridTemplate template, Dimensions fullDimensions, Generator northCorner, Generator southCorner)
		{
			Generator generator = new Generator(new Dimensions(1, fullDimensions.yAxisCells(), fullDimensions.zAxisCells() - 1),
					template.grid.connectionTester, template::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.NORTH, northCorner);
			generator.setupEdgeBounds(Direction.SOUTH, southCorner);
			return generator;
		}
		
		private static Generator zEdgeGenerator(GridTemplate template, Dimensions fullDimensions, Generator westCorner, Generator eastCorner)
		{
			Generator generator = new Generator(new Dimensions(fullDimensions.xAxisCells() - 1, fullDimensions.yAxisCells(), 1),
					template.grid.connectionTester, template::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.WEST, westCorner);
			generator.setupEdgeBounds(Direction.EAST, eastCorner);
			return generator;
		}
		
		private static Generator centerGenerator(GridTemplate template, Dimensions fullDimensions, Generator northSide, Generator westSide, Generator southSide, Generator eastSide)
		{
			Generator generator = new Generator(new Dimensions(fullDimensions.xAxisCells() - 1, fullDimensions.yAxisCells(), fullDimensions.zAxisCells() - 1),
					template.grid.connectionTester, template::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.NORTH, northSide);
			generator.setupEdgeBounds(Direction.WEST, westSide);
			generator.setupEdgeBounds(Direction.SOUTH, southSide);
			generator.setupEdgeBounds(Direction.EAST, eastSide);
			return generator;
		}
	}
	
	public static final class GridTemplate
	{
		private final PieceEntryGrid grid;
		
		public GridTemplate(Dimensions dimensions, WFCData.EntryPalette entryPalette)
		{
			this.grid = new PieceEntryGrid(new Dimensions(1, dimensions.yAxisCells(), 1),
					entryPalette.connectionTester(), true, (ignored, list) -> list.addAll(entryPalette.entries()));
		}
		
		public void setupFixedEdgeBounds(Direction direction, Set<WFCData.ConnectorType> connections)
		{
			for(CellPos pos : this.grid.dimensions.iterateEdge(direction))
			{
				this.grid.removeUnsupportedEntriesForSide(pos, direction, connections);
			}
		}
		
		void entriesFromTemplate(CellPos pos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>> entryList)
		{
			entryList.addAll(this.grid.availableEntriesMap.get(new CellPos(0, pos.y(), 0)).entries);
		}
	}
	
	public static final class Generator
	{
		private final PieceEntryGrid grid;
		
		Generator(Dimensions dimensions, WFCData.ConnectionTester connectionTester, BiConsumer<CellPos,
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.grid = new PieceEntryGrid(dimensions, connectionTester, false, dataInitializer);
		}
		
		void setupEdgeBounds(Direction direction, Generator adjacentGenerator)
		{
			for(CellPos pos : this.grid.dimensions.iterateEdge(direction))
			{
				CellPos projectedPos = adjacentGenerator.grid.dimensions.projectOntoEdge(pos, direction.getOpposite());
				Set<WFCData.ConnectorType> connections = adjacentGenerator.grid.availableConnectorsAt(projectedPos, direction.getOpposite());
				this.grid.removeUnsupportedEntriesForSide(pos, direction, connections);
			}
		}
		
		public void collapse(RandomSource random, PiecePlacer piecePlacer)
		{
			collapse(random, piecePlacer, null);
		}
		
		public void collapse(RandomSource random, PiecePlacer piecePlacer, @Nullable PerformanceMeasurer performanceMeasurer)
		{
			Set<CellPos> cellsToGenerate = new HashSet<>(this.grid.availableEntriesMap.keySet());
			
			while(!cellsToGenerate.isEmpty())
			{
				if(performanceMeasurer != null)
					performanceMeasurer.start(PerformanceMeasurer.Type.ENTROPY_SEARCH);
				MinValueSearchResult<CellPos> leastEntropyResult = MinValueSearchResult.search(cellsToGenerate,
						pos -> {
							if(performanceMeasurer != null)
								performanceMeasurer.start(PerformanceMeasurer.Type.ENTROPY_CALC);
							var entropy = this.grid.availableEntriesMap.get(pos).getEntropy();
							if(performanceMeasurer != null)
								performanceMeasurer.end(PerformanceMeasurer.Type.ENTROPY_CALC);
							return entropy;
						});
				if(performanceMeasurer != null)
					performanceMeasurer.end(PerformanceMeasurer.Type.ENTROPY_SEARCH);
				
				if(leastEntropyResult.entries.isEmpty())
					break;
				
				CellPos pos = Util.getRandom(leastEntropyResult.entries, random);
				cellsToGenerate.remove(pos);
				
				EntropyList<WeightedEntry.Wrapper<WFCData.PieceEntry>> availableEntries = this.grid.availableEntriesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(random, availableEntries.entries);
				if(chosenEntry.isEmpty())
				{
					piecePlacer.logNoEntries(pos);
					continue;
				}
				
				piecePlacer.place(pos, chosenEntry.get().data());
				
				if(performanceMeasurer != null)
					performanceMeasurer.start(PerformanceMeasurer.Type.ADJACENCY_UPDATE);
				if(availableEntries.removeIf(entry -> entry != chosenEntry.get()))
					this.grid.removeAdjacentUnsupportedEntries(pos, null);
				if(performanceMeasurer != null)
					performanceMeasurer.end(PerformanceMeasurer.Type.ADJACENCY_UPDATE);
			}
		}
	}
	
	public interface PiecePlacer
	{
		PiecePlacer EMPTY = new PiecePlacer()
		{
			@Override
			public void place(CellPos cellPos, WFCData.PieceEntry entry)
			{
			}
			
			@Override
			public void logNoEntries(CellPos cellPos)
			{
			}
		};
		
		static PiecePlacer placeAt(PositionTransform transform, StructurePieceAccessor pieceAccessor)
		{
			return new PiecePlacer()
			{
				@Override
				public void place(CellPos cellPos, WFCData.PieceEntry entry)
				{
					BlockPos pos = transform.toBlockPos(cellPos);
					StructurePiece piece = entry.constructor().apply(pos);
					if(piece != null)
						pieceAccessor.addPiece(piece);
				}
				
				@Override
				public void logNoEntries(CellPos cellPos)
				{
					BlockPos pos = transform.toBlockPos(cellPos);
					WFC.LOGGER.warn("No entries possible at {}!", pos);
				}
			};
		}
		
		void place(CellPos cellPos, WFCData.PieceEntry entry);
		
		void logNoEntries(CellPos cellPos);
	}
	
	private static final class PieceEntryGrid
	{
		final Dimensions dimensions;
		final WFCData.ConnectionTester connectionTester;
		final boolean loopHorizontally;
		
		private final Map<CellPos, EntropyList<WeightedEntry.Wrapper<WFCData.PieceEntry>>> availableEntriesMap = new HashMap<>();
		
		public PieceEntryGrid(Dimensions dimensions, WFCData.ConnectionTester connectionTester, boolean loopHorizontally,
							  BiConsumer<CellPos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.dimensions = dimensions;
			this.connectionTester = connectionTester;
			this.loopHorizontally = loopHorizontally;
			
			for(CellPos pos : this.dimensions.iterateAll())
			{
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>> list = new ArrayList<>();
				dataInitializer.accept(pos, list);
				this.availableEntriesMap.put(pos, new EntropyList<>(list));
			}
		}
		
		Set<WFCData.ConnectorType> availableConnectorsAt(CellPos pos, Direction direction)
		{
			return this.availableEntriesMap.get(pos).stream()
					.map(entry -> entry.data().connections().get(direction))
					.collect(Collectors.toSet());
		}
		
		void removeAdjacentUnsupportedEntries(CellPos pos, @Nullable Direction excluding)
		{
			for(Direction direction : Direction.values())
			{
				if(direction == excluding)
					continue;
				
				pos.tryOffset(direction, this.dimensions, this.loopHorizontally).ifPresent(adjacentPos ->
				{
					Set<WFCData.ConnectorType> availableConnectors = this.availableConnectorsAt(pos, direction);
					
					this.removeUnsupportedEntriesForSide(adjacentPos, direction.getOpposite(), availableConnectors);
				});
			}
		}
		
		void removeUnsupportedEntriesForSide(CellPos pos, Direction direction, Set<WFCData.ConnectorType> availableConnectors)
		{
			if(availableConnectors.isEmpty())
				return;
			if(this.availableEntriesMap.get(pos).removeIf(entry ->
					!connectionTester.canConnect(entry.data().connections().get(direction), availableConnectors)))
				this.removeAdjacentUnsupportedEntries(pos, direction);
		}
	}
	
	private static final class EntropyList<T extends WeightedEntry>
	{
		private final List<T> entries;
		private double cachedEntropy = -1;
		
		EntropyList(List<T> entries)
		{
			this.entries = entries;
		}
		
		double getEntropy()
		{
			if(this.cachedEntropy == -1)
				this.cachedEntropy = calculateEntropy(this.entries);
			return this.cachedEntropy;
		}
		
		Stream<T> stream()
		{
			return this.entries.stream();
		}
		
		boolean removeIf(Predicate<? super T> predicate)
		{
			boolean removed = this.entries.removeIf(predicate);
			if(removed)
				this.cachedEntropy = -1;
			return removed;
		}
	}
	
	private static double calculateEntropy(List<? extends WeightedEntry> entries)
	{
		int totalWeight = WeightedRandom.getTotalWeight(entries);
		double entropy = 0;
		for(WeightedEntry entry : entries)
		{
			if(entry.getWeight().asInt() == 0)
				continue;
			
			double probability = (double) entry.getWeight().asInt() / totalWeight;
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
	
	public record Dimensions(int xAxisCells, int yAxisCells, int zAxisCells)
	{
		public Dimensions
		{
			if(xAxisCells <= 0)
				throw new IllegalArgumentException("Nonpositive x size: " + xAxisCells);
			if(yAxisCells <= 0)
				throw new IllegalArgumentException("Nonpositive y size: " + xAxisCells);
			if(zAxisCells <= 0)
				throw new IllegalArgumentException("Nonpositive z size: " + xAxisCells);
		}
		
		public boolean isInBounds(int x, int y, int z)
		{
			return 0 <= x && x < this.xAxisCells()
					&& 0 <= y && y < this.yAxisCells()
					&& 0 <= z && z < this.zAxisCells();
		}
		
		public boolean isOnEdge(CellPos cellPos, Direction edge)
		{
			return switch(edge)
			{
				case DOWN -> cellPos.y() == 0;
				case UP -> cellPos.y() == this.yAxisCells() - 1;
				case NORTH -> cellPos.z() == 0;
				case SOUTH -> cellPos.z() == this.zAxisCells() - 1;
				case WEST -> cellPos.x() == 0;
				case EAST -> cellPos.x() == this.xAxisCells() - 1;
			};
		}
		
		public int loopX(int x)
		{
			if(x < 0)
				return this.xAxisCells() - 1;
			if(x >= this.xAxisCells())
				return 0;
			return x;
		}
		
		public int loopZ(int z)
		{
			if(z < 0)
				return this.zAxisCells() - 1;
			if(z >= this.zAxisCells())
				return 0;
			return z;
		}
		
		public CellPos projectOntoEdge(CellPos pos, Direction edge)
		{
			int x, y, z;
			if(edge == Direction.WEST)
				x = 0;
			else if(edge == Direction.EAST)
				x = this.xAxisCells() - 1;
			else
				x = pos.x;
			
			if(edge == Direction.DOWN)
				y = 0;
			else if(edge == Direction.UP)
				y = this.yAxisCells() - 1;
			else
				y = pos.y;
			
			if(edge == Direction.NORTH)
				z = 0;
			else if(edge == Direction.SOUTH)
				z = this.zAxisCells() - 1;
			else
				z = pos.z;
			
			if(!this.isInBounds(x, y, z))
				throw new IllegalArgumentException("Unable to project pos " + pos + " on edge " + edge + " in dimensions " + this);
			return new CellPos(x, y, z);
		}
		
		public Iterable<CellPos> iterateAll()
		{
			return CellPos.iterateBox(0, 0, 0,
					this.xAxisCells() - 1, this.yAxisCells() - 1, this.zAxisCells() - 1);
		}
		
		public Iterable<CellPos> iterateEdge(Direction direction)
		{
			int minX = direction != Direction.EAST ? 0 : this.xAxisCells() - 1;
			int minY = direction != Direction.UP ? 0 : this.yAxisCells() - 1;
			int minZ = direction != Direction.SOUTH ? 0 : this.zAxisCells() - 1;
			int maxX = direction != Direction.WEST ? this.xAxisCells() - 1 : 0;
			int maxY = direction != Direction.DOWN ? this.yAxisCells() - 1 : 0;
			int maxZ = direction != Direction.NORTH ? this.zAxisCells() - 1 : 0;
			return CellPos.iterateBox(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}
	
	public record CellSize(int width, int height)
	{
	}
	
	public record CellPos(int x, int y, int z)
	{
		public Optional<CellPos> tryOffset(Direction direction, Dimensions dimensions, boolean loopHorizontalEdges)
		{
			int newX = this.x + direction.getStepX();
			int newY = this.y + direction.getStepY();
			int newZ = this.z + direction.getStepZ();
			
			if(loopHorizontalEdges)
			{
				newX = dimensions.loopX(newX);
				newZ = dimensions.loopZ(newZ);
			}
			
			if(!dimensions.isInBounds(newX, newY, newZ))
				return Optional.empty();
			
			return Optional.of(new CellPos(newX, newY, newZ));
		}
		
		public static Iterable<CellPos> iterateBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
		{
			return () -> new AbstractIterator<>()
			{
				@Nullable
				CellPos pos = null;
				
				@Nullable
				@Override
				protected CellPos computeNext()
				{
					if(pos == null)
						pos = new CellPos(minX, minY, minZ);
					else if(pos.y < maxY)
						pos = new CellPos(pos.x, pos.y + 1, pos.z);
					else if(pos.z < maxZ)
						pos = new CellPos(pos.x, minY, pos.z + 1);
					else if(pos.x < maxX)
						pos = new CellPos(pos.x + 1, minY, minZ);
					else
						return this.endOfData();
					
					return pos;
				}
			};
		}
	}
	
	public record PositionTransform(BlockPos cornerPos, CellSize cellSize)
	{
		public BlockPos toBlockPos(CellPos cellPos)
		{
			return this.cornerPos.offset(cellPos.x * this.cellSize.width(), cellPos.y * this.cellSize.height(), cellPos.z * this.cellSize.width());
		}
		
		public RandomSource random(PositionalRandomFactory randomFactory)
		{
			return randomFactory.at(this.cornerPos);
		}
		
		public PositionTransform offset(int x, int z)
		{
			return new PositionTransform(this.cornerPos.offset(x * this.cellSize.width(), 0, z * this.cellSize.width()), this.cellSize);
		}
	}
	
	public static final class PerformanceMeasurer
	{
		private final Map<Type, Long> measuredTimes = new EnumMap<>(Type.class);
		private final Map<Type, Long> measurementStarts = new EnumMap<>(Type.class);
		
		void start(Type type)
		{
			this.measurementStarts.put(type, System.currentTimeMillis());
		}
		
		void end(Type type)
		{
			long measuredTime = System.currentTimeMillis() - this.measurementStarts.remove(type);
			this.measuredTimes.merge(type, measuredTime, Long::sum);
		}
		
		@Override
		public String toString()
		{
			return this.measuredTimes.toString();
		}
		
		enum Type
		{
			CENTER,
			ENTROPY_SEARCH,
			ENTROPY_CALC,
			ADJACENCY_UPDATE,
		}
	}
}
