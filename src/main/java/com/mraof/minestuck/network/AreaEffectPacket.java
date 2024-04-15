package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.blockentity.redstone.AreaEffectBlockEntity;
import com.mraof.minestuck.effects.MSEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class AreaEffectPacket implements MSPacket.PlayToServer
{
	private final MobEffect effect;
	private final int effectAmp;
	private final boolean isAllMobs;
	private final BlockPos minEffectPos;
	private final BlockPos maxEffectPos;
	private final BlockPos beBlockPos;
	
	public AreaEffectPacket(MobEffect effect, int effectAmp, boolean isAllMobs, BlockPos minPos, BlockPos maxPos, BlockPos beBlockPos)
	{
		this.effect = effect;
		this.effectAmp = effectAmp;
		this.isAllMobs = isAllMobs;
		this.minEffectPos = minPos;
		this.maxEffectPos = maxPos;
		this.beBlockPos = beBlockPos;
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeId(BuiltInRegistries.MOB_EFFECT, effect);
		buffer.writeInt(effectAmp);
		buffer.writeBoolean(isAllMobs);
		buffer.writeBlockPos(minEffectPos);
		buffer.writeBlockPos(maxEffectPos);
		buffer.writeBlockPos(beBlockPos);
	}
	
	public static AreaEffectPacket decode(FriendlyByteBuf buffer)
	{
		MobEffect effect = Objects.requireNonNullElse(buffer.readById(BuiltInRegistries.MOB_EFFECT), MSEffects.CREATIVE_SHOCK.get());
		
		int effectAmp = buffer.readInt();
		boolean isAllMobs = buffer.readBoolean();
		
		BlockPos minEffectPos = buffer.readBlockPos();
		BlockPos maxEffectPos = buffer.readBlockPos();
		BlockPos beBlockPos = buffer.readBlockPos();
		
		return new AreaEffectPacket(effect, effectAmp, isAllMobs, minEffectPos, maxEffectPos, beBlockPos);
	}
	
	@SuppressWarnings("resource")
	@Override
	public void execute(ServerPlayer player)
	{
		if(player.level().isAreaLoaded(beBlockPos, 0))
		{
			if(player.level().getBlockEntity(beBlockPos) instanceof AreaEffectBlockEntity areaEffect)
			{
				if(Math.sqrt(player.distanceToSqr(beBlockPos.getX() + 0.5, beBlockPos.getY() + 0.5, beBlockPos.getZ() + 0.5)) <= 8)
				{
					areaEffect.setMinAndMaxEffectPosOffset(minEffectPos, maxEffectPos);
					areaEffect.setEffect(effect, effectAmp);
					//Imitates the structure block to ensure that changes are sent client-side
					areaEffect.setChanged();
					player.level().setBlock(beBlockPos, areaEffect.getBlockState().setValue(AreaEffectBlock.ALL_MOBS, isAllMobs), Block.UPDATE_ALL);
					BlockState state = player.level().getBlockState(beBlockPos);
					player.level().sendBlockUpdated(beBlockPos, state, state, 3);
				}
			}
		}
	}
}