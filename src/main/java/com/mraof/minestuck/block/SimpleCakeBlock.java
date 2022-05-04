package com.mraof.minestuck.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	protected void applyEffects(World worldIn, BlockPos pos, BlockState state, PlayerEntity player)
	{
		player.getFoodData().eat(foodLevel, saturation);
		if(effect != null)
			effect.applyEffects(player);
	}
	
	public interface Effect
	{
		void applyEffects(PlayerEntity player);
	}
}
