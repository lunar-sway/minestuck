package com.mraof.minestuck.network;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.util.MagicEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record MagicRangedEffectPacket(MagicEffect.RangedType type, Vec3 pos, Vec3 lookVec, int length, boolean collides) implements MSPacket.PlayToClient
{
	public static final ResourceLocation ID = Minestuck.id("magic_ranged_effect");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeInt(type.toInt());
		buffer.writeVec3(pos);
		buffer.writeVec3(lookVec);
		buffer.writeInt(length);
		buffer.writeBoolean(collides);
	}
	
	public static MagicRangedEffectPacket read(FriendlyByteBuf buffer)
	{
		MagicEffect.RangedType type = MagicEffect.RangedType.fromInt(buffer.readInt());
		Vec3 pos = buffer.readVec3();
		Vec3 lookVec = buffer.readVec3();
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