package com.mraof.minestuck.network.computer;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.DiskBurnerData;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record BurnDiskPacket(BlockPos computerPos, boolean isClientDisk) implements MSPacket.PlayToServer
{
	public static final Type<BurnDiskPacket> ID = new Type<>(Minestuck.id("burn_disk"));
	public static final StreamCodec<FriendlyByteBuf, BurnDiskPacket> STREAM_CODEC = StreamCodec.composite(
			BlockPos.STREAM_CODEC,
			BurnDiskPacket::computerPos,
			ByteBufCodecs.BOOL,
			BurnDiskPacket::isClientDisk,
			BurnDiskPacket::new
	);
	
	public static BurnDiskPacket create(ComputerBlockEntity be, boolean isClientDisk)
	{
		return new BurnDiskPacket(be.getBlockPos(), isClientDisk);
	}
	
	@Override
	public Type<BurnDiskPacket> type()
	{
		return ID;
	}
	
	@Override
	public void execute(IPayloadContext context, ServerPlayer player)
	{
		ComputerBlockEntity.getAccessibleComputer(player, this.computerPos)
				.ifPresent(this::tryBurnDisk);
	}
	
	private void tryBurnDisk(ComputerBlockEntity computer)
	{
		Item disk = this.isClientDisk ? MSItems.CLIENT_DISK.get() : MSItems.SERVER_DISK.get();
		Level level = computer.getLevel();
		BlockPos bePos = computer.getBlockPos();
		if(level == null)
			return;
		
		if(!computer.getProgramData(ProgramTypes.DISK_BURNER).map(DiskBurnerData::hasAllCode).orElse(false))
			return;
		
		if(computer.tryTakeBlankDisk())
		{
			RandomSource random = level.getRandom();
			
			float rx = random.nextFloat() * 0.8F + 0.1F;
			float ry = random.nextFloat() * 0.8F + 0.1F;
			float rz = random.nextFloat() * 0.8F + 0.1F;
			
			ItemEntity entityItem = new ItemEntity(level, bePos.getX() + rx, bePos.getY() + ry, bePos.getZ() + rz, disk.getDefaultInstance());
			entityItem.setDeltaMovement(random.nextGaussian() * 0.05F, random.nextGaussian() * 0.05F + 0.2F, random.nextGaussian() * 0.05F);
			level.addFreshEntity(entityItem);
		}
	}
}
