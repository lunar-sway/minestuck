package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DowelItem extends BlockItem
{
	
	public DowelItem(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
		this.addPropertyOverride(CaptchaCardItem.CONTENT_NAME, CaptchaCardItem.CONTENT);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		if(stack.hasTag())
			return 16;
		else return 64;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(AlchemyRecipes.hasDecodedItem(stack))
		{
			ItemStack containedStack = AlchemyRecipes.getDecodedItem(stack);
			
			if(!containedStack.isEmpty())
			{
				tooltip.add(new StringTextComponent("(").appendSibling(containedStack.getDisplayName()).appendText(")").setStyle(new Style().setColor(TextFormatting.GRAY)));
			}
			else
			{
				tooltip.add(new StringTextComponent("(").appendSibling(new StringTextComponent("item.captchaCard.invalid")).appendText(")").setStyle(new Style().setColor(TextFormatting.GRAY)));//TODO translation key
			}
		}
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = super.getStateForPlacement(context);
		if(state == null)
			return null;
		
		ItemStack stack = context.getItem();
		if(AlchemyRecipes.hasDecodedItem(stack))
			state = state.with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.TOTEM);
		else
			state = state.with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.DOWEL);
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
}