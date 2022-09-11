package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
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
	private final BlockPos tePos;
	private final int programId;
	
	public BurnDiskPacket(BlockPos pos, int programId)
	{
		this.tePos = pos;
		this.programId = programId;
	}
	
	public static BurnDiskPacket create(ComputerTileEntity te, int programId)
	{
		return new BurnDiskPacket(te.getBlockPos(), programId);
	}
	
	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(tePos);
		buffer.writeInt(programId);
	}
	
	public static BurnDiskPacket decode(FriendlyByteBuf buffer)
	{
		BlockPos tePos = buffer.readBlockPos();
		int programId = buffer.readInt();
		return new BurnDiskPacket(tePos, programId);
	}
	
	@Override
	public void execute(ServerPlayer player)
	{
		Level level = player.level;
		BlockEntity tileEntity = level.getBlockEntity(tePos);
		if(tileEntity instanceof ComputerTileEntity computerTileEntity)
		{
			ItemStack diskStack = ProgramData.getItem(programId);
			if(diskStack != null && computerTileEntity.blankDisksStored > 0)
			{
				Random random = level.getRandom();
				
				float rx = random.nextFloat() * 0.8F + 0.1F;
				float ry = random.nextFloat() * 0.8F + 0.1F;
				float rz = random.nextFloat() * 0.8F + 0.1F;
				
				ItemEntity entityItem = new ItemEntity(level, tePos.getX() + rx, tePos.getY() + ry, tePos.getZ() + rz, diskStack);
				entityItem.setDeltaMovement(random.nextGaussian() * 0.05F, random.nextGaussian() * 0.05F + 0.2F, random.nextGaussian() * 0.05F);
				level.addFreshEntity(entityItem);
				
				computerTileEntity.blankDisksStored--;
				
				//Imitates the structure block to ensure that changes are sent client-side
				tileEntity.setChanged();
				BlockState state = computerTileEntity.getBlockState();
				player.level.sendBlockUpdated(tePos, state, state, 3);
			}
		}
	}
}
