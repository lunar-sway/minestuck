package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.MSProperties;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.block.Block;
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
				
				if(hasText(itemStackIn))
					worldIn.setBlockState(posIn, MSBlocks.STONE_SLAB_BLOCK.getStateAtViewpoint(stateIn, worldIn, posIn, context.getHitVec()).with(MSProperties.CARVED, true));
				else
					worldIn.setBlockState(posIn, MSBlocks.STONE_SLAB_BLOCK.getDefaultState());
				
				TileEntity tileEntity = worldIn.getTileEntity(posIn);
				if(tileEntity instanceof ItemStackTileEntity)
				{
					ItemStack newStack = itemStackIn.copy();
					newStack.setCount(1);
					((ItemStackTileEntity) tileEntity).setStack(newStack);
				}
				//world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, 2F);
				
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}

/*public class StoneTabletItem extends BlockItem
{
	
	public StoneTabletItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
		addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, world, holder) -> hasText(stack) ? 1 : 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(hasText(stack))
			tooltip.add(new TranslationTextComponent(getTranslationKey()+".carved").applyTextStyle(TextFormatting.GRAY));
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = super.getStateForPlacement(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItem();
		stack.setTag(stack.getTag());
		return state;
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			((ItemStackTileEntity) te).setStack(newStack);
		}
		return true;
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
}*/