package com.mraof.minestuck.tileentity;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.block.BlockCruxtiteDowel;
import com.mraof.minestuck.block.BlockCruxtruder;
import com.mraof.minestuck.block.MinestuckBlocks;
import com.mraof.minestuck.item.MinestuckItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCruxtruder extends TileEntity
{
	private int color = -1;
	private boolean broken = false;
	private int material = 0;
	
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
			if(top && MinestuckConfig.cruxtruderIntake && state.getBlock().isReplaceable(getWorld(), pos) && material < 64 && material > -1)
			{
				ItemStack stack = player.getHeldItemMainhand();
				if(stack.getItem() != MinestuckItems.rawCruxite)
					stack = player.getHeldItemOffhand();
				if(stack.getItem() == MinestuckItems.rawCruxite)
				{
					int count = 1;
					if(player.isSneaking())	//Doesn't actually work just yet
						count = Math.min(64 - material, stack.getCount());
					stack.shrink(count);
					material += count;
				}
			} else if(!top)
			{
				if(state.getBlock() == MinestuckBlocks.blockCruxiteDowel)
				{
					BlockCruxtiteDowel.dropDowel(getWorld(), pos);
				} else if(state.getBlock().isReplaceable(getWorld(), pos))
				{
					if(MinestuckConfig.cruxtruderIntake && material == 0)
					{
						world.playEvent(1001, pos, 0);
					} else
					{
						world.setBlockState(pos, MinestuckBlocks.blockCruxiteDowel.getDefaultState().withProperty(BlockCruxtiteDowel.TYPE, BlockCruxtiteDowel.Type.CRUXTRUDER));
						TileEntity te = world.getTileEntity(pos);
						if(te instanceof TileEntityItemStack)
							((TileEntityItemStack) te).getStack().setItemDamage(color + 1);
						if(material > 0)
							material--;
					}
				}
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		
		if(tagCompound.hasKey("color"))
			color = tagCompound.getInteger("color");
		if(tagCompound.hasKey("broken"))
			broken = tagCompound.getBoolean("broken");
		material = tagCompound.getInteger("material");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("color", color);
		tagCompound.setBoolean("broken", broken);
		tagCompound.setInteger("material", material);
		return tagCompound;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
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
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock() || oldState.getValue(BlockCruxtruder.PART) != newSate.getValue(BlockCruxtruder.PART);
	}
}
