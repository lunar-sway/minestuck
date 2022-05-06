package com.mraof.minestuck.tileentity.machine;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.tileentity.MSTileEntityTypes;
import com.mraof.minestuck.util.ColorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

public class CruxtruderTileEntity extends TileEntity	//TODO check if it is broken
{
	public static final String EMPTY = "block.minestuck.cruxtruder.empty";
	
	private int color = -1;
	private boolean broken = false;
	private int material = 0;
	
	public CruxtruderTileEntity()
	{
		super(MSTileEntityTypes.CRUXTRUDER.get());
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

	public void onRightClick(PlayerEntity player, boolean top)
	{
		if(!isBroken())
		{
			BlockPos pos = getBlockPos().above();
			BlockState state = getLevel().getBlockState(pos);
			if(top && MinestuckConfig.SERVER.cruxtruderIntake.get() && state.isAir(getLevel(), pos) && material < 64 && material > -1)
			{
				ItemStack stack = player.getMainHandItem();
				if(stack.getItem() != MSItems.RAW_CRUXITE)
					stack = player.getOffhandItem();
				if(stack.getItem() == MSItems.RAW_CRUXITE)
				{
					int count = 1;
					if(player.isShiftKeyDown())	//Doesn't actually work just yet
						count = Math.min(64 - material, stack.getCount());
					stack.shrink(count);
					material += count;
				}
			} else if(!top)
			{
				if(state.getBlock() == MSBlocks.CRUXITE_DOWEL)
				{
					CruxiteDowelBlock.dropDowel(getLevel(), pos);
				} else if(state.isAir(getLevel(), pos))
				{
					if(MinestuckConfig.SERVER.cruxtruderIntake.get() && material == 0)
					{
						level.levelEvent(Constants.WorldEvents.DISPENSER_FAIL_SOUND, pos, 0);
						player.sendMessage(new TranslationTextComponent(EMPTY), Util.NIL_UUID);
					} else
					{
						level.setBlockAndUpdate(pos, MSBlocks.CRUXITE_DOWEL.defaultBlockState().setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.CRUXTRUDER));
						TileEntity te = level.getBlockEntity(pos);
						if(te instanceof ItemStackTileEntity)
							ColorHandler.setStackColor(((ItemStackTileEntity) te).getStack(), color);
						if(material > 0)
							material--;
					}
				}
			}
		}
	}
	
	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		
		if(nbt.contains("color"))
			color = nbt.getInt("color");
		if(nbt.contains("broken"))
			broken = nbt.getBoolean("broken");
		material = nbt.getInt("material");
	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound)
	{
		super.save(compound);
		compound.putInt("color", color);
		compound.putBoolean("broken", broken);
		compound.putInt("material", material);
		return compound;
	}
}