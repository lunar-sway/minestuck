package com.mraof.minestuck.block;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class CustomOreBlock extends OreBlock
{
	private final int minExp, maxExp;
	
	public CustomOreBlock(Properties properties)
	{
		this(0, 0, properties);
	}
	
	public CustomOreBlock(int minExp, int maxExp, Properties properties)
	{
		super(properties);
		this.minExp = minExp;
		this.maxExp = maxExp;
	}
	
	@Override
	protected int getExperience(Random random)
	{
		return MathHelper.nextInt(random, minExp, maxExp);
	}
}