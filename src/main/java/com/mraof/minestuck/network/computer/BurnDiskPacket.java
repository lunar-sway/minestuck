package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.network.PlayToServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BurnDiskPacket implements PlayToServerPacket
{
	private final BlockPos bePos;
	private final int programId;
	
	public BurnDiskPacket(BlockPos pos, int programId)
	{
		this.bePos = pos;
		this.programId = programId;
	}
	
	public static BurnDiskPacket create(ComputerBlockEntity be, int programId)
	{
		return new BurnDiskPacket(be.getBlockPos(), programId);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(bePos);
		buffer.writeInt(programId);
	}
	
	public static BurnDiskPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos bePos = buffer.readBlockPos();
		int programId = buffer.readInt();
		return new BurnDiskPacket(bePos, programId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		Level level = player.level;
		if(level.isAreaLoaded(bePos, 0))    //TODO also check distance to the computer pos (together with a continual check clientside)
		{
			if(level.getBlockEntity(bePos) instanceof ComputerBlockEntity computerBlockEntity)
			{
				ItemStack diskStack = ProgramData.getItem(programId);
				if(diskStack != null && !diskStack.isEmpty() && computerBlockEntity.blankDisksStored > 0)
				{
					Random random = level.getRandom();
					
					float rx = random.nextFloat() * 0.8F + 0.1F;
					float ry = random.nextFloat() * 0.8F + 0.1F;
					float rz = random.nextFloat() * 0.8F + 0.1F;
					
					ItemEntity entityItem = new ItemEntity(level, bePos.getX() + rx, bePos.getY() + ry, bePos.getZ() + rz, diskStack);
					entityItem.setDeltaMovement(random.nextGaussian() * 0.05F, random.nextGaussian() * 0.05F + 0.2F, random.nextGaussian() * 0.05F);
					level.addFreshEntity(entityItem);
					
					computerBlockEntity.blankDisksStored--;
					
					//Imitates the structure block to ensure that changes are sent client-side
					computerBlockEntity.setChanged();
					BlockState state = computerBlockEntity.getBlockState();
					player.level.sendBlockUpdated(bePos, state, state, 3);
				}
			}
		}
	}
}
