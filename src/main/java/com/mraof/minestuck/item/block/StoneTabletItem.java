package com.mraof.minestuck.item.block;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.block.StoneTabletBlock;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletItem extends BlockItem //stone slab is the same as stone tablet, both are used in different circumstances
{
	public StoneTabletItem(Block blockIn, Properties properties)
	{
		super(blockIn, properties);
		addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "carved"), (stack, world, holder) -> hasText(stack) ? 1 : 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		if(hasText(stack))
			tooltip.add(new TranslationTextComponent(getTranslationKey() + ".carved").applyTextStyle(TextFormatting.GRAY));
	}
	
	public static boolean hasText(ItemStack stack)
	{
		CompoundNBT nbt = stack.getTag();
		return (nbt != null && nbt.contains("text") && !nbt.getString("text").isEmpty());
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = super.getStateForPlacement(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItem();
		
		state = state.with(StoneTabletBlock.FACING, context.getPlacementHorizontalFacing()).with(StoneTabletBlock.CARVED, hasText(stack));
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
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		
		if(!playerIn.isSneaking())
		{
			//Display the stone tablet screen
			if(worldIn.isRemote)
			{
				boolean canEdit = playerIn.getHeldItem(handIn == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).getItem() == MSItems.CARVING_TOOL;
				String text = hasText(itemStackIn) ? itemStackIn.getTag().getString("text") : "";
				MSScreenFactories.displayStoneTabletScreen(playerIn, handIn, text, canEdit);
			}
			return ActionResult.resultSuccess(itemStackIn);
		} else
		{
			return super.onItemRightClick(worldIn, playerIn, handIn);
		}
	}
	
	@Override
	public ActionResultType tryPlace(BlockItemUseContext context)
	{
		PlayerEntity playerIn = context.getPlayer();
		if(playerIn == null || playerIn.isSneaking())
			return super.tryPlace(context);
		return ActionResultType.PASS;
	}
}