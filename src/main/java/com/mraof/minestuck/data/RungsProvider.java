package com.mraof.minestuck.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.player.EnumAspect;
import com.mraof.minestuck.player.Rung;
import com.mraof.minestuck.player.Rungs;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.mraof.minestuck.player.EnumAspect.*;
import static net.minecraft.world.effect.MobEffects.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RungsProvider implements DataProvider
{
	public static final ResourceLocation HEALTH_BOOST_ID = Minestuck.id("echeladder_health_boost");
	public static final ResourceLocation DAMAGE_BOOST_ID = Minestuck.id("echeladder_damage_boost");
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
		//At max rung, the player will have three rows of hearts and 200% damage
		add(0, 60,
				new Rung.EcheladderAttribute(Attributes.MAX_HEALTH, HEALTH_BOOST_ID, 0.8163265306D, 0, AttributeModifier.Operation.ADD_VALUE),
				new Rung.EcheladderAttribute(Attributes.ATTACK_DAMAGE, DAMAGE_BOOST_ID, 0.02040816326530612D, 0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)); //0
		add(50, 75);
		add(75, 93);
		add(105, 116);
		add(140, 145);
		add(170, 181); //5
		add(200, 226);
		add(250, 282);
		add(320, 352);
		add(425, 440);
		add(575, 550,
				addEffect(LIGHT, LUCK, 0),
				addEffect(SPACE, JUMP, 0)); //10
		add(790, 687);
		add(1140, 858,
				addEffect(MIND, NIGHT_VISION, 0),
				addEffect(VOID, INVISIBILITY, 0));
		add(1630, 1072, addEffect(TIME, DIG_SPEED, 0));
		add(2230, 1340,
				addEffect(BLOOD, ABSORPTION, 0),
				addEffect(HEART, ABSORPTION, 0));
		add(2980, 1675, addEffect(BREATH, MOVEMENT_SPEED, 0)); //15
		add(3850, 2093);
		add(4800, 2616);
		add(6000, 3270, addEffect(HOPE, FIRE_RESISTANCE, 0));
		add(7500, 4087);
		add(9500, 5108,
				addEffect(LIFE, REGENERATION, 0),
				addEffect(LIGHT, LUCK, 1),
				addEffect(SPACE, JUMP, 1)); //20
		add(11900, 6385);
		add(15200, 7981);
		add(19300, 9976);
		add(24400, 12470,
				addEffect(MIND, NIGHT_VISION, 1),
				addEffect(VOID, INVISIBILITY, 1));
		add(45000, 15587,
				addEffect(HOPE, WATER_BREATHING, 0),
				addEffect(RAGE, DAMAGE_BOOST, 0)); //25
		add(68000, 19483, addEffect(TIME, DIG_SPEED, 1));
		add(95500, 24353);
		add(124000, 30441,
				addEffect(BLOOD, ABSORPTION, 1),
				addEffect(DOOM, DAMAGE_RESISTANCE, 0),
				addEffect(HEART, ABSORPTION, 0));
		add(180000, 38051);
		add(260000, 47563,
				addEffect(BREATH, MOVEMENT_SPEED, 1),
				addEffect(LIGHT, LUCK, 2),
				addEffect(SPACE, JUMP, 2)); //30
		add(425000, 59453);
		add(632000, 74316);
		add(880000, 92895);
		add(1000000, 116118);
		add(1000000, 145147); //35
		add(1000000, 181433,
				addEffect(HOPE, FIRE_RESISTANCE, 1),
				addEffect(MIND, NIGHT_VISION, 2),
				addEffect(VOID, INVISIBILITY, 2));
		add(1000000, 226791);
		add(1000000, 283488);
		add(1000000, 354360);
		add(1000000, 442950,
				addEffect(LIFE, REGENERATION, 1),
				addEffect(LIGHT, LUCK, 3),
				addEffect(SPACE, JUMP, 3)); //40
		add(1000000, 553687);
		add(1000000, 692108,
				addEffect(BLOOD, ABSORPTION, 2),
				addEffect(HEART, ABSORPTION, 1));
		add(1000000, 865135);
		add(1000000, 1081418);
		add(1000000, 1351772, addEffect(BREATH, MOVEMENT_SPEED, 2)); //45
		add(1000000, 1689715);
		add(1000000, 2112143);
		add(1000000, 2640178,
				addEffect(MIND, NIGHT_VISION, 3),
				addEffect(VOID, INVISIBILITY, 3));
		add(1000000, 3300222); //49
	}
	
	private void add(long boondollars, long gristCapacity)
	{
		rungs.add(new Rung(rungIterate, rungRequirement(rungIterate), boondollars, gristCapacity, List.of(), List.of()));
		rungIterate++;
	}
	
	private void add(long boondollars, long gristCapacity, Rung.AspectEffect... aspectEffects)
	{
		rungs.add(new Rung(rungIterate, rungRequirement(rungIterate), boondollars, gristCapacity, List.of(aspectEffects), List.of()));
		rungIterate++;
	}
	
	private void add(long boondollars, long gristCapacity, Rung.EcheladderAttribute... attributes)
	{
		rungs.add(new Rung(rungIterate, rungRequirement(rungIterate), boondollars, gristCapacity, List.of(), List.of(attributes)));
		rungIterate++;
	}
	
	private static long rungRequirement(int rung)
	{
		return 15L * rung + 10;
	}
	
	private Rung.AspectEffect addEffect(EnumAspect aspect, Holder<MobEffect> effectHolder, int strength)
	{
		return new Rung.AspectEffect(aspect, effectHolder, strength);
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
