package com.mraof.minestuck.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleCakeBlock extends CustomCakeBlock
{
	private final int foodLevel;
	private final float saturation;
	private final Effect effect;
	
	public SimpleCakeBlock(Properties builder, int foodLevel, float saturation, Effect effect)
	{
		super(builder);
		this.foodLevel = foodLevel;
		this.saturation = saturation;
		this.effect = effect;
	}
	
	@Override
	protected void applyEffects(Level level, BlockPos pos, BlockState state, Player player)
	{
		player.getFoodData().eat(foodLevel, saturation);
		if(effect != null)
			effect.applyEffects(player);
	}
	
	public interface Effect
	{
		void applyEffects(Player player);
	}
}
