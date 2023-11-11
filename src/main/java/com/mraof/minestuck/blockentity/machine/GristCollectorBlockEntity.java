package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.api.alchemy.GristAmount;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.block.machine.GristCollectorBlock;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.entity.item.GristEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class GristCollectorBlockEntity extends BlockEntity
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private MutableGristSet storedGrist = MutableGristSet.newDefault();
	
	public GristCollectorBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.GRIST_COLLECTOR.get(), pos, state);
	}
	
	public GristSet getStoredGrist()
	{
		return storedGrist.asImmutable();
	}
	
	public void addGristAmount(GristAmount gristAmount)
	{
		storedGrist.add(gristAmount);
		this.setChanged();
	}
	
	/**
	 * Removes all collected grist
	 */
	public void clearStoredGrist()
	{
		storedGrist = MutableGristSet.newDefault();
		this.setChanged();
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		storedGrist = MutableGristSet.CODEC.parse(NbtOps.INSTANCE, compound.get("storedGrist"))
				.resultOrPartial(LOGGER::error).orElseGet(MutableGristSet::newDefault);
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		MutableGristSet.CODEC.encodeStart(NbtOps.INSTANCE, storedGrist)
				.resultOrPartial(LOGGER::error).ifPresent(tag -> compound.put("storedGrist", tag));
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, GristCollectorBlockEntity blockEntity)
	{
		if(!level.isAreaLoaded(pos, 1))
			return;
		
		//runs once every half second, and only if the block is not powered
		if(level.getGameTime() % 10 == 0 && blockEntity != null && !state.getValue(GristCollectorBlock.POWERED))
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