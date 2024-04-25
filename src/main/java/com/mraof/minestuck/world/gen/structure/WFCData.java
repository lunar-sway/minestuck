package com.mraof.minestuck.world.gen.structure;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
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
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Contains types for describing pieces and how they connect, as well as builders for defining data of these types.
 */
public final class WFCData
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static Map<Direction, ConnectorType> rotateConnectors(Map<Direction, ConnectorType> connections, Rotation rotation)
	{
		if(rotation == Rotation.NONE)
			return Map.copyOf(connections);
		
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
	}
	
	public record PieceEntry(Function<BlockPos, StructurePiece> constructor,
							 Map<Direction, ConnectorType> connections) implements EntryProvider
	{
		@Override
		public void build(EntryBuilderContext context)
		{
			context.entriesBuilder().add(this);
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
			StructureTemplate template = context.templateManager().getOrCreate(this.templateId);
			
			TemplateEntryBuilder
					.init(this.templateId, template.getSize(), context.pieceSize())
					.flatMap(builder -> {
						loadConnectorsFromJigsaws(this.templateId, template, context.pieceSize(), builder);
						return builder.verify();
					})
					.ifPresent(loadedEntry -> {
						for(Rotation rotation : this.rotations)
							loadedEntry.build(rotation, context);
					});
		}
		
		private static void loadConnectorsFromJigsaws(ResourceLocation templateId, StructureTemplate template, WFC.PieceSize pieceSize, TemplateEntryBuilder builder)
		{
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
				WFC.PiecePos pos = new WFC.PiecePos(blockInfo.pos().getX() / pieceSize.width(),
						blockInfo.pos().getY() / pieceSize.height(), blockInfo.pos().getZ() / pieceSize.width());
				Direction direction = blockInfo.state().getValue(JigsawBlock.ORIENTATION).front();
				
				builder.put(pos, direction, connectorType);
			}
		}
	}
	
	private static final class TemplateEntryBuilder
	{
		private final ResourceLocation templateId;
		private final WFC.Dimensions templateDimensions;
		
		final Map<WFC.PiecePos, Map<Direction, ConnectorType>> connectors = new HashMap<>();
		boolean failed = false;
		
		private TemplateEntryBuilder(ResourceLocation templateId, WFC.Dimensions templateDimensions)
		{
			this.templateId = templateId;
			this.templateDimensions = templateDimensions;
		}
		
		static Optional<TemplateEntryBuilder> init(ResourceLocation templateId, Vec3i templateSize, WFC.PieceSize pieceSize)
		{
			if(templateSize.getX() % pieceSize.width() != 0 || templateSize.getY() % pieceSize.height() != 0 || templateSize.getZ() % pieceSize.width() != 0)
			{
				LOGGER.error("Template {} of size {} is not a multiple of {}", templateId, templateSize, pieceSize);
				return Optional.empty();
			}
			WFC.Dimensions dimensions = new WFC.Dimensions(templateSize.getX() / pieceSize.width(),
					templateSize.getY() / pieceSize.height(), templateSize.getZ() / pieceSize.width());
			return Optional.of(new TemplateEntryBuilder(templateId, dimensions));
		}
		
		void put(WFC.PiecePos pos, Direction direction, ConnectorType connectorType)
		{
			if(!this.templateDimensions.isOnEdge(pos, direction))
			{
				LOGGER.warn("Connector {} in template {} is not at the edge", connectorType, this.templateId);
				return;
			}
			
			if(this.connectors.computeIfAbsent(pos, ignored -> new HashMap<>()).put(direction, connectorType) != null)
			{
				LOGGER.error("Template {} has multiple jigsaws for the same side ({})", this.templateId, direction);
				this.failed = false;
			}
		}
		
		Optional<LoadedTemplateEntry> verify()
		{
			List<Direction> missingDirections = Arrays.stream(Direction.values()).filter(this::isConnectorMissingOnSide).toList();
			if(!missingDirections.isEmpty())
			{
				LOGGER.error("Template {} is missing connector type for the following sides: {}", this.templateId, missingDirections);
				return Optional.empty();
			}
			
			if(this.failed)
				return Optional.empty();
			
			return Optional.of(new LoadedTemplateEntry(this.templateId, this.templateDimensions,
					this.connectors.entrySet().stream().map(entry -> Map.entry(entry.getKey(), Map.copyOf(entry.getValue())))
							.collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, Map.Entry::getValue))));
		}
		
		private boolean isConnectorMissingOnSide(Direction direction)
		{
			return Streams.stream(this.templateDimensions.iterateEdge(direction))
					.anyMatch(pos -> !this.connectors.getOrDefault(pos, Collections.emptyMap()).containsKey(direction));
		}
	}
	
	private record LoadedTemplateEntry(ResourceLocation templateId, WFC.Dimensions templateDimensions, Map<WFC.PiecePos, Map<Direction, ConnectorType>> edgeConnectors)
	{
		void build(Rotation rotation, EntryBuilderContext context)
		{
			Map<Direction.Axis, Map<WFC.PiecePos, ConnectorType>> connectors = new EnumMap<>(Direction.Axis.class);
			
			BiFunction<Direction.Axis, WFC.PiecePos, ConnectorType> innerConnectorGetter = (axis, piecePos) ->
					connectors.computeIfAbsent(axis, ignored -> new HashMap<>())
							.computeIfAbsent(piecePos, ignored -> addNewConnector(piecePos, axis, rotation, context.connectionsBuilder()));
			
			for(WFC.PiecePos piecePos : this.templateDimensions.iterateAll())
			{
				Function<BlockPos, StructurePiece> constructor;
				if(piecePos.x() == 0 && piecePos.y() == 0 && piecePos.z() == 0)
					constructor = templateConstructor(context.pieceSize(), rotation, context.templateManager());
				else
					constructor = pos -> null;
				
				Map<Direction, ConnectorType> entryConnectors = Map.of(
						Direction.DOWN, templateDimensions.isOnEdge(piecePos, Direction.DOWN)
								? edgeConnectors().get(piecePos).get(Direction.DOWN)
								: innerConnectorGetter.apply(Direction.Axis.Y, new WFC.PiecePos(piecePos.x(), piecePos.y() - 1, piecePos.z())),
						Direction.UP, templateDimensions.isOnEdge(piecePos, Direction.UP)
								? edgeConnectors().get(piecePos).get(Direction.UP)
								: innerConnectorGetter.apply(Direction.Axis.Y, piecePos),
						Direction.NORTH, templateDimensions.isOnEdge(piecePos, Direction.NORTH)
								? edgeConnectors().get(piecePos).get(Direction.NORTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, new WFC.PiecePos(piecePos.x(), piecePos.y(), piecePos.z() - 1)),
						Direction.EAST, templateDimensions.isOnEdge(piecePos, Direction.EAST)
								? edgeConnectors().get(piecePos).get(Direction.EAST)
								: innerConnectorGetter.apply(Direction.Axis.X, piecePos),
						Direction.SOUTH, templateDimensions.isOnEdge(piecePos, Direction.SOUTH)
								? edgeConnectors().get(piecePos).get(Direction.SOUTH)
								: innerConnectorGetter.apply(Direction.Axis.Z, piecePos),
						Direction.WEST, templateDimensions.isOnEdge(piecePos, Direction.WEST)
								? edgeConnectors().get(piecePos).get(Direction.WEST)
								: innerConnectorGetter.apply(Direction.Axis.X, new WFC.PiecePos(piecePos.x() - 1, piecePos.y(), piecePos.z()))
				);
				
				PieceEntry entry = new PieceEntry(constructor, rotateConnectors(entryConnectors, rotation));
				context.entriesBuilder().add(entry);
			}
		}
		
		private ConnectorType addNewConnector(WFC.PiecePos pos, Direction.Axis axis, Rotation rotation, ConnectionsBuilder builder)
		{
			ConnectorType newConnector = new ConnectorType(templateId.withSuffix("/" + pos.x() + "_" + pos.y() + "_" + pos.z()
					+ "_" + axis.getSerializedName() + "_" + rotation.getSerializedName()));
			builder.connectSelf(newConnector);
			return newConnector;
		}
		
		private Function<BlockPos, StructurePiece> templateConstructor(WFC.PieceSize pieceSize, Rotation rotation, StructureTemplateManager templateManager)
		{
			BlockPos zeroPos = StructureTemplate.getZeroPositionWithTransform(BlockPos.ZERO, Mirror.NONE, rotation, pieceSize.width(), pieceSize.width());
			return pos -> new SimpleTemplatePiece(templateManager, this.templateId, pos.offset(zeroPos), rotation);
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
