package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.multiblock.CruxtruderMultiblock;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.CruxtruderTileEntity;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSRotationUtil;
import com.mraof.minestuck.world.storage.PlayerSavedData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CruxtruderItem extends MultiblockItem
{
	private final CruxtruderMultiblock multiblock;
	
	public CruxtruderItem(CruxtruderMultiblock multiblock, Properties properties)
	{
		super(multiblock, properties);
		this.multiblock = multiblock;
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		if(player == null)
			return false;
		TileEntity te = world.getTileEntity(multiblock.getTilePos(pos, MSRotationUtil.fromDirection(player.getHorizontalFacing().getOpposite())));
		if(te instanceof CruxtruderTileEntity)
		{
			int color;
			EditData editData = ServerEditHandler.getData(player);
			if(editData != null)
				color = PlayerSavedData.getData(editData.getTarget(), world).getColor();
			else color = PlayerSavedData.getData((ServerPlayerEntity) player).getColor();
			
			((CruxtruderTileEntity) te).setColor(color);
			return true;
		} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
		return false;
	}
}