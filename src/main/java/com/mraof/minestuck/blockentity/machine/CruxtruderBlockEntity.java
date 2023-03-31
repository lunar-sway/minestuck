package com.mraof.minestuck.blockentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.blockentity.ItemStackBlockEntity;
import com.mraof.minestuck.blockentity.MSBlockEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CruxtruderBlockEntity extends BlockEntity
{
	public static final String EMPTY = "block.minestuck.cruxtruder.empty";
	
	private int color = -1;
	private boolean broken = false;
	private int material = 0;
	
	public CruxtruderBlockEntity(BlockPos pos, BlockState state)
	{
		super(MSBlockEntityTypes.CRUXTRUDER.get(), pos, state);
	}
	
	public int getColor()
	{
		return color;
	}
	
	public void setColor(int Color)
	{
		color = Color;
	}
	
	public boolean isBroken()
	{
		return broken;
	}
	public void destroy()
	{
		broken = true;
	}

	public void onRightClick(Player player, boolean top)
	{
		if(!isBroken() && level != null)
		{
			BlockPos pos = getBlockPos().above();
			BlockState state = level.getBlockState(pos);
			if(top && MinestuckConfig.SERVER.cruxtruderIntake.get() && state.isAir() && material < 64 && material > -1)
			{
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() != MSItems.RAW_CRUXITE.get())
					stack = player.getOffhandItem();
				if(stack.getItem() == MSItems.RAW_CRUXITE.get())
				{
					int count = 1;
					if(player.isShiftKeyDown())	//Doesn't actually work just yet
						count = Math.min(64 - material, stack.getCount());
					
					if(!player.isCreative())
						stack.shrink(count);
					
					material += count;
					
					level.playSound(null, pos, MSSoundEvents.CRUXTRUDER_DOWEL.get(), SoundSource.BLOCKS, 1F, 1F);
				}
			} else if(!top)
			{
				if(state.getBlock() == MSBlocks.CRUXITE_DOWEL.get())
				{
					CruxiteDowelBlock.dropDowel(level, pos);
				} else if(state.isAir())
				{
					if(MinestuckConfig.SERVER.cruxtruderIntake.get() && material == 0)
					{
						level.levelEvent(LevelEvent.SOUND_DISPENSER_FAIL, pos, 0);
						player.sendMessage(new TranslatableComponent(EMPTY), Util.NIL_UUID);
					} else
					{
						level.setBlockAndUpdate(pos, MSBlocks.CRUXITE_DOWEL.get().defaultBlockState().setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.CRUXTRUDER));
						level.playSound(null, pos, MSSoundEvents.CRUXTRUDER_DOWEL.get(), SoundSource.BLOCKS, 1F, 1.75F);
						
						if(level.getBlockEntity(pos) instanceof ItemStackBlockEntity blockEntity)
							ColorHandler.setColor(blockEntity.getStack(), color);
						if(material > 0)
							material--;
					}
				}
			}
		}
	}
	
	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		
		if(nbt.contains("color"))
			color = nbt.getInt("color");
		if(nbt.contains("broken"))
			broken = nbt.getBoolean("broken");
		material = nbt.getInt("material");
	}
	
	@Override
	public void saveAdditional(CompoundTag compound)
	{
		super.saveAdditional(compound);
		compound.putInt("color", color);
		compound.putBoolean("broken", broken);
		compound.putInt("material", material);
	}
}