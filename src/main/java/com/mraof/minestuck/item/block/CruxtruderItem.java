package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.machine.CruxtruderMultiblock;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.tileentity.machine.CruxtruderTileEntity;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.Debug;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

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
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		if(player == null)
			return false;
		BlockEntity te = level.getBlockEntity(multiblock.getTilePos(pos, MSRotationUtil.fromDirection(player.getDirection().getOpposite())));
		if(te instanceof CruxtruderTileEntity)
		{
			int color;
			EditData editData = ServerEditHandler.getData(player);
			if(editData != null)
				color = ColorHandler.getColorForPlayer(editData.getConnection().getClientIdentifier(), level);
			else color =  ColorHandler.getColorForPlayer((ServerPlayer) player);
			
			((CruxtruderTileEntity) te).setColor(color);
			return true;
		} else Debug.warnf("Placed cruxtruder, but can't find tile entity. Instead found %s.", te);
		return false;
	}
}