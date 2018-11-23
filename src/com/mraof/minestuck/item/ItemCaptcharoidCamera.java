package com.mraof.minestuck.item;

import com.mraof.minestuck.alchemy.AlchemyRecipes;
import com.mraof.minestuck.block.BlockLargeMachine;
import com.mraof.minestuck.item.MinestuckItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCaptcharoidCamera extends Item {

	public ItemCaptcharoidCamera()
	{
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(64);
		
		this.setUnlocalizedName("captcharoidCamera");
		//this.setRegistryName("captcharoid_camera");
	}

	@Override
	protected boolean isInCreativeTab(CreativeTabs targetTab)
	{
		return targetTab == CreativeTabs.SEARCH || targetTab == MinestuckItems.tabMinestuck;
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {MinestuckItems.tabMinestuck};
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(!worldIn.isRemote) {
			ItemStack block = new ItemStack(Item.getItemFromBlock(worldIn.getBlockState(pos).getBlock()));
			
			if(worldIn.getBlockState(pos).getBlock() instanceof BlockLargeMachine)
				block = new ItemStack(((BlockLargeMachine) worldIn.getBlockState(pos).getBlock()).getItemFromMachine());
			
				player.inventory.addItemStackToInventory(AlchemyRecipes.createGhostCard(block));
				player.getHeldItem(hand).damageItem(1, player);
			return EnumActionResult.PASS;
		}
		
		return EnumActionResult.SUCCESS;
	}
}
