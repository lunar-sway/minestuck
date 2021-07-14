package com.mraof.minestuck.network;

import com.mraof.minestuck.client.util.GunEffect;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;

public class GunEffectPacket implements PlayToClientPacket
{
	private final GunEffect.Type type;
	private final Vec3d pos, lookVec;
	private final int length;
	private final boolean collides;
	
	public GunEffectPacket(GunEffect.Type type, Vec3d pos, Vec3d lookVec, int length, boolean collides)
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
	
	public static GunEffectPacket decode(PacketBuffer buffer)
	{
		GunEffect.Type type = GunEffect.Type.fromInt(buffer.readInt());
		Vec3d pos = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vec3d lookVec = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		int length = buffer.readInt();
		boolean collided = buffer.readBoolean();
		return new GunEffectPacket(type, pos, lookVec, length, collided);
	}
	
	@Override
	public void execute()
	{
		GunEffect.particleEffect(type, Minecraft.getInstance().world, pos, lookVec, length, collides);
	}
}