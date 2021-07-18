package com.mraof.minestuck.item;

import com.mraof.minestuck.client.util.GunEffect;
import com.mraof.minestuck.item.weapon.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BulletItem extends Item
{
	private final double accuracy;
	private final int damage;
	private final int distance;
	private final int penetratingPower;
	//@Nullable
	//private BulletItem.Type type;
	@Nullable
	private final Supplier<Item> specificGun;
	@Nullable
	private final Supplier<EffectInstance> effect;
	@Nullable
	private final int flame;
	@Nullable
	private final boolean ricochet;
	
	
	@Deprecated
	public BulletItem(Properties properties, double accuracy, int damage, int distance, int penetratingPower)
	{
		this(new BulletItem.Builder(accuracy, damage, distance, penetratingPower), properties);
	}
	
	public BulletItem(BulletItem.Builder builder, Properties properties)
	{
		super(properties);
		accuracy = builder.accuracy;
		damage = builder.damage;
		distance = builder.distance;
		penetratingPower = builder.penetratingPower;
		//type = builder.type;
		specificGun = builder.specificGun;
		effect = builder.effect;
		flame = builder.flame;
		ricochet = builder.ricochet;
	}
	
	/*public enum Type
	{
		DEFAULT(null, 0, false),
		URANIUM(() -> new EffectInstance(Effects.WITHER, 120, 2), 0, false),
		DRAGON(null, 5, false);
		
		private final Supplier<EffectInstance> effect;
		private final int flame;
		private final boolean ricochet;
		
		Type(Supplier<EffectInstance> effectIn, int flameIn, boolean ricochetIn)
		{
			this.effect = effectIn;
			this.flame = flameIn;
			this.ricochet = ricochetIn;
		}
	}*/
	
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
	
	public int getFlame()
	{
		return this.flame;
	}
	
	public boolean getRicochet()
	{
		return this.ricochet;
	}
	
	/*public BulletItem.Type getType()
	{
		if(this.type == null)
			return Type.DEFAULT;
		else
			return this.type;
	}*/
	
	public Supplier<Item> getSpecificGun()
	{
		return this.specificGun;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(new TranslationTextComponent("item.minestuck." + this + ".values", accuracy, damage, distance, penetratingPower));
	}
	
	public static class Builder
	{
		private final double accuracy;
		private final int damage;
		private final int distance;
		private final int penetratingPower;
		
		private Supplier<Item> specificGun;
		private Supplier<EffectInstance> effect;
		private int flame;
		private boolean ricochet;
		
		//@Nullable
		//BulletItem.Type type;
		
		public Builder(double accuracy, int damage, int distance, int penetratingPower)
		{
			this.accuracy = accuracy;
			this.damage = damage;
			this.distance = distance;
			this.penetratingPower = penetratingPower;
		}
		
		public BulletItem.Builder soundEffect(Supplier<SoundEvent> soundEffect)
		{
			//sound = soundEffect;
			return this;
		}
		
		public BulletItem.Builder needsSpecificGun(Supplier<Item> itemIn)
		{
			specificGun = itemIn;
			return this;
		}
		
		public BulletItem.Builder potionEffect(Supplier<EffectInstance> potionEffect)
		{
			effect = potionEffect;
			return this;
		}
		
		public BulletItem.Builder flame(int flameIn)
		{
			flame = flameIn;
			return this;
		}
		
		public BulletItem.Builder ricochets(boolean ricochetIn)
		{
			ricochet = ricochetIn;
			return this;
		}
		
		/*public BulletItem.Builder setType(BulletItem.Type typeIn)
		{
			type = typeIn;
			return this;
		}*/
	}
}