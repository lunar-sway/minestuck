package com.mraof.minestuck.network.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;

import java.util.Objects;

public record AreaEffectSettingsPacket(MobEffect effect, int effectAmp, boolean isAllMobs, BlockPos minEffectPos, BlockPos maxEffectPos, BlockPos beBlockPos) implements MSPacket.PlayToServer
{
	public static final ResourceLocation ID = Minestuck.id("area_effect_settings");
	
	@Override
	public ResourceLocation id()
	{
		return ID;
	}
	
	@Override
	public void write(FriendlyByteBuf buffer)
	{
		buffer.writeId(BuiltInRegistries.MOB_EFFECT, effect);
		buffer.writeInt(effectAmp);
		buffer.writeBoolean(isAllMobs);
		buffer.writeBlockPos(minEffectPos);
		buffer.writeBlockPos(maxEffectPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static AreaEffectSettingsPacket read(FriendlyByteBuf buffer)
	{
		MobEffect effect = Objects.requireNonNullElse(buffer.readById(BuiltInRegistries.MOB_EFFECT), MSEffects.CREATIVE_SHOCK.get());
		
		int effectAmp = buffer.readInt();
		boolean isAllMobs = buffer.readBoolean();
		
		BlockPos minEffectPos = buffer.readBlockPos();
		BlockPos maxEffectPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new AreaEffectSettingsPacket(effect, effectAmp, isAllMobs, minEffectPos, maxEffectPos, beBlockPos);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		if(!AreaEffectBlock.canInteract(player))
			return;
		
		MSPacket.getAccessibleBlockEntity(player, this.beBlockPos, AreaEffectBlockEntity.class)
				.ifPresent(areaEffect -> areaEffect.handleSettingsPacket(this));
	}
}
