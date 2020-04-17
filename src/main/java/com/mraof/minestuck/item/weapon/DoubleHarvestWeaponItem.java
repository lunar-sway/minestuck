package com.mraof.minestuck.item.weapon;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.MelonBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DoubleHarvestWeaponItem extends WeaponItem
{
    public DoubleHarvestWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if((state.getBlock() instanceof CropsBlock && ((CropsBlock) state.getBlock()).isMaxAge(state)) || state.getBlock() instanceof PumpkinBlock)
        {
            CropsBlock.spawnDrops(state,worldIn,pos);
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
