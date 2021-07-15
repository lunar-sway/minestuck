package com.mraof.minestuck.item;

import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

import java.util.function.Supplier;

public class BulletItem extends Item	//TODO Add custom crafting recipe that merges boondollar stacks
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	private final int penetratingPower;
	private final Supplier<EffectInstance> effect;
	
	public BulletItem(Properties properties, double accuracy, int damage, int distance, int penetratingPower, Supplier<EffectInstance> effect)
	{
		super(properties);
		this.accuracy = accuracy;
		this.damage = damage;
		this.distance = distance;
		this.penetratingPower = penetratingPower;
		this.effect = effect;
	}
	
	public double getAccuracy()
	{
		return this.accuracy;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	public int getDistance()
	{
		return this.distance;
	}
	
	public int getPenetratingPower()
	{
		return this.penetratingPower;
	}
	
	public Supplier<EffectInstance> getEffect()
	{
		return this.effect;
	}
}