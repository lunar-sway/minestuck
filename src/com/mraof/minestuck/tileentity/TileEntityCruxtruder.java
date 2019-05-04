package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockCruxiteDowel;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;

import com.mraof.minestuck.util.ColorCollector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityCruxtruder extends TileEntity
{
	private int color = -1;
	private boolean broken = false;
	private int material = 0;
	
	public TileEntityCruxtruder()
	{
		super(MinestuckTiles.CRUXTRUDER);
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

	public void onRightClick(EntityPlayer player, boolean top)
	{
		if(!isBroken())
		{
			BlockPos pos = getPos().up();
			IBlockState state = getWorld().getBlockState(pos);
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
					BlockCruxiteDowel.dropDowel(getWorld(), pos);
				} else if(state.isAir(getWorld(), pos))
				{
					if(MinestuckConfig.cruxtruderIntake && material == 0)
					{
						world.playEvent(1001, pos, 0);
					} else
					{
						world.setBlockState(pos, MinestuckBlocks.CRUXITE_DOWEL.getDefaultState().with(BlockCruxiteDowel.DOWEL_TYPE, BlockCruxiteDowel.Type.CRUXTRUDER));
						TileEntity te = world.getTileEntity(pos);
						if(te instanceof TileEntityItemStack)
							ColorCollector.setColor(((TileEntityItemStack) te).getStack(), color + 1);
						if(material > 0)
							material--;
					}
				}
			}
		}
	}
	
	@Override
	public void read(NBTTagCompound compound)
	{
		super.read(compound);
		
		if(compound.hasKey("color"))
			color = compound.getInt("color");
		if(compound.hasKey("broken"))
			broken = compound.getBoolean("broken");
		material = compound.getInt("material");
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound)
	{
		super.write(compound);
		compound.setInt("color", color);
		compound.setBoolean("broken", broken);
		compound.setInt("material", material);
		return compound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return write(new NBTTagCompound());
	}
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}
}