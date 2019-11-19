package com.mraof.minestuck.world.gen.feature;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.world.gen.feature.structure.SmallRuinPiece;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public final class MSStructurePieces
{
	public static IStructurePieceType SMALL_RUIN;
	
	/**
	 * Should only be called by {@link com.mraof.minestuck.world.gen.feature.MSFeatures} on feature registry.
	 */
	static void init()
	{
		SMALL_RUIN = register(SmallRuinPiece::new, Minestuck.MOD_ID+":small_ruin");
	}
	
	private static IStructurePieceType register(IStructurePieceType type, String name) {
		return Registry.register(Registry.STRUCTURE_PIECE, name, type);
	}
}