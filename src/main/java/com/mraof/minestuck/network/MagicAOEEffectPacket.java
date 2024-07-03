package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record MagicAOEEffectPacket(MagicEffect.AOEType type, Vec3 minAOEBound, Vec3 maxAOEBound) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("magic_aoe_effect");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(type.toInt());
		buffer.writeVec3(minAOEBound);
		buffer.writeVec3(maxAOEBound);
	}
	
	public static MagicAOEEffectPacket read(FriendlyByteBuf buffer)
	{
		MagicEffect.AOEType type = MagicEffect.AOEType.fromInt(buffer.readInt());
		Vec3 minAOEBound = buffer.readVec3();
		Vec3 maxAOEBound = buffer.readVec3();
		return new MagicAOEEffectPacket(type, minAOEBound, maxAOEBound);
	}
	
	@Override
	public void execute()
	{
		MagicEffect.AOEParticleEffect(type, Minecraft.getInstance().level, minAOEBound, maxAOEBound);
	}
}