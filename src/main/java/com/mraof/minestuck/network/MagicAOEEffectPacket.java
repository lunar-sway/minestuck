package com.mraof.minestuck.network;

import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class MagicAOEEffectPacket implements PlayToClientPacket
{
	private final MagicEffect.AOEType type;
	private final Vec3 minAOEBound, maxAOEBound;
	
	public MagicAOEEffectPacket(MagicEffect.AOEType type, Vec3 minAOEBound, Vec3 maxAOEBound)
	{
		this.type = type;
		this.minAOEBound = minAOEBound;
		this.maxAOEBound = maxAOEBound;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeInt(type.toInt());
		buffer.writeDouble(minAOEBound.x);
		buffer.writeDouble(minAOEBound.y);
		buffer.writeDouble(minAOEBound.z);
		buffer.writeDouble(maxAOEBound.x);
		buffer.writeDouble(maxAOEBound.y);
		buffer.writeDouble(maxAOEBound.z);
	}
	
	public static MagicAOEEffectPacket decode(FriendlyByteBuf buffer)
	{
		MagicEffect.AOEType type = MagicEffect.AOEType.fromInt(buffer.readInt());
		Vec3 minAOEBound = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		Vec3 maxAOEBound = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
		return new MagicAOEEffectPacket(type, minAOEBound, maxAOEBound);
	}
	
	@Override
	public void execute()
	{
		MagicEffect.AOEParticleEffect(type, Minecraft.getInstance().level, minAOEBound, maxAOEBound);
	}
}