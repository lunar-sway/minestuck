package com.mraof.minestuck.network;

import com.mraof.minestuck.block.redstone.AreaEffectBlock;
import com.mraof.minestuck.effects.MSEffects;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

public class AreaEffectPacket implements PlayToServerPacket
{
	private final Effect effect;
	private final int effectAmp;
	private final boolean isAllMobs;
	private final BlockPos minEffectPos;
	private final BlockPos maxEffectPos;
	private final BlockPos tileBlockPos;
	
	public AreaEffectPacket(Effect effect, int effectAmp, boolean isAllMobs, BlockPos minPos, BlockPos maxPos, BlockPos tileBlockPos)
	{
		this.effect = effect;
		this.effectAmp = effectAmp;
		this.isAllMobs = isAllMobs;
		this.minEffectPos = minPos;
		this.maxEffectPos = maxPos;
		this.tileBlockPos = tileBlockPos;
	}
	
	@Override
	public void encode(PacketBuffer buffer)
	{
		buffer.writeInt(Effect.getId(effect));
		buffer.writeInt(effectAmp);
		buffer.writeBoolean(isAllMobs);
		buffer.writeBlockPos(minEffectPos);
		buffer.writeBlockPos(maxEffectPos);
		buffer.writeBlockPos(tileBlockPos);
	}
	
	public static AreaEffectPacket decode(PacketBuffer buffer)
	{
		Effect effect = MSEffects.CREATIVE_SHOCK.get(); //will keep this as its type if effectRead is null
		Effect effectRead = Effect.byId(buffer.readInt());
		if(effectRead != null)
		{
			effect = effectRead;
		}
		
		int effectAmp = buffer.readInt();
		boolean isAllMobs = buffer.readBoolean();
		
		BlockPos minEffectPos = buffer.readBlockPos();
		BlockPos maxEffectPos = buffer.readBlockPos();
		BlockPos tileBlockPos = buffer.readBlockPos();
		
		return new AreaEffectPacket(effect, effectAmp, isAllMobs, minEffectPos, maxEffectPos, tileBlockPos);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		if(player.level.isAreaLoaded(tileBlockPos, 0))
		{
			TileEntity te = player.level.getBlockEntity(tileBlockPos);
			if(te instanceof AreaEffectTileEntity)
			{
				if(Math.sqrt(player.distanceToSqr(tileBlockPos.getX() + 0.5, tileBlockPos.getY() + 0.5, tileBlockPos.getZ() + 0.5)) <= 8)
				{
					((AreaEffectTileEntity) te).setMinAndMaxEffectPosOffset(minEffectPos, maxEffectPos);
					((AreaEffectTileEntity) te).setEffect(effect, effectAmp);
					//Imitates the structure block to ensure that changes are sent client-side
					te.setChanged();
					player.level.setBlock(tileBlockPos, te.getBlockState().setValue(AreaEffectBlock.ALL_MOBS, isAllMobs), Constants.BlockFlags.DEFAULT);
					BlockState state = player.level.getBlockState(tileBlockPos);
					player.level.sendBlockUpdated(tileBlockPos, state, state, 3);
				}
			}
		}
	}
}