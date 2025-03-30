package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.Rung;
import com.mraof.minestuck.player.Rungs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RungsProvider implements DataProvider
{
	public static final ResourceLocation HEALTH_BOOST_ID = Minestuck.id("echeladder_health_boost");
	public static final ResourceLocation DAMAGE_BOOST_ID = Minestuck.id("echeladder_damage_boost");
	public static final ResourceLocation UNDERLING_PROTECTION_ID = Minestuck.id("echeladder_underling_protection");
	public static final ResourceLocation UNDERLING_DAMAGE_ID = Minestuck.id("echeladder_underling_damage");
	public static final ResourceLocation FALL_DAMAGE_REDUCTION_ID = Minestuck.id("echeladder_fall_damage_reduction");
	private final PackOutput output;
	
	public RungsProvider(PackOutput output)
	{
		this.output = output;
	}
	
	private final List<Rung> rungs = new ArrayList<>();
	private int rungIterate = 0;
	
	protected void createRungs()
	{
		//Grist capacity is achieved by multiplying the previous by 1.25 and then rounding the result down to get an integer number
		add(0xFF4FD400, 0xFDFF2B, 0, 60); //0
		add(0xFFFF0000, 0x404040, 50, 75);
		add(0xFF956C4C, 0xB6FF00, 75, 93);
		add(0xFF7DB037, 0x775716, 105, 116);
		add(0xFFD8A600, 0xFFFFFF, 140, 145);
		add(0xFF7F0000, 0xFF6A00, 170, 181); //5
		add(0xFF007F0E, 0x0094FF, 200, 226);
		add(0xFF808080, 0x3F3F3F, 250, 282);
		add(0xFF00FF21, 0x007F7F, 320, 352);
		add(0xFF4800FF, 0xB200FF, 425, 440);
		add(0xFF404040, 0x7B9CB5, 575, 550); //10
		add(0xFFE4FF00, 0x6D9A00, 790, 687);
		add(0xFFDFBB6C, 0x219621, 1140, 858);
		add(0xFFCECECE, 0x7F743F, 1630, 1072);
		add(0xFFFF0000, 0xFF7F7F, 2230, 1340);
		add(0xFFC68E4D, 0xAF0A8C, 2980, 1675); //15
		add(0xFF60E554, 0x2A9659, 3850, 2093);
		add(0xFF88CE88, 0xFFD8F2, 4800, 2616);
		add(0xFF006EBC, 0xFFFFFF, 6000, 3270);
		add(0xFFF12B26, 0xDAFF7F, 7500, 4087);
		add(0xFFC11000, 0x3459BC, 9500, 5108); //20
		add(0xFFBA8B34, 0xDFE868, 11900, 6385);
		add(0xFF5134A8, 0x2AA3D3, 15200, 7981);
		add(0xFF92CC00, 0x4C4C4C, 19300, 9976);
		add(0xFF93613B, 0x00D318, 24400, 12470);
		add(0xFF111121, 0x6F22A5, 45000, 15587); //25
		add(0xFFD61B28, 0xC4AA29, 68000, 19483);
		add(0xFFEF8181, 0x237C00, 95500, 24353);
		add(0xFFED5C1A, 0x2D2D2D, 124000, 30441);
		add(0xFFDBDBDB, 0xFF6721, 180000, 38051);
		add(0xFFEFC300, 0x8487E0, 260000, 47563); //30
		add(0xFF3529A5, 0x000000, 425000, 59453);
		add(0xFF634021, 0xADADAD, 632000, 74316);
		add(0xFFBCBCBC, 0xE24400, 880000, 92895);
		add(0xFFBA1500, 0xE27609, 1000000, 116118);
		add(0xFF42A3B5, 0x0A08A0, 1000000, 145147); //35
		add(0xFF3C6354, 0xC6A623, 1000000, 181433);
		add(0xFFC4B681, 0x38C151, 1000000, 226791);
		add(0xFF969696, 0xF9A640, 1000000, 283488);
		add(0xFF6B6B6B, 0x368E4A, 1000000, 354360);
		add(0xFFAD1BA3, 0xFFFFFF, 1000000, 442950); //40
		add(0xFF0021FF, 0x00A1C1, 1000000, 553687);
		add(0xFF000000, 0x6B699E, 1000000, 692108);
		add(0xFF294F9B, 0x2D2D2D, 1000000, 865135);
		add(0xFFADA87B, 0x18117A, 1000000, 1081418);
		add(0xFF439E35, 0xFF9028, 1000000, 1351772); //45
		add(0xFF8E583E, 0x1F7C8E, 1000000, 1689715);
		add(0xFF606060, 0xE25012, 1000000, 2112143);
		add(0xFFDDC852, 0x9721E0, 1000000, 2640178);
		add(0xFFFFFFFF, 0x000000, 1000000, 3300222); //49
	}
	
	private void add(int backgroundColor, int textColor, long boondollars, long gristCapacity)
	{
		rungs.add(new Rung(rungIterate, backgroundColor, textColor, rungRequirement(rungIterate), boondollars, gristCapacity));
		rungIterate++;
	}
	
	private static long rungRequirement(int rung)
	{
		return 15L * rung + 10;
	}
	
	@Override
	public final CompletableFuture<?> run(CachedOutput cache)
	{
		createRungs();
		List<CompletableFuture<?>> futures = new ArrayList<>(rungs.size());
		
		Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(Minestuck.MOD_ID).resolve(Rungs.PATH);
		
		JsonElement jsonElement = Rung.LIST_CODEC.encodeStart(JsonOps.INSTANCE, rungs).getOrThrow();
		
		futures.add(DataProvider.saveStable(cache, jsonElement, path));
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	@Override
	public String getName()
	{
		return "Minestuck Rungs";
	}
}
