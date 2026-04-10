package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.machine.CruxtruderMultiblock;
import com.mraof.minestuck.blockentity.machine.CruxtruderBlockEntity;
import com.mraof.minestuck.computer.editmode.EditData;
import com.mraof.minestuck.computer.editmode.ServerEditHandler;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerIdentifier;
import com.mraof.minestuck.util.ColorHandler;
import com.mraof.minestuck.util.MSAttachments;
import com.mraof.minestuck.util.MSRotationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CruxtruderItem extends MultiblockItem
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final CruxtruderMultiblock multiblock;
	
	public CruxtruderItem(CruxtruderMultiblock multiblock, Properties properties)
	{
		super(multiblock, properties);
		this.multiblock = multiblock;
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state)
	{
		if(player == null || level.isClientSide)
			return false;
		BlockEntity be = level.getBlockEntity(multiblock.getBEPos(pos, MSRotationUtil.fromDirection(player.getDirection().getOpposite())));
		if(be instanceof CruxtruderBlockEntity cruxtruder)
		{
			EditData editData = ServerEditHandler.getData(player);
			if(editData != null)
			{
				cruxtruder.setColor(ColorHandler.getColorForPlayer(editData.getTarget(), level));
				cruxtruder.setOwner(editData.getTarget());
			} else
			{
				PlayerIdentifier identifier = IdentifierHandler.encode(player);
				cruxtruder.setColor(ColorHandler.getColorForPlayer(identifier, level));
				cruxtruder.setOwner(identifier);
			}
			
			//TODO remove this
			PlayerData.get((ServerPlayer) player).get().setData(MSAttachments.HAS_KERNELSPRITE, false);
			
			return true;
		} else LOGGER.warn("Placed cruxtruder, but can't find block entity. Instead found {}.", be);
		return false;
	}
}