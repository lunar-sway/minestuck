package com.mraof.minestuck.network;

import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class MagicEffectPacket implements PlayToClientPacket
{
	private final MagicEffect.Type type;
	private final Vec3d pos, lookVec;
	private final int length;
	private final boolean collides;
	
	public MagicEffectPacket(MagicEffect.Type type, Vec3d pos, Vec3d lookVec, int length, boolean collides)
	{
		this.type = type;
		this.pos = pos;
		this.lookVec = lookVec;
		this.length = length;
		this.collides = collides;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
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
	
	public static MagicEffectPacket decode(PacketBuffer buffer)
	{
		MagicEffect.Type type = MagicEffect.Type.fromInt(buffer.readInt());
		Vec3d pos = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vec3d lookVec = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		int length = buffer.readInt();
		boolean collided = buffer.readBoolean();
		return new MagicEffectPacket(type, pos, lookVec, length, collided);
	}
	
	@Override
	public void execute()
	{
		MagicEffect.particleEffect(type, Minecraft.getInstance().world, pos, lookVec, length, collides);
	}
}