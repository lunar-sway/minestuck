package com.mraof.minestuck.player;

import com.mraof.minestuck.MinestuckConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

import static com.mraof.minestuck.player.EnumAspect.HOPE;

public final class Title
{
	public static final String FORMAT = "title.format";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final MobEffect[] aspectEffects = {MobEffects.ABSORPTION, MobEffects.MOVEMENT_SPEED, MobEffects.DAMAGE_RESISTANCE, MobEffects.ABSORPTION, MobEffects.FIRE_RESISTANCE, MobEffects.REGENERATION, MobEffects.LUCK, MobEffects.NIGHT_VISION, MobEffects.DAMAGE_BOOST, MobEffects.JUMP, MobEffects.DIG_SPEED, MobEffects.INVISIBILITY }; //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
	// Increase the starting rungs
	private static final float[] aspectStrength = new float[] {1.0F/14, 1.0F/15, 1.0F/28, 1.0F/14, 1.0F/18, 1.0F/20, 1.0F/10, 1.0F/12, 1.0F/25, 1.0F/10, 1.0F/13, 1.0F/12}; //Absorption, Speed, Resistance, Saturation, Fire Resistance, Regeneration, Luck, Night Vision, Strength, Jump Boost, Haste, Invisibility
	
	
	private final EnumClass heroClass;
	private final EnumAspect heroAspect;

	public Title(EnumClass heroClass, EnumAspect heroAspect)
	{
		this.heroClass = Objects.requireNonNull(heroClass);
		this.heroAspect = Objects.requireNonNull(heroAspect);
	}
	
	public EnumClass getHeroClass()
	{
		return this.heroClass;
	}
	
	public EnumAspect getHeroAspect()
	{
		return this.heroAspect;
	}
	
	public void handleAspectEffects(ServerPlayer player)
	{
		if(!MinestuckConfig.SERVER.aspectEffects.get())
			return;
		PlayerData data = PlayerSavedData.getData(player);
		if(data.effectToggle())
		{
			int rung = data.getEcheladder().getRung();
			EnumAspect aspect = data.getTitle().getHeroAspect();
			int potionLevel = (int) (aspectStrength[aspect.ordinal()] * rung); //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
			
			if(player.getCommandSenderWorld().getGameTime() % 380 == 0)
			{
				if(rung > 18 && aspect == HOPE)
				{
					player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600, 0));
				}
				
				if(potionLevel > 0)
				{
					player.addEffect(new MobEffectInstance(aspectEffects[aspect.ordinal()], 600, potionLevel - 1));
					LOGGER.debug("Applied aspect potion effect to {}", player.getDisplayName().getString());
				}
			}
		}
	}
	
	@Override
	public String toString()
	{
		return heroClass.toString() + " of " + heroAspect.toString();
	}
	
	public Component asTextComponent()
	{
		return Component.translatable(FORMAT, heroClass.asTextComponent(), heroAspect.asTextComponent());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Title title)
		{
			return title.heroClass.equals(this.heroClass) && title.heroAspect.equals(this.heroAspect);
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(heroClass, heroAspect);
	}
	
	private static String makeNBTPrefix(String prefix)
	{
		return prefix != null && !prefix.isEmpty() ? prefix + "_" : "";
	}
	
	public static Title read(FriendlyByteBuf buffer)
	{
		EnumClass c = EnumClass.getClassFromInt(buffer.readByte());
		EnumAspect a = EnumAspect.getAspectFromInt(buffer.readByte());
		return new Title(c, a);
	}
	
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeByte(EnumClass.getIntFromClass(heroClass));
		buffer.writeByte(EnumAspect.getIntFromAspect(heroAspect));
	}
	
	public static Title read(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix+"class"));
		EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix+"aspect"));
		return new Title(c, a);
	}
	
	public static Title tryRead(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		if(nbt.contains(keyPrefix+"class", Tag.TAG_ANY_NUMERIC))
		{
			EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix+"class"));
			EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix+"aspect"));
			if(c != null && a != null)
				return new Title(c, a);
		}
		return null;
	}
	
	public CompoundTag write(CompoundTag nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		nbt.putByte(keyPrefix+"class", (byte) EnumClass.getIntFromClass(heroClass));
		nbt.putByte(keyPrefix+"aspect", (byte) EnumAspect.getIntFromAspect(heroAspect));
		return nbt;
	}
}