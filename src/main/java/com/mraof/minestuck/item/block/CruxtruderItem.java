package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.machine.CruxtruderMultiblock;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.machine.CruxtruderTileEntity;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSRotationUtil;
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
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		if(player == null)
			return false;
		TileEntity te = world.getBlockEntity(multiblock.getTilePos(pos, MSRotationUtil.fromDirection(player.getDirection().getOpposite())));
		if(te instanceof CruxtruderTileEntity)
		{
			int color;
			EditData editData = ServerEditHandler.getData(player);
			if(editData != null)
				color = ColorHandler.getColorForPlayer(editData.getConnection().getClientIdentifier(), world);
			else color =  ColorHandler.getColorForPlayer((ServerPlayerEntity) player);
			
			((CruxtruderTileEntity) te).setColor(color);
			return true;
		} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
		return false;
	}
}