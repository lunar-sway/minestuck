package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mraof.minestuck.world.gen.structure.wfc.ProspitStructure;
import com.mraof.minestuck.world.gen.structure.wfc.WFC;
import com.mraof.minestuck.world.gen.structure.wfc.WFCData;
import com.mraof.minestuck.world.gen.structure.wfc.WFCUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import javax.annotation.Nullable;

@EventBusSubscriber
public final class WFCPerformanceTestCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("wfc_performance_test").requires(source -> source.hasPermission(Commands.LEVEL_OWNERS))
				.then(Commands.argument("times", IntegerArgumentType.integer(1))
						.executes(context -> {
							run(IntegerArgumentType.getInteger(context, "times"), context.getSource().getLevel().getStructureManager());
							return 1;
						}))
				.executes(context -> {
					run(context.getSource().getServer().registryAccess(), context.getSource().getLevel().getStructureManager());
					return 1;
				}));
	}
	
	private static void run(int times, StructureTemplateManager templateManager)
	{
		timesToRun = times;
	}
	
	private static int timesToRun = 0;
	
	@SubscribeEvent
	private static void onTick(ServerTickEvent.Post event)
	{
		if(timesToRun > 0 && event.hasTime() && event.getServer().getTickCount() % 40 == 0)
		{
			timesToRun--;
			run(event.getServer().registryAccess(), event.getServer().getStructureManager());
		}
	}
	
	private static final StructurePieceAccessor DUMMY_PIECE_ACCESSOR = new StructurePieceAccessor()
	{
		@Override
		public void addPiece(StructurePiece piece)
		{
		}
		
		@Nullable
		@Override
		public StructurePiece findCollisionPiece(BoundingBox box)
		{
			return null;
		}
	};
	
	private static void run(RegistryAccess registryAccess, StructureTemplateManager templateManager)
	{
		HolderGetter<WFCData.PaletteData> paletteLookup = registryAccess.lookupOrThrow(WFCData.PaletteData.REGISTRY_KEY);
		
		WFCUtil.PerformanceMeasurer performanceMeasurer = new WFCUtil.PerformanceMeasurer();
		
		PositionalRandomFactory randomFactory = RandomSource.create(1L).forkPositional();
		WFCUtil.PositionTransform middleTransform = new WFCUtil.PositionTransform(BlockPos.ZERO, ProspitStructure.CELL_SIZE);
		
		WFCData.EntryPalette centerPalette = paletteLookup.getOrThrow(ProspitStructure.Palettes.NORMAL).value().build(ProspitStructure.CELL_SIZE, templateManager);
		WFCData.EntryPalette borderPalette = paletteLookup.getOrThrow(ProspitStructure.Palettes.BORDER).value().build(ProspitStructure.CELL_SIZE, templateManager);
		
		WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitStructure.WFC_DIMENSIONS,
				centerPalette, borderPalette, randomFactory, DUMMY_PIECE_ACCESSOR, performanceMeasurer);
		
		System.out.printf("Measured performance: %s%n", performanceMeasurer);
	}
}
