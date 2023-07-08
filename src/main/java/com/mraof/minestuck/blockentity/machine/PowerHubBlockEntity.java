package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import net.minecraft.core.BlockPos;
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
	
	private short power = 0;
	
	public PowerHubBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.POWER_HUB.get(), pos, state);
	}
	
	@Override
	public void load(CompoundTag compound)
	{
		super.load(compound);
		
		power = compound.getShort("power");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		
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
		return power;
	}
	
	private void changePower(int amount)
	{
		this.power += (short) amount;
		
		this.setChanged();
	}
}