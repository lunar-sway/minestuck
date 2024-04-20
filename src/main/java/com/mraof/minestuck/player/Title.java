package com.mraof.minestuck.player;

import com.mojang.serialization.Codec;
import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.util.MSCapabilities;
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

import java.util.Map;
import java.util.Objects;

public final class Title
{
	public static final String FORMAT = "title.format";
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<EnumAspect, MobEffect> ASPECT_EFFECTS = Map.ofEntries(
			Map.entry(EnumAspect.BLOOD, MobEffects.ABSORPTION),
			Map.entry(EnumAspect.BREATH, MobEffects.MOVEMENT_SPEED),
			Map.entry(EnumAspect.DOOM, MobEffects.DAMAGE_RESISTANCE),
			Map.entry(EnumAspect.HEART, MobEffects.ABSORPTION),
			Map.entry(EnumAspect.HOPE, MobEffects.FIRE_RESISTANCE),
			Map.entry(EnumAspect.LIFE, MobEffects.REGENERATION),
			Map.entry(EnumAspect.LIGHT, MobEffects.LUCK),
			Map.entry(EnumAspect.MIND, MobEffects.NIGHT_VISION),
			Map.entry(EnumAspect.RAGE, MobEffects.DAMAGE_BOOST),
			Map.entry(EnumAspect.SPACE, MobEffects.JUMP),
			Map.entry(EnumAspect.TIME, MobEffects.DIG_SPEED),
			Map.entry(EnumAspect.VOID, MobEffects.INVISIBILITY)
	);
	private static final Map<EnumAspect, Float> ASPECT_STRENGTH = Map.ofEntries(
			Map.entry(EnumAspect.BLOOD, 1.0F/14),
			Map.entry(EnumAspect.BREATH, 1.0F/15),
			Map.entry(EnumAspect.DOOM, 1.0F/28),
			Map.entry(EnumAspect.HEART, 1.0F/14),
			Map.entry(EnumAspect.HOPE, 1.0F/18),
			Map.entry(EnumAspect.LIFE, 1.0F/20),
			Map.entry(EnumAspect.LIGHT, 1.0F/10),
			Map.entry(EnumAspect.MIND, 1.0F/12),
			Map.entry(EnumAspect.RAGE, 1.0F/25),
			Map.entry(EnumAspect.SPACE, 1.0F/10),
			Map.entry(EnumAspect.TIME, 1.0F/13),
			Map.entry(EnumAspect.VOID, 1.0F/12)
	);
	
	
	private final EnumClass heroClass;
	private final EnumAspect heroAspect;
	
	public static final Codec<EnumClass> CLASS_CODEC = Codec.STRING.xmap(EnumClass::valueOf, EnumClass::name);
	public static final Codec<EnumAspect> ASPECT_CODEC = Codec.STRING.xmap(EnumAspect::valueOf, EnumAspect::name);

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
		if(!MinestuckConfig.SERVER.aspectEffects.get() || !player.getData(MSCapabilities.EFFECT_TOGGLE))
			return;
		if(player.getCommandSenderWorld().getGameTime() % 380 != 0)
			return;
		PlayerData data = PlayerSavedData.getData(player);
		if(data == null)
			return;
		
		int rung = data.getEcheladder().getRung();
		EnumAspect aspect = this.getHeroAspect();
		int potionLevel = (int) (ASPECT_STRENGTH.get(aspect) * rung); //Blood, Breath, Doom, Heart, Hope, Life, Light, Mind, Rage, Space, Time, Void
		
		if(rung > 18 && aspect == EnumAspect.HOPE)
			player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600, 0));
		
		if(potionLevel > 0)
		{
			player.addEffect(new MobEffectInstance(ASPECT_EFFECTS.get(aspect), 600, potionLevel - 1));
			LOGGER.debug("Applied aspect potion effect to {}", player.getDisplayName().getString());
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