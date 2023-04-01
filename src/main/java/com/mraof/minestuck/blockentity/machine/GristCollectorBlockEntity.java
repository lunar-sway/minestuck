package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.alchemy.*;
import com.mraof.minestuck.block.machine.GristCollectorBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GristCollectorBlockEntity extends BlockEntity
{
	private GristSet storedGrist = new GristSet();
	
	public GristCollectorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.GRIST_COLLECTOR.get(), pos, state);
	}
	
	public GristSet getStoredGrist()
	{
		return storedGrist;
	}
	
	public void addGristAmount(GristAmount gristAmount)
	{
		storedGrist.addGrist(gristAmount);
	}
	
	/**
	 * Removes all collected grist
	 */
	public void clearStoredGrist()
	{
		storedGrist = new GristSet();
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		storedGrist = GristSet.read(compound.getList("storedGrist", Tag.TAG_COMPOUND));
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.put("storedGrist", storedGrist.write(new ListTag()));
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, GristCollectorBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		//runs once per second, and only if the block is not powered
		if(level.getGameTime() % 20 == 0 && blockEntity != null && !state.getValue(GristCollectorBlock.POWERED))
		{
			AABB aabb = new AABB(pos).inflate(1);
			List<GristEntity> gristList = level.getEntitiesOfClass(GristEntity.class, aabb);
			if(!gristList.isEmpty())
			{
				for(GristEntity iteratedGrist : gristList)
				{
					GristAmount gristAmount = iteratedGrist.getAmount();
					
					if(gristAmount != null && gristAmount.getValue() >= 0)
					{
						blockEntity.addGristAmount(gristAmount);
						iteratedGrist.discard();
					}
				}
				
				level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F, 0.5F * ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.8F));
			}
		}
	}
}