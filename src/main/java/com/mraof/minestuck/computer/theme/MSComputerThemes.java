package com.mraof.minestuck.computer.theme;

import com.mraof.minestuck.Minestuck;
import net.minecraft.resources.ResourceLocation;

/**
 * Ids for computer themes added by minestuck.
 */
public final class MSComputerThemes
{
	public static final ResourceLocation
			DEFAULT = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "default"),
			PESTERCHUM = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "pesterchum"),
			TROLLIAN = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "trollian"),
			CROCKER = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "crocker"),
			TYPHEUS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "typheus"),
			CETUS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "cetus"),
			HEPHAESTUS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "hephaestus"),
			ECHIDNA = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "echidna"),
			JOY = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "joy"),
			SBURB_95 = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "sburb_95"),
			SBURB_10 = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "sburb_10"),
			SCOURGING_HEAT = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "scourging_heat"),
			LIFDOFF = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "lifdoff"),
			SKAIANET_GREEN = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "skaianet_green"),
			SKAIANET_WHITE = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "skaianet_white"),
			SKAIANET_BLACK = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "skaianet_black"),
			ASTRAL_CHARTS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "astral_charts"),
			TILLDEATH = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "tilldeath"),
			LOWAS = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "lowas"),
			SPIROGRAPH = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "spirograph"),
			MINESTUCK = ResourceLocation.fromNamespaceAndPath(Minestuck.MOD_ID, "minestuck");
	
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
