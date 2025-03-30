package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.BarbasolBombEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BarbasolBombItem extends Item implements ProjectileItem
{
	public BarbasolBombItem(Properties properties)
	{
		super(properties);
		DispenserBlock.registerProjectileBehavior(this);
	}
	
	@Override
	public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction)
	{
		BarbasolBombEntity barbasolBomb = new BarbasolBombEntity(MSEntityTypes.BARBASOL_BOMB.get(), pos.x(), pos.y(), pos.z(), level);
		barbasolBomb.setItem(stack);
		return barbasolBomb;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand handIn)
	{
		ItemStack item = playerIn.getItemInHand(handIn);
		
		if(!playerIn.isCreative())
		{
			item.shrink(1);
		}
		
		level.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.TNT_PRIMED, SoundSource.NEUTRAL, 1.0F, 1.0F);
		
		if(!level.isClientSide)
		{
			
			BarbasolBombEntity bomb = new BarbasolBombEntity(MSEntityTypes.BARBASOL_BOMB.get(), playerIn, level, playerIn.getAbilities().mayBuild);
			bomb.setItem(item);
			bomb.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), -20.0F, 0.7F, 1.0F);
			level.addFreshEntity(bomb);
		}
		
		playerIn.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.success(item);
	}
}
