package com.mraof.minestuck.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.renderer.RenderCard;
import com.mraof.minestuck.util.AlchemyRecipeHandler;

public class ItemCaptchaCard extends Item {
	
	public IIcon punchedIcon;
	public IIcon emptyIcon;
	public IIcon fullIcon;
	
	public ItemCaptchaCard() {
		this.maxStackSize = 16;
		this.setCreativeTab(Minestuck.tabMinestuck);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("captchaCard");
	}
	
	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		punchedIcon = par1IconRegister.registerIcon("minestuck:CardPunched");
		emptyIcon = par1IconRegister.registerIcon("minestuck:CardBlank");
		fullIcon = par1IconRegister.registerIcon("minestuck:CardFull");
		RenderCard.cardIcon = par1IconRegister.registerIcon("minestuck:CardBig");
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconIndex(stack);
	}
	
	@Override
	public IIcon getIconIndex(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt != null && nbt.hasKey("contentID")) {
			
			if(nbt.getBoolean("punched") && !(Item.itemRegistry.getObject(nbt.getString("contentID")) == Item.getItemFromBlock(Minestuck.blockStorage)
					&& nbt.getInteger("contentMeta") == 1))
				return punchedIcon;
			else return fullIcon;
		} else return emptyIcon;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List itemList) {
		itemList.add(new ItemStack(this));
		itemList.add(AlchemyRecipeHandler.createCard(new ItemStack(Minestuck.cruxiteArtifact),new ItemStack(Minestuck.cruxiteArtifact), true));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer,
			@SuppressWarnings("rawtypes") List par3List, boolean par4) {
		if (par1ItemStack.hasTagCompound()) {
			NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();
			NBTTagString contentID = (NBTTagString)nbttagcompound.getTag("contentID");
			NBTTagInt contentMeta = (NBTTagInt)nbttagcompound.getTag("contentMeta");
			
			if (contentID != null && contentMeta != null && Item.itemRegistry.containsKey(contentID.func_150285_a_())) {
				//par3List.add("(" + contentID.data + ":" + contentMeta.data + ")");
				par3List.add("(" + (AlchemyRecipeHandler.getDecodedItem(par1ItemStack)).getDisplayName() + ")");
				if(nbttagcompound.getBoolean("punched") && !(Item.itemRegistry.getObject(contentID.func_150285_a_()) == Item.getItemFromBlock(Minestuck.blockStorage)
						&& contentMeta.func_150287_d() == 1))
					par3List.add("("+StatCollector.translateToLocal("item.captchaCard.punched")+")");
				return;
			}
			else {
				par3List.add("("+StatCollector.translateToLocal("item.captchaCard.invalid")+")");
			}
		} else {
			par3List.add("("+StatCollector.translateToLocal("item.captchaCard.empty")+")");
		}
		
	}
	
}
