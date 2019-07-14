package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;

import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class CruxtruderTileEntity extends TileEntity
{
	private int color = -1;
	private boolean broken = false;
	private int material = 0;
	
	public CruxtruderTileEntity()
	{
		super(ModTileEntityTypes.CRUXTRUDER);
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
			BlockPos pos = getPos().up();
			BlockState state = getWorld().getBlockState(pos);
			if(top && MinestuckConfig.cruxtruderIntake && state.isAir(getWorld(), pos) && material < 64 && material > -1)
			{
				ItemStack stack = player.getHeldItemMainhand();
				if(stack.getItem() != MinestuckItems.RAW_CRUXITE)
					stack = player.getHeldItemOffhand();
				if(stack.getItem() == MinestuckItems.RAW_CRUXITE)
				{
					int count = 1;
					if(player.isSneaking())	//Doesn't actually work just yet
						count = Math.min(64 - material, stack.getCount());
					stack.shrink(count);
					material += count;
				}
			} else if(!top)
			{
				if(state.getBlock() == MinestuckBlocks.CRUXITE_DOWEL)
				{
					CruxiteDowelBlock.dropDowel(getWorld(), pos);
				} else if(state.isAir(getWorld(), pos))
				{
					if(MinestuckConfig.cruxtruderIntake && material == 0)
					{
						world.playEvent(1001, pos, 0);
					} else
					{
						world.setBlockState(pos, MinestuckBlocks.CRUXITE_DOWEL.getDefaultState().with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.CRUXTRUDER));
						TileEntity te = world.getTileEntity(pos);
						if(te instanceof ItemStackTileEntity)
							ColorCollector.setColor(((ItemStackTileEntity) te).getStack(), color + 1);
						if(material > 0)
							material--;
					}
				}
			}
		}
	}
	
	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		
		if(compound.contains("color"))
			color = compound.getInt("color");
		if(compound.contains("broken"))
			broken = compound.getBoolean("broken");
		material = compound.getInt("material");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putInt("color", color);
		compound.putBoolean("broken", broken);
		compound.putInt("material", material);
		return compound;
	}
	
	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
}