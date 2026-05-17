package com.mraof.minestuck.blockentity.machine;

import javax.annotation.Nullable;

import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumCapabilities;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PowerHubBlockEntity extends BlockEntity
{
	public static final String POWER_PROMPT = "block.minestuck.power_hub.power_prompt";
	
	public static final int MAX_POWER = 256;
	
	private int power;
	private final IUraniumHandler powerHandler = new SimpleUraniumHandler(() -> MAX_POWER, () -> this.power, power -> this.power = power)
	{
		public boolean canReceiveUranium()
		{
			return false;
		}
	};
	
	public PowerHubBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.POWER_HUB.get(), pos, state);
	}
	
	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.loadAdditional(compound, provider);
		
		if (compound.contains("power", 2))
			power = compound.getShort("power");
		else
			power = compound.getInt("power");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		
		compound.putInt("power", power);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, PowerHubBlockEntity blockEntity)
	{
		if(level.getGameTime() % 400 == 0)
		{
			blockEntity.increasePower();
		}
		
		if(blockEntity.power > 0)
		{
			if(level.getBlockEntity(pos.above()) instanceof UraniumPowered poweredBlockEntity && !poweredBlockEntity.atMaxFuel())
			{
				poweredBlockEntity.addFuel((short) 1);
				blockEntity.changePower(-1);
			}
			var capability = UraniumCapabilities.BLOCK.getCapability(level, pos.above(), null, null, null);
			if(capability != null && capability.canReceiveUranium() && capability.getUraniumStored() < capability.getMaxUraniumStored())
			{
				int inserted = capability.receiveUranium(blockEntity.getPower(), false);
				blockEntity.changePower(-inserted);
			}
		}
	}
	
	public void sendStatusMessage(Player player)
	{
		player.displayClientMessage(Component.translatable(POWER_PROMPT, getPower()), true);
	}
	
	public void increasePower()
	{
		if(power < MAX_POWER)
		{
			changePower(1);
		}
	}
	
	public int getPower()
	{
		return power;
	}
	
	private void changePower(int amount)
	{
		power += amount;
		
		this.setChanged();
	}
	
	@Nullable
	public IUraniumHandler getUraniumHandler(@Nullable Direction side)
	{
		return powerHandler;
	}
}