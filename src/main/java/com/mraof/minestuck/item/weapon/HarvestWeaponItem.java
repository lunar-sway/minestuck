package com.mraof.minestuck.item.weapon;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Supplier;

public class HarvestWeaponItem extends WeaponItem
{
    private final Supplier<Item> itemDropped;
    private final Supplier<Block> harvestedBlock;
    private final int maxBonusItems;
    private final float percentage;
    private final boolean melonOverload;
    
    public HarvestWeaponItem(IItemTier tier, int attackDamageIn, float attackSpeedIn, float efficiency, boolean melonOverload, float percentage, int maxBonusItems, Supplier<Item> itemDropped, Supplier<Block> harvestedBlock, @Nullable MSToolType toolType, Properties builder)
    {
        super(tier, attackDamageIn, attackSpeedIn, efficiency, toolType, builder);
        this.itemDropped = itemDropped;
        this.harvestedBlock = harvestedBlock;
        this.maxBonusItems = maxBonusItems;
        this.percentage = percentage;
        this.melonOverload = melonOverload;
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if(state == harvestedBlock.get().getDefaultState())
        {
            int harvestCounter = 0;
            for(Float i = entityLiving.getRNG().nextFloat(); i <= percentage || harvestCounter >= maxBonusItems; i = entityLiving.getRNG().nextFloat())
            {
                ItemEntity item = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemDropped.get().getDefaultInstance());
                worldIn.addEntity(item);
                
                harvestCounter++;
            }
            
            if(melonOverload && harvestCounter >= 12 && entityLiving instanceof PlayerEntity)
            {
                ITextComponent message = new TranslationTextComponent("item.melonsbane.message");
                message.getStyle().setColor(TextFormatting.GREEN);
                message.getStyle().setBold(true);
                if(!worldIn.isRemote)
                    entityLiving.sendMessage(message);
            }
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
}
