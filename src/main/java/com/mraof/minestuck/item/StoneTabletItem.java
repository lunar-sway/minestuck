package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.block.StoneTabletBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletItem extends Item //stone slab is the same as stone tablet, both are used in different circumstances
{
	public StoneTabletItem(Properties properties)
	{
		super(properties);
		addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, world, holder) -> hasText(stack) ? 1 : 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(hasText(stack))
			tooltip.add(new TranslationTextComponent(getTranslationKey()+".carved").applyTextStyle(TextFormatting.GRAY));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		if(worldIn.isRemote && !playerIn.isSneaking())
		{
			boolean canEdit = playerIn.getHeldItem(handIn == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).isItemEqual(new ItemStack(MSItems.CARVING_TOOL));
			String text = hasText(stack) ? stack.getTag().getString("text") : "";
			MSScreenFactories.displayStoneTabletScreen(playerIn, handIn, text, canEdit);
		}
		
		return ActionResult.resultSuccess(stack);
	}
	
	public static boolean hasText(ItemStack stack)
	{
		CompoundNBT nbt = stack.getTag();
		return (nbt != null && nbt.contains("text") && !nbt.getString("text").isEmpty());
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		World worldIn = context.getWorld();
		PlayerEntity playerIn = context.getPlayer();
		ItemStack itemStackIn = context.getItem();
		Direction facingIn = context.getFace();
		BlockPos posIn = context.getPos().offset(facingIn);
		
		if(!worldIn.isRemote && playerIn != null && playerIn.isSneaking())
		{
			BlockState stateIn = worldIn.getBlockState(posIn);
			
			if(stateIn.getBlock() == Blocks.AIR)
			{
				BlockState stateTablet;
				if(hasText(itemStackIn))
					stateTablet = MSBlocks.STONE_SLAB_BLOCK.getDefaultState().with(StoneTabletBlock.FACING, context.getPlacementHorizontalFacing()).with(MSProperties.CARVED, true);
				else
					stateTablet = MSBlocks.STONE_SLAB_BLOCK.getDefaultState().with(StoneTabletBlock.FACING, context.getPlacementHorizontalFacing());
				worldIn.setBlockState(posIn, stateTablet);
				
				TileEntity tileEntity = worldIn.getTileEntity(posIn);
				if(tileEntity instanceof ItemStackTileEntity)
				{
					ItemStack newStack = itemStackIn.copy();
					newStack.setCount(1);
					((ItemStackTileEntity) tileEntity).setStack(newStack);
				}
				
				itemStackIn.shrink(1);
				worldIn.playSound(null, posIn, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.BLOCKS, 1, 1);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}