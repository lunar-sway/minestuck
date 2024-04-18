package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class MagicRangedEffectPacket implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("magic_ranged_effect");
	
	private final MagicEffect.RangedType type;
	private final Vec3 pos, lookVec;
	private final int length;
	private final boolean collides;
	
	public MagicRangedEffectPacket(MagicEffect.RangedType type, Vec3 pos, Vec3 lookVec, int length, boolean collides)
	{
		this.type = type;
		this.pos = pos;
		this.lookVec = lookVec;
		this.length = length;
		this.collides = collides;
	}
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(type.toInt());
		buffer.writeDouble(pos.x);
		buffer.writeDouble(pos.y);
		buffer.writeDouble(pos.z);
		buffer.writeDouble(lookVec.x);
		buffer.writeDouble(lookVec.y);
		buffer.writeDouble(lookVec.z);
		buffer.writeInt(length);
		buffer.writeBoolean(collides);
	}
	
	public static MagicRangedEffectPacket read(FriendlyByteBuf buffer)
	{
		MagicEffect.RangedType type = MagicEffect.RangedType.fromInt(buffer.readInt());
		Vec3 pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vec3 lookVec = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		int length = buffer.readInt();
		boolean collided = buffer.readBoolean();
		return new MagicRangedEffectPacket(type, pos, lookVec, length, collided);
	}
	
	@Override
	public void execute()
	{
		MagicEffect.rangedParticleEffect(type, Minecraft.getInstance().level, pos, lookVec, length, collides);
	}
}