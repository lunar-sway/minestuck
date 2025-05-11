package com.mraof.minestuck.world.gen.structure.wfc;

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
import java.util.stream.Collectors;

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
		public static void generateModule(WFCUtil.PositionTransform middleTransform, WFCUtil.Dimensions dimensions,
										  WFCData.EntryPalette centerPalette, WFCData.EntryPalette borderPalette,
										  PositionalRandomFactory randomFactory, StructurePieceAccessor pieceAccessor,
										  @Nullable WFCUtil.PerformanceMeasurer performanceMeasurer)
		{
			GridTemplate borderTemplate = new GridTemplate(dimensions, borderPalette);
			borderTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.TOP_BORDER));
			borderTemplate.setupFixedEdgeBounds(Direction.DOWN, Set.of(WFCData.ConnectorType.BOTTOM_BORDER));
			
			GridTemplate centerTemplate = new GridTemplate(dimensions, centerPalette);
			centerTemplate.setupFixedEdgeBounds(Direction.UP, Set.of(WFCData.ConnectorType.TOP_BORDER));
			centerTemplate.setupFixedEdgeBounds(Direction.DOWN, Set.of(WFCData.ConnectorType.BOTTOM_BORDER));
			
			WFCUtil.PositionTransform northWestTransform = middleTransform.offset(-dimensions.xAxisCells() / 2, -dimensions.zAxisCells() / 2);
			Generator northWestGenerator = cornerGenerator(borderTemplate, dimensions);
			northWestGenerator.collapse(northWestTransform.random(randomFactory),
					PiecePlacer.placeAt(northWestTransform, pieceAccessor));
			
			WFCUtil.PositionTransform northEastTransform = northWestTransform.offset(dimensions.xAxisCells(), 0);
			Generator northEastGenerator = cornerGenerator(borderTemplate, dimensions);
			northEastGenerator.collapse(northEastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			WFCUtil.PositionTransform southWestTransform = northWestTransform.offset(0, dimensions.zAxisCells());
			Generator southWestGenerator = cornerGenerator(borderTemplate, dimensions);
			southWestGenerator.collapse(southWestTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			WFCUtil.PositionTransform southEastTransform = northWestTransform.offset(dimensions.xAxisCells(), dimensions.zAxisCells());
			Generator southEastGenerator = cornerGenerator(borderTemplate, dimensions);
			southEastGenerator.collapse(southEastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			
			Generator northGenerator = zEdgeGenerator(borderTemplate, dimensions, northWestGenerator, northEastGenerator);
			WFCUtil.PositionTransform northTransform = northWestTransform.offset(1, 0);
			northGenerator.collapse(northTransform.random(randomFactory),
					PiecePlacer.placeAt(northTransform, pieceAccessor));
			
			Generator westGenerator = xEdgeGenerator(borderTemplate, dimensions, northWestGenerator, southWestGenerator);
			WFCUtil.PositionTransform westTransform = northWestTransform.offset(0, 1);
			westGenerator.collapse(westTransform.random(randomFactory),
					PiecePlacer.placeAt(westTransform, pieceAccessor));
			
			WFCUtil.PositionTransform southTransform = northWestTransform.offset(1, dimensions.zAxisCells());
			Generator southGenerator = zEdgeGenerator(borderTemplate, dimensions, southWestGenerator, southEastGenerator);
			southGenerator.collapse(southTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			WFCUtil.PositionTransform eastTransform = northWestTransform.offset(dimensions.xAxisCells(), 1);
			Generator eastGenerator = xEdgeGenerator(borderTemplate, dimensions, northEastGenerator, southEastGenerator);
			eastGenerator.collapse(eastTransform.random(randomFactory), PiecePlacer.EMPTY);
			
			if(performanceMeasurer != null)
				performanceMeasurer.start(WFCUtil.PerformanceMeasurer.Type.CENTER);
			WFCUtil.PositionTransform centerTransform = northWestTransform.offset(1, 1);
			Generator centerGenerator = centerGenerator(centerTemplate, dimensions,
					northGenerator, westGenerator, southGenerator, eastGenerator);
			centerGenerator.collapse(centerTransform.random(randomFactory),
					PiecePlacer.placeAt(centerTransform, pieceAccessor), performanceMeasurer);
			if(performanceMeasurer != null)
				performanceMeasurer.end(WFCUtil.PerformanceMeasurer.Type.CENTER);
		}
		
		private static Generator cornerGenerator(GridTemplate template, WFCUtil.Dimensions fullDimensions)
		{
			return new Generator(new WFCUtil.Dimensions(1, fullDimensions.yAxisCells(), 1),
					template.grid.connectionTester, template::entriesFromTemplate);
		}
		
		private static Generator xEdgeGenerator(GridTemplate template, WFCUtil.Dimensions fullDimensions, Generator northCorner, Generator southCorner)
		{
			Generator generator = new Generator(new WFCUtil.Dimensions(1, fullDimensions.yAxisCells(), fullDimensions.zAxisCells() - 1),
					template.grid.connectionTester, template::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.NORTH, northCorner);
			generator.setupEdgeBounds(Direction.SOUTH, southCorner);
			return generator;
		}
		
		private static Generator zEdgeGenerator(GridTemplate template, WFCUtil.Dimensions fullDimensions, Generator westCorner, Generator eastCorner)
		{
			Generator generator = new Generator(new WFCUtil.Dimensions(fullDimensions.xAxisCells() - 1, fullDimensions.yAxisCells(), 1),
					template.grid.connectionTester, template::entriesFromTemplate);
			generator.setupEdgeBounds(Direction.WEST, westCorner);
			generator.setupEdgeBounds(Direction.EAST, eastCorner);
			return generator;
		}
		
		private static Generator centerGenerator(GridTemplate template, WFCUtil.Dimensions fullDimensions, Generator northSide, Generator westSide, Generator southSide, Generator eastSide)
		{
			Generator generator = new Generator(new WFCUtil.Dimensions(fullDimensions.xAxisCells() - 1, fullDimensions.yAxisCells(), fullDimensions.zAxisCells() - 1),
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
		
		public GridTemplate(WFCUtil.Dimensions dimensions, WFCData.EntryPalette entryPalette)
		{
			this.grid = new PieceEntryGrid(new WFCUtil.Dimensions(1, dimensions.yAxisCells(), 1),
					entryPalette.connectionTester(), true, (ignored, list) -> list.addAll(entryPalette.entries()));
		}
		
		public void setupFixedEdgeBounds(Direction direction, Set<WFCData.ConnectorType> connections)
		{
			for(WFCUtil.CellPos pos : this.grid.dimensions.iterateEdge(direction))
			{
				this.grid.removeUnsupportedEntriesForSide(pos, direction, connections);
			}
		}
		
		void entriesFromTemplate(WFCUtil.CellPos pos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>> entryList)
		{
			entryList.addAll(this.grid.availableEntriesMap.get(new WFCUtil.CellPos(0, pos.y(), 0)).entries());
		}
	}
	
	public static final class Generator
	{
		private final PieceEntryGrid grid;
		
		Generator(WFCUtil.Dimensions dimensions, WFCData.ConnectionTester connectionTester, BiConsumer<WFCUtil.CellPos,
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.grid = new PieceEntryGrid(dimensions, connectionTester, false, dataInitializer);
		}
		
		void setupEdgeBounds(Direction direction, Generator adjacentGenerator)
		{
			for(WFCUtil.CellPos pos : this.grid.dimensions.iterateEdge(direction))
			{
				WFCUtil.CellPos projectedPos = adjacentGenerator.grid.dimensions.projectOntoEdge(pos, direction.getOpposite());
				Set<WFCData.ConnectorType> connections = adjacentGenerator.grid.availableConnectorsAt(projectedPos, direction.getOpposite());
				this.grid.removeUnsupportedEntriesForSide(pos, direction, connections);
			}
		}
		
		public void collapse(RandomSource random, PiecePlacer piecePlacer)
		{
			collapse(random, piecePlacer, null);
		}
		
		public void collapse(RandomSource random, PiecePlacer piecePlacer, @Nullable WFCUtil.PerformanceMeasurer performanceMeasurer)
		{
			Set<WFCUtil.CellPos> cellsToGenerate = new HashSet<>(this.grid.availableEntriesMap.keySet());
			
			while(!cellsToGenerate.isEmpty())
			{
				if(performanceMeasurer != null)
					performanceMeasurer.start(WFCUtil.PerformanceMeasurer.Type.ENTROPY_SEARCH);
				WFCUtil.MinValueSearchResult<WFCUtil.CellPos> leastEntropyResult = WFCUtil.MinValueSearchResult.search(cellsToGenerate,
						pos -> {
							if(performanceMeasurer != null)
								performanceMeasurer.start(WFCUtil.PerformanceMeasurer.Type.ENTROPY_CALC);
							var entropy = this.grid.availableEntriesMap.get(pos).getEntropy();
							if(performanceMeasurer != null)
								performanceMeasurer.end(WFCUtil.PerformanceMeasurer.Type.ENTROPY_CALC);
							return entropy;
						});
				if(performanceMeasurer != null)
					performanceMeasurer.end(WFCUtil.PerformanceMeasurer.Type.ENTROPY_SEARCH);
				
				if(leastEntropyResult.entries().isEmpty())
					break;
				
				WFCUtil.CellPos pos = Util.getRandom(leastEntropyResult.entries(), random);
				cellsToGenerate.remove(pos);
				
				WFCUtil.EntropyList<WeightedEntry.Wrapper<WFCData.PieceEntry>> availableEntries = this.grid.availableEntriesMap.get(pos);
				var chosenEntry = WeightedRandom.getRandomItem(random, availableEntries.entries());
				if(chosenEntry.isEmpty())
				{
					piecePlacer.logNoEntries(pos);
					continue;
				}
				
				piecePlacer.place(pos, chosenEntry.get().data());
				
				if(performanceMeasurer != null)
					performanceMeasurer.start(WFCUtil.PerformanceMeasurer.Type.ADJACENCY_UPDATE);
				if(availableEntries.removeIf(entry -> entry != chosenEntry.get()))
					this.grid.removeAdjacentUnsupportedEntries(pos, null);
				if(performanceMeasurer != null)
					performanceMeasurer.end(WFCUtil.PerformanceMeasurer.Type.ADJACENCY_UPDATE);
			}
		}
	}
	
	public interface PiecePlacer
	{
		PiecePlacer EMPTY = new PiecePlacer()
		{
			@Override
			public void place(WFCUtil.CellPos cellPos, WFCData.PieceEntry entry)
			{
			}
			
			@Override
			public void logNoEntries(WFCUtil.CellPos cellPos)
			{
			}
		};
		
		static PiecePlacer placeAt(WFCUtil.PositionTransform transform, StructurePieceAccessor pieceAccessor)
		{
			return new PiecePlacer()
			{
				@Override
				public void place(WFCUtil.CellPos cellPos, WFCData.PieceEntry entry)
				{
					BlockPos pos = transform.toBlockPos(cellPos);
					StructurePiece piece = entry.constructor().apply(pos);
					if(piece != null)
						pieceAccessor.addPiece(piece);
				}
				
				@Override
				public void logNoEntries(WFCUtil.CellPos cellPos)
				{
					BlockPos pos = transform.toBlockPos(cellPos);
					WFC.LOGGER.warn("No entries possible at {}!", pos);
				}
			};
		}
		
		void place(WFCUtil.CellPos cellPos, WFCData.PieceEntry entry);
		
		void logNoEntries(WFCUtil.CellPos cellPos);
	}
	
	private static final class PieceEntryGrid
	{
		final WFCUtil.Dimensions dimensions;
		final WFCData.ConnectionTester connectionTester;
		final boolean loopHorizontally;
		
		private final Map<WFCUtil.CellPos, WFCUtil.EntropyList<WeightedEntry.Wrapper<WFCData.PieceEntry>>> availableEntriesMap = new HashMap<>();
		
		public PieceEntryGrid(WFCUtil.Dimensions dimensions, WFCData.ConnectionTester connectionTester, boolean loopHorizontally,
							  BiConsumer<WFCUtil.CellPos, List<WeightedEntry.Wrapper<WFCData.PieceEntry>>> dataInitializer)
		{
			this.dimensions = dimensions;
			this.connectionTester = connectionTester;
			this.loopHorizontally = loopHorizontally;
			
			for(WFCUtil.CellPos pos : this.dimensions.iterateAll())
			{
				List<WeightedEntry.Wrapper<WFCData.PieceEntry>> list = new ArrayList<>();
				dataInitializer.accept(pos, list);
				this.availableEntriesMap.put(pos, new WFCUtil.EntropyList<>(list));
			}
		}
		
		Set<WFCData.ConnectorType> availableConnectorsAt(WFCUtil.CellPos pos, Direction direction)
		{
			return this.availableEntriesMap.get(pos).stream()
					.map(entry -> entry.data().connections().get(direction))
					.collect(Collectors.toSet());
		}
		
		void removeAdjacentUnsupportedEntries(WFCUtil.CellPos pos, @Nullable Direction excluding)
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
		
		void removeUnsupportedEntriesForSide(WFCUtil.CellPos pos, Direction direction, Set<WFCData.ConnectorType> availableConnectors)
		{
			if(availableConnectors.isEmpty())
				return;
			if(this.availableEntriesMap.get(pos).removeIf(entry ->
					!connectionTester.canConnect(entry.data().connections().get(direction), availableConnectors)))
				this.removeAdjacentUnsupportedEntries(pos, direction);
		}
	}
}
