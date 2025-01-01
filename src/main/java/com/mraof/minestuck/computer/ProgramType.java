package com.mraof.minestuck.computer;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ProgramType implements StringRepresentable
{
	CLIENT,
	SERVER,
	DISK_BURNER,
	SETTINGS;
	
	public static final Codec<ProgramType> CODEC = StringRepresentable.fromEnum(ProgramType::values);
	public static final StreamCodec<ByteBuf, ProgramType> STREAM_CODEC = ByteBufCodecs.idMapper(
			ByIdMap.continuous(ProgramType::ordinal, ProgramType.values(), ByIdMap.OutOfBoundsStrategy.ZERO), ProgramType::ordinal);
	
	@Override
	public String getSerializedName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
}
