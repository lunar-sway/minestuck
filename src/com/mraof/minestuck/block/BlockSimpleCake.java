package com.mraof.minestuck.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockSimpleCake extends BlockCustomCake
{
	private final int foodLevel;
	private final float saturation;
	private final Effect effect;
	
	public BlockSimpleCake(Properties builder, int foodLevel, float saturation, Effect effect)
	{
		super(builder);
		this.foodLevel = foodLevel;
		this.saturation = saturation;
		this.effect = effect;
	}
	
	@Override
	protected void applyEffects(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		player.getFoodStats().addStats(foodLevel, saturation);
		if(effect != null)
			effect.applyEffects(player);
	}
	
	public interface Effect
	{
		void applyEffects(EntityPlayer player);
	}
}
