package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
public final class WFCData
{
	public record PieceEntry(BiFunction<StructureTemplateManager, BlockPos, StructurePiece> constructor,
							 Map<Direction, ConnectorType> connections)
	{
		public static final PieceEntry EMPTY = PieceEntry.symmetric(pos -> null, ConnectorType.AIR, ConnectorType.AIR, ConnectorType.AIR);
		
		public static PieceEntry symmetric(ResourceLocation templateId,
										   ConnectorType down, ConnectorType up, ConnectorType side)
		{
			return new PieceEntry(templateConstructor(templateId, Rotation.NONE), Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, side,
					Direction.EAST, side,
					Direction.SOUTH, side,
					Direction.WEST, side
			));
		}
		
		public static PieceEntry symmetric(Function<BlockPos, StructurePiece> constructor,
										   ConnectorType down, ConnectorType up, ConnectorType side)
		{
			return new PieceEntry((ignored, pos) -> constructor.apply(pos), Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, side,
					Direction.EAST, side,
					Direction.SOUTH, side,
					Direction.WEST, side
			));
		}
		
		public static Collection<PieceEntry> axisSymmetric(ResourceLocation templateId,
														   ConnectorType down, ConnectorType up, ConnectorType front, ConnectorType side)
		{
			Map<Direction, ConnectorType> connections = Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, front,
					Direction.EAST, side,
					Direction.SOUTH, front,
					Direction.WEST, side
			);
			
			return List.of(
					new PieceEntry(templateConstructor(templateId, Rotation.NONE), connections),
					new PieceEntry(templateConstructor(templateId, Rotation.CLOCKWISE_90), rotateConnections(connections, Rotation.CLOCKWISE_90))
			);
		}
		
		public static Collection<PieceEntry> axisSymmetric(BiFunction<BlockPos, Direction.Axis, StructurePiece> constructor,
														   ConnectorType down, ConnectorType up, ConnectorType front, ConnectorType side)
		{
			Map<Direction, ConnectorType> connections = Map.of(
					Direction.DOWN, down,
					Direction.UP, up,
					Direction.NORTH, front,
					Direction.EAST, side,
					Direction.SOUTH, front,
					Direction.WEST, side
			);
			
			return List.of(
					new PieceEntry((ignored, pos) -> constructor.apply(pos, Direction.Axis.Z), connections),
					new PieceEntry((ignored, pos) -> constructor.apply(pos, Direction.Axis.X), rotateConnections(connections, Rotation.CLOCKWISE_90))
			);
		}
		
		public static Collection<PieceEntry> rotatable(BiFunction<BlockPos, Rotation, StructurePiece> constructor,
													   Map<Direction, ConnectorType> connections)
		{
			return Arrays.stream(Rotation.values())
					.map(rotation -> new PieceEntry((ignored, pos) -> constructor.apply(pos, rotation), rotateConnections(connections, rotation)))
					.toList();
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
		
		private static BiFunction<StructureTemplateManager, BlockPos, StructurePiece> templateConstructor(ResourceLocation templateId, Rotation rotation)
		{
			return (templateManager, pos) -> {
				BlockPos templatePos = templateManager.getOrCreate(templateId).getZeroPositionWithTransform(pos, Mirror.NONE, rotation);
				return new SimpleTemplatePiece(templateManager, templateId, templatePos, rotation);
			};
		}
	}
	
	public record ConnectorType(ResourceLocation id)
	{
		public ConnectorType(String path)
		{
			this(Minestuck.id(path));
		}
		
		public static final ConnectorType AIR = new ConnectorType("air"),
				SOLID = new ConnectorType("solid"),
				WALL = new ConnectorType("wall"),
				ROOF_SIDE = new ConnectorType("roof_side"),
				BRIDGE = new ConnectorType("bridge"),
				LEDGE_FRONT = new ConnectorType("ledge/front"),
				LEDGE_LEFT = new ConnectorType("ledge/left"),
				LEDGE_RIGHT = new ConnectorType("ledge/right"),
				LEDGE_BACK = new ConnectorType("ledge/back");
		
		public static void addCoreConnections(ConnectionsBuilder builder)
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
	
	public record MultiPieceEntry(Collection<PieceEntry> entries, Collection<Pair<ConnectorType, ConnectorType>> connections)
	{
		public static MultiPieceEntry symmetricPillar(Function<BlockPos, StructurePiece> constructor, String name, int height,
													  ConnectorType down, ConnectorType up, List<ConnectorType> sides)
		{
			if(sides.size() != height)
				throw new IllegalArgumentException("Number of sides must match height");
			
			List<Pair<ConnectorType, ConnectorType>> verticalConnections = IntStream.range(0, height - 1).mapToObj(y -> {
				ConnectorType connector = new ConnectorType(name + "/vertical_" + y);
				return Pair.of(connector, connector);
			}).toList();
			
			ImmutableList.Builder<PieceEntry> entriesBuilder = ImmutableList.builder();
			entriesBuilder.add(PieceEntry.symmetric(constructor, down, verticalConnections.get(0).getFirst(), sides.get(0)));
			for(int y = 1; y < height - 1; y++)
				entriesBuilder.add(PieceEntry.symmetric(pos -> null, verticalConnections.get(y - 1).getSecond(), verticalConnections.get(y).getFirst(), sides.get(y)));
			entriesBuilder.add(PieceEntry.symmetric(pos -> null, verticalConnections.get(height - 2).getSecond(), up, sides.get(height - 1)));
			
			return new MultiPieceEntry(entriesBuilder.build(), verticalConnections);
		}
	}
	
	public interface ConnectionTester
	{
		boolean canConnect(ConnectorType connection, Set<ConnectorType> connections);
	}
	
	public record EntriesData(Collection<WeightedEntry.Wrapper<PieceEntry>> entries, ConnectionTester connectionTester)
	{
	}
	
	public static final class ConnectionsBuilder
	{
		private final Map<ConnectorType, ImmutableSet.Builder<ConnectorType>> connections = new HashMap<>();
		
		public void connectSelf(ConnectorType type)
		{
			this.connect(type, type);
		}
		
		public void connect(ConnectorType type1, ConnectorType type2)
		{
			connections.computeIfAbsent(type1, ignored -> ImmutableSet.builder()).add(type2);
			connections.computeIfAbsent(type2, ignored -> ImmutableSet.builder()).add(type1);
		}
		
		private ConnectionTester buildConnectionTester()
		{
			Map<ConnectorType, Set<ConnectorType>> map = this.connections.entrySet().stream()
					.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> entry.getValue().build()));
			return (type, otherTypes) -> {
				Set<ConnectorType> supportedTypes = map.get(type);
				if(supportedTypes == null)
					return false;
				return otherTypes.stream().anyMatch(supportedTypes::contains);
			};
		}
	}
	
	public static final class EntriesBuilder
	{
		private final ConnectionsBuilder connectionsBuilder = new ConnectionsBuilder();
		private final ImmutableList.Builder<WeightedEntry.Wrapper<PieceEntry>> pieceEntries = ImmutableList.builder();
		
		public ConnectionsBuilder connections()
		{
			return this.connectionsBuilder;
		}
		
		
		public void add(MultiPieceEntry entry, int weight)
		{
			entry.connections().forEach(pair -> this.connections().connect(pair.getFirst(), pair.getSecond()));
			this.add(entry.entries(), weight);
		}
		
		public void add(Collection<PieceEntry> entries, int weight)
		{
			entries.forEach(pieceEntry -> this.add(pieceEntry, weight));
		}
		
		public void add(PieceEntry pieceEntry, int weight)
		{
			this.pieceEntries.add(WeightedEntry.wrap(pieceEntry, weight));
		}
		
		
		public EntriesData build()
		{
			return new EntriesData(this.pieceEntries.build(), this.connectionsBuilder.buildConnectionTester());
		}
	}
}
