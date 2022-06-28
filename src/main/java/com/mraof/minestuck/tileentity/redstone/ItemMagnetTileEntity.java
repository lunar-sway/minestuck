package com.mraof.minestuck.tileentity.redstone;

import com.mraof.minestuck.block.PushableBlock;
import com.mraof.minestuck.block.redstone.ItemMagnetBlock;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
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
import net.minecraft.world.World;

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
				Direction magnetFacing = getBlockState().getValue(ItemMagnetBlock.FACING);
				boolean reversePolarity = getBlockState().getValue(ItemMagnetBlock.REVERSE_POLARITY);
				//boolean alreadyMovingPortableBlock = false;
				
				//TODO figure out if alreadyMovingPortableBlock is worth trying, if trying that then put this section below the entity one
				if(!level.isClientSide && level.getGameTime() % 5 == 0 &&
						((magnetFacing == Direction.DOWN && !reversePolarity) || (magnetFacing == Direction.UP && reversePolarity))/* &&
						!alreadyMovingPortableBlock*/) //will only try turning a portable block into a falling entity if its getting moved upwards and if there is not already a portable block being moved, only does so every quarter second
				{
					for(int blockIterate = 1; blockIterate < powerIn + 1; blockIterate++)
					{
						BlockPos iteratePos = new BlockPos(getBlockPos().relative(magnetFacing, blockIterate));
						if(!level.isAreaLoaded(getBlockPos(), blockIterate) || World.isOutsideBuildHeight(iteratePos.getY())) //checks for portable blocks to grab up until the world bounds
						{
							break;
						}
						
						BlockState iterateBlockState = level.getBlockState(iteratePos);
						
						if(iterateBlockState.getBlock() instanceof PushableBlock)
						{
							FallingBlockEntity fallingblockentity = new FallingBlockEntity(level, iteratePos.getX() + 0.5D, iteratePos.getY(), iteratePos.getZ() + 0.5D, iterateBlockState);
							level.addFreshEntity(fallingblockentity);
							fallingblockentity.time = Integer.MIN_VALUE; //puzzles ideally should not be holding the block indefinitely but this prevents the falling block entity from converting to an item for as long as that is the case
							level.removeBlock(iteratePos, false);
						}
					}
				}
				
				BlockPos offsetPosClose = getBlockPos().relative(magnetFacing);
				BlockPos offsetPosFar = offsetPosClose.relative(magnetFacing, gatherLength);
				
				AxisAlignedBB axisalignedbb = new AxisAlignedBB(
						offsetPosClose.getX() + 0.5, offsetPosClose.getY() + 0.5, offsetPosClose.getZ() + 0.5,
						offsetPosFar.getX() + 0.5, offsetPosFar.getY() + 0.5, offsetPosFar.getZ() + 0.5).inflate(0.5);
				List<Entity> list = level.getLoadedEntitiesOfClass(Entity.class, axisalignedbb);
				if(!list.isEmpty())
				{
					for(Entity itemEntity : list)
					{
						if(MSTags.EntityTypes.MAGNET_RECEPTIVE.contains(itemEntity.getType()) || (itemEntity instanceof FallingBlockEntity && ((FallingBlockEntity) itemEntity).getBlockState().getBlock() instanceof PushableBlock))
						{
							Direction momentumFromFacing = magnetFacing.getOpposite();
							Vector3d facingVec = new Vector3d(momentumFromFacing.getStepX(), momentumFromFacing.getStepY(), momentumFromFacing.getStepZ());
							if(reversePolarity)
								itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(facingVec).scale(0.2).reverse());
							else
								itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(facingVec).scale(0.2));
						}
					}
				}
				
				//particles to give the illusion that small bits of material are being pulled towards the magnet
				if(level.random.nextInt(6) == 0)
				{
					BlockPos randomPosInAABB = offsetPosFar.relative(magnetFacing.getOpposite(), level.random.nextInt(Math.abs(offsetPosFar.compareTo(offsetPosClose))));
					if(reversePolarity)
						level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getStepX(), magnetFacing.getStepY(), magnetFacing.getStepZ());
					else
						level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getOpposite().getStepX(), magnetFacing.getOpposite().getStepY(), magnetFacing.getOpposite().getStepZ());
				}
				if(level.random.nextInt(3) == 0)
				{
					BlockPos randomPosInAABB = offsetPosFar.relative(magnetFacing.getOpposite(), level.random.nextInt(Math.abs(offsetPosFar.compareTo(offsetPosClose))));
					if(reversePolarity)
						level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getStepX(), magnetFacing.getStepY(), magnetFacing.getStepZ());
					else
						level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getOpposite().getStepX(), magnetFacing.getOpposite().getStepY(), magnetFacing.getOpposite().getStepZ());
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