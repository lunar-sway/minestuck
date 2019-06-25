package com.mraof.minestuck.item.block;

import com.mraof.minestuck.block.BlockCruxiteDowel;
import com.mraof.minestuck.item.ItemCaptchaCard;
import com.mraof.minestuck.tileentity.TileEntityItemStack;
import com.mraof.minestuck.alchemy.AlchemyRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDowel extends ItemBlock
{
	
	public ItemDowel(Block blockIn, Properties builder)
	{
		super(blockIn, builder);
		this.addPropertyOverride(ItemCaptchaCard.CONTENT_NAME, ItemCaptchaCard.CONTENT);
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
		if (stack.hasTag() && stack.getTag().hasKey("contentID"))
		{
			NBTTagCompound nbttagcompound = stack.getTag();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			
			if (ForgeRegistries.ITEMS.containsKey(new ResourceLocation(contentID.getString())))
			{
				tooltip.add(new TextComponentString("(").appendSibling(AlchemyRecipes.getDecodedItem(stack).getDisplayName()).appendText(")"));
			}
			else
			{
				tooltip.add(new TextComponentString("(").appendSibling(new TextComponentTranslation("item.captchaCard.invalid")).appendText(")"));
			}
		}
	}
	
	@Nullable
	@Override
	protected IBlockState getStateForPlacement(BlockItemUseContext context)
	{
		IBlockState state = super.getStateForPlacement(context);
		ItemStack stack = context.getItem();
		if(stack.hasTag() && stack.getTag().hasKey("contentID"))
			state = state.with(BlockCruxiteDowel.DOWEL_TYPE, BlockCruxiteDowel.Type.TOTEM);
		else
			state = state.with(BlockCruxiteDowel.DOWEL_TYPE, BlockCruxiteDowel.Type.DOWEL);
		return state;
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable EntityPlayer player, ItemStack stack, IBlockState state)
	{
			TileEntityItemStack te = (TileEntityItemStack) world.getTileEntity(pos);
			ItemStack newStack = stack.copy();
			newStack.setCount(1);
			te.setStack(newStack);
			return true;
	}
}