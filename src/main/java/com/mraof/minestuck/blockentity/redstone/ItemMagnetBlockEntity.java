package com.mraof.minestuck.blockentity.redstone;

import com.mraof.minestuck.block.PushableBlock;
import com.mraof.minestuck.block.redstone.ItemMagnetBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.MSTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ItemMagnetBlockEntity extends BlockEntity
{
	private int gatherLength;
	
	public ItemMagnetBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.ITEM_MAGNET.get(), pos, state);
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, ItemMagnetBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		blockEntity.sendUpdate();
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
				
				//TODO figure out if alreadyMovingPushableBlock(hypothetical boolean for preventing more than one pushable block from getting moved) is worth trying, if trying that then put this section below the entity one
				//will only try turning a pushable block into a falling entity if its getting moved upwards, only does so every quarter second
				if(!level.isClientSide && level.getGameTime() % 5 == 0 && ((magnetFacing == Direction.DOWN && !reversePolarity) || (magnetFacing == Direction.UP && reversePolarity)))
				{
					for(int blockIterate = 1; blockIterate < powerIn + 1; blockIterate++)
					{
						BlockPos iteratePos = new BlockPos(getBlockPos().relative(magnetFacing, blockIterate));
						if(!level.isAreaLoaded(getBlockPos(), blockIterate) || level.isOutsideBuildHeight(iteratePos.getY())) //checks for pushable blocks to grab up until the world bounds
						{
							break;
						}
						
						BlockState iterateBlockState = level.getBlockState(iteratePos);
						
						if(iterateBlockState.getBlock() instanceof PushableBlock)
						{
							FallingBlockEntity fallingblockentity = FallingBlockEntity.fall(level, iteratePos, iterateBlockState);
							fallingblockentity.time = Integer.MIN_VALUE; //puzzles ideally should not be holding the block indefinitely but this prevents the falling block entity from converting to an item for as long as that is the case
						}
					}
				}
				
				BlockPos offsetPosClose = getBlockPos().relative(magnetFacing);
				BlockPos offsetPosFar = offsetPosClose.relative(magnetFacing, gatherLength);
				
				AABB axisalignedbb = new AABB(
						offsetPosClose.getX() + 0.5, offsetPosClose.getY() + 0.5, offsetPosClose.getZ() + 0.5,
						offsetPosFar.getX() + 0.5, offsetPosFar.getY() + 0.5, offsetPosFar.getZ() + 0.5).inflate(0.5);
				List<Entity> list = level.getEntitiesOfClass(Entity.class, axisalignedbb);
				if(!list.isEmpty())
				{
					for(Entity itemEntity : list)
					{
						if(itemEntity.getType().is(MSTags.EntityTypes.MAGNET_RECEPTIVE) || (itemEntity instanceof FallingBlockEntity && ((FallingBlockEntity) itemEntity).getBlockState().getBlock() instanceof PushableBlock))
						{
							Direction momentumFromFacing = magnetFacing.getOpposite();
							Vec3 facingVec = new Vec3(momentumFromFacing.getStepX(), momentumFromFacing.getStepY(), momentumFromFacing.getStepZ());
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
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getStepX(), magnetFacing.getStepY(), magnetFacing.getStepZ());
					else
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getOpposite().getStepX(), magnetFacing.getOpposite().getStepY(), magnetFacing.getOpposite().getStepZ());
				}
				if(level.random.nextInt(3) == 0)
				{
					BlockPos randomPosInAABB = offsetPosFar.relative(magnetFacing.getOpposite(), level.random.nextInt(Math.abs(offsetPosFar.compareTo(offsetPosClose))));
					if(reversePolarity)
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getStepX(), magnetFacing.getStepY(), magnetFacing.getStepZ());
					else
						level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(randomPosInAABB.relative(Direction.getRandom(level.random)))), randomPosInAABB.getX() + 0.5, randomPosInAABB.getY() + 0.9, randomPosInAABB.getZ() + 0.5, magnetFacing.getOpposite().getStepX(), magnetFacing.getOpposite().getStepY(), magnetFacing.getOpposite().getStepZ());
				}
			}
		}
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		gatherLength = nbt.getInt("gatherLength");
	}
	
	@Override
	public void saveAdditional(CompoundTag compoundNBT)
	{
		super.saveAdditional(compoundNBT);
		
		compoundNBT.putInt("gatherLength", gatherLength);
	}
	
	@Override
	public CompoundTag getUpdateTag()
	{
		return this.saveWithoutMetadata();
	}
	
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}
	
}