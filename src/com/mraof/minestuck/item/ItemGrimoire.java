package com.mraof.minestuck.item;

import com.mraof.minestuck.util.MinestuckSoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemGrimoire extends Item {
	public ItemGrimoire() {
		this.setCreativeTab(TabMinestuck.instance);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote && playerIn != null) {
			ITextComponent message = new TextComponentTranslation("After flipping through some pages, you feel significantly more insignificant.");
			playerIn.sendMessage(message);
			playerIn.world.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, MinestuckSoundHandler.soundWhispers, SoundCategory.AMBIENT, 0.5F, 0.8F);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
