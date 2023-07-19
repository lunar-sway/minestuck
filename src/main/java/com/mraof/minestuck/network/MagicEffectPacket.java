package com.mraof.minestuck.network;

import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class MagicEffectPacket implements PlayToClientPacket
{
	private final MagicEffect.Type type;
	private final Vec3 pos, lookVec;
	private final int length;
	private final boolean collides;
	
	public MagicEffectPacket(MagicEffect.Type type, Vec3 pos, Vec3 lookVec, int length, boolean collides)
	{
		this.type = type;
		this.pos = pos;
		this.lookVec = lookVec;
		this.length = length;
		this.collides = collides;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
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
	
	public static MagicEffectPacket decode(FriendlyByteBuf buffer)
	{
		MagicEffect.Type type = MagicEffect.Type.fromInt(buffer.readInt());
		Vec3 pos = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vec3 lookVec = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		int length = buffer.readInt();
		boolean collided = buffer.readBoolean();
		return new MagicEffectPacket(type, pos, lookVec, length, collided);
	}
	
	@Override
	public void execute()
	{
		MagicEffect.particleEffect(type, Minecraft.getInstance().level, pos, lookVec, length, collides);
	}
}