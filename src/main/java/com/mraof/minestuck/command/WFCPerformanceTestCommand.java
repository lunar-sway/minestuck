package com.mraof.minestuck.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mraof.minestuck.world.gen.structure.wfc.ProspitStructure;
import com.mraof.minestuck.world.gen.structure.wfc.WFC;
import com.mraof.minestuck.world.gen.structure.wfc.WFCData;
import com.mraof.minestuck.world.gen.structure.wfc.WFCUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import javax.annotation.Nullable;

public final class WFCPerformanceTestCommand
{
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
	{
		dispatcher.register(Commands.literal("wfc_performance_test").requires(source -> source.hasPermission(Commands.LEVEL_OWNERS))
				.executes(context -> {
					run(context.getSource().getLevel().getStructureManager());
					return 1;
				}));
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
	
	private static void run(StructureTemplateManager templateManager)
	{
		WFCUtil.PerformanceMeasurer performanceMeasurer = new WFCUtil.PerformanceMeasurer();
		
		PositionalRandomFactory randomFactory = RandomSource.create(1L).forkPositional();
		WFCUtil.PositionTransform middleTransform = new WFCUtil.PositionTransform(BlockPos.ZERO, ProspitStructure.PIECE_SIZE);
		
		WFCData.EntryPalette centerPalette = ProspitStructure.buildCenterPalette(templateManager);
		WFCData.EntryPalette borderPalette = ProspitStructure.buildBorderPalette(templateManager);
		
		WFC.InfiniteModularGeneration.generateModule(middleTransform, ProspitStructure.WFC_DIMENSIONS,
				centerPalette, borderPalette, randomFactory, DUMMY_PIECE_ACCESSOR, performanceMeasurer);
		
		System.out.printf("Measured performance: %s%n", performanceMeasurer);
	}
}
