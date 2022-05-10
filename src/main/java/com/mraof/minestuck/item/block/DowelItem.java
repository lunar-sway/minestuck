package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.item.AlchemizedColored;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyHelper;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DowelItem extends BlockItem implements AlchemizedColored
{
	
	public DowelItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyHelper.hasDecodedItem(stack))
		{
			ItemStack containedStack = AlchemyHelper.getDecodedItem(stack);
			
			if(!containedStack.isEmpty())
			{
				tooltip.add(new StringTextComponent("(").append(containedStack.getHoverName()).append(")").withStyle(TextFormatting.GRAY));
			}
			else
			{
				tooltip.add(new StringTextComponent("(").append(new TranslationTextComponent(getDescriptionId() + ".invalid")).append(")").withStyle(TextFormatting.GRAY));
			}
		}
	}
	
	@Nullable
	@Override
	protected BlockState getPlacementState(BlockItemUseContext context)
	{
		BlockState state = super.getPlacementState(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItemInHand();
		if(AlchemyHelper.hasDecodedItem(stack))
			state = state.setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.TOTEM);
		else
			state = state.setValue(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.DOWEL);
		return state;
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
		TileEntity te = world.getBlockEntity(pos);
		if(te instanceof ItemStackTileEntity)
		{
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			((ItemStackTileEntity) te).setStack(newStack);
		}
		return true;
	}
}