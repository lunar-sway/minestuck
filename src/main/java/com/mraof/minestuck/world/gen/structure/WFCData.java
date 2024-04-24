package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mraof.minestuck.Minestuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
public final class WFCData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static PieceEntry symmetricPiece(Function<BlockPos, StructurePiece> constructor,
											ConnectorType down, ConnectorType up, ConnectorType side)
	{
		return new PieceEntry(constructor, Map.of(
				Direction.DOWN, down,
				Direction.UP, up,
				Direction.NORTH, side,
				Direction.EAST, side,
				Direction.SOUTH, side,
				Direction.WEST, side
		));
	}
	
	public static EntryProvider symmetricPillarPieces(Function<BlockPos, StructurePiece> constructor, String name, int height,
													  ConnectorType down, ConnectorType up, List<ConnectorType> sides)
	{
		if(sides.size() != height)
			throw new IllegalArgumentException("Number of sides must match height");
		
		List<Pair<ConnectorType, ConnectorType>> verticalConnections = IntStream.range(0, height - 1).mapToObj(y -> {
			ConnectorType connector = new ConnectorType(name + "/vertical_" + y);
			return Pair.of(connector, connector);
		}).toList();
		
		ImmutableList.Builder<PieceEntry> entriesBuilder = ImmutableList.builder();
		entriesBuilder.add(symmetricPiece(constructor, down, verticalConnections.get(0).getFirst(), sides.get(0)));
		for(int y = 1; y < height - 1; y++)
			entriesBuilder.add(symmetricPiece(pos -> null, verticalConnections.get(y - 1).getSecond(), verticalConnections.get(y).getFirst(), sides.get(y)));
		entriesBuilder.add(symmetricPiece(pos -> null, verticalConnections.get(height - 2).getSecond(), up, sides.get(height - 1)));
		
		return new MultiPieceEntry(entriesBuilder.build(), verticalConnections);
	}
	
	private static Map<Direction, ConnectorType> rotateConnectors(Map<Direction, ConnectorType> connections, Rotation rotation)
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
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor,
							 Map<Direction, ConnectorType> connections) implements EntryProvider
	{
		public static final EntryProvider EMPTY = symmetricPiece(pos -> null, ConnectorType.AIR, ConnectorType.AIR, ConnectorType.AIR);
		
		@Override
		public void build(EntryBuilderContext context)
		{
			context.entriesBuilder().add(this);
		}
	}
	
	public record MultiPieceEntry(Collection<PieceEntry> entries, Collection<Pair<ConnectorType, ConnectorType>> connections) implements EntryProvider
	{
		@Override
		public void build(EntryBuilderContext context)
		{
			this.connections.forEach(pair -> context.connectionsBuilder().connect(pair.getFirst(), pair.getSecond()));
			context.entriesBuilder().add(this.entries);
		}
	}
	
	public record TemplateEntry(ResourceLocation templateId, List<Rotation> rotations) implements EntryProvider
	{
		public static EntryProvider symmetric(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.NONE));
		}
		
		public static EntryProvider axisSymmetric(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.NONE, Rotation.CLOCKWISE_90));
		}
		
		public static EntryProvider rotatable(ResourceLocation templateId)
		{
			return new TemplateEntry(templateId, List.of(Rotation.values()));
		}
		
		@Override
		public void build(EntryBuilderContext context)
		{
			StructureTemplate template = context.templateManager().getOrCreate(templateId);
			
			loadTemplateConnectors(templateId, template, context.pieceSize()).ifPresent(connectors -> {
				for(Rotation rotation : rotations)
				{
					Function<BlockPos, StructurePiece> constructor = templateConstructor(templateId, template, rotation, context.templateManager());
					PieceEntry entry = new PieceEntry(constructor, rotateConnectors(connectors, rotation));
					context.entriesBuilder().add(entry);
				}
			});
		}
		
		private static Optional<Map<Direction, ConnectorType>> loadTemplateConnectors(ResourceLocation templateId, StructureTemplate template, WFC.PieceSize pieceSize)
		{
			Vec3i size = template.getSize();
			if(size.getX() != pieceSize.width() || size.getY() != pieceSize.height() || size.getZ() != pieceSize.width())
			{
				LOGGER.error("Template {} of size {} does not have the right size.", templateId, size);
				return Optional.empty();
			}
			
			Map<Direction, ConnectorType> connectors = new HashMap<>();
			
			for(StructureTemplate.StructureBlockInfo blockInfo : template.filterBlocks(BlockPos.ZERO, new StructurePlaceSettings(), Blocks.JIGSAW, false))
			{
				if(blockInfo.nbt() == null)
				{
					LOGGER.warn("Jigsaw block is missing data in template {} at {}", templateId, blockInfo.pos());
					continue;
				}
				String connectorName = blockInfo.nbt().getString(JigsawBlockEntity.NAME);
				ResourceLocation connectorId = ResourceLocation.tryParse(connectorName);
				if(connectorId == null)
				{
					LOGGER.error("Connector name \"{}\" in template {} is not a valid id", connectorName, templateId);
					continue;
				}
				
				ConnectorType connectorType = new ConnectorType(connectorId);
				Direction direction = blockInfo.state().getValue(JigsawBlock.ORIENTATION).front();
				
				if(connectors.containsKey(direction))
				{
					LOGGER.error("Template {} has multiple jigsaws for the same side ({})", templateId, direction);
					return Optional.empty();
				}
				
				connectors.put(direction, connectorType);
			}
			
			List<Direction> missingDirections = Arrays.stream(Direction.values()).filter(key -> !connectors.containsKey(key)).toList();
			if(!missingDirections.isEmpty())
			{
				LOGGER.error("Template {} is missing connector type for the following sides: {}", templateId, missingDirections);
				return Optional.empty();
			}
			
			return Optional.of(Map.copyOf(connectors));
		}
		
		private static Function<BlockPos, StructurePiece> templateConstructor(ResourceLocation templateId, StructureTemplate template,
																			  Rotation rotation, StructureTemplateManager templateManager)
		{
			BlockPos zeroPos = template.getZeroPositionWithTransform(BlockPos.ZERO, Mirror.NONE, rotation);
			return pos -> new SimpleTemplatePiece(templateManager, templateId, pos.offset(zeroPos), rotation);
		}
	}
	
	public interface WeightedEntriesBuilder
	{
		void add(PieceEntry entry);
		
		default void add(PieceEntry... entries)
		{
			for(PieceEntry entry : entries)
				this.add(entry);
		}
		
		default void add(Iterable<PieceEntry> entries)
		{
			entries.forEach(this::add);
		}
	}
	
	public record EntryBuilderContext(ConnectionsBuilder connectionsBuilder, WeightedEntriesBuilder entriesBuilder,
									  WFC.PieceSize pieceSize, StructureTemplateManager templateManager)
	{
	}
	
	public interface EntryProvider
	{
		void build(EntryBuilderContext context);
	}
	
	public static final class ConnectionTester
	{
		private final Map<ConnectorType, Set<ConnectorType>> connectionMap;
		
		private ConnectionTester(Map<ConnectorType, Set<ConnectorType>> connectionMap)
		{
			this.connectionMap = connectionMap;
		}
		
		public boolean canConnect(ConnectorType type, Set<ConnectorType> otherTypes)
		{
			Set<ConnectorType> supportedTypes = this.connectionMap.get(type);
			if(supportedTypes == null)
				return false;
			return otherTypes.stream().anyMatch(supportedTypes::contains);
		}
		
		public boolean isKnown(ConnectorType connectorType)
		{
			return this.connectionMap.containsKey(connectorType);
		}
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
			return new ConnectionTester(map);
		}
	}
	
	public static final class EntriesBuilder
	{
		private final WFC.PieceSize pieceSize;
		private final StructureTemplateManager templateManager;
		
		private final ConnectionsBuilder connectionsBuilder = new ConnectionsBuilder();
		private final ImmutableList.Builder<WeightedEntry.Wrapper<PieceEntry>> pieceEntries = ImmutableList.builder();
		
		public EntriesBuilder(WFC.PieceSize pieceSize, StructureTemplateManager templateManager)
		{
			this.pieceSize = pieceSize;
			this.templateManager = templateManager;
		}
		
		public ConnectionsBuilder connections()
		{
			return this.connectionsBuilder;
		}
		
		public void add(EntryProvider provider, int weight)
		{
			provider.build(new EntryBuilderContext(this.connectionsBuilder, pieceEntry -> this.pieceEntries.add(WeightedEntry.wrap(pieceEntry, weight)), this.pieceSize, this.templateManager));
		}
		
		public EntriesData build()
		{
			ImmutableList<WeightedEntry.Wrapper<PieceEntry>> entriesList = this.pieceEntries.build();
			ConnectionTester connectionTester = this.connectionsBuilder.buildConnectionTester();
			
			entriesList.stream().flatMap(entry -> entry.getData().connections.values().stream()).forEach(connector -> {
				if(!connectionTester.isKnown(connector))
					LOGGER.warn("Found unknown connector: {}", connector.id());
			});
			
			return new EntriesData(entriesList, connectionTester);
		}
	}
}
