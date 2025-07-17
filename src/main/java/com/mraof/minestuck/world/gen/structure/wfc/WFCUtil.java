package com.mraof.minestuck.world.gen.structure.wfc;

import com.google.common.collect.AbstractIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

public final class WFCUtil
{
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
	
	static final class EntropyList<T extends WeightedEntry>
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
		
		List<T> entries()
		{
			return this.entries;
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
	}
	
	static final class MinValueSearchResult<T>
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
		
		public List<T> entries()
		{
			return this.entries;
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
