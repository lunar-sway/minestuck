package com.mraof.minestuck.computer.theme;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;

/**
 * Ids for computer themes added by minestuck.
 */
public final class MSComputerThemes
{
	public static final ResourceLocation
			DEFAULT = new ResourceLocation(Minestuck.MOD_ID, "default"),
			PESTERCHUM = new ResourceLocation(Minestuck.MOD_ID, "pesterchum"),
			TROLLIAN = new ResourceLocation(Minestuck.MOD_ID, "trollian"),
			CROCKER = new ResourceLocation(Minestuck.MOD_ID, "crocker"),
			TYPHEUS = new ResourceLocation(Minestuck.MOD_ID, "typheus"),
			CETUS = new ResourceLocation(Minestuck.MOD_ID, "cetus"),
			HEPHAESTUS = new ResourceLocation(Minestuck.MOD_ID, "hephaestus"),
			ECHIDNA = new ResourceLocation(Minestuck.MOD_ID, "echidna"),
			JOY = new ResourceLocation(Minestuck.MOD_ID, "joy"),
			SBURB_95 = new ResourceLocation(Minestuck.MOD_ID, "sburb_95"),
			SBURB_10 = new ResourceLocation(Minestuck.MOD_ID, "sburb_10"),
			SCOURGING_HEAT = new ResourceLocation(Minestuck.MOD_ID, "scourging_heat"),
			LIFDOFF = new ResourceLocation(Minestuck.MOD_ID, "lifdoff"),
			SKAIANET_GREEN = new ResourceLocation(Minestuck.MOD_ID, "skaianet_green"),
			SKAIANET_WHITE = new ResourceLocation(Minestuck.MOD_ID, "skaianet_white"),
			SKAIANET_BLACK = new ResourceLocation(Minestuck.MOD_ID, "skaianet_black"),
			ASTRAL_CHARTS = new ResourceLocation(Minestuck.MOD_ID, "astral_charts"),
			TILLDEATH = new ResourceLocation(Minestuck.MOD_ID, "tilldeath"),
			LOWAS = new ResourceLocation(Minestuck.MOD_ID, "lowas"),
			SPIROGRAPH = new ResourceLocation(Minestuck.MOD_ID, "spirograph"),
			MINESTUCK = new ResourceLocation(Minestuck.MOD_ID, "minestuck");
	
	/**
	 * Used for backwards-compatibility with Minestuck-1.20.1-1.11.2.0 and earlier
	 */
	public static ResourceLocation getThemeFromOldOrdinal(int ordinal)
	{
		return switch(ordinal)
		{
			case 1 -> PESTERCHUM;
			case 2 -> TROLLIAN;
			case 3 -> CROCKER;
			case 4 -> TYPHEUS;
			case 5 -> CETUS;
			case 6 -> HEPHAESTUS;
			case 7 -> ECHIDNA;
			case 8 -> JOY;
			case 9 -> SBURB_95;
			default -> DEFAULT;
		};
	}
}
