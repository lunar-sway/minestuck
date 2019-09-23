package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.CruxiteDowelBlock;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.tileentity.ItemStackTileEntity;
import com.mraof.minestuck.item.crafting.alchemy.AlchemyRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

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
		if (stack.hasTag() && stack.getTag().contains("contentID"))
		{
			CompoundNBT nbttagcompound = stack.getTag();
			StringNBT contentID = (StringNBT) nbttagcompound.get("contentID");
			
			if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(contentID.getString())))
			{
				tooltip.add(new StringTextComponent("(").appendSibling(AlchemyRecipes.getDecodedItem(stack).getDisplayName()).appendText(")"));
			}
			else
			{
				tooltip.add(new StringTextComponent("(").appendSibling(new StringTextComponent("item.captchaCard.invalid")).appendText(")"));
			}
		}
	}
	
	@Nullable
	@Override
	protected BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockState state = super.getStateForPlacement(context);
		ItemStack stack = context.getItem();
		if(stack.hasTag() && stack.getTag().contains("contentID"))
			state = state.with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.TOTEM);
		else
			state = state.with(CruxiteDowelBlock.DOWEL_TYPE, CruxiteDowelBlock.Type.DOWEL);
		return state;
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state)
	{
			ItemStackTileEntity te = (ItemStackTileEntity) world.getTileEntity(pos);
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			te.setStack(newStack);
			return true;
	}
}