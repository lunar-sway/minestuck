package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.AbstractIterator;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * Contains components for an implementation of Wave Function Collapse.
 * The main component for running WFC is {@link Generator}.
 * @see WFCData
 */
public final class WFC
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final class Template
	{
		private final Dimensions fullDimensions;
		
		private final PieceEntryGrid grid;
		
		public Template(Dimensions dimensions, WFCData.EntriesData entriesData)
		{
			this.fullDimensions = dimensions;
			this.grid = new PieceEntryGrid(new Dimensions(1, dimensions.yAxisPieces(), 1),
					entriesData.connectionTester(), true, (ignored, list) -> list.addAll(entriesData.entries()));
		}
		
		public void setupFixedEdgeBounds(Direction direction, Set<WFCData.ConnectorType> connections)
		{
			for(PiecePos pos : this.grid.dimensions.iterateEdge(direction))
			{
				this.grid.removeConflictsFromConnection(pos, direction, connections);
			}
		}
		
		public Generator cornerGenerator()
		{
			return new Generator(new Dimensions(1, this.fullDimensions.yAxisPieces(), 1),
					this.grid.connectionTester, this::entriesFromTemplate);
		}
		
		public Generator xEdgeGenerator(Generator northCorner, Generator southCorner)
		{
			Generator generator = new Generator(new Dimensions(1, this.fullDimensions.yAxisPieces(), this.fullDimensions.zAxisPieces() - 1),
					this.grid.connectionTester, this::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.NORTH, northCorner);
			generator.setupEdgeBounds(Direction.SOUTH, southCorner);
			return generator;
		}
		
		public Generator zEdgeGenerator(Generator westCorner, Generator eastCorner)
		{
			Generator generator = new Generator(new Dimensions(this.fullDimensions.xAxisPieces() - 1, this.fullDimensions.yAxisPieces(), 1),
					this.grid.connectionTester, this::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.WEST, westCorner);
			generator.setupEdgeBounds(Direction.EAST, eastCorner);
			return generator;
		}
		
		public Generator centerGenerator(Generator northSide, Generator westSide, Generator southSide, Generator eastSide)
		{
			Generator generator = new Generator(new Dimensions(this.fullDimensions.xAxisPieces() - 1, this.fullDimensions.yAxisPieces(), this.fullDimensions.zAxisPieces() - 1),
					this.grid.connectionTester, this::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.NORTH, northSide);
			generator.setupEdgeBounds(Direction.WEST, westSide);
			generator.setupEdgeBounds(Direction.SOUTH, southSide);
			generator.setupEdgeBounds(Direction.EAST, eastSide);
			return generator;
		}
		
		private void entriesFromTemplate(PiecePos pos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>> entryList)
		{
			entryList.addAll(this.grid.availablePiecesMap.get(new PiecePos(0, pos.y(), 0)));
		}
	}
	
	public static final class Generator
	{
		private final PieceEntryGrid grid;
		
		Generator(Dimensions dimensions, WFCData.ConnectionTester connectionTester, BiConsumer<PiecePos,
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.grid = new PieceEntryGrid(dimensions, connectionTester, false, dataInitializer);
		}
		
		void setupEdgeBounds(Direction direction, Generator adjacentGenerator)
		{
			for(PiecePos pos : this.grid.dimensions.iterateEdge(direction))
			{
				PiecePos projectedPos = adjacentGenerator.grid.dimensions.projectOntoEdge(pos, direction.getOpposite());
				Set<WFCData.ConnectorType> connections = adjacentGenerator.grid.availableConnections(projectedPos, direction.getOpposite());
				this.grid.removeConflictsFromConnection(pos, direction, connections);
			}
		}
		
		public void collapse(RandomSource random, PiecePlacer piecePlacer)
		{
			Set<PiecePos> piecesToGenerate = new HashSet<>(this.grid.availablePiecesMap.keySet());
			
			while(!piecesToGenerate.isEmpty())
			{
				MinValueSearchResult<PiecePos> leastEntropyResult = MinValueSearchResult.search(piecesToGenerate,
						pos -> entropy(this.grid.availablePiecesMap.get(pos)));
				
				if(leastEntropyResult.entries.isEmpty())
					break;
				
				PiecePos pos = Util.getRandom(leastEntropyResult.entries, random);
				piecesToGenerate.remove(pos);
				
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>> availablePieces = this.grid.availablePiecesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(random, availablePieces);
				if(chosenEntry.isEmpty())
				{
					piecePlacer.logNoEntries(pos);
					continue;
				}
				
				piecePlacer.place(pos, chosenEntry.get().getData());
				
				if(availablePieces.removeIf(entry -> entry != chosenEntry.get()))
					this.grid.removeConflictingPieces(pos, null);
			}
		}
	}
	
	public interface PiecePlacer
	{
		PiecePlacer EMPTY = new PiecePlacer()
		{
			@Override
			public void place(PiecePos piecePos, WFCData.PieceEntry entry)
			{
			}
			
			@Override
			public void logNoEntries(PiecePos piecePos)
			{
			}
		};
		
		static PiecePlacer placeAt(BlockPos cornerPos, PieceSize pieceSize, StructureTemplateManager templateManager, StructurePiecesBuilder piecesBuilder)
		{
			return new PiecePlacer()
			{
				@Override
				public void place(PiecePos piecePos, WFCData.PieceEntry entry)
				{
					BlockPos pos = piecePos.toBlockPos(cornerPos, pieceSize);
					StructurePiece piece = entry.constructor().apply(templateManager, pos);
					if(piece != null)
						piecesBuilder.addPiece(piece);
				}
				
				@Override
				public void logNoEntries(PiecePos piecePos)
				{
					BlockPos pos = piecePos.toBlockPos(cornerPos, pieceSize);
					WFC.LOGGER.warn("No entries possible at {}!", pos);
				}
			};
		}
		
		void place(PiecePos piecePos, WFCData.PieceEntry entry);
		
		void logNoEntries(PiecePos piecePos);
	}
	
	private static final class PieceEntryGrid
	{
		final Dimensions dimensions;
		final WFCData.ConnectionTester connectionTester;
		final boolean loopHorizontally;
		
		private final Map<PiecePos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> availablePiecesMap = new HashMap<>();
		
		public PieceEntryGrid(Dimensions dimensions, WFCData.ConnectionTester connectionTester, boolean loopHorizontally,
							  BiConsumer<PiecePos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.dimensions = dimensions;
			this.connectionTester = connectionTester;
			this.loopHorizontally = loopHorizontally;
			
			for(PiecePos pos : this.dimensions.iterateAll())
			{
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>> list = new ArrayList<>();
				dataInitializer.accept(pos, list);
				this.availablePiecesMap.put(pos, list);
			}
		}
		
		Set<WFCData.ConnectorType> availableConnections(PiecePos pos, Direction direction)
		{
			return this.availablePiecesMap.get(pos).stream()
					.map(entry -> entry.getData().connections().get(direction))
					.collect(Collectors.toSet());
		}
		
		void removeConflictingPieces(PiecePos pos, @Nullable Direction excluding)
		{
			for(Direction direction : Direction.values())
			{
				if(direction == excluding)
					continue;
				
				pos.tryOffset(direction, this.dimensions, this.loopHorizontally).ifPresent(adjacentPos ->
				{
					Set<WFCData.ConnectorType> connections = this.availableConnections(pos, direction);
					
					this.removeConflictsFromConnection(adjacentPos, direction.getOpposite(), connections);
				});
			}
		}
		
		void removeConflictsFromConnection(PiecePos pos, Direction direction, Set<WFCData.ConnectorType> connections)
		{
			if(connections.isEmpty())
				return;
			if(this.availablePiecesMap.get(pos).removeIf(entry -> !connectionTester.canConnect(entry.getData().connections().get(direction), connections)))
				this.removeConflictingPieces(pos, direction);
		}
	}
	
	private static double entropy(List<? extends WeightedEntry> entries)
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
	
	public record Dimensions(int xAxisPieces, int yAxisPieces, int zAxisPieces)
	{
		public Dimensions {
			if(xAxisPieces <= 0)
				throw new IllegalArgumentException("Nonpositive x size: " + xAxisPieces);
			if(yAxisPieces <= 0)
				throw new IllegalArgumentException("Nonpositive y size: " + xAxisPieces);
			if(zAxisPieces <= 0)
				throw new IllegalArgumentException("Nonpositive z size: " + xAxisPieces);
		}
		
		public boolean isInBounds(int x, int y, int z)
		{
			return 0 <= x && x < this.xAxisPieces()
					&& 0 <= y && y < this.yAxisPieces()
					&& 0 <= z && z < this.zAxisPieces();
		}
		
		public int loopX(int x)
		{
			if(x < 0)
				return this.xAxisPieces() - 1;
			if(x >= this.xAxisPieces())
				return 0;
			return x;
		}
		
		public int loopZ(int z)
		{
			if(z < 0)
				return this.zAxisPieces() - 1;
			if(z >= this.zAxisPieces())
				return 0;
			return z;
		}
		
		public PiecePos projectOntoEdge(PiecePos pos, Direction edge)
		{
			int x, y, z;
			if(edge == Direction.WEST)
				x = 0;
			else if(edge == Direction.EAST)
				x = this.xAxisPieces() - 1;
			else
				x = pos.x;
			
			if(edge == Direction.DOWN)
				y = 0;
			else if(edge == Direction.UP)
				y = this.yAxisPieces() - 1;
			else
				y = pos.y;
			
			if(edge == Direction.NORTH)
				z = 0;
			else if(edge == Direction.SOUTH)
				z = this.zAxisPieces() - 1;
			else
				z = pos.z;
			
			if(!this.isInBounds(x, y, z))
				throw new IllegalArgumentException("Unable to project pos " + pos + " on edge " + edge + " in dimensions " + this);
			return new PiecePos(x, y, z);
		}
		
		public Iterable<PiecePos> iterateAll()
		{
			return PiecePos.iterateBox(0, 0, 0,
					this.xAxisPieces() - 1, this.yAxisPieces() - 1, this.zAxisPieces() - 1);
		}
		
		public Iterable<PiecePos> iterateEdge(Direction direction)
		{
			int minX = direction != Direction.EAST ? 0 : this.xAxisPieces() - 1;
			int minY = direction != Direction.UP ? 0 : this.yAxisPieces() - 1;
			int minZ = direction != Direction.SOUTH ? 0 : this.zAxisPieces() - 1;
			int maxX = direction != Direction.WEST ? this.xAxisPieces() - 1 : 0;
			int maxY = direction != Direction.DOWN ? this.yAxisPieces() - 1 : 0;
			int maxZ = direction != Direction.NORTH ? this.zAxisPieces() - 1 : 0;
			return PiecePos.iterateBox(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}
	
	public record PieceSize(int width, int height)
	{}
	
	public record PiecePos(int x, int y, int z)
	{
		public BlockPos toBlockPos(BlockPos cornerPos, PieceSize pieceSize)
		{
			return cornerPos.offset(this.x * pieceSize.width(), this.y * pieceSize.height(), this.z * pieceSize.width());
		}
		
		public Optional<PiecePos> tryOffset(Direction direction, Dimensions dimensions, boolean loopHorizontalEdges)
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
}
