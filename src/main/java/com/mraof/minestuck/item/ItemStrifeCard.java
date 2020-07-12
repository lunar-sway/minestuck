package com.mraof.minestuck.item;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MSScreenFactories;
import com.mraof.minestuck.inventory.specibus.StrifePortfolioHandler;
import com.mraof.minestuck.inventory.specibus.StrifeSpecibus;
import com.mraof.minestuck.player.KindAbstratusList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStrifeCard extends Item
{
	
	public ItemStrifeCard(Item.Properties builder)
	{
		super(builder);
		addPropertyOverride(new ResourceLocation(Minestuck.MOD_ID, "assigned"), (stack, world, entity) -> stack.hasTag() && stack.getTag().contains("abstrata", Constants.NBT.TAG_ANY_NUMERIC) ? 1 : 0);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		if(stack.hasTag())
		{
			CompoundNBT nbt = stack.getTag();
			
			if(nbt.contains("abstrata", Constants.NBT.TAG_ANY_NUMERIC))
			{
				ITextComponent kind = KindAbstratusList.getTypeList().get(nbt.getInt("abstrata")).getDisplayName();
				tooltip.add(new StringTextComponent("(").appendSibling(kind).appendText(")"));
				if(nbt.contains("items", Constants.NBT.TAG_COMPOUND))
				{
					CompoundNBT items = nbt.getCompound("items");
					int size = items.size();
					int remaining = size;
					for(int i = 0; i < Math.min(size,5); i++)
						if(items.contains("slot"+i, Constants.NBT.TAG_COMPOUND))
						{
							ItemStack s = ItemStack.read(items.getCompound("slot"+i));
							tooltip.add(s.getDisplayName());
							remaining--;
						}
						else break;
					if(remaining > 0)tooltip.add(new TranslationTextComponent("container.shulkerBox.more", remaining).applyTextStyle(TextFormatting.ITALIC));
				}
				
			}
			else
			{
				tooltip.add(new StringTextComponent("(").appendSibling(new TranslationTextComponent("item.strifeCard.invalid")).appendText(")"));
			}
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		
		if(!worldIn.isRemote && !StrifePortfolioHandler.checkSpecibusLimit((ServerPlayerEntity) playerIn))
			{
				playerIn.sendStatusMessage(new TranslationTextComponent("specibus.full"), false);
				return new ActionResult<>(ActionResultType.FAIL, stack);
			}
		
		if(!stack.hasTag())
		{
			BlockPos pos = playerIn.getPosition();
			MSScreenFactories.displayStrifeCardScreen();
		}
		else if(worldIn.isRemote)
		{
			CompoundNBT nbt = stack.getTag();
			
			if(nbt.contains("abstrata", Constants.NBT.TAG_ANY_NUMERIC))
			{
				StrifeSpecibus specibus = new StrifeSpecibus(nbt.getInt("abstrata"));
				if(nbt.contains("items", Constants.NBT.TAG_COMPOUND))
				{
					CompoundNBT items = nbt.getCompound("items");
					for(int i = 0; i < items.size(); i++)
						if(items.contains("slot"+i, Constants.NBT.TAG_COMPOUND))
							specibus.putItemStack(ItemStack.read(items.getCompound("slot"+i)));
						else break;
				}
				
				StrifePortfolioHandler.addSpecibus(playerIn, specibus);
				stack.shrink(1);
			}
			else
			{
				playerIn.sendStatusMessage(new TranslationTextComponent("specibus.corrupted"), false);
				return new ActionResult<>(ActionResultType.FAIL, stack);
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}
}