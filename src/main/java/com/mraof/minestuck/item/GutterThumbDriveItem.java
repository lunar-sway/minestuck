package com.mraof.minestuck.item;

import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.player.IdentifierHandler;
import com.mraof.minestuck.skaianet.Session;
import com.mraof.minestuck.skaianet.SessionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;


public class GutterThumbDriveItem extends Item
{
	public GutterThumbDriveItem(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		if(pContext.getLevel().isClientSide)
		{
			Player player = pContext.getPlayer();
			int mod = 1;
			Level level = player.getLevel();
			
			for(BlockPos blockPos : BlockPos.betweenClosed(player.blockPosition().offset(2 * mod, mod, 2 * mod), player.blockPosition().offset(-2 * mod, -1 * mod, -2 * mod)))
			{
				if(level.getBlockEntity(blockPos) instanceof ComputerBlockEntity)
				{
					Session playerSession = SessionHandler.get(level).getPlayerSession(IdentifierHandler.encode(player));
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 0.5F, 0.3F);
					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.2F, 0.6F);
					
					playerSession.increaseGutterMultiplier(1.0);
				}
			}
		}
		return super.useOn(pContext);
		
	}

}

