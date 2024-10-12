package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.item.PosterEntity;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.components.PosterComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Optional;

public class PosterItem extends Item
{
	public PosterItem(PosterComponent posterComponent, Properties properties)
	{
		super(properties.component(MSItemComponents.POSTER, posterComponent));
	}
	
	@Override
	public InteractionResult useOn(UseOnContext pContext)
	{
		BlockPos blockpos = pContext.getClickedPos();
		Direction direction = pContext.getClickedFace();
		BlockPos blockpos1 = blockpos.relative(direction);
		Player player = pContext.getPlayer();
		ItemStack itemstack = pContext.getItemInHand();
		Level level = pContext.getLevel();
		
		PosterComponent posterComponent = itemstack.get(MSItemComponents.POSTER);
		
		if (posterComponent == null || (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)))
			return InteractionResult.FAIL;
		
		Optional<PosterEntity> optional = PosterEntity.createArt(level, blockpos1, direction, itemstack, posterComponent.variantPool());
		if (optional.isEmpty()) {
			return InteractionResult.CONSUME;
		}
		
		PosterEntity hangingentity = optional.get();
		if (hangingentity.survives()) {
			if (!level.isClientSide) {
				hangingentity.playPlacementSound();
				level.gameEvent(player, GameEvent.ENTITY_PLACE, hangingentity.position());
				level.addFreshEntity(hangingentity);
			}
			
			itemstack.shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.CONSUME;
		}
	}
	
	protected boolean mayPlace(Player pPlayer, Direction pDirection, ItemStack pHangingEntityStack, BlockPos pPos) {
		return !pDirection.getAxis().isVertical() && pPlayer.mayUseItemAt(pPos, pDirection, pHangingEntityStack);
	}
	
}
