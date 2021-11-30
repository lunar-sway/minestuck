package com.mraof.minestuck.block.redstone;

import com.mraof.minestuck.effects.CreativeShockEffect;
import com.mraof.minestuck.tileentity.redstone.AreaEffectTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AreaEffectBlock extends Block
{
	public AreaEffectBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new AreaEffectTileEntity();
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if(player.isCreative() && player.getHeldItem(hand).getItem() instanceof PotionItem && !CreativeShockEffect.doesCreativeShockLimit(player, 1, 4))
		{
			ItemStack heldItemStack = player.getHeldItem(hand);
			
			EffectInstance firstEffect = PotionUtils.getEffectsFromStack(heldItemStack).get(0);
			if(firstEffect != null)
			{
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity instanceof AreaEffectTileEntity)
				{
					AreaEffectTileEntity te = (AreaEffectTileEntity) tileEntity;
					te.setEffect(firstEffect.getPotion());
					te.setEffectAmplifier(firstEffect.getAmplifier());
					
					worldIn.playSound(null, pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.6F, 0.8F);
				}
			}
			
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}
}