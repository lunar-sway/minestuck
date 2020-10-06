package com.mraof.minestuck.item;

import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.item.BarbasolBombEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BarbasolBombItem extends Item
{
    public BarbasolBombItem(Properties properties)
    {
        super(properties);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        ItemStack item = playerIn.getHeldItem(handIn);
        
        if(!playerIn.isCreative())
        {
            item.shrink(1);
        }
    
        worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        
        if(!worldIn.isRemote)
        {
            
            BarbasolBombEntity bomb = new BarbasolBombEntity(MSEntityTypes.BARBASOL_BOMB, playerIn, worldIn, playerIn.abilities.allowEdit);
            bomb.setItem(item);
            bomb.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.7F, 1.0F);    //shoot
            worldIn.addEntity(bomb);
        }
        
        playerIn.addStat(Stats.ITEM_USED.get(this));
        return new ActionResult<>(ActionResultType.SUCCESS, item);
    }
}
