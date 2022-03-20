package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.redstone.ItemMagnetBlock;
import com.mraof.minestuck.entity.item.GristEntity;
import com.mraof.minestuck.entity.item.VitalityGelEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

public class ItemMagnetTileEntity extends TileEntity implements ITickableTileEntity
{
	private int gatherLength;
	
	public ItemMagnetTileEntity()
	{
		super(MSTileEntityTypes.ITEM_MAGNET.get());
	}
	
	@Override
	public void tick()
	{
		if(level == null || !level.isAreaLoaded(getBlockPos(), 1))
			return;
		
		sendUpdate();
	}
	
	private void sendUpdate()
	{
		if(level != null) //not sided
		{
			int powerIn = getBlockState().getValue(ItemMagnetBlock.POWER);
			gatherLength = powerIn;
			
			if(powerIn > 0)
			{
				Direction blockFacing = level.getBlockState(getBlockPos()).getValue(ItemMagnetBlock.FACING);
				BlockPos offsetPosClose = getBlockPos().relative(blockFacing);
				BlockPos offsetPosFar = offsetPosClose.relative(blockFacing, gatherLength);
				
				AxisAlignedBB axisalignedbb = new AxisAlignedBB(
						offsetPosClose.getX() + 0.5, offsetPosClose.getY() + 0.5, offsetPosClose.getZ() + 0.5,
						offsetPosFar.getX() + 0.5, offsetPosFar.getY() + 0.5, offsetPosFar.getZ() + 0.5).inflate(0.5);
				List<Entity> list = level.getLoadedEntitiesOfClass(Entity.class, axisalignedbb);
				if(!list.isEmpty())
				{
					for(Entity itemEntity : list)
					{
						if(itemEntity instanceof GristEntity || itemEntity instanceof VitalityGelEntity || itemEntity instanceof ItemEntity || itemEntity instanceof ExperienceOrbEntity)
						{
							Vector3d motionVec3d = new Vector3d(blockFacing.getOpposite().step());
							motionVec3d.add(itemEntity.getDeltaMovement());
							itemEntity.setDeltaMovement(motionVec3d.scale(0.2));
						}
					}
				}
				
				//particles to give the illusion that small bits of material are being pulled towards the magnet
				if(level.random.nextInt(6) == 0)
				{
					BlockPos randomPosInAABB = offsetPosFar.relative(blockFacing.getOpposite(), level.random.nextInt(Math.abs(offsetPosFar.compareTo(offsetPosClose))));
					level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, blockFacing.getOpposite().getStepX(), blockFacing.getOpposite().getStepY(), blockFacing.getOpposite().getStepZ());
				}
				if(level.random.nextInt(3) == 0)
				{
					BlockPos randomPosInAABB = offsetPosFar.relative(blockFacing.getOpposite(), level.random.nextInt(Math.abs(offsetPosFar.compareTo(offsetPosClose))));
					level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, blockFacing.getOpposite().getStepX(), blockFacing.getOpposite().getStepY(), blockFacing.getOpposite().getStepZ());
				}
			}
		}
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		gatherLength = nbt.getInt("gatherLength");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compoundNBT)
	{
		super.save(compoundNBT);
		
		compoundNBT.putInt("gatherLength", gatherLength);
		
		return compoundNBT;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return this.save(new CompoundNBT());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(getBlockPos(), 2, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		this.load(getBlockState(), pkt.getTag());
	}
	
}