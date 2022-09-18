package com.mraof.minestuck.entity.consort;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.Random;
import java.util.function.Supplier;

public enum EnumConsort	//TODO Could ideally be changed into a registry.
{
	SALAMANDER(MSEntityTypes.SALAMANDER, "salamander", ChatFormatting.YELLOW, () -> MSSoundEvents.ENTITY_SALAMANDER_AMBIENT, () -> MSSoundEvents.ENTITY_SALAMANDER_HURT, () -> MSSoundEvents.ENTITY_SALAMANDER_DEATH),
	TURTLE(MSEntityTypes.TURTLE, "turtle", ChatFormatting.LIGHT_PURPLE, () -> null, () -> MSSoundEvents.ENTITY_TURTLE_HURT, () -> MSSoundEvents.ENTITY_TURTLE_DEATH),
	NAKAGATOR(MSEntityTypes.NAKAGATOR, "nakagator", ChatFormatting.RED, () -> MSSoundEvents.ENTITY_NAKAGATOR_AMBIENT, () -> MSSoundEvents.ENTITY_NAKAGATOR_HURT, () -> MSSoundEvents.ENTITY_NAKAGATOR_DEATH),
	IGUANA(MSEntityTypes.IGUANA, "iguana", ChatFormatting.AQUA, () -> MSSoundEvents.ENTITY_IGUANA_AMBIENT, () -> MSSoundEvents.ENTITY_IGUANA_HURT, () -> MSSoundEvents.ENTITY_IGUANA_DEATH);
	
	private final Supplier<? extends EntityType<? extends ConsortEntity>> consortType;
	private final String name;
	private final ChatFormatting color;
	private final Supplier<SoundEvent> ambientSound, hurtSound, deathSound;
	
	EnumConsort(Supplier<? extends EntityType<? extends ConsortEntity>> consort, String name, ChatFormatting color,
				Supplier<SoundEvent> ambientSound, Supplier<SoundEvent> hurtSound, Supplier<SoundEvent> deathSound)
	{
		consortType = consort;
		this.color = color;
		this.name = name;
		this.ambientSound = ambientSound;
		this.hurtSound = hurtSound;
		this.deathSound = deathSound;
	}
	
	public boolean isConsort(Entity consort)
	{
		return consortType.get().equals(consort.getType());
	}
	
	public ChatFormatting getColor()
	{
		return color;
	}
	
	public String getName()
	{
		return name;
	}
	
	public EntityType<? extends ConsortEntity> getConsortType()
	{
		return consortType.get();
	}
	
	public SoundEvent getAmbientSound()
	{
		return ambientSound.get();
	}
	
	public SoundEvent getHurtSound()
	{
		return hurtSound.get();
	}
	
	public SoundEvent getDeathSound()
	{
		return deathSound.get();
	}
	
	public ConsortEntity create(EntityType<? extends ConsortEntity> type, Level level)
	{
		return new ConsortEntity(this, type, level);
	}
	
	public static MerchantType getRandomMerchant(Random rand)
	{
		float f = rand.nextFloat();
		if(f < 0.4f)
			return MerchantType.FOOD;
		else return MerchantType.GENERAL;
	}
	
	public static EnumConsort getFromName(String str)
	{
		for(EnumConsort type : EnumConsort.values())
			if(type.getName().equals(str))
				return type;
		throw new IllegalArgumentException("Invalid consort type " + str);
	}
	
	public enum MerchantType
	{
		NONE,
		SHADY,
		FOOD,
		GENERAL,
		;
		
		public String getName()
		{
			return name().toLowerCase();
		}
		
		public static MerchantType getFromName(String str)
		{
			for(MerchantType type : MerchantType.values())
				if(type.name().toLowerCase().equals(str))
					return type;
			throw new IllegalArgumentException("Invalid merchant type " + str);
		}
	}
}