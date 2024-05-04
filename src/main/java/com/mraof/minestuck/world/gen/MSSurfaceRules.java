package com.mraof.minestuck.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.Minestuck;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.List;

@MethodsReturnNonnullByDefault
public final class MSSurfaceRules extends SurfaceRules	//This extension is here only for access to the protected interface SurfaceRule
{
	public static final DeferredRegister<Codec<? extends SurfaceRules.RuleSource>> REGISTER = DeferredRegister.create(Registries.MATERIAL_RULE, Minestuck.MOD_ID);
	
	static {
		REGISTER.register("checkered", CheckeredRuleSource.CODEC::codec);
	}
	
	public record CheckeredRuleSource(int squareSize, List<RuleSource> rules) implements RuleSource
	{
		private static final KeyDispatchDataCodec<CheckeredRuleSource> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.create(instance -> instance.group(
				Codec.intRange(1, Integer.MAX_VALUE).fieldOf("size").forGetter(CheckeredRuleSource::squareSize),
				SurfaceRules.RuleSource.CODEC.listOf().fieldOf("rules").forGetter(CheckeredRuleSource::rules)
		).apply(instance, CheckeredRuleSource::new)));
		
		@Override
		public KeyDispatchDataCodec<CheckeredRuleSource> codec()
		{
			return CODEC;
		}
		
		@Override
		public SurfaceRule apply(Context context)
		{
			if(this.rules.size() == 1)
				return this.rules.get(0).apply(context);
			
			return new CheckeredSurfaceRule(this.squareSize, this.rules.stream().map(ruleSource -> ruleSource.apply(context)).toList());
		}
	}
	
	private record CheckeredSurfaceRule(int squareSize, List<SurfaceRule> rules) implements SurfaceRule
	{
		@Nullable
		@Override
		public BlockState tryApply(int x, int y, int z)
		{
			int size = this.rules.size();
			if(size == 0)
				return null;
			
			int squareX = Math.floorDiv(x, this.squareSize), squareZ = Math.floorDiv(z, this.squareSize);
			
			int ruleIndex = Math.floorMod(squareX - squareZ, size);
			
			return this.rules.get(ruleIndex).tryApply(x, y, z);
		}
	}
}
