package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
public final class WFCData
{
	public interface ConnectionTester
	{
		boolean canConnect(ConnectorType connection, Set<ConnectorType> connections);
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