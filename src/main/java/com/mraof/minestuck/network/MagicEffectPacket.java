package com.mraof.minestuck.network;

import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;

public class MagicEffectPacket implements PlayToClientPacket
{
	private final MagicEffect.Type type;
	private final Vector3d pos, lookVec;
	private final int length;
	private final boolean collides;
	
	public MagicEffectPacket(MagicEffect.Type type, Vector3d pos, Vector3d lookVec, int length, boolean collides)
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
		Vector3d pos = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vector3d lookVec = new Vector3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
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