package com.mraof.minestuck.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BulletItem extends Item
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	private final int penetratingPower;
	@Nullable
	private BulletItem.Type type;
	
	public BulletItem(Properties properties, double accuracy, int damage, int distance, int penetratingPower, BulletItem.Type type)
	{
		super(properties);
		this.accuracy = accuracy;
		this.damage = damage;
		this.distance = distance;
		this.penetratingPower = penetratingPower;
		this.type = type;
	}
	
	public enum Type
	{
		DEFAULT(null, 0, false),
		URANIUM(() -> new EffectInstance(Effects.WITHER, 120, 2), 0, false),
		DRAGON(null, 5 , false);
		
		private final Supplier<EffectInstance> effect;
		private final int flame;
		private final boolean ricochet;
		
		Type(Supplier<EffectInstance> effectIn, int flameIn, boolean ricochetIn)
		{
			this.effect = effectIn;
			this.flame = flameIn;
			this.ricochet = ricochetIn;
		}
		
		public int toInt()
		{
			return ordinal();
		}
		
		public static BulletItem.Type fromInt(int i)
		{
			if(i >= 0 && i < values().length)
				return values()[i];
			else return DEFAULT;
		}
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
		return type.effect;
	}
	
	public int getFlame()
	{
		return type.flame;
	}
	
	public boolean getRicochet()
	{
		return type.ricochet;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".values", accuracy, damage, distance, penetratingPower));
	}
}