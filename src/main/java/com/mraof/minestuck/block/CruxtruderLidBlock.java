package com.mraof.minestuck.block;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.machine.CruxtruderBlock;
import com.mraof.minestuck.blockentity.machine.CruxtruderBlockEntity;
import com.mraof.minestuck.entity.KernelspriteEntity;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.MSAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CruxtruderLidBlock extends Block
{
	public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 5, 14);
	
	public CruxtruderLidBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public PushReaction getPistonPushReaction(BlockState state)
	{
		return PushReaction.DESTROY;
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		super.onRemove(state, level, pos, newState, movedByPiston);
		
		if(level instanceof ServerLevel serverLevel && MinestuckConfig.SERVER.kernelspriteSpawn.get())
		{
			BlockState cruxState = level.getBlockState(pos.below());
			if(cruxState.getBlock() instanceof CruxtruderBlock cruxtruderBlock &&
					level.getBlockEntity(cruxtruderBlock.getMainPos(cruxState, pos.below())) instanceof CruxtruderBlockEntity cruxtruder)
			{
				PlayerIdentifier playerIdentifier = cruxtruder.getOwner();
				
				if(playerIdentifier == null)
					return;
				
				PlayerData data = PlayerData.get(playerIdentifier, serverLevel.getServer());
				
				if(data.getData(MSAttachments.HAS_KERNELSPRITE))
					return;
				
				KernelspriteEntity kernelsprite = new KernelspriteEntity(MSEntityTypes.KERNELSPRITE.get(), level);
				kernelsprite.setColor(cruxtruder.getColor());
				kernelsprite.setOwner(playerIdentifier);
				kernelsprite.setBoundOrigin(pos);
				kernelsprite.setPos(pos.getCenter());
				kernelsprite.setWanderRadius(false);
				kernelsprite.addDeltaMovement(new Vec3(0D, 0.08D, 0D)); //has a tendency to immediately sink into cruxtruder
				
				level.addFreshEntity(kernelsprite);
				
				serverLevel.sendParticles(ParticleTypes.FLASH, pos.getX(), pos.getY() + 0.5D, pos.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
				serverLevel.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.NEUTRAL, 1.0F, 1.0F);
				
				data.setData(MSAttachments.HAS_KERNELSPRITE, true);
			}
		}
	}
}
