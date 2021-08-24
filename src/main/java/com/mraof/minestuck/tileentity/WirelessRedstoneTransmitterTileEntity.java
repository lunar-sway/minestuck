package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.WirelessRedstoneRecieverBlock;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.Teleport;
import com.mraof.minestuck.world.storage.TransportalizerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class WirelessRedstoneTransmitterTileEntity extends TileEntity implements INameable
{
	private BlockPos destBlockPos;
	//private int destX;
	//private int destY;
	//private int destZ;
	
	public WirelessRedstoneTransmitterTileEntity()
	{
		super(MSTileEntityTypes.WIRELESS_REDSTONE_TRANSMITTER.get());
	}
	
	@Override
	public void validate()
	{
		super.validate();
		
	}
	
	@Override
	public void remove()
	{
		super.remove();
		
	}
	
	public BlockPos getDestinationBlockPos()
	{
		return destBlockPos;
	}
	
	public void setDestinationBlockPos(BlockPos destinationPosIn)
	{
		//this.destX = destinationPosIn.getX();
		//this.destY = destinationPosIn.getY();
		//this.destZ = destinationPosIn.getZ();
		this.destBlockPos = destinationPosIn;
		BlockState state = world.getBlockState(pos);
		this.markDirty();
		world.notifyBlockUpdate(pos, state, state, 0);
	}
	
	public void sendUpdateToPosition(ServerWorld worldIn, int powerIn)
	{
		/*for(Direction direction : Direction.values()) {
			worldIn.notifyNeighborsOfStateChange(pos.offset(direction), world.getBlockState(destBlockPos).getBlock());
		}*/
		//BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
		//if(!worldIn.isRemote)
			//worldIn.notifyBlockUpdate(destBlockPos, blockStateIn, blockStateIn.with(WirelessRedstoneRecieverBlock.POWERED, isPowered), 3);
		if(destBlockPos != null && worldIn.isAreaLoaded(destBlockPos, 1))
		{
			Debug.debugf("not null destination of %s and area loaded, powerIn = %s", destBlockPos, powerIn);
			BlockState blockStateIn = worldIn.getBlockState(destBlockPos);
			if(blockStateIn.getBlock() instanceof WirelessRedstoneRecieverBlock)
			{
				worldIn.setBlockState(destBlockPos, blockStateIn.with(WirelessRedstoneRecieverBlock.POWER, powerIn), Constants.BlockFlags.BLOCK_UPDATE);
				//worldIn.notifyBlockUpdate(destBlockPos, blockStateIn, blockStateIn.with(WirelessRedstoneRecieverBlock.POWERED, isPowered), 3);
			}
		}
		
	}
	
	@Override
	public ITextComponent getName()
	{
		return new StringTextComponent("Wireless Redstone Interface");
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return hasCustomName() ? getCustomName() : getName();
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		int destX = compound.getInt("destX");
		int destY = compound.getInt("destY");
		int destZ = compound.getInt("destZ");
		this.destBlockPos = new BlockPos(destX, destY, destZ);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		
		compound.putInt("destX", destBlockPos.getX());
		compound.putInt("destY", destBlockPos.getY());
		compound.putInt("destZ", destBlockPos.getZ());
		
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.write(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 2, this.write(new CompoundNBT()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.read(pkt.getNbtCompound());
	}
	
}