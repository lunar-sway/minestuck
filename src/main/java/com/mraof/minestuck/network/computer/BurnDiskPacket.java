package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.computer.ProgramData;
import com.mraof.minestuck.network.PlayToServerPacket;
import com.mraof.minestuck.tileentity.ComputerTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public void encode(PacketBuffer buffer)
	{
		buffer.writeBlockPos(tePos);
		buffer.writeInt(programId);
	}
	
	public static BurnDiskPacket decode(PacketBuffer buffer)
	{
		BlockPos tePos = buffer.readBlockPos();
		int programId = buffer.readInt();
		return new BurnDiskPacket(tePos, programId);
	}
	
	@Override
	public void execute(ServerPlayerEntity player)
	{
		World world = player.level;
		TileEntity tileEntity = world.getBlockEntity(tePos);
		if(tileEntity instanceof ComputerTileEntity)
		{
			ComputerTileEntity computerTileEntity = (ComputerTileEntity) tileEntity;
			
			ItemStack diskStack = ProgramData.getItem(programId);
			if(diskStack != null && computerTileEntity.blankDisksStored > 0)
			{
				Random random = world.getRandom();
				
				float rx = random.nextFloat() * 0.8F + 0.1F;
				float ry = random.nextFloat() * 0.8F + 0.1F;
				float rz = random.nextFloat() * 0.8F + 0.1F;
				
				ItemEntity entityItem = new ItemEntity(world, tePos.getX() + rx, tePos.getY() + ry, tePos.getZ() + rz, diskStack);
				entityItem.setDeltaMovement(random.nextGaussian() * 0.05F, random.nextGaussian() * 0.05F + 0.2F, random.nextGaussian() * 0.05F);
				world.addFreshEntity(entityItem);
				
				computerTileEntity.blankDisksStored--;
				
				//Imitates the structure block to ensure that changes are sent client-side
				tileEntity.setChanged();
				BlockState state = computerTileEntity.getBlockState();
				player.level.sendBlockUpdated(tePos, state, state, 3);
			}
		}
	}
}
