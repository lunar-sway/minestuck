package com.mraof.minestuck.item;

import com.mraof.minestuck.client.gui.MSScreenFactories;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class StoneTabletItem extends Item
{
	public StoneTabletItem(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		
		if(hasText(stack))
			tooltip.add(new TranslationTextComponent(getDescriptionId()+".carved").withStyle(TextFormatting.GRAY));
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getItemInHand(handIn);
		if(worldIn.isClientSide)
		{
			boolean canEdit = playerIn.getItemInHand(handIn == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND).sameItem(new ItemStack(MSItems.CARVING_TOOL));
			String text = hasText(stack) ? stack.getTag().getString("text") : "";
			MSScreenFactories.displayStoneTabletScreen(playerIn, handIn, text, canEdit);
		}
		
		return ActionResult.success(stack);
	}
	
	public static boolean hasText(ItemStack stack)
	{
		CompoundNBT nbt = stack.getTag();
		return (nbt != null && nbt.contains("text") && !nbt.getString("text").isEmpty());
	}
}
