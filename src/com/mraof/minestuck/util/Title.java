package com.mraof.minestuck.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import java.util.Objects;

public final class Title
{
	public static final String FORMAT = "title.format";
	
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
	
	@Override
	public String toString()
	{
		return heroClass.toString() + " of " + heroAspect.toString();
	}
	
	public ITextComponent asTextComponent()
	{
		return new TranslationTextComponent(FORMAT, heroClass.asTextComponent(), heroAspect.asTextComponent());
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Title)
		{
			Title title = (Title) obj;
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
	
	public static Title read(PacketBuffer buffer)
	{
		EnumClass c = EnumClass.getClassFromInt(buffer.readByte());
		EnumAspect a = EnumAspect.getAspectFromInt(buffer.readByte());
		return new Title(c, a);
	}
	
	public void write(PacketBuffer buffer)
	{
		buffer.writeByte(EnumClass.getIntFromClass(heroClass));
		buffer.writeByte(EnumAspect.getIntFromAspect(heroAspect));
	}
	
	public static Title read(CompoundNBT nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix+"class"));
		EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix+"aspect"));
		return new Title(c, a);
	}
	
	public static Title tryRead(CompoundNBT nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		if(nbt.contains(keyPrefix+"class", Constants.NBT.TAG_ANY_NUMERIC))
		{
			EnumClass c = EnumClass.getClassFromInt(nbt.getByte(keyPrefix+"class"));
			EnumAspect a = EnumAspect.getAspectFromInt(nbt.getByte(keyPrefix+"aspect"));
			if(c != null && a != null)
				return new Title(c, a);
		}
		return null;
	}
	
	public CompoundNBT write(CompoundNBT nbt, String keyPrefix)
	{
		keyPrefix = makeNBTPrefix(keyPrefix);
		nbt.putByte(keyPrefix+"class", (byte) EnumClass.getIntFromClass(heroClass));
		nbt.putByte(keyPrefix+"aspect", (byte) EnumAspect.getIntFromAspect(heroAspect));
		return nbt;
	}
}