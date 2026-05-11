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
	
	public static final short MAX_POWER = 256;
	
	private short power;
	private final IUraniumHandler powerHandler = new SimpleUraniumHandler(() -> (int) MAX_POWER, () -> (int) this.power, power -> this.power = (short) ((int) power))
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
		
		power = compound.getShort("power");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider provider)
	{
		super.saveAdditional(compound, provider);
		
		compound.putShort("power", power);
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
	
	public short getPower()
	{
		return (short) power;
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